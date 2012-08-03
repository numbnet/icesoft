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
(function() {
    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables  none for this class

    //-------------------------------------
    function Carousel(clientId) {
        var carouselId = clientId+'_carousel';
        var myScroll = new iScroll(carouselId, {
	                    snap: 'li',
	                    momentum: false,
	                    hScrollbar: false,
                        checkDOMChanges: false,
                        zoom: true,
	                    onScrollEnd: function () {
                            mobi.carousel.scrollUpd(clientId, this.currPageX);
    	                }
	    });
    //    myScroll.refresh();
        return {
           scrollUpdate: function( clientId, pageVal, cfg) {
               var hidden = document.getElementById(clientId+'_hidden');
               var changedVal = false;
               if (hidden){
                   var temp = hidden.value;
                   if (temp!=pageVal){
                        changedVal = true;
                        hidden.value=pageVal;
                        ice.log.debug(ice.log, 'old hidden='+temp+ ' updated to hidden.value = '+hidden.value);
                        document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
                        document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (pageVal + 1) + ')').className = 'active';
                   }
               }
               if (changedVal){
                   var behaviors = cfg.behaviors;
                   var hasBehaviors = false;
                    if (behaviors){
                        hasBehaviors = true;
                    }
                   var singleSubmit = cfg.singleSubmit;
                   if (hasBehaviors){
                      if (behaviors.change){
                            behaviors.change();
                        }
                    }
                    if (!hasBehaviors && singleSubmit){
                        ice.se(null, clientId);
                    }
               }
        //       myScroll.refresh();
           },
           scrollToPage: function(key){
               myScroll.scrollToPage(key,0);
               myScroll.refresh();
           } ,
           updateProperties: function (clientId) {
               var hidden = document.getElementById(clientId+'_hidden');
               if (hidden){
                   var temp = hidden.value;
                   this.scrollUpdate(clientId, temp);
               }
                myScroll.refresh();
           }
        }
    }
    mobi.carousel = {
        acarousel: {},
        cfg: {},
        loaded: function(clientId, cfgIn) {
        //    alert('onload');
            if (!this.acarousel[clientId]){
                this.cfg[clientId] = cfgIn;
                this.acarousel[clientId] = Carousel(clientId);
            } else {
                this.cfg[clientId] = cfgIn;
                this.acarousel[clientId].updateProperties(clientId);
            }
        },
        scrollUpd: function(clientId, pageVal){
            this.acarousel[clientId].scrollUpdate(clientId, pageVal, this.cfg[clientId]);
        },
        scrollTo: function(clientId, key){
            this.acarousel[clientId].scrollToPage(key);
        }
    }

  })();
