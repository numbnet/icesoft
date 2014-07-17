/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
ice.mobi.submitnotify = {
    visible:{},
    bgClass:"mobi-submitnotific-bg",
    bgHideClass: "mobi-submitnotific-bg-hide",
    containerClass:"mobi-submitnotific-container",
    contHideClass: "mobi-submitnotific-container-hide",
    centerCalculation:{},
    cfg:{},
    open: function (clientId, callerId, cfg, options) {
        //console.log("submitNotif OPEN for clientId="+clientId);
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        var behaviors = cfg.behaviors && cfg.behaviors.click || null;
        var bgNode = document.getElementById(idPanel);
        var pNode = document.getElementById(containerId);
        ice.mobi.swapClasses(bgNode, this.bgHideClass, this.bgClass);
        ice.mobi.swapClasses(pNode, this.contHideClass, this.containerClass);
        // apply centering code.
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
        // add scroll listener
        this.centerCalculation[clientId] = function () {
            mobi.panelAutoCenter(containerId);
        };
        ice.mobi.addListener(window, scrollEvent, this.centerCalculation[clientId]);
        ice.mobi.addListener(window, 'resize', this.centerCalculation[clientId]);
        // calculate center for first view
        mobi.panelAutoCenter(containerId);
        var closeCall = function(xhr, status, args) {ice.mobi.submitnotify.close(clientId);};
        options.oncomplete = closeCall;
      //  var keyCall = function(xhr, status, args) {ice.mobi.button.unSelect(callerId);};
        if (behaviors){
             if (!cfg.source)cfg.source = callerId;
             ice.mobi.ab(ice.mobi.extendAjaxArguments(behaviors, options));
        }else {
             mobi.AjaxRequest(options);
        }
    },
    close:function (clientId) {
        var idPanel = clientId + "_bg";
        var containerId = clientId + "_popup";
        var bgNode = document.getElementById(idPanel);
        var pNode = document.getElementById(containerId);
        if (bgNode==null || pNode == null){
            return;
        }
        var contains = (bgNode.className.indexOf(this.bgHideClass)>-1) ;
        if (!contains){
            ice.mobi.swapClasses(bgNode, this.bgClass, this.bgHideClass);
        }
        var contains2 = (pNode.className.indexOf(this.contHideClass)>-1) ;
        if (!contains2){
            ice.mobi.swapClasses(pNode, this.containerClass, this.contHideClass);
        }
        // clean up centering listeners.
        var scrollEvent = 'ontouchstart' in window ? "touchmove" : "scroll";
        ice.mobi.removeListener(window, scrollEvent, this.centerCalculation[clientId]);
        ice.mobi.removeListener(window, 'resize', this.centerCalculation[clientId]);
        this.centerCalculation[clientId] = undefined;
    }

}
