if (!window['mobi']) {
    window.mobi = {};
}
if( !window['ice']){
    window.ice = {};
}
if (!window.ice['mobi']) {
    window.ice.mobi = {};
}
ice.mobi.splitpane = {
    panels: {},
    initClient: function(clientId, cfgIn) {
        if (!this.panels[clientId]){
            this.panels[clientId] = ice.mobi.splitpane.Scrollable(clientId, cfgIn);
            this.panels[clientId].resize(clientId);
        } else {
            this.panels[clientId].resize(clientId);
        }
    },
    resizeHt: function(clientId) {
        if (this.panels[clientId])
            this.panels[clientId].resize(clientId);
        else {
            this.panels[clientId].unload(clientid);
        }
    },
    Scrollable: function Scrollable(clientId, cfgIn) {
        var wrapPanel = clientId+"_wrp";
        var leftNode = document.getElementById(clientId+"_left") ;
        var rightNode = document.getElementById(clientId+"_right") ;
        var resizeCall = function(){ ice.mobi.splitpane.resizeHt(clientId);};
        //
        if (cfgIn.width){
            var width= cfgIn.width || -1;
            if (width >0 && width < 99){
                leftNode.style.width=width + "%";
                rightNode.style.width=(100-width) + "%";
            }
        }
    /*    var supportsOrientationChange = "onorientationchange" in window,
            orientationEvent = supportsOrientationChange ? "orientationchange" : "resize"; */
        if (window.addEventListener) {
            //    window.addEventListener(orientationEvent, resizeCall, false);
                window.addEventListener('resize', resizeCall, false);
            } else { // older ie event listener
             //   window.attachEvent(orientationEvent, resizeCall, false);
                window.attachEvent("resize", resizeCall, false);
        }
        return {
           resize: function(elId){
                var height = 0;
                var leftNode = document.getElementById(elId+"_left");
                var rtNode = document.getElementById(elId+"_right");
                var splt  = document.getElementById(elId+"_splt");
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
           },
           unload: function(){
               //remove listeners and set object back to empty
               if (window.addEventListener) {
                   window.removeEventListener("resize", resizeCall, false);
               }else {
                   window.detachEvent("resize", resizeCall, false);
               }
           }
        }
    }
}