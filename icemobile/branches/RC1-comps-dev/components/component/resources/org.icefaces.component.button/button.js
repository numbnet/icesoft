/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */


if (!window['mobi']) {
    window.mobi = {};
}

mobi.button = {
    cfg: {},
    select: function(clientId, cfg){
        //if panelConf, then we want this to display and set the submitNotifyId if present
        this.cfg[clientId] = cfg;
        if (cfg.snId){
            mobi.submitnotify.open(cfg.snId);
            //if here, then no panelConfirmation as this action is responsible for submit
        }
        //otherwise, just check for behaviors, singleSubmit and go
        var singleSubmit = false;
        if (cfg.singleSubmit){
            singleSubmit=true;
        }
        var hasBehaviors = cfg.behaviors;
        if (hasBehaviors){
            singleSubmit=false; //hasBehaviors takes precedence and singlessubmit is ignored
            //show the submitNotification panel
            if (cfg.behaviors.click){
                cfg.behaviors.click();
            }
            return;
        }
        var event = cfg.elVal.event;
        var params = cfg.params;
        if (singleSubmit){
            ice.se(event, clientId, params);
        } else {
            ice.s(event, clientId, params);
        }
    }
};
