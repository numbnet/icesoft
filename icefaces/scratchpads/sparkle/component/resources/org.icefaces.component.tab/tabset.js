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
             try {
             	var form=formOf(document.getElementById(clientId)); 
             } catch(e) {
             	logger.info(e);
             }
             //get hidden hidden field which keeps tab index
             var hdn = thiz.getHdnFld(form); 
             logger.info('hdn found '+ hdn );       
             if (hdn) { 
                  event.target = document.getElementById(clientId);
                  tbset = document.getElementById(clientId);
                  hdn.value = clientId + '='+ tabview.getTabIndex(event.newValue);
                  var isClientSide = Ice.component.getProperty(clientId, 'isClientSide')['new'];
               
                  if (isClientSide){
                     logger.info('Client side tab ');
                  } else {
 	             logger.info('Server side tab '+ event);
		     try {
 		     	ice.singleSubmit(event, tbset, function(parameter) {
 		     	   parameter('yti', hdn.value);
 		     	}); 
		     } catch(e) {
			logger.info(e);
		     } 	             

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
        var yti;
        if (form) {
		yti = YAHOO.util.Dom.getElementBy(function(ele) {
		    if (ele.id == fn) return true;
		}, 'input', form);
		if(!yti) {
		        logger.info('Form was fouond but filed not found adding to form');
			yti = this.createHiddenField(fn);
			form.appendChild(yti);
		}
        } else {//now add to body
                yti = document.getElementById(fn);
		if(!yti) {
		        logger.info('Form was not fouond and filed not found adding to body');
			yti = this.createHiddenField(fn);
			document.body.appendChild(yti);
		}        
        }
        return yti;
    },  
    
    createHiddenField:function(name) {
	   yti = document.createElement('input');
	   yti.setAttribute('id',  name);
	   yti.setAttribute('name', name);           
	   yti.setAttribute('type', 'hidden');
	   return yti;
    },
    
    execute: function(_id) {
       var ele = document.getElementById(_id+'call');
       logger.info('execute ele '+ ele);
       logger.info('execute ele '+ ele.innerHTML);
       eval(ele.innerHTML);
    }
};

