function registerListener(eventType, obj, listener) {
    var previousListener = obj[eventType];
    if (previousListener) {
        obj[eventType] = function() {
            previousListener();
            listener();
        };
    } else {
        obj[eventType] = listener;
    }
}

var onLoad = curry(registerListener, 'onload');
var onUnload = curry(registerListener, 'onunload');
var onBeforeUnload = curry(registerListener, 'onbeforeunload');
var onKeyPress = curry(registerListener, 'onkeypress')
var onKeyUp = curry(registerListener, 'onkeyup')
