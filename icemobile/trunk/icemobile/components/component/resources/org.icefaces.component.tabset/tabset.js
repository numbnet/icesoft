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
        var contents = tabContent.childNodes;
        var newPage = contents[tabIndex];
        newPage.className="mobi-tabpage";
        // set current tab
     //   var tabIndex = cfgIn.tIndex || 0;
        //hide two tab contents we don't need
    /*    var pages = tabContent.getElementsByTagName("div");
        for (var i = 1; i < pages.length; i++) {
            if (i==curIdx){
               pages.item(i).style.opacity="1";
            } else {
                pages.item(i).style.opacity=0;
                pages.item(i).style.visibility = "visible";
            }
        }; */
     return {
          showContent: function( el, cfgIn) {
              var parent = el.parentNode;
              if (!parent){
                  parent = el.parentElement;
              }
              var current = parent.getAttribute("data-current");
              var tabIndex = cfgIn.tIndex || 0;
              var isClient = cfgIn.client || false;
              var currCtrl = myTabId+"tab_"+current;
              var oldCtrl =  document.getElementById(currCtrl);
              var contents = tabContent.childNodes;
              updateHidden(myTabId, tabIndex);
              parent.setAttribute("data-current",tabIndex);
              if (!isClient){
                    ice.se(null, myTabId);
              } else {
                  var newPage = contents[tabIndex];
                  newPage.className="mobi-tabpage";
              //remove class of activetabheader and hide old contents
                 removeClass(oldCtrl, "activeTab ");
                 el.setAttribute("class","activeTab");
              }
              var oldPage = contents[current];
              oldPage.className = "mobi-tabpage-hidden";
        },
       updateProperties: function (clientId, cfgUpd) {
                if (cfgUpd.tIndex != tabIndex){
                    tabIndex = cfgUpd.tIndex;
                    var tabsId = clientId+"_tabs";
                    var tabElem = document.getElementById(tabsId);
                    if (tabElem){
                        var lis = tabElem.getElementsByTagName("li");
                        var contents = tabContent.childNodes;
                        var newPage = contents[tabIndex];
                        newPage.className = "mobi-tabpage";
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

