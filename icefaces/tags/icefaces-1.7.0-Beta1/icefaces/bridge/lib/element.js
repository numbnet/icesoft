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

[ Ice.ElementModel = new Object ].as(function(This) {

    This.TemporaryContainer = function() {
        var container = document.body.appendChild(document.createElement('div'));
        container.style.visibility = 'hidden';
        This.TemporaryContainer = function() {
            return container;
        };

        return container;
    };

    This.Update = Object.subclass({
        initialize: function(element) {
            this.element = element;
            var tag = element.getAttribute('tag');
            this.startTag = function(html) {
                html.push('<');
                html.push(tag);
                this.eachAttribute(function(name, value) {
                    html.push(' ');
                    html.push(name);
                    html.push('="');
                    html.push(value);
                    html.push('"');
                });
                html.push('>');
            };
            this.endTag = function(html) {
                html.push('</');
                html.push(tag);
                html.push('>');
            };
        },

        eachAttribute: function(iterator) {
            $enumerate(this.element.getElementsByTagName('attribute')).each(function(attribute) {
                var value = attribute.firstChild ? attribute.firstChild.data : '';
                iterator(attribute.getAttribute('name'), value);
            });
        },

        content: function() {
            var contentElement = this.element.getElementsByTagName('content')[0];
            return contentElement.firstChild ?
                   contentElement.firstChild.data.replace(/<\!\#cdata\#/g, '<![CDATA[').replace(/\#\#>/g, ']]>') : '';
        },

        asHTML: function() {
            var html = [];
            this.startTag(html);
            html.push(this.content());
            this.endTag(html);
            return html.join('');
        },

        asString: function() {
            var html = [];
            this.startTag(html);
            html.push('...');
            this.endTag(html);
            return html.join('');
        }
    });

    This.Element = Object.subclass({
        MouseListenerNames: [ 'onClick', 'onDblClick', 'onMouseDown', 'onMouseMove', 'onMouseOut', 'onMouseOver', 'onMouseUp' ],

        KeyListenerNames: [ 'onKeyDown', 'onKeyPress', 'onKeyUp', 'onHelp' ],

        initialize: function(element) {
            this.element = element;
        },

        id: function() {
            return this.element.id;
        },

        isSubmit: function() {
            return false;
        },

        updateDOM: function(update) {
            this.replaceHtml(update.asHTML());
        },

        replaceHtml: function(html) {
            this.withTemporaryContainer(function(container) {
                container.innerHTML = html;
                var newElement = container.firstChild;
                this.disconnectAllListenersAndPeers();
                this.replaceHostElementWith(newElement);
            });
        },

        disconnectAllListenersAndPeers: function() {
            var elements = this.element.getElementsByTagName('*');
            var length = elements.length;
            for (var i = 0; i < length; i++) {
                var element = elements[i];
                var peer = element.peer;
                if (peer) {
                    //disconnect listeners
                    peer.eachListenerName(function(listenerName) {
                        element[listenerName.toLowerCase()] = null;
                    });
                    //disconnect peers
                    element.peer = null;
                    peer.element = null;
                }
            }
        },

        serializeOn: function(query) {
        },

        sendOn: function(connection) {
            Query.create(function(query) {
                this.serializeOn(query);
            }.bind(this)).sendOn(connection);
        },

        send: function() {
            this.sendOn(connection);
        },

        withTemporaryContainer: function(execute) {
            try {
                execute.apply(this, [ This.TemporaryContainer() ]);
            } finally {
                This.TemporaryContainer().innerHTML = '';
            }
        },

        defaultReplaceHostElementWith: function(newElement) {
            this.displayOff();
            this.element.parentNode.replaceChild(newElement, this.element);
            this.element = newElement;
            this.element.peer = this;
        },

        replaceHostElementWith: function(newElement) {
            this.defaultReplaceHostElementWith(newElement);
        },

        //hide deleted elements -- Firefox 1.0.x renders tables after they are removed from the document.
        displayOff: /Safari/.test(navigator.userAgent) ? Function.NOOP : function() {
            this.element.style.display = 'none';
        },

        eachListenerName: function(iterator) {
            this.MouseListenerNames.each(iterator);
            this.KeyListenerNames.each(iterator);
        },

        findConnection: function() {
            return this.findContainerFor('bridge').bridge.connection;
        },

        findContainerFor: function(property) {
            var parent = this.element;
            while (parent) {
                if (parent[property]) {
                    return parent;
                } else {
                    parent = parent.parentNode;
                }
            }

            throw 'couldn\'t find container for property: ' + property;
        }
    });

    This.Element.adaptToElement = function(e) {
        if (e.peer) return e.peer;
        //no polymophism here...'switch' is the way then.
        switch (e.tagName.toLowerCase()) {
            case 'textarea':
            case 'input': e.peer = new This.InputElement(e); break;
            case 'th':
            case 'td':
            case 'tr': e.peer = new This.TableCellElement(e); break;
            case 'button': e.peer = new This.ButtonElement(e); break;
            case 'select': e.peer = new This.SelectElement(e); break;
            case 'form': e.peer = new This.FormElement(e); break;
            case 'head': e.peer = new This.HeadElement(e); break;
            case 'body': e.peer = new This.BodyElement(e); break;
            case 'script': e.peer = new This.ScriptElement(e); break;
            case 'title': e.peer = new This.TitleElement(e); break;
            case 'a': e.peer = new This.AnchorElement(e); break;
            case 'iframe': e.peer = new This.IFrameElement(e); break;
            case 'html': e.peer = new This.HtmlElement(e); break;
            default : e.peer = new This.Element(e); break;
        }

        return e.peer;
    };

    This.InputElement = This.Element.subclass({
        InputListenerNames: [ 'onBlur', 'onFocus', 'onChange' ],

        initialize: function(element) {
            this.element = element;
            var type = element.type.toLowerCase();
            this.isSubmitElement = type == 'submit' || type == 'image' || type == 'button';
        },

        isSubmit: function() {
            return this.isSubmitElement;
        },

        form: function() {
            return This.Element.adaptToElement(this.element.form);
        },

        focus: function() {
            var onFocusListener = this.element.onfocus;
            this.element.onfocus = Function.NOOP;
            this.element.focus();
            this.element.onfocus = onFocusListener;
        },

        replaceHostElementWith: function(newElement) {
            this.eachAttributeName(function(attributeName) {
                var newValue = newElement[attributeName]
                var oldValue = this.element[attributeName];
                if (oldValue != newValue) {
                    this.element[attributeName] = newValue;
                }
            }.bind(this));

            //'style' attribute special case
            var newStyle = newElement.getAttribute('style');
            var oldStyle = this.element.getAttribute('style');
            if (newStyle != oldStyle) {
                this.element.setAttribute('style', newStyle);
            }

            //overwrite listeners and bind them to the existing element
            this.eachListenerName(function(listenerName) {
                var name = listenerName.toLowerCase();
                this.element[name] = newElement[name] ? newElement[name].bind(this.element) : null;
                newElement[name] = null;
            }.bind(this));
        },

        eachAttributeName: function(iterator) {
            //core and i18n attributes (except 'id' and 'style' attributes)
            ['className', 'title', 'lang'].each(iterator);
            //input element attributes
            ['name', 'value', 'checked', 'disabled', 'readOnly',
                'size', 'maxLength', 'src', 'alt', 'useMap', 'isMap',
                'tabIndex', 'accessKey', 'accept'].each(iterator);
            //'dir' attribute cannot be updated dynamically in IE 7
            //'type' attribute cannot be updated dynamically in Firefox 2.0
        },

        serializeOn: function(query) {
            switch (this.element.type.toLowerCase()) {
                case 'image':
                case 'textarea':
                case 'submit':
                case 'hidden':
                case 'password':
                case 'text': query.add(this.element.name, this.element.value); break;
                case 'checkbox':
                case 'radio': if (this.element.checked) query.add(this.element.name, this.element.value || 'on'); break;
            }
        },

        eachListenerName: function(iterator) {
            this.MouseListenerNames.each(iterator);
            this.KeyListenerNames.each(iterator);
            this.InputListenerNames.each(iterator);
        }
    });

    This.SelectElement = This.InputElement.subclass({
        isSubmit: function() {
            return false;
        },

        replaceHostElementWith: function(newElement) {
            this.defaultReplaceHostElementWith(newElement);
        },

        serializeOn: function(query) {
            $enumerate(this.element.options).select(function(option) {
                return option.selected;
            }).each(function(selectedOption) {
                var value = selectedOption.value || (selectedOption.value == '' ? '' : selectedOption.text);
                query.add(this.element.name, value);
            }.bind(this));
        }
    });

    This.ButtonElement = This.InputElement.subclass({

        initialize: function(element) {
            this.element = element;
            this.isSubmitElement = element.type.toLowerCase() == 'submit';
        },

        isSubmit: function() {
            return this.isSubmitElement;
        },

        replaceHostElementWith: function(newElement) {
            this.defaultReplaceHostElementWith(newElement);
        },

        serializeOn: function(query) {
            query.add(this.element.name, this.element.value);
        }
    });

    This.FormElement = This.Element.subclass({
        FormListenerNames: [ 'onReset', 'onSubmit', 'submit' ],
        FormAttributeNames: [ 'acceptcharset', 'action', 'enctype', 'method', 'name', 'target' ],

        detectDefaultSubmit: function() {
            var formElements = this.element.elements;
            var length = formElements.length;
            var defaultID = this.element.id + ':default';

            for (var i = 0; i < length; i++) {
                var formElement = formElements[i];
                if (formElement.id == defaultID) {
                    return This.Element.adaptToElement(formElement);
                }
            }

            return null;
        },

        eachFormElement: /Safari/.test(navigator.userAgent) ? function(iterator) {
            //todo: find a more performant way to discard old form elements in Safari
            var newestElements = [];
            $enumerate(this.element.elements).reverse().each(function(element) {
                //Safari keeps old form elements around so they need to be discarded
                if (!newestElements.detect(function(newestElement) {
                    return element.id && newestElement.id && newestElement.id == element.id;
                })) {
                    newestElements.push(element);
                    iterator(This.Element.adaptToElement(element));
                }
            });
        } : function(iterator) {
            $enumerate(this.element.elements).each(function(e) {
                iterator(This.Element.adaptToElement(e));
            });
        },

        //captures normal form submit events and sends them through a XMLHttpRequest
        captureOnSubmit: function() {
            var previousOnSubmit = this.element.onsubmit;
            this.element.onsubmit = function(event) {
                if (previousOnSubmit) previousOnSubmit();
                $event(event).cancelDefaultAction();
                iceSubmit(this.element, null, event);
            };
        },

        //redirect normal form submits through a XMLHttpRequest
        redirectSubmit: function() {
            var previousSubmit = this.element.submit;
            this.element.submit = function() {
                if (previousSubmit) previousSubmit();
                iceSubmit(this.element, null, null);
            };
        },

        captureAndRedirectSubmit: function() {
            this.captureOnSubmit();
            this.redirectSubmit();
        },

        updateDOM: function(update) {
            this.disconnectAllListenersAndPeers();
            this.element.innerHTML = update.content();
            var remove = function(name) {
                this.element[name] = null;
            }.bind(this);
            this.FormAttributeNames.each(function(name) {
                this.element.removeAttribute(name);
            }.bind(this));
            this.eachListenerName(remove);
            update.eachAttribute(function(name, value) {
                try {
                    this.element.setAttribute(name, value);
                } catch (e) {
                    logger.error('failed to set attribute ' + name + ':' + value, e);
                }
            }.bind(this));
        },

        serializeOn: function(query) {
            this.eachFormElement(function(formElement) {
                if (!formElement.isSubmit()) formElement.serializeOn(query);
            });
        },

        eachListenerName: function(iterator) {
            this.MouseListenerNames.each(iterator);
            this.KeyListenerNames.each(iterator);
            this.FormListenerNames.each(iterator);
        }
    });


    This.BodyElement = This.Element.subclass({
        updateDOM: function(update) {
            this.disconnectAllListenersAndPeers();
            this.element.innerHTML = update.content();
        }
    });

    This.ScriptElement = This.Element.subclass({
        updateDOM: function(update) {
            //if script element is updated its code will be evaluate in IE (thus evaluating it twice)
            //evaluate code in the 'window' context
            var scriptCode = update.content();
            if (scriptCode != '' && scriptCode != ';') {
                var evalFunc = function() {
                    eval(scriptCode);
                };
                evalFunc.apply(window);
            }
        }
    });

    This.TitleElement = This.Element.subclass({
        updateDOM: function(update) {
            this.element.ownerDocument.title = update.content();
        }
    });

    This.AnchorElement = This.Element.subclass({
        isSubmit: function() {
            return true;
        },

        focus: function() {
            var onFocusListener = this.element.onfocus;
            this.element.onfocus = Function.NOOP;
            this.element.focus();
            this.element.onfocus = onFocusListener;
        },

        serializeOn: function(query) {
            if (this.element.name) query.add(this.element.name, this.element.name);
        },

        form: function() {
            var parent = this.element.parentNode;
            while (parent) {
                if (parent.tagName && parent.tagName.toLowerCase() == 'form') return This.Element.adaptToElement(parent);
                parent = parent.parentNode;
            }

            throw 'Cannot find enclosing form.';
        }
    });

    This.TableCellElement = This.Element.subclass({
        replaceHtml: function(html) {
            this.withTemporaryContainer(function(container) {
                container.innerHTML = '<TABLE>' + html + '</TABLE>';
                var newElement = container.firstChild;
                while ((null != newElement) && (this.element.id != newElement.id)) {
                    newElement = newElement.firstChild;
                }
                this.disconnectAllListenersAndPeers();
                this.replaceHostElementWith(newElement);
            });
        }
    });

    This.IFrameElement = This.Element.subclass({
        replaceHostElementWith: function(newElement) {
            this.eachAttributeName(function(attributeName) {
                var value = newElement.getAttribute(attributeName);
                if (value == null) {
                    this.element.removeAttribute(attributeName);
                } else {
                    this.element.setAttribute(attributeName, value);
                }
            }.bind(this));

            //special case for the 'src' attribute (Safari bug?)
            var oldLocation = this.element.contentWindow.location.href;
            var newLocation = newElement.contentWindow.location.href;
            if (oldLocation != newLocation) {
                this.element.contentWindow.location = newLocation;
            }

            //overwrite listeners and bind them to the existing element
            this.eachListenerName(function(listenerName) {
                var name = listenerName.toLowerCase();
                this.element[name] = newElement[name] ? newElement[name].bind(this.element) : null;
                newElement[name] = null;
            }.bind(this));
        },

        eachAttributeName: function(iterator) {
            ['title', 'lang', 'dir', 'class', 'style', 'align', 'frameborder',
                'width', 'height', 'hspace', 'ismap', 'longdesc', 'marginwidth',
                'marginheight', 'name', 'scrolling'].each(iterator);
        }
    });

    //public call
    window.$element = This.Element.adaptToElement;
});
