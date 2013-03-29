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
mobi.panelConf = {
    opened:{},
    cfg:{},
    caller:{},
    centerCalculation:{},
    scrollEvent:{},
    init:function (clientId, callerId, cfgIn) {
        this.cfg[clientId] = cfgIn;
        this.caller[clientId] = callerId;
        var idPanel = clientId + "_bg";
        if (!document.getElementById(idPanel).className) {
            document.getElementById(idPanel).className = 'mobi-panelconf-bg-hide';
        }
        this.scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";

        this.open(clientId);

    },
    confirm:function (clientId) {
        var event = this.cfg.event;
        var callerId = this.caller[clientId];
        var snId = this.cfg[clientId].snId || null;
        if (snId ==null && callerId) {
            ice.s(event, callerId);
            this.close(clientId);
        }
        else if (snId!=null) {
            this.close(clientId);
            mobi.submitnotify.open(snId, callerId, false, null);
        }
    },
    open:function (clientId) {
        var containerId = clientId + "_popup";
        var idPanel = clientId + "_bg";

        document.getElementById(idPanel).className = "mobi-panelconf-bg";
        document.getElementById(containerId).className = "mobi-panelconf-container";

        // add scroll listener
        this.centerCalculation[clientId] = function () {
            mobi.panelAutoCenter(containerId);
        };

        if (window.addEventListener) {
            window.addEventListener(this.scrollEvent, this.centerCalculation[clientId], false);
            window.addEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie event listener
            window.attachEvent(this.scrollEvent, this.centerCalculation[clientId]);
            window.attachEvent("resize", this.centerCalculation[clientId]);
        }
        // mark as visible
        this.opened[clientId] = true;
        // calculate center for first view
        mobi.panelAutoCenter(containerId);
    },
    close:function (clientId) {
        var idPanel = clientId + "_bg";

        // remove scroll listener
        if (window.removeEventListener) {
            window.removeEventListener(this.scrollEvent, this.centerCalculation[clientId], false);
            window.removeEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie cleanup
            window.detachEvent(this.scrollEvent, this.centerCalculation[clientId], false);
            window.detachEvent('resize', this.centerCalculation[clientId], false);
        }
        // hide panel
        document.getElementById(idPanel).className = "mobi-panelconf-bg-hide";
        document.getElementById(clientId + "_popup").className = "mobi-panelconf-container-hide";
        this.opened[clientId] = false;
        this.centerCalculation[clientId] = undefined;
    },
    unload:function (clientId) {
        this.cfg[clientId] = null;
        this.opened[clientId] = null;
        this.caller[clientId] = null;
    }

};