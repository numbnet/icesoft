Ice.component.tabset= Class.create();
Ice.component.tabset = {
    initialize:function(clientId) {
       logger.info('tabset initialize');
       var tabview = new YAHOO.widget.TabView(clientId);  
       var o = Ice.component.getProperty(clientId, 'orientation');
       if(o) {
          logger.info('ori  new -------'+  o['new'] + ' : pre '+  o['previous'] + ' : source '+ o['modifiedByClient']);
          tabview.set('orientation', o['new']);
       }
       var thiz = this;
       var tabChange=function(event) {
             logger.info('Tab Change');
             var o = Ice.component.getProperty(clientId, 'orientation');
             var t = Ice.component.getProperty(clientId, 'tabIdx');
             logger.info('Tab Change ORI '+ o['new']);
             logger.info('Tab Change tIdx '+ t['new']);
             
             var form=formOf(document.getElementById(clientId)); 
             
             //create a utility form
             if (!form) { 
                 form = Ice.component.getUtilForm();
             }             
             
             //get hidden hidden field which keeps tab index
             var hdn = thiz.getHdnFld(form); 
                       
             if (hdn) { 
                  hdn.value = clientId + '='+ tabview.getTabIndex(event.newValue);
                  var query = null;
                  var isClientSide = Ice.component.getProperty(clientId, 'isClientSide')['new'];
                  logger.info('Tab Change isClientSide '+ isClientSide);                
                  query = new Ice.Parameter.Query(); 
                  query.add(hdn.id, hdn.value); 
                                   
                  if (isClientSide){
                     logger.info('Client side tab ');
                  } else {
                      var partialSubmit = Ice.component.getProperty(clientId, 'partialSubmit');
                      if (partialSubmit) {
                          iceSubmitPartial(form, hdn, event, query);
                      } else {
                          iceSubmit(form, hdn, event, query); 
                      }               
                  }                  
             }             
  
/*            
             if(obj['idxSetByRenderer']) { 
                 obj['idxSetByRenderer']=false; 
                 logger.info('the tab change caused by the server so do not do anything.'); 
                 return; 
             }  
             var activeIndex = obj.get('activeIndex');
             var form=formOf(document.getElementById(clientId)); 
             logger.info('Dynamic form '+ form);
             if (!form) { 
                 form = Ice.yui.getUtilForm();
             }
             var hdn = Ice.yui.tabset.getInxFld(form); 
             logger.info('2.Tab Change ..'+ hdn); 
             logger.info('Tab Change ID ..'+ hdn.id);                         
             if (hdn) { 
                  hdn.value = clientId+ '='+ obj.get('activeIndex'); 
                  var query = null; 
                  logger.info('3.Tab Change'+ obj); 
                  if (props.dynamic) {
                      logger.info('Dynamic and ParitlaSubmit');  

                      if (!form) { 
                          logger.info('tabset is not inside the form'); 

                          query = new Ice.Parameter.Query(); 
                          logger.info('query object created ...'+ query);
                          query.add(clientId + 'hdn', hdn.value);
                      } 
                      if (props.partialSubmit) {
                          iceSubmitPartial(form, hdn, event, query);
                      } else {
                          iceSubmit(form, hdn, event, query); 
                      }
                   }//dynamic
                  logger.info('Active index '+ obj.get('activeIndex')); 
             }//hdn;    
*/
       }//tabchange; 
       tabview.addListener('activeTabChange', tabChange);       
       return tabview;
   },
   
   updateProperties:function(_id, props) {
         logger.info('Updateing properties orientation ');
	     //update properties first
	     var tabview = Ice.component.updateProperties(_id, props, this);
	     logger.info('Updateing tabview tabview ');
   },
   
    getHdnFld: function(form) {  logger.info('getInxFld called: '+ form); 
        var fn = "yti";
        var yti = YAHOO.util.Dom.getElementBy(function(ele) {
            if (ele.id == fn) return true;
        }, 'input', form);
        logger.info('getInxFld yti: '+ yti);         
        if (YAHOO.lang.isUndefined(yti.id)) {
           logger.info('getInxFld creatinggg : '+ yti);  
           yti = document.createElement('input');
           yti.setAttribute('id', fn);
           yti.setAttribute('name', fn);           
           yti.setAttribute('type', 'hidden');
           form.appendChild(yti);   
        }
        return yti;
    },  
    
    execute: function(_id) {
       var ele = document.getElementById(_id+'call');
       logger.info('execute ele '+ ele);
       logger.info('execute ele '+ ele.innerHTML);
       eval(ele.innerHTML);
    } 
};

