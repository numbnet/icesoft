if (!window.ice['ace']) {
    window.ice.ace = {};
}

ice.ace.ListControls = {};

ice.ace.ListControl = function(id, cfg) {
    this.id = id;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.cfg = cfg;
    this.element = ice.ace.jq(this.jqId);
    this.selector = cfg.selector;
    this.sep = String.fromCharCode(this.cfg.separator);

    // global list of list control objects, used by lists to
    // find lists they share migration controls with, in order
    // to enforce mutually exclusive selection
    ice.ace.ListControls[id] = this;

    this.setupControls();
};

ice.ace.ListControl.prototype.setupControls = function(){
    var self = this, selector;

    if (this.element.hasClass('if-list-nctrls'))
        selector = '> .if-list-ctrl-spcr > .if-list-nctrl';
    else
        selector = '> .if-list-nctrls .if-list-nctrl, > .if-list-dl > .if-list-nctrls .if-list-nctrl';

    this.element.find(selector)
        .off('mouseenter').on('mouseenter', function(e) {
            var ctrl = e.currentTarget;
            ice.ace.jq(ctrl).addClass('ui-state-hover');
        })
        .off('mouseleave').on('mouseleave', function(e) {
            var ctrl = e.currentTarget;
            ice.ace.jq(ctrl).removeClass('ui-state-hover');
        })
        .off('click').on('click', function(e) { self.controlClickHandler.call(self, e); });
};


ice.ace.ListControl.prototype.refreshLists = function() {
    this.lists = ice.ace.jq(this.selector);
};

ice.ace.ListControl.prototype.controlClickHandler = function(e) {
    var ctrl = e.currentTarget,
        jqCtrl = ice.ace.jq(ctrl),
        dir,
        all = false;

    this.refreshLists();

    jqCtrl.toggleClass('ui-state-active', 50)
        .toggleClass('ui-state-active', 50);

    if (jqCtrl.hasClass('if-list-nctrl-alll')) {
        dir = "alll";
        all = true;
    }
    else if (jqCtrl.hasClass('if-list-nctrl-lft'))
        dir = "lft";
    else if (jqCtrl.hasClass('if-list-nctrl-rgt'))
        dir = "rgt";
    else if (jqCtrl.hasClass('if-list-nctrl-allr')) {
        dir = "allr";
        all = true;
    }

    var from = this.getSourceList(dir, all);
    var to = this.getDestinationList(from, dir);

    if (!to) return;

    var im = [];
    im.push(from.id);
    im.push(this.getRecords(from, to, all));

    to.immigrantMessage = im;
    to.sendMigrateRequest();
};

ice.ace.ListControl.prototype.getSourceList = function(dir, all) {
    // If we are moving all the elements and this is dual list mode
    if (all && this.element.hasClass('if-list-dl-cnt')) {
        var list;
        if (dir.substr(dir.length-1) == 'r')
            list = this.element.find('.if-list-dl-1:first .if-list:first');
        else
            list = this.element.find('.if-list-dl-2:first .if-list:first');

        return ice.ace.Lists[list.attr('id')];
    }

    // Return first list in selector that has a selected row
    return ice.ace.Lists[
        this.lists.find('.if-list-item.ui-state-active:first')
            .closest('.if-list').attr('id')
        ];
};

ice.ace.ListControl.prototype.getDestinationList = function(source, dir) {
    var sourceIndex = this.lists.index(source.element);

    if (dir == 'allr' || dir == 'rgt') {
        if (sourceIndex != (this.lists.length-1))
            return ice.ace.Lists[ice.ace.jq(this.lists[sourceIndex+1]).attr('id')];
    }
    else
        if (sourceIndex != 0)
            return ice.ace.Lists[ice.ace.jq(this.lists[sourceIndex-1]).attr('id')];

    return undefined;
};

ice.ace.ListControl.prototype.getRecords = function(source, dest, all) {
    var childSelector = all ? '*' : '.ui-state-active' ,
        sourceChildren = ice.ace.jq(source.element).find('.if-list-body:first').children(),
        sourceIds = sourceChildren.filter(childSelector).map(function() { return ice.ace.jq(this).attr('id'); }),
        sourceLength = sourceChildren.length,
        sourceReorderings = source.read('reorderings'),
        records = [],
        destIndex = ice.ace.jq(dest.element).find('.if-list-body:first').children().length;

    for (var i = 0; i < sourceIds.length; i++) {
        var record = [], id = sourceIds[i];
        record.push(source.getUnshiftedIndex(sourceLength, sourceReorderings, parseInt(id.substr(id.lastIndexOf(this.sep)+1))));
        record.push(destIndex + i);
        records.push(record);
    }

    return records;
};
