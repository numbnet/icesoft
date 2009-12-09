var Ajax = {
    getTransport: function() {
        return Try.these(
                function() {
                    return new XMLHttpRequest()
                },
                function() {
                    return new ActiveXObject('Msxml2.XMLHTTP')
                },
                function() {
                    return new ActiveXObject('Microsoft.XMLHTTP')
                }
                ) || false;
    },

    activeRequestCount: 0
};

Ajax.Responders = {
    responders: [],

    _each: function(iterator) {
        this.responders._each(iterator);
    },

    register: function(responder) {
        if (!this.include(responder))
            this.responders.push(responder);
    },

    unregister: function(responder) {
        this.responders = this.responders.without(responder);
    },

    dispatch: function(callback, request, transport, json) {
        this.each(function(responder) {
            if (Object.isFunction(responder[callback])) {
                try {
                    responder[callback].apply(responder, [request, transport, json]);
                } catch (e) {
                }
            }
        });
    }
};

Object.extend(Ajax.Responders, Enumerable);

Ajax.Responders.register({
    onCreate:   function() {
        Ajax.activeRequestCount++
    },
    onComplete: function() {
        Ajax.activeRequestCount--
    }
});

Ajax.Base = Class.create({
    initialize: function(options) {
        this.options = {
            method:       'post',
            asynchronous: true,
            contentType:  'application/x-www-form-urlencoded',
            encoding:     'UTF-8',
            parameters:   '',
            evalJSON:     true,
            evalJS:       true
        };
        Object.extend(this.options, options || { });

        this.options.method = this.options.method.toLowerCase();

        if (Object.isString(this.options.parameters))
            this.options.parameters = this.options.parameters.toQueryParams();
        else if (Object.isHash(this.options.parameters))
            this.options.parameters = this.options.parameters.toObject();
    }
});

Ajax.Request = Class.create(Ajax.Base, {
    _complete: false,

    initialize: function($super, url, options) {
        $super(options);
        this.transport = Ajax.getTransport();
        this.request(url);
    },

    request: function(url) {
        this.url = url;
        this.method = this.options.method;
        var params = Object.clone(this.options.parameters);

        if (!['get', 'post'].include(this.method)) {
            // simulate other verbs over post
            params['_method'] = this.method;
            this.method = 'post';
        }

        this.parameters = params;

        if (params = Object.toQueryString(params)) {
            // when GET, append parameters to URL
            if (this.method == 'get')
                this.url += (this.url.include('?') ? '&' : '?') + params;
            else if (/Konqueror|Safari|KHTML/.test(navigator.userAgent))
                params += '&_=';
        }

        try {
            var response = new Ajax.Response(this);
            if (this.options.onCreate) this.options.onCreate(response);
            Ajax.Responders.dispatch('onCreate', this, response);

            this.transport.open(this.method.toUpperCase(), this.url,
                    this.options.asynchronous);

            if (this.options.asynchronous) this.respondToReadyState.bind(this).defer(1);

            this.transport.onreadystatechange = this.onStateChange.bind(this);
            this.setRequestHeaders();

            this.body = this.method == 'post' ? (this.options.postBody || params) : null;
            this.transport.send(this.body);

            /* Force Firefox to handle ready state 4 for synchronous requests */
            if (!this.options.asynchronous && this.transport.overrideMimeType)
                this.onStateChange();

        }
        catch (e) {
            this.dispatchException(e);
        }
    },

    onStateChange: function() {
        var readyState = this.transport.readyState;
        if (readyState > 1 && !((readyState == 4) && this._complete))
            this.respondToReadyState(this.transport.readyState);
    },

    setRequestHeaders: function() {
        var headers = {
            'X-Requested-With': 'XMLHttpRequest',
            'X-Prototype-Version': Prototype.Version,
            'Accept': 'text/javascript, text/html, application/xml, text/xml, */*'
        };

        if (this.method == 'post') {
            headers['Content-type'] = this.options.contentType +
                                      (this.options.encoding ? '; charset=' + this.options.encoding : '');

            /* Force "Connection: close" for older Mozilla browsers to work
             * around a bug where XMLHttpRequest sends an incorrect
             * Content-length header. See Mozilla Bugzilla #246651.
             */
            if (this.transport.overrideMimeType &&
                (navigator.userAgent.match(/Gecko\/(\d{4})/) || [0,2005])[1] < 2005)
                headers['Connection'] = 'close';
        }

        // user-defined headers
        if (typeof this.options.requestHeaders == 'object') {
            var extras = this.options.requestHeaders;

            if (Object.isFunction(extras.push))
                for (var i = 0, length = extras.length; i < length; i += 2)
                    headers[extras[i]] = extras[i + 1];
            else
                $H(extras).each(function(pair) {
                    headers[pair.key] = pair.value
                });
        }

        for (var name in headers)
            this.transport.setRequestHeader(name, headers[name]);
    },

    success: function() {
        var status = this.getStatus();
        return !status || (status >= 200 && status < 300);
    },

    getStatus: function() {
        try {
            return this.transport.status || 0;
        } catch (e) {
            return 0
        }
    },

    respondToReadyState: function(readyState) {
        var state = Ajax.Request.Events[readyState], response = new Ajax.Response(this);

        if (state == 'Complete') {
            try {
                this._complete = true;
                (this.options['on' + response.status]
                        || this.options['on' + (this.success() ? 'Success' : 'Failure')]
                        || Prototype.emptyFunction)(response, response.headerJSON);
            } catch (e) {
                this.dispatchException(e);
            }

            var contentType = response.getHeader('Content-type');
            if (this.options.evalJS == 'force'
                    || (this.options.evalJS && contentType
                    && contentType.match(/^\s*(text|application)\/(x-)?(java|ecma)script(;.*)?\s*$/i)))
                this.evalResponse();
        }

        try {
            (this.options['on' + state] || Prototype.emptyFunction)(response, response.headerJSON);
            Ajax.Responders.dispatch('on' + state, this, response, response.headerJSON);
        } catch (e) {
            this.dispatchException(e);
        }

        if (state == 'Complete') {
            // avoid memory leak in MSIE: clean up
            this.transport.onreadystatechange = Prototype.emptyFunction;
        }
    },

    getHeader: function(name) {
        try {
            return this.transport.getResponseHeader(name);
        } catch (e) {
            return null
        }
    },

    evalResponse: function() {
        try {
            return eval((this.transport.responseText || '').unfilterJSON());
        } catch (e) {
            this.dispatchException(e);
        }
    },

    dispatchException: function(exception) {
        (this.options.onException || Prototype.emptyFunction)(this, exception);
        Ajax.Responders.dispatch('onException', this, exception);
    }
});

Ajax.Request.Events =
['Uninitialized', 'Loading', 'Loaded', 'Interactive', 'Complete'];

Ajax.Response = Class.create({
    initialize: function(request) {
        this.request = request;
        var transport = this.transport = request.transport,
                readyState = this.readyState = transport.readyState;

        if ((readyState > 2 && !Prototype.Browser.IE) || readyState == 4) {
            this.status = this.getStatus();
            this.statusText = this.getStatusText();
            this.responseText = String.interpret(transport.responseText);
            this.headerJSON = this._getHeaderJSON();
        }

        if (readyState == 4) {
            var xml = transport.responseXML;
            this.responseXML = Object.isUndefined(xml) ? null : xml;
            this.responseJSON = this._getResponseJSON();
        }
    },

    status:      0,
    statusText: '',

    getStatus: Ajax.Request.prototype.getStatus,

    getStatusText: function() {
        try {
            return this.transport.statusText || '';
        } catch (e) {
            return ''
        }
    },

    getHeader: Ajax.Request.prototype.getHeader,

    getAllHeaders: function() {
        try {
            return this.getAllResponseHeaders();
        } catch (e) {
            return null
        }
    },

    getResponseHeader: function(name) {
        return this.transport.getResponseHeader(name);
    },

    getAllResponseHeaders: function() {
        return this.transport.getAllResponseHeaders();
    },

    _getHeaderJSON: function() {
        var json = this.getHeader('X-JSON');
        if (!json) return null;
        json = decodeURIComponent(escape(json));
        try {
            return json.evalJSON(this.request.options.sanitizeJSON);
        } catch (e) {
            this.request.dispatchException(e);
        }
    },

    _getResponseJSON: function() {
        var options = this.request.options;
        if (!options.evalJSON || (options.evalJSON != 'force' &&
                                  !(this.getHeader('Content-type') || '').include('application/json')) ||
            this.responseText.blank())
            return null;
        try {
            return this.responseText.evalJSON(options.sanitizeJSON);
        } catch (e) {
            this.request.dispatchException(e);
        }
    }
});

Ajax.Updater = Class.create(Ajax.Request, {
    initialize: function($super, container, url, options) {
        this.container = {
            success: (container.success || container),
            failure: (container.failure || (container.success ? null : container))
        };

        options = Object.clone(options);
        var onComplete = options.onComplete;
        options.onComplete = (function(response, json) {
            this.updateContent(response.responseText);
            if (Object.isFunction(onComplete)) onComplete(response, json);
        }).bind(this);

        $super(url, options);
    },

    updateContent: function(responseText) {
        var receiver = this.container[this.success() ? 'success' : 'failure'],
                options = this.options;

        if (!options.evalScripts) responseText = responseText.stripScripts();

        if (receiver = $(receiver)) {
            if (options.insertion) {
                if (Object.isString(options.insertion)) {
                    var insertion = { };
                    insertion[options.insertion] = responseText;
                    receiver.insert(insertion);
                }
                else options.insertion(receiver, responseText);
            }
            else receiver.update(responseText);
        }
    }
});

Ajax.PeriodicalUpdater = Class.create(Ajax.Base, {
    initialize: function($super, container, url, options) {
        $super(options);
        this.onComplete = this.options.onComplete;

        this.frequency = (this.options.frequency || 2);
        this.decay = (this.options.decay || 1);

        this.updater = { };
        this.container = container;
        this.url = url;

        this.start();
    },

    start: function() {
        this.options.onComplete = this.updateComplete.bind(this);
        this.onTimerEvent();
    },

    stop: function() {
        this.updater.options.onComplete = undefined;
        clearTimeout(this.timer);
        (this.onComplete || Prototype.emptyFunction).apply(this, arguments);
    },

    updateComplete: function(response) {
        if (this.options.decay) {
            this.decay = (response.responseText == this.lastText ?
                          this.decay * this.options.decay : 1);

            this.lastText = response.responseText;
        }
        this.timer = this.onTimerEvent.bind(this).delay(this.decay * this.frequency);
    },

    onTimerEvent: function() {
        this.updater = new Ajax.Updater(this.container, this.url, this.options);
    }
});
function $(element) {
    if (arguments.length > 1) {
        for (var i = 0, elements = [], length = arguments.length; i < length; i++)
            elements.push($(arguments[i]));
        return elements;
    }
    if (Object.isString(element))
        element = document.getElementById(element);
    return Element.extend(element);
}

if (Prototype.BrowserFeatures.XPath) {
    document._getElementsByXPath = function(expression, parentElement) {
        var results = [];
        var query = document.evaluate(expression, $(parentElement) || document,
                null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
        for (var i = 0, length = query.snapshotLength; i < length; i++)
            results.push(Element.extend(query.snapshotItem(i)));
        return results;
    };
}

/*--------------------------------------------------------------------------*/

if (!window.Node) var Node = { };

if (!Node.ELEMENT_NODE) {
    // DOM level 2 ECMAScript Language Binding
    Object.extend(Node, {
        ELEMENT_NODE: 1,
        ATTRIBUTE_NODE: 2,
        TEXT_NODE: 3,
        CDATA_SECTION_NODE: 4,
        ENTITY_REFERENCE_NODE: 5,
        ENTITY_NODE: 6,
        PROCESSING_INSTRUCTION_NODE: 7,
        COMMENT_NODE: 8,
        DOCUMENT_NODE: 9,
        DOCUMENT_TYPE_NODE: 10,
        DOCUMENT_FRAGMENT_NODE: 11,
        NOTATION_NODE: 12
    });
}

(function() {
    var element = this.Element;
    this.Element = function(tagName, attributes) {
        attributes = attributes || { };
        tagName = tagName.toLowerCase();
        var cache = Element.cache;
        if (Prototype.Browser.IE && attributes.name) {
            tagName = '<' + tagName + ' name="' + attributes.name + '">';
            delete attributes.name;
            return Element.writeAttribute(document.createElement(tagName), attributes);
        }
        if (!cache[tagName]) cache[tagName] = Element.extend(document.createElement(tagName));
        return Element.writeAttribute(cache[tagName].cloneNode(false), attributes);
    };
    Object.extend(this.Element, element || { });
}).call(window);

Element.cache = { };

Element.Methods = {
    visible: function(element) {
        return $(element).style.display != 'none';
    },

    toggle: function(element) {
        element = $(element);
        Element[Element.visible(element) ? 'hide' : 'show'](element);
        return element;
    },

    hide: function(element) {
        $(element).style.display = 'none';
        return element;
    },

    show: function(element) {
        $(element).style.display = '';
        return element;
    },

    remove: function(element) {
        element = $(element);
        element.parentNode.removeChild(element);
        return element;
    },

    update: function(element, content) {
        element = $(element);
        if (content && content.toElement) content = content.toElement();
        if (Object.isElement(content)) return element.update().insert(content);
        content = Object.toHTML(content);
        element.innerHTML = content.stripScripts();
        content.evalScripts.bind(content).defer();
        return element;
    },

    replace: function(element, content) {
        element = $(element);
        if (content && content.toElement) content = content.toElement();
        else if (!Object.isElement(content)) {
            content = Object.toHTML(content);
            var range = element.ownerDocument.createRange();
            range.selectNode(element);
            content.evalScripts.bind(content).defer();
            content = range.createContextualFragment(content.stripScripts());
        }
        element.parentNode.replaceChild(content, element);
        return element;
    },

    insert: function(element, insertions) {
        element = $(element);

        if (Object.isString(insertions) || Object.isNumber(insertions) ||
            Object.isElement(insertions) || (insertions && (insertions.toElement || insertions.toHTML)))
            insertions = {bottom:insertions};

        var content, t, range;

        for (position in insertions) {
            content = insertions[position];
            position = position.toLowerCase();
            t = Element._insertionTranslations[position];

            if (content && content.toElement) content = content.toElement();
            if (Object.isElement(content)) {
                t.insert(element, content);
                continue;
            }

            content = Object.toHTML(content);

            range = element.ownerDocument.createRange();
            t.initializeRange(element, range);
            t.insert(element, range.createContextualFragment(content.stripScripts()));

            content.evalScripts.bind(content).defer();
        }

        return element;
    },

    wrap: function(element, wrapper, attributes) {
        element = $(element);
        if (Object.isElement(wrapper))
            $(wrapper).writeAttribute(attributes || { });
        else if (Object.isString(wrapper)) wrapper = new Element(wrapper, attributes);
        else wrapper = new Element('div', wrapper);
        if (element.parentNode)
            element.parentNode.replaceChild(wrapper, element);
        wrapper.appendChild(element);
        return wrapper;
    },

    inspect: function(element) {
        element = $(element);
        var result = '<' + element.tagName.toLowerCase();
        $H({'id': 'id', 'className': 'class'}).each(function(pair) {
            var property = pair.first(), attribute = pair.last();
            var value = (element[property] || '').toString();
            if (value) result += ' ' + attribute + '=' + value.inspect(true);
        });
        return result + '>';
    },

    recursivelyCollect: function(element, property) {
        element = $(element);
        var elements = [];
        while (element = element[property])
            if (element.nodeType == 1)
                elements.push(Element.extend(element));
        return elements;
    },

    ancestors: function(element) {
        return $(element).recursivelyCollect('parentNode');
    },

    descendants: function(element) {
        return $(element).getElementsBySelector("*");
    },

    firstDescendant: function(element) {
        element = $(element).firstChild;
        while (element && element.nodeType != 1) element = element.nextSibling;
        return $(element);
    },

    immediateDescendants: function(element) {
        if (!(element = $(element).firstChild)) return [];
        while (element && element.nodeType != 1) element = element.nextSibling;
        if (element) return [element].concat($(element).nextSiblings());
        return [];
    },

    previousSiblings: function(element) {
        return $(element).recursivelyCollect('previousSibling');
    },

    nextSiblings: function(element) {
        return $(element).recursivelyCollect('nextSibling');
    },

    siblings: function(element) {
        element = $(element);
        return element.previousSiblings().reverse().concat(element.nextSiblings());
    },

    match: function(element, selector) {
        if (Object.isString(selector))
            selector = new Selector(selector);
        return selector.match($(element));
    },

    up: function(element, expression, index) {
        element = $(element);
        if (arguments.length == 1) return $(element.parentNode);
        var ancestors = element.ancestors();
        return expression ? Selector.findElement(ancestors, expression, index) :
               ancestors[index || 0];
    },

    down: function(element, expression, index) {
        element = $(element);
        if (arguments.length == 1) return element.firstDescendant();
        var descendants = element.descendants();
        return expression ? Selector.findElement(descendants, expression, index) :
               descendants[index || 0];
    },

    previous: function(element, expression, index) {
        element = $(element);
        if (arguments.length == 1) return $(Selector.handlers.previousElementSibling(element));
        var previousSiblings = element.previousSiblings();
        return expression ? Selector.findElement(previousSiblings, expression, index) :
               previousSiblings[index || 0];
    },

    next: function(element, expression, index) {
        element = $(element);
        if (arguments.length == 1) return $(Selector.handlers.nextElementSibling(element));
        var nextSiblings = element.nextSiblings();
        return expression ? Selector.findElement(nextSiblings, expression, index) :
               nextSiblings[index || 0];
    },

    select: function() {
        var args = $A(arguments), element = $(args.shift());
        return Selector.findChildElements(element, args);
    },

    adjacent: function() {
        var args = $A(arguments), element = $(args.shift());
        return Selector.findChildElements(element.parentNode, args).without(element);
    },

    identify: function(element) {
        element = $(element);
        var id = element.readAttribute('id'), self = arguments.callee;
        if (id) return id;
        do {
            id = 'anonymous_element_' + self.counter++
        } while ($(id));
        element.writeAttribute('id', id);
        return id;
    },

    readAttribute: function(element, name) {
        element = $(element);
        if (Prototype.Browser.IE) {
            var t = Element._attributeTranslations.read;
            if (t.values[name]) return t.values[name](element, name);
            if (t.names[name]) name = t.names[name];
            if (name.include(':')) {
                return (!element.attributes || !element.attributes[name]) ? null :
                       element.attributes[name].value;
            }
        }
        return element.getAttribute(name);
    },

    writeAttribute: function(element, name, value) {
        element = $(element);
        var attributes = { }, t = Element._attributeTranslations.write;

        if (typeof name == 'object') attributes = name;
        else attributes[name] = Object.isUndefined(value) ? true : value;

        for (var attr in attributes) {
            name = t.names[attr] || attr;
            value = attributes[attr];
            if (t.values[attr]) name = t.values[attr](element, value);
            if (value === false || value === null)
                element.removeAttribute(name);
            else if (value === true)
                element.setAttribute(name, name);
            else element.setAttribute(name, value);
        }
        return element;
    },

    getHeight: function(element) {
        return $(element).getDimensions().height;
    },

    getWidth: function(element) {
        return $(element).getDimensions().width;
    },

    classNames: function(element) {
        return new Element.ClassNames(element);
    },

    hasClassName: function(element, className) {
        if (!(element = $(element))) return;
        var elementClassName = element.className;
        return (elementClassName.length > 0 && (elementClassName == className ||
                                                new RegExp("(^|\\s)" + className + "(\\s|$)").test(elementClassName)));
    },

    addClassName: function(element, className) {
        if (!(element = $(element))) return;
        if (!element.hasClassName(className))
            element.className += (element.className ? ' ' : '') + className;
        return element;
    },

    removeClassName: function(element, className) {
        if (!(element = $(element))) return;
        element.className = element.className.replace(
                new RegExp("(^|\\s+)" + className + "(\\s+|$)"), ' ').strip();
        return element;
    },

    toggleClassName: function(element, className) {
        if (!(element = $(element))) return;
        return element[element.hasClassName(className) ?
                       'removeClassName' : 'addClassName'](className);
    },

    // removes whitespace-only text node children
    cleanWhitespace: function(element) {
        element = $(element);
        var node = element.firstChild;
        while (node) {
            var nextNode = node.nextSibling;
            if (node.nodeType == 3 && !/\S/.test(node.nodeValue))
                element.removeChild(node);
            node = nextNode;
        }
        return element;
    },

    empty: function(element) {
        return $(element).innerHTML.blank();
    },

    descendantOf: function(element, ancestor) {
        element = $(element),ancestor = $(ancestor);
        var originalAncestor = ancestor;

        if (element.compareDocumentPosition)
            return (element.compareDocumentPosition(ancestor) & 8) === 8;

        if (element.sourceIndex && !Prototype.Browser.Opera) {
            var e = element.sourceIndex, a = ancestor.sourceIndex,
                    nextAncestor = ancestor.nextSibling;
            if (!nextAncestor) {
                do {
                    ancestor = ancestor.parentNode;
                }
                while (!(nextAncestor = ancestor.nextSibling) && ancestor.parentNode);
            }
            if (nextAncestor) return (e > a && e < nextAncestor.sourceIndex);
        }

        while (element = element.parentNode)
            if (element == originalAncestor) return true;
        return false;
    },

    scrollTo: function(element) {
        element = $(element);
        var pos = element.cumulativeOffset();
        window.scrollTo(pos[0], pos[1]);
        return element;
    },

    getStyle: function(element, style) {
        element = $(element);
        style = style == 'float' ? 'cssFloat' : style.camelize();
        var value = element.style[style];
        if (!value) {
            var css = document.defaultView.getComputedStyle(element, null);
            value = css ? css[style] : null;
        }
        if (style == 'opacity') return value ? parseFloat(value) : 1.0;
        return value == 'auto' ? null : value;
    },

    getOpacity: function(element) {
        return $(element).getStyle('opacity');
    },

    setStyle: function(element, styles) {
        element = $(element);
        var elementStyle = element.style, match;
        if (Object.isString(styles)) {
            element.style.cssText += ';' + styles;
            return styles.include('opacity') ?
                   element.setOpacity(styles.match(/opacity:\s*(\d?\.?\d*)/)[1]) : element;
        }
        for (var property in styles)
            if (property == 'opacity') element.setOpacity(styles[property]);
            else
                elementStyle[(property == 'float' || property == 'cssFloat') ?
                             (Object.isUndefined(elementStyle.styleFloat) ? 'cssFloat' : 'styleFloat') :
                             property] = styles[property];

        return element;
    },

    setOpacity: function(element, value) {
        element = $(element);
        element.style.opacity = (value == 1 || value === '') ? '' :
                                (value < 0.00001) ? 0 : value;
        return element;
    },

    getDimensions: function(element) {
        element = $(element);
        var display = $(element).getStyle('display');
        if (display != 'none' && display != null) // Safari bug
            return {width: element.offsetWidth, height: element.offsetHeight};

        // All *Width and *Height properties give 0 on elements with display none,
        // so enable the element temporarily
        var els = element.style;
        var originalVisibility = els.visibility;
        var originalPosition = els.position;
        var originalDisplay = els.display;
        els.visibility = 'hidden';
        els.position = 'absolute';
        els.display = 'block';
        var originalWidth = element.clientWidth;
        var originalHeight = element.clientHeight;
        els.display = originalDisplay;
        els.position = originalPosition;
        els.visibility = originalVisibility;
        return {width: originalWidth, height: originalHeight};
    },

    makePositioned: function(element) {
        element = $(element);
        var pos = Element.getStyle(element, 'position');
        if (pos == 'static' || !pos) {
            element._madePositioned = true;
            element.style.position = 'relative';
            // Opera returns the offset relative to the positioning context, when an
            // element is position relative but top and left have not been defined
            if (window.opera) {
                element.style.top = 0;
                element.style.left = 0;
            }
        }
        return element;
    },

    undoPositioned: function(element) {
        element = $(element);
        if (element._madePositioned) {
            element._madePositioned = undefined;
            element.style.position =
            element.style.top =
            element.style.left =
            element.style.bottom =
            element.style.right = '';
        }
        return element;
    },

    makeClipping: function(element) {
        element = $(element);
        if (element._overflow) return element;
        element._overflow = Element.getStyle(element, 'overflow') || 'auto';
        if (element._overflow !== 'hidden')
            element.style.overflow = 'hidden';
        return element;
    },

    undoClipping: function(element) {
        element = $(element);
        if (!element._overflow) return element;
        element.style.overflow = element._overflow == 'auto' ? '' : element._overflow;
        element._overflow = null;
        return element;
    },

    cumulativeOffset: function(element) {
        var valueT = 0, valueL = 0;
        do {
            valueT += element.offsetTop || 0;
            valueL += element.offsetLeft || 0;
            element = element.offsetParent;
        } while (element);
        return Element._returnOffset(valueL, valueT);
    },

    positionedOffset: function(element) {
        var valueT = 0, valueL = 0;
        do {
            valueT += element.offsetTop || 0;
            valueL += element.offsetLeft || 0;
            element = element.offsetParent;
            if (element) {
                if (element.tagName == 'BODY') break;
                var p = Element.getStyle(element, 'position');
                if (p == 'relative' || p == 'absolute') break;
            }
        } while (element);
        return Element._returnOffset(valueL, valueT);
    },

    absolutize: function(element) {
        element = $(element);
        if (element.getStyle('position') == 'absolute') return;
        // Position.prepare(); // To be done manually by Scripty when it needs it.

        var offsets = element.positionedOffset();
        var top = offsets[1];
        var left = offsets[0];
        var width = element.clientWidth;
        var height = element.clientHeight;

        element._originalLeft = left - parseFloat(element.style.left || 0);
        element._originalTop = top - parseFloat(element.style.top || 0);
        element._originalWidth = element.style.width;
        element._originalHeight = element.style.height;

        element.style.position = 'absolute';
        element.style.top = top + 'px';
        element.style.left = left + 'px';
        element.style.width = width + 'px';
        element.style.height = height + 'px';
        return element;
    },

    relativize: function(element) {
        element = $(element);
        if (element.getStyle('position') == 'relative') return;
        // Position.prepare(); // To be done manually by Scripty when it needs it.

        element.style.position = 'relative';
        var top = parseFloat(element.style.top || 0) - (element._originalTop || 0);
        var left = parseFloat(element.style.left || 0) - (element._originalLeft || 0);

        element.style.top = top + 'px';
        element.style.left = left + 'px';
        element.style.height = element._originalHeight;
        element.style.width = element._originalWidth;
        return element;
    },

    cumulativeScrollOffset: function(element) {
        var valueT = 0, valueL = 0;
        do {
            valueT += element.scrollTop || 0;
            valueL += element.scrollLeft || 0;
            element = element.parentNode;
        } while (element);
        return Element._returnOffset(valueL, valueT);
    },

    getOffsetParent: function(element) {
        if (element.offsetParent) return $(element.offsetParent);
        if (element == document.body) return $(element);

        while ((element = element.parentNode) && element != document.body)
            if (Element.getStyle(element, 'position') != 'static')
                return $(element);

        return $(document.body);
    },

    viewportOffset: function(forElement) {
        var valueT = 0, valueL = 0;

        var element = forElement;
        do {
            valueT += element.offsetTop || 0;
            valueL += element.offsetLeft || 0;

            // Safari fix
            if (element.offsetParent == document.body &&
                Element.getStyle(element, 'position') == 'absolute') break;

        } while (element = element.offsetParent);

        element = forElement;
        do {
            if (!Prototype.Browser.Opera || element.tagName == 'BODY') {
                valueT -= element.scrollTop || 0;
                valueL -= element.scrollLeft || 0;
            }
        } while (element = element.parentNode);

        return Element._returnOffset(valueL, valueT);
    },

    clonePosition: function(element, source) {
        var options = Object.extend({
            setLeft:    true,
            setTop:     true,
            setWidth:   true,
            setHeight:  true,
            offsetTop:  0,
            offsetLeft: 0
        }, arguments[2] || { });

        // find page position of source
        source = $(source);
        var p = source.viewportOffset();

        // find coordinate system to use
        element = $(element);
        var delta = [0, 0];
        var parent = null;
        // delta [0,0] will do fine with position: fixed elements,
        // position:absolute needs offsetParent deltas
        if (Element.getStyle(element, 'position') == 'absolute') {
            parent = element.getOffsetParent();
            delta = parent.viewportOffset();
        }

        // correct by body offsets (fixes Safari)
        if (parent == document.body) {
            delta[0] -= document.body.offsetLeft;
            delta[1] -= document.body.offsetTop;
        }

        // set position
        if (options.setLeft)   element.style.left = (p[0] - delta[0] + options.offsetLeft) + 'px';
        if (options.setTop)    element.style.top = (p[1] - delta[1] + options.offsetTop) + 'px';
        if (options.setWidth)  element.style.width = source.offsetWidth + 'px';
        if (options.setHeight) element.style.height = source.offsetHeight + 'px';
        return element;
    }
};

Element.Methods.identify.counter = 1;

Object.extend(Element.Methods, {
    getElementsBySelector: Element.Methods.select,
    childElements: Element.Methods.immediateDescendants
});

Element._attributeTranslations = {
    write: {
        names: {
            className: 'class',
            htmlFor:   'for'
        },
        values: { }
    }
};


if (!document.createRange || Prototype.Browser.Opera) {
    Element.Methods.insert = function(element, insertions) {
        element = $(element);

        if (Object.isString(insertions) || Object.isNumber(insertions) ||
            Object.isElement(insertions) || (insertions && (insertions.toElement || insertions.toHTML)))
            insertions = { bottom: insertions };

        var t = Element._insertionTranslations, content, position, pos, tagName;

        for (position in insertions) {
            content = insertions[position];
            position = position.toLowerCase();
            pos = t[position];

            if (content && content.toElement) content = content.toElement();
            if (Object.isElement(content)) {
                pos.insert(element, content);
                continue;
            }

            content = Object.toHTML(content);
            tagName = ((position == 'before' || position == 'after')
                    ? element.parentNode : element).tagName.toUpperCase();

            if (t.tags[tagName]) {
                var fragments = Element._getContentFromAnonymousElement(tagName, content.stripScripts());
                if (position == 'top' || position == 'after') fragments.reverse();
                fragments.each(pos.insert.curry(element));
            }
            else element.insertAdjacentHTML(pos.adjacency, content.stripScripts());

            content.evalScripts.bind(content).defer();
        }

        return element;
    };
}

if (Prototype.Browser.Opera) {
    Element.Methods.getStyle = Element.Methods.getStyle.wrap(
            function(proceed, element, style) {
                switch (style) {
                    case 'left': case 'top': case 'right': case 'bottom':
                    if (proceed(element, 'position') === 'static') return null;
                    case 'height': case 'width':
                    // returns '0px' for hidden elements; we want it to return null
                    if (!Element.visible(element)) return null;

                    // returns the border-box dimensions rather than the content-box
                    // dimensions, so we subtract padding and borders from the value
                    var dim = parseInt(proceed(element, style), 10);

                    if (dim !== element['offset' + style.capitalize()])
                        return dim + 'px';

                    var properties;
                    if (style === 'height') {
                        properties = ['border-top-width', 'padding-top',
                            'padding-bottom', 'border-bottom-width'];
                    }
                    else {
                        properties = ['border-left-width', 'padding-left',
                            'padding-right', 'border-right-width'];
                    }
                    return properties.inject(dim, function(memo, property) {
                        var val = proceed(element, property);
                        return val === null ? memo : memo - parseInt(val, 10);
                    }) + 'px';
                    default: return proceed(element, style);
                }
            }
            );

    Element.Methods.readAttribute = Element.Methods.readAttribute.wrap(
            function(proceed, element, attribute) {
                if (attribute === 'title') return element.title;
                return proceed(element, attribute);
            }
            );
}

else if (Prototype.Browser.IE) {
    $w('positionedOffset getOffsetParent viewportOffset').each(function(method) {
        Element.Methods[method] = Element.Methods[method].wrap(
                function(proceed, element) {
                    element = $(element);
                    var position = element.getStyle('position');
                    if (position != 'static') return proceed(element);
                    element.setStyle({ position: 'relative' });
                    var value = proceed(element);
                    element.setStyle({ position: position });
                    return value;
                }
                );
    });

    Element.Methods.getStyle = function(element, style) {
        element = $(element);
        style = (style == 'float' || style == 'cssFloat') ? 'styleFloat' : style.camelize();
        var value = element.style[style];
        if (!value && element.currentStyle) value = element.currentStyle[style];

        if (style == 'opacity') {
            if (value = (element.getStyle('filter') || '').match(/alpha\(opacity=(.*)\)/))
                if (value[1]) return parseFloat(value[1]) / 100;
            return 1.0;
        }

        if (value == 'auto') {
            if ((style == 'width' || style == 'height') && (element.getStyle('display') != 'none'))
                return element['offset' + style.capitalize()] + 'px';
            return null;
        }
        return value;
    };

    Element.Methods.setOpacity = function(element, value) {
        function stripAlpha(filter) {
            return filter.replace(/alpha\([^\)]*\)/gi, '');
        }

        element = $(element);
        var currentStyle = element.currentStyle;
        if ((currentStyle && !currentStyle.hasLayout) ||
            (!currentStyle && element.style.zoom == 'normal'))
            element.style.zoom = 1;

        var filter = element.getStyle('filter'), style = element.style;
        if (value == 1 || value === '') {
            (filter = stripAlpha(filter)) ?
            style.filter = filter : style.removeAttribute('filter');
            return element;
        } else if (value < 0.00001) value = 0;
        style.filter = stripAlpha(filter) +
                       'alpha(opacity=' + (value * 100) + ')';
        return element;
    };

    Element._attributeTranslations = {
        read: {
            names: {
                'class': 'className',
                'for':   'htmlFor'
            },
            values: {
                _getAttr: function(element, attribute) {
                    return element.getAttribute(attribute, 2);
                },
                _getAttrNode: function(element, attribute) {
                    var node = element.getAttributeNode(attribute);
                    return node ? node.value : "";
                },
                _getEv: function(element, attribute) {
                    attribute = element.getAttribute(attribute);
                    return attribute ? attribute.toString().slice(23, -2) : null;
                },
                _flag: function(element, attribute) {
                    return $(element).hasAttribute(attribute) ? attribute : null;
                },
                style: function(element) {
                    return element.style.cssText.toLowerCase();
                },
                title: function(element) {
                    return element.title;
                }
            }
        }
    };

    Element._attributeTranslations.write = {
        names: Object.clone(Element._attributeTranslations.read.names),
        values: {
            checked: function(element, value) {
                element.checked = !!value;
            },

            style: function(element, value) {
                element.style.cssText = value ? value : '';
            }
        }
    };

    Element._attributeTranslations.has = {};

    $w('colSpan rowSpan vAlign dateTime accessKey tabIndex ' +
       'encType maxLength readOnly longDesc').each(function(attr) {
        Element._attributeTranslations.write.names[attr.toLowerCase()] = attr;
        Element._attributeTranslations.has[attr.toLowerCase()] = attr;
    });

    (function(v) {
        Object.extend(v, {
            href:        v._getAttr,
            src:         v._getAttr,
            type:        v._getAttr,
            action:      v._getAttrNode,
            disabled:    v._flag,
            checked:     v._flag,
            readonly:    v._flag,
            multiple:    v._flag,
            onload:      v._getEv,
            onunload:    v._getEv,
            onclick:     v._getEv,
            ondblclick:  v._getEv,
            onmousedown: v._getEv,
            onmouseup:   v._getEv,
            onmouseover: v._getEv,
            onmousemove: v._getEv,
            onmouseout:  v._getEv,
            onfocus:     v._getEv,
            onblur:      v._getEv,
            onkeypress:  v._getEv,
            onkeydown:   v._getEv,
            onkeyup:     v._getEv,
            onsubmit:    v._getEv,
            onreset:     v._getEv,
            onselect:    v._getEv,
            onchange:    v._getEv
        });
    })(Element._attributeTranslations.read.values);
}

else if (Prototype.Browser.Gecko && /rv:1\.8\.0/.test(navigator.userAgent)) {
        Element.Methods.setOpacity = function(element, value) {
            element = $(element);
            element.style.opacity = (value == 1) ? 0.999999 :
                                    (value === '') ? '' : (value < 0.00001) ? 0 : value;
            return element;
        };
    }

    else if (Prototype.Browser.WebKit) {
            Element.Methods.setOpacity = function(element, value) {
                element = $(element);
                element.style.opacity = (value == 1 || value === '') ? '' :
                                        (value < 0.00001) ? 0 : value;

                if (value == 1)
                    if (element.tagName == 'IMG' && element.width) {
                        element.width++;
                        element.width--;
                    } else try {
                        var n = document.createTextNode(' ');
                        element.appendChild(n);
                        element.removeChild(n);
                    } catch (e) {
                    }

                return element;
            };

            // Safari returns margins on body which is incorrect if the child is absolutely
            // positioned.  For performance reasons, redefine Element#cumulativeOffset for
            // KHTML/WebKit only.
            Element.Methods.cumulativeOffset = function(element) {
                var valueT = 0, valueL = 0;
                do {
                    valueT += element.offsetTop || 0;
                    valueL += element.offsetLeft || 0;
                    if (element.offsetParent == document.body)
                        if (Element.getStyle(element, 'position') == 'absolute') break;

                    element = element.offsetParent;
                } while (element);

                return Element._returnOffset(valueL, valueT);
            };
        }

if (Prototype.Browser.IE || Prototype.Browser.Opera) {
    // IE and Opera are missing .innerHTML support for TABLE-related and SELECT elements
    Element.Methods.update = function(element, content) {
        element = $(element);

        if (content && content.toElement) content = content.toElement();
        if (Object.isElement(content)) return element.update().insert(content);

        content = Object.toHTML(content);
        var tagName = element.tagName.toUpperCase();

        if (tagName in Element._insertionTranslations.tags) {
            $A(element.childNodes).each(function(node) {
                element.removeChild(node)
            });
            Element._getContentFromAnonymousElement(tagName, content.stripScripts())
                    .each(function(node) {
                element.appendChild(node)
            });
        }
        else element.innerHTML = content.stripScripts();

        content.evalScripts.bind(content).defer();
        return element;
    };
}

if (document.createElement('div').outerHTML) {
    Element.Methods.replace = function(element, content) {
        element = $(element);

        if (content && content.toElement) content = content.toElement();
        if (Object.isElement(content)) {
            element.parentNode.replaceChild(content, element);
            return element;
        }

        content = Object.toHTML(content);
        var parent = element.parentNode, tagName = parent.tagName.toUpperCase();

        if (Element._insertionTranslations.tags[tagName]) {
            var nextSibling = element.next();
            var fragments = Element._getContentFromAnonymousElement(tagName, content.stripScripts());
            parent.removeChild(element);
            if (nextSibling)
                fragments.each(function(node) {
                    parent.insertBefore(node, nextSibling)
                });
            else
                fragments.each(function(node) {
                    parent.appendChild(node)
                });
        }
        else element.outerHTML = content.stripScripts();

        content.evalScripts.bind(content).defer();
        return element;
    };
}

Element._returnOffset = function(l, t) {
    var result = [l, t];
    result.left = l;
    result.top = t;
    return result;
};

Element._getContentFromAnonymousElement = function(tagName, html) {
    var div = new Element('div'), t = Element._insertionTranslations.tags[tagName];
    div.innerHTML = t[0] + html + t[1];
    t[2].times(function() {
        div = div.firstChild
    });
    return $A(div.childNodes);
};

Element._insertionTranslations = {
    before: {
        adjacency: 'beforeBegin',
        insert: function(element, node) {
            element.parentNode.insertBefore(node, element);
        },
        initializeRange: function(element, range) {
            range.setStartBefore(element);
        }
    },
    top: {
        adjacency: 'afterBegin',
        insert: function(element, node) {
            element.insertBefore(node, element.firstChild);
        },
        initializeRange: function(element, range) {
            range.selectNodeContents(element);
            range.collapse(true);
        }
    },
    bottom: {
        adjacency: 'beforeEnd',
        insert: function(element, node) {
            element.appendChild(node);
        }
    },
    after: {
        adjacency: 'afterEnd',
        insert: function(element, node) {
            element.parentNode.insertBefore(node, element.nextSibling);
        },
        initializeRange: function(element, range) {
            range.setStartAfter(element);
        }
    },
    tags: {
        TABLE:  ['<table>',                '</table>',                   1],
        TBODY:  ['<table><tbody>',         '</tbody></table>',           2],
        TR:     ['<table><tbody><tr>',     '</tr></tbody></table>',      3],
        TD:     ['<table><tbody><tr><td>', '</td></tr></tbody></table>', 4],
        SELECT: ['<select>',               '</select>',                  1]
    }
};

(function() {
    this.bottom.initializeRange = this.top.initializeRange;
    Object.extend(this.tags, {
        THEAD: this.tags.TBODY,
        TFOOT: this.tags.TBODY,
        TH:    this.tags.TD
    });
}).call(Element._insertionTranslations);

Element.Methods.Simulated = {
    hasAttribute: function(element, attribute) {
        attribute = Element._attributeTranslations.has[attribute] || attribute;
        var node = $(element).getAttributeNode(attribute);
        return node && node.specified;
    }
};

Element.Methods.ByTag = { };

Object.extend(Element, Element.Methods);

if (!Prototype.BrowserFeatures.ElementExtensions &&
    document.createElement('div').__proto__) {
    window.HTMLElement = { };
    window.HTMLElement.prototype = document.createElement('div').__proto__;
    Prototype.BrowserFeatures.ElementExtensions = true;
}

Element.extend = (function() {
    if (Prototype.BrowserFeatures.SpecificElementExtensions)
        return Prototype.K;

    var Methods = { }, ByTag = Element.Methods.ByTag;

    var extend = Object.extend(function(element) {
        if (!element || element._extendedByPrototype ||
            element.nodeType != 1 || element == window) return element;

        var methods = Object.clone(Methods),
                tagName = element.tagName, property, value;

        // extend methods for specific tags
        if (ByTag[tagName]) Object.extend(methods, ByTag[tagName]);

        for (property in methods) {
            value = methods[property];
            if (Object.isFunction(value) && !(property in element))
                element[property] = value.methodize();
        }

        element._extendedByPrototype = Prototype.emptyFunction;
        return element;

    }, {
        refresh: function() {
            // extend methods for all tags (Safari doesn't need this)
            if (!Prototype.BrowserFeatures.ElementExtensions) {
                Object.extend(Methods, Element.Methods);
                Object.extend(Methods, Element.Methods.Simulated);
            }
        }
    });

    extend.refresh();
    return extend;
})();

Element.hasAttribute = function(element, attribute) {
    if (element.hasAttribute) return element.hasAttribute(attribute);
    return Element.Methods.Simulated.hasAttribute(element, attribute);
};

Element.addMethods = function(methods) {
    var F = Prototype.BrowserFeatures, T = Element.Methods.ByTag;

    if (!methods) {
        Object.extend(Form, Form.Methods);
        Object.extend(Form.Element, Form.Element.Methods);
        Object.extend(Element.Methods.ByTag, {
            "FORM":     Object.clone(Form.Methods),
            "INPUT":    Object.clone(Form.Element.Methods),
            "SELECT":   Object.clone(Form.Element.Methods),
            "TEXTAREA": Object.clone(Form.Element.Methods)
        });
    }

    if (arguments.length == 2) {
        var tagName = methods;
        methods = arguments[1];
    }

    if (!tagName) Object.extend(Element.Methods, methods || { });
    else {
        if (Object.isArray(tagName)) tagName.each(extend);
        else extend(tagName);
    }

    function extend(tagName) {
        tagName = tagName.toUpperCase();
        if (!Element.Methods.ByTag[tagName])
            Element.Methods.ByTag[tagName] = { };
        Object.extend(Element.Methods.ByTag[tagName], methods);
    }

    function copy(methods, destination, onlyIfAbsent) {
        onlyIfAbsent = onlyIfAbsent || false;
        for (var property in methods) {
            var value = methods[property];
            if (!Object.isFunction(value)) continue;
            if (!onlyIfAbsent || !(property in destination))
                destination[property] = value.methodize();
        }
    }

    function findDOMClass(tagName) {
        var klass;
        var trans = {
            "OPTGROUP": "OptGroup", "TEXTAREA": "TextArea", "P": "Paragraph",
            "FIELDSET": "FieldSet", "UL": "UList", "OL": "OList", "DL": "DList",
            "DIR": "Directory", "H1": "Heading", "H2": "Heading", "H3": "Heading",
            "H4": "Heading", "H5": "Heading", "H6": "Heading", "Q": "Quote",
            "INS": "Mod", "DEL": "Mod", "A": "Anchor", "IMG": "Image", "CAPTION":
                    "TableCaption", "COL": "TableCol", "COLGROUP": "TableCol", "THEAD":
                    "TableSection", "TFOOT": "TableSection", "TBODY": "TableSection", "TR":
                    "TableRow", "TH": "TableCell", "TD": "TableCell", "FRAMESET":
                    "FrameSet", "IFRAME": "IFrame"
        };
        if (trans[tagName]) klass = 'HTML' + trans[tagName] + 'Element';
        if (window[klass]) return window[klass];
        klass = 'HTML' + tagName + 'Element';
        if (window[klass]) return window[klass];
        klass = 'HTML' + tagName.capitalize() + 'Element';
        if (window[klass]) return window[klass];

        window[klass] = { };
        window[klass].prototype = document.createElement(tagName).__proto__;
        return window[klass];
    }

    if (F.ElementExtensions) {
        copy(Element.Methods, HTMLElement.prototype);
        copy(Element.Methods.Simulated, HTMLElement.prototype, true);
    }

    if (F.SpecificElementExtensions) {
        for (var tag in Element.Methods.ByTag) {
            var klass = findDOMClass(tag);
            if (Object.isUndefined(klass)) continue;
            copy(T[tag], klass.prototype);
        }
    }

    Object.extend(Element, Element.Methods);
    delete Element.ByTag;

    if (Element.extend.refresh) Element.extend.refresh();
    Element.cache = { };
};

document.viewport = {
    getDimensions: function() {
        var dimensions = { };
        var B = Prototype.Browser;
        $w('width height').each(function(d) {
            var D = d.capitalize();
            if (B.WebKit && !document.evaluate) {
                dimensions[d] = self['inner' + D];
            } else if (B.Opera && parseFloat(window.opera.version()) < 9.5 || !document.compatMode || document.compatMode != "CSS1Compat") {
                dimensions[d] = document.body['client' + D];
            } else {
                dimensions[d] = document.documentElement['client' + D];
            }
        });
        return dimensions;
    },

    getWidth: function() {
        return this.getDimensions().width;
    },

    getHeight: function() {
        return this.getDimensions().height;
    },

    getScrollOffsets: function() {
        return Element._returnOffset(
                window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft,
                window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop);
    }
};
/* Portions of the Selector class are derived from Jack Slocum’s DomQuery,
 * part of YUI-Ext version 0.40, distributed under the terms of an MIT-style
 * license.  Please see http://www.yui-ext.com/ for more information. */

var Selector = Class.create({
    initialize: function(expression) {
        this.expression = expression.strip();
        this.compileMatcher();
    },

    shouldUseXPath: function() {
        if (!Prototype.BrowserFeatures.XPath) return false;

        var e = this.expression;

        // Safari 3 chokes on :*-of-type and :empty
        if (Prototype.Browser.WebKit &&
            (e.include("-of-type") || e.include(":empty")))
            return false;

        // XPath can't do namespaced attributes, nor can it read
        // the "checked" property from DOM nodes
        if ((/(\[[\w-]*?:|:checked)/).test(this.expression))
            return false;

        return true;
    },

    compileMatcher: function() {
        if (this.shouldUseXPath())
            return this.compileXPathMatcher();

        var e = this.expression, ps = Selector.patterns, h = Selector.handlers,
                c = Selector.criteria, le, p, m;

        if (Selector._cache[e]) {
            this.matcher = Selector._cache[e];
            return;
        }

        this.matcher = ["this.matcher = function(root) {",
            "var r = root, h = Selector.handlers, c = false, n;"];

        while (e && le != e && (/\S/).test(e)) {
            le = e;
            for (var i in ps) {
                p = ps[i];
                if (m = e.match(p)) {
                    this.matcher.push(Object.isFunction(c[i]) ? c[i](m) :
                                      new Template(c[i]).evaluate(m));
                    e = e.replace(m[0], '');
                    break;
                }
            }
        }

        this.matcher.push("return h.unique(n);\n}");
        eval(this.matcher.join('\n'));
        Selector._cache[this.expression] = this.matcher;
    },

    compileXPathMatcher: function() {
        var e = this.expression, ps = Selector.patterns,
                x = Selector.xpath, le, m;

        if (Selector._cache[e]) {
            this.xpath = Selector._cache[e];
            return;
        }

        this.matcher = ['.//*'];
        while (e && le != e && (/\S/).test(e)) {
            le = e;
            for (var i in ps) {
                if (m = e.match(ps[i])) {
                    this.matcher.push(Object.isFunction(x[i]) ? x[i](m) :
                                      new Template(x[i]).evaluate(m));
                    e = e.replace(m[0], '');
                    break;
                }
            }
        }

        this.xpath = this.matcher.join('');
        Selector._cache[this.expression] = this.xpath;
    },

    findElements: function(root) {
        root = root || document;
        if (this.xpath) return document._getElementsByXPath(this.xpath, root);
        return this.matcher(root);
    },

    match: function(element) {
        this.tokens = [];

        var e = this.expression, ps = Selector.patterns, as = Selector.assertions;
        var le, p, m;

        while (e && le !== e && (/\S/).test(e)) {
            le = e;
            for (var i in ps) {
                p = ps[i];
                if (m = e.match(p)) {
                    // use the Selector.assertions methods unless the selector
                    // is too complex.
                    if (as[i]) {
                        this.tokens.push([i, Object.clone(m)]);
                        e = e.replace(m[0], '');
                    } else {
                        // reluctantly do a document-wide search
                        // and look for a match in the array
                        return this.findElements(document).include(element);
                    }
                }
            }
        }

        var match = true, name, matches;
        for (var i = 0, token; token = this.tokens[i]; i++) {
            name = token[0],matches = token[1];
            if (!Selector.assertions[name](element, matches)) {
                match = false;
                break;
            }
        }

        return match;
    },

    toString: function() {
        return this.expression;
    },

    inspect: function() {
        return "#<Selector:" + this.expression.inspect() + ">";
    }
});

Object.extend(Selector, {
    _cache: { },

    xpath: {
        descendant:   "//*",
        child:        "/*",
        adjacent:     "/following-sibling::*[1]",
        laterSibling: '/following-sibling::*',
        tagName:      function(m) {
            if (m[1] == '*') return '';
            return "[local-name()='" + m[1].toLowerCase() +
                   "' or local-name()='" + m[1].toUpperCase() + "']";
        },
        className:    "[contains(concat(' ', @class, ' '), ' #{1} ')]",
        id:           "[@id='#{1}']",
        attrPresence: function(m) {
            m[1] = m[1].toLowerCase();
            return new Template("[@#{1}]").evaluate(m);
        },
        attr: function(m) {
            m[1] = m[1].toLowerCase();
            m[3] = m[5] || m[6];
            return new Template(Selector.xpath.operators[m[2]]).evaluate(m);
        },
        pseudo: function(m) {
            var h = Selector.xpath.pseudos[m[1]];
            if (!h) return '';
            if (Object.isFunction(h)) return h(m);
            return new Template(Selector.xpath.pseudos[m[1]]).evaluate(m);
        },
        operators: {
            '=':  "[@#{1}='#{3}']",
            '!=': "[@#{1}!='#{3}']",
            '^=': "[starts-with(@#{1}, '#{3}')]",
            '$=': "[substring(@#{1}, (string-length(@#{1}) - string-length('#{3}') + 1))='#{3}']",
            '*=': "[contains(@#{1}, '#{3}')]",
            '~=': "[contains(concat(' ', @#{1}, ' '), ' #{3} ')]",
            '|=': "[contains(concat('-', @#{1}, '-'), '-#{3}-')]"
        },
        pseudos: {
            'first-child': '[not(preceding-sibling::*)]',
            'last-child':  '[not(following-sibling::*)]',
            'only-child':  '[not(preceding-sibling::* or following-sibling::*)]',
            'empty':       "[count(*) = 0 and (count(text()) = 0 or translate(text(), ' \t\r\n', '') = '')]",
            'checked':     "[@checked]",
            'disabled':    "[@disabled]",
            'enabled':     "[not(@disabled)]",
            'not': function(m) {
                var e = m[6], p = Selector.patterns,
                        x = Selector.xpath, le, v;

                var exclusion = [];
                while (e && le != e && (/\S/).test(e)) {
                    le = e;
                    for (var i in p) {
                        if (m = e.match(p[i])) {
                            v = Object.isFunction(x[i]) ? x[i](m) : new Template(x[i]).evaluate(m);
                            exclusion.push("(" + v.substring(1, v.length - 1) + ")");
                            e = e.replace(m[0], '');
                            break;
                        }
                    }
                }
                return "[not(" + exclusion.join(" and ") + ")]";
            },
            'nth-child':      function(m) {
                return Selector.xpath.pseudos.nth("(count(./preceding-sibling::*) + 1) ", m);
            },
            'nth-last-child': function(m) {
                return Selector.xpath.pseudos.nth("(count(./following-sibling::*) + 1) ", m);
            },
            'nth-of-type':    function(m) {
                return Selector.xpath.pseudos.nth("position() ", m);
            },
            'nth-last-of-type': function(m) {
                return Selector.xpath.pseudos.nth("(last() + 1 - position()) ", m);
            },
            'first-of-type':  function(m) {
                m[6] = "1";
                return Selector.xpath.pseudos['nth-of-type'](m);
            },
            'last-of-type':   function(m) {
                m[6] = "1";
                return Selector.xpath.pseudos['nth-last-of-type'](m);
            },
            'only-of-type':   function(m) {
                var p = Selector.xpath.pseudos;
                return p['first-of-type'](m) + p['last-of-type'](m);
            },
            nth: function(fragment, m) {
                var mm, formula = m[6], predicate;
                if (formula == 'even') formula = '2n+0';
                if (formula == 'odd')  formula = '2n+1';
                if (mm = formula.match(/^(\d+)$/)) // digit only
                    return '[' + fragment + "= " + mm[1] + ']';
                if (mm = formula.match(/^(-?\d*)?n(([+-])(\d+))?/)) { // an+b
                    if (mm[1] == "-") mm[1] = -1;
                    var a = mm[1] ? Number(mm[1]) : 1;
                    var b = mm[2] ? Number(mm[2]) : 0;
                    predicate = "[((#{fragment} - #{b}) mod #{a} = 0) and " +
                                "((#{fragment} - #{b}) div #{a} >= 0)]";
                    return new Template(predicate).evaluate({
                        fragment: fragment, a: a, b: b });
                }
            }
        }
    },

    criteria: {
        tagName:      'n = h.tagName(n, r, "#{1}", c);   c = false;',
        className:    'n = h.className(n, r, "#{1}", c); c = false;',
        id:           'n = h.id(n, r, "#{1}", c);        c = false;',
        attrPresence: 'n = h.attrPresence(n, r, "#{1}"); c = false;',
        attr: function(m) {
            m[3] = (m[5] || m[6]);
            return new Template('n = h.attr(n, r, "#{1}", "#{3}", "#{2}"); c = false;').evaluate(m);
        },
        pseudo: function(m) {
            if (m[6]) m[6] = m[6].replace(/"/g, '\\"');
            return new Template('n = h.pseudo(n, "#{1}", "#{6}", r, c); c = false;').evaluate(m);
        },
        descendant:   'c = "descendant";',
        child:        'c = "child";',
        adjacent:     'c = "adjacent";',
        laterSibling: 'c = "laterSibling";'
    },

    patterns: {
        // combinators must be listed first
        // (and descendant needs to be last combinator)
        laterSibling: /^\s*~\s*/,
        child:        /^\s*>\s*/,
        adjacent:     /^\s*\+\s*/,
        descendant:   /^\s/,

        // selectors follow
        tagName:      /^\s*(\*|[\w\-]+)(\b|$)?/,
        id:           /^#([\w\-\*]+)(\b|$)/,
        className:    /^\.([\w\-\*]+)(\b|$)/,
        pseudo:       /^:((first|last|nth|nth-last|only)(-child|-of-type)|empty|checked|(en|dis)abled|not)(\((.*?)\))?(\b|$|(?=\s)|(?=:))/,
        attrPresence: /^\[([\w]+)\]/,
        attr:         /\[((?:[\w-]*:)?[\w-]+)\s*(?:([!^$*~|]?=)\s*((['"])([^\4]*?)\4|([^'"][^\]]*?)))?\]/
    },

    // for Selector.match and Element#match
    assertions: {
        tagName: function(element, matches) {
            return matches[1].toUpperCase() == element.tagName.toUpperCase();
        },

        className: function(element, matches) {
            return Element.hasClassName(element, matches[1]);
        },

        id: function(element, matches) {
            return element.id === matches[1];
        },

        attrPresence: function(element, matches) {
            return Element.hasAttribute(element, matches[1]);
        },

        attr: function(element, matches) {
            var nodeValue = Element.readAttribute(element, matches[1]);
            return Selector.operators[matches[2]](nodeValue, matches[3]);
        }
    },

    handlers: {
        // UTILITY FUNCTIONS
        // joins two collections
        concat: function(a, b) {
            for (var i = 0, node; node = b[i]; i++)
                a.push(node);
            return a;
        },

        // marks an array of nodes for counting
        mark: function(nodes) {
            for (var i = 0, node; node = nodes[i]; i++)
                node._counted = true;
            return nodes;
        },

        unmark: function(nodes) {
            for (var i = 0, node; node = nodes[i]; i++)
                node._counted = undefined;
            return nodes;
        },

        // mark each child node with its position (for nth calls)
        // "ofType" flag indicates whether we're indexing for nth-of-type
        // rather than nth-child
        index: function(parentNode, reverse, ofType) {
            parentNode._counted = true;
            if (reverse) {
                for (var nodes = parentNode.childNodes, i = nodes.length - 1, j = 1; i >= 0; i--) {
                    var node = nodes[i];
                    if (node.nodeType == 1 && (!ofType || node._counted)) node.nodeIndex = j++;
                }
            } else {
                for (var i = 0, j = 1, nodes = parentNode.childNodes; node = nodes[i]; i++)
                    if (node.nodeType == 1 && (!ofType || node._counted)) node.nodeIndex = j++;
            }
        },

        // filters out duplicates and extends all nodes
        unique: function(nodes) {
            if (nodes.length == 0) return nodes;
            var results = [], n;
            for (var i = 0, l = nodes.length; i < l; i++)
                if (!(n = nodes[i])._counted) {
                    n._counted = true;
                    results.push(Element.extend(n));
                }
            return Selector.handlers.unmark(results);
        },

        // COMBINATOR FUNCTIONS
        descendant: function(nodes) {
            var h = Selector.handlers;
            for (var i = 0, results = [], node; node = nodes[i]; i++)
                h.concat(results, node.getElementsByTagName('*'));
            return results;
        },

        child: function(nodes) {
            var h = Selector.handlers;
            for (var i = 0, results = [], node; node = nodes[i]; i++) {
                for (var j = 0, child; child = node.childNodes[j]; j++)
                    if (child.nodeType == 1 && child.tagName != '!') results.push(child);
            }
            return results;
        },

        adjacent: function(nodes) {
            for (var i = 0, results = [], node; node = nodes[i]; i++) {
                var next = this.nextElementSibling(node);
                if (next) results.push(next);
            }
            return results;
        },

        laterSibling: function(nodes) {
            var h = Selector.handlers;
            for (var i = 0, results = [], node; node = nodes[i]; i++)
                h.concat(results, Element.nextSiblings(node));
            return results;
        },

        nextElementSibling: function(node) {
            while (node = node.nextSibling)
                if (node.nodeType == 1) return node;
            return null;
        },

        previousElementSibling: function(node) {
            while (node = node.previousSibling)
                if (node.nodeType == 1) return node;
            return null;
        },

        // TOKEN FUNCTIONS
        tagName: function(nodes, root, tagName, combinator) {
            tagName = tagName.toUpperCase();
            var results = [], h = Selector.handlers;
            if (nodes) {
                if (combinator) {
                    // fastlane for ordinary descendant combinators
                    if (combinator == "descendant") {
                        for (var i = 0, node; node = nodes[i]; i++)
                            h.concat(results, node.getElementsByTagName(tagName));
                        return results;
                    } else nodes = this[combinator](nodes);
                    if (tagName == "*") return nodes;
                }
                for (var i = 0, node; node = nodes[i]; i++)
                    if (node.tagName.toUpperCase() == tagName) results.push(node);
                return results;
            } else return root.getElementsByTagName(tagName);
        },

        id: function(nodes, root, id, combinator) {
            var targetNode = $(id), h = Selector.handlers;
            if (!targetNode) return [];
            if (!nodes && root == document) return [targetNode];
            if (nodes) {
                if (combinator) {
                    if (combinator == 'child') {
                        for (var i = 0, node; node = nodes[i]; i++)
                            if (targetNode.parentNode == node) return [targetNode];
                    } else if (combinator == 'descendant') {
                        for (var i = 0, node; node = nodes[i]; i++)
                            if (Element.descendantOf(targetNode, node)) return [targetNode];
                    } else if (combinator == 'adjacent') {
                        for (var i = 0, node; node = nodes[i]; i++)
                            if (Selector.handlers.previousElementSibling(targetNode) == node)
                                return [targetNode];
                    } else nodes = h[combinator](nodes);
                }
                for (var i = 0, node; node = nodes[i]; i++)
                    if (node == targetNode) return [targetNode];
                return [];
            }
            return (targetNode && Element.descendantOf(targetNode, root)) ? [targetNode] : [];
        },

        className: function(nodes, root, className, combinator) {
            if (nodes && combinator) nodes = this[combinator](nodes);
            return Selector.handlers.byClassName(nodes, root, className);
        },

        byClassName: function(nodes, root, className) {
            if (!nodes) nodes = Selector.handlers.descendant([root]);
            var needle = ' ' + className + ' ';
            for (var i = 0, results = [], node, nodeClassName; node = nodes[i]; i++) {
                nodeClassName = node.className;
                if (nodeClassName.length == 0) continue;
                if (nodeClassName == className || (' ' + nodeClassName + ' ').include(needle))
                    results.push(node);
            }
            return results;
        },

        attrPresence: function(nodes, root, attr) {
            if (!nodes) nodes = root.getElementsByTagName("*");
            var results = [];
            for (var i = 0, node; node = nodes[i]; i++)
                if (Element.hasAttribute(node, attr)) results.push(node);
            return results;
        },

        attr: function(nodes, root, attr, value, operator) {
            if (!nodes) nodes = root.getElementsByTagName("*");
            var handler = Selector.operators[operator], results = [];
            for (var i = 0, node; node = nodes[i]; i++) {
                var nodeValue = Element.readAttribute(node, attr);
                if (nodeValue === null) continue;
                if (handler(nodeValue, value)) results.push(node);
            }
            return results;
        },

        pseudo: function(nodes, name, value, root, combinator) {
            if (nodes && combinator) nodes = this[combinator](nodes);
            if (!nodes) nodes = root.getElementsByTagName("*");
            return Selector.pseudos[name](nodes, value, root);
        }
    },

    pseudos: {
        'first-child': function(nodes, value, root) {
            for (var i = 0, results = [], node; node = nodes[i]; i++) {
                if (Selector.handlers.previousElementSibling(node)) continue;
                results.push(node);
            }
            return results;
        },
        'last-child': function(nodes, value, root) {
            for (var i = 0, results = [], node; node = nodes[i]; i++) {
                if (Selector.handlers.nextElementSibling(node)) continue;
                results.push(node);
            }
            return results;
        },
        'only-child': function(nodes, value, root) {
            var h = Selector.handlers;
            for (var i = 0, results = [], node; node = nodes[i]; i++)
                if (!h.previousElementSibling(node) && !h.nextElementSibling(node))
                    results.push(node);
            return results;
        },
        'nth-child':        function(nodes, formula, root) {
            return Selector.pseudos.nth(nodes, formula, root);
        },
        'nth-last-child':   function(nodes, formula, root) {
            return Selector.pseudos.nth(nodes, formula, root, true);
        },
        'nth-of-type':      function(nodes, formula, root) {
            return Selector.pseudos.nth(nodes, formula, root, false, true);
        },
        'nth-last-of-type': function(nodes, formula, root) {
            return Selector.pseudos.nth(nodes, formula, root, true, true);
        },
        'first-of-type':    function(nodes, formula, root) {
            return Selector.pseudos.nth(nodes, "1", root, false, true);
        },
        'last-of-type':     function(nodes, formula, root) {
            return Selector.pseudos.nth(nodes, "1", root, true, true);
        },
        'only-of-type':     function(nodes, formula, root) {
            var p = Selector.pseudos;
            return p['last-of-type'](p['first-of-type'](nodes, formula, root), formula, root);
        },

        // handles the an+b logic
        getIndices: function(a, b, total) {
            if (a == 0) return b > 0 ? [b] : [];
            return $R(1, total).inject([], function(memo, i) {
                if (0 == (i - b) % a && (i - b) / a >= 0) memo.push(i);
                return memo;
            });
        },

        // handles nth(-last)-child, nth(-last)-of-type, and (first|last)-of-type
        nth: function(nodes, formula, root, reverse, ofType) {
            if (nodes.length == 0) return [];
            if (formula == 'even') formula = '2n+0';
            if (formula == 'odd')  formula = '2n+1';
            var h = Selector.handlers, results = [], indexed = [], m;
            h.mark(nodes);
            for (var i = 0, node; node = nodes[i]; i++) {
                if (!node.parentNode._counted) {
                    h.index(node.parentNode, reverse, ofType);
                    indexed.push(node.parentNode);
                }
            }
            if (formula.match(/^\d+$/)) { // just a number
                formula = Number(formula);
                for (var i = 0, node; node = nodes[i]; i++)
                    if (node.nodeIndex == formula) results.push(node);
            } else if (m = formula.match(/^(-?\d*)?n(([+-])(\d+))?/)) { // an+b
                if (m[1] == "-") m[1] = -1;
                var a = m[1] ? Number(m[1]) : 1;
                var b = m[2] ? Number(m[2]) : 0;
                var indices = Selector.pseudos.getIndices(a, b, nodes.length);
                for (var i = 0, node, l = indices.length; node = nodes[i]; i++) {
                    for (var j = 0; j < l; j++)
                        if (node.nodeIndex == indices[j]) results.push(node);
                }
            }
            h.unmark(nodes);
            h.unmark(indexed);
            return results;
        },

        'empty': function(nodes, value, root) {
            for (var i = 0, results = [], node; node = nodes[i]; i++) {
                // IE treats comments as element nodes
                if (node.tagName == '!' || (node.firstChild && !node.innerHTML.match(/^\s*$/))) continue;
                results.push(node);
            }
            return results;
        },

        'not': function(nodes, selector, root) {
            var h = Selector.handlers, selectorType, m;
            var exclusions = new Selector(selector).findElements(root);
            h.mark(exclusions);
            for (var i = 0, results = [], node; node = nodes[i]; i++)
                if (!node._counted) results.push(node);
            h.unmark(exclusions);
            return results;
        },

        'enabled': function(nodes, value, root) {
            for (var i = 0, results = [], node; node = nodes[i]; i++)
                if (!node.disabled) results.push(node);
            return results;
        },

        'disabled': function(nodes, value, root) {
            for (var i = 0, results = [], node; node = nodes[i]; i++)
                if (node.disabled) results.push(node);
            return results;
        },

        'checked': function(nodes, value, root) {
            for (var i = 0, results = [], node; node = nodes[i]; i++)
                if (node.checked) results.push(node);
            return results;
        }
    },

    operators: {
        '=':  function(nv, v) {
            return nv == v;
        },
        '!=': function(nv, v) {
            return nv != v;
        },
        '^=': function(nv, v) {
            return nv.startsWith(v);
        },
        '$=': function(nv, v) {
            return nv.endsWith(v);
        },
        '*=': function(nv, v) {
            return nv.include(v);
        },
        '~=': function(nv, v) {
            return (' ' + nv + ' ').include(' ' + v + ' ');
        },
        '|=': function(nv, v) {
            return ('-' + nv.toUpperCase() + '-').include('-' + v.toUpperCase() + '-');
        }
    },

    matchElements: function(elements, expression) {
        var matches = new Selector(expression).findElements(), h = Selector.handlers;
        h.mark(matches);
        for (var i = 0, results = [], element; element = elements[i]; i++)
            if (element._counted) results.push(element);
        h.unmark(matches);
        return results;
    },

    findElement: function(elements, expression, index) {
        if (Object.isNumber(expression)) {
            index = expression;
            expression = false;
        }
        return Selector.matchElements(elements, expression || '*')[index || 0];
    },

    findChildElements: function(element, expressions) {
        var exprs = expressions.join(',');
        expressions = [];
        exprs.scan(/(([\w#:.~>+()\s-]+|\*|\[.*?\])+)\s*(,|$)/, function(m) {
            expressions.push(m[1].strip());
        });
        var results = [], h = Selector.handlers;
        for (var i = 0, l = expressions.length, selector; i < l; i++) {
            selector = new Selector(expressions[i].strip());
            h.concat(results, selector.findElements(element));
        }
        return (l > 1) ? h.unique(results) : results;
    }
});

if (Prototype.Browser.IE) {
    // IE returns comment nodes on getElementsByTagName("*").
    // Filter them out.
    Selector.handlers.concat = function(a, b) {
        for (var i = 0, node; node = b[i]; i++)
            if (node.tagName !== "!") a.push(node);
        return a;
    };
}

function $$() {
    return Selector.findChildElements(document, $A(arguments));
}
if (!window.Event) var Event = { };

Object.extend(Event, {
    KEY_BACKSPACE: 8,
    KEY_TAB:       9,
    KEY_RETURN:   13,
    KEY_ESC:      27,
    KEY_LEFT:     37,
    KEY_UP:       38,
    KEY_RIGHT:    39,
    KEY_DOWN:     40,
    KEY_DELETE:   46,
    KEY_HOME:     36,
    KEY_END:      35,
    KEY_PAGEUP:   33,
    KEY_PAGEDOWN: 34,
    KEY_INSERT:   45,

    cache: { },

    relatedTarget: function(event) {
        var element;
        switch (event.type) {
            case 'mouseover': element = event.fromElement; break;
            case 'mouseout':  element = event.toElement;   break;
            default: return null;
        }
        return Element.extend(element);
    }
});

Event.Methods = (function() {
    var isButton;

    if (Prototype.Browser.IE) {
        var buttonMap = { 0: 1, 1: 4, 2: 2 };
        isButton = function(event, code) {
            return event.button == buttonMap[code];
        };

    } else if (Prototype.Browser.WebKit) {
        isButton = function(event, code) {
            switch (code) {
                case 0: return event.which == 1 && !event.metaKey;
                case 1: return event.which == 1 && event.metaKey;
                default: return false;
            }
        };

    } else {
        isButton = function(event, code) {
            return event.which ? (event.which === code + 1) : (event.button === code);
        };
    }

    return {
        isLeftClick:   function(event) {
            return isButton(event, 0)
        },
        isMiddleClick: function(event) {
            return isButton(event, 1)
        },
        isRightClick:  function(event) {
            return isButton(event, 2)
        },

        element: function(event) {
            var node = Event.extend(event).target;
            return Element.extend(node.nodeType == Node.TEXT_NODE ? node.parentNode : node);
        },

        findElement: function(event, expression) {
            var element = Event.element(event);
            if (!expression) return element;
            var elements = [element].concat(element.ancestors());
            return Selector.findElement(elements, expression, 0);
        },

        pointer: function(event) {
            return {
                x: event.pageX || (event.clientX +
                                   (document.documentElement.scrollLeft || document.body.scrollLeft)),
                y: event.pageY || (event.clientY +
                                   (document.documentElement.scrollTop || document.body.scrollTop))
            };
        },

        pointerX: function(event) {
            return Event.pointer(event).x
        },
        pointerY: function(event) {
            return Event.pointer(event).y
        },

        stop: function(event) {
            Event.extend(event);
            event.preventDefault();
            event.stopPropagation();
            event.stopped = true;
        }
    };
})();

Event.extend = (function() {
    var methods = Object.keys(Event.Methods).inject({ }, function(m, name) {
        m[name] = Event.Methods[name].methodize();
        return m;
    });

    if (Prototype.Browser.IE) {
        Object.extend(methods, {
            stopPropagation: function() {
                this.cancelBubble = true
            },
            preventDefault:  function() {
                this.returnValue = false
            },
            inspect: function() {
                return "[object Event]"
            }
        });

        return function(event) {
            if (!event) return false;
            if (event._extendedByPrototype) return event;

            event._extendedByPrototype = Prototype.emptyFunction;
            var pointer = Event.pointer(event);
            Object.extend(event, {
                target: event.srcElement,
                relatedTarget: Event.relatedTarget(event),
                pageX:  pointer.x,
                pageY:  pointer.y
            });
            return Object.extend(event, methods);
        };

    } else {
        Event.prototype = Event.prototype || document.createEvent("HTMLEvents").__proto__;
        Object.extend(Event.prototype, methods);
        return Prototype.K;
    }
})();

Object.extend(Event, (function() {
    var cache = Event.cache;

    function getEventID(element) {
        if (element._eventID) return element._eventID;
        arguments.callee.id = arguments.callee.id || 1;
        return element._eventID = ++arguments.callee.id;
    }

    function getDOMEventName(eventName) {
        if (eventName && eventName.include(':')) return "dataavailable";
        return eventName;
    }

    function getCacheForID(id) {
        return cache[id] = cache[id] || { };
    }

    function getWrappersForEventName(id, eventName) {
        var c = getCacheForID(id);
        return c[eventName] = c[eventName] || [];
    }

    function createWrapper(element, eventName, handler) {
        var id = getEventID(element);
        var c = getWrappersForEventName(id, eventName);
        if (c.pluck("handler").include(handler)) return false;

        var wrapper = function(event) {
            if (!Event || !Event.extend ||
                (event.eventName && event.eventName != eventName))
                return false;

            Event.extend(event);
            handler.call(element, event)
        };

        wrapper.handler = handler;
        c.push(wrapper);
        return wrapper;
    }

    function findWrapper(id, eventName, handler) {
        var c = getWrappersForEventName(id, eventName);
        return c.find(function(wrapper) {
            return wrapper.handler == handler
        });
    }

    function destroyWrapper(id, eventName, handler) {
        var c = getCacheForID(id);
        if (!c[eventName]) return false;
        c[eventName] = c[eventName].without(findWrapper(id, eventName, handler));
    }

    function destroyCache() {
        for (var id in cache)
            for (var eventName in cache[id])
                cache[id][eventName] = null;
    }

    if (window.attachEvent) {
        window.attachEvent("onunload", destroyCache);
    }

    return {
        observe: function(element, eventName, handler) {
            element = $(element);
            var name = getDOMEventName(eventName);

            var wrapper = createWrapper(element, eventName, handler);
            if (!wrapper) return element;

            if (element.addEventListener) {
                element.addEventListener(name, wrapper, false);
            } else {
                element.attachEvent("on" + name, wrapper);
            }

            return element;
        },

        stopObserving: function(element, eventName, handler) {
            element = $(element);
            var id = getEventID(element), name = getDOMEventName(eventName);

            if (!handler && eventName) {
                getWrappersForEventName(id, eventName).each(function(wrapper) {
                    element.stopObserving(eventName, wrapper.handler);
                });
                return element;

            } else if (!eventName) {
                Object.keys(getCacheForID(id)).each(function(eventName) {
                    element.stopObserving(eventName);
                });
                return element;
            }

            var wrapper = findWrapper(id, eventName, handler);
            if (!wrapper) return element;

            if (element.removeEventListener) {
                element.removeEventListener(name, wrapper, false);
            } else {
                element.detachEvent("on" + name, wrapper);
            }

            destroyWrapper(id, eventName, handler);

            return element;
        },

        fire: function(element, eventName, memo) {
            element = $(element);
            if (element == document && document.createEvent && !element.dispatchEvent)
                element = document.documentElement;

            if (document.createEvent) {
                var event = document.createEvent("HTMLEvents");
                event.initEvent("dataavailable", true, true);
            } else {
                var event = document.createEventObject();
                event.eventType = "ondataavailable";
            }

            event.eventName = eventName;
            event.memo = memo || { };

            if (document.createEvent) {
                element.dispatchEvent(event);
            } else {
                element.fireEvent(event.eventType, event);
            }

            return Event.extend(event);
        }
    };
})());

Object.extend(Event, Event.Methods);

Element.addMethods({
    fire:          Event.fire,
    observe:       Event.observe,
    stopObserving: Event.stopObserving
});

Object.extend(document, {
    fire:          Element.Methods.fire.methodize(),
    observe:       Element.Methods.observe.methodize(),
    stopObserving: Element.Methods.stopObserving.methodize()
});

(function() {
    /* Support for the DOMContentLoaded event is based on work by Dan Webb,
     Matthias Miller, Dean Edwards and John Resig. */

    var timer, fired = false;

    function fireContentLoadedEvent() {
        if (fired) return;
        if (timer) window.clearInterval(timer);
        document.fire("dom:loaded");
        fired = true;
    }

    if (document.addEventListener) {
        if (Prototype.Browser.WebKit) {
            timer = window.setInterval(function() {
                if (/loaded|complete/.test(document.readyState))
                    fireContentLoadedEvent();
            }, 0);

            Event.observe(window, "load", fireContentLoadedEvent);

        } else {
            document.addEventListener("DOMContentLoaded",
                    fireContentLoadedEvent, false);
        }

    } else {
        document.write("<script id=__onDOMContentLoaded defer src=//:><\/script>");
        $("__onDOMContentLoaded").onreadystatechange = function() {
            if (this.readyState == "complete") {
                this.onreadystatechange = null;
                fireContentLoadedEvent();
            }
        };
    }
})();
var Form = {
    reset: function(form) {
        $(form).reset();
        return form;
    },

    serializeElements: function(elements, options) {
        if (typeof options != 'object') options = { hash: !!options };
        else if (Object.isUndefined(options.hash)) options.hash = true;
        var key, value, submitted = false, submit = options.submit;

        var data = elements.inject({ }, function(result, element) {
            if (!element.disabled && element.name) {
                key = element.name;
                value = $(element).getValue();
                if (value != null && (element.type != 'submit' || (!submitted &&
                                                                   submit !== false && (!submit || key == submit) && (submitted = true)))) {
                    if (key in result) {
                        // a key is already present; construct an array of values
                        if (!Object.isArray(result[key])) result[key] = [result[key]];
                        result[key].push(value);
                    }
                    else result[key] = value;
                }
            }
            return result;
        });

        return options.hash ? data : Object.toQueryString(data);
    }
};

Form.Methods = {
    serialize: function(form, options) {
        return Form.serializeElements(Form.getElements(form), options);
    },

    getElements: function(form) {
        return $A($(form).getElementsByTagName('*')).inject([],
                function(elements, child) {
                    if (Form.Element.Serializers[child.tagName.toLowerCase()])
                        elements.push(Element.extend(child));
                    return elements;
                }
                );
    },

    getInputs: function(form, typeName, name) {
        form = $(form);
        var inputs = form.getElementsByTagName('input');

        if (!typeName && !name) return $A(inputs).map(Element.extend);

        for (var i = 0, matchingInputs = [], length = inputs.length; i < length; i++) {
            var input = inputs[i];
            if ((typeName && input.type != typeName) || (name && input.name != name))
                continue;
            matchingInputs.push(Element.extend(input));
        }

        return matchingInputs;
    },

    disable: function(form) {
        form = $(form);
        Form.getElements(form).invoke('disable');
        return form;
    },

    enable: function(form) {
        form = $(form);
        Form.getElements(form).invoke('enable');
        return form;
    },

    findFirstElement: function(form) {
        var elements = $(form).getElements().findAll(function(element) {
            return 'hidden' != element.type && !element.disabled;
        });
        var firstByIndex = elements.findAll(function(element) {
            return element.hasAttribute('tabIndex') && element.tabIndex >= 0;
        }).sortBy(function(element) {
            return element.tabIndex
        }).first();

        return firstByIndex ? firstByIndex : elements.find(function(element) {
            return ['input', 'select', 'textarea'].include(element.tagName.toLowerCase());
        });
    },

    focusFirstElement: function(form) {
        form = $(form);
        form.findFirstElement().activate();
        return form;
    },

    request: function(form, options) {
        form = $(form),options = Object.clone(options || { });

        var params = options.parameters, action = form.readAttribute('action') || '';
        if (action.blank()) action = window.location.href;
        options.parameters = form.serialize(true);

        if (params) {
            if (Object.isString(params)) params = params.toQueryParams();
            Object.extend(options.parameters, params);
        }

        if (form.hasAttribute('method') && !options.method)
            options.method = form.method;

        return new Ajax.Request(action, options);
    }
};

/*--------------------------------------------------------------------------*/

Form.Element = {
    focus: function(element) {
        $(element).focus();
        return element;
    },

    select: function(element) {
        $(element).select();
        return element;
    }
};

Form.Element.Methods = {
    serialize: function(element) {
        element = $(element);
        if (!element.disabled && element.name) {
            var value = element.getValue();
            if (value != undefined) {
                var pair = { };
                pair[element.name] = value;
                return Object.toQueryString(pair);
            }
        }
        return '';
    },

    getValue: function(element) {
        element = $(element);
        var method = element.tagName.toLowerCase();
        return Form.Element.Serializers[method](element);
    },

    setValue: function(element, value) {
        element = $(element);
        var method = element.tagName.toLowerCase();
        Form.Element.Serializers[method](element, value);
        return element;
    },

    clear: function(element) {
        $(element).value = '';
        return element;
    },

    present: function(element) {
        return $(element).value != '';
    },

    activate: function(element) {
        element = $(element);
        try {
            element.focus();
            if (element.select && (element.tagName.toLowerCase() != 'input' ||
                                   !['button', 'reset', 'submit'].include(element.type)))
                element.select();
        } catch (e) {
        }
        return element;
    },

    disable: function(element) {
        element = $(element);
        element.blur();
        element.disabled = true;
        return element;
    },

    enable: function(element) {
        element = $(element);
        element.disabled = false;
        return element;
    }
};

/*--------------------------------------------------------------------------*/

var Field = Form.Element;
var $F = Form.Element.Methods.getValue;

/*--------------------------------------------------------------------------*/

Form.Element.Serializers = {
    input: function(element, value) {
        switch (element.type.toLowerCase()) {
            case 'checkbox':
            case 'radio':
                return Form.Element.Serializers.inputSelector(element, value);
            default:
                return Form.Element.Serializers.textarea(element, value);
        }
    },

    inputSelector: function(element, value) {
        if (Object.isUndefined(value)) return element.checked ? element.value : null;
        else element.checked = !!value;
    },

    textarea: function(element, value) {
        if (Object.isUndefined(value)) return element.value;
        else element.value = value;
    },

    select: function(element, index) {
        if (Object.isUndefined(index))
            return this[element.type == 'select-one' ?
                        'selectOne' : 'selectMany'](element);
        else {
            var opt, value, single = !Object.isArray(index);
            for (var i = 0, length = element.length; i < length; i++) {
                opt = element.options[i];
                value = this.optionValue(opt);
                if (single) {
                    if (value == index) {
                        opt.selected = true;
                        return;
                    }
                }
                else opt.selected = index.include(value);
            }
        }
    },

    selectOne: function(element) {
        var index = element.selectedIndex;
        return index >= 0 ? this.optionValue(element.options[index]) : null;
    },

    selectMany: function(element) {
        var values, length = element.length;
        if (!length) return null;

        for (var i = 0, values = []; i < length; i++) {
            var opt = element.options[i];
            if (opt.selected) values.push(this.optionValue(opt));
        }
        return values;
    },

    optionValue: function(opt) {
        // extend element because hasAttribute may not be native
        return Element.extend(opt).hasAttribute('value') ? opt.value : opt.text;
    }
};

/*--------------------------------------------------------------------------*/

Abstract.TimedObserver = Class.create(PeriodicalExecuter, {
    initialize: function($super, element, frequency, callback) {
        $super(callback, frequency);
        this.element = $(element);
        this.lastValue = this.getValue();
    },

    execute: function() {
        var value = this.getValue();
        if (Object.isString(this.lastValue) && Object.isString(value) ?
            this.lastValue != value : String(this.lastValue) != String(value)) {
            this.callback(this.element, value);
            this.lastValue = value;
        }
    }
});

Form.Element.Observer = Class.create(Abstract.TimedObserver, {
    getValue: function() {
        return Form.Element.getValue(this.element);
    }
});

Form.Observer = Class.create(Abstract.TimedObserver, {
    getValue: function() {
        return Form.serialize(this.element);
    }
});

/*--------------------------------------------------------------------------*/

Abstract.EventObserver = Class.create({
    initialize: function(element, callback) {
        this.element = $(element);
        this.callback = callback;

        this.lastValue = this.getValue();
        if (this.element.tagName.toLowerCase() == 'form')
            this.registerFormCallbacks();
        else
            this.registerCallback(this.element);
    },

    onElementEvent: function() {
        var value = this.getValue();
        if (this.lastValue != value) {
            this.callback(this.element, value);
            this.lastValue = value;
        }
    },

    registerFormCallbacks: function() {
        Form.getElements(this.element).each(this.registerCallback, this);
    },

    registerCallback: function(element) {
        if (element.type) {
            switch (element.type.toLowerCase()) {
                case 'checkbox':
                case 'radio':
                    Event.observe(element, 'click', this.onElementEvent.bind(this));
                    break;
                default:
                    Event.observe(element, 'change', this.onElementEvent.bind(this));
                    break;
            }
        }
    }
});

Form.Element.EventObserver = Class.create(Abstract.EventObserver, {
    getValue: function() {
        return Form.Element.getValue(this.element);
    }
});

Form.EventObserver = Class.create(Abstract.EventObserver, {
    getValue: function() {
        return Form.serialize(this.element);
    }
});
/*------------------------------- DEPRECATED -------------------------------*/

Hashtable.toQueryString = Object.toQueryString;

var Toggle = { display: Element.toggle };

Element.Methods.childOf = Element.Methods.descendantOf;

var Insertion = {
    Before: function(element, content) {
        return Element.insert(element, {before:content});
    },

    Top: function(element, content) {
        return Element.insert(element, {top:content});
    },

    Bottom: function(element, content) {
        return Element.insert(element, {bottom:content});
    },

    After: function(element, content) {
        return Element.insert(element, {after:content});
    }
};

var $continue = new Error('"throw $continue" is deprecated, use "return" instead');

// This should be moved to script.aculo.us; notice the deprecated methods
// further below, that map to the newer Element methods.
var Position = {
    // set to true if needed, warning: firefox performance problems
    // NOT neeeded for page scrolling, only if draggable contained in
    // scrollable elements
    includeScrollOffsets: false,

    // must be called before calling withinIncludingScrolloffset, every time the
    // page is scrolled
    prepare: function() {
        this.deltaX = window.pageXOffset
                || document.documentElement.scrollLeft
                || document.body.scrollLeft
                || 0;
        this.deltaY = window.pageYOffset
                || document.documentElement.scrollTop
                || document.body.scrollTop
                || 0;
    },

    // caches x/y coordinate pair to use with overlap
    within: function(element, x, y) {
        if (this.includeScrollOffsets)
            return this.withinIncludingScrolloffsets(element, x, y);
        this.xcomp = x;
        this.ycomp = y;
        this.offset = Element.cumulativeOffset(element);

        return (y >= this.offset[1] &&
                y < this.offset[1] + element.offsetHeight &&
                x >= this.offset[0] &&
                x < this.offset[0] + element.offsetWidth);
    },

    withinIncludingScrolloffsets: function(element, x, y) {
        var offsetcache = Element.cumulativeScrollOffset(element);

        this.xcomp = x + offsetcache[0] - this.deltaX;
        this.ycomp = y + offsetcache[1] - this.deltaY;
        this.offset = Element.cumulativeOffset(element);

        return (this.ycomp >= this.offset[1] &&
                this.ycomp < this.offset[1] + element.offsetHeight &&
                this.xcomp >= this.offset[0] &&
                this.xcomp < this.offset[0] + element.offsetWidth);
    },

    // within must be called directly before
    overlap: function(mode, element) {
        if (!mode) return 0;
        if (mode == 'vertical')
            return ((this.offset[1] + element.offsetHeight) - this.ycomp) /
                   element.offsetHeight;
        if (mode == 'horizontal')
            return ((this.offset[0] + element.offsetWidth) - this.xcomp) /
                   element.offsetWidth;
    },

    // Deprecation layer -- use newer Element methods now (1.5.2).

    cumulativeOffset: Element.Methods.cumulativeOffset,

    positionedOffset: Element.Methods.positionedOffset,

    absolutize: function(element) {
        Position.prepare();
        return Element.absolutize(element);
    },

    relativize: function(element) {
        Position.prepare();
        return Element.relativize(element);
    },

    realOffset: Element.Methods.cumulativeScrollOffset,

    offsetParent: Element.Methods.getOffsetParent,

    page: Element.Methods.viewportOffset,

    clone: function(source, target, options) {
        options = options || { };
        return Element.clonePosition(target, source, options);
    }
};

/*--------------------------------------------------------------------------*/

if (!document.getElementsByClassName) document.getElementsByClassName = function(instanceMethods) {
    function iter(name) {
        return name.blank() ? null : "[contains(concat(' ', @class, ' '), ' " + name + " ')]";
    }

    instanceMethods.getElementsByClassName = Prototype.BrowserFeatures.XPath ?
                                             function(element, className) {
                                                 className = className.toString().strip();
                                                 var cond = /\s/.test(className) ? $w(className).map(iter).join('') : iter(className);
                                                 return cond ? document._getElementsByXPath('.//*' + cond, element) : [];
                                             } : function(element, className) {
        className = className.toString().strip();
        var elements = [], classNames = (/\s/.test(className) ? $w(className) : null);
        if (!classNames && !className) return elements;

        var nodes = $(element).getElementsByTagName('*');
        className = ' ' + className + ' ';

        for (var i = 0, child, cn; child = nodes[i]; i++) {
            if (child.className && (cn = ' ' + child.className + ' ') && (cn.include(className) ||
                                                                          (classNames && classNames.all(function(name) {
                                                                              return !name.toString().blank() && cn.include(' ' + name + ' ');
                                                                          }))))
                elements.push(Element.extend(child));
        }
        return elements;
    };

    return function(className, parentElement) {
        return $(parentElement || document.body).getElementsByClassName(className);
    };
}(Element.Methods);

/*--------------------------------------------------------------------------*/

Element.ClassNames = Class.create();
Element.ClassNames.prototype = {
    initialize: function(element) {
        this.element = $(element);
    },

    _each: function(iterator) {
        this.element.className.split(/\s+/).select(function(name) {
            return name.length > 0;
        })._each(iterator);
    },

    set: function(className) {
        this.element.className = className;
    },

    add: function(classNameToAdd) {
        if (this.include(classNameToAdd)) return;
        this.set($A(this).concat(classNameToAdd).join(' '));
    },

    remove: function(classNameToRemove) {
        if (!this.include(classNameToRemove)) return;
        this.set($A(this).without(classNameToRemove).join(' '));
    },

    toString: function() {
        return $A(this).join(' ');
    }
};

Object.extend(Element.ClassNames.prototype, Enumerable);

/*--------------------------------------------------------------------------*/

Element.addMethods();


// Copyright (c) 2005-2007 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

var Builder = {
    NODEMAP: {
        AREA: 'map',
        CAPTION: 'table',
        COL: 'table',
        COLGROUP: 'table',
        LEGEND: 'fieldset',
        OPTGROUP: 'select',
        OPTION: 'select',
        PARAM: 'object',
        TBODY: 'table',
        TD: 'table',
        TFOOT: 'table',
        TH: 'table',
        THEAD: 'table',
        TR: 'table'
    },
    // note: For Firefox < 1.5, OPTION and OPTGROUP tags are currently broken,
    //       due to a Firefox bug
    node: function(elementName) {
        elementName = elementName.toUpperCase();

        // try innerHTML approach
        var parentTag = this.NODEMAP[elementName] || 'div';
        var parentElement = document.createElement(parentTag);
        try { // prevent IE "feature": http://dev.rubyonrails.org/ticket/2707
            parentElement.innerHTML = "<" + elementName + "></" + elementName + ">";
        } catch(e) {
        }
        var element = parentElement.firstChild || null;

        // see if browser added wrapping tags
        if (element && (element.tagName.toUpperCase() != elementName))
            element = element.getElementsByTagName(elementName)[0];

        // fallback to createElement approach
        if (!element) element = document.createElement(elementName);

        // abort if nothing could be created
        if (!element) return;

        // attributes (or text)
        if (arguments[1])
            if (this._isStringOrNumber(arguments[1]) ||
                (arguments[1] instanceof Array) ||
                arguments[1].tagName) {
                this._children(element, arguments[1]);
            } else {
                var attrs = this._attributes(arguments[1]);
                if (attrs.length) {
                    try { // prevent IE "feature": http://dev.rubyonrails.org/ticket/2707
                        parentElement.innerHTML = "<" + elementName + " " +
                                                  attrs + "></" + elementName + ">";
                    } catch(e) {
                    }
                    element = parentElement.firstChild || null;
                    // workaround firefox 1.0.X bug
                    if (!element) {
                        element = document.createElement(elementName);
                        for (attr in arguments[1])
                            element[attr == 'class' ? 'className' : attr] = arguments[1][attr];
                    }
                    if (element.tagName.toUpperCase() != elementName)
                        element = parentElement.getElementsByTagName(elementName)[0];
                }
            }

        // text, or array of children
        if (arguments[2])
            this._children(element, arguments[2]);

        return element;
    },
    _text: function(text) {
        return document.createTextNode(text);
    },

    ATTR_MAP: {
        'className': 'class',
        'htmlFor': 'for'
    },

    _attributes: function(attributes) {
        var attrs = [];
        for (attribute in attributes)
            attrs.push((attribute in this.ATTR_MAP ? this.ATTR_MAP[attribute] : attribute) +
                       '="' + attributes[attribute].toString().escapeHTML().gsub(/"/, '&quot;') + '"');
        return attrs.join(" ");
    },
    _children: function(element, children) {
        if (children.tagName) {
            element.appendChild(children);
            return;
        }
        if (typeof children == 'object') { // array can hold nodes and text
            children.flatten().each(function(e) {
                if (typeof e == 'object')
                    element.appendChild(e)
                else
                    if (Builder._isStringOrNumber(e))
                        element.appendChild(Builder._text(e));
            });
        } else
            if (Builder._isStringOrNumber(children))
                element.appendChild(Builder._text(children));
    },
    _isStringOrNumber: function(param) {
        return(typeof param == 'string' || typeof param == 'number');
    },
    build: function(html) {
        var element = this.node('div');
        $(element).update(html.strip());
        return element.down();
    },
    dump: function(scope) {
        if (typeof scope != 'object' && typeof scope != 'function') scope = window; //global scope

        var tags = ("A ABBR ACRONYM ADDRESS APPLET AREA B BASE BASEFONT BDO BIG BLOCKQUOTE BODY " +
                    "BR BUTTON CAPTION CENTER CITE CODE COL COLGROUP DD DEL DFN DIR DIV DL DT EM FIELDSET " +
                    "FONT FORM FRAME FRAMESET H1 H2 H3 H4 H5 H6 HEAD HR HTML I IFRAME IMG INPUT INS ISINDEX " +
                    "KBD LABEL LEGEND LI LINK MAP MENU META NOFRAMES NOSCRIPT OBJECT OL OPTGROUP OPTION P " +
                    "PARAM PRE Q S SAMP SCRIPT SELECT SMALL SPAN STRIKE STRONG STYLE SUB SUP TABLE TBODY TD " +
                    "TEXTAREA TFOOT TH THEAD TITLE TR TT U UL VAR").split(/\s+/);

        tags.each(function(tag) {
            scope[tag] = function() {
                return Builder.node.apply(Builder, [tag].concat($A(arguments)));
            }
        });
    }
}
// Copyright (c) 2005-2007 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
// Contributors:
//  Justin Palmer (http://encytemedia.com/)
//  Mark Pilgrim (http://diveintomark.org/)
//  Martin Bialasinki
// 
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/ 

// converts rgb() and #xxx to #xxxxxx format,  
// returns self (or first argument) if not convertable  
String.prototype.parseColor = function() {
    var color = '#';
    if (this.slice(0, 4) == 'rgb(') {
        var cols = this.slice(4, this.length - 1).split(',');
        var i = 0;
        do {
            color += parseInt(cols[i]).toColorPart()
        } while (++i < 3);
    } else {
        if (this.slice(0, 1) == '#') {
            if (this.length == 4) for (var i = 1; i < 4; i++) color += (this.charAt(i) + this.charAt(i)).toLowerCase();
            if (this.length == 7) color = this.toLowerCase();
        }
    }
    return (color.length == 7 ? color : (arguments[0] || this));
};

/*--------------------------------------------------------------------------*/

Element.collectTextNodes = function(element) {
    return $A($(element).childNodes).collect(function(node) {
        return (node.nodeType == 3 ? node.nodeValue :
                (node.hasChildNodes() ? Element.collectTextNodes(node) : ''));
    }).flatten().join('');
};

Element.collectTextNodesIgnoreClass = function(element, className) {
    return $A($(element).childNodes).collect(function(node) {
        return (node.nodeType == 3 ? node.nodeValue :
                ((node.hasChildNodes() && !Element.hasClassName(node, className)) ?
                 Element.collectTextNodesIgnoreClass(node, className) : ''));
    }).flatten().join('');
};

Element.setContentZoom = function(element, percent) {
    element = $(element);
    element.setStyle({fontSize: (percent / 100) + 'em'});
    if (Prototype.Browser.WebKit) window.scrollBy(0, 0);
    return element;
};

Element.getInlineOpacity = function(element) {
    return $(element).style.opacity || '';
};

Element.forceRerendering = function(element) {
    try {
        element = $(element);
        var n = document.createTextNode(' ');
        element.appendChild(n);
        element.removeChild(n);
    } catch(e) {
    }
};

/*--------------------------------------------------------------------------*/

var Effect = {
    _elementDoesNotExistError: {
        name: 'ElementDoesNotExistError',
        message: 'The specified DOM element does not exist, but is required for this effect to operate'
    },
    Transitions: {
        linear: Prototype.K,
        sinoidal: function(pos) {
            return (-Math.cos(pos * Math.PI) / 2) + 0.5;
        },
        reverse: function(pos) {
            return 1 - pos;
        },
        flicker: function(pos) {
            var pos = ((-Math.cos(pos * Math.PI) / 4) + 0.75) + Math.random() / 4;
            return pos > 1 ? 1 : pos;
        },
        wobble: function(pos) {
            return (-Math.cos(pos * Math.PI * (9 * pos)) / 2) + 0.5;
        },
        pulse: function(pos, pulses) {
            pulses = pulses || 5;
            return (
                    ((pos % (1 / pulses)) * pulses).round() == 0 ?
                    ((pos * pulses * 2) - (pos * pulses * 2).floor()) :
                    1 - ((pos * pulses * 2) - (pos * pulses * 2).floor())
                    );
        },
        spring: function(pos) {
            return 1 - (Math.cos(pos * 4.5 * Math.PI) * Math.exp(-pos * 6));
        },
        none: function(pos) {
            return 0;
        },
        full: function(pos) {
            return 1;
        }
    },
    DefaultOptions: {
        duration:   1.0,   // seconds
        fps:        100,   // 100= assume 66fps max.
        sync:       false, // true for combining
        from:       0.0,
        to:         1.0,
        delay:      0.0,
        queue:      'parallel'
    },
    tagifyText: function(element) {
        var tagifyStyle = 'position:relative';
        if (Prototype.Browser.IE) tagifyStyle += ';zoom:1';

        element = $(element);
        $A(element.childNodes).each(function(child) {
            if (child.nodeType == 3) {
                child.nodeValue.toArray().each(function(character) {
                    element.insertBefore(
                            new Element('span', {style: tagifyStyle}).update(
                                    character == ' ' ? String.fromCharCode(160) : character),
                            child);
                });
                Element.remove(child);
            }
        });
    },
    multiple: function(element, effect) {
        var elements;
        if (((typeof element == 'object') ||
             Object.isFunction(element)) &&
            (element.length))
            elements = element;
        else
            elements = $(element).childNodes;

        var options = Object.extend({
            speed: 0.1,
            delay: 0.0
        }, arguments[2] || { });
        var masterDelay = options.delay;

        $A(elements).each(function(element, index) {
            new effect(element, Object.extend(options, { delay: index * options.speed + masterDelay }));
        });
    },
    PAIRS: {
        'slide':  ['SlideDown','SlideUp'],
        'blind':  ['BlindDown','BlindUp'],
        'appear': ['Appear','Fade']
    },
    toggle: function(element, effect) {
        element = $(element);
        effect = (effect || 'appear').toLowerCase();
        var options = Object.extend({
            queue: { position:'end', scope:(element.id || 'global'), limit: 1 }
        }, arguments[2] || { });
        Effect[element.visible() ?
               Effect.PAIRS[effect][1] : Effect.PAIRS[effect][0]](element, options);
    }
};

Effect.DefaultOptions.transition = Effect.Transitions.sinoidal;

/* ------------- core effects ------------- */

Effect.ScopedQueue = Class.create(Enumerable, {
    initialize: function() {
        this.effects = [];
        this.interval = null;
    },
    _each: function(iterator) {
        this.effects._each(iterator);
    },
    add: function(effect) {
        var timestamp = new Date().getTime();

        var position = Object.isString(effect.options.queue) ?
                       effect.options.queue : effect.options.queue.position;

        switch (position) {
            case 'front':
                // move unstarted effects after this effect
                this.effects.findAll(function(e) {
                    return e.state == 'idle'
                }).each(function(e) {
                    e.startOn += effect.finishOn;
                    e.finishOn += effect.finishOn;
                });
                break;
            case 'with-last':
                timestamp = this.effects.pluck('startOn').max() || timestamp;
                break;
            case 'end':
                // start effect after last queued effect has finished
                timestamp = this.effects.pluck('finishOn').max() || timestamp;
                break;
        }

        effect.startOn += timestamp;
        effect.finishOn += timestamp;

        if (!effect.options.queue.limit || (this.effects.length < effect.options.queue.limit))
            this.effects.push(effect);

        if (!this.interval)
            this.interval = setInterval(this.loop.bind(this), 15);
    },
    remove: function(effect) {
        this.effects = this.effects.reject(function(e) {
            return e == effect
        });
        if (this.effects.length == 0) {
            clearInterval(this.interval);
            this.interval = null;
        }
    },
    loop: function() {
        var timePos = new Date().getTime();
        for (var i = 0, len = this.effects.length; i < len; i++)
            this.effects[i] && this.effects[i].loop(timePos);
    }
});

Effect.Queues = {
    instances: $H(),
    get: function(queueName) {
        if (!Object.isString(queueName)) return queueName;

        return this.instances.get(queueName) ||
               this.instances.set(queueName, new Effect.ScopedQueue());
    }
};
Effect.Queue = Effect.Queues.get('global');

Effect.Base = Class.create({
    position: null,
    start: function(options) {
        function codeForEvent(options, eventName) {
            return (
                    (options[eventName + 'Internal'] ? 'this.options.' + eventName + 'Internal(this);' : '') +
                    (options[eventName] ? 'this.options.' + eventName + '(this);' : '')
                    );
        }

        if (options && options.transition === false) options.transition = Effect.Transitions.linear;
        this.options = Object.extend(Object.extend({ }, Effect.DefaultOptions), options || { });
        this.currentFrame = 0;
        this.state = 'idle';
        this.startOn = this.options.delay * 1000;
        this.finishOn = this.startOn + (this.options.duration * 1000);
        this.fromToDelta = this.options.to - this.options.from;
        this.totalTime = this.finishOn - this.startOn;
        this.totalFrames = this.options.fps * this.options.duration;

        eval('this.render = function(pos){ ' +
             'if (this.state=="idle"){this.state="running";' +
             codeForEvent(this.options, 'beforeSetup') +
             (this.setup ? 'this.setup();' : '') +
             codeForEvent(this.options, 'afterSetup') +
             '};if (this.state=="running"){' +
             'pos=this.options.transition(pos)*' + this.fromToDelta + '+' + this.options.from + ';' +
             'this.position=pos;' +
             codeForEvent(this.options, 'beforeUpdate') +
             (this.update ? 'this.update(pos);' : '') +
             codeForEvent(this.options, 'afterUpdate') +
             '}}');

        this.event('beforeStart');
        if (!this.options.sync)
            Effect.Queues.get(Object.isString(this.options.queue) ?
                              'global' : this.options.queue.scope).add(this);
    },
    loop: function(timePos) {
        if (timePos >= this.startOn) {
            if (timePos >= this.finishOn) {
                this.render(1.0);
                this.cancel();
                this.event('beforeFinish');
                if (this.finish) this.finish();
                this.event('afterFinish');
                return;
            }
            var pos = (timePos - this.startOn) / this.totalTime,
                    frame = (pos * this.totalFrames).round();
            if (frame > this.currentFrame) {
                this.render(pos);
                this.currentFrame = frame;
            }
        }
    },
    cancel: function() {
        if (!this.options.sync)
            Effect.Queues.get(Object.isString(this.options.queue) ?
                              'global' : this.options.queue.scope).remove(this);
        this.state = 'finished';
    },
    event: function(eventName) {
        if (this.options[eventName + 'Internal']) this.options[eventName + 'Internal'](this);
        if (this.options[eventName]) this.options[eventName](this);
    },
    inspect: function() {
        var data = $H();
        for (property in this)
            if (!Object.isFunction(this[property])) data.set(property, this[property]);
        return '#<Effect:' + data.inspect() + ',options:' + $H(this.options).inspect() + '>';
    }
});

Effect.Parallel = Class.create(Effect.Base, {
    initialize: function(effects) {
        this.effects = effects || [];
        this.start(arguments[1]);
    },
    update: function(position) {
        this.effects.invoke('render', position);
    },
    finish: function(position) {
        this.effects.each(function(effect) {
            effect.render(1.0);
            effect.cancel();
            effect.event('beforeFinish');
            if (effect.finish) effect.finish(position);
            effect.event('afterFinish');
        });
    }
});

Effect.Tween = Class.create(Effect.Base, {
    initialize: function(object, from, to) {
        object = Object.isString(object) ? $(object) : object;
        var args = $A(arguments), method = args.last(),
                options = args.length == 5 ? args[3] : null;
        this.method = Object.isFunction(method) ? method.bind(object) :
                      Object.isFunction(object[method]) ? object[method].bind(object) :
                      function(value) {
                          object[method] = value
                      };
        this.start(Object.extend({ from: from, to: to }, options || { }));
    },
    update: function(position) {
        this.method(position);
    }
});

Effect.Event = Class.create(Effect.Base, {
    initialize: function() {
        this.start(Object.extend({ duration: 0 }, arguments[0] || { }));
    },
    update: Prototype.emptyFunction
});

Effect.Opacity = Class.create(Effect.Base, {
    initialize: function(element) {
        this.element = $(element);
        if (!this.element) throw(Effect._elementDoesNotExistError);
        // make this work on IE on elements without 'layout'
        if (Prototype.Browser.IE && (!this.element.currentStyle.hasLayout))
            this.element.setStyle({zoom: 1});
        var options = Object.extend({
            from: this.element.getOpacity() || 0.0,
            to:   1.0
        }, arguments[1] || { });
        this.start(options);
    },
    update: function(position) {
        this.element.setOpacity(position);
    }
});

Effect.Move = Class.create(Effect.Base, {
    initialize: function(element) {
        this.element = $(element);
        if (!this.element) throw(Effect._elementDoesNotExistError);
        var options = Object.extend({
            x:    0,
            y:    0,
            mode: 'relative'
        }, arguments[1] || { });
        this.start(options);
    },
    setup: function() {
        this.element.makePositioned();
        this.originalLeft = parseFloat(this.element.getStyle('left') || '0');
        this.originalTop = parseFloat(this.element.getStyle('top') || '0');
        if (this.options.mode == 'absolute') {
            this.options.x = this.options.x - this.originalLeft;
            this.options.y = this.options.y - this.originalTop;
        }
    },
    update: function(position) {
        this.element.setStyle({
            left: (this.options.x * position + this.originalLeft).round() + 'px',
            top:  (this.options.y * position + this.originalTop).round() + 'px'
        });
    }
});

// for backwards compatibility
Effect.MoveBy = function(element, toTop, toLeft) {
    return new Effect.Move(element,
            Object.extend({ x: toLeft, y: toTop }, arguments[3] || { }));
};

Effect.Scale = Class.create(Effect.Base, {
    initialize: function(element, percent) {
        this.element = $(element);
        if (!this.element) throw(Effect._elementDoesNotExistError);
        var options = Object.extend({
            scaleX: true,
            scaleY: true,
            scaleContent: true,
            scaleFromCenter: false,
            scaleMode: 'box',        // 'box' or 'contents' or { } with provided values
            scaleFrom: 100.0,
            scaleTo:   percent
        }, arguments[2] || { });
        this.start(options);
    },
    setup: function() {
        this.restoreAfterFinish = this.options.restoreAfterFinish || false;
        this.elementPositioning = this.element.getStyle('position');

        this.originalStyle = { };
        ['top','left','width','height','fontSize'].each(function(k) {
            this.originalStyle[k] = this.element.style[k];
        }.bind(this));

        this.originalTop = this.element.offsetTop;
        this.originalLeft = this.element.offsetLeft;

        var fontSize = this.element.getStyle('font-size') || '100%';
        ['em','px','%','pt'].each(function(fontSizeType) {
            if (fontSize.indexOf(fontSizeType) > 0) {
                this.fontSize = parseFloat(fontSize);
                this.fontSizeType = fontSizeType;
            }
        }.bind(this));

        this.factor = (this.options.scaleTo - this.options.scaleFrom) / 100;

        this.dims = null;
        if (this.options.scaleMode == 'box')
            this.dims = [this.element.offsetHeight, this.element.offsetWidth];
        if (/^content/.test(this.options.scaleMode))
            this.dims = [this.element.scrollHeight, this.element.scrollWidth];
        if (!this.dims)
            this.dims = [this.options.scaleMode.originalHeight,
                this.options.scaleMode.originalWidth];
    },
    update: function(position) {
        var currentScale = (this.options.scaleFrom / 100.0) + (this.factor * position);
        if (this.options.scaleContent && this.fontSize)
            this.element.setStyle({fontSize: this.fontSize * currentScale + this.fontSizeType });
        this.setDimensions(this.dims[0] * currentScale, this.dims[1] * currentScale);
    },
    finish: function(position) {
        if (this.restoreAfterFinish) this.element.setStyle(this.originalStyle);
    },
    setDimensions: function(height, width) {
        var d = { };
        if (this.options.scaleX) d.width = width.round() + 'px';
        if (this.options.scaleY) d.height = height.round() + 'px';
        if (this.options.scaleFromCenter) {
            var topd = (height - this.dims[0]) / 2;
            var leftd = (width - this.dims[1]) / 2;
            if (this.elementPositioning == 'absolute') {
                if (this.options.scaleY) d.top = this.originalTop - topd + 'px';
                if (this.options.scaleX) d.left = this.originalLeft - leftd + 'px';
            } else {
                if (this.options.scaleY) d.top = -topd + 'px';
                if (this.options.scaleX) d.left = -leftd + 'px';
            }
        }
        this.element.setStyle(d);
    }
});

Effect.Highlight = Class.create(Effect.Base, {
    initialize: function(element) {
        this.element = $(element);
        if (!this.element) throw(Effect._elementDoesNotExistError);
        var options = Object.extend({ startcolor: '#ffff99' }, arguments[1] || { });
        this.start(options);
    },
    setup: function() {
        // Prevent executing on elements not in the layout flow
        if (this.element.getStyle('display') == 'none') {
            this.cancel();
            return;
        }
        // Disable background image during the effect
        this.oldStyle = { };
        if (!this.options.keepBackgroundImage) {
            this.oldStyle.backgroundImage = this.element.getStyle('background-image');
            this.element.setStyle({backgroundImage: 'none'});
        }
        if (!this.options.endcolor)
            this.options.endcolor = this.element.getStyle('background-color').parseColor('#ffffff');
        if (!this.options.restorecolor)
            this.options.restorecolor = this.element.getStyle('background-color');
        // init color calculations
        this._base = $R(0, 2).map(function(i) {
            return parseInt(this.options.startcolor.slice(i * 2 + 1, i * 2 + 3), 16)
        }.bind(this));
        this._delta = $R(0, 2).map(function(i) {
            return parseInt(this.options.endcolor.slice(i * 2 + 1, i * 2 + 3), 16) - this._base[i]
        }.bind(this));
    },
    update: function(position) {
        this.element.setStyle({backgroundColor: $R(0, 2).inject('#', function(m, v, i) {
            return m + ((this._base[i] + (this._delta[i] * position)).round().toColorPart());
        }.bind(this)) });
    },
    finish: function() {
        this.element.setStyle(Object.extend(this.oldStyle, {
            backgroundColor: this.options.restorecolor
        }));
    }
});

Effect.ScrollTo = function(element) {
    var options = arguments[1] || { },
            scrollOffsets = document.viewport.getScrollOffsets(),
            elementOffsets = $(element).cumulativeOffset(),
            max = document.viewport.getScrollOffsets[0] - document.viewport.getHeight();

    if (options.offset) elementOffsets[1] += options.offset;

    return new Effect.Tween(null,
            scrollOffsets.top,
            elementOffsets[1] > max ? max : elementOffsets[1],
            options,
            function(p) {
                scrollTo(scrollOffsets.left, p.round())
            }
            );
};

/* ------------- combination effects ------------- */

Effect.Fade = function(element) {
    element = $(element);
    var oldOpacity = element.getInlineOpacity();
    var options = Object.extend({
        from: element.getOpacity() || 1.0,
        to:   0.0,
        afterFinishInternal: function(effect) {
            if (effect.options.to != 0) return;
            effect.element.hide().setStyle({opacity: oldOpacity});
        }
    }, arguments[1] || { });
    return new Effect.Opacity(element, options);
};

Effect.Appear = function(element) {
    element = $(element);
    var options = Object.extend({
        from: (element.getStyle('display') == 'none' ? 0.0 : element.getOpacity() || 0.0),
        to:   1.0,
        // force Safari to render floated elements properly
        afterFinishInternal: function(effect) {
            effect.element.forceRerendering();
        },
        beforeSetup: function(effect) {
            effect.element.setOpacity(effect.options.from).show();
        }}, arguments[1] || { });
    return new Effect.Opacity(element, options);
};

Effect.Puff = function(element) {
    element = $(element);
    var oldStyle = {
        opacity: element.getInlineOpacity(),
        position: element.getStyle('position'),
        top:  element.style.top,
        left: element.style.left,
        width: element.style.width,
        height: element.style.height
    };
    return new Effect.Parallel(
            [ new Effect.Scale(element, 200,
            { sync: true, scaleFromCenter: true, scaleContent: true, restoreAfterFinish: true }),
                new Effect.Opacity(element, { sync: true, to: 0.0 }) ],
            Object.extend({ duration: 1.0,
                beforeSetupInternal: function(effect) {
                    Position.absolutize(effect.effects[0].element)
                },
                afterFinishInternal: function(effect) {
                    effect.effects[0].element.hide().setStyle(oldStyle);
                }
            }, arguments[1] || { })
            );
};

Effect.BlindUp = function(element) {
    element = $(element);
    element.makeClipping();
    return new Effect.Scale(element, 0,
            Object.extend({ scaleContent: false,
                scaleX: false,
                restoreAfterFinish: true,
                afterFinishInternal: function(effect) {
                    effect.element.hide().undoClipping();
                }
            }, arguments[1] || { })
            );
};

Effect.BlindDown = function(element) {
    element = $(element);
    var elementDimensions = element.getDimensions();
    return new Effect.Scale(element, 100, Object.extend({
        scaleContent: false,
        scaleX: false,
        scaleFrom: 0,
        scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
        restoreAfterFinish: true,
        afterSetup: function(effect) {
            effect.element.makeClipping().setStyle({height: '0px'}).show();
        },
        afterFinishInternal: function(effect) {
            effect.element.undoClipping();
        }
    }, arguments[1] || { }));
};

Effect.SwitchOff = function(element) {
    element = $(element);
    var oldOpacity = element.getInlineOpacity();
    return new Effect.Appear(element, Object.extend({
        duration: 0.4,
        from: 0,
        transition: Effect.Transitions.flicker,
        afterFinishInternal: function(effect) {
            new Effect.Scale(effect.element, 1, {
                duration: 0.3, scaleFromCenter: true,
                scaleX: false, scaleContent: false, restoreAfterFinish: true,
                beforeSetup: function(effect) {
                    effect.element.makePositioned().makeClipping();
                },
                afterFinishInternal: function(effect) {
                    effect.element.hide().undoClipping().undoPositioned().setStyle({opacity: oldOpacity});
                }
            })
        }
    }, arguments[1] || { }));
};

Effect.DropOut = function(element) {
    element = $(element);
    var oldStyle = {
        top: element.getStyle('top'),
        left: element.getStyle('left'),
        opacity: element.getInlineOpacity() };
    return new Effect.Parallel(
            [ new Effect.Move(element, {x: 0, y: 100, sync: true }),
                new Effect.Opacity(element, { sync: true, to: 0.0 }) ],
            Object.extend(
            { duration: 0.5,
                beforeSetup: function(effect) {
                    effect.effects[0].element.makePositioned();
                },
                afterFinishInternal: function(effect) {
                    effect.effects[0].element.hide().undoPositioned().setStyle(oldStyle);
                }
            }, arguments[1] || { }));
};

Effect.Shake = function(element) {
    element = $(element);
    var options = Object.extend({
        distance: 20,
        duration: 0.5
    }, arguments[1] || {});
    var distance = parseFloat(options.distance);
    var split = parseFloat(options.duration) / 10.0;
    var oldStyle = {
        top: element.getStyle('top'),
        left: element.getStyle('left') };
    return new Effect.Move(element,
    { x:  distance, y: 0, duration: split, afterFinishInternal: function(effect) {
        new Effect.Move(effect.element,
        { x: -distance * 2, y: 0, duration: split * 2,  afterFinishInternal: function(effect) {
            new Effect.Move(effect.element,
            { x:  distance * 2, y: 0, duration: split * 2,  afterFinishInternal: function(effect) {
                new Effect.Move(effect.element,
                { x: -distance * 2, y: 0, duration: split * 2,  afterFinishInternal: function(effect) {
                    new Effect.Move(effect.element,
                    { x:  distance * 2, y: 0, duration: split * 2,  afterFinishInternal: function(effect) {
                        new Effect.Move(effect.element,
                        { x: -distance, y: 0, duration: split, afterFinishInternal: function(effect) {
                            effect.element.undoPositioned().setStyle(oldStyle);
                        }})
                    }})
                }})
            }})
        }})
    }});
};

Effect.SlideDown = function(element) {
    element = $(element).cleanWhitespace();
    // SlideDown need to have the content of the element wrapped in a container element with fixed height!
    var oldInnerBottom = element.down().getStyle('bottom');
    var elementDimensions = element.getDimensions();
    return new Effect.Scale(element, 100, Object.extend({
        scaleContent: false,
        scaleX: false,
        scaleFrom: window.opera ? 0 : 1,
        scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
        restoreAfterFinish: true,
        afterSetup: function(effect) {
            effect.element.makePositioned();
            effect.element.down().makePositioned();
            if (window.opera) effect.element.setStyle({top: ''});
            effect.element.makeClipping().setStyle({height: '0px'}).show();
        },
        afterUpdateInternal: function(effect) {
            effect.element.down().setStyle({bottom:
                    (effect.dims[0] - effect.element.clientHeight) + 'px' });
        },
        afterFinishInternal: function(effect) {
            effect.element.undoClipping().undoPositioned();
            effect.element.down().undoPositioned().setStyle({bottom: oldInnerBottom});
        }
    }, arguments[1] || { })
            );
};

Effect.SlideUp = function(element) {
    element = $(element).cleanWhitespace();
    var oldInnerBottom = element.down().getStyle('bottom');
    var elementDimensions = element.getDimensions();
    return new Effect.Scale(element, window.opera ? 0 : 1,
            Object.extend({ scaleContent: false,
                scaleX: false,
                scaleMode: 'box',
                scaleFrom: 100,
                scaleMode: {originalHeight: elementDimensions.height, originalWidth: elementDimensions.width},
                restoreAfterFinish: true,
                afterSetup: function(effect) {
                    effect.element.makePositioned();
                    effect.element.down().makePositioned();
                    if (window.opera) effect.element.setStyle({top: ''});
                    effect.element.makeClipping().show();
                },
                afterUpdateInternal: function(effect) {
                    effect.element.down().setStyle({bottom:
                            (effect.dims[0] - effect.element.clientHeight) + 'px' });
                },
                afterFinishInternal: function(effect) {
                    effect.element.hide().undoClipping().undoPositioned();
                    effect.element.down().undoPositioned().setStyle({bottom: oldInnerBottom});
                }
            }, arguments[1] || { })
            );
};

// Bug in opera makes the TD containing this element expand for a instance after finish 
Effect.Squish = function(element) {
    return new Effect.Scale(element, window.opera ? 1 : 0, {
        restoreAfterFinish: true,
        beforeSetup: function(effect) {
            effect.element.makeClipping();
        },
        afterFinishInternal: function(effect) {
            effect.element.hide().undoClipping();
        }
    });
};

Effect.Grow = function(element) {
    element = $(element);
    var options = Object.extend({
        direction: 'center',
        moveTransition: Effect.Transitions.sinoidal,
        scaleTransition: Effect.Transitions.sinoidal,
        opacityTransition: Effect.Transitions.full
    }, arguments[1] || { });
    var oldStyle = {
        top: element.style.top,
        left: element.style.left,
        height: element.style.height,
        width: element.style.width,
        opacity: element.getInlineOpacity() };

    var dims = element.getDimensions();
    var initialMoveX, initialMoveY;
    var moveX, moveY;

    switch (options.direction) {
        case 'top-left':
            initialMoveX = initialMoveY = moveX = moveY = 0;
            break;
        case 'top-right':
            initialMoveX = dims.width;
            initialMoveY = moveY = 0;
            moveX = -dims.width;
            break;
        case 'bottom-left':
            initialMoveX = moveX = 0;
            initialMoveY = dims.height;
            moveY = -dims.height;
            break;
        case 'bottom-right':
            initialMoveX = dims.width;
            initialMoveY = dims.height;
            moveX = -dims.width;
            moveY = -dims.height;
            break;
        case 'center':
            initialMoveX = dims.width / 2;
            initialMoveY = dims.height / 2;
            moveX = -dims.width / 2;
            moveY = -dims.height / 2;
            break;
    }

    return new Effect.Move(element, {
        x: initialMoveX,
        y: initialMoveY,
        duration: 0.01,
        beforeSetup: function(effect) {
            effect.element.hide().makeClipping().makePositioned();
        },
        afterFinishInternal: function(effect) {
            new Effect.Parallel(
                    [ new Effect.Opacity(effect.element, { sync: true, to: 1.0, from: 0.0, transition: options.opacityTransition }),
                        new Effect.Move(effect.element, { x: moveX, y: moveY, sync: true, transition: options.moveTransition }),
                        new Effect.Scale(effect.element, 100, {
                            scaleMode: { originalHeight: dims.height, originalWidth: dims.width },
                            sync: true, scaleFrom: window.opera ? 1 : 0, transition: options.scaleTransition, restoreAfterFinish: true})
                    ], Object.extend({
                beforeSetup: function(effect) {
                    effect.effects[0].element.setStyle({height: '0px'}).show();
                },
                afterFinishInternal: function(effect) {
                    effect.effects[0].element.undoClipping().undoPositioned().setStyle(oldStyle);
                }
            }, options)
                    )
        }
    });
};

Effect.Shrink = function(element) {
    element = $(element);
    var options = Object.extend({
        direction: 'center',
        moveTransition: Effect.Transitions.sinoidal,
        scaleTransition: Effect.Transitions.sinoidal,
        opacityTransition: Effect.Transitions.none
    }, arguments[1] || { });
    var oldStyle = {
        top: element.style.top,
        left: element.style.left,
        height: element.style.height,
        width: element.style.width,
        opacity: element.getInlineOpacity() };

    var dims = element.getDimensions();
    var moveX, moveY;

    switch (options.direction) {
        case 'top-left':
            moveX = moveY = 0;
            break;
        case 'top-right':
            moveX = dims.width;
            moveY = 0;
            break;
        case 'bottom-left':
            moveX = 0;
            moveY = dims.height;
            break;
        case 'bottom-right':
            moveX = dims.width;
            moveY = dims.height;
            break;
        case 'center':
            moveX = dims.width / 2;
            moveY = dims.height / 2;
            break;
    }

    return new Effect.Parallel(
            [ new Effect.Opacity(element, { sync: true, to: 0.0, from: 1.0, transition: options.opacityTransition }),
                new Effect.Scale(element, window.opera ? 1 : 0, { sync: true, transition: options.scaleTransition, restoreAfterFinish: true}),
                new Effect.Move(element, { x: moveX, y: moveY, sync: true, transition: options.moveTransition })
            ], Object.extend({
        beforeStartInternal: function(effect) {
            effect.effects[0].element.makePositioned().makeClipping();
        },
        afterFinishInternal: function(effect) {
            effect.effects[0].element.hide().undoClipping().undoPositioned().setStyle(oldStyle);
        }
    }, options)
            );
};

Effect.Pulsate = function(element) {
    element = $(element);
    var options = arguments[1] || { };
    var oldOpacity = element.getInlineOpacity();
    var transition = options.transition || Effect.Transitions.sinoidal;
    var reverser = function(pos) {
        return transition(1 - Effect.Transitions.pulse(pos, options.pulses))
    };
    reverser.bind(transition);
    return new Effect.Opacity(element,
            Object.extend(Object.extend({  duration: 2.0, from: 0,
                afterFinishInternal: function(effect) {
                    effect.element.setStyle({opacity: oldOpacity});
                }
            }, options), {transition: reverser}));
};

Effect.Fold = function(element) {
    element = $(element);
    var oldStyle = {
        top: element.style.top,
        left: element.style.left,
        width: element.style.width,
        height: element.style.height };
    element.makeClipping();
    return new Effect.Scale(element, 5, Object.extend({
        scaleContent: false,
        scaleX: false,
        afterFinishInternal: function(effect) {
            new Effect.Scale(element, 1, {
                scaleContent: false,
                scaleY: false,
                afterFinishInternal: function(effect) {
                    effect.element.hide().undoClipping().setStyle(oldStyle);
                } });
        }}, arguments[1] || { }));
};

Effect.Morph = Class.create(Effect.Base, {
    initialize: function(element) {
        this.element = $(element);
        if (!this.element) throw(Effect._elementDoesNotExistError);
        var options = Object.extend({
            style: { }
        }, arguments[1] || { });

        if (!Object.isString(options.style)) this.style = $H(options.style);
        else {
            if (options.style.include(':'))
                this.style = options.style.parseStyle();
            else {
                this.element.addClassName(options.style);
                this.style = $H(this.element.getStyles());
                this.element.removeClassName(options.style);
                var css = this.element.getStyles();
                this.style = this.style.reject(function(style) {
                    return style.value == css[style.key];
                });
                options.afterFinishInternal = function(effect) {
                    effect.element.addClassName(effect.options.style);
                    effect.transforms.each(function(transform) {
                        effect.element.style[transform.style] = '';
                    });
                }
            }
        }
        this.start(options);
    },

    setup: function() {
        function parseColor(color) {
            if (!color || ['rgba(0, 0, 0, 0)','transparent'].include(color)) color = '#ffffff';
            color = color.parseColor();
            return $R(0, 2).map(function(i) {
                return parseInt(color.slice(i * 2 + 1, i * 2 + 3), 16)
            });
        }

        this.transforms = this.style.map(function(pair) {
            var property = pair[0], value = pair[1], unit = null;

            if (value.parseColor('#zzzzzz') != '#zzzzzz') {
                value = value.parseColor();
                unit = 'color';
            } else if (property == 'opacity') {
                value = parseFloat(value);
                if (Prototype.Browser.IE && (!this.element.currentStyle.hasLayout))
                    this.element.setStyle({zoom: 1});
            } else if (Element.CSS_LENGTH.test(value)) {
                var components = value.match(/^([\+\-]?[0-9\.]+)(.*)$/);
                value = parseFloat(components[1]);
                unit = (components.length == 3) ? components[2] : null;
            }

            var originalValue = this.element.getStyle(property);
            return {
                style: property.camelize(),
                originalValue: unit == 'color' ? parseColor(originalValue) : parseFloat(originalValue || 0),
                targetValue: unit == 'color' ? parseColor(value) : value,
                unit: unit
            };
        }.bind(this)).reject(function(transform) {
            return (
                    (transform.originalValue == transform.targetValue) ||
                    (
                            transform.unit != 'color' &&
                            (isNaN(transform.originalValue) || isNaN(transform.targetValue))
                            )
                    )
        });
    },
    update: function(position) {
        var style = { }, transform, i = this.transforms.length;
        while (i--)
            style[(transform = this.transforms[i]).style] =
            transform.unit == 'color' ? '#' +
                                        (Math.round(transform.originalValue[0] +
                                                    (transform.targetValue[0] - transform.originalValue[0]) * position)).toColorPart() +
                                        (Math.round(transform.originalValue[1] +
                                                    (transform.targetValue[1] - transform.originalValue[1]) * position)).toColorPart() +
                                        (Math.round(transform.originalValue[2] +
                                                    (transform.targetValue[2] - transform.originalValue[2]) * position)).toColorPart() :
            (transform.originalValue +
             (transform.targetValue - transform.originalValue) * position).toFixed(3) +
            (transform.unit === null ? '' : transform.unit);
        this.element.setStyle(style, true);
    }
});

Effect.Transform = Class.create({
    initialize: function(tracks) {
        this.tracks = [];
        this.options = arguments[1] || { };
        this.addTracks(tracks);
    },
    addTracks: function(tracks) {
        tracks.each(function(track) {
            track = $H(track);
            var data = track.values().first();
            this.tracks.push($H({
                ids:     track.keys().first(),
                effect:  Effect.Morph,
                options: { style: data }
            }));
        }.bind(this));
        return this;
    },
    play: function() {
        return new Effect.Parallel(
                this.tracks.map(function(track) {
                    var ids = track.get('ids'), effect = track.get('effect'), options = track.get('options');
                    var elements = [$(ids) || $$(ids)].flatten();
                    return elements.map(function(e) {
                        return new effect(e, Object.extend({ sync:true }, options))
                    });
                }).flatten(),
                this.options
                );
    }
});

Element.CSS_PROPERTIES = $w(
        'backgroundColor backgroundPosition borderBottomColor borderBottomStyle ' +
        'borderBottomWidth borderLeftColor borderLeftStyle borderLeftWidth ' +
        'borderRightColor borderRightStyle borderRightWidth borderSpacing ' +
        'borderTopColor borderTopStyle borderTopWidth bottom clip color ' +
        'fontSize fontWeight height left letterSpacing lineHeight ' +
        'marginBottom marginLeft marginRight marginTop markerOffset maxHeight ' +
        'maxWidth minHeight minWidth opacity outlineColor outlineOffset ' +
        'outlineWidth paddingBottom paddingLeft paddingRight paddingTop ' +
        'right textIndent top width wordSpacing zIndex');

Element.CSS_LENGTH = /^(([\+\-]?[0-9\.]+)(em|ex|px|in|cm|mm|pt|pc|\%))|0$/;

String.__parseStyleElement = document.createElement('div');
String.prototype.parseStyle = function() {
    var style, styleRules = $H();
    if (Prototype.Browser.WebKit)
        style = new Element('div', {style:this}).style;
    else {
        String.__parseStyleElement.innerHTML = '<div style="' + this + '"></div>';
        style = String.__parseStyleElement.childNodes[0].style;
    }

    Element.CSS_PROPERTIES.each(function(property) {
        if (style[property]) styleRules.set(property, style[property]);
    });

    if (Prototype.Browser.IE && this.include('opacity'))
        styleRules.set('opacity', this.match(/opacity:\s*((?:0|1)?(?:\.\d*)?)/)[1]);

    return styleRules;
};

if (document.defaultView && document.defaultView.getComputedStyle) {
    Element.getStyles = function(element) {
        var css = document.defaultView.getComputedStyle($(element), null);
        return Element.CSS_PROPERTIES.inject({ }, function(styles, property) {
            styles[property] = css[property];
            return styles;
        });
    };
} else {
    Element.getStyles = function(element) {
        element = $(element);
        var css = element.currentStyle, styles;
        styles = Element.CSS_PROPERTIES.inject({ }, function(results, property) {
            results[property] = css[property];
            return results;
        });
        if (!styles.opacity) styles.opacity = element.getOpacity();
        return styles;
    };
}
;

Effect.Methods = {
    morph: function(element, style) {
        element = $(element);
        new Effect.Morph(element, Object.extend({ style: style }, arguments[2] || { }));
        return element;
    },
    visualEffect: function(element, effect, options) {
        element = $(element)
        var s = effect.dasherize().camelize(), klass = s.charAt(0).toUpperCase() + s.substring(1);
        new Effect[klass](element, options);
        return element;
    },
    highlight: function(element, options) {
        element = $(element);
        new Effect.Highlight(element, options);
        return element;
    }
};

$w('fade appear grow shrink fold blindUp blindDown slideUp slideDown ' +
   'pulsate shake puff squish switchOff dropOut').each(
        function(effect) {
            Effect.Methods[effect] = function(element, options) {
                element = $(element);
                Effect[effect.charAt(0).toUpperCase() + effect.substring(1)](element, options);
                return element;
            }
        }
        );

$w('getInlineOpacity forceRerendering setContentZoom collectTextNodes collectTextNodesIgnoreClass getStyles').each(
        function(f) {
            Effect.Methods[f] = Element[f];
        }
        );

Element.addMethods(Effect.Methods);
// Copyright (c) 2005-2007 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//           (c) 2005-2007 Sammi Williams (http://www.oriontransfer.co.nz, sammi@oriontransfer.co.nz)
// 
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

if (Object.isUndefined(Effect))
    throw("dragdrop.js requires including script.aculo.us' effects.js library");

var Droppables = {
    drops: [],

    remove: function(element) {
        this.drops = this.drops.reject(function(d) {
            return d.element == $(element)
        });
    },

    add: function(element) {
        element = $(element);
        var options = Object.extend({
            greedy:     true,
            hoverclass: null,
            tree:       false,
            scrollid:   null
        }, arguments[1] || { });

        // cache containers
        if (options.containment) {
            options._containers = [];
            var containment = options.containment;
            if (Object.isArray(containment)) {
                containment.each(function(c) {
                    options._containers.push($(c))
                });
            } else {
                options._containers.push($(containment));
            }
        }

        if (options.accept) options.accept = [options.accept].flatten();

        Element.makePositioned(element); // fix IE
        options.element = element;

        this.drops.push(options);
    },

    findDeepestChild: function(drops) {
        deepest = drops[0];

        for (i = 1; i < drops.length; ++i)
            if (Element.isParent(drops[i].element, deepest.element))
                deepest = drops[i];

        return deepest;
    },

    isContained: function(element, drop) {
        var containmentNode;
        if (drop.tree) {
            containmentNode = element.treeNode;
        } else {
            containmentNode = element.parentNode;
        }
        return drop._containers.detect(function(c) {
            return containmentNode == c
        });
    },

    isAffected: function(point, element, drop) {
        if (drop.scrollid) {
            Position.includeScrollOffsets = true;
            scrolling_element = $(drop.scrollid)
            var container_off = Element.cumulativeOffset(scrolling_element);
            if (point[1] < container_off[1] || point[1] > Element.getHeight(scrolling_element) + container_off[1])
            {
                return false;
            }
            else if (point[0] < container_off[0] || point[0] > Element.getWidth(scrolling_element) + container_off[0])
            {
                return false;
            }
        }
        return (
                (drop.element != element) &&
                ((!drop._containers) ||
                 this.isContained(element, drop)) &&
                ((!drop.accept) ||
                 (Element.classNames(element).detect(
                         function(v) {
                             return drop.accept.include(v)
                         }) )) &&
                Position.within(drop.element, point[0], point[1]) );
    },

    deactivate: function(drop) {
        if (drop.hoverclass)
            Element.removeClassName(drop.element, drop.hoverclass);
        this.last_active = null;
    },

    activate: function(drop) {
        if (drop.hoverclass)
            Element.addClassName(drop.element, drop.hoverclass);
        this.last_active = drop;
    },

    show: function(point, element) {
        if (!this.drops.length) return;
        var drop, affected = [];

        this.drops.each(function(drop) {
            if (Droppables.isAffected(point, element, drop))
                affected.push(drop);
        });

        if (affected.length > 0)
            drop = Droppables.findDeepestChild(affected);

        if (this.last_active && this.last_active != drop) this.deactivate(this.last_active);
        if (drop) {
            Position.within(drop.element, point[0], point[1]);
            if (drop.onHover)
                drop.onHover(element, drop.element, Position.overlap(drop.overlap, drop.element));

            if (drop != this.last_active) Droppables.activate(drop);
        }
    },

    fire: function(event, element) {
        if (!this.last_active) return;
        Position.prepare();

        if (this.isAffected([Event.pointerX(event), Event.pointerY(event)], element, this.last_active))
            if (this.last_active.onDrop) {
                this.last_active.onDrop(element, this.last_active.element, event);
                return true;
            }
    },

    reset: function() {
        if (this.last_active)
            this.deactivate(this.last_active);
    }
}

var Draggables = {
    drags: [],
    observers: [],

    register: function(draggable) {
        if (this.drags.length == 0) {
            this.eventMouseUp = this.endDrag.bindAsEventListener(this);
            this.eventMouseMove = this.updateDrag.bindAsEventListener(this);
            this.eventKeypress = this.keyPress.bindAsEventListener(this);

            Event.observe(document, "mouseup", this.eventMouseUp);
            Event.observe(document, "mousemove", this.eventMouseMove);
            Event.observe(document, "keypress", this.eventKeypress);
        }
        this.drags.push(draggable);
    },

    unregister: function(draggable) {
        this.drags = this.drags.reject(function(d) {
            return d == draggable
        });
        if (this.drags.length == 0) {
            Event.stopObserving(document, "mouseup", this.eventMouseUp);
            Event.stopObserving(document, "mousemove", this.eventMouseMove);
            Event.stopObserving(document, "keypress", this.eventKeypress);
        }
    },

    activate: function(draggable) {
        if (draggable.options.delay) {
            this._timeout = setTimeout(function() {
                Draggables._timeout = null;
                window.focus();
                Draggables.activeDraggable = draggable;
            }.bind(this), draggable.options.delay);
        } else {
            window.focus(); // allows keypress events if window isn't currently focused, fails for Safari
            this.activeDraggable = draggable;
        }
    },

    deactivate: function() {
        this.activeDraggable = null;
    },

    updateDrag: function(event) {
        if (!this.activeDraggable) return;
        var pointer = [Event.pointerX(event), Event.pointerY(event)];
        // Mozilla-based browsers fire successive mousemove events with
        // the same coordinates, prevent needless redrawing (moz bug?)
        if (this._lastPointer && (this._lastPointer.inspect() == pointer.inspect())) return;
        this._lastPointer = pointer;

        this.activeDraggable.updateDrag(event, pointer);
    },

    endDrag: function(event) {
        if (this._timeout) {
            clearTimeout(this._timeout);
            this._timeout = null;
        }
        if (!this.activeDraggable) return;
        this._lastPointer = null;
        this.activeDraggable.endDrag(event);
        this.activeDraggable = null;
    },

    keyPress: function(event) {
        if (this.activeDraggable)
            this.activeDraggable.keyPress(event);
    },

    addObserver: function(observer) {
        this.observers.push(observer);
        this._cacheObserverCallbacks();
    },

    removeObserver: function(element) {  // element instead of observer fixes mem leaks
        this.observers = this.observers.reject(function(o) {
            return o.element == element
        });
        this._cacheObserverCallbacks();
    },

    notify: function(eventName, draggable, event) {  // 'onStart', 'onEnd', 'onDrag'
        if (this[eventName + 'Count'] > 0)
            this.observers.each(function(o) {
                if (o[eventName]) o[eventName](eventName, draggable, event);
            });
        if (draggable.options[eventName]) draggable.options[eventName](draggable, event);
    },

    _cacheObserverCallbacks: function() {
        ['onStart','onEnd','onDrag'].each(function(eventName) {
            Draggables[eventName + 'Count'] = Draggables.observers.select(
                    function(o) {
                        return o[eventName];
                    }
                    ).length;
        });
    }
}

/*--------------------------------------------------------------------------*/

var Draggable = Class.create({
    initialize: function(element) {
        var defaults = {
            handle: false,
            reverteffect: function(element, top_offset, left_offset) {
                var dur = Math.sqrt(Math.abs(top_offset ^ 2) + Math.abs(left_offset ^ 2)) * 0.02;
                new Effect.Move(element, { x: -left_offset, y: -top_offset, duration: dur,
                    queue: {scope:'_draggable', position:'end'}
                });
            },
            endeffect: function(element) {
                var toOpacity = Object.isNumber(element._opacity) ? element._opacity : 1.0;
                new Effect.Opacity(element, {duration:0.2, from:0.7, to:toOpacity,
                    queue: {scope:'_draggable', position:'end'},
                    afterFinish: function() {
                        Draggable._dragging[element] = false
                    }
                });
            },
            zindex: 1000,
            revert: false,
            quiet: false,
            scroll: false,
            scrollSensitivity: 20,
            scrollSpeed: 15,
            snap: false,  // false, or xy or [x,y] or function(x,y){ return [x,y] }
            delay: 0
        };

        if (!arguments[1] || Object.isUndefined(arguments[1].endeffect))
            Object.extend(defaults, {
                starteffect: function(element) {
                    element._opacity = Element.getOpacity(element);
                    Draggable._dragging[element] = true;
                    new Effect.Opacity(element, {duration:0.2, from:element._opacity, to:0.7});
                }
            });

        var options = Object.extend(defaults, arguments[1] || { });

        this.element = $(element);

        if (options.handle && Object.isString(options.handle))
            this.handle = this.element.down('.' + options.handle, 0);

        if (!this.handle) this.handle = $(options.handle);
        if (!this.handle) this.handle = this.element;

        if (options.scroll && !options.scroll.scrollTo && !options.scroll.outerHTML) {
            options.scroll = $(options.scroll);
            this._isScrollChild = Element.childOf(this.element, options.scroll);
        }

        Element.makePositioned(this.element); // fix IE

        this.options = options;
        this.dragging = false;

        this.eventMouseDown = this.initDrag.bindAsEventListener(this);
        Event.observe(this.handle, "mousedown", this.eventMouseDown);

        Draggables.register(this);
    },

    destroy: function() {
        Event.stopObserving(this.handle, "mousedown", this.eventMouseDown);
        Draggables.unregister(this);
    },

    currentDelta: function() {
        return([
            parseInt(Element.getStyle(this.element, 'left') || '0'),
            parseInt(Element.getStyle(this.element, 'top') || '0')]);
    },

    initDrag: function(event) {
        if (!Object.isUndefined(Draggable._dragging[this.element]) &&
            Draggable._dragging[this.element]) return;
        if (Event.isLeftClick(event)) {
            // abort on form elements, fixes a Firefox issue
            var src = Event.element(event);
            if ((tag_name = src.tagName.toUpperCase()) && (
                    tag_name == 'INPUT' ||
                    tag_name == 'SELECT' ||
                    tag_name == 'OPTION' ||
                    tag_name == 'BUTTON' ||
                    tag_name == 'TEXTAREA')) return;

            var pointer = [Event.pointerX(event), Event.pointerY(event)];
            var pos = Position.cumulativeOffset(this.element);
            this.offset = [0,1].map(function(i) {
                return (pointer[i] - pos[i])
            });

            Draggables.activate(this);
            Event.stop(event);
        }
    },

    startDrag: function(event) {
        this.dragging = true;
        if (!this.delta)
            this.delta = this.currentDelta();

        if (this.options.zindex) {
            this.originalZ = parseInt(Element.getStyle(this.element, 'z-index') || 0);
            this.element.style.zIndex = this.options.zindex;
        }

        if (this.options.ghosting) {
            this._clone = this.element.cloneNode(true);
            this._originallyAbsolute = (this.element.getStyle('position') == 'absolute');
            if (!this._originallyAbsolute)
                Position.absolutize(this.element);
            this.element.parentNode.insertBefore(this._clone, this.element);
        }

        if (this.options.scroll) {
            if (this.options.scroll == window) {
                var where = this._getWindowScroll(this.options.scroll);
                this.originalScrollLeft = where.left;
                this.originalScrollTop = where.top;
            } else {
                this.originalScrollLeft = this.options.scroll.scrollLeft;
                this.originalScrollTop = this.options.scroll.scrollTop;
            }
        }

        Draggables.notify('onStart', this, event);

        if (this.options.starteffect) this.options.starteffect(this.element);
    },
    /************************************************************
     *  ATTENTION! ATTENTION! ATTENTION! ATTENTION! ATTENTION!
     * The following code was added as an improvement to ICEfaces,
     * and it's not part of scriptaculous. This fix should be
     * included in any future updates of the scriptaculous
     * library.
     * This fix correctly calculates the coordinates of drag and
     * drop events that occur inside scrollable divs.
     ************************************************************/
    // ICE-3457 {
    hasOffsets: function(elem) {
        var parent = elem.parentNode;
        if (parent == document || !parent) return false;
        if (parent.scrollTop != 0) return true;
        return this.hasOffsets(parent);
    },
    // }
    updateDrag: function(event, pointer) {
        if (!this.dragging) this.startDrag(event);

        if (!this.options.quiet) {
            Position.prepare();
            // ICE-3457 {
            if (this.hasOffsets(this.element)) {
                var windowOffset = document.viewport.getScrollOffsets()
                var realOffset = Position.realOffset(this.element);
                var dropOffset = new Array(pointer[0] + realOffset[0] - windowOffset[0],
                        pointer[1] + realOffset[1] - windowOffset[1]);
                Droppables.show(dropOffset, this.element);
            } else {
                // }
                Droppables.show(pointer, this.element);
            }
        }

        Draggables.notify('onDrag', this, event);

        this.draw(pointer);
        if (this.options.change) this.options.change(this);

        if (this.options.scroll) {
            this.stopScrolling();

            var p;
            if (this.options.scroll == window) {
                with (this._getWindowScroll(this.options.scroll)) {
                    p = [ left, top, left + width, top + height ];
                }
            } else {
                p = Position.page(this.options.scroll);
                p[0] += this.options.scroll.scrollLeft + Position.deltaX;
                p[1] += this.options.scroll.scrollTop + Position.deltaY;
                p.push(p[0] + this.options.scroll.offsetWidth);
                p.push(p[1] + this.options.scroll.offsetHeight);
            }
            var speed = [0,0];
            if (pointer[0] < (p[0] + this.options.scrollSensitivity)) speed[0] = pointer[0] - (p[0] + this.options.scrollSensitivity);
            if (pointer[1] < (p[1] + this.options.scrollSensitivity)) speed[1] = pointer[1] - (p[1] + this.options.scrollSensitivity);
            if (pointer[0] > (p[2] - this.options.scrollSensitivity)) speed[0] = pointer[0] - (p[2] - this.options.scrollSensitivity);
            if (pointer[1] > (p[3] - this.options.scrollSensitivity)) speed[1] = pointer[1] - (p[3] - this.options.scrollSensitivity);
            this.startScrolling(speed);
        }

        // fix AppleWebKit rendering
        if (Prototype.Browser.WebKit) window.scrollBy(0, 0);

        Event.stop(event);
    },

    finishDrag: function(event, success) {
        this.dragging = false;

        if (this.options.quiet) {
            Position.prepare();
            var pointer = [Event.pointerX(event), Event.pointerY(event)];
            Droppables.show(pointer, this.element);
        }

        if (this.options.ghosting) {
            if (!this._originallyAbsolute)
                Position.relativize(this.element);
            delete this._originallyAbsolute;
            Element.remove(this._clone);
            this._clone = null;
        }

        var dropped = false;
        if (success) {
            dropped = Droppables.fire(event, this.element);
            if (!dropped) dropped = false;
        }
        if (dropped && this.options.onDropped) this.options.onDropped(this.element);
        Draggables.notify('onEnd', this, event);

        var revert = this.options.revert;
        if (revert && Object.isFunction(revert)) revert = revert(this.element);

        var d = this.currentDelta();
        if (revert && this.options.reverteffect) {
            if (dropped == 0 || revert != 'failure')
                this.options.reverteffect(this.element,
                        d[1] - this.delta[1], d[0] - this.delta[0]);
        } else {
            this.delta = d;
        }

        if (this.options.zindex)
            this.element.style.zIndex = this.originalZ;

        if (this.options.endeffect)
            this.options.endeffect(this.element);

        Draggables.deactivate(this);
        Droppables.reset();
    },

    keyPress: function(event) {
        if (event.keyCode != Event.KEY_ESC) return;
        this.finishDrag(event, false);
        Event.stop(event);
    },

    endDrag: function(event) {
        if (!this.dragging) return;
        this.stopScrolling();
        this.finishDrag(event, true);
        Event.stop(event);
    },

    draw: function(point) {
        var pos = Position.cumulativeOffset(this.element);
        if (this.options.ghosting) {
            var r = Position.realOffset(this.element);
            pos[0] += r[0] - Position.deltaX;
            pos[1] += r[1] - Position.deltaY;
        }

        var d = this.currentDelta();
        pos[0] -= d[0];
        pos[1] -= d[1];

        if (this.options.scroll && (this.options.scroll != window && this._isScrollChild)) {
            pos[0] -= this.options.scroll.scrollLeft - this.originalScrollLeft;
            pos[1] -= this.options.scroll.scrollTop - this.originalScrollTop;
        }

        var p = [0,1].map(function(i) {
            return (point[i] - pos[i] - this.offset[i])
        }.bind(this));

        if (this.options.snap) {
            if (Object.isFunction(this.options.snap)) {
                p = this.options.snap(p[0], p[1], this);
            } else {
                if (Object.isArray(this.options.snap)) {
                    p = p.map(function(v, i) {
                        return (v / this.options.snap[i]).round() * this.options.snap[i]
                    }.bind(this))
                } else {
                    p = p.map(function(v) {
                        return (v / this.options.snap).round() * this.options.snap
                    }.bind(this))
                }
            }
        }

        var style = this.element.style;
        if ((!this.options.constraint) || (this.options.constraint == 'horizontal'))
            style.left = p[0] + "px";
        if ((!this.options.constraint) || (this.options.constraint == 'vertical'))
            style.top = p[1] + "px";

        if (style.visibility == "hidden") style.visibility = ""; // fix gecko rendering
    },

    stopScrolling: function() {
        if (this.scrollInterval) {
            clearInterval(this.scrollInterval);
            this.scrollInterval = null;
            Draggables._lastScrollPointer = null;
        }
    },

    startScrolling: function(speed) {
        if (!(speed[0] || speed[1])) return;
        this.scrollSpeed = [speed[0] * this.options.scrollSpeed,speed[1] * this.options.scrollSpeed];
        this.lastScrolled = new Date();
        this.scrollInterval = setInterval(this.scroll.bind(this), 10);
    },

    scroll: function() {
        var current = new Date();
        var delta = current - this.lastScrolled;
        this.lastScrolled = current;
        if (this.options.scroll == window) {
            with (this._getWindowScroll(this.options.scroll)) {
                if (this.scrollSpeed[0] || this.scrollSpeed[1]) {
                    var d = delta / 1000;
                    this.options.scroll.scrollTo(left + d * this.scrollSpeed[0], top + d * this.scrollSpeed[1]);
                }
            }
        } else {
            this.options.scroll.scrollLeft += this.scrollSpeed[0] * delta / 1000;
            this.options.scroll.scrollTop += this.scrollSpeed[1] * delta / 1000;
        }

        Position.prepare();
        Droppables.show(Draggables._lastPointer, this.element);
        Draggables.notify('onDrag', this);
        if (this._isScrollChild) {
            Draggables._lastScrollPointer = Draggables._lastScrollPointer || $A(Draggables._lastPointer);
            Draggables._lastScrollPointer[0] += this.scrollSpeed[0] * delta / 1000;
            Draggables._lastScrollPointer[1] += this.scrollSpeed[1] * delta / 1000;
            if (Draggables._lastScrollPointer[0] < 0)
                Draggables._lastScrollPointer[0] = 0;
            if (Draggables._lastScrollPointer[1] < 0)
                Draggables._lastScrollPointer[1] = 0;
            this.draw(Draggables._lastScrollPointer);
        }

        if (this.options.change) this.options.change(this);
    },

    _getWindowScroll: function(w) {
        var T, L, W, H;
        with (w.document) {
            if (w.document.documentElement && documentElement.scrollTop) {
                T = documentElement.scrollTop;
                L = documentElement.scrollLeft;
            } else if (w.document.body) {
                T = body.scrollTop;
                L = body.scrollLeft;
            }
            if (w.innerWidth) {
                W = w.innerWidth;
                H = w.innerHeight;
            } else if (w.document.documentElement && documentElement.clientWidth) {
                W = documentElement.clientWidth;
                H = documentElement.clientHeight;
            } else {
                W = body.offsetWidth;
                H = body.offsetHeight
            }
        }
        return { top: T, left: L, width: W, height: H };
    }
});

Draggable._dragging = { };

/*--------------------------------------------------------------------------*/

var SortableObserver = Class.create({
    initialize: function(element, observer) {
        this.element = $(element);
        this.observer = observer;
        this.lastValue = Sortable.serialize(this.element);
    },

    onStart: function() {
        this.lastValue = Sortable.serialize(this.element);
    },

    onEnd: function() {
        Sortable.unmark();
        if (this.lastValue != Sortable.serialize(this.element))
            this.observer(this.element)
    }
});

var Sortable = {
    SERIALIZE_RULE: /^[^_\-](?:[A-Za-z0-9\-\_]*)[_](.*)$/,

    sortables: { },

    _findRootElement: function(element) {
        while (element.tagName.toUpperCase() != "BODY") {
            if (element.id && Sortable.sortables[element.id]) return element;
            element = element.parentNode;
        }
    },

    options: function(element) {
        element = Sortable._findRootElement($(element));
        if (!element) return;
        return Sortable.sortables[element.id];
    },

    destroy: function(element) {
        var s = Sortable.options(element);

        if (s) {
            Draggables.removeObserver(s.element);
            s.droppables.each(function(d) {
                Droppables.remove(d)
            });
            s.draggables.invoke('destroy');

            delete Sortable.sortables[s.element.id];
        }
    },

    create: function(element) {
        element = $(element);
        var options = Object.extend({
            element:     element,
            tag:         'li',       // assumes li children, override with tag: 'tagname'
            dropOnEmpty: false,
            tree:        false,
            treeTag:     'ul',
            overlap:     'vertical', // one of 'vertical', 'horizontal'
            constraint:  'vertical', // one of 'vertical', 'horizontal', false
            containment: element,    // also takes array of elements (or id's); or false
            handle:      false,      // or a CSS class
            only:        false,
            delay:       0,
            hoverclass:  null,
            ghosting:    false,
            quiet:       false,
            scroll:      false,
            scrollSensitivity: 20,
            scrollSpeed: 15,
            format:      this.SERIALIZE_RULE,

            // these take arrays of elements or ids and can be
            // used for better initialization performance
            elements:    false,
            handles:     false,

            onChange:    Prototype.emptyFunction,
            onUpdate:    Prototype.emptyFunction
        }, arguments[1] || { });

        // clear any old sortable with same element
        this.destroy(element);

        // build options for the draggables
        var options_for_draggable = {
            revert:      true,
            quiet:       options.quiet,
            scroll:      options.scroll,
            scrollSpeed: options.scrollSpeed,
            scrollSensitivity: options.scrollSensitivity,
            delay:       options.delay,
            ghosting:    options.ghosting,
            constraint:  options.constraint,
            handle:      options.handle };

        if (options.starteffect)
            options_for_draggable.starteffect = options.starteffect;

        if (options.reverteffect)
            options_for_draggable.reverteffect = options.reverteffect;
        else
            if (options.ghosting) options_for_draggable.reverteffect = function(element) {
                element.style.top = 0;
                element.style.left = 0;
            };

        if (options.endeffect)
            options_for_draggable.endeffect = options.endeffect;

        if (options.zindex)
            options_for_draggable.zindex = options.zindex;

        // build options for the droppables
        var options_for_droppable = {
            overlap:     options.overlap,
            containment: options.containment,
            tree:        options.tree,
            hoverclass:  options.hoverclass,
            onHover:     Sortable.onHover
        }

        var options_for_tree = {
            onHover:      Sortable.onEmptyHover,
            overlap:      options.overlap,
            containment:  options.containment,
            hoverclass:   options.hoverclass
        }

        // fix for gecko engine
        Element.cleanWhitespace(element);

        options.draggables = [];
        options.droppables = [];

        // drop on empty handling
        if (options.dropOnEmpty || options.tree) {
            Droppables.add(element, options_for_tree);
            options.droppables.push(element);
        }

        (options.elements || this.findElements(element, options) || []).each(function(e, i) {
            var handle = options.handles ? $(options.handles[i]) :
                         (options.handle ? $(e).select('.' + options.handle)[0] : e);
            options.draggables.push(
                    new Draggable(e, Object.extend(options_for_draggable, { handle: handle })));
            Droppables.add(e, options_for_droppable);
            if (options.tree) e.treeNode = element;
            options.droppables.push(e);
        });

        if (options.tree) {
            (Sortable.findTreeElements(element, options) || []).each(function(e) {
                Droppables.add(e, options_for_tree);
                e.treeNode = element;
                options.droppables.push(e);
            });
        }

        // keep reference
        this.sortables[element.id] = options;

        // for onupdate
        Draggables.addObserver(new SortableObserver(element, options.onUpdate));

    },

    // return all suitable-for-sortable elements in a guaranteed order
    findElements: function(element, options) {
        return Element.findChildren(
                element, options.only, options.tree ? true : false, options.tag);
    },

    findTreeElements: function(element, options) {
        return Element.findChildren(
                element, options.only, options.tree ? true : false, options.treeTag);
    },

    onHover: function(element, dropon, overlap) {
        if (Element.isParent(dropon, element)) return;

        if (overlap > .33 && overlap < .66 && Sortable.options(dropon).tree) {
            return;
        } else if (overlap > 0.5) {
            Sortable.mark(dropon, 'before');
            if (dropon.previousSibling != element) {
                var oldParentNode = element.parentNode;
                element.style.visibility = "hidden"; // fix gecko rendering
                dropon.parentNode.insertBefore(element, dropon);
                if (dropon.parentNode != oldParentNode)
                    Sortable.options(oldParentNode).onChange(element);
                Sortable.options(dropon.parentNode).onChange(element);
            }
        } else {
            Sortable.mark(dropon, 'after');
            var nextElement = dropon.nextSibling || null;
            if (nextElement != element) {
                var oldParentNode = element.parentNode;
                element.style.visibility = "hidden"; // fix gecko rendering
                dropon.parentNode.insertBefore(element, nextElement);
                if (dropon.parentNode != oldParentNode)
                    Sortable.options(oldParentNode).onChange(element);
                Sortable.options(dropon.parentNode).onChange(element);
            }
        }
    },

    onEmptyHover: function(element, dropon, overlap) {
        var oldParentNode = element.parentNode;
        var droponOptions = Sortable.options(dropon);

        if (!Element.isParent(dropon, element)) {
            var index;

            var children = Sortable.findElements(dropon, {tag: droponOptions.tag, only: droponOptions.only});
            var child = null;

            if (children) {
                var offset = Element.offsetSize(dropon, droponOptions.overlap) * (1.0 - overlap);

                for (index = 0; index < children.length; index += 1) {
                    if (offset - Element.offsetSize(children[index], droponOptions.overlap) >= 0) {
                        offset -= Element.offsetSize(children[index], droponOptions.overlap);
                    } else if (offset - (Element.offsetSize(children[index], droponOptions.overlap) / 2) >= 0) {
                        child = index + 1 < children.length ? children[index + 1] : null;
                        break;
                    } else {
                        child = children[index];
                        break;
                    }
                }
            }

            dropon.insertBefore(element, child);

            Sortable.options(oldParentNode).onChange(element);
            droponOptions.onChange(element);
        }
    },

    unmark: function() {
        if (Sortable._marker) Sortable._marker.hide();
    },

    mark: function(dropon, position) {
        // mark on ghosting only
        var sortable = Sortable.options(dropon.parentNode);
        if (sortable && !sortable.ghosting) return;

        if (!Sortable._marker) {
            Sortable._marker =
            ($('dropmarker') || Element.extend(document.createElement('DIV'))).
                    hide().addClassName('dropmarker').setStyle({position:'absolute'});
            document.getElementsByTagName("body").item(0).appendChild(Sortable._marker);
        }
        var offsets = Position.cumulativeOffset(dropon);
        Sortable._marker.setStyle({left: offsets[0] + 'px', top: offsets[1] + 'px'});

        if (position == 'after')
            if (sortable.overlap == 'horizontal')
                Sortable._marker.setStyle({left: (offsets[0] + dropon.clientWidth) + 'px'});
            else
                Sortable._marker.setStyle({top: (offsets[1] + dropon.clientHeight) + 'px'});

        Sortable._marker.show();
    },

    _tree: function(element, options, parent) {
        var children = Sortable.findElements(element, options) || [];

        for (var i = 0; i < children.length; ++i) {
            var match = children[i].id.match(options.format);

            if (!match) continue;

            var child = {
                id: encodeURIComponent(match ? match[1] : null),
                element: element,
                parent: parent,
                children: [],
                position: parent.children.length,
                container: $(children[i]).down(options.treeTag)
            }

            /* Get the element containing the children and recurse over it */
            if (child.container)
                this._tree(child.container, options, child)

            parent.children.push(child);
        }

        return parent;
    },

    tree: function(element) {
        element = $(element);
        var sortableOptions = this.options(element);
        var options = Object.extend({
            tag: sortableOptions.tag,
            treeTag: sortableOptions.treeTag,
            only: sortableOptions.only,
            name: element.id,
            format: sortableOptions.format
        }, arguments[1] || { });

        var root = {
            id: null,
            parent: null,
            children: [],
            container: element,
            position: 0
        }

        return Sortable._tree(element, options, root);
    },

    /* Construct a [i] index for a particular node */
    _constructIndex: function(node) {
        var index = '';
        do {
            if (node.id) index = '[' + node.position + ']' + index;
        } while ((node = node.parent) != null);
        return index;
    },

    sequence: function(element) {
        element = $(element);
        var options = Object.extend(this.options(element), arguments[1] || { });

        return $(this.findElements(element, options) || []).map(function(item) {
            return item.id.match(options.format) ? item.id.match(options.format)[1] : '';
        });
    },

    setSequence: function(element, new_sequence) {
        element = $(element);
        var options = Object.extend(this.options(element), arguments[2] || { });

        var nodeMap = { };
        this.findElements(element, options).each(function(n) {
            if (n.id.match(options.format))
                nodeMap[n.id.match(options.format)[1]] = [n, n.parentNode];
            n.parentNode.removeChild(n);
        });

        new_sequence.each(function(ident) {
            var n = nodeMap[ident];
            if (n) {
                n[1].appendChild(n[0]);
                delete nodeMap[ident];
            }
        });
    },

    serialize: function(element) {
        element = $(element);
        var options = Object.extend(Sortable.options(element), arguments[1] || { });
        var name = encodeURIComponent(
                (arguments[1] && arguments[1].name) ? arguments[1].name : element.id);

        if (options.tree) {
            return Sortable.tree(element, arguments[1]).children.map(function (item) {
                return [name + Sortable._constructIndex(item) + "[id]=" +
                        encodeURIComponent(item.id)].concat(item.children.map(arguments.callee));
            }).flatten().join('&');
        } else {
            return Sortable.sequence(element, arguments[1]).map(function(item) {
                return name + "[]=" + encodeURIComponent(item);
            }).join('&');
        }
    }
}

// Returns true if child is contained within element
Element.isParent = function(child, element) {
    if (!child.parentNode || child == element) return false;
    if (child.parentNode == element) return true;
    return Element.isParent(child.parentNode, element);
}

Element.findChildren = function(element, only, recursive, tagName) {
    if (!element.hasChildNodes()) return null;
    tagName = tagName.toUpperCase();
    if (only) only = [only].flatten();
    var elements = [];
    $A(element.childNodes).each(function(e) {
        if (e.tagName && e.tagName.toUpperCase() == tagName &&
            (!only || (Element.classNames(e).detect(function(v) {
                return only.include(v)
            }))))
            elements.push(e);
        if (recursive) {
            var grandchildren = Element.findChildren(e, only, recursive, tagName);
            if (grandchildren) elements.push(grandchildren);
        }
    });

    return (elements.length > 0 ? elements.flatten() : []);
}

Element.offsetSize = function (element, type) {
    return element['offset' + ((type == 'vertical' || type == 'height') ? 'Height' : 'Width')];
}
// Copyright (c) 2005-2007 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//           (c) 2005-2007 Ivan Krstic (http://blogs.law.harvard.edu/ivan)
//           (c) 2005-2007 Jon Tirsen (http://www.tirsen.com)
// Contributors:
//  Richard Livsey
//  Rahul Bhargava
//  Rob Wills
// 
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

// Autocompleter.Base handles all the autocompletion functionality 
// that's independent of the data source for autocompletion. This
// includes drawing the autocompletion menu, observing keyboard
// and mouse events, and similar.
//
// Specific autocompleters need to provide, at the very least, 
// a getUpdatedChoices function that will be invoked every time
// the text inside the monitored textbox changes. This method 
// should get the text for which to provide autocompletion by
// invoking this.getToken(), NOT by directly accessing
// this.element.value. This is to allow incremental tokenized
// autocompletion. Specific auto-completion logic (AJAX, etc)
// belongs in getUpdatedChoices.
//
// Tokenized incremental autocompletion is enabled automatically
// when an autocompleter is instantiated with the 'tokens' option
// in the options parameter, e.g.:
// new Ajax.Autocompleter('id','upd', '/url/', { tokens: ',' });
// will incrementally autocomplete with a comma as the token.
// Additionally, ',' in the above example can be replaced with
// a token array, e.g. { tokens: [',', '\n'] } which
// enables autocompletion on multiple tokens. This is most 
// useful when one of the tokens is \n (a newline), as it 
// allows smart autocompletion after linebreaks.

if (typeof Effect == 'undefined')
    throw("controls.js requires including script.aculo.us' effects.js library");

var Autocompleter = { }
Autocompleter.Base = Class.create({
    baseInitialize: function(element, update, options) {
        element = $(element)
        this.element = element;
        this.update = $(update);
        this.hasFocus = false;
        this.changed = false;
        this.active = false;
        this.index = 0;
        this.entryCount = 0;
        this.oldElementValue = this.element.value;

        if (this.setOptions)
            this.setOptions(options);
        else
            this.options = options || { };

        this.options.paramName = this.options.paramName || this.element.name;
        this.options.tokens = this.options.tokens || [];
        this.options.frequency = this.options.frequency || 0.4;
        this.options.minChars = this.options.minChars || 1;
        this.options.onShow = this.options.onShow ||
                              function(element, update) {
                                  if (!update.style.position || update.style.position == 'absolute') {
                                      update.style.position = 'absolute';
                                      Position.clone(element, update, {
                                          setHeight: false,
                                          offsetTop: element.offsetHeight
                                      });
                                  }
                                  Effect.Appear(update, {duration:0.15});
                              };
        this.options.onHide = this.options.onHide ||
                              function(element, update) {
                                  new Effect.Fade(update, {duration:0.15})
                              };

        if (typeof(this.options.tokens) == 'string')
            this.options.tokens = new Array(this.options.tokens);
        // Force carriage returns as token delimiters anyway
        if (!this.options.tokens.include('\n'))
            this.options.tokens.push('\n');

        this.observer = null;

        this.element.setAttribute('autocomplete', 'off');

        Element.hide(this.update);

        Event.observe(this.element, 'blur', this.onBlur.bindAsEventListener(this));
        Event.observe(this.element, 'keydown', this.onKeyPress.bindAsEventListener(this));
    },

    show: function() {
        if (Element.getStyle(this.update, 'display') == 'none') this.options.onShow(this.element, this.update);
        if (!this.iefix &&
            (Prototype.Browser.IE) &&
            (Element.getStyle(this.update, 'position') == 'absolute')) {
            new Insertion.After(this.update,
                    '<iframe id="' + this.update.id + '_iefix" ' +
                    'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' +
                    'src="javascript:false;" frameborder="0" scrolling="no"></iframe>');
            this.iefix = $(this.update.id + '_iefix');
        }
        if (this.iefix) setTimeout(this.fixIEOverlapping.bind(this), 50);
    },

    fixIEOverlapping: function() {
        Position.clone(this.update, this.iefix, {setTop:(!this.update.style.height)});
        this.iefix.style.zIndex = 1;
        this.update.style.zIndex = 2;
        Element.show(this.iefix);
    },

    hide: function() {
        this.stopIndicator();
        if (Element.getStyle(this.update, 'display') != 'none') this.options.onHide(this.element, this.update);
        if (this.iefix) Element.hide(this.iefix);
    },

    startIndicator: function() {
        if (this.options.indicator) Element.show(this.options.indicator);
    },

    stopIndicator: function() {
        if (this.options.indicator) Element.hide(this.options.indicator);
    },

    onKeyPress: function(event) {
        if (this.active)
            switch (event.keyCode) {
                case Event.KEY_TAB:
                case Event.KEY_RETURN:
                    this.selectEntry();
                    Event.stop(event);
                case Event.KEY_ESC:
                    this.hide();
                    this.active = false;
                    Event.stop(event);
                    return;
                case Event.KEY_LEFT:
                case Event.KEY_RIGHT:
                    return;
                case Event.KEY_UP:
                    this.markPrevious();
                    this.render();
                    Event.stop(event);
                    return;
                case Event.KEY_DOWN:
                    this.markNext();
                    this.render();
                    Event.stop(event);
                    return;
            }
        else
            if (event.keyCode == Event.KEY_TAB || event.keyCode == Event.KEY_RETURN ||
                (Prototype.Browser.WebKit > 0 && event.keyCode == 0)) return;

        this.changed = true;
        this.hasFocus = true;

        if (this.observer) clearTimeout(this.observer);
        this.observer =
        setTimeout(this.onObserverEvent.bind(this), this.options.frequency * 1000);
    },

    activate: function() {
        this.changed = false;
        this.hasFocus = true;
        this.getUpdatedChoices();
    },

    onHover: function(event) {
        var element = Event.findElement(event, 'LI');
        if (this.index != element.autocompleteIndex)
        {
            this.index = element.autocompleteIndex;
            this.render();
        }
        Event.stop(event);
    },

    onClick: function(event) {
        var element = Event.findElement(event, 'LI');
        this.index = element.autocompleteIndex;
        this.selectEntry();
        this.hide();
    },

    onBlur: function(event) {
        // needed to make click events working
        setTimeout(this.hide.bind(this), 250);
        this.hasFocus = false;
        this.active = false;
    },

    render: function() {
        if (this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++)
                this.index == i ?
                Element.addClassName(this.getEntry(i), "selected") :
                Element.removeClassName(this.getEntry(i), "selected");
            if (this.hasFocus) {
                this.show();
                this.active = true;
            }
        } else {
            this.active = false;
            this.hide();
        }
    },

    markPrevious: function() {
        if (this.index > 0) this.index--
        else this.index = this.entryCount - 1;
        this.getEntry(this.index).scrollIntoView(true);
    },

    markNext: function() {
        if (this.index < this.entryCount - 1) this.index++
        else this.index = 0;
        this.getEntry(this.index).scrollIntoView(false);
    },

    getEntry: function(index) {
        return this.update.firstChild.childNodes[index];
    },

    getCurrentEntry: function() {
        return this.getEntry(this.index);
    },

    selectEntry: function() {
        this.active = false;
        this.updateElement(this.getCurrentEntry());
    },

    updateElement: function(selectedElement) {
        if (this.options.updateElement) {
            this.options.updateElement(selectedElement);
            return;
        }
        var value = '';
        if (this.options.select) {
            var nodes = $(selectedElement).select('.' + this.options.select) || [];
            if (nodes.length > 0) value = Element.collectTextNodes(nodes[0], this.options.select);
        } else
            value = Element.collectTextNodesIgnoreClass(selectedElement, 'informal');

        var bounds = this.getTokenBounds();
        if (bounds[0] != -1) {
            var newValue = this.element.value.substr(0, bounds[0]);
            var whitespace = this.element.value.substr(bounds[0]).match(/^\s+/);
            if (whitespace)
                newValue += whitespace[0];
            this.element.value = newValue + value + this.element.value.substr(bounds[1]);
        } else {
            this.element.value = value;
        }
        this.oldElementValue = this.element.value;
        this.element.focus();

        if (this.options.afterUpdateElement)
            this.options.afterUpdateElement(this.element, selectedElement);
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
            Element.cleanWhitespace(this.update);
            Element.cleanWhitespace(this.update.down());

            if (this.update.firstChild && this.update.down().childNodes) {
                this.entryCount =
                this.update.down().childNodes.length;
                for (var i = 0; i < this.entryCount; i++) {
                    var entry = this.getEntry(i);
                    entry.autocompleteIndex = i;
                    this.addObservers(entry);
                }
            } else {
                this.entryCount = 0;
            }

            this.stopIndicator();
            this.index = 0;

            if (this.entryCount == 1 && this.options.autoSelect) {
                this.selectEntry();
                this.hide();
            } else {
                this.render();
            }
        }
    },

    addObservers: function(element) {
        Event.observe(element, "mouseover", this.onHover.bindAsEventListener(this));
        Event.observe(element, "click", this.onClick.bindAsEventListener(this));
    },

    onObserverEvent: function() {
        this.changed = false;
        this.tokenBounds = null;
        if (this.getToken().length >= this.options.minChars) {
            this.getUpdatedChoices();
        } else {
            this.active = false;
            this.hide();
        }
        this.oldElementValue = this.element.value;
    },

    getToken: function() {
        var bounds = this.getTokenBounds();
        return this.element.value.substring(bounds[0], bounds[1]).strip();
    },

    getTokenBounds: function() {
        if (null != this.tokenBounds) return this.tokenBounds;
        var value = this.element.value;
        if (value.strip().empty()) return [-1, 0];
        var diff = arguments.callee.getFirstDifferencePos(value, this.oldElementValue);
        var offset = (diff == this.oldElementValue.length ? 1 : 0);
        var prevTokenPos = -1, nextTokenPos = value.length;
        var tp;
        for (var index = 0, l = this.options.tokens.length; index < l; ++index) {
            tp = value.lastIndexOf(this.options.tokens[index], diff + offset - 1);
            if (tp > prevTokenPos) prevTokenPos = tp;
            tp = value.indexOf(this.options.tokens[index], diff + offset);
            if (-1 != tp && tp < nextTokenPos) nextTokenPos = tp;
        }
        return (this.tokenBounds = [prevTokenPos + 1, nextTokenPos]);
    }
});

Autocompleter.Base.prototype.getTokenBounds.getFirstDifferencePos = function(newS, oldS) {
    var boundary = Math.min(newS.length, oldS.length);
    for (var index = 0; index < boundary; ++index)
        if (newS[index] != oldS[index])
            return index;
    return boundary;
};

Ajax.Autocompleter = Class.create(Autocompleter.Base, {
    initialize: function(element, update, url, options) {
        this.baseInitialize(element, update, options);
        this.options.asynchronous = true;
        this.options.onComplete = this.onComplete.bind(this);
        this.options.defaultParams = this.options.parameters || null;
        this.url = url;
    },

    getUpdatedChoices: function() {
        this.startIndicator();

        var entry = encodeURIComponent(this.options.paramName) + '=' +
                    encodeURIComponent(this.getToken());

        this.options.parameters = this.options.callback ?
                                  this.options.callback(this.element, entry) : entry;

        if (this.options.defaultParams)
            this.options.parameters += '&' + this.options.defaultParams;

        new Ajax.Request(this.url, this.options);
    },

    onComplete: function(request) {
        this.updateChoices(request.responseText);
    }
});

// The local array autocompleter. Used when you'd prefer to
// inject an array of autocompletion options into the page, rather
// than sending out Ajax queries, which can be quite slow sometimes.
//
// The constructor takes four parameters. The first two are, as usual,
// the id of the monitored textbox, and id of the autocompletion menu.
// The third is the array you want to autocomplete from, and the fourth
// is the options block.
//
// Extra local autocompletion options:
// - choices - How many autocompletion choices to offer
//
// - partialSearch - If false, the autocompleter will match entered
//                    text only at the beginning of strings in the 
//                    autocomplete array. Defaults to true, which will
//                    match text at the beginning of any *word* in the
//                    strings in the autocomplete array. If you want to
//                    search anywhere in the string, additionally set
//                    the option fullSearch to true (default: off).
//
// - fullSsearch - Search anywhere in autocomplete array strings.
//
// - partialChars - How many characters to enter before triggering
//                   a partial match (unlike minChars, which defines
//                   how many characters are required to do any match
//                   at all). Defaults to 2.
//
// - ignoreCase - Whether to ignore case when autocompleting.
//                 Defaults to true.
//
// It's possible to pass in a custom function as the 'selector' 
// option, if you prefer to write your own autocompletion logic.
// In that case, the other options above will not apply unless
// you support them.

Autocompleter.Local = Class.create(Autocompleter.Base, {
    initialize: function(element, update, array, options) {
        this.baseInitialize(element, update, options);
        this.options.array = array;
    },

    getUpdatedChoices: function() {
        this.updateChoices(this.options.selector(this));
    },

    setOptions: function(options) {
        this.options = Object.extend({
            choices: 10,
            partialSearch: true,
            partialChars: 2,
            ignoreCase: true,
            fullSearch: false,
            selector: function(instance) {
                var ret = []; // Beginning matches
                var partial = []; // Inside matches
                var entry = instance.getToken();
                var count = 0;

                for (var i = 0; i < instance.options.array.length &&
                                ret.length < instance.options.choices; i++) {

                    var elem = instance.options.array[i];
                    var foundPos = instance.options.ignoreCase ?
                                   elem.toLowerCase().indexOf(entry.toLowerCase()) :
                                   elem.indexOf(entry);

                    while (foundPos != -1) {
                        if (foundPos == 0 && elem.length != entry.length) {
                            ret.push("<li><strong>" + elem.substr(0, entry.length) + "</strong>" +
                                     elem.substr(entry.length) + "</li>");
                            break;
                        } else if (entry.length >= instance.options.partialChars &&
                                   instance.options.partialSearch && foundPos != -1) {
                            if (instance.options.fullSearch || /\s/.test(elem.substr(foundPos - 1, 1))) {
                                partial.push("<li>" + elem.substr(0, foundPos) + "<strong>" +
                                             elem.substr(foundPos, entry.length) + "</strong>" + elem.substr(
                                        foundPos + entry.length) + "</li>");
                                break;
                            }
                        }

                        foundPos = instance.options.ignoreCase ?
                                   elem.toLowerCase().indexOf(entry.toLowerCase(), foundPos + 1) :
                                   elem.indexOf(entry, foundPos + 1);

                    }
                }
                if (partial.length)
                    ret = ret.concat(partial.slice(0, instance.options.choices - ret.length))
                return "<ul>" + ret.join('') + "</ul>";
            }
        }, options || { });
    }
});

// AJAX in-place editor and collection editor
// Full rewrite by Christophe Porteneuve <tdd@tddsworld.com> (April 2007).

// Use this if you notice weird scrolling problems on some browsers,
// the DOM might be a bit confused when this gets called so do this
// waits 1 ms (with setTimeout) until it does the activation
Field.scrollFreeActivate = function(field) {
    setTimeout(function() {
        Field.activate(field);
    }, 1);
}

Ajax.InPlaceEditor = Class.create({
    initialize: function(element, url, options) {
        this.url = url;
        this.element = element = $(element);
        this.prepareOptions();
        this._controls = { };
        arguments.callee.dealWithDeprecatedOptions(options); // DEPRECATION LAYER!!!
        Object.extend(this.options, options || { });
        if (!this.options.formId && this.element.id) {
            this.options.formId = this.element.id + '-inplaceeditor';
            if ($(this.options.formId))
                this.options.formId = '';
        }
        if (this.options.externalControl)
            this.options.externalControl = $(this.options.externalControl);
        if (!this.options.externalControl)
            this.options.externalControlOnly = false;
        this._originalBackground = this.element.getStyle('background-color') || 'transparent';
        this.element.title = this.options.clickToEditText;
        this._boundCancelHandler = this.handleFormCancellation.bind(this);
        this._boundComplete = (this.options.onComplete || Prototype.emptyFunction).bind(this);
        this._boundFailureHandler = this.handleAJAXFailure.bind(this);
        this._boundSubmitHandler = this.handleFormSubmission.bind(this);
        this._boundWrapperHandler = this.wrapUp.bind(this);
        this.registerListeners();
    },
    checkForEscapeOrReturn: function(e) {
        if (!this._editing || e.ctrlKey || e.altKey || e.shiftKey) return;
        if (Event.KEY_ESC == e.keyCode)
            this.handleFormCancellation(e);
        else if (Event.KEY_RETURN == e.keyCode)
            this.handleFormSubmission(e);
    },
    createControl: function(mode, handler, extraClasses) {
        var control = this.options[mode + 'Control'];
        var text = this.options[mode + 'Text'];
        if ('button' == control) {
            var btn = document.createElement('input');
            btn.type = 'submit';
            btn.value = text;
            btn.className = 'editor_' + mode + '_button';
            if ('cancel' == mode)
                btn.onclick = this._boundCancelHandler;
            this._form.appendChild(btn);
            this._controls[mode] = btn;
        } else if ('link' == control) {
            var link = document.createElement('a');
            link.href = '#';
            link.appendChild(document.createTextNode(text));
            link.onclick = 'cancel' == mode ? this._boundCancelHandler : this._boundSubmitHandler;
            link.className = 'editor_' + mode + '_link';
            if (extraClasses)
                link.className += ' ' + extraClasses;
            this._form.appendChild(link);
            this._controls[mode] = link;
        }
    },
    createEditField: function() {
        var text = (this.options.loadTextURL ? this.options.loadingText : this.getText());
        var fld;
        if (1 >= this.options.rows && !/\r|\n/.test(this.getText())) {
            fld = document.createElement('input');
            fld.type = 'text';
            var size = this.options.size || this.options.cols || 0;
            if (0 < size) fld.size = size;
        } else {
            fld = document.createElement('textarea');
            fld.rows = (1 >= this.options.rows ? this.options.autoRows : this.options.rows);
            fld.cols = this.options.cols || 40;
        }
        fld.name = this.options.paramName;
        fld.value = text; // No HTML breaks conversion anymore
        fld.className = 'editor_field';
        if (this.options.submitOnBlur)
            fld.onblur = this._boundSubmitHandler;
        this._controls.editor = fld;
        if (this.options.loadTextURL)
            this.loadExternalText();
        this._form.appendChild(this._controls.editor);
    },
    createForm: function() {
        var ipe = this;

        function addText(mode, condition) {
            var text = ipe.options['text' + mode + 'Controls'];
            if (!text || condition === false) return;
            ipe._form.appendChild(document.createTextNode(text));
        }

        ;
        this._form = $(document.createElement('form'));
        this._form.id = this.options.formId;
        this._form.addClassName(this.options.formClassName);
        this._form.onsubmit = this._boundSubmitHandler;
        this.createEditField();
        if ('textarea' == this._controls.editor.tagName.toLowerCase())
            this._form.appendChild(document.createElement('br'));
        if (this.options.onFormCustomization)
            this.options.onFormCustomization(this, this._form);
        addText('Before', this.options.okControl || this.options.cancelControl);
        this.createControl('ok', this._boundSubmitHandler);
        addText('Between', this.options.okControl && this.options.cancelControl);
        this.createControl('cancel', this._boundCancelHandler, 'editor_cancel');
        addText('After', this.options.okControl || this.options.cancelControl);
    },
    destroy: function() {
        if (this._oldInnerHTML)
            this.element.innerHTML = this._oldInnerHTML;
        this.leaveEditMode();
        this.unregisterListeners();
    },
    enterEditMode: function(e) {
        if (this._saving || this._editing) return;
        this._editing = true;
        this.triggerCallback('onEnterEditMode');
        if (this.options.externalControl)
            this.options.externalControl.hide();
        this.element.hide();
        this.createForm();
        this.element.parentNode.insertBefore(this._form, this.element);
        if (!this.options.loadTextURL)
            this.postProcessEditField();
        if (e) Event.stop(e);
    },
    enterHover: function(e) {
        if (this.options.hoverClassName)
            this.element.addClassName(this.options.hoverClassName);
        if (this._saving) return;
        this.triggerCallback('onEnterHover');
    },
    getText: function() {
        return this.element.innerHTML;
    },
    handleAJAXFailure: function(transport) {
        this.triggerCallback('onFailure', transport);
        if (this._oldInnerHTML) {
            this.element.innerHTML = this._oldInnerHTML;
            this._oldInnerHTML = null;
        }
    },
    handleFormCancellation: function(e) {
        this.wrapUp();
        if (e) Event.stop(e);
    },
    handleFormSubmission: function(e) {
        var form = this._form;
        var value = $F(this._controls.editor);
        this.prepareSubmission();
        var params = this.options.callback(form, value) || '';
        if (Object.isString(params))
            params = params.toQueryParams();
        params.editorId = this.element.id;
        if (this.options.htmlResponse) {
            var options = Object.extend({ evalScripts: true }, this.options.ajaxOptions);
            Object.extend(options, {
                parameters: params,
                onComplete: this._boundWrapperHandler,
                onFailure: this._boundFailureHandler
            });
            new Ajax.Updater({ success: this.element }, this.url, options);
        } else {
            var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
            Object.extend(options, {
                parameters: params,
                onComplete: this._boundWrapperHandler,
                onFailure: this._boundFailureHandler
            });
            new Ajax.Request(this.url, options);
        }
        if (e) Event.stop(e);
    },
    leaveEditMode: function() {
        this.element.removeClassName(this.options.savingClassName);
        this.removeForm();
        this.leaveHover();
        this.element.style.backgroundColor = this._originalBackground;
        this.element.show();
        if (this.options.externalControl)
            this.options.externalControl.show();
        this._saving = false;
        this._editing = false;
        this._oldInnerHTML = null;
        this.triggerCallback('onLeaveEditMode');
    },
    leaveHover: function(e) {
        if (this.options.hoverClassName)
            this.element.removeClassName(this.options.hoverClassName);
        if (this._saving) return;
        this.triggerCallback('onLeaveHover');
    },
    loadExternalText: function() {
        this._form.addClassName(this.options.loadingClassName);
        this._controls.editor.disabled = true;
        var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
        Object.extend(options, {
            parameters: 'editorId=' + encodeURIComponent(this.element.id),
            onComplete: Prototype.emptyFunction,
            onSuccess: function(transport) {
                this._form.removeClassName(this.options.loadingClassName);
                var text = transport.responseText;
                if (this.options.stripLoadedTextTags)
                    text = text.stripTags();
                this._controls.editor.value = text;
                this._controls.editor.disabled = false;
                this.postProcessEditField();
            }.bind(this),
            onFailure: this._boundFailureHandler
        });
        new Ajax.Request(this.options.loadTextURL, options);
    },
    postProcessEditField: function() {
        var fpc = this.options.fieldPostCreation;
        if (fpc)
            $(this._controls.editor)['focus' == fpc ? 'focus' : 'activate']();
    },
    prepareOptions: function() {
        this.options = Object.clone(Ajax.InPlaceEditor.DefaultOptions);
        Object.extend(this.options, Ajax.InPlaceEditor.DefaultCallbacks);
        [this._extraDefaultOptions].flatten().compact().each(function(defs) {
            Object.extend(this.options, defs);
        }.bind(this));
    },
    prepareSubmission: function() {
        this._saving = true;
        this.removeForm();
        this.leaveHover();
        this.showSaving();
    },
    registerListeners: function() {
        this._listeners = { };
        var listener;
        $H(Ajax.InPlaceEditor.Listeners).each(function(pair) {
            listener = this[pair.value].bind(this);
            this._listeners[pair.key] = listener;
            if (!this.options.externalControlOnly)
                this.element.observe(pair.key, listener);
            if (this.options.externalControl)
                this.options.externalControl.observe(pair.key, listener);
        }.bind(this));
    },
    removeForm: function() {
        if (!this._form) return;
        this._form.remove();
        this._form = null;
        this._controls = { };
    },
    showSaving: function() {
        this._oldInnerHTML = this.element.innerHTML;
        this.element.innerHTML = this.options.savingText;
        this.element.addClassName(this.options.savingClassName);
        this.element.style.backgroundColor = this._originalBackground;
        this.element.show();
    },
    triggerCallback: function(cbName, arg) {
        if ('function' == typeof this.options[cbName]) {
            this.options[cbName](this, arg);
        }
    },
    unregisterListeners: function() {
        $H(this._listeners).each(function(pair) {
            if (!this.options.externalControlOnly)
                this.element.stopObserving(pair.key, pair.value);
            if (this.options.externalControl)
                this.options.externalControl.stopObserving(pair.key, pair.value);
        }.bind(this));
    },
    wrapUp: function(transport) {
        this.leaveEditMode();
        // Can't use triggerCallback due to backward compatibility: requires
        // binding + direct element
        this._boundComplete(transport, this.element);
    }
});

Object.extend(Ajax.InPlaceEditor.prototype, {
    dispose: Ajax.InPlaceEditor.prototype.destroy
});

Ajax.InPlaceCollectionEditor = Class.create(Ajax.InPlaceEditor, {
    initialize: function($super, element, url, options) {
        this._extraDefaultOptions = Ajax.InPlaceCollectionEditor.DefaultOptions;
        $super(element, url, options);
    },

    createEditField: function() {
        var list = document.createElement('select');
        list.name = this.options.paramName;
        list.size = 1;
        this._controls.editor = list;
        this._collection = this.options.collection || [];
        if (this.options.loadCollectionURL)
            this.loadCollection();
        else
            this.checkForExternalText();
        this._form.appendChild(this._controls.editor);
    },

    loadCollection: function() {
        this._form.addClassName(this.options.loadingClassName);
        this.showLoadingText(this.options.loadingCollectionText);
        var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
        Object.extend(options, {
            parameters: 'editorId=' + encodeURIComponent(this.element.id),
            onComplete: Prototype.emptyFunction,
            onSuccess: function(transport) {
                var js = transport.responseText.strip();
                if (!/^\[.*\]$/.test(js)) // TODO: improve sanity check
                    throw 'Server returned an invalid collection representation.';
                this._collection = eval(js);
                this.checkForExternalText();
            }.bind(this),
            onFailure: this.onFailure
        });
        new Ajax.Request(this.options.loadCollectionURL, options);
    },

    showLoadingText: function(text) {
        this._controls.editor.disabled = true;
        var tempOption = this._controls.editor.firstChild;
        if (!tempOption) {
            tempOption = document.createElement('option');
            tempOption.value = '';
            this._controls.editor.appendChild(tempOption);
            tempOption.selected = true;
        }
        tempOption.update((text || '').stripScripts().stripTags());
    },

    checkForExternalText: function() {
        this._text = this.getText();
        if (this.options.loadTextURL)
            this.loadExternalText();
        else
            this.buildOptionList();
    },

    loadExternalText: function() {
        this.showLoadingText(this.options.loadingText);
        var options = Object.extend({ method: 'get' }, this.options.ajaxOptions);
        Object.extend(options, {
            parameters: 'editorId=' + encodeURIComponent(this.element.id),
            onComplete: Prototype.emptyFunction,
            onSuccess: function(transport) {
                this._text = transport.responseText.strip();
                this.buildOptionList();
            }.bind(this),
            onFailure: this.onFailure
        });
        new Ajax.Request(this.options.loadTextURL, options);
    },

    buildOptionList: function() {
        this._form.removeClassName(this.options.loadingClassName);
        this._collection = this._collection.map(function(entry) {
            return 2 === entry.length ? entry : [entry, entry].flatten();
        });
        var marker = ('value' in this.options) ? this.options.value : this._text;
        var textFound = this._collection.any(function(entry) {
            return entry[0] == marker;
        }.bind(this));
        this._controls.editor.update('');
        var option;
        this._collection.each(function(entry, index) {
            option = document.createElement('option');
            option.value = entry[0];
            option.selected = textFound ? entry[0] == marker : 0 == index;
            option.appendChild(document.createTextNode(entry[1]));
            this._controls.editor.appendChild(option);
        }.bind(this));
        this._controls.editor.disabled = false;
        Field.scrollFreeActivate(this._controls.editor);
    }
});

//**** DEPRECATION LAYER FOR InPlace[Collection]Editor! ****
//**** This only  exists for a while,  in order to  let ****
//**** users adapt to  the new API.  Read up on the new ****
//**** API and convert your code to it ASAP!            ****

Ajax.InPlaceEditor.prototype.initialize.dealWithDeprecatedOptions = function(options) {
    if (!options) return;
    function fallback(name, expr) {
        if (name in options || expr === undefined) return;
        options[name] = expr;
    }

    ;
    fallback('cancelControl', (options.cancelLink ? 'link' : (options.cancelButton ? 'button' :
                                                              options.cancelLink == options.cancelButton == false ? false : undefined)));
    fallback('okControl', (options.okLink ? 'link' : (options.okButton ? 'button' :
                                                      options.okLink == options.okButton == false ? false : undefined)));
    fallback('highlightColor', options.highlightcolor);
    fallback('highlightEndColor', options.highlightendcolor);
};

Object.extend(Ajax.InPlaceEditor, {
    DefaultOptions: {
        ajaxOptions: { },
        autoRows: 3,                                // Use when multi-line w/ rows == 1
        cancelControl: 'link',                      // 'link'|'button'|false
        cancelText: 'cancel',
        clickToEditText: 'Click to edit',
        externalControl: null,                      // id|elt
        externalControlOnly: false,
        fieldPostCreation: 'activate',              // 'activate'|'focus'|false
        formClassName: 'inplaceeditor-form',
        formId: null,                               // id|elt
        highlightColor: '#ffff99',
        highlightEndColor: '#ffffff',
        hoverClassName: '',
        htmlResponse: true,
        loadingClassName: 'inplaceeditor-loading',
        loadingText: 'Loading...',
        okControl: 'button',                        // 'link'|'button'|false
        okText: 'ok',
        paramName: 'value',
        rows: 1,                                    // If 1 and multi-line, uses autoRows
        savingClassName: 'inplaceeditor-saving',
        savingText: 'Saving...',
        size: 0,
        stripLoadedTextTags: false,
        submitOnBlur: false,
        textAfterControls: '',
        textBeforeControls: '',
        textBetweenControls: ''
    },
    DefaultCallbacks: {
        callback: function(form) {
            return Form.serialize(form);
        },
        onComplete: function(transport, element) {
            // For backward compatibility, this one is bound to the IPE, and passes
            // the element directly.  It was too often customized, so we don't break it.
            new Effect.Highlight(element, {
                startcolor: this.options.highlightColor, keepBackgroundImage: true });
        },
        onEnterEditMode: null,
        onEnterHover: function(ipe) {
            ipe.element.style.backgroundColor = ipe.options.highlightColor;
            if (ipe._effect)
                ipe._effect.cancel();
        },
        onFailure: function(transport, ipe) {
            alert('Error communication with the server: ' + transport.responseText.stripTags());
        },
        onFormCustomization: null, // Takes the IPE and its generated form, after editor, before controls.
        onLeaveEditMode: null,
        onLeaveHover: function(ipe) {
            ipe._effect = new Effect.Highlight(ipe.element, {
                startcolor: ipe.options.highlightColor, endcolor: ipe.options.highlightEndColor,
                restorecolor: ipe._originalBackground, keepBackgroundImage: true
            });
        }
    },
    Listeners: {
        click: 'enterEditMode',
        keydown: 'checkForEscapeOrReturn',
        mouseover: 'enterHover',
        mouseout: 'leaveHover'
    }
});

Ajax.InPlaceCollectionEditor.DefaultOptions = {
    loadingCollectionText: 'Loading options...'
};

// Delayed observer, like Form.Element.Observer, 
// but waits for delay after last key input
// Ideal for live-search fields

Form.Element.DelayedObserver = Class.create({
    initialize: function(element, delay, callback) {
        this.delay = delay || 0.5;
        this.element = $(element);
        this.callback = callback;
        this.timer = null;
        this.lastValue = $F(this.element);
        Event.observe(this.element, 'keyup', this.delayedListener.bindAsEventListener(this));
    },
    delayedListener: function(event) {
        if (this.lastValue == $F(this.element)) return;
        if (this.timer) clearTimeout(this.timer);
        this.timer = setTimeout(this.onTimerEvent.bind(this), this.delay * 1000);
        this.lastValue = $F(this.element);
    },
    onTimerEvent: function() {
        this.timer = null;
        this.callback(this.element, $F(this.element));
    }
});
// Copyright (c) 2005-2007 Marty Haught, Thomas Fuchs 
//
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

if (!Control) var Control = { };

// options:
//  axis: 'vertical', or 'horizontal' (default)
//
// callbacks:
//  onChange(value)
//  onSlide(value)
Control.Slider = Class.create({
    initialize: function(handle, track, options) {
        var slider = this;

        if (Object.isArray(handle)) {
            this.handles = handle.collect(function(e) {
                return $(e)
            });
        } else {
            this.handles = [$(handle)];
        }

        this.track = $(track);
        this.options = options || { };

        this.axis = this.options.axis || 'horizontal';
        this.increment = this.options.increment || 1;
        this.step = parseInt(this.options.step || '1');
        this.range = this.options.range || $R(0, 1);

        this.value = 0; // assure backwards compat
        this.values = this.handles.map(function() {
            return 0
        });
        this.spans = this.options.spans ? this.options.spans.map(function(s) {
            return $(s)
        }) : false;
        this.options.startSpan = $(this.options.startSpan || null);
        this.options.endSpan = $(this.options.endSpan || null);

        this.restricted = this.options.restricted || false;

        this.maximum = this.options.maximum || this.range.end;
        this.minimum = this.options.minimum || this.range.start;

        // Will be used to align the handle onto the track, if necessary
        this.alignX = parseInt(this.options.alignX || '0');
        this.alignY = parseInt(this.options.alignY || '0');

        this.trackLength = this.maximumOffset() - this.minimumOffset();

        this.handleLength = this.isVertical() ?
                            (this.handles[0].offsetHeight != 0 ?
                             this.handles[0].offsetHeight : this.handles[0].style.height.replace(/px$/, "")) :
                            (this.handles[0].offsetWidth != 0 ? this.handles[0].offsetWidth :
                             this.handles[0].style.width.replace(/px$/, ""));

        this.active = false;
        this.dragging = false;
        this.disabled = false;

        if (this.options.disabled) this.setDisabled();

        // Allowed values array
        this.allowedValues = this.options.values ? this.options.values.sortBy(Prototype.K) : false;
        if (this.allowedValues) {
            this.minimum = this.allowedValues.min();
            this.maximum = this.allowedValues.max();
        }

        this.eventMouseDown = this.startDrag.bindAsEventListener(this);
        this.eventMouseUp = this.endDrag.bindAsEventListener(this);
        this.eventMouseMove = this.update.bindAsEventListener(this);

        // Initialize handles in reverse (make sure first handle is active)
        this.handles.each(function(h, i) {
            i = slider.handles.length - 1 - i;
            slider.setValue(parseFloat(
                    (Object.isArray(slider.options.sliderValue) ?
                     slider.options.sliderValue[i] : slider.options.sliderValue) ||
                    slider.range.start), i);
            h.makePositioned().observe("mousedown", slider.eventMouseDown);
        });

        this.track.observe("mousedown", this.eventMouseDown);
        document.observe("mouseup", this.eventMouseUp);
        document.observe("mousemove", this.eventMouseMove);

        this.initialized = true;
    },
    dispose: function() {
        var slider = this;
        Event.stopObserving(this.track, "mousedown", this.eventMouseDown);
        Event.stopObserving(document, "mouseup", this.eventMouseUp);
        Event.stopObserving(document, "mousemove", this.eventMouseMove);
        this.handles.each(function(h) {
            Event.stopObserving(h, "mousedown", slider.eventMouseDown);
        });
    },
    setDisabled: function() {
        this.disabled = true;
    },
    setEnabled: function() {
        this.disabled = false;
    },
    getNearestValue: function(value) {
        if (this.allowedValues) {
            if (value >= this.allowedValues.max()) return(this.allowedValues.max());
            if (value <= this.allowedValues.min()) return(this.allowedValues.min());

            var offset = Math.abs(this.allowedValues[0] - value);
            var newValue = this.allowedValues[0];
            this.allowedValues.each(function(v) {
                var currentOffset = Math.abs(v - value);
                if (currentOffset <= offset) {
                    newValue = v;
                    offset = currentOffset;
                }
            });
            return newValue;
        }
        if (value > this.range.end) return this.range.end;
        if (value < this.range.start) return this.range.start;
        return value;
    },
    setValue: function(sliderValue, handleIdx) {
        if (!this.active) {
            this.activeHandleIdx = handleIdx || 0;
            this.activeHandle = this.handles[this.activeHandleIdx];
            this.updateStyles();
        }
        handleIdx = handleIdx || this.activeHandleIdx || 0;
        if (this.initialized && this.restricted) {
            if ((handleIdx > 0) && (sliderValue < this.values[handleIdx - 1]))
                sliderValue = this.values[handleIdx - 1];
            if ((handleIdx < (this.handles.length - 1)) && (sliderValue > this.values[handleIdx + 1]))
                sliderValue = this.values[handleIdx + 1];
        }
        sliderValue = this.getNearestValue(sliderValue);
        this.values[handleIdx] = sliderValue;
        this.value = this.values[0]; // assure backwards compat

        this.handles[handleIdx].style[this.isVertical() ? 'top' : 'left'] =
        this.translateToPx(sliderValue);

        this.drawSpans();
        if (!this.dragging || !this.event) this.updateFinished();
    },
    setValueBy: function(delta, handleIdx) {
        this.setValue(this.values[handleIdx || this.activeHandleIdx || 0] + delta,
                handleIdx || this.activeHandleIdx || 0);
    },
    translateToPx: function(value) {
        return Math.round(
                ((this.trackLength - this.handleLength) / (this.range.end - this.range.start)) *
                (value - this.range.start)) + "px";
    },
    translateToValue: function(offset) {
        return ((offset / (this.trackLength - this.handleLength) *
                 (this.range.end - this.range.start)) + this.range.start);
    },
    getRange: function(range) {
        var v = this.values.sortBy(Prototype.K);
        range = range || 0;
        return $R(v[range], v[range + 1]);
    },
    minimumOffset: function() {
        return(this.isVertical() ? this.alignY : this.alignX);
    },
    maximumOffset: function() {
        return(this.isVertical() ?
               (this.track.offsetHeight != 0 ? this.track.offsetHeight :
                this.track.style.height.replace(/px$/, "")) - this.alignY :
               (this.track.offsetWidth != 0 ? this.track.offsetWidth :
                this.track.style.width.replace(/px$/, "")) - this.alignX);
    },
    isVertical:  function() {
        return (this.axis == 'vertical');
    },
    drawSpans: function() {
        var slider = this;
        if (this.spans)
            $R(0, this.spans.length - 1).each(function(r) {
                slider.setSpan(slider.spans[r], slider.getRange(r))
            });
        if (this.options.startSpan)
            this.setSpan(this.options.startSpan,
                    $R(0, this.values.length > 1 ? this.getRange(0).min() : this.value));
        if (this.options.endSpan)
            this.setSpan(this.options.endSpan,
                    $R(this.values.length > 1 ? this.getRange(this.spans.length - 1).max() : this.value, this.maximum));
    },
    setSpan: function(span, range) {
        if (this.isVertical()) {
            span.style.top = this.translateToPx(range.start);
            span.style.height = this.translateToPx(range.end - range.start + this.range.start);
        } else {
            span.style.left = this.translateToPx(range.start);
            span.style.width = this.translateToPx(range.end - range.start + this.range.start);
        }
    },
    updateStyles: function() {
        this.handles.each(function(h) {
            Element.removeClassName(h, 'selected')
        });
        Element.addClassName(this.activeHandle, 'selected');
    },
    startDrag: function(event) {
        if (Event.isLeftClick(event)) {
            if (!this.disabled) {
                this.active = true;

                var handle = Event.element(event);
                var pointer = [Event.pointerX(event), Event.pointerY(event)];
                var track = handle;
                if (track == this.track) {
                    var offsets = Position.cumulativeOffset(this.track);
                    this.event = event;
                    this.setValue(this.translateToValue(
                            (this.isVertical() ? pointer[1] - offsets[1] : pointer[0] - offsets[0]) - (this.handleLength / 2)
                            ));
                    var offsets = Position.cumulativeOffset(this.activeHandle);
                    this.offsetX = (pointer[0] - offsets[0]);
                    this.offsetY = (pointer[1] - offsets[1]);
                } else {
                    // find the handle (prevents issues with Safari)
                    while ((this.handles.indexOf(handle) == -1) && handle.parentNode)
                        handle = handle.parentNode;

                    if (this.handles.indexOf(handle) != -1) {
                        this.activeHandle = handle;
                        this.activeHandleIdx = this.handles.indexOf(this.activeHandle);
                        this.updateStyles();

                        var offsets = Position.cumulativeOffset(this.activeHandle);
                        this.offsetX = (pointer[0] - offsets[0]);
                        this.offsetY = (pointer[1] - offsets[1]);
                    }
                }
            }
            Event.stop(event);
        }
    },
    update: function(event) {
        if (this.active) {
            if (!this.dragging) this.dragging = true;
            this.draw(event);
            if (Prototype.Browser.WebKit) window.scrollBy(0, 0);
            Event.stop(event);
        }
    },
    draw: function(event) {
        var pointer = [Event.pointerX(event), Event.pointerY(event)];
        var offsets = Position.cumulativeOffset(this.track);
        pointer[0] -= this.offsetX + offsets[0];
        pointer[1] -= this.offsetY + offsets[1];
        this.event = event;
        this.setValue(this.translateToValue(this.isVertical() ? pointer[1] : pointer[0]));
        if (this.initialized && this.options.onSlide)
            this.options.onSlide(this.values.length > 1 ? this.values : this.value, this);
    },
    endDrag: function(event) {
        if (this.active && this.dragging) {
            this.finishDrag(event, true);
            Event.stop(event);
        }
        this.active = false;
        this.dragging = false;
    },
    finishDrag: function(event, success) {
        this.active = false;
        this.dragging = false;
        this.updateFinished();
    },
    updateFinished: function() {
        if (this.initialized && this.options.onChange)
            this.options.onChange(this.values.length > 1 ? this.values : this.value, this);
        this.event = null;
    }
});
// Copyright (c) 2005-2007 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//
// Based on code created by Jules Gravinese (http://www.webveteran.com/)
//
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

Sound = {
    tracks: {},
    _enabled: true,
    template:
            new Template('<embed style="height:0" id="sound_#{track}_#{id}" src="#{url}" loop="false" autostart="true" hidden="true"/>'),
    enable: function() {
        Sound._enabled = true;
    },
    disable: function() {
        Sound._enabled = false;
    },
    play: function(url) {
        if (!Sound._enabled) return;
        var options = Object.extend({
            track: 'global', url: url, replace: false
        }, arguments[1] || {});

        if (options.replace && this.tracks[options.track]) {
            $R(0, this.tracks[options.track].id).each(function(id) {
                var sound = $('sound_' + options.track + '_' + id);
                sound.Stop && sound.Stop();
                sound.remove();
            })
            this.tracks[options.track] = null;
        }

        if (!this.tracks[options.track])
            this.tracks[options.track] = { id: 0 }
        else
            this.tracks[options.track].id++;

        options.id = this.tracks[options.track].id;
        $$('body')[0].insert(
                Prototype.Browser.IE ? new Element('bgsound', {
                    id: 'sound_' + options.track + '_' + options.id,
                    src: options.url, loop: 1, autostart: true
                }) : Sound.template.evaluate(options));
    }
};

if (Prototype.Browser.Gecko && navigator.userAgent.indexOf("Win") > 0) {
    if (navigator.plugins && $A(navigator.plugins).detect(function(p) {
        return p.name.indexOf('QuickTime') != -1
    }))
        Sound.template = new Template('<object id="sound_#{track}_#{id}" width="0" height="0" type="audio/mpeg" data="#{url}"/>')
    else
        Sound.play = function() {
        }
}
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

Ice.DnD = Class.create();
var IceLoaded = false;
Ice.DnD.logger = Class.create();
Ice.DnD.logger = {
    debug:function() {
    },
    warn:function() {
    },
    error:function() {
    }
};

Ice.DnD = {
    log: function(s) {
        Ice.DnD.logger.debug(s);
    },
    DRAG_START:1,
    DRAG_CANCEL:2,
    DROPPED:3,
    HOVER_START:4,
    HOVER_END:5,

    init:function() {
        Ice.DnD.logger = window.logger.child('dragDrop');
        Ice.StateMon.logger = window.logger.child('stateMon');
        Ice.StateMon.destroyAll();
        IceLoaded = true;
    },

    elementReplaced:function(ele) {
        var currentEle = $(ele.id);
        return currentEle != null && currentEle != ele;
    },

    check:function () {
        Ice.StateMon.checkAll();
    },

    alreadyDrag:function(ele) {
        ele = $(ele)
        var found = false;
        $A(Draggables.drags).each(function(drag) {
            if (drag.element && drag.element.id == ele.id) {
                found = true;
            }
        });
        return found;
    },

    sortableDraggable:function(ele) {
        ele = $(ele)
        var found = false;

        $A(Draggables.drags).each(function(drag) {
            if (drag.element && drag.element.id == ele.id) {
                if (drag.options.sort) {
                    found = true;
                }
            }
        });
        return found;
    },

    alreadyDrop:function(ele) {
        ele = $(ele)
        var found = false;
        $(Droppables.drops).each(function(drop) {
            if (drop && drop.element.id == ele.id) {

                found = true;
            }
        });
        return found;
    },

    alreadySort:function(ele) {
        var opts = Sortable.options(ele);
        if (opts)return true;
        return false;
    }
};

Ice.PanelCollapsible = {
    fire:function(eleId, hdrEleId, styleCls, usrdfndCls) {
        var ele = document.getElementById(eleId);
        var hdrEle = document.getElementById(hdrEleId);
        try {
            if (Element.visible(ele)) {
                hdrEle.className = Ice.PanelCollapsible.getStyleClass(styleCls, usrdfndCls, 'ColpsdHdr');
                Ice.PanelCollapsible.collapse(eleId);
            } else {
                hdrEle.className = Ice.PanelCollapsible.getStyleClass(styleCls, usrdfndCls, 'Hdr');
                Ice.PanelCollapsible.expand(eleId);
            }
        } catch(eee) {

            console.log("Error in panel collapsible [" + eee + "]");
        }

    },

    expand:function(eleId) {
        var ele = document.getElementById(eleId);
        if (!Element.visible(ele)) {
            Effect.SlideDown(eleId, {uploadCSS:true,submit:true,duration:0.5});
        }
    },

    collapse:function(eleId) {
        var ele = document.getElementById(eleId);
        if (Element.visible(ele)) {
            Effect.SlideUp(eleId, {uploadCSS:true,submit:true,duration:0.5});
        }
    },

    getStyleClass:function(styleCls, usrdfndCls, suffix) {
        var activeClass = styleCls + suffix;
        if (usrdfndCls != null) {
            activeClass += ' ' + usrdfndCls + suffix;
        }
        return activeClass;
    }
}

Ice.tableRowClicked = function(event, useEvent, rowid, formId, hdnFld, toggleClassNames) {
    var ctrlKyFld = document.getElementsByName(hdnFld + 'ctrKy');
    var sftKyFld = document.getElementsByName(hdnFld + 'sftKy');
    if (ctrlKyFld.length > 0) {
        ctrlKyFld = ctrlKyFld[0];
    } else {
        ctrlKyFld = null;
    }

    if (sftKyFld.length > 0) {
        sftKyFld = sftKyFld[0];
    } else {
        sftKyFld = null;
    }
    if (ctrlKyFld && event) {
        ctrlKyFld.value = event.ctrlKey || event.metaKey;
    }
    if (sftKyFld && event) {
        sftKyFld.value = event.shiftKey;
    }
    try {
        if (useEvent) {
            var targ;
            if (!event)
                var event = window.event;
            if (event.target)
                targ = event.target;
            else if (event.srcElement)
                targ = event.srcElement;
            // Some versions of Safari return the text node,
            //  while other browsers return the parent tag
            if (targ.nodeType == 3)
                targ = targ.parentNode;
            while (true) {
                var tname = targ.tagName.toLowerCase();
                if (tname == 'tr') {
                    break;
                }
                if (tname == 'input' ||
                    tname == 'select' ||
                    tname == 'option' ||
                    tname == 'a' ||
                    tname == 'textarea')
                {
                    return;
                }
                // Search up to see if we're deep within an anchor
                if (targ.parentNode)
                    targ = targ.parentNode;
                else {
                    break;
                }
            }
        }
        var evt = Event.extend(event);
        var row = evt.element();
        if (row.tagName.toLowerCase() != "tr") {
            row = evt.element().up("tr[onclick*='Ice.tableRowClicked']");
        }
        if (row) {
            // If preStyleOnSelection=false, then toggleClassNames=='', so we
            // should leave the row styling alone
            if (toggleClassNames) {
                row.className = toggleClassNames;
                row.onmouseover = Prototype.emptyFunction;
                row.onmouseout = Prototype.emptyFunction;
            }
        }
        var fld = document.forms[formId][hdnFld];
        fld.value = rowid;
        var nothingEvent = new Object();
        iceSubmitPartial(null, fld, nothingEvent);
        setFocus('');
    } catch(e) {
        console.log("Error in rowSelector[" + e + "]");
    }
}

Ice.clickEvents = {};

Ice.registerClick = function(elem, hdnClkRow, hdnClkCount, rowid, formId, delay, toggleOnClick, event, useEvent, hdnFld, toggleClassNames) {
    if (!Ice.clickEvents[elem.id]) {
        Ice.clickEvents[elem.id] = new Ice.clickEvent(elem, hdnClkRow, hdnClkCount, rowid, formId, delay, toggleOnClick, event, useEvent, hdnFld, toggleClassNames);
    }
}

Ice.registerDblClick = function(elem) {
    if (document.selection) document.selection.empty();
    if (Ice.clickEvents[elem.id]) {
        Ice.clickEvents[elem.id].submit(2);
    }
}

Ice.clickEvent = Class.create({
    initialize: function(elem, hdnClkRow, hdnClkCount, rowid, formId, delay, toggleOnClick, event, useEvent, hdnFld, toggleClassNames) {
        this.elem = elem;
        this.hdnClkRow = hdnClkRow;
        this.hdnClkCount = hdnClkCount;
        this.rowid = rowid;
        this.formId = formId;

        if (delay < 0) this.delay = 0;
        else if (delay > 1000) this.delay = 1000;
        else this.delay = delay;

        this.toggleOnClick = toggleOnClick;
        if (this.toggleOnClick) {
            this.event = Object.clone(event);
            this.useEvent = useEvent;
            this.hdnFld = hdnFld;
            this.toggleClassNames = toggleClassNames;
        }

        this.timer = setTimeout(this.submit.bind(this, 1), this.delay);
    },
    submit: function(numClicks) {
        clearTimeout(this.timer);
        Ice.clickEvents[this.elem.id] = null;
        var rowField = document.forms[this.formId][this.hdnClkRow];
        rowField.value = this.rowid;
        var countField = document.forms[this.formId][this.hdnClkCount];
        countField.value = numClicks;
        if (this.toggleOnClick) {
            Ice.tableRowClicked(this.event, this.useEvent, this.rowid, this.formId, this.hdnFld, this.toggleClassNames);
        } else {
            var nothingEvent = new Object();
            iceSubmitPartial(null, rowField, nothingEvent);
        }
    }
});

Ice.preventTextSelection = function(event) {
    var evt = event || window.event;
    var evt = Event.extend(evt);
    var elem = evt.element();
    var tag = elem.tagName.toLowerCase();
    if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea') {
        return true;
    } else {
        Ice.disableTxtSelection(document.body);
        return false;
    }
}

Ice.disableTxtSelection = function (element) {
    element.onselectstart = function () {
        return false;
    }
    element.onmousedown = function (evt) {
        return false;
    };
}

Ice.enableTxtSelection = function (element) {
    element.onselectstart = function () {
        return true;
    }
    element.onmousedown = function (evt) {
        return true;
    };
}

Ice.txtAreaMaxLen = function(field, maxlength) {
    if (maxlength >= 0 && field.value.length > maxlength) {
        field.value = field.value.substring(0, maxlength);
    }
}
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

Ice.util = Class.create();
Ice.util = {
    findForm: function(a) {
        var n = a.nodeName.toLowerCase();
        if (n == 'form') return a;
        return this.findForm(a.parentNode);
    },
    //used by the selectInputDate component
    adjustMyPosition: function(element, containerElement) {
        var elementHeight = $(element).getHeight();
        var elementCumulativeTop = Element.cumulativeOffset($(element)).top;
        var documentX = document.viewport.getScrollOffsets().top + document.viewport.getHeight();
        var containerElementTop = Element.cumulativeOffset($(containerElement)).top ;
        var diff = elementCumulativeTop - containerElementTop ;
        var elementX = elementCumulativeTop + $(element).getHeight();
        var newElementX = elementHeight + diff;

        if (documentX < elementX &&
            newElementX < documentX &&
            elementHeight < containerElementTop) {
            $(element).parentNode.style.position = "absolute";
            $(element).parentNode.style.top = "-" + newElementX + "px";
        }
    },
    radioCheckboxEnter: function(form, component, evt) {
        if (evt.keyCode == Event.KEY_RETURN) {
            iceSubmit(form, component, evt);
        }
    }
};

var IE = (Try.these(
        function() {
            new ActiveXObject('Msxml2.XMLHTTP');
            return true;
        },

        function() {
            new ActiveXObject('Microsoft.XMLHTTP');
            return true;
        }

        ) || false);

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

Ice.StateMon = Class.create();
Ice.StateMon = {
    logger:null,

    monitors:Array(),

    add:function(monitor) {
        this.logger.debug('Added monitor for [' + monitor.id + '] type [' + monitor.type + ']');
        this.monitors.push(monitor);
    },

    checkAll:function() {
        // Remove all elements no longer found, que found elements
        // that have new HTML elements for rebuilding
        var i = 0;
        var monitor = null;
        var size = this.monitors.length;
        for (i = 0; i < size; i++) {
            monitor = this.monitors[i];
            try {
                if (monitor.changeDetected()) {
                    this.logger.debug('Monitor [' + monitor.id + '] has been replaced');
                    monitor.rebuildMe = true;
                } else {
                    this.logger.debug('Monitor [' + monitor.id + '] has not been replaced');
                    monitor.rebuildMe = false;
                }
                if (!this.elementExists(monitor.id)) {
                    this.logger.debug('Monitor [' + monitor.id + '] no longer exists in dom');
                    monitor.destroyMe = true;
                }
            } catch(ee) {
                this.logger.error("Error checking monitor [" + monitor.id + "] Msg [" + ee + "]");
            }
        }
        this.destroy();
        // Rebuild monitors
        this.rebuild();
    },

    removeMonitors:function(monitor) {
        var nm = Array();
        var i = 0;
        for (i = 0; i < this.monitors.length; i++) {
            if (!this.monitors[i].removeMe)
                nm.push(this.monitors[i]);
        }
        this.monitors = nm;
    },

    destroy:function() {
        var i = 0;
        var monitor = null;
        // Destroy monitors that no longer have HTML elements
        var newMonitors = Array();
        for (i = 0; i < this.monitors.length; i++) {
            monitor = this.monitors[i];
            try {
                if (!monitor.destroyMe) {
                    newMonitors.push(monitor);
                } else {
                    try {
                        this.logger.debug("Destroyed monitor [" + monitor.id + "]");
                        monitor.destroy();
                    } catch(destroyException) {
                        this.logger.warn("Monitor [" + monitor.id + "] destroyed with exception [" + destroyException + "]");
                    }
                    monitor = null;
                }
            } catch(ee) {
                this.logger.error("Error destroying monitor [" + monitor.id + "] Msg [" + ee + "]");
            }
        }
        this.monitors = newMonitors;
    },

    destroyAll:function() {
        var i = 0;
        var monitor = null;
        // Destroy monitors that no longer have HTML elements
        var newMonitors = Array();
        for (i = 0; i < this.monitors.length; i++) {
            monitor = this.monitors[i];
            try {
                if (monitor != null) {
                    this.logger.debug("Destroyed monitor [" + monitor.id + "]");
                    monitor.destroy();
                }
            } catch(destroyException) {
                this.logger.warn("Monitor [" + monitor.id + "] destroyed with exception [" + destroyException + "]");
            }
            monitor = null;

        }
        this.monitors = Array();
    },

    rebuild:function() {
        var size = this.monitors.length;
        try {
            for (i = 0; i < size; i++) {
                monitor = this.monitors[i];
                if (monitor.rebuildMe) {
                    this.logger.debug('Rebuilding [' + monitor.id + ']');
                    try {
                        monitor.destroy();
                    } catch(monitorDestroyException) {
                        this.logger.warn('Could not destroy monitor before rebuilding ID[' + monitor.id + '] msg [' + monitorDestroyException + ']');
                    }
                    monitor.rebuild();
                    monitor.rebuildMe = false;
                    monitor.removeMe = true;
                } else {
                    this.logger.debug("Not rebuilding [" + monitor.id + "] type [" + monitor.type + "]");
                }
            }
            // This monitor is dead. A new one was created
            this.removeMonitors();
        } catch(ee) {
            this.logger.error("Error rebuilding monitors [" + ee + "]");
        }
    },

    elementExists:function(id) {
        var o = $(id);
        if (!o)return false;
        return true;
    },

    elementReplaced:function(ele) {
        if (ele && !ele.id) {
            // If element does not have an ID then it wont require initialization
            return false;
        }
        var currentEle = $(ele.id);
        if (!currentEle) {
            this.logger.debug('Element not found id[' + ele.id + '] element[' + ele + '] type [' + ele.nodeName + ']');
        }
        if (currentEle != null && currentEle != ele) {
            this.logger.debug("Element replaced");
            return true;
        }
    }
};

Ice.MonitorBase = Class.create();
Ice.MonitorBase.prototype = {
    object:null,
    id:null,
    htmlElement:null,
    rebuildMe:false,

    rebuild:function() {
    },

    createOptions:null,
    options:null,
    destroyMe:false,

    destroy:function() {
    },

    type:'Base',

    initialize:function() {
    },

    changeDetected:function() {
        return Ice.StateMon.elementReplaced(this.htmlElement);
    },

    removeMe:false
};

Ice.SortableMonitor = Class.create();
Ice.SortableMonitor.prototype = Object.extend(new Ice.MonitorBase(), {
    initialize:function(htmlElement, createOptions) {
        this.type = 'Sortable';
        this.object = null;
        this.id = htmlElement.id;
        this.htmlElement = htmlElement;
        this.createOptions = createOptions;
    },

    destroy:function() {
        Sortable.destroy(this.htmlElement);
    },

    rebuild:function() {
        Ice.StateMon.logger.debug('Rebuilding Sortable ID[' + this.id + '] Options[' + this.createOptions + ']');
        Sortable.create(this.id, this.createOptions);
    },

    changeDetected:function() {
        return true;
    }
});

Ice.DraggableMonitor = Class.create();
Ice.DraggableMonitor.prototype = Object.extend(new Ice.MonitorBase(), {
    initialize:function(htmlElement, createOptions) {
        this.type = 'Draggable';
        this.object = null;
        this.id = htmlElement.id;
        this.htmlElement = htmlElement;
        this.createOptions = createOptions;
    },

    destroy:function() {
        this.object.destroy();
        Ice.StateMon.logger.debug('Destroyed Draggable [' + this.id + ']');
        $A(Draggables.drags).each(function(drag) {
            Ice.StateMon.logger.debug('Draggable [' + drag.element.id + "] not destroyed");
        });
    },

    rebuild:function() {
        Ice.StateMon.logger.debug('Rebuilding Draggable ID[' + this.id + '] Options[' + this.createOptions + ']');
        var d = new Draggable(this.id, this.createOptions);
        Ice.StateMon.logger.debug('Rebuilding Draggable ID[' + this.id + '] Options[' + this.createOptions + '] Complete');
    }
});


Ice.DroppableMonitor = Class.create();
Ice.DroppableMonitor.prototype = Object.extend(new Ice.MonitorBase, {
    initialize:function(htmlElement, createOptions) {
        this.type = 'Droppable';
        this.object = null;
        this.id = htmlElement.id;
        this.htmlElement = htmlElement;
        this.createOptions = createOptions;
    },

    destroy:function() {
        Droppables.remove(this.htmlElement);
    },

    rebuild:function() {
        Ice.StateMon.logger.debug('Rebuilding Droppables ID[' + this.id + '] Options[' + this.createOptions + ']');
        Droppables.add(this.id, this.createOptions);
    }
});

Ice.AutocompleterMonitor = Class.create();
Ice.AutocompleterMonitor.prototype = Object.extend(new Ice.MonitorBase, {
    initialize:function(htmlElement, update, createOptions, rowClass, selectedRowClass) {
        this.type = 'Autocompleter';
        this.object = null;
        this.id = htmlElement.id;
        this.htmlElement = htmlElement;
        this.createOptions = createOptions;
        this.update = update;
        this.rowClass = rowClass;
        this.selectedRowClass = selectedRowClass;
    },

    destroy:function() {
        this.object.dispose();
    },

    rebuild:function() {
        Ice.StateMon.logger.debug('Rebuilding Autocompleter ID[' + this.id + '] Options[' + this.createOptions + ']');
        return new Ice.Autocompleter(this.id, this.update.id, this.createOptions, this.rowClass, this.selectedRowClass);
    }
});
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

Ice.DnD.StyleReader = Class.create();
Ice.DnD.StyleReader = {

    styles: 'position,top,left,display',

    buildStyle: function(ele) {
        //Ice.DnD.logger.debug("Building Style");
        var result = '';
        Ice.DnD.StyleReader.styles.split(',').each(
                function(style) {
                    result += style + ':' + Ice.DnD.StyleReader.getStyle(ele, style) + ';';
                });
        return result;
    },
    getStyle: function(x, styleProp) {
        if (x.currentStyle)
            var y = x.currentStyle[styleProp];
        else if (window.getComputedStyle)
            var y = document.defaultView.getComputedStyle(x, null).getPropertyValue(styleProp);
        else
            var y = x.style[styleProp];
        return y;
    },

    findCssField:function(ele, f) {
        if (!f)
            f = Ice.util.findForm(ele);
        var fe = f.getElementsByTagName('input');
        var cssUpdate = null;
        var i = 0;
        // We only want hidden fields.
        for (i = 0; i < fe.length; i++) {
            if (fe[i].type == 'hidden' && fe[i].name == 'icefacesCssUpdates') {
                cssUpdate = fe[i];
                break;
            }
        }
        return cssUpdate;
    },
    upload: function(ele, submit) {

        var cssUpdate = Ice.DnD.StyleReader.findCssField(ele);

        if (cssUpdate) {
            var val = cssUpdate.value;
            var css = Ice.DnD.StyleReader.buildStyle(ele);
            Ice.DnD.logger.debug("Update CSS ID[" + ele.id + "] CSS[" + css + "] form filed name = [" + cssUpdate.name + "]");
            cssUpdate.value = val + ele.id + '{' + css + '}';
            if (submit) {
                var form = Ice.util.findForm(ele);
                iceSubmitPartial(form, ele, null);
            }
        }
    }
}


Ice.modal = Class.create();
Ice.modal = {
    running:false,
    target:null,
    id:null,
    tabindexValues: [],
    start:function(target, iframeUrl, trigger) {
        var modal = document.getElementById(target);
        modal.style.visibility = 'hidden';
        modal.style.position = 'absolute';
        var iframe = document.getElementById('iceModalFrame');
        if (!iframe) {
            iframe = document.createElement('iframe');
            iframe.title = 'Ice Modal Frame';
            iframe.frameborder = "0";
            iframe.id = 'iceModalFrame';
            iframe.src = iframeUrl;
            iframe.style.zIndex = 25000;
            iframe.style.opacity = 0.5;
            iframe.style.filter = 'alpha(opacity=50)';

            iframe.style.position = 'absolute';
            iframe.style.visibility = 'hidden';
            iframe.style.backgroundColor = 'black';
            iframe.style.borderWidth = "0";

            iframe.style.top = '0';
            iframe.style.left = '0';
            //trick to avoid bug in IE, see http://support.microsoft.com/kb/927917
            modal.parentNode.insertBefore(iframe, modal);
            var resize = function() {
                //lookup element again because 'resize' closure is registered only once
                var frame = document.getElementById('iceModalFrame');
                if (frame) {
                    var frameDisp = frame.style.display;
                    frame.style.display = "none";
                    var documentWidth = document.documentElement.scrollWidth;
                    var bodyWidth = document.body.scrollWidth;
                    var documentHeight = document.documentElement.scrollHeight;
                    var bodyHeight = document.body.scrollHeight;
                    var width = (bodyWidth > documentWidth ? bodyWidth : documentWidth) ;
                    var height = (bodyHeight > documentHeight ? bodyHeight : documentHeight);
                    var viewportHeight = document.viewport.getHeight();
                    if (height < viewportHeight) height = viewportHeight;
                    frame.style.width = width + 'px';
                    frame.style.height = height + 'px';
                    frame.style.visibility = 'visible';
                    var modalWidth = 100;
                    var modalHeight = 100;
                    try {
                        modalWidth = Element.getWidth(modal);
                        modalHeight = Element.getHeight(modal);
                    } catch (e) {
                    }
                    modalWidth = parseInt(modalWidth) / 2;
                    modalHeight = parseInt(modalHeight) / 2;
                    modal.style.top = (parseInt(height) / 2) - modalHeight + "px";
                    modal.style.left = (parseInt(width) / 2 ) - modalWidth + "px";
                    frame.style.display = frameDisp;
                }
            };
            resize();
            Event.observe(window, "resize", resize);
            Event.observe(window, "scroll", resize);
        }

        var modal = document.getElementById(target);

        modal.style.zIndex = parseInt(iframe.style.zIndex) + 1;
        Ice.modal.target = modal;
        Ice.modal.id = target;
        if (!Ice.modal.running) {
            Ice.modal.disableTabindex();
        }
        Ice.modal.running = true;
        modal.style.visibility = 'visible';
        if (trigger) {
            Ice.modal.trigger = trigger;
            $(trigger).blur();
            setFocus('');
        }
    },
    stop:function(target) {
        if (Ice.modal.id == target) {
            var iframe = document.getElementById('iceModalFrame');
            if (iframe) {
                iframe.parentNode.removeChild(iframe);
                logger.debug('removed modal iframe for : ' + target);
            }
            Ice.modal.running = false;
            if (Ice.modal.trigger) {
                Ice.Focus.setFocus(Ice.modal.trigger);
                Ice.modal.trigger = '';
            }
            Ice.modal.restoreTabindex();
        }
    },
    disableTabindex: function() {
        var focusables = {};
        focusables.a = document.getElementsByTagName('a');
        focusables.area = document.getElementsByTagName('area');
        focusables.button = document.getElementsByTagName('button');
        focusables.input = document.getElementsByTagName('input');
        focusables.object = document.getElementsByTagName('object');
        focusables.select = document.getElementsByTagName('select');
        focusables.textarea = document.getElementsByTagName('textarea');

        var tabindexValues = [];
        for (listName in focusables) {
            var list = focusables[listName]
            for (var j = 0; j < list.length; j++) {
                var ele = list[j];
                if (!Ice.modal.containedInId(ele, Ice.modal.id)) {
                    var obj = {};
                    obj.element = ele;
                    obj.tabIndex = ele.tabIndex ? ele.tabIndex : '';
                    ele.tabIndex = '-1';
                    tabindexValues.push(obj);
                }
            }
        }
        Ice.modal.tabindexValues = tabindexValues;
    },
    restoreTabindex: function() {
        Ice.modal.tabindexValues.each(function(obj) {
            obj.element.tabIndex = obj.tabIndex;
        });
        Ice.modal.tabindexValues = [];
    },
    containedInId:function(node, id) {
        if (node.id == id) {
            return true;
        }
        var parent = node.parentNode;
        if (parent) {
            return Ice.modal.containedInId(parent, id);
        }
        return false;
    }
};

Ice.autoCentre = Class.create();
Ice.autoCentre = {
    id:null,
    keepCentred:function() {
        var scrollX = window.pageXOffset || document.body.scrollLeft || document.documentElement.scrollLeft;
        var scrollY = window.pageYOffset || document.body.scrollTop || document.documentElement.scrollTop;
        var div = document.getElementById(Ice.autoCentre.id);
        if (div) {
            var x = Math.round((Element.getWidth(document.body) - Element.getWidth(div)) / 2 + scrollX);
            if (x < 0) x = 0;
            var y = Math.round(((window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight) - Element.getHeight(div)) / 2 + scrollY);
            if (y < 0) y = 0;
            x = x + "px";
            y = y + "px";
            Element.setStyle(div, {position:'absolute'});
            Element.setStyle(div, {left: x});
            Element.setStyle(div, {top:y});
        }
    },
    start:function(target) {
        Ice.autoCentre.id = target;
        Ice.autoCentre.keepCentred();
        Event.observe(window, 'resize', Ice.autoCentre.keepCentred);
        Event.observe(window, 'scroll', Ice.autoCentre.keepCentred);
    },
    stop:function(target) {
        if (Ice.autoCentre.id == target) {
            Event.stopObserving(window, 'resize', Ice.autoCentre.keepCentred);
            Event.s
            topObserving(window, 'scroll', Ice.autoCentre.keepCentred);
        }
    }
};

Ice.autoPosition = Class.create();
Ice.autoPosition = {
    id:null,
    xPos:null,
    yPos:null,
    keepPositioned:function() {
        var scrollX = window.pageXOffset || document.body.scrollLeft || document.documentElement.scrollLeft;
        var scrollY = window.pageYOffset || document.body.scrollTop || document.documentElement.scrollTop;
        var div = document.getElementById(Ice.autoPosition.id);
        if (div) {
            var x = Math.round(Ice.autoPosition.xPos + scrollX) + "px";
            var y = Math.round(Ice.autoPosition.yPos + scrollY) + "px";
            Element.setStyle(div, {position:'absolute'});
            Element.setStyle(div, {left: x});
            Element.setStyle(div, {top:y});
        }
    },
    start:function(target, x, y) {
        Ice.autoPosition.id = target;
        Ice.autoPosition.xPos = x;
        Ice.autoPosition.yPos = y;
        var s = document.getElementById(target).style;
        if (!Prototype.Browser.IE) s.visibility = 'hidden';
        Ice.autoPosition.keepPositioned();
        if (!Prototype.Browser.IE) s.visibility = 'visible';
        Event.observe(window, 'scroll', Ice.autoPosition.keepPositioned);
    },
    stop:function(target) {
        if (Ice.autoPosition.id == target) {
            Event.stopObserving(window, 'scroll', Ice.autoPosition.keepPositioned);
        }
    }
};

Ice.iFrameFix = Class.create();
Ice.iFrameFix = {
    start: function(elementId, url) {
        var index = navigator.userAgent.indexOf("MSIE");
        if (index == -1) return;

        var version = parseFloat(navigator.userAgent.substring(index + 5));
        if (version >= 7) return;

        var popupDiv = document.getElementById(elementId);
        if (!popupDiv) return;

        var popupIFrame = document.getElementById(elementId + ":iframe");
        if (!popupIFrame) {
            popupIFrame = document.createElement("iframe");
            //          popupIFrame.src = "javascript:void 0;";
            popupIFrame.src = url;
            popupIFrame.setAttribute("id", elementId + ":iframe")
            //          popupDiv.insertBefore(popupIFrame, popupDiv.firstChild);
            popupDiv.appendChild(popupIFrame);
        }
        popupIFrame.style.position = "absolute";
        popupIFrame.style.zIndex = -1;
        popupIFrame.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=0)";
        popupIFrame.style.left = "0px";
        popupIFrame.style.top = "0px";
        popupIFrame.style.width = popupDiv.offsetWidth + 'px';
        popupIFrame.style.height = popupDiv.offsetHeight + 'px';
    }
};

Ice.DnD.adjustPosition = function(id) {
    var element = $(id);
    var viewportDimensions = document.viewport.getDimensions();
    var elementDimensions = element.getDimensions();
    var viewportOffset = element.viewportOffset();
    var positionedOffset = element.positionedOffset();
    var widthDiff = viewportDimensions.width - viewportOffset.left - elementDimensions.width;
    var heightDiff = viewportDimensions.height - viewportOffset.top - elementDimensions.height;

    if (viewportOffset.left < 0) {
        element.style.left = positionedOffset.left - viewportOffset.left + "px";
    } else if (widthDiff < 0) {
        element.style.left = positionedOffset.left + widthDiff + "px";
    }
    if (viewportOffset.top < 0) {
        element.style.top = positionedOffset.top - viewportOffset.top + "px";
    } else if (heightDiff < 0) {
        element.style.top = positionedOffset.top + heightDiff + "px";
    }
}
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

Ice.Initializer = Class.create();
Ice.Initializer = {
    queuedCalls: new Array(),
    ranCalls: new Array(),
    loaded: false,
    addCall: function(id, call) {
        if (Ice.Initializer.loaded) {
            Ice.DnD.logger.debug("Call Ran");
            eval(call);
        } else {
            rapper = new Object();
            rapper.call = call;
            rapper.id = id;
            Ice.Initializer.queuedCalls[Ice.Initializer.queuedCalls.length] = rapper;
            Ice.DnD.logger.debug("Call Queued");
        }
    },

    runQueuedCalls: function() {
        Ice.Initializer.loaded = true;
        ar = Ice.Initializer.queuedCalls;
        var i = 0;
        for (i = 0; i < ar.length; i++) {
            eval(ar[i].call);
            Ice.Initializer.ranCalls[ar[i].id] = true;
        }
        Ice.DnD.logger.debug("Run Queued Calls");
    }
};
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


// Used to improve drag perofmance when many drop targets are present
var DropRegions = {
    init:false,
    SCALE:10,
    map:[],

    register: function(drop) {
        var element = drop.element;
        var topLeft = Position.cumulativeOffset(element);
        var bottomRight = [topLeft[0] + element.offsetWidth, topLeft[1] + element.offsetHeight];
        var tlX = Math.round(topLeft[0] / this.SCALE);
        var tlY = Math.round(topLeft[1] / this.SCALE);
        var brX = Math.round(bottomRight[0] / this.SCALE) + 1;
        var brY = Math.round(bottomRight[1] / this.SCALE) + 1;
        var x = 0;
        var y = 0;

        for (x = tlX; x < brX; x++) {
            for (y = tlY; y < brY; y++) {
                if (this.map[x] == null)
                    this.map[x] = [];
                if (this.map[x][y] == null)
                    this.map[x][y] = [];
                this.map[x][y].push(drop);
            }
        }
    },

    drops: function(point) {
        var x = Math.round(point[0] / DropRegions.SCALE);
        var y = Math.round(point[1] / DropRegions.SCALE);
        if (this.map[x] == null)
            return [];
        if (this.map[x][y] == null)
            return [];
        return this.map[x][y];
    }
}

Ice.DndEvent = Class.create();
Ice.DndEvent.lastEvent = null;
Ice.DndEvent.prototype = {
    drag:null,
    drop:null,
    eventType:null,
    dragFire:null,
    dropFire:null,

    initialize: function() {
    },

    submit: function() {
        var ele = this.drag.element;
        var iframe = document.getElementById('iceModalFrame');
        if (iframe) {
            ele.style.zIndex = parseInt(iframe.style.zIndex) + 1;
        }
        if (this.drag.options.sort == true)return;
        thisEv = ele.id + '-' + this.eventType;

        try {
            Ice.DndEvent.lastEvent = thisEv;
            ignoreDrag = this.ignoreEvent(this.drag.options.mask);
            if (ignoreDrag)Ice.DnD.logger.debug("Drag Type [" + this.eventType + "] Ignored. Mask [" + this.drag.options.mask + "]");
            ignoreDrop = true;

            if (this.drop)ignoreDrop = this.ignoreEvent(this.drop.mask);
            if (this.drop)Ice.DnD.logger.debug("Drop Mask [" + this.drop.mask + "] Ignored [" + ignoreDrop + "]");
            if (ignoreDrag && ignoreDrop)return;
            if (this.drop && ignoreDrop)Ice.DnD.logger.debug("Drop Type [" + this.eventType + "] Ignored. Mask [" + this.drop.mask + "]");

            var ignoreCss = false;
            if (this.drag.options.revert == true)ignoreCss = true;
            if (this.drag.options.dragGhost == true)ignoreCss = true;

            if (this.eventType == 4 || this.eventType == 5 || this.eventType == 1)ignoreCss = true; // Don't send style updates on hovering

            Ice.DnD.logger.debug("DnD Event [" + this.eventType + "] ignoreCss[" + ignoreCss + "] value [" + Ice.DnD.StyleReader.buildStyle(ele) + "]");
            if (!ignoreDrag) {
                Ice.DnD.logger.debug("Drag CSS");
                this.populateDrag(ele, ignoreCss);
                // Different browsers need different values (Safri will take second, IE will take first for example)
                if (this.drag.dragGhost == true)this.populateDrag(this.drag._original, ignoreCss);
            }
            if (!ignoreDrop) {
                Ice.DnD.logger.debug("Drop CSS");
                this.populateDrop(this.drop.element, ignoreCss);
            }
            //don't submit if the "clientOnly" attribute is true on the panelPopup
            var clientOnly = $(ele.id + "clientOnly");
            if ((!ignoreDrag || !ignoreDrop) && !clientOnly) {
                Ice.DnD.logger.debug("DnD Event [" + this.eventType + "] Sent");
                var form = Ice.util.findForm(ele);
                var formId = form.id;
                var nothingEvent = new Object();
                var cssUpdate = Ice.DnD.StyleReader.findCssField(ele, form);
                fe = ele.getElementsByTagName('input');
                Ice.DnD.logger.debug("Sending Drag Form. Form is [" + form.id + "] Type [" + fe[0].value + "]");
                var ii = 0;
                for (ii = 0; ii < fe.length; ii++) {
                    Ice.DnD.logger.debug("Drag Form Field[" + fe[ii].name + "] Value [" + fe[ii].value + "]");
                }
                Ice.DnD.logger.debug("Submitting  drag form ID[" + form.id + "] CssUpdate [" + cssUpdate.value + "]!");
                try {
                    iceSubmitPartial(form, ele, nothingEvent);
                } catch(formExcept) {
                    Ice.DnD.logger.error("error submitting dnd event", formExcept);

                }
                Ice.DnD.logger.debug("drag form ID[" + form.id + "] submitted");
                // Drop targets might be in a separate form. If this is the case then
                // submit both forms
                if (!ignoreDrop) {
                    form = Ice.util.findForm(this.drop.element);

                    if (form.id != formId) {
                        Ice.DnD.logger.debug("Diff [" + form.id + "]!=[" + formId + "] Submitting");
                        iceSubmitPartial(form, this.drop.element, nothingEvent);
                    }
                }
            }
        } catch(exc) {
            Ice.DnD.logger.error("Could not find form in drag drop", exc);
        }
        return;
    },

    populateDrag:function(ele, ignoreCss) {
        var fe = ele.getElementsByTagName('input');
        var ne = new Array();
        var i = 0;

        var status = null;
        var dropID = null;

        // We only want hidden fields.
        for (i = 0; i < fe.length; i++) {
            if (fe[i].type == 'hidden') {
                if (fe[i].id.indexOf('status') > 1) {
                    status = fe[i];
                }
                if (fe[i].id.indexOf('dropID') > 1) {
                    dropID = fe[i];
                }
            }
        }

        if (status != null) {
            status.value = this.eventType;
        }
        if (this.drop && dropID != null) {
            dropID.value = this.drop.element.id;
        }
        if (!ignoreCss) Ice.DnD.StyleReader.upload(ele);
        return true;
    },

    populateDrop:function(ele, ignoreCss) {
        var fe = ele.getElementsByTagName('input');
        var ne = new Array();
        var i = 0;
        // We only want hidden fields.
        for (i = 0; i < fe.length; i++) {
            if (fe[i].type == 'hidden') {
                ne.push(fe[i]);
            }
        }
        fe = ne;
        fe[0].value = this.eventType;
        fe[1].value = this.drag.element.id;
        if (!ignoreCss) Ice.DnD.StyleReader.upload(ele);

        return true;
    },

    ignoreEvent:function(mask) {
        if (!mask)return false;//No mask, no ignore
        var result = false;
        if (mask) {
            if (mask.indexOf(this.eventType) != -1) {
                result = true;
            }
        }
        return result;
    }
};

Ice.SortEvent = Class.create();
Ice.SortEvent.prototype = {
    start:function() {
        Ice.DnD.logger.debug('Starting Sort Event');
    },

    end:function() {
        Ice.DnD.logger.debug('Ending Sort Event');
    }
};
Draggable.prototype.dragGhost = false;
Draggable.prototype.ORIGINAL_initialize = Draggable.prototype.initialize;
Draggable.prototype.initialize = function(C) {
    var B = Ice.StateMon.monitors;
    for (i = 0; i < B.length; i++) {
        A = B[i];
        if (A.id == C && A.type == "Draggable") {
            Ice.DnD.logger.debug("Draggable [" + $(C).id + "] has already been created");
            return;
        }
    }
    this.element = $(C);
    var D = arguments[1];
    if (D.dragGhost == true) {
        this.dragGhost = true;
    }
    if (!D.starteffect) {
        D.starteffect = function() {
        };
    }
    if (!D.endeffect) {
        D.endeffect = function() {
        };
    }
    if (D.handle) {
        D.handle = $(D.handle);
        D.handle = $(D.handle.id);
    }
    this.ORIGINAL_initialize(this.element, D);
    if (!D.sort) {
        Ice.DnD.logger.debug("Draggable Created ID[" + this.element.id + "]");
        var A = new Ice.DraggableMonitor(this.element, D);
        A.object = this;
        Ice.StateMon.add(A);
    }
    Ice.DnD.logger.debug("Draggable [" + this.element.id + "] created");
};
Draggable.prototype.ORIGINAL_startDrag = Draggable.prototype.startDrag;
Draggable.prototype.startDrag = function(C) {
    this.dragging = true;
    if (this.dragGhost == true) {
        Ice.DnD.logger.debug("Init Drag Ghost ID[" + this.element.id + "]");
        Draggables.register(this);
        try {
            this._ghost = this.element.cloneNode(true);
            var B = Ice.util.findForm(this.element);
            B.appendChild(this._ghost);
            Position.absolutize(this._ghost);
            Element.makePositioned(this._ghost);
            this._original = this.element;
            Position.clone(this._original, this._ghost);
            var D = parseInt(this._original.style.zIndex);
            this._ghost.style.left = Event.pointerX(C) + "px";
            this._ghost.style.zIndex = ++D;
            this.element = this._ghost;
            this.eventResize = this.resize.bindAsEventListener(this);
            Event.observe(window, "resize", this.eventResize);
        } catch(A) {
            Ice.DnD.logger.error("Error init DragGhost  ID[" + this.element.id + "]", A);
        }
    }
    if (this.options.dragCursor) {
        this._cursor = this.element.cloneNode(true);
        document.body.appendChild(this._cursor);
        Position.absolutize(this._cursor);
        var D = 1 + this.element.style.zIndex;
        this._cursor.style.zIndex = D;
        Ice.DnD.logger.debug("clone created");
    }
    this.ORIGINAL_startDrag(C);
};
Draggable.prototype.ORIGINAL_draw = Draggable.prototype.draw;
Draggable.prototype.draw = function(A) {
    if (!this.options.dragCursor) {
        return this.ORIGINAL_draw(A);
    }
    var E = Position.cumulativeOffset(this.element);
    var D = this.currentDelta();
    E[0] -= D[0];
    E[1] -= D[1];
    var C = A;
    if (this.options.snap) {
        if (typeof this.options.snap == "function") {
            C = this.options.snap(C[0], C[1]);
        } else {
            if (this.options.snap instanceof Array) {
                C = C.map(function(F, G) {
                    return Math.round(F / this.options.snap[G]) * this.options.snap[G];
                }.bind(this));
            } else {
                C = C.map(function(F) {
                    return Math.round(F / this.options.snap) * this.options.snap;
                }.bind(this));
            }
        }
    }
    var B = this._cursor.style;
    if ((!this.options.constraint) || (this.options.constraint == "horizontal")) {
        B.left = C[0] + "px";
    }
    if ((!this.options.constraint) || (this.options.constraint == "vertical")) {
        B.top = C[1] + "px";
    }
    if (B.visibility == "hidden") {
        B.visibility = "";
    }
};
Draggable.prototype.resize = function(A) {
};
Draggable.removeMe = function(D) {
    $(D).undoPositioned();
    var C = Ice.StateMon.monitors;
    var F = Array();
    for (i = 0; i < C.length; i++) {
        monitor = C[i];
        try {
            if (monitor.id == D && monitor.type == "Draggable") {
                if (monitor.object.dragging) {
                    return;
                }
                try {
                    var E = $(D + "clientOnly");
                    if (!E) {
                        monitor.destroyMe = true;
                        monitor.destroy();
                    }
                } catch(B) {
                    logger.warn("Monitor [" + monitor.id + "] destroyed with exception [" + B + "]");
                }
            } else {
                F.push(monitor);
            }
        } catch(A) {
            logger.error("Error destroying monitor [" + monitor.id + "] Msg [" + A + "]");
        }
    }
    Ice.StateMon.monitors = F;
};
Draggable.prototype.ORIGINAL_updateDrag = Draggable.prototype.updateDrag;
Draggable.prototype.updateDrag = function(F, G) {
    Droppables.affectedDrop = null;
    this.ORIGINAL_updateDrag(F, G);
    ad = Droppables.affectedDrop;
    iceEv = new Ice.DndEvent();
    iceEv.drag = this;
    if (this.dragGhost == true) {
        var A = parseInt(this.element.offsetHeight);
        var C = parseInt(Element.getStyle(this.element, "top").split("px")[0]);
        if (Prototype.Browser.IE) {
            C = this.element.cumulativeOffset().top;
        }
        var B = Event.pointerY(F);
        var E = A + C;
        var D = (B > C && B < E);
        if (!D) {
            this.element.style.top = B + "px";
        }
    }
    if (this.hoveringDrop && !ad) {
        iceEv.eventType = Ice.DnD.HOVER_END;
    }
    if (ad && (!this.hoveringDrop || this.hoveringDrop.element.id != ad.element.id)) {
        iceEv.eventType = Ice.DnD.HOVER_START;
        iceEv.drop = ad;
    }
    this.hoveringDrop = (ad != null) ? ad : null;
    if (!iceEv.eventType) {
        iceEv.eventType = Ice.DnD.DRAG_START;
    }
    iceEv.submit();
};
Draggable.prototype.ORIGINAL_finishDrag = Draggable.prototype.finishDrag;
Draggable.prototype.finishDrag = function(D, E) {
    if (!this.options.sort) {
        this.dragging = false;
        if (E) {
            iceEv = new Ice.DndEvent();
            iceEv.drag = this;
            if (this.hoveringDrop) {
                iceEv.drop = this.hoveringDrop;
                iceEv.eventType = Ice.DnD.DROPPED;
            } else {
                iceEv.eventType = Ice.DnD.DRAG_CANCEL;
            }
            iceEv.submit();
            if (this.dragGhost == true) {
                this.element = this._original;
                Element.remove(this._ghost);
                this._ghost = null;
            }
            if (this.options.dragCursor) {
                Element.remove(this._cursor);
                this._cursor = null;
            }
            Draggable.removeMe(this.element.id);
        }
    }
    this.ORIGINAL_finishDrag(D, E);
    DropRegions.init = false;
    DropRegions.map = [];
    if (this.options.sort && E) {
        try {
            var C = Ice.util.findForm(this.element);
            var B = new Object();
            Ice.DnD.logger.debug("Submitting Sortable [" + this.element + "]");
            iceSubmit(C, this.element, B);
        } catch(A) {
            Ice.DnD.logger.error("error submiting sortable element[" + this.element + "] Err Msg[" + A + "]");
        }
    }
};
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
// Original license and copyright:
// Copyright (c) 2005 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//           (c) 2005 Sammi Williams (http://www.oriontransfer.co.nz, sammi@oriontransfer.co.nz)
// 
// See scriptaculous.js for full license.

Droppables.affectedDrop = null;

Droppables.ORIGINAL_show = Droppables.show;
Droppables.show = function(point, element) {
    if (!this.drops.length) return;

    if (this.last_active) this.deactivate(this.last_active);
    var dropsToScan = this.drops;
    if (DropRegions.init && this.drops.all(function(drop) {
        return !drop.scrollid;
    })) {
        dropsToScan = DropRegions.drops(point);
    }
    dropsToScan.each(function(drop) {
        if (Position.within(drop.element, point[0], point[1])) {
            if (Droppables.isAffected(point, element, drop)) {
                //Ice.DnD.logger.debug("Affected True OnHover" + drop.onHover + "]");
                Droppables.affectedDrop = drop;
                if (drop.onHover)
                    drop.onHover(element, drop.element, Position.overlap(drop.overlap, drop.element));
                if (drop.greedy) {
                    Droppables.activate(drop);
                }
            }
        }
        if (!DropRegions.init) {
            DropRegions.register(drop);
        }
    });
    DropRegions.init = true;
};

Droppables.ORIGINAL_isAffected = Droppables.isAffected;
Droppables.isAffected = function(point, element, drop) {
    var result = false;
    result = Droppables.ORIGINAL_isAffected(point, element, drop);
    if (result && drop.sort) {
        if (!Ice.DnD.sortableDraggable(element)) {
            result = false;
        }
    }
    return result;
};

Droppables.ORIGINAL_add = Droppables.add;
Droppables.add = function(ele, options) {
    var monitors = Ice.StateMon.monitors;
    for (i = 0; i < monitors.length; i++) {
        monitor = monitors[i];
        if (monitor.id == ele && monitor.type == 'Droppable') {
            if (ele['hasDroppable']) {
                return;
            } else {
                monitor.removeMe = true;
                Ice.StateMon.removeMonitors();
            }
        }
    }
    ele['hasDroppable'] = true;
    Droppables.ORIGINAL_add(ele, options);
    if (options && !options.sort) {
        var monitor = new Ice.DroppableMonitor($(ele), options);
        Ice.StateMon.add(monitor);
    }
}
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
// Original license and copyright:
// Copyright (c) 2005 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//           (c) 2005 Sammi Williams (http://www.oriontransfer.co.nz, sammi@oriontransfer.co.nz)
// 
// See scriptaculous.js for full license.


var SortableObserver = Class.create();
SortableObserver.prototype = {

    initialize: function(element, observer) {
        this.element = $(element);
        this.observer = observer;
        this.lastValue = Sortable.serialize(this.element);
    },

    onStart: function(name, drag) {
        this.lastValue = Sortable.serialize(this.element);
        var options = Sortable.options(this.element);
        options.lastDrag = drag;
        //alert("Name [" + name + "] drag [" + drag.element.id + "]");
        options.lastDrag = drag.element.id;
        //SortableObserver.count++;
    },

    onEnd: function() {
        Sortable.unmark();
        var newValue = Sortable.serialize(this.element);
        var options = Sortable.options(this.element);
        options.serializeValue = newValue;
        if (this.lastValue != newValue) {
            this.observer(this.element)
        }
    }
}

var Sortable = {
    sortables: {},
    sortableElements: Array(),
    kids:null,
    _findRootElement: function(element) {
        while (element.tagName != "BODY") {
            if (element.id && Sortable.sortables[element.id]) return element;
            element = element.parentNode;
        }
    },

    options: function(element) {
        element = Sortable._findRootElement($(element));
        if (!element) return;
        return Sortable.sortables[element.id];
    },

    destroy: function(element) {
        var s = Sortable.options(element);

        if (s) {
            Draggables.removeObserver(s.element);
            s.droppables.each(function(d) {
                Droppables.remove(d)
            });
            s.draggables.invoke('destroy');

            delete Sortable.sortables[s.element.id];
            var i = 0;
            var n = Array();
            for (i = 0; i < Sortable.sortableElements.length; i++) {
                if (Sortable.sortableElements[i] && Sortable.sortableElements[i].id != s.element.id) {
                    n.push(Sortable.sortableElements[i]);
                }
            }
            Sortable.sortableElements = n;
        }
    },

    create: function(element, o, override) {
        element = $(element);
        if (Ice.DnD.alreadySort(element)) {
            Ice.DnD.logger.debug('Sort ID [' + element.id + '] already created');
            return;
        }
        var monitor = new Ice.SortableMonitor(element, o);
        var options = Object.extend({
            element:     element,
            tag:         'li',       // assumes li children, override with tag: 'tagname'
            dropOnEmpty: false,

            overlap:     'vertical', // one of 'vertical', 'horizontal'
            constraint:  'vertical', // one of 'vertical', 'horizontal', false
            containment: element,    // also takes array of elements (or id's); or false
            handle:      false,      // or a CSS class
            only:        false,
            hoverclass:  null,
            ghosting:    false,
            format:      null,
            onChange:    Prototype.emptyFunction,
            onUpdate:    Prototype.emptyFunction
        }, arguments[1] || {});
        // clear any old sortable with same element
        this.destroy(element);
        // build options for the draggables
        var options_for_draggable = {
            revert:      true,
            ghosting:    options.ghosting,
            constraint:  options.constraint,
            handle:      options.handle,
            // Sort flag is used by Drag and Drop javascript to avoid Drag and Drop events from being sent
            sort:        true};
        if (options.starteffect)
            options_for_draggable.starteffect = options.starteffect;
        if (options.reverteffect)
            options_for_draggable.reverteffect = options.reverteffect;
        else
            if (options.ghosting) options_for_draggable.reverteffect = function(element) {
                element.style.top = 0;
                element.style.left = 0;
            };
        if (options.endeffect)
            options_for_draggable.endeffect = options.endeffect;
        if (options.zindex)
            options_for_draggable.zindex = options.zindex;
        // build options for the droppables
        var options_for_droppable = {
            overlap:     options.overlap,
            containment: options.containment,
            hoverclass:  options.hoverclass,
            onHover:     Sortable.onHover,
            greedy:      !options.dropOnEmpty,
            sort:        true
        }
        // fix for gecko engine
        Element.cleanWhitespace(element);
        options.draggables = [];
        options.droppables = [];

        // drop on empty handling
        if (options.dropOnEmpty) {
            Droppables.add(element,
            {
                containment: options.containment,
                onHover: Sortable.onEmptyHover, greedy: false, sort:true});
            options.droppables.push(element);
        }
        (options.elements || this.findElements(element, options) || []).each(function(e, i) {
            var handle = options.handles ? $(options.handles[i]) :
                         (options.handle ? $(e).select('.' + options.handle)[0] : e);
            options.draggables.push(
                    new Draggable(e, Object.extend(options_for_draggable, { handle: handle })));
            Droppables.add(e, options_for_droppable);
            if (options.tree) e.treeNode = element;
            options.droppables.push(e);
        });

        // keep reference
        this.sortables[element.id] = options;
        this.sortableElements.push(element);
        monitor.options = options;
        Ice.StateMon.add(monitor);
        // for onupdate
        var observer = new SortableObserver(element, options.onUpdate);
        Draggables.addObserver(observer);
    },

    //return all suitable-for-sortable elements in a guaranteed order
    findElements: function(element, options) {
        if (!element.hasChildNodes()) return null;
        var elements = [];
        $A(element.childNodes).each(function(e) {
            if (e.tagName && e.tagName.toUpperCase() == options.tag.toUpperCase() &&
                (!options.only || (Element.hasClassName(e, options.only))))
                elements.push(e);
        });
        return (elements.length > 0 ? elements.flatten() : null);
    },

    onHover: function(element, dropon, overlap) {
        if (overlap > 0.5) {
            Sortable.mark(dropon, 'before');
            if (dropon.previousSibling != element) {
                var oldParentNode = element.parentNode;
                element.style.visibility = "hidden";
                // fix gecko rendering
                dropon.parentNode.insertBefore(element, dropon);
                if (dropon.parentNode != oldParentNode)
                    Sortable.options(oldParentNode).onChange(element);
                Sortable.options(dropon.parentNode).onChange(element);
            }
        } else {
            Sortable.mark(dropon, 'after');
            var nextElement = dropon.nextSibling || null;
            if (nextElement != element) {
                var oldParentNode = element.parentNode;
                element.style.visibility = "hidden";
                // fix gecko rendering
                dropon.parentNode.insertBefore(element, nextElement);
                if (dropon.parentNode != oldParentNode)
                    Sortable.options(oldParentNode).onChange(element);
                Sortable.options(dropon.parentNode).onChange(element);
            }
        }
    },

    onEmptyHover: function(element, dropon) {
        if (element.parentNode != dropon) {
            var oldParentNode = element.parentNode;
            dropon.appendChild(element);
            Sortable.options(oldParentNode).onChange(element);
            Sortable.options(dropon).onChange(element);
        }
    },

    unmark: function() {
        if (Sortable._marker) Element.hide(Sortable._marker);
    },

    mark: function(dropon, position) {
        // mark on ghosting only
        var sortable = Sortable.options(dropon.parentNode);
        if (!sortable) return;
        if (sortable && !sortable.ghosting) return;

        if (!Sortable._marker) {
            Sortable._marker = $('dropmarker') || document.createElement('DIV');
            Element.hide(Sortable._marker);
            Element.addClassName(Sortable._marker, 'dropmarker');
            Sortable._marker.style.position = 'absolute';
            document.getElementsByTagName("body").item(0).appendChild(Sortable._marker);
        }
        var offsets = Position.cumulativeOffset(dropon);
        Sortable._marker.style.left = offsets[0] + 'px';
        Sortable._marker.style.top = offsets[1] + 'px';

        if (position == 'after')
            if (sortable.overlap == 'horizontal')
                Sortable._marker.style.left = (offsets[0] + dropon.clientWidth) + 'px';
            else
                Sortable._marker.style.top = (offsets[1] + dropon.clientHeight) + 'px';

        Element.show(Sortable._marker);
    },

    serialize : function(element) {
        element = $(element);
        var sortableOptions = Sortable.options(element);
        var options = Object.extend({
            tag:  sortableOptions.tag,
            only: sortableOptions.only,
            name: element.id,
            format: sortableOptions.format || /^[^_]*_(.*)$/
        }, arguments[1] || {});
        //alert("Last Drag [" + sortableOptions.lastDrag + "]");
        return "first;" + sortableOptions.lastDrag + ";changed;" + $(this.findElements(element, options) || []).map(function(item) {
            return item.id;
        }).join(";");
    }
}
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
// Original license and copyright:
// Copyright (c) 2005 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//           (c) 2005 Ivan Krstic (http://blogs.law.harvard.edu/ivan)
//           (c) 2005 Jon Tirsen (http://www.tirsen.com)
// Contributors:
//  Richard Livsey
//  Rahul Bhargava
//  Rob Wills
// 
// See scriptaculous.js for full license.

var Autocompleter = {};

Autocompleter.Finder = {
    list:new Array(),
    add: function(ele, autocomplete) {
        this.list[ele.id] = autocomplete;
    },
    find: function(id) {
        return this.list[id];
    }
};

Autocompleter.Base = function() {
};

Autocompleter.Base.prototype = {
    baseInitialize: function(element, update, options, rowC, selectedRowC) {
        this.element = $(element);
        this.update = $(update);
        this.hasFocus = false;
        this.changed = false;
        this.active = false;
        this.index = -1;
        this.entryCount = 0;
        this.rowClass = rowC;
        this.selectedRowClass = selectedRowC;

        if (this.setOptions)
            this.setOptions(options);
        else
            this.options = options || {};

        this.options.paramName = this.options.paramName || this.element.name;
        this.options.tokens = this.options.tokens || [];
        this.options.frequency = this.options.frequency || 0.4;
        this.options.minChars = this.options.minChars || 1;
        this.options.onShow = this.options.onShow ||
                              function(element, update) {
                                  if (!update.style.position || update.style.position == 'absolute') {
                                      update.style.position = 'absolute';
                                      Position.clone(element, update, {setHeight: false, offsetTop: element.offsetHeight});
                                  }
                                  Effect.Appear(update, {duration:0.15});
                              };
        this.options.onHide = this.options.onHide ||
                              function(element, update) {
                                  new Effect.Fade(update, {duration:0.15})
                              };

        if (typeof(this.options.tokens) == 'string')
            this.options.tokens = new Array(this.options.tokens);

        this.observer = null;
        this.element.setAttribute('autocomplete', 'off');
        Element.hide(this.update);
        Event.observe(this.element, "blur", this.onBlur.bindAsEventListener(this));
        Event.observe(this.element, "keypress", this.onKeyPress.bindAsEventListener(this));
        if (Prototype.Browser.IE || Prototype.Browser.WebKit)
            Event.observe(this.element, "keydown", this.onKeyDown.bindAsEventListener(this));
        // ICE-3830
        if (Prototype.Browser.IE || Prototype.Browser.WebKit)
            Event.observe(this.element, "paste", this.onPaste.bindAsEventListener(this));
    },

    show: function() {
        if (Element.getStyle(this.update, 'display') == 'none')this.options.onShow(this.element, this.update);
        if (!this.iefix &&
            (navigator.appVersion.indexOf('MSIE') > 0) &&
            (navigator.userAgent.indexOf('Opera') < 0) &&
            (Element.getStyle(this.update, 'position') == 'absolute')) {
            new Insertion.After(this.update,
                    '<iframe id="' + this.update.id + '_iefix" title="IE6_Fix" ' +
                    'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' +
                    'src="javascript:\'<html></html>\'" frameborder="0" scrolling="no"></iframe>');
            this.iefix = $(this.update.id + '_iefix');
        }
        if (this.iefix) setTimeout(this.fixIEOverlapping.bind(this), 50);
        this.element.focus();
    },

    fixIEOverlapping: function() {
        Position.clone(this.update, this.iefix);
        this.iefix.style.zIndex = 1;
        this.update.style.zIndex = 2;
        Element.show(this.iefix);
    },

    hide: function() {
        this.stopIndicator();
        if (Element.getStyle(this.update, 'display') != 'none') this.options.onHide(this.element, this.update);
        if (this.iefix) Element.hide(this.iefix);
    },

    startIndicator: function() {
        if (this.options.indicator) Element.show(this.options.indicator);
    },

    stopIndicator: function() {
        if (this.options.indicator) Element.hide(this.options.indicator);
    },

    onKeyPress: function(event) {
        if (!this.active) {
            Ice.Autocompleter.logger.debug("Key press ignored. Not active.");
            switch (event.keyCode) {
                case Event.KEY_TAB:
                case Event.KEY_RETURN:
                    this.getUpdatedChoices(true, event, -1);
                    return;
                case Event.KEY_DOWN:
                    this.getUpdatedChoices(false, event, -1);
                    return;
            }
        }

        Ice.Autocompleter.logger.debug("Key Press");
        if (this.active) {
            switch (event.keyCode) {
                case Event.KEY_TAB:
                case Event.KEY_RETURN:
                    //this.selectEntry();
                    //Event.stop(event);

                    this.hidden = true; // Hack to fix before beta. Was popup up the list after a selection was made
                    var idx = this.selectEntry();
                    Ice.Autocompleter.logger.debug("Getting updated choices on enter");
                    this.getUpdatedChoices(true, event, idx);
                    this.hide();
                    Event.stop(event);
                    return;
                case Event.KEY_ESC:
                    this.hide();
                    this.active = false;
                    Event.stop(event);
                    return;
                case Event.KEY_LEFT:
                case Event.KEY_RIGHT:
                    return;
                case Event.KEY_UP:
                    this.markPrevious();
                    this.render();
                    //if(navigator.appVersion.indexOf('AppleWebKit')>0)
                    Event.stop(event);
                    return;
                case Event.KEY_DOWN:
                    this.markNext();
                    this.render();
                    //if(navigator.appVersion.indexOf('AppleWebKit')>0)
                    Event.stop(event);
                    return;

            }
        }
        else {
            if (event.keyCode == Event.KEY_TAB || event.keyCode == Event.KEY_RETURN) return;
        }

        this.changed = true;
        this.hasFocus = true;
        this.index = -1;
        //This is to avoid an element being select because the mouse just happens to be over the element when the list pops up
        this.skip_mouse_hover = true;
        if (this.active) this.render();
        if (this.observer) clearTimeout(this.observer);
        this.observer = setTimeout(this.onObserverEvent.bind(this), this.options.frequency * 1000);
    },

    onKeyDown: function(event) {
        if (!this.active) {
            switch (event.keyCode) {
                case Event.KEY_DOWN:
                    this.getUpdatedChoices(false, event, -1);
                    return;
                case Event.KEY_BACKSPACE:
                case Event.KEY_DELETE:
                    if (this.observer) clearTimeout(this.observer);
                    this.observer = setTimeout(this.onObserverEvent.bind(this), this.options.frequency * 1000);
                    return;
            }
        }
        else if (this.active) {
            switch (event.keyCode) {
                case Event.KEY_UP:
                    this.markPrevious();
                    this.render();
                    Event.stop(event);
                    return;
                case Event.KEY_DOWN:
                    this.markNext();
                    this.render();
                    Event.stop(event);
                    return;
                case Event.KEY_ESC:
                    if (Prototype.Browser.WebKit) {
                        this.hide();
                        this.active = false;
                        Event.stop(event);
                        return;
                    }
                case Event.KEY_BACKSPACE:
                case Event.KEY_DELETE:
                    if (this.observer) clearTimeout(this.observer);
                    this.observer = setTimeout(this.onObserverEvent.bind(this), this.options.frequency * 1000);
                    return;
            }
        }
    },

    activate: function() {
        this.changed = false;
        this.hasFocus = true;
    },

    onHover: function(event) {
        var element = Event.findElement(event, 'DIV');
        if (this.index != element.autocompleteIndex) {
            if (!this.skip_mouse_hover) this.index = element.autocompleteIndex;
            this.render();
        }
        Event.stop(event);
    },

    onMove: function(event) {
        if (this.skip_mouse_hover) {
            this.skip_mouse_hover = false;
            this.onHover(event);
        }
    },

    onClick: function(event) {
        this.hidden = true;
        // Hack to fix before beta. Was popup up the list after a selection was made
        var element = Event.findElement(event, 'DIV');
        this.index = element.autocompleteIndex;
        var idx = element.autocompleteIndex;
        this.selectEntry();
        this.getUpdatedChoices(true, event, idx);
        this.hide();

    },

    onBlur: function(event) {
        if (navigator.userAgent.indexOf("MSIE") >= 0) { // ICE-2225
            var strictMode = document.compatMode && document.compatMode == "CSS1Compat";
            var docBody = strictMode ? document.documentElement : document.body;
            // Right or bottom border, if any, will be treated as scrollbar.
            // No way to determine their width or scrollbar width accurately.
            if (event.clientX > docBody.clientLeft + docBody.clientWidth ||
                event.clientY > docBody.clientTop + docBody.clientHeight) {
                this.element.focus();
                return;
            }
        }
        // needed to make click events working
        setTimeout(this.hide.bind(this), 250);
        this.hasFocus = false;
        this.active = false;
    },

    // ICE-3830
    onPaste: function(event) {
        this.changed = true;
        this.hasFocus = true;
        this.index = -1;
        this.skip_mouse_hover = true;
        if (this.active) this.render();
        if (this.observer) clearTimeout(this.observer);
        this.observer = setTimeout(this.onObserverEvent.bind(this), this.options.frequency * 1000);
        return;
    },

    render: function() {
        if (this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++)
                if (this.index == i) {
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        Element.removeClassName(this.getEntry(i), ar[ai]);
                    ar = this.selectedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        Element.addClassName(this.getEntry(i), ar[ai]);
                }
                else {
                    ar = this.selectedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        Element.removeClassName(this.getEntry(i), ar[ai]);
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        Element.addClassName(this.getEntry(i), ar[ai]);
                }
            if (this.hasFocus) {
                this.show();
                this.active = true;
            }
        } else {
            this.active = false;
            this.hide();
        }
    },

    markPrevious: function() {
        if (this.index > 0) this.index--
        else this.index = this.entryCount - 1;
    },

    markNext: function() {
        if (this.index == -1) {
            this.index++;
            return;
        }
        if (this.index < this.entryCount - 1) this.index++
        else this.index = 0;
    },

    getEntry: function(index) {
        try {
            return this.update.firstChild.childNodes[index];
        } catch(ee) {
            return null;
        }
    },

    getCurrentEntry: function() {
        return this.getEntry(this.index);
    },

    selectEntry: function() {
        var idx = -1;
        this.active = false;
        if (this.index >= 0) {
            idx = this.index;
            this.updateElement(this.getCurrentEntry());
            this.index = -1;
        }
        return idx;
    },

    updateElement: function(selectedElement) {
        if (this.options.updateElement) {
            this.options.updateElement(selectedElement);
            return;
        }
        var value = '';
        if (this.options.select) {
            var nodes = document.getElementsByClassName(this.options.select, selectedElement) || [];
            if (nodes.length > 0) value = Element.collectTextNodes(nodes[0], this.options.select);
        } else
            value = Element.collectTextNodesIgnoreClass(selectedElement, 'informal');

        var lastTokenPos = this.findLastToken();
        if (lastTokenPos != -1) {
            var newValue = this.element.value.substr(0, lastTokenPos + 1);
            var whitespace = this.element.value.substr(lastTokenPos + 1).match(/^\s+/);
            if (whitespace)
                newValue += whitespace[0];
            this.element.value = newValue + value;
        } else {
            this.element.value = value;
        }
        this.element.focus();

        if (this.options.afterUpdateElement)
            this.options.afterUpdateElement(this.element, selectedElement);
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
            Element.cleanWhitespace(this.update);
            Element.cleanWhitespace(this.update.firstChild);

            if (this.update.firstChild && this.update.firstChild.childNodes) {
                this.entryCount =
                this.update.firstChild.childNodes.length;
                for (var i = 0; i < this.entryCount; i++) {
                    var entry = this.getEntry(i);
                    entry.autocompleteIndex = i;
                    this.addObservers(entry);
                }
            } else {
                this.entryCount = 0;
            }
            this.stopIndicator();
            this.index = -1;
            this.render();
        } else {
            Ice.Autocompleter.logger.debug("Not updating choices Not Changed[" + this.changed + "] hasFocus[" + this.hasFocus + "]");
        }
    },

    addObservers: function(element) {
        Event.observe(element, "mouseover", this.onHover.bindAsEventListener(this));
        Event.observe(element, "click", this.onClick.bindAsEventListener(this));
        Event.observe(element, "mousemove", this.onMove.bindAsEventListener(this));
    },

    dispose:function() {
        for (var i = 0; i < this.entryCount; i++) {
            var entry = this.getEntry(i);
            entry.autocompleteIndex = i;
            Event.stopObserving(entry, "mouseover", this.onHover);
            Event.stopObserving(entry, "click", this.onClick);
            Event.stopObserving(entry, "mousemove", this.onMove);
        }
        Event.stopObserving(this.element, "mouseover", this.onHover);
        Event.stopObserving(this.element, "click", this.onClick);
        Event.stopObserving(this.element, "mousemove", this.onMove);
        Event.stopObserving(this.element, "blur", this.onBlur);
        Event.stopObserving(this.element, "keypress", this.onKeyPress);
        if (Prototype.Browser.IE || Prototype.Browser.WebKit)
            Event.stopObserving(this.element, "keydown", this.onKeyDown);
        Autocompleter.Finder.list[this.element.id] = null;
        Ice.Autocompleter.logger.debug("Destroyed autocomplete [" + this.element.id + "]");
    },

    onObserverEvent: function() {
        this.changed = false;
        if (this.getToken().length >= this.options.minChars) {
            this.startIndicator();
            this.getUpdatedChoices(false, undefined, -1);
        } else {
            this.active = false;
            this.hide();
            this.getUpdatedChoices(false, undefined, -1);
        }
    },

    getToken: function() {
        var tokenPos = this.findLastToken();
        if (tokenPos != -1)
            var ret = this.element.value.substr(tokenPos + 1).replace(/^\s+/, '').replace(/\s+$/, '');
        else
            var ret = this.element.value;

        return /\n/.test(ret) ? '' : ret;
    },

    findLastToken: function() {
        var lastTokenPos = -1;

        for (var i = 0; i < this.options.tokens.length; i++) {
            var thisTokenPos = this.element.value.lastIndexOf(this.options.tokens[i]);
            if (thisTokenPos > lastTokenPos)
                lastTokenPos = thisTokenPos;
        }
        return lastTokenPos;
    }
}

Ajax.Autocompleter = Class.create();
Object.extend(Object.extend(Ajax.Autocompleter.prototype, Autocompleter.Base.prototype), {
    initialize: function(element, update, url, options) {
        this.baseInitialize(element, update, options);
        this.options.asynchronous = true;
        this.options.onComplete = this.onComplete.bind(this);
        this.options.defaultParams = this.options.parameters || null;
        this.url = url;
    },

    getUpdatedChoices: function() {
        entry = encodeURIComponent(this.options.paramName) + '=' +
                encodeURIComponent(this.getToken());

        this.options.parameters = this.options.callback ?
                                  this.options.callback(this.element, entry) : entry;

        if (this.options.defaultParams)
            this.options.parameters += '&' + this.options.defaultParams;

        new Ajax.Request(this.url, this.options);
    },

    onComplete: function(request) {
        this.updateChoices(request.responseText);
    }
});


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
// Original license and copyright:
// Copyright (c) 2005 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//           (c) 2005 Ivan Krstic (http://blogs.law.harvard.edu/ivan)
//           (c) 2005 Jon Tirsen (http://www.tirsen.com)
// Contributors:
//  Richard Livsey
//  Rahul Bhargava
//  Rob Wills
//
// See scriptaculous.js for full license.

Ice.Autocompleter = Class.create();


Object.extend(Object.extend(Ice.Autocompleter.prototype, Autocompleter.Base.prototype), {
    initialize: function(id, updateId, options, rowClass, selectedRowClass) {
        Ice.Autocompleter.logger.debug("Building Ice Autocompleter ID [" + id + "]");
        var existing = Autocompleter.Finder.list[id];
        if (existing && !existing.monitor.changeDetected()) {
            return;
        }

        if (options)
            options.minChars = 0;
        else
            options = {minChars:0};
        var element = $(id);
        var ue = $(updateId);
        this.baseInitialize(element, ue, options, rowClass, selectedRowClass);

        this.options.onComplete = this.onComplete.bind(this);
        this.options.defaultParams = this.options.parameters || null;
        this.monitor = new Ice.AutocompleterMonitor(element, ue, options, rowClass, selectedRowClass);
        this.monitor.object = this;
        Ice.StateMon.add(this.monitor);
        Autocompleter.Finder.add(this.element, this);
        Ice.Autocompleter.logger.debug("Done building Ice Autocompleter");
        if (this.monitor.changeDetected()) {
            Ice.Autocompleter.logger.debug("Change has been detected");
        }
    },

    getUpdatedChoices: function(isEnterKey, event, idx) {
        if (!event) {
            event = new Object();
        }
        entry = encodeURIComponent(this.options.paramName) + '=' +
                encodeURIComponent(this.getToken());

        this.options.parameters = this.options.callback ?
                                  this.options.callback(this.element, entry) : entry;

        if (this.options.defaultParams)
            this.options.parameters += '&' + this.options.defaultParams;

        var form = Ice.util.findForm(this.element);
        if (idx > -1) {
            var indexName = this.element.id + "_idx";
            form[indexName].value = idx;
        }

        //     form.focus_hidden_field.value=this.element.id;
        if (isEnterKey) {
            Ice.Autocompleter.logger.debug("Sending submit");
            iceSubmit(form, this.element, event);
        }
        else {
            Ice.Autocompleter.logger.debug("Sending partial submit");
            iceSubmitPartial(form, this.element, event);
        }
    },

    onComplete: function(request) {
        this.updateChoices(request.responseText);
    },

    updateNOW: function(text) {


        if (this.hidden) {
            this.hidden = false;
            //Ice.Autocompleter.logger.debug("Not showing due to hide force");
            return;
        }
        this.hasFocus = true;
        Element.cleanWhitespace(this.update);
        this.updateChoices(text);
        this.show();
        this.render();
    }
});
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
// Original copyright and license
// Copyright (c) 2005 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
// Contributors:
//  Justin Palmer (http://encytemedia.com/)
//  Mark Pilgrim (http://diveintomark.org/)
//  Martin Bialasinki
// 
// See scriptaculous.js for full license.  

Effect.Highlight.prototype.ORIGINAL_setup = Effect.Highlight.prototype.setup;
Effect.Highlight.prototype.setup = function() {
    if (this.element.highlighting) {
        this.cancel();
        return;
    }
    this.ORIGINAL_setup();
    this.element.highlighting = true;
}

Effect.Highlight.prototype.ORIGINAL_finish = Effect.Highlight.prototype.finish;
Effect.Highlight.prototype.finish = function() {
    this.ORIGINAL_finish();
    this.element.highlighting = false;
}


Object.extend(Effect.DefaultOptions, {afterFinish:function(ele) {
    if (this.uploadCSS != null) {
        Ice.DnD.StyleReader.upload(ele.element, ele.options.submit);
    }
    if (ele.options.iceFinish)
        ele.options.iceFinish(ele);
}});


function blankEffect() {
}// Blank Effect, used as a place holder in local effects

Effect.Grow = function(element) {
    element = $(element);
    var options = Object.extend({
        direction: 'center',
        moveTransition: Effect.Transitions.sinoidal,
        scaleTransition: Effect.Transitions.sinoidal,
        opacityTransition: Effect.Transitions.full
    }, arguments[1] || {});
    var oldStyle = {
        top: element.style.top,
        left: element.style.left,
        height: element.style.height,
        width: element.style.width,
        opacity: element.getInlineOpacity() };

    var dims = element.getDimensions();
    var initialMoveX, initialMoveY;
    var moveX, moveY;

    switch (options.direction) {
        case 'top-left':
            initialMoveX = initialMoveY = moveX = moveY = 0;
            break;
        case 'top-right':
            initialMoveX = dims.width;
            initialMoveY = moveY = 0;
            moveX = -dims.width;
            break;
        case 'bottom-left':
            initialMoveX = moveX = 0;
            initialMoveY = dims.height;
            moveY = -dims.height;
            break;
        case 'bottom-right':
            initialMoveX = dims.width;
            initialMoveY = dims.height;
            moveX = -dims.width;
            moveY = -dims.height;
            break;
        case 'center':
            initialMoveX = dims.width / 2;
            initialMoveY = dims.height / 2;
            moveX = -dims.width / 2;
            moveY = -dims.height / 2;
            break;
    }

    return new Effect.Move(element, {
        x: initialMoveX,
        y: initialMoveY,
        duration: 0.01,
        beforeSetup: function(effect) {
            effect.element.hide().makeClipping().makePositioned();
        },
        afterFinishInternal: function(effect) {
            new Effect.Parallel(
                    [ new Effect.Opacity(effect.element, { sync: true, to: 1.0, from: 0.0, transition: options.opacityTransition }),
                        new Effect.Move(effect.element, { x: moveX, y: moveY, sync: false, transition: options.moveTransition }),
                        new Effect.Scale(effect.element, 100, {
                            scaleMode: { originalHeight: dims.height, originalWidth: dims.width },
                            sync: false, scaleFrom: window.opera ? 1 : 0, transition: options.scaleTransition, restoreAfterFinish: true})
                    ], Object.extend({
                beforeSetup: function(effect) {
                    effect.effects[0].element.setStyle({height: '10px'}, {width: '10px'}).show();
                },
                afterFinishInternal: function(effect) {
                    effect.effects[0].element.undoClipping().undoPositioned().setStyle(oldStyle);
                }
            }, options))
        }
    });
}
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

try {
    Ice.DnD.init();
    Ice.Autocompleter.logger = logger.child('autocomplete');
    Ice.StateMon.checkAll();
    Ice.StateMon.rebuild();
} catch(ee) {
    alert('Error in extras bootstrap [' + ee + ']');
}

window.onUnload(function() {
    try {
        Ice.StateMon.destroyAll();
        Autocompleter.Finder.list = new Array();
    } catch(ee) {
        Ice.DnD.logger.debug('Unload Error [' + ee + ']');
    }
});

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
var GMapRepository = new Array();

var GMapWrapper = Class.create();
GMapWrapper.prototype = {
    initialize: function(eleId, realGMap) {
        this.eleId = eleId;
        this.realGMap = realGMap;
        this.controls = new Object();
        this.overlays = new Object();
        this.geoMarker = new Object();
        this.geoMarkerAddress;
        this.geoMarkerSet = false;
    },

    getElementId: function() {
        return this.eleId;
    },

    getRealGMap: function() {
        return this.realGMap;
    },

    getControlsArray: function() {
        return this.controls;
    }
};

Ice.GoogleMap = Class.create();
Ice.GoogleMap = {
    getGeocoder: function(id) {
        var geocoder = GMapRepository[id + 'geo'];
        if (geocoder == null) {
            GMapRepository[id + 'geo'] = new GClientGeocoder();
            return GMapRepository[id + 'geo'];
        } else {
            return geocoder;
        }
    },

    getGDirection: function(id, text_div) {
        var gdirection = GMapRepository[id + 'dir'];
        if (gdirection == null) {
            var map = Ice.GoogleMap.getGMapWrapper(id).getRealGMap();
            var directionsPanel = document.getElementById(text_div);
            GMapRepository[id + 'dir'] = new GDirections(map, directionsPanel);
            return GMapRepository[id + 'dir'];
        } else {
            return gdirection;
        }
    },

    getGMapWrapper:function (id) {
        var gmapWrapper = GMapRepository[id];
        if (gmapWrapper) {
            var gmapComp = document.getElementById(id);
            //the googlemap view must be unrendered, however
            //javascript object still exist, so recreate the googlemap
            //with its old state.
            if (!gmapComp.hasChildNodes()) {
                gmapWrapper = Ice.GoogleMap.recreate(id, gmapWrapper);
            }
        } else {
            //googleMap not found create a fresh new googleMap object
            gmapWrapper = Ice.GoogleMap.create(id);
        }
        return gmapWrapper;
    },

    loadDirection:function(id, text_div, query) {
        var direction = GMapRepository[id + 'dir'];
        var map = Ice.GoogleMap.getGMapWrapper(id).getRealGMap();
        if (direction == null) {
            var directionsPanel = document.getElementById(text_div);
            direction = new GDirections(map, directionsPanel);
            GMapRepository[id + 'dir'] = direction;
        }
        direction.load(query);
    },

    addOverlay:function (ele, overlayId, ovrLay) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var overlay = gmapWrapper.overlays[overlayId];
        if (overlay == null) {
            overlay = eval(ovrLay);
            gmapWrapper.getRealGMap().addOverlay(overlay);
            gmapWrapper.overlays[overlayId] = overlay;
        }
    },

    removeOverlay:function(ele, overlayId) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var overlay = gmapWrapper.overlays[overlayId];
        if (overlay != null) {
            gmapWrapper.getRealGMap().removeOverlay(overlay);
        } else {
            //nothing found just return
            return;
        }
        var newOvrLyArray = new Object();
        for (overlayObj in gmapWrapper.overlays) {
            if (overlayId != overlayObj) {
                newOvrLyArray[overlayObj] = gmapWrapper.overlays[overlayObj];
            }
        }
        gmapWrapper.overlays = newOvrLyArray;
    },

    locateAddress: function(clientId, address) {
        var gLatLng = function(point) {
            if (!point) {
                alert(address + ' not found');
            } else {
                var gmapWrapper = Ice.GoogleMap.getGMapWrapper(clientId);
                if (gmapWrapper) {
                    gmapWrapper.getRealGMap().setCenter(point, 13);
                    var marker = new GMarker(point);
                    gmapWrapper.getRealGMap().addOverlay(marker);
                    marker.openInfoWindowHtml(address);
                    gmapWrapper.geoMarker = marker;
                    gmapWrapper.geoMarkerAddress = address;
                    Ice.GoogleMap.submitEvent(clientId, gmapWrapper.getRealGMap(), "geocoder");
                } else {
                    //FOR IS DEFINED BUT MAP IS NOT FOUND,
                    //LOGGING CAN BE DONE HERE
                }
            } //outer if
        }; //function ends here

        var geocoder = Ice.GoogleMap.getGeocoder(clientId);
        geocoder.getLatLng(address, gLatLng);
    },

    create:function (ele) {
        var gmapWrapper = new GMapWrapper(ele, new GMap2(document.getElementById(ele)));
        var hiddenField = document.getElementById(ele + 'hdn');
        var mapTypedRegistered = false;

        GEvent.addListener(gmapWrapper.getRealGMap(), "zoomend", function(oldLevel, newLevel) {
            if (oldLevel != null)
                Ice.GoogleMap.submitEvent(ele, gmapWrapper.getRealGMap(), "zoomend", newLevel);
        });

        GEvent.addListener(gmapWrapper.getRealGMap(), "dragend", function() {
            Ice.GoogleMap.submitEvent(ele, gmapWrapper.getRealGMap(), "dragend");
        });

        GEvent.addListener(gmapWrapper.getRealGMap(), "maptypechanged", function() {
            if (mapTypedRegistered) {
                var type = $(ele + 'type');
                type.value = gmapWrapper.getRealGMap().getCurrentMapType().getName();
                Ice.GoogleMap.submitEvent(ele, gmapWrapper.getRealGMap(), "maptypechanged");
            }
            mapTypedRegistered = true;
        });
        initializing = false;
        GMapRepository[ele] = gmapWrapper;
        return gmapWrapper;
    },

    submitEvent: function(ele, map, eventName, zoomLevel) {
        try {
            var center = map.getCenter();
            var lat = $(ele + 'lat');
            var lng = $(ele + 'lng');
            var event = $(ele + 'event');
            var zoom = $(ele + 'zoom');
            var type = $(ele + 'type');
            lat.value = center.lat();
            lng.value = center.lng();
            event.value = eventName;
            if (zoomLevel == null) {
                zoom.value = map.getZoom();
            } else {
                zoom.value = zoomLevel;
                if (zoom.value == map.getZoom()) {
                    return;
                }
            }
            var form = Ice.util.findForm(lat);
            var nothingEvent = new Object();
            iceSubmitPartial(form, lat, nothingEvent);
            //reset event value, so the decode method of gmap can
            //make deceison before decode
            event.value = "";
        } catch(e) {
        }
    },

    recreate:function(ele, gmapWrapper) {
        Ice.GoogleMap.remove(ele);
        var controls = gmapWrapper.controls;
        var geoMarker = gmapWrapper.geoMarker;
        var geoMarkerAddress = gmapWrapper.geoMarkerAddress;
        gmapWrapper = Ice.GoogleMap.create(ele);
        gmapWrapper.geoMarker = geoMarker;
        gmapWrapper.geoMarkerAddress = geoMarkerAddress;
        gmapWrapper.geoMarkerSet = 'true';
        var tempObject = new Object();
        for (control in controls) {
            if (tempObject[control] == null) {
                Ice.GoogleMap.removeControl(ele, control);
                Ice.GoogleMap.addControl(ele, control)
            }
        }
        return gmapWrapper;
    },

    addControl:function(ele, controlName) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var control = gmapWrapper.controls[controlName];
        if (control == null) {
            control = eval('new ' + controlName + '()');
            gmapWrapper.getRealGMap().addControl(control);
            gmapWrapper.controls[controlName] = control;
        }
    },

    removeControl:function(ele, controlName) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        var control = gmapWrapper.controls[controlName];
        if (control != null) {
            gmapWrapper.getRealGMap().removeControl(control);
        }
        var newCtrlArray = new Object();
        for (control in gmapWrapper.controls) {
            if (controlName != control) {
                newCtrlArray[control] = gmapWrapper.controls[control];
            }
        }
        gmapWrapper.controls = newCtrlArray;
    },

    remove:function(ele) {
        var newRepository = new Array();
        for (map in GMapRepository) {
            if (map != ele) {
                newRepository[map] = GMapRepository[map];
            }
        }
        GMapRepository = newRepository;
    },

    setMapType:function(ele, type) {
        var gmapWrapper = Ice.GoogleMap.getGMapWrapper(ele);
        //if the chart is recreated, so add any geoCoderMarker that was exist before.
        if (gmapWrapper.geoMarkerSet
                && gmapWrapper.geoMarker != null
                && gmapWrapper.geoMarkerAddress != null
                )
        {
            gmapWrapper.getRealGMap().addOverlay(gmapWrapper.geoMarker);
            gmapWrapper.geoMarker.openInfoWindowHtml(gmapWrapper.geoMarkerAddress);
            gmapWrapper.geoMarkerSet = false;
        }
        if (gmapWrapper.getRealGMap().getCurrentMapType() != null) {
            //set the map type only when difference found
            if (gmapWrapper.getRealGMap().getCurrentMapType().getName() != type) {
                switch (type)
                        {
                    case "Satellite":
                        gmapWrapper.getRealGMap().setMapType(G_SATELLITE_MAP);
                        break
                    case "Hybrid":
                        gmapWrapper.getRealGMap().setMapType(G_HYBRID_MAP);
                        break
                    case "Map":
                        gmapWrapper.getRealGMap().setMapType(G_NORMAL_MAP);
                        break
                }//switch
            }//inner if
        }//outer if        
    }//setMapType    
}

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
var JSObjects = new Array();
Ice.Repository = Class.create();
Ice.Repository = {
    obj:null,

    getInstance:function(_id) {
        if (JSObjects[_id] == null) {
            JSObjects[_id] = eval(this.obj);
        }
        return JSObjects[_id];
    },

    register:function(_id, obj) {
        this.obj = obj;
        if (JSObjects[_id] == null) {
            JSObjects[_id] = this.obj;
        }
        return this.obj;
    },

    remove:function(_id) {
        var removeArray = new Array();
        for (key in JSObjects) {
            if (key == _id) {
                //	JSObjects["iceIndex"] = parseInt(JSObjects["iceIndex"]) - 1;
                continue;
            }
            removeArray[key] = JSObjects[key];
        }
        JSObjects = removeArray;
    },

    getAll:function() {
        var tempArray = new Array();
        var i = 0;
        for (key in JSObjects) {
            if (key.indexOf(':') > 0) {
                tempArray[i++] = JSObjects[key];
            }
        }
        return tempArray;
    }
}

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
var visibleTooltipList = new Array();

ToolTipPanelPopup = Class.create({
  initialize: function(srcComp, tooltipCompId, event, hideOn, delay, dynamic, formId, ctxValue, iFrameUrl, displayOn, moveWithMouse) {
    this.src = srcComp;
    this.delay = delay || 500;
    this.dynamic = (dynamic == "true");
    this.tooltipCompId = tooltipCompId;
    this.srcCompId = srcComp.id;
    this.hideOn = hideOn;
    this.x = Event.pointerX(event);
    this.y = Event.pointerY(event);
    this.formId = formId;
    this.ctxValue = ctxValue
    this.iFrameUrl = iFrameUrl;
    this.moveWithMouse = moveWithMouse;
    //cancel bubbling
    event.cancelBubble = true;
    //attach events

    if (this.hideOn == "mousedown") {
        this.hideEvent = this.hidePopupOnMouseClick.bindAsEventListener(this);
    } else if (this.hideOn == "mouseout") {
        this.hideEvent = this.hidePopupOnMouseOut.bindAsEventListener(this);
    } else {
        this.hideOn = "none";
    }

    this.eventMouseMove = this.updateCordinate.bindAsEventListener(this);
    this.clearTimerEvent = this.clearTimer.bindAsEventListener(this);
    Event.observe(document, "mouseout" , this.clearTimerEvent);
    Event.observe(document, this.hideOn , this.hideEvent);
    Event.observe(document, "mousemove", this.eventMouseMove);
      if (displayOn == "hover") {
          this.timer = setTimeout(this.showPopup.bind(this), parseInt(this.delay));
      } else {
          this.showPopup.bind(this)();
          Event.extend(event).stop();
      }
  },

  showPopup: function() {
    if (this.isTooltipVisible()) return;
    if (this.dynamic) {
         //its a dynamic tooltip, so remove all its childres
        var tooltip = this.getTooltip();
        if(tooltip) {
	        tooltip.style.visibility = "hidden";        
	        var table = tooltip.childNodes[0];
	        if (table) {
	            tooltip.removeChild(table);
	        }
        }
    //dynamic? set status=show, populatefields, and submit
      this.submit("show");
      if (this.hideOn == "none") {
        //reset the info
        this.populateFields(true);
      }
    } else {
        //static? just set the visibility= true 
       var tooltip = this.getTooltip();
        tooltip.style.visibility = "visible";
        tooltip.style.position = "absolute" ;
        tooltip.style.display = "";
        tooltip.style.top = this.y - tooltip.offsetHeight - 4 + "px";
        tooltip.style.left = this.x+4+"px";
        ToolTipPanelPopupUtil.adjustPosition(tooltip);
        Ice.iFrameFix.start(this.tooltipCompId, this.iFrameUrl);
    }
    this.addToVisibleList();    
  },

  hidePopupOnMouseOut: function(event) {
    if (!this.isTooltipVisible()) return;
    if (Position.within($(this.tooltipCompId), Event.pointerX(event), Event.pointerY(event))) return; //ICE-3521
    this.hidePopup(event);
    this.state = "hide";
    this.populateFields();
    if (this.hideOn == "mouseout") {
        this.removedFromVisibleList();
    }
    this.dispose(event);
  },

  hidePopupOnMouseClick: function(event) {
    if (!this.isTooltipVisible() || !Event.isLeftClick(event)) return;
    var eventSrc = Event.element(event);
    if(this.srcOrchildOfSrcElement(eventSrc)) {
        return;
    } else {
        this.hidePopup(event);
    }
    if (this.hideOn == "mousedown") {
        this.removedFromVisibleList();
    }
    this.dispose(event);
  },


 dispose: function(event) {
    Event.stopObserving(document, this.hideOn, this.hideEvent);
    Event.stopObserving(document, "mousemove", this.eventMouseMove);

   },

  hidePopup:function(event) {
    if(this.dynamic) {
    //dynamic? set status=hide, populatefiels and submit 
        this.submit("hide");
    } else {
        //static? set visibility = false;
        tooltip =  this.getTooltip();
        tooltip.style.visibility = "hidden";
        tooltip.style.display = "none";
    }
  },
  
  
  submit:function(state, event) {
      if (!event) event = new Object();
      this.state = state;
      this.populateFields();
      var element = $(this.srcCompId);
      try {
        var form = Ice.util.findForm(element);
        iceSubmitPartial(form,element,event);
      } catch (e) {logger.info("Form not found" + e);}
  },
  
  clearTimer:function() {
     //   $(action).innerHTML += "<br/> Clearing the event";
        Event.stopObserving(document, "mouseout", this.clearTimerEvent);
        clearTimeout(this.timer);

  },

  updateCordinate: function(event) {
    if (Event.element(event) != this.src && !event.element().descendantOf(this.src)) return;
    this.x = Event.pointerX(event);
    this.y = Event.pointerY(event);
      if (!this.isTooltipVisible() || !this.moveWithMouse) return;
      var tooltip = this.getTooltip();
      tooltip.style.top = this.y - tooltip.offsetHeight - 4 + "px";
      tooltip.style.left = this.x + 4 + "px";
      ToolTipPanelPopupUtil.adjustPosition(tooltip);
      Ice.iFrameFix.start(this.tooltipCompId, this.iFrameUrl);
  },

  srcOrchildOfSrcElement: function(ele) {
     var tooltip =  this.getTooltip();
     if (tooltip  == ele) return true;
     while (ele.parentNode) {
        ele = ele.parentNode;
        if (tooltip  == ele){
            return true;
        }
     }
  },

  getTooltip: function () {
      return $(this.tooltipCompId);
  },
  
  populateFields: function(reset) {
  // the following field should be rendered by the panelPoupRenderer if rendered as tooltip


	    var form = $(this.formId);
	    if (form == null) return;
	    var iceTooltipInfo = form.getElements().find( function(element) {
	        if (element.id == "iceTooltipInfo") return element;
	    });
	    if (!iceTooltipInfo) { 
		    iceTooltipInfo = document.createElement('input');
		    iceTooltipInfo.id="iceTooltipInfo";
		    iceTooltipInfo.name="iceTooltipInfo";            
		    iceTooltipInfo.type="hidden";
	        form.appendChild(iceTooltipInfo);
	    }  else {
	 
	    }
	    if (reset) {
	       iceTooltipInfo.value = "";
	    } else {
	       iceTooltipInfo.value = "tooltip_id=" + this.tooltipCompId + 
	                     "; tooltip_src_id="+ this.src.id+ 
	                     "; tooltip_state="+ this.state +
	                     "; tooltip_x="+ this.x +
	                     "; tooltip_y="+ this.y +
	                     "; cntxValue="+ this.ctxValue;
	    }
    },
    
    addToVisibleList: function() {
        if (!this.isTooltipVisible()) {
            this.removedFromVisibleList('all');
            visibleTooltipList[parseInt(visibleTooltipList.length)] = {tooltipId: this.tooltipCompId, srcCompId: this.srcCompId};
        } else {
        }
    },
    
    removedFromVisibleList: function(all) {
        if (this.isTooltipVisible() || all) {
	        var newList = new Array();
		    var index = -1;
		    for (i=0; i < visibleTooltipList.length; i++) {
		        if (visibleTooltipList[i].tooltipId != this.tooltipCompId) {
		            index = parseInt(index)+ 1;
		            newList[index] = visibleTooltipList[i];
		        }else {
		        }
		    }
		    visibleTooltipList = newList;
		} else {
		}
    },
    
    isTooltipVisible: function(onlyTooltip) {
        for (i=0; i < visibleTooltipList.length; i++) {
            if (onlyTooltip) {
                if (visibleTooltipList[i].tooltipId== this.tooltipCompId) {
                    return true;
                }             
            } else {
                if (visibleTooltipList[i].tooltipId== this.tooltipCompId && visibleTooltipList[i].srcCompId == this.srcCompId) {
                    return true;
                } 
            }
  
        }
        return false;
    }
});

ToolTipPanelPopupUtil = {
    removeFromVisibleList:function(comp_id) {
        var newList = new Array();
        var index = -1;
        for (i=0; i < visibleTooltipList.length; i++) {
            if (visibleTooltipList[i].tooltipId != comp_id) {
                index = parseInt(index)+ 1;
                newList[index] = visibleTooltipList[i];
            }else {
            }
        }
        visibleTooltipList = newList;
    },
    adjustPosition: function(id) {
        var element = $(id);
        var viewportDimensions = document.viewport.getDimensions();
        var viewportScrollOffsets = document.viewport.getScrollOffsets();
        var elementDimensions = element.getDimensions();
        var elementOffsets = element.cumulativeOffset();
        var positionedOffset = element.positionedOffset();

        var diff = 0;
        if (elementOffsets.left < viewportScrollOffsets.left) {
            diff = viewportScrollOffsets.left - elementOffsets.left;
        } else if (elementOffsets.left + elementDimensions.width > viewportScrollOffsets.left + viewportDimensions.width) {
            diff = (elementOffsets.left + elementDimensions.width) - (viewportScrollOffsets.left + viewportDimensions.width);
            diff = - Math.min(diff, (elementOffsets.left - viewportScrollOffsets.left));
        }
        element.style.left = positionedOffset.left + diff + "px";
        
        diff = 0;
        if (elementOffsets.top < viewportScrollOffsets.top) {
            diff = viewportScrollOffsets.top - elementOffsets.top;
        } else if (elementOffsets.top + elementDimensions.height > viewportScrollOffsets.top + viewportDimensions.height) {
            diff = (elementOffsets.top + elementDimensions.height) - (viewportScrollOffsets.top + viewportDimensions.height);
            diff = - Math.min(diff, (elementOffsets.top - viewportScrollOffsets.top));
        }
        element.style.top = positionedOffset.top + diff + "px";
    },
    showPopup: function(id) {
        var tooltip = $(id);
        tooltip.style.top = parseInt(tooltip.style.top) - tooltip.offsetHeight - 4 + "px";
        tooltip.style.left = parseInt(tooltip.style.left) + 4 + "px";
        ToolTipPanelPopupUtil.adjustPosition(id);
    }
};


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
Ice.Resizable = Class.create({
    initialize: function(event, horizontal) {

        //resize handler
        this.source = Event.element(event);
        this.horizontal = horizontal;

        //initial pointer location
        if (this.horizontal) {
            this.pointerLocation = parseInt(Event.pointerY(event));
        } else {
            this.pointerLocation = parseInt(Event.pointerX(event));
        }

        this.eventMouseMove = this.resize.bindAsEventListener(this);
        this.eventMouseUp = this.detachEvent.bindAsEventListener(this);
        Event.observe(document, "mousemove", this.eventMouseMove);
        Event.observe(document, "mouseup", this.eventMouseUp);
        this.origionalHeight = this.source.style.height;
        this.disableTextSelection();
        this.getGhost().style.position = "absolute";
        //    this.getGhost().style.backgroundColor = "green";
        //    this.getGhost().style.border= "1px dashed";

        this.deadPoint = 20;
    },

    print: function(msg) {
        logger.info(msg);
    },

    getPreviousElement: function() {
    },

    getContainerElement: function() {
    },

    getNextElement: function() {
    },

    getGhost:function() {
        return this.source;
    },

    finalize: function (event) {
        this.source.style.position = "";
        this.source.style.left = Event.pointerX(event) + "px";
        //   this.source.style.backgroundColor = "#EFEFEF";
        //   this.source.style.border = "none";
    },

    resize: function(event) {
        this.getGhost().style.visibility = "";
        if (this.deadEnd(event)) return;
        //   this.getGhost().style.backgroundColor = "green";
        if (this.horizontal) {
            this.getGhost().style.cursor = "n-resize";
            var top = Event.pointerY(event) - this.getGhost().getOffsetParent().cumulativeOffset().top;
            this.getGhost().style.top = top + "px";
        } else {
            this.getGhost().style.cursor = "e-resize";
            var left = Event.pointerX(event) - this.getGhost().getOffsetParent().cumulativeOffset().left;
            this.getGhost().style.left = left + "px";
        }
    },


    detachEvent: function(event) {
        //restore height
        this.source.style.height = this.origionalHeight;
        if (this.getDifference(event) > 0 && !this.deadEnd(event)) {
            this.adjustPosition(event);
        }

        Event.stopObserving(document, "mousemove", this.eventMouseMove);
        Event.stopObserving(document, "mouseup", this.eventMouseUp);
        this.enableTextSelection();
        this.finalize(event);
    },

    adjustPosition:function(event) {
        var leftElementWidth = Element.getWidth(this.getPreviousElement());
        var rightElementWidth = Element.getWidth(this.getNextElement());
        var tableWidth = Element.getWidth(this.getContainerElement());
        var diff = this.getDifference(event);

        if (this.resizeAction == "inc") {
            this.getPreviousElement().style.width = (leftElementWidth + diff) + "px";
            this.getNextElement().style.width = (rightElementWidth - diff) + "px"

            //    this.getContainerElement().style.width = tableWidth + diff + "px";;

            this.print("Diff " + diff);
            this.print("Td width " + leftElementWidth + this.getPreviousElement().id);
            this.print("Table width " + tableWidth);


        } else {
            this.getPreviousElement().style.width = (leftElementWidth - diff) + "px";
            this.getNextElement().style.width = (rightElementWidth + diff) + "px"

            //      this.getContainerElement().style.width = tableWidth - diff + "px";
        }
    },

    getDifference: function(event) {
        var x;
        if (this.horizontal) {
            x = parseInt(Event.pointerY(event));
        } else {
            x = parseInt(Event.pointerX(event));
        }
        if (this.pointerLocation > x) {
            this.resizeAction = "dec";
            return this.pointerLocation - x;
        } else {
            this.resizeAction = "inc";
            return x - this.pointerLocation;
        }
    },

    deadEnd: function(event) {
        var diff = this.getDifference(event);
        if (this.resizeAction == "dec") {
            var leftElementWidth;
            if (this.horizontal) {
                leftElementWidth = Element.getHeight(this.getPreviousElement());
            } else {
                leftElementWidth = Element.getWidth(this.getPreviousElement());
            }

            if ((leftElementWidth - diff) < this.deadPoint) {
                // this.getGhost().style.backgroundColor = "red";
                return true;
            }
        } else {
            var rightElementWidth;
            if (this.horizontal) {
                rightElementWidth = Element.getHeight(this.getNextElement());
            } else {
                rightElementWidth = Element.getWidth(this.getNextElement());
            }

            if ((rightElementWidth - diff) < this.deadPoint) {
                //    this.getGhost().style.backgroundColor = "red";
                return true;
            }
        }
        return false;
    },

    disableTextSelection:function() {
        this.getContainerElement().onselectstart = function () {
            return false;
        }
        this.source.style.unselectable = "on";
        this.source.style.MozUserSelect = "none";
        this.source.style.KhtmlUserSelect = "none";
    },

    enableTextSelection:function() {
        this.getContainerElement().onselectstart = function () {
            return true;
        }
        this.source.style.unselectable = "";
        this.source.style.MozUserSelect = "";
        this.source.style.KhtmlUserSelect = "";
    }
});

Ice.ResizableGrid = Class.create(Ice.Resizable, {
    initialize: function($super, event) {
        $super(event);
        logger.info(">>>>>>>>>>>>>>>>>>> ");
        this.cntHght = (Element.getHeight(this.getContainerElement())) + "px";
        this.source.style.height = this.cntHght;
        this.getGhost().style.left = Event.pointerX(event) + "px";
        this.source.style.backgroundColor = "#CCCCCC";
    }
});

Ice.ResizableGrid.addMethods({
    getDifference: function($super, event) {
        return $super(event);
    },

    getContainerElement: function() {
        return this.source.parentNode.parentNode.parentNode.parentNode;
    },

    getPreviousElement: function() {
        if (this.source.parentNode.previousSibling.tagName == "TH") {
            return this.source.parentNode.previousSibling.firstChild;
        } else {
            return this.source.parentNode.previousSibling.previousSibling.firstChild;
        }
    },

    getNextElement: function() {
        if (this.source.parentNode.nextSibling.tagName == "TH") {
            return this.source.parentNode.nextSibling.firstChild;
        } else {
            return this.source.parentNode.nextSibling.nextSibling.firstChild;
        }
    },

    resize: function($super, event) {
        this.source.style.height = this.cntHght;
        this.getGhost().style.height = this.cntHght;
        $super(event);
        this.source.style.height = this.cntHght;
        this.getGhost().style.height = this.cntHght;
    },

    finalize: function ($super, event) {
        $super(event);
        this.source.style.height = "1px";
        this.source.style.backgroundColor = "transparent";
        this.getGhost().style.height = "1px";
        var clientOnly = $(this.getContainerElement().id + "clientOnly");
        if (clientOnly) {
            clientOnly.value = this.getAllColumnsWidth();
            var form = Ice.util.findForm(clientOnly);
            iceSubmitPartial(form, clientOnly, event);
        }
    },

    getAllColumnsWidth:function() {
        var container = this.getContainerElement();
        var children = container.firstChild.firstChild.childNodes;
        var gap = 2;
        if (Prototype.Browser.Gecko) {
            gap += 2;
        }
        var widths = "";
        for (i = 0; i < children.length; i++) {
            if (i % gap == 0) {
                widths += Element.getStyle(children[i].firstChild, "width") + ",";
            }
        }
        return widths;
    }


});

Ice.PanelDivider = Class.create(Ice.Resizable, {
    initialize: function($super, event, horizontal) {
        $super(event, horizontal);
        this.deadPoint = 20;
        if (this.horizontal) {
            var spliterHeight = Element.getHeight(this.source);
            var mouseTop = Event.pointerY(event);
            this.getGhost().style.top = (mouseTop - (spliterHeight )) + "px";
            this.getGhost().style.width = (Element.getWidth(this.getContainerElement())) + "px";
        } else {
            var spliterWidth = Element.getWidth(this.source);
            var borderLeft = parseInt(Element.getStyle(this.source, 'border-left-width'));
            var borderRight = parseInt(Element.getStyle(this.source, 'border-right-width'));
            if (borderLeft && borderLeft >= 1) {
                spliterWidth -= borderLeft;
            }
            if (borderRight && borderRight >= 1) {
                spliterWidth -= borderRight;
            }
            var mouseLeft = Event.pointerX(event);
            this.getGhost().style.left = (mouseLeft - (spliterWidth )) + "px";
            this.getGhost().style.width = spliterWidth + "px";
            this.getGhost().style.height = (Element.getHeight(this.getContainerElement())) + "px";
        }
    }
});

Ice.PanelDivider.addMethods({
    getDifference: function($super, event) {
        return $super(event);
    },

    getContainerElement: function() {
        return this.source.parentNode.parentNode;
    },


    getPreviousElement: function() {
        if (this.source.previousSibling.tagName == "DIV") {
            return this.source.previousSibling;
        } else {
            return this.source.previousSibling.previousSibling;
        }
    },

    getNextElement: function() {
        if (this.source.nextSibling.tagName == "DIV") {
            return this.source.nextSibling;
        } else {
            return this.source.nextSibling.nextSibling;
        }
    },

    getGhost: function() {
        if (!this.ghost) {
            this.ghost = this.source.cloneNode(true);
            this.ghost.id = this.source.id + ":ghost";
            this.ghost.onmousedown = null;
            this.source.parentNode.appendChild(this.ghost);
            this.ghost.style.width = Element.getWidth(this.source) + "px";
            this.getGhost().style.visibility = "hidden";
        }
        this.ghost.setStyle({width:this.source.getStyle("width")});
        return this.ghost;
    },

    finalize: function (event) {
        Element.remove(this.ghost);
    },

    adjustPosition:function(event) {
        logger.info("<<<<<<<<<<<<<<<<<<<<< ADJUST POSTITITITITITI >>>>>>>>>>>>>>>>");
        var savedVisibility = this.getNextElement().style.visibility;
        this.getNextElement().style.visibility = "hidden";
        if (this.horizontal) {
            var leftElementHeight = (Element.getHeight(this.getPreviousElement()));
            var rightElementHeight = (Element.getHeight(this.getNextElement()));

            var tableHeight = Element.getHeight(this.getContainerElement());
            var totalHeight = (parseInt(leftElementHeight) + parseInt(rightElementHeight));
            var diff = this.getDifference(event);
            var inPercent;
            if (this.resizeAction == "inc") {
                inPercent = (leftElementHeight + diff) / tableHeight;
                topInPercent = Math.round(inPercent * 100);
                bottomInPercent = 99 - topInPercent;
                this.getPreviousElement().style.height = (topInPercent) + "%";
                //            this.getNextElement().style.height = bottomInPercent + "%"

            } else {
                inPercent = (leftElementHeight - diff) / tableHeight;
                topInPercent = Math.round(inPercent * 100);
                bottomInPercent = 99 - topInPercent;
                this.getPreviousElement().style.height = (topInPercent) + "%";
                //            this.getNextElement().style.height = bottomInPercent + "%"

            }
        } else {
            var leftElementWidth = (Element.getWidth(this.getPreviousElement()));
            var rightElementWidth = (Element.getWidth(this.getNextElement()));
            var splitterWidth = (Element.getWidth(this.source));
            var tableWidth = Element.getWidth(this.getContainerElement());
            var totalWidth = (parseInt(leftElementWidth) + parseInt(rightElementWidth));
            var diff = this.getDifference(event);
            if (this.resizeAction == "inc") {
                inPercent = (leftElementWidth + diff) / tableWidth;
                leftInPercent = Math.round(inPercent * 100);
                rightInPercent = 100 - leftInPercent;
                this.getPreviousElement().style.width = leftInPercent + "%";
                //            this.getNextElement().style.width = rightInPercent + "%"


            } else {
                inPercent = (leftElementWidth - diff) / tableWidth;
                leftInPercent = Math.round(inPercent * 100);
                rightInPercent = 100 - leftInPercent;
                this.getPreviousElement().style.width = leftInPercent + "%";
                //            this.getNextElement().style.width = rightInPercent + "%"

            }
        }
        Ice.PanelDivider.adjustSecondPaneSize(this.source, this.horizontal);
        this.getNextElement().style.visibility = savedVisibility;
        inPercent = inPercent + 0.01;
        this.submitInfo(event, inPercent);
    },

    submitInfo:function(event, inPercent) {
        var form = Ice.util.findForm(this.source);
        var clientId = this.getContainerElement().id;
        var firstPaneStyleElement = $(clientId + "FirstPane");
        var secondPaneStyleElement = $(clientId + "SecondPane");
        var inPercentElement = $(clientId + "InPercent");
        firstPaneStyleElement.value = this.getPreviousElement().style.cssText;
        secondPaneStyleElement.value = this.getNextElement().style.cssText;
        inPercentElement.value = Math.round(inPercent * 100);
        iceSubmitPartial(form, this.source, event);
    }

});

Ice.PanelDivider.adjustSecondPaneSize = function(divider, isHorizontal) {
    divider = $(divider);
    //    var container = $(Ice.PanelDivider.prototype.getContainerElement.call({source:divider})); // <ice:panelDivider>
    var container = $(divider.parentNode); // dimensions could be different from <ice:panelDivider>
    var firstPane = $(Ice.PanelDivider.prototype.getPreviousElement.call({source:divider}));
    var secondPane = $(Ice.PanelDivider.prototype.getNextElement.call({source:divider}));
    // Assuming no padding in container, no margin in divider and panes, and no padding or border in 2nd pane.
    // No way to determine their pixel values. Also, there may be margin collapsing, and
    // (offsetWidth - clientWidth) may include the scrollbar width, not just the border width.
    if (isHorizontal) {
        secondPane.style.height = container.clientHeight - firstPane.offsetHeight - divider.offsetHeight + "px";
    } else {
        // Firefox often wraps right pane around even though it should fit exactly, therefore subtract 1 more pixel.
        secondPane.style.width = container.clientWidth - firstPane.offsetWidth - divider.offsetWidth - 1 + "px";
    }
}

Ice.PanelDivider.dividerHash = $H();

Ice.PanelDivider.onWindowResize = function() {
    Ice.PanelDivider.dividerHash.each(function(pair) {
        if (!$(pair.key)) {
            Ice.PanelDivider.dividerHash.unset(pair.key);
            return;
        }
        Ice.PanelDivider.adjustSecondPaneSize(pair.key, pair.value);
    });
}

Ice.PanelDivider.onLoad = function(divider, isHorizontal) {
    Event.stopObserving(window, "resize", Ice.PanelDivider.onWindowResize); // Will register multiple times if don't do this?
    Ice.PanelDivider.dividerHash.set(divider, isHorizontal); // Will replace existing, if any.
    Event.observe(window, "resize", Ice.PanelDivider.onWindowResize);
    Ice.PanelDivider.adjustSecondPaneSize(divider, isHorizontal);
    Ice.PanelDivider.adjustPercentBasedHeight(divider, isHorizontal);
}

ResizableUtil = {
    adjustHeight:function(src) {
        var height = Element.getHeight(src);
        var paddingTop = parseInt(Element.getStyle(src, 'padding-top'));
        var paddingBottom = parseInt(Element.getStyle(src, 'padding-top'));
        if (paddingTop && paddingTop > 1) {
            height -= paddingTop;
        }
        if (paddingBottom && paddingBottom > 1) {
            height -= paddingBottom;
        }
        src.firstChild.style.height = (height - 1) + 'px';
    }
}

//this function added to fix ICE-4044 (Issue when setting panelDivider to a non-fixed height )
Ice.PanelDivider.adjustPercentBasedHeight = function(divider, isHorizontal) {
    if (isHorizontal)return;
    var rootElementId = divider.replace("Divider", "");
    var rootElement = $(rootElementId);

    var rootHeight = Element.getStyle(rootElement, 'height');
    var percentBasedHeight = null;
    if (rootHeight && rootHeight.indexOf("%") > 0) {
        percentBasedHeight = rootHeight.split("%")[0];
    }
    if (percentBasedHeight) {
        parentHeight = Ice.PanelDivider.getParentHeight(rootElement);
        newVal = Math.round(parentHeight * (percentBasedHeight / 100));
        rootElement.style.height = newVal + "px";
        $(divider).style.height = newVal + "px";
    }
}

//this function recusivly check the height of the parent element, until one found
//if none found and body has reached, then return the height of the viewport
Ice.PanelDivider.getParentHeight = function(element) {
    //if ture means that height is not assigned to any parent, so now get the
    //height of the viewPort
    if (element.tagName == 'BODY') {
        var viewPortHeight = document.viewport.getHeight();
        //for opera get the window.innerHeight
        if (Prototype.Browser.WebKit && typeof window.innerHeight != 'undefined') {
            viewPortHeight = window.innerHeight;
        }       //sub 4 to avoid scrollbar
        return (viewPortHeight - 4);
    }
    var sHeight = Element.getStyle(element, 'height');
    if (sHeight.indexOf("%") > 0) {
        return Ice.PanelDivider.getParentHeight(element.parentNode);
    } else {
        sHeight = Element.getHeight(element);
        //if no height defined on the element, it returns 2 without any unit
        //so get the height of its parent
        if (sHeight == "2") {

            return Ice.PanelDivider.getParentHeight(element.parentNode);
        }
    }
    return sHeight;
}
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

Ice.KeyNavigator = Class.create({
    initialize: function(componentId) {
        this.component = $(componentId);
        this.component.onkeydown = this.keydown.bindAsEventListener(this);
    },

    keydown: function(event) {
        this.srcElement = Event.element(event);
        switch (event.keyCode) {

            case Event.KEY_RETURN:
                this.showMenu(event);
                break;
            /*
             case Event.KEY_UP:
             this.goNorth(event);
             Event.stop(event);
             break;

             case Event.KEY_DOWN:
             this.goSouth(event);
             Event.stop(event);
             break;

             case Event.KEY_LEFT:
             this.goWest(event);
             Event.stop(event);
             break;
             */
            case Event.KEY_RIGHT:
                this.goEast(event);
                Event.stop(event);
                break;
        }
    },

    goNorth: function(event) {
    },

    goSouth: function(event) {
    },

    goWest: function(event) {
    },

    goEast: function(event) {
    }

});

Ice.MenuBarKeyNavigator = Class.create(Ice.KeyNavigator, {
    initialize: function($super, componentId, displayOnClick) {
        $super(componentId);
        this.displayOnClick = displayOnClick;
        this.component.onclick = this.hideAll.bindAsEventListener(this);
        document.onclick = this.hideAllDocument.bindAsEventListener(this);

        if (Element.hasClassName(this.component, 'iceMnuBarVrt')) {
            this.vertical = true;
        } else {
            this.vertical = false;
        }
        this.clicked = true;
        this.configureRootItems();
    }
});

Ice.MenuBarKeyNavigator.addMethods({
    goEast: function(event) {
        var rootItem = this.srcElement.up('.' + this.getMenuBarItemClass());
        if (rootItem) {
            //        Element.removeClassName(rootItem, 'iceMnuBarItemhover');
            var nextItem = rootItem.next('.' + this.getMenuBarItemClass());
            if (nextItem) {
                //            Element.addClassName(nextItem, 'iceMnuBarItemhover');
                var anch = nextItem.down('a');
                anch.focus();
            }
            return;
        }

        var submenu = this.srcElement.down('.' + this.getSubMenuIndClass());
        if (submenu) {
            //       Element.removeClassName(submenu, 'iceMnuBarItemhover');
            var pdiv = this.srcElement.up('.iceMnuItm');
            //     Element.addClassName(pdiv, 'iceMnuBarItemhover');
            var submnuDiv = $(pdiv.id + '_sub');
            var firstAnch = submnuDiv.down('a');
            firstAnch.focus();

        }
    },

    goWest: function(event) {
        var rootItem = this.srcElement.up('.' + this.getMenuBarItemClass());
        if (rootItem) {
            Element.removeClassName(rootItem, 'iceMnuBarItemhover');
            var previousItem = rootItem.previous('.' + this.getMenuBarItemClass());
            if (previousItem) {
                Element.addClassName(previousItem, 'iceMnuBarItemhover');
                var anch = previousItem.down('a');
                anch.focus();
            }
            return;
        }

        var submenu = this.srcElement.previous('.iceMnuItm');
        Element.removeClassName(submenu, 'iceMnuItemhover');
        if (submenu == null) {
            var pdiv = this.srcElement.up('.' + this.getSubMenuClass());
            if (pdiv) {
                var owner = $(pdiv.id.substring(0, pdiv.id.length - 4));
                if (owner) {
                    var anch = owner.down('a');
                    Element.addClassName(owner, 'iceMnuItemhover');
                    anch.focus();
                }
            }
        }
    },

    goSouth: function(event) {
        var rootItem = this.srcElement.up('.' + this.getMenuBarItemClass());
        if (rootItem) {
            Element.removeClassName(rootItem, 'iceMnuBarItemhover');
            var submenu = $(rootItem.id + '_sub');
            if (submenu) {
                var anch = submenu.down('a');
                Element.addClassName(submenu, 'iceMnuBarItemhover');
                anch.focus();
            }
            return;
        }
        var menuItem = this.srcElement.up('.iceMnuItm');
        Element.removeClassName(menuItem, 'iceMnuBarItemhover');
        var nextItem = menuItem.next('.iceMnuItm');
        if (nextItem) {
            var anch = nextItem.down('a');
            Element.addClassName(nextItem, 'iceMnuBarItemhover');
            anch.focus();
        }
    },

    goNorth: function(event) {
        var menuItem = this.srcElement.up('.iceMnuItm');
        Element.removeClassName(menuItem, 'iceMnuBarItemhover');
        var previousItem = menuItem.previous('.iceMnuItm');
        if (previousItem) {
            var anch = previousItem.down('a');
            Element.addClassName(previousItem, 'iceMnuItemhover');
            anch.focus();
        }
    },

    getMenuBarItemClass: function(event) {
        if (this.vertical) {
            return "iceMnuBarVrtItem";
        } else {
            return "iceMnuBarItem";
        }
    },

    getSubMenuClass: function(event) {
        if (this.vertical) {
            return "iceMnuBarVrtSubMenu";
        } else {
            return "iceMnuBarSubMenu";
        }
    },

    getSubMenuIndClass: function(event) {
        if (this.vertical) {
            return "iceMnuBarVrtSubMenuInd";
        } else {
            return "iceMnuBarSubMenuInd";
        }
    },

    getRootClass: function() {
        if (this.vertical) {
            return "iceMnuBarVrt";
        } else {
            return "iceMnuBar";
        }
    },

    hover: function(event) {
        if (this.clicked) {
            element = Event.element(event).up('.' + this.getMenuBarItemClass());
            if (!element) return;
            var submenu = $(element.id + '_sub');
            Ice.Menu.hideOrphanedMenusNotRelatedTo(element);
            if (this.vertical) {
                var rootElement = element.up('.' + this.getRootClass())
                Ice.Menu.show(rootElement, submenu, element);
            } else {
                Ice.Menu.show(element, submenu, null);
            }
        }
    },

    mousedown: function(event) {
        element = Event.element(event);
        if (this.clicked) {
            this.clicked = false;
        } else {
            this.clicked = true;
            this.hover(event);
        }
    },

    focus: function(event) {
        this.hover(event);
    },

    configureRootItems: function () {
        var rootLevelItems = this.component.childNodes;
        for (i = 0; i < rootLevelItems.length; i++) {
            if (rootLevelItems[i].tagName == "DIV") {
                if (Element.hasClassName(rootLevelItems[i], this.getMenuBarItemClass())) {
                    rootLevelItems[i].onmouseover = this.hover.bindAsEventListener(this);
                    //add focus support
                    var anch = rootLevelItems[i].firstChild;
                    if (anch.tagName == "A") {
                        anch.onfocus = this.focus.bindAsEventListener(this);
                    }
                    if (this.displayOnClick) {
                        rootLevelItems[i].onmousedown = this.mousedown.bindAsEventListener(this);
                        this.clicked = false;
                    }
                }
            }
        }
    },

    hideAll:function(event) {
        element = Event.element(event);
        var baritem = element.up('.' + this.getMenuBarItemClass());
        var elt = event.element();
        if (elt && elt.match("a[onclick]")) {
            elt = elt.down();
        }
        if (elt) {
            elt = elt.up(".iceMnuItm a[onclick^='return false']");
        }
        if (!(baritem && this.clicked) && !elt) {
            Ice.Menu.hideAll();
            if (this.displayOnClick) {
                this.clicked = false;
            }
        }
        event.stopPropagation();
    },

    hideAllDocument:function(event) {
        Ice.Menu.hideAll();
    },

    showMenu:function(event) {
        element = Event.element(event);
        var baritem = element.up('.' + this.getMenuBarItemClass());
        if (baritem && this.displayOnClick) {
            this.mousedown(event);
        }
    }

});
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

Ice.dataTable = {};

Ice.dataTable.DataTable = Class.create({
    initialize: function(id) {
        this.id = id;
        this.resizeObserver = this.resize.bindAsEventListener(this);
    },

    resize: function() {
        var table = $(this.id);
        if (!table) return;
        var scrollTable = table.select("div.iceDatTblScrlSpr")[0];
        //no scrollabletable
        if (!scrollTable) return;

        var spacer = scrollTable.select("table > thead > tr > th:last-child > div")[0];
        var body = table.select("div.iceDatTblScrlSpr + div")[0];
        //nobody
        if (!body) return;

        var borderLeftWidth = body.getStyle("borderLeftWidth");
        var borderRightWidth = body.getStyle("borderRightWidth");
        if (Prototype.Browser.IE && body.scrollHeight > body.clientHeight) {
            body.style.overflowX = "hidden";
            body.style.overflowY = "scroll";
        }
        var width = body.getWidth();
        var scrollWidth = width - body.clientWidth;

        //no scroller
        if (scrollWidth == 0) return;

        body.setStyle({borderLeftWidth:0, borderRightWidth:0});
        var innerTable = body.select("table")[0];
        var headerTable = scrollTable.select("table")[0];

        if (spacer)
            spacer.setStyle({width:scrollWidth + "px"});

        //fixing IE6 bug, table width should be decreased by scrollWidth    
        var innerTable = body.select("table")[0];
        if (innerTable) {
            var innerTableWidth = innerTable.getWidth();
            if (Prototype.Browser.IE) {
                //            innerTable.setStyle({width:body.clientWidth  + "px"});
            }
        }

        body.setStyle({borderLeftWidth:borderLeftWidth, borderRightWidth:borderRightWidth});

    }
});

Ice.dataTable.DataTable.hash = $H();

Ice.dataTable.onLoad = function(id) {
    var table = Ice.dataTable.DataTable.hash.get(id);
    if (table) {
        Event.stopObserving(window, "load", table.resizeObserver);
        Event.stopObserving(window, "resize", table.resizeObserver);
    }
    table = new Ice.dataTable.DataTable(id);
    table.resize();
    Event.observe(window, "load", table.resizeObserver);
    Event.observe(window, "resize", table.resizeObserver);
    Ice.dataTable.DataTable.hash.set(id, table);
};
Ice.PanelConfirmation = Class.create({
    initialize: function(trigger, e, confirmationPanelId, autoCentre, draggable, displayAtMouse, iframeUrl, handler) {
        this.srcComp = trigger;
        this.event = e;
        this.panel = $(confirmationPanelId);
        this.url = iframeUrl;
        this.srcHandler = handler;

        this.isAutoCentre = autoCentre;
        this.isDraggable = draggable;
        this.isAtMouse = displayAtMouse;

        Ice.PanelConfirmation.current = this;
        this.showPanel();
    },
    showPanel: function() {
        Ice.modal.start(this.panel.id, this.url);
        Ice.iFrameFix.start(this.panel.id, this.url);
        this.panel.style.display = '';
        this.handleDraggableObject();
        Ice.autoPosition.stop(this.panel.id);
        if (this.isAtMouse) {
            this.panel.style.left = parseInt(Event.pointerX(this.event)) + "px";
            this.panel.style.top = parseInt(Event.pointerY(this.event)) + "px";
        } else {
            Ice.autoCentre.start(this.panel.id);
        }
        if (!this.isAutoCentre) {
            Ice.autoCentre.stop(this.panel.id);
        }
        this.setDefaultFocus();
    },
    accept: function() {
        this.close();
        setFocus(this.srcComp.id);
        this.srcHandler.call(this.srcComp, this.event);
    },
    cancel: function() {
        this.close();
    },
    close: function() {
        Ice.PanelConfirmation.current = null;
        this.panel.style.visibility = 'hidden';
        this.panel.style.display = 'none';
        Ice.modal.stop(this.panel.id);
        Ice.autoCentre.stop(this.panel.id);
        Draggable.removeMe(this.panel.id);
        Ice.Focus.setFocus(this.srcComp.id);
    },
    handleDraggableObject: function() {
        if (this.isDraggable) {
            Ice.DnD.adjustPosition(this.panel.id);
            new Draggable(this.panel.id, {
                handle:this.panel.id + '-handle',
                dragGhost:false,
                dragCursor:false,
                ghosting:false,
                revert:false,
                mask:'1,2,3,4,5'
            });
        }
    },
    setDefaultFocus: function() {
        var cancel = $(this.panel.id + '-cancel');
        if (cancel) {
            cancel.focus();
        } else {
            $(this.panel.id + '-accept').focus();
        }
    }
});

Ice.PanelConfirmation.current = null;

Ice.Calendar = {};
Ice.Calendar.listeners = {};

Ice.Calendar.addCloseListener = function(calendar, form, commandLink, hiddenField) {
    if (Ice.Calendar.listeners[calendar]) {
        return;
    } else {
        Ice.Calendar.listeners[calendar] = new Ice.Calendar.CloseListener(calendar, form, commandLink, hiddenField);
    }
};

Ice.Calendar.CloseListener = Class.create({
    initialize: function(calendar, form, commandLink, hiddenField) {
        this.calendarId = calendar;
        this.formId = form;
        this.commandLinkId = commandLink;
        this.hiddenFieldId = hiddenField;

        this.popupId = this.calendarId + '_ct';
        this.buttonId = this.calendarId + '_cb'

        this.handler = this.closePopupOnClickOutside.bindAsEventListener(this);
        Event.observe(document, 'click', this.handler);
    },
    closePopupOnClickOutside: function(event) {
        if (this.getPopup()) {
            if (this.isInPopup(event.element())) {
                return;
            }
            if (this.isWithin(this.getPopup(), event)) {
                return;
            }
            if (event.element() == this.getButton()) {
                this.dispose();
                return;
            }

            var id = event.element().id;
            if (id) setFocus(id);
            else setFocus('');

            this.submit(event);
            this.dispose();
        }
    },
    isInPopup: function(element) {
        if (element.id == this.popupId) return true;
        if (element == undefined || element == document) return false;
        return this.isInPopup(element.parentNode);
    },
    isWithin: function(element, event) {
        return Position.within(element, Event.pointerX(event), Event.pointerY(event));
    },
    dispose: function() {
        Ice.Calendar.listeners[this.calendarId] = null;
        Event.stopObserving(document, 'click', this.handler);
    },
    submit: function(event) {
        document.forms[this.formId][this.commandLinkId].value = this.getButton().id;
        document.forms[this.formId][this.hiddenFieldId].value = 'toggle';
        iceSubmitPartial(document.forms[this.formId], this.getButton(), event);
    },
    getPopup: function() {
        return $(this.popupId);
    },
    getButton: function() {
        return $(this.buttonId);
    }
});

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

Ice.Menu = Class.create();
Ice.Menu = {
    menuContext:null,
    currentMenu:null,
    openMenus:new Array(0),
    printOpenMenus:function() {
        var openMenuString = '';
        for (var i = 0; i < Ice.Menu.openMenus.length; i++) {
            openMenuString = openMenuString + Ice.Menu.openMenus[i].id + ' , ';
        }
        return openMenuString;
    },
    printHoverMenuAndOpenMenus: function(hoverMenu) {
        alert('hoverMenu=[' + hoverMenu.id + ']\n'
                + 'openMenus=[' + Ice.Menu.printOpenMenus() + ']');
    },
    printArray: function(arrayToPrint) {
        var buffer = '';
        for (var i = 0; i < arrayToPrint.length; i++) {
            buffer = buffer + arrayToPrint[i] + ', ';
        }
        return buffer;
    },
    printArrayOfIds: function(arrayToPrint) {
        var buffer = '';
        for (var i = 0; i < arrayToPrint.length; i++) {
            buffer = buffer + arrayToPrint[i].id + ', ';
        }
        return buffer;
    },
    hideAll: function() {
        for (var i = 0; i < Ice.Menu.openMenus.length; i++) {
            if (Ice.Menu.openMenus[i].iframe) Ice.Menu.openMenus[i].iframe.hide(); // ICE-2066, ICE-2912
            Ice.Menu.openMenus[i].style.display = 'none';
        }
        Ice.Menu.openMenus = new Array();
        Ice.Menu.currentMenu = null;
        Ice.Menu.menuContext = null;
    },
    getPosition: function(element, positionProperty) {
        var position = 0;
        while (element != null) {
            position += element["offset" + positionProperty];
            element = element.offsetParent;
        }
        return position;
    },
    show: function(supermenu, submenu, submenuDiv) {
        supermenu = $(supermenu);
        submenu = $(submenu);
        submenuDiv = $(submenuDiv);
        if (submenu) {
            var menu = $(submenu);
            //menu is already visible, don't do anything
            if (menu && menu.style.display == '') return;
            Ice.Menu.showMenuWithId(submenu);
            if (submenuDiv) {
                // ICE-3196, ICE-3620
                if (supermenu.viewportOffset().left + supermenu.offsetWidth + submenu.offsetWidth < document.viewport.getWidth()) {
                    submenu.clonePosition(supermenu, {setTop:false, setWidth:false, setHeight:false, offsetLeft:supermenu.offsetWidth});
                } else {
                    submenu.clonePosition(supermenu, {setTop:false, setWidth:false, setHeight:false, offsetLeft:- submenu.offsetWidth});
                }
                if (submenuDiv.viewportOffset().top + submenu.offsetHeight < document.viewport.getHeight()) {
                    submenu.clonePosition(submenuDiv, {setLeft:false, setWidth:false, setHeight:false});
                } else {
                    submenu.clonePosition(submenuDiv, {setLeft:false, setWidth:false, setHeight:false,
                        offsetTop:- submenu.offsetHeight + submenuDiv.offsetHeight});
                }
            } else {
                // ICE-3196, ICE-3620
                if (supermenu.viewportOffset().left + submenu.offsetWidth < document.viewport.getWidth()) {
                    submenu.clonePosition(supermenu, {setTop:false, setWidth:false, setHeight:false});
                } else {
                    submenu.clonePosition(supermenu, {setTop:false, setWidth:false, setHeight:false,
                        offsetLeft:document.viewport.getWidth() - supermenu.viewportOffset().left - submenu.offsetWidth});
                }
                if (supermenu.viewportOffset().top + supermenu.offsetHeight + submenu.offsetHeight < document.viewport.getHeight()) {
                    submenu.clonePosition(supermenu, {setLeft:false, setWidth:false, setHeight:false, offsetTop:supermenu.offsetHeight});
                } else {
                    submenu.clonePosition(supermenu, {setLeft:false, setWidth:false, setHeight:false, offsetTop:- submenu.offsetHeight});
                }
            }
            if (submenu.viewportOffset().top < 0) { // ICE-3658
                submenu.clonePosition(submenu, {setLeft:false, setWidth:false, setHeight:false, offsetTop:- submenu.viewportOffset().top});
            }
            Ice.Menu.showIframe(submenu); // ICE-2066, ICE-2912
        }
        Ice.Menu.currentMenu = submenu;
    },
    showPopup: function(showX, showY, submenu) {
        Ice.Menu.hideAll();
        submenu = $(submenu);
        if (submenu) {
            Ice.Menu.showMenuWithId(submenu);
            var styleLeft = showX + "px";
            submenu.style.left = styleLeft;

            var styleTop = showY + "px";
            submenu.style.top = styleTop;
            Ice.Menu.showIframe(submenu); // ICE-2066, ICE-2912
        }
        Ice.Menu.currentMenu = submenu;
    },
    showIframe: function(menuDiv) { // ICE-2066, ICE-2912
        if (!Prototype.Browser.IE) return;
        if (parseFloat(navigator.userAgent.substring(navigator.userAgent.indexOf("MSIE") + 5)) >= 7) return;
        var iframe = menuDiv.iframe;
        if (!iframe) {
            menuDiv.iframe = iframe = new Element("iframe", {src: 'javascript:\'<html></html>\';', frameborder: "0", scrolling: "no"});
            iframe.setStyle({position: "absolute", opacity: 0}).hide();
            menuDiv.insert({before: iframe});
        }
        iframe.clonePosition(menuDiv).show();
    },
    contextMenuPopup: function(event, popupMenu, targComp) {
        var dynamic = $(popupMenu + "_dynamic");
        if (!event) {
            event = window.event;
        }
        if (event) {
            event.returnValue = false;
            event.cancelBubble = true;

            if (event.stopPropagation) {
                event.stopPropagation();
            }

            var posx = 0; // Mouse position relative to
            var posy = 0; //  the document
            if (event.pageX || event.pageY) {
                posx = event.pageX;
                posy = event.pageY;
            }
            else if (event.clientX || event.clientY) {
                posx = event.clientX + document.body.scrollLeft
                        + document.documentElement.scrollLeft;
                posy = event.clientY + document.body.scrollTop
                        + document.documentElement.scrollTop;
            }
            if (dynamic) {
                dynamic.value = posx + ", " + posy + ", " + popupMenu + ", " + targComp;
                try {
                    var form = Ice.util.findForm(dynamic);
                    iceSubmitPartial(form, dynamic, event);
                } catch (e) {
                    logger.info("Form not found" + e);
                }
                return;
            }

            Ice.Menu.showIt(posx, posy, popupMenu, targComp);
        }
    },
    showIt: function(posx, posy, popupMenu, targComp) {
        Ice.Menu.showPopup(posx, posy, popupMenu.strip());
        Event.observe(document, "click", Ice.Menu.hidePopupMenu);
        Ice.Menu.setMenuContext(targComp.strip());
    },

    setMenuContext: function(mnuCtx) {
        if (Ice.Menu.menuContext == null) {
            Ice.Menu.menuContext = mnuCtx;
        }
    },
    hideOrphanedMenusNotRelatedTo: function(hoverMenu) {
        // form an array of allowable names
        var relatedMenus = new Array();
        var idSegments = hoverMenu.id.split(':');
        var nextRelatedMenu = '';
        for (var i = 0; i < idSegments.length; i++) {
            nextRelatedMenu = nextRelatedMenu + idSegments[i];
            var concatArray = new Array(nextRelatedMenu + '_sub');
            relatedMenus = relatedMenus.concat(concatArray);
            nextRelatedMenu = nextRelatedMenu + ':';
        }
        // iterate over open menus and set display='none' for any menu
        // that is not in the array of menus related to the current menu
        var arrayLength = Ice.Menu.openMenus.length;
        var menusToHide = new Array();
        for (var j = 0; j < arrayLength; j ++) {
            var nextOpenMenu = $(Ice.Menu.openMenus[j]);
            var found = 'false';
            for (var k = 0; k < relatedMenus.length; k++) {
                if (nextOpenMenu.id == relatedMenus[k]) {
                    found = 'true';
                }
            }
            if (found != 'true') {
                menusToHide[menusToHide.length] = nextOpenMenu.id;
                if (nextOpenMenu == Ice.Menu.currentMenu) {
                    Ice.Menu.currentMenu = null;
                }
            }
        }
        // iterate over the menus to hide
        Ice.Menu.hideMenusWithIdsInArray(menusToHide);
    },
    hideSubmenu: function(hoverMenu) {
        var cur = Ice.Menu.currentMenu;
        var hoverParentId = hoverMenu.id.substring(0, hoverMenu.id.lastIndexOf(':'));
        var curParentId = cur.id.substring(0, cur.id.lastIndexOf(':'));
        if (hoverParentId == curParentId) {
            Ice.Menu.hideMenuWithId(Ice.Menu.currentMenu);
        }
    },
    hideMenusWithIdsInArray: function(menuIdArray) {
        if (menuIdArray) {
            for (var i = 0; i < menuIdArray.length; i ++) {
                Ice.Menu.hideMenuWithId(menuIdArray[i]);
            }
        }
    },
    hideMenuWithId: function(menu) {
        menu = $(menu);
        if (menu) {
            if (menu.iframe) menu.iframe.hide(); // ICE-2066, ICE-2912
            menu.style.display = 'none';
            Ice.Menu.removeFromOpenMenus(menu);
        }
        return;
    },
    removeFromOpenMenus: function(menu) {
        var tempArray = new Array();
        for (var i = 0; i < Ice.Menu.openMenus.length; i ++) {
            if (Ice.Menu.openMenus[i].id != menu.id) {
                tempArray = tempArray.concat(new Array(Ice.Menu.openMenus[i]));
            }
        }
        Ice.Menu.openMenus = tempArray;
    },
    showMenuWithId: function(menu) {
        if (menu) {
            menu = $(menu);
            menu.style.display = '';
            Ice.Menu.addToOpenMenus(menu);
        }
    },
    addToOpenMenus: function(menu) {
        if (menu) {
            menu = $(menu);
            var found = 'false';
            for (var i = 0; i < Ice.Menu.openMenus.length; i ++) {
                if (Ice.Menu.openMenus[i].id == menu.id) {
                    found = 'true';
                    break;
                }
            }
            if (found != 'true') {
                var openMenu = new Array(menu);
                Ice.Menu.openMenus = Ice.Menu.openMenus.concat(openMenu);
            }

        }
    },
    appendHoverClasses: function(menuItem) {
        var styleClasses = menuItem.className.replace(/^\s+|\s+$/g, "").split(/\s+/);
        if (styleClasses[0] == "") return;

        for (var i = 0; i < styleClasses.length; i++) {
            if (styleClasses[i] == "portlet-menu-item-selected") {
                menuItem.className += " portlet-menu-item-hover-selected";
            } else {
                menuItem.className += " " + styleClasses[i] + "-hover";
            }
        }
    },
    removeHoverClasses: function(menuItem) {
        var n = menuItem.className.replace(/^\s+|\s+$/g, "").split(/\s+/).length / 2;
        var regExp = new RegExp("( portlet-menu-item-hover-selected| \\S+-hover){" + n + "}$");
        menuItem.className = menuItem.className.replace(regExp, "");
    },
    hidePopupMenu:function() {
        Ice.Menu.hideAll();
        Event.stopObserving(document, "click", Ice.Menu.hidePopupMenu);
    },
    hideOnMouseOut: function(rootID, event) {
        if (!event) {
            event = window.event;
        }
        if (event) {
            var element;
            element = event.relatedTarget;
            if (!element) element = event.toElement;
            if (element) {
                if (!Ice.Menu.isInMenu(element, rootID)) {
                    Ice.Menu.hideAll();
                }
            }
        }
    },
    isInMenu: function(element, rootID) {
        if (element.id == rootID) return true;
        if (element == undefined || element == document) return false;
        return Ice.Menu.isInMenu(element.parentNode, rootID);
    }
}

ice.onAfterUpdate(function() {
    if (Ice.StateMon) {
        Ice.StateMon.checkAll();
        Ice.StateMon.rebuild();
    }
});

Ice.treeNavigator = Class.create();
Ice.treeNavigator = {
  
      handleFocus: function (event, root, deep) {
           var type = event.type;
           if(type == 'click') {
              Ice.treeNavigator.reset();
              return;
           }

           var ele = Event.element(event);
           var kc= event.keyCode;
           var imgSrc = null;
           if (ele && ele.firstChild.getAttribute) {
              imgSrc = ele.firstChild.getAttribute('src');
           }
          if (!imgSrc) return;
          switch (kc) {
	          case 37: //left
	          //root node
	          if (imgSrc.indexOf('top_close_no_siblings') > 0 ||
	          imgSrc.indexOf('middle_close') > 0 ||
	          imgSrc.indexOf('bottom_close') > 0 ) {
	            logger.info('LEFT_KEY: top_close_no_siblings FOUND, root Node opend close it and reinitialize index'); 
	            ele.onclick();
	            Ice.treeNavigator.reset();
                return false;
	          }    
	    
	          break;
	
	          case 39: //right
		          if (imgSrc.indexOf('top_open_no_siblings') > 0 ||
		          imgSrc.indexOf('middle_open') > 0 ||
		          imgSrc.indexOf('bottom_open') > 0 ) {
		                ele.onclick();
		                Ice.treeNavigator.reset();  
		                return false;             
		          }        
	                
	          break;
	
	          case 38: //up
	              if (!Ice.treeNavigator.anchors) {
		             Ice.treeNavigator.updateAnchor(root, ele, deep);
		          }
		
		          if (imgSrc) {
		             if(imgSrc.indexOf('top_close_no_siblings') > 0) {
			            Ice.treeNavigator.index = 1;
			            Ice.treeNavigator.anchors[Ice.treeNavigator.index].focus();
		             }
		            Ice.treeNavigator.focusPrevious();
		          }  else {
		            Ice.treeNavigator.focusPrevious();
		          }
	          return false;              
	          case 40: //down
	              logger.info ('down'); 
	              if (!Ice.treeNavigator.anchors) {
	                Ice.treeNavigator.updateAnchor(root, ele, deep);
	              }
	              Ice.treeNavigator.focusNext();
	          return false;
          }//switch ends    
      }, //func ends
  
          index:0,
          
          anchors:null,
          
          reset: function() {
             Ice.treeNavigator.index = 0;
             Ice.treeNavigator.anchors = null;     
      },
      
      focusNext: function(deep) {  
         if (Ice.treeNavigator.index <(Ice.treeNavigator.anchors.length-1)){
             Ice.treeNavigator.index = Ice.treeNavigator.index + 1;
         }
         Ice.treeNavigator.anchors[Ice.treeNavigator.index].focus();
      },
      
      focusPrevious : function(deep) {
         if (Ice.treeNavigator.index>0) {
             Ice.treeNavigator.index = Ice.treeNavigator.index - 1;
         }
         Ice.treeNavigator.anchors[Ice.treeNavigator.index].focus();      
      },
      
      updateAnchor: function(root, ele, deep) {
        var anchors = [];
            if(deep) { 
                anchors = root.parentNode.getElementsByTagName('a');
                for (i=0; anchors.length > i; i++) {
                    if (ele == anchors[i]) {
                        Ice.treeNavigator.index = i;   
                    }
                }//for                  
            } else {   
	            _anchors = root.parentNode.getElementsByTagName('a');
	            j = 0;
	            for (i=0; _anchors.length > i; i++) {
	               if (_anchors[i].firstChild.src && _anchors[i].firstChild.src.indexOf('tree_nav') > 0) {
	                    if (ele == _anchors[i]) {
	                       Ice.treeNavigator.index = j;     
	                    }
	                    anchors[j++] = _anchors[i];
	               }
	            }//for    
            }
            Ice.treeNavigator.anchors = anchors;
         }//updateAnchor
  }// func ends
  
  Ice.tabNavigator = function(event) {
       var ele = Event.element(event);
       var kc= event.keyCode;
       switch (kc) {
          case 37:
          case 38:
             var preCell = ele.up('.icePnlTb').previousSibling;
             if (preCell) {
                 var lnk = preCell.down('.icePnlTbLblLnk');
                 if(lnk) {
                    lnk.focus();
                 }
             }
             if (ele.up('.icePnlTb')) {
                 if (event.preventDefault){
                     event.preventDefault();
                 } else if (event.returnValue){
                     event.returnValue = false;
                 }
             }           
          break; 
          case 39:
          case 40:
             var nextCell = ele.up('.icePnlTb').nextSibling;
             if (nextCell && Element.hasClassName(nextCell, 'icePnlTb')) {
                 var lnk = nextCell.down('.icePnlTbLblLnk');
                 if(lnk) {
                    lnk.focus();
                 }
             }
             if (ele.up('.icePnlTb')) {
                 if (event.preventDefault){
                     event.preventDefault();
                 } else if (event.returnValue){
                     event.returnValue = false;
                 }
             }
          break;            
       
      }    
  }
  
  Ice.pnlTabOnFocus = function(ele, facet, kbs) {
      setFocus(ele.id);
      if(kbs) { 
          Event.observe(ele, 'keydown', Ice.tabNavigator); 
      }   
      if (!facet) return;
      Ice.simulateFocus(ele.parentNode, ele);
  }
  
  Ice.pnlTabOnBlur = function(ele, facet, kbs) {
      if(kbs) { 
          Event.stopObserving(ele, 'keydown', Ice.tabNavigator);
      }
      if (!facet)return;    
      setFocus('');
      Ice.simulateBlur(ele.parentNode, ele);
  }