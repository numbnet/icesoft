/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

if (!window['mobi']) {
    window.mobi = {};
}
mobi.carousel = {
	  acarousel: null,
	  loaded: function(clientId, singleSubmit){
	            var carouselId = clientId+'_carousel';
	            //carousel iscroll loading
	        //    ice.log.debug(ice.log, 'in the carouselLoaded method clientId is '+clientId);
	            setTimeout(function () {
	                if (this.acarousel) {
	                 //   ice.log.debug(ice.log, 'REFRESH existing carousel='+this.acarousel);
	                    mobi.carousel.refresh(clientId, singleSubmit);
	                }
	                else {
	                //    ice.log.debug(ice.log, 'CREATE onload carousel');
	                    this.acarousels = new iScroll(carouselId, {
	                    snap: 'li',
	                    momentum: false,
	                    hScrollbar: false, 
	                    onScrollEnd: function () {
                           // ice.log.debug(ice.log, 'onScrollEnd and current page is='+this.currPageX);
                            var hidden = document.getElementById(clientId+'_hidden');
                            mobi.carousel.scrollUpdate(clientId, singleSubmit, this.currPageX);
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
	   scrollUpdate: function(clientId, singleSubmit, pageVal){
           document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
           document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (pageVal + 1) + ')').className = 'active';
       //    ice.log.debug(ice.log, 'scrollUpdate and current page is='+pageVal);
           var hidden = document.getElementById(clientId+'_hidden');
           if (hidden){                               
           	hidden.value=pageVal;
      //     	ice.log.debug(ice.log, 'set hidden.value = '+hidden.value);
           }
           if (singleSubmit){
           	ice.se(null, clientId);
           } 
	   },
	   refresh: function(clientId, singleSubmit){
		   if (this.acarousel){
               ice.log.debug(ice.log, "  have a carousel to refresh");
			   var currPageX = 1;
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
               this.loaded(clientId, singleSubmit);
           }

	   }

}

