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

    if (cfg.dblclk_migrate)
        this.setupClickMigration();

    if (cfg.controls)
        this.setupControls();

    if (cfg.dragging)
        this.element.find("> ul").sortable(cfg);
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
            src.element.find('> ul').children().length,
            src.read('reorderings'),
            fromIndex);

    this.immigrantMessage = [];
    this.immigrantMessage.push(srcId);
    this.immigrantMessage.push([[fromIndex , item.index()]]);

    this.element.find('> ul > li').removeClass('if-list-last-clicked');
    src.element.find('> ul > li').removeClass('if-list-last-clicked');

    // Deselect all in connected lists but the currently
    // dragged item.
    if (src.cfg.selection) {
        this.deselectConnectedLists();
        this.deselectAll(item);
        src.addSelectedItem(item, fromIndex);
    }

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

    options.onsuccess = function(responseXML) {;
        destList.element = ice.ace.jq(ice.ace.escapeClientId(destList.element.attr('id')));
        if (destList.cfg.dragging) destList.element.find("> ul").sortable(destList.cfg);

        sourceList.element = ice.ace.jq(ice.ace.escapeClientId(sourceList.element.attr('id')));
        if (sourceList.cfg.dragging) sourceList.element.find("> ul").sortable(sourceList.cfg);

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

    // Align FF and IE with Webkit, produce a mouseout event
    // on the dropped item if 100ms post drop it has been aligned
    // out from under our cursor.
    var item = ui.item,
        self = this;

    setTimeout(function () {
        var ie = ice.ace.jq.browser.msie && (ice.ace.jq.browser.version == 8 || ice.ace.jq.browser.version == 7);
        if (!ie && !ui.item.is(':hover')) self.itemLeave({currentTarget : item});
    }, 100);

    /*
        For an odd reason jq.closest() returns no results incorrectly here for some IDs.
        I have the feeling it doesn't process the escaping in the selector for the ':' character correctly */
    if (ui.item.parents(this.jqId).length > 0) {
        var swapRecords = this.read('reorderings'),
            recordStart = swapRecords.length,
            index = ui.item.index(),
            lower = (index > this.startIndex),
            to = ui.item,
            from = lower ? to.prev() : to.next(),
            id = item.attr('id'),
            idIndex = parseInt(id.substr(id.lastIndexOf(this.sep)+1));

        idIndex = this.getUnshiftedIndex(
                        this.element.find('> ul').children().length,
                        this.read('reorderings'),
                        idIndex);

        if (index != this.startIndex) do {
            if (this.cfg.selection) {
                this.deselectAll();
                this.deselectConnectedLists();
                this.addSelectedItem(item, idIndex);
            }

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

    this.element.find('> div.if-list-ctrls .if-list-ctrl')
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
    var self = this,
        selector = this.jqId +  ' > ul > li';

    ice.ace.jq(this.element)
            .off('mouseenter mouseleave click', selector)
            .on('mouseenter', selector, this.itemEnter)
            .on('mouseleave', selector, this.itemLeave)
            .on('click', selector, function(e) { self.itemClickHandler.call(self, e); });
};

ice.ace.List.prototype.setupClickMigration = function() {
    var self = this,
        selector = this.jqId +  ' > ul > li';

    ice.ace.jq(this.element)
            .off('dblclick', selector)
            .on('dblclick', selector, function(e) { self.itemDoubleClickHandler.call(self, e); });
}

ice.ace.List.prototype.itemEnter = function(e) {
    ice.ace.jq(e.currentTarget).addClass('ui-state-hover');
};

ice.ace.List.prototype.itemLeave = function(e) {
    ice.ace.jq(e.currentTarget).removeClass('ui-state-hover');
};

ice.ace.List.prototype.itemDoubleClickHandler = function(e) {
    var item = ice.ace.jq(e.currentTarget),
        id = item.attr('id'),
        fromIndex = parseInt(id.substr(id.lastIndexOf(this.sep)+1));
        to = this.getSiblingList(e.shiftKey);

    fromIndex = this.getUnshiftedIndex(
            this.element.find('> ul').children().length,
            this.read('reorderings'),
            fromIndex);

    to.immigrantMessage = [];
    to.immigrantMessage.push(this.id);
    to.immigrantMessage.push([[fromIndex , to.element.find('> ul').children().length]]);

    this.element.find('> ul > li').removeClass('if-list-last-clicked');
    to.element.find('> ul > li').removeClass('if-list-last-clicked');

    if (this.cfg.selection) {
        to.deselectConnectedLists();
        to.deselectAll();
        this.addSelectedItem(item, fromIndex);
    }

    to.sendMigrateRequest();
}

/* Get the following (or if shift is held, previous) list in the first
   listControl binding that associates this list with another */
ice.ace.List.prototype.getSiblingList = function (shift) {
    for(var controlId in ice.ace.ListControls) {
        if(ice.ace.ListControls.hasOwnProperty(controlId)) {
            var listSet = ice.ace.jq(ice.ace.ListControls[controlId].selector),
                listContainer = this.element.parent().parent(),
                lastSibling = (shift || listContainer.hasClass('if-list-dl-2')),
                listIndex = listSet.index(this.element);

            if (listIndex < 0) continue;

            listIndex = lastSibling ? listSet.index(this.element)-1 : listSet.index(this.element)+1;

            if ((!lastSibling && listIndex >= listSet.length) || (lastSibling && listIndex < 0))
                return undefined;

            if (listIndex >= 0)
                return ice.ace.Lists[ice.ace.jq(listSet[listIndex]).attr('id')];
        }
    }
}

ice.ace.List.prototype.pendingClickHandling;

ice.ace.List.prototype.itemClickHandler = function(e) {
    if (this.pendingClickHandling == undefined) {
        var li = e.currentTarget,
            jqLi = ice.ace.jq(li),
            self = this,
            timeout = this.cfg.dblclk_migrate ? 250 : 0;

        this.pendingClickHandling =
            setTimeout(function () {
                // Clear double click monitor token
                self.pendingClickHandling = undefined;

                var index = jqLi.index();

                // find connected lists and deselect all
                self.deselectConnectedLists();

                if (e.shiftKey && self.cfg.selection != "single") {
                    // Clear selection from shift key use
                    self.clearSelection();

                    var lower, higher, last_clicked = jqLi.siblings('.if-list-last-clicked').index();
                    if (last_clicked < index) {
                        lower = last_clicked + 1;
                        higher = index + 1;
                    } else {
                        lower = index;
                        higher = last_clicked;
                    }

                    jqLi.parent().children().slice(lower, higher).filter(":not(.ui-state-active)").each(function () {
                        self.addSelectedItem(ice.ace.jq(this));
                    });
                }
                else {
                    var deselection = jqLi.hasClass('ui-state-active');

                    if (!(e.metaKey || e.ctrlKey) || self.cfg.selection == "single")
                        self.deselectAll();

                    if (deselection) {
                        jqLi.addClass('if-list-last-clicked').siblings().removeClass('if-list-last-clicked');
                        self.removeSelectedItem(jqLi);
                    } else {
                        jqLi.addClass('if-list-last-clicked').siblings().removeClass('if-list-last-clicked');
                        self.addSelectedItem(jqLi);
                    }
                }
            }, timeout);
    } else {
        clearTimeout(this.pendingClickHandling);
        this.pendingClickHandling = undefined;
    }
};

/* Determines the original index of an item at a particular index */
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

ice.ace.List.prototype.addSelectedItem = function(item, inputIndex) {
    if (!item.hasClass('ui-state-active')) {
        var selections = this.read('selections'),
            deselections = this.read('deselections'),
            reorderings = this.read('reorderings'),
            id = item.attr('id'),
            index;

        if (inputIndex) index = inputIndex;
        else {
            index = id.substr(id.lastIndexOf(this.sep)+1),
            index = this.getUnshiftedIndex(item.siblings().length, reorderings, parseInt(index));
        }

        item.addClass('ui-state-active');

        deselections = ice.ace.jq.grep(deselections, function(r) { return r != index; });
        selections.push(index);

        this.write('selections', selections);
        this.write('deselections', deselections);

        if (this.behaviors)
            if (this.behaviors.select)
                ice.ace.ab(this.behaviors.select);
    }
};


ice.ace.List.prototype.deselectConnectedLists = function() {
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
    if (item.hasClass('ui-state-active')) {
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
    }
};

ice.ace.List.prototype.deselectAll = function(except) {
    var self = this,
        reorderings = this.read('reorderings'),
        selections = this.read('selections'),
        deselections = this.read('deselections');

    this.element.find('> ul.if-list-body > li.if-list-item.ui-state-active').each(function(i, elem) {{
                if (except != undefined && except.is(elem)) return;

                var item = ice.ace.jq(elem),
                    id = item.attr('id'),
                    index = parseInt(id.substr(id.lastIndexOf(self.sep)+1));

                item.removeClass('ui-state-active');

                if (index != undefined)
                    index = self.getUnshiftedIndex(item.parent().children().length, reorderings, index);

                if (index != undefined) {
                    deselections.push(index);
                    selections = ice.ace.jq.grep(selections, function(r) { return r != index; });
                }
            }});

    this.write('selections', selections);
    this.write('deselections', deselections);

    if (this.behaviors && !isNaN(deselections.length) && deselections.length > 0)
        if (this.behaviors.deselect)
            ice.ace.ab(this.behaviors.deselect);
}

ice.ace.List.prototype.moveItems = function(dir) {
    var selectedItems = this.element.find('.ui-state-active');

    if (selectedItems.length > 0) {
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
    }
};

// Used to keep id for each child in place, so per-item updates
// occur as expected
ice.ace.List.prototype.swapIdPrefix = function(from, to) {
    if (from.length == 0 || to.length == 0) return;

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
    var contents = this.element.find(' > input[name="'+this.jqId.substr(1)+'_'+field+'"]').attr('value');
    if (contents != "") return JSON.parse(contents);
    else return [];
};

ice.ace.List.prototype.write= function(field, data) {
    var element = this.element.find(' > input[name="'+this.jqId.substr(1)+'_'+field+'"]');
    element.attr('value', JSON.stringify(data));
};

ice.ace.List.prototype.clearSelection = function() {
    if (window.getSelection) {
        if (window.getSelection().empty) {  // Chrome
            window.getSelection().empty();
        } else if (window.getSelection().removeAllRanges) {  // Firefox
            window.getSelection().removeAllRanges();
        }
    } else if (document.selection) {  // IE?
        document.selection.empty();
    }
}
