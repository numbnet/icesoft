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
        if (0 === formAction.indexOf("/")) {
            uploadURL = window.location.origin + formAction;
        } else if ((0 === formAction.indexOf("http://")) ||
                (0 === formAction.indexOf("https://"))) {
            uploadURL = formAction;
        } else {
            uploadURL = baseURL + formAction;
        }
    }
    else {
        uploadURL += '/';
    }


    var returnURL = window.location;
    if ("" == returnURL.hash) {
        var wloc = "" + returnURL;
        var lastHash = wloc.lastIndexOf("#");
        if (lastHash > 0) {
            returnURL = wloc.substring(0, lastHash);
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

ice.formOf = function formOf(element) {
    var parent = element;
    while (null != parent) {
        if ("form" == parent.nodeName.toLowerCase()) {
            return parent;
        }
        parent = parent.parentNode;
    }
}
if (!window['ice.mobi']) {
    window.ice.mobi = {};
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
}


ice.mobi.carousel = {
    acarousel: null,
    loaded: function (clientId) {
        var carouselId = clientId + '_carousel';
        //carousel iscroll loading
        setTimeout(function () {
            if( !document.getElementById(clientId)){
                return;
            }
            if (this.acarousel) {
                ice.mobi.carousel.refresh(clientId);
            }
            else {
                this.acarousels = new iScroll(carouselId, {
                    snap: 'li',
                    momentum: false,
                    hScrollbar: false,
                    onScrollEnd: function () {
                        ice.mobi.carousel.scrollUpdate(clientId, this.currPageX);
                    }
                });
            }
        }, 100);
    },
    unloaded: function(clientId) {
        if (this.acarousel != null) {
            this.acarousel.destroy();
            this.acarousel = null;
        }
    },

    scrollUpdate: function(clientId, pageVal) {
        //only update if different than last one.
        var hidden = document.getElementById(clientId + '_hidden');
        var changedVal = false;
        if (hidden) {

            var temp = hidden.value;
            if (temp != pageVal) {
                changedVal = true;
                hidden.value = pageVal;
//                     'old hidden='+temp+ ' updated to hidden.value = '+hidden.value);
                document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
                document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (pageVal + 1) + ')').className = 'active';
            }
        }
    },

    refresh: function(clientId) {
        if (this.acarousel) {
//               ice.log.debug(ice.log, "  have a carousel to refresh from hidden value");
            var currPageX = 0;
            var hidden = document.getElementById(clientId + "_hidden");
            if (hidden) {
                currPageX = hidden.value;
            }
            //if this.current is different from hidden, then scroll to hidden value.
            this.acarousel.scrollToPage(currPageX);
            document.querySelector('.mobi-carousel-cursor-list > li.active').className = '';
            document.querySelector('.mobi-carousel-cursor-list > li:nth-child(' + (this.currPageX + 1) + ')').className = 'active';
            setTimeout(function() {
                if( !document.getElementById(clientId)){
                    return;
                }
                this.acarousel.refresh();

            }, 0);

        }

        if (!this.acarousel) {
            //    ice.log.debug(ice.log, "REFRESH HAS NO OBJECT FOR CAROUSEL clientId="+clientId+' ss -'+singleSubmit);
            this.acarousel = null;
            this.loaded(clientId);
        }
    }
}


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
}

ice.mobi.accordionController = {

    // Panels wont ever have more than one panel in it, since it's a single accordian.
    panels: {},
    initClient: function(clientId, cfg) {
        this.panels[clientId] = ice.mobi.accordionController.Accordion(clientId, cfg);
    },
    toggleClient: function(clientId, el, cachetyp) {
        if (this.panels[clientId]) {
            this.panels[clientId].toggle(el, cachetyp);
        }
    },

    //functions that do not encapsulate any state, they just work with the provided parameters
    //and globally accessible variables
    //---------------------------------------
    updateHidden: function (clientId, value) {
        var hidden = document.getElementById(clientId + "_hidden");
        if (hidden) {
            hidden.value = value;
        }
    },

    getDivHeight: function (clientId) {
        return document.getElementById(clientId).innerHeight;
    },

    updateHeightInOpenClass: function (ruleName, height) {

        if (document.styleSheets) {
            for (var i = 0; i < document.styleSheets.length; i++) {
                var styleSh = document.styleSheets[i];
                var index = 0;
                var found = false;
                var cssRule;
                do {
                    if (styleSh.cssRules) {
                        cssRule = styleSh.cssRules[index];
                    }
                    else {
                        cssRule = styleSh.rules[index];
                    }
                    // Not every cssRule had a selectorText, which was
                    // throwing an exception
                    if (cssRule && cssRule.selectorText) {
                        if (cssRule.selectorText.toLowerCase() == ruleName) {
                            //update height and maxheight
                            cssRule.style.maxHeight = height;
                            cssRule.style.height = height;
                            break;
                        }
                    }
                    index ++;
                } while (cssRule);
            }
        }
    },
    calcMaxDivHeight: function (clientId) {
        var accord = document.getElementById(clientId);
        var mxht = 0;
        //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
        var children = document.getElementById(clientId).getElementsByTagName('section');
        for (var i = 0; i < children.length; i++) {
            if (children[i].scrollHeight > mxht) {
                mxht = children[i].scrollHeight;
            }
        }
        return mxht;
    },

    //declare functions who creates object with methods that have access to the local variables of the function
    //so in effect the returned object can operate on the local state declared in the function ...
    //think about them as object fields in Java, also gone is the chore of copying the constructor parameters into fields
    //-------------------------------------
    Accordion: function Accordion(clientId, cfgIn) {
        //local variables are not public but public open, close, visible and updateProperties functions can operate on them
        var theContainer = document.getElementById(clientId);
        var openClass = ".mobi-accordion .open";
        var myclient = clientId;
        var alreadyOpenId = theContainer.getAttribute('data-opened'); //do I care about this?
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
        var cgfObj = cfgIn;
        var autoheight = cfgIn.autoheight || false;
        var maxheight = cfgIn.maxheight || "200";
        if (autoheight == true) {
            maxheight = ice.mobi.accordionController.calcMaxDivHeight(clientId);
        }
        var heightString = maxheight + "px";
        ice.mobi.accordionController.updateHeightInOpenClass(openClass, heightString);
        //as you can see all the 'this' noise is gone
        //object properly encapsulates state
        return {
            toggle: function(el, cached) {
                var theParent = el.parentElement;
                if (!theParent) {
                    theParent = el.parentNode; //mozilla
                }
                //which child is the element?
                ice.mobi.accordionController.updateHidden(myclient, theParent.id);
//                if (!cached){
//                    ice.se(null, myclient);
//                }
                var alreadyOpen = theContainer.getAttribute('data-opened');
                if (alreadyOpen && alreadyOpen !== theParent.id) {
                    var openedEl = document.getElementById(alreadyOpen);
                    if (openedEl) {
                        document.getElementById(alreadyOpen).className = 'closed';
                    }
                }
                if ('open' === theParent.className) {
                    theParent.className = 'closed';
                } else {
                    theParent.className = 'open';
                    theContainer.setAttribute('data-opened', theParent.id);
                }

            },

            updateProperties: function (clientId, cfgUpd) {
                cfgObj = cfgIn; //not sure I need to keep this?
                //server may want to push new dynamic values for maxheight and autoheight
                var change = false;
                if (autoheight != cfgUpd.autoheight) {
                    autoheight = cfgUpd.autoheight || false;
                    change = true;
                }
                if (maxheight != cfgUpd.maxheight) {
                    maxheight = cfgUpd.maxheight || "200px";
                    change = true;
                }
                if (change == true) {
                    var newHeight = ice.mobi.accordionController.calcMaxDivHeight(clientId);
                }
            }
        }
    }
}

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
    console.log('__ StoreDirection! __');
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
        // It seems like on Android (at least) that passing any argument at all
        // for enableHighAccuracy enables high accuracy.
        if (highAccuracy == 'false') {
            console.log('Launching low precision watchPosition, maxAge: ' +
                    maxAge + '(s), timeout: ' + timeout + '(s)');

            ice.mobi.geolocation.watchId = navigator.geolocation.watchPosition(
                    this.successCallback, this.errorCallback,
                    { maximumAge: maxAge * 1000, timeout: timeout * 1000 }
            );

        } else {
            console.log('Launching HIGH precision watchPosition, maxAge: ' +
                    maxAge + '(s), timeout: ' + timeout + '(s)');
            ice.mobi.geolocation.watchId = navigator.geolocation.watchPosition(
                    this.successCallback, this.errorCallback,
                    { enableHighAccuracy: true, maximumAge: maxAge * 1000, timeout: timeout * 1000 }
            );
        }
        window.addEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
        console.log('Lauching positionWatch for client: ' + pClientId + ' watchId: ' +
                ice.mobi.geolocation.watchId + ', highAccuracy? : ' + highAccuracy);
    },

    /**
     * Perform a single call to the navigator getCurrentPosition call.
     */
    getLocation: function (pClientId, highAccuracy, maxAge, timeout) {

        ice.mobi.geolocation.clientId = pClientId;
        ice.mobi.geolocation.clearWatch();

        console.log('Launching getCurrentPosition');
        if (highAccuracy == 'false') {
            console.log('Launching low precision getCurrentPosition, maxAge: ' +
                    maxAge + '(s), timeout: ' + timeout + '(s)');

            navigator.geolocation.getCurrentPosition(this.successCallback, this.errorCallback,
                    { maximumAge: maxAge, timeout: timeout });
        } else {
            console.log('Launching HIGH precision getCurrentPosition, maxAge: ' +
                    maxAge + '(s), timeout: ' + timeout + '(s)');
            ice.mobi.geolocation.watchId = navigator.geolocation.getCurrentPosition(
                    this.oneTimeSuccessCallback, this.errorCallback,
                    { enableHighAccuracy: true, maximumAge: maxAge * 1000, timeout: timeout * 1000 }
            );
        }
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
         // should put if the node is not found in the document, remove??
        if (this.panels[clientId])
            this.panels[clientId].resize(clientId);
        else {
            this.panels[clientId].unload(clientid);
        }
    },
    resizeElementHeight: function(elId){
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
            var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
            var supportsOrientationChange = "onorientationchange" in window,
                orientationEvent = supportsOrientationChange ? "orientationchange" : "resize";
            if (window.addEventListener) {
                    window.addEventListener(orientationEvent, resizeCall, false);
                    window.addEventListener('resize', resizeCall, false);
                } else { // older ie event listener
                    window.attachEvent(orientationEvent, resizeCall, false);
                    window.attachEvent("resize", resizeCall, false);
            }
                   }
        return {
           resize: function(clientId){
                ice.mobi.splitpane.resizeElementHeight(clientId);
           },
           unload: function(clientId){
               //remove listeners and set object back to empty
               if (window.addEventListener) {
                   window.removeEventListener(orientationEvent, resizeCall, false);
                   window.removeEventListener("resize", resizeCall, false);
               }else {
                   window.detachEvent(orientationEvent, resizeCall, false);
                   window.detachEvent("resize", resizeCall, false);
               }
           }
        }
    }
}

/* add js marker for progressive enhancement */
document.documentElement.className = document.documentElement.className + ' js';

