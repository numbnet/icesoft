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

var setupDefaultIndicators;
(function() {
    function PopupIndicator(message, description, panel) {
        panel();
        var messageContainer = document.body.appendChild(document.createElement('div'));
        var messageContainerStyle = messageContainer.style;
        messageContainerStyle.position = 'absolute';
        messageContainerStyle.textAlign = 'center';
        messageContainerStyle.zIndex = '28001';
        messageContainerStyle.color = 'black';
        messageContainerStyle.backgroundColor = 'white';
        messageContainerStyle.paddingLeft = '10px';
        messageContainerStyle.paddingRight = '10px';
        messageContainerStyle.paddingTop = '15px';
        messageContainerStyle.paddingBottom = '15px';
        messageContainerStyle.borderBottomColor = 'gray';
        messageContainerStyle.borderRightColor = 'gray';
        messageContainerStyle.borderTopColor = 'silver';
        messageContainerStyle.borderLeftColor = 'silver';
        messageContainerStyle.borderWidth = '2px';
        messageContainerStyle.borderStyle = 'solid';

        var messageElement = messageContainer.appendChild(document.createElement('div'));
        messageElement.appendChild(document.createTextNode(message));
        var messageElementStyle = messageElement.style;
        messageElementStyle.textAlign = 'left';
        messageElementStyle.fontSize = '14px';
        messageElementStyle.fontSize = '14px';
        messageElementStyle.fontWeight = 'bold';

        var descriptionElement = messageElement.appendChild(document.createElement('div'));
        descriptionElement.innerHTML = description;
        var descriptionElementStyle = descriptionElement.style;
        descriptionElementStyle.fontSize = '11px';
        descriptionElementStyle.marginTop = '7px';
        descriptionElementStyle.marginBottom = '7px';
        descriptionElementStyle.fontWeight = 'normal';

        var resize = function() {
            messageContainerStyle.left = ((window.width() - messageContainer.clientWidth) / 2) + 'px';
            messageContainerStyle.top = ((window.height() - messageContainer.clientHeight) / 2) + 'px';
        };
        resize();
        onResize(window, resize);
    }

    function BackgroundOverlay(container) {
        return function() {
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
        };
    }

    function extractTagContent(tag, html) {
        var start = new RegExp('\<' + tag + '[^\<]*\>', 'g').exec(html);
        var end = new RegExp('\<\/' + tag + '\>', 'g').exec(html);
        var tagWithContent = html.substring(start.index, end.index + end[0].length);
        return tagWithContent.substring(tagWithContent.indexOf('>') + 1, tagWithContent.lastIndexOf('<'));
    }

    setupDefaultIndicators = function(container, configuration) {
        var overlay = BackgroundOverlay(container);

        namespace.onServerError(function(code, html, xmlContent) {
            if (!configuration.disableDefaultIndicators) {
                //test if server error message is formatted in XML
                var message;
                var description;
                if (xmlContent) {
                    message = xmlContent.getElementsByTagName("error-message")[0].firstChild.nodeValue;
                    description = xmlContent.getElementsByTagName("error-name")[0].firstChild.nodeValue;
                } else {
                    message = extractTagContent('title', html);
                    description = extractTagContent('body', html);
                }
                PopupIndicator(message, description, overlay);
            }
        });

        namespace.onNetworkError(function() {
            if (!configuration.disableDefaultIndicators) {
                PopupIndicator("Network Connection Interrupted", "Reload this page to try to reconnect.", overlay);
            }
        });

        namespace.onSessionExpiry(function() {
            if (!configuration.disableDefaultIndicators) {
                PopupIndicator("User Session Expired", "Reload this page to start a new user session.", overlay);
            }
        });
    }
})();
