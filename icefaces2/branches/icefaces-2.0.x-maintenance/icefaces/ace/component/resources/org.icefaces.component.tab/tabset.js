/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

ice.component.tabset = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
       //logger.info('1. tabset initialize');
	 
	 ice.yui3.use(function(Y){ 
     Y.use('yui2-tabview', function(Yui/*, result*/) {
         /*
         if (!result.success) {
             alert("Load failure: " + result.msg);
         }
         */
	 Y.on('domready', function(){
	     var YAHOO = Y.YUI2;//TODO Yui.YUI2 ?
		 var Dom = YAHOO.util.Dom;

       var tabview = new YAHOO.widget.TabView(clientId);  
       tabview.set('orientation', jsProps.orientation);
       var thiz = this;
       
       //if tabset is client side, lets find out if the state is already stored.
       if (jsfProps.isClientSide) {
    	   if(ice.component.clientState.has(clientId)){
    		   tabview.set('activeIndex', ice.component.clientState.get(clientId));      
    	   }
    	   else {
    		   tabview.set('activeIndex', jsfProps.selectedIndex);      
    	   }
       }
       else {
           //alert("server side init");
           if(!ice.component.clientState.has(clientId)) {
               //alert("server side init - no context");
               tabview.set('activeIndex', jsfProps.selectedIndex);
               /*
                       tabview.removeListener('activeTabChange');
                       tabview.selectTab(currentIndex);
                       tabview.addListener('activeTabChange', tabChange);
               */
           }
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
               target = ice.component_util.eventTarget(event);
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
            var rootElem = document.getElementById(clientId);
            if ('suppressTabChange' in rootElem) {
                return;
            }
            var context = ice.component.getJSContext(clientId);
            var tabview = context.getComponent();
            var sJSFProps = context.getJSFProps();
            event.target = rootElem;
            tbset = document.getElementById(clientId);
            currentIndex = tabview.getTabIndex(event.newValue);
            //YAHOO.log(" currentIndex="+currentIndex);
            tabIndexInfo = clientId + '='+ currentIndex;
            var params = function(parameter) {
							parameter('ice.focus', event.newValue.get('element').firstChild.id);
                            parameter('onevent', function(data) {
                                var context = ice.component.getJSContext(clientId);
                                var tabview = context.getComponent();
                                if (data.status == 'success') {
                                    //TODO
                                   try {
									document.getElementById(event.newValue.get('element').firstChild.id).focus();		
                                   } catch(e) {}    
                                }
                            });
                        };
            if (sJSFProps.isClientSide){
            	//YAHOO.log(" clientSide and currentIndex="+currentIndex);
            	ice.component.clientState.set(clientId, currentIndex);
                //console.info('Client side tab ');
            } else {
                var targetElement = ice.component.tabset.getTabIndexField(tbset);
                if(targetElement) {
                	targetElement.value = tabIndexInfo;
                }            	
                //logger.info('Server side tab '+ event);
                try {
                    if (sJSFProps.isSingleSubmit) {
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
           };
       }
       var onKeyPress = function(event, index) {
            var context = ice.component.getJSContext(clientId);
            var tabview = context.getComponent();
            var target = Event.getTarget(event).parentNode;
			if(ice.component_util.isEventSourceInputElement(event)) {
				return true ;
			}
			//check for enter or space key
            var isEnter = Event.getCharCode(event) == 13 || 
					Event.getCharCode(event) == 32 ; 
            if (isEnter) {
               tabview.set('activeIndex', index);
			   event.cancelBubble = true;
            }
       };
       
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
               var context = ice.component.getJSContext(clientId);
               var tabview = context.getComponent();
               var currentIndex = tabview.getTabIndex(newTab);

					var callback = function(_effect) {
					    //console.info('_EFFEFEFEF '+ _effect);

                        var context = ice.component.getJSContext(clientId);
                        var sJSFProps = context.getJSFProps();
                        var tbset = document.getElementById(clientId);
					    //console.info('3. onend server side tab ');
						oldTab.set('contentVisible', false);
						YAHOO.util.Dom.setStyle(newTab.get('contentEl').id, 'opacity', 0);
						newTab.set('contentVisible', true);
						//console.info('3.a onend server side tab ');
						 
						if (sJSFProps.isClientSide){
							
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

										var ele = document.getElementById(newTab.get('element').id).firstChild;
										parameter('ice.focus',  ele.id);
										parameter('onevent', function(data) { 
											if (data.status == 'success') {//console.info('Sucesssssss');
								   // YAHOO.util.Dom.setStyle(newTab.get('contentEl').id, 'opacity', 0);
									newTab.set('contentVisible', true);
									animation.chain.set('node', '#'+  newTab.get('contentEl').id);
                                        //set the focus back to the selected tab
                                       // var selectedTab = tabview.getTab(currentIndex);
                                                var ele = document.getElementById(newTab.get('element').id).firstChild;
                                                ele.focus();
 										//ele.parentNode.focus();
                                                                  									
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
												if (sJSFProps.isSingleSubmit) {
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

	 }); // *** end of domready
	 }); // *** end of Y.use
	 }); // *** end of ice.yui3.use
   },
   
   //this function is responsible to provide an element that keeps tab index
   //only one field will be used per form element.
   getTabIndexField:function(tabset) {
	   //YAHOO.log("in getTabIndexField");
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
			   f = ice.component.tabset.createHiddenField(_form, _form.id + 'yti');
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
       var lib = this;
       ice.yui3.use(function(Y){
       Y.use('yui2-tabview', function(Yui) {
       Y.on('domready', function(){

       // Call handlePotentialTabChanges if we're NOT going to initialise
       var oldJSFProps = null;
       var context = ice.component.getJSContext(clientId);
       if (context) {
           oldJSFProps = context.getJSFProps();
       }
       var requiresInitialise = ice.component.tabset.handlePotentialTabChanges(
               Y, clientId, oldJSFProps, jsfProps);
       // If the tab info changed sufficiently to require an initialise
       if (context) {
           if (requiresInitialise) {
               // component.destroy() will remove all the markup fro the page
               //var component = context.getComponent();
               //if (component) {
               //    component.destroy();
               //}
               var rootToReInit = document.getElementById(clientId);
               if (rootToReInit) {
                   rootToReInit['JSContext'] = null;
                   rootToReInit.removeAttribute('JSContext');
               }
               //document.getElementById(clientId)['JSContext'] = null;
               JSContext[clientId] = null;
           }
           else {
               var tabviewObj = context.getComponent();
               var lastKnownIndex = context.getJSFProps().selectedIndex;
               var index = jsfProps.selectedIndex;
               var objIndex = tabviewObj.get('activeIndex');

               if (index != objIndex) {
                   var rootElem = document.getElementById(clientId);
                   rootElem.setAttribute('suppressTabChange', true);
                   //tabviewObj.removeListener('activeTabChange');
                   if (!jsfProps.isClientSide){
                       tabviewObj.set('activeIndex', index);
                   } else {
                       tabviewObj.selectTab(index);
                   }
                   rootElem.removeAttribute('suppressTabChange');
                   //tabviewObj.addListener('activeTabChange', tabChange);
               }
           }
       }
       ice.yui3.updateProperties(clientId, jsProps, jsfProps, events, lib);
       });
       });
       });
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.component.getInstance(clientId, callback, this);
   },

    // Used by updateProperties(-) when we're already initialised
    // Updates the dom, re-parents the content, and triggers a new initialise
    handlePotentialTabChanges : function(Y, clientId, oldJSFProps, newJSFProps) {
        var oldSafeIds = ( (!oldJSFProps) ? null : oldJSFProps.safeIds );
        var newSafeIds = ( (!newJSFProps) ? null : newJSFProps.safeIds );
        if (!oldSafeIds) {
            oldSafeIds = new Array();
        }
        if (!newSafeIds) {
            newSafeIds = new Array();
        }

        var ret = false;

        if (Ice.arraysEqual(oldSafeIds, newSafeIds)) {
            // We can have a scenario where the [client-side] tabSet is
            // completely updated by the dom-diff, and nothing has changed
            // with the tabs, but now the tab content is stored in the safe,
            // but the old safeIds list is not null, and is exactly equal to
            // the new safeIds list, so we don't know to re-parent the content
            // into the content area. So we'll need to scan through the new
            // safeIds list and see if the content is there, and handle it.
            var contentDiv = document.getElementById(clientId + 'cnt');
            if (contentDiv && !contentDiv.hasChildNodes()) {
                var index;
                for (index = 0; index < newSafeIds.length; index++) {
                    var safeDiv = document.getElementById(newSafeIds[index]);
                    if (safeDiv && safeDiv.hasChildNodes()) {
                        var appendedDiv = document.createElement('div');
                        contentDiv.appendChild(appendedDiv);

                        // Reparent new safe-house entry into content area
                        ice.component.tabset.moveSafeToContent(safeDiv, appendedDiv);

                        ret = true;
                    }
                }
            }

            return ret;
        }

        var appendNewContent = new Array();
        // [ [oldContent, newIndex where it should go or -1 for delete], É ]
        var moveOldContent = new Array();
        var skipNewIndexes = new Array();
        var oldSafeIndex = 0;
        var newSafeIndex = 0;

        //var contentNode = Y.one('#' + clientId + 'cnt');//' .yui-content'); // Y.DOM.byId(clientId + 'cnt');
        var contentDiv = document.getElementById(clientId + 'cnt');//Y.Node.getDOMNode(contentNode);

        while (true) {
            // (0) Detect if done
            if (oldSafeIndex >= oldSafeIds.length && newSafeIndex >= newSafeIds.length) {
                break;
            }

            // (3.5 skip) Skip past newSafeIndex if in skip list
            if (Ice.arrayIndexOf(skipNewIndexes, newSafeIndex, 0) >= 0) {
                newSafeIndex++;
                continue;
            }

            // (1) Detect if tab on end was deleted
            // If past end of new list but more in old list
            if (newSafeIndex >= newSafeIds.length && oldSafeIndex < oldSafeIds.length) {
                // Everything left in old list has been deleted. Just delete one, then loop to remove any more
                moveOldContent.push( [oldSafeIndex, -1] );
                oldSafeIndex++;
                ret = true;
                continue;
            }

            // (2) Detect if appended to end
            // If past end of old list, but at least another in new list
            if (oldSafeIndex >= oldSafeIds.length && newSafeIndex < newSafeIds.length) {
                // Current entry in new is appended
                // Create new div and append it to content area.
                var appendedDiv = document.createElement('div');
                contentDiv.appendChild(appendedDiv);

                // Reparent new safe-house entry into content area
                ice.component.tabset.moveSafeIdToContent(newSafeIds[newSafeIndex], appendedDiv);

                // Increment newSafeIndex, but not oldSafeIndex, and continue looping
                newSafeIndex++;
                ret = true;
                continue;
            }

            // (3) Detect if non-end deleted, inserted, visited, moved
            var oldsid = oldSafeIds[oldSafeIndex];
            var newsid = newSafeIds[newSafeIndex];
            if (oldsid !== newsid) {
                // (3.5) Detect if old moved. Also covers unvisited inserts,
                // which inadvertently move the pre-existing sections, which
                // we'll try to avoid, since moving a section involves
                // refreshing iframe content, which we need to avoid
                // oldsid not null and in new list
                // ?? Search from newSafeIndex onwards or beginning?? Just use beginning
                var foundInNewIndex;
                if (oldsid !== null &&
                    (foundInNewIndex = Ice.arrayIndexOf(newSafeIds, oldsid, 0)) >= 0)
                {
                    // Detect if newsid is unvisited/visiting insert
                    var foundInOldIndex;
                    if (newsid === null ||
                        ( ((foundInOldIndex = Ice.arrayIndexOf(oldSafeIds, newsid, 0)) < 0) &&
                          document.getElementById(newsid).hasChildNodes()
                        )) {
                        var newDiv = document.createElement('div');
                        var newIndex = contentDiv.childNodes.length;
                        contentDiv.appendChild(newDiv);

                        // Reparent new safe-house entry into content area
                        // if newsid is not null
                        ice.component.tabset.moveSafeIdToContent(newsid, newDiv);

                        // Mark the new content div to be moved to it's proper insertion point
                        appendNewContent.push( [newIndex, newSafeIndex] );

                        // Increment newSafeIndex, but not oldSafeIndex, and continue looping
                        newSafeIndex++;
                        ret = true;
                        continue;
                    }

                    // Mark the location in new list as something to skip over
                    skipNewIndexes.push(foundInNewIndex);

                    // Save the reference to the old content, and where it should end up
                    moveOldContent.push( [oldSafeIndex, foundInNewIndex] );

                    // Increment oldSafeIndex, but not newSafeIndex, and continue looping
                    oldSafeIndex++;
                    ret = true;
                    continue;
                }

                // Have to detect which of non-end delete, insert, or visit

                // (4) Detect if non-end deleted
                // If newsid is null, it's a delete (oldsid is non-null since oldsid !== newsid)
                if (newsid === null) {
                    moveOldContent.push( [oldSafeIndex, -1] );
                    oldSafeIndex++;
                    ret = true;
                    continue;
                }

                // (5) Detect if non-end deleted or instead insert/visit
                // If new safe entry has no child markup, then is non-end delete
                var safeDiv = document.getElementById(newsid);
                if (!safeDiv.hasChildNodes()) {
                    moveOldContent.push( [oldSafeIndex, -1] );
                    oldSafeIndex++;
                    ret = true;
                    continue;
                }
                // If new safe entry has child markup, then is either insert or visit
                else {// safeDiv.hasChildNodes()
                    // If changed from null to non-null means visited
                    if (oldsid === null && newsid !== null) {
                        // Get the content area
                        var visitedDiv = contentDiv.childNodes[oldSafeIndex];

                        // Reparent new safe-house entry into content area
                        ice.component.tabset.moveSafeToContent(safeDiv, visitedDiv);

                        oldSafeIndex++;
                        newSafeIndex++;
                        continue;
                    }

                    // Inserted
                    // We don't want to alter the content area indexing as we
                    // go, so inserting right away is a no-go. We also don't
                    // want to special case this. So, we'll treat this as an
                    // append, where we'll subsequently move it to the
                    // insertion point
                    // Create new div and append it to content area.
                    var newDiv = document.createElement('div');
                    var newIndex = contentDiv.childNodes.length;
                    contentDiv.appendChild(newDiv);

                    // Reparent new safe-house entry into content area
                    ice.component.tabset.moveSafeToContent(safeDiv, newDiv);

                    // Mark the new content div to be moved to it's proper insertion point
                    appendNewContent.push( [newIndex, newSafeIndex] );

                    // Increment newSafeIndex, but not oldSafeIndex, and continue looping
                    newSafeIndex++;
                    ret = true;
                    continue;
                }
            }
            // If they're the same, move on to next. Increment both
            else { // oldsid === newsid
                oldSafeIndex++;
                newSafeIndex++;
                continue;
            }
        }

        // moveOldContent assumes that as its index increases, the old content
        // indexes increase as well. But when we append, we're putting larger
        // old content indexes early towards the beginning of moveOldContent.
        // So we'll buffer them in appendNewContent, and then use that to
        // place them toward the end of moveOldContent.
        var index;
        for (index = 0; index < appendNewContent.length; index++) {
            moveOldContent.push(appendNewContent[index]);
        }

        // moveOldContent, but our algorithm assumes that the the old indexes increase
        // Iterate through moveOldContent in reverse, removing the divs from
        // the content area, and setting the div into moveOldContent where
        // the index had been
        for (index = moveOldContent.length - 1; index >= 0; index--) {
            var removeIndex = moveOldContent[index][0];
            var removeDiv = contentDiv.childNodes[removeIndex];
            moveOldContent[index][0] = removeDiv;
            contentDiv.removeChild(removeDiv);
        }
        // Then iterate through that list in the original forward sequence,
        // inserting the divs into their designated positions, unless the
        // insert index is -1, in which case discard them.
        for (index = 0; index < moveOldContent.length; index++) {
            var fromTo = moveOldContent[index];
            var insertDiv = fromTo[0];
            var toIndex = fromTo[1];
            if (toIndex >= 0) {
                Ice.insertElementAtIndex(contentDiv, insertDiv, toIndex);
            }
        }

        return ret;
    },

    moveSafeIdToContent : function(safeId, tabContentDiv) {
        if (safeId) {
            var safeDiv = document.getElementById(safeId);
            ice.component.tabset.moveSafeToContent(safeDiv, tabContentDiv);
        }
    },

    moveSafeToContent : function(safeDiv, tabContentDiv) {
        if (safeDiv.hasChildNodes()) {
            // Clean out tabContentDiv, and put the div child of safeDiv into tabContentDiv
            while (tabContentDiv.hasChildNodes()) {
                tabContentDiv.removeChild(tabContentDiv.firstChild);
            }
            var contentsToMove = safeDiv.firstChild; //.getElementByTagName("div")[0];
            safeDiv.removeChild(contentsToMove);
            tabContentDiv.appendChild(contentsToMove);
        }
    }
};

