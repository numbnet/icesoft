/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
    function is_firefox(){
        return navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
    }
    function enhance(clientId, key)  {
        var carouselId = clientId+'_carousel';
        var page = key || 0;
        var iscroller = new IScroll(document.getElementById(carouselId), {
            scrollX: true,
            scrollY: false,
            snap: 'li',
            snapSpeed: 400,
            momentum: false,
            scrollbars: false,
            bounce: false,
            keyBindings: true,
            zoom: false
	    });
        iscroller.on('scrollEnd', function () {
            var evt = window.event; 
            if( !evt ) evt = null;
            ice.mobi.carousel.scrollUpd(evt, clientId, this.currPageX);
            });
        return iscroller;
    }
    function Carousel(clientId, key) {
        var myScroll = enhance(clientId, key);
        myScroll.goToPage(key, 0, 10);
        var myId = clientId;
        var currentVal=key;
        return {
           scrollUpdate: function(event, cfg) {
               var changedVal = false;
               var pageVal = myScroll.currentPage.pageX;
            //   console.log('pageVal passed in='+pageVal);
               if (currentVal!=pageVal){
                    changedVal = true;
                   var undoNode = document.querySelector('.mobi-carousel-cursor-list > li.active');
                   if (undoNode){
                       undoNode.className = '';
                   }
               //    console.log( 'old hidden='+this.getHiddenVal()+ ' updated to hidden.value = '+pageVal);
                   this.setActive(pageVal);
               }
               if (changedVal){
                   var behaviors = cfg.behaviors;
                   var hasBehaviors = false;
                   if (cfg.behaviors){
                       hasBehaviors = true;
                   }
                   var submitcfg = {};
                   submitcfg.source = myId;
                   submitcfg.execute = "@all";
                   submitcfg.render = "@all";
                   if (cfg.singleSubmit){
                       submitcfg.execute = "@this";
                       var refreshXHR = function(xhr, status, args) { ice.mobi.carousel.refreshCall(clientId, pageVal);};
                       submitcfg.oncomplete = refreshXHR;
                       mobi.AjaxRequest(submitcfg);
                   }
                   else if (hasBehaviors && cfg.behaviors.change){
                       ice.mobi.ab(cfg.behaviors.change);
                   }
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
           setActive: function(pageVal){
               
               if (currentVal != pageVal){
                  var nodeoldActive = document.querySelector('.mobi-carousel-cursor-list > li.active');
                  if (nodeoldActive){
                      nodeoldActive.className='';
                  }
                  currentVal =  pageVal;
                  this.updateHidden(pageVal);
               }
               var anode = document.querySelector('.mobi-carousel-cursor-list > li:nth-child('+(pageVal + 1) + ')');
               if (anode){
                   anode.className = 'active';
               }
               myScroll.refresh();
           },
           scrollToPage: function(key){
               myScroll.goToPage(key,0);
               var newVal = currentVal;
               if (key == "next"){
                   newVal++;
               }
               if (key== "prev"){
                   newVal--;
               }
               this.setActive(newVal);
           },
           refreshMe: function(key){
               if (myScroll){
                  this.setActive(key);
               }
           },
           updateProperties: function (clientId, cfgIn) {
               var hid= this.getHiddenVal();
               if (hid != currentVal){
                  this.goToPage(hid, 0, 10);
                  this.setActive(hid);
               }
               if (!myScroll.wrapper)  {
                   enhance(clientId);
               }
               if (is_firefox()){
                   ice.mobi.carousel.acarousel[clientId] = Carousel(clientId, cfgIn.key);
               }
           },
           disable: function(){
              myScroll.disable();
           } ,
            unload: function(){
                myScroll.destroy();
                myScroll = null;
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
            //  var node = document.getElementById(clientId);
                ice.mobi.addListener(document, "DOMSubtreeModified", this.unload[clientId]);
            } else {
                this.cfg[clientId] = cfgIn;
                this.acarousel[clientId].updateProperties(clientId, cfgIn);
                this.acarousel[clientId].setActive(cfgIn.key);

            }
        },
        scrollUpd: function(event, clientId, pageVal){
            if (this.acarousel[clientId]){
                this.acarousel[clientId].scrollUpdate(event, this.cfg[clientId]);
            }
        },
        scrollTo: function(clientId, key){
            this.acarousel[clientId].scrollToPage(key);
        },
        refreshCall: function(clientId, key){
            if (this.acarousel[clientId]){
                this.acarousel[clientId].refreshMe(key);
            }
        },
        unloadTest: function(clientId){
            if (!document.getElementById(clientId) && this.acarousel[clientId]!=null){
                this.acarousel[clientId].unload();
                //  console.log("unloadTest disable and then setting id ="+clientId+" to null");
                this.acarousel[clientId] = null;
                this.cfg[clientId] = null;
                document.removeEventListener("DOMSubtreeModified",this.unload[clientId], false ) ;
            }
        }
    }
  })();
