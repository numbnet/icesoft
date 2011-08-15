/**
 *  == DOM ==
 *  Extensions to DOM elements, plus other utilities for DOM traversal
 *  and modification.
 *
 *  Prototype's DOM extensions represent a large portion of where you'll spend
 *  your time. Prototype adds many convenience methods to elements returned by
 *  the [[$]] function. For instance, you can write
 *
 *      $('comments').addClassName('active').show();
 *
 *  to get the element with the ID of `comments`, add a class name to it, and
 *  show it (if it was previously hidden).
 *
 *  In other words, Prototype adds "instance" methods to DOM nodes. This is
 *  made possible by direct extension of the backing DOM objects (in browsers
 *  that support it) and by manual extension of individual nodes (in browsers
 *  that do not).
 *
 **/

/** section: DOM, related to: Element
 *  $(id) -> Element
 *  $(id...) -> [Element...]
 *    - id (String | Element): A DOM node or a string that references a node's
 *      ID.
 *
 *  If provided with a string, returns the element in the document with
 *  matching ID; otherwise returns the passed element.
 *
 *  Takes in an arbitrary number of arguments. Returns one [[Element]] if
 *  given one argument; otherwise returns an [[Array]] of [[Element]]s.
 *
 *  All elements returned by the function are "extended" with [[Element]]
 *  instance methods.
 *
 *  ##### More Information
 *
 *  The [[$]] function is the cornerstone of Prototype. Not only does it
 *  provide a handy alias for `document.getElementById`, it also lets you pass
 *  indifferently IDs (strings) or DOM node references to your functions:
 *
 *      function foo(element) {
 *          element = $(element);
 *          //  rest of the function...
 *      }
 *
 *  Code written this way is flexible — you can pass it the ID of the element
 *  or the element itself without any type sniffing.
 *
 *  Invoking it with only one argument returns the [[Element]], while invoking it
 *  with multiple arguments returns an [[Array]] of [[Element]]s (and this
 *  works recursively: if you're twisted, you could pass it an array
 *  containing some arrays, and so forth). As this is dependent on
 *  `getElementById`, [W3C specs](http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-getElBId)
 *  apply: nonexistent IDs will yield `null` and IDs present multiple times in
 *  the DOM will yield erratic results. *If you're assigning the same ID to
 *  multiple elements, you're doing it wrong!*
 *
 *  The function also *extends every returned element* with [[Element.extend]]
 *  so you can use Prototype's DOM extensions on it. In the following code,
 *  the two lines are equivalent. However, the second one feels significantly
 *  more object-oriented:
 *
 *      // Note quite OOP-like...
 *      Element.hide('itemId');
 *      // A cleaner feel, thanks to guaranted extension
 *      $('itemId').hide();
 *
 *  However, when using iterators, leveraging the [[$]] function makes for
 *  more elegant, more concise, and also more efficient code:
 *
 *      ['item1', 'item2', 'item3'].each(Element.hide);
 *      // The better way:
 *      $('item1', 'item2', 'item3').invoke('hide');
 *
 *  See [How Prototype extends the DOM](http://prototypejs.org/learn/extensions)
 *  for more info.
 **/

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

if (!Node) var Node = { };

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

/** section: DOM
 *  class Element
 *
 *  The [[Element]] object provides a variety of powerful DOM methods for
 *  interacting with DOM elements&nbsp;&mdash; creating them, updating them,
 *  traversing them, etc. You can access these either as methods of [[Element]]
 *  itself, passing in the element to work with as the first argument, or as
 *  methods on extended element *instances*:
 *
 *      // Using Element:
 *      Element.addClassName('target', 'highlighted');
 *
 *      // Using an extended element instance:
 *      $('target').addClassName('highlighted');
 *
 *  [[Element]] is also a constructor for building element instances from scratch,
 *  see [`new Element`](#new-constructor) for details.
 *
 *  Most [[Element]] methods return the element instance, so that you can chain
 *  them easily:
 *
 *      $('message').addClassName('read').update('I read this message!');
 *
 *  ##### More Information
 *
 *  For more information about extended elements, check out ["How Prototype
 *  extends the DOM"](http://prototypejs.org/learn/extensions), which will walk
 *  you through the inner workings of Prototype's DOM extension mechanism.
 **/

/**
 *  new Element(tagName[, attributes])
 *  - tagName (String): The name of the HTML element to create.
 *  - attributes (Object): An optional group of attribute/value pairs to set on
 *    the element.
 *
 *  Creates an HTML element with `tagName` as the tag name, optionally with the
 *  given attributes. This can be markedly more concise than working directly
 *  with the DOM methods, and takes advantage of Prototype's workarounds for
 *  various browser issues with certain attributes:
 *
 *  ##### Example
 *
 *      // The old way:
 *      var a = document.createElement('a');
 *      a.setAttribute('class', 'foo');
 *      a.setAttribute('href', '/foo.html');
 *      a.appendChild(document.createTextNode("Next page"));
 *
 *      // The new way:
 *      var a = new Element('a', {'class': 'foo', href: '/foo.html'}).update("Next page");
 **/

(function(global) {
    // For performance reasons, we create new elements by cloning a "blank"
    // version of a given element. But sometimes this causes problems. Skip
    // the cache if:
    //   (a) We're creating a SELECT element (troublesome in IE6);
    //   (b) We're setting the `type` attribute on an INPUT element
    //       (troublesome in IE9).
    function shouldUseCache(tagName, attributes) {
        if (tagName === 'select') return false;
        if ('type' in attributes) return false;
        return true;
    }

    var HAS_EXTENDED_CREATE_ELEMENT_SYNTAX = (function() {
        try {
            var el = document.createElement('<input name="x">');
            return el.tagName.toLowerCase() === 'input' && el.name === 'x';
        }
        catch(err) {
            return false;
        }
    })();

    var element = global.Element;

    global.Element = function(tagName, attributes) {
        attributes = attributes || { };
        tagName = tagName.toLowerCase();
        var cache = Element.cache;

        if (HAS_EXTENDED_CREATE_ELEMENT_SYNTAX && attributes.name) {
            tagName = '<' + tagName + ' name="' + attributes.name + '">';
            delete attributes.name;
            return Element.writeAttribute(document.createElement(tagName), attributes);
        }

        if (!cache[tagName]) cache[tagName] = Element.extend(document.createElement(tagName));

        var node = shouldUseCache(tagName, attributes) ?
            cache[tagName].cloneNode(false) : document.createElement(tagName);

        return Element.writeAttribute(node, attributes);
    };

    Object.extend(global.Element, element || { });
    if (element) global.Element.prototype = element.prototype;

})(this);

Element.idCounter = 1;
Element.cache = { };

// Performs cleanup on an element before it is removed from the page.
// See `Element#purge`.
Element._purgeElement = function(element) {
    var uid = element._prototypeUID;
    if (uid) {
        // Must go first because it relies on Element.Storage.
        Element.stopObserving(element);
        element._prototypeUID = void 0;
        delete Element.Storage[uid];
    }
}

/**
 *  mixin Element.Methods
 *
 *  [[Element.Methods]] is a mixin for DOM elements. The methods of this object
 *  are accessed through the [[$]] utility or through the [[Element]] object and
 *  shouldn't be accessed directly.
 *
 *  ##### Examples
 *
 *  Hide the element
 *
 *      $(element).hide();
 *
 *  Return an [[Enumerable]] of all descendant nodes of the element with the id
 *  "article"
 *
 *      $('articles').descendants();
 **/
Element.Methods = {
    /**
     *  Element.visible(@element) -> Boolean
     *
     *  Tells whether `element` is visible (i.e., whether its inline `display`
     *  CSS property is set to `none`.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="visible"></div>
     *      <div id="hidden" style="display: none;"></div>
     *
     *  And the associated JavaScript:
     *
     *      $('visible').visible();
     *      // -> true
     *
     *      $('hidden').visible();
     *      // -> false
     *
     *  ##### Notes
     *
     *  Styles applied via a CSS stylesheet are _not_ taken into consideration.
     *  Note that this is not a Prototype limitation, it is a CSS limitation.
     *
     *      language: html
     *      <style>
     *        #hidden-by-css {
     *          display: none;
     *        }
     *      </style>
     *
     *      [...]
     *
     *      <div id="hidden-by-css"></div>
     *
     *  And the associated JavaScript:
     *
     *      $('hidden-by-css').visible();
     *      // -> true
     **/
    visible: function(element) {
        return $(element).style.display != 'none';
    },

    /**
     *  Element.toggle(@element) -> Element
     *
     *  Toggles the visibility of `element`. Returns `element`.
     *
     *  ##### Examples
     *
     *      <div id="welcome-message"></div>
     *      <div id="error-message" style="display:none;"></div>
     *
     *      $('welcome-message').toggle();
     *      // -> Element (and hides div#welcome-message)
     *
     *      $('error-message').toggle();
     *      // -> Element (and displays div#error-message)
     *
     *  Toggle multiple elements using [[Enumerable#each]]:
     *
     *      ['error-message', 'welcome-message'].each(Element.toggle);
     *      // -> ['error-message', 'welcome-message']
     *
     *  Toggle multiple elements using [[Enumerable#invoke]]:
     *
     *      $('error-message', 'welcome-message').invoke('toggle');
     *      // -> [Element, Element]
     *
     *  ##### Notes
     *
     *  [[Element.toggle]] _cannot_ display elements hidden via CSS stylesheets.
     *  Note that this is not a Prototype limitation but a consequence of how the
     *  CSS `display` property works.
     *
     *      <style>
     *        #hidden-by-css {
     *          display: none;
     *        }
     *      </style>
     *
     *      [...]
     *
     *      <div id="hidden-by-css"></div>
     *
     *      $('hidden-by-css').toggle(); // WONT' WORK!
     *      // -> Element (div#hidden-by-css is still hidden!)
     **/
    toggle: function(element) {
        element = $(element);
        Element[Element.visible(element) ? 'hide' : 'show'](element);
        return element;
    },

    /**
     *  Element.hide(@element) -> Element
     *
     *  Sets `display: none` on `element`. Returns `element`.
     *
     *  ##### Examples
     *
     *  Hide a single element:
     *
     *      <div id="error-message"></div>
     *
     *      $('error-message').hide();
     *      // -> Element (and hides div#error-message)
     *
     *  Hide multiple elements using [[Enumerable#each]]:
     *
     *      ['content', 'navigation', 'footer'].each(Element.hide);
     *      // -> ['content', 'navigation', 'footer']
     *
     *  Hide multiple elements using [[Enumerable#invoke]]:
     *
     *      $('content', 'navigation', 'footer').invoke('hide');
     *      // -> [Element, Element, Element]
     **/
    hide: function(element) {
        element = $(element);
        element.style.display = 'none';
        return element;
    },

    /**
     *  Element.show(@element) -> Element
     *
     *  Removes `display: none` on `element`. Returns `element`.
     *
     *  ##### Examples
     *
     *  Show a single element:
     *
     *      <div id="error-message" style="display:none;"></div>
     *
     *      $('error-message').show();
     *      // -> Element (and displays div#error-message)
     *
     *  Show multiple elements using [[Enumerable#each]]:
     *
     *      ['content', 'navigation', 'footer'].each(Element.show);
     *      // -> ['content', 'navigation', 'footer']
     *
     *  Show multiple elements using [[Enumerable#invoke]]:
     *
     *      $('content', 'navigation', 'footer').invoke('show');
     *      // -> [Element, Element, Element]
     *
     *  ##### Notes
     *
     *  [[Element.show]] _cannot_ display elements hidden via CSS stylesheets.
     *  Note that this is not a Prototype limitation but a consequence of how the
     *  CSS `display` property works.
     *
     *      <style>
     *        #hidden-by-css {
     *          display: none;
     *        }
     *      </style>
     *
     *      [...]
     *
     *      <div id="hidden-by-css"></div>
     *
     *      $('hidden-by-css').show(); // DOES NOT WORK!
     *      // -> Element (div#error-message is still hidden!)
     **/
    show: function(element) {
        element = $(element);
        element.style.display = '';
        return element;
    },

    /**
     *  Element.remove(@element) -> Element
     *
     *  Completely removes `element` from the document and returns it.
     *
     *  If you would rather just hide the element and keep it around for further
     *  use, try [[Element.hide]] instead.
     *
     *  ##### Examples
     *
     *      language: html
     *      // Before:
     *      <ul>
     *        <li id="golden-delicious">Golden Delicious</li>
     *        <li id="mutsu">Mutsu</li>
     *        <li id="mcintosh">McIntosh</li>
     *        <li id="ida-red">Ida Red</li>
     *      </ul>
     *
     *  And the associated JavaScript:
     *
     *      $('mutsu').remove();
     *      // -> Element (and removes li#mutsu)
     *
     *  The resulting HTML:
     *
     *      language: html
     *      <ul>
     *        <li id="golden-delicious">Golden Delicious</li>
     *        <li id="mcintosh">McIntosh</li>
     *        <li id="ida-red">Ida Red</li>
     *      </ul>
     **/
    remove: function(element) {
        element = $(element);
        element.parentNode.removeChild(element);
        return element;
    },

    /**
     *  Element.update(@element[, newContent]) -> Element
     *
     *  Replaces _the content_ of `element` with the `newContent` argument and
     *  returns `element`.
     *
     *  `newContent` may be in any of these forms:
     *  - [[String]]: A string of HTML to be parsed and rendered
     *  - [[Element]]: An Element instance to insert
     *  - ...any object with a `toElement` method: The method is called and the resulting element used
     *  - ...any object with a `toHTML` method: The method is called and the resulting HTML string
     *    is parsed and rendered
     *
     *  If `newContent` is omitted, the element's content is blanked out (i.e.,
     *  replaced with an empty string).
     *
     *  If `newContent` is a string and contains one or more inline `<script>`
     *  tags, the scripts are scheduled to be evaluated after a very brief pause
     *  (using [[Function#defer]]) to allow the browser to finish updating the
     *  DOM. Note that the scripts are evaluated in the scope of
     *  [[String#evalScripts]], not in the global scope, which has important
     *  ramifications for your `var`s and `function`s.
     *  See [[String#evalScripts]] for details.
     *
     *  Note that this method allows seamless content update of table related
     *  elements in Internet Explorer 6 and beyond.
     *
     *  Any nodes replaced with `Element.update` will first have event
     *  listeners unregistered and storage keys removed. This frees up memory
     *  and prevents leaks in certain versions of Internet Explorer. (See
     *  [[Element.purge]]).
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="fruits">carrot, eggplant and cucumber</div>
     *
     *  Passing a regular string:
     *
     *      $('fruits').update('kiwi, banana and apple');
     *      // -> Element
     *      $('fruits').innerHTML;
     *      // -> 'kiwi, banana and apple'
     *
     *  Clearing the element's content:
     *
     *      $('fruits').update();
     *      // -> Element
     *      $('fruits').innerHTML;
     *      // -> '' (an empty string)
     *
     *  And now inserting an HTML snippet:
     *
     *      $('fruits').update('<p>Kiwi, banana <em>and</em> apple.</p>');
     *      // -> Element
     *      $('fruits').innerHTML;
     *      // -> '<p>Kiwi, banana <em>and</em> apple.</p>'
     *
     *  ... with a `<script>` tag thrown in:
     *
     *      $('fruits').update('<p>Kiwi, banana <em>and</em> apple.</p><script>alert("updated!")</script>');
     *      // -> Element (and prints "updated!" in an alert dialog).
     *      $('fruits').innerHTML;
     *      // -> '<p>Kiwi, banana <em>and</em> apple.</p>'
     *
     *  Relying on the `toString()` method:
     *
     *      $('fruits').update(123);
     *      // -> Element
     *      $('fruits').innerHTML;
     *      // -> '123'
     *
     *  Finally, you can do some pretty funky stuff by defining your own
     *  `toString()` method on your custom objects:
     *
     *      var Fruit = Class.create({
     *        initialize: function(fruit){
     *          this.fruit = fruit;
     *        },
     *        toString: function(){
     *          return 'I am a fruit and my name is "' + this.fruit + '".';
     *        }
     *      });
     *      var apple = new Fruit('apple');
     *
     *      $('fruits').update(apple);
     *      $('fruits').innerHTML;
     *      // -> 'I am a fruit and my name is "apple".'
     **/
    update: (function() {

        // see: http://support.microsoft.com/kb/276228
        var SELECT_ELEMENT_INNERHTML_BUGGY = (function() {
            var el = document.createElement("select"),
                isBuggy = true;
            el.innerHTML = "<option value=\"test\">test</option>";
            if (el.options && el.options[0]) {
                isBuggy = el.options[0].nodeName.toUpperCase() !== "OPTION";
            }
            el = null;
            return isBuggy;
        })();

        // see: http://msdn.microsoft.com/en-us/library/ms533897(VS.85).aspx
        var TABLE_ELEMENT_INNERHTML_BUGGY = (function() {
            try {
                var el = document.createElement("table");
                if (el && el.tBodies) {
                    el.innerHTML = "<tbody><tr><td>test</td></tr></tbody>";
                    var isBuggy = typeof el.tBodies[0] == "undefined";
                    el = null;
                    return isBuggy;
                }
            } catch (e) {
                return true;
            }
        })();

        var LINK_ELEMENT_INNERHTML_BUGGY = (function() {
            try {
                var el = document.createElement('div');
                el.innerHTML = "<link>";
                var isBuggy = (el.childNodes.length === 0);
                el = null;
                return isBuggy;
            } catch(e) {
                return true;
            }
        })();

        var ANY_INNERHTML_BUGGY = SELECT_ELEMENT_INNERHTML_BUGGY ||
            TABLE_ELEMENT_INNERHTML_BUGGY || LINK_ELEMENT_INNERHTML_BUGGY;

        var SCRIPT_ELEMENT_REJECTS_TEXTNODE_APPENDING = (function () {
            var s = document.createElement("script"),
                isBuggy = false;
            try {
                s.appendChild(document.createTextNode(""));
                isBuggy = !s.firstChild ||
                    s.firstChild && s.firstChild.nodeType !== 3;
            } catch (e) {
                isBuggy = true;
            }
            s = null;
            return isBuggy;
        })();


        function update(element, content) {
            element = $(element);
            var purgeElement = Element._purgeElement;

            // Purge the element's existing contents of all storage keys and
            // event listeners, since said content will be replaced no matter
            // what.
            var descendants = element.getElementsByTagName('*'),
                i = descendants.length;
            while (i--) purgeElement(descendants[i]);

            if (content && content.toElement)
                content = content.toElement();

            if (Object.isElement(content))
                return element.update().insert(content);

            content = Object.toHTML(content);

            var tagName = element.tagName.toUpperCase();

            if (tagName === 'SCRIPT' && SCRIPT_ELEMENT_REJECTS_TEXTNODE_APPENDING) {
                // scripts are not evaluated when updating SCRIPT element
                element.text = content;
                return element;
            }

            if (ANY_INNERHTML_BUGGY) {
                if (tagName in Element._insertionTranslations.tags) {
                    while (element.firstChild) {
                        element.removeChild(element.firstChild);
                    }
                    Element._getContentFromAnonymousElement(tagName, content.stripScripts())
                        .each(function(node) {
                            element.appendChild(node)
                        });
                } else if (LINK_ELEMENT_INNERHTML_BUGGY && Object.isString(content) && content.indexOf('<link') > -1) {
                    // IE barfs when inserting a string that beings with a LINK
                    // element. The workaround is to add any content to the beginning
                    // of the string; we'll be inserting a text node (see
                    // Element._getContentFromAnonymousElement below).
                    while (element.firstChild) {
                        element.removeChild(element.firstChild);
                    }
                    var nodes = Element._getContentFromAnonymousElement(tagName, content.stripScripts(), true);
                    nodes.each(function(node) {
                        element.appendChild(node)
                    });
                }
                else {
                    element.innerHTML = content.stripScripts();
                }
            }
            else {
                element.innerHTML = content.stripScripts();
            }

            content.evalScripts.bind(content).defer();
            return element;
        }

        return update;
    })(),

    /**
     *  Element.replace(@element[, newContent]) -> Element
     *
     *  Replaces `element` _itself_ with `newContent` and returns `element`.
     *
     *  Keep in mind that this method returns the element that has just been
     *  removed &mdash; not the element that took its place.
     *
     *  `newContent` can be either plain text, an HTML snippet or any JavaScript
     *  object which has a `toString()` method.
     *
     *  If `newContent` contains any `<script>` tags, these will be evaluated
     *  after `element` has been replaced ([[Element.replace]] internally calls
     *  [[String#evalScripts]]).
     *
     *  Note that if no argument is provided, [[Element.replace]] will simply
     *  clear `element` of its content. However, using [[Element.remove]] to do so
     *  is both faster and more standard compliant.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="food">
     *        <div id="fruits">
     *          <p id="first">Kiwi, banana <em>and</em> apple.</p>
     *        </div>
     *      </div>
     *
     *  Passing an HTML snippet:
     *
     *      $('first').replace('<ul id="favorite"><li>kiwi</li><li>banana</li><li>apple</li></ul>');
     *      // -> Element (p#first)
     *
     *      $('fruits').innerHTML;
     *      // -> '<ul id="favorite"><li>kiwi</li><li>banana</li><li>apple</li></ul>'
     *
     *  Again, with a `<script>` tag thrown in:
     *
     *      $('favorite').replace('<p id="still-first">Melon, oranges <em>and</em> grapes.</p><script>alert("removed!")</script>');
     *      // -> Element (ul#favorite) and prints "removed!" in an alert dialog.
     *
     *      $('fruits').innerHTML;
     *      // -> '<p id="still-first">Melon, oranges <em>and</em> grapes.</p>'
     *
     *  With plain text:
     *
     *      $('still-first').replace('Melon, oranges and grapes.');
     *      // -> Element (p#still-first)
     *
     *      $('fruits').innerHTML;
     *      // -> 'Melon, oranges and grapes.'
     *
     *  Finally, relying on the `toString()` method:
     *
     *      $('fruits').replace(123);
     *      // -> Element
     *
     *      $('food').innerHTML;
     *      // -> '123'
     *
     *  ##### Warning
     *
     *  Using [[Element.replace]] as an instance method (e.g.,
     *  `$('foo').replace('<p>Bar</p>')`) causes errors in Opera 9 when used on
     *  `input` elements. The `replace` property is reserved on `input` elements
     *  as part of [Web Forms 2](http://www.whatwg.org/specs/web-forms/current-work/).
     *  As a workaround, use the generic version instead
     *  (`Element.replace('foo', '<p>Bar</p>')`).
     *
     **/
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

    /**
     *  Element.insert(@element, content) -> Element
     *  - content (String | Element | Object): The content to insert.
     *
     *  Inserts content `above`, `below`, at the `top`, and/or at the `bottom` of
     *  the given element, depending on the option(s) given.
     *
     *  `insert` accepts content in any of these forms:
     *  - [[String]]: A string of HTML to be parsed and rendered
     *  - [[Element]]: An Element instance to insert
     *  - ...any object with a `toElement` method: The method is called and the resulting element used
     *  - ...any object with a `toHTML` method: The method is called and the resulting HTML string
     *    is parsed and rendered
     *
     *  The `content` argument can be the content to insert, in which case the
     *  implied insertion point is `bottom`, or an object that specifies one or
     *  more insertion points (e.g., `{ bottom: "foo", top: "bar" }`).
     *
     *  Accepted insertion points are:
     *  - `before` (as `element`'s previous sibling)
     *  - `after` (as `element's` next sibling)
     *  - `top` (as `element`'s first child)
     *  - `bottom` (as `element`'s last child)
     *
     *  Note that if the inserted HTML contains any `<script>` tag, these will be
     *  automatically evaluated after the insertion (`insert` internally calls
     *  [[String.evalScripts]] when inserting HTML).
     *
     *  <h5>Examples</h5>
     *
     *  Insert the given HTML at the bottom of the element (using the default):
     *
     *      $('myelement').insert("<p>HTML to append</p>");
     *
     *      $('myelement').insert({
     *        top: new Element('img', {src: 'logo.png'})
     *      });
     *
     *  Put `hr`s `before` and `after` the element:
     *
     *      $('myelement').insert({
     *        before: "<hr>",
     *        after: "<hr>"
     *      });
     **/
    insert: function(element, insertions) {
        element = $(element);

        if (Object.isString(insertions) || Object.isNumber(insertions) ||
            Object.isElement(insertions) || (insertions && (insertions.toElement || insertions.toHTML)))
            insertions = {bottom:insertions};

        var content, insert, tagName, childNodes;

        for (var position in insertions) {
            content = insertions[position];
            position = position.toLowerCase();
            insert = Element._insertionTranslations[position];

            if (content && content.toElement) content = content.toElement();
            if (Object.isElement(content)) {
                insert(element, content);
                continue;
            }

            content = Object.toHTML(content);

            tagName = ((position == 'before' || position == 'after')
                ? element.parentNode : element).tagName.toUpperCase();

            childNodes = Element._getContentFromAnonymousElement(tagName, content.stripScripts());

            if (position == 'top' || position == 'after') childNodes.reverse();
            childNodes.each(insert.curry(element));

            content.evalScripts.bind(content).defer();
        }

        return element;
    },

    /**
     *  Element.wrap(@element, wrapper[, attributes]) -> Element
     *  - wrapper (Element | String): An element to wrap `element` inside, or
     *    else a string representing the tag name of an element to be created.
     *  - attributes (Object): A set of attributes to apply to the wrapper
     *    element. Refer to the [[Element]] constructor for usage.
     *
     *  Wraps an element inside another, then returns the wrapper.
     *
     *  If the given element exists on the page, [[Element.wrap]] will wrap it in
     *  place — its position will remain the same.
     *
     *  The `wrapper` argument can be _either_ an existing [[Element]] _or_ a
     *  string representing the tag name of an element to be created. The optional
     *  `attributes` argument can contain a list of attribute/value pairs that
     *  will be set on the wrapper using [[Element.writeAttribute]].
     *
     *  ##### Examples
     *
     *  Original HTML:
     *
     *      language: html
     *      <table id="data">
     *        <tr>
     *          <th>Foo</th>
     *          <th>Bar</th>
     *        </tr>
     *        <tr>
     *          <td>1</td>
     *          <td>2</td>
     *        </tr>
     *      </table>
     *
     *  JavaScript:
     *
     *      // approach 1:
     *      var div = new Element('div', { 'class': 'table-wrapper' });
     *      $('data').wrap(div);
     *
     *      // approach 2:
     *      $('data').wrap('div', { 'class': 'table-wrapper' });
     *
     *      // Both examples are equivalent &mdash; they return the DIV.
     *
     *  Resulting HTML:
     *
     *      language: html
     *      <div class="table-wrapper">
     *        <table id="data">
     *          <tr>
     *            <th>Foo</th>
     *            <th>Bar</th>
     *          </tr>
     *          <tr>
     *            <td>1</td>
     *            <td>2</td>
     *          </tr>
     *        </table>
     *      </div>
     *
     *  ##### Warning
     *
     *  Using [[Element.wrap]] as an instance method (e.g., `$('foo').wrap('p')`)
     *  causes errors in Internet Explorer when used on `textarea` elements. The
     *  `wrap` property is reserved on `textarea`'s as a proprietary extension to
     *  HTML. As a workaround, use the generic version instead
     *  (`Element.wrap('foo', 'p')`).
     **/
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

    /**
     *  Element.inspect(@element) -> String
     *
     *  Returns the debug-oriented string representation of `element`.
     *
     *  For more information on `inspect` methods, see [[Object.inspect]].
     *
     *      language: html
     *      <ul>
     *        <li id="golden-delicious">Golden Delicious</li>
     *        <li id="mutsu" class="yummy apple">Mutsu</li>
     *        <li id="mcintosh" class="yummy">McIntosh</li>
     *        <li></li>
     *      </ul>
     *
     *  And the associated JavaScript:
     *
     *      $('golden-delicious').inspect();
     *      // -> '<li id="golden-delicious">'
     *
     *      $('mutsu').inspect();
     *      // -> '<li id="mutsu" class="yummy apple">'
     *
     *      $('mutsu').next().inspect();
     *      // -> '<li>'
     **/
    inspect: function(element) {
        element = $(element);
        var result = '<' + element.tagName.toLowerCase();
        $H({'id': 'id', 'className': 'class'}).each(function(pair) {
            var property = pair.first(),
                attribute = pair.last(),
                value = (element[property] || '').toString();
            if (value) result += ' ' + attribute + '=' + value.inspect(true);
        });
        return result + '>';
    },

    /**
     *  Element.recursivelyCollect(@element, property) -> [Element...]
     *
     *  Recursively collects elements whose relationship to `element` is
     *  specified by `property`. `property` has to be a _property_ (a method
     *  won't do!) of `element` that points to a single DOM node (e.g.,
     *  `nextSibling` or `parentNode`).
     *
     *  ##### More information
     *
     *  This method is used internally by [[Element.ancestors]],
     *  [[Element.descendants]], [[Element.nextSiblings]],
     *  [[Element.previousSiblings]] and [[Element.siblings]] which offer really
     *  convenient way to grab elements, so directly accessing
     *  [[Element.recursivelyCollect]] should seldom be needed. However, if you
     *  are after something out of the ordinary, it is the way to go.
     *
     *  Note that all of Prototype's DOM traversal methods ignore text nodes and
     *  return element nodes only.
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul id="fruits">
     *        <li id="apples">
     *          <ul id="list-of-apples">
     *            <li id="golden-delicious"><p>Golden Delicious</p></li>
     *            <li id="mutsu">Mutsu</li>
     *            <li id="mcintosh">McIntosh</li>
     *            <li id="ida-red">Ida Red</li>
     *          </ul>
     *        </li>
     *      </ul>
     *
     *  And the associated JavaScript:
     *
     *      $('fruits').recursivelyCollect('firstChild');
     *      // -> [li#apples, ul#list-of-apples, li#golden-delicious, p]
     **/
    recursivelyCollect: function(element, property, maximumLength) {
        element = $(element);
        maximumLength = maximumLength || -1;
        var elements = [];

        while (element = element[property]) {
            if (element.nodeType == 1)
                elements.push(Element.extend(element));
            if (elements.length == maximumLength)
                break;
        }

        return elements;
    },

    /**
     *  Element.ancestors(@element) -> [Element...]
     *
     *  Collects all of `element`'s ancestor elements and returns them as an
     *  array of extended elements.
     *
     *  The returned array's first element is `element`'s direct ancestor (its
     *  `parentNode`), the second one is its grandparent, and so on until the
     *  `<html>` element is reached. `<html>` will always be the last member of
     *  the array. Calling `ancestors` on the `<html>` element will return an
     *  empty array.
     *
     *  ##### Example
     *
     *  Assuming:
     *
     *      language: html
     *      <html>
     *      [...]
     *        <body>
     *          <div id="father">
     *            <div id="kid">
     *            </div>
     *          </div>
     *        </body>
     *      </html>
     *
     *  Then:
     *
     *      $('kid').ancestors();
     *      // -> [div#father, body, html]
     **/
    ancestors: function(element) {
        return Element.recursivelyCollect(element, 'parentNode');
    },

    /**
     *  Element.descendants(@element) -> [Element...]
     *
     *  Collects all of the element's descendants (its children, their children,
     *  etc.) and returns them as an array of extended elements. As with all of
     *  Prototype's DOM traversal methods, only [[Element]]s are returned, other
     *  nodes (text nodes, etc.) are skipped.
     **/
    descendants: function(element) {
        return Element.select(element, "*");
    },

    /**
     *  Element.firstDescendant(@element) -> Element
     *
     *  Returns the first child that is an element.
     *
     *  This is opposed to the `firstChild` DOM property, which will return
     *  any node, including text nodes and comment nodes.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="australopithecus">
     *        <div id="homo-erectus"><!-- Latin is super -->
     *          <div id="homo-neanderthalensis"></div>
     *          <div id="homo-sapiens"></div>
     *        </div>
     *      </div>
     *
     *  Then:
     *
     *      $('australopithecus').firstDescendant();
     *      // -> div#homo-herectus
     *
     *      // the DOM property returns any first node
     *      $('homo-herectus').firstChild;
     *      // -> comment node "Latin is super"
     *
     *      // this is what we want!
     *      $('homo-herectus').firstDescendant();
     *      // -> div#homo-neanderthalensis
     **/
    firstDescendant: function(element) {
        element = $(element).firstChild;
        while (element && element.nodeType != 1) element = element.nextSibling;
        return $(element);
    },

    /** deprecated, alias of: Element.childElements
     *  Element.immediateDescendants(@element) -> [Element...]
     *
     *  **This method is deprecated, please see [[Element.childElements]]**.
     **/
    immediateDescendants: function(element) {
        var results = [], child = $(element).firstChild;
        while (child) {
            if (child.nodeType === 1) {
                results.push(Element.extend(child));
            }
            child = child.nextSibling;
        }
        return results;
    },

    /**
     *  Element.previousSiblings(@element) -> [Element...]
     *
     *  Collects all of `element`'s previous siblings and returns them as an
     *  [[Array]] of elements.
     *
     *  Two elements are siblings if they have the same parent. So for example,
     *  the `<head>` and `<body>` elements are siblings (their parent is the
     *  `<html>` element). Previous-siblings are simply the ones which precede
     *  `element` in the document.
     *
     *  The returned [[Array]] reflects the siblings _inversed_ order in the
     *  document (e.g. an index of 0 refers to the lowest sibling i.e., the one
     *  closest to `element`).
     *
     *  Note that all of Prototype's DOM traversal methods ignore text nodes and
     *  return element nodes only.
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul>
     *        <li id="golden-delicious">Golden Delicious</li>
     *        <li id="mutsu">Mutsu</li>
     *        <li id="mcintosh">McIntosh</li>
     *        <li id="ida-red">Ida Red</li>
     *      </ul>
     *
     *  Then:
     *
     *      $('mcintosh').previousSiblings();
     *      // -> [li#mutsu, li#golden-delicious]
     *
     *      $('golden-delicious').previousSiblings();
     *      // -> []
     **/
    previousSiblings: function(element, maximumLength) {
        return Element.recursivelyCollect(element, 'previousSibling');
    },

    /**
     *  Element.nextSiblings(@element) -> [Element...]
     *
     *  Collects all of `element`'s next siblings and returns them as an [[Array]]
     *  of elements.
     *
     *  Two elements are siblings if they have the same parent. So for example,
     *  the `head` and `body` elements are siblings (their parent is the `html`
     *  element). Next-siblings are simply the ones which follow `element` in the
     *  document.
     *
     *  The returned [[Array]] reflects the siblings order in the document
     *  (e.g. an index of 0 refers to the sibling right below `element`).
     *
     *  Note that all of Prototype's DOM traversal methods ignore text nodes and
     *  return element nodes only.
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul>
     *        <li id="golden-delicious">Golden Delicious</li>
     *        <li id="mutsu">Mutsu</li>
     *        <li id="mcintosh">McIntosh</li>
     *        <li id="ida-red">Ida Red</li>
     *      </ul>
     *
     *  Then:
     *
     *      $('mutsu').nextSiblings();
     *      // -> [li#mcintosh, li#ida-red]
     *
     *      $('ida-red').nextSiblings();
     *      // -> []
     **/
    nextSiblings: function(element) {
        return Element.recursivelyCollect(element, 'nextSibling');
    },

    /**
     *  Element.siblings(@element) -> [Element...]
     *  Collects all of element's siblings and returns them as an [[Array]] of
     *  elements.
     *
     *  Two elements are siblings if they have the same parent. So for example,
     *  the `head` and `body` elements are siblings (their parent is the `html`
     *  element).
     *
     *  The returned [[Array]] reflects the siblings' order in the document (e.g.
     *  an index of 0 refers to `element`'s topmost sibling).
     *
     *  Note that all of Prototype's DOM traversal methods ignore text nodes and
     *  return element nodes only.
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul>
     *        <li id="golden-delicious">Golden Delicious</li>
     *        <li id="mutsu">Mutsu</li>
     *        <li id="mcintosh">McIntosh</li>
     *        <li id="ida-red">Ida Red</li>
     *      </ul>
     *
     *  Then:
     *
     *      $('mutsu').siblings();
     *      // -> [li#golden-delicious, li#mcintosh, li#ida-red]
     **/
    siblings: function(element) {
        element = $(element);
        return Element.previousSiblings(element).reverse()
            .concat(Element.nextSiblings(element));
    },

    /**
     *  Element.match(@element, selector) -> boolean
     *  - selector (String): A CSS selector.
     *
     *  Checks if `element` matches the given CSS selector.
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul id="fruits">
     *        <li id="apples">
     *          <ul>
     *            <li id="golden-delicious">Golden Delicious</li>
     *            <li id="mutsu" class="yummy">Mutsu</li>
     *            <li id="mcintosh" class="yummy">McIntosh</li>
     *            <li id="ida-red">Ida Red</li>
     *          </ul>
     *        </li>
     *      </ul>
     *
     *  Then:
     *
     *      $('fruits').match('ul');
     *      // -> true
     *
     *      $('mcintosh').match('li#mcintosh.yummy');
     *      // -> true
     *
     *      $('fruits').match('p');
     *      // -> false
     **/
    match: function(element, selector) {
        element = $(element);
        if (Object.isString(selector))
            return Prototype.Selector.match(element, selector);
        return selector.match(element);
    },

    /**
     *  Element.up(@element[, expression[, index = 0]]) -> Element
     *  Element.up(@element[, index = 0]) -> Element
     *  - expression (String): A CSS selector.
     *
     *  Returns `element`'s first ancestor (or the Nth ancestor, if `index`
     *  is specified) that matches `expression`. If no `expression` is
     *  provided, all ancestors are considered. If no ancestor matches these
     *  criteria, `undefined` is returned.
     *
     *  ##### More information
     *
     *  The [[Element.up]] method is part of Prototype's ultimate DOM traversal
     *  toolkit (check out [[Element.down]], [[Element.next]] and
     *  [[Element.previous]] for some more Prototypish niceness). It allows
     *  precise index-based and/or CSS rule-based selection of any of `element`'s
     *  **ancestors**.
     *
     *  As it totally ignores text nodes (it only returns elements), you don't
     *  have to worry about whitespace nodes.
     *
     *  And as an added bonus, all elements returned are already extended
     *  (see [[Element.extended]]) allowing chaining:
     *
     *      $(element).up(1).next('li', 2).hide();
     *
     *  Walking the DOM has never been that easy!
     *
     *  ##### Arguments
     *
     *  If no arguments are passed, `element`'s first ancestor is returned (this
     *  is similar to calling `parentNode` except [[Element.up]] returns an already
     *  extended element.
     *
     *  If `index` is defined, `element`'s corresponding ancestor is returned.
     *  (This is equivalent to selecting an element from the array of elements
     *  returned by the method [[Element.ancestors]]). Note that the first element
     *  has an index of 0.
     *
     *  If `expression` is defined, [[Element.up]] will return the first ancestor
     *  that matches it.
     *
     *  If both `expression` and `index` are defined, [[Element.up]] will collect
     *  all the ancestors matching the given CSS expression and will return the
     *  one at the specified index.
     *
     *  **In all of the above cases, if no descendant is found,** `undefined`
     *  **will be returned.**
     *
     *  ### Examples
     *
     *      language: html
     *      <html>
     *        [...]
     *        <body>
     *          <ul id="fruits">
     *            <li id="apples" class="keeps-the-doctor-away">
     *              <ul>
     *                <li id="golden-delicious">Golden Delicious</li>
     *                <li id="mutsu" class="yummy">Mutsu</li>
     *                <li id="mcintosh" class="yummy">McIntosh</li>
     *                <li id="ida-red">Ida Red</li>
     *              </ul>
     *            </li>
     *          </ul>
     *        </body>
     *      </html>
     *
     *  Get the first ancestor of "#fruites":
     *
     *      $('fruits').up();
     *      // or:
     *      $('fruits').up(0);
     *      // -> body
     *
     *  Get the third ancestor of "#mutsu":
     *
     *      $('mutsu').up(2);
     *      // -> ul#fruits
     *
     *  Get the first ancestor of "#mutsu" with the node name "li":
     *
     *      $('mutsu').up('li');
     *      // -> li#apples
     *
     *  Get the first ancestor of "#mutsu" with the class name
     *  "keeps-the-doctor-away":
     *
     *      $('mutsu').up('.keeps-the-doctor-away');
     *      // -> li#apples
     *
     *  Get the second ancestor of "#mutsu" with the node name "ul":
     *
     *      $('mutsu').up('ul', 1);
     *      // -> ul#fruits
     *
     *  Try to get the first ancestor of "#mutsu" with the node name "div":
     *
     *      $('mutsu').up('div');
     *      // -> undefined
     **/
    up: function(element, expression, index) {
        element = $(element);
        if (arguments.length == 1) return $(element.parentNode);
        var ancestors = Element.ancestors(element);
        return Object.isNumber(expression) ? ancestors[expression] :
            Prototype.Selector.find(ancestors, expression, index);
    },

    /**
     *  Element.down(@element[, expression[, index = 0]]) -> Element
     *  Element.down(@element[, index = 0]) -> Element
     *  - expression (String): A CSS selector.
     *
     *  Returns `element`'s first descendant (or the Nth descendant, if `index`
     *  is specified) that matches `expression`. If no `expression` is
     *  provided, all descendants are considered. If no descendant matches these
     *  criteria, `undefined` is returned.
     *
     *  ##### More information
     *
     *  The [[Element.down]] method is part of Prototype's ultimate DOM traversal
     *  toolkit (check out [[Element.up]], [[Element.next]] and
     *  [[Element.previous]] for some more Prototypish niceness). It allows
     *  precise index-based and/or CSS rule-based selection of any of the
     *  element's **descendants**.
     *
     *  As it totally ignores text nodes (it only returns elements), you don't
     *  have to worry about whitespace nodes.
     *
     *  And as an added bonus, all elements returned are already extended
     *  (see [[Element.extend]]) allowing chaining:
     *
     *      $(element).down(1).next('li', 2).hide();
     *
     *  Walking the DOM has never been that easy!
     *
     *  ##### Arguments
     *
     *  If no arguments are passed, `element`'s first descendant is returned (this
     *  is similar to calling `firstChild` except [[Element.down]] returns an
     *  extended element.
     *
     *  If `index` is defined, `element`'s corresponding descendant is returned.
     *  (This is equivalent to selecting an element from the array of elements
     *  returned by the method [[Element.descendants]].) Note that the first
     *  element has an index of 0.
     *
     *  If `expression` is defined, [[Element.down]] will return the first
     *  descendant that matches it. This is a great way to grab the first item in
     *  a list for example (just pass in 'li' as the method's first argument).
     *
     *  If both `expression` and `index` are defined, [[Element.down]] will collect
     *  all the descendants matching the given CSS expression and will return the
     *  one at the specified index.
     *
     *  **In all of the above cases, if no descendant is found,** `undefined`
     *  **will be returned.**
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul id="fruits">
     *        <li id="apples">
     *          <ul>
     *            <li id="golden-delicious">Golden Delicious</li>
     *            <li id="mutsu" class="yummy">Mutsu</li>
     *            <li id="mcintosh" class="yummy">McIntosh</li>
     *            <li id="ida-red">Ida Red</li>
     *          </ul>
     *        </li>
     *      </ul>
     *
     *  Get the first descendant of "#fruites":
     *
     *      $('fruits').down();
     *      // or:
     *      $('fruits').down(0);
     *      // -> li#apples
     *
     *  Get the third descendant of "#fruits":
     *
     *      $('fruits').down(3);
     *      // -> li#golden-delicious
     *
     *  Get the first descendant of "#apples" with the node name "li":
     *
     *      $('apples').down('li');
     *      // -> li#golden-delicious
     *
     *  Get the first descendant of "#apples" with the node name "li" and the
     *  class name "yummy":
     *
     *      $('apples').down('li.yummy');
     *      // -> li#mutsu
     *
     *  Get the second descendant of "#fruits" with the class name "yummy":
     *
     *      $('fruits').down('.yummy', 1);
     *      // -> li#mcintosh
     *
     *  Try to get the ninety-ninth descendant of "#fruits":
     *
     *      $('fruits').down(99);
     *      // -> undefined
     **/
    down: function(element, expression, index) {
        element = $(element);
        if (arguments.length == 1) return Element.firstDescendant(element);
        return Object.isNumber(expression) ? Element.descendants(element)[expression] :
            Element.select(element, expression)[index || 0];
    },

    /**
     *  Element.previous(@element[, expression[, index = 0]]) -> Element
     *  Element.previous(@element[, index = 0]) -> Element
     *  - expression (String): A CSS selector.
     *
     *  Returns `element`'s first previous sibling (or the Nth, if `index`
     *  is specified) that matches `expression`. If no `expression` is
     *  provided, all previous siblings are considered. If none matches these
     *  criteria, `undefined` is returned.
     *
     *  ##### More information
     *
     *  The [[Element.previous]] method is part of Prototype's ultimate DOM
     *  traversal toolkit (check out [[Element.up]], [[Element.down]] and
     *  [[Element.next]] for some more Prototypish niceness). It allows precise
     *  index-based and/or CSS expression-based selection of any of `element`'s
     *  **previous siblings**. (Note that two elements are considered siblings if
     *  they have the same parent, so for example, the `head` and `body` elements
     *  are siblings&#8212;their parent is the `html` element.)
     *
     *  As it totally ignores text nodes (it only returns elements), you don't
     *  have to worry about whitespace nodes.
     *
     *  And as an added bonus, all elements returned are already extended (see
     *  [[Element.extend]]) allowing chaining:
     *
     *      $(element).down('p').previous('ul', 2).hide();
     *
     *  Walking the DOM has never been that easy!
     *
     *  ##### Arguments
     *
     *  If no arguments are passed, `element`'s previous sibling is returned
     *  (this is similar as calling `previousSibling` except [[Element.previous]]
     *  returns an already extended element).
     *
     *  If `index` is defined, `element`'s corresponding previous sibling is
     *  returned. (This is equivalent to selecting an element from the array of
     *  elements returned by the method [[Element.previousSiblings]]). Note that
     *  the sibling _right above_ `element` has an index of 0.
     *
     *  If `expression` is defined, [[Element.previous]] will return the `element`
     *  first previous sibling that matches it.
     *
     *  If both `expression` and `index` are defined, [[Element.previous]] will
     *  collect all of `element`'s previous siblings matching the given CSS
     *  expression and will return the one at the specified index.
     *
     *  **In all of the above cases, if no previous sibling is found,**
     *  `undefined` **will be returned.**
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul id="fruits">
     *        <li id="apples">
     *          <h3>Apples</h3>
     *          <ul id="list-of-apples">
     *            <li id="golden-delicious" class="yummy">Golden Delicious</li>
     *            <li id="mutsu" class="yummy">Mutsu</li>
     *            <li id="mcintosh">McIntosh</li>
     *            <li id="ida-red">Ida Red</li>
     *          </ul>
     *          <p id="saying">An apple a day keeps the doctor away.</p>
     *        </li>
     *      </ul>
     *
     *  Get the first previous sibling of "#saying":
     *
     *      $('saying').previous();
     *      // or:
     *      $('saying').previous(0);
     *      // -> ul#list-of-apples
     *
     *  Get the second previous sibling of "#saying":
     *
     *      $('saying').previous(1);
     *      // -> h3
     *
     *  Get the first previous sibling of "#saying" with node name "h3":
     *
     *      $('saying').previous('h3');
     *      // -> h3
     *
     *  Get the first previous sibling of "#ida-red" with class name "yummy":
     *
     *      $('ida-red').previous('.yummy');
     *      // -> li#mutsu
     *
     *  Get the second previous sibling of "#ida-red" with class name "yummy":
     *
     *      $('ida-red').previous('.yummy', 1);
     *      // -> li#golden-delicious
     *
     *  Try to get the sixth previous sibling of "#ida-red":
     *
     *      $('ida-red').previous(5);
     *      // -> undefined
     **/
    previous: function(element, expression, index) {
        element = $(element);
        if (Object.isNumber(expression)) index = expression,expression = false;
        if (!Object.isNumber(index)) index = 0;

        if (expression) {
            return Prototype.Selector.find(element.previousSiblings(), expression, index);
        } else {
            return element.recursivelyCollect("previousSibling", index + 1)[index];
        }
    },

    /**
     *  Element.next(@element[, expression[, index = 0]]) -> Element
     *  Element.next(@element[, index = 0]) -> Element
     *  - expression (String): A CSS selector.
     *
     *  Returns `element`'s first following sibling (or the Nth, if `index`
     *  is specified) that matches `expression`. If no `expression` is
     *  provided, all following siblings are considered. If none matches these
     *  criteria, `undefined` is returned.
     *
     *  ##### More information
     *
     *  The [[Element.next]] method is part of Prototype's ultimate DOM traversal
     *  toolkit (check out [[Element.up]], [[Element.down]] and
     *  [[Element.previous]] for some more Prototypish niceness). It allows
     *  precise index-based and/or CSS expression-based selection of any of
     *  `element`'s **following siblings**. (Note that two elements are considered
     *  siblings if they have the same parent, so for example, the `head` and
     *  `body` elements are siblings&#8212;their parent is the `html` element.)
     *
     *  As it totally ignores text nodes (it only returns elements), you don't
     *  have to worry about whitespace nodes.
     *
     *  And as an added bonus, all elements returned are already extended (see
     *  [[Element.extend]]) allowing chaining:
     *
     *      $(element).down(1).next('li', 2).hide();
     *
     *  Walking the DOM has never been that easy!
     *
     *  ##### Arguments
     *
     *  If no arguments are passed, `element`'s following sibling is returned
     *  (this is similar as calling `nextSibling` except [[Element.next]] returns an
     *  already extended element).
     *
     *  If `index` is defined, `element`'s corresponding following sibling is
     *  returned. (This is equivalent to selecting an element from the array of
     *  elements returned by the method [[Element.nextSiblings]]). Note that the
     *  sibling _right below_ `element` has an index of 0.
     *
     *  If `expression` is defined, [[Element.next]] will return the `element` first
     *  following sibling that matches it.
     *
     *  If both `expression` and `index` are defined, [[Element.next]] will collect
     *  all of `element`'s following siblings matching the given CSS expression
     *  and will return the one at the specified index.
     *
     *  **In all of the above cases, if no following sibling is found,**
     *  `undefined` **will be returned.**
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul id="fruits">
     *        <li id="apples">
     *          <h3 id="title">Apples</h3>
     *          <ul id="list-of-apples">
     *            <li id="golden-delicious">Golden Delicious</li>
     *            <li id="mutsu">Mutsu</li>
     *            <li id="mcintosh" class="yummy">McIntosh</li>
     *            <li id="ida-red" class="yummy">Ida Red</li>
     *          </ul>
     *          <p id="saying">An apple a day keeps the doctor away.</p>
     *        </li>
     *      </ul>
     *
     *  Get the first sibling after "#title":
     *
     *      $('title').next();
     *      // or:
     *      $('title').next(0);
     *      // -> ul#list-of-apples
     *
     *  Get the second sibling after "#title":
     *
     *      $('title').next(1);
     *      // -> p#saying
     *
     *  Get the first sibling after "#title" with node name "p":
     *
     *      $('title').next('p');
     *      // -> p#sayings
     *
     *  Get the first sibling after "#golden-delicious" with class name "yummy":
     *
     *      $('golden-delicious').next('.yummy');
     *      // -> li#mcintosh
     *
     *  Get the second sibling after "#golden-delicious" with class name "yummy":
     *
     *      $('golden-delicious').next('.yummy', 1);
     *      // -> li#ida-red
     *
     *  Try to get the first sibling after "#ida-red":
     *
     *      $('ida-red').next();
     *      // -> undefined
     **/
    next: function(element, expression, index) {
        element = $(element);
        if (Object.isNumber(expression)) index = expression,expression = false;
        if (!Object.isNumber(index)) index = 0;

        if (expression) {
            return Prototype.Selector.find(element.nextSiblings(), expression, index);
        } else {
            var maximumLength = Object.isNumber(index) ? index + 1 : 1;
            return element.recursivelyCollect("nextSibling", index + 1)[index];
        }
    },


    /**
     *  Element.select(@element, expression...) -> [Element...]
     *  - expression (String): A CSS selector.
     *
     *  Takes an arbitrary number of CSS selectors and returns an array of
     *  descendants of `element` that match any of them.
     *
     *  This method is very similar to [[$$]] but can be used within the context
     *  of one element, rather than the whole document. The supported CSS syntax
     *  is identical, so please refer to the [[$$]] docs for details.
     *
     *  ##### Examples
     *
     *      language: html
     *      <ul id="fruits">
     *        <li id="apples">
     *          <h3 title="yummy!">Apples</h3>
     *          <ul id="list-of-apples">
     *            <li id="golden-delicious" title="yummy!" >Golden Delicious</li>
     *            <li id="mutsu" title="yummy!">Mutsu</li>
     *            <li id="mcintosh">McIntosh</li>
     *            <li id="ida-red">Ida Red</li>
     *          </ul>
     *          <p id="saying">An apple a day keeps the doctor away.</p>
     *        </li>
     *      </ul>
     *
     *  Then:
     *
     *      $('apples').select('[title="yummy!"]');
     *      // -> [h3, li#golden-delicious, li#mutsu]
     *
     *      $('apples').select( 'p#saying', 'li[title="yummy!"]');
     *      // -> [li#golden-delicious, li#mutsu,  p#saying]
     *
     *      $('apples').select('[title="disgusting!"]');
     *      // -> []
     *
     *  ##### Tip
     *
     *  [[Element.select]] can be used as a pleasant alternative to the native
     *  method `getElementsByTagName`:
     *
     *      var nodes  = $A(someUL.getElementsByTagName('li')).map(Element.extend);
     *      var nodes2 = someUL.select('li');
     *
     *  In the first example, you must explicitly convert the result set to an
     *  [[Array]] (so that Prototype's [[Enumerable]] methods can be used) and
     *  must manually call [[Element.extend]] on each node (so that custom
     *  instance methods can be used on the nodes). [[Element.select]] takes care
     *  of both concerns on its own.
     *
     *  If you're using 1.6 or above (and the performance optimizations therein),
     *  the speed difference between these two examples is negligible.
     **/
    select: function(element) {
        element = $(element);
        var expressions = Array.prototype.slice.call(arguments, 1).join(', ');
        return Prototype.Selector.select(expressions, element);
    },

    /**
     *  Element.adjacent(@element, selector...) -> [Element...]
     *  - selector (String): A CSS selector.
     *
     *  Finds all siblings of the current element that match the given
     *  selector(s). If you provide multiple selectors, siblings matching *any*
     *  of the selectors are included. If a sibling matches multiple selectors,
     *  it is only included once. The order of the returned array is not defined.
     *
     *  ##### Example
     *
     *  Assuming this list:
     *
     *      language: html
     *      <ul id="cities">
     *        <li class="us" id="nyc">New York</li>
     *        <li class="uk" id="lon">London</li>
     *        <li class="us" id="chi">Chicago</li>
     *        <li class="jp" id="tok">Tokyo</li>
     *        <li class="us" id="la">Los Angeles</li>
     *        <li class="us" id="aus">Austin</li>
     *      </ul>
     *
     *  Then:
     *
     *      $('nyc').adjacent('li.us');
     *      // -> [li#chi, li#la, li#aus]
     *      $('nyc').adjacent('li.uk', 'li.jp');
     *      // -> [li#lon, li#tok]
     **/
    adjacent: function(element) {
        element = $(element);
        var expressions = Array.prototype.slice.call(arguments, 1).join(', ');
        return Prototype.Selector.select(expressions, element.parentNode).without(element);
    },

    /**
     *  Element.identify(@element) -> String
     *
     *  Returns `element`'s ID. If `element` does not have an ID, one is
     *  generated, assigned to `element`, and returned.
     *
     *  ##### Examples
     *
     *  Original HTML:
     *
     *        <ul>
     *          <li id="apple">apple</li>
     *          <li>orange</li>
     *        </ul>
     *
     *  JavaScript:
     *
     *        $('apple').identify();
     *        // -> 'apple'
     *
     *        $('apple').next().identify();
     *        // -> 'anonymous_element_1'
     *
     *  Resulting HTML:
     *
     *        <ul>
     *          <li id="apple">apple</li>
     *          <li id="anonymous_element_1">orange</li>
     *        </ul>
     **/
    identify: function(element) {
        element = $(element);
        var id = Element.readAttribute(element, 'id');
        if (id) return id;
        do {
            id = 'anonymous_element_' + Element.idCounter++
        } while ($(id));
        Element.writeAttribute(element, 'id', id);
        return id;
    },

    /**
     *  Element.readAttribute(@element, attributeName) -> String | null
     *
     *  Returns the value of `element`'s `attribute` or `null` if `attribute` has
     *  not been specified.
     *
     *  This method serves two purposes. First it acts as a simple wrapper around
     *  `getAttribute` which isn't a "real" function in Safari and Internet
     *  Explorer (it doesn't have `.apply` or `.call` for instance). Secondly, it
     *  cleans up the horrible mess Internet Explorer makes when handling
     *  attributes.
     *
     *  ##### Examples
     *
     *      language: html
     *      <a id="tag" href="/tags/prototype" rel="tag" title="view related bookmarks." my_widget="some info.">Prototype</a>
     *
     *  Then:
     *
     *      $('tag').readAttribute('href');
     *      // -> '/tags/prototype'
     *
     *      $('tag').readAttribute('title');
     *      // -> 'view related bookmarks.'
     *
     *      $('tag').readAttribute('my_widget');
     *      // -> 'some info.'
     **/
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

    /**
     *  Element.writeAttribute(@element, attribute[, value = true]) -> Element
     *  Element.writeAttribute(@element, attributes) -> Element
     *
     *  Adds, specifies or removes attributes passed as either a hash or a
     *  name/value pair.
     **/
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

    /**
     *  Element.getHeight(@element) -> Number
     *
     *  Returns the height of `element`.
     *
     *  This method returns correct values on elements whose display is set to
     *  `none` either in an inline style rule or in an CSS stylesheet.
     *
     *  For performance reasons, if you need to query both width _and_ height of
     *  `element`, you should consider using [[Element.getDimensions]] instead.
     *
     *  Note that the value returned is a _number only_ although it is
     *  _expressed in pixels_.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="rectangle" style="font-size: 10px; width: 20em; height: 10em"></div>
     *
     *  Then:
     *
     *      $('rectangle').getHeight();
     *      // -> 100
     **/
    getHeight: function(element) {
        return Element.getDimensions(element).height;
    },

    /**
     *  Element.getWidth(@element) -> Number
     *
     *  Returns the width of `element`.
     *
     *  This method returns correct values on elements whose display is set to
     *  `none` either in an inline style rule or in an CSS stylesheet.
     *
     *  For performance reasons, if you need to query both width _and_ height of
     *  `element`, you should consider using [[Element.getDimensions]] instead.
     *
     *  Note that the value returned is a _number only_ although it is
     *  _expressed in pixels_.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="rectangle" style="font-size: 10px; width: 20em; height: 10em"></div>
     *
     *  Then:
     *
     *      $('rectangle').getWidth();
     *      // -> 200
     **/
    getWidth: function(element) {
        return Element.getDimensions(element).width;
    },

    /** deprecated
     *  Element.classNames(@element) -> [String...]
     *
     *  Returns a new instance of [[Element.ClassNames]], an [[Enumerable]]
     *  object used to read and write CSS class names of `element`.
     *
     *  **Deprecated**, please see [[Element.addClassName]],
     *  [[Element.removeClassName]], and [[Element.hasClassName]]. If you want
     *  an array of classnames, you can use `$w(element.className)`.
     **/
    classNames: function(element) {
        return new Element.ClassNames(element);
    },

    /**
     *  Element.hasClassName(@element, className) -> Boolean
     *
     *  Checks for the presence of CSS class `className` on `element`.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="mutsu" class="apple fruit food"></div>
     *
     *  Then:
     *
     *      $('mutsu').hasClassName('fruit');
     *      // -> true
     *
     *      $('mutsu').hasClassName('vegetable');
     *      // -> false
     **/
    hasClassName: function(element, className) {
        if (!(element = $(element))) return;
        var elementClassName = element.className;
        return (elementClassName.length > 0 && (elementClassName == className ||
            new RegExp("(^|\\s)" + className + "(\\s|$)").test(elementClassName)));
    },

    /**
     *  Element.addClassName(@element, className) -> Element
     *  - className (String): The class name to add.
     *
     *  Adds the given CSS class to `element`.
     *
     *  ##### Example
     *
     *  Assuming this HTML:
     *
     *      language: html
     *      <div id="mutsu" class="apple fruit"></div>
     *
     *  Then:
     *
     *      $('mutsu').className;
     *      // -> 'apple fruit'
     *      $('mutsu').addClassName('food');
     *      $('mutsu').className;
     *      // -> 'apple fruit food'
     **/
    addClassName: function(element, className) {
        if (!(element = $(element))) return;
        if (!Element.hasClassName(element, className))
            element.className += (element.className ? ' ' : '') + className;
        return element;
    },

    /**
     *  Element.removeClassName(@element, className) -> Element
     *
     *  Removes CSS class `className` from `element`.
     *
     *  ##### Examples
     *
     *  Assuming this HTML:
     *
     *      language: html
     *      <div id="mutsu" class="apple fruit food"></div>
     *
     *  Then:
     *
     *      $('mutsu').removeClassName('food');
     *      // -> Element
     *
     *      $('mutsu').classNames;
     *      // -> 'apple fruit'
     **/
    removeClassName: function(element, className) {
        if (!(element = $(element))) return;
        element.className = element.className.replace(
            new RegExp("(^|\\s+)" + className + "(\\s+|$)"), ' ').strip();
        return element;
    },

    /**
     *  Element.toggleClassName(@element, className) -> Element
     *
     *  Toggles the presence of CSS class `className` on `element`.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="mutsu" class="apple"></div>
     *
     *  Then:
     *
     *      $('mutsu').hasClassName('fruit');
     *      // -> false
     *
     *      $('mutsu').toggleClassName('fruit');
     *      // -> element
     *
     *      $('mutsu').hasClassName('fruit');
     *      // -> true
     **/
    toggleClassName: function(element, className) {
        if (!(element = $(element))) return;
        return Element[Element.hasClassName(element, className) ?
            'removeClassName' : 'addClassName'](element, className);
    },

    /**
     *  Element.cleanWhitespace(@element) -> Element
     *
     *  Removes all of `element`'s child text nodes that contain *only*
     *  whitespace. Returns `element`.
     *
     *  This can be very useful when using standard properties like `nextSibling`,
     *  `previousSibling`, `firstChild` or `lastChild` to walk the DOM. Usually
     *  you'd only do that if you are interested in all of the DOM nodes, not
     *  just Elements (since if you just need to traverse the Elements in the
     *  DOM tree, you can use [[Element.up]], [[Element.down]],
     *  [[Element.next]], and [[Element.previous]] instead).
     *
     *  #### Example
     *
     *  Consider the following HTML snippet:
     *
     *      language: html
     *      <ul id="apples">
     *        <li>Mutsu</li>
     *        <li>McIntosh</li>
     *        <li>Ida Red</li>
     *      </ul>
     *
     *  Let's grab what we think is the first list item using the raw DOM
     *  method:
     *
     *      var element = $('apples');
     *      element.firstChild.innerHTML;
     *      // -> undefined
     *
     *  It's undefined because the `firstChild` of the `apples` element is a
     *  text node containing the whitespace after the end of the `ul` and before
     *  the first `li`.
     *
     *  If we remove the useless whitespace, then `firstChild` works as expected:
     *
     *      var element = $('apples');
     *      element.cleanWhitespace();
     *      element.firstChild.innerHTML;
     *      // -> 'Mutsu'
     **/
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

    /**
     *  Element.empty(@element) -> Element
     *
     *  Tests whether `element` is empty (i.e., contains only whitespace).
     *
     *  ##### Examples
     *
     *      <div id="wallet">     </div>
     *      <div id="cart">full!</div>
     *
     *      $('wallet').empty();
     *      // -> true
     *
     *      $('cart').empty();
     *      // -> false
     **/
    empty: function(element) {
        return $(element).innerHTML.blank();
    },

    /**
     *  Element.descendantOf(@element, ancestor) -> Boolean
     *  - ancestor (Element | String): The element to check against (or its ID).
     *
     *  Checks if `element` is a descendant of `ancestor`.
     *
     *  ##### Example
     *
     *  Assuming:
     *
     *      language: html
     *      <div id="australopithecus">
     *        <div id="homo-erectus">
     *          <div id="homo-sapiens"></div>
     *        </div>
     *      </div>
     *
     *  Then:
     *
     *      $('homo-sapiens').descendantOf('australopithecus');
     *      // -> true
     *
     *      $('homo-erectus').descendantOf('homo-sapiens');
     *      // -> false
     **/
    descendantOf: function(element, ancestor) {
        element = $(element),ancestor = $(ancestor);

        if (element.compareDocumentPosition)
            return (element.compareDocumentPosition(ancestor) & 8) === 8;

        if (ancestor.contains)
            return ancestor.contains(element) && ancestor !== element;

        while (element = element.parentNode)
            if (element == ancestor) return true;

        return false;
    },

    /**
     *  Element.scrollTo(@element) -> Element
     *
     *  Scrolls the window so that `element` appears at the top of the viewport.
     *
     *  This has a similar effect than what would be achieved using
     *  [HTML anchors](http://www.w3.org/TR/html401/struct/links.html#h-12.2.3)
     *  (except the browser's history is not modified).
     *
     *  ##### Example
     *
     *      $(element).scrollTo();
     *      // -> Element
     **/
    scrollTo: function(element) {
        element = $(element);
        var pos = Element.cumulativeOffset(element);
        window.scrollTo(pos[0], pos[1]);
        return element;
    },

    /**
     *  Element.getStyle(@element, style) -> String | null
     *  - style (String): The property name to be retrieved.
     *
     *  Returns the given CSS property value of `element`. The property can be
     *  specified in either its CSS form (`font-size`) or its camelized form
     *  (`fontSize`).
     *
     *  This method looks up the CSS property of an element whether it was
     *  applied inline or in a stylesheet. It works around browser inconsistencies
     *  regarding `float`, `opacity`, which returns a value between `0`
     *  (fully transparent) and `1` (fully opaque), position properties
     *  (`left`, `top`, `right` and `bottom`) and when getting the dimensions
     *  (`width` or `height`) of hidden elements.
     *
     *  ##### Examples
     *
     *      $(element).getStyle('font-size');
     *      // equivalent:
     *
     *      $(element).getStyle('fontSize');
     *      // -> '12px'
     *
     *  ##### Notes
     *
     *  Internet Explorer returns literal values while other browsers return
     *  computed values.
     *
     *  Consider the following HTML snippet:
     *
     *      language: html
     *      <style>
     *        #test {
     *          font-size: 12px;
     *          margin-left: 1em;
     *        }
     *      </style>
     *      <div id="test"></div>
     *
     *  Then:
     *
     *      $('test').getStyle('margin-left');
     *      // -> '1em' in Internet Explorer,
     *      // -> '12px' elsewhere.
     *
     *  Safari returns `null` for *any* non-inline property if the element is
     *  hidden (has `display` set to `'none'`).
     *
     *  Not all CSS shorthand properties are supported. You may only use the CSS
     *  properties described in the
     *  [Document Object Model (DOM) Level 2 Style Specification](http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-ElementCSSInlineStyle).
     **/
    getStyle: function(element, style) {
        element = $(element);
        style = style == 'float' ? 'cssFloat' : style.camelize();
        var value = element.style[style];
        if (!value || value == 'auto') {
            var css = document.defaultView.getComputedStyle(element, null);
            value = css ? css[style] : null;
        }
        if (style == 'opacity') return value ? parseFloat(value) : 1.0;
        return value == 'auto' ? null : value;
    },

    /**
     *  Element.getOpacity(@element) -> String | null
     *
     *  Returns the opacity of the element.
     **/
    getOpacity: function(element) {
        return $(element).getStyle('opacity');
    },

    /**
     *  Element.setStyle(@element, styles) -> Element
     *
     *  Modifies `element`'s CSS style properties. Styles are passed as a hash of
     *  property-value pairs in which the properties are specified in their
     *  camelized form.
     *
     *  ##### Examples
     *
     *      $(element).setStyle({
     *        backgroundColor: '#900',
     *        fontSize: '12px'
     *      });
     *      // -> Element
     *
     *  ##### Notes
     *
     *  The method transparently deals with browser inconsistencies for `float`
     *  (however, as `float` is a reserved keyword, you must either escape it or
     *  use `cssFloat` instead) and `opacity` (which accepts values between `0`
     *  -fully transparent- and `1` -fully opaque-). You can safely use either of
     *  the following across all browsers:
     *
     *      $(element).setStyle({
     *        cssFloat: 'left',
     *        opacity: 0.5
     *      });
     *      // -> Element
     *
     *      $(element).setStyle({
     *        'float': 'left', // notice how float is surrounded by single quotes
     *        opacity: 0.5
     *      });
     *      // -> Element
     *
     *  Not all CSS shorthand properties are supported. You may only use the CSS
     *  properties described in the
     *  [Document Object Model (DOM) Level 2 Style Specification](http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-ElementCSSInlineStyle).
     **/
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

    /**
     *  Element.setOpacity(@element, opacity) -> [Element...]
     *
     *  Sets the visual opacity of an element while working around inconsistencies
     *  in various browsers. The `opacity` argument should be a floating point
     *  number, where the value of `0` is fully transparent and `1` is fully opaque.
     *
     *  [[Element.setStyle]] method uses [[Element.setOpacity]] internally when needed.
     *
     *  ##### Examples
     *
     *      var element = $('myelement');
     *      // set to 50% transparency
     *      element.setOpacity(0.5);
     *
     *      // these are equivalent, but allow for setting more than
     *      // one CSS property at once:
     *      element.setStyle({ opacity: 0.5 });
     *      element.setStyle("opacity: 0.5");
     **/
    setOpacity: function(element, value) {
        element = $(element);
        element.style.opacity = (value == 1 || value === '') ? '' :
            (value < 0.00001) ? 0 : value;
        return element;
    },

    /**
     *  Element.makePositioned(@element) -> Element
     *
     *  Allows for the easy creation of a CSS containing block by setting
     *  `element`'s CSS `position` to `relative` if its initial position is
     *  either `static` or `undefined`.
     *
     *  To revert back to `element`'s original CSS position, use
     *  [[Element.undoPositioned]].
     **/
    makePositioned: function(element) {
        element = $(element);
        var pos = Element.getStyle(element, 'position');
        if (pos == 'static' || !pos) {
            element._madePositioned = true;
            element.style.position = 'relative';
            // Opera returns the offset relative to the positioning context, when an
            // element is position relative but top and left have not been defined
            if (Prototype.Browser.Opera) {
                element.style.top = 0;
                element.style.left = 0;
            }
        }
        return element;
    },

    /**
     *  Element.undoPositioned(@element) -> Element
     *
     *  Sets `element` back to the state it was in _before_
     *  [[Element.makePositioned]] was applied to it.
     *
     *  `element`'s absolutely positioned children will now have their positions
     *  set relatively to `element`'s nearest ancestor with a CSS `position` of
     *  `'absolute'`, `'relative'` or `'fixed'`.
     **/
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

    /**
     *  Element.makeClipping(@element) -> Element
     *
     *  Simulates the poorly-supported CSS `clip` property by setting `element`'s
     *  `overflow` value to `hidden`.
     *
     *  To undo clipping, use [[Element.undoClipping]].
     *
     *  The visible area is determined by `element`'s width and height.
     *
     *  ##### Example
     *
     *      language:html
     *      <div id="framer">
     *        <img src="/assets/2007/1/14/chairs.jpg" alt="example" />
     *      </div>
     *
     *  Then:
     *
     *      $('framer').makeClipping().setStyle({width: '100px', height: '100px'});
     *      // -> Element
     *
     *  Another example:
     *
     *      language: html
     *      <a id="clipper" href="#">Click me to try it out.</a>
     *
     *      <div id="framer">
     *        <img src="/assets/2007/2/24/chairs.jpg" alt="example" />
     *      </div>
     *
     *      <script type="text/javascript" charset="utf-8">
     *        var Example = {
     *          clip: function(){
     *            $('clipper').update('undo clipping!');
     *            $('framer').makeClipping().setStyle({width: '100px', height: '100px'});
     *          },
     *          unClip: function(){
     *            $('clipper').update('clip!');
     *            $('framer').undoClipping();
     *          },
     *          toggleClip: function(event){
     *            if($('clipper').innerHTML == 'undo clipping!') Example.unClip();
     *            else Example.clip();
     *            Event.stop(event);
     *          }
     *        };
     *        Event.observe('clipper', 'click', Example.toggleClip);
     *      </script>
     **/
    makeClipping: function(element) {
        element = $(element);
        if (element._overflow) return element;
        element._overflow = Element.getStyle(element, 'overflow') || 'auto';
        if (element._overflow !== 'hidden')
            element.style.overflow = 'hidden';
        return element;
    },

    /**
     *  Element.undoClipping(@element) -> Element
     *
     *  Sets `element`'s CSS `overflow` property back to the value it had
     *  _before_ [[Element.makeClipping]] was applied.
     *
     *  ##### Example
     *
     *      language: html
     *      <div id="framer">
     *        <img src="/assets/2007/1/14/chairs.jpg" alt="example" />
     *      </div>
     *
     *  Then:
     *
     *      $('framer').undoClipping();
     *      // -> Element (and sets the CSS overflow property to its original value).
     *
     *  Another example:
     *
     *      language: html
     *      <a id="clipper" href="#">Click me to try it out.</a>
     *
     *      <div id="framer">
     *        <img src="/assets/2007/2/24/chairs_1.jpg" alt="example" />
     *      </div>
     *
     *      <script type="text/javascript" charset="utf-8">
     *        var Example = {
     *          clip: function(){
     *            $('clipper').update('undo clipping!');
     *            $('framer').makeClipping().setStyle({width: '100px', height: '100px'});
     *          },
     *          unClip: function(){
     *            $('clipper').update('clip!');
     *            $('framer').undoClipping();
     *          },
     *          toggleClip: function(event){
     *            if($('clipper').innerHTML == 'clip!') Example.clip();
     *            else Example.unClip();
     *            Event.stop(event);
     *          }
     *        };
     *        $('framer').makeClipping().setStyle({width: '100px', height: '100px'});
     *        Event.observe('clipper', 'click', Example.toggleClip);
     *      </script>
     **/
    undoClipping: function(element) {
        element = $(element);
        if (!element._overflow) return element;
        element.style.overflow = element._overflow == 'auto' ? '' : element._overflow;
        element._overflow = null;
        return element;
    },

    /**
     *  Element.clonePosition(@element, source[, options]) -> Element
     *  - source (Element | String): The source element (or its ID).
     *  - options (Object): The position fields to clone.
     *
     *  Clones the position and/or dimensions of `source` onto the element as
     *  defined by `options`, with an optional offset for the `left` and `top`
     *  properties.
     *
     *  Note that the element will be positioned exactly like `source` whether or
     *  not it is part of the same [CSS containing
     *  block](http://www.w3.org/TR/CSS21/visudet.html#containing-block-details).
     *
     *  ##### Options
     *
     *  <table class='options'>
     *  <thead>
     *    <tr>
     *      <th style='text-align: left; padding-right: 1em'>Name</th>
     *      <th style='text-align: left; padding-right: 1em'>Default</th>
     *      <th style='text-align: left; padding-right: 1em'>Description</th>
     *    </tr>
     *  </thead>
     *  <tbody>
     *    <tr>
     *    <td><code>setLeft</code></td>
     *    <td><code>true</code></td>
     *  <td>Clones <code>source</code>'s <code>left</code> CSS property onto <code>element</code>.</td>
     *  </tr>
     *  <tr>
     *    <td><code>setTop</code></td>
     *    <td><code>true</code></td>
     *  <td>Clones <code>source</code>'s <code>top</code> CSS property onto <code>element</code>.</td>
     *  </tr>
     *  <tr>
     *    <td><code>setWidth</code></td>
     *    <td><code>true</code></td>
     *  <td>Clones <code>source</code>'s <code>width</code> onto <code>element</code>.</td>
     *  </tr>
     *  <tr>
     *    <td><code>setHeight</code></td>
     *    <td><code>true</code></td>
     *  <td>Clones <code>source</code>'s <code>width</code> onto <code>element</code>.</td>
     *  </tr>
     *  <tr>
     *    <td><code>offsetLeft</code></td>
     *    <td><code>0</code></td>
     *  <td>Number by which to offset <code>element</code>'s <code>left</code> CSS property.</td>
     *  </tr>
     *  <tr>
     *    <td><code>offsetTop</code></td>
     *    <td><code>0</code></td>
     *  <td>Number by which to offset <code>element</code>'s <code>top</code> CSS property.</td>
     *  </tr>
     *  </tbody>
     *  </table>
     **/
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
        var p = Element.viewportOffset(source).toArray(), delta = [0, 0], parent = null;

        // find coordinate system to use
        element = $(element);

        // delta [0,0] will do fine with position: fixed elements,
        // position:absolute needs offsetParent deltas
        if (Element.getStyle(element, 'position') == 'absolute') {
            parent = Element.getOffsetParent(element);
            delta = Element.viewportOffset(parent).toArray();
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

Object.extend(Element.Methods, {
            /** alias of: Element.select
             *  Element.getElementsBySelector(@element, selector) -> [Element...]
             **/
            getElementsBySelector: Element.Methods.select,

            /**
             *  Element.childElements(@element) -> [Element...]
             *
             *  Collects all of the element's children and returns them as an array of
             *  [[Element.extended extended]] elements, in document order. The first
             *  entry in the array is the topmost child of `element`, the next is the
             *  child after that, etc.
             *
             *  Like all of Prototype's DOM traversal methods, [[Element.childElements]]
             *  ignores text nodes and returns element nodes only.
             *
             *  ##### Example
             *
             *  Assuming:
             *
             *      language: html
             *      <div id="australopithecus">
             *        Some text in a text node
             *        <div id="homo-erectus">
             *          <div id="homo-neanderthalensis"></div>
             *          <div id="homo-sapiens"></div>
             *        </div>
             *      </div>
             *
             *  Then:
             *
             *      $('australopithecus').childElements();
             *      // -> [div#homo-erectus]
             *
             *      $('homo-erectus').childElements();
             *      // -> [div#homo-neanderthalensis, div#homo-sapiens]
             *
             *      $('homo-sapiens').childElements();
             *      // -> []
             **/
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

if (Prototype.Browser.Opera) {
    Element.Methods.getStyle = Element.Methods.getStyle.wrap(
            function(proceed, element, style) {
                switch (style) {
                    case 'height':
                    case 'width':
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
                    default:
                        return proceed(element, style);
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

    Element._attributeTranslations = (function() {

        var classProp = 'className',
                forProp = 'for',
                el = document.createElement('div');

        // try "className" first (IE <8)
        el.setAttribute(classProp, 'x');

        if (el.className !== 'x') {
            // try "class" (IE 8)
            el.setAttribute('class', 'x');
            if (el.className === 'x') {
                classProp = 'class';
            }
        }
        el = null;

        el = document.createElement('label');
        el.setAttribute(forProp, 'x');
        if (el.htmlFor !== 'x') {
            el.setAttribute('htmlFor', 'x');
            if (el.htmlFor === 'x') {
                forProp = 'htmlFor';
            }
        }
        el = null;

        return {
            read: {
                names: {
                    'class':      classProp,
                    'className':  classProp,
                    'for':        forProp,
                    'htmlFor':    forProp
                },
                values: {
                    _getAttr: function(element, attribute) {
                        return element.getAttribute(attribute);
                    },
                    _getAttr2: function(element, attribute) {
                        return element.getAttribute(attribute, 2);
                    },
                    _getAttrNode: function(element, attribute) {
                        var node = element.getAttributeNode(attribute);
                        return node ? node.value : "";
                    },
                    _getEv: (function() {

                        var el = document.createElement('div'), f;
                        el.onclick = Prototype.emptyFunction;
                        var value = el.getAttribute('onclick');

                        // IE<8
                        if (String(value).indexOf('{') > -1) {
                            // intrinsic event attributes are serialized as `function { ... }`
                            f = function(element, attribute) {
                                attribute = element.getAttribute(attribute);
                                if (!attribute) return null;
                                attribute = attribute.toString();
                                attribute = attribute.split('{')[1];
                                attribute = attribute.split('}')[0];
                                return attribute.strip();
                            };
                        }
                        // IE8
                        else if (value === '') {
                            // only function body is serialized
                            f = function(element, attribute) {
                                attribute = element.getAttribute(attribute);
                                if (!attribute) return null;
                                return attribute.strip();
                            };
                        }
                        el = null;
                        return f;
                    })(),
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
        }
    })();

    Element._attributeTranslations.write = {
        names: Object.extend({
                    cellpadding: 'cellPadding',
                    cellspacing: 'cellSpacing'
                }, Element._attributeTranslations.read.names),
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
            'encType maxLength readOnly longDesc frameBorder').each(function(attr) {
                Element._attributeTranslations.write.names[attr.toLowerCase()] = attr;
                Element._attributeTranslations.has[attr.toLowerCase()] = attr;
            });

    (function(v) {
        Object.extend(v, {
                    href:        v._getAttr2,
                    src:         v._getAttr2,
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

    // We optimize Element#down for IE so that it does not call
    // Element#descendants (and therefore extend all nodes).
    if (Prototype.BrowserFeatures.ElementExtensions) {
        (function() {
            function _descendants(element) {
                var nodes = element.getElementsByTagName('*'), results = [];
                for (var i = 0, node; node = nodes[i]; i++)
                    if (node.tagName !== "!") // Filter out comment nodes.
                        results.push(node);
                return results;
            }

            Element.Methods.down = function(element, expression, index) {
                element = $(element);
                if (arguments.length == 1) return element.firstDescendant();
                return Object.isNumber(expression) ? _descendants(element)[expression] :
                        Element.select(element, expression)[index || 0];
            }
        })();
    }

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
            if (element.tagName.toUpperCase() == 'IMG' && element.width) {
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
}

if ('outerHTML' in document.documentElement) {
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
            var nextSibling = element.next(),
                    fragments = Element._getContentFromAnonymousElement(tagName, content.stripScripts());
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

Element._getContentFromAnonymousElement = function(tagName, html, force) {
    var div = new Element('div'),
            t = Element._insertionTranslations.tags[tagName];

    var workaround = false;
    if (t) workaround = true;
    else if (force) {
        workaround = true;
        t = ['', '', 0];
    }

    if (workaround) {
        // Adding a text node to the beginning of the string (then removing it)
        // fixes an issue in Internet Explorer. See Element#update above.
        div.innerHTML = '&nbsp;' + t[0] + html + t[1];
        div.removeChild(div.firstChild);
        for (var i = t[2]; i--;) {
            div = div.firstChild;
        }
    }
    else {
        div.innerHTML = html;
    }
    return $A(div.childNodes);
};

Element._insertionTranslations = {
    before: function(element, node) {
        element.parentNode.insertBefore(node, element);
    },
    top: function(element, node) {
        element.insertBefore(node, element.firstChild);
    },
    bottom: function(element, node) {
        element.appendChild(node);
    },
    after: function(element, node) {
        element.parentNode.insertBefore(node, element.nextSibling);
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
    var tags = Element._insertionTranslations.tags;
    Object.extend(tags, {
                THEAD: tags.TBODY,
                TFOOT: tags.TBODY,
                TH:    tags.TD
            });
})();

Element.Methods.Simulated = {
    /**
     *  Element.hasAttribute(@element, attribute) -> Boolean
     *
     *  Simulates the standard compliant DOM method
     *  [`hasAttribute`](http://www.w3.org/TR/DOM-Level-2-Core/core.html#ID-ElHasAttr)
     *  for browsers missing it (Internet Explorer 6 and 7).
     *
     *  ##### Example
     *
     *      language: html
     *      <a id="link" href="http://prototypejs.org">Prototype</a>
     *
     *  Then:
     *
     *      $('link').hasAttribute('href');
     *      // -> true
     **/
    hasAttribute: function(element, attribute) {
        attribute = Element._attributeTranslations.has[attribute] || attribute;
        var node = $(element).getAttributeNode(attribute);
        return !!(node && node.specified);
    }
};

Element.Methods.ByTag = { };

Object.extend(Element, Element.Methods);

(function(div) {

    if (!Prototype.BrowserFeatures.ElementExtensions && div['__proto__']) {
        window.HTMLElement = { };
        window.HTMLElement.prototype = div['__proto__'];
        Prototype.BrowserFeatures.ElementExtensions = true;
    }

    div = null;

})(document.createElement('div'));

/**
 *  Element.extend(element) -> Element
 *
 *  Extends the given element instance with all of the Prototype goodness and
 *  syntactic sugar, as well as any extensions added via [[Element.addMethods]].
 *  (If the element instance was already extended, this is a no-op.)
 *
 *  You only need to use [[Element.extend]] on element instances you've acquired
 *  directly from the DOM; **all** Prototype methods that return element
 *  instances (such as [[$]], [[Element.down]], etc.) will pre-extend the
 *  element before returning it.
 *
 *  Check out ["How Prototype extends the
 *  DOM"](http://prototypejs.org/learn/extensions) for more about element
 *  extensions.
 *
 *  ##### Details
 *
 *  Specifically, [[Element.extend]] extends the given instance with the methods
 *  contained in [[Element.Methods]] and `Element.Methods.Simulated`. If `element`
 *  is an `input`, `textarea`, or `select` element, it will also be extended
 *  with the methods from `Form.Element.Methods`. If it is a `form` element, it
 *  will also be extended with the methods from `Form.Methods`.
 **/
Element.extend = (function() {

    function checkDeficiency(tagName) {
        if (typeof window.Element != 'undefined') {
            var proto = window.Element.prototype;
            if (proto) {
                var id = '_' + (Math.random() + '').slice(2),
                        el = document.createElement(tagName);
                proto[id] = 'x';
                var isBuggy = (el[id] !== 'x');
                delete proto[id];
                el = null;
                return isBuggy;
            }
        }
        return false;
    }

    function extendElementWith(element, methods) {
        for (var property in methods) {
            var value = methods[property];
            if (Object.isFunction(value) && !(property in element))
                element[property] = value.methodize();
        }
    }

    var HTMLOBJECTELEMENT_PROTOTYPE_BUGGY = checkDeficiency('object');

    if (Prototype.BrowserFeatures.SpecificElementExtensions) {
        // IE8 has a bug with `HTMLObjectElement` and `HTMLAppletElement` objects
        // not being able to "inherit" from `Element.prototype`
        // or a specific prototype - `HTMLObjectElement.prototype`, `HTMLAppletElement.prototype`
        if (HTMLOBJECTELEMENT_PROTOTYPE_BUGGY) {
            return function(element) {
                if (element && typeof element._extendedByPrototype == 'undefined') {
                    var t = element.tagName;
                    if (t && (/^(?:object|applet|embed)$/i.test(t))) {
                        extendElementWith(element, Element.Methods);
                        extendElementWith(element, Element.Methods.Simulated);
                        extendElementWith(element, Element.Methods.ByTag[t.toUpperCase()]);
                    }
                }
                return element;
            }
        }
        return Prototype.K;
    }

    var Methods = { }, ByTag = Element.Methods.ByTag;

    var extend = Object.extend(function(element) {
        // need to use actual `typeof` operator
        // to prevent errors in some environments (when accessing node expandos)
        if (!element || typeof element._extendedByPrototype != 'undefined' ||
                element.nodeType != 1 || element == window) return element;

        var methods = Object.clone(Methods),
                tagName = element.tagName.toUpperCase();

        // extend methods for specific tags
        if (ByTag[tagName]) Object.extend(methods, ByTag[tagName]);

        extendElementWith(element, methods);

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

if (document.documentElement.hasAttribute) {
    Element.hasAttribute = function(element, attribute) {
        return element.hasAttribute(attribute);
    };
}
else {
    Element.hasAttribute = Element.Methods.Simulated.hasAttribute;
}

/**
 *  Element.addMethods(methods) -> undefined
 *  Element.addMethods(tagName, methods) -> undefined
 *  - tagName (String): (Optional) The name of the HTML tag for which the
 *    methods should be available; if not given, all HTML elements will have
 *    the new methods.
 *  - methods (Object): A hash of methods to add.
 *
 *  [[Element.addMethods]] makes it possible to mix your *own* methods into the
 *  [[Element]] object and extended element instances (all of them, or only ones
 *  with the given HTML tag if you specify `tagName`).
 *
 *  You define the methods in a hash that you provide to [[Element.addMethods]].
 *  Here's an example adding two methods:
 *
 *      Element.addMethods({
 *
 *        // myOwnMethod: Do something cool with the element
 *        myOwnMethod: function(element) {
 *          if (!(element = $(element))) return;
 *          // ...do smething with 'element'...
 *          return element;
 *        },
 *
 *        // wrap: Wrap the element in a new element using the given tag
 *        wrap: function(element, tagName) {
 *          var wrapper;
 *          if (!(element = $(element))) return;
 *          wrapper = new Element(tagName);
 *          element.parentNode.replaceChild(wrapper, element);
 *          wrapper.appendChild(element);
 *          return wrapper;
 *        }
 *
 *      });
 *
 *  Once added, those can be used either via [[Element]]:
 *
 *      // Wrap the element with the ID 'foo' in a div
 *      Element.wrap('foo', 'div');
 *
 *  ...or as instance methods of extended elements:
 *
 *      // Wrap the element with the ID 'foo' in a div
 *      $('foo').wrap('div');
 *
 *  Note the following requirements and conventions for methods added to
 *  [[Element]]:
 *
 *  - The first argument is *always* an element or ID, by convention this
 *    argument is called `element`.
 *  - The method passes the `element` argument through [[$]] and typically
 *    returns if the result is undefined.
 *  - Barring a good reason to return something else, the method returns the
 *    extended element to enable chaining.
 *
 *  Our `myOwnMethod` method above returns the element because it doesn't have
 *  a good reason to return anything else. Our `wrap` method returns the
 *  wrapper, because that makes more sense for that method.
 *
 *  ##### Extending only specific elements
 *
 *  If you call [[Element.addMethods]] with *two* arguments, it will apply the
 *  methods only to elements with the given HTML tag:
 *
 *      Element.addMethods('DIV', my_div_methods);
 *      // the given methods are now available on DIV elements, but not others
 *
 *  You can also pass an *[[Array]]* of tag names as the first argument:
 *
 *      Element.addMethods(['DIV', 'SPAN'], my_additional_methods);
 *      // DIV and SPAN now both have the given methods
 *
 *  (Tag names in the first argument are not case sensitive.)
 *
 *  Note: [[Element.addMethods]] has built-in security which prevents you from
 *  overriding native element methods or properties (like `getAttribute` or
 *  `innerHTML`), but nothing prevents you from overriding one of Prototype's
 *  methods. Prototype uses a lot of its methods internally; overriding its
 *  methods is best avoided or at least done only with great care.
 *
 *  ##### Example 1
 *
 *  Our `wrap` method earlier was a complete example. For instance, given this
 *  paragraph:
 *
 *      language: html
 *      <p id="first">Some content...</p>
 *
 *  ...we might wrap it in a `div`:
 *
 *      $('first').wrap('div');
 *
 *  ...or perhaps wrap it and apply some style to the `div` as well:
 *
 *      $('first').wrap('div').setStyle({
 *        backgroundImage: 'url(images/rounded-corner-top-left.png) top left'
 *      });
 *
 *  ##### Example 2
 *
 *  We can add a method to elements that makes it a bit easier to update them
 *  via [[Ajax.Updater]]:
 *
 *      Element.addMethods({
 *        ajaxUpdate: function(element, url, options) {
 *          if (!(element = $(element))) return;
 *          element.update('<img src="/images/spinner.gif" alt="Loading...">');
 *          options = options || {};
 *          options.onFailure = options.onFailure || defaultFailureHandler.curry(element);
 *          new Ajax.Updater(element, url, options);
 *          return element;
 *        }
 *      });
 *
 *  Now we can update an element via an Ajax call much more concisely than
 *  before:
 *
 *      $('foo').ajaxUpdate('/new/content');
 *
 *  That will use [[Ajax.Updater]] to load new content into the 'foo' element,
 *  showing a spinner while the call is in progress. It even applies a default
 *  failure handler (since we didn't supply one).
 **/
Element.addMethods = function(methods) {
    var F = Prototype.BrowserFeatures, T = Element.Methods.ByTag;

    if (!methods) {
        Object.extend(Form, Form.Methods);
        Object.extend(Form.Element, Form.Element.Methods);
        Object.extend(Element.Methods.ByTag, {
                    "FORM":     Object.clone(Form.Methods),
                    "INPUT":    Object.clone(Form.Element.Methods),
                    "SELECT":   Object.clone(Form.Element.Methods),
                    "TEXTAREA": Object.clone(Form.Element.Methods),
                    "BUTTON":   Object.clone(Form.Element.Methods)
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

        var element = document.createElement(tagName),
                proto = element['__proto__'] || element.constructor.prototype;

        element = null;
        return proto;
    }

    var elementPrototype = window.HTMLElement ? HTMLElement.prototype :
            Element.prototype;

    if (F.ElementExtensions) {
        copy(Element.Methods, elementPrototype);
        copy(Element.Methods.Simulated, elementPrototype, true);
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

/**
 *  document.viewport
 *
 *  The `document.viewport` namespace contains methods that return information
 *  about the viewport &mdash; the rectangle that represents the portion of a web
 *  page within view. In other words, it's the browser window minus all chrome.
 **/

document.viewport = {

    /**
     *  document.viewport.getDimensions() -> Object
     *
     *  Returns an object containing viewport dimensions in the form
     *  `{ width: Number, height: Number }`.
     *
     *  The _viewport_ is the subset of the browser window that a page occupies
     *  &mdash; the "usable" space in a browser window.
     *
     *  ##### Example
     *
     *      document.viewport.getDimensions();
     *      //-> { width: 776, height: 580 }
     **/
    getDimensions: function() {
        return { width: this.getWidth(), height: this.getHeight() };
    },

    /**
     *  document.viewport.getScrollOffsets() -> Array
     *
     *  Returns the viewport's horizontal and vertical scroll offsets.
     *
     *  Returns an array in the form of `[leftValue, topValue]`. Also accessible
     *  as properties: `{ left: leftValue, top: topValue }`.
     *
     *  ##### Examples
     *
     *      document.viewport.getScrollOffsets();
     *      //-> { left: 0, top: 0 }
     *
     *      window.scrollTo(0, 120);
     *      document.viewport.getScrollOffsets();
     *      //-> { left: 0, top: 120 }
     **/
    getScrollOffsets: function() {
        return Element._returnOffset(
                window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft,
                window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop);
    }
};

(function(viewport) {
    var B = Prototype.Browser, doc = document, element, property = {};

    function getRootElement() {
        // Older versions of Safari.
        if (B.WebKit && !doc.evaluate)
            return document;

        // Older versions of Opera.
        if (B.Opera && window.parseFloat(window.opera.version()) < 9.5 || !document.compatMode || document.compatMode != "CSS1Compat")
            return document.body;

        return document.documentElement;
    }

    function define(D) {
        if (!element) element = getRootElement();

        property[D] = (B.WebKit && !doc.evaluate ? 'inner' : 'client') + D;

        viewport['get' + D] = function() {
            return element[property[D]]
        };
        return viewport['get' + D]();
    }

    /**
     *  document.viewport.getWidth() -> Number
     *
     *  Returns the width of the viewport.
     *
     *  Equivalent to calling `document.viewport.getDimensions().width`.
     **/
    viewport.getWidth = define.curry('Width');

    /**
     *  document.viewport.getHeight() -> Number
     *
     *  Returns the height of the viewport.
     *
     *  Equivalent to `document.viewport.getDimensions().height`.
     **/
    viewport.getHeight = define.curry('Height');
})(document.viewport);


Element.Storage = {
    UID: 1
};

Element.addMethods({
            /**
             *  Element.getStorage(@element) -> Hash
             *
             *  Returns the [[Hash]] object that stores custom metadata for this element.
             **/
            getStorage: function(element) {
                if (!(element = $(element))) return;

                var uid;
                if (element === window) {
                    uid = 0;
                } else {
                    if (typeof element._prototypeUID === "undefined")
                        element._prototypeUID = Element.Storage.UID++;
                    uid = element._prototypeUID;
                }

                if (!Element.Storage[uid])
                    Element.Storage[uid] = $H();

                return Element.Storage[uid];
            },

            /**
             *  Element.store(@element, key, value) -> Element
             *
             *  Stores a key/value pair of custom metadata on the element.
             *
             *  The metadata can later be retrieved with [[Element.retrieve]].
             **/
            store: function(element, key, value) {
                if (!(element = $(element))) return;

                if (arguments.length === 2) {
                    // Assume we've been passed an object full of key/value pairs.
                    Element.getStorage(element).update(key);
                } else {
                    Element.getStorage(element).set(key, value);
                }

                return element;
            },

            /**
             *  Element.retrieve(@element, key[, defaultValue]) -> ?
             *
             *  Retrieves custom metadata set on `element` with [[Element.store]].
             *
             *  If the value is `undefined` and `defaultValue` is given, it will be
             *  stored on the element as its new value for that key, then returned.
             **/
            retrieve: function(element, key, defaultValue) {
                if (!(element = $(element))) return;
                var hash = Element.getStorage(element), value = hash.get(key);

                if (Object.isUndefined(value)) {
                    hash.set(key, defaultValue);
                    value = defaultValue;
                }

                return value;
            },

            /**
             *  Element.clone(@element, deep) -> Element
             *  - deep (Boolean): Whether to clone `element`'s descendants as well.
             *
             *  Returns a duplicate of `element`.
             *
             *  A wrapper around DOM Level 2 `Node#cloneNode`, [[Element.clone]] cleans up
             *  any expando properties defined by Prototype.
             **/
            clone: function(element, deep) {
                if (!(element = $(element))) return;
                var clone = element.cloneNode(deep);
                clone._prototypeUID = void 0;
                if (deep) {
                    var descendants = Element.select(clone, '*'),
                            i = descendants.length;
                    while (i--) {
                        descendants[i]._prototypeUID = void 0;
                    }
                }
                return Element.extend(clone);
            },

            /**
             *  Element.purge(@element) -> null
             *
             *  Removes all event listeners and storage keys from an element.
             *
             *  To be used just before removing an element from the page.
             **/
            purge: function(element) {
                if (!(element = $(element))) return;
                var purgeElement = Element._purgeElement;

                purgeElement(element);

                var descendants = element.getElementsByTagName('*'),
                        i = descendants.length;

                while (i--) purgeElement(descendants[i]);

                return null;
            }
        });


(function() {

    // Converts a CSS percentage value to a decimal.
    // Ex: toDecimal("30%"); // -> 0.3
    function toDecimal(pctString) {
        var match = pctString.match(/^(\d+)%?$/i);
        if (!match) return null;
        return (Number(match[1]) / 100);
    }

    // Can be called like this:
    //   getPixelValue("11px");
    // Or like this:
    //   getPixelValue(someElement, 'paddingTop');
    function getPixelValue(value, property, context) {
        var element = null;
        if (Object.isElement(value)) {
            element = value;
            value = element.getStyle(property);
        }

        if (value === null) {
            return null;
        }

        // Non-IE browsers will always return pixels if possible.
        // (We use parseFloat instead of parseInt because Firefox can return
        // non-integer pixel values.)
        if ((/^(?:-)?\d+(\.\d+)?(px)?$/i).test(value)) {
            return window.parseFloat(value);
        }

        var isPercentage = value.include('%'), isViewport = (context === document.viewport);

        // When IE gives us something other than a pixel value, this technique
        // (invented by Dean Edwards) will convert it to pixels.
        //
        // (This doesn't work for percentage values on elements with `position: fixed`
        // because those percentages are relative to the viewport.)
        if (/\d/.test(value) && element && element.runtimeStyle && !(isPercentage && isViewport)) {
            var style = element.style.left, rStyle = element.runtimeStyle.left;
            element.runtimeStyle.left = element.currentStyle.left;
            element.style.left = value || 0;
            value = element.style.pixelLeft;
            element.style.left = style;
            element.runtimeStyle.left = rStyle;

            return value;
        }

        // For other browsers, we have to do a bit of work.
        // (At this point, only percentages should be left; all other CSS units
        // are converted to pixels by getComputedStyle.)
        if (element && isPercentage) {
            context = context || element.parentNode;
            var decimal = toDecimal(value);
            var whole = null;
            var position = element.getStyle('position');

            var isHorizontal = property.include('left') || property.include('right') ||
                    property.include('width');

            var isVertical = property.include('top') || property.include('bottom') ||
                    property.include('height');

            if (context === document.viewport) {
                if (isHorizontal) {
                    whole = document.viewport.getWidth();
                } else if (isVertical) {
                    whole = document.viewport.getHeight();
                }
            } else {
                if (isHorizontal) {
                    whole = $(context).measure('width');
                } else if (isVertical) {
                    whole = $(context).measure('height');
                }
            }

            return (whole === null) ? 0 : whole * decimal;
        }

        // If we get this far, we should probably give up.
        return 0;
    }

    // Turns plain numbers into pixel measurements.
    function toCSSPixels(number) {
        if (Object.isString(number) && number.endsWith('px')) {
            return number;
        }
        return number + 'px';
    }

    function isDisplayed(element) {
        var originalElement = element;
        while (element && element.parentNode) {
            var display = element.getStyle('display');
            if (display === 'none') {
                return false;
            }
            element = $(element.parentNode);
        }
        return true;
    }

    var hasLayout = Prototype.K;
    if ('currentStyle' in document.documentElement) {
        hasLayout = function(element) {
            if (!element.currentStyle.hasLayout) {
                element.style.zoom = 1;
            }
            return element;
        };
    }

    // Converts the layout hash property names back to the CSS equivalents.
    // For now, only the border properties differ.
    function cssNameFor(key) {
        if (key.include('border')) key = key + '-width';
        return key.camelize();
    }

    /**
     *  class Element.Layout < Hash
     *
     *  A set of key/value pairs representing measurements of various
     *  dimensions of an element.
     *
     *  <h4>Overview</h4>
     *
     *  The `Element.Layout` class is a specialized way to measure elements.
     *  It helps mitigate:
     *
     *  * The convoluted steps often needed to get common measurements for
     *    elements.
     *  * The tendency of browsers to report measurements in non-pixel units.
     *  * The quirks that lead some browsers to report inaccurate measurements.
     *  * The difficulty of measuring elements that are hidden.
     *
     *  <h4>Usage</h4>
     *
     *  Instantiate an `Element.Layout` class by passing an element into the
     *  constructor:
     *
     *      var layout = new Element.Layout(someElement);
     *
     *  You can also use [[Element.getLayout]], if you prefer.
     *
     *  Once you have a layout object, retrieve properties using [[Hash]]'s
     *  familiar `get` and `set` syntax.
     *
     *      layout.get('width');  //-> 400
     *      layout.get('top');    //-> 180
     *
     *  The following are the CSS-related properties that can be retrieved.
     *  Nearly all of them map directly to their property names in CSS. (The
     *  only exception is for borders — e.g., `border-width` instead of
     *  `border-left-width`.)
     *
     *  * `height`
     *  * `width`
     *  * `top`
     *  * `left`
     *  * `right`
     *  * `bottom`
     *  * `border-left`
     *  * `border-right`
     *  * `border-top`
     *  * `border-bottom`
     *  * `padding-left`
     *  * `padding-right`
     *  * `padding-top`
     *  * `padding-bottom`
     *  * `margin-top`
     *  * `margin-bottom`
     *  * `margin-left`
     *  * `margin-right`
     *
     *  In addition, these "composite" properties can be retrieved:
     *
     *  * `padding-box-width` (width of the content area, from the beginning of
     *    the left padding to the end of the right padding)
     *  * `padding-box-height` (height of the content area, from the beginning
     *    of the top padding to the end of the bottom padding)
     *  * `border-box-width` (width of the content area, from the outer edge of
     *    the left border to the outer edge of the right border)
     *  * `border-box-height` (height of the content area, from the outer edge
     *    of the top border to the outer edge of the bottom border)
     *  * `margin-box-width` (width of the content area, from the beginning of
     *    the left margin to the end of the right margin)
     *  * `margin-box-height` (height of the content area, from the beginning
     *    of the top margin to the end of the bottom margin)
     *
     *  <h4>Caching</h4>
     *
     *  Because these properties can be costly to retrieve, `Element.Layout`
     *  behaves differently from an ordinary [[Hash]].
     *
     *  First: by default, values are "lazy-loaded" — they aren't computed
     *  until they're retrieved. To measure all properties at once, pass
     *  a second argument into the constructor:
     *
     *      var layout = new Element.Layout(someElement, true);
     *
     *  Second: once a particular value is computed, it's cached. Asking for
     *  the same property again will return the original value without
     *  re-computation. This means that **an instance of `Element.Layout`
     *  becomes stale when the element's dimensions change**. When this
     *  happens, obtain a new instance.
     *
     *  <h4>Hidden elements</h4>
     *
     *  Because it's a common case to want the dimensions of a hidden element
     *  (e.g., for animations), it's possible to measure elements that are
     *  hidden with `display: none`.
     *
     *  However, **it's only possible to measure a hidden element if its parent
     *  is visible**. If its parent (or any other ancestor) is hidden, any
     *  width and height measurements will return `0`, as will measurements for
     *  `top|bottom|left|right`.
     *
     **/
    Element.Layout = Class.create(Hashtable, {
                /**
                 *  new Element.Layout(element[, preCompute = false])
                 *  - element (Element): The element to be measured.
                 *  - preCompute (Boolean): Whether to compute all values at once. Default
                 *    is `false`.
                 *
                 *  Declare a new layout hash.
                 *
                 *  The `preCompute` argument determines whether measurements will be
                 *  lazy-loaded or not. If you plan to use many different measurements,
                 *  it's often more performant to pre-compute, as it minimizes the
                 *  amount of overhead needed to measure. If you need only one or two
                 *  measurements, it's probably not worth it.
                 **/
                initialize: function($super, element, preCompute) {
                    $super();
                    this.element = $(element);

                    // nullify all properties keys
                    Element.Layout.PROPERTIES.each(function(property) {
                        this._set(property, null);
                    }, this);

                    // The 'preCompute' boolean tells us whether we should fetch all values
                    // at once. If so, we should do setup/teardown only once. We set a flag
                    // so that we can ignore calls to `_begin` and `_end` elsewhere.
                    if (preCompute) {
                        this._preComputing = true;
                        this._begin();
                        Element.Layout.PROPERTIES.each(this._compute, this);
                        this._end();
                        this._preComputing = false;
                    }
                },

                _set: function(property, value) {
                    return Hashtable.prototype.set.call(this, property, value);
                },

                // TODO: Investigate.
                set: function(property, value) {
                    throw "Properties of Element.Layout are read-only.";
                },

                /**
                 *  Element.Layout#get(property) -> Number
                 *  - property (String): One of the properties defined in
                 *    [[Element.Layout.PROPERTIES]].
                 *
                 *  Retrieve the measurement specified by `property`. Will throw an error
                 *  if the property is invalid.
                 *
                 *  ##### Caveats
                 *
                 *  * `Element.Layout` can measure the dimensions of an element hidden with
                 *    CSS (`display: none`), but _only_ if its parent element is visible.
                 **/
                get: function($super, property) {
                    // Try to fetch from the cache.
                    var value = $super(property);
                    return value === null ? this._compute(property) : value;
                },

                // `_begin` and `_end` are two functions that are called internally
                // before and after any measurement is done. In certain conditions (e.g.,
                // when hidden), elements need a "preparation" phase that ensures
                // accuracy of measurements.
                _begin: function() {
                    if (this._prepared) return;

                    var element = this.element;
                    if (isDisplayed(element)) {
                        this._prepared = true;
                        return;
                    }

                    // Remember the original values for some styles we're going to alter.
                    var originalStyles = {
                        position:   element.style.position || '',
                        width:      element.style.width || '',
                        visibility: element.style.visibility || '',
                        display:    element.style.display || ''
                    };

                    // We store them so that the `_end` function can retrieve them later.
                    element.store('prototype_original_styles', originalStyles);

                    var position = element.getStyle('position'),
                            width = element.getStyle('width');

                    if (width === "0px" || width === null) {
                        // Opera won't report the true width of the element through
                        // `getComputedStyle` if it's hidden. If we got a nonsensical value,
                        // we need to show the element and try again.
                        element.style.display = 'block';
                        width = element.getStyle('width');
                    }

                    // Preserve the context in case we get a percentage value.
                    var context = (position === 'fixed') ? document.viewport :
                            element.parentNode;

                    element.setStyle({
                                position:   'absolute',
                                visibility: 'hidden',
                                display:    'block'
                            });

                    var positionedWidth = element.getStyle('width');

                    var newWidth;
                    if (width && (positionedWidth === width)) {
                        // If the element's width is the same both before and after
                        // we set absolute positioning, that means:
                        //  (a) it was already absolutely-positioned; or
                        //  (b) it has an explicitly-set width, instead of width: auto.
                        // Either way, it means the element is the width it needs to be
                        // in order to report an accurate height.
                        newWidth = getPixelValue(element, 'width', context);
                    } else if (position === 'absolute' || position === 'fixed') {
                        // Absolute- and fixed-position elements' dimensions don't depend
                        // upon those of their parents.
                        newWidth = getPixelValue(element, 'width', context);
                    } else {
                        // Otherwise, the element's width depends upon the width of its
                        // parent.
                        var parent = element.parentNode, pLayout = $(parent).getLayout();

                        newWidth = pLayout.get('width') -
                                this.get('margin-left') -
                                this.get('border-left') -
                                this.get('padding-left') -
                                this.get('padding-right') -
                                this.get('border-right') -
                                this.get('margin-right');
                    }

                    element.setStyle({ width: newWidth + 'px' });

                    // The element is now ready for measuring.
                    this._prepared = true;
                },

                _end: function() {
                    var element = this.element;
                    var originalStyles = element.retrieve('prototype_original_styles');
                    element.store('prototype_original_styles', null);
                    element.setStyle(originalStyles);
                    this._prepared = false;
                },

                _compute: function(property) {
                    var COMPUTATIONS = Element.Layout.COMPUTATIONS;
                    if (!(property in COMPUTATIONS)) {
                        throw "Property not found.";
                    }

                    return this._set(property, COMPUTATIONS[property].call(this, this.element));
                },

                /**
                 *  Element.Layout#toObject([keys...]) -> Object
                 *  - keys (String): A space-separated list of keys to include.
                 *
                 *  Converts the layout hash to a plain object of key/value pairs,
                 *  optionally including only the given keys.
                 *
                 *  Keys can be passed into this method as individual arguments _or_
                 *  separated by spaces within a string.
                 *
                 *      // Equivalent statements:
                 *      someLayout.toObject('top', 'bottom', 'left', 'right');
                 *      someLayout.toObject('top bottom left right');
                 **/
                toObject: function() {
                    var args = $A(arguments);
                    var keys = (args.length === 0) ? Element.Layout.PROPERTIES :
                            args.join(' ').split(' ');
                    var obj = {};
                    keys.each(function(key) {
                        // Key needs to be a valid Element.Layout property.
                        if (!Element.Layout.PROPERTIES.include(key)) return;
                        var value = this.get(key);
                        if (value != null) obj[key] = value;
                    }, this);
                    return obj;
                },

                /**
                 *  Element.Layout#toHash([keys...]) -> Hash
                 *  - keys (String): A space-separated list of keys to include.
                 *
                 *  Converts the layout hash to an ordinary hash of key/value pairs,
                 *  optionally including only the given keys.
                 *
                 *  Keys can be passed into this method as individual arguments _or_
                 *  separated by spaces within a string.
                 *
                 *      // Equivalent statements:
                 *      someLayout.toHash('top', 'bottom', 'left', 'right');
                 *      someLayout.toHash('top bottom left right');
                 **/
                toHash: function() {
                    var obj = this.toObject.apply(this, arguments);
                    return new Hashtable(obj);
                },

                /**
                 *  Element.Layout#toCSS([keys...]) -> Object
                 *  - keys (String): A space-separated list of keys to include.
                 *
                 *  Converts the layout hash to a plain object of CSS property/value
                 *  pairs, optionally including only the given keys.
                 *
                 *  Keys can be passed into this method as individual arguments _or_
                 *  separated by spaces within a string.
                 *
                 *      // Equivalent statements:
                 *      someLayout.toCSS('top', 'bottom', 'left', 'right');
                 *      someLayout.toCSS('top bottom left right');
                 *
                 *  Useful for passing layout properties to [[Element.setStyle]].
                 **/
                toCSS: function() {
                    var args = $A(arguments);
                    var keys = (args.length === 0) ? Element.Layout.PROPERTIES :
                            args.join(' ').split(' ');
                    var css = {};

                    keys.each(function(key) {
                        // Key needs to be a valid Element.Layout property...
                        if (!Element.Layout.PROPERTIES.include(key)) return;
                        // ...but not a composite property.
                        if (Element.Layout.COMPOSITE_PROPERTIES.include(key)) return;

                        var value = this.get(key);
                        // Unless the value is null, add 'px' to the end and add it to the
                        // returned object.
                        if (value != null) css[cssNameFor(key)] = value + 'px';
                    }, this);
                    return css;
                },

                inspect: function() {
                    return "#<Element.Layout>";
                }
            });

    Object.extend(Element.Layout, {
                /**
                 *  Element.Layout.PROPERTIES = Array
                 *
                 *  A list of all measurable properties.
                 **/
                PROPERTIES: $w('height width top left right bottom border-left border-right border-top border-bottom padding-left padding-right padding-top padding-bottom margin-top margin-bottom margin-left margin-right padding-box-width padding-box-height border-box-width border-box-height margin-box-width margin-box-height'),

                /**
                 *  Element.Layout.COMPOSITE_PROPERTIES = Array
                 *
                 *  A list of all composite properties. Composite properties don't map
                 *  directly to CSS properties — they're combinations of other
                 *  properties.
                 **/
                COMPOSITE_PROPERTIES: $w('padding-box-width padding-box-height margin-box-width margin-box-height border-box-width border-box-height'),

                COMPUTATIONS: {
                    'height': function(element) {
                        if (!this._preComputing) this._begin();

                        var bHeight = this.get('border-box-height');
                        if (bHeight <= 0) {
                            if (!this._preComputing) this._end();
                            return 0;
                        }

                        var bTop = this.get('border-top'),
                                bBottom = this.get('border-bottom');

                        var pTop = this.get('padding-top'),
                                pBottom = this.get('padding-bottom');

                        if (!this._preComputing) this._end();

                        return bHeight - bTop - bBottom - pTop - pBottom;
                    },

                    'width': function(element) {
                        if (!this._preComputing) this._begin();

                        var bWidth = this.get('border-box-width');
                        if (bWidth <= 0) {
                            if (!this._preComputing) this._end();
                            return 0;
                        }

                        var bLeft = this.get('border-left'),
                                bRight = this.get('border-right');

                        var pLeft = this.get('padding-left'),
                                pRight = this.get('padding-right');

                        if (!this._preComputing) this._end();

                        return bWidth - bLeft - bRight - pLeft - pRight;
                    },

                    'padding-box-height': function(element) {
                        var height = this.get('height'),
                                pTop = this.get('padding-top'),
                                pBottom = this.get('padding-bottom');

                        return height + pTop + pBottom;
                    },

                    'padding-box-width': function(element) {
                        var width = this.get('width'),
                                pLeft = this.get('padding-left'),
                                pRight = this.get('padding-right');

                        return width + pLeft + pRight;
                    },

                    'border-box-height': function(element) {
                        if (!this._preComputing) this._begin();
                        var height = element.offsetHeight;
                        if (!this._preComputing) this._end();
                        return height;
                    },

                    'border-box-width': function(element) {
                        if (!this._preComputing) this._begin();
                        var width = element.offsetWidth;
                        if (!this._preComputing) this._end();
                        return width;
                    },

                    'margin-box-height': function(element) {
                        var bHeight = this.get('border-box-height'),
                                mTop = this.get('margin-top'),
                                mBottom = this.get('margin-bottom');

                        if (bHeight <= 0) return 0;

                        return bHeight + mTop + mBottom;
                    },

                    'margin-box-width': function(element) {
                        var bWidth = this.get('border-box-width'),
                                mLeft = this.get('margin-left'),
                                mRight = this.get('margin-right');

                        if (bWidth <= 0) return 0;

                        return bWidth + mLeft + mRight;
                    },

                    'top': function(element) {
                        var offset = element.positionedOffset();
                        return offset.top;
                    },

                    'bottom': function(element) {
                        var offset = element.positionedOffset(),
                                parent = element.getOffsetParent(),
                                pHeight = parent.measure('height');

                        var mHeight = this.get('border-box-height');

                        return pHeight - mHeight - offset.top;
                        //
                        // return getPixelValue(element, 'bottom');
                    },

                    'left': function(element) {
                        var offset = element.positionedOffset();
                        return offset.left;
                    },

                    'right': function(element) {
                        var offset = element.positionedOffset(),
                                parent = element.getOffsetParent(),
                                pWidth = parent.measure('width');

                        var mWidth = this.get('border-box-width');

                        return pWidth - mWidth - offset.left;
                        //
                        // return getPixelValue(element, 'right');
                    },

                    'padding-top': function(element) {
                        return getPixelValue(element, 'paddingTop');
                    },

                    'padding-bottom': function(element) {
                        return getPixelValue(element, 'paddingBottom');
                    },

                    'padding-left': function(element) {
                        return getPixelValue(element, 'paddingLeft');
                    },

                    'padding-right': function(element) {
                        return getPixelValue(element, 'paddingRight');
                    },

                    'border-top': function(element) {
                        return getPixelValue(element, 'borderTopWidth');
                    },

                    'border-bottom': function(element) {
                        return getPixelValue(element, 'borderBottomWidth');
                    },

                    'border-left': function(element) {
                        return getPixelValue(element, 'borderLeftWidth');
                    },

                    'border-right': function(element) {
                        return getPixelValue(element, 'borderRightWidth');
                    },

                    'margin-top': function(element) {
                        return getPixelValue(element, 'marginTop');
                    },

                    'margin-bottom': function(element) {
                        return getPixelValue(element, 'marginBottom');
                    },

                    'margin-left': function(element) {
                        return getPixelValue(element, 'marginLeft');
                    },

                    'margin-right': function(element) {
                        return getPixelValue(element, 'marginRight');
                    }
                }
            });

    // An easier way to compute right and bottom offsets.
    if ('getBoundingClientRect' in document.documentElement) {
        Object.extend(Element.Layout.COMPUTATIONS, {
                    'right': function(element) {
                        var parent = hasLayout(element.getOffsetParent());
                        var rect = element.getBoundingClientRect(),
                                pRect = parent.getBoundingClientRect();

                        return (pRect.right - rect.right).round();
                    },

                    'bottom': function(element) {
                        var parent = hasLayout(element.getOffsetParent());
                        var rect = element.getBoundingClientRect(),
                                pRect = parent.getBoundingClientRect();

                        return (pRect.bottom - rect.bottom).round();
                    }
                });
    }

    /**
     *  class Element.Offset
     *
     *  A representation of the top- and left-offsets of an element relative to
     *  another.
     *
     *  All methods that compute offsets return an instance of `Element.Offset`.
     *
     **/
    Element.Offset = Class.create({
                /**
                 *  new Element.Offset(left, top)
                 *
                 *  Instantiates an [[Element.Offset]]. You shouldn't need to call this
                 *  directly.
                 **/
                initialize: function(left, top) {
                    this.left = left.round();
                    this.top = top.round();

                    // Act like an array.
                    this[0] = this.left;
                    this[1] = this.top;
                },

                /**
                 *  Element.Offset#relativeTo(offset) -> Element.Offset
                 *  - offset (Element.Offset): Another offset to compare to.
                 *
                 *  Returns a new [[Element.Offset]] with its origin at the given
                 *  `offset`. Useful for determining an element's distance from another
                 *  arbitrary element.
                 **/
                relativeTo: function(offset) {
                    return new Element.Offset(
                            this.left - offset.left,
                            this.top - offset.top
                    );
                },

                /**
                 *  Element.Offset#inspect() -> String
                 *
                 *  Returns a debug-friendly representation of the offset.
                 **/
                inspect: function() {
                    return "#<Element.Offset left: #{left} top: #{top}>".interpolate(this);
                },

                /**
                 *  Element.Offset#toString() -> String
                 **/
                toString: function() {
                    return "[#{left}, #{top}]".interpolate(this);
                },

                /**
                 *  Element.Offset#toArray() -> Array
                 *
                 *  Returns an array representation fo the offset in [x, y] format.
                 **/
                toArray: function() {
                    return [this.left, this.top];
                }
            });

    /**
     *  Element.getLayout(@element[, preCompute = false]) -> Element.Layout
     *  - element (Element): The element to be measured.
     *  - preCompute (Boolean): Whether to compute all values at once. Default
     *    is `false`.
     *
     *  Returns an instance of [[Element.Layout]] for measuring an element's
     *  dimensions.
     *
     *  Note that this method returns a _new_ `Element.Layout` object each time
     *  it's called. If you want to take advantage of measurement caching,
     *  retain a reference to one `Element.Layout` object, rather than calling
     *  `Element.getLayout` whenever you need a measurement. You should call
     *  `Element.getLayout` again only when the values in an existing
     *  `Element.Layout` object have become outdated.
     *
     *  Remember that instances of `Element.Layout` compute values the first
     *  time they're asked for and remember those values for later retrieval.
     *  If you want to compute all an element's measurements at once, pass
     *
     *  ##### Examples
     *
     *      var layout = $('troz').getLayout();
     *
     *      layout.get('width');  //-> 150
     *      layout.get('height'); //-> 500
     *      layout.get('padding-left');  //-> 10
     *      layout.get('margin-left');   //-> 25
     *      layout.get('border-top');    //-> 5
     *      layout.get('border-bottom'); //-> 5
     *
     *      // Won't re-compute width; remembers value from first time.
     *      layout.get('width');  //-> 150
     *
     *      // Composite values obtained by adding together other properties;
     *      // will re-use any values we've already looked up above.
     *      layout.get('padding-box-width'); //-> 170
     *      layout.get('border-box-height'); //-> 510
     *
     *  ##### Caveats
     *
     *  * Instances of `Element.Layout` can measure the dimensions of an
     *    element hidden with CSS (`display: none`), but _only_ if its parent
     *    element is visible.
     **/
    function getLayout(element, preCompute) {
        return new Element.Layout(element, preCompute);
    }

    /**
     *  Element.measure(@element, property) -> Number
     *
     *  Gives the pixel value of `element`'s dimension specified by
     *  `property`.
     *
     *  Useful for one-off measurements of elements. If you find yourself
     *  calling this method frequently over short spans of code, you might want
     *  to call [[Element.getLayout]] and operate on the [[Element.Layout]]
     *  object itself (thereby taking advantage of measurement caching).
     *
     *  ##### Examples
     *
     *      $('troz').measure('width'); //-> 150
     *      $('troz').measure('border-top'); //-> 5
     *      $('troz').measure('top'); //-> 226
     *
     *  ##### Caveats
     *
     *  * `Element.measure` can measure the dimensions of an element hidden with
     *    CSS (`display: none`), but _only_ if its parent element is visible.
     **/
    function measure(element, property) {
        return $(element).getLayout().get(property);
    }

    /**
     *  Element.getDimensions(@element) -> Object
     *
     *  Finds the computed width and height of `element` and returns them as
     *  key/value pairs of an object.
     *
     *  For backwards-compatibility, these dimensions represent the dimensions
     *  of the element's "border box" (including CSS padding and border). This
     *  is equivalent to the built-in `offsetWidth` and `offsetHeight`
     *  browser properties.
     *
     *  Note that all values are returned as _numbers only_ although they are
     *  _expressed in pixels_.
     *
     *  ##### Caveats
     *
     *  * If the element is hidden via `display: none` in CSS, this method will
     *    attempt to measure the element by temporarily removing that CSS and
     *    applying `visibility: hidden` and `position: absolute`. This gives
     *    the element dimensions without making it visible or affecting the
     *    positioning of surrounding elements &mdash; but may not give accurate
     *    results in some cases. [[Element.measure]] is designed to give more
     *    accurate results.
     *
     *  * In order to avoid calling the method twice, you should consider
     *    caching the returned values in a variable, as shown in the example
     *    below.
     *
     *  * For more complex use cases, use [[Element.measure]], which is able
     *    to measure many different aspects of an element's dimensions and
     *    offsets.
     *
     *  ##### Examples
     *
     *      language: html
     *      <div id="rectangle" style="font-size: 10px; width: 20em; height: 10em"></div>
     *
     *  Then:
     *
     *      var dimensions = $('rectangle').getDimensions();
     *      // -> {width: 200, height: 100}
     *
     *      dimensions.width;
     *      // -> 200
     *
     *      dimensions.height;
     *      // -> 100
     **/
    function getDimensions(element) {
        element = $(element);
        var display = Element.getStyle(element, 'display');

        if (display && display !== 'none') {
            return { width: element.offsetWidth, height: element.offsetHeight };
        }

        // All *Width and *Height properties give 0 on elements with
        // `display: none`, so show the element temporarily.
        var style = element.style;
        var originalStyles = {
            visibility: style.visibility,
            position:   style.position,
            display:    style.display
        };

        var newStyles = {
            visibility: 'hidden',
            display:    'block'
        };

        // Switching `fixed` to `absolute` causes issues in Safari.
        if (originalStyles.position !== 'fixed')
            newStyles.position = 'absolute';

        Element.setStyle(element, newStyles);

        var dimensions = {
            width:  element.offsetWidth,
            height: element.offsetHeight
        };

        Element.setStyle(element, originalStyles);

        return dimensions;
    }

    /**
     *  Element.getOffsetParent(@element) -> Element
     *
     *  Returns `element`'s closest _positioned_ ancestor. If none is found, the
     *  `body` element is returned.
     **/
    function getOffsetParent(element) {
        element = $(element);

        // For unusual cases like these, we standardize on returning the BODY
        // element as the offset parent.
        if (isDocument(element) || isDetached(element) || isBody(element) || isHtml(element))
            return $(document.body);

        // IE reports offset parent incorrectly for inline elements.
        var isInline = (Element.getStyle(element, 'display') === 'inline');
        if (!isInline && element.offsetParent) return $(element.offsetParent);

        while ((element = element.parentNode) && element !== document.body) {
            if (Element.getStyle(element, 'position') !== 'static') {
                return isHtml(element) ? $(document.body) : $(element);
            }
        }

        return $(document.body);
    }


    /**
     *  Element.cumulativeOffset(@element) -> Element.Offset
     *
     *  Returns the offsets of `element` from the top left corner of the
     *  document.
     **/
    function cumulativeOffset(element) {
        element = $(element);
        var valueT = 0, valueL = 0;
        if (element.parentNode) {
            do {
                valueT += element.offsetTop || 0;
                valueL += element.offsetLeft || 0;
                element = element.offsetParent;
            } while (element);
        }
        return new Element.Offset(valueL, valueT);
    }

    /**
     *  Element.positionedOffset(@element) -> Element.Offset
     *
     *  Returns `element`'s offset relative to its closest positioned ancestor
     *  (the element that would be returned by [[Element.getOffsetParent]]).
     **/
    function positionedOffset(element) {
        element = $(element);

        // Account for the margin of the element.
        var layout = element.getLayout();

        var valueT = 0, valueL = 0;
        do {
            valueT += element.offsetTop || 0;
            valueL += element.offsetLeft || 0;
            element = element.offsetParent;
            if (element) {
                if (isBody(element)) break;
                var p = Element.getStyle(element, 'position');
                if (p !== 'static') break;
            }
        } while (element);

        valueL -= layout.get('margin-top');
        valueT -= layout.get('margin-left');

        return new Element.Offset(valueL, valueT);
    }

    /**
     *  Element.cumulativeScrollOffset(@element) -> Element.Offset
     *
     *  Calculates the cumulative scroll offset of an element in nested
     *  scrolling containers.
     **/
    function cumulativeScrollOffset(element) {
        var valueT = 0, valueL = 0;
        do {
            valueT += element.scrollTop || 0;
            valueL += element.scrollLeft || 0;
            element = element.parentNode;
        } while (element);
        return new Element.Offset(valueL, valueT);
    }

    /**
     *  Element.viewportOffset(@element) -> Element.Offset
     *
     *  Returns the X/Y coordinates of element relative to the viewport.
     **/
    function viewportOffset(forElement) {
        element = $(element);
        var valueT = 0, valueL = 0, docBody = document.body;

        var element = forElement;
        do {
            valueT += element.offsetTop || 0;
            valueL += element.offsetLeft || 0;
            // Safari fix
            if (element.offsetParent == docBody &&
                    Element.getStyle(element, 'position') == 'absolute') break;
        } while (element = element.offsetParent);

        element = forElement;
        do {
            // Opera < 9.5 sets scrollTop/Left on both HTML and BODY elements.
            // Other browsers set it only on the HTML element. The BODY element
            // can be skipped since its scrollTop/Left should always be 0.
            if (!Prototype.Browser.Opera || element == docBody) {
                valueT -= element.scrollTop || 0;
                valueL -= element.scrollLeft || 0;
            }
        } while (element = element.parentNode);
        return new Element.Offset(valueL, valueT);
    }

    /**
     *  Element.absolutize(@element) -> Element
     *
     *  Turns `element` into an absolutely-positioned element _without_
     *  changing its position in the page layout.
     **/
    function absolutize(element) {
        element = $(element);

        if (Element.getStyle(element, 'position') === 'absolute') {
            return element;
        }

        var offsetParent = getOffsetParent(element);
        var eOffset = element.viewportOffset(),
                pOffset = offsetParent.viewportOffset();

        var offset = eOffset.relativeTo(pOffset);
        var layout = element.getLayout();

        element.store('prototype_absolutize_original_styles', {
                    left:   element.getStyle('left'),
                    top:    element.getStyle('top'),
                    width:  element.getStyle('width'),
                    height: element.getStyle('height')
                });

        element.setStyle({
                    position: 'absolute',
                    top:    offset.top + 'px',
                    left:   offset.left + 'px',
                    width:  layout.get('width') + 'px',
                    height: layout.get('height') + 'px'
                });

        return element;
    }

    /**
     *  Element.relativize(@element) -> Element
     *
     *  Turns `element` into a relatively-positioned element without changing
     *  its position in the page layout.
     *
     *  Used to undo a call to [[Element.absolutize]].
     **/
    function relativize(element) {
        element = $(element);
        if (Element.getStyle(element, 'position') === 'relative') {
            return element;
        }

        // Restore the original styles as captured by Element#absolutize.
        var originalStyles =
                element.retrieve('prototype_absolutize_original_styles');

        if (originalStyles) element.setStyle(originalStyles);
        return element;
    }

    if (Prototype.Browser.IE) {
        // IE doesn't report offsets correctly for static elements, so we change them
        // to "relative" to get the values, then change them back.
        getOffsetParent = getOffsetParent.wrap(
                function(proceed, element) {
                    element = $(element);

                    // For unusual cases like these, we standardize on returning the BODY
                    // element as the offset parent.
                    if (isDocument(element) || isDetached(element) || isBody(element) || isHtml(element))
                        return $(document.body);

                    var position = element.getStyle('position');
                    if (position !== 'static') return proceed(element);

                    element.setStyle({ position: 'relative' });
                    var value = proceed(element);
                    element.setStyle({ position: position });
                    return value;
                }
        );

        positionedOffset = positionedOffset.wrap(function(proceed, element) {
            element = $(element);
            if (!element.parentNode) return new Element.Offset(0, 0);
            var position = element.getStyle('position');
            if (position !== 'static') return proceed(element);

            // Trigger hasLayout on the offset parent so that IE6 reports
            // accurate offsetTop and offsetLeft values for position: fixed.
            var offsetParent = element.getOffsetParent();
            if (offsetParent && offsetParent.getStyle('position') === 'fixed')
                hasLayout(offsetParent);

            element.setStyle({ position: 'relative' });
            var value = proceed(element);
            element.setStyle({ position: position });
            return value;
        });
    } else if (Prototype.Browser.Webkit) {
        // Safari returns margins on body which is incorrect if the child is absolutely
        // positioned.  For performance reasons, redefine Element#cumulativeOffset for
        // KHTML/WebKit only.
        cumulativeOffset = function(element) {
            element = $(element);
            var valueT = 0, valueL = 0;
            do {
                valueT += element.offsetTop || 0;
                valueL += element.offsetLeft || 0;
                if (element.offsetParent == document.body)
                    if (Element.getStyle(element, 'position') == 'absolute') break;

                element = element.offsetParent;
            } while (element);

            return new Element.Offset(valueL, valueT);
        };
    }


    Element.addMethods({
                getLayout:              getLayout,
                measure:                measure,
                getDimensions:          getDimensions,
                getOffsetParent:        getOffsetParent,
                cumulativeOffset:       cumulativeOffset,
                positionedOffset:       positionedOffset,
                cumulativeScrollOffset: cumulativeScrollOffset,
                viewportOffset:         viewportOffset,
                absolutize:             absolutize,
                relativize:             relativize
            });

    function isBody(element) {
        return element.nodeName.toUpperCase() === 'BODY';
    }

    function isHtml(element) {
        return element.nodeName.toUpperCase() === 'HTML';
    }

    function isDocument(element) {
        return element.nodeType === Node.DOCUMENT_NODE;
    }

    function isDetached(element) {
        return element !== document.body &&
                !Element.descendantOf(element, document.body);
    }
})();

/** section: DOM, related to: Prototype.Selector
 *  $$(cssRule...) -> [Element...]
 *
 *  Takes an arbitrary number of CSS selectors (strings) and returns a document-order
 *  array of extended DOM elements that match any of them.
 *
 *  Sometimes the usual tools from your DOM arsenal -- `document.getElementById` encapsulated
 *  by [[$]], `getElementsByTagName` and even Prototype's very own `getElementsByClassName`
 *  extensions -- just aren't enough to quickly find elements or collections of elements.
 *  If you know the DOM tree structure, you can simply resort to CSS selectors to get
 *  the job done.
 *
 *  ##### Quick examples
 *
 *      $$('div');
 *      // -> all DIVs in the document. Same as document.getElementsByTagName('div').
 *      // Nice addition, the elements you're getting back are already extended!
 *
 *      $$('#contents');
 *      // -> same as $('contents'), only it returns an array anyway (even though IDs must
 *      // be unique within a document).
 *
 *      $$('li.faux');
 *      // -> all LI elements with class 'faux'
 *
 *  The [[$$]] function searches the entire document. For selector queries on more specific
 *  sections of a document, use [[Element.select]].
 *
 *  ##### Supported CSS syntax
 *
 *  The [[$$]] function does not rely on the browser's internal CSS parsing capabilities
 *  (otherwise, we'd be in cross-browser trouble...), and therefore offers a consistent
 *  set of selectors across all supported browsers.
 *
 *  ###### Supported in v1.5.0
 *
 *  * Type selector: tag names, as in `div`.
 *  * Descendant selector: the space(s) between other selectors, as in `#a li`.
 *  * Attribute selectors: the full CSS 2.1 set of `[attr]`, `[attr=value]`, `[attr~=value]`
 *  and `[attr|=value]`. It also supports `[attr!=value]`. If the value you're matching
 *  against includes a space, be sure to enclose the value in quotation marks (`[title="Hello World!"]`).
 *  * Class selector: CSS class names, as in `.highlighted` or `.example.wrong`.
 *  * ID selector: as in `#item1`.
 *
 *  ###### Supported from v1.5.1
 *
 *  Virtually all of [CSS3](http://www.w3.org/TR/2001/CR-css3-selectors-20011113/#selectors)
 *  is supported, with the exception of pseudo-elements (like `::first-letter`) and some
 *  pseudo-classes (like `:hover`). Some examples of new selectors that can be used in 1.5.1:
 *
 *  * Child selector: selects immediate descendants, as in `#a > li`.
 *  * Attribute selectors: all attribute operators are supported, including `~=` (matches
 *  part of a space-delimited attribute value, like `rel` or `class`); `^=` (matches the
 *  beginning of a value); `$=` (matches the end of a value); and `*=` (matches any part
 *  of the value).
 *  * The `:not` pseudo-class, as in `#a *:not(li)` (matches all descendants of `#a` that
 *  aren't LIs).
 *  * All the `:nth`, `:first`, and `:last` pseudo-classes. Examples include `tr:nth-child(even)`
 *  (all even table rows), `li:first-child` (the first item in any list), or `p:nth-last-of-type(3)`
 *  (the third-to-last paragraph on the page).
 *  * The `:empty` pseudo-class (for selecting elements without children or text content).
 *  * The `:enabled`, `:disabled`, and `:checked` pseudo-classes (for use with form controls).
 *
 *  ##### Examples
 *
 *      $$('#contents a[rel]');
 *      // -> all links inside the element of ID "contents" with a rel attribute
 *
 *      $$('a[href="#"]');
 *      // -> all links with a href attribute of value "#" (eyeew!)
 *
 *      $$('#navbar a', '#sidebar a');
 *      // -> all links within the elements of ID "navbar" or "sidebar"
 *
 *  **With version 1.5.1 and above** you can do various types of advanced selectors:
 *
 *      $$('a:not([rel~=nofollow])');
 *      // -> all links, excluding those whose rel attribute contains the word "nofollow"
 *
 *      $$('table tbody > tr:nth-child(even)');
 *      // -> all even rows within all table bodies
 *
 *      $$('div:empty');
 *      // -> all DIVs without content (i.e., whitespace-only)
 **/
window.$$ = function() {
    var expression = $A(arguments).join(', ');
    return Prototype.Selector.select(expression, document);
};

/**
 * Prototype.Selector
 *
 * A namespace that acts as a wrapper around
 * the choosen selector engine (Sizzle by default).
 *
 **/
Prototype.Selector = (function() {

    /**
     *  Prototype.Selector.select(expression[, root = document]) -> [Element...]
     *  - expression (String): A CSS selector.
     *  - root (Element | document): A "scope" to search within. All results will
     *    be descendants of this node.
     *
     *  Searches `root` for elements that match the provided CSS selector and returns an
     *  array of extended [[Element]] objects.
     **/
    function select() {
        throw new Error('Method "Prototype.Selector.select" must be defined.');
    }

    /**
     *  Prototype.Selector.match(element, expression) -> Boolean
     *  - element (Element): a DOM element.
     *  - expression (String): A CSS selector.
     *
     *  Tests whether `element` matches the CSS selector.
     **/
    function match() {
        throw new Error('Method "Prototype.Selector.match" must be defined.');
    }

    /**
     *  Prototype.Selector.find(elements, expression[, index = 0]) -> Element
     *  - elements (Enumerable): a collection of DOM elements.
     *  - expression (String): A CSS selector.
     *  - index: Numeric index of the match to return, defaults to 0.
     *
     *  Filters the given collection of elements with `expression` and returns the
     *  first matching element (or the `index`th matching element if `index` is
     *  specified).
     **/
    function find(elements, expression, index) {
        index = index || 0;
        var match = Prototype.Selector.match, length = elements.length, matchIndex = 0, i;

        for (i = 0; i < length; i++) {
            if (match(elements[i], expression) && index == matchIndex++) {
                return Element.extend(elements[i]);
            }
        }
    }

    /**
     *  Prototype.Selector.extendElements(elements) -> Enumerable
     *  - elements (Enumerable): a collection of DOM elements.
     *
     *  If necessary, extends the elements contained in `elements`
     *  and returns `elements` untouched. This is provided as a
     *  convenience method for selector engine wrapper implementors.
     **/
    function extendElements(elements) {
        for (var i = 0, length = elements.length; i < length; i++) {
            Element.extend(elements[i]);
        }
        return elements;
    }

    /** alias of: Element.extend
     *  Prototype.Selector.extendElement(element) -> Element
     **/

    var K = Prototype.K;

    return {
        select: select,
        match: match,
        find: find,
        extendElements: (Element.extend === K) ? K : extendElements,
        extendElement: Element.extend
    };
})();

Prototype._original_property = window.Sizzle;
/*!
 * Sizzle CSS Selector Engine - v1.0
 *  Copyright 2009, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 */
(function() {

    var chunker = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^[\]]*\]|['"][^'"]*['"]|[^[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,
            done = 0,
            toString = Object.prototype.toString,
            hasDuplicate = false,
            baseHasDuplicate = true;

// Here we check if the JavaScript engine is using some sort of
// optimization where it does not always call our comparision
// function. If that is the case, discard the hasDuplicate value.
//   Thus far that includes Google Chrome.
    [0, 0].sort(function() {
        baseHasDuplicate = false;
        return 0;
    });

    var Sizzle = function(selector, context, results, seed) {
        results = results || [];
        var origContext = context = context || document;

        if (context.nodeType !== 1 && context.nodeType !== 9) {
            return [];
        }

        if (!selector || typeof selector !== "string") {
            return results;
        }

        var parts = [], m, set, checkSet, check, mode, extra, prune = true, contextXML = isXML(context),
                soFar = selector;

        // Reset the position of the chunker regexp (start from head)
        while ((chunker.exec(""),m = chunker.exec(soFar)) !== null) {
            soFar = m[3];

            parts.push(m[1]);

            if (m[2]) {
                extra = m[3];
                break;
            }
        }

        if (parts.length > 1 && origPOS.exec(selector)) {
            if (parts.length === 2 && Expr.relative[ parts[0] ]) {
                set = posProcess(parts[0] + parts[1], context);
            } else {
                set = Expr.relative[ parts[0] ] ?
                        [ context ] :
                        Sizzle(parts.shift(), context);

                while (parts.length) {
                    selector = parts.shift();

                    if (Expr.relative[ selector ])
                        selector += parts.shift();

                    set = posProcess(selector, set);
                }
            }
        } else {
            // Take a shortcut and set the context if the root selector is an ID
            // (but not if it'll be faster if the inner selector is an ID)
            if (!seed && parts.length > 1 && context.nodeType === 9 && !contextXML &&
                    Expr.match.ID.test(parts[0]) && !Expr.match.ID.test(parts[parts.length - 1])) {
                var ret = Sizzle.find(parts.shift(), context, contextXML);
                context = ret.expr ? Sizzle.filter(ret.expr, ret.set)[0] : ret.set[0];
            }

            if (context) {
                var ret = seed ?
                { expr: parts.pop(), set: makeArray(seed) } :
                        Sizzle.find(parts.pop(), parts.length === 1 && (parts[0] === "~" || parts[0] === "+") && context.parentNode ? context.parentNode : context, contextXML);
                set = ret.expr ? Sizzle.filter(ret.expr, ret.set) : ret.set;

                if (parts.length > 0) {
                    checkSet = makeArray(set);
                } else {
                    prune = false;
                }

                while (parts.length) {
                    var cur = parts.pop(), pop = cur;

                    if (!Expr.relative[ cur ]) {
                        cur = "";
                    } else {
                        pop = parts.pop();
                    }

                    if (pop == null) {
                        pop = context;
                    }

                    Expr.relative[ cur ](checkSet, pop, contextXML);
                }
            } else {
                checkSet = parts = [];
            }
        }

        if (!checkSet) {
            checkSet = set;
        }

        if (!checkSet) {
            throw "Syntax error, unrecognized expression: " + (cur || selector);
        }

        if (toString.call(checkSet) === "[object Array]") {
            if (!prune) {
                results.push.apply(results, checkSet);
            } else if (context && context.nodeType === 1) {
                for (var i = 0; checkSet[i] != null; i++) {
                    if (checkSet[i] && (checkSet[i] === true || checkSet[i].nodeType === 1 && contains(context, checkSet[i]))) {
                        results.push(set[i]);
                    }
                }
            } else {
                for (var i = 0; checkSet[i] != null; i++) {
                    if (checkSet[i] && checkSet[i].nodeType === 1) {
                        results.push(set[i]);
                    }
                }
            }
        } else {
            makeArray(checkSet, results);
        }

        if (extra) {
            Sizzle(extra, origContext, results, seed);
            Sizzle.uniqueSort(results);
        }

        return results;
    };

    Sizzle.uniqueSort = function(results) {
        if (sortOrder) {
            hasDuplicate = baseHasDuplicate;
            results.sort(sortOrder);

            if (hasDuplicate) {
                for (var i = 1; i < results.length; i++) {
                    if (results[i] === results[i - 1]) {
                        results.splice(i--, 1);
                    }
                }
            }
        }

        return results;
    };

    Sizzle.matches = function(expr, set) {
        return Sizzle(expr, null, null, set);
    };

    Sizzle.find = function(expr, context, isXML) {
        var set, match;

        if (!expr) {
            return [];
        }

        for (var i = 0, l = Expr.order.length; i < l; i++) {
            var type = Expr.order[i], match;

            if ((match = Expr.leftMatch[ type ].exec(expr))) {
                var left = match[1];
                match.splice(1, 1);

                if (left.substr(left.length - 1) !== "\\") {
                    match[1] = (match[1] || "").replace(/\\/g, "");
                    set = Expr.find[ type ](match, context, isXML);
                    if (set != null) {
                        expr = expr.replace(Expr.match[ type ], "");
                        break;
                    }
                }
            }
        }

        if (!set) {
            set = context.getElementsByTagName("*");
        }

        return {set: set, expr: expr};
    };

    Sizzle.filter = function(expr, set, inplace, not) {
        var old = expr, result = [], curLoop = set, match, anyFound,
                isXMLFilter = set && set[0] && isXML(set[0]);

        while (expr && set.length) {
            for (var type in Expr.filter) {
                if ((match = Expr.match[ type ].exec(expr)) != null) {
                    var filter = Expr.filter[ type ], found, item;
                    anyFound = false;

                    if (curLoop == result) {
                        result = [];
                    }

                    if (Expr.preFilter[ type ]) {
                        match = Expr.preFilter[ type ](match, curLoop, inplace, result, not, isXMLFilter);

                        if (!match) {
                            anyFound = found = true;
                        } else if (match === true) {
                            continue;
                        }
                    }

                    if (match) {
                        for (var i = 0; (item = curLoop[i]) != null; i++) {
                            if (item) {
                                found = filter(item, match, i, curLoop);
                                var pass = not ^ !!found;

                                if (inplace && found != null) {
                                    if (pass) {
                                        anyFound = true;
                                    } else {
                                        curLoop[i] = false;
                                    }
                                } else if (pass) {
                                    result.push(item);
                                    anyFound = true;
                                }
                            }
                        }
                    }

                    if (found !== undefined) {
                        if (!inplace) {
                            curLoop = result;
                        }

                        expr = expr.replace(Expr.match[ type ], "");

                        if (!anyFound) {
                            return [];
                        }

                        break;
                    }
                }
            }

            // Improper expression
            if (expr == old) {
                if (anyFound == null) {
                    throw "Syntax error, unrecognized expression: " + expr;
                } else {
                    break;
                }
            }

            old = expr;
        }

        return curLoop;
    };

    var Expr = Sizzle.selectors = {
        order: [ "ID", "NAME", "TAG" ],
        match: {
            ID: /#((?:[\w\u00c0-\uFFFF-]|\\.)+)/,
            CLASS: /\.((?:[\w\u00c0-\uFFFF-]|\\.)+)/,
            NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF-]|\\.)+)['"]*\]/,
            ATTR: /\[\s*((?:[\w\u00c0-\uFFFF-]|\\.)+)\s*(?:(\S?=)\s*(['"]*)(.*?)\3|)\s*\]/,
            TAG: /^((?:[\w\u00c0-\uFFFF\*-]|\\.)+)/,
            CHILD: /:(only|nth|last|first)-child(?:\((even|odd|[\dn+-]*)\))?/,
            POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^-]|$)/,
            PSEUDO: /:((?:[\w\u00c0-\uFFFF-]|\\.)+)(?:\((['"]*)((?:\([^\)]+\)|[^\2\(\)]*)+)\2\))?/
        },
        leftMatch: {},
        attrMap: {
            "class": "className",
            "for": "htmlFor"
        },
        attrHandle: {
            href: function(elem) {
                return elem.getAttribute("href");
            }
        },
        relative: {
            "+": function(checkSet, part, isXML) {
                var isPartStr = typeof part === "string",
                        isTag = isPartStr && !/\W/.test(part),
                        isPartStrNotTag = isPartStr && !isTag;

                if (isTag && !isXML) {
                    part = part.toUpperCase();
                }

                for (var i = 0, l = checkSet.length, elem; i < l; i++) {
                    if ((elem = checkSet[i])) {
                        while ((elem = elem.previousSibling) && elem.nodeType !== 1) {
                        }

                        checkSet[i] = isPartStrNotTag || elem && elem.nodeName === part ?
                                elem || false :
                                elem === part;
                    }
                }

                if (isPartStrNotTag) {
                    Sizzle.filter(part, checkSet, true);
                }
            },
            ">": function(checkSet, part, isXML) {
                var isPartStr = typeof part === "string";

                if (isPartStr && !/\W/.test(part)) {
                    part = isXML ? part : part.toUpperCase();

                    for (var i = 0, l = checkSet.length; i < l; i++) {
                        var elem = checkSet[i];
                        if (elem) {
                            var parent = elem.parentNode;
                            checkSet[i] = parent.nodeName === part ? parent : false;
                        }
                    }
                } else {
                    for (var i = 0, l = checkSet.length; i < l; i++) {
                        var elem = checkSet[i];
                        if (elem) {
                            checkSet[i] = isPartStr ?
                                    elem.parentNode :
                                    elem.parentNode === part;
                        }
                    }

                    if (isPartStr) {
                        Sizzle.filter(part, checkSet, true);
                    }
                }
            },
            "": function(checkSet, part, isXML) {
                var doneName = done++, checkFn = dirCheck;

                if (!/\W/.test(part)) {
                    var nodeCheck = part = isXML ? part : part.toUpperCase();
                    checkFn = dirNodeCheck;
                }

                checkFn("parentNode", part, doneName, checkSet, nodeCheck, isXML);
            },
            "~": function(checkSet, part, isXML) {
                var doneName = done++, checkFn = dirCheck;

                if (typeof part === "string" && !/\W/.test(part)) {
                    var nodeCheck = part = isXML ? part : part.toUpperCase();
                    checkFn = dirNodeCheck;
                }

                checkFn("previousSibling", part, doneName, checkSet, nodeCheck, isXML);
            }
        },
        find: {
            ID: function(match, context, isXML) {
                if (typeof context.getElementById !== "undefined" && !isXML) {
                    var m = context.getElementById(match[1]);
                    return m ? [m] : [];
                }
            },
            NAME: function(match, context, isXML) {
                if (typeof context.getElementsByName !== "undefined") {
                    var ret = [], results = context.getElementsByName(match[1]);

                    for (var i = 0, l = results.length; i < l; i++) {
                        if (results[i].getAttribute("name") === match[1]) {
                            ret.push(results[i]);
                        }
                    }

                    return ret.length === 0 ? null : ret;
                }
            },
            TAG: function(match, context) {
                return context.getElementsByTagName(match[1]);
            }
        },
        preFilter: {
            CLASS: function(match, curLoop, inplace, result, not, isXML) {
                match = " " + match[1].replace(/\\/g, "") + " ";

                if (isXML) {
                    return match;
                }

                for (var i = 0, elem; (elem = curLoop[i]) != null; i++) {
                    if (elem) {
                        if (not ^ (elem.className && (" " + elem.className + " ").indexOf(match) >= 0)) {
                            if (!inplace)
                                result.push(elem);
                        } else if (inplace) {
                            curLoop[i] = false;
                        }
                    }
                }

                return false;
            },
            ID: function(match) {
                return match[1].replace(/\\/g, "");
            },
            TAG: function(match, curLoop) {
                for (var i = 0; curLoop[i] === false; i++) {
                }
                return curLoop[i] && isXML(curLoop[i]) ? match[1] : match[1].toUpperCase();
            },
            CHILD: function(match) {
                if (match[1] == "nth") {
                    // parse equations like 'even', 'odd', '5', '2n', '3n+2', '4n-1', '-n+6'
                    var test = /(-?)(\d*)n((?:\+|-)?\d*)/.exec(
                            match[2] == "even" && "2n" || match[2] == "odd" && "2n+1" ||
                                    !/\D/.test(match[2]) && "0n+" + match[2] || match[2]);

                    // calculate the numbers (first)n+(last) including if they are negative
                    match[2] = (test[1] + (test[2] || 1)) - 0;
                    match[3] = test[3] - 0;
                }

                // TODO: Move to normal caching system
                match[0] = done++;

                return match;
            },
            ATTR: function(match, curLoop, inplace, result, not, isXML) {
                var name = match[1].replace(/\\/g, "");

                if (!isXML && Expr.attrMap[name]) {
                    match[1] = Expr.attrMap[name];
                }

                if (match[2] === "~=") {
                    match[4] = " " + match[4] + " ";
                }

                return match;
            },
            PSEUDO: function(match, curLoop, inplace, result, not) {
                if (match[1] === "not") {
                    // If we're dealing with a complex expression, or a simple one
                    if (( chunker.exec(match[3]) || "" ).length > 1 || /^\w/.test(match[3])) {
                        match[3] = Sizzle(match[3], null, null, curLoop);
                    } else {
                        var ret = Sizzle.filter(match[3], curLoop, inplace, true ^ not);
                        if (!inplace) {
                            result.push.apply(result, ret);
                        }
                        return false;
                    }
                } else if (Expr.match.POS.test(match[0]) || Expr.match.CHILD.test(match[0])) {
                    return true;
                }

                return match;
            },
            POS: function(match) {
                match.unshift(true);
                return match;
            }
        },
        filters: {
            enabled: function(elem) {
                return elem.disabled === false && elem.type !== "hidden";
            },
            disabled: function(elem) {
                return elem.disabled === true;
            },
            checked: function(elem) {
                return elem.checked === true;
            },
            selected: function(elem) {
                // Accessing this property makes selected-by-default
                // options in Safari work properly
                elem.parentNode.selectedIndex;
                return elem.selected === true;
            },
            parent: function(elem) {
                return !!elem.firstChild;
            },
            empty: function(elem) {
                return !elem.firstChild;
            },
            has: function(elem, i, match) {
                return !!Sizzle(match[3], elem).length;
            },
            header: function(elem) {
                return /h\d/i.test(elem.nodeName);
            },
            text: function(elem) {
                return "text" === elem.type;
            },
            radio: function(elem) {
                return "radio" === elem.type;
            },
            checkbox: function(elem) {
                return "checkbox" === elem.type;
            },
            file: function(elem) {
                return "file" === elem.type;
            },
            password: function(elem) {
                return "password" === elem.type;
            },
            submit: function(elem) {
                return "submit" === elem.type;
            },
            image: function(elem) {
                return "image" === elem.type;
            },
            reset: function(elem) {
                return "reset" === elem.type;
            },
            button: function(elem) {
                return "button" === elem.type || elem.nodeName.toUpperCase() === "BUTTON";
            },
            input: function(elem) {
                return /input|select|textarea|button/i.test(elem.nodeName);
            }
        },
        setFilters: {
            first: function(elem, i) {
                return i === 0;
            },
            last: function(elem, i, match, array) {
                return i === array.length - 1;
            },
            even: function(elem, i) {
                return i % 2 === 0;
            },
            odd: function(elem, i) {
                return i % 2 === 1;
            },
            lt: function(elem, i, match) {
                return i < match[3] - 0;
            },
            gt: function(elem, i, match) {
                return i > match[3] - 0;
            },
            nth: function(elem, i, match) {
                return match[3] - 0 == i;
            },
            eq: function(elem, i, match) {
                return match[3] - 0 == i;
            }
        },
        filter: {
            PSEUDO: function(elem, match, i, array) {
                var name = match[1], filter = Expr.filters[ name ];

                if (filter) {
                    return filter(elem, i, match, array);
                } else if (name === "contains") {
                    return (elem.textContent || elem.innerText || "").indexOf(match[3]) >= 0;
                } else if (name === "not") {
                    var not = match[3];

                    for (var i = 0, l = not.length; i < l; i++) {
                        if (not[i] === elem) {
                            return false;
                        }
                    }

                    return true;
                }
            },
            CHILD: function(elem, match) {
                var type = match[1], node = elem;
                switch (type) {
                    case 'only':
                    case 'first':
                        while ((node = node.previousSibling)) {
                            if (node.nodeType === 1) return false;
                        }
                        if (type == 'first') return true;
                        node = elem;
                    case 'last':
                        while ((node = node.nextSibling)) {
                            if (node.nodeType === 1) return false;
                        }
                        return true;
                    case 'nth':
                        var first = match[2], last = match[3];

                        if (first == 1 && last == 0) {
                            return true;
                        }

                        var doneName = match[0],
                                parent = elem.parentNode;

                        if (parent && (parent.sizcache !== doneName || !elem.nodeIndex)) {
                            var count = 0;
                            for (node = parent.firstChild; node; node = node.nextSibling) {
                                if (node.nodeType === 1) {
                                    node.nodeIndex = ++count;
                                }
                            }
                            parent.sizcache = doneName;
                        }

                        var diff = elem.nodeIndex - last;
                        if (first == 0) {
                            return diff == 0;
                        } else {
                            return ( diff % first == 0 && diff / first >= 0 );
                        }
                }
            },
            ID: function(elem, match) {
                return elem.nodeType === 1 && elem.getAttribute("id") === match;
            },
            TAG: function(elem, match) {
                return (match === "*" && elem.nodeType === 1) || elem.nodeName === match;
            },
            CLASS: function(elem, match) {
                return (" " + (elem.className || elem.getAttribute("class")) + " ")
                        .indexOf(match) > -1;
            },
            ATTR: function(elem, match) {
                var name = match[1],
                        result = Expr.attrHandle[ name ] ?
                                Expr.attrHandle[ name ](elem) :
                                elem[ name ] != null ?
                                        elem[ name ] :
                                        elem.getAttribute(name),
                        value = result + "",
                        type = match[2],
                        check = match[4];

                return result == null ?
                        type === "!=" :
                        type === "=" ?
                                value === check :
                                type === "*=" ?
                                        value.indexOf(check) >= 0 :
                                        type === "~=" ?
                                                (" " + value + " ").indexOf(check) >= 0 :
                                                !check ?
                                                        value && result !== false :
                                                        type === "!=" ?
                                                                value != check :
                                                                type === "^=" ?
                                                                        value.indexOf(check) === 0 :
                                                                        type === "$=" ?
                                                                                value.substr(value.length - check.length) === check :
                                                                                type === "|=" ?
                                                                                        value === check || value.substr(0, check.length + 1) === check + "-" :
                                                                                        false;
            },
            POS: function(elem, match, i, array) {
                var name = match[2], filter = Expr.setFilters[ name ];

                if (filter) {
                    return filter(elem, i, match, array);
                }
            }
        }
    };

    var origPOS = Expr.match.POS;

    for (var type in Expr.match) {
        Expr.match[ type ] = new RegExp(Expr.match[ type ].source + /(?![^\[]*\])(?![^\(]*\))/.source);
        Expr.leftMatch[ type ] = new RegExp(/(^(?:.|\r|\n)*?)/.source + Expr.match[ type ].source);
    }

    var makeArray = function(array, results) {
        array = Array.prototype.slice.call(array, 0);

        if (results) {
            results.push.apply(results, array);
            return results;
        }

        return array;
    };

// Perform a simple check to determine if the browser is capable of
// converting a NodeList to an array using builtin methods.
    try {
        Array.prototype.slice.call(document.documentElement.childNodes, 0);

// Provide a fallback method if it does not work
    } catch(e) {
        makeArray = function(array, results) {
            var ret = results || [];

            if (toString.call(array) === "[object Array]") {
                Array.prototype.push.apply(ret, array);
            } else {
                if (typeof array.length === "number") {
                    for (var i = 0, l = array.length; i < l; i++) {
                        ret.push(array[i]);
                    }
                } else {
                    for (var i = 0; array[i]; i++) {
                        ret.push(array[i]);
                    }
                }
            }

            return ret;
        };
    }

    var sortOrder;

    if (document.documentElement.compareDocumentPosition) {
        sortOrder = function(a, b) {
            if (!a.compareDocumentPosition || !b.compareDocumentPosition) {
                if (a == b) {
                    hasDuplicate = true;
                }
                return 0;
            }

            var ret = a.compareDocumentPosition(b) & 4 ? -1 : a === b ? 0 : 1;
            if (ret === 0) {
                hasDuplicate = true;
            }
            return ret;
        };
    } else if ("sourceIndex" in document.documentElement) {
        sortOrder = function(a, b) {
            if (!a.sourceIndex || !b.sourceIndex) {
                if (a == b) {
                    hasDuplicate = true;
                }
                return 0;
            }

            var ret = a.sourceIndex - b.sourceIndex;
            if (ret === 0) {
                hasDuplicate = true;
            }
            return ret;
        };
    } else if (document.createRange) {
        sortOrder = function(a, b) {
            if (!a.ownerDocument || !b.ownerDocument) {
                if (a == b) {
                    hasDuplicate = true;
                }
                return 0;
            }

            var aRange = a.ownerDocument.createRange(), bRange = b.ownerDocument.createRange();
            aRange.setStart(a, 0);
            aRange.setEnd(a, 0);
            bRange.setStart(b, 0);
            bRange.setEnd(b, 0);
            var ret = aRange.compareBoundaryPoints(Range.START_TO_END, bRange);
            if (ret === 0) {
                hasDuplicate = true;
            }
            return ret;
        };
    }

// Check to see if the browser returns elements by name when
// querying by getElementById (and provide a workaround)
    (function() {
        // We're going to inject a fake input element with a specified name
        var form = document.createElement("div"),
                id = "script" + (new Date).getTime();
        form.innerHTML = "<a name='" + id + "'/>";

        // Inject it into the root element, check its status, and remove it quickly
        var root = document.documentElement;
        root.insertBefore(form, root.firstChild);

        // The workaround has to do additional checks after a getElementById
        // Which slows things down for other browsers (hence the branching)
        if (!!document.getElementById(id)) {
            Expr.find.ID = function(match, context, isXML) {
                if (typeof context.getElementById !== "undefined" && !isXML) {
                    var m = context.getElementById(match[1]);
                    return m ? m.id === match[1] || typeof m.getAttributeNode !== "undefined" && m.getAttributeNode("id").nodeValue === match[1] ? [m] : undefined : [];
                }
            };

            Expr.filter.ID = function(elem, match) {
                var node = typeof elem.getAttributeNode !== "undefined" && elem.getAttributeNode("id");
                return elem.nodeType === 1 && node && node.nodeValue === match;
            };
        }

        root.removeChild(form);
        root = form = null; // release memory in IE
    })();

    (function() {
        // Check to see if the browser returns only elements
        // when doing getElementsByTagName("*")

        // Create a fake element
        var div = document.createElement("div");
        div.appendChild(document.createComment(""));

        // Make sure no comments are found
        if (div.getElementsByTagName("*").length > 0) {
            Expr.find.TAG = function(match, context) {
                var results = context.getElementsByTagName(match[1]);

                // Filter out possible comments
                if (match[1] === "*") {
                    var tmp = [];

                    for (var i = 0; results[i]; i++) {
                        if (results[i].nodeType === 1) {
                            tmp.push(results[i]);
                        }
                    }

                    results = tmp;
                }

                return results;
            };
        }

        // Check to see if an attribute returns normalized href attributes
        div.innerHTML = "<a href='#'></a>";
        if (div.firstChild && typeof div.firstChild.getAttribute !== "undefined" &&
                div.firstChild.getAttribute("href") !== "#") {
            Expr.attrHandle.href = function(elem) {
                return elem.getAttribute("href", 2);
            };
        }

        div = null; // release memory in IE
    })();

    if (document.querySelectorAll) (function() {
        var oldSizzle = Sizzle, div = document.createElement("div");
        div.innerHTML = "<p class='TEST'></p>";

        // Safari can't handle uppercase or unicode characters when
        // in quirks mode.
        if (div.querySelectorAll && div.querySelectorAll(".TEST").length === 0) {
            return;
        }

        Sizzle = function(query, context, extra, seed) {
            context = context || document;

            // Only use querySelectorAll on non-XML documents
            // (ID selectors don't work in non-HTML documents)
            if (!seed && context.nodeType === 9 && !isXML(context)) {
                try {
                    return makeArray(context.querySelectorAll(query), extra);
                } catch(e) {
                }
            }

            return oldSizzle(query, context, extra, seed);
        };

        for (var prop in oldSizzle) {
            Sizzle[ prop ] = oldSizzle[ prop ];
        }

        div = null; // release memory in IE
    })();

    if (document.getElementsByClassName && document.documentElement.getElementsByClassName) (function() {
        var div = document.createElement("div");
        div.innerHTML = "<div class='test e'></div><div class='test'></div>";

        // Opera can't find a second classname (in 9.6)
        if (div.getElementsByClassName("e").length === 0)
            return;

        // Safari caches class attributes, doesn't catch changes (in 3.2)
        div.lastChild.className = "e";

        if (div.getElementsByClassName("e").length === 1)
            return;

        Expr.order.splice(1, 0, "CLASS");
        Expr.find.CLASS = function(match, context, isXML) {
            if (typeof context.getElementsByClassName !== "undefined" && !isXML) {
                return context.getElementsByClassName(match[1]);
            }
        };

        div = null; // release memory in IE
    })();

    function dirNodeCheck(dir, cur, doneName, checkSet, nodeCheck, isXML) {
        var sibDir = dir == "previousSibling" && !isXML;
        for (var i = 0, l = checkSet.length; i < l; i++) {
            var elem = checkSet[i];
            if (elem) {
                if (sibDir && elem.nodeType === 1) {
                    elem.sizcache = doneName;
                    elem.sizset = i;
                }
                elem = elem[dir];
                var match = false;

                while (elem) {
                    if (elem.sizcache === doneName) {
                        match = checkSet[elem.sizset];
                        break;
                    }

                    if (elem.nodeType === 1 && !isXML) {
                        elem.sizcache = doneName;
                        elem.sizset = i;
                    }

                    if (elem.nodeName === cur) {
                        match = elem;
                        break;
                    }

                    elem = elem[dir];
                }

                checkSet[i] = match;
            }
        }
    }

    function dirCheck(dir, cur, doneName, checkSet, nodeCheck, isXML) {
        var sibDir = dir == "previousSibling" && !isXML;
        for (var i = 0, l = checkSet.length; i < l; i++) {
            var elem = checkSet[i];
            if (elem) {
                if (sibDir && elem.nodeType === 1) {
                    elem.sizcache = doneName;
                    elem.sizset = i;
                }
                elem = elem[dir];
                var match = false;

                while (elem) {
                    if (elem.sizcache === doneName) {
                        match = checkSet[elem.sizset];
                        break;
                    }

                    if (elem.nodeType === 1) {
                        if (!isXML) {
                            elem.sizcache = doneName;
                            elem.sizset = i;
                        }
                        if (typeof cur !== "string") {
                            if (elem === cur) {
                                match = true;
                                break;
                            }

                        } else if (Sizzle.filter(cur, [elem]).length > 0) {
                            match = elem;
                            break;
                        }
                    }

                    elem = elem[dir];
                }

                checkSet[i] = match;
            }
        }
    }

    var contains = document.compareDocumentPosition ? function(a, b) {
        return a.compareDocumentPosition(b) & 16;
    } : function(a, b) {
        return a !== b && (a.contains ? a.contains(b) : true);
    };

    var isXML = function(elem) {
        return elem.nodeType === 9 && elem.documentElement.nodeName !== "HTML" ||
                !!elem.ownerDocument && elem.ownerDocument.documentElement.nodeName !== "HTML";
    };

    var posProcess = function(selector, context) {
        var tmpSet = [], later = "", match,
                root = context.nodeType ? [context] : context;

        // Position selectors must be done after the filter
        // And so must :not(positional) so we move all PSEUDOs to the end
        while ((match = Expr.match.PSEUDO.exec(selector))) {
            later += match[0];
            selector = selector.replace(Expr.match.PSEUDO, "");
        }

        selector = Expr.relative[selector] ? selector + "*" : selector;

        for (var i = 0, l = root.length; i < l; i++) {
            Sizzle(selector, root[i], tmpSet);
        }

        return Sizzle.filter(later, tmpSet);
    };

// EXPOSE

    window.Sizzle = Sizzle;

})();

;
(function(engine) {
    var extendElements = Prototype.Selector.extendElements;

    function select(selector, scope) {
        return extendElements(engine(selector, scope || document));
    }

    function match(element, selector) {
        return engine.matches(selector, [element]).length == 1;
    }

    Prototype.Selector.engine = engine;
    Prototype.Selector.select = select;
    Prototype.Selector.match = match;
})(Sizzle);

// Restore globals.
window.Sizzle = Prototype._original_property;
delete Prototype._original_property;

/** section: DOM
 * Form
 *
 *  Utilities for dealing with forms in the DOM.
 *
 *  [[Form]] is a namespace for all things form-related, packed with form
 *  manipulation and serialization goodness. While it holds methods dealing
 *  with forms as a whole, its submodule [[Form.Element]] deals with specific
 *  form controls.
 *
 *  Many of these methods are also available directly on `form` elements.
 **/

var Form = {
    /**
     *  Form.reset(@form) -> Element
     *
     *  Resets a form to its default values.
     *
     *  Example usage:
     *
     *      Form.reset('contact')
     *
     *      // equivalent:
     *      $('contact').reset()
     *
     *      // both have the same effect as pressing the reset button
     *
     *  This method allows you to programatically reset a form. It is a wrapper
     *  for the `reset()` method native to `HTMLFormElement`.
     **/
    reset: function(form) {
        form = $(form);
        form.reset();
        return form;
    },

    /**
     *  Form.serializeElements(elements[, options]) -> String | Object
     *  - elements (Array): A collection of elements to include in the
     *    serialization.
     *  - options (Object): _(Optional)_ A set of options that affect the return
     *    value of the method.
     *
     *  Serialize an array of form elements to an object or string suitable
     *  for [[Ajax]] requests.
     *
     *  As per the HTML spec, disabled fields are not included.
     *
     *  If multiple elements have the same name and we're returning an object,
     *  the value for that key in the object will be an array of the field values
     *  in the order they appeared on the array of elements.
     *
     *  The preferred method to serialize a form is [[Form.serialize]]. Refer to
     *  it for further information and examples on the `hash` option. However,
     *  with [[Form.serializeElements]] you can serialize *specific* input
     *  elements of your choice, allowing you to specify a subset of form elements
     *  that you want to serialize data from.
     *
     *  ##### The Options
     *
     *  The options allow you to control two things: What kind of return
     *  value you get (an object or a string), and whether and which `submit`
     *  fields are included in that object or string.
     *
     *  If you do not supply an `options` object _at all_, the options
     *  `{ hash: false }` are used.
     *
     *  If you supply an `options` object, it may have the following options:
     *
     *  * `hash` ([[Boolean]]): `true` to return a plain object with keys and
     *    values (not a [[Hash]]; see below), `false` to return a String in query
     *    string format. If you supply an `options` object with no `hash` member,
     *    `hash` defaults to `true`. Note that this is __not__ the same as leaving
     *    off the `options` object entirely (see above).
     *
     *  * `submit` ([[Boolean]] | [[String]]): In essence: If you omit this option
     *    the first submit button in the form is included; if you supply `false`,
     *    no submit buttons are included; if you supply the name of a submit
     *    button, the first button with that name is included. Note that the
     *    `false` value __must__ really be `false`, not _falsey_;
     *    falsey-but-not-false is like omitting the option.
     *
     *  _(Deprecated)_ If you pass in a [[Boolean]] instead of an object for
     *  `options`, it is used as the `hash` option and all other options are
     *  defaulted.
     *
     *  ##### A _hash_, not a [[Hash]]
     *
     *  If you opt to receive an object, it is a plain JavaScript object with keys
     *  and values, __not__ a [[Hash]]. All JavaScript objects are hashes in the
     *  lower-case sense of the word, which is why the option has that
     *  somewhat-confusing name.
     *
     *  ##### Examples
     *
     *  To serialize all input elements of type "text":
     *
     *      Form.serializeElements( $('myform').getInputs('text') )
     *      // -> serialized data
     **/
    serializeElements: function(elements, options) {
        // An earlier version accepted a boolean second parameter (hash) where
        // the default if omitted was false; respect that, but if they pass in an
        // options object (e.g., the new signature) but don't specify the hash option,
        // default true, as that's the new preferred approach.
        if (typeof options != 'object') options = { hash: !!options };
        else if (Object.isUndefined(options.hash)) options.hash = true;
        var key, value, submitted = false, submit = options.submit, accumulator, initial;

        if (options.hash) {
            initial = {};
            accumulator = function(result, key, value) {
                if (key in result) {
                    if (!Object.isArray(result[key])) result[key] = [result[key]];
                    result[key].push(value);
                } else result[key] = value;
                return result;
            };
        } else {
            initial = '';
            accumulator = function(result, key, value) {
                return result + (result ? '&' : '') + encodeURIComponent(key) + '=' + encodeURIComponent(value);
            }
        }

        return elements.inject(initial, function(result, element) {
            if (!element.disabled && element.name) {
                key = element.name;
                value = $(element).getValue();
                if (value != null && element.type != 'file' && (element.type != 'submit' || (!submitted &&
                        submit !== false && (!submit || key == submit) && (submitted = true)))) {
                    result = accumulator(result, key, value);
                }
            }
            return result;
        });
    }
};

Form.Methods = {
    /**
     *  Form.serialize(@form[, options]) -> String | Object
     *  - options (Object): A list of options that affect the return value
     *    of the method.
     *
     *  Serializes form data to a string suitable for [[Ajax]] requests (default
     *  behavior) or, if the `hash` option evaluates to `true`, an object hash
     *  where keys are form control names and values are data.
     *
     *  Depending of whether or not the `hash` option evaluates to `true`, the
     *  result is either an object of the form `{name: "johnny", color: "blue"}`
     *  or a [[String]] of the form `"name=johnny&color=blue"`, suitable for
     *  parameters in an [[Ajax]] request. This method mimics the way browsers
     *  serialize forms natively so that form data can be sent without refreshing
     *  the page.
     *
     *  See [[Form.serializeElements]] for more details on the options.
     *
     *  ##### Examples
     *
     *      $('person-example').serialize()
     *      // -> 'username=sulien&age=22&hobbies=coding&hobbies=hiking'
     *
     *      $('person-example').serialize(true)
     *      // -> {username: 'sulien', age: '22', hobbies: ['coding', 'hiking']}
     *
     *  ##### Notes
     *
     *  Disabled form elements are not serialized (as per W3C HTML recommendation).
     *  Also, file inputs are skipped as they cannot be serialized and sent using
     *  only JavaScript.
     **/
    serialize: function(form, options) {
        return Form.serializeElements(Form.getElements(form), options);
    },

    /**
     *  Form.getElements(@form) -> [Element...]
     *
     *  Returns a collection of all controls within a form.
     *
     *  ##### Note
     *
     *  OPTION elements are not included in the result; only their parent
     *  SELECT control is.
     **/
    getElements: function(form) {
        var elements = $(form).getElementsByTagName('*'),
                element,
                arr = [ ],
                serializers = Form.Element.Serializers;
        // `length` is not used to prevent interference with
        // length-named elements shadowing `length` of a nodelist
        for (var i = 0; element = elements[i]; i++) {
            arr.push(element);
        }
        return arr.inject([], function(elements, child) {
            if (serializers[child.tagName.toLowerCase()])
                elements.push(Element.extend(child));
            return elements;
        })
    },

    /**
     *  Form.getInputs(@form [, type [, name]]) -> [Element...]
     *  - type (String): A value for the `type` attribute against which to filter.
     *  - name (String): A value for the `name` attribute against which to filter.
     *
     *  Returns a collection of all INPUT elements in a form.
     *
     *  Use optional `type` and `name` arguments to restrict the search on
     *  these attributes.
     *
     *  ##### Example
     *
     *      var form = $('myform');
     *
     *      form.getInputs();       // -> all INPUT elements
     *      form.getInputs('text'); // -> only text inputs
     *
     *      var buttons = form.getInputs('radio', 'education');
     *      // -> only radio buttons of name "education"
     *
     *      // now disable these radio buttons:
     *      buttons.invoke('disable');
     *
     *  ##### Note
     *
     *  Elements are returned in the *document* order, not the
     *  [tabindex order](http://www.w3.org/TR/html4/interact/forms.html#h-17.11.1).
     **/
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

    /**
     *  Form.disable(@form) -> Element
     *
     *  Disables the form as a whole. Form controls will be visible but
     *  uneditable.
     *
     *  Disabling the form is done by iterating over form elements and calling
     *  [[Form.Element.disable]] on them.
     *
     *  ##### Note
     *
     *  Keep in mind that *disabled elements are skipped* by serialization
     *  methods! You cannot serialize a disabled form.
     **/
    disable: function(form) {
        form = $(form);
        Form.getElements(form).invoke('disable');
        return form;
    },

    /**
     *  Form.enable(@form) -> Element
     *
     *  Enables a fully- or partially-disabled form.
     *
     *  Enabling the form is done by iterating over form elements and calling
     *  [[Form.Element.enable]] on them.
     *
     *  ##### Note
     *
     *  This will enable all form controls regardless of how they were disabled
     *  (by scripting or by HTML attributes).
     **/
    enable: function(form) {
        form = $(form);
        Form.getElements(form).invoke('enable');
        return form;
    },

    /**
     *  Form.findFirstElement(@form) -> Element
     *
     *  Finds the first non-hidden, non-disabled control within the form.
     *
     *  The returned object is either an INPUT, SELECT or TEXTAREA element. This
     *  method is used by the [[Form.focusFirstElement]] method.
     *
     *  ##### Note
     *
     *  The result of this method is the element that comes first in the
     *  *document* order, not the
     *  [tabindex order](http://www.w3.org/TR/html4/interact/forms.html#h-17.11.1).
     **/
    findFirstElement: function(form) {
        var elements = $(form).getElements().findAll(function(element) {
            return 'hidden' != element.type && !element.disabled;
        });
        var firstByIndex = elements.findAll(
                function(element) {
                    return element.hasAttribute('tabIndex') && element.tabIndex >= 0;
                }).sortBy(
                function(element) {
                    return element.tabIndex
                }).first();

        return firstByIndex ? firstByIndex : elements.find(function(element) {
            return /^(?:input|select|textarea)$/i.test(element.tagName);
        });
    },

    /**
     *  Form.focusFirstElement(@form) -> Element
     *
     *  Gives keyboard focus to the first element of the form. Returns the form.
     *
     *  Uses [[Form.findFirstElement]] to get the first element and calls
     *  [[Form.Element.activate]] on it. This is useful for enhancing usability on
     *  your site by bringing focus on page load to forms such as search forms or
     *  contact forms where a user is ready to start typing right away.
     **/
    focusFirstElement: function(form) {
        form = $(form);
        var element = form.findFirstElement();
        if (element) element.activate();
        return form;
    },

    /**
     *  Form.request(@form[, options]) -> Ajax.Request
     *  - options (Object): Options to pass along to the [[Ajax.Request]]
     *    constructor.
     *
     *  A convenience method for serializing and submitting the form via an
     *  [[Ajax.Request]] to the URL of the form's `action` attribute.
     *
     *  The `options` parameter is passed to the [[Ajax.Request]] instance,
     *  allowing one to override the HTTP method and/or specify additional
     *  parameters and callbacks.
     *
     *  - If the form has a method attribute, its value is used for the
     *  [[Ajax.Request]] `method` option. If a method option is passed to
     *  `request()`, it takes precedence over the form's method attribute. If
     *  neither is specified, method defaults to "POST".
     *
     *  - Key-value pairs specified in the `parameters` option (either as a hash
     *  or a query string) will be merged with (and *take precedence* over) the
     *  serialized form parameters.
     *
     *  ##### Example
     *
     *  Suppose you have this HTML form:
     *
     *      language: html
     *      <form id="person-example" method="POST" action="/user/info">
     *        <fieldset><legend>User info</legend>
     *          <ul>
     *            <li>
     *              <label for="username">Username:</label>
     *              <input type="text" name="username" id="username" value="" />
     *            </li>
     *            <li>
     *              <label for="age">Age:</label>
     *              <input type="text" name="age" id="age" value="" size="3" />
     *            </li>
     *            <li>
     *              <label for="hobbies">Your hobbies are:</label>
     *              <select name="hobbies[]" id="hobbies" multiple="multiple">
     *                <option>coding</option>
     *                <option>swimming</option>
     *                <option>biking</option>
     *                <option>hiking</option>
     *                <option>drawing</option>
     *              </select>
     *            </li>
     *          </ul>
     *          <input type="submit" value="serialize!" />
     *        </fieldset>
     *      </form>
     *
     *  You can easily post it with Ajax like this:
     *
     *      $('person-example').request(); //done - it's posted
     *
     *      // do the same with a callback:
     *      $('person-example').request({
     *        onComplete: function(){ alert('Form data saved!') }
     *      })
     *
     *  To override the HTTP method and add some parameters, simply use `method`
     *  and `parameters` in the options. In this example we set the method to GET
     *  and set two fixed parameters:
     *  `interests` and `hobbies`. The latter already exists in the form but this
     *  value will take precedence.
     *
     *      $('person-example').request({
     *        method: 'get',
     *        parameters: { interests:'JavaScript', 'hobbies[]':['programming', 'music'] },
     *        onComplete: function(){ alert('Form data saved!') }
     *      })
     **/
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

/**
 * Form.Element
 *
 *  Utilities for dealing with form controls in the DOM.
 *
 *  This is a collection of methods that assist in dealing with form controls.
 *  They provide ways to [[Form.Element.focus focus]], [[Form.Element.serialize
 *  serialize]], [[Form.Element.disable disable]]/[[Form.Element.enable enable]]
 *  or extract current value from a specific control.
 *
 *  Note that nearly all these methods are available directly on `input`,
 *  `select`, and `textarea` elements. Therefore, these are equivalent:
 *
 *      Form.Element.activate('myfield');
 *      $('myfield').activate();
 *
 *  Naturally, you should always prefer the shortest form suitable in a
 *  situation. Most of these methods also return the element itself (as
 *  indicated by the return type) for chainability.
 **/

Form.Element = {
    /**
     *  Form.Element.focus(element) -> Element
     *
     *  Gives keyboard focus to an element. Returns the element.
     *
     *  ##### Example
     *
     *      Form.Element.focus('searchbox')
     *
     *      // Almost equivalent, but does NOT return the form element (uses the native focus() method):
     *      $('searchbox').focus()
     **/
    focus: function(element) {
        $(element).focus();
        return element;
    },

    /**
     *  Form.Element.select(element) -> Element
     *
     *  Selects the current text in a text input. Returns the element.
     *
     *  ##### Example
     *
     *  Some search boxes are set up so that they auto-select their content when they receive focus.
     *
     *        $('searchbox').onfocus = function() {
     *          Form.Element.select(this)
     *
     *          // You can also rely on the native method, but this will NOT return the element!
     *          this.select()
     *        }
     *
     *  ##### Focusing + selecting: use [[Form.Element.activate]]!
     *
     *  The [[Form.Element.activate]] method is a nifty way to both focus a form
     *  field and select its current text, all in one portable JavaScript call.
     **/
    select: function(element) {
        $(element).select();
        return element;
    }
};

Form.Element.Methods = {

    /**
     *  Form.Element.serialize(@element) -> String
     *
     *  Returns a URL-encoded string representation of a form control in the
     *  `name=value` format.
     *
     *  The result of this method is a string suitable for Ajax requests. However,
     *  it serializes only a single element - if you need to serialize the whole
     *  form use [[Form.serialize]] instead.
     *
     *  ##### Notes
     *
     *  Serializing a disabled control or a one without a name will always result
     *  in an empty string.
     *
     *  If you simply need an element's value for reasons other than Ajax
     *  requests, use [[Form.Element.getValue]] instead.
     **/
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

    /** alias of: $F
     *  Form.Element.getValue(@element) -> String | Array
     *
     *  Returns the current value of a form control.
     *
     *  A string is returned for most controls; only multiple `select` boxes
     *  return an array of values.
     *
     *  The global shortcut for this method is [[$F]].
     *
     *  ##### How to reference form controls by their _name_
     *
     *  This method is consistent with other DOM extensions in that it requires an
     *  element **ID** as the string argument, not the name of the
     *  form control (as some might think). If you want to reference controls by
     *  their names, first find the control the regular JavaScript way and use the
     *  node itself instead of an ID as the argument.
     *
     *  For example, if you have an `input` named "company" in a `form` with an
     *  ID "contact":
     *
     *      var form = $('contact');
     *      var input = form['company'];
     *
     *      Form.Element.getValue(input);
     *
     *      // but, the preferred call is:
     *      $(input).getValue(); // we used the $() method so the node gets extended
     *
     *      // you can also use the shortcut
     *      $F(input);
     *
     *  ##### Note
     *
     *  An error is thrown ("element has no properties") if the `element` argument
     *  is an unknown ID.
     **/
    getValue: function(element) {
        element = $(element);
        var method = element.tagName.toLowerCase();
        return Form.Element.Serializers[method](element);
    },

    /**
     *  Form.Element.setValue(@element, value) -> Element
     *
     *  Sets `value` to be the value of the form control. Returns the element.
     **/
    setValue: function(element, value) {
        element = $(element);
        var method = element.tagName.toLowerCase();
        Form.Element.Serializers[method](element, value);
        return element;
    },

    /**
     *  Form.Element.clear(@element) -> Element
     *
     *  Clears the contents of a text input. Returns the element.
     *
     *  ##### Example
     *
     *  This code sets up a text field in a way that it clears its contents the
     *  first time it receives focus:
     *
     *        $('some_field').onfocus = function() {
     *          // if already cleared, do nothing
     *          if (this._cleared) return
     *
     *          // when this code is executed, "this" keyword will in fact be the field itself
     *          this.clear()
     *          this._cleared = true
     *        }
     **/
    clear: function(element) {
        $(element).value = '';
        return element;
    },

    /**
     *  Form.Element.present(@element) -> Element
     *
     *  Returns `true` if a text input has contents, `false` otherwise.
     *
     *  ##### Example
     *
     *  This method is very handy in a generic form validation routine.
     *  On the following form's submit event, the presence of each text input is
     *  checked and lets the user know if they left a text input blank.
     *
     *      language: html
     *      <form id="example" class="example" action="#">
     *        <fieldset>
     *          <legend>User Details</legend>
     *          <p id="msg" class="message">Please fill out the following fields:</p>
     *          <p>
     *            <label for="username">Username</label>
     *            <input id="username" type="text" name="username" />
     *          </p>
     *          <p>
     *            <label for="email">Email Address</label>
     *            <input id="email" type="text" name="email" />
     *          </p>
     *          <input type="submit" value="submit" />
     *        </fieldset>
     *      </form>
     *
     *      <script type="text/javascript">
     *        $('example').onsubmit = function(){
     *          var valid, msg = $('msg')
     *
     *          // are both fields present?
     *          valid = $(this.username).present() && $(this.email).present()
     *
     *          if (valid) {
     *            // in the real world we would return true here to allow the form to be submitted
     *            // return true
     *            msg.update('Passed validation!').style.color = 'green'
     *          } else {
     *            msg.update('Please fill out <em>all</em> the fields.').style.color = 'red'
     *          }
     *          return false
     *        }
     *      </script>
     **/
    present: function(element) {
        return $(element).value != '';
    },

    /**
     *  Form.Element.activate(@element) -> Element
     *
     *  Gives focus to a form control and selects its contents if it is a text
     *  input.
     *
     *  This method is just a shortcut for focusing and selecting; therefore,
     *  these are equivalent (aside from the fact that the former one will __not__
     *  return the field) :
     *
     *      Form.Element.focus('myelement').select()
     *      $('myelement').activate()
     *
     *  Guess which call is the nicest? ;)
     **/
    activate: function(element) {
        element = $(element);
        try {
            element.focus();
            if (element.select && (element.tagName.toLowerCase() != 'input' ||
                    !(/^(?:button|reset|submit)$/i.test(element.type))))
                element.select();
        } catch (e) {
        }
        return element;
    },

    /**
     *  Form.Element.disable(@element) -> Element
     *
     *  Disables a form control, effectively preventing its value from changing
     *  until it is enabled again.
     *
     *  This method sets the native `disabled` property of an element to `true`.
     *  You can use this property to check the state of a control.
     *
     *  ##### Notes
     *
     *  Disabled form controls are never serialized.
     *
     *  Never disable a form control as a security measure without having
     *  validation for it server-side. A user with minimal experience of
     *  JavaScript can enable these fields on your site easily using any browser.
     *  Instead, use disabling as a usability enhancement - with it you can
     *  indicate that a specific value should not be changed at the time being.
     **/
    disable: function(element) {
        element = $(element);
        element.disabled = true;
        return element;
    },

    /**
     *  Form.Element.enable(@element) -> Element
     *
     *  Enables a previously disabled form control.
     **/
    enable: function(element) {
        element = $(element);
        element.disabled = false;
        return element;
    }
};

/*--------------------------------------------------------------------------*/

var Field = Form.Element;

/** section: DOM, related to: Form
 *  $F(element) -> String | Array
 *
 *  Returns the value of a form control. This is a convenience alias of
 *  [[Form.Element.getValue]]. Refer to it for full details.
 **/
var $F = Form.Element.Methods.getValue;

/*--------------------------------------------------------------------------*/

Form.Element.Serializers = (function() {
    function input(element, value) {
        switch (element.type.toLowerCase()) {
            case 'checkbox':
            case 'radio':
                return inputSelector(element, value);
            default:
                return valueSelector(element, value);
        }
    }

    function inputSelector(element, value) {
        if (Object.isUndefined(value))
            return element.checked ? element.value : null;
        else element.checked = !!value;
    }

    function valueSelector(element, value) {
        if (Object.isUndefined(value)) return element.value;
        else element.value = value;
    }

    function select(element, value) {
        if (Object.isUndefined(value))
            return (element.type === 'select-one' ? selectOne : selectMany)(element);

        var opt, currentValue, single = !Object.isArray(value);
        for (var i = 0, length = element.length; i < length; i++) {
            opt = element.options[i];
            currentValue = this.optionValue(opt);
            if (single) {
                if (currentValue == value) {
                    opt.selected = true;
                    return;
                }
            }
            else opt.selected = value.include(currentValue);
        }
    }

    function selectOne(element) {
        var index = element.selectedIndex;
        return index >= 0 ? optionValue(element.options[index]) : null;
    }

    function selectMany(element) {
        var values, length = element.length;
        if (!length) return null;

        for (var i = 0, values = []; i < length; i++) {
            var opt = element.options[i];
            if (opt.selected) values.push(optionValue(opt));
        }
        return values;
    }

    function optionValue(opt) {
        return Element.hasAttribute(opt, 'value') ? opt.value : opt.text;
    }

    return {
        input:         input,
        inputSelector: inputSelector,
        textarea:      valueSelector,
        select:        select,
        selectOne:     selectOne,
        selectMany:    selectMany,
        optionValue:   optionValue,
        button:        valueSelector
    };
})();

/*--------------------------------------------------------------------------*/

/** section: DOM
 * Abstract
 **/

/**
 *  class Abstract.TimedObserver
 *
 *  An abstract DOM element observer class, subclasses of which can be used to
 *  periodically check a value and trigger a callback when the value has changed.
 *
 *  A `TimedObserver` object will try to check a value using the `getValue()`
 *  instance method which must be defined by the subclass. There are two
 *  out-of-the-box subclasses:
 *  [[Form.Observer]], which serializes a form and triggers when the result has
 *  changed; and [[Form.Element.Observer]], which triggers when the value of a
 *  given form field changes.
 *
 *
 *  Using `TimedObserver` implementations is straightforward; simply instantiate
 *  them with appropriate arguments. For example:
 *
 *      new Form.Element.Observer(
 *        'myelement',
 *        0.2,  // 200 milliseconds
 *        function(el, value){
 *          alert('The form control has changed value to: ' + value)
 *        }
 *      )
 *
 *  Now that we have instantiated an object, it will check the value of the form
 *  control every 0.2 seconds and alert us of any change. While it is useless to
 *  alert the user of his own input (like in the example), we could be doing
 *  something useful like updating a certain part of the UI or informing the
 *  application on server of stuff happening (over Ajax).
 *
 *  The callback function is always called with 2 arguments: the element given
 *  when the observer instance was made and the actual value that has changed
 *  and caused the callback to be triggered in the first place.
 *
 *  ##### Creating Your Own TimedObserver Implementations
 *
 *  It's easy to create your own `TimedObserver` implementations: Simply subclass
 *  `TimedObserver` and provide the `getValue()` method. For example, this is the
 *  complete source code for [[Form.Element.Observer]]:
 *
 *      Form.Element.Observer = Class.create(Abstract.TimedObserver, {
 *        getValue: function() {
 *          return Form.Element.getValue(this.element);
 *        }
 *      });
 **/
Abstract.TimedObserver = Class.create(PeriodicalExecuter, {
            /**
             *  new Abstract.TimedObserver(element, frequency, callback)
             *  - element (String | Element): The DOM element to watch. Can be an element
             *    instance or an ID.
             *  - frequency (Number): The frequency, in seconds&nbsp;&mdash; e.g., 0.33 to
             *    check for changes every third of a second.
             *  - callback (Function): The callback to trigger when the value changes.
             *
             *  Initializes an [[Abstract.TimedObserver]]; used by subclasses.
             **/
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

/**
 *  class Form.Element.Observer < Abstract.TimedObserver
 *
 *  An [[Abstract.TimedObserver]] subclass that watches for changes to a form
 *  field's value. This triggers the callback when the form field's value
 *  (according to [[Form.Element.getValue]]) changes. (Note that when the value
 *  actually changes can vary from browser to browser, particularly with
 *  `select` boxes.)
 *
 *  Form.Element observer implements the `getValue()` method using
 *  [[Form.Element.getValue]] on the given element. See [[Abstract.TimedObserver]]
 *  for general documentation on timed observers.
 **/
Form.Element.Observer = Class.create(Abstract.TimedObserver, {
            /**
             *  new Form.Element.Observer(element, frequency, callback)
             *  - element (String | Element): The form element to watch. Can be an element instance or an ID.
             *  - frequency (Number): The frequency, in seconds&nbsp;&mdash; e.g., 0.33 to check for changes every
             *    third of a second.
             *  - callback (Function): The callback to trigger when the value changes.
             *
             *  Creates a [[Form.Element.Observer]].
             **/
            getValue: function() {
                return Form.Element.getValue(this.element);
            }
        });

/**
 *  class Form.Observer < Abstract.TimedObserver
 *
 *  An [[Abstract.TimedObserver]] subclass that watches for changes to a form.
 *  The callback is triggered when the form changes&nbsp;&mdash; e.g., when any
 *  of its fields' values changes, when fields are added/removed, etc.; anything
 *  that affects the serialized form of the form (see [[Form#serialize]]).
 *
 *  ##### Example
 *
 *  In this example an `observer` is used to change the appearance of the form
 *  if any of the values had been changed. It returns to its initial state when
 *  the data is submitted (saved).
 *
 *      language: html
 *      <form id="example" action="#">
 *        <fieldset>
 *          <legend>Login Preferences</legend>
 *          <p id="msg" class="message">Current settings:</p>
 *          <p>
 *            <label for="greeting">Greeting message</label>
 *            <input id="greeting" type="text" name="greeting" value="Hello world!" />
 *          </p>
 *          <h4>Login options</h4>
 *          <p>
 *              <input id="login-visible" type="checkbox" name="login-visible" checked="checked" />
 *              <label for="login-visible">allow others to see my last login date</label>
 *          </p>
 *          <p>
 *              <input id="land-recent" type="checkbox" name="land-recent" />
 *              <label for="land-recent">land on recent changes overview instead of the Dashboard</label>
 *          </p>
 *          <input type="submit" value="save" />
 *        </fieldset>
 *      </form>
 *
 *      <script type="text/javascript">
 *        new Form.Observer('example', 0.3, function(form, value){
 *          $('msg').update('Your preferences have changed. Resubmit to save').style.color = 'red'
 *          form.down().setStyle({ background:'lemonchiffon', borderColor:'red' })
 *        })
 *
 *        $('example').onsubmit = function() {
 *          $('msg').update('Preferences saved!').style.color = 'green'
 *          this.down().setStyle({ background:null, borderColor:null })
 *          return false
 *        }
 *      </script>
 **/
Form.Observer = Class.create(Abstract.TimedObserver, {
            /**
             *  new Form.Observer(element, frequency, callback)
             *  - element (String | Element): The element of the form to watch. Can be an element
             *    instance or an ID.
             *  - frequency (Number): The frequency, in seconds -- e.g., 0.33 to check for changes every
             *    third of a second.
             *  - callback (Function): The callback to trigger when the form changes.
             *
             *  Creates a [[Form.Observer]].
             **/
            getValue: function() {
                return Form.serialize(this.element);
            }
        });

/*--------------------------------------------------------------------------*/

/**
 *  class Abstract.EventObserver
 **/
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

/**
 *  class Form.Element.EventObserver < Abstract.EventObserver
 **/
Form.Element.EventObserver = Class.create(Abstract.EventObserver, {
            getValue: function() {
                return Form.Element.getValue(this.element);
            }
        });

/**
 *  class Form.EventObserver < Abstract.EventObserver
 **/
Form.EventObserver = Class.create(Abstract.EventObserver, {
            getValue: function() {
                return Form.serialize(this.element);
            }
        });

(function() {

    /** section: DOM
     * class Event
     *
     *  The namespace for Prototype's event system.
     *
     *  ##### Events: a fine mess
     *
     *  Event management is one of the really sore spots of cross-browser
     *  scripting.
     *
     *  True, the prevalent issue is: everybody does it the W3C way, and MSIE
     *  does it another way altogether. But there are quite a few subtler,
     *  sneakier issues here and there waiting to bite your ankle &mdash; such as the
     *  `keypress`/`keydown` issue with KHTML-based browsers (Konqueror and
     *  Safari). Also, MSIE has a tendency to leak memory when it comes to
     *  discarding event handlers.
     *
     *  ##### Prototype to the rescue
     *
     *  Of course, Prototype smooths it over so well you'll forget these
     *  troubles even exist. Enter the [[Event]] namespace. It is replete with
     *  methods that help to normalize the information reported by events across
     *  browsers.
     *
     *  [[Event]] also provides a standardized list of key codes you can use with
     *  keyboard-related events, including `KEY_BACKSPACE`, `KEY_TAB`,
     *  `KEY_RETURN`, `KEY_ESC`, `KEY_LEFT`, `KEY_UP`, `KEY_RIGHT`, `KEY_DOWN`,
     *  `KEY_DELETE`, `KEY_HOME`, `KEY_END`, `KEY_PAGEUP`, `KEY_PAGEDOWN` and
     *  `KEY_INSERT`.
     *
     *  The functions you're most likely to use a lot are [[Event.observe]],
     *  [[Event.element]] and [[Event.stop]]. If your web app uses custom events,
     *  you'll also get a lot of mileage out of [[Event.fire]].
     *
     *  ##### Instance methods on event objects
     *  As of Prototype 1.6, all methods on the `Event` object are now also
     *  available as instance methods on the event object itself:
     *
     *  **Before**
     *
     *      $('foo').observe('click', respondToClick);
     *
     *      function respondToClick(event) {
     *        var element = Event.element(event);
     *        element.addClassName('active');
     *      }
     *
     *  **After**
     *
     *      $('foo').observe('click', respondToClick);
     *
     *      function respondToClick(event) {
     *        var element = event.element();
     *        element.addClassName('active');
     *      }
     *
     *  These methods are added to the event object through [[Event.extend]],
     *  in the same way that `Element` methods are added to DOM nodes through
     *  [[Element.extend]]. Events are extended automatically when handlers are
     *  registered with Prototype's [[Event.observe]] method; if you're using a
     *  different method of event registration, for whatever reason,you'll need to
     *  extend these events manually with [[Event.extend]].
     **/
    var Event = {
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

        cache: {}
    };

    var docEl = document.documentElement;
    var MOUSEENTER_MOUSELEAVE_EVENTS_SUPPORTED = 'onmouseenter' in docEl
            && 'onmouseleave' in docEl;


    // We need to support three different event "modes":
    //  1. browsers with only DOM L2 Events (WebKit, FireFox);
    //  2. browsers with only IE's legacy events system (IE 6-8);
    //  3. browsers with _both_ systems (IE 9 and arguably Opera).
    //
    // Groups 1 and 2 are easy; group three is trickier.

    var isIELegacyEvent = function(event) {
        return false;
    };

    if (window.attachEvent) {
        if (window.addEventListener) {
            // Both systems are supported. We need to decide at runtime.
            // (Though Opera supports both systems, the event object appears to be
            // the same no matter which system is used. That means that this function
            // will always return `true` in Opera, but that's OK; it keeps us from
            // having to do a browser sniff.
            isIELegacyEvent = function(event) {
                return !(event instanceof window.Event);
            };
        } else {
            // No support for DOM L2 events. All events will be legacy.
            isIELegacyEvent = function(event) {
                return true;
            };
        }
    }

    // The two systems have different ways of indicating which button was used
    // for a mouse event.
    var _isButton;

    function _isButtonForDOMEvents(event, code) {
        return event.which ? (event.which === code + 1) : (event.button === code);
    }

    var legacyButtonMap = { 0: 1, 1: 4, 2: 2 };

    function _isButtonForLegacyEvents(event, code) {
        return event.button === legacyButtonMap[code];
    }

    // In WebKit we have to account for when the user holds down the "meta" key.
    function _isButtonForWebKit(event, code) {
        switch (code) {
            case 0:
                return event.which == 1 && !event.metaKey;
            case 1:
                return event.which == 2 || (event.which == 1 && event.metaKey);
            case 2:
                return event.which == 3;
            default:
                return false;
        }
    }

    if (window.attachEvent) {
        if (!window.addEventListener) {
            // Legacy IE events only.
            _isButton = _isButtonForLegacyEvents;
        } else {
            // Both systems are supported; decide at runtime.
            _isButton = function(event, code) {
                return isIELegacyEvent(event) ? _isButtonForLegacyEvents(event, code) :
                        _isButtonForDOMEvents(event, code);
            }
        }
    } else if (Prototype.Browser.WebKit) {
        _isButton = _isButtonForWebKit;
    } else {
        _isButton = _isButtonForDOMEvents;
    }

    /**
     *  Event.isLeftClick(@event) -> Boolean
     *
     *  Determines whether a button-related mouse event involved the left
     *  mouse button.
     *
     *  Keep in mind that the "left" mouse button is actually the "primary" mouse
     *  button. When a mouse is in left-handed mode, the browser will report
     *  clicks of the _right_ button as "left-clicks."
     **/
    function isLeftClick(event) {
        return _isButton(event, 0)
    }

    /**
     *  Event.isMiddleClick(@event) -> Boolean
     *
     *  Determines whether a button-related mouse event involved the middle
     *  mouse button.
     **/
    function isMiddleClick(event) {
        return _isButton(event, 1)
    }

    /**
     *  Event.isRightClick(@event) -> Boolean
     *
     *  Determines whether a button-related mouse event involved the right
     *  mouse button.
     *
     *  Keep in mind that the "left" mouse button is actually the "secondary"
     *  mouse button. When a mouse is in left-handed mode, the browser will
     *  report clicks of the _left_ button as "left-clicks."
     **/
    function isRightClick(event) {
        return _isButton(event, 2)
    }

    /** deprecated
     *  Event.element(@event) -> Element
     *  - event (Event): An Event object
     *
     *  Returns the DOM element on which the event occurred. This method
     *  is deprecated, use [[Event.findElement]] instead.
     *
     *  ##### Example
     *
     *  Here's a simple bit of code which hides any paragraph when directly clicked.
     *
     *      document.observe('click', function(event) {
     *        var element = Event.element(event);
     *        if ('P' == element.tagName)
     *          element.hide();
     *      });
     *
     *  ##### See also
     *
     *  There is a subtle distinction between this function and
     *  [[Event.findElement]].
     *
     *  ##### Note for Prototype 1.5.0
     *
     *  Note that prior to version 1.5.1, if the browser does not support
     *  *native DOM extensions* (see the [[Element]] section for further details),
     *  the element returned by [[Event.element]] might very well
     *  *not be extended*. If you intend to use methods from [[Element.Methods]]
     *  on it, you need to wrap the call in the [[$]] function like so:
     *
     *      document.observe('click', function(event) {
     *        var element = $(Event.element(event));
     *        // ...
     *      });
     **/
    function element(event) {
        event = Event.extend(event);

        var node = event.target, type = event.type,
                currentTarget = event.currentTarget;

        if (currentTarget && currentTarget.tagName) {
            // Firefox screws up the "click" event when moving between radio buttons
            // via arrow keys. It also screws up the "load" and "error" events on images,
            // reporting the document as the target instead of the original image.
            if (type === 'load' || type === 'error' ||
                    (type === 'click' && currentTarget.tagName.toLowerCase() === 'input'
                            && currentTarget.type === 'radio'))
                node = currentTarget;
        }

        // Fix a Safari bug where a text node gets passed as the target of an
        // anchor click rather than the anchor itself.
        if (node.nodeType == Node.TEXT_NODE)
            node = node.parentNode;

        return Element.extend(node);
    }

    /**
     *  Event.findElement(@event[, expression]) -> Element
     *  - event (Event): An Event object
     *  - expression (String): An optional CSS selector
     *
     *  Returns the first DOM element that matches a given CSS selector &mdash;
     *  starting with the element on which the event occurred, then moving up
     *  its ancestor chain. If `expression` is not given, the element which fired
     *  the event is returned.
     *
     *  *If no matching element is found, the document itself (`HTMLDocument` node)
     *  is returned.*
     *
     *  ##### Example
     *
     *  Here's a simple code that lets you click everywhere on the page and hides
     *  the closest-fitting paragraph around your click (if any).
     *
     *      document.observe('click', function(event) {
     *        var element = Event.findElement(event, 'p');
     *        if (element != document)
     *          $(element).hide();
     *      });
     **/
    function findElement(event, expression) {
        var element = Event.element(event);

        if (!expression) return element;
        while (element) {
            if (Object.isElement(element) && Prototype.Selector.match(element, expression)) {
                return Element.extend(element);
            }
            element = element.parentNode;
        }
    }

    /**
     *  Event.pointer(@event) -> Object
     *
     *  Returns the absolute position of the pointer for a mouse event.
     *
     *  Returns an object in the form `{ x: Number, y: Number}`.
     *
     *  Note that this position is absolute on the _page_, not on the
     *  _viewport_.
     **/
    function pointer(event) {
        return { x: pointerX(event), y: pointerY(event) };
    }

    /**
     *  Event.pointerX(@event) -> Number
     *
     *  Returns the absolute horizontal position of the pointer for a mouse
     *  event.
     *
     *  Note that this position is absolute on the `<body>`, not on the
     *  viewport: scrolling right increases the returned value for events on
     *  the same viewport location.
     **/
    function pointerX(event) {
        var docElement = document.documentElement,
                body = document.body || { scrollLeft: 0 };

        return event.pageX || (event.clientX +
                (docElement.scrollLeft || body.scrollLeft) -
                (docElement.clientLeft || 0));
    }

    /**
     *  Event.pointerY(@event) -> Number
     *
     *  Returns the absolute vertical position of the pointer for a mouse
     *  event.
     *
     *  Note that this position is absolute on the `<body>`, not on the
     *  viewport: scrolling down increases the returned value for events on
     *  the same viewport location.
     **/
    function pointerY(event) {
        var docElement = document.documentElement,
                body = document.body || { scrollTop: 0 };

        return  event.pageY || (event.clientY +
                (docElement.scrollTop || body.scrollTop) -
                (docElement.clientTop || 0));
    }


    /**
     *  Event.stop(@event) -> undefined
     *
     *  Stops the event's propagation and prevents its eventual default action
     *  from being triggered.
     *
     *  Stopping an event also sets a `stopped` property on that event for
     *  future inspection.
     *
     *  There are two aspects to how your browser handles an event once it fires up:
     *
     *  1. The browser usually triggers event handlers on the actual element the
     *  event occurred on, then on its parent element, and so on and so forth,
     *  until the document's root element is reached. This is called
     *  *event bubbling*, and is the most common form of event propagation. You
     *  may very well want to stop this propagation when you just handled an event,
     *  and don't want it to keep bubbling up (or see no need for it).
     *
     *  2. Once your code had a chance to process the event, the browser handles
     *  it as well, if that event has a *default behavior*. For instance, clicking
     *  on links navigates to them; submitting forms sends them over to the server
     *  side; hitting the Return key in a single-line form field submits it; etc.
     *  You may very well want to prevent this default behavior if you do your own
     *  handling.
     *
     *  Because stopping one of those aspects means, in 99.9% of the cases,
     *  preventing the other one as well, Prototype bundles both in this `stop`
     *  function. Calling it on an event object, stops propagation *and* prevents
     *  the default behavior.
     *
     *  ##### Example
     *
     *  Here's a simple script that prevents a form from being sent to the server
     *  side if certain field is empty.
     *
     *      Event.observe('signinForm', 'submit', function(event) {
     *        var login = $F('login').strip();
     *        if ('' == login) {
     *          Event.stop(event);
     *          // Display the issue one way or another
     *        }
     *      });
     **/
    function stop(event) {
        Event.extend(event);
        event.preventDefault();
        event.stopPropagation();

        // Set a "stopped" property so that a custom event can be inspected
        // after the fact to determine whether or not it was stopped.
        event.stopped = true;
    }


    Event.Methods = {
        isLeftClick:   isLeftClick,
        isMiddleClick: isMiddleClick,
        isRightClick:  isRightClick,

        element:     element,
        findElement: findElement,

        pointer:  pointer,
        pointerX: pointerX,
        pointerY: pointerY,

        stop: stop
    };

    // Compile the list of methods that get extended onto Events.
    var methods = Object.keys(Event.Methods).inject({ }, function(m, name) {
        m[name] = Event.Methods[name].methodize();
        return m;
    });

    if (window.attachEvent) {
        // For IE's event system, we need to do some work to make the event
        // object behave like a standard event object.
        function _relatedTarget(event) {
            var element;
            switch (event.type) {
                case 'mouseover':
                case 'mouseenter':
                    element = event.fromElement;
                    break;
                case 'mouseout':
                case 'mouseleave':
                    element = event.toElement;
                    break;
                default:
                    return null;
            }
            return Element.extend(element);
        }

        // These methods should be added _only_ to legacy IE event objects.
        var additionalMethods = {
            stopPropagation: function() {
                this.cancelBubble = true
            },
            preventDefault:  function() {
                this.returnValue = false
            },
            inspect: function() {
                return '[object Event]'
            }
        };

        /**
         *  Event.extend(@event) -> Event
         *
         *  Extends `event` with all of the methods contained in `Event.Methods`.
         *
         *  Note that all events inside handlers that were registered using
         *  [[Event.observe]] or [[Element.observe]] will be extended automatically.
         *
         *  You need only call `Event.extend` manually if you register a handler a
         *  different way (e.g., the `onclick` attribute). We really can't encourage
         *  that sort of thing, though.
         **/
        // IE's method for extending events.
        Event.extend = function(event, element) {
            if (!event) return false;

            // If it's not a legacy event, it doesn't need extending.
            if (!isIELegacyEvent(event)) return event;

            // Mark this event so we know not to extend a second time.
            if (event._extendedByPrototype) return event;
            event._extendedByPrototype = Prototype.emptyFunction;

            var pointer = Event.pointer(event);

            // The optional `element` argument gives us a fallback value for the
            // `target` property in case IE doesn't give us through `srcElement`.
            Object.extend(event, {
                        target: event.srcElement || element,
                        relatedTarget: _relatedTarget(event),
                        pageX:  pointer.x,
                        pageY:  pointer.y
                    });

            Object.extend(event, methods);
            Object.extend(event, additionalMethods);

            return event;
        };
    } else {
        // Only DOM events, so no manual extending necessary.
        Event.extend = Prototype.K;
    }

    if (window.addEventListener) {
        // In all browsers that support DOM L2 Events, we can augment
        // `Event.prototype` directly.
        Event.prototype = window.Event.prototype || document.createEvent('HTMLEvents').__proto__;
        Object.extend(Event.prototype, methods);
    }

    function _createResponder(element, eventName, handler) {
        // We don't set a default on the call to Element#retrieve so that we can
        // handle the element's "virgin" state.
        var registry = Element.retrieve(element, 'prototype_event_registry');

        if (Object.isUndefined(registry)) {
            // First time we've handled this element. Put it into the cache.
            CACHE.push(element);
            registry = Element.retrieve(element, 'prototype_event_registry', $H());
        }

        var respondersForEvent = registry.get(eventName);
        if (Object.isUndefined(respondersForEvent)) {
            respondersForEvent = [];
            registry.set(eventName, respondersForEvent);
        }

        // Work around the issue that permits a handler to be attached more than
        // once to the same element & event type.
        if (respondersForEvent.pluck('handler').include(handler)) return false;

        var responder;
        if (eventName.include(":")) {
            // Custom event.
            responder = function(event) {
                // If it's not a custom event, ignore it.
                if (Object.isUndefined(event.eventName))
                    return false;

                // If it's a custom event, but not the _correct_ custom event, ignore it.
                if (event.eventName !== eventName)
                    return false;

                Event.extend(event, element);
                handler.call(element, event);
            };
        } else {
            // Non-custom event.
            if (!MOUSEENTER_MOUSELEAVE_EVENTS_SUPPORTED &&
                    (eventName === "mouseenter" || eventName === "mouseleave")) {
                // If we're dealing with mouseenter or mouseleave in a non-IE browser,
                // we create a custom responder that mimics their behavior within
                // mouseover and mouseout.
                if (eventName === "mouseenter" || eventName === "mouseleave") {
                    responder = function(event) {
                        Event.extend(event, element);

                        var parent = event.relatedTarget;
                        while (parent && parent !== element) {
                            try {
                                parent = parent.parentNode;
                            }
                            catch(e) {
                                parent = element;
                            }
                        }

                        if (parent === element) return;

                        handler.call(element, event);
                    };
                }
            } else {
                responder = function(event) {
                    Event.extend(event, element);
                    handler.call(element, event);
                };
            }
        }

        responder.handler = handler;
        respondersForEvent.push(responder);
        return responder;
    }

    function _destroyCache() {
        for (var i = 0, length = CACHE.length; i < length; i++) {
            Event.stopObserving(CACHE[i]);
            CACHE[i] = null;
        }
    }

    var CACHE = [];

    // Internet Explorer needs to remove event handlers on page unload
    // in order to avoid memory leaks.
    if (Prototype.Browser.IE)
        window.attachEvent('onunload', _destroyCache);

    // Safari needs a dummy event handler on page unload so that it won't
    // use its bfcache. Safari <= 3.1 has an issue with restoring the "document"
    // object when page is returned to via the back button using its bfcache.
    if (Prototype.Browser.WebKit)
        window.addEventListener('unload', Prototype.emptyFunction, false);


    var _getDOMEventName = Prototype.K,
            translations = { mouseenter: "mouseover", mouseleave: "mouseout" };

    if (!MOUSEENTER_MOUSELEAVE_EVENTS_SUPPORTED) {
        _getDOMEventName = function(eventName) {
            return (translations[eventName] || eventName);
        };
    }

    /**
     *  Event.observe(element, eventName, handler) -> Element
     *  - element (Element | String): The DOM element to observe, or its ID.
     *  - eventName (String): The name of the event, in all lower case, without
     *    the "on" prefix&nbsp;&mdash; e.g., "click" (not "onclick").
     *  - handler (Function): The function to call when the event occurs.
     *
     *  Registers an event handler on a DOM element. Aliased as [[Element#observe]].
     *
     *  [[Event.observe]] smooths out a variety of differences between browsers
     *  and provides some handy additional features as well. Key features in brief:
     *  * Several handlers can be registered for the same event on the same element.
     *  * Prototype figures out whether to use `addEventListener` (W3C standard) or
     *    `attachEvent` (MSIE); you don't have to worry about it.
     *  * The handler is passed an _extended_ [[Event]] object (even on MSIE).
     *  * The handler's context (`this` value) is set to the extended element
     *    being observed (even if the event actually occurred on a descendent
     *    element and bubbled up).
     *  * Prototype handles cleaning up the handler when leaving the page
     *    (important for MSIE memory leak prevention).
     *  * [[Event.observe]] makes it possible to stop observing the event easily
     *    via [[Event.stopObserving]].
     *  * Adds support for `mouseenter` / `mouseleave` events in all browsers.
     *
     *  Although you can use [[Event.observe]] directly and there are times when
     *  that's the most convenient or direct way, it's more common to use its
     *  alias [[Element#observe]]. These two statements have the same effect:
     *
     *      Event.observe('foo', 'click', myHandler);
     *      $('foo').observe('click', myHandler);
     *
     *  The examples in this documentation use the [[Element#observe]] form.
     *
     *  ##### The Handler
     *
     *  Signature:
     *
     *      function handler(event) {
     *        // `this` = the element being observed
     *      }
     *
     *  So for example, this will turn the background of the element 'foo' blue
     *  when it's clicked:
     *
     *      $('foo').observe('click', function(event) {
     *        this.setStyle({backgroundColor: 'blue'});
     *      });
     *
     *  Note that we used `this` to refer to the element, and that we received the
     *  `event` object as a parameter (even on MSIE).
     *
     *  ##### It's All About Timing
     *
     *  One of the most common errors trying to observe events is trying to do it
     *  before the element exists in the DOM. Don't try to observe elements until
     *  after the [[document.observe dom:loaded]] event or `window` `load` event
     *  has been fired.
     *
     *  ##### Preventing the Default Event Action and Bubbling
     *
     *  If we want to stop the event (e.g., prevent its default action and stop it
     *  bubbling), we can do so with the extended event object's [[Event#stop]]
     *  method:
     *
     *      $('foo').observe('click', function(event) {
     *        event.stop();
     *      });
     *
     *  ##### Finding the Element Where the Event Occurred
     *
     *  Since most events bubble from descendant elements up through the hierarchy
     *  until they're handled, we can observe an event on a container rather than
     *  individual elements within the container. This is sometimes called "event
     *  delegation". It's particularly handy for tables:
     *
     *      language: html
     *      <table id='records'>
     *        <thead>
     *          <tr><th colspan='2'>No record clicked</th></tr>
     *        </thead>
     *        <tbody>
     *          <tr data-recnum='1'><td>1</td><td>First record</td></tr>
     *          <tr data-recnum='2'><td>2</td><td>Second record</td></tr>
     *          <tr data-recnum='3'><td>3</td><td>Third record</td></tr>
     *        </tbody>
     *      </table>
     *
     *  Instead of observing each cell or row, we can simply observe the table:
     *
     *      $('records').observe('click', function(event) {
     *        var clickedRow = event.findElement('tr');
     *        if (clickedRow) {
     *          this.down('th').update("You clicked record #" + clickedRow.readAttribute("data-recnum"));
     *        }
     *      });
     *
     *  When any row in the table is clicked, we update the table's first header
     *  cell saying which record was clicked. [[Event#findElement]] finds the row
     *  that was clicked, and `this` refers to the table we were observing.
     *
     *  ##### Stopping Observing the Event
     *
     *  If we don't need to observe the event anymore, we can stop observing it
     *  with [[Event.stopObserving]] or its [[Element#stopObserving]] alias.
     *
     *  ##### Using an Instance Method as a Handler
     *
     *  If we want to use an instance method as a handler, we will probably want
     *  to use [[Function#bind]] to set the handler's context; otherwise, the
     *  context will be lost and `this` won't mean what we expect it to mean
     *  within the handler function. E.g.:
     *
     *      var MyClass = Class.create({
     *        initialize: function(name, element) {
     *          this.name = name;
     *          element = $(element);
     *          if (element) {
     *            element.observe(this.handleClick.bind(this));
     *          }
     *        },
     *        handleClick: function(event) {
     *          alert("My name is " + this.name);
     *        },
     *      });
     *
     *  Without the [[Function#bind]], when `handleClick` was triggered by the
     *  event, `this` wouldn't refer to the instance and so the alert wouldn't
     *  show the name. Because we used [[Function#bind]], it works correctly. See
     *  [[Function#bind]] for details. There's also [[Function#bindAsEventListener]],
     *  which is handy for certain very specific situations. (Normally,
     *  [[Function#bind]] is all you need.)
     *
     *  ##### Side Notes
     *
     *  Although Prototype smooths out most of the differences between browsers,
     *  the fundamental behavior of a browser implementation isn't changed. For
     *  example, the timing of the `change` or `blur` events varies a bit from
     *  browser to browser.
     *
     *  ##### Changes in 1.6.x
     *
     *  Prior to Prototype 1.6, [[Event.observe]] supported a fourth argument
     *  (`useCapture`), a boolean that indicated whether to use the browser's
     *  capturing phase or its bubbling phase. Since MSIE does not support the
     *  capturing phase, we removed this argument from 1.6, lest it give users the
     *  false impression that they can use the capturing phase in all browsers.
     *
     *  1.6 also introduced setting the `this` context to the element being
     *  observed, automatically extending the [[Event]] object, and the
     *  [[Event#findElement]] method.
     **/
    function observe(element, eventName, handler) {
        element = $(element);

        var responder = _createResponder(element, eventName, handler);

        if (!responder) return element;

        if (eventName.include(':')) {
            // Custom event.
            if (element.addEventListener)
                element.addEventListener("dataavailable", responder, false);
            else {
                // We observe two IE-proprietarty events: one for custom events that
                // bubble and one for custom events that do not bubble.
                element.attachEvent("ondataavailable", responder);
                element.attachEvent("onlosecapture", responder);
            }
        } else {
            var actualEventName = _getDOMEventName(eventName);

            // Ordinary event.
            if (element.addEventListener)
                element.addEventListener(actualEventName, responder, false);
            else
                element.attachEvent("on" + actualEventName, responder);
        }

        return element;
    }

    /**
     *  Event.stopObserving(element[, eventName[, handler]]) -> Element
     *  - element (Element | String): The element to stop observing, or its ID.
     *  - eventName (String): _(Optional)_ The name of the event to stop
     *    observing, in all lower case, without the "on"&nbsp;&mdash; e.g.,
     *    "click" (not "onclick").
     *  - handler (Function): _(Optional)_ The handler to remove; must be the
     *    _exact same_ reference that was passed to [[Event.observe]].
     *
     *  Unregisters one or more event handlers.
     *
     *  If `handler` is omitted, unregisters all event handlers on `element`
     *  for that `eventName`. If `eventName` is also omitted, unregisters _all_
     *  event handlers on `element`. (In each case, only affects handlers
     *  registered via Prototype.)
     *
     *  ##### Examples
     *
     *  Assuming:
     *
     *      $('foo').observe('click', myHandler);
     *
     *  ...we can stop observing using that handler like so:
     *
     *      $('foo').stopObserving('click', myHandler);
     *
     *  If we want to remove _all_ 'click' handlers from 'foo', we leave off the
     *  handler argument:
     *
     *      $('foo').stopObserving('click');
     *
     *  If we want to remove _all_ handlers for _all_ events from 'foo' (perhaps
     *  we're about to remove it from the DOM), we simply omit both the handler
     *  and the event name:
     *
     *      $('foo').stopObserving();
     *
     *  ##### A Common Error
     *
     *  When using instance methods as observers, it's common to use
     *  [[Function#bind]] on them, e.g.:
     *
     *      $('foo').observe('click', this.handlerMethod.bind(this));
     *
     *  If you do that, __this will not work__ to unregister the handler:
     *
     *      $('foo').stopObserving('click', this.handlerMethod.bind(this)); // <== WRONG
     *
     *  [[Function#bind]] returns a _new_ function every time it's called, and so
     *  if you don't retain the reference you used when observing, you can't
     *  unhook that function specifically. (You can still unhook __all__ handlers
     *  for an event, or all handlers on the element entirely.)
     *
     *  To do this, you need to keep a reference to the bound function:
     *
     *      this.boundHandlerMethod = this.handlerMethod.bind(this);
     *      $('foo').observe('click', this.boundHandlerMethod);
     *
     *  ...and then to remove:
     *
     *      $('foo').stopObserving('click', this.boundHandlerMethod); // <== Right
     **/
    function stopObserving(element, eventName, handler) {
        element = $(element);

        var registry = Element.retrieve(element, 'prototype_event_registry');
        if (!registry) return element;

        if (!eventName) {
            // We stop observing all events.
            // e.g.: $(element).stopObserving();
            registry.each(function(pair) {
                var eventName = pair.key;
                stopObserving(element, eventName);
            });
            return element;
        }

        var responders = registry.get(eventName);
        if (!responders) return element;

        if (!handler) {
            // We stop observing all handlers for the given eventName.
            // e.g.: $(element).stopObserving('click');
            responders.each(function(r) {
                stopObserving(element, eventName, r.handler);
            });
            return element;
        }

        var i = responders.length, responder;
        while (i--) {
            if (responders[i].handler === handler) {
                responder = responders[i];
                break;
            }
        }
        if (!responder) return element;

        if (eventName.include(':')) {
            // Custom event.
            if (element.removeEventListener)
                element.removeEventListener("dataavailable", responder, false);
            else {
                element.detachEvent("ondataavailable", responder);
                element.detachEvent("onlosecapture", responder);
            }
        } else {
            // Ordinary event.
            var actualEventName = _getDOMEventName(eventName);
            if (element.removeEventListener)
                element.removeEventListener(actualEventName, responder, false);
            else
                element.detachEvent('on' + actualEventName, responder);
        }

        registry.set(eventName, responders.without(responder));

        return element;
    }

    /**
     *  Event.fire(element, eventName[, memo[, bubble = true]]) -> Event
     *  - memo (?): Metadata for the event. Will be accessible to event
     *    handlers through the event's `memo` property.
     *  - bubble (Boolean): Whether the event should bubble.
     *
     *  Fires a custom event of name `eventName` with `element` as its target.
     *
     *  Custom events **must** include a colon (`:`) in their names.
     **/
    function fire(element, eventName, memo, bubble) {
        element = $(element);

        if (Object.isUndefined(bubble))
            bubble = true;

        if (element == document && document.createEvent && !element.dispatchEvent)
            element = document.documentElement;

        var event;
        if (document.createEvent) {
            event = document.createEvent('HTMLEvents');
            event.initEvent('dataavailable', bubble, true);
        } else {
            event = document.createEventObject();
            event.eventType = bubble ? 'ondataavailable' : 'onlosecapture';
        }

        event.eventName = eventName;
        event.memo = memo || { };

        if (document.createEvent)
            element.dispatchEvent(event);
        else
            element.fireEvent(event.eventType, event);

        return Event.extend(event);
    }

    /**
     *  class Event.Handler
     *
     *  Creates an observer on an element that listens for a particular event on
     *  that element's descendants, optionally filtering by a CSS selector.
     *
     *  This class simplifies the common "event delegation" pattern, in which one
     *  avoids adding an observer to a number of individual elements and instead
     *  listens on a _common ancestor_ element.
     *
     *  For more information on usage, see [[Event.on]].
     **/
    Event.Handler = Class.create({
                /**
                 *  new Event.Handler(element, eventName[, selector], callback)
                 *  - element (Element): The element to listen on.
                 *  - eventName (String): An event to listen for. Can be a standard browser
                 *    event or a custom event.
                 *  - selector (String): A CSS selector. If specified, will call `callback`
                 *    _only_ when it can find an element that matches `selector` somewhere
                 *    in the ancestor chain between the event's target element and the
                 *    given `element`.
                 *  - callback (Function): The event handler function. Should expect two
                 *    arguments: the event object _and_ the element that received the
                 *    event. (If `selector` was given, this element will be the one that
                 *    satisfies the criteria described just above; if not, it will be the
                 *    one specified in the `element` argument).
                 *
                 *  Instantiates an `Event.Handler`. **Will not** begin observing until
                 *  [[Event.Handler#start]] is called.
                 **/
                initialize: function(element, eventName, selector, callback) {
                    this.element = $(element);
                    this.eventName = eventName;
                    this.selector = selector;
                    this.callback = callback;
                    this.handler = this.handleEvent.bind(this);
                },

                /**
                 *  Event.Handler#start -> Event.Handler
                 *
                 *  Starts listening for events. Returns itself.
                 **/
                start: function() {
                    Event.observe(this.element, this.eventName, this.handler);
                    return this;
                },

                /**
                 *  Event.Handler#stop -> Event.Handler
                 *
                 *  Stops listening for events. Returns itself.
                 **/
                stop: function() {
                    Event.stopObserving(this.element, this.eventName, this.handler);
                    return this;
                },

                handleEvent: function(event) {
                    var element = Event.findElement(event, this.selector);
                    if (element) this.callback.call(this.element, event, element);
                }
            });

    /**
     *  Event.on(element, eventName[, selector], callback) -> Event.Handler
     *  - element (Element | String): The DOM element to observe, or its ID.
     *  - eventName (String): The name of the event, in all lower case, without
     *    the "on" prefix&nbsp;&mdash; e.g., "click" (not "onclick").
     *  - selector (String): A CSS selector. If specified, will call `callback`
     *    _only_ when it can find an element that matches `selector` somewhere
     *    in the ancestor chain between the event's target element and the
     *    given `element`.
     *  - callback (Function): The event handler function. Should expect two
     *    arguments: the event object _and_ the element that received the
     *    event. (If `selector` was given, this element will be the one that
     *    satisfies the criteria described just above; if not, it will be the
     *    one specified in the `element` argument). This function is **always**
     *    bound to `element`.
     *
     *  Listens for events on an element's descendants, optionally filtering
     *  to match a given CSS selector.
     *
     *  Creates an instance of [[Event.Handler]], calls [[Event.Handler#start]],
     *  then returns that instance. Keep a reference to this returned instance if
     *  you later want to unregister the observer.
     *
     *  ##### Usage
     *
     *  `Event.on` can be used to set up event handlers with or without event
     *  delegation. In its simplest form, it works just like [[Event.observe]]:
     *
     *      $("messages").on("click", function(event) {
     *        // ...
     *      });
     *
     *  An optional second argument lets you specify a CSS selector for event
     *  delegation. This encapsulates the pattern of using [[Event#findElement]]
     *  to retrieve the first ancestor element matching a specific selector.
     *
     *      $("messages").on("click", "a.comment", function(event, element) {
     *         // ...
     *      });
     *
     *  Note the second argument in the handler above: it references the
     *  element matched by the selector (in this case, an `a` tag with a class
     *  of `comment`). This argument is important to use because within the
     *  callback, the `this` keyword **will always refer to the original
     *  element** (in this case, the element with the id of `messages`).
     *
     *  `Event.on` differs from `Event.observe` in one other important way:
     *  its return value is an instance of [[Event.Handler]]. This instance
     *  has a `stop` method that will remove the event handler when invoked
     *  (and a `start` method that will attach the event handler again after
     *  it's been removed).
     *
     *      // Register the handler:
     *      var handler = $("messages").on("click", "a.comment",
     *       this.click.bind(this));
     *
     *      // Unregister the handler:
     *      handler.stop();
     *
     *      // Re-register the handler:
     *      handler.start();
     *
     *  This means that, unlike `Event.stopObserving`, there's no need to
     *  retain a reference to the handler function.
     **/
    function on(element, eventName, selector, callback) {
        element = $(element);
        if (Object.isFunction(selector) && Object.isUndefined(callback)) {
            callback = selector,selector = null;
        }

        return new Event.Handler(element, eventName, selector, callback).start();
    }

    Object.extend(Event, Event.Methods);

    Object.extend(Event, {
                fire:          fire,
                observe:       observe,
                stopObserving: stopObserving,
                on:            on
            });

    Element.addMethods({
                /**
                 *  Element.fire(@element, eventName[, memo[, bubble = true]]) -> Event
                 *  See [[Event.fire]].
                 *
                 *  Fires a custom event with the current element as its target.
                 *
                 *  [[Element.fire]] creates a custom event with the given name, then triggers
                 *  it on the given element. The custom event has all the same properties
                 *  and methods of native events. Like a native event, it will bubble up
                 *  through the DOM unless its propagation is explicitly stopped.
                 *
                 *  The optional second argument will be assigned to the `memo` property of
                 *  the event object so that it can be read by event handlers.
                 *
                 *  Custom events are dispatched synchronously: [[Element.fire]] waits until
                 *  the event finishes its life cycle, then returns the event itself.
                 *
                 *  ##### Note
                 *
                 *  [[Element.fire]] does not support firing native events. All custom event
                 *  names _must_ be namespaced (using a colon). This is to avoid custom
                 *  event names conflicting with non-standard native DOM events such as
                 *  `mousewheel` and `DOMMouseScroll`.
                 *
                 *  ##### Examples
                 *
                 *      document.observe("widget:frobbed", function(event) {
                 *        console.log("Element with ID (" + event.target.id +
                 *         ") frobbed widget #" + event.memo.widgetNumber + ".");
                 *      });
                 *
                 *      var someNode = $('foo');
                 *      someNode.fire("widget:frobbed", { widgetNumber: 19 });
                 *
                 *      //-> "Element with ID (foo) frobbed widget #19."
                 *
                 *  ##### Tip
                 *
                 *  Events that have been stopped with [[Event.stop]] will have a boolean
                 *  `stopped` property set to true. Since [[Element.fire]] returns the custom
                 *  event, you can inspect this property to determine whether the event was
                 *  stopped.
                 **/
                fire:          fire,

                /**
                 *  Element.observe(@element, eventName, handler) -> Element
                 *  See [[Event.observe]].
                 **/
                observe:       observe,

                /**
                 *  Element.stopObserving(@element[, eventName[, handler]]) -> Element
                 *  See [[Event.stopObserving]].
                 **/
                stopObserving: stopObserving,

                /**
                 *  Element.on(@element, eventName[, selector], callback) -> Element
                 *  See [[Event.on]].
                 **/
                on:            on
            });

    /** section: DOM
     *  document
     *
     *  Prototype extends the built-in `document` object with several convenience
     *  methods related to events.
     **/
    Object.extend(document, {
                /**
                 *  document.fire(eventName[, memo[, bubble = true]]) -> Event
                 *  - memo (?): Metadata for the event. Will be accessible through the
                 *    event's `memo` property.
                 *  - bubble (Boolean): Whether the event will bubble.
                 *
                 *  Fires a custom event of name `eventName` with `document` as the target.
                 *
                 *  `document.fire` is the document-wide version of [[Element.fire]].
                 *
                 *  Custom events must include a colon (`:`) in their names.
                 **/
                fire:          fire.methodize(),

                /**
                 *  document.observe(eventName, handler) -> Element
                 *
                 *  Listens for the given event over the entire document. Can also be used
                 *  for listening to `"dom:loaded"` event.
                 *
                 *  [[document.observe]] is the document-wide version of [[Element#observe]].
                 *  Using [[document.observe]] is equivalent to
                 *  `Event.observe(document, eventName, handler)`.
                 *
                 *  ##### The `"dom:loaded"` event
                 *
                 *  One really useful event generated by Prototype that you might want to
                 *  observe on the document is `"dom:loaded"`. On supporting browsers it
                 *  fires on `DOMContentLoaded` and on unsupporting browsers it simulates it
                 *  using smart workarounds. If you used `window.onload` before you might
                 *  want to switch to `dom:loaded` because it will fire immediately after
                 *  the HTML document is fully loaded, but _before_ images on the page are
                 *  fully loaded. The `load` event on `window` only fires after all page
                 *  images are loaded, making it unsuitable for some initialization purposes
                 *  like hiding page elements (so they can be shown later).
                 *
                 *  ##### Example
                 *
                 *      document.observe("dom:loaded", function() {
                 *        // initially hide all containers for tab content
                 *        $$('div.tabcontent').invoke('hide');
                 *      });
                 **/
                observe:       observe.methodize(),

                /**
                 *  document.stopObserving([eventName[, handler]]) -> Element
                 *
                 *  Unregisters an event handler from the document.
                 *
                 *  [[document.stopObserving]] is the document-wide version of
                 *  [[Element.stopObserving]].
                 **/
                stopObserving: stopObserving.methodize(),

                /**
                 *  Element.on(@element, eventName[, selector], callback) -> Event.Handler
                 *
                 *  See [[Event.on]].
                 **/
                on:            on.methodize(),

                /**
                 *  document.loaded -> Boolean
                 *
                 *  Whether the full DOM tree is ready for manipulation.
                 **/
                loaded:        false
            });

    // Export to the global scope.
    if (window.Event) Object.extend(window.Event, Event);
    else window.Event = Event;
})();

(function() {
    /* Support for the DOMContentLoaded event is based on work by Dan Webb,
     Matthias Miller, Dean Edwards, John Resig, and Diego Perini. */

    var timer;

    function fireContentLoadedEvent() {
        if (document.loaded) return;
        if (timer) window.clearTimeout(timer);
        document.loaded = true;
        document.fire('dom:loaded');
    }

    function checkReadyState() {
        if (document.readyState === 'complete') {
            document.stopObserving('readystatechange', checkReadyState);
            fireContentLoadedEvent();
        }
    }

    function pollDoScroll() {
        try {
            document.documentElement.doScroll('left');
        }
        catch(e) {
            timer = pollDoScroll.defer();
            return;
        }
        fireContentLoadedEvent();
    }

    if (document.addEventListener) {
        document.addEventListener('DOMContentLoaded', fireContentLoadedEvent, false);
    } else {
        document.observe('readystatechange', checkReadyState);
        if (window == top)
            timer = pollDoScroll.defer();
    }

    // Worst-case fallback
    Event.observe(window, 'load', fireContentLoadedEvent);
})();

Element.addMethods();
