ice.ace.Tree = function (cfg) {
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(cfg.id);
    this.element = ice.ace.jq(this.jqId);
    this.behaviors = cfg.behaviors;

    // Selectors
    this.expansionButtonDeselector = this.jqId + " * .if-tree * .if-node-sw, noexp";
    this.selectionTargetDeselector = this.jqId + " * .if-tree * .if-node, noselect";
    this.expansionButtonSelector = this.jqId + " .if-node-sw:not("+this.expansionButtonDeselector+")";
    this.selectionTargetSelector = this.jqId + " .if-node:not("+this.selectionTargetDeselector+")";

    // Setup events
    // Expansion
    if (this.cfg.expansion) {
        this.tearDownExpansion();
        this.setupExpansion();
    }

    // Selection
    if (this.cfg.selection) {
        this.tearDownSelection();
        this.setupSelection();
    }

    // Cleanup
    if (!window[this.cfg.widgetVar]) {
        var self = this;
        ice.onElementUpdate(this.id, function() { self.unload(); });
    }
}

ice.ace.Tree.prototype.unload = function() {
    this.tearDownSelection();
    this.tearDownExpansion();
}

ice.ace.Tree.prototype.tearDownExpansion = function() {
    this.element.off('click', this.expansionButtonSelector);
}

ice.ace.Tree.prototype.setupExpansion = function() {
    var self = this;
    this.element.on('click', this.expansionButtonSelector, function (event) {
        var container = ice.ace.jq(this),
            icon = container.find('> div > span.ui-icon'),
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
    this.element
            .off('click', this.selectionTargetSelector)
            .off('mouseenter', this.selectionTargetSelector)
            .off('mouseleave', this.selectionTargetSelector);
}

ice.ace.Tree.prototype.setupSelection = function() {
    var self = this;
    this.element.on('mouseenter', this.selectionTargetSelector, function(event) {
        var tar = ice.ace.jq(this),
            wrap = tar.find('> div.if-node-wrp'),
            selected = wrap.is('.ui-state-active');

        if (!selected) wrap.addClass('ui-state-hover');
    });

    this.element.on('mouseleave', this.selectionTargetSelector, function(event) {
        var tar = ice.ace.jq(this),
            wrap = tar.find('> div.if-node-wrp');

        wrap.removeClass('ui-state-hover');
    });

    this.element.on('click', this.selectionTargetSelector, function(event) {
        var tar = ice.ace.jq(this),
            wrap = tar.find('> div.if-node-wrp'),
            selected = wrap.is('.ui-state-active'),
            node = tar.closest('.if-node-cnt');

        if (self.cfg.selectionMode == 'server') {
            if (selected)
                self.sendNodeDeselectionRequest(node);
            else
                self.sendNodeSelectionRequest(node);
        } else {
            if (selected)
                self.doClientDeselection(node, wrap);
            else
                self.doClientSelection(node, wrap);
        }
    });
}

ice.ace.Tree.prototype.sendNodeDeselectionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.append('deselect', this.getNodeKey(node));

    if (this.cfg.behaviors && this.cfg.behaviors['deselect']) {
        ice.ace.ab(ice.ace.extendAjaxArguments(
                this.cfg.behaviors['deselect'],
                ice.ace.removeExecuteRenderOptions(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
}

ice.ace.Tree.prototype.sendNodeSelectionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.append('select', this.getNodeKey(node));

    if (this.cfg.behaviors && this.cfg.behaviors['select']) {
        ice.ace.ab(ice.ace.extendAjaxArguments(
                this.cfg.behaviors['select'],
                ice.ace.removeExecuteRenderOptions(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
}

ice.ace.Tree.prototype.doClientDeselection = function(node, wrap) {
    var key = this.getNodeKey(node);

    wrap.removeClass('ui-state-active');

    this.append('deselect', key);
    this.remove('select', key);
}

ice.ace.Tree.prototype.doClientSelection = function(node, wrap) {
    var key = this.getNodeKey(node);

    wrap.addClass('ui-state-active');

    this.append('select', key);
    this.remove('deselect', key);
}

ice.ace.Tree.prototype.doClientContraction = function(node) {
    var key = this.getNodeKey(node),
        icon = node.find(' > div > div.if-node-sw > div > span.ui-icon'),
        sub = node.find(' > div > div.if-node-sub');

    icon.removeClass('ui-icon-minus');
    icon.addClass('ui-icon-plus')

    sub.css('display', 'none');

    this.append('contract', key);
    this.remove('expand', key);
}

ice.ace.Tree.prototype.doClientExpansion = function(node) {
    var key = this.getNodeKey(node),
        icon = node.find(' > div > div.if-node-sw > div  > span.ui-icon'),
        sub = node.find(' > div > div.if-node-sub');

    icon.removeClass('ui-icon-plus');
    icon.addClass('ui-icon-minus')

    sub.css('display', 'block');

    this.append('expand', key);
    this.remove('contract', key);
}

ice.ace.Tree.prototype.sendNodeContractionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.append('contract', this.getNodeKey(node));

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

    this.append('expand', this.getNodeKey(node));

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

ice.ace.Tree.prototype.append = function(key, val) {
    var arr = this.read(key);
    arr.push(val);
    this.write(key, arr);
}

ice.ace.Tree.prototype.remove = function(key, val) {
    this.write(key, ice.ace.jq.grep(this.read(key),
        function (o) {
            return o != val;
        }
    ));
}