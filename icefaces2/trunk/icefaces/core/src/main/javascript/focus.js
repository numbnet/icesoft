/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

var setFocus;
var applyFocus;
var currentFocus;

(function() {
    setFocus = function(id) {
        currentFocus = id;
        debug(logger, 'persisted focus for element "' + id + '"');
    };

    function isValidID(id) {
        return /^\w[\w\-\:]*$/.test(id);
    }

    var isIE = /MSIE/.test(navigator.userAgent);

    var focusOn = function(id) {
        runOnce(Delay(function() {
            if (id && isValidID(id)) {
                var e = document.getElementById(id);
                if (e) {
                    setFocus(id);
                    if (e.focus) {
                        try {
                            e.focus();
                        } catch (ex) {
                            //IE throws exception if element is invisible
                        } finally {
                            if (isIE) {
                                //IE sometimes requires that focus() be called again
                                try {
                                    e.focus();
                                } catch (ex2) {
                                    //IE throws exception if element is invisible
                                }
                            }
                            debug(logger, 'focused element "' + id + '"');
                        }
                    }
                }
            }
        }, 100));
    };

    var focusStrategy = focusOn;
    //indirect reference to the currently used focus strategy
    applyFocus = function(id) {
        focusStrategy(id);
    };

    if (isIE) {
        var activeElement;
        //initialize activeElement if IE
        onLoad(window, function() {
            activeElement = document.activeElement;
        });

        //window.onblur in IE is triggered also when moving focus from window to an element inside the same window
        //to avoid bogus 'blur' events in IE the window.onblur behavior is simulated with the help of document.onfocusout
        //event handler
        var onBlur = function(callback) {
            registerElementListener(document, 'onfocusout', function() {
                if (activeElement == document.activeElement) {
                    callback();
                } else {
                    activeElement = document.activeElement;
                }
            });
        };

        var onFocus = function(callback) {
            registerElementListener(window, 'onfocus', callback);
        };

        //on window blur the ID of the focused element is just saved, not applied
        onBlur(function() {
            focusStrategy = setFocus;
        });

        onFocus(function() {
            focusStrategy = focusOn;
        });
    }

    function registerElementListener(element, eventType, listener) {
        var previousListener = element[eventType];
        if (previousListener) {
            element[eventType] = function(e) {
                var args = [e];
                //execute listeners so that 'this' variable points to the current element
                previousListener.apply(element, args);
                listener.apply(element, args);
            };
        } else {
            element[eventType] = listener;
        }
    }

    function setFocusListener(e) {
        var evt = e || window.event;
        var element = evt.srcElement || evt.target;
        setFocus(element.id);
    }

    var focusableElements = ['select', 'input', 'button', 'a'];

    function captureFocusIn(root) {
        if (contains(focusableElements, root.nodeName)) {
            registerElementListener(root, 'onfocus', setFocusListener);
        }
        each(focusableElements, function(type) {
            each(root.getElementsByTagName(type), function(element) {
                registerElementListener(element, 'onfocus', setFocusListener);
            });
        });
    }

    namespace.onAfterUpdate(function(updates) {
        each(updates.getElementsByTagName('update'), function(update) {
            var id = update.getAttribute('id');
            var element = lookupElementById(id);
            if (id != 'javax.faces.ViewState' && element) captureFocusIn(element);
        });
    });

    onLoad(window, function() {
        captureFocusIn(document);
    });
})();
