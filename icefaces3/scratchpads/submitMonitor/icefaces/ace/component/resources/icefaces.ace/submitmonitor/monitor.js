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

(function() {
    var broadcast = function(funcs, args) {
        args = args || [];

        for (var i in funcs)
            if (ice.ace.jq.isNumeric(i)) funcs[i].apply(funcs[i],args);
    };

    var fold = function(items, initialValue, injector) {
        var tally = initialValue;
        var size = items.length;
        for (var i = 0; i < size; i++) {
            tally = injector(tally, items[i]);
        }

        return tally;
    };

    function Overlay(cfg, container) {
        var overlay = document.createElement('iframe');
        overlay.setAttribute('src', 'about:blank');
        overlay.setAttribute('frameborder', '0');
        overlay.className = 'ice-blockui-overlay';
        var overlayStyle = overlay.style;
        overlayStyle.top = '0';
        overlayStyle.left = '0';

        if (container.tagName.toLowerCase() == 'body') {
            overlayStyle.width = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) + 'px';
            overlayStyle.height = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) + 'px';
        } else {
            overlayStyle.width = container.offsetWidth + 'px';
            overlayStyle.height = container.offsetHeight + 'px';
        }

        container.appendChild(overlay);

        var cloneToRemove;
        var revertElem;
        var revertZIndex;
        if (cfg.autoCenter) {
            cloneToRemove = ice.ace.jq(ice.ace.escapeClientId(cfg.id)+"_display").clone(false,true);
            cloneToRemove.attr('id', cfg.id + '_clone');
            cloneToRemove.addClass('clone');
            cloneToRemove.css('z-index', '28001');
            cloneToRemove.css('display', '');
            cloneToRemove.appendTo(container);
            cloneToRemove.position({
                my: 'center center',
                at: 'center center',
                of: container,
                collision: 'fit'});
        } else {
            revertElem = ice.ace.jq(ice.ace.escapeClientId(cfg.id)+"_display");
            if (revertElem) {
                revertZIndex = revertElem.css('z-index');
                revertElem.css('z-index', '28001');
                revertElem.css('display', '');
            }
        }

        return function() {
            if (overlay) {
                try { container.removeChild(overlay); }
                catch (e) { //ignore, the overlay does not match the document after a html/body level update
                }
            }
            if (cloneToRemove) {
                try { cloneToRemove.remove(); /*container.removeChild(cloneToRemove[0]);*/ }
                catch (e) { //ignore, the cloneToRemove does not match the document after a html/body level update
                }
            }
            if (revertElem) {
                try {
                    revertElem.css('z-index', revertZIndex);
                    revertElem.css('display', 'none');
                }
                catch (e) { //ignore, the cloneToRemove does not match the document after a html/body level update
                }
            }
        };
    }

    function eventSink(element) {
        return function(e) {
            var evenType = e.type;
            var triggeringElement = e.srcElement ? e.srcElement : e.target;
            var capturingElement = element;
            console.log('event [type: ' + evenType +
                    ', triggered by: ' + triggeringElement.id || triggeringElement +
                    ', captured in: ' + capturingElement.id || capturingElement + '] was discarded.');
            return false;
        }
    }

    var stopBlockingUI = function () {};

    if (!ice.ace) ice.ace = {};

    ice.ace.Monitor = function (cfg) {
        var jqId = ice.ace.escapeClientId(cfg.id);

        function isBlockUIEnabled() {
            return (cfg.blockUI == undefined || cfg.blockUI != '@none');
        }

        function resolveBlockUIElement(source) {
            var rawBlockUI = (cfg.blockUI == undefined) ? '@all' : cfg.blockUI;
            if (rawBlockUI == '@all') {
                return document.body;
            } else if (rawBlockUI == '@source') {
                return source;
            } else if (rawBlockUI == '@none') {
                return null;
            } else {
                var elem = ice.ace.jq(ice.ace.escapeClientId(rawBlockUI));
                if (elem && elem.length > 0) {
                    return elem[0];
                }
                return null;
            }
        }

        var allStates = ['idle', 'active', 'serverError', 'networkError', 'sessionExpired'];
        var IDLE = 0, ACTIVE = 1, SERVER_ERROR = 2, NETWORK_ERROR = 3, SESSION_EXPIRED = 4;

        var changeState = function(state) {
            console.log('changeState: ' + state + ' : ' + allStates[state]);
            ice.ace.jq(jqId+'_display > div.if-sub-mon-mid').hide().filter('.'+allStates[state]).show();
            ice.ace.jq(jqId+'_clone > div.if-sub-mon-mid').hide().filter('.'+allStates[state]).show();
        };

        var doOverlayIfBlockingUI = function(source,isClientRequest) {
            //Only block the UI for client-initiated requests (not push requests)
            if (isClientRequest && isBlockUIEnabled()) {
                console.log('Blocking UI');
                var overlayContainerElem = resolveBlockUIElement(source);
                var blockUIOverlay = Overlay(cfg, overlayContainerElem);
                var rollbacks = fold(['input', 'select', 'textarea', 'button', 'a'], [], function(result, type) {
                    return result.concat(
                            ice.ace.jq.map(overlayContainerElem.getElementsByTagName(type), function(e) {
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
                    })
                    );
                });

                stopBlockingUI = function() {
                    broadcast(rollbacks);
                    blockUIOverlay();
                    console.log('Unblocked UI');
                };
            } else {
                stopBlockingUI = function () {};
            }
        };

        window.ice.onBeforeSubmit(function(source,isClientRequest) {
            changeState(ACTIVE);
            doOverlayIfBlockingUI(source,isClientRequest);
        });

        window.ice.onAfterUpdate(function() {
            stopBlockingUI();
            changeState(IDLE);
        });

        window.ice.onServerError(function() {
            changeState(SERVER_ERROR);
        });

        window.ice.onNetworkError(function() {
            changeState(NETWORK_ERROR);
        });

        window.ice.onSessionExpiry(function() {
            changeState(SESSION_EXPIRED);
        });

        changeState(IDLE);
    }
})();