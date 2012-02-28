/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
if (!window['mobi']) {
    window.mobi = {};
}
mobi.panelpopup = {
      visible: {},
      cfg: {},
	  init: function(clientId, cfgIn){
          this.cfg[clientId] = cfgIn;
          var visible = false;
          if (this.cfg[clientId].visible) {   //if nothing already in client saved state, then we use the server passed value
              visible = this.visible[clientId];
          } else {
             visible = this.visible[clientId];
          }
          if (visible){
             this.open(clientId);
          } else {
              if (this.visible[clientId]){
                  this.close[clientId];
              }
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
            var hiddenId = clientId;
            var hidden=document.getElementById(hiddenId);
            if (hidden){
                hidden.value = visible;
                return;
            }
        },
        unload: function(clientId){
            this.cfg[clientId] = null;
            this.visible[clientId] = null;
        }

}
