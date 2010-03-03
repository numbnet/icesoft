Ice.component.tabset = {
    hover:false,
    initialize:function(clientId) {
       //logger.info('1. tabset initialize');
       var tabview = new YAHOO.widget.TabView(clientId);  
       var o = Ice.component.getProperty(clientId, 'orientation');
       if(o) {
          //logger.info('ori  new -------'+  o['new'] + ' : pre '+  o['previous'] + ' : source '+ o['modifiedByClient']);
          tabview.set('orientation', o['new']);
       }
       var thiz = this;
       //logger.info('2. tabset initialize');
        if (!Ice.component.registeredComponents[clientId]) {
         //logger.info('2.1 tabset initialize');
            var onupdate = Ice.component.getProperty(clientId, 'onupdate');
            if (onupdate["new"] != null) {
          	    //logger.info('2.2 tabset initialize');	
                ice.onAfterUpdate(function() {
                	tabview = Ice.component.getInstance(clientId, Ice.component.tabset);
                    onupdate["new"](clientId, tabview );
                });
               //logger.info('2.3 tabset initialize');     
            }       
        }
       
       //add hover effect 
       var hover = Ice.component.tabset.hover; //temp var should be accessed from the component
       if (hover) {
           hoverEffect = function(event, attributes) {  
               target = Ice.eventTarget(event);
               target = target.parentNode;
               if ("selected" == target.parentNode.className) return;         
               anim = new YAHOO.util.ColorAnim(target, attributes); 
               anim.animate(); 
           };       
           var tabs = tabview.get('tabs');
           //logger.info('tabs.length  '+ tabs.length);       
           for (i=0; i<tabs.length; i++) {
              tab = tabs[i].get('labelEl');
              tabs[i].get('labelEl').onmouseover = function(event) {
                  var attributes = { 
                       backgroundColor: { from: '#DEDBDE', to: '#9F9F9F' } 
                  }; 
                  hoverEffect(event, attributes);        
              }
              tabs[i].get('labelEl').onmouseout = function(event) {
                  var attributes = { 
                     backgroundColor: { from: '#9F9F9F', to: '#DEDBDE'  } 
                  }; 
                  hoverEffect(event, attributes);         
              }          
           }
       }
       //hover ends
               
       //logger.info('3. tabset initialize');
       var tabChange=function(event) {
            //logger.info('on tab change ');
            var o = Ice.component.getProperty(clientId, 'orientation');
            event.target = document.getElementById(clientId);
            tbset = document.getElementById(clientId);
            currentIndex = tabview.getTabIndex(event.newValue);
            tabIndexInfo = clientId + '='+ currentIndex;
            var isClientSide = Ice.component.getProperty(clientId, 'isClientSide');
            var partialSubmit = Ice.component.getProperty(clientId, 'partialSubmit');
            var params = function(parameter) {
                            parameter('yti', tabIndexInfo);
                            parameter('onevent', function(data) { 
                                if (data.status == 'success') {
                                        var i = Ice.component.getProperty(clientId, 'tabIdx');
                                        var si = i['new'];
                                        if (currentIndex != si){//Validation failed so reset the tab index with the server tabindex
                                            tabview.removeListener('activeTabChange'); 
                                            tabview.set('activeIndex', si);
                                            tabview.addListener('activeTabChange', tabChange);                                          
                                        }                                    
                                }
                            });
                        };
            if (isClientSide && isClientSide['new']){
                //logger.info('Client side tab ');
            } else {
                //logger.info('Server side tab '+ event);
                try {
                    if (partialSubmit && partialSubmit["new"]) {
	                    ice.singleSubmit(event, tbset, params); 
                    } else {
                        ice.submit(event, tbset, params);                    
                    }
                } catch(e) {
                    logger.info(e);
                } 	             
			}//end if    
	   }//tabchange; 
	   tabview.addListener('activeTabChange', tabChange);       
	   //logger.info('4. tabset initialize');       
	   return tabview;
   },
   
   updateProperties:function(_id, props) {
         //logger.info('Updateing properties orientation ');
	     //update properties first
	     var tabview = Ice.component.updateProperties(_id, props, this);
	     //logger.info('Updateing tabview tabview ');
   },
   
    getHdnFld: function(form) {  //logger.info('getInxFld called: '+ form); 
        var fn = "yti";
        var yti;
        if (form) {
		yti = YAHOO.util.Dom.getElementBy(function(ele) {
		    if (ele.id == fn) return true;
		}, 'input', form);
		if(!yti) {
		        //logger.info('Form was fouond but filed not found adding to form');
			yti = this.createHiddenField(fn);
			form.appendChild(yti);
		}
        } else {//now add to body
            yti = document.getElementById(fn);
			if(!yti) {
			    //logger.info('Form was not fouond and filed not found adding to body');
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
       //logger.info('execute ele '+ ele);
       //logger.info('execute ele '+ ele.innerHTML);
       eval(ele.innerHTML);
    }   
};

