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
ice.ace.TextEntry = function(id, cfg) {
    var jQ = ice.ace.jq;
    var inputId = id + "_input";
    var labelName = id + "_label";
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id) + " input.ui-textentry";
    this.jq = jQ(this.jqId);

    if (cfg.autoTab) {
        this.jq.keypress(
            function(e) {
                var curLength = this.value.length + 1, maxLength = this.maxLength;
                var nextTabElement = ice.ace.findNextTabElement(this);
                /*
                 console.log("id: ", this.id);
                 console.log("value: ", this.value);
                 console.log("value.length: ", this.value.length);
                 console.log("maxLength: ", maxLength);
                 console.log("charCode: ", e.charCode);
                 console.log("keyCode: ", e.keyCode);
                 console.log("which: ", e.which);
                 //            console.dir(e);
                 */
                if (curLength < maxLength || !nextTabElement) {
                    return;
                }
                if (e.which < 32 || e.charCode == 0 || e.ctrlKey || e.altKey) {
                    return;
                }
                e.preventDefault();
                if (curLength == maxLength) {
                    this.value += String.fromCharCode(e.which);
                }
                /*
                 console.log("value: ", this.value);
                 console.log("value.length: ", this.value.length);
                 */
                nextTabElement.focus();
            }
        );
    }
    if (cfg.embeddedLabel) {
        this.jq.focus(
            function() {
                var input = jQ(this);
                if (input.attr("name") == labelName) {
                    input.attr({name: inputId, value: ""});
                    input.removeClass("ui-input-label-infield");
                }
            }).blur(
            function() {
                var input = jQ(this);
                if (jQ.trim(input.val()) == "") {
                    input.attr({name: labelName, value: cfg.embeddedLabel});
                    input.addClass("ui-input-label-infield");
                }
            });
    }
    this.jq.blur(function() {
        setFocus();
    });
    if (this.cfg.behaviors) {
        ice.ace.attachBehaviors(this.jq, this.cfg.behaviors);
    }
};