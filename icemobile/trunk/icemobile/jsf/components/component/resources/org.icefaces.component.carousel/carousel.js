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
if (!window['mobi']) {
    window.mobi = {};
}
if( !window['ice']){
    window.ice = {};
}
if (!window.ice['mobi']) {
    window.ice.mobi = {};
}
(function() {
    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables

    function enhance(clientId)  {
        var carouselId = clientId+'_carousel';
   //     var carElem = document.getElementById(carouselId);
    //    carElem.addEventListener ("DOMNodeRemoved", ice.mobi.carousel.nodeRemoved(event), false);
    //    carElem.addEventListener('DOMNodeRemovedFromDocument', ice.mobi.carousel.nodeRemovedFromDoc(event), false);
        var iscroller = new iScroll(carouselId, {
	                    snap: 'li',
	                    momentum: false,
	                    hScrollbar: false,
                        checkDOMChanges: false,
                        zoom: true,
	                    onScrollEnd: function () {
                            ice.mobi.carousel.scrollUpd(event, clientId, this.currPageX);
    	                }
	    });
        return iscroller;
    }
    function Carousel(clientId, key) {
        var myScroll = enhance(clientId);
        var myId = clientId;
        var currentVal=0;
        return {
           scrollUpdate: function(event, pageVal, cfg) {
               var changedVal = false;
               if (currentVal!=pageVal){
                    changedVal = true;
                   var undoNode = document.querySelector('.mobi-carousel-cursor-list > li.active');
                   if (undoNode){
                       undoNode.className = '';
                   }
                   this.setActive(pageVal);
               }
               var isJsfSubmit = (cfg.singleSubmit || behaviors);
               if (changedVal && isJsfSubmit){
                   var behaviors = cfg.behaviors;
                   var submitcfg = {};
                   submitcfg.source = myId;
                   submitcfg.execute = "@all";
                   submitcfg.render = "@all";
                   if (cfg.singleSubmit){
                       submitcfg.execute = "@this";
                   }
                   if (behaviors){
                       /** to do ensure proper format */
                       submitcfg.behaviors = behaviors;
                   }
                   var refreshXHR = function(xhr, status, args) { ice.mobi.carousel.refreshCall(clientId, pageVal);};
                   submitcfg.oncomplete = refreshXHR;
                   mobi.AjaxRequest(submitcfg);
               }
           },
           getClientId: function(){
               return myId;
           },
           getSelectedItem: function(){
               return currentVal;
           } ,
           updateHidden: function( val){
             var hidden = document.getElementById(myId+"_hidden");
             if (hidden){
                 hidden.value= val;
             }
           },
           getHiddenVal: function(){
              var hidden = document.getElementById(myId+'_hidden');
              if (hidden){
                  var temp = hidden.value;
                  return temp;
              } else {
                  return 0;
              }
           },
           setActive: function(val){
               if (currentVal != val){
                  var nodeoldActive = document.querySelector('.mobi-carousel-cursor-list > li.active');
                  if (nodeoldActive){
                      nodeoldActive.className='';
                  }
                  currentVal =  val;
                  this.updateHidden(val);
               }
               var node =  document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (currentVal + 1) + ')');
               if (node){
                   node.className = 'active';
               }
           },
           scrollToPage: function(key){
               myScroll.scrollToPage(key,0);
               this.updateHidden(key);
               this.setActive(key);
               myScroll.refresh();
           } ,
           refreshMe: function(key){
               if (myScroll){
                  myScroll.refresh();
                  this.setActive(key);
               }
           } ,
           updateProperties: function (clientId) {
               this.setActive(this.getHiddenVal());
               if (!myScroll.wrapper)  {
                   console.log('WARNING:_ reinitialized scroller');
                   enhance(clientId);
               }

           }

        }
    }
    ice.mobi.carousel = {
        acarousel: {},
        cfg: {},
        unload: {},
        loaded: function(clientId, cfgIn) {
            if (!this.acarousel[clientId]){
                this.cfg[clientId] = cfgIn;
                this.acarousel[clientId] = Carousel(clientId, cfgIn.key);
                this.acarousel[clientId].setActive(cfgIn.key);
                this.unload[clientId] = function () {
                    ice.mobi.carousel.unloadTest(clientId);
                 };
                 document.addEventListener("DOMSubtreeModified", this.unload[clientId], false);
              //  document.addEventListener("DOMContentLoaded", this.unload(clientId), false);

            } else {
                this.cfg[clientId] = cfgIn;
                this.acarousel[clientId].updateProperties(clientId);
              //  this.refreshCall(clientId, cfgIn.key); //just need this for jsp version
            }
        },
        scrollUpd: function(event, clientId, pageVal){
            this.acarousel[clientId].scrollUpdate(event, pageVal, this.cfg[clientId]);
        },
        scrollTo: function(clientId, key){
            this.acarousel[clientId].scrollToPage(key);
        },
        refreshCall: function(clientId, key){
            this.acarousel[clientId].refreshMe(key);
        },
        unloadTest: function(clientId){
       //    console.log('unloadTest fncall');
            if (!document.getElementById(clientId) && this.acarousel[clientId]!=null){
               console.log("unloadTest setting id="+clientId+" to null");
               this.acarousel[clientId] = null;
               this.cfg[clientId] = null;
               document.removeEventListener("DOMSubtreeModified",this.unload[clientId], false ) ;
            }
        }
  /*      nodeRemoved: function (event) {
            alert ("The element  has been removed with event="+event);
        } ,
        nodeRemovedFromDoc: function (event) {
            alert ("The element  been removed FROM DOC with event="+event);
        } */

    }
  })();
