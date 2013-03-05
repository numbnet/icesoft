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
(function ($, undefined) {

var ThemeSelect = ice.ace.ThemeSelect = function (clientId, cfg) {
    this.clientId = clientId;
    this.$sel = $(ice.ace.escapeClientId("select_" + clientId));

    this.$sel.change(function (event) {
            var styleSheet, option, href;
            option = $(this).children("option:selected");
            if (option.length > 0) {
                href = option.attr("data-href");
                styleSheet = $("link[href*='theme.css.jsf?ln=icefaces.ace'],link[href*='theme.css.jsf?ln=ace-']");
                if (option.val() == "none") {
                    styleSheet.remove();
                } else if (styleSheet.length > 0) {
                    styleSheet[0].href = href;
                } else {
                    $("<link type='text/css' rel='stylesheet' href='" + href + "'/>").prependTo("head");
                }
            }
        }
    ).change();
    if (cfg.behaviors) {
        ice.ace.attachBehaviors(this.$sel, cfg.behaviors);
    }
};

ThemeSelect.prototype.destroy = function () {
    var instances = this.constructor.instances;
    this.$sel.off("change");
    instances[this.clientId] = null;
    delete instances[this.clientId];
};

ThemeSelect.instances = {};

ThemeSelect.singleEntry = function (clientId, cfg) {
    $(function () {
        var instance = ThemeSelect.instances[clientId] = new ThemeSelect(clientId, cfg);
        ice.onElementUpdate(clientId, function () {
            instance.destroy();
        });
    });
};

})(ice.ace.jq);
