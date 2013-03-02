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
(function ($, undefined) { ice.ace.ThemeSelect = {

singleEntry: function (clientId) {
    $(function () {
        ice.ace.ThemeSelect.init(clientId);
    });
},

init: function (clientId) {
    this.escSelId = ice.ace.escapeClientId("select_" + clientId);

    $(this.escSelId).change(function (event) {
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
    ice.onElementUpdate(clientId, this.destroy);
},

destroy: function () {
    $(this.escSelId).off("change");
}

} })(ice.ace.jq);
