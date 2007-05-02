
var bridgeRequest;

if (!window.requestStarted) {
    window.requestStarted = true;
    //alert("requesting the bridge " + bridgeURL);
    retrieveBridge(bridgeURL);
} else {
    //alert("bridge already retrieved " + requestStarted);
}

function retrieveBridge(url) {
    if (window.XMLHttpRequest) {
        bridgeRequest = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        bridgeRequest = new ActiveXObject("Microsoft.XMLHTTP");
    }
    if (bridgeRequest) {
        bridgeRequest.onreadystatechange = processStateChange;
        try {
            bridgeRequest.open("GET", url, true);
            bridgeRequest.send('');
        } catch (e) {
            alert(e);
        }
    }
}

function processStateChange() {
    if (bridgeRequest.readyState == 4) {
        if (bridgeRequest.status == 200) {
            eval(bridgeRequest.responseText);
            //alert( "bridge evaluated");
        } else {
            alert("problem retrieving the data:\n" +
                  bridgeRequest.statusText);
        }
    }
}
