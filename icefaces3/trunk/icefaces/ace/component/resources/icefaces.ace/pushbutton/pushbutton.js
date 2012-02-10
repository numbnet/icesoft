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

ice.ace.pushbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
//      if (YAHOO.widget.Logger) {
//            YAHOO.widget.Logger.enableBrowserConsole();
//      }
		YAHOO.util.Event.onDOMReady(function () {
        //want the span id
        var spanId = clientId + "_span";
        YAHOO.log("clientId=" + clientId + " spanId=" + spanId);
        //get the button html element
        var buttonNode = document.getElementById(spanId);

        var button = new YAHOO.widget.Button(spanId,
            {tabindex: null, type: jsProps.type});

	button.addStateCSSClasses = function(state) {
	
		if (state == 'hover') {
			jQuery(this._button).addClass('ui-state-hover');
		} else if (state == 'active') {
			jQuery(this._button).addClass('ui-state-active');
		} else if (state == 'disabled') {
			jQuery(this._button).addClass('ui-state-disabled ');
		}
	};
	
	button.removeStateCSSClasses = function(state) {
	
		if (state == 'hover') {
			jQuery(this._button).removeClass('ui-state-hover');
		} else if (state == 'active') {
			jQuery(this._button).removeClass('ui-state-active');
		} else if (state == 'disabled') {
			jQuery(this._button).removeClass('ui-state-disabled ');
		}
	};
	
        if (jsProps.label) {
            button.set('label', jsProps.label);
        }

        if (jsProps.type) {
            button.set('button', jsProps.type);
        }


        if (jsfProps.disabled) {
            button.set("disabled", true);
        } else {
            button.set("disabled", false);
        }

        var params = function(parameter) {
            var context = ice.ace.getJSContext(clientId);
            var sJSFProps = context.getJSFProps();
            var postParameters = sJSFProps.postParameters;
            if (postParameters != null) {
                 var argCount = postParameters.length / 2;
                 for (var idx =0; idx < argCount; idx ++ ) {
                     parameter( postParameters[idx*2], postParameters[(idx*2)+1] );
                 }
            }
        };


        var onClick = function (e) {
            YAHOO.log(" in onClick and e.target=" + e.target);
            YAHOO.log("  buttonRoot=" + buttonRoot + "  buttonNode=" + buttonNode);
            var divRoot = document.getElementById(clientId);
            var context = ice.ace.getJSContext(clientId);
            var behaviors = context.getJSProps().behaviors;
            var fullSubmit = context.getJSFProps().fullSubmit;

            if (behaviors && behaviors.activate) {
                // Convert core style params into ace style params
                var p = {};
                params(function(name, value) { p[name] = value; });
                ice.ace.ab(ice.ace.extendAjaxArguments(behaviors.activate, {params:p}));
            } else if (fullSubmit) {
                ice.s(e, divRoot, params);
            } else {
                ice.se(e, divRoot, params);
            }
        };

        buttonRoot = document.getElementById(spanId);
        if (jsfProps.aria) {
            //add roles and attributes to the YUI slider widget
            buttonRoot.firstChild.setAttribute("role", "button");
            if (jsfProps.ariaLabel) {
                buttonRoot.firstChild.setAttribute("aria-describedby", jsfProps.ariaLabel);
            } else {
                buttonRoot.firstChild.setAttribute("aria-describedby", "button description unavailable");
            }
            if (jsfProps.disabled) {
                buttonRoot.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
            }
        }

        button.on("click", onClick);


        bindYUI(button);
	 }); // *** end of domready
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
