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


    //-------------------------------------
    function LayoutMenu(clientId, cfgIn) {
    //    var mymenu = clientId;
        var singleVisClass = "mobi-contentpane-single";
        var singleHidClass = "mobi-contentpane-single-hidden";
        var nonSingleVisClass = "mobi-contentpane";
        var nonSingleHidClass = "mobi-contentpane-hidden";
        var singleLeftHidClass = "mobi-contentpane-single-menu-hidden";
        var visClass = nonSingleVisClass;
        var hideClass = nonSingleHidClass;
        if (cfgIn.single){
             visClass = singleVisClass;
             hideClass = singleHidClass;
        }
        var singleView = cfgIn.single || false;
        var panes = document.getElementById(clientId+"_panes") || null;
        if (panes!=null){
            var cpanes = panes.children;
            var length = cpanes.length;
            for (i=0; i< length-1; i++){
           //     var dir = cpanes[i].getAttribute("dir");
                var iStr = i+'';
                cpanes[i].setAttribute('order', iStr);
                //after remove the singleView and let it slide for a test
                if ( i==0 && singleView) { //assume first panel is always menu or home
                    cpanes[i].className = singleLeftHidClass;
                } else {
                    cpanes[i].className = hideClass;
                }
            }
        }
        var myStackId = clientId;
        var selectedPaneId = cfgIn.selectedId || null;
        var selClientId = cfgIn.selClientId || null;
        if (selClientId==null) {
            selClientId = panes[0].childNodes[0].id;
        }
        var wrpId = selClientId+ "_wrp";
        var currPane =  document.getElementById(wrpId);
        if (currPane){
            currPane.className=visClass;
        }
        var prevId = wrpId;
        return {
           showContent: function(event, cfgIn) {
               if (cfgIn.selectedId == selectedPaneId){
                    return;
               }
               var item = cfgIn.item || myStackId;
               selectedPaneId = cfgIn.selectedId;
               var client = cfgIn.client || false;
               var singleSubmit = cfgIn.singleSubmit || false;
               var selClId = cfgIn.selClientId || null;
               var wrpId = selClId +"_wrp";
               currPane = document.getElementById(wrpId);
               var prevPane = document.getElementById(prevId);
               var oldOrd = prevPane.getAttribute("order");
               var newOrd = currPane.getAttribute("order");
               if (singleView && oldOrd<newOrd){
                   prevPane.className= singleLeftHidClass ;
               } else {
                   prevPane.className =  hideClass;
               }
               if (!client || selClId ==null){
  /*                 var hiddenId = myStackId + '_hidden';
                   updateHidden(myStackId, selectedPaneId);*/
                   if (singleSubmit){
                      try{
                       ice.se(event,item);
                      }catch(err){
                          ice.log.debug(ice.log, 'error message='+err.message);
                      }
                   }
                   else {
                       ice.log.debug(ice.log, ' no implementation for full submit at this time');
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
        }
    }

  })();
