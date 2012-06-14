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

mobi.menubutton = {
    cfg: {},
    initmenu: function(clientId){
         var myselect = document.getElementById(clientId+'_sel');
         var anoption = document.createElement("option");
         anoption.label="Select";
         try{
            myselect.add(new Option("Select", "0"), myselect.options[0]);
         } catch(e) {
             myselect.add(new Option("Select", "0"), 0);
         }
        myselect.options[0].selected=true;
        myselect.render;
    },
    select: function(clientId){
         var myselect = document.getElementById(clientId+'_sel');
         var index = 0;
         for (var i=1; i<myselect.options.length; i++){
             if (myselect.options[i].selected==true){
                index = i;
                break
             }
         }
    //     var textA = myselect.options[index].text;
         var optId = myselect.options[index].id;
         var singleSubmit = false;
         var disabled = false;
         if (this.cfg[optId]){
             singleSubmit = this.cfg[optId].singleSubmit;
             disabled = this.cfg[optId].disabled;
         }
         if (index ==0)return;
         var snId;
         if (this.cfg[optId].snId){
             snId = this.cfg[optId].snId;
         }
         if (this.cfg[optId].pcId){
            var pcId= this.cfg[optId].pcId;
            mobi.panelConf.init(pcId, optId, true, this.cfg[optId] ) ;
         }
         else if (singleSubmit){
             ice.se(null, optId);
             if (snId){
                 mobi.submitnotify.init(snId);
             }
            // this.reset(myselect, index);
         } else {
             ice.s(null, optId);
             if (snId){
                 mobi.submitnotify.init(snId);
             }
         }

    },
    reset: function reset(myselect, index) {
            myselect.options[index].selected = false;
            myselect.options.index = 0;

    },
    initCfg: function(clientId, optionId, cfg){
        this.cfg[optionId] = cfg;
    }

};
