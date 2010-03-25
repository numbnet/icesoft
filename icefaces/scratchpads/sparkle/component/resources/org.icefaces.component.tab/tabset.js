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
            var singleSubmit = Ice.component.getProperty(clientId, 'singleSubmit');
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
                    if (singleSubmit && singleSubmit["new"]) {
	                    ice.singleSubmit(event, tbset, params); 
                    } else {
                        ice.submit(event, tbset, params);                    
                    }
                } catch(e) {
                    logger.info(e);
                } 	             
			}//end if    
	   }//tabchange; 
	   
       //Check for aria support
       var aria = Ice.component.getProperty(clientId, 'aria');
       var onKeyDown = null;
       var Event = YAHOO.util.Event;
       //add aria + keyboard support
       if (aria && aria["new"]) {
           var Dom = YAHOO.util.Dom;
           var goNext = function(target) {
               var nextLi = Dom.getNextSibling(target);
               if (nextLi == null) {
                   goFirst(target);
               } else {
                   Dom.getFirstChild(nextLi).focus();
               }
           };
       
           var goPrevious= function(target) {
               var previousLi = Dom.getPreviousSibling(target);
               if (previousLi == null) {
                  goLast(target);
               } else {
                  Dom.getFirstChild(previousLi).focus();
               }
           };
           
           var goLast= function(target) {
               var lastLi = Dom.getLastChild(target.parentNode);  
               Dom.getFirstChild(lastLi).focus(); 
           };
           
           var goFirst= function(target) {
               var firstLi = Dom.getFirstChild(target.parentNode);
               Dom.getFirstChild(firstLi).focus();                             
           };
           	       
           onKeyDown = function(event) {
                var target = Event.getTarget(event).parentNode;
                var charCode = Event.getCharCode(event);
                switch (charCode) {
                   case 37://Left
                   case 38://Up
                     goPrevious(target);
                     break;
                     
                   case 39://Right
                   case 40://Down
                     goNext(target);
                     break;                     
                    
                   case 36: //HOME
                     goFirst(target);
                     break;                   
                     
                   case 35: //End  
                     goLast(target);
                     break;    
                }
           }
       }
       var onKeyPress = function(event, index) {
            var target = Event.getTarget(event).parentNode;
            var isEnter = Event.getCharCode(event) == 13;
            if (isEnter) {
               tabview.set('activeIndex', index);
            }
       }
       
       var tabs = tabview.get('tabs');
       for (i=0; i<tabs.length; i++) {
           if (onKeyDown){//do it for aria only
              tabs[i].on('keydown', onKeyDown);
           }
           //support enter key regardless of keyboard or aria support 
           tabs[i].on('keypress', onKeyPress, i); 
       }
       tabview.addListener('activeTabChange', tabChange);       
       return tabview;
   },
   
   updateProperties:function(_id, props) {
	     var tabview = Ice.component.updateProperties(_id, props, this);
   },
    
    execute: function(_id) {
       var ele = document.getElementById(_id+'call');
       eval(ele.innerHTML);
    }   
};

