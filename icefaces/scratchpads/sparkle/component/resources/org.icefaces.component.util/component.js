/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
 
logger = {
   info: function(msg) {
	   if (window["console"]) {
	   	    window["console"].info (msg);
	   }else {
	        //alert(msg);
	   }
   }
};

var JSContext = function() {};
JSContext.prototype = {
   setComponent:function(component) {
      this.component = component;
   },
   
   getComponent:function() {
      return this.component;
   },
   
   setJSProps:function(props) {
      this.jsPorps = props;
   },
   
   getJSProps:function() {
      return this.jsPorps;
   }  
};

ice.component = {
    updateProperties:function(clientId, jsProps, jsfProps, events, lib) {
        this.getInstance(clientId, function(yuiComp) {
            for (prop in jsProps) {
                var propValue = yuiComp.get(prop);
                if (propValue != jsProps[prop]) {
                  logger.info('change found in '+ prop +' updating from ['+ propValue + '] to [' + jsProps[prop]); 
                  yuiComp.set(prop, jsProps[prop]);        
                }
            }
        }, lib, jsProps, jsfProps);
        
    },
    getInstance:function(clientId, callback, lib, jsProps, jsfProps) {
        var component = document.getElementById(clientId);
        //could be either new component, or part of the DOM diff
        if (!component['JSContext']) {
            component['JSContext'] = new JSContext();
            component['JSContext'].setJSProps(jsProps);
            lib.initialize(clientId, jsProps, jsfProps, function(YUIJS) {
                logger.info('getInstance callback executed..');
                component['JSContext'].setComponent(YUIJS);
                callback(component['JSContext'].getComponent());
            });
        } else {
            component['JSContext'].setJSProps(jsProps);
            callback(component['JSContext'].getComponent());
        }
    },
    
    getJSContext: function(clientId) {
        var component = document.getElementById(clientId);
        if (component)
            return component['JSContext'];
        return null;
    }
};
