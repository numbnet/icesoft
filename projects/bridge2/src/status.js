/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

var on = operator();
var off = operator();

[ Ice.Status = new Object ].as(function(This) {
    This.NOOPIndicator = object(function (method) {
        method(on, noop);

        method(off, noop);
    });

    This.RedirectIndicator = function(uri) {
        return object(function (method) {
            method(on, function(self) {
                window.location.href = uri;
            });

            method(off, noop);
        });
    };

    This.ElementIndicator = function(elementID, indicators) {
        var instance = object(function (method) {
            method(on, function(self) {
                each(indicators, function(indicator) {
                    if (indicator != self) off(indicator);
                });
                var e = elementID.asElement();
                if (e) {
                    e.style.visibility = 'visible';
                }
            });

            method(off, function(self) {
                var e = elementID.asElement();
                if (e) {
                    e.style.visibility = 'hidden';
                }
            });
        });

        append(indicators, instance);
        off(instance);

        return instance;
    };

    This.OverlappingStateProtector = function(indicator) {
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
    };

    This.ToggleIndicator = function(onElement, offElement) {
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
    };

    This.MuxIndicator = function(a, b) {
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
    };

    This.PointerIndicator = function(element) {
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
    };

    This.OverlayIndicator = function(message, description, buttonText, iconPath, panel) {
        return object(function (method) {
            method(on, function(self) {
                on(panel);
                var messageContainer = document.createElement('div');
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
                document.body.appendChild(messageContainer);

                var messageElement = document.createElement('div');
                messageElement.appendChild(document.createTextNode(message));
                var messageElementStyle = messageElement.style;
                messageElementStyle.marginLeft = '30px';
                messageElementStyle.textAlign = 'left';
                messageElementStyle.fontSize = '14px';
                messageElementStyle.fontSize = '14px';
                messageElementStyle.fontWeight = 'bold';
                messageContainer.appendChild(messageElement);

                var descriptionElement = document.createElement('div');
                descriptionElement.appendChild(document.createTextNode(description));
                var descriptionElementStyle = descriptionElement.style;
                descriptionElementStyle.fontSize = '11px';
                descriptionElementStyle.marginTop = '7px';
                descriptionElementStyle.marginBottom = '7px';
                descriptionElementStyle.fontWeight = 'normal';
                messageElement.appendChild(descriptionElement);

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
    };

    This.DefaultStatusManager = function(configuration, container) {
        var connectionLostRedirect = configuration.connectionLostRedirectURI ? This.RedirectIndicator(configuration.connectionLostRedirectURI) : null;
        var sessionExpiredRedirect = configuration.sessionExpiredRedirectURI ? This.RedirectIndicator(configuration.sessionExpiredRedirectURI) : null;
        var messages = configuration.messages;
        var sessionExpiredIcon = configuration.connection.context + '/xmlhttp/css/xp/css-images/connect_disconnected.gif';
        var connectionLostIcon = configuration.connection.context + '/xmlhttp/css/xp/css-images/connect_caution.gif';
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

        return {
            busy: This.OverlappingStateProtector(This.PointerIndicator(container)),
            sessionExpired: sessionExpiredRedirect ? sessionExpiredRedirect : This.OverlayIndicator(messages.sessionExpired, messages.description, messages.buttonText, sessionExpiredIcon, overlay),
            connectionLost: connectionLostRedirect ? connectionLostRedirect : This.OverlayIndicator(messages.connectionLost, messages.description, messages.buttonText, connectionLostIcon, overlay),
            serverError: This.OverlayIndicator(messages.serverError, messages.description, messages.buttonText, connectionLostIcon, overlay),
            connectionTrouble: This.NOOPIndicator
        };
    };

    This.ComponentStatusManager = function(workingID, idleID, troubleID, lostID, defaultStatusManager, showPopups, displayHourglassWhenActive) {
        var indicators = [];
        var connectionWorking = Ice.Status.ElementIndicator(workingID, indicators);
        var connectionIdle = Ice.Status.ElementIndicator(idleID, indicators);
        var connectionLost = Ice.Status.ElementIndicator(lostID, indicators);
        var busyIndicator = Ice.Status.ToggleIndicator(connectionWorking, connectionIdle);

        var busy = Ice.Status.OverlappingStateProtector(displayHourglassWhenActive ? Ice.Status.MuxIndicator(defaultStatusManager.busy, busyIndicator) : busyIndicator);
        var connectionTrouble = Ice.Status.ElementIndicator(troubleID, indicators);
        if (showPopups) {
            return {
                busy: busy,
                connectionTrouble: connectionTrouble,
                connectionLost: Ice.Status.MuxIndicator(connectionLost, defaultStatusManager.connectionLost),
                sessionExpired: Ice.Status.MuxIndicator(connectionLost, defaultStatusManager.sessionExpired),
                serverError: Ice.Status.MuxIndicator(connectionLost, defaultStatusManager.serverError)
            }
        } else {
            return {
                busy: busy,
                connectionTrouble: connectionTrouble,
                connectionLost: defaultStatusManager.connectionLostRedirect ? defaultStatusManager.connectionLostRedirect : connectionLost,
                sessionExpired: defaultStatusManager.sessionExpiredRedirect ? defaultStatusManager.sessionExpiredRedirect : connectionLost,
                serverError: connectionLost
            }
        }
    };
});
