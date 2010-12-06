ice.component.tabset = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
       //logger.info('1. tabset initialize');
     var Dom = YAHOO.util.Dom;
     YAHOO.util.Event.onDOMReady(function() {    
       var tabview = new YAHOO.widget.TabView(clientId);  
       tabview.set('orientation', jsProps.orientation);
       var thiz = this;
       
       //if tabset is client side, lets find out if the state is already stored.
       if (jsfProps.isClientSide && ice.component.clientState.has(clientId)){
    	   tabview.set('activeIndex', ice.component.clientState.get(clientId));    	   
       }
       
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
                            parameter('onevent', function(data) { 
                                if (data.status == 'success') {
                                        var lastKnownSelectedIndex = ice.component.getJSContext(clientId).getJSFProps().selectedIndex;   
	                                    if (lastKnownSelectedIndex != currentIndex) {
	                                            tabview.removeListener('activeTabChange'); 
	                                            tabview.set('activeIndex', lastKnownSelectedIndex);
	                                            tabview.addListener('activeTabChange', tabChange); 
	                                            currentIndex = lastKnownSelectedIndex; 
	                                    }
                                   
                                   /*
                                       var LIs = Dom.getFirstChild(document.getElementById(clientId)).children;

                                        //set the focus back to the selected tab
                                        if (LIs.length > currentIndex) {
                                            Dom.getFirstChild(LIs[currentIndex]).focus();
                                        }        
                                     */                                    
                                }
                            });
                        };
            if (jsfProps.isClientSide){
            	ice.component.clientState.set(clientId, currentIndex);
                //console.info('Client side tab ');
            } else {
                var targetElement = ice.component.tabset.getTabIndexField(tbset);
                if(targetElement) {
                	targetElement.value = tabIndexInfo;
                }            	
                //logger.info('Server side tab '+ event);
                try {
                    if (jsfProps.isSingleSubmit) {
                    	//backup id
                    	var elementId = targetElement.id;
                    	//replace id with the id of tabset component, so the "execute" property can be set to tabset id
                    	targetElement.id = clientId;
                    	ice.se(event, targetElement, params);
                    	//restore id
                    	targetElement.id = elementId;
                    } else {
                        ice.submit(event, targetElement, params);                    
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
       
    
	   //console.info('effect >>> '+ jsfProps.effect );
 
	   var animation = ice.animation.getAnimation(clientId, "transition");
	   
	   if (animation) {
		   //console.info('effect found... length ='+ jsfProps.effect.length + 'value = '+ jsfProps.effect);
		//   var effect = eval(jsfProps.effect);
		   tabview.contentTransition = function(newTab, oldTab) {	//console.info('1. server side tab ');
		        var currentIndex = tabview.getTabIndex(newTab);

					var callback = function(_effect) {
					    //console.info('_EFFEFEFEF '+ _effect);
						
						var tbset = document.getElementById(clientId);
					    //console.info('3. onend server side tab ');
						oldTab.set('contentVisible', false);
						YAHOO.util.Dom.setStyle(newTab.get('contentEl').id, 'opacity', 0);
						newTab.set('contentVisible', true);
						//console.info('3.a onend server side tab ');
						 
						if (jsfProps.isClientSide){
							
							ice.component.clientState.set(clientId, currentIndex);
							var Effect = new ice.yui3.effects['Appear'](newTab.get('contentEl').id);
							Effect.setContainerId(clientId);
							Effect.run();	
							//console.info('Client side tab ');
							
							
						} else {

						        tabIndexInfo = clientId + '='+ currentIndex;

							    var targetElement = ice.component.tabset.getTabIndexField(tbset);

								if(targetElement) {
									targetElement.value = tabIndexInfo;
								}            	
							var event ={};
							            var params = function(parameter) {
										parameter('onevent', function(data) { 
											if (data.status == 'success') {//console.info('Sucesssssss');
								   // YAHOO.util.Dom.setStyle(newTab.get('contentEl').id, 'opacity', 0);
									newTab.set('contentVisible', true);
									animation.chain.set('node', '#'+  newTab.get('contentEl').id);
											// _effect.set('node', '#'+ newTab.get('contentEl').id);
										  //  	Appear = new ice.yui3.effects.Appear(newTab.get('contentEl').id);
											//	Appear.setContainerId(clientId);
											//	Appear.run();
												/*
													var lastKnownSelectedIndex = ice.component.getJSContext(clientId).getJSFProps().selectedIndex;   
																						   if (lastKnownSelectedIndex != currentIndex) {
															tabview.removeListener('activeTabChange'); 
															tabview.set('activeIndex', lastKnownSelectedIndex);
															tabview.addListener('activeTabChange', tabChange); 
															currentIndex = lastKnownSelectedIndex; 
													  }
												*/
											   /*
												   var LIs = Dom.getFirstChild(document.getElementById(clientId)).children;

													//set the focus back to the selected tab
													if (LIs.length > currentIndex) {
														Dom.getFirstChild(LIs[currentIndex]).focus();
													}        
												 */                                    
											}
                            });
                        };
								
											try {
												if (jsfProps.isSingleSubmit) {
													//backup id
													var elementId = targetElement.id;
													//replace id with the id of tabset component, so the "execute" property can be set to tabset id
													targetElement.id = clientId;
													ice.se(event, targetElement, params);
													//restore id
													targetElement.id = elementId;
												} else {
													ice.submit(event, targetElement, params);                    
												}
											} catch(e) {
												logger.info(e);
											} 
					
						}
					};

					//var Effect = new ice.yui3.effects[effect]({node: '#'+  oldTab.get('contentEl').id,  revert:true}, callback);
					//Effect.setContainerId(clientId);
					
					//console.info('2. server side tab '+ oldTab.get('contentEl').id);
					animation.setContainerId(clientId);
					animation.chain.set('node', '#'+  oldTab.get('contentEl').id);
					animation.chain.on('end', callback);
					//effect.set('node', '#'+  oldTab.get('contentEl').id);
					//effect.setContainerId(clientId);
				   // effect.revert = true;
					//effect.setPreRevert(callback);
					try {//console.info('run executed. ');
					//effect.run();
					//alert(animation.next());
					
					animation.chain.run(true);
		 
					} catch(e) {					//console.info('run executed. server side tab '+ e);
					}
					console.info('run executed. server side tab ');
		   }
	   } else { 
		   tabview.addListener('activeTabChange', tabChange);
	   }
       bindYUI(tabview);
     });
   },
   
   //this function is responsible to provide an element that keeps tab index
   //only one field will be used per form element.
   getTabIndexField:function(tabset) {
	   var _form = null;
	   try {
		   //see if the tabset is enclosed inside a form
	       _form = formOf(tabset);
	   } catch(e) {
		   //seems like tabset is not enclosed inside a form, now look for tabsetproxy component 
		   if (!_form) {
			   var tsc = document.getElementById(tabset.id + '_tsc');
			   if(tsc) {
				   try {
					   _form = formOf(tsc);
				   } catch(e) {
					   logger.info('ERROR: The tabSetProxy must be enclosed inside a Form element');
				   }
			   } else {
				   logger.info('ERROR: If tabset is not inside a form, then you must use tabSetProxy component');
			   }
		   }
	   }
	   //form element has been resolved by now
	   if (_form) {
		   var f = document.getElementById(_form.id + 'yti');
		   //if tabindex holder is not exist already, then create it lazily.
		   if (!f) {
			   f = this.createHiddenField(_form, _form.id + 'yti');
		   }
	       return f 
	   } else {
		   return null;   
	   }
   },
   
   createHiddenField:function(parent, id) {
	   var field = document.createElement('input'); 
	   field.setAttribute('type', 'hidden');
	   field.setAttribute('id', id);
	   field.setAttribute('name', 'yti');
	   parent.appendChild(field);
	   return field;
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

