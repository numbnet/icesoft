/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

function registerListener(eventType, obj, listener) {
    var previousListener = obj[eventType];
    if (previousListener) {
        obj[eventType] = function() {
            apply(previousListener, arguments);
            apply(listener, arguments);
        };
    } else {
        obj[eventType] = listener;
    }
}

var onLoad = curry(registerListener, 'onload');
var onUnload = curry(registerListener, 'onunload');
var onBeforeUnload = curry(registerListener, 'onbeforeunload');
var onResize = curry(registerListener, 'onresize');
var onKeyPress = curry(registerListener, 'onkeypress');
var onKeyUp = curry(registerListener, 'onkeyup');

window.width = function() {
    return window.innerWidth ? window.innerWidth : (document.documentElement && document.documentElement.clientWidth) ? document.documentElement.clientWidth : document.body.clientWidth;
};

window.height = function() {
    return window.innerHeight ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.clientHeight;
};
