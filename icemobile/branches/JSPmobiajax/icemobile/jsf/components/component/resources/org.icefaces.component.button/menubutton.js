/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

ice.mobi.menubutton = {
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
        var optId = myselect.options[index].id || null;
        if (!optId){
            console.log(" Problem selecting items in menuButton. See docs.")
        }
        var singleSubmit = this.cfg[optId].singleSubmit || false;
        var disabled = this.cfg[optId].disabled || false;
        //currently menuButtonItem doesn't yet support mobi ajax.
        var options = {
            source: optId,
            render: '@all'
        };
        if (index ==0)return;
        var snId =this.cfg[optId].snId || null ;
        var pcId = this.cfg[optId].pcId || null;
        if (singleSubmit){
            options.execute="@this";
        } else {
            options.execute="@all";
        }
        if (pcId){
           ice.mobi.panelConf.init(pcId, optId, this.cfg[optId], options) ;
           return;
        }
        if (snId){
            ice.mobi.submitnotify.open(snId, optId, this.cfg[optId], options);
            return;
        }
        mobi.AjaxRequest(options);

    },
    reset: function reset(myselect, index) {
            myselect.options[index].selected = false;
            myselect.options.index = 0;

    },
    initCfg: function(clientId, optionId, cfg){
        this.cfg[optionId] = cfg;
    }

};
