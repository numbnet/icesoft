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

mobi.flipjq = {
		
		init: function(clientId, event, value, singleSubmit){
		  ice.log.debug(ice.log, 'clientId ='+clientId+" for jqflipper with value="+value);	
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
    	var prevVal;
    	var element = document.getElementById(clientId);
    	var hiddenId = clientId+'_hidden';
    	var hidden = document.getElementById(hiddenId);
    	if (prevVal == value){ //no change
    		ice.log.debug(ice.log, 'prevVal='+prevVal+' value='+value);
    		return;
    	}
    	if (hidden){
        	hidden.value = value;
         	ice.log.debug(ice.log, 'value has been updated to:'+hidden.value);
        } else {
           /* using this function generically also for jquery comps which may not have a hidden field*/
           var input = document.createElement("input");
           input.setAttribute("type", "hidden");
           input.setAttribute("name", clientId + "_hidden");
           input.setAttribute("id", clientId + "hidden");
           input.setAttribute("value", value);
           element.appendChild(input);  
        }
    	prevVal = value;
 	    if (singleSubmit){
			ice.se( event, clientId);
  	   }
    },

};

