/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

(function() {
    var off = operator();

    function Overlay(element) {
        var container = element || document.body;
        var overlay = container.ownerDocument.createElement('iframe');
        overlay.setAttribute('src', 'about:blank');
        overlay.setAttribute('frameborder', '0');
        var overlayStyle = overlay.style;
        overlayStyle.position = 'absolute';
        overlayStyle.backgroundColor = 'white';
        overlayStyle.zIndex = '28000';
        overlayStyle.top = '0';
        overlayStyle.left = '0';
        overlayStyle.opacity = 0.22;
        overlayStyle.filter = 'alpha(opacity=22)';

        if (container.tagName.toLowerCase() == 'body') {
            overlayStyle.width = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) + 'px';
            overlayStyle.height = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) + 'px';
        } else {
            overlayStyle.width = container.offsetWidth + 'px';
            overlayStyle.height = container.offsetHeight + 'px';
        }

        container.appendChild(overlay);

        return object(function(method) {
            method(off, function(self) {
                if (overlay) container.removeChild(overlay);
            });
        });
    }

    //in the future this function can be modified to read configuration set per component container element 
    function isBlockUIEnabled(source) {
        return configurationOf(source).blockUIOnSubmit;
    }

    function eventSink(element) {
        return function(e) {
            var ev = $event(e, element);
            var evenType = type(ev);
            var triggeringElement = triggeredBy(ev);
            var capturingElement = capturedBy(ev);
            debug(logger, 'event [type: ' + evenType +
                    ', triggered by: ' + identifier(triggeringElement) || triggeringElement +
                    ', captured in: ' + identifier(capturingElement) || capturingElement + '] was discarded.');
        }
    }

    var stopBlockingUI = noop;
    namespace.onBeforeSubmit(function(source) {
        //don't block UI for the retrieveUpdate requests
        if (viewIDOf(source) != source.id && isBlockUIEnabled(source)) {
            debug(logger, 'blocking UI');
            var blockUIOverlay = Overlay();
            var rollbacks = inject(['input', 'select', 'textarea', 'button', 'a'], [], function(result, type) {
                return concatenate(result, asArray(collect(document.body.getElementsByTagName(type), function(e) {
                    var sink = eventSink(e);
                    var onkeypress = e.onkeypress;
                    var onkeyup = e.onkeyup;
                    var onkeydown = e.onkeydown;
                    var onclick = e.onclick;
                    e.onkeypress = sink;
                    e.onkeyup = sink;
                    e.onkeydown = sink;
                    e.onclick = sink;

                    return function() {
                        try {
                            e.onkeypress = onkeypress;
                            e.onkeyup = onkeyup;
                            e.onkeydown = onkeydown;
                            e.onclick = onclick;
                        } catch (ex) {
                            //don't fail if element is not present anymore
                        }
                    };
                })));
            });

            stopBlockingUI = function() {
                broadcast(rollbacks);
                off(blockUIOverlay);
                debug(logger, 'unblocked UI');
            };
        } else {
            stopBlockingUI = noop;
        }
    });

    namespace.onAfterUpdate(function() {
        stopBlockingUI();
    });
})();
