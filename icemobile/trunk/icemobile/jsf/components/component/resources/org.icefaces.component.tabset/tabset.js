/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
        var hidden = document.getElementById(clientId + "_hidden");
        if (hidden) {
            hidden.value = value;
        }
    }

    /* taken from accordion with slight modifications */
    function calcMaxChildHeight(containerEl) {
        var mxht = 0;
        //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
        var children = containerEl.getElementsByTagName('div');
        for (var i = 0; i < children.length; i++) {
            if (children[i].scrollHeight > mxht) {
                mxht = children[i].scrollHeight;
            }
        }
        return mxht;
    }

    function hasClass(ele, cls) {
        return ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
    }

    function addClass(ele, cls) {
        if (hasClass(ele, cls)) {
            ele.className = cls;
        }
    }

    function removeClass(ele, cls) {
        if (hasClass(ele, cls)) {
            // var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');  don't need if we don't allow to skin?
            ele.className = " ";
        }
    }

    function setWidthStyle(root){
        var nodes = root.getElementsByTagName('ul');
        var ul = nodes[0];
        var children = ul.getElementsByTagName('li');
        var liLngth = children.length;
        var containerWidth = root.clientWidth;
        var width = Math.floor(containerWidth/liLngth);
        var rem = 100 % liLngth;
        for (var i = 0; i < liLngth; i++){
            children[i].style.width = width+"px";
        }
    }

    function setTabActive(id, cls) {
        var curTab = document.getElementById(id);
        if (curTab) {
            curTab.setAttribute("class", cls);
        }
    }

    //declare functions who creates object with methods that have access to the local variables of the function
    //so in effect the returned object can operate on the local state declared in the function ...
    //think about them as object fields in Java, also gone is the chore of copying the constructor parameters into fields
    //-------------------------------------
    function TabSet(clientId, cfgIn) {
        // setup tabContainer
        var tabContainer = document.getElementById(clientId);
        var tabContent = document.getElementById(clientId + "_tabContent");
        var classHid = "mobi-tabpage-hidden";
        var classVis = "mobi-tabpage";
        var clsActiveTab = "activeTab ui-btn-active";
        var tabCtrl = clientId + "tab_";
        var tabIndex = cfgIn.tIndex;
        var autoWidth = cfgIn.autoWidth;
        if (autoWidth){
            setWidthStyle(tabContainer);
        }
        var lastServerIndex = tabIndex;
        var height = cfgIn.height || -1;
        var disabled = cfgIn.disabled;
        var autoheight = cfgIn.autoheight || false;
        var cntr = 0;
        if (height !== -1) {
            tabContent.style.maxHeight = height;
            tabContent.style.height = height;
        } else if (autoheight == true){
            var ht = calcMaxChildHeight(tabContent);
            if (ht > 0) {
                tabContent.style.height = ht + "px";
            }
        }
        setTabActive(tabCtrl + tabIndex, clsActiveTab);

        var origcontents = tabContent.children;
        var length = origcontents.length;
        var newPage = origcontents[tabIndex];
        newPage.className = classVis;
        for (i = 0; i < length; i++) {
            if (i != tabIndex) {
                origcontents[i].className = classHid;
            }
        }

        return {
            showContent: function(el, cfgIn) {
                if (cfgIn.tIndex == tabIndex || disabled == true) {
                    return;
                }
                var parent = el.parentNode;
                if (!parent) {
                    parent = el.parentElement;
                }
                var current = tabIndex;
                var contents = tabContent.children;
                var oldPage = contents[current];
                oldPage.className = classHid;
                var currCtrl = tabCtrl + current;
                var oldCtrl = document.getElementById(currCtrl);
                removeClass(oldCtrl, clsActiveTab);
                var isClient = cfgIn.client || false;
                var currIndex = cfgIn.tIndex;
                if (!isClient) {
                    if (lastServerIndex==currIndex){
                        cntr= cntr + 1;
                    } else {
                        cntr = 0;
                        lastServerIndex = currIndex;
                    }
                    var submitted = currIndex +","+cntr;
                    console.log(" submitted="+submitted);
                    updateHidden(clientId, submitted);
                    contents[cfgIn.tIndex].className = classHid;
                    ice.se(null, clientId);
                } else {
                    tabIndex = cfgIn.tIndex || 0;
                    var newPage = contents[tabIndex];
                    newPage.className = classVis;
                }
                //remove class of activetabheader and hide old contents
                el.setAttribute("class", clsActiveTab);
            },
            updateProperties: function (clientId, cfgUpd) {
                var oldIdx = tabIndex;
                tabIndex = cfgUpd.tIndex;
                var newHt = cfgUpd.height || -1;
                console.log("newHt="+newHt+" height="+height);
                if (newHt !== -1 && newHt !== height ){
                    tabContainer.style.maxHeight = newHt;
                    tabContainer.style.height = newHt;
                }
            /*    if (oldIdx == tabIndex){
                    return;
                } */
                var oldCtrl = document.getElementById(tabCtrl + oldIdx);
                if (oldCtrl) {
                    removeClass(oldCtrl, clsActiveTab);
                }
                var autoWidth = cfgUpd.autoWidth;
                if (autoWidth){
                    setWidthStyle(document.getElementById(clientId));
                }
                //check to see if pages have been added or removed
                var contents = tabContent.children;
                var tabsId = clientId + "_tabs";
                setTabActive(tabCtrl + tabIndex, clsActiveTab);
                var tabElem = document.getElementById(tabsId);
                if (tabElem) {
                    if (oldIdx != tabIndex) {
                        contents[oldIdx].className = classHid; //need in case change is from server
                    }
                    contents[tabIndex].className = classVis;
                    if (cfgUpd.height) {
                        var height = cfgUpd.height;
                        if (height != tabContent.style.height) {
                            tabContainer.style.height = height;
                        }
                    }
                }
            },
            setDisabled: function(disabledIn){
                disabled = disabledIn;
            }
        }
    }

    ice.mobi.tabsetController = {
        tabsets: {},
        initClient: function(clientId, cfg) {
            if (!this.tabsets[clientId]) {
                this.tabsets[clientId] = TabSet(clientId, cfg);
            } else {
                this.tabsets[clientId].setDisabled(cfg.disabled);
                this.tabsets[clientId].updateProperties(clientId, cfg);
            }
        },
        showContent: function(clientId, el, cfgIn) {
            if (this.tabsets[clientId]) {
                this.tabsets[clientId].showContent(el, cfgIn);
            }
        }
    }

})();