if (!window['mobi']) {
    window.mobi = {};
}
mobi.submitnotify = {
      visible: {},
      bgHideClass: "mobi-submitnotific-bg",
      containerClass: "mobi-submitnotific-container ",
      cfg: {},
      open: function(clientId){
            var idPanel = clientId+"_bg";
            var containerId = clientId+"_popup";
            document.getElementById(idPanel).className = this.bgHideClass;
            document.getElementById(containerId).className = this.containerClass;
      },
      close: function(clientId){
            var idPanel = clientId+"_bg" ;
            document.getElementById(idPanel).className = this.bgbHideClass+'-hide';
            document.getElementById(clientId+"_popup").className = this.containerClass+'-hide ';
      },
      unload: function(clientId){
            this.cfg[clientId] = null;
            this.visible[clientId] = null;
      }

}
