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
            for (i=0; i< length-1; i++){
                var isMenu = cpanes[i].getAttribute("isMenu");
                if (isMenu=="true" && singleView) {
                    if (!mobi.layoutMenu.menuId[clientId]){
                       mobi.layoutMenu.menuId[clientId] = cpanes[i].childNodes[0].id;
                    }
                    cpanes[i].className = singleMnuHidClass;
                } else {
                    cpanes[i].className = hideClass;
                }
            }
        }
   //     var viewportdim = viewport();
        var myStackId = clientId;
        var selectedPaneId = cfgIn.selectedId || null;
        var selClientId = cfgIn.selClientId || null;
        if (selClientId==null) {
            selClientId = panes[0].childNodes[0].id;
        }
        var wrpId = selClientId+ "_wrp";
        var currPane =  document.getElementById(wrpId);
        var isMenu = currPane.getAttribute("isMenu") || false;
        if (isMenu=="true"){
            this.mobi.layoutMenu.menuId[clientId] = selClientId;
        }
        if (currPane){
            currPane.className=visClass;
        }
        var prevId = wrpId;
        return {
           showContent: function( el, cfgIn) {
               if (cfgIn.selectedId == selectedPaneId){
                    return;
               }
               selectedPaneId = cfgIn.selectedId;
               var client = cfgIn.client || false;
               var singleSubmit = cfgIn.singleSubmit || false;
               var selClId = cfgIn.selClientId || null;
               var wrpId = selClId +"_wrp";
               currPane = document.getElementById(wrpId);
               var prevPane = document.getElementById(prevId);
               var wasMenu = prevPane.getAttribute("isMenu") || false;
               if (wasMenu=="true"){
                   prevPane.className= singleMnuHidClass ;
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
               prevId  = wrpId;
           },
           updateProperties: function (clientId, cfgUpd) {
             /*   if (cfgUpd.selectedId == selectedPaneId){
                    return;
                }*/
                if (cfgUpd.selClientId){
                    this.showContent(null, cfgUpd);
                }

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
