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
    initmenu: function(clientId, cfgI){
         var myselect = document.getElementById(clientId+'_sel');
         var selTitle = cfgI.selectTitle;
         var option = myselect.options[0];
         if (option && option.label!=selTitle) {
             try{
                var anoption = document.createElement("option");
                anoption.label=selTitle;
                myselect.add(new Option(selTitle, "0"), myselect.options[0]);
             } catch(e) {
                myselect.add(new Option(selTitle, "0"), 0);
             }
            myselect.options[0].selected=true;
            myselect.render;
         }
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
         var optId = myselect.options[index].id;
         var singleSubmit = false;
         var disabled = false;
         if (this.cfg[optId]){
             singleSubmit = this.cfg[optId].singleSubmit;
             disabled = this.cfg[optId].disabled;
         }
         if (index ==0)return;
         var snId =this.cfg[optId].snId || null
         if (this.cfg[optId].pcId){
            var pcId= this.cfg[optId].pcId;
            mobi.panelConf.init(pcId, optId, this.cfg[optId], singleSubmit ) ;
         }
         else if (singleSubmit){
             if (snId){
                 mobi.submitnotify.open(snId, optId, true);
             }else {
                 ice.se(null, optId);
             }
            // this.reset(myselect, index);
         } else {
             if (snId){
                 mobi.submitnotify.open(snId, optId, false,null);
             } else {
                 ice.s(null, optId);
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
