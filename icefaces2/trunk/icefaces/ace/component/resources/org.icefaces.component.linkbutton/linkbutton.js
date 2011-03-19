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

ice.component.linkButton = {

    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
//	   if (YAHOO.widget.Logger){
//	 	  YAHOO.widget.Logger.enableBrowserConsole();
//       }

	  //YAHOO.util.Event.onDOMReady(function() {
	 ice.yui3.use(function(Y){ 
	 Y.on('domready', function(){
		var thisYUI = ice.yui3.getNewInstance();
		thisYUI.use('yui2-button', function(Yui) {
		var YAHOO = Yui.YUI2;

        var spanId = clientId + "_span";
	    var oLinkButton = new YAHOO.widget.Button(spanId,{ label: jsProps.label, tabindex: null }, {type: jsProps.type});

        root = document.getElementById(spanId);

		root.firstChild.setAttribute("role", "link");
	    root.firstChild.setAttribute("aria-labelledby",jsProps.label);
	    if (jsfProps.disabled){
	        root.firstChild.setAttribute("aria-disabled", jsfProps.disabled);
	    }
	    // If there's no action listener, this is standard anchor behaviour
	    // otherwise it's got an actionListener/action attribute. Described by offers further description
	    if (!jsfProps.doAction ) {
	       root.firstChild.setAttribute("aria-describedby", "Standard HTML anchor");
	    } else {
           root.firstChild.setAttribute("aria-describedby", "JSF action event source");
        } 

		bindYUI(oLinkButton);
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
        ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
    },
 
    //delegate call to ice.yui.getInstance(..) with the reference of this lib
    getInstance:function(clientId, callback) {
        ice.component.getInstance(clientId, callback, this);
    },

    // Click handler visible from Renderer code 
    clickHandler : function (e, clientId) {
        var JSContext = ice.component.getJSContext(clientId);
        var singleSubmit = JSContext.getJSFProps().singleSubmit;
        var doAction = JSContext.getJSFProps().doAction;
        //YAHOO.log("--> Button.doAction = " + doAction);

        var divRoot = document.getElementById(clientId);

        var postParameters = JSContext.getJSFProps().postParameters;
        var params = function(parameter) {
            if (postParameters != null) {
                var argCount = postParameters.length / 2;
                for (var idx =0; idx < argCount; idx ++ ) {
                    parameter( postParameters[idx*2], postParameters[(idx*2)+1] );
                }
            }
        };
        if (singleSubmit) {
            //YAHOO.log("Single Submit on element: " + divRoot);
            ice.se(e, divRoot, params );
        } else {
            //YAHOO.log("Full Submit on element: " + divRoot);
            ice.s(e, divRoot, params );
        }
        // If there are actionListeners, don't do default behaviour
        if (doAction) {
            return false;
        }
    },

    // keyDown handler visible from Renderer code
    keyDownHandler : function (e, clientId) {
        if (e.keyCode != 13) {
            return true;
        }
        var JSContext = ice.component.getJSContext(clientId);
        var singleSubmit = JSContext.getJSFProps().singleSubmit;
        var doAction = JSContext.getJSFProps().doAction;
        //YAHOO.log("--> Button.doAction = " + doAction);

        var divRoot = document.getElementById(clientId);

        var postParameters = JSContext.getJSFProps().postParameters;
        var params = function(parameter) {
            if (postParameters != null) {
                var argCount = postParameters.length / 2;
                for (var idx =0; idx < argCount; idx ++ ) {
                    parameter( postParameters[idx*2], postParameters[(idx*2)+1] );
                }
            }
        };
        if (singleSubmit) {
            //YAHOO.log("Single Submit on element: " + divRoot);
            ice.se(e, divRoot, params );
        } else {
            //YAHOO.log("Full Submit on element: " + divRoot);
            ice.s(e, divRoot, params );
        }
        // If there are actionListeners, don't do default behaviour
        if (doAction) {
            return false;
        }
    }
};
