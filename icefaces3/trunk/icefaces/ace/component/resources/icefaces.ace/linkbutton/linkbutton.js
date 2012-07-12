/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

ice.ace.linkButton = {

    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
        var spanId = clientId + "_span";
        var oLinkButton = new YAHOO.widget.Button(spanId, { label: jsProps.label, tabindex: null }, {type: jsProps.type});

        oLinkButton.addStateCSSClasses = function(state) {

            if (state == 'hover') {
                ice.ace.jq(this._button).addClass('ui-state-hover');
            } else if (state == 'active') {
                ice.ace.jq(this._button).addClass('ui-state-active');
            } else if (state == 'disabled') {
                ice.ace.jq(this._button).addClass('ui-state-disabled ');
            }
        };

        oLinkButton.removeStateCSSClasses = function(state) {

            if (state == 'hover') {
                ice.ace.jq(this._button).removeClass('ui-state-hover');
            } else if (state == 'active') {
                ice.ace.jq(this._button).removeClass('ui-state-active');
            } else if (state == 'disabled') {
                ice.ace.jq(this._button).removeClass('ui-state-disabled ');
            }
        };

        root = document.getElementById(spanId);

        root.firstChild.setAttribute("role", "link");
        root.firstChild.setAttribute("aria-labelledby", jsProps.label);
        if (jsfProps.disabled) {
            root.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
        }
        // If there's no action listener, this is standard anchor behaviour
        // otherwise it's got an actionListener/action attribute. Described by offers further description
        if (!jsfProps.doAction) {
            root.firstChild.setAttribute("aria-describedby", "Standard HTML anchor");
        } else {
            root.firstChild.setAttribute("aria-describedby", "JSF action event source");
        }

        bindYUI(oLinkButton);
    },

    //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
    updateProperties:function(clientId, jsProps, jsfProps, events) {

        var context = ice.ace.getJSContext(clientId);
        if (context && context.isAttached()) {
            var prevJSFProps = context.getJSFProps();
            if (prevJSFProps.hashCode != jsfProps.hashCode) {
                context.getComponent().destroy();
                document.getElementById(clientId)['JSContext'] = null;
                JSContext[clientId] = null;
            }
        }
        ice.ace.updateProperties(clientId, jsProps, jsfProps, events, this);
    },

    //delegate call to ice.yui.getInstance(..) with the reference of this lib
    getInstance:function(clientId, callback) {
        ice.ace.getInstance(clientId, callback, this);
    },

    // Click handler visible from Renderer code 
    clickHandler : function (e, clientId) {
        var JSContext = ice.ace.getJSContext(clientId);
        var doAction = JSContext.getJSFProps().doAction;
        //YAHOO.log("--> Button.doAction = " + doAction);

        var divRoot = document.getElementById(clientId);

        var postParameters = JSContext.getJSFProps().postParameters;
        var params = function(parameter) {
            if (postParameters != null) {
                var argCount = postParameters.length / 2;
                for (var idx = 0; idx < argCount; idx ++) {
                    parameter(postParameters[idx * 2], postParameters[(idx * 2) + 1]);
                }
            }
        };
        var evTarget = e.target || e.srcElement;
        if (evTarget.nodeType == 3) {
            evTarget = evTarget.parentNode;
        }
        var hrefAttr = YAHOO.util.Dom.getAttribute(evTarget, "href");

        var behaviors = JSContext.getJSProps().behaviors;
        if (behaviors && behaviors.activate) {
            // Convert core style params into ace style params
            var p = {};
            params(function(name, value) {
                p[name] = value;
            });
            ice.ace.ab(ice.ace.extendAjaxArguments(behaviors.activate, {params:p}));
        } else if (doAction) {
            ice.s(e, divRoot, params);
        } else if (!hrefAttr) {
            ice.se(e, divRoot, params);
        }

        // If there are actionListeners, don't do default behaviour
        if (doAction) {
            return false;
        }
    },

    // keyDown handler visible from Renderer code
    keyDownHandler : function (e, clientId) {
        if (e.keyCode != 13) {
            return true;
        }
        var JSContext = ice.ace.getJSContext(clientId);
        var doAction = JSContext.getJSFProps().doAction;
        //YAHOO.log("--> Button.doAction = " + doAction);

        var divRoot = document.getElementById(clientId);

        var postParameters = JSContext.getJSFProps().postParameters;
        var params = function(parameter) {
            if (postParameters != null) {
                var argCount = postParameters.length / 2;
                for (var idx = 0; idx < argCount; idx ++) {
                    parameter(postParameters[idx * 2], postParameters[(idx * 2) + 1]);
                }
            }
        };
        var behaviors = JSContext.getJSProps().behaviors;
        if (behaviors && behaviors.activate) {
            // Convert core style params into ace style params
            var p = {};
            params(function(name, value) {
                p[name] = value;
            });
            ice.ace.ab(ice.ace.extendAjaxArguments(behaviors.activate, {params:p}));
        } else if (doAction) {
            ice.s(e, divRoot, params);
        } else {
            ice.se(e, divRoot, params);
        }

        // If there are actionListeners, don't do default behaviour
        if (doAction) {
            return false;
        }
    }
};
