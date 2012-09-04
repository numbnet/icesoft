if (!window['mobi']) {
    window.mobi = {};
}
(function() {
    function resizeElementHeight(elId) {
        var height = 0;
        var leftNode = document.getElementById(elId+"_left");
        var rtNode = document.getElementById(elId+"_right");
        var body = window.document.body || null;
        if (body ==null) return;
        if (leftNode && rtNode){
            if (window.innerHeight) {
                height = window.innerHeight;
            } else if (body.parentElement.clientHeight) {
                height = body.parentElement.clientHeight;
            } else if (body) {
                if (body.clientHeight) {
                    height = body.clientHeight;
                }
            }
            if (height > 0){
                leftNode.style.height = ((height - leftNode.offsetTop) + "px");
                rtNode.style.height = ((height - rtNode.offsetTop) + "px");
            }
        }

    }
    //-------------------------------------
    function Scrollable(clientId, cfg) {
        var wrapPanel = clientId+"_wrp";
        var leftNode = document.getElementById(clientId+"_left") ;
        var rightNode = document.getElementById(clientId+"_right") ;
        //
        if (cfg.width){
            var width= cfg.width || -1;
            if (width >0){
                leftNode.style.width=width + "%";
                rightNode.style.width=(100-width) + "%";
            }
            var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
            var supportsOrientationChange = "onorientationchange" in window,
                orientationEvent = supportsOrientationChange ? "orientationchange" : "resize";

            if (window.addEventListener) {
                    window.addEventListener(orientationEvent, resizeElementHeight(clientId), false);
                    window.addEventListener('resize', resizeElementHeight(clientId), false);
                } else { // older ie event listener
                    window.attachEvent(orientationEvent, resizeElementHeight(clientId), false);
                    window.attachEvent("resize", resizeElementHeight(clientId), false);
            }
        }
        return {
           resize: function(clientId){
                resizeElementHeight(clientId);
           },
           unload: function(clientId){
               //remove listeners and set object back to empty
               if (window.addEventListener) {
                   window.removeEventListener(orientationEvent, resizeElementHeight(clientId), false);
                   window.removeEventListener("resize", resizeElementHeight(clientId), false);
               }else {
                   window.detachEvent(orientationEvent, resizeElementHeight(clientId), false);
                   window.detachEvent("resize", resizeElementHeight(clientId), false);
               }
           }
        }
    }
    mobi.layout = {
        panels: {},
        initClient: function(clientId, cfgIn) {
            if (!this.panels[clientId]){
                this.panels[clientId] = Scrollable(clientId, cfgIn);
            } else {
                this.panels[clientId].resize(clientId);
            }
        }
    }

  })();
