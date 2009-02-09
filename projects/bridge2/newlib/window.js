function registerWindowListener(eventType, w, listener) {
    var previousListener = w[eventType];
    if (previousListener) {
        w[eventType] = function() {
            previousListener();
            listener();
        };
    } else {
        w[eventType] = listener;
    }
}

var onLoad = curry(registerWindowListener, 'onload');
var onUnload = curry(registerWindowListener, 'onunload');
var onBeforeUnload = curry(registerWindowListener, 'onbeforeunload');

function registerDocumentListener(eventType, w, listener) {
    var previousListener = w[eventType];
    if (previousListener) {
        w[eventType] = function(e) {
            previousListener(e);
            listener(e);
        };
    } else {
        w[eventType] = listener;
    }
}

var onKeyPress = curry(registerDocumentListener, 'onkeypress')
var onKeyUp = curry(registerDocumentListener, 'onkeyup') 
