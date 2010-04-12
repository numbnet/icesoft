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

var YUIHolder = function() {};
YUIHolder.prototype = {
   setComponent:function(component) {
      this.component = component;
   },
   
   getComponent:function() {
      return this.component;
   },
   
   setProperties:function(properties) {
      //check for any update, if changed then update on component  
      this.properties = properties;
   },
   
   setEvents:function(component) {
      this.component = component;
   }   
};

ice = {};
ice.yui = {
    y : null,
    modules: {},
    use :function(callback) {
	        if(this.yui == null) {  
	          logger.info('Loading modules '+  this.getModules());
	           YUI({combine: true, timeout: 10000}).use(this.getModules(), function(Y){
	               this.y = Y;
	               callback(this.y);
	           });
            } else {
               callback(this.y);
            }
    },
    loadModule: function(module) {
        if (!this.modules[module])
             this.modules[module] = module;
    },
    getModules: function() {
        var modules = ''; 
        for (module in this.modules)
             modules+= module + ',';
        return modules.substring(0, modules.length-1);     
    },
    updateProperties:function(clientId, yuiProps, jsfProps, events, lib) {
        this.getInstance(clientId, lib, function(slider) {
            for (prop in yuiProps) {
                var propValue = slider.get(prop);
                if (propValue != yuiProps[prop]) {
                  logger.info('change found in '+ prop +' updating from ['+ propValue + '] to [' + yuiProps[prop]); 
                  slider.set(prop, yuiProps[prop]);        
                }
            }
        },yuiProps, jsfProps);
        
    },
    getInstance:function(clientId, lib, callback, yuiProps, jsfProps) {
        var component = document.getElementById(clientId);
        //could be either new component, or part of the DOM diff
        if (!component['YUIHolder']) {
            component['YUIHolder'] = new YUIHolder();
            lib.register(clientId, function(YUIJS) {
                logger.info('getInstance callback executed');
                component['YUIHolder'].setComponent(YUIJS);
                callback(component['YUIHolder'].getComponent());
            },yuiProps, jsfProps);
        } else {
            callback(component['YUIHolder'].getComponent());
        }
    },
    
    getYUIHolder:function(clientId) {
        var component = document.getElementById(clientId);
        return(component['YUIHolder']);
    }
};
