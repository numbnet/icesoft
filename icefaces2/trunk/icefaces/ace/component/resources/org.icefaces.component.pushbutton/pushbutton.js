/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

ice.component.pushbutton = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
//      if (YAHOO.widget.Logger) {
//            YAHOO.widget.Logger.enableBrowserConsole();
//      }

	 ice.yui3.use(function(Y){ 
	 Y.on('domready', function(){
		var thisYUI = ice.yui3.getNewInstance();
		thisYUI.use('yui2-button', function(Yui) {
	    var YAHOO = Yui.YUI2;
	 
      //YAHOO.util.Event.onDOMReady(function() {
        //want the span id
        var spanId = clientId + "_span";
        YAHOO.log("clientId=" + clientId + " spanId=" + spanId);
        //get the button html element
        var buttonNode = document.getElementById(spanId);

        var button = new YAHOO.widget.Button(spanId,
            {label: jsProps.label, tabindex: null,
            type: jsProps.type});


        if (jsProps.label) {
            button.set('label', jsProps.label);
        }

        if (jsProps.type) {
            button.set('button', jsProps.type);
        }
/*
        if (!jsProps.tabindex) {
            button.set('tabindex', jsProps.tabindex);
        }
*/

        if (jsfProps.disabled) {
            button.set("disabled", true);
        } else {
            button.set("disabled", false);
        }

        var params = function(parameter) {
            var context = ice.component.getJSContext(clientId);
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
            //singleSubmit means button just submits itself and renders itself
            //single submit false means that it submits the form
            var context = ice.component.getJSContext(clientId);
            var singleSubmit = context.getJSFProps().singleSubmit;

            if (singleSubmit) {
                YAHOO.log(" single submit is true for clientId=" + spanId);
                ice.se(e, divRoot, params);
            } else {
                YAHOO.log("single Submit is false for clientId=" + spanId);
                ice.s(e, divRoot, params);
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
	 }); // *** end of thisYUI
	 }); // *** end of ondomready
	 }); // *** end of function(Y)
      //});
    },
	
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, jsProps, jsfProps, events) {
       var context = ice.component.getJSContext(clientId);
       if (context && context.isAttached()) {
           var prevJSFProps = context.getJSFProps();
           if (prevJSFProps.hashCode != jsfProps.hashCode) {
               context.getComponent().destroy();
               document.getElementById(clientId)['JSContext'] = null;
               JSContext[clientId] = null;
           }
       }
	   ice.yui3.updateProperties(clientId, jsProps, jsfProps, events, this);
       //ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   }
   
   
};
