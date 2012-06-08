/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
  (function() {
    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables
    //---------------------------------------
    function updateHidden(clientId, value) {
        var hidden = document.getElementById(clientId+"_hidden");
        if (hidden) {
            hidden.value = value;
        }
    }
     /* taken from accordion with slight modifications */
    function calcMaxChildHeight(containerEl){
        var mxht = 0;
        //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
        var children = containerEl.getElementsByTagName('div');
        for (var i= 0; i < children.length; i++){
           if ( children[0].scrollHeight > mxht) {
               mxht= children[0].scrollHeight;
           }
        }
        return mxht;
    }
    function hasClass(ele,cls) {
	       return ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
    }
    function addClass(ele,cls) {
            if (hasClass(ele,cls)) ele.className =cls;
    }
    function removeClass(ele,cls) {
         if (hasClass(ele,cls)) {
            // var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');  don't need if we don't allow to skin?
             ele.className=" ";
         }
    }
    //declare functions who creates object with methods that have access to the local variables of the function
    //so in effect the returned object can operate on the local state declared in the function ...
    //think about them as object fields in Java, also gone is the chore of copying the constructor parameters into fields
    //-------------------------------------
    function TabSet(clientId, cfgIn) {
      // setup tabContainer
        var myTabId = clientId;
        var tabContainer = document.getElementById(clientId);
        var tabContent = document.getElementById(clientId+"_tabContent");
        var cfg = cfgIn;
        var tabIndex = cfgIn.tIndex;
        if (cfgIn.height){
            tabContent.style.height = cfgIn.height;
        } else {
             var ht =  calcMaxChildHeight(tabContent);
             tabContent.style.height =  ht+"px";
        }
        var origcontents = tabContent.children;
        var newPage = origcontents[tabIndex];
        newPage.className="mobi-tabpage";

        return {
           showContent: function( el, cfgIn) {
              if (cfgIn.tIndex == tabIndex){
                  return;
              }
              var parent = el.parentNode;
              if (!parent){
                  parent = el.parentElement;
              }
      //        var current = parent.getAttribute("data-current");
              tabContent = document.getElementById(clientId+"_tabContent");
              var current = tabIndex;
              var contents = tabContent.children;
              var oldPage = contents[current];
              oldPage.className = "mobi-tabpage-hidden";
              var currCtrl = myTabId+"tab_"+current;
              var oldCtrl =  document.getElementById(currCtrl);
              removeClass(oldCtrl, "activeTab ");
              var isClient = cfgIn.client || false;
              if (!isClient){
                    updateHidden(myTabId, tabIndex+"," +cfgIn.tIndex);
                    contents[cfgIn.tIndex].className="mobi-tabpage-hidden";
                    ice.se(null, myTabId);
               } else {
                  tabIndex = cfgIn.tIndex || 0;
                  var newPage = contents[tabIndex];
                  newPage.className="mobi-tabpage";
             }
              parent.setAttribute("data-current",tabIndex);
              //remove class of activetabheader and hide old contents
              el.setAttribute("class","activeTab ");
           },
           updateProperties: function (clientId, cfgUpd) {
               var oldIdx = tabIndex;
               //check to see if pages have been added or removed
               tabContent = document.getElementById(clientId+"_tabContent");
               var contents = tabContent.children;
               tabIndex = cfgUpd.tIndex;
               var tabsId = clientId+"_tabs";
               var dataCurrent = document.getElementById(clientId+"_dc");
               if (dataCurrent){
                       dataCurrent.value=tabIndex+'';
               }
               var tabElem = document.getElementById(tabsId);
               if (tabElem){
                  var lis = tabElem.getElementsByTagName("li");
                  if (oldIdx != tabIndex){
                     contents[oldIdx].className = "mobi-tabpage-hidden"; //need in case change is from server
                  }
                  contents[tabIndex].className = "mobi-tabpage";
                  if (cfgUpd.height){
                     var height = cfgUpd.height;
                     if (height != tabContent.style.height){
                            tabContainer.style.height = height;
                     }
                  }
               }
           },
           tabIndex: function() {
               return tabIndex;
           }
       }
    }
    mobi.tabsetController = {
        panels: {},
        initClient: function(clientId, cfg) {
            if (!this.panels[clientId]){
                this.panels[clientId] = TabSet(clientId, cfg);
            } else {
                this.panels[clientId].updateProperties(clientId, cfg);
            }
        },
        showContent: function(clientId, el, cfgIn){
            if (this.panels[clientId]){
                this.panels[clientId].showContent(el, cfgIn);
            }
        }
    }

  })();