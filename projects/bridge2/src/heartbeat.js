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
var startBeat = operator();
var stopBeat = operator();
var onPing = operator();
var onLostPongs = operator();

[ Ice.Reliability = new Object ].as(function(This) {
    var notify = operator();
    var reset = operator();
    This.CoalescingListener = function(retries, callback) {
        var count = 0;
        return object(function(method) {
            method(notify, function(self) {
                if (++count == retries) {
                    try {
                        callback();
                    } finally {
                        reset(self);
                    }
                }
            });

            method(reset, function(self) {
                count = 0;
            });
        });
    };

    This.Heartbeat = function(period, timeout, logger) {
        var logger = logger.child('heartbeat');
        var pingListeners = [];
        var lostPongListeners = [];

        var beat = Delay(function() {
            var timeoutBomb = runOnce(Delay(function() {
                logger.warn('pong lost');
                each(lostPongListeners, notify);
            }, timeout));

            logger.info('ping');
            broadcast(pingListeners, [function() {
                logger.info('pong');
                each(lostPongListeners, reset);
                stop(timeoutBomb);
            }]);
        }, period);

        return object(function(method) {
            method(startBeat, function(self) {
                run(beat);
                logger.info('heartbeat started');

                return self;
            });

            method(stopBeat, function(self) {
                stop(beat);

                empty(pingListeners);
                empty(lostPongListeners);
                logger.info('heartbeat stopped');

                return self;
            });

            method(onPing, function(self, callback) {
                append(pingListeners, callback);
            });

            method(onLostPongs, function(self, callback, lostPongsCount) {
                append(lostPongListeners, This.CoalescingListener(lostPongsCount || 1, callback));
            });
        });
    };
});