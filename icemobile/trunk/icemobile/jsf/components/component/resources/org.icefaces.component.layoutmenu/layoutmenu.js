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
    function viewport(){
       var e = window;
       var a = 'inner';
       if (!('innerWidth' in window)){
           a = 'client';
           e = document.documentElement || document.body;
       }
       return { width : e[ a+'Width' ] , height : e[ a+'Height' ]
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
              // var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');
           ele.className=" ";
       }
    }


    //-------------------------------------
    function LayoutMenu(clientId, cfgIn) {
    //    var mymenu = clientId;
        var singleVisClass = "mobi-contentpane-single";
        var singleHidClass = "mobi-contentpane-single-hidden";
        var nonSingleVisClass = "mobi-contentpane";
        var nonSingleHidClass = "mobi-contentpane-hidden";
        var singleMnuHidClass = "mobi-contentpane-single-menu-hidden";
        var visClass = nonSingleVisClass;
        var hideClass = nonSingleHidClass;
        if (cfgIn.single){
             visClass = singleVisClass;
             hideClass = singleHidClass;
        }
        var menuId = cfgIn.home || null;
        var singleView = cfgIn.single || false;
        var panes = document.getElementById(clientId+"_panes") || null;
        if (panes!=null){
            var cpanes = panes.children;
            var length = cpanes.length;
            for (i=0; i< length; i++){
                var isMenu = cpanes[i].getAttribute("isMenu");
                if (isMenu == true && singleView) {
                    mobi.layoutMenu.menuId[clientId] = cpanes[i].id;
                    cpanes[i].className=singleMnuHidClass;
                } else {
                    cpanes[i].className = hideClass;
                }
            }
        }
   //     var viewportdim = viewport();
        var myStackId = clientId;
        var myStack = document.getElementById(myStackId); //renderer does error checking
        var selectedPaneId = cfgIn.selectedId || null;
        var selClientId = cfgIn.selClientId || null;
        if (selClientId==null) {
            selClientId = myStack.getElementByTagName("div")[0].id;
        }
        var currPane =  document.getElementById(selClientId);
        var isMenu = currPane.getAttribute("isMenu") || false;
        if (isMenu){
            this.mobi.layoutMenu.menuId[clientId] = currPane.id;
        }
        if (currPane){
            currPane.className=visClass;
         //   currPane.setAttribute("isMenu", isMenu);
        }
        var prevId = selClientId;
        return {
           showContent: function( el, cfgIn) {
               if (cfgIn.selectedId == selectedPaneId){
                    return;
               }
               selectedPaneId = cfgIn.selectedId;
                //remove old   selected class on element and add new
               if (el !=null) {
                   el.className = "current";
               }
               var client = cfgIn.client || false;
               var singleSubmit = cfgIn.singleSubmit || false;
               var isSingle = cfgIn.single || false;
               var selClId = cfgIn.selClientId || null;
               currPane = document.getElementById(selClId);
               var prevPane = document.getElementById(prevId);
               var wasMenu = prevPane.getAttribute("isMenu") || false;
               if (wasMenu == true){
                   prevPane.className= singleMnuHidClass
               } else {
                   prevPane.className =  hideClass;
               }
               if (!client || selClId ==null){
                   updateHidden(myStackId, selectedPaneId);
                   if (singleSubmit){
                       //look for a form, for ice.s
                       ice.se(null, myStackId);
                   }
                   else {
                       //look for form and do full submit. ???
                   }
               }
               currPane.className=visClass;
               prevId  = selClId;
           },
           updateProperties: function (clientId, cfgUpd) {
                if (cfgUpd.selectedId == selectedPaneId){
                    return;
                }
                if (cfgUpd.selClientId){
                    var elem = document.getElementById(selClientId);
                    this.showContent(null, cfgUpd);
                }

           },
           showPrevious: function(clientId,cfgUpd){
                alert('show previous content');
           },
           showMenu: function(clientId, cfg){
               if (cfg.selClientId !=null){
                   this.showContent(null,cfg);
               }
           }
        }
    }
    mobi.layoutMenu = {
        last5Selected: {},
        menus: {},
        menuId: {},
        initClient: function(clientId, cfg) {
            if (!this.menus[clientId]){
                this.menus[clientId] = LayoutMenu(clientId, cfg);
            } else {
                this.menus[clientId].updateProperties(clientId, cfg);
            }
        },
        showContent: function(clientId, el, cfgIn){
            if (this.menus[clientId]){
                this.menus[clientId].showContent(el, cfgIn);
            }
        },
        showMenu: function(clientId, cfgIn){
            if (this.menus[clientId]){
                cfgIn.selClientId = this.menuId[clientId];
                this.menus[clientId].showMenu(clientId, cfgIn);
            }
        }
    }

  })();
