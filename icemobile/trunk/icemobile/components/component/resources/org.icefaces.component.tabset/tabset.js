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
    function hasClass(ele,cls) {
	       return ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
    }
    function addClass(ele,cls) {
            if (!this.hasClass(ele,cls)) ele.className =cls;
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
        // set current tab
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
              updateHidden(myTabId, tabIndex);
              if (!isClient){
                    ice.se(null, myTabId);
              }
              //remove class of activetabheader and hide old contents
              var currCtrl = myTabId+"tab_"+current;
              var oldCtrl =  document.getElementById(currCtrl);
              removeClass(oldCtrl, "activeTab ");

              var navitem = el;
              navitem.setAttribute("class","activeTab");
              var contents = tabContent.childNodes;
              contents[current].style.opacity = 0;
              contents[current].style.visibility = "hidden";
              contents[tabIndex].style.opacity = 1;
              contents[tabIndex].style.visibility = "visible";
              parent.setAttribute("data-current",tabIndex);
           },
       updateProperties: function (clientId, cfgUpd) {
                cfg = cfgUpd; //not sure I need to keep this?
                //server may want to push new dynamic values ??
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

