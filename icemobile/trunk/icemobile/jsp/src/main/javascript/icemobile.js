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


if (!window['ice']) {
    window.ice = {};
}
if (!window['ice.mobi']) {
    window.ice.mobi = {};
}

if (!window.console) {
    console = {};
    if (ice.logInContainer) {
        console.log = ice.logInContainer;
    } else {
        log = function() {
        };
    }
}

ice.registerAuxUpload = function registerAuxUpload(sessionid, uploadURL) {
    var auxiframe = document.getElementById('auxiframe');
    if (null == auxiframe) {
        auxiframe = document.createElement('iframe');
        auxiframe.setAttribute("id", "auxiframe");
        auxiframe.setAttribute("style", "width:0px; height:0px; border: 0px");
        auxiframe.setAttribute("src",
                "icemobile://c=register&r=" +
                        escape(window.location) + "&JSESSIONID=" + sessionid +
                        "&u=" + escape(uploadURL)
        );
        document.body.appendChild(auxiframe);
    }
};

ice.mobiserial = function mobiserial(formId, typed) {
    var form = document.getElementById(formId);
    var els = form.elements;
    var len = els.length;
    var qString = [];
    var addField = function(name, value) {
        var tmpStr = "";
        if (qString.length > 0) {
            tmpStr = "&";
        }
        tmpStr += encodeURIComponent(name) + "=" + encodeURIComponent(value);
        qString.push(tmpStr);
    };
    for (var i = 0; i < len; i++) {
        var el = els[i];
        if (!el.disabled) {
            var prefix = "";
            if (typed) {
                var vtype = el.getAttribute("data-type");
                if (vtype) {
                    prefix = vtype + "-";
                } else {
                    prefix = el.type + "-";
                }
            }
            switch (el.type) {
                case 'submit':
                case 'button':
                case 'fieldset':
                    break;
                case 'text':
                case 'password':
                case 'hidden':
                case 'textarea':
                    addField(prefix + el.name, el.value);
                    break;
                case 'select-one':
                    if (el.selectedIndex >= 0) {
                        addField(prefix + el.name, el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    for (var j = 0; j < el.options.length; j++) {
                        if (el.options[j].selected) {
                            addField(prefix + el.name, el.options[j].value);
                        }
                    }
                    break;
                case 'checkbox':
                case 'radio':
                    if (el.checked) {
                        addField(prefix + el.name, el.value);
                    }
                    break;
                default:
                    addField(prefix + el.name, el.value);
            }
        }
    }
    // concatenate the array
    return qString.join("");
}

ice.mobilesx = function mobilesx(element, uploadURL) {
    var ampchar = String.fromCharCode(38);
    var form = ice.formOf(element);
    var formID = form.getAttribute('id');
    var formAction = form.getAttribute("action");
    var command = element.getAttribute("data-command");
    var id = element.getAttribute("data-id");
    var params = element.getAttribute("data-params");
    var sessionid = element.getAttribute("data-jsessionid");
    var windowLocation = window.location;
    var barURL = windowLocation.toString();
    var baseURL = barURL.substring(0,
            barURL.lastIndexOf("/")) + "/";
    
    if (!uploadURL) {
        uploadURL = element.getAttribute("data-posturl");
    }
    if (!uploadURL) {        
        if (0 === formAction.indexOf("/")) {
            uploadURL = window.location.origin + formAction;
        } else if ((0 === formAction.indexOf("http://")) ||
                (0 === formAction.indexOf("https://"))) {
            uploadURL = formAction;
        } else {
            uploadURL = baseURL + formAction;
        }
    }
//    else {
//        uploadURL += '/';
//    }


    var returnURL = "" + window.location;
    if ("" == window.location.hash) {
        var lastHash = returnURL.lastIndexOf("#");
        if (lastHash > 0) {
            returnURL = returnURL.substring(0, lastHash);
        }
        returnURL += "#icemobilesx";
    }

    if ("" != params) {
        params = "ub=" + escape(baseURL) + ampchar + params;
    }

    var sessionidClause = "";
    if ("" != sessionid) {
        sessionidClause = "&JSESSIONID=" + escape(sessionid);
    }
    var sxURL = "icemobile://c=" + escape(command +
            "?id=" + id + ampchar + (params ? params : '')) +
            "&u=" + escape(uploadURL) + "&r=" + escape(returnURL) +
            sessionidClause +
            "&p=" + escape(ice.mobiserial(formID, false));

    window.location = sxURL;
}

ice.mobi.sx = ice.mobilesx;

ice.mobi.invoke = function(element)  {
    var command = element.getAttribute("data-command");
    if (ice[command])  {
        var params = element.getAttribute("data-params");
        var id = element.getAttribute("data-id");
        if ((null == id) || ("" == id)) {
            id = element.getAttribute("id");
        }
        ice[command](id,params);
    } else {
        ice.mobi.sx(element);
    }
}

ice.formOf = function formOf(element) {
    var parent = element;
    while (null != parent) {
        if ("form" == parent.nodeName.toLowerCase()) {
            return parent;
        }
        parent = parent.parentNode;
    }
}

ice.mobi.flipvalue = function flipvalue(id, vars) {

    try {
        this.id = id;
        this.flipperEl = vars.elVal;

        this.event = vars.event;
        var hasBehaviors = false;

        if (this.flipperEl) {
            var oldClass = this.flipperEl.className;
            var value = "off";
            if (oldClass.indexOf('-off ') > 0) {
                this.flipperEl.className = 'mobi-flip-switch mobi-flip-switch-on ';
                value = true;
            } else {
                this.flipperEl.className = 'mobi-flip-switch mobi-flip-switch-off ';
                value = false;
            }
            var hidden = this.id + "_hidden";
            var thisEl = document.getElementById(hidden);
            if (thisEl) {
                thisEl.value = value.toString();
            }
        }

    } catch (e) {
        alert('Exception finding switcher: ' + e);
    }
};

(function() {
    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables

    function enhance(clientId)  {
        var carouselId = clientId+'_carousel';
        var iscroller = new iScroll(carouselId, {
	                    snap: 'li',
	                    momentum: false,
	                    hScrollbar: false,
                        checkDOMChanges: false,
                        bounce: false,
                        zoom: true,
	                    onScrollEnd: function () {
                            ice.mobi.carousel.scrollUpd(event, clientId, this.currPageX);
    	                }
	    });
        return iscroller;
    }
    function Carousel(clientId, key) {
        var myScroll = enhance(clientId);
        myScroll.scrollToPage(key);
        var myId = clientId;
        var currentVal=key;
        return {
           scrollUpdate: function(event, pageVal, cfg) {
               var changedVal = false;
            //   console.log('pageVal passed in='+pageVal);
               if (currentVal!=pageVal){
                    changedVal = true;
                   // this.setActive();
                   var undoNode = document.querySelector('.mobi-carousel-cursor-list > li.active');
                   if (undoNode){
                       undoNode.className = '';
                   }
               //    console.log( 'old hidden='+this.getHiddenVal()+ ' updated to hidden.value = '+pageVal);
                   this.setActive(pageVal);
               }
               if (changedVal){
                   var behaviors = cfg.behaviors;
                   var submitcfg = {};
                   submitcfg.source = myId;
                   submitcfg.execute = "@all";
                   submitcfg.render = "@all";
                   if (cfg.singleSubmit){
                       submitcfg.execute = "@this";
                       var refreshXHR = function(xhr, status, args) { ice.mobi.carousel.refreshCall(clientId, pageVal);};
                       submitcfg.oncomplete = refreshXHR;
                       mobi.AjaxRequest(submitcfg);
                   }
               /*    if (behaviors){  disabled for now until determined if required
                       submitcfg.behaviors = behaviors;
                   }  */

               }
           },
           getClientId: function(){
               return myId;
           },
           getSelectedItem: function(){
               return currentVal;
           } ,
           updateHidden: function( val){
             var hidden = document.getElementById(myId+"_hidden");
             if (hidden){
                 hidden.value= val;
             }
           },
           getHiddenVal: function(){
              var hidden = document.getElementById(myId+'_hidden');
              if (hidden){
                  var temp = hidden.value;
                  return temp;
              } else {
                  return 0;
              }
           },
           setActive: function(pageVal){
               if (currentVal != pageVal){
                  var nodeoldActive = document.querySelector('.mobi-carousel-cursor-list > li.active');
                  if (nodeoldActive){
                      nodeoldActive.className='';
                  }
                  currentVal =  pageVal;
                  this.updateHidden(pageVal);
               }
               var anode = document.querySelector('.mobi-carousel-cursor-list > li:nth-child('+(pageVal + 1) + ')');
               if (anode){
                   anode.className = 'active';
               }
               myScroll.refresh();
           },
           scrollToPage: function(key){
               myScroll.scrollToPage(key,0);
               var newVal = currentVal;
               if (key == "next"){
                   newVal++;
               }
               if (key== "prev"){
                   newVal--;
               }
               this.setActive(newVal);
           },
           refreshMe: function(key){
               if (myScroll){
                  this.setActive(key);
               }
           },
           updateProperties: function (clientId, cfgIn) {
               var hid= this.getHiddenVal();
               if (hid != currentVal){
                  this.scrollToPage(hid);
                  this.setActive(hid);
               }
               if (!myScroll.wrapper)  {
                //   console.log('WARNING:_ reinitialized scroller');
                   enhance(clientId);
               }
           }

        }
    }
    ice.mobi.carousel = {
        acarousel: {},
        cfg: {},
        unload: {},
        loaded: function(clientId, cfgIn) {
            if (!this.acarousel[clientId]){
                this.cfg[clientId] = cfgIn;
                this.acarousel[clientId] = Carousel(clientId, cfgIn.key);
                this.acarousel[clientId].setActive(cfgIn.key);
                this.unload[clientId] = function () {
                    ice.mobi.carousel.unloadTest(clientId);
                };
                var node = document.getElementById(clientId);
                node.addEventListener("DOMSubtreeModified", this.unload[clientId], false);
            } else {
                this.cfg[clientId] = cfgIn;
                this.acarousel[clientId].updateProperties(clientId, cfgIn);
                this.acarousel[clientId].setActive(cfgIn.key);

            }
        },
        scrollUpd: function(event, clientId, pageVal){
            this.acarousel[clientId].scrollUpdate(event, pageVal, this.cfg[clientId]);
        },
        scrollTo: function(clientId, key){
            this.acarousel[clientId].scrollToPage(key);
        },
        refreshCall: function(clientId, key){
            this.acarousel[clientId].refreshMe(key);
        },
        unloadTest: function(clientId){
            if (!document.getElementById(clientId) && this.acarousel[clientId]!=null){
           //    console.log("unloadTest setting id="+clientId+" to null");
               this.acarousel[clientId] = null;
               this.cfg[clientId] = null;
            //   document.removeEventListener("DOMSubtreeModified",this.unload[clientId], false ) ;
            }
        }
    }
  })();


ice.mobi.tabsetController = {
    panels: {},
    initClient: function(clientId, cfg) {
        if (true) {
            this.panels[clientId] = ice.mobi.tabsetController.TabSet(clientId, cfg);
        } else {
            this.panels[clientId].updateProperties(clientId, cfg);
        }
    },
    showContent: function(clientId, el, cfgIn) {
//        alert('showContent ' + clientId + ', tabIndex: ' + cfgIn.tIndex );
        if (this.panels[clientId]) {
            this.panels[clientId].showContent(el, cfgIn);
        }
    },

    updateHidden: function(clientId, value) {
        var hidden = document.getElementById(clientId + "_hidden");
        if (hidden) {
            hidden.value = value;
        }
    },
    /* taken from accordion with slight modifications */
    calcMaxChildHeight: function (containerEl) {
        var mxht = 0;
        //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
        var children = containerEl.getElementsByTagName('div');
        for (var i = 0; i < children.length; i++) {
            if (children[i].scrollHeight > mxht) {
                mxht = children[i].scrollHeight;
            }
        }
        return mxht;
    },
    hasClass: function (ele, cls) {
        return ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
    },
    addClass: function (ele, cls) {
        if (ice.mobi.tabsetController.hasClass(ele, cls)) {
            ele.className = cls;
        }
    },
    removeClass: function (ele, cls) {
        if (ice.mobi.tabsetController.hasClass(ele, cls)) {
            // var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');  don't need if we don't allow to skin?
            ele.className = " ";
        }
    },

    findAndSet: function (pool, searchToken, newClass) {

        var page;
        for (idx = 0; idx < pool.length; idx ++) {
            page = pool[idx];
            if (page.id) {
                i = page.id.indexOf(searchToken);
                if (i > 0) {
                    page.className = newClass;
                }
            }
        }
    },

//declare functions who creates object with methods that have access to the local variables of the function
//so in effect the returned object can operate on the local state declared in the function ...
//think about them as object fields in Java, also gone is the chore of copying the constructor parameters into fields
//-------------------------------------
    TabSet: function TabSet(clientId, cfgIn) {
        // setup tabContainer
        var myTabId = clientId;
        var tabContainer = document.getElementById(clientId);
        var tabContent = document.getElementById(clientId + "_tabContent");
        var cfg = cfgIn;
        var tabIndex = cfgIn.tIndex;
        if (cfgIn.height) {
            tabContainer.style.height = height;
        } else {
            var ht = ice.mobi.tabsetController.calcMaxChildHeight(tabContent);
            if (ht > 0) {
                tabContent.style.height = ht + "px";
            }
        }
        var contents = tabContent.getElementsByClassName("mobi-tabpage-hidden");

        return {
            showContent: function(el, cfgIn) {
                if (cfgIn.tIndex == tabIndex) {
                    return;
                }
                var parent = el.parentNode;
                if (!parent) {
                    parent = el.parentElement;
                }
                var current = parent.getAttribute("data-current");
                var contents = tabContent.childNodes;

                searchToken = "tab" + current + "_wrapper";
                ice.mobi.tabsetController.findAndSet(contents, searchToken, "mobi-tabpage-hidden");

                var currCtrl = myTabId + "tab_" + current;
                var oldCtrl = document.getElementById(currCtrl);
                ice.mobi.tabsetController.removeClass(oldCtrl, "activeTab");
                var hiddenVal = cfgIn.tIndex;
                ice.mobi.tabsetController.updateHidden(myTabId, hiddenVal);

                tabIndex = cfgIn.tIndex || 0;
                searchToken = "tab" + tabIndex + "_wrapper";
                ice.mobi.tabsetController.findAndSet(contents, searchToken, "mobi-tabpage");
                parent.setAttribute("data-current", cfgIn.tIndex);

                //remove class of activetabheader and hide old contents
                el.setAttribute("class", "activeTab");
            },


            updateProperties: function (clientId, cfgUpd) {
                var oldIdx = tabIndex;
                if (cfgUpd.tIndex != tabIndex) {
                    tabIndex = cfgUpd.tIndex;
                    var tabsId = clientId + "_tabs";
                    var tabElem = document.getElementById(tabsId);
                    if (tabElem) {
                        var lis = tabElem.getElementsByTagName("li");
                        var contents = tabContent.childNodes;
                        contents[oldIdx].className = "mobi-tabpage-hidden"; //need in case change is from server
                        contents[tabIndex].className = "mobi-tabpage";
                        if (cfgUpd.height && cfgUpd.height != tabContainer.style.height) {
                            tabContainer.style.height = height;
                        }
                    }
                }
            }
        }
    }
};
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
    function getHiddenVal(clientId){
        var hidden = document.getElementById(clientId+"_hidden");
        if (hidden && hidden.value){
            return hidden.value;
        } else {
            return null;
        }
    }
    function getHandleHeight(aRoot){
        var handleNode = aRoot.querySelector('.mobi-accordion .handle');
        var handleHeight = "33"; //default css handle height is 33px
        if (handleNode){
           var temp = handleNode.scrollHeight || handleNode.height || handleNode.offsetHeight || handleNode.maxHeight;
           if (temp > 0){
               handleHeight = temp;
           }
        }
        return handleHeight;
    }
    function calcMaxDivHeight(clientId, handleHt) {
        var accord = document.getElementById(clientId);
        var mxht = 0;
        //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
        var children = document.getElementById(clientId).getElementsByTagName('section');
        for (var i = 0; i < children.length; i++) {
            var anode = children[i];
            var max = Math.max(anode.offsetHeight, anode.scrollHeight, anode.clientHeight);
            //init all classes to close
         //   anode.className = "close";
         //   anode.style.height = handleHt+"px";
            if (max > 0 && max > mxht) {
                mxht = max;
            }
        }
        if (mxht <= handleHt && accord.clientHeight>0) {
            mxht = accord.clientHeight - handleHt;
        }
        return mxht;
    }
    function updateFixedHeight(clientId, handleheight, fixedHeight){
         var calcht = calcMaxDivHeight(clientId, handleheight) ;
         if (calcht > 0) {
             return  calcht+"px";
         }
        else return fixedHeight;
    }
    function setHeight(opened, height){
        opened.style.height = height;
        opened.style.maxHeight = height;
    }
    function setPane(elem, height, style){
        elem.className=style;
        if (height){
            setHeight(elem, height);
        }
    }
    function Accordion(clientId, cfgIn) {
        var containerId = clientId+"_acc" ;
        var paneOpId = cfgIn.opened || null;
        var accordRoot = document.getElementById(containerId);
        if (!paneOpId && accordRoot.hasChildNodes()){
            paneOpId = accordRoot.firstChild.id;
        }
        var scroll = false;
        paneOpId += "_sect";
        var openElem = document.getElementById(paneOpId);
        var handleheight = getHandleHeight(accordRoot);
        var autoheight = cfgIn.autoHeight || false;
        var fixedHeight = cfgIn.fixedHeight || null;
        if (autoheight == true) {
            fixedHeight = updateFixedHeight(containerId, handleheight, fixedHeight, scroll);
        }  else {
            scroll = true;
        }
        if (scroll){
            //so this with js? or with renderer?? see how it affects domdiff
        }
        openElem.className="open";
        if (fixedHeight){
            setHeight(openElem, fixedHeight);
        }
        return {
            toggle: function(clientId, el, cached) {
                var theParent = el.parentElement;
                if (!theParent) {
                    theParent = el.parentNode; //mozilla
                }
                var accId = clientId+"_acc";
                var autoheight = ice.mobi.accordionController.autoheight[clientId];
                //which child is the element?
                var changed= true;
                openElem = document.getElementById(paneOpId);
                if (paneOpId && paneOpId == theParent.id){
                    if (openElem.className=="open"){
                        setPane(openElem, handleheight+"px", "closed");

                    } else {
                        setPane( openElem, fixedHeight, "open");
                    }
                    changed=false;
                }
                else {//panel has changed
                    setPane(openElem, handleheight+"px", "closed");
                    if (autoheight){
                        fixedHeight = updateFixedHeight(accId, handleheight, fixedHeight);
                    }
                    setPane(theParent,fixedHeight,"open");
                    paneOpId = theParent.id;
                }
                var pString = theParent.getAttribute("id");
                var subString = pString.replace("_sect","");
                updateHidden(clientId, subString);
                if (!cached && ice.mobi.accordionController.singleSubmit[clientId] && changed) { //renderer take care of closed panes
                    ice.se(null, clientId);
                }
            },
            updateProperties: function (clientId, cfgUpd) {
                  var fixedHeight=cfgUpd.fixedHeight ||null;
                var autoheight = cfgUpd.autoHeight;
                if (autoheight ) {
                    ice.mobi.accordionController.autoheight[clientId] = autoheight;
                }
                if (autoheight) {
                    var updHeight = updateFixedHeight(clientId+"_acc", handleheight, fixedHeight);
                    if (fixedHeight != updHeight){
                        fixedHeight = updHeight;
                        setHeight(document.getElementById(paneOpId), fixedHeight);
                    }
                }
                if( !paneOpId){
                    paneOpId = cfgIn.opened+"_sect" || null;
                }
                var opened = document.getElementById(paneOpId);
                if (!opened){  //may have been deleted or removed
                    var root = document.getElementById(clientId+"_acc");
                    opened = root.firstChild;
                    paneOpId = root.firstChild.id;
                }
                var hiddenVal = getHiddenVal(clientId); //could have pushed new value
                if (hiddenVal!=null) {
                    var newPane = hiddenVal+"_sect";
                    if (newPane!=paneOpId){
                       setPane(opened, handleheight+"px", "closed");
                       var newElem = document.getElementById(newPane);
                       if (newElem){
                           setPane(newElem, fixedHeight, "open");
                       }
                        paneOpId = newPane;
                        openElem = newElem;
                    }
                }
            }
        }
    }

    ice.mobi.accordionController = {
        panels: {},
        autoheight: {},
        singleSubmit: {},
        initClient: function(clientId, cfg) {
            if (!this.panels[clientId]) {
                this.autoheight[clientId]= cfg.autoHeight;
                this.singleSubmit[clientId] = cfg.singleSubmit;
                this.panels[clientId] = Accordion(clientId, cfg);
            } else {
                this.panels[clientId].updateProperties(clientId, cfg);
            }
        },
        toggleClient: function(clientId, el, cachetyp) {
            if (this.panels[clientId]) {
                this.panels[clientId].toggle(clientId, el, cachetyp);
            } else {
                this.initClient(clientId, {});
                this.panels[clientId].toggle(clientId, el, cachetyp);
            }
        },
        toggleMenu: function(clientId, el){
            if (this.panels[clientId]) {
                this.panels[clientId].toggle(el, true);
            } else{
                this.initClient(clientId, {autoheight:false});
                this.panels[clientId].toggle(clientId, el, true);
            }
        } ,
        unload: function(clientId){
            this.panels[clientId] = null;
            this.autoheight[clientId]=null;
            this.panels[clientId]=null;
        }
    }

})();


ice.mobi.panelAutoCenter = function panelAutoCenter(clientId) {
    var windowWidth = ice.mobi._windowWidth();
    var windowHeight = ice.mobi._windowHeight();
    var scrollTop = document.body.scrollTop;
    if (scrollTop == 0) {
        scrollTop = document.documentElement.scrollTop;
    }
    if (windowHeight > 0) {
        var contentElement = document.getElementById(clientId);
        var contentHeight = contentElement.offsetHeight;
        var contentWidth = contentElement.offsetWidth;
        if (windowHeight - contentHeight > 0) {
            contentElement.style.position = 'absolute';
            contentElement.style.top = scrollTop + ((windowHeight / 2) - (contentHeight / 2)) + 'px';
            contentElement.style.left = ((windowWidth / 2) - (contentWidth / 2)) + 'px';
        } else {
            contentElement.style.position = 'absolute';
            contentElement.style.top = 0;
            contentElement.style.left = ((windowWidth / 2) - (contentWidth / 2)) + 'px';
        }
    }
};

ice.mobi._windowHeight = function () {
    var windowHeight = 0;
    if (typeof(window.innerHeight) == 'number') {
        windowHeight = window.innerHeight;
    } else {
        if (document.documentElement && document.documentElement.clientHeight) {
            windowHeight = document.documentElement.clientHeight;
        } else {
            if (document.body && document.body.clientHeight) {
                windowHeight = document.body.clientHeight;
            }
        }
    }
    return windowHeight;
};
ice.mobi._windowWidth = function () {
    var windowWidth = 0;
    if (typeof(window.innerWidth) == 'number') {
        windowWidth = window.innerWidth;
    } else {
        if (document.documentElement && document.documentElement.clientWidth) {
            windowWidth = document.documentElement.clientWidth;
        } else {
            if (document.body && document.body.clientWidth) {
                windowWidth = document.body.clientWidth;
            }
        }
    }
    return windowWidth;
};

ice.mobi.datespinner = {
    pattern:{}, //supported formats are dd/MM/yyyy, MM-dd-yyyy, dd-MM-yyyy, yyyy-MM-dd, yyyy-dd-MM
    opened:{},
    centerCalculation:{},
    scrollEvent:{},
    init:function (clientId, yrSel, mSel, dSel, format) {
        var idPanel = clientId + "_bg";
        if (!document.getElementById(idPanel).className) {
            document.getElementById(idPanel).className = 'mobi-date-bg-inv';
        }
        var intDt = parseInt(dSel);
        var intMth = parseInt(mSel);
        var intYr = parseInt(yrSel);
        if (format) {
            this.pattern[clientId] = format;
            //           ice.log.debug(ice.log, ' pattern changed to ='+this.pattern);
        }
        this.opened[clientId] = false;
        //have to set the value controls to the correct integer
        var mnthEl = document.getElementById(clientId + "_mInt");
        var yrEl = document.getElementById(clientId + "_yInt");
        var dateEl = document.getElementById(clientId + "_dInt");
        if (mnthEl) {
            mnthEl.innerHTML = intMth;
        }
        if (yrEl) {
            yrEl.innerHTML = intYr;
        }
        if (dateEl) {
            dateEl.innerHTML = intDt;
        }
        this.updateDate(clientId);
        this.scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
    },
    mUp:function (clientId) {
        var mId = clientId + '_mInt';
        var mnthEl = document.getElementById(mId);
        if (mnthEl) {
            var mInt = this.getIntValue(mId);
            if (mInt == 12) {
                mnthEl.innerHTML = 1;
            }
            else {
                mnthEl.innerHTML = mInt + 1;
            }
        }
        this.updateDate(clientId);
    },
    mDn:function (clientId) {
        var mId = clientId + '_mInt';
        var mnthEl = document.getElementById(mId);
        if (mnthEl) {
            var mInt = this.getIntValue(mId);
            if (mInt == 1) {
                mnthEl.innerHTML = 12;
            }
            else {
                mnthEl.innerHTML = mInt - 1;
            }
        }
        this.updateDate(clientId);
    },
    yUp:function (clientId, yrMin, yrMax) {
        var yId = clientId + '_yInt';
        var yrEl = document.getElementById(yId);
        if (yrEl) {
            var yInt = this.getIntValue(yId);
            if (yInt == yrMax) {
                //nothing
            }
            else {
                yrEl.innerHTML = yInt + 1;
            }
        }
        this.updateDate(clientId);
    },
    yDn:function (clientId, yrMin, yrMax) {
        var yrEl = document.getElementById(clientId + '_yInt');
        if (yrEl) {
            yInt = this.getIntValue(clientId + '_yInt');
            if (yInt == yrMin) {
                //nothing
            }
            else {
                yrEl.innerHTML = yInt - 1;
            }
        }
        this.updateDate(clientId);
    },
    dUp:function (clientId) {
        var dId = clientId + '_dInt';
        var dEl = document.getElementById(dId);
        var mInt = this.getIntValue(clientId + "_mInt");
        var yInt = this.getIntValue(clientId + "_yInt");
        var dInt = this.getIntValue(dId);
        var numDaysInMonth = this.daysInMonth(mInt, yInt);
        if (dEl) {
            if (dInt >= numDaysInMonth) {
                dEl.innerHTML = 1;
            }
            else {
                dEl.innerHTML = dInt + 1;
            }
        }
        this.updateDate(clientId);
    },
    dDn:function (clientId) {
        var dId = clientId + '_dInt';
        var dEl = document.getElementById(dId);
        var mInt = this.getIntValue(clientId + "_mInt");
        var yInt = this.getIntValue(clientId + "_yInt");
        var dInt = this.getIntValue(dId);
        var numDaysInMonth = this.daysInMonth(mInt, yInt);
        if (dEl) {
            if (dInt == 1 || dInt > numDaysInMonth) {
                dEl.innerHTML = numDaysInMonth;
            }
            else {
                dEl.innerHTML = dInt - 1;
            }
        }
        this.updateDate(clientId);
    },
    updateDate:function (clientId) {
        var dId = clientId + "_dInt";
        var dEl = document.getElementById(dId);
        var mInt = this.getIntValue(clientId + "_mInt");
        var yInt = this.getIntValue(clientId + "_yInt");
        var dInt = this.getIntValue(dId);
        var upDate = this.validate(yInt, mInt, dInt);
        if (!upDate) {
            dInt = this.daysInMonth(mInt, yInt);
            dEl.innerHTML = dInt;
            upDate = this.validate(yInt, mInt, dInt);

        }
        this.writeTitle(clientId, upDate);
    },

    writeTitle:function (clientId, date) {
        var titleEl = document.getElementById(clientId + '_title');
        titleEl.innerHTML = date.toDateString();
    },

    daysInMonth:function (iMnth, iYr) {
        var aDate = new Date(iYr, iMnth, 0);
        return aDate.getDate();
    },

    validate:function (iY, iM, iD) {
        if (iY != parseInt(iY, 10) || iM != parseInt(iM, 10) || iD != parseInt(iD, 10)) {
            return false;
        }
        iM--;
        var newDate = new Date(iY, iM, iD);
        if ((iY == newDate.getFullYear()) && (iM == newDate.getMonth()) && (iD == newDate.getDate())) {
            return newDate;
        }
        else {
            return false;
        }
    },

    getIntValue:function (id) {
        var element = document.getElementById(id);
        if (element) {
            var stringEl = element.innerHTML;
            return parseInt(stringEl);
        } else {
            return 1;
        }
    },
    select:function (clientId, cfg) {
        //
        var inputEl = document.getElementById(clientId + '_input');
        var hiddenEl = document.getElementById(clientId + '_hidden');
        var dInt = this.getIntValue(clientId + "_dInt");
        var mInt = this.getIntValue(clientId + "_mInt");
        var yInt = this.getIntValue(clientId + "_yInt");
        var dStr = dInt;
        var mStr = mInt;
        if (dInt < 10) {
            dStr = '0' + dInt;
        }
        if (mInt < 10) {
            mStr = '0' + mInt;
        }
        // default to american MM/dd/yyyy, pattern
        var dateStr = mStr + '/' + dStr + '/' + yInt;
        var myPattern = this.pattern[clientId];
        // compare '-' dash delimiter
        if (myPattern == 'MM-dd-yyyy') {
            dateStr = mStr + '-' + dStr + '-' + yInt;
        } else if (myPattern == 'yyyy-MM-dd') {
            dateStr = yInt + "-" + mStr + "-" + dStr;
        } else if (myPattern == 'yyyy-dd-MM') {
            dateStr = yInt + "-" + dStr + "-" + mStr;
        } else if (myPattern == 'dd-MM-yyyy') {
            dateStr = dStr + '-' + mStr + '-' + yInt;
        }
        // compare '/' dash delimiter
        else if (myPattern == 'MM/dd/yyyy') {
            dateStr = mStr + '/' + dStr + '/' + yInt;
        } else if (myPattern == 'yyyy/MM/dd') {
            dateStr = yInt + "/" + mStr + "/" + dStr;
        } else if (myPattern == 'yyyy/dd/MM') {
            dateStr = yInt + "/" + dStr + "/" + mStr;
        } else if (myPattern == 'dd/MM/yyyy') {
            dateStr = dStr + '/' + mStr + '/' + yInt;
        }
        hiddenEl.value = dateStr;
        inputEl.value = dateStr;

        //this.dateSubmit(cfg, clientId);
        this.close(clientId);
    },
    dateSubmit: function(cfg, clientId) {
        this.cfg = cfg;
        var singleSubmit = this.cfg.singleSubmit;
        var event = this.cfg.event;
        var hasBehaviors = false;
        var behaviors = this.cfg.behaviors;
        if (behaviors) {
            hasBehaviors = true;
        }
        if (hasBehaviors) {
            if (behaviors.change) {
                behaviors.change();
            }
        }
        if (!hasBehaviors && singleSubmit) {
            alert('Bad shit. Calling ice.se');
            ice.se(event, clientId);
        }
    },
    inputSubmit: function(clientId, cfg) {
        if (this.opened[clientId] == true) {
            return;
        }
        var hiddenEl = document.getElementById(clientId + '_hidden');
        var inputEl = document.getElementById(clientId + '_input');
        hiddenEl.value = inputEl.value;
        this.dateSubmit(cfg, clientId);
    },
    toggle:function (clientId) {
        if (!this.opened[clientId]) {
            this.open(clientId);
        } else {
            this.close(clientId);
        }
    },
    open:function (clientId) {
        var idPanel = clientId + "_bg";
        var idPopPanel = clientId + "_popup";

        // add scroll listener
        this.centerCalculation[clientId] = function () {
            ice.mobi.panelAutoCenter(idPopPanel);
        };

        if (window.addEventListener) {
            window.addEventListener(this.scrollEvent, this.centerCalculation[clientId], false);
            window.addEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie event listener
            window.attachEvent(this.scrollEvent, this.centerCalculation[clientId]);
            window.attachEvent("resize", this.centerCalculation[clientId]);
        }
        // add visible style classes
        document.getElementById(idPanel).className = "mobi-date-bg";
        document.getElementById(idPopPanel).className = "mobi-date-container";
        // set as visible.
        this.opened[clientId] = true;
        // calculate center for first view
        ice.mobi.panelAutoCenter(idPopPanel);
    },
    close:function (clientId) {
        var idPanel = clientId + "_bg";

        // remove scroll listener
        if (window.removeEventListener) {
            window.removeEventListener(this.scrollEvent, this.centerCalculation[clientId], false);
            window.removeEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie cleanup
            window.detachEvent(this.scrollEvent, this.centerCalculation[clientId], false);
            window.detachEvent('resize', this.centerCalculation[clientId], false);
        }
        // hide the dialog
        document.getElementById(idPanel).className = "mobi-date-bg-inv";
        document.getElementById(clientId + "_popup").className = "mobi-date-container-inv";
        this.opened[clientId] = false;
        this.centerCalculation[clientId] = undefined;
    },
    unload:function (clientId) {
        /* var titleEl = document.getElementById(clientId+'_title');
         titleEl.innerHTML = "";  */
        this.pattern[clientId] = null;
        this.opened[clientId] = null;
    }
}

ice.mobi.panelpopup = {
    visible:{},
    centerCalculation:{},
    cfg:{},
    init:function (clientId, cfgIn) {
        this.cfg[clientId] = cfgIn;
        var cfg = this.cfg[clientId];
        var visible = cfg.visible;
        var autoCenter = cfg.autocenter;
        var containerId = clientId + "_popup";

        //if nothing already in client saved state, then we use the server passed value
        if (!this.visible[clientId]) {
            this.visible[clientId] = visible;
        }

        if (this.visible[clientId]) {
            this.open(clientId);
        } else {
            this.close(clientId);
        }
    },
    // only called when in client side mode
    open:function (clientId) {
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        var cfg = this.cfg[clientId];
        var autocenter = cfg.autocenter;
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";

        document.getElementById(idPanel).className = "mobi-panelpopup-bg ";
        document.getElementById(containerId).className = "mobi-panelpopup-container ";

        if (autocenter) {
            // add scroll listener
            this.centerCalculation[clientId] = function () {
                ice.mobi.panelAutoCenter(containerId);
            };

            if (window.addEventListener) {
                window.addEventListener(scrollEvent, this.centerCalculation[clientId], false);
                window.addEventListener('resize', this.centerCalculation[clientId], false);
            } else { // older ie event listener
                window.attachEvent(scrollEvent, this.centerCalculation[clientId]);
                window.attachEvent("resize", this.centerCalculation[clientId]);
            }
            // calculate center for first view
            ice.mobi.panelAutoCenter(containerId);
        }

        this.visible[clientId] = true;
//        this.updateHidden(clientId, true);
    },
    // only called when in client side mode
    close:function (clientId) {
        var idPanel = clientId + "_bg";
        var cfg = this.cfg[clientId];
        var autocenter = cfg.autocenter;
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";

        document.getElementById(idPanel).className = "mobi-panelpopup-bg-hide ";
        document.getElementById(clientId + "_popup").className = "mobi-panelpopup-container-hide ";

        if (autocenter) {
            if (window.removeEventListener) {
                window.removeEventListener(scrollEvent, this.centerCalculation[clientId], false);
                window.removeEventListener('resize', this.centerCalculation[clientId], false);
            } else { // older ie cleanup
                window.detachEvent(scrollEvent, this.centerCalculation[clientId], false);
                window.detachEvent('resize', this.centerCalculation[clientId], false);
            }
            this.centerCalculation[clientId] = undefined;
        }

        this.visible[clientId] = false;
//        this.updateHidden(clientId, false);
    },
    updateHidden:function (clientId, value) {
        var hidden = document.getElementById(clientId + "_hidden");
        if (hidden) {
            hidden.value = value;
        }
    },
    unload:function (clientId) {
        this.cfg[clientId] = null;
        this.visible[clientId] = null;
    }

}

ice.mobi.timespinner = {
    pattern:{}, //only supports 'hh:mm a' at this time.
    opened:{},
    centerCalculation:{},
    scrollEvent:{},
    init:function (clientId, hrSel, mSel, aSel, format) {
        var idPanel = clientId + "_bg";
        if (!document.getElementById(idPanel).className) {
            document.getElementById(idPanel).className = 'mobi-date-bg-inv';
        }
        var intAmPm = parseInt(aSel);
        var intMinute = parseInt(mSel);
        var intHr = parseInt(hrSel);
        if (format) {
            this.pattern[clientId] = format;
//            ice.log.debug(ice.log, ' pattern change not yet implemented =' + this.pattern);
        }
        this.opened[clientId] = false;
        //have to set the value controls to the correct integer
        var hrEl = document.getElementById(clientId + "_hrInt");
        var minEl = document.getElementById(clientId + "_mInt");
        var ampmEl = document.getElementById(clientId + "_ampmInt");
        if (minEl) {
            minEl.innerHTML = intMinute;
        }
        if (hrEl) {
            hrEl.innerHTML = intHr;
        }
        var ampm = 'AM';
        if (intAmPm > 0) {
            ampm = 'PM';
        }
        //       ice.log.debug(ice.log, 'val of intAmpm ='+intAmPm + ' ampm = '+ampm);
        if (ampmEl) {
            ampmEl.innerHTML = ampm;
        }
        this.updateTime(clientId);
        this.scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
    },
    mUp:function (clientId) {
        var mId = clientId + '_mInt';
        var minEl = document.getElementById(mId);
        if (minEl) {
            var mInt = this.getIntValue(mId);
            if (mInt == 59) {
                minEl.innerHTML = 0;
            }
            else {
                minEl.innerHTML = mInt + 1;
            }
        }
        this.updateTime(clientId);
    },
    mDn:function (clientId) {
        var mId = clientId + '_mInt';
        var minEl = document.getElementById(mId);
        if (minEl) {
            var mInt = this.getIntValue(mId);
            if (mInt == 0) {
                minEl.innerHTML = 59;
            }
            else {
                minEl.innerHTML = mInt - 1;
            }
        }
        this.updateTime(clientId);
    },
    hrUp:function (clientId) {
        var hrId = clientId + '_hrInt';
        var hrEl = document.getElementById(hrId);
        if (hrEl) {
            var hrInt = this.getIntValue(hrId);
            if (hrInt == 12) {
                hrEl.innerHTML = 1;
            }
            else {
                hrEl.innerHTML = hrInt + 1;
            }
        }
        this.updateTime(clientId);
    },
    hrDn:function (clientId) {
        var hrEl = document.getElementById(clientId + '_hrInt');
        if (hrEl) {
            var hrInt = this.getIntValue(clientId + '_hrInt');
            if (hrInt == 1) {
                hrEl.innerHTML = 12;
            }
            else {
                hrEl.innerHTML = hrInt - 1;
            }
        }
        this.updateTime(clientId);
    },
    ampmToggle:function (clientId) {
        var aId = clientId + '_ampmInt';
        var aEl = document.getElementById(aId);
        if (aEl.innerHTML == "AM") {
            aEl.innerHTML = 'PM';
        }
        else {
            aEl.innerHTML = 'AM';
        }
        this.updateTime(clientId);
    },
    updateTime:function (clientId) {
        var ampm = clientId + "_ampmInt";
        var ampmEl = document.getElementById(ampm);
        var mInt = this.getIntValue(clientId + "_mInt");
        var hrInt = this.getIntValue(clientId + "_hrInt");
        this.writeTitle(clientId, hrInt, mInt, ampmEl.innerHTML);
    },

    writeTitle:function (clientId, hr, min, ampm) {
        if (hr < 10) {
            hr = '0' + hr;
        }
        if (min < 10) {
            min = '0' + min;
        }
        var time = hr + ':' + min + ' ' + ampm;
        var titleEl = document.getElementById(clientId + '_title');
        titleEl.innerHTML = time;
    },

    getIntValue:function (id) {
        var element = document.getElementById(id);
        if (element) {
            var stringEl = element.innerHTML;
            return parseInt(stringEl);
        } else {
            return 1;
        }
    },
    select:function (clientId, cfg) {
        this.cfg = cfg;
        var singleSubmit = this.cfg.singleSubmit;
        var event = this.cfg.event;
        var hasBehaviors = false;
        var behaviors = this.cfg.behaviors;
        if (behaviors) {
            hasBehaviors = true;
        }
        var inputEl = document.getElementById(clientId + '_input');
        var titleEl = document.getElementById(clientId + '_title');
        inputEl.value = titleEl.innerHTML;
        if (hasBehaviors) {
            if (behaviors.change) {
                behaviors.change();
            }
        }
        if (!hasBehaviors && singleSubmit) {
            alert("should never get here");
            ice.se(event, clientId);
        }
        this.close(clientId);
    },
    toggle:function (clientId) {
        if (!this.opened[clientId]) {
            this.open(clientId);
        } else {
            this.close(clientId);
        }
    },
    open:function (clientId) {
        var idPanel = clientId + "_bg";
        var idPopPanel = clientId + "_popup";

        document.getElementById(idPanel).className = "mobi-time-bg";
        document.getElementById(idPopPanel).className = "mobi-time-container";

        // add scroll listener
        this.centerCalculation[clientId] = function () {
            ice.mobi.panelAutoCenter(idPopPanel);
        };

        if (window.addEventListener) {
            window.addEventListener(this.scrollEvent, this.centerCalculation[clientId], false);
            window.addEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie event listener
            window.attachEvent(this.scrollEvent, this.centerCalculation[clientId]);
            window.attachEvent("resize", this.centerCalculation[clientId]);
        }
        // set visible
        this.opened[clientId] = true;
        // calculate center for first view
        ice.mobi.panelAutoCenter(idPopPanel);
    },
    close:function (clientId) {
        var idPanel = clientId + "_bg";
        // remove scroll listener
        if (window.removeEventListener) {
            window.removeEventListener(this.scrollEvent, this.centerCalculation[clientId], false);
            window.removeEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie cleanup
            window.detachEvent(this.scrollEvent, this.centerCalculation[clientId], false);
            window.detachEvent('resize', this.centerCalculation[clientId], false);
        }
        // hide the panels
        document.getElementById(idPanel).className = "mobi-time";
        document.getElementById(clientId + "_popup").className = "mobi-time-container-inv";
        this.opened[clientId] = false;
        this.centerCalculation[clientId] = undefined;
    },
    unload:function (clientId) {
        alert("unloading for clientId");
        /* var titleEl = document.getElementById(clientId+'_title');
         titleEl.innerHTML = "";  */
        this.pattern[clientId] = null;
        this.opened[clientId] = null;
    }
};
ice.mobi.setCookie = function setCookie(name, val, path) {
    if (!val) {
        var date = new Date();
        date.setTime(date.getTime() + (-1 * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
        document.cookie = name + "=''; path=" + path + expires;
    }
    else {
        document.cookie = name + '=' + encodeURIComponent(val) + '; path=' + path;
    }
}
ice.mobi.locationWithoutViewParam = function() {
    var url = window.location.href;
    url = url.replace(/l=[m|t|d]/, '');
    url = url.replace(/\?&/, '\?');
    return url;
}

ice.mobi.storeLocation = function(id, coords) {
    if (!coords) {
        return;
    }
    var el = document.getElementById(id);
    if (!el) {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {
        parts = new Array(4)
    }
    ;
    parts[0] = coords.latitude;
    parts[1] = coords.longitude;
    parts[2] = coords.altitude;
    el.value = parts.join();
}

ice.mobi.storeDirection = function(id, orient) {
    if (orient.webkitCompassAccuracy <= 0) {
        return;
    }
    var el = document.getElementById(id);
    if (!el) {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {
        parts = new Array(4)
    }
    parts[3] = orient.webkitCompassHeading;
    el.value = parts.join();
}

ice.mobi.geolocation = {
    watchId: 0,
    clientId: "",

    /**
     * Perform a call to watchPosition to fetch running updates to position.
     *
     * @param pClientId base id of hidden field
     * @param highAccuracy true if highAccuracy results to be fetched
     * @param maxAge oldest acceptable cached results (in ms)
     * @param timeout longest time to wait for value (in ms)
     */
    watchLocation: function (pClientId, highAccuracy, maxAge, timeout) {

        ice.mobi.geolocation.clientId = pClientId;
        ice.mobi.geolocation.clearWatch();
        // It seems like on Android that passing any argument at all for enableHighAccuracy
        // enables high accuracy.
        var geoParams = {};
        if (maxAge > 0)  {
            geoParams.maximumAge = maxAge * 1000;
        }
        if (timeout > 0)  {
            geoParams.timeout = timeout * 1000;
        }
        if (highAccuracy != 'false')  {
            geoParams.enableHighAccuracy = true;
        }
        console.log('Launching watchPosition, ' + 
            'maxAge: ' + geoParams.maximumAge + '(ms),' +
            ' timeout: ' + geoParams.timeout + '(ms)' +
            ' highAccuracy: ' + geoParams.enableHighAccuracy);

        ice.mobi.geolocation.watchId = navigator.geolocation.watchPosition(
                this.successCallback, this.errorCallback,
                geoParams );

        window.addEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
        ice.onElementRemove(pClientId, ice.mobi.geolocation.clearWatch);
        console.log('Lauching positionWatch for client: ' + pClientId + 
                ' watchId: ' + ice.mobi.geolocation.watchId);
    },

    /**
     * Perform a single call to the navigator getCurrentPosition call.
     */
    getLocation: function (pClientId, highAccuracy, maxAge, timeout) {

        ice.mobi.geolocation.clientId = pClientId;
        ice.mobi.geolocation.clearWatch();

        var geoParams = {};
        if (maxAge > 0)  {
            geoParams.maximumAge = maxAge * 1000;
        }
        if (timeout > 0)  {
            geoParams.timeout = timeout * 1000;
        }
        if (highAccuracy != 'false')  {
            geoParams.enableHighAccuracy = true;
        }
        console.log('Launching watchPosition, ' +
                'maxAge: ' + geoParams.maximumAge + '(ms),' +
                ' timeout: ' + geoParams.timeout + '(ms)' +
                ' highAccuracy: ' + geoParams.enableHighAccuracy);


        navigator.geolocation.getCurrentPosition(this.oneTimeSuccessCallback, this.errorCallback,
                                                 geoParams);

        window.addEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
    },

    successCallback: function(pos) {
        console.log('Position update for client: ' + ice.mobi.geolocation.clientId);
        try {
            inputId = ice.mobi.geolocation.clientId + "_locHidden";
            console.log('LOGGING Position TO hidden field: ' + inputId);
            ice.mobi.storeLocation(inputId, pos.coords);

        } catch(e) {
            console.log('Exception: ' + e);
        }
    },

    // Success Callback for getCurrentPosition  in that it removes deviceorientation listener
    oneTimeSuccessCallback: function(pos) {
        console.log('Position update for client: ' + ice.mobi.geolocation.clientId);
        inputId = ice.mobi.geolocation.clientId + "_locHidden";
        ice.mobi.storeLocation(inputId, pos.coords);
        clearWatch();
    },

    errorCallback: function(positionError) {
        console.log('Error in watchPosition, code: ' + positionError.code + ' Message: ' + positionError.message);
        ice.mobi.geolocation.clearWatch();
    },

    orientationCallback: function(orient) {
        inputId = ice.mobi.geolocation.clientId + "_locHidden";
        ice.mobi.storeDirection(inputId, orient);
    },

    // Clear any existing positionUpdate listeners
    clearWatch: function() {
        if (ice.mobi.geolocation.watchId > 0) {
            console.log('Existing positionWatch: ' + ice.mobi.geolocation.watchId + ' removed');
            navigator.geolocation.clearWatch(ice.mobi.geolocation.watchId);
            ice.mobi.geolocation.watchId = 0;
        }
        window.removeEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
    }
}
ice.mobi.splitpane = {
    panels: {},
    initClient: function(clientId, cfgIn) {
        if (!this.panels[clientId]){
            this.panels[clientId] = ice.mobi.splitpane.Scrollable(clientId, cfgIn);
            this.panels[clientId].resize(clientId);
        } else {
            this.panels[clientId].resize(clientId);
        }
    },
    resizeHt: function(clientId) {
        if (this.panels[clientId])
            this.panels[clientId].resize(clientId);
        else {
            this.panels[clientId].unload(clientId);
        }
    },
    Scrollable: function Scrollable(clientId, cfgIn) {
        var wrapPanel = clientId+"_wrp";
        var leftNode = document.getElementById(clientId+"_left") ;
        var rightNode = document.getElementById(clientId+"_right") ;
        var resizeCall = function(){ ice.mobi.splitpane.resizeHt(clientId);};
        //
        if (cfgIn.width){
            var width= cfgIn.width || -1;
            if (width >0 && width < 99){
                leftNode.style.width=width + "%";
                rightNode.style.width=(100-width) + "%";
            }
        }
        if (window.addEventListener) {
                window.addEventListener('resize', resizeCall, false);
            } else { // older ie event listener
                window.attachEvent("resize", resizeCall, false);
        }
        return {
           resize: function(elId){
                var height = 0;
                var leftNode = document.getElementById(elId+"_left");
                var rtNode = document.getElementById(elId+"_right");
                var splt  = document.getElementById(elId+"_splt");
                var body = window.document.body || null;
                if (body ==null) return;
                if (leftNode && rtNode){
                    if (window.innerHeight) {
                        height = window.innerHeight;
                    } else if (body.parentElement.clientHeight) {
                        height = body.parentElement.clientHeight;
                    } else if (body) {
                        if (body.clientHeight) {
                            height = body.clientHeight;
                        }
                    }
                    if (height > 0){
                        leftNode.style.height = ((height - leftNode.offsetTop) + "px");
                        rtNode.style.height = ((height - rtNode.offsetTop) + "px");
                    }
                }
           },
           unload: function(){
               if (window.addEventListener) {
                   window.removeEventListener("resize", resizeCall, false);
               }else {
                   window.detachEvent("resize", resizeCall, false);
               }
           }
        }
    }
}

/* add js marker for progressive enhancement */
document.documentElement.className = document.documentElement.className + ' js';

/* touch active state support */
document.addEventListener("touchstart", function(){}, true);


