ice.ace.TextEntry = function(id, cfg) {
    var jQ = ice.ace.jq;
    var inputId = id + "_input";
    var labelName = id + "_label";
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(inputId);
    this.jq = jQ(this.jqId);

    this.jq.keyup(
        function(e) {
//            console.log("value: ", this.value);
            var maxLength = this.maxLength;
//            console.log("maxLength:", maxLength);
            if (jQ.isNumeric(maxLength) && maxLength > 0 && this.value.length >= maxLength) {
                var fields = jQ(this).parents('form:eq(0),body').find(':input').not('[type=hidden]');
                var index = fields.index(this);
                if (index > -1 && ( index + 1 ) < fields.length) {
                    fields.eq(index + 1).focus();
                }
            }
        }
    );
    if (cfg.embeddedLabel) {
        this.jq.focus(
            function() {
                var input = jQ(this);
                if (input.attr("name") == labelName) {
                    input.attr({name: inputId, value: ""});
                    input.removeClass("ui-embedded-label");
                }
            }).blur(
            function() {
                var input = jQ(this);
                if (jQ.trim(input.val()) == "") {
                    input.attr({name: labelName, value: cfg.embeddedLabel});
                    input.addClass("ui-embedded-label");
                }
//                setFocus();
            });
    }

    this.jq.blur(function() { setFocus(); });
    //Client behaviors
    if(this.cfg.behaviors) {
        ice.ace.attachBehaviors(this.jq, this.cfg.behaviors);
    }

    //Visuals
//    if (this.cfg.theme != false) {
//        ice.ace.skinInput(this.jq);
//    }
};