onLoad(window, function() {
    var container = document.body.appendChild(document.createElement('div'));
    container.style.visibility = 'hidden';
    container.style.display = 'none';

    window.HTMLParser = function(parserCallback) {
        parserCallback(function(html) {
            container.innerHTML = html;
            return container.childNodes;
        });
        container.innerHTML = '';
    };
});

var id = operator();
var tag = operator();
var property = operator();
var attribute = operator();
var parents = operator();
var enclosingForm = operator();
var enclosingBridge = operator();
var updateElement = operator();

function disconnectEventListeners(element) {
    each([
        'onkeydown', 'onkeypress', 'onkeyup', 'onhelp', 'onclick', 'ondblclick',
        'onmousedown', 'onmousemove', 'onmouseout', 'onmouseover', 'onmouseup',
        'onblur', 'onfocus', 'onchange', 'onreset', 'onsubmit'
    ], function(listenerName) {
        try {
            element[listenerName] = null;
        } catch (e) {
            //ignore
        }
    });
}

var attributes = operator();
var content = operator();
var asHTML = operator();

function Update(element) {
    var tag = element.getAttribute('tag');

    function appendStartTag(self, html) {
        append(html, '<');
        append(html, tag);
        each(attributes(self), function(attr) {
            append(html, ' ');
            append(html, key(attr));
            append(html, '="');
            append(html, value(attr));
            append(html, '"');
        });
        append(html, '>');
    }

    function appendEndTag(self, html) {
        append(html, '</');
        append(html, tag);
        append(html, '>');
    }

    return object(function(method) {
        method(attributes, function(self) {
            return collect(element.getElementsByTagName('*'), function(e) {
                return Cell(e.getAttribute('name'), attribute.firstChild ? attribute.firstChild.data : '');
            });
        });

        method(content, function(self) {
            var contentElement = element.getElementsByTagName('content')[0];
            return contentElement.firstChild ?
                   contentElement.firstChild.data.replace(/<\!\#cdata\#/g, '<![CDATA[').replace(/\#\#>/g, ']]>') : '';
        });

        method(asHTML, function(self) {
            var html = [];
            appendStartTag(self, html);
            append(html, content(self));
            appendEndTag(self, html);
            return join(html, '');
        });

        method(asString, function(self) {
            var html = [];
            appendStartTag(self, html);
            append(html, "...");
            appendEndTag(self, html);
            return join(html, '');
        });
    });
}

var focus = operator();

function Element(element) {
    return object(function(method) {
        method(id, function(self) {
            return element.id;
        });

        method(tag, function(self) {
            return toLowerCase(element.tagName);
        });

        method(property, function(self, name) {
            return element[name];
        });

        method(parents, function(self) {
            return Stream(function(cellConstructor) {
                function parentStream(e) {
                    if (e == null || e == document) return null;
                    return function() {
                        return cellConstructor($element(e), parentStream(e.parentNode));
                    };
                }

                return parentStream(element.parentNode);
            });
        });

        method(enclosingForm, function(self) {
            return detect(parents(self), function(e) {
                return tag(e) == 'form';
            }, function() {
                throw 'cannot find enclosing form';
            });
        });

        method(enclosingBridge, function(self) {
            return detect(parents(self), function(e) {
                return property(e, 'bridge') != null;
            }, function() {
                throw 'cannot find enclosing bridge';
            });
        });

        method(asString, function(self) {
            return asString(element);
        });

        method(updateElement, function(self, update) {
            HTMLParser(function(parse) {
                var newElement = parse(asHTML(update))[0];
                each(element.getElementsByTagName('*'), disconnectEventListeners);
                element.parentNode.replaceChild(newElement, element);
                element = newElement;
            });
        });

        method(serializeOn, noop);
    });
}

function InputElement(element) {
    return objectWithAncestors(function(method) {
        method(enclosingForm, function(self) {
            var f = element.form;
            if (f) {
                return $element(f);
            } else {
                throw 'cannot find enclosing form';
            }
        });

        method(focus, function(self) {
            var onFocusListener = element.onfocus;
            element.onfocus = noop;
            element.focus();
            element.onfocus = onFocusListener;
        });

        method(serializeOn, function(self, query) {
            switch (toLowerCase(element.type)) {
                case 'image':
                case 'textarea':
                case 'submit':
                case 'hidden':
                case 'password':
                case 'text': addNameValue(query, element.name, element.value); break;
                case 'checkbox':
                case 'radio': if (element.checked) addNameValue(query, element.name, element.value || 'on'); break;
            }
        });
    }, Element(element));
}

function $element(e) {
    //no polymophism here...'switch' is the way then.
    switch (toLowerCase(e.tagName)) {
        case 'textarea':
        case 'input': return InputElement(e);
        //            case 'th':
        //            case 'td':
        //            case 'tr': return TableCellElement(e);
        //            case 'button': return ButtonElement(e);
        //            case 'select': return SelectElement(e);
        //            case 'form': return FormElement(e);
        //            case 'body': return BodyElement(e);
        //            case 'script': return ScriptElement(e);
        //            case 'title': return TitleElement(e);
        //            case 'a': return AnchorElement(e);
        //            case 'iframe': return IFrameElement(e);
        default : return Element(e);
    }
}

function $elementWithID(id) {
    return $element(document.getElementById(id));
}