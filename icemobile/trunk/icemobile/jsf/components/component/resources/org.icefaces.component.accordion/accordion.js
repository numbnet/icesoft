(function() {
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
            var max = Math.max(anode.scrollHeight, anode.offsetHeight, anode.clientHeight);
            if (max > 0 && max > mxht) {
                mxht = max;
            }
        }
        if (mxht <= handleHt ) {
            mxht = 0;
        }
        return mxht;
    }
    function updateFixedHeight(clientId, handleheight, fixedHeight){
         var calcht = calcMaxDivHeight(clientId, handleheight) ;
         if (calcht > 0) {
             return  calcht+"px";
         }
         else {
             return fixedHeight;
         }
    }
    function setHeight(opened, height){
        if (opened && height){
            opened.style.height = height;
            opened.style.maxHeight = height;
        }
    }
    function openPane(elem, height){
        elem.className="open";
        if (height){
            setHeight(elem, height);
        }
    }
    function closePane(elem, closeHt){
        elem.className="closed";
        if (closeHt) {
            setHeight(elem, closeHt);
        }
    }
    function updateHt(paneId){
        console.log("triggered update on height for id="+paneId);
        var pane = document.getElementById(paneId);
        console.log("scrollHeight="+pane.scrollHeight+" offsetHeight="+pane.offsetHeight+" clientHt="+pane.clientHeight);
    }
    function Accordion(clientId, cfgIn) {
        var containerId = clientId+"_acc" ;
        var paneId = cfgIn.opened || null;
        var accordRoot = document.getElementById(containerId);
        var paneOpId;
        if (paneId) {
            paneOpId = paneId + "_sect";
        }
        if (!paneOpId && accordRoot.hasChildNodes()){
            var children = accordRoot.getElementsByTagName("section");
            paneOpId = children[0].id;
        }
        console.log("paneOpId="+paneOpId);
        var openElem = document.getElementById(paneOpId);
        if (openElem){
            openElem.className = "open";
            console.log("opened element has height="+openElem.offsetHeight);
        }
        var handleheight = getHandleHeight(accordRoot);
        var handleht = handleheight + "px";
        var autoheight = cfgIn.autoHeight || false;
        var fixedHeight = cfgIn.fixedHeight || null;
        if (!autoheight && !fixedHeight){
            handleht = null;
        }
        var maxHeight = calcMaxDivHeight(containerId, handleheight);
        if (autoheight && (maxHeight > 0)){
            ice.mobi.accordionController.maxHt[clientId]=maxHeight;
            openPane(openElem, maxHeight+"px");
        }
        if (autoheight && (maxHeight==0)){
            ice.onAfterUpdate(function() {
                ice.mobi.accordionController.updateHeight(clientId, handleheight);
            });
        }
        return {
            toggle: function(clientId, el, cached) {
                var singleSubmit  = ice.mobi.accordionController.singleSubmit[clientId];
                if (autoheight){
                    var tmp = ice.mobi.accordionController.maxHt[clientId];
                    if (tmp && maxHeight==0){
                        console.log("toggle and maxHeight = "+maxHeight+" tmp="+tmp) ;
                        maxHeight = tmp;
                    } if (tmp && maxHeight > 0){
                        maxhHeight = Math.max(tmp, maxHeight);
                    }
                }
                var theParent = el.parentElement;
                if (!theParent) {
                    theParent = el.parentNode; //mozilla
                }
                var pString = theParent.getAttribute("id");
                var subString = pString.replace("_sect","");
                updateHidden(clientId, subString);
              //  console.log("paneOpId="+paneOpId+" parentId ="+theParent.id);
                openElem = document.getElementById(paneOpId);
                if (autoheight && openElem && (maxHeight > 0)){
                    fixedHeight = maxHeight+"px";
                   // console.log("updated fixedHeight="+fixedHeight);
                }
                if (paneOpId && paneOpId == theParent.id){
                    if (openElem.className=="open"){
                      //  console.log("no pane change so close existing");
                        closePane(openElem, handleht);
                    } else {
                     //   console.log("no pane change to open existing");
                        openPane( openElem, fixedHeight);
                    }
                }
                else {//panel has changed
                    closePane(openElem, handleht);
                    openPane(theParent,fixedHeight);
                    paneOpId = theParent.id;
                    if (cached){
                        openElem = theParent;
                        openPane(openElem, fixedHeight);
                    }
                    if (!cached && singleSubmit) {
                       ice.se(null, clientId);
                    }
                }
            },
            updateHeight: function(clientId, handleheight){
                    maxHeight = calcMaxDivHeight(clientId,  handleheight);
                    return maxHeight;
            } ,
            updateProperties: function (clientId, cfgUpd) {
                var fixedHeight=cfgUpd.fixedHeight ||null;
                autoheight = cfgUpd.autoHeight;
                if (autoheight) {
                    //calc new maxHeight
                    var tmp1 = calcMaxDivHeight(clientId, handleheight);
                    maxHeight = Math.max(tmp1, maxHeight);
                    if (maxHeight == 0){
                        if (!ice.mobi.accordionController[clientId]) {
                           ice.onAfterUpdate(function() {
                               ice.mobi.accordionController.updateHeight(clientId, handleheight);
                            }) ;
                        }else {
                        //if tmp comes back 0 may not have page loaded..then what
                            maxHeight = Math.max(ice.mobi.accordionController.maxHt[clientId], maxHeight);
                        }
                    }
                    if ( maxHeight && maxHeight > 0){
                        ice.mobi.accordionController.maxHt[clientId]=maxHeight;
                        fixedHeight = maxHeight+"px";
                    }
                }
                if (openElem){
                    closePane(openElem, handleheight+"px");
                } else {  //may have been deleted or removed
                     var root = document.getElementById(clientId+"_acc");
                     openElem = root.firstChild;
                     paneOpId = root.firstChild.id;
                }
                var pushedId = getHiddenVal(clientId)+"_sect";
                if(paneOpId != pushedId){
                    paneOpId = pushedId || null;
                    openElem = document.getElementById(paneOpId);
                    openPane(openElem, fixedHeight) ;
                }
            }
        }
    }
    ice.mobi.accordionController = {
        panels: {},
        autoheight: {},
        maxHt: {},
        singleSubmit: {},
        lastOpen: {},
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
                console.log(" had to init accordion again!");
                this.panels[clientId].toggle(clientId, el, cachetyp);
            }
        },
        toggleMenu: function(clientId, el){
            if (this.panels[clientId]) {
                this.panels[clientId].toggle(clientId, el, true);
            } else{
                this.initClient(clientId, {autoheight:true});
            }
        } ,
        updateHeight: function(clientId, handleHt){
            if (this.panels[clientId]){
            //    console.log("updateHeight");
                var tmp = this.panels[clientId].updateHeight(clientId, handleHt);
                if (!this.maxHt[clientId] && tmp > 0){
                    this.maxHt[clientId] = tmp;
                }else {
                    this.maxHt[clientId] = Math.max(tmp, this.maxHt[clientId]);
                }
             //   console.log("updateProps ->maxHt clientId = "+this.maxHt[clientId]);
            }
        },
        unload: function(clientId){
            this.panels[clientId] = null;
            this.autoheight[clientId]=null;
            this.panels[clientId]=null;
        }
    }
})();