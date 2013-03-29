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
    visible:{},
    centerCalculation:{},
    cfg:{},
    init:function (clientId, cfgIn) {
        this.cfg[clientId] = cfgIn;
        var cfg = this.cfg[clientId];
        var visible = cfg.visible;
        var autoCenter = cfg.autocenter;
        var containerId = clientId + "_popup";

        //if nothing already in client saved state, then we use the server passed value
        if (!this.visible[clientId]) {
            this.visible[clientId] = visible;
        }

        if (this.visible[clientId]) {
            this.open(clientId);
        } else {
            this.close(clientId);
        }
    },
    // only called when in client side mode
    open:function (clientId) {
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        var cfg = this.cfg[clientId];
        var autocenter = cfg.autocenter;
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";

        document.getElementById(idPanel).className = "mobi-panelpopup-bg ";
        document.getElementById(containerId).className = "mobi-panelpopup-container ";

        if (autocenter) {
            // add scroll listener
            this.centerCalculation[clientId] = function () {
                mobi.panelAutoCenter(containerId);
            };

            if (window.addEventListener) {
                window.addEventListener(scrollEvent, this.centerCalculation[clientId], false);
                window.addEventListener('resize', this.centerCalculation[clientId], false);
            } else { // older ie event listener
                window.attachEvent(scrollEvent, this.centerCalculation[clientId]);
                window.attachEvent("resize", this.centerCalculation[clientId]);
            }
            // calculate center for first view
            mobi.panelAutoCenter(containerId);
        }

        this.visible[clientId] = true;
        this.updateHidden(clientId, true);
    },
    // only called when in client side mode
    close:function (clientId) {
        var idPanel = clientId + "_bg";
        var autocenter = this.cfg.autocenter;
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";

        document.getElementById(idPanel).className = "mobi-panelpopup-bg-hide ";
        document.getElementById(clientId + "_popup").className = "mobi-panelpopup-container-hide ";

        if (autocenter) {
            if (window.removeEventListener) {
                window.removeEventListener(scrollEvent, this.centerCalculation[clientId], false);
                window.removeEventListener('resize', this.centerCalculation[clientId], false);
            } else { // older ie cleanup
                window.detachEvent(scrollEvent, this.centerCalculation[clientId], false);
                window.detachEvent('resize', this.centerCalculation[clientId], false);
            }
            this.centerCalculation[clientId] = undefined;
        }

        this.visible[clientId] = false;
        this.updateHidden(clientId, false);
    },
    updateHidden:function (clientId, visible) {
        var hidden = document.getElementById(clientId);
        if (hidden) {
            hidden.value = visible;
        }
    },
    unload:function (clientId) {
        this.cfg[clientId] = null;
        this.visible[clientId] = null;
    }

};
