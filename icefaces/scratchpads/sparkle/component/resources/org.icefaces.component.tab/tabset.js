ice.component.tabset = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
       //logger.info('1. tabset initialize');
       var Dom = YAHOO.util.Dom;
       var tabview = new YAHOO.widget.TabView(clientId);  
       tabview.set('orientation', jsProps.orientation);
       var thiz = this;
       /*
        if (!Ice.component.registeredComponents[clientId]) {
            var onupdate = Ice.component.getProperty(clientId, 'onupdate');
            if (onupdate["new"] != null) {
                ice.onAfterUpdate(function() {
                    tabview = Ice.component.getInstance(clientId, Ice.component.tabset);
                    onupdate["new"](clientId, tabview );
                });
            }       
        }
        */
       
       //add hover effect 
       if (jsfProps.hover) {
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
            event.target = document.getElementById(clientId);
            tbset = document.getElementById(clientId);
            currentIndex = tabview.getTabIndex(event.newValue);
            tabIndexInfo = clientId + '='+ currentIndex;
            var params = function(parameter) {
                            parameter('yti', tabIndexInfo);
                            parameter('onevent', function(data) { 
                                if (data.status == 'success') {
                                      /*
                                        var i = Ice.component.getProperty(clientId, 'tabIdx');
                                        var si = i['new'];
                                        if (currentIndex != si){//Validation failed so reset the tab index with the server tabindex
                                            tabview.removeListener('activeTabChange'); 
                                            tabview.set('activeIndex', si);
                                            tabview.addListener('activeTabChange', tabChange);                                          
                                        }
                                        var LIs = Dom.getFirstChild(document.getElementById(clientId)).children;

                                        //set the focus back to the selected tab
                                        if (LIs.length > si) {
                                            Dom.getFirstChild(LIs[si]).focus();
                                        }        
                                        */                                 
                                }
                            });
                        };
            if (jsfProps.isClientSide){
                //TODO what should go here?
                //logger.info('Client side tab ');
            } else {
                //logger.info('Server side tab '+ event);
                try {
                    if (jsfProps.isSingleSubmit) {
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

       var onKeyDown = null;
       var Event = YAHOO.util.Event;
       //add aria + keyboard support
       if (jsfProps.aria) {
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
       bindYUI(tabview);
   },
   
   //delegate call to ice.yui.updateProperties(..)  with the reference of this lib
   updateProperties:function(clientId, jsProps, jsfProps, events) {
       ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   }   
};

