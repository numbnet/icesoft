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
    if (obj.addEventListener) {
        obj.addEventListener(eventType, listener, false);
    } else {
        obj.attachEvent('on' + eventType, listener);
    }
}

var onLoad = curry(registerListener, 'load');
var onUnload = curry(registerListener, 'unload');
var onBeforeUnload = curry(registerListener, 'beforeunload');
var onResize = curry(registerListener, 'resize');
var onKeyPress = curry(registerListener, 'keypress');
var onKeyUp = curry(registerListener, 'keyup');

window.width = function() {
    return window.innerWidth ? window.innerWidth : (document.documentElement && document.documentElement.clientWidth) ? document.documentElement.clientWidth : document.body.clientWidth;
};

window.height = function() {
    return window.innerHeight ? window.innerHeight : (document.documentElement && document.documentElement.clientHeight) ? document.documentElement.clientHeight : document.body.clientHeight;
};
