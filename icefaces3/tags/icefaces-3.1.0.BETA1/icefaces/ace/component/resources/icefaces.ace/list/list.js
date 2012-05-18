if (!window.ice['ace']) {
    window.ice.ace = {};
}

ice.ace.Lists = {};

ice.ace.List = function(id, cfg) {
    var self = this;
    this.id = id;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.cfg = cfg;
    this.sep = String.fromCharCode(this.cfg.separator);
    this.element = ice.ace.jq(this.jqId);
    this.behaviors = cfg.behaviors;

    // global list of list objects, used by a component
    // to rebuild widget lists following an inserting update.
    ice.ace.Lists[id] = self;

    // Setup drag wrapped drag events
    this.appStartHandler = this.cfg.start;
    cfg.start = function(event, ui) {
        self.dragFromHandler.call(self, event, ui);
        if (self.appStartHandler)
            return self.appStartHandler(event, ui);
        return true;
    };

    this.appStopHandler = this.cfg.stop;
    cfg.stop = function(event, ui) {
        self.dragToHandler.call(self, event, ui);
        if (self.appStopHandler)
            return self.appStopHandler.call(self, event, ui);
        return true;
    };

    this.appRecieveHandler = this.cfg.receive;
    cfg.receive = function(event, ui) {
        self.itemReceiveHandler.call(self, event, ui);
        if (self.appRecieveHandler)
            return self.appRecieveHandler.call(self, event, ui);
        return true;
    };

    if (cfg.selection)
        this.setupSelection();

    if (cfg.controls)
        this.setupControls();

    if (cfg.dragging)
        this.element.find("ul:first").sortable(cfg);
};

// ************************************************************************* //
// List Features //
// ************************************************************************* //

ice.ace.List.prototype.itemReceiveHandler = function(event, ui) {
    var item = ui.item,
        id = item.attr('id'),
        fromIndex = parseInt(id.substr(id.lastIndexOf(this.sep)+1)),
        srcId = ui.sender.closest('.if-list').attr('id'),
        src = ice.ace.Lists[srcId];

    fromIndex = src.getUnshiftedIndex(
            src.element.find('.if-list-body:first').children().length,
            src.read('reorderings'),
            fromIndex);

    this.immigrantMessage = [];
    this.immigrantMessage.push(srcId);
    this.immigrantMessage.push([[fromIndex , item.index()]]);

    this.sendMigrateRequest();
};

ice.ace.List.prototype.sendMigrateRequest = function() {
    var destList = this,
        sourceListId = destList.immigrantMessage[0],
        sourceList = ice.ace.Lists[sourceListId],
        options = {
            source: destList.id,
            execute: destList.id + " " + sourceListId,
            render: destList.id + " " + sourceListId
        };

    var params = {};
    params[sourceListId+'_emigration'] = destList.id;
    params[destList.id+'_immigration'] = JSON.stringify(destList.immigrantMessage);
    options.params = params;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });

        destList.element = ice.ace.jq(ice.ace.escapeClientId(destList.element.attr('id')));
        if (destList.cfg.dragging) destList.element.find("ul:first").sortable(destList.cfg);

        sourceList.element = ice.ace.jq(ice.ace.escapeClientId(sourceList.element.attr('id')));
        if (sourceList.cfg.dragging) sourceList.element.find("ul:first").sortable(sourceList.cfg);

        return true;
    };

    if (this.behaviors)
        if (this.behaviors.migrate) {
            ice.ace.ab(ice.ace.extendAjaxArguments(this.behaviors.migrate, options));
            return;
        }

    ice.ace.AjaxRequest(options);
};

ice.ace.List.prototype.dragFromHandler = function(event, ui) {
    this.startIndex = ui.item.index();
};

ice.ace.List.prototype.dragToHandler = function(event, ui) {
    // If moving in list
    if (ui.item.parents(this.jqId).length > 0) {
        var swapRecords = this.read('reorderings'),
            recordStart = swapRecords.length,
            index = ui.item.index(),
            lower = (index > this.startIndex),
            to = ui.item,
            from = lower ? to.prev() : to.next();

        if (index != this.startIndex) do {
            var record = [];
            record.push(from.index());
            record.push(to.index());

            swapRecords.splice(recordStart,0,record);
            this.swapIdPrefix(from, to);

            to = from;
            from = lower ? to.prev() : to.next();
        } while (to.index() != this.startIndex);

        this.write('reorderings', swapRecords);

        if (this.behaviors)
            if (this.behaviors.move)
                ice.ace.ab(this.behaviors.move);
    }
    // Migrating between lists handled by new item insertion handler, not this drop handler
};

ice.ace.List.prototype.setupControls = function() {
    var self = this;

    this.element.find('.if-list-ctrls:first .if-list-ctrl')
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

ice.ace.List.prototype.controlClickHandler = function(e) {
    var ctrl = e.currentTarget,
        jqCtrl = ice.ace.jq(ctrl),
        dir;

    if (!ice.ace.jq.browser.msie || ice.ace.jq.browser.version == 9) {
        jqCtrl.toggleClass('ui-state-active', 50)
            .toggleClass('ui-state-active', 50);
    }

    if (jqCtrl.hasClass('if-list-ctrl-top'))
        dir = "top";
    else if (jqCtrl.hasClass('if-list-ctrl-up'))
        dir = "up";
    else if (jqCtrl.hasClass('if-list-ctrl-dwn'))
        dir = "dwn";
    else if (jqCtrl.hasClass('if-list-ctrl-btm'))
        dir = "btm";

    this.moveItems(dir);
};

ice.ace.List.prototype.setupSelection = function() {
    var self = this;

    ice.ace.jq(document)
            .off('mouseenter mouseleave click', this.jqId + ' ul:first > li')
            .on('mouseenter', this.jqId + ' ul:first > li', this.itemHover)
            .on('mouseleave', this.jqId + ' ul:first > li', this.itemHover)
            .on('click', this.jqId + ' ul:first > li', function(e) { self.itemClickHandler.call(self, e); });
};

ice.ace.List.prototype.itemHover = function(e) {
    // hover cb event
    var li = e.currentTarget;
    ice.ace.jq(li).toggleClass('ui-state-hover');
};

ice.ace.List.prototype.itemClickHandler = function(e) {
    var li = e.currentTarget,
        jqLi = ice.ace.jq(li);

    if (jqLi.hasClass('ui-state-active'))
        this.removeSelectedItem(jqLi);
    else
        this.addSelectedItem(jqLi);
    // click cb event

    // add selected item
};

ice.ace.List.prototype.getUnshiftedIndex = function(length, reorderings, index) {
    var indexes = [];
    for (var i = 0; length - i >= 0; i++) indexes.push(i);
    for (var i = 0; i < reorderings.length; i++) {
        var from = reorderings[i][0];
            to = reorderings[i][1];
            t = indexes[to];

        indexes[to] = indexes[from];
        indexes[from] = t;
    }

    return indexes[index];
};

ice.ace.List.prototype.addSelectedItem = function(item) {
    var selections = this.read('selections'),
        deselections = this.read('deselections'),
        reorderings = this.read('reorderings'),
        id = item.attr('id'),
        index = id.substr(id.lastIndexOf(this.sep)+1),
        origIndex = this.getUnshiftedIndex(item.siblings().length, reorderings, parseInt(index));

    // find connected lists and deselect all
    this.deselectConnectedLists();

    item.addClass('ui-state-active');

    deselections = ice.ace.jq.grep(deselections, function(r) { return r != index; });
    selections.push(origIndex);

    this.write('selections', selections);
    this.write('deselections', deselections);

    if (this.behaviors)
        if (this.behaviors.select)
            ice.ace.ab(this.behaviors.select);
};


ice.ace.List.prototype.deselectConnectedLists = function(list) {
    for(var controlId in ice.ace.ListControls) {
        if(ice.ace.ListControls.hasOwnProperty(controlId)) {
            var listSet = ice.ace.jq(ice.ace.ListControls[controlId].selector);
            if (listSet.is(this.element))
                listSet.not(this.element)
                        .each(function (i, elem) {
                            ice.ace.Lists[ice.ace.jq(elem).attr('id')].deselectAll();
                        });
        }
    }
}

ice.ace.List.prototype.removeSelectedItem = function(item) {
    var selections = this.read('selections'),
        deselections = this.read('deselections'),
        reorderings = this.read('reorderings'),
        id = item.attr('id'),
        index = id.substr(id.lastIndexOf(this.sep)+1),
        origIndex = this.getUnshiftedIndex(item.siblings().length, reorderings, parseInt(index));

    item.removeClass('ui-state-active');

    selections = ice.ace.jq.grep(selections, function(r) { return r != index; });
    deselections.push(origIndex);

    this.write('selections', selections);
    this.write('deselections', deselections);

    if (this.behaviors)
        if (this.behaviors.deselect)
            ice.ace.ab(this.behaviors.deselect);
};

ice.ace.List.prototype.deselectAll = function() {
    var self = this,
        reorderings = this.read('reorderings'),
        selections = this.read('selections'),
        deselections = this.read('deselections');

    this.element.find('.if-list-body:first > .if-list-item')
            .removeClass('ui-state-active').each(function(i, elem) {{
                var item = ice.ace.jq(elem),
                    id = item.attr('id'),
                    index = id.substr(id.lastIndexOf(self.sep)+1);
                deselections.push(self
                        .getUnshiftedIndex(item.siblings().length, reorderings, parseInt(index)));
            }});

    this.write('selections', []);
    this.write('deselections', deselections);
}

ice.ace.List.prototype.moveItems = function(dir) {
    var selectedItems = this.element.find('.ui-state-active');

    // do element swaps
    var swapRecords = this.read('reorderings');

    if (dir == "top") {
        for (var i = selectedItems.length-1; i >= 0; i--) {
            var item = ice.ace.jq(selectedItems[i]),
                target = item.prev();

            if (target.length > 0) do {
                var record = [];
                record.push(item.index());
                target.before(item);
                record.push(item.index());

                swapRecords.push(record);
                this.swapIdPrefix(item, target);

                target = item.prev();
            } while (target.length > 0);
        }
    }
    else if (dir == "up") {
        for (var i = 0; i < selectedItems.length; i++) {
            var item = ice.ace.jq(selectedItems[i]),
                record = [];
            record.push(item.index());

            var target = item.prev(':first');
            target.before(item);

            record.push(item.index());
            swapRecords.push(record);
            this.swapIdPrefix(item, target);
        }
    }
    else if (dir == "dwn" || dir == "down") {
        for (var i = selectedItems.length-1; i >= 0; i--) {
            var item = ice.ace.jq(selectedItems[i]),
                record = [];
            record.push(item.index());

            var target = item.next(':first');
            target.after(item);

            record.push(item.index());
            swapRecords.push(record);
            this.swapIdPrefix(item, target);
        }
    }
    else if (dir == "btm" || dir == "bottom") {
        for (var i = 0; i < selectedItems.length; i++) {
            var item = ice.ace.jq(selectedItems[i]),
                target = item.next();

            if (target.length > 0) do {
                var record = [];
                record.push(item.index());
                target.after(item);
                record.push(item.index());

                swapRecords.push(record);
                this.swapIdPrefix(item, target);

                target = item.next();
            } while (target.length > 0);
        }
    }

    // write swaps or ajax submit
    this.write('reorderings', swapRecords);

    if (this.behaviors)
        if (this.behaviors.move)
            ice.ace.ab(this.behaviors.move);
};

// Used to keep id for each child in place, so per-item updates
// occur as expected
ice.ace.List.prototype.swapIdPrefix = function(from, to) {
    var fromId = from.attr('id'),
        toId = to.attr('id'),
        fromElems = from.find('*[id^="'+fromId+'"]'),
        toElems = to.find('*[id^="'+toId+'"]');

    from.attr('id', toId);
    to.attr('id', fromId);

    for (var x = 0; x < fromElems.length; x++) {
        var i = ice.ace.jq(fromElems[x]);
        i.attr('id', i.attr('id').replace(fromId, toId));
    }

    for (var x = 0; x < toElems.length; x++) {
        var i = ice.ace.jq(toElems[x]);
        i.attr('id', i.attr('id').replace(toId, fromId));
    }
};

ice.ace.List.prototype.read = function(field) {
    var contents = this.element.find('input[name="'+this.jqId.substr(1)+'_'+field+'"]:first').attr('value');
    if (contents != "") return JSON.parse(contents);
    else return [];
};

ice.ace.List.prototype.write= function(field, data) {
    var element = this.element.find('input[name="'+this.jqId.substr(1)+'_'+field+'"]:first');
    element.attr('value', JSON.stringify(data));
};
