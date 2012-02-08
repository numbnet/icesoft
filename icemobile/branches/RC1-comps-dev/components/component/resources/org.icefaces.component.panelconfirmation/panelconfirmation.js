if (!window['mobi']) {
    window.mobi = {};
}
mobi.panelConf = {
      opened: {},
      cfg: {},
      caller: {},
	  init: function(clientId, callerId, cfgIn ){
          this.cfg[clientId] = cfgIn;
          this.caller[clientId] = callerId;
          var idPanel = clientId+"_bg";
          if (!document.getElementById(idPanel).className ){
             document.getElementById(idPanel).className = 'mobi-date-bg-inv';
          }
          this.open(clientId);
          this.opened[clientId]=true;
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
        },
        toggle: function(clientId){
            if (this.opened[clientId]==false){
                this.open(clientId);
            }  else {
                this.close(clientId);
            }
        },
        open: function(clientId){
            var idPanel = clientId+"_bg";
            document.getElementById(idPanel).className = "mobi-date-bg";
            document.getElementById(clientId+"_popup").className = "mobi-date-container";
            this.opened[clientId]= true;
        },
        close: function(clientId){
            var idPanel = clientId+"_bg" ;
            document.getElementById(idPanel).className = "mobi-date";
            document.getElementById(clientId+"_popup").className = "mobi-date-container-hide";
            this.opened[clientId]= false;
        },
        unload: function(clientId){
            this.cfg[clientId] = null;
            this.opened[clientId] = null;
            this.caller[clientId] = null;
        }

}