//update methods
var eachUpdateAttribute = operator();
var updateContent = operator();
var asHTML = operator();

function Update(element) {
    var tag = element.getAttribute('tag');

    function appendStartTag(self, html) {
        append(html, '<');
        append(html, tag);
        eachUpdateAttribute(self, function(name, value) {
            append(html, ' ');
            append(html, name);
            append(html, '="');
            append(html, value);
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
        method(eachUpdateAttribute, function(self, iterator) {
            each(element.getElementsByTagName('attribute'), function(e) {
                iterator(e.getAttribute('name'), e.firstChild ? e.firstChild.data : '');
            });
        });

        method(updateContent, function(self) {
            var contentElement = element.getElementsByTagName('content')[0];
            return contentElement.firstChild ?
                   contentElement.firstChild.data.replace(/<\!\#cdata\#/g, '<![CDATA[').replace(/\#\#>/g, ']]>') : '';
        });

        method(asHTML, function(self) {
            var html = [];
            appendStartTag(self, html);
            append(html, updateContent(self));
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

//element methods
var identifier = operator();
var enclosingForm = operator();
var enclosingBridge = operator();
var gainFocus = operator();
var canSubmitForm = operator();
//form methods
var detectDefaultSubmit = operator();
var captureAndRedirectSubmit = operator();
var submit = operator();

//DOM patching methods
var updateElement = operator();
var removeElement = operator();

var $element;
var $elementWithID;

(function() {
    var HTMLParser;
    onLoad(window, function() {
        var container = document.body.appendChild(document.createElement('div'));
        container.style.visibility = 'hidden';
        container.style.display = 'none';

        HTMLParser = function(parserCallback) {
            parserCallback(function(html) {
                container.innerHTML = html;
                return container.childNodes;
            });
            container.innerHTML = '';
        };
    });
    var EvaluateScripts;
    onLoad(window, function() {
        //the scripts present on the page are already evaluated
        var referencedScripts = asArray(select(document.documentElement.getElementsByTagName('script'), function(script) {
            return script.src;
        }));
        var client = Client(this.logger);

        function evaluateScript(script) {
            var uri = script.src;
            if (uri) {
                if (!contains(referencedScripts, script.src)) {
                    getSynchronously(client, uri, noop, noop, $witch(function(condition) {
                        condition(OK, function(response) {
                            append(referencedScripts, uri);
                            try {
                                evaluate(contentAsText(response));
                            } catch (e) {
                                throw 'Failed to evaluate script located at: ' + uri;
                            }
                        });
                    }));
                }
            } else {
                var code = script.innerHTML;
                try {
                    evaluate(code);
                } catch (e) {
                    throw 'Failed to evaluate script: \n' + code;
                }
            }
        }

        EvaluateScripts = function(element) {
            each(element.getElementsByTagName('script'), evaluateScript);
        };
    });

    var EventNames = [
        'onkeydown', 'onkeypress', 'onkeyup', 'onhelp', 'onclick', 'ondblclick', 'onmousedown', 'onmousemove',
        'onmouseout', 'onmouseover', 'onmouseup', 'onblur', 'onfocus', 'onchange', 'onreset', 'onsubmit'
    ];

    var tag = operator();
    var property = operator();
    var parents = operator();
    var disconnectListeners = operator();

    function disconnectEventListeners(element) {
        each(EventNames, function(listenerName) {
            try {
                element[listenerName] = null;
            } catch (e) {
                //ignore
            }
        });
    }

    function DefaultElement(element) {
        return object(function(method) {
            method(identifier, function(self) {
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
                return property(detect(parents(self), function(e) {
                    return property(e, 'bridge') != null;
                }, function() {
                    throw 'cannot find enclosing bridge';
                }), 'bridge');
            });

            method(updateElement, function(self, update) {
                HTMLParser(function(parse) {
                    var newElement = parse(asHTML(update))[0];
                    disconnectListeners(self);
                    element.parentNode.replaceChild(newElement, element);
                    element = newElement;
                });
                EvaluateScripts(element);
            });

            method(disconnectListeners, function(self) {
                disconnectEventListeners(element);
                each(element.getElementsByTagName('*'), disconnectEventListeners);
            });

            method(submit, function(self) {
                var query = Query();
                serializeOn(self, query);
                send(namespace.connection(enclosingBridge(self)), query);
            });

            method(serializeOn, noop);

            method(canSubmitForm, none);

            method(asString, function(self) {
                return asString(element);
            });
        });
    }

    function Focusable(method, element) {
        method(gainFocus, function(self) {
            var onFocusListener = element.onfocus;
            element.onfocus = noop;
            element.focus();
            element.onfocus = onFocusListener;
        });
    }

    function InputElement(element) {
        return objectWithAncestors(function(method) {
            Focusable(method, element);

            method(enclosingForm, function(self) {
                var f = element.form;
                if (f) {
                    return FormElement(f);
                } else {
                    throw 'cannot find enclosing form';
                }
            });

            method(canSubmitForm, function(self) {
                var type = toLowerCase(element.type);
                return type == 'submit' || type == 'image' || type == 'button';
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
        }, DefaultElement(element));
    }

    function SelectElement(element) {
        return objectWithAncestors(function(method) {
            method(canSubmitForm, none);

            method(serializeOn, function(self, query) {
                each(select(element.options, function(option) {
                    return option.selected;
                }), function(selectedOption) {
                    var value = selectedOption.value || (selectedOption.value == '' ? '' : selectedOption.text);
                    addNameValue(query, element.name, value);
                });
            });
        }, InputElement(element));
    }

    function ButtonElement(element) {
        return objectWithAncestors(function(method) {
            method(canSubmitForm, function(self) {
                return toLowerCase(element.type) == 'submit';
            });

            method(serializeOn, function(self, query) {
                addNameValue(query, element.name, element.value);
            });
        }, InputElement(element));
    }

    function AnchorElement(element) {
        return objectWithAncestors(function(method) {
            Focusable(method, element);

            method(canSubmitForm, any);

            method(serializeOn, function(self, query) {
                var name = element.name;
                if (name) addNameValue(query, name, name);
            });
        }, DefaultElement(element));
    }

    function FormElement(element) {
        return objectWithAncestors(function(method) {
            method(enclosingForm, function(self) {
                return self;
            });

            //todo: this method should go away since it encourages special treatment for elements with ID ending in ':default'
            method(detectDefaultSubmit, function(self) {
                var defaultID = element.id + ':default';
                return $element(detect(element.elements, function(e) {
                    e.id = defaultID;
                }, function() {
                    throw 'cannot find default submit';
                }));
            });

            method(captureAndRedirectSubmit, function(self) {
                //captures normal form submit events and sends them through a XMLHttpRequest
                var previousOnSubmit = element.onsubmit;
                element.onsubmit = function(event) {
                    if (previousOnSubmit) previousOnSubmit();
                    submit(self);
                };

                element.submit = function() {
                    element.onsubmit = noop;
                    submit(self);
                };
            });

            method(updateElement, function(self, update) {
                disconnectListeners(self);
                element.innerHTML = updateContent(update);
                each(['acceptcharset', 'action', 'enctype', 'method', 'name', 'target'], function(name) {
                    element.removeAttribute(name);
                });
                eachUpdateAttribute(update, function(name, value) {
                    try {
                        element.setAttribute(name, value);
                    } catch (e) {
                        error(logger, 'failed to set attribute ' + name + ':' + value, e);
                    }
                });
                EvaluateScripts(element);
            });

            method(serializeOn, function(self, query) {
                each(collect(element.elements, $element), function(e) {
                    serializeOn(e, query);
                });
            });
        }, DefaultElement(element));
    }

    function ScriptElement(element) {
        return objectWithAncestors(function(method) {
            method(updateElement, function(self, update) {
                //if script element is updated its code will be evaluate in IE (thus evaluating it twice)
                //evaluate code in the 'window' context
                var scriptCode = updateContent(update);
                if (scriptCode != '' && scriptCode != ';') {
                    var evalFunc = function() {
                        evaluate(scriptCode);
                    };
                    evalFunc.apply(window);
                }
            });
        }, DefaultElement(element));
    }

    function TitleElement(element) {
        return objectWithAncestors(function(method) {
            method(updateElement, function(self, update) {
                element.ownerDocument.title = updateContent(update);
            });
        }, DefaultElement(element));
    }

    function TableCellElement(element) {
        return objectWithAncestors(function(method) {
            method(updateElement, function(self, update) {
                HTMLParser(function(parse) {
                    var newElement = parse('<TABLE>' + asHTML(update) + '</TABLE>')[0];
                    disconnectListeners(self);
                    while ((null != newElement) && (element.id != newElement.id)) {
                        newElement = newElement.firstChild;
                    }
                    element.parentNode.replaceChild(newElement, element);
                    element = newElement;
                });
                EvaluateScripts(element);
            });
        }, DefaultElement(element));
    }

    function BodyElement(element) {
        return objectWithAncestors(function(method) {
            method(updateElement, function(self, update) {
                disconnectListeners(self);
                //strip <noscript> tag to fix Safari bug
                // #3131 If this is a response from an error code, there may not be a <noscript> tag.
                var html = updateContent(update);
                var start = new RegExp('\<noscript\>', 'g').exec(html);
                if (start == null) {
                    element.innerHTML = html;
                } else {
                    var end = new RegExp('\<\/noscript\>', 'g').exec(html);
                    element.innerHTML = substring(html, 0, start.index) + substring(html, end.index + 11, html.length);
                }
                EvaluateScripts(element);
            });
        }, DefaultElement(element));
    }

    function IFrameElement(element) {
        return objectWithAncestors(function(method) {
            method(updateElement, function(self, update) {
                HTMLParser(function(parse) {
                    var newElement = parse(asHTML(update))[0];

                    each(['title', 'lang', 'dir', 'class', 'style', 'align', 'frameborder',
                        'width', 'height', 'hspace', 'ismap', 'longdesc', 'marginwidth',
                        'marginheight', 'name', 'scrolling'], function(attributeName) {
                        var value = newElement.getAttribute(attributeName);
                        if (value == null) {
                            element.removeAttribute(attributeName);
                        } else {
                            element.setAttribute(attributeName, value);
                        }
                    });

                    //special case for the 'src' attribute (Safari bug?)
                    var oldLocation = element.contentWindow.location.href;
                    var newLocation = newElement.contentWindow.location.href;
                    if (oldLocation != newLocation) {
                        element.contentWindow.location = newLocation;
                    }

                    //overwrite listeners and bind them to the existing element
                    each(EventNames, function(name) {
                        element[name] = newElement[name] ? newElement[name] : null;
                        newElement[name] = null;
                    });
                });
                EvaluateScripts(element);
            });
        }, DefaultElement(element));
    }

    $element = function(e) {
        if (!e) return null;
        //no polymophism here...'switch' is the way then.
        switch (toLowerCase(e.tagName)) {
            case 'textarea':
            case 'input': return InputElement(e);
            case 'th':
            case 'td':
            case 'tr': return TableCellElement(e);
            case 'button': return ButtonElement(e);
            case 'select': return SelectElement(e);
            case 'form': return FormElement(e);
            case 'body': return BodyElement(e);
            case 'script': return ScriptElement(e);
            case 'title': return TitleElement(e);
            case 'a': return AnchorElement(e);
            case 'iframe': return IFrameElement(e);
            default : return DefaultElement(e);
        }
    };

    $elementWithID = function(id) {
        return $element(document.getElementById(id));
    };
})();