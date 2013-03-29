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
        var visible = cfgIn.visible || false;
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        var autocenter = cfgIn.autocenter || true;
        var centerCalculation = {};
        this.mobi.panelPopup.visible[clientId]= visible;
        return {
           openPopup: function(clientId, cfg) {
                var autocenter = cfg.autocenter || true;
                var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
                document.getElementById(idPanel).className = "mobi-panelpopup-bg ";
                document.getElementById(containerId).className = "mobi-panelpopup-container ";
                if (autocenter) {
                    // add scroll listener
                    centerCalculation[clientId] = function () {
                        mobi.panelAutoCenter(containerId);
                    };
                    if (window.addEventListener) {
                        window.addEventListener(scrollEvent, centerCalculation[clientId], false);
                        window.addEventListener('resize', centerCalculation[clientId], false);
                    } else { // older ie event listener
                        window.attachEvent(scrollEvent, centerCalculation[clientId]);
                        window.attachEvent("resize", centerCalculation[clientId]);
                    }
                    // calculate center for first view
                    mobi.panelAutoCenter(containerId);
                }
        //        this.visible[clientId] = true;
                updateHidden(clientId, true);
           },
           closePopup: function(clientId, cfg){
               var autocenter = cfg.autocenter || true;
               var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
               var greyed = document.getElementById(clientId+"_bg");
               var container = document.getElementById(clientId+"_popup");
               if (greyed){
                   greyed.className = "mobi-panelpopup-bg-hide ";
               }
               if (container){
                   document.getElementById(clientId + "_popup").className = "mobi-panelpopup-container-hide ";
               }
               if (autocenter) {
                     if (window.removeEventListener) {
                         window.removeEventListener(scrollEvent, centerCalculation[clientId], false);
                         window.removeEventListener('resize', centerCalculation[clientId], false);
                     } else { // older ie cleanup
                         window.detachEvent(scrollEvent, centerCalculation[clientId], false);
                         window.detachEvent('resize', centerCalculation[clientId], false);
                     }
                     centerCalculation[clientId] = undefined;
               }
         //      this.visible[clientId] = false;
               updateHidden(clientId, false);
           }
        }
    }
    mobi.panelPopup = {
        panels: {},
        visible: {},
        centerCalculation:{},
        cfg: {},
        initClient: function(clientId, cfgIn) {
            this.cfg = cfgIn;
            if (!this.panels[clientId]){
                this.panels[clientId] = PanelPopup(clientId, cfgIn);
            } else {
               // this.panels[clientId].updateProperties(clientId, cfg);
                var vis = cfgIn.visible || false;
               if (vis==true){
                   this.panels[clientId].openPopup(clientId, cfgIn);
               }else{
                   this.panels[clientId].closePopup(clientId, cfgIn);
               }
            }
        },
        openClient: function(clientId){
            if (this.panels[clientId]){
                this.panels[clientId].openPopup(clientId, this.cfg);
            }
        },
        closeClient: function(clientId){
            if (this.panels[clientId]){
                this.panels[clientId].closePopup(clientId, this.cfg);
            }
        }
    }

  })();
