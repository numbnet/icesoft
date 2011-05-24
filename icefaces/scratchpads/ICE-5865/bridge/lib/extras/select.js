Ice.selectChange = function(form, elem, event, partialSubmitDelay) {
    if (!partialSubmitDelay) {
        partialSubmitDelay = 300;
    }
    if (elem.timeout) {
        clearTimeout(elem.timeout);
        elem.removeAttribute('timeout');
    }
    else {
        var currentTime = new Date();
        var currentMillis = currentTime.getTime();
        if (! elem.lastTime ||
            ((currentMillis - elem.lastTime) > partialSubmitDelay) ||
            (partialSubmitDelay <= 0))
        {
            elem.lastTime = currentTime.getTime();
            return iceSubmitPartial(form, elem, event);
        }
    }
    
    elem.timeout = setTimeout(function() {
        elem.removeAttribute('timeout');
        var currentTime = new Date();
        elem.lastTime = currentTime.getTime();
        // In IE 8, can't use event in timeout
        iceSubmitPartial(form, elem, null);
    }, partialSubmitDelay);
};
