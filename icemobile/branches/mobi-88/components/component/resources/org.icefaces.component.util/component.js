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

mobi.flipper = {                
        init: function(clientId, event, flipperEl, singleSubmit){
              ice.log.debug(ice.log, 'clientId is '+clientId);               
              if (flipperEl){
                var oldClass = flipperEl.className; 
                ice.log.debug(ice.log, 'have a switch element with class='+oldClass);
                var value = "off";
                if (oldClass.indexOf('-off ')>0){
                	flipperEl.className='mobi-flip-switch mobi-flip-switch-on ';
                	value = true;
                }else{
                	flipperEl.className='mobi-flip-switch mobi-flip-switch-off ';
                	value = false;
                }
                mobi.input.updateHidden(clientId, event, value, singleSubmit);
              }
              else{
                ice.log.debug(ice.log, 'do not have a switch element');
              }

         },
}       
mobi.carousel = {
		  carousels: null,
		  loaded: function(clientId, singleSubmit){
		            ice.log.debug(ice.log, 'clientId is '+clientId);
		            var carouselId = clientId+'_carousel';
		            //carousel iscroll loading
		            ice.log.debug(ice.log, 'in the carouselLoaded method');
		            setTimeout(function () {
	    	            ice.log.debug(ice.log, "in setTimeout function");
		                if (this.carousels) {
		                    ice.log.debug(ice.log, 'have existing carousel');
		                //    this.carousels.destroy();
		                    ice.mobi.refresh(clientId, singleSubmit);
		                }
		                else {
		                    ice.log.debug(ice.log, 'onload create carousel');
		                    this.carousels = new iScroll(carouselId, {
		                    snap: 'li',
		                    momentum: false,
		                    hScrollbar: false, 
		                    onScrollEnd: function () {
//		                        document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
//		                        document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (this.currPageX + 1) + ')').className = 'active';
	                            ice.log.debug(ice.log, 'onScrollEnd and current page is='+this.currPageX);
	                            var hidden = document.getElementById(clientId+'_hidden');
	                            mobi.carousel.scrollUpdate(clientId, singleSubmit, this.currPageX);
//	                            if (hidden){                               
//	                            	hidden.value=this.currPageX;
//	                            	ice.log.debug(ice.log, 'set hidden.value = '+hidden.value);
//	                            }
//	                            if (singleSubmit){
//	                            	ice.ser(null, clientId);
//	                            }
	    	                }
		                 });
		                }
		              }, 100);
		               ice.log.debug(ice.log,"after setTimeout function");
		   },
		   unloaded: function(clientId){
		       ice.log.debug(ice.log, 'unload handler carousel');
		       if (this.carousels!=null){
		           this.carousels.destroy();
		           this.carousels = null;
		       }
		   }, 
		   scrollUpdate: function(clientId, singleSubmit, pageVal){
	           document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
	           document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (pageVal + 1) + ')').className = 'active';
	           ice.log.debug(ice.log, 'scrollUpdate and current page is='+pageVal);
	           var hidden = document.getElementById(clientId+'_hidden');
	           if (hidden){                               
	           	hidden.value=pageVal;
	           	ice.log.debug(ice.log, 'set hidden.value = '+hidden.value);
	           }
	           if (singleSubmit){
	           	ice.se(null, clientId);
	           } 
		   },
		   refresh: function(clientId, singleSubmit){
			   ice.log.debug(ice.log, 'refresh carousel');
			   if (this.carousels!=null){
				   var currPageX = 1;
				   var hidden = document.getElementById(clientId+"_hidden");
				   if (hidden){
					   currPageX = hidden.value;
					   ice.log.debug(ice.log, 'in refresh and currPageX ='+this.currPageX+' hiddenVal is ='+hidden.value);
				   }
				   //if this.current is different from hidden, then scroll to hidden value.
				   this.carousels.scrollToPage(currPageX);
	               document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
	               document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (this.currPageX + 1) + ')').className = 'active';
				   setTimeout(function(){
					   this.carousels.refresh();
				   },0);
	              
			   }
		   }
	}

mobi.flipjq = {
		
		init: function(clientId, event, value, singleSubmit){
			console.log("in init for flipjq");
//		  ice.log.debug(ice.log, 'clientId ='+clientId+" for jqflipper with value="+value);	
		  if (value){
			  ice.log.debug(ice.log, ' sliderVal is='+value);
		  }else{
			  ice.log.debug(ice.log, 'no sliderVal');
		  }
		  if (singleSubmit){
			  ice.se(event, clientId);
		  }else{
			  ice.log.debug(ice.log, 'singleSubmit false but value='+value);
		  }
			  
		 // mobi.input.updateHidden(clientId, event, value, false);
		},
}
		

mobi.input = {
	
    updateHidden: function(clientId, event, value, singleSubmit) {
    	ice.log.debug(ice.log,'in updateHidden');
    	var element = document.getElementById(clientId);
    	var hiddenId = clientId+'_hidden';
    	var hidden = document.getElementById(hiddenId);
    	if (value){ //no change
    		ice.log.debug(ice.log, ' value='+value);
    		element.value+=value+'';
    	}
    	if (hidden){
        	hidden.value = value+'';
         	ice.log.debug(ice.log, 'value has been updated to:'+hidden.value);
        } else {
           /* using this function generically also for jquery comps which may not have a hidden field*/
           var input = document.createElement("input");
           input.setAttribute("type", "hidden");
           input.setAttribute("name", clientId + "_hidden");
           input.setAttribute("id", clientId + "hidden");
           input.setAttribute("value", value+'');
           element.appendChild(input);  
        }
    	//prevVal = value;
 	    if (singleSubmit){
			ice.se( event, clientId);
  	   }
    },

};

