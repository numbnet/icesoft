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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 */

window.logger = new Ice.Log.Logger([ 'window' ]);
window.console && window.console.firebug ? new Ice.Log.FirebugLogHandler(window.logger) : new Ice.Log.WindowLogHandler(window.logger, window);

[ Ice.Community ].as(function(This) {
    var registerSession = function(sessionID) {
        try {
            var sessionsCookie = Ice.Cookie.lookup('ice.sessions');
            var sessions = sessionsCookie.loadValue().split(' ');
            var alreadyRegistered = function(s) {
                return s.startsWith(sessionID);
            };
            var session = sessions.detect(alreadyRegistered);
            if (session) {
                sessions = sessions.reject(alreadyRegistered);
                var occurences = session.split('#')[1].asNumber() + 1;
                sessions.push(sessionID + '#' + occurences);
            } else {
                sessions.push(sessionID + '#' + 1);
            }
            sessionsCookie.saveValue(sessions.join(' '));
        } catch (e) {
            new Ice.Cookie('ice.sessions', sessionID + '#' + 1);
        }
    };

    var deregisterSession = function(sessionID) {
        //cookie can be gone because its value was emptied when all the windows where disposed
        if (Ice.Cookie.exists('ice.sessions')) {
            var sessionsCookie = Ice.Cookie.lookup('ice.sessions');
            var sessions = sessionsCookie.loadValue().split(' ');
            sessionsCookie.saveValue(sessions.inject([],
                    function(tally, s) {
                        if (s) {
                            var entry = s.split('#');
                            var id = entry[0];
                            var occurences = entry[1].asNumber();
                            if (id == sessionID) {
                                --occurences;
                            }
                            if (occurences > 0) {
                                tally.push(entry[0] + '#' + occurences);
                            }
                        }
                        return tally;
                    }).join(' '));
        }
    };

    var views = window.views = window.views ? window.views : [];
    var registerView = function(session, view) {
        registerSession(session);
        views.push(new Ice.Parameter.Association(session, view));
    };

    var deregisterWindowViews = function() {
        views.each(function(v) {
            deregisterSession(v.name);
        });
        views.clear();
    };

    var channel = new Ice.Ajax.Client(logger.child('dispose'));
    var sendDisposeViews = function(parameters) {
        if (parameters.isEmpty()) return;
        try {
            var query = parameters.inject(new Ice.Parameter.Query(), function(query, parameter) {
                return query.addParameter(parameter);
            });

            channel.postSynchronously(window.disposeViewsURI, query.asURIEncodedString(), function(request) {
                Ice.Connection.FormPost(request);
                request.on(Ice.Connection.OK, Ice.Connection.Close);
            });
        } catch (e) {
            logger.warn('Failed to notify view disposal', e);
        }
    };

    var deregisterView = function(session, view) {
        deregisterSession(session);
        views = views.reject(function(i) {
            return i.name == session && i.value == view;
        });
    };

    var disposeView = function(session, view) {
        deregisterView(session, view);
        sendDisposeViews([new Ice.Parameter.Association(session, view)]);
    };

    var disposeWindowViews = function() {
        sendDisposeViews(views);
        deregisterWindowViews();
    };

    window.onBeforeUnload(disposeWindowViews);

    This.Application = Object.subclass({
        initialize: function(configuration, container) {
            var optimizedJSListenerCleanup = configuration.optimizedJSListenerCleanup;
            var sessionID = configuration.session;
            var viewID = configuration.view;
            registerView(sessionID, viewID);
            var logger = window.logger.child(sessionID.substring(0, 4) + '#' + viewID);
            var statusManager = new Ice.Status.DefaultStatusManager(configuration, container);
            var scriptLoader = new Ice.Script.Loader(logger);
            var commandDispatcher = new Ice.Command.Dispatcher();
            var documentSynchronizer = new Ice.Document.Synchronizer(window.logger, sessionID, viewID, optimizedJSListenerCleanup);
            var parameters = Ice.Parameter.Query.create(function(query) {
                query.add('ice.session', sessionID);
                query.add('ice.view', viewID);
            });
            var replaceContainerHTML = function(html) {
                Ice.Document.replaceContainerHTML(container, html, optimizedJSListenerCleanup);
                scriptLoader.searchAndEvaluateScripts(container);
            };

            var connection = configuration.synchronous ?
                    new Ice.Connection.SyncConnection(logger, configuration.connection, parameters) :
                    new This.Connection.AsyncConnection(logger, sessionID, viewID, configuration.connection, parameters, commandDispatcher);
            var dispose = function() {
                try {
                    dispose = Function.NOOP;
                    connection.shutdown();
                    //                    documentSynchronizer.shutdown();
                } finally {
                    //clean-up reference to the bridge instance
                    container.bridge = null;
                }
            };

            var sessionExpiredListeners = [];
            var onSessionExpired = function(callback) {
                sessionExpiredListeners.push(callback);
            };

            commandDispatcher.register('noop', Function.NOOP);
            commandDispatcher.register('set-cookie', Ice.Command.SetCookie);
            commandDispatcher.register('parsererror', Ice.Command.ParsingError);
            commandDispatcher.register('redirect', function(element) {
                dispose();
                //replace ampersand entities incorrectly decoded by Safari 2.0.4
                var url = element.getAttribute("url").replace(/&#38;/g, "&");
                logger.info('Redirecting to ' + url);
                window.location.href = url;
            });
            commandDispatcher.register('reload', function(element) {
                logger.info('Reloading');
                var url = window.location.href;
                sendDisposeViews = Function.NOOP;
                dispose();
                if (url.contains('rvn=')) {
                    window.location.reload();
                } else {
                    var view = element.getAttribute('view');
                    if (view == '') {
                        window.location.reload();
                    } else {
                        var queryPrefix = url.contains('?') ? '&' : '?';
                        window.location.href = url + queryPrefix + 'rvn=' + view;
                    }
                }
            });
            commandDispatcher.register('macro', function(message) {
                $enumerate(message.childNodes).each(function(subMessage) {
                    commandDispatcher.deserializeAndExecute(subMessage);
                });
            });
            commandDispatcher.register('updates', function(element) {
                var numUpdate = 0;
                $enumerate(element.getElementsByTagName('update')).each(function(updateElement) {
                    try {
                        var address = updateElement.getAttribute('address');
                        var update = new Ice.ElementModel.Update(updateElement);
                        var extElem = address.asExtendedElement();
                        extElem.updateDOM(update, optimizedJSListenerCleanup);
                        logger.debug('applied update : ' + update.asString());
                        var updatedElement = address.asElement();
                        Ice.Focus.captureFocusIn(updatedElement);
                        scriptLoader.searchAndEvaluateScripts(updatedElement);
                        numUpdate++;
                    } catch (e) {
                        logger.error('failed to insert element: ' + update.asString(), e);
                    }
                });
                if (numUpdate > 0) {
                    if (Ice.StateMon) {
                        Ice.StateMon.checkAll();
                        Ice.StateMon.rebuild();
                    }
                }
            });
            commandDispatcher.register('session-expired', function() {
                try {
                    logger.warn('Session has expired');
                    statusManager.busy.off();
                    statusManager.sessionExpired.on();
                    //avoid sending "dispose-views" request, the view is disposed by the server on session expiry
                    deregisterView(sessionID, viewID);
                } finally {
                    dispose();
                    sessionExpiredListeners.broadcast();
                }
            });

            window.onUnload(function() {
                dispose();
            });


            var blockUI;
            if (configuration.connection.blockUI) {
                var blockUIOverlay = new Ice.Status.OverlayIndicator(configuration);
                var cancelEventCallback = function() {
                    return false;
                };
                blockUI = function() {
                    blockUIOverlay.on();
                    var rollbacks = ['input', 'select', 'textarea', 'button', 'a'].inject([], function(result, type) {
                        return result.concat($enumerate(container.getElementsByTagName(type)).collect(function(e) {
                            if (e.hasCallbacksDisabled) {
                                //return No-Op rollback function when the callbacks are already disabled
                                return Function.NOOP;
                            } else {
                                e.hasCallbacksDisabled = true;

                                var onkeypress = e.onkeypress;
                                var onkeyup = e.onkeyup;
                                var onkeydown = e.onkeydown;
                                var onclick = e.onclick;
                                e.onkeypress = cancelEventCallback;
                                e.onkeyup = cancelEventCallback;
                                e.onkeydown = cancelEventCallback;
                                e.onclick = cancelEventCallback;

                                return function() {
                                    e.onkeypress = onkeypress;
                                    e.onkeyup = onkeyup;
                                    e.onkeydown = onkeydown;
                                    e.onclick = onclick;

                                    e.hasCallbacksDisabled = null;
                                };
                            }
                        }));
                    });

                    return function() {
                        rollbacks.broadcast();
                        blockUIOverlay.off();
                    };
                };
            } else {
                blockUI = function() {
                    return Function.NOOP;
                };
            }

            connection.onSend(function(onReceive, ignoreLocking) {
                var unblockUI = ignoreLocking ? Function.NOOP : blockUI();
                statusManager.busy.on();

                onReceive(function() {
                    statusManager.busy.off();
                    unblockUI();
                });
            });

            connection.onReceive(function(response) {
                var mimeType = response.getResponseHeader('Content-Type');
                if (mimeType.startsWith('text/html')) {
                    replaceContainerHTML(response.content());
                } else if (mimeType.startsWith('text/xml')) {
                    documentSynchronizer.synchronize();
                    commandDispatcher.deserializeAndExecute(response.contentAsDOM().documentElement);
                } else {
                    logger.warn('unknown content in response');
                }
                statusManager.connectionTrouble.off();
            });

            connection.onServerError(function (response) {
                logger.warn('server side error');
                statusManager.busy.off();
                disposeView(sessionID, viewID);
                if (response.isEmpty()) {
                    statusManager.serverError.on();
                } else {
                    replaceContainerHTML(response.content());
                }
                dispose();
            });

            connection.whenDown(function() {
                logger.warn('connection to server was lost');
                statusManager.busy.off();
                statusManager.connectionLost.on();
                dispose();
            });

            connection.whenTrouble(function() {
                logger.warn('connection in trouble');
                statusManager.connectionTrouble.on();
            });

            //public method used to modify bridge's status manager
            this.attachStatusManager = function(setup) {
                statusManager.off();
                statusManager = setup(new Ice.Status.DefaultStatusManager(configuration, container));
                logger.info("status indicators were updated");
            };
            //public methods
            this.connection = connection;
            this.onSessionExpired = onSessionExpired;
            this.dispose = dispose;
            this.disposeAndNotify = function() {
                disposeView(sessionID, viewID);
                dispose();
            };
            logger.info('bridge loaded!');
        }
    });
});

window.onKeyPress(function(e) {
    if (e.isEscKey()) e.cancelDefaultAction();
});

//***
//clean-up window custom properties and standard event handlers
window.onUnload(function() {
    window.logger = null;
    window.views = null;
    window.disposeViewsURI = null;
    window.disposeOnViewRemoval = null;
    window.$element = null;
    window.$event = null;
    window.$enumerate = null;
});