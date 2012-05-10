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
            tabContainer.style.height = height;
        } else {
             var ht =  calcMaxChildHeight(tabContent);
             tabContent.style.height =  ht+"px";
        }
        var contents = tabContent.getElementsByTagName("div");
        var newPage = contents[tabIndex];
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
              var current = parent.getAttribute("data-current");
              var currCtrl = myTabId+"tab_"+current;
              var oldCtrl =  document.getElementById(currCtrl);
              removeClass(oldCtrl, "activeTab ");
              var contents = tabContent.childNodes;
              var isClient = cfgIn.client || false;
              var oldPage = contents[current];
              oldPage.className = "mobi-tabpage-hidden";
              if (!isClient){
                    updateHidden(myTabId, cfgIn.tIndex);
                    ice.se(null, myTabId);
                  contents[cfgIn.tIndex].className="mobi-tabpage";
               } else {
                  tabIndex = cfgIn.tIndex || 0;
                  var newPage = contents[tabIndex];
                  newPage.className="mobi-tabpage";
                  parent.setAttribute("data-current",cfgIn.tIndex);
             }
              //remove class of activetabheader and hide old contents
              el.setAttribute("class","activeTab");
           },
           updateProperties: function (clientId, cfgUpd) {
                if (cfgUpd.tIndex != tabIndex){
                    var oldIdx = tabIndex;
                    tabIndex = cfgUpd.tIndex;
                    var tabsId = clientId+"_tabs";
                    var tabElem = document.getElementById(tabsId);
                    if (tabElem){
                        var lis = tabElem.getElementsByTagName("li");
                        var contents = tabContent.getElementsByTagName("div");
                        contents[tabIndex].style.opacity = 0;
                        var newPage = contents[tabIndex];
                        newPage.className = "mobi-tabpage";
                        newPage.style.opacity = 1;
                        if (cfgUpd.height && cfgUpd.height != tabContainer.style.height){
                           tabContainer.style.height = height;
                        }
                    }
                }
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

