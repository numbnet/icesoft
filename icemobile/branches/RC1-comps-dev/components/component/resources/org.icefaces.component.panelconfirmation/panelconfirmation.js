if (!window['mobi']) {
    window.mobi = {};
}
mobi.panelConf = {
      opened: {},
      cfg: {},
      caller: {},
      autocenter: {},
	  init: function(clientId, callerId, center, cfgIn ){
          this.cfg[clientId] = cfgIn;
          this.caller[clientId] = callerId;
          this.autocenter[clientId] = center;
          var idPanel = clientId+"_bg";
          if (!document.getElementById(idPanel).className ){
             document.getElementById(idPanel).className = 'mobi-panelconf-bg-hide';
          }
          this.open(clientId);
	   },
        confirm: function(clientId){
            var event = this.cfg.event;
            var hasBehaviors = false;
            var behaviors = this.cfg[clientId].behaviors;
            if (behaviors){
                hasBehaviors = true;
            }
            if (hasBehaviors){
                if (behaviors.click){
                    behaviors.click();
                }
            }
            if (!hasBehaviors){
                var callerId = this.caller[clientId];
                if (callerId){
                    ice.s(event, callerId);
                }
            }
            this.close(clientId);
            if (this.cfg[clientId].snId){
                var snId = this.cfg[clientId].snId;
                mobi.submitnotify.open(snId);
            }
        },
        open: function(clientId){
            var containerId = clientId+"_popup";
            if (this.autocenter[clientId]){
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
            var idPanel = clientId+"_bg";
            document.getElementById(idPanel).className = "mobi-panelconf-bg";
            document.getElementById(containerId).className = "mobi-panelconf-container";
            this.opened[clientId]= true;
        },
        close: function(clientId){
            var idPanel = clientId+"_bg" ;
            document.getElementById(idPanel).className = "mobi-panelconf-bg-hide";
            document.getElementById(clientId+"_popup").className = "mobi-panelconf-container-hide";
            this.opened[clientId]= false;
        },
        unload: function(clientId){
            this.cfg[clientId] = null;
            this.opened[clientId] = null;
            this.caller[clientId] = null;
        }

}