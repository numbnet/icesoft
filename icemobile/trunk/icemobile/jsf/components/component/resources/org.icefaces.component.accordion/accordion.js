(function() {
    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables
    //---------------------------------------
    function updateHidden(clientId, value) {
        var hidden = document.getElementById(clientId + "_hidden");
        if (hidden) {
            hidden.value = value;
        }
    }
    function getHiddenVal(clientId){
        var hidden = document.getElementById(clientId+"_hidden");
        if (hidden && hidden.value){
            return hidden.value;
        } else {
            return null;
        }
    }
    function getHandleHeight(aRoot){
        var handleNode = aRoot.querySelector('.mobi-accordion .handle');
        var handleHeight = "33"; //default css handle height is 33px
        if (handleNode){
           var temp = handleNode.scrollHeight || handleNode.height || handleNode.offsetHeight || handleNode.maxHeight;
           if (temp > 0){
               handleHeight = temp;
           }
        }
        return handleHeight;
    }
    function calcMaxDivHeight(clientId, handleHt) {
        var accord = document.getElementById(clientId);
        var mxht = 0;
        //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
        var children = document.getElementById(clientId).getElementsByTagName('section');
        for (var i = 0; i < children.length; i++) {
            var anode = children[i];
            var max = Math.max(anode.offsetHeight, anode.scrollHeight, anode.clientHeight);
            //init all classes to close
         //   anode.className = "close";
         //   anode.style.height = handleHt+"px";
            if (max > 0 && max > mxht) {
                mxht = max;
            }
        }
        if (mxht <= handleHt && accord.clientHeight>0) {
            mxht = accord.clientHeight - handleHt;
        }
        return mxht;
    }
    function updateFixedHeight(clientId, handleheight, fixedHeight){
         var calcht = calcMaxDivHeight(clientId, handleheight) ;
         if (calcht > 0) {
             return  calcht+"px";
         }
        else return fixedHeight;
    }
    function setHeight(opened, height){
        opened.style.height = height;
        opened.style.maxHeight = height;
    }
    function setPane(elem, height, style){
        elem.className=style;
        if (height){
            setHeight(elem, height);
        }  else {
            setHeight(elem, " ");
        }
    }
    function Accordion(clientId, cfgIn) {
        var containerId = clientId+"_acc" ;
        var paneOpId = cfgIn.opened || null;
        var accordRoot = document.getElementById(containerId);
        if (!paneOpId && accordRoot.hasChildNodes()){
            var childNodes = accordRoot.getElementsByTagName('section');
            paneOpId = childNodes[0].id;
        }
        var openElem = document.getElementById(paneOpId);
        var handleheight = getHandleHeight(accordRoot);
        var autoheight = cfgIn.autoHeight || false;
        var fixedHeight = cfgIn.fixedHeight || null;
        if (autoheight == true) {
            fixedHeight = updateFixedHeight(containerId, handleheight, fixedHeight, scroll);
        }
        if (openElem){
            openElem.className="open";
        }

        if (fixedHeight){
            setHeight(openElem, fixedHeight);
        }
        return {
            toggle: function(clientId, el, cached) {
                var theParent = el.parentElement;
                if (!theParent) {
                    theParent = el.parentNode; //mozilla
                }
                var accId = clientId+"_acc";
                var autoheight = ice.mobi.accordionController.autoheight[clientId];
                //which child is the element?
                var changed= true;
                openElem = document.getElementById(paneOpId);
                if (paneOpId && paneOpId == theParent.id){
                    if (openElem.className=="open"){
                        setPane(openElem, handleheight+"px", "closed");

                    } else {
                        setPane( openElem, fixedHeight, "open");
                    }
                    changed=false;
                }
                else {//panel has changed
                    setPane(openElem, handleheight+"px", "closed");
                    if (autoheight){
                        fixedHeight = updateFixedHeight(accId, handleheight, fixedHeight);
                    }
                    setPane(theParent,fixedHeight,"open");
                    paneOpId = theParent.id;
                }
                var pString = theParent.getAttribute("id");
                var subString = pString.replace("_sect","");
                updateHidden(clientId, subString);
                if (!cached && ice.mobi.accordionController.singleSubmit[clientId] && changed) { //renderer take care of closed panes
                    ice.se(null, clientId);
                }
            },
            updateProperties: function (clientId, cfgUpd) {
                var fixedHeight=cfgUpd.fixedHeight ||null;
                var autoheight = cfgUpd.autoHeight;
                if (autoheight ) {
                    ice.mobi.accordionController.autoheight[clientId] = autoheight;
                }
                if (autoheight) {
                    var updHeight = updateFixedHeight(clientId+"_acc", handleheight, fixedHeight);
                    if (fixedHeight != updHeight){
                        fixedHeight = updHeight;
                        setHeight(document.getElementById(paneOpId), fixedHeight);
                    }
                }
                if( !paneOpId){
                    paneOpId = cfgIn.opened+"_sect" || null;
                }
                var opened = document.getElementById(paneOpId);
                if (!opened){  //may have been deleted or removed
                    var root = document.getElementById(clientId+"_acc");
                    opened = root.firstChild;
                    paneOpId = root.firstChild.id;
                }
                var hiddenVal = getHiddenVal(clientId); //could have pushed new value
                if (hiddenVal!=null) {
                    var newPane = hiddenVal+"_sect";
                    if (newPane!=paneOpId){
                       setPane(opened, handleheight+"px", "closed");
                       var newElem = document.getElementById(newPane);
                       if (newElem){
                           setPane(newElem, fixedHeight, "open");
                       }
                        paneOpId = newPane;
                        openElem = newElem;
                    }
                }
            }
        }
    }

    ice.mobi.accordionController = {
        panels: {},
        autoheight: {},
        singleSubmit: {},
        initClient: function(clientId, cfg) {
            if (!this.panels[clientId]) {
                this.autoheight[clientId]= cfg.autoHeight;
                this.singleSubmit[clientId] = cfg.singleSubmit;
                this.panels[clientId] = Accordion(clientId, cfg);
            } else {
                this.panels[clientId].updateProperties(clientId, cfg);
            }
        },
        toggleClient: function(clientId, el, cachetyp) {
            if (this.panels[clientId]) {
                this.panels[clientId].toggle(clientId, el, cachetyp);
            } else {
                this.initClient(clientId, {});
                this.panels[clientId].toggle(clientId, el, cachetyp);
            }
        },
        toggleMenu: function(clientId, el){
            if (this.panels[clientId]) {
                this.panels[clientId].toggle(clientId, el, true);
            } else{
                this.autoheight[clientId]= true;
                this.initClient(clientId, {autoHeight:true});
              //  this.panels[clientId].toggle(clientId, el, true);
            }
        } ,
        unload: function(clientId){
            this.panels[clientId] = null;
            this.autoheight[clientId]=null;
            this.panels[clientId]=null;
        }
    }

})();

