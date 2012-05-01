ice.ace.TextEntry = function(id, cfg) {
    var jQ = ice.ace.jq;
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jq = jQ(this.jqId);
    var promptLabelName = id + "_promptLabel";

    if(this.cfg.theme != false) {
        ice.ace.skinInput(this.jq);
    }

    this.jq.focus(
        function() {
            var input = jQ(this);
            if (input.attr("name") == promptLabelName) {
                input.attr({name: id, value: ""});
                input.removeClass("ui-prompt-label");
            }
        }).blur(
        function() {
            var input = jQ(this);
            if (jQ.trim(input.val()) == "") {
                input.attr({name: promptLabelName, value: cfg.promptLabel});
                input.addClass("ui-prompt-label");
            }
            setFocus();
        });

    if(this.cfg.behaviors) {
        ice.ace.attachBehaviors(this.jq, this.cfg.behaviors);
    }
};