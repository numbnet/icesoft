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
    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables
    //---------------------------------------
    function updateHidden(clientId, value) {
        var hidden = document.getElementById(clientId+"_hidden");
        if (hidden) {
            hidden.value = value;
        }
    }
    //-------------------------------------
    function PanelPopup(clientId, cfgIn) {
        var cId = clientId;
        var myId = cfgIn.id || null;
        var client = cfgIn.client;
        var visible = cfgIn.visible || false;
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        var autocenter = cfgIn.autocenter || true;
        var centerCalculation = {};
        ice.mobi.panelPopup.visible[clientId]= visible;
        return {
            openPopup: function(clientId, cfg) {
                autocenter = cfg.autocenter || true;
              //  console.log("openPopup: disabled="+cfg.disabled);
                if (cfg.disabled){
                    return;//no opening
                }
                var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
                var fadedDiv = document.getElementById(idPanel);
                if (fadedDiv){
                   document.getElementById(idPanel).className = "mobi-panelpopup-bg ";
                }
                var containerNode = document.getElementById(containerId);
                containerNode.className = "mobi-panelpopup-container ";
                if (cfg.autocenter==true) {
                    centerCfg = {} ;
                    if (cfg.width){
                        centerCfg.width = cfg.width;
                    }
                    if (cfg.height){
                        centerCfg.height = cfg.height;
                    }
                    if (cfg.useForm){
                        var frm = mobi.findForm(clientId);
                        if (frm){
                           centerCfg.containerElem = frm.id;
                           // console.log("form id="+frm.id);
                        }
                    }
                    // add scroll listener
                    centerCalculation[clientId] = function () {
                        ice.mobi.panelCenter(containerId, centerCfg);
                    };
                    if (window.addEventListener) {
                        window.addEventListener(scrollEvent, centerCalculation[clientId], false);
                        window.addEventListener('resize', centerCalculation[clientId], false);
                    } else { // older ie event listener
                        window.attachEvent(scrollEvent, centerCalculation[clientId]);
                        window.attachEvent("resize", centerCalculation[clientId]);
                    }
                    // calculate center for first view
                    ice.mobi.panelCenter(containerId, centerCfg);
                }  else{
                 //   console.log("NO AUTOCENTER");
                    //check to see if existing style var
                    if (containerNode.getAttribute("style")){
                        styleVar = containerNode.getAttribute("style");
                    }else {
                        var styleVar = "";
                    }
                    if (cfg.width){
                        var wStr = width+"px";
                        styleVar+="width: "+cfg.width+"px;";
                    }
                    if (cfg.height){
                        var hStr = height+"px";
                        styleVar +=" height: "+cfg.height+"px;";
                    }
                    containerNode.setAttribute("style", styleVar);
                }
                ice.mobi.panelPopup.visible[clientId] = true;
                updateHidden(clientId, "true");
           },
           closePopup: function(clientId, cfg){
               var containerNode = clientId+"_popup";
               var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
               var greyed = document.getElementById(clientId+"_bg");
               var container = document.getElementById(containerNode);
               if (greyed){
                   greyed.className = "mobi-panelpopup-bg-hide ";
               }
               if (container){
                   document.getElementById(containerNode).className = "mobi-panelpopup-container-hide ";
               }
               if (cfg.autocenter==true) {
                     if (window.removeEventListener) {
                         window.removeEventListener(scrollEvent, centerCalculation[clientId], false);
                         window.removeEventListener('resize', centerCalculation[clientId], false);
                     } else { // older ie cleanup
                         window.detachEvent(scrollEvent, centerCalculation[clientId], false);
                         window.detachEvent('resize', centerCalculation[clientId], false);
                     }
                     centerCalculation[clientId] = undefined;
               } else {
                   container.setAttribute("style", "");
               }
               ice.mobi.panelPopup.visible[clientId] = false;
               updateHidden(clientId, "false");
           },
           getId: function(){
               return myId;
           },
           getClientId: function(){
               return cId;
           },
           isClient: function(){
               return client;
           }
        }
    }
    ice.mobi.panelPopup = {
        panels: new Array(),
        visible: {},
        cfg: {},
        centerCalculation:{},
        init: function(clientId, cfgIn) {
          //  console.log("INIT");
            this.cfg[clientId] = cfgIn;
            var thisOne = this.findPanel(clientId, false);
            var i = this.panels.length;
            if (thisOne == null){
                this.panels[i] = PanelPopup(clientId, cfgIn);
            } else {
                var vis =  cfgIn.visible || false;
                if (cfgIn.client && ice.mobi.panelPopup.visible[clientId]){
                     vis = ice.mobi.panelPopup.visible[clientId];
                }
             //   console.log(" disabled="+cfgIn.disabled+"  VISIBLE="+vis);
                if (vis==true){
                   thisOne.openPopup(clientId, cfgIn);
                }else{
                   thisOne.closePopup(clientId, cfgIn);
                }
            }
        },
        openClient: function(popupId){
            var opC = this.findPanel(popupId, true);
            if (!opC){
                var index = this.panels.length;
            }
            if (opC){
                var cId = opC.getClientId();
                var cfgA = this.cfg[cId];
                if (!cfgA.disabled){
                    var chkNode = document.getElementById(cId);
                    if (!chkNode){
                        ice.log.debug(ice.log,"NO ELEMENT CAN BE FOUND FOR ID="+popupId+" clientId="+cId);
                    } else {
                       opC.openPopup(cId, cfgA);
                    }
                }
            }
        },
        closeClient: function(popupId){
            var clA = this.findPanel(popupId,true);
            if (clA){
                var cId =clA.getClientId();
                var cfgA = this.cfg[cId];
                clA.closePopup(cId, cfgA);
            }
        },
        findPanel: function(popupId, isId){
            if (this.panels.length < 1){
                ice.log.debug(ice.log,' no popups available in view to open');
                return;
            }
            var found = false;
            for (var i=0; i < this.panels.length; i++){
                var pane = this.panels[i];
                if (isId){
                    var myId = pane.getId();
                    console.log ("id of pane="+myId);
                    if (pane.getId()==popupId){
                        found = true;
                        return pane;
                    }
                }else {
                    if (pane.getClientId()==popupId){
                        found = true;
                        return pane;
                    }
                }
            }
            if (!found){
                ice.log.debug(ice.log, ' Cannot find popup with id='+popupId);
            }
            return null;
        }
    }

  })();