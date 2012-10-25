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

    function updateHeightInOpenClass(ruleName, height) {
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
                    if (cssRule) {
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
    }

    function calcMaxDivHeight(clientId) {
        var accord = document.getElementById(clientId);
        var mxht = 0;
        //find all sections of the clientId and calc height.  set maxheight and height to max height of the divs
        var children = document.getElementById(clientId).getElementsByTagName('section');
        for (var i = 0; i < children.length; i++) {
            if (children[i].scrollHeight > mxht) {
                mxht = children[i].scrollHeight;
            }
        }
        if (mxht <= 33) {
            mxht = accord.clientHeight - 33;
        }
        return mxht;
    }

    //declare functions who creates object with methods that have access to the local variables of the function
    //so in effect the returned object can operate on the local state declared in the function ...
    //think about them as object fields in Java, also gone is the chore of copying the constructor parameters into fields
    //-------------------------------------
    function Accordion(clientId, cfgIn) {
        //local variables are not public but public open, close, visible and updateProperties functions can operate on them
        var theContainer = document.getElementById(clientId);
        var openClass = ".mobi-accordion .open";
        var myclient = clientId;
        var paneOpId = cfgIn.opened || null;
        var accordRoot = document.getElementById(clientId);
        if (accordRoot.hasChildNodes()){
            paneOpId = accordRoot.firstChild.id;
        } else {
            //  var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
            var autoheight = cfgIn.autoheight || false;
            var fixedHeight = cfgIn.fixedHeight || "200px";
            if (autoheight == true) {
                var calcht = calcMaxDivHeight(clientId)
                if (calcht > 0) {
                    fixedHeight = calcht;
                }
            }
            updateHeightInOpenClass(openClass, fixedHeight);
        }
        return {
            toggle: function(el, cached) {
                var theParent = el.parentElement;
                if (!theParent) {
                    theParent = el.parentNode; //mozilla
                }
                //which child is the element?
                updateHidden(myclient, theParent.id);
                if (!cached) {
                    ice.se(null, myclient);
                }
                var alreadyOpen = paneOpId;
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
                    paneOpId = theParent.id;
                }

            },
            updateProperties: function (clientId, cfgUpd) {
                cfgObj = cfgIn; //not sure I need to keep this?
                //server may want to push new dynamic values for fixedHeight and autoheight
                var change = false;
                if (autoheight != cfgUpd.autoheight) {
                    autoheight = cfgUpd.autoheight || false;
                    change = true;
                }
                if (fixedHeight != cfgUpd.fixedHeight) {
                    fixedHeight = cfgUpd.fixedHeight || "200px";
                    change = true;
                }
                if (change == true) {
                    var newHeight = calcMaxDivHeight(clientId);
                }
                if( !paneOpId ){
                    paneOpId = cfgIn.opened || null;
                }

            }
        }
    }

    ice.mobi.accordionController = {
        panels: {},
        initClient: function(clientId, cfg) {
            if (!this.panels[clientId]) {
                this.panels[clientId] = Accordion(clientId, cfg);
            } else {
                this.panels[clientId].updateProperties(clientId, cfg);
            }
        },
        toggleClient: function(clientId, el, cachetyp) {
            if (this.panels[clientId]) {
                this.panels[clientId].toggle(el, cachetyp);
            } else {
                this.initClient(clientId, {});
                this.panels[clientId].toggle(el, cachetyp);
            }
        },
        toggleMenu: function(clientId, el){
            if (this.panels[clientId]) {
                this.panels[clientId].toggle(el, true);
            } else{
                this.initClient(clientId, {autoheight:false});
                this.panels[clientId].toggle(el, true);
            }
        }
    }

})();

