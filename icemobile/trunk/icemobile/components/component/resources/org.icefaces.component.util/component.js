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

/*mobi.flipper = {
        init: function(clientId, event, flipperEl, singleSubmit){
              ice.log.debug(ice.log, 'clientId is '+clientId);               
              if (flipperEl){
                var oldClass = flipperEl.className;
                var value = "off";
                if (oldClass.indexOf('-off ')>0){
                	flipperEl.className='mobi-flip-switch mobi-flip-switch-on ';
                	value = true;
                }else{
                	flipperEl.className='mobi-flip-switch mobi-flip-switch-off ';
                	value = false;
                }
                var hidden = clientId+"_hidden";
                var thisEl = document.getElementById(hidden);
                if (thisEl){
                    thisEl.value=value.toString();
                }
                if (singleSubmit){
                    ice.se(event, clientId);
                }
              }
              else{
                ice.log.debug(ice.log, 'do not have a switch element');
              }

         }
} ; */
/*mobi.carousel = {
		  carousels: null,
		  loaded: function(clientId, singleSubmit){
		            ice.log.debug(ice.log, 'clientId is '+clientId);
		            var carouselId = clientId+'_carousel';
		            //carousel iscroll loading
		            setTimeout(function () {
	    	        //    ice.log.debug(ice.log, "in setTimeout function");
                    //    ice.log.debug(ice.log, 'onload create carousel');
                        this.carousels = new iScroll(carouselId, {
                            snap: 'li',
                            momentum: false,
                            hScrollbar: false, 
                            onScrollEnd: function () {
                                ice.log.debug(ice.log, 'onScrollEnd and current page is='+this.currPageX);
                                var hidden = document.getElementById(clientId+'_hidden');
                                mobi.carousel.scrollUpdate(clientId, singleSubmit, this.currPageX);
                            }
		                 });
		              }, 100);
		   },
		   unloaded: function(clientId){
		 //      ice.log.debug(ice.log, 'unload handler carousel');
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
	} ; */


/*mobi.input = {

	   submit : function(event, clientId, value, singleSubmit){
        var valueString = value.toString();
        var thisEl = document.getElementById(clientId);
        if (thisEl==null){
            ice.log.debug(ice.log, 'cannot get hidden field for clientId='+clientId+' submit value ='+value);
            return;
        }
        else {
            thisEl.value = valueString;
        }
        if (singleSubmit){
            ice.se(event, clientId);
        }
    }

}; */

function html5getViewState(form) {
    if (!form) {
        throw new Error("jsf.getViewState:  form must be set");
    }
    var els = form.elements;
    var len = els.length;
    var qString = [];
    var addField = function(name, value) {
        var tmpStr = "";
        if (qString.length > 0) {
            tmpStr = "&";
        }
        tmpStr += encodeURIComponent(name) + "=" + encodeURIComponent(value);
        qString.push(tmpStr);
    };
    for (var i = 0; i < len; i++) {
        var el = els[i];
        if (!el.disabled) {
            switch (el.type) {
                case 'submit':
                case 'button':
                    break;
                case 'text':
                case 'password':
                case 'hidden':
                case 'textarea':
                    addField(el.name, el.value);
                    break;
                case 'select-one':
                    if (el.selectedIndex >= 0) {
                        addField(el.name, el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    for (var j = 0; j < el.options.length; j++) {
                        if (el.options[j].selected) {
                            addField(el.name, el.options[j].value);
                        }
                    }
                    break;
                case 'checkbox':
                case 'radio':
                    if (el.checked) {
                        addField(el.name, el.value);
                    }
                    break;
                default:
                    addField(el.name, el.value);
            }
        }
    }
    // concatenate the array
    return qString.join("");
};

if (window.addEventListener)  {
    window.addEventListener( "load", 
    function() {jsf.getViewState = html5getViewState}, false );
}
