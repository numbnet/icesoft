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
logger = {
   info: function(msg) {
	   if (window["console"]) {
	   	    window["console"].info (msg);
	   }else {
	        //alert(msg);
	   }
   }
};

Ice = {};
Ice.component = {
    logEnabled: true,
    registeredComponents:[],
    register:function(_id, lib) {
       this.log('register called', {'_id': _id, 'lib': lib});
       var ele = this.getElement(_id);
       if (!ele['ICE']) {
          ele['ICE'] = {};
       }
       if (!ele['ICE']['obj']) {
          this.log('register called', {'iceComponent':'Not Found'});  
          var call = lib.initialize(_id);     
          this.log('register called.. ', {'call':call}); 
          var comp = eval(call);
          ele['ICE']['obj'] = comp;
          this.log('register called', {'component added ':comp});
          ele.onmouseover = null; 
          this.registeredComponents[_id] = comp;          
          return this.registeredComponents[_id];         
       }
    },
    
    getElement:function(_id) { 
        return document.getElementById(_id);
    },
 
    getInstance:function(_id, lib) {
        logger.info('get instance called');
        var ele = this.getElement(_id);
        if(ele) {
            if (ele['ICE']) {
                if (ele['ICE']['obj']) {
                  logger.info('get instance called returnming exixting');
                    return ele['ICE']['obj'];
                }
            }
        }
        logger.info('get instance called returnming NEEEEEW');
        return this.register(_id, lib);
    },
    
    log:function(msg, props) {
        if (!this.logEnabled)return;
        for(p in props) {
            msg+= '  ['+ p +' = '+ props[p] + ']';
        }
        logger.info(msg);
    },
    
    updateProperties:function(_id, props, lib, client) {
        logger.info('updateProperties:function');
       client = client || false;
   
       var ele = this.getElement(_id);
       if (!ele['ICE']) {
       logger.info('updateProperties:function NO ICEEEEEE');
          ele['ICE'] = {};
       }
       
       if (!ele['ICE']['props']) {
          ele['ICE']['props'] = {};
       }
       
       for (p in props) {
         var pn = p;
         var ev;
         if (['ICE']['props'] && ['ICE']['props'][p])
              ev = ['ICE']['props'][p]['new'];
         var cv = props[p];
         if (!ev) {
            this.log('updateProperties() new value added .. ' + p, {'eleVal': ev,'compVal': cv});
            ele['ICE']['props'][pn] = {'new': cv, 'previous': null, 'modifiedByClient': (client)};
            continue;
         }
         if (ev  != cv) {
            ele['ICE']['props'][pn] = {'new': cv, 'previous': ev , 'modifiedByClient': (client)};      
            this.log('updateProperties() property changed', {'eleValue':ev, 'compValue':cv }); 
         } else {
            this.log('updateProperties() property intact', {'eleValue':ev, 'compValue':cv });
         }
       }
       return this.getInstance(_id, lib);
    },
    
    getProperty:function(_id, prop) {
       var ele = this.getElement(_id);
       if (ele['ICE'] && ele['ICE']['props']) {
          return ele['ICE']['props'][prop];
       }
       var call = this.getElement(_id+'call');
       eval(call.innerHTML);
       return ele['ICE']['props'][prop];;    
    },
    
    getUtilForm:function() {
        var uform = document.getElementById('ice-util-form');
        if (!uform){
           logger.info('Utility form was not found creatingggg one');       
           uform = document.createElement('form');
           uform.setAttribute('id', 'ice-util-form');
           uform.setAttribute('onsubmit', 'return false;');
           uform.setAttribute('action', 'javascript:;');  
           document.body.appendChild(uform);         
           logger.info('Utility form was not found created one'); 
        }
        return uform;
    }
    
         
};
