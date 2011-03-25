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


var DefaultIndicators;
var ComponentIndicators;
(function() {
    var on = operator();
    var off = operator();

    var NOOPIndicator = object(function (method) {
        method(on, noop);

        method(off, noop);
    });

    function RedirectIndicator(uri) {
        return object(function (method) {
            method(on, function(self) {
                window.location.href = uri;
            });

            method(off, noop);
        });
    }

    function ElementIndicator(elementID, indicators) {
        var instance = object(function (method) {
            method(on, function(self) {
                each(indicators, function(indicator) {
                    if (indicator != self) off(indicator);
                });
                var e = document.getElementById(elementID);
                if (e) {
                    e.style.visibility = 'visible';
                }
            });

            method(off, function(self) {
                var e = document.getElementById(elementID);
                if (e) {
                    e.style.visibility = 'hidden';
                }
            });
        });

        append(indicators, instance);
        off(instance);

        return instance;
    }

    function OverlappingStateProtector(indicator) {
        var counter = 0;

        return object(function (method) {
            method(on, function() {
                if (counter == 0) on(indicator);
                ++counter;
            });

            method(off, function() {
                if (counter < 1) return;
                if (counter == 1) off(indicator);
                --counter;
            });
        });
    }

    function ToggleIndicator(onElement, offElement) {
        var instance = object(function (method) {
            method(on, function(self) {
                on(onElement);
                off(offElement);
            });

            method(off, function(self) {
                off(onElement);
                on(offElement);
            });
        });

        off(instance);
        return instance;
    }

    function MuxIndicator() {
        var indicators = arguments;
        var instance = object(function (method) {
            method(on, function(self) {
                each(indicators, on);
            });

            method(off, function(self) {
                each(indicators, off);
            });
        });

        off(instance);
        return instance;
    }

    function PointerIndicator(element) {
        var privateOff = noop;

        function toggle() {
            //block any other action from triggering the indicator before being in 'off' state again
            privateOn = noop;
            //prepare cursor shape rollback
            function toggleElementCursor(e) {
                var c = e.style.cursor;
                e.style.cursor = 'wait';
                return function() {
                    e.style.cursor = c;
                };
            }

            var cursorRollbacks = inject(['input', 'select', 'textarea', 'button', 'a'], [ toggleElementCursor(element) ], function(result, type) {
                each(element.getElementsByTagName(type), function(e) {
                    append(result, toggleElementCursor(e));
                });
                return result;
            });

            privateOff = function() {
                broadcast(cursorRollbacks);
                privateOn = toggle;
                privateOff = noop;
            };
        }

        var privateOn = toggle;

        return object(function (method) {
            method(on, /Safari/.test(navigator.userAgent) ? noop : function(self) {
                privateOn();
            });

            method(off, function(self) {
                privateOff();
            });
        });
    }

    function OverlayIndicator() {
        return object(function(method) {
            var isIEBrowser = /MSIE/.test(navigator.userAgent);
            var overlay;
            var delayedOverlayRender;

            function createOverlay() {
                if (isIEBrowser) {
                    overlay = document.createElement('iframe');
                    overlay.setAttribute('src', 'javascript:document.write(\'<html><body style="cursor: wait;"></body><html>\');');
                    overlay.setAttribute('frameborder', '0');
                    document.body.appendChild(overlay);
                } else {
                    overlay = document.body.appendChild(document.createElement('div'));
                    overlay.style.cursor = 'wait';
                }

                var overlayStyle = overlay.style;
                overlayStyle.position = 'absolute';
                overlayStyle.backgroundColor = 'white';
                overlayStyle.zIndex = '38000';
                overlayStyle.top = '0';
                overlayStyle.left = '0';
                overlayStyle.opacity = '0';
                overlayStyle.filter = 'alpha(opacity=0)';
                overlayStyle.width = (Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) - 20) + 'px';
                overlayStyle.height = (Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) - 20) + 'px';
            }

            function deleteOverlay() {
                if (isIEBrowser) {
                    var blankOverlay = document.createElement('iframe');
                    blankOverlay.setAttribute('src', 'javascript:document.write("<html></html>");');
                    blankOverlay.setAttribute('frameborder', '0');
                    document.body.replaceChild(blankOverlay, overlay);
                    document.body.removeChild(blankOverlay);
                } else {
                    document.body.removeChild(overlay);
                }
                overlay = null;
            }

            method(on, function(self) {
                delayedOverlayRender = runOnce(Delay(createOverlay, 750));
            });

            method(off, function(self) {
                if (delayedOverlayRender) {
                    stop(delayedOverlayRender);
                }
                if (overlay) {
                    deleteOverlay();
                }
            });
        });
    }

    function PopupIndicator(message, description, buttonText, iconPath, panel) {
        return object(function (method) {
            method(on, function(self) {
                on(panel);
                var messageContainer = document.body.appendChild(document.createElement('div'));
                var messageContainerStyle = messageContainer.style;
                messageContainerStyle.position = 'absolute';
                messageContainerStyle.textAlign = 'center';
                messageContainerStyle.zIndex = '28001';
                messageContainerStyle.color = 'black';
                messageContainerStyle.backgroundColor = 'white';
                messageContainerStyle.paddingLeft = '0';
                messageContainerStyle.paddingRight = '0';
                messageContainerStyle.paddingTop = '15px';
                messageContainerStyle.paddingBottom = '15px';
                messageContainerStyle.borderBottomColor = 'gray';
                messageContainerStyle.borderRightColor = 'gray';
                messageContainerStyle.borderTopColor = 'silver';
                messageContainerStyle.borderLeftColor = 'silver';
                messageContainerStyle.borderWidth = '2px';
                messageContainerStyle.borderStyle = 'solid';
                messageContainerStyle.width = '270px';

                var messageElement = messageContainer.appendChild(document.createElement('div'));
                messageElement.appendChild(document.createTextNode(message));
                var messageElementStyle = messageElement.style;
                messageElementStyle.marginLeft = '30px';
                messageElementStyle.textAlign = 'left';
                messageElementStyle.fontSize = '14px';
                messageElementStyle.fontSize = '14px';
                messageElementStyle.fontWeight = 'bold';

                var descriptionElement = messageElement.appendChild(document.createElement('div'));
                descriptionElement.appendChild(document.createTextNode(description));
                var descriptionElementStyle = descriptionElement.style;
                descriptionElementStyle.fontSize = '11px';
                descriptionElementStyle.marginTop = '7px';
                descriptionElementStyle.marginBottom = '7px';
                descriptionElementStyle.fontWeight = 'normal';

                var buttonElement = document.createElement('input');
                buttonElement.type = 'button';
                buttonElement.value = buttonText;
                var buttonElementStyle = buttonElement.style;
                buttonElementStyle.fontSize = '11px';
                buttonElementStyle.fontWeight = 'normal';
                buttonElement.onclick = function() {
                    window.location.reload();
                };
                messageContainer.appendChild(buttonElement);
                var resize = function() {
                    messageContainerStyle.left = ((window.width() - messageContainer.clientWidth) / 2) + 'px';
                    messageContainerStyle.top = ((window.height() - messageContainer.clientHeight) / 2) + 'px';
                };
                resize();
                onResize(window, resize);
            });

            method(off, noop);
        });
    }

    var indctrs;

    DefaultIndicators = function(configuration, setupID) {
        var container = document.getElementById(setupID).parentNode;

        //disable core's default indicators/popups
        container.configuration.disableDefaultErrorPopups = true;

        var connectionLostURI = configuration.connectionLostRedirectURI;
        if (connectionLostURI == "null") {
            connectionLostURI = null;
        }

        var sessionExpiredURI = configuration.sessionExpiredRedirectURI;
        if (sessionExpiredURI == "null") {
            sessionExpiredURI = null;
        }

        var connectionLostRedirect = connectionLostURI ? RedirectIndicator(connectionLostURI) : null;
        var sessionExpiredRedirect = sessionExpiredURI ? RedirectIndicator(sessionExpiredURI) : null;
        var messages = configuration.messages;
        var sessionExpiredIcon = configuration.connection.context + '/xmlhttp/css/xp/css-images/connect_disconnected.gif';
        var connectionLostIcon = configuration.connection.context + '/xmlhttp/css/xp/css-images/connect_caution.gif';
        var busyIndicator = PointerIndicator(container);
        var overlay = object(function(method) {
            method(on, function(self) {
                var overlay = container.ownerDocument.createElement('iframe');
                overlay.setAttribute('src', 'about:blank');
                overlay.setAttribute('frameborder', '0');
                var overlayStyle = overlay.style;
                overlayStyle.position = 'absolute';
                overlayStyle.display = 'block';
                overlayStyle.visibility = 'visible';
                overlayStyle.backgroundColor = 'white';
                overlayStyle.zIndex = '28000';
                overlayStyle.top = '0';
                overlayStyle.left = '0';
                overlayStyle.opacity = 0.22;
                overlayStyle.filter = 'alpha(opacity=22)';
                container.appendChild(overlay);

                var resize = container.tagName.toLowerCase() == 'body' ?
                        function() {
                            overlayStyle.width = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) + 'px';
                            overlayStyle.height = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) + 'px';
                        } :
                        function() {
                            overlayStyle.width = container.offsetWidth + 'px';
                            overlayStyle.height = container.offsetHeight + 'px';
                        };
                resize();
                onResize(window, resize);
            });

            method(off, noop);
        });

        indctrs = {
            busy: busyIndicator,
            sessionExpired: sessionExpiredRedirect ? sessionExpiredRedirect : PopupIndicator(messages.sessionExpired, messages.description, messages.buttonText, sessionExpiredIcon, overlay),
            connectionLost: connectionLostRedirect ? connectionLostRedirect : PopupIndicator(messages.connectionLost, messages.description, messages.buttonText, connectionLostIcon, overlay),
            serverError: NOOPIndicator,
            connectionTrouble: NOOPIndicator
        };
    };

    ComponentIndicators = function(workingID, idleID, troubleID, lostID, showPopups, displayHourglassWhenActive) {
        var indicators = [];
        var connectionWorking = ElementIndicator(workingID, indicators);
        var connectionIdle = ElementIndicator(idleID, indicators);
        var connectionLost = ElementIndicator(lostID, indicators);
        var busyElementIndicator = ToggleIndicator(connectionWorking, connectionIdle);
        //avoid displaying the overlay twice
        var busyIndicator = displayHourglassWhenActive ? busyElementIndicator : MuxIndicator(busyElementIndicator, OverlayIndicator());

        var busy = OverlappingStateProtector(displayHourglassWhenActive ? MuxIndicator(indctrs.busy, busyIndicator) : busyIndicator);
        var connectionTrouble = ElementIndicator(troubleID, indicators);
        if (showPopups) {
            indctrs = {
                busy: busy,
                connectionTrouble: connectionTrouble,
                connectionLost: MuxIndicator(connectionLost, indctrs.connectionLost),
                sessionExpired: MuxIndicator(connectionLost, indctrs.sessionExpired),
                serverError: MuxIndicator(connectionLost, indctrs.serverError)
            };
        } else {
            indctrs = {
                busy: busy,
                connectionTrouble: connectionTrouble,
                connectionLost: indctrs.connectionLostRedirect ? indctrs.connectionLostRedirect : connectionLost,
                sessionExpired: indctrs.sessionExpiredRedirect ? indctrs.sessionExpiredRedirect : connectionLost,
                serverError: connectionLost
            };
        }
    };

    onLoad(window, function() {
        ice.onSubmitSend(function() {
            indctrs && on(indctrs.busy);
        });
        ice.onSubmitResponse(function() {
            indctrs && off(indctrs.busy);
        });
        ice.onNetworkError(function() {
            indctrs && on(indctrs.connectionLost);
        });
        ice.onServerError(function() {
            indctrs && on(indctrs.serverError);
        });
        ice.onSessionExpiry(function() {
            if (indctrs) {
                //avoid displaying irrelevant notifications from ICEpush bridge
                indctrs.connectionTrouble = NOOPIndicator;
                indctrs.connectionLost = NOOPIndicator;
                on(indctrs.sessionExpired);
            }
        });

        if (ice.push) {
            ice.onBlockingConnectionUnstable(function() {
                indctrs && on(indctrs.connectionTrouble);
            });
            ice.onBlockingConnectionLost(function() {
                indctrs && on(indctrs.connectionLost);
            });
        }
    });
})();
