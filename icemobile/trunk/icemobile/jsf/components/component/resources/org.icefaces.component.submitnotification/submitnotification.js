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
mobi.submitnotify = {
    visible:{},
    bgClass:"mobi-submitnotific-bg",
    containerClass:"mobi-submitnotific-container",
    centerCalculation:{},
    cfg:{},
    open:function (clientId, callerId, singleSubmit, params) {
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        document.getElementById(idPanel).className = 'mobi-submitnotific-bg';
        document.getElementById(containerId).className = 'mobi-submitnotific-container';
        // apply centering code.
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
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
        var cfg = {};
        cfg.source = callerId;
        cfg.execute = "@all";
        cfg.render = "@all";
        if (singleSubmit){
            cfg.execute="@this";
        }
        if (params !=null){
            cfg.params = params;
        }
      //  var closeCall = function(xhr, status, args) {alert('close');mobi.submitnotify.close(clientId);};
        var closeCall = function(xhr, status, args) {mobi.submitnotify.close(clientId);};
        cfg.oncomplete = closeCall;
        cfg.onsuccess = closeCall;
        mobi.AjaxRequest(cfg);
    },
    close:function (clientId) {
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        document.getElementById(idPanel).className = 'mobi-submitnotific-bg-hide ';
        document.getElementById(containerId).className = 'mobi-submitnotific-container-hide ';
        // clean up centering listeners.
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
        if (window.removeEventListener) {
            window.removeEventListener(scrollEvent, this.centerCalculation[clientId], false);
            window.removeEventListener('resize', this.centerCalculation[clientId], false);
        } else { // older ie cleanup
            window.detachEvent(scrollEvent, this.centerCalculation[clientId], false);
            window.detachEvent('resize', this.centerCalculation[clientId], false);
        }
        this.centerCalculation[clientId] = undefined;
    }

}
