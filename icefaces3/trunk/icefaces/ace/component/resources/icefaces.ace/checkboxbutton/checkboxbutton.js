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

ice.ace.checkboxbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
		YAHOO.util.Event.onDOMReady(function () {
	
	  var Dom = YAHOO.util.Dom;
      var divNode = document.getElementById(clientId);

//      if (YAHOO.widget.Logger){
//	 	  YAHOO.widget.Logger.enableBrowserConsole();
//      }
       var spanId = clientId+"_span";
	   var button = new YAHOO.widget.Button(spanId, {type: jsProps.type, tabindex: null});
	   
	button.addStateCSSClasses = function(state) {
	
		var node = jQuery(this._button);
		// add span for icon
		if (!node.find('.ui-icon').get(0)) {
			node.prepend(jQuery('<span />').addClass('ui-icon').addClass('ui-icon-unchecked'));
		}
	
		if (state == 'hover') {
			jQuery(this._button).addClass('ui-state-hover');
		} else if (state == 'checked') {
			var node = jQuery(this._button);
			node.addClass('ui-state-active');
			node.find('.ui-icon').removeClass('ui-icon-unchecked').addClass('ui-icon-check');
		} else if (state == 'disabled') {
			jQuery(this._button).addClass('ui-state-disabled');
		}
	};
	
	button.removeStateCSSClasses = function(state) {
	
		if (state == 'hover') {
			jQuery(this._button).removeClass('ui-state-hover');
		} else if (state == 'checked') {
			var node = jQuery(this._button);
			node.removeClass('ui-state-active');
			node.find('.ui-icon').removeClass('ui-icon-check').addClass('ui-icon-unchecked');
		} else if (state == 'disabled') {
			jQuery(this._button).removeClass('ui-state-disabled');
		}
	};

		var hiddenField = Dom.get(clientId+"_hidden");

		//use input hidden field instead to update value for server push
  		if(jsProps.checked) {
   			button.set('checked', hiddenField.value);
 		}
    
		var onCheckedChange = function (e) {
           var context = ice.ace.getJSContext(clientId);
           var postParameters = context.getJSFProps().postParameters;
           var params = function(parameter) {
               if (postParameters != null) {
                   var argCount = postParameters.length / 2;
                   for (var idx =0; idx < argCount; idx ++ ) {
                       parameter( postParameters[idx*2], postParameters[(idx*2)+1] );
                   }
               }
           };
		    buttonNode = document.getElementById(spanId);
	        divNode = document.getElementById(clientId);
			//get the current value of checked
 	        var submittedValue =  e.newValue;

            YAHOO.log("         e.newValue="+e.newValue+" old="+e.prevValue);

			hiddenField.value=submittedValue;

	        YAHOO.log(" hidden Field="+clientId+"_hidden"+" has value="+hiddenField.value);

			var behaviors = context.getJSProps().behaviors;
			if (behaviors) {
				if (behaviors.activate) {
					ice.ace.ab(behaviors.activate);
				}
			}
		};

		button.on("checkedChange", onCheckedChange);
		
		// add icon element
		var addIcon = function() {
			if(jsProps.checked) {
				button.addStateCSSClasses('checked');
			} else {
				button.addStateCSSClasses('');
			}
		};
		setTimeout(addIcon, 10);

	    if (jsfProps.aria) {
	         //add roles and attributes to the YUI slider widget
	            buttonNode = document.getElementById(spanId);
	            buttonNode.firstChild.setAttribute("role", "button");
	            buttonNode.firstChild.setAttribute("aria-describedby",jsProps.label);
	            if (jsfProps.disabled){
	            	buttonNode.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
	            }
	            //listen for keydown event, to provide short-cut key support.

	            button.on("keydown", function(event) {
	                //get the current value of the element and set the aria values
	            	//the space bar toggles the button
	                var isSpace = event.keyCode == 32;

	                if (isSpace) {
	                	// submit the thing
	                	YAHOO.log(" submit this thing for keycode="+event.keyCode);

	                	//update the root of this element to set the aria values
	        			buttonNode = document.getElementById(spanId);
	          			//get the current value of checked
	         	        var submittedValue =  event.newValue;
	         	        if (submittedValue){
	        	            buttonNode.firstChild.setAttribute("aria-checked",true);
	         	        }else {
	        	            buttonNode.firstChild.setAttribute("aria-checked",false);
	         	        }

	         	       //the space seems to fire the onClick event for both FF and Safari
	                }
	            }, divNode);
	    }
		bindYUI(button);
	 }); // *** end of ondomready
	},
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, jsProps, jsfProps, events) {        		

        var context = ice.ace.getJSContext(clientId);
        if (context && context.isAttached()) {
            var prevJSFProps = context.getJSFProps();
            if (prevJSFProps.hashCode != jsfProps.hashCode) {
                context.getComponent().destroy();
                document.getElementById(clientId)['JSContext'] = null;
                JSContext[clientId] = null;
            }
        }
       ice.ace.updateProperties(clientId, jsProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.ace.getInstance(clientId, callback, this);
   }


};
