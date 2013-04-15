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
ice.mobi.PANELCONF_BG_HIDE = "mobi-panelconf-bg-hide";
ice.mobi.PANELCONF_BG = "mobi-panelconf-bg";
ice.mobi.PANELCONF_CONTAINER = "mobi-panelconf-container";
ice.mobi.PANELCONF_CONTAINER_HIDE = "mobi-panelconf-container-hide";
ice.mobi.panelConf = {
    opened:{},
    cfg:{},
    options: {},
    caller:{},
    centerCalculation:{},
    scrollEvent:{},
    init:function (clientId, callerId, cfgIn, options) {
        this.cfg[clientId] = cfgIn;
        this.caller[clientId] = callerId;
        this.options[clientId] = options;
        var idPanel = clientId + "_bg";
        var bgNode = document.getElementById(idPanel);
        ice.mobi.swapClasses(bgNode, ice.mobi.PANELCONF_BG, ice.mobi.PANELCONF_BG_HIDE);
        this.scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
        this.open(clientId);
    },
    confirm:function (clientId) {
      // does not yet support mobi ajax.  need more work on mobi ajax support first
        var callerId = this.cfg.source || this.caller[clientId];
        var snId = this.cfg[clientId].snId || this.options[clientId].snId || null;
        if (snId ==null && callerId) {
            this.close(clientId);
            mobi.AjaxRequest(this.options[clientId]);
        }
        else if (snId!=null) {
            this.close(clientId);
            ice.mobi.submitnotify.open(snId, callerId, this.cfg[clientId], this.options[clientId]);
        }
    },
    open:function (clientId) {
        var containerId = clientId + "_popup";
        var idPanel = clientId + "_bg";
        var bgNode = document.getElementById(idPanel);
        var popNode = document.getElementById(containerId);
        ice.mobi.swapClasses(bgNode, ice.mobi.PANELCONF_BG_HIDE, ice.mobi.PANELCONF_BG);
        ice.mobi.swapClasses(popNode, ice.mobi.PANELCONF_CONTAINER_HIDE, ice.mobi.PANELCONF_CONTAINER);
        // add scroll listener
        this.centerCalculation[clientId] = function () {
            mobi.panelAutoCenter(containerId);
        };
        ice.mobi.addListener(window, this.scrollEvent, this.centerCalculation[clientId]);
        ice.mobi.addListener(window, 'resize', this.centerCalculation[clientId]);
        // mark as visible
        this.opened[clientId] = true;
        // calculate center for first view
        mobi.panelAutoCenter(containerId);
    },
    close:function (clientId) {
        var idPanel = clientId + "_bg";
        var bgNode = document.getElementById(idPanel);
        var popupId = clientId+"_popup";
        var pNode = document.getElementById(popupId);
        // remove scroll listener
        ice.mobi.removeListener(window, this.scrollEvent, this.centerCalculation[clientId]);
        ice.mobi.removeListener(window, 'resize', this.centerCalculation[clientId]);
        // hide panel
        ice.mobi.swapClasses(bgNode, ice.mobi.PANELCONF_BG, ice.mobi.PANELCONF_BG_HIDE );
        ice.mobi.swapClasses(pNode, ice.mobi.PANELCONF_CONTAINER, ice.mobi.PANELCONF_CONTAINER_HIDE);
        this.opened[clientId] = false;
        this.centerCalculation[clientId] = undefined;
    },
    unload:function (clientId) {
        this.cfg[clientId] = null;
        this.opened[clientId] = null;
        this.centerCalculation[clientId]=null;
        this.caller[clientId] = null;
    }

};