ice.ace.Tree = function (cfg) {
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(cfg.id);
    this.behaviors = cfg.behaviors;

    // Selectors
    this.expansionButtonDeselector = this.jqId + " * .if-tree * .if-node-sw";
    this.selectionTargetDeselector = this.jqId + " * .if-tree *";
    this.expansionButtonSelector = this.jqId + " .if-node-sw:not("+this.expansionButtonDeselector+")";
    this.selectionTargetSelector = this.jqId + " :not("+this.selectionTargetDeselector+")";

    // Setup events
    // Expansion
    this.tearDownExpansion();
    this.setupExpansion();
    // Selection
    this.tearDownSelection();
    this.setupSelection();
}

ice.ace.Tree.prototype.tearDownExpansion = function() {
    ice.ace.jq(this.jqId).off('click', this.expansionButtonSelector);
}

ice.ace.Tree.prototype.setupExpansion = function() {
    var self = this;
    ice.ace.jq(this.jqId).on('click', this.expansionButtonSelector, function (event) {
        var container = ice.ace.jq(this),
            icon = container.find('> span'),
            expanded = icon.is('.ui-icon-minus'),
            node = container.closest('.if-node-cnt');

        //if (self.cfg.activeMode) {
            if (expanded)
                self.sendNodeContractionRequest(node);
            else
                self.sendNodeExpansionRequest(node);
        //} else {
        //    if (expanded)
        //        self.doClientContraction(node);
        //    else
        //        self.doClientExpansion(node);
        //}
    });
}

ice.ace.Tree.prototype.tearDownSelection = function() {

}

ice.ace.Tree.prototype.setupSelection = function() {

}

ice.ace.Tree.prototype.doClientContraction = function() {

}

ice.ace.Tree.prototype.doClientExpansion = function() {

}

ice.ace.Tree.prototype.sendNodeContractionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    var params = {};

    params[this.cfg.id + '_contract'] = [this.getNodeKey(node)];

    options.params =  params;

    if (this.cfg.behaviors && this.cfg.behaviors['contract']) {
        ice.ace.ab(ice.ace.extendAjaxArguments(
                this.cfg.behaviors['contract'],
                ice.ace.removeExecuteRenderOptions(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
}

ice.ace.Tree.prototype.sendNodeExpansionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    var params = {};

    params[this.cfg.id + '_expand'] = [this.getNodeKey(node)];

    options.params =  params;

    if (this.cfg.behaviors && this.cfg.behaviors['expand']) {
        ice.ace.ab(ice.ace.extendAjaxArguments(
                this.cfg.behaviors['expand'],
                ice.ace.removeExecuteRenderOptions(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
}

ice.ace.Tree.prototype.getNodeKey = function(node) {
    var startStr = this.cfg.id + ':-:',
        endStr = ':-',
        id = node.attr('id');

    var startIndex = id.indexOf(startStr) + startStr.length,
        endIndex = id.indexOf(endStr, startIndex);

    return id.substring(startIndex, endIndex);
}