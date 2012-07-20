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

    function Overlay(cfg) {
        var container = cfg.element == undefined ? document.body : cfg.element;
        var overlay = container.ownerDocument.createElement('div');
        var activeLabel = cfg.activeLabel || cfg.activeImgUrl;
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

        if (activeLabel) {
            ice.ace.jq(ice.ace.escapeClientId(cfg.id)+"_display").clone(false,true).appendTo(overlay);
            var subMon = ice.ace.jq(overlay).find('> div:first-child');
            subMon.css('display', '').css('marginLeft', '-'+subMon.clientWidth / 2);
        }

        return function() {
            if (overlay) {
                try { container.removeChild(overlay); }
                catch (e) { //ignore, the overlay does not match the document after a html/body level update
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

        //in the future this function can be modified to read configuration set per component container element
        function isBlockUIEnabled(source) {
            return (cfg.blockUI != undefined && cfg.blockUI != false);
        }

        var changeState = function(state) {
            var text = '';
            var img = '';

            switch (state) {
                case 'active':
                    text = cfg.activeLabel;
                    img = cfg.activeImgUrl;
                    break;
                case 'server':
                    text = cfg.serverErrorLabel;
                    img = cfg.disconnectedImgUrl;
                    break;
                case 'network':
                    text = cfg.networkErrorLabel;
                    img = cfg.disconnectedImgUrl;
                    break;
                case 'session':
                    text = cfg.sessionExpiredLabel;
                    img = cfg.cautionImgUrl;
                    break;
                case 'idle':
                    text = cfg.idleLabel;
                    img = cfg.idleImgUrl;
                    break;
                default:
                    return;
            }

            ice.ace.jq(jqId+'_display > img.if-sub-mon-img').attr('src', img);
            ice.ace.jq(jqId+'_display > span.if-sub-mon-txt').html (text);
        }

        window.ice.onBeforeSubmit(function(source,isClientRequest) {
            changeState('active');

            //Only block the UI for client-initiated requests (not push requests)
            if (isClientRequest && isBlockUIEnabled(source)) {
                console.log('Blocking UI');
                var blockUIOverlay = Overlay(cfg);
                var rollbacks = fold(['input', 'select', 'textarea', 'button', 'a'], [], function(result, type) {
                    return result.concat(
                            ice.ace.jq.map(document.body.getElementsByTagName(type), function(e) {
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
                stopBlockingUI = function () {};;
            }
        });

        window.ice.onAfterUpdate(function() {
            stopBlockingUI();
            changeState('idle');
        });

        changeState('idle');
    }
})();