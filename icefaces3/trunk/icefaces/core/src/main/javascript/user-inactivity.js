/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

(function() {
    var userInactivityListeners = [];
    var isUserInactivityMonitorStarted = false;

    namespace.onUserInactivity = function(timeout, callback) {
        if (!isUserInactivityMonitorStarted) {
            isUserInactivityMonitorStarted = true;
            observeUserInactivity();
        }
        var tuple = {t: (timeout * 1000), c: callback};
        append(userInactivityListeners, tuple);
        return removeCallbackCallback(userInactivityListeners, detectByReference(tuple));
    };

    function observeUserInactivity() {
        var userActivityMonitor = Delay(function() {
            var now = (new Date).getTime();
            userInactivityListeners = reject(userInactivityListeners, function(tuple) {
                var timeout = tuple.t;
                var runCallback = now > lastActivityTime + timeout;
                if (runCallback) {
                    var callback = tuple.c;
                    try {
                        callback();
                    } catch (ex) {
                        warn('onUserInactivity callback failed to run', ex);
                    }
                }

                return runCallback;
            });
        }, 3 * 1000);//poll every 3 seconds
        run(userActivityMonitor);

        var stopActivityMonitor = curry(stop, userActivityMonitor);
        namespace.onSessionExpiry(stopActivityMonitor);
        namespace.onNetworkError(stopActivityMonitor);
        namespace.onServerError(stopActivityMonitor);
        namespace.onUnload(stopActivityMonitor);

        var lastActivityTime = (new Date).getTime();
        function resetUserInactivity() {
            lastActivityTime = (new Date).getTime();
        }
        registerListener('keydown', document, resetUserInactivity);
        registerListener('mouseover', document, resetUserInactivity);
    }
})();