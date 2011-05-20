/**
 *  == Ajax ==
 *
 *  Prototype's APIs around the `XmlHttpRequest` object.
 *
 *  The Prototype framework enables you to deal with Ajax calls in a manner that
 *  is both easy and compatible with all modern browsers.
 *
 *  Actual requests are made by creating instances of [[Ajax.Request]].
 *
 *  ##### Request headers
 *
 *  The following headers are sent with all Ajax requests (and can be
 *  overridden with the `requestHeaders` option described below):
 *
 *  * `X-Requested-With` is set to `XMLHttpRequest`.
 *  * `X-Prototype-Version` is set to Prototype's current version (e.g.,
 *    `<%= PROTOTYPE_VERSION %>`).
 *  * `Accept` is set to `text/javascript, text/html, application/xml,
 *     text/xml, * / *`
 *  * `Content-type` is automatically determined based on the `contentType`
 *    and `encoding` options.
 *
 *  ##### Ajax options
 *
 *  All Ajax classes share a common set of _options_ and _callbacks_.
 *  Callbacks are called at various points in the life-cycle of a request, and
 *  always feature the same list of arguments.
 *
 *  ##### Common options
 *
 *  * `asynchronous` ([[Boolean]]; default `true`): Determines whether
 *    `XMLHttpRequest` is used asynchronously or not. Synchronous usage is
 *    **strongly discouraged** &mdash; it halts all script execution for the
 *    duration of the request _and_ blocks the browser UI.
 *  * `contentType` ([[String]]; default `application/x-www-form-urlencoded`):
 *    The `Content-type` header for your request. Change this header if you
 *    want to send data in another format (like XML).
 *  * `encoding` ([[String]]; default `UTF-8`): The encoding for the contents
 *    of your request. It is best left as-is, but should weird encoding issues
 *    arise, you may have to tweak this.
 *  * `method` ([[String]]; default `post`): The HTTP method to use for the
 *    request. The other common possibility is `get`. Abiding by Rails
 *    conventions, Prototype also reacts to other HTTP verbs (such as `put` and
 *    `delete`) by submitting via `post` and adding a extra `_method` parameter
 *    with the originally-requested method.
 *  * `parameters` ([[String]]): The parameters for the request, which will be
 *    encoded into the URL for a `get` method, or into the request body for the
 *    other methods. This can be provided either as a URL-encoded string, a
 *    [[Hash]], or a plain [[Object]].
 *  * `postBody` ([[String]]): Specific contents for the request body on a
 *    `post` method. If it is not provided, the contents of the `parameters`
 *    option will be used instead.
 *  * `requestHeaders` ([[Object]]): A set of key-value pairs, with properties
 *    representing header names.
 *  * `evalJS` ([[Boolean]] | [[String]]; default `true`): Automatically `eval`s
 *    the content of [[Ajax.Response#responseText]] and populates
 *    [[Ajax.Response#responseJSON]] with it if the `Content-type` returned by
 *    the server is set to `application/json`. If the request doesn't obey
 *    same-origin policy, the content is sanitized before evaluation. If you
 *    need to force evalutation, pass `'force'`. To prevent it altogether, pass
 *    `false`.
 *  * `sanitizeJSON` ([[Boolean]]; default is `false` for same-origin requests,
 *    `true` otherwise): Sanitizes the contents of
 *    [[Ajax.Response#responseText]] before evaluating it.
 *
 *  ##### Common callbacks
 *
 *  When used on individual instances, all callbacks (except `onException`) are
 *  invoked with two parameters: the [[Ajax.Response]] object and the result of
 *  evaluating the `X-JSON` response header, if any (can be `null`).
 *
 *  For another way of describing their chronological order and which callbacks
 *  are mutually exclusive, see [[Ajax.Request]].
 *
 *  * `onCreate`: Triggered when the [[Ajax.Request]] object is initialized.
 *    This is _after_ the parameters and the URL have been processed, but
 *    _before_ opening the connection via the XHR object.
 *  * `onUninitialized` (*Not guaranteed*):  Invoked just after the XHR object
 *    is created.
 *  * `onLoading` (*Not guaranteed*): Triggered when the underlying XHR object
 *    is being setup, and its connection opened.
 *  * `onLoaded` (*Not guaranteed*): Triggered once the underlying XHR object
 *    is setup, the connection is open, and it is ready to send its actual
 *    request.
 *  * `onInteractive` (*Not guaranteed*): Triggered whenever the requester
 *    receives a part of the response (but not the final part), should it
 *    be sent in several packets.
 *  * `onSuccess`: Invoked when a request completes and its status code is
 *    `undefined` or belongs in the `2xy` family. This is skipped if a
 *    code-specific callback is defined (e.g., `on200`), and happens _before_
 *    `onComplete`.
 *  * `onFailure`: Invoked when a request completes and its status code exists
 *    but _is not_ in the `2xy` family. This is skipped if a code-specific
 *    callback is defined (e.g. `on403`), and happens _before_ `onComplete`.
 *  * `onXYZ` (_with `XYZ` representing any HTTP status code_): Invoked just
 *    after the response is complete _if_ the status code is the exact code
 *    used in the callback name. _Prevents_ execution of `onSuccess` and
 *    `onFailure`. Happens _before_ `onComplete`.
 *  * `onException`: Triggered whenever an XHR error arises. Has a custom
 *    signature: the first argument is the requester (i.e. an [[Ajax.Request]]
 *    instance), and the second is the exception object.
 *  * `onComplete`: Triggered at the _very end_ of a request's life-cycle, after
 *    the request completes, status-specific callbacks are called, and possible
 *    automatic behaviors are processed. Guaranteed to run regardless of what
 *    happened during the request.
 *
 **/

/** section: Ajax
 * Ajax
 **/

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

    /**
     *  Ajax.activeRequestCount -> Number
     *
     *  Represents the number of active XHR requests triggered through
     *  [[Ajax.Request]], [[Ajax.Updater]], or [[Ajax.PeriodicalUpdater]].
     **/
    activeRequestCount: 0
};

/** section: Ajax
 * Ajax.Responders
 *  includes Enumerable
 *
 *  A repository of global listeners notified about every step of
 *  Prototype-based Ajax requests.
 *
 *  Sometimes, you need to provide generic behaviors over all Ajax operations
 *  happening on the page (through [[Ajax.Request]], [[Ajax.Updater]] or
 *  [[Ajax.PeriodicalUpdater]]).
 *
 *  For instance, you might want to automatically show an indicator when an
 *  Ajax request is ongoing, and hide it when none are. You may well want to
 *  factor out exception handling as well, logging those somewhere on the page
 *  in a custom fashion. The possibilities are myriad.
 *
 *  To achieve this, Prototype provides `Ajax.Responders`, which lets you
 *  register (and, if you wish, unregister later) _responders_, which are
 *  objects with specially-named methods. These names come from a set of
 *  general callbacks corresponding to different points in time (or outcomes)
 *  of an Ajax request's life cycle.
 *
 *  For instance, Prototype automatically registers a responder that maintains
 *  a nifty variable: [[Ajax.activeRequestCount]]. This represents, at a given
 *  time, the number of currently active Ajax requests &mdash; by monitoring their
 *  `onCreate` and `onComplete` events. The code for this is fairly simple:
 *
 *      Ajax.Responders.register({
 *        onCreate: function() {
 *          Ajax.activeRequestCount++;
 *        },
 *        onComplete: function() {
 *          Ajax.activeRequestCount--;
 *        }
 *      });
 *
 *  ##### Responder callbacks
 *
 *  The callbacks for responders are similar to the callbacks described in
 *  the [[Ajax section]], but take a different signature. They're invoked with
 *  three parameters: the requester object (i.e., the corresponding "instance"
 *  of [[Ajax.Request]]), the `XMLHttpRequest` object, and the result of
 *  evaluating the `X-JSON` response header, if any (can be `null`). They also
 *  execute in the context of the responder, bound to the `this` reference.
 *
 *  * `onCreate`: Triggered whenever a requester object from the `Ajax`
 *    namespace is created, after its parameters are adjusted and before its
 *    XHR connection is opened. This takes *two* arguments: the requester
 *    object and the underlying XHR object.
 *  * `onUninitialized` (*Not guaranteed*):  Invoked just after the XHR object
 *    is created.
 *  * `onLoading` (*Not guaranteed*): Triggered when the underlying XHR object
 *    is being setup, and its connection opened.
 *  * `onLoaded` (*Not guaranteed*): Triggered once the underlying XHR object
 *    is setup, the connection is open, and it is ready to send its actual
 *    request.
 *  * `onInteractive` (*Not guaranteed*): Triggered whenever the requester
 *    receives a part of the response (but not the final part), should it
 *    be sent in several packets.
 *  * `onException`: Triggered whenever an XHR error arises. Has a custom
 *    signature: the first argument is the requester (i.e. an [[Ajax.Request]]
 *    instance), and the second is the exception object.
 *  * `onComplete`: Triggered at the _very end_ of a request's life-cycle, after
 *    the request completes, status-specific callbacks are called, and possible
 *    automatic behaviors are processed. Guaranteed to run regardless of what
 *    happened during the request.
 **/

Ajax.Responders = {
    responders: [],

    _each: function(iterator) {
        this.responders._each(iterator);
    },

    /**
     *  Ajax.Responders.register(responder) -> undefined
     *  - responder (Object): A list of functions with keys corresponding to the
     *    names of possible callbacks.
     *
     *  Add a group of responders to all Ajax requests.
     **/
    register: function(responder) {
        if (!this.include(responder))
            this.responders.push(responder);
    },

    /**
     *  Ajax.Responders.unregister(responder) -> undefined
     *  - responder (Object): A list of functions with keys corresponding to the
     *    names of possible callbacks.
     *
     *  Remove a previously-added group of responders.
     *
     *  As always, unregistering something requires you to use the very same
     *  object you used at registration. If you plan to use `unregister`, be sure
     *  to assign your responder to a _variable_ before passing it into
     *  [[Ajax.Responders#register]] &mdash; don't pass it an object literal.
     **/
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

// Abstract class; does not need documentation.
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

                if (Object.isHash(this.options.parameters))
                    this.options.parameters = this.options.parameters.toObject();
            }
        });

/** section: Ajax
 *  class Ajax.Request
 *
 *  Initiates and processes an Ajax request.
 *
 *  [[Ajax.Request]] is a general-purpose class for making HTTP requests which
 *  handles the life-cycle of the request, handles the boilerplate, and lets
 *  you plug in callback functions for your custom needs.
 *
 *  In the optional `options` hash, you usually provide an `onComplete` and/or
 *  `onSuccess` callback, unless you're in the edge case where you're getting a
 *  JavaScript-typed response, that will automatically be `eval`'d.
 *
 *  For a full list of common options and callbacks, see "Ajax options" heading
 *  of the [[Ajax section]].
 *
 *  ##### A basic example
 *
 *      new Ajax.Request('/your/url', {
 *        onSuccess: function(response) {
 *          // Handle the response content...
 *        }
 *      });
 *
 *  ##### Request life-cycle
 *
 *  Underneath our nice requester objects lies, of course, `XMLHttpRequest`. The
 *  defined life-cycle is as follows:
 *
 *  1. Created
 *  2. Initialized
 *  3. Request sent
 *  4. Response being received (can occur many times, as packets come in)
 *  5. Response received, request complete
 *
 *  As you can see under the "Ajax options" heading of the [[Ajax section]],
 *  Prototype's AJAX objects define a whole slew of callbacks, which are
 *  triggered in the following order:
 *
 *  1. `onCreate` (this is actually a callback reserved to [[Ajax.Responders]])
 *  2. `onUninitialized` (maps on Created)
 *  3. `onLoading` (maps on Initialized)
 *  4. `onLoaded` (maps on Request sent)
 *  5. `onInteractive` (maps on Response being received)
 *  6. `on`*XYZ* (numerical response status code), onSuccess or onFailure (see below)
 *  7. `onComplete`
 *
 *  The two last steps both map on *Response received*, in that order. If a
 *  status-specific callback is defined, it gets invoked. Otherwise, if
 *  `onSuccess` is defined and the response is deemed a success (see below), it
 *  is invoked. Otherwise, if `onFailure` is defined and the response is *not*
 *  deemed a sucess, it is invoked. Only after that potential first callback is
 *  `onComplete` called.
 *
 *  ##### A note on portability
 *
 *  Depending on how your browser implements `XMLHttpRequest`, one or more
 *  callbacks may never be invoked. In particular, `onLoaded` and
 *  `onInteractive` are not a 100% safe bet so far. However, the global
 *  `onCreate`, `onUninitialized` and the two final steps are very much
 *  guaranteed.
 *
 *  ##### `onSuccess` and `onFailure`, the under-used callbacks
 *
 *  Way too many people use [[Ajax.Request]] in a similar manner to raw XHR,
 *  defining only an `onComplete` callback even when they're only interested in
 *  "successful" responses, thereby testing it by hand:
 *
 *      // This is too bad, there's better!
 *      new Ajax.Request('/your/url', {
 *        onComplete: function(response) {
 *          if (200 == response.status)
 *            // yada yada yada
 *        }
 *      });
 *
 *  First, as described below, you could use better "success" detection: success
 *  is generally defined, HTTP-wise, as either no response status or a "2xy"
 *  response status (e.g., 201 is a success, too). See the example below.
 *
 *  Second, you could dispense with status testing altogether! Prototype adds
 *  callbacks specific to success and failure, which we listed above. Here's
 *  what you could do if you're only interested in success, for instance:
 *
 *      new Ajax.Request('/your/url', {
 *        onSuccess: function(response) {
 *            // yada yada yada
 *        }
 *      });
 *
 *  ##### Automatic JavaScript response evaluation
 *
 *  If an Ajax request follows the _same-origin policy_ **and** its response
 *  has a JavaScript-related `Content-type`, the content of the `responseText`
 *  property will automatically be passed to `eval`.
 *
 *  In other words: you don't even need to provide a callback to leverage
 *  pure-JavaScript Ajax responses. This is the convention that drives Rails's
 *  RJS.
 *
 *  The list of JavaScript-related MIME-types handled by Prototype is:
 *
 *  * `application/ecmascript`
 *  * `application/javascript`
 *  * `application/x-ecmascript`
 *  * `application/x-javascript`
 *  * `text/ecmascript`
 *  * `text/javascript`
 *  * `text/x-ecmascript`
 *  * `text/x-javascript`
 *
 *  The MIME-type string is examined in a case-insensitive manner.
 *
 *  ##### Methods you may find useful
 *
 *  Instances of the [[Ajax.Request]] object provide several methods that come
 *  in handy in your callback functions, especially once the request is complete.
 *
 *  ###### Is the response a successful one?
 *
 *  The [[Ajax.Request#success]] method examines the XHR object's `status`
 *  property and follows general HTTP guidelines: unknown status is deemed
 *  successful, as is the whole `2xy` status code family. It's a generally
 *  better way of testing your response than the usual
 *  `200 == transport.status`.
 *
 *  ###### Getting HTTP response headers
 *
 *  While you can obtain response headers from the XHR object using its
 *  `getResponseHeader` method, this makes for verbose code, and several
 *  implementations raise an exception when the header is not found. To make
 *  this easier, you can use the [[Ajax.Response#getHeader]] method, which
 *  delegates to the longer version and returns `null` if an exception occurs:
 *
 *      new Ajax.Request('/your/url', {
 *        onSuccess: function(response) {
 *          // Note how we brace against null values
 *          if ((response.getHeader('Server') || '').match(/Apache/))
 *            ++gApacheCount;
 *          // Remainder of the code
 *        }
 *      });
 *
 *  ##### Evaluating JSON headers
 *
 *  Some backends will return JSON not as response text, but in the `X-JSON`
 *  header. In this case, you don't even need to evaluate the returned JSON
 *  yourself, as Prototype automatically does so. It passes the result as the
 *  `headerJSON` property of the [[Ajax.Response]] object. Note that if there
 *  is no such header &mdash; or its contents are invalid &mdash; `headerJSON`
 *  will be set to `null`.
 *
 *      new Ajax.Request('/your/url', {
 *        onSuccess: function(transport) {
 *          transport.headerJSON
 *        }
 *      });
 **/
Ajax.Request = Class.create(Ajax.Base, {
            _complete: false,

            /**
             *  new Ajax.Request(url[, options])
             *  - url (String): The URL to fetch. When the _same-origin_ policy is in
             *    effect (as it is in most cases), `url` **must** be a relative URL or an
             *    absolute URL that starts with a slash (i.e., it must not begin with
             *    `http`).
             *  - options (Object): Configuration for the request. See the
             *    [[Ajax section]] for more information.
             *
             *  Creates a new `Ajax.Request`.
             **/
            initialize: function($super, url, options) {
                $super(options);
                this.transport = Ajax.getTransport();
                this.request(url);
            },

            request: function(url) {
                this.url = url;
                this.method = this.options.method;
                var params = Object.isString(this.options.parameters) ?
                        this.options.parameters :
                        Object.toQueryString(this.options.parameters);

                if (!['get', 'post'].include(this.method)) {
                    // simulate other verbs over post
                    params += (params ? '&' : '') + "_method=" + this.method;
                    this.method = 'post';
                }

                if (params && this.method === 'get') {
                    // when GET, append parameters to URL
                    this.url += (this.url.include('?') ? '&' : '?') + params;
                }

                this.parameters = params.toQueryParams();

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

            /**
             *  Ajax.Request#success() -> Boolean
             *
             *  Tests whether the request was successful.
             **/
            success: function() {
                var status = this.getStatus();
                return !status || (status >= 200 && status < 300) || status == 304;
            },

            getStatus: function() {
                try {
                    // IE sometimes returns 1223 for a 204 response.
                    if (this.transport.status === 1223) return 204;
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
                            || (this.options.evalJS && this.isSameOrigin() && contentType
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

            isSameOrigin: function() {
                var m = this.url.match(/^\s*https?:\/\/[^\/]*/);
                return !m || (m[0] == '#{protocol}//#{domain}#{port}'.interpolate({
                            protocol: location.protocol,
                            domain: document.domain,
                            port: location.port ? ':' + location.port : ''
                        }));
            },

            /**
             *  Ajax.Request#getHeader(name) -> String | null
             *  - name (String): The name of an HTTP header that may have been part of
             *    the response.
             *
             *  Returns the value of the given response header, or `null` if that header
             *  was not found.
             **/
            getHeader: function(name) {
                try {
                    return this.transport.getResponseHeader(name) || null;
                } catch (e) {
                    return null;
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

/** section: Ajax
 *  class Ajax.Response
 *
 *  A wrapper class around `XmlHttpRequest` for dealing with HTTP responses
 *  of Ajax requests.
 *
 *  An instance of [[Ajax.Response]] is passed as the first argument of all Ajax
 *  requests' callbacks. You _will not_ need to create instances of
 *  [[Ajax.Response]] yourself.
 **/

/**
 *  Ajax.Response#readyState -> Number
 *
 *  A [[Number]] corresponding to the request's current state.
 *
 *  `0` : `"Uninitialized"`<br />
 *  `1` : `"Loading"`<br />
 *  `2` : `"Loaded"`<br />
 *  `3` : `"Interactive"`<br />
 *  `4` : `"Complete"`
 **/

/**
 *  Ajax.Response#responseText -> String
 *
 *  The text body of the response.
 **/

/**
 *  Ajax.Response#responseXML -> document | null
 *
 *  The XML body of the response if the `Content-type` of the request is set
 *  to `application/xml`; `null` otherwise.
 **/

/**
 *  Ajax.Response#responseJSON -> Object | Array | null
 *
 *  The JSON body of the response if the `Content-type` of the request is set
 *  to `application/json`; `null` otherwise.
 **/

/**
 *  Ajax.Response#headerJSON -> Object | Array | null
 *
 *  Auto-evaluated content of the `X-JSON` header if present; `null` otherwise.
 *  This is useful to transfer _small_ amounts of data.
 **/

/**
 *  Ajax.Response#request -> Ajax.Request | Ajax.Updater
 *
 *  The request object itself (an instance of [[Ajax.Request]] or
 *  [[Ajax.Updater]]).
 **/

/**
 *  Ajax.Response#transport -> XmlHttpRequest
 *
 *  The native `XmlHttpRequest` object itself.
 **/

Ajax.Response = Class.create({
            // Don't document the constructor; should never be manually instantiated.
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

            /**
             *  Ajax.Response#status -> Number
             *
             *  The HTTP status code sent by the server.
             **/
            status:      0,

            /**
             *  Ajax.Response#statusText -> String
             *
             *  The HTTP status text sent by the server.
             **/
            statusText: '',

            getStatus: Ajax.Request.prototype.getStatus,

            getStatusText: function() {
                try {
                    return this.transport.statusText || '';
                } catch (e) {
                    return ''
                }
            },

            /**
             *  Ajax.Response#getHeader(name) -> String | null
             *
             *  See [[Ajax.Request#getHeader]].
             **/
            getHeader: Ajax.Request.prototype.getHeader,

            /**
             *  Ajax.Response#getAllHeaders() -> String | null
             *
             *  Returns a [[String]] containing all headers separated by line breaks.
             *  _Does not_ throw errors if no headers are present the way its native
             *  counterpart does.
             **/
            getAllHeaders: function() {
                try {
                    return this.getAllResponseHeaders();
                } catch (e) {
                    return null
                }
            },

            /**
             *  Ajax.Response#getResponseHeader(name) -> String
             *
             *  Returns the value of the requested header if present; throws an error
             *  otherwise. This is just a wrapper around the `XmlHttpRequest` method of
             *  the same name. Prefer it's shorter counterpart:
             *  [[Ajax.Response#getHeader]].
             **/
            getResponseHeader: function(name) {
                return this.transport.getResponseHeader(name);
            },

            /**
             *  Ajax.Response#getAllResponseHeaders() -> String
             *
             *  Returns a [[String]] containing all headers separated by line breaks; throws
             *  an error if no headers exist. This is just a wrapper around the
             *  `XmlHttpRequest` method of the same name. Prefer it's shorter counterpart:
             *  [[Ajax.Response#getAllHeaders]].
             **/
            getAllResponseHeaders: function() {
                return this.transport.getAllResponseHeaders();
            },

            _getHeaderJSON: function() {
                var json = this.getHeader('X-JSON');
                if (!json) return null;
                json = decodeURIComponent(escape(json));
                try {
                    return json.evalJSON(this.request.options.sanitizeJSON ||
                            !this.request.isSameOrigin());
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
                    return this.responseText.evalJSON(options.sanitizeJSON ||
                            !this.request.isSameOrigin());
                } catch (e) {
                    this.request.dispatchException(e);
                }
            }
        });

/** section: Ajax
 *  class Ajax.Updater < Ajax.Request
 *
 *  A class that performs an Ajax request and updates a container's contents
 *  with the contents of the response.
 *
 *  [[Ajax.Updater]] is a subclass of [[Ajax.Request]] built for a common
 *  use-case.
 *
 *  ##### Example
 *
 *      new Ajax.Updater('items', '/items', {
 *        parameters: { text: $F('text') }
 *      });
 *
 *  This example will make a request to the URL `/items` (with the given
 *  parameters); it will then replace the contents of the element with the ID
 *  of `items` with whatever response it receives.
 *
 *  ##### Callbacks
 *
 *  [[Ajax.Updater]] supports all the callbacks listed in the [[Ajax section]].
 *  Note that the `onComplete` callback will be invoked **after** the element
 *  is updated.
 *
 *  ##### Additional options
 *
 *  [[Ajax.Updater]] has some options of its own apart from the common options
 *  described in the [[Ajax section]]:
 *
 *  * `evalScripts` ([[Boolean]]; defaults to `false`): Whether `<script>`
 *    elements in the response text should be evaluated.
 *  * `insertion` ([[String]]): By default, `Element.update` is used, meaning
 *    the contents of the response will replace the entire contents of the
 *    container. You may _instead_ insert the response text without disrupting
 *    existing contents. The `insertion` option takes one of four strings &mdash;
 *    `top`, `bottom`, `before`, or `after` &mdash; and _inserts_ the contents
 *    of the response in the manner described by [[Element#insert]].
 *
 *  ##### More About `evalScripts`
 *
 *  If you use `evalScripts: true`, any _inline_ `<script>` block will be
 *  evaluated. This **does not** mean it will be evaluated in the global scope;
 *  it won't, and that has important ramifications for your `var` and `function`
 *  statements.  Also note that only inline `<script>` blocks are supported;
 *  external scripts are ignored. See [[String#evalScripts]] for the details.
 *
 *  ##### Single container, or success/failure split?
 *
 *  The examples above all assume you're going to update the same container
 *  whether your request succeeds or fails. Instead, you may want to update
 *  _only_ for successful requests, or update a _different container_ on failed
 *  requests.
 *
 *  To achieve this, you can pass an object instead of a DOM element for the
 *  `container` parameter. This object _must_ have a `success` property whose
 *  value identifies the container to be updated on successful requests.
 *
 *  If you also provide it with a `failure` property, its value will be used as
 *  the container for failed requests.
 *
 *  In the following code, only successful requests get an update:
 *
 *      new Ajax.Updater({ success: 'items' }, '/items', {
 *        parameters: { text: $F('text') },
 *        insertion: 'bottom'
 *      });
 *
 *  This next example assumes failed requests will deliver an error message as
 *  response text &mdash; one that should be shown to the user in another area:
 *
 *      new Ajax.Updater({ success: 'items', failure: 'notice' }, '/items',
 *        parameters: { text: $F('text') },
 *        insertion: 'bottom'
 *      });
 *
 **/

Ajax.Updater = Class.create(Ajax.Request, {
            /**
             *  new Ajax.Updater(container, url[, options])
             *  - container (String | Element): The DOM element whose contents to update
             *    as a result of the Ajax request. Can be a DOM node or a string that
             *    identifies a node's ID.
             *  - url (String): The URL to fetch. When the _same-origin_ policy is in
             *    effect (as it is in most cases), `url` **must** be a relative URL or an
             *    absolute URL that starts with a slash (i.e., it must not begin with
             *    `http`).
             *  - options (Object): Configuration for the request. See the
             *    [[Ajax section]] for more information.
             *
             *  Creates a new `Ajax.Updater`.
             **/
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

/** section: Ajax
 *  class Ajax.PeriodicalUpdater
 *
 *  Periodically performs an Ajax request and updates a container's contents
 *  based on the response text.
 *
 *  [[Ajax.PeriodicalUpdater]] behaves like [[Ajax.Updater]], but performs the
 *  update at a prescribed interval, rather than only once. (Note that it is
 *  _not_ a subclass of [[Ajax.Updater]]; it's a wrapper around it.)
 *
 *  This class addresses the common need of periodical update, as required by
 *  all sorts of "polling" mechanisms (e.g., an online chatroom or an online
 *  mail client).
 *
 *  The basic idea is to run a regular [[Ajax.Updater]] at regular intervals,
 *  keeping track of the response text so it can (optionally) react to
 *  receiving the exact same response consecutively.
 *
 *  ##### Additional options
 *
 *  [[Ajax.PeriodicalUpdater]] features all the common options and callbacks
 *  described in the [[Ajax section]] &mdash; _plus_ those added by
 *  [[Ajax.Updater]].
 *
 *  It also provides two new options:
 *
 *  * `frequency` ([[Number]]; default is `2`): How long, in seconds, to wait
 *    between the end of one request and the beginning of the next.
 *  * `decay` ([[Number]]; default is `1`): The rate at which the `frequency`
 *    grows when the response received is _exactly_ the same as the previous.
 *    The default of `1` means `frequency` will never grow; override the
 *    default if a stale response implies it's worthwhile to poll less often.
 *    If `decay` is set to `2`, for instance, `frequency` will double
 *    (2 seconds, 4 seconds, 8 seconds...) each consecutive time the result
 *    is the same; when the result is different once again, `frequency` will
 *    revert to its original value.
 *
 *  ##### Disabling and re-enabling a [[Ajax.PeriodicalUpdater]]
 *
 *  You can hit the brakes on a running [[Ajax.PeriodicalUpdater]] by calling
 *  [[Ajax.PeriodicalUpdater#stop]]. If you wish to re-enable it later, call
 *  [[Ajax.PeriodicalUpdater#start]].
 *
 **/

Ajax.PeriodicalUpdater = Class.create(Ajax.Base, {
            /**
             *  new Ajax.PeriodicalUpdater(container, url[, options])
             *  - container (String | Element): The DOM element whose contents to update
             *    as a result of the Ajax request. Can be a DOM node or a string that
             *    identifies a node's ID.
             *  - url (String): The URL to fetch. When the _same-origin_ policy is in
             *    effect (as it is in most cases), `url` **must** be a relative URL or an
             *    absolute URL that starts with a slash (i.e., it must not begin with
             *    `http`).
             *  - options (Object): Configuration for the request. See the
             *    [[Ajax section]] for more information.
             *
             *  Creates a new [[Ajax.PeriodicalUpdater]].
             *
             *  Periodically performs an AJAX request and updates a container's contents
             *  based on the response text. Offers a mechanism for "decay," which lets it
             *  trigger at widening intervals while the response is unchanged.
             *
             *  This object addresses the common need of periodical update, which is used
             *  by all sorts of "polling" mechanisms (e.g. in an online chatroom or an
             *  online mail client).
             *
             *  The basic idea is to run a regular [[Ajax.Updater]] at
             *  regular intervals, monitoring changes in the response text if the `decay`
             *  option (see below) is active.
             *
             *  ##### Additional options
             *
             *  [[Ajax.PeriodicalUpdater]] features all the common options and callbacks
             *  (see the [[Ajax section]] for more information), plus those added by
             *  [[Ajax.Updater]]. It also provides two new options that deal with the
             *  original period, and its decay rate (how Rocket Scientist does that make
             *  us sound, uh?!).
             *
             *  <table>
             *  <thead>
             *    <tr>
             *      <th>Option</th>
             *      <th>Default</th>
             *      <th>Description</th>
             *    </tr>
             *  </thead>
             *  <tbody>
             *    <tr>
             *      <th><code>frequency</code></th>
             *      <td><code>2</code></td>
             *  <td>Okay, this is not a frequency (e.g 0.5Hz), but a period (i.e. a number of seconds).
             *  Don't kill me, I didn't write this one! This is the minimum interval at which AJAX
             *  requests are made. You don't want to make it too short (otherwise you may very well
             *  end up with multiple requests in parallel, if they take longer to process and return),
             *  but you technically can provide a number below one, e.g. 0.75 second.</td>
             *    </tr>
             *    <tr>
             *      <th><code>decay</code></th>
             *      <td>1</td>
             *  <td>This controls the rate at which the request interval grows when the response is
             *  unchanged. It is used as a multiplier on the current period (which starts at the original
             *  value of the <code>frequency</code> parameter). Every time a request returns an unchanged
             *  response text, the current period is multiplied by the decay. Therefore, the default
             *  value means regular requests (no change of interval). Values higher than one will
             *  yield growing intervals. Values below one are dangerous: the longer the response text
             *  stays the same, the more often you'll check, until the interval is so short your browser
             *  is left with no other choice than suicide. Note that, as soon as the response text
             *  <em>does</em> change, the current period resets to the original one.</td>
             *    </tr>
             *  </tbody>
             *  </table>
             *
             *  To better understand decay, here is a small sequence of calls from the
             *  following example:
             *
             *      new Ajax.PeriodicalUpdater('items', '/items', {
             *        method: 'get', frequency: 3, decay: 2
             *      });
             *
             *  <table id="decayTable">
             *  <thead>
             *    <tr>
             *      <th>Call#</th>
             *      <th>When?</th>
             *      <th>Decay before</th>
             *      <th>Response changed?</th>
             *      <th>Decay after</th>
             *      <th>Next period</th>
             *      <th>Comments</th>
             *    </tr>
             *  </thead>
             *  <tbody>
             *    <tr>
             *      <td>1</td>
             *      <td>00:00</td>
             *      <td>2</td>
             *      <td>n/a</td>
             *      <td>1</td>
             *      <td>3</td>
             *  <td>Response is deemed changed, since there is no prior response to compare to!</td>
             *    </tr>
             *    <tr>
             *      <td>2</td>
             *      <td>00:03</td>
             *      <td>1</td>
             *      <td>yes</td>
             *      <td>1</td>
             *      <td>3</td>
             *  <td>Response did change again: we "reset" to 1, which was already the decay.</td>
             *    </tr>
             *    <tr>
             *      <td>3</td>
             *      <td>00:06</td>
             *      <td>1</td>
             *      <td>no</td>
             *      <td>2</td>
             *      <td>6</td>
             *  <td>Response didn't change: decay augments by the <code>decay</code> option factor:
             *  we're waiting longer now&#8230;</td>
             *    </tr>
             *    <tr>
             *      <td>4</td>
             *      <td>00:12</td>
             *      <td>2</td>
             *      <td>no</td>
             *      <td>4</td>
             *      <td>12</td>
             *      <td>Still no change, doubling again.</td>
             *    </tr>
             *    <tr>
             *      <td>5</td>
             *      <td>00:24</td>
             *      <td>4</td>
             *      <td>no</td>
             *      <td>8</td>
             *      <td>24</td>
             *      <td>Jesus, is this thing going to change or what?</td>
             *    </tr>
             *    <tr>
             *      <td>6</td>
             *      <td>00:48</td>
             *      <td>8</td>
             *      <td>yes</td>
             *      <td>1</td>
             *      <td>3</td>
             *  <td>Ah, finally! Resetting decay to 1, and therefore using the original period.</td>
             *    </tr>
             *  </tbody>
             *  </table>
             *
             *  ##### Disabling and re-enabling a [[Ajax.PeriodicalUpdater]]
             *
             *  You can pull the brake on a running [[Ajax.PeriodicalUpdater]] by simply
             *  calling its `stop` method. If you wish to re-enable it later, just call
             *  its `start` method. Both take no argument.
             *
             *  ##### Beware!  Not a specialization!
             *
             *  [[Ajax.PeriodicalUpdater]] is not a specialization of [[Ajax.Updater]],
             *  despite its name. When using it, do not expect to be able to use methods
             *  normally provided by [[Ajax.Request]] and "inherited" by [[Ajax.Updater]],
             *  such as `evalJSON` or `getHeader`. Also the `onComplete` callback is
             *  hijacked to be used for update management, so if you wish to be notified
             *  of every successful request, use `onSuccess` instead (beware: it will get
             *  called *before* the update is performed).
             **/
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

            /**
             *  Ajax.PeriodicalUpdater#start() -> undefined
             *
             *  Starts the periodical updater (if it had previously been stopped with
             *  [[Ajax.PeriodicalUpdater#stop]]).
             **/
            start: function() {
                this.options.onComplete = this.updateComplete.bind(this);
                this.onTimerEvent();
            },

            /**
             *  Ajax.PeriodicalUpdater#stop() -> undefined
             *
             *  Stops the periodical updater.
             *
             *  Also calls the `onComplete` callback, if one has been defined.
             **/
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

