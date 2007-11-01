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

[ Ice.Status = new Object ].as(function(This) {
    This.NOOPIndicator = {
        on: Function.NOOP,
        off: Function.NOOP
    };

    This.RedirectIndicator = Object.subclass({
        initialize: function(uri) {
            this.uri = uri;
        },

        on: function() {
            window.location.href = this.uri;
        }
    });

    This.ElementIndicator = Object.subclass({
        initialize: function(elementID, indicators) {
            this.elementID = elementID;
            this.indicators = indicators;
            this.indicators.push(this);
            this.off();
        },

        on: function() {
            this.indicators.each(function(indicator) {
                if (indicator != this) indicator.off();
            }.bind(this));
            this.elementID.asElement().style.visibility = 'visible';
        },

        off: function() {
            this.elementID.asElement().style.visibility = 'hidden';
        }
    });

    This.ToggleIndicator = Object.subclass({
        initialize: function(onElement, offElement) {
            this.onElement = onElement;
            this.offElement = offElement;
            this.off();
        },

        on: function() {
            this.onElement.on();
            this.offElement.off();
        },

        off: function() {
            this.onElement.off();
            this.offElement.on();
        }
    });

    This.PointerIndicator = Object.subclass({
        initialize: function(element) {
            this.element = element;
            this.previousCursor = this.element.style.cursor;
        },

        on: /Safari/.test(navigator.userAgent) ? Function.NOOP : function() {
            this.element.style.cursor = 'wait';
        },

        off: /Safari/.test(navigator.userAgent) ? Function.NOOP : function() {
            this.element.style.cursor = this.previousCursor;
        }
    });

    This.OverlayIndicator = Object.subclass({
        initialize: function(message, description, iconPath, panel) {
            this.message = message;
            this.description = description;
            this.iconPath = iconPath;
            this.panel = panel;
        },

        on: function() {
            this.panel.on();
            var messageContainer = document.createElement('div');
            var messageContainerStyle = messageContainer.style;
            messageContainerStyle.position = 'absolute';
            messageContainerStyle.textAlign = 'center';
            messageContainerStyle.zIndex = '10001';
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
            messageElement.appendChild(document.createTextNode(this.message));
            var messageElementStyle = messageElement.style;
            messageElementStyle.marginLeft = '30px';
            messageElementStyle.textAlign = 'left';
            messageElementStyle.fontSize = '14px';
            messageElementStyle.fontSize = '14px';
            messageElementStyle.fontWeight = 'bold';
            messageContainer.appendChild(messageElement);

            var descriptionElement = document.createElement('div');
            descriptionElement.appendChild(document.createTextNode(this.description));
            var descriptionElementStyle = descriptionElement.style;
            descriptionElementStyle.fontSize = '11px';
            descriptionElementStyle.marginTop = '7px';
            descriptionElementStyle.marginBottom = '7px';
            descriptionElementStyle.fontWeight = 'normal';
            messageElement.appendChild(descriptionElement);

            var buttonElement = document.createElement('input');
            buttonElement.type = 'button';
            buttonElement.value = 'Reload';
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
            }.bind(this);
            resize();
            window.onResize(resize);
        }
    });

    This.StatusManager = Object.subclass({
        initialize: function(configuration, container) {
            this.container = container;
            this.busy = This.NOOPIndicator;
            this.connectionLost = This.NOOPIndicator;
            this.connectionTrouble = This.NOOPIndicator;
            this.sessionExpired = This.NOOPIndicator;
            this.serverError = This.NOOPIndicator;
            //need to look for the status component after the page has loaded
            //this relies on the bridge registering the 'onload' handler after
            //the status component registering its own handler
            window.onLoad(function() {
                var connectionLostRedirect = configuration.redirectURI ? new This.RedirectIndicator(configuration.redirectURI) : null;
                if (container.connectionStatus) {
                    this.indicators = [];
                    var connectionWorking = new This.ElementIndicator(container.connectionStatus.working, this.indicators);
                    var connectionIdle = new This.ElementIndicator(container.connectionStatus.idle, this.indicators);
                    this.busy = new This.ToggleIndicator(connectionWorking, connectionIdle);
                    this.connectionLost = connectionLostRedirect ? connectionLostRedirect : new This.ElementIndicator(container.connectionStatus.lost, this.indicators);
                    this.connectionTrouble = new This.ElementIndicator(container.connectionStatus.trouble, this.indicators);
                    this.sessionExpired = this.connectionLost;
                    this.serverError = this.connectionLost;
                } else {
                    this.busy = new This.PointerIndicator(container);
                    var description = 'To reconnect click the Reload button on the browser or click the button below';
                    var sessionExpiredIcon = configuration.connection.context + '/xmlhttp/css/xp/css-images/connect_disconnected.gif';
                    var connectionLostIcon = configuration.connection.context + '/xmlhttp/css/xp/css-images/connect_caution.gif';
                    this.sessionExpired = new This.OverlayIndicator('User Session Expired', description, sessionExpiredIcon, this)
                    this.serverError = new This.OverlayIndicator('Server Internal Error', description, connectionLostIcon, this)
                    this.connectionLost = connectionLostRedirect ? connectionLostRedirect : new This.OverlayIndicator('Network Connection Interrupted', description, connectionLostIcon, this);
                    this.connectionTrouble = { on: Function.NOOP, off: Function.NOOP };
                }
            }.bind(this));
        },

        on: function() {
            var overlay = this.container.ownerDocument.createElement('iframe');
            overlay.setAttribute('src', 'about:blank');
            overlay.setAttribute('frameborder', '0');
            var overlayStyle = overlay.style;
            overlayStyle.position = 'absolute';
            overlayStyle.display = 'block';
            overlayStyle.visibility = 'visible';
            overlayStyle.backgroundColor = 'white';
            overlayStyle.zIndex = '10000';
            overlayStyle.top = '0';
            overlayStyle.left = '0';
            overlayStyle.opacity = 0.22;
            overlayStyle.filter = 'alpha(opacity=22)';
            this.container.appendChild(overlay);

            var resize = this.container.tagName.toLowerCase() == 'body' ?
                         function() {
                             overlayStyle.width = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) + 'px';
                             overlayStyle.height = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) + 'px';
                         } :
                         function() {
                             overlayStyle.width = this.container.offsetWidth + 'px';
                             overlayStyle.height = this.container.offsetHeight + 'px';
                         };
            resize();
            window.onResize(resize);
        }
    });
});
