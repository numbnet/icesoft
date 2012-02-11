if (!window['mobi']) {
    window.mobi = {};
}
mobi.panelpopup = {
      visible: {},
      cfg: {},
	  init: function(clientId, cfgIn){
          this.cfg[clientId] = cfgIn;
          var visible = this.cfg[clientId].visible;
          if (visible[clientId]) {   //if nothing already in client saved state, then we use the server passed value
              visible=visible[clientId];
          }
          var hidden = clientId+"_hidden";
          var check = "false";
          if (hidden && hidden.value){
             check = hidden.value;
          }
          if (check=="true"){
            visible=true;
          }
          this.visible[clientId]  = visible;
   //       var idPanel = clientId+"_bg";
 /*         if (!document.getElementById(idPanel).className ){
             document.getElementById(idPanel).className = 'mobi-date-bg-inv';
          }  */
          if (this.visible[clientId]){
             this.open(clientId);
          }
	   },

        open: function(clientId){
            var idPanel = clientId+"_bg";
            var containerId = clientId+"_popup";
            var cfg = this.cfg[clientId];
            var autocenter = cfg.autocenter;
            if (autocenter){
               var w=window, d=document, e= d.documentElement, g=d.getElementsByTagName('body')[0];
               x = w.innerWidth||e.clientWidth||g.clientWidth, y=w.innerHeight||e.clientHeight||g.clientHeight;
               var iPanelHeight = 122;
               var iPaneWidth=138;
               var iWidth = (x/2) - (iPanelHeight);
               var iHeight = (y/2) - (iPaneWidth);
               var contDiv = document.getElementById(containerId);
               contDiv.style.position = 'absolute';  //use fixed if want panel to stay in same place while scrolling
               contDiv.style.left = iWidth+'px';
               contDiv.style.top = (g.scrollTop + iHeight)+'px';
            }
            document.getElementById(idPanel).className = "mobi-panelpopup-bg ";
            document.getElementById(clientId+"_popup").className = "mobi-panelpopup-container ";
            this.visible[clientId]= true;
            this.updateHidden(clientId, true);
        },
        close: function(clientId){
            var idPanel = clientId+"_bg" ;
            document.getElementById(idPanel).className = "mobi-panelpopup-bg-hide ";
            document.getElementById(clientId+"_popup").className = "mobi-panelpopup-container-hide ";
            this.visible[clientId]= false;
            this.updateHidden(clientId, false);
        },
        updateHidden: function(clientId, visible){
            var hiddenId = clientId+"_hidden";
            var parentId = clientId+"_popup";
            var hidden=document.getElementById(hiddenId);
            if (hidden){
                hidden.value = visible;
                return;
            } //otherwise, have to add the hidden element to the parent form
            // currently the only way to synchronize client and server is to have parent form of panelPopup
            var parent = document.getElementById(parentId);
            var form = mobi.findForm(parent);
            if (form){
                var el = document.createElement("input");
                el.type="hidden";
                el.name=hiddenId;
                el.id = hiddenId;
                el.value=visible;
                form.appendChild(el)
            }
        },
        unload: function(clientId){
            this.cfg[clientId] = null;
            this.visible[clientId] = null;
        }

}
