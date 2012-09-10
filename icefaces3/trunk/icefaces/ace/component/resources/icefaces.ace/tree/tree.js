ice.ace.Tree = function (cfg) {
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(cfg.id);
    this.element = ice.ace.jq(this.jqId);
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
    this.element.off('click', this.expansionButtonSelector);
}

ice.ace.Tree.prototype.setupExpansion = function() {
    var self = this;
    this.element.on('click', this.expansionButtonSelector, function (event) {
        var container = ice.ace.jq(this),
            icon = container.find('> span'),
            expanded = icon.is('.ui-icon-minus'),
            node = container.closest('.if-node-cnt');

        if (self.cfg.expansionMode == 'server') {
            if (expanded)
                self.sendNodeContractionRequest(node);
            else
                self.sendNodeExpansionRequest(node);
        } else {
            if (expanded)
                self.doClientContraction(node);
            else
                self.doClientExpansion(node);
        }
    });
}

ice.ace.Tree.prototype.tearDownSelection = function() {

}

ice.ace.Tree.prototype.setupSelection = function() {

}

ice.ace.Tree.prototype.doClientContraction = function(node) {
    var key = this.getNodeKey(node),
        record = this.read('contract'),
        icon = node.find(' > div > div > span.ui-icon'),
        sub = node.find(' > div > div.if-node-sub');

    icon.removeClass('ui-icon-minus');
    icon.addClass('ui-icon-plus')
    record.push(key);

    sub.css('display', 'none');

    this.write('contract', record);
}

ice.ace.Tree.prototype.doClientExpansion = function(node) {
    var key = this.getNodeKey(node),
        record = this.read('expand'),
        icon = node.find(' > div > div > span.ui-icon'),
        sub = node.find(' > div > div.if-node-sub');

    icon.removeClass('ui-icon-plus');
    icon.addClass('ui-icon-minus')
    record.push(key);

    sub.css('display', 'block');

    this.write('expand', record);
}

ice.ace.Tree.prototype.sendNodeContractionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.write('contract', [this.getNodeKey(node)]);

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

    this.write('expand', [this.getNodeKey(node)]);

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

ice.ace.Tree.prototype.write = function(key, val) {
    this.element.find(this.jqId+"_"+key).val(JSON.stringify(val));
}

ice.ace.Tree.prototype.read = function(key) {
    var val = this.element.find(this.jqId+"_"+key).val();
    if (val != "") return JSON.parse(val);
    else return [];
}