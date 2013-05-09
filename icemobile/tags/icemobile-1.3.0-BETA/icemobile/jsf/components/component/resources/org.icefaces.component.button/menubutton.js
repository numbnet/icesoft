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
    select: function(clientId){
        var myselect = document.getElementById(clientId+'_sel');
        var myOptions = myselect.options;
        var index = myselect.selectedIndex;
        if (index==0){
            return;  //assume index of 0 is select title so no submit happens
        }
        var behaviors = myOptions[index].getAttribute('cfg');
        var singleSubmit = myOptions[index].getAttribute("singleSubmit") || null;
        var myForm = ice.formOf(document.getElementById(clientId));
        var params = myOptions[index].getAttribute("params") || null;
        var optId = myOptions[index].id || null;
        if (!optId){
            console.log(" Problem selecting items in menuButton. See docs. index = ") ;
            return;
        }
        var disabled = myOptions[index].getAttribute("disabled") || false;
        if (disabled==true){
            console.log(" option id="+optId+" is disabled no submit");
            return;
        }
        var options = {
            source: optId,
            jspForm: myForm
        };
        var cfg = {
            source: optId
        }
        var snId = myOptions[index].getAttribute("snId") || null ;
        var pcId = myOptions[index].getAttribute("pcId") || null;
        if (singleSubmit){
            options.execute="@this";
        } else {
            options.execute="@all";
        }
        if (behaviors){
            cfg.behaviors = behaviors;
        }
        if (pcId){
            if (snId){
                options.snId = snId;
            }
            options.pcId = pcId;
            ice.mobi.panelConf.init(pcId, optId, cfg, options) ;
            this.reset(myselect, index);
            return;
        }
        if (snId){
            var resetCall = function(xhr, status, args) {ice.mobi.menubutton.reset(myselect, index);};
            options.onsuccess = resetCall;
            ice.mobi.submitnotify.open(snId, optId, cfg, options);
       //     this.reset(myselect, index);
            return;
        }
        mobi.AjaxRequest(options);
        this.reset(myselect, index);
    },
    reset: function reset(myselect, index) {
        console.log("RESET");
        myselect.options[index].selected = false;
        myselect.options[0].selected=true;
       //     myselect.options.index = 0;

    }
};