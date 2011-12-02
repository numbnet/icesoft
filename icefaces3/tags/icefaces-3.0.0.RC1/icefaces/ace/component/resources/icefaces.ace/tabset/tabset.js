/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

ice.ace.tabset = {
    initialize:function(clientId, jsProps, jsfProps, bindYUI) {
       //logger.info('1. tabset initialize');
	 
         /*
         if (!result.success) {
             alert("Load failure: " + result.msg);
         }
         */
		 YAHOO.util.Event.onDOMReady(function () {
		 var Dom = YAHOO.util.Dom;
	
       var tabview = new YAHOO.widget.TabView(clientId);  
       tabview.set('orientation', jsProps.orientation);

       //if tabset is client side, lets find out if the state is already stored.
       var initElem = document.getElementById(clientId);
       initElem.suppressTabChange = true;
       if (jsfProps.isClientSide) {
    	   if(ice.ace.clientState.has(clientId)){
    		   tabview.set('activeIndex', ice.ace.clientState.get(clientId));
    	   }
    	   else {
    		   tabview.set('activeIndex', jsfProps.selectedIndex);      
    	   }
       }
       else {
           //alert("server side init");
           if(!ice.ace.clientState.has(clientId)) {
               //alert("server side init - no context");
               tabview.set('activeIndex', jsfProps.selectedIndex);
               /*
                       tabview.removeListener('activeTabChange');
                       tabview.selectTab(currentIndex);
                       tabview.addListener('activeTabChange', tabChange);
               */
           }
       }
       initElem.suppressTabChange = null;
       
       /*
        if (!ice.ace.registeredComponents[clientId]) {
            var onupdate = ice.ace.getProperty(clientId, 'onupdate');
            if (onupdate["new"] != null) {
                ice.onAfterUpdate(function() {
                    tabview = ice.ace.getInstance(clientId, ice.ace.tabset);
                    onupdate["new"](clientId, tabview );
                });
            }       
        }
        */
       
       //logger.info('3. tabset initialize');
       var tabChange=function(event) {
            var rootElem = document.getElementById(clientId);
            if (rootElem.suppressTabChange) {
                return;
            }
            var context = ice.ace.getJSContext(clientId);
            var tabview = context.getComponent();
            var sJSFProps = context.getJSFProps();
            event.target = rootElem;
            var currentIndex = tabview.getTabIndex(event.newValue);
            if (currentIndex == null) {
                return;
            }
            //YAHOO.log(" currentIndex="+currentIndex);
            var tabIndexInfo = clientId + '='+ currentIndex;
            var params = function(parameter) {
							parameter('ice.focus', event.newValue.get('element').firstChild.id);
                            parameter('onevent', function(data) {
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
            	ice.ace.clientState.set(clientId, currentIndex);
                if (sJSFProps.behaviors) {
                    if (sJSFProps.behaviors.clientSideTabChange) {
                        sJSFProps.behaviors.clientSideTabChange();
                    }
                }
                //console.info('Client side tab ');
            } else {
                var targetElement = ice.ace.tabset.getTabIndexField(rootElem);
                if(targetElement) {
                	targetElement.value = tabIndexInfo;
                }            	
                //logger.info('Server side tab '+ event);
                try {
                    var haveBehaviour = false;
                    if (sJSFProps.behaviors) {
                        if (sJSFProps.behaviors.serverSideTabChange) {
                            haveBehaviour = true;

                            var elementId = targetElement.id;
                            //replace id with the id of tabset component, so the "execute" property can be set to tabset id
                            targetElement.id = clientId;
                            sJSFProps.behaviors.serverSideTabChange(params);
                            //restore id
                            targetElement.id = elementId;
                        }
                    }
                    if (!haveBehaviour) {
                        ice.submit(event, targetElement, params);
                    }
                    /*
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
                    */
                } catch(e) {
                    //logger.info(e);
                }                
            }//end if    
       };//tabchange;
       
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
            var context = ice.ace.getJSContext(clientId);
            var tabview = context.getComponent();
            var target = Event.getTarget(event).parentNode;
			if(ice.ace.util.isEventSourceInputElement(event)) {
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

	tabview.contentTransition = function(newTab, oldTab) {
		if (newTab) {
			newTab.set('contentVisible', true);
		}
		if (oldTab) {
			oldTab.set('contentVisible', false);
		}
		// effect
		if (jsfProps.isClientSide && jsProps.showEffect) {
			var content = newTab.get('contentEl').childNodes[0];
			if (jsProps.showEffect == 'fade') jQuery(content).hide();
			if (content) ice.ace.animation.run({node: content, name: jsProps.showEffect}, {mode: 'show'}, jsProps.showEffectLength);
		}
	}

    
	   //console.info('effect >>> '+ jsfProps.effect );
 
	   tabview.addListener('activeTabChange', tabChange);
       bindYUI(tabview);

	 }); // *** end of domready
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
					   //logger.info('ERROR: The tabSetProxy must be enclosed inside a Form element');
				   }
			   } else {
				   //logger.info('ERROR: If tabset is not inside a form, then you must use tabSetProxy component');
			   }
		   }
	   }
	   //form element has been resolved by now
	   if (_form) {
		   var f = document.getElementById(_form.id + 'yti');
		   //if tabindex holder is not exist already, then create it lazily.
		   if (!f) {
			   f = ice.ace.tabset.createHiddenField(_form, _form.id + 'yti');
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
	   YAHOO.util.Event.onDOMReady(function () {
           YAHOO.widget.Tab.prototype.ACTIVE_CLASSNAME = 'ui-state-active';
           YAHOO.widget.Tab.prototype.HIDDEN_CLASSNAME = 'ui-tabs-hide';
           YAHOO.widget.Tab.prototype.DISABLED_CLASSNAME = 'ui-state-disabled';

       // Call handlePotentialTabChanges if we're NOT going to initialise
       var oldJSFProps = null;
       var context = ice.ace.getJSContext(clientId);
       if (context) {
           oldJSFProps = context.getJSFProps();
       }
       var requiresInitialise = ice.ace.tabset.handlePotentialTabChanges(
               clientId, oldJSFProps, jsfProps);
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
                   rootElem.suppressTabChange = true;
                   //tabviewObj.removeListener('activeTabChange');
                   if (!jsfProps.isClientSide){
                       tabviewObj.set('activeIndex', index);
                   } else {
                       tabviewObj.selectTab(index);
                   }
                   rootElem.suppressTabChange = null;
                   //tabviewObj.addListener('activeTabChange', tabChange);
               }
               if (jsProps.showEffect) {
                   var node = tabviewObj.getTab(index).get('contentEl').childNodes[0];
                   if (jsProps.showEffect == 'fade') jQuery(node).hide();
                   if (node) ice.ace.animation.run({node: node, name: jsProps.showEffect}, {mode: 'show'}, jsProps.showEffectLength);
               }
           }
       }
       ice.ace.updateProperties(clientId, jsProps, jsfProps, events, lib);
       });
   },
 
   //delegate call to ice.yui.getInstance(..) with the reference of this lib 
   getInstance:function(clientId, callback) {
       ice.ace.getInstance(clientId, callback, this);
   },

    // Used by updateProperties(-) when we're already initialised
    // Updates the dom, re-parents the content, and triggers a new initialise
    handlePotentialTabChanges : function(clientId, oldJSFProps, newJSFProps) {
        var oldSafeIds = ( (!oldJSFProps) ? null : oldJSFProps.safeIds );
        var newSafeIds = ( (!newJSFProps) ? null : newJSFProps.safeIds );
        if (!oldSafeIds) {
            oldSafeIds = new Array();
        }
        if (!newSafeIds) {
            newSafeIds = new Array();
        }

        var ret = false;

        if (ice.ace.util.arraysEqual(oldSafeIds, newSafeIds)) {
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
                        var isSelectedTab = (newJSFProps.selectedIndex == index);
                        var appendedDiv = ice.ace.tabset.createDiv(!isSelectedTab);

                        // Reparent new safe-house entry into content area
                        ice.ace.tabset.moveSafeToContent(safeDiv, appendedDiv);
                        contentDiv.appendChild(appendedDiv);

                        ret = true;
                    }
                }
            }

            return ret;
        }

        var appendNewContent = new Array();
        // [ [oldContent, newIndex where it should go or -1 for delete], � ]
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
            if (ice.ace.util.arrayIndexOf(skipNewIndexes, newSafeIndex, 0) >= 0) {
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
                var isSelectedTab = (newJSFProps.selectedIndex == newSafeIndex);
                var appendedDiv = ice.ace.tabset.createDiv(!isSelectedTab);

                // Reparent new safe-house entry into content area
                ice.ace.tabset.moveSafeIdToContent(newSafeIds[newSafeIndex], appendedDiv);
                contentDiv.appendChild(appendedDiv);

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
                    (foundInNewIndex = ice.ace.util.arrayIndexOf(newSafeIds, oldsid, 0)) >= 0)
                {
                    // Detect if newsid is unvisited/visiting insert
                    var foundInOldIndex;
                    if (newsid === null ||
                        ( ((foundInOldIndex = ice.ace.util.arrayIndexOf(oldSafeIds, newsid, 0)) < 0) &&
                          document.getElementById(newsid).hasChildNodes()
                        )) {
                        var isSelectedTab = (newJSFProps.selectedIndex == newSafeIndex);
                        var newDiv = ice.ace.tabset.createDiv(!isSelectedTab);
                        var newIndex = contentDiv.childNodes.length;

                        // Reparent new safe-house entry into content area
                        // if newsid is not null
                        ice.ace.tabset.moveSafeIdToContent(newsid, newDiv);
                        contentDiv.appendChild(newDiv);

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

                // Unvisited tab. old goes to null and isn't in new list anymore
                if (oldsid !== null &&
                         newsid === null &&
                         (foundInNewIndex = ice.ace.util.arrayIndexOf(newSafeIds, oldsid, 0)) < 0) {
                    // Clear out / un-cache that tab's contents
                    var unvisitedDiv = contentDiv.childNodes[oldSafeIndex];
                    while (unvisitedDiv.hasChildNodes()) {
                        unvisitedDiv.removeChild(unvisitedDiv.firstChild);
                    }

                    oldSafeIndex++;
                    newSafeIndex++;
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
                        ice.ace.tabset.moveSafeToContent(safeDiv, visitedDiv);

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
                    var isSelectedTab = (newJSFProps.selectedIndex == newSafeIndex);
                    var newDiv = ice.ace.tabset.createDiv(!isSelectedTab);
                    var newIndex = contentDiv.childNodes.length;

                    // Reparent new safe-house entry into content area
                    ice.ace.tabset.moveSafeToContent(safeDiv, newDiv);
                    contentDiv.appendChild(newDiv);

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
                ice.ace.util.insertElementAtIndex(contentDiv, insertDiv, toIndex);
            }
        }

        return ret;
    },

    createDiv : function(preStyleHidden) {
        var theDiv = document.createElement('div');
        if (preStyleHidden) {
            ////YAHOO.util.Dom.addClass(theDiv, YAHOO.widget.Tab.prototype.HIDDEN_CLASSNAME);
            // Y.YUI2.util.Dom.hasClass(theDiv, 'yui-hidden');
            // Y.YUI2.util.Dom.removeClass(theDiv, 'yui-hidden');
        }
        return theDiv;
    },

    moveSafeIdToContent : function(safeId, tabContentDiv) {
        if (safeId) {
            var safeDiv = document.getElementById(safeId);
            ice.ace.tabset.moveSafeToContent(safeDiv, tabContentDiv);
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

