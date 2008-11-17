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

window.logger = new Ice.Log.Logger([ 'window' ]);
window.console && window.console.firebug ? new Ice.Log.FirebugLogHandler(window.logger) : new Ice.Log.WindowLogHandler(window.logger, window);

[ Ice.Community ].as(function(This) {
    var views = window.views = window.views ? window.views : [];
    var registerView = function(session, view) {
        views.push(new Ice.Parameter.Association(session, view));
    };

    var deregisterAllViews = function() {
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

    var delistView = function(session, view) {
        views = views.reject(function(i) {
            return i.name == session && i.value == view;
        });
    };

    var disposeView = function(session, view) {
        delistView(session, view);
        sendDisposeViews([new Ice.Parameter.Association(session, view)]);
    };

    var disposeWindowViews = function() {
        sendDisposeViews(views);
        views.clear();
    };

    window.onBeforeUnload(disposeWindowViews);

    This.Application = Object.subclass({
        initialize: function(configuration, container) {
            var sessionID = configuration.session;
            var viewID = configuration.view;
            registerView(sessionID, viewID);
            var logger = window.logger.child(sessionID.substring(0, 4) + '#' + viewID);
            var statusManager = new Ice.Status.DefaultStatusManager(configuration, container);
            var scriptLoader = new Ice.Script.Loader(logger);
            var commandDispatcher = new Ice.Command.Dispatcher();
            var documentSynchronizer = new Ice.Document.Synchronizer(window.logger, sessionID, viewID);
            var parameters = Ice.Parameter.Query.create(function(query) {
                query.add('ice.session', sessionID);
                query.add('ice.view', viewID);
            });
            var replaceContainerHTML = function(html) {
                Ice.Document.replaceContainerHTML(container, html);
                scriptLoader.searchAndEvaluateScripts(container);
            };

            var connection = configuration.synchronous ?
                             new Ice.Connection.SyncConnection(logger, configuration.connection, parameters) :
                             new This.Connection.AsyncConnection(logger, sessionID, viewID, configuration.connection, parameters, commandDispatcher);
            var dispose = function() {
                dispose = Function.NOOP;
                documentSynchronizer.shutdown();
                connection.shutdown();
            };

            commandDispatcher.register('noop', Function.NOOP);
            commandDispatcher.register('set-cookie', Ice.Command.SetCookie);
            commandDispatcher.register('parsererror', Ice.Command.ParsingError);
            commandDispatcher.register('redirect', function(element) {
                //replace ampersand entities incorrectly decoded by Safari 2.0.4
                var url = element.getAttribute("url").replace(/&#38;/g, "&");
                logger.info('Redirecting to ' + url);
                window.location.href = url;
            });
            commandDispatcher.register('reload', function(element) {
                logger.info('Reloading');
                var url = window.location.href;
                deregisterAllViews();
                if (url.contains('rvn=')) {
                    window.location.reload();
                } else {
                    var view = element.getAttribute('view');
                    var queryPrefix = url.contains('?') ? '&' : '?';
                    window.location.href = url + queryPrefix + 'rvn=' + view;
                }
            });
            commandDispatcher.register('macro', function(message) {
                $enumerate(message.childNodes).each(function(subMessage) {
                    commandDispatcher.deserializeAndExecute(subMessage);
                });
            });
            commandDispatcher.register('updates', function(element) {
                $enumerate(element.getElementsByTagName('update')).each(function(updateElement) {
                    try {
                        var address = updateElement.getAttribute('address');
                        var update = new Ice.ElementModel.Update(updateElement);
                        address.asExtendedElement().updateDOM(update);
                        logger.debug('applied update : ' + update.asString());
                        scriptLoader.searchAndEvaluateScripts(address.asElement());
                        if (Ice.StateMon) {
                            Ice.StateMon.checkAll();
                            Ice.StateMon.rebuild();
                        }
                    } catch (e) {
                        logger.error('failed to insert element: ' + update.asString(), e);
                    }
                });
            });
            commandDispatcher.register('session-expired', function() {
                logger.warn('Session has expired');
                statusManager.sessionExpired.on();
                //avoid sending "dispose-views" request, the view is disposed by the server on session expiry
                delistView(sessionID, viewID);
                dispose();
            });

            window.onUnload(function() {
                dispose();
            });

            connection.onSend(function() {
                Ice.Focus.userInterupt = false;
                statusManager.busy.on();
            });

            connection.onReceive(function(response) {
                statusManager.busy.off();

                var mimeType = response.getResponseHeader('Content-Type');
                if (mimeType.startsWith('text/html')) {
                    replaceContainerHTML(response.content());
                } else if (mimeType.startsWith('text/xml')) {
                    commandDispatcher.deserializeAndExecute(response.contentAsDOM().documentElement);
                    documentSynchronizer.synchronize();
                } else {
                    logger.warn('unknown content in response');
                }
            });

            connection.onServerError(function (response) {
                logger.warn('server side error');
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
