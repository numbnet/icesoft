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
