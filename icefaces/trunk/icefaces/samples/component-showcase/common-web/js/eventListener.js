function dmlListener(event) {
    var ESC = 27;
    var DEL = 46;
    var E = 69;
    var N = 78;
    switch(event.keyCode) {
        case ESC:
        case DEL:
            return true;
        case E:
        case N:
            if(event.shiftKey) {
               Event.stop(event);
               return true;
            }   
    }
    return false;
}

function preventBrowser(event) {
    var E = (event.keyCode == 69);
    var N = (event.keyCode == 78);
    if (event.shiftKey && (E || N)) {
        Event.stop(event);
        return false;
    }
    return false;
}