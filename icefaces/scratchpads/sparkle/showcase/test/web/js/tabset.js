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
             var form=formOf(document.getElementById(clientId)); 
             
             //create a utility form
             if (!form) { 
                 form = Ice.component.getUtilForm();
             }             
             
             //get hidden hidden field which keeps tab index
             var hdn = thiz.getHdnFld(form); 
                       
             if (hdn) { 
                  hdn.value = clientId + '='+ tabview.getTabIndex(event.newValue);
                  var isClientSide = Ice.component.getProperty(clientId, 'isClientSide')['new'];
                                  
                  if (isClientSide){
                     logger.info('Client side tab ');
                  } else {
                  logger.info('Server side tab ');
                      var partialSubmit = Ice.component.getProperty(clientId, 'partialSubmit');
                      
                      var options = {execute: '@all', render: '@all'};
                              
                       logger.info('partialSubmit '+ hdn +  event + options );
          
                      var param = function(parameter) {
				 parameter(hdn.id, hdn.value);
				 if (partialSubmit) {
					parameter('ice.submit.partial', true);
				 }
		                }                  
   		      jsf.ajax.request(hdn, event, options);            
                  }                  
             }             
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

