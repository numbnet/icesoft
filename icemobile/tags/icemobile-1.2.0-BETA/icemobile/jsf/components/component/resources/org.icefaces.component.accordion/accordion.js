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
            console.log("COULD NOT CALC A mxht");
        }
        return mxht;
    }
    function calcFixedSectionHeight(fixedHeight, handleHeight){
        try {
            var fHtVal =parseInt(fixedHeight);
        }catch (Exception ){
            ice.log.debug("problem calculating height of contentPane to set section Height");
        }
        if (fHtVal) {
            return  fHtVal+handleHeight;
        }
        else return null;
    }
    function setHeight(opened, fht){
        if (opened && fht){
           // opened.setAttribute("style", "height:"+fht+"; maxHeight: "+fht+";");
            opened.style.height=fht;
            opened.style.maxHeight = fht;
        }
    }
    function openPane(elem, h){
        setHeight(elem, h);
        elem.className="open";
    }
    function closePane(elem, closeHt){
        setHeight(elem, closeHt);
        elem.className="closed";
    }
    function updateMaxHeight(clientId, mH, hH){
        var tmp = ice.mobi.accordionController.maxHt[clientId];
        if (tmp && mH==0){
            mH = tmp;
        }
        else if (tmp && mH > 0){
            mH = Math.max(tmp, mH);
        }
        else {
            mH = calcMaxDivHeight(clientId, hH);
        }
        return mH ;
    }
    function Accordion(clientId, cfgIn) {
        var disabled = cfgIn.disabled || false;
        var containerId = clientId+"_acc" ;
        var paneId = cfgIn.opened || null;
        var accordRoot = document.getElementById(containerId);
        var paneOpId;
        var lastServerId = paneId;
        if (paneId) {
            paneOpId = paneId + "_sect";
        }
        var openElem = document.getElementById(paneOpId);
        var cntr = 0;
        if (!openElem && accordRoot.hasChildNodes()){
            var children = accordRoot.getElementsByTagName("section");
            openElem = children[0];
            paneOpId = children[0].id;
        }
        var autoheight = cfgIn.autoHeight || false;
        var fixedHeight = cfgIn.fixedHeight || null;
        var fHtVal = cfgIn.fHtVal || null;
        if (!openElem){
            ice.log.debug(ice.log,"Accordion has no children");
            this.setDisabled(true);
        }
        var handleheight = getHandleHeight(accordRoot);
        var handleht = handleheight + "px";
        if (!autoheight && !fixedHeight){
            handleht = null;
        }
        var maxHeight;
        if (autoheight){ //default
            maxHeight = calcMaxDivHeight(containerId, handleheight);
        }
        if (autoheight && (maxHeight > 0)){
            ice.mobi.accordionController.maxHt[clientId]=maxHeight;
            fixedHeight = maxHeight+"px";
        } else if (fixedHeight){
            if (fHtVal){
                var val = parseInt(fHtVal)+ parseInt(handleheight);
                fixedHeight = val + "px";
            }else {
                fixedHeight = calcFixedSectionHeight(fixedHeight, handleheight);
            }
        }
        if (!disabled){
            openPane(openElem, fixedHeight);
        }else {
             ice.log.debug(ice.log, "Accordion has been disabled");
        }
        if (autoheight && (maxHeight==0)){
            ice.onAfterUpdate(function() {
                ice.mobi.accordionController.updateHeight(clientId, handleheight);
            });
        }
        return {
            toggle: function(clientId, el, cached) {
                if (!el || disabled){    //is getting triggered on page load
                 //   ice.log.debug(ice.log, 'accordion id='+clientId+' unable to open handle or is disabled');
                    return;
                }
                var singleSubmit  = ice.mobi.accordionController.singleSubmit[clientId];
                if (autoheight){
                    maxHeight = updateMaxHeight(clientId, maxHeight, handleheight);
                }
                var theParent = el.parentElement;
                if (!theParent) {
                    theParent = el.parentNode; //mozilla
                }
                var pString = theParent.getAttribute("id");
                var subString = pString.replace("_sect","");
                updateHidden(clientId, subString);
                openElem = document.getElementById(paneOpId);
                if (!openElem){
                    var children = accordRoot.getElementsByTagName("section");
                    openElem = children[0];
                    paneOpId = children[0].id;
                }
                if (autoheight && openElem && (maxHeight > 0)){
                    fixedHeight = maxHeight+"px";
                   // console.log("\t updated fixedHeight="+fixedHeight);
                }
                if (paneOpId && paneOpId == theParent.id){
                    if (openElem.className=="open"){
                        closePane(openElem, handleht);
                    } else {
                        openPane( openElem, fixedHeight);
                    }
                }
                else {//panel has changed
                    closePane(openElem, handleht);
                    if (cached){
                        openPane(theParent,fixedHeight);
                        paneOpId = theParent.id;
                        openElem = theParent;
                    }
                    if (!cached && singleSubmit) {
                        if (lastServerId == theParent.id){
                            cntr= cntr + 1;
                            //try to keep correct pane open despite caching
                            var submitted = subString +","+cntr;
                            updateHidden(clientId,submitted);
                        }  else {
                            cntr = 0;
                            lastServerId = theParent.id;
                        }
                        ice.se(null, clientId);
                    }
                }
            },
            updateHeight: function(clientId, handleheight){
                var node = document.getElementById(clientId);
                if (node){
                    maxHeight = calcMaxDivHeight(clientId,  handleheight);
             //       console.log("returning maxHeight="+maxHeight);
                    return maxHeight;
                }
            } ,
            updateProperties: function (clientId, cfgUpd) {
                disabled = cfgUpd.disabled || false;
                if (disabled){
                    return;
                }
                var changedFH = false;
                var changedAH = false;
                if (fixedHeight != cfgUpd.fixedHeight) {
                    fixedHeight=cfgUpd.fixedHeight || null;
                    changedFH = true;
                }
                if (autoheight != cfgUpd.autoHeight){
                    autoheight = cfgUpd.autoHeight;
                    changedAH=true;
                }
                if (changedAH || changedFH && autoheight){
                    ice.mobi.accordionController.maxHt[clientId]=null;
                    maxHeight=0;
                }
                if (autoheight) {
                    //calc new maxHeight
                    var tmp1 = calcMaxDivHeight(clientId, handleheight);
                    var storedHt = ice.mobi.accordionController.maxHt[clientId];
                    maxHeight = Math.max(tmp1, maxHeight);
                    if (maxHeight == 0 && !storedHt){
                      //  console.log(" \t !!!set ONAFTER UPDATE value so have to listen for update");
                           ice.onAfterUpdate(function() {
                               ice.mobi.accordionController.updateHeight(clientId, handleheight);
                            }) ;
                    }
                    else {
                        maxHeight = Math.max(storedHt, maxHeight);
                    }
                    if (maxHeight && maxHeight > 0){
                        ice.mobi.accordionController.maxHt[clientId]=maxHeight;
                        fixedHeight = maxHeight+"px";
                    }
                } else if (fixedHeight && changedFH){
                    if (cfgUpd.fHtVal){
                        var val = parseInt(fixedHeight) + parseInt(handleheight);
                        fixedHeight = val + "px";
                    }else {
                        fixedHeight = calcFixedSectionHeight(fixedHeight, handleheight);
                    }
                }
                //allow server to push last submitted or encoded value
                openElem = document.getElementById(paneOpId);
                if (openElem){
                    closePane(openElem, handleheight+"px");
                } else {  //may have been deleted or removed
                     var root = document.getElementById(clientId+"_acc");
                     openElem = root.firstChild;
                     paneOpId = root.firstChild.id;
                }
                var pushedId = getHiddenVal(clientId)+"_sect";
                var delPushed=false;
                if (!document.getElementById(pushedId)){
                    pushedId = paneOpId;
                    delPushed = true;
                }
                if(paneOpId != pushedId){
                    paneOpId = pushedId || null;
                    openElem = document.getElementById(paneOpId);
                    openPane(openElem, fixedHeight) ;
                } else if (!delPushed){
                    openPane(openElem, fixedHeight);
                }
            },
            getDisabled: function(){
                return disabled;
            },
            setDisabled: function(dis){
                disabled= dis;
            }
        }

    }
    ice.mobi.accordionController = {
        panels: {},
        autoheight: {},
        maxHt: {},
        singleSubmit: {},
        initClient: function(clientId, cfg) {
            if (!this.panels[clientId]) {
                this.autoheight[clientId]= cfg.autoHeight;
                this.singleSubmit[clientId] = cfg.singleSubmit;
                this.panels[clientId] = Accordion(clientId, cfg);
                ice.onElementUpdate(clientId, function(){
                    ice.mobi.accordionController.unload(clientId);
                });
            } else {
                this.panels[clientId].updateProperties(clientId, cfg);
            }
        },
        toggleClient: function(clientId, el, cachetyp) {
            if (this.panels[clientId] && !this.panels[clientId].getDisabled()){
                   this.panels[clientId].toggle(clientId, el, cachetyp);
            } else {
                this.initClient(clientId, {});
                if (!this.panels[clientId].getDisabled()){
                    this.panels[clientId].toggle(clientId, el, cachetyp);
                }
            }
        },
        toggleMenu: function(clientId, el){
            if (this.panels[clientId]) {  //have yet to implement disabled for menu
                this.panels[clientId].toggle(clientId, el, true);
            } else{
                this.initClient(clientId, {autoheight:true});
            }
        } ,
        updateHeight: function(clientId, handleHt){
            if (this.panels[clientId]){
                var tmp = this.panels[clientId].updateHeight(clientId, handleHt);
                if (!this.maxHt[clientId] && tmp > 0){
                    this.maxHt[clientId] = tmp;
                }else {
                    this.maxHt[clientId] = Math.max(tmp, this.maxHt[clientId]);
                }
            }
        },
        unload: function(clientId){
            var anode = document.getElementById(clientId);
            if (!anode){
             //   console.log("REMOVE ACCORDION WITH  id="+clientId);
                this.panels[clientId] = null;
                this.autoheight[clientId]=null;
                this.panels[clientId]=null;
                this.maxHt[clientId]=null;
            }
        }
    }
})();