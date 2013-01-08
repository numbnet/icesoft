/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
            ice.log.debug(ice.log,"COULD NOT CALC A mxht");
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
           // opened.style.maxHeight = fht;
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
        var origHeight, fixedHeight, maxHeight, scp;
        scp = cfgIn.scp || false;
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
        origHeight = fixedHeight =  cfgIn.fixedHeight || null;
        var fHtVal = cfgIn.fHtVal || null;
        if (!openElem){
            ice.log.debug(ice.log,"Accordion has no children");
            this.setDisabled(true);
        }
        var handleheight = getHandleHeight(accordRoot);
        var handleht = handleheight + "px";
        if (autoheight==false && !fixedHeight){
            handleht = null;
        }
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
        if (disabled!=true){
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
                if (!el || disabled==true){    //is getting triggered on page load
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
                        //contents may have changed so get new ones or may be single pane MOBI-611
                        if (cached!=true){
                            ice.se(null, clientId);
                        }
                        openPane( openElem, fixedHeight);
                    }
                }
                else {//panel has changed
                    closePane(openElem, handleht);
                    if (cached==true){
                        openPane(theParent,fixedHeight);
                        paneOpId = theParent.id;
                        openElem = theParent;
                    }
                    else if ( singleSubmit) { //only have singleSubmit support for now
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
                    return maxHeight;
                }
            } ,
            updateProperties: function (clientId, cfgUpd) {
                disabled = cfgUpd.disabled || false;
                if (disabled==true){
                    return;
                }
                var changedFH, changed, changedAH;
                changedFH = changed = changedAH = false;

                if (cfgUpd.scp != scp){
                    changed= true;
                    scp = cfgUpd.scp;
                }
                if (origHeight != cfgUpd.fixedHeight) {
                    origHeight=cfgUpd.fixedHeight || null;
                    fixedHeight = origHeight;
                    changed = changedFH = true;
                }
                if (autoheight != cfgUpd.autoHeight){
                    autoheight = cfgUpd.autoHeight;
                    changed = changedAH=true;
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
                } else if (fixedHeight && changed){
                    if (cfgUpd.fHtVal){
                        var val = parseInt(cfgUpd.fHtVal) + parseInt(handleheight);
                        fixedHeight = val + "px";
                    }else if (changedFH){
                        var temp = calcFixedSectionHeight(fixedHeight, handleheight);
                        if (temp !=null){
                            fixedHeight = temp +"px";
                        }
                    }
                }
                if (autoheight==false && !fixedHeight){
                    handleht = null;
                } else {
                    handleht = getHandleHeight(accordRoot) + "px";
                }
                //did the active pane change?
                var pushedId = getHiddenVal(clientId)+"_sect";
                if ((paneOpId == pushedId) || disabled==true){
                    return;
                }
                //allow server to push last submitted or encoded value
                openElem = document.getElementById(paneOpId);
                if (openElem){
                    closePane(openElem, handleht);
                } else {  //may have been deleted or removed
                     var root = document.getElementById(clientId+"_acc");
                     openElem = root.firstChild;
                     paneOpId = root.firstChild.id;
                }
                var delPushed=false;
                if (!document.getElementById(pushedId)){
                    pushedId = paneOpId;
                    delPushed = true;
                }
                if(paneOpId != pushedId){
                    paneOpId = pushedId || null;
                    openElem = document.getElementById(paneOpId);
                }
                if (openElem.className=="closed" || changed){ //only modify opened pane if height changes
                    openPane(openElem, fixedHeight) ;
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
        lastTime: 0,
        initClient: function(clientId, cfg) {
            if (!this.panels[clientId]) {
                this.autoheight[clientId]= cfg.autoHeight;
                this.singleSubmit[clientId] = cfg.singleSubmit;
                this.panels[clientId] = Accordion(clientId, cfg);
                ice.onElementUpdate(clientId, function(){
                    ice.mobi.accordionController.unload(clientId);
                });
            } else {
                //getting phantom calls to this
                if (!cfg.hash){
                    return;
                }
                this.panels[clientId].updateProperties(clientId, cfg);
            }
        },
        toggleClient: function(clientId, el, cachetyp, transHack) {
            if (transHack ) {
                var currentTimeMillis = new Date().getTime();
                if ( (currentTimeMillis - this.lastTime) < 100 ) {
                   ice.log.debug(ice.log,"__Accordion Double click suppression required");
                   return;
                }
                this.lastTime = currentTimeMillis;
            }
            if (this.panels[clientId] && !this.panels[clientId].getDisabled()){
                   this.panels[clientId].toggle(clientId, el, cachetyp);
            } else if (!this.panels[clientId].getDisabled()) {
               this.initClient(clientId, {});
                   this.panels[clientId].toggle(clientId, el, cachetyp);
            }
        },
        toggleMenu: function(clientId, el){
            if (this.panels[clientId]) {  //have yet to implement disabled for menu
                this.panels[clientId].toggle(clientId, el, true);
            } else{
                this.initClient(clientId, {autoheight:false});
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