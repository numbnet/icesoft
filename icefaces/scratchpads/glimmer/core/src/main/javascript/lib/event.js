var cancel = operator();
var cancelBubbling = operator();
var cancelDefaultAction = operator();
var isKeyEvent = operator();
var isMouseEvent = operator();
var capturedBy = operator();
var triggeredBy = operator();
var serializeEventOn = operator();
var serializePositionOn = operator();
var type = operator();

var yes = any;
var no = none;

function Event(event, capturingElement) {
    return object(function(method) {
        method(cancel, function(self) {
            cancelBubbling(self);
            cancelDefaultAction(self);
        });

        method(isKeyEvent, no);

        method(isMouseEvent, no);

        method(type, function(self) {
            return event.type;
        });

        method(triggeredBy, function(self) {
            return capturingElement;
        });

        method(capturedBy, function(self) {
            return capturingElement;
        });

        method(serializeEventOn, function(self, query) {
            addNameValue(query, 'ice.event.target', identifier(triggeredBy(self)));
            addNameValue(query, 'ice.event.captured', identifier(capturedBy(self)));
            addNameValue(query, 'ice.event.type', 'on' + type(self));
        });

        method(serializeOn, curry(serializeEventOn));
    });
}

function IEEvent(event, capturingElement) {
    return objectWithAncestors(function(method) {
        method(triggeredBy, function(self) {
            return event.srcElement ? $element(event.srcElement) : null;
        });

        method(cancelBubbling, function(self) {
            event.cancelBubble = true;
        });

        method(cancelDefaultAction, function(self) {
            event.returnValue = false;
        });

        method(asString, function(self) {
            return 'IEEvent[' + type(self) + ']';
        });
    }, Event(event, capturingElement));
}

function NetscapeEvent(event, capturingElement) {
    return objectWithAncestors(function(method) {
        method(triggeredBy, function(self) {
            return event.target ? $element(event.target) : null;
        });

        method(cancelBubbling, function(self) {
            event.stopPropagation();
        });

        method(cancelDefaultAction, function(self) {
            event.preventDefault();
        });

        method(asString, function(self) {
            return 'NetscapeEvent[' + type(self) + ']';
        });
    }, Event(event, capturingElement));
}

var isAltPressed = operator();
var isCtrlPressed = operator();
var isShiftPressed = operator();
var isMetaPressed = operator();
var serializeKeyOrMouseEventOn = operator();
function KeyOrMouseEvent(event) {
    return object(function(method) {
        method(isAltPressed, function(self) {
            return event.altKey;
        });

        method(isCtrlPressed, function(self) {
            return event.ctrlKey;
        });

        method(isShiftPressed, function(self) {
            return event.shiftKey;
        });

        method(isMetaPressed, function(self) {
            return event.metaKey;
        });

        method(serializeKeyOrMouseEventOn, function(self, query) {
            addNameValue(query, 'ice.event.alt', isAltPressed(self));
            addNameValue(query, 'ice.event.ctrl', isCtrlPressed(self));
            addNameValue(query, 'ice.event.shift', isShiftPressed(self));
            addNameValue(query, 'ice.event.meta', isMetaPressed(self));
        });
    });
}

var isLeftButton = operator();
var isRightButton = operator();
var positionX = operator();
var positionY = operator();
var serializeMouseEventOn = operator();
function MouseEvent(event) {
    return objectWithAncestors(function(method) {
        method(isMouseEvent, yes);

        method(serializeMouseEventOn, function(self, query) {
            serializeKeyOrMouseEventOn(self, query);
            addNameValue(query, 'ice.event.x', positionX(self));
            addNameValue(query, 'ice.event.y', positionY(self));
            addNameValue(query, 'ice.event.left', isLeftButton(self));
            addNameValue(query, 'ice.event.right', isRightButton(self));
        });

    }, KeyOrMouseEvent(event));
}

function MouseEventTrait(method) {
    method(serializeOn, function(self, query) {
        serializeEventOn(self, query);
        serializeMouseEventOn(self, query);
    });
}

function IEMouseEvent(event, capturingElement) {
    return objectWithAncestors(function(method) {
        MouseEventTrait(method);

        method(positionX, function(self) {
            return event.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft);
        });

        method(positionY, function(self) {
            return event.clientY + (document.documentElement.scrollTop || document.body.scrollTop);
        });

        method(isLeftButton, function(self) {
            return event.button == 1;
        });

        method(isRightButton, function(self) {
            return event.button == 2;
        });

        method(asString, function(self) {
            return 'IEMouseEvent[' + type(self) + ']';
        });
    }, IEEvent(event, capturingElement), MouseEvent(event));
}

function NetscapeMouseEvent(event, capturingElement) {
    return objectWithAncestors(function(method) {
        MouseEventTrait(method);

        method(positionX, function(self) {
            return event.pageX;
        });

        method(positionY, function(self) {
            return event.pageY;
        });

        method(isLeftButton, function(self) {
            return event.which == 1;
        });

        method(isRightButton, function(self) {
            return event.which == 2;
        });

        method(asString, function(self) {
            return 'NetscapeMouseEvent[' + type(self) + ']';
        });

    }, NetscapeEvent(event, capturingElement), MouseEvent(event));
}

var keyCharacter = operator();
var keyCode = operator();
var serializeKeyEventOn = operator();
function KeyEvent(event) {
    return objectWithAncestors(function(method) {
        method(isKeyEvent, yes);

        method(keyCharacter, function(self) {
            return String.fromCharCode(keyCode(self));
        });

        method(serializeKeyEventOn, function(self, query) {
            serializeKeyOrMouseEventOn(self, query);
            addNameValue(query, 'ice.event.keycode', keyCode(self));
        });
    }, KeyOrMouseEvent(event));
}

function KeyEventTrait(method) {
    method(serializeOn, function(self, query) {
        serializeEventOn(self, query);
        serializeKeyEventOn(self, query);
    });
}

function IEKeyEvent(event, capturingElement) {
    return objectWithAncestors(function(method) {
        KeyEventTrait(method);

        method(keyCode, function(self) {
            return event.keyCode;
        });

        method(asString, function(self) {
            return 'IEKeyEvent[' + type(self) + ']';
        });
    }, IEEvent(event, capturingElement), KeyEvent(event));
}

function NetscapeKeyEvent(event, capturingElement) {
    return objectWithAncestors(function(method) {
        KeyEventTrait(method);

        method(keyCode, function(self) {
            return event.which == 0 ? event.keyCode : event.which;
        });

        method(asString, function(self) {
            return 'NetscapeKeyEvent[' + type(self) + ']';
        });
    }, NetscapeEvent(event, capturingElement), KeyEvent(event));
}

function isEnterKey(event) {
    return isKeyEvent(event) && keyCode(event) == 13;
}

function isEscKey(event) {
    return isKeyEvent(event) && keyCode(event) == 27;
}

function UnknownEvent(capturingElement) {
    return objectWithAncestors(function(method) {
        method(cancelBubbling, noop);

        method(cancelDefaultAction, noop);

        method(type, function(self) {
            return 'unknown';
        });

        method(asString, function(self) {
            return 'UnkownEvent[]';
        });

    }, Event(null, capturingElement));
}

var MouseListenerNames = [ 'onclick', 'ondblclick', 'onmousedown', 'onmousemove', 'onmouseout', 'onmouseover', 'onmouseup' ];
var KeyListenerNames = [ 'onkeydown', 'onkeypress', 'onkeyup', 'onhelp' ];

function $event(e, element) {
    var capturedEvent = window.event || e;
    var capturingElement = $element(element);
    if (capturedEvent && capturedEvent.type) {
        var eventType = 'on' + capturedEvent.type;
        if (contains(KeyListenerNames, eventType)) {
            return window.event ? IEKeyEvent(event, capturingElement) : NetscapeKeyEvent(e, capturingElement);
        } else if (contains(MouseListenerNames, eventType)) {
            return window.event ? IEMouseEvent(event, capturingElement) : NetscapeMouseEvent(e, capturingElement);
        } else {
            return window.event ? IEEvent(event, capturingElement) : NetscapeEvent(e, capturingElement);
        }
    } else {
        return UnknownEvent(capturingElement);
    }
}