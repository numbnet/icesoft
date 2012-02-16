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
mobi.carousel = {
	  acarousel: null,
	  loaded: function(clientId, cfg){
	            var carouselId = clientId+'_carousel';
	            //carousel iscroll loading
	        //    ice.log.debug(ice.log, 'in the carouselLoaded method clientId is '+clientId);
	            setTimeout(function () {
	                if (this.acarousel) {
	                 //   ice.log.debug(ice.log, 'REFRESH existing carousel='+this.acarousel);
	                    mobi.carousel.refresh(clientId, singleSubmit);
	                }
	                else {
	                    ice.log.debug(ice.log, 'CREATE onload carousel');
	                    this.acarousels = new iScroll(carouselId, {
	                    snap: 'li',
	                    momentum: false,
	                    hScrollbar: false, 
	                    onScrollEnd: function () {
                            mobi.carousel.scrollUpdate(clientId, this.currPageX, cfg);
    	                }
	                 });
	                }
	              }, 100);
	               ice.log.debug(ice.log,"after setTimeout function");
	   },
	   unloaded: function(clientId){
	       if (this.acarousel!=null){
      //        ice.log.debug(ice.log, 'DESTROY carousel with id='+clientId);
	           this.acarousel.destroy();
	           this.acarousel = null;
	       }
	   }, 
	   scrollUpdate: function(clientId, pageVal, cfg){
            //only update if different than last one.
  //         ice.log.debug(ice.log, 'scrollUpdate and current page is='+pageVal);
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
                   ice.log.debug(ice.log, ' HAS BEHAVIORS');
                  if (behaviors.change){
                        behaviors.change();
                    }
                }
                if (!hasBehaviors && singleSubmit){
                    ice.se(null, clientId);
                }
           }
	   },
	   refresh: function(clientId, cfg){
		   if (this.acarousel){
               ice.log.debug(ice.log, "  have a carousel to refresh from hidden value");
			   var currPageX = 0;
			   var hidden = document.getElementById(clientId+"_hidden");
			   if (hidden){
				   currPageX = hidden.value;
				   ice.log.debug(ice.log, 'in refresh and currPageX ='+this.currPageX+' hiddenVal is ='+hidden.value);
			   }
		 	   //if this.current is different from hidden, then scroll to hidden value.
			   this.acarousel.scrollToPage(currPageX);
               document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
               document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (this.currPageX + 1) + ')').className = 'active';
			   setTimeout(function(){
			      this.acarousel.refresh();

			   },0);

		   }
           if (!this.acarousel){
           //    ice.log.debug(ice.log, "REFRESH HAS NO OBJECT FOR CAROUSEL clientId="+clientId+' ss -'+singleSubmit);
               this.acarousel=null;
               this.loaded(clientId, cfg);
           }

	   }

}
