/*
* Original Code developed and contributed by Prime Technology.
* Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
*
* Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
*
* Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
* Contributors: ICEsoft Technologies Canada Corp. (c)
*
* Code Modification 2: Improved Scrollable DataTable Column Sizing - ICE-7028
* Contributors: Nils Lundquist
*
* Code Modification 3: Added CustomUpdate Param - Fixed DomDiff - ICE-6950
* Contributors: Nils Lundquist
*
* Code Modification 4: Added Keyboard Navigation
* Contributors: Nils Lundquist
*
* Code Modification 5: Row Deselection Tracking
* Contributors: Nils Lundquist
*
*/
/**
 * DataTable Widget
 */
if (!window.ice['ace']) {
    window.ice.ace = {};
}
ice.ace.DataTable = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.sortOrder = [];
    this.tbody = this.jqId + '_data';

    if (this.cfg.paginator) this.setupPaginator();
    this.setupSortEvents(); // shouldn't this have a switch like the other configurations?


    if (this.cfg.selectionMode || this.cfg.columnSelectionMode) {
        this.selectionHolder = this.jqId + '_selection';
        this.deselectionHolder = this.jqId + '_deselection';
        var preselection = $(this.jqId + '_selection').val();
        this.selection = (preselection == "") ? [] : preselection.split(',');
        this.deselection = [];
        this.setupSelectionEvents();
    }

    if (this.cfg.panelExpansion) this.setupPanelExpansionEvents();
    if (this.cfg.rowExpansion) this.setupRowExpansionEvents();
    if (this.cfg.scrollable) this.setupScrolling();
    var rowEditors = this.getRowEditors();
    if (rowEditors.length > 0) this.setupCellEditorEvents(rowEditors);
    if (this.cfg.resizableColumns) this.setupResizableColumns();
    if (this.cfg.reorderableColumns) {
        this.reorderStart = 0;
        this.reorderEnd = 0;
        this.setupReorderableColumns();
    }
}
ice.ace.DataTable.prototype.setupPaginator = function() {
    var paginator = this.getPaginator();

    paginator.subscribe('changeRequest', this.paginate, null, this);
    paginator.render();
}

getOpacity = function(elem) {
    var ori = $(elem).css('opacity');
    var ori2 = $(elem).css('filter');
    if (ori2) {
        ori2 = parseInt( ori2.replace(')','').replace('alpha(opacity=','') ) / 100;
        if (!isNaN(ori2) && ori2 != '') {
            ori = ori2;
        }
    }
    return ori;
}

ice.ace.DataTable.prototype.setupSortEvents = function() {
    var _self = this;
    $(this.jqId + ' th.ui-sortable-column .ui-sortable-control')
            .click(function(event, altY, altMeta) {
                var $this = $(this),
                    topCarat = $this.find(".ui-icon-triangle-1-n")[0],
                    bottomCarat = $this.find(".ui-icon-triangle-1-s")[0],
                    headerCell =  $this.parent().parent(),
                    controlOffset = $this.offset(),
                    controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22,
                    descending = false,
                    metaKey = (altMeta == undefined) ? event.metaKey : altMeta;
                // altY and altMeta allow these event parameters to be optionally passed in
                // from an event triggering this event artificially
                var eventY = (altY == undefined) ? event.pageY : altY;
                if (eventY > (controlOffset.top + (controlHeight / 2)+3))
                    descending = true;

                // TODO: add toggle on single-sort mode
                if (!metaKey || _self.cfg.singleSort) {
                    // Remake sort criteria
                    // Reset all other arrows
                    _self.sortOrder = [];
                    $this.closest('.ui-header-column').siblings().find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s').animate({opacity : .2},  200).removeClass('ui-toggled');
                    headerCell.parent().siblings().find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s').animate({opacity : .2},  200).removeClass('ui-toggled');
                    if (!_self.cfg.singleSort) {
                        // Handle stacked cell case
                        $this.closest('.ui-header-column').siblings().find('.ui-sortable-column-order').html('&#160;');
                        // Handle sibling cell case
                        headerCell.parent().siblings().find('.ui-sortable-column-order').html('&#160;');
                    }

                    // remove previous gradients
                    //headerCell.siblings().removeClass('ui-state-active').find('.ui-sortable-column-icon').removeClass('ui-icon-triangle-1-n ui-icon-triangle-1-s');
                }

                var cellFound = false;
                $(_self.sortOrder).each(function() { if (headerCell.attr('id') === this.attr('id')) { cellFound = true; } });
                if (metaKey && cellFound) {
                    if ((getOpacity(topCarat) == 1 && !descending) ||
                        (getOpacity(bottomCarat) == 1 && descending)) {
                        // Remove from sort order
                        _self.sortOrder.splice(headerCell.find('.ui-sortable-column-order').html()-1,1);
                        $(bottomCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                        $(topCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                        if (!_self.cfg.singleSort) {
                            headerCell.find('.ui-sortable-column-order').html('&#160;');
                            var i = 0;
                            $(_self.sortOrder).each(function(){
                               this.find('.ui-sortable-column-order').html(parseInt(i++)+1);
                            });
                        }
                    } else {
                        // Not a deselect, just a meta-toggle
                        if (descending) {
                            $(bottomCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                            $(topCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                        } else {
                            $(topCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                            $(bottomCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                        }
                    }
                } else {
                    // add header gradient
                    //headerCell.addClass('ui-state-active');

                    if (descending) {
                        $(bottomCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                        $(topCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                    } else {
                        $(topCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                        $(bottomCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                    }

                    // add to sort order
                    var cellFound = false;
                    $(_self.sortOrder).each(function() { if (headerCell.attr('id') === this.attr('id')) { cellFound = true; } });
                    if (cellFound == false) {
                        var order = _self.sortOrder.push(headerCell);
                        // write control display value
                        if (!_self.cfg.singleSort) $this.find('.ui-sortable-column-order').html(order);
                    }
                }
                // submit sort info
                _self.sort(_self.sortOrder);
            });
    // Pre-fade and bind keypress to kb-navigable sort icons
    $(this.jqId + ' th.ui-sortable-column .ui-sortable-control')
            .find('.ui-icon-triangle-1-n')
            .fadeTo(0, 0.2)
            .keypress(function(event) {
                if (event.which == 32 || event.which == 13) {
                    var $currentTarget = $(event.currentTarget);
                    $currentTarget.closest('.ui-sortable-control')
                            .trigger('click', [$currentTarget.offset().top, event.metaKey]);
            }});
    $(this.jqId + ' th.ui-sortable-column .ui-sortable-control')
            .find('.ui-icon-triangle-1-s')
            .fadeTo(0, 0.2)
            .keypress(function(event) {
                if (event.which == 32 || event.which == 13) {
                    var $currentTarget = $(event.currentTarget);
                    $currentTarget.closest('.ui-sortable-control')
                            .trigger('click', [$currentTarget.offset().top + 6, event.metaKey]);
            }});
}
ice.ace.DataTable.prototype.setupSelectionEvents = function() {
    var _self = this;

    //Row mouseover, mouseout, click
    if (this.cfg.selectionMode) {
        var selectEvent = this.cfg.dblclickSelect ? 'dblclick' : 'click',
                selector = this.isCellSelectionEnabled() ? this.jqId + ' tbody.ui-datatable-data > tr > td'
                                                         : this.jqId + ' tbody.ui-datatable-data > tr:not(.ui-unselectable)';

        $(selector)
                .css('cursor', 'pointer')
                .die()
                .live('mouseover', function() {
                    var element = $(this);
                    if (!element.hasClass('ui-selected')) element.addClass('ui-state-hover');
                })
                .live('mouseout', function() {
                    var element = $(this);
                    if (!element.hasClass('ui-selected')) element.removeClass('ui-state-hover');
                })
                .live(selectEvent, function(event) {
                    if (this.nodeName == 'TR') _self.onRowClick(event, this);
                    else _self.onCellClick(event, this);
                });

    }
    //Radio-Checkbox based rowselection
    else if (this.cfg.columnSelectionMode) {
        if (this.cfg.columnSelectionMode == 'single')
            $(this.jqId + ' tbody.ui-datatable-data td.ui-selection-column input:radio').die()
                    .live('click', function() { _self.selectRowWithRadio(this); });
        else
            $(this.jqId + ' tbody.ui-datatable-data td.ui-selection-column input:checkbox').die()
                    .live('click', function() { _self.selectRowWithCheckbox(this); });
    }
}
ice.ace.DataTable.prototype.setupReorderableColumns = function() {
    var _self = this;
    $(this.jqId + ' thead').sortable({
                items:'th', helper:'clone',
                axis:'x', appendTo:this.jqId + ' thead',
                cursor:'move', placeholder:'ui-state-hover',
                cancel:'.ui-header-right, :input, button, .ui-tableconf-button, .ui-header-text'})
            .bind( "sortstart", function(event, ui) {
                _self.reorderStart = ui.item.index();
            })
            .bind( "sortstop", function(event, ui) {
                _self.reorderEnd = ui.item.index();
                if (_self.reorderStart != _self.reorderEnd)
                    _self.reorderColumns(_self.reorderStart, _self.reorderEnd);
            });
}
ice.ace.DataTable.prototype.setupRowExpansionEvents = function() {
    var table = this;
    $(this.jqId + ' tbody.ui-datatable-data tr td a.ui-row-toggler')
        .die()
        .live('keyup', function(event) { if (event.which == 32 || event.which == 13) { table.toggleExpansion(this); }})
        .live('click', function(event) { event.stopPropagation(); table.toggleExpansion(this); });
}
ice.ace.DataTable.prototype.setupPanelExpansionEvents = function() {
    var table = this;
    $(this.jqId + ' tbody.ui-datatable-data tr td a.ui-row-panel-toggler')
        .die()
        .live('keyup', function(event) { if (event.which == 32 || event.which == 13) { table.toggleExpansion(this); }})
        .live('click', function(event) { event.stopPropagation(); table.toggleExpansion(this); });
}
ice.ace.DataTable.prototype.resizeScrolling = function() {
    // get table body column widths
    var i = 0;
    var bodyElems = new Array();
    var widths = new Array();
    $(this.jqId + ' .ui-datatable-scrollable-body .ui-datatable-data tr:first-child td > div:first-child').each(function(){
        bodyElems[i] = $(this).parent();
        widths[i] = $(this).width();
        i++;
    });

    // set table head & foot column widths
    var headerSingleCols = $(this.jqId + ' .ui-datatable-scrollable-header thead th:not([colspan]) .ui-header-column');
    if (headerSingleCols.size() == 0) headerSingleCols = $(this.jqId + ' .ui-datatable-scrollable-header thead th[colspan="1"] .ui-header-column');
    var footerSingleCols = $(this.jqId + ' .ui-datatable-scrollable-footer tfoot td:not([colspan]) .ui-footer-column');
    if (footerSingleCols.size() == 0) footerSingleCols = $(this.jqId + ' .ui-datatable-scrollable-footer tfoot td[colspan="1"] .ui-header-column');

    var i  = 0;
    headerSingleCols.each(function() {
        var headWidth = $(this).width();
        if ((parseInt(headWidth) < parseInt(widths[i])) || ((bodyElems[i].children().attr('style') != null) &&
                (bodyElems[i].children().attr('style').indexOf("width") != -1))) $(this).width(widths[i]);
        else if (bodyElems[i] != undefined) bodyElems[i].width(headWidth); // if custom column styling has been set always resize head/foot
        i++;
    });

    i = 0;
    footerSingleCols.each(function() {
        var footWidth = $(this).width();
        if ((parseInt(footWidth) < parseInt(widths[i])) || ((bodyElems[i].children().attr('style') != null) &&
                (bodyElems[i].children().attr('style').indexOf("width") != -1))) $(this).width(widths[i]);
        else if (bodyElems[i] != undefined) bodyElems[i].width(footWidth); // 19 is table cell x padding with 1 px diff for internal border
        i++;
    });

    var bodyContainer = $(this.jqId + ' .ui-datatable-scrollable-body'),
        bodyContainerEl = bodyContainer.get(0),
        tbodyElement = $(this.tbody).get(0),
        theadElement = $('.ui-datatable-scrollable-header thead')[0];

    var maxWidth = isNaN(theadElement.parentNode.clientWidth) ? tbodyElement.parentNode.clientWidth : theadElement.parentNode.clientWidth;
    var containerWidth = (bodyContainerEl.scrollHeight > bodyContainerEl.clientHeight) ? (maxWidth + 18) + "px" : "auto";
    $(this.jqId).css('width', containerWidth);
}
ice.ace.DataTable.prototype.setupScrolling = function() {
    this.resizeScrolling();

    //live scroll
    if (this.cfg.liveScroll) {
        var bodyContainer = $(this.jqId + ' .ui-datatable-scrollable-body');
        this.scrollOffset = this.cfg.scrollStep;
        this.shouldLiveScroll = true;
        var _self = this;
        bodyContainer.scroll(function() {
            if (_self.shouldLiveScroll) {
                var $this = $(this);
                var sTop = $this.scrollTop(), sHeight = this.scrollHeight, viewHeight = $this.height();
                if (sTop >= (sHeight - viewHeight)) _self.loadLiveRows();
            }
        });
    }
}

ice.ace.DataTable.prototype.loadLiveRows = function() {
    var options = {
        source: this.id,
        process: this.id,
        update: this.id,
        formId: this.cfg.formId
    },
    _self = this;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id){
                $(_self.jqId + ' .ui-datatable-scrollable-body table tr:last').after(content);
                _self.scrollOffset += _self.cfg.scrollStep;
                if (_self.scrollOffset == _self.cfg.scrollLimit) _self.shouldLiveScroll = false;
            }
            else ice.ace.AjaxUtils.updateElement(id, content);
        });
        _self.resizeScrolling();
        return false;
    };

    var params = {};
    params[this.id + "_scrolling"] = true;
    params[this.id + "_scrollOffset"] = this.scrollOffset;
    params['ice.customUpdate'] = this.id;

    options.params = params;

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.getPaginator = function() {
    return this.cfg.paginator;
}

ice.ace.DataTable.prototype.reorderColumns = function(oldIndex, newIndex) {
    var options = {
        source: this.id,
        process: this.id,
        formId: this.cfg.formId
    };

    var params = {},
    _self = this;
    params[this.id + '_columnReorder'] = oldIndex + '-' + newIndex;
    params['ice.customUpdate'] = this.id;
    options.params = params;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id != _self.id) ice.ace.AjaxUtils.updateElement(id, content);
        });
        return false;
    };

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.paginate = function(newState) {
    var options = {
        source: this.id,
        update: this.id,
        process: this.id,
        formId: this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id){
                $(_self.tbody).replaceWith(content);
                _self.getPaginator().setState(newState);
            }
            else ice.ace.AjaxUtils.updateElement(id, content);
        });

        return false;
    };

    var params = {};
    params[this.id + "_paging"] = true;
    params[this.id + "_rows"] = newState.rowsPerPage;
    params[this.id + "_page"] = newState.page;
    params['ice.customUpdate'] = this.id;

    options.params = params;

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.sort = function(headerCells) {
    var options = {
        source: this.id,
        update: this.id,
        process: this.id,
        formId: this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id){
                $(_self.tbody).replaceWith(content);
            }
            else ice.ace.AjaxUtils.updateElement(id, content);
        });

        if (_self.isSelectionEnabled()) _self.clearSelection();

        return false;
    };

    var params = {}, sortDirs = [], sortKeys=[];
    params[this.id + "_sorting"] = true;
    $.each(headerCells, function() {
        sortKeys.push($(this).attr('id'));
    });
    params[this.id + "_sortKeys"] = sortKeys;
    $.each(headerCells, function() {
        sortDirs.push($(this).find('.ui-icon-triangle-1-n').hasClass('ui-toggled'));
    });
    params[this.id + "_sortDirs"] = sortDirs;
    params['ice.customUpdate'] = _self.id;

    options.params = params;

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.filter = function() {
    var options = {
        source: this.id,
        update: this.id,
        process: this.id,
        formId: this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function(responseXML) {
        var xmlDoc = responseXML.documentElement,
                updates = xmlDoc.getElementsByTagName("extension");

        var paginator = _self.getPaginator();
        if (paginator) {
            var extensions = xmlDoc.getElementsByTagName("extension"),
              totalRecords = _self.getPaginator().getTotalRecords();

            for (var i=0; i < extensions.length; i++) {
                var callbackParam = extensions[i].attributes.getNamedItem("aceCallbackParam");
                if ((callbackParam) && (callbackParam.nodeValue == 'totalRecords')) {
                    totalRecords = $.parseJSON(extensions[i].firstChild.data).totalRecords;

                    //Reset paginator state
                    paginator.setPage(1, true);
                    paginator.setTotalRecords(totalRecords, true);
                }
            }
        }

        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id){
                $(_self.tbody).replaceWith(content);
            }
            else {
                ice.ace.AjaxUtils.updateElement(id, content);
            }
        });

        if (_self.isSelectionEnabled()) _self.clearSelection();

        return false;
    };

    var params = {};
    params[this.id + "_filtering"] = true;
    params['ice.customUpdate'] = this.id;
    options.params = params;

    ice.ace.AjaxRequest(options);
}
ice.ace.DataTable.prototype.clearFilters = function() {
    $(this.jqId + ' thead th .ui-column-filter').val('');
}

ice.ace.DataTable.prototype.onRowClick = function(event, rowElement) {
    //Check if rowclick triggered this event not an element in row content
    if ($(event.target).is('td,span,div')) {
        var row = $(rowElement);

        if (row.hasClass('ui-selected')) this.unselectRow(row);
        else this.selectRow(row);
    }

}
ice.ace.DataTable.prototype.selectRow = function(row) {
    var rowId = row.attr('id').split('_row_')[1],
        deselectRowId = undefined;

    //unselect previous selection
    if (this.isSingleSelection()) {
        row.siblings('.ui-selected').removeClass('ui-selected ui-state-highlight ui-state-hover');
        this.deselection = [];
        deselectRowId = this.selection[0];
        this.deselection.push(deselectRowId);
        this.selection = [];
    }

    //add to selection
    row.addClass('ui-state-highlight ui-selected');
    this.deselection = $.grep(this.deselection, function(r) { return r != rowId; });
    this.selection.push(rowId);

    //save state
    this.writeSelections();
    if (this.cfg.instantSelect) this.fireRowSelectEvent(rowId, deselectRowId);
}
ice.ace.DataTable.prototype.unselectRow = function(row) {
    var rowId = row.attr('id').split('_row_')[1];

    //remove visual style
    row.removeClass('ui-selected ui-state-highlight ui-state-hover');

    if (this.isSingleSelection()) this.deselection = [];

    //remove from selection
    this.selection = $.grep(this.selection, function(r) { return r != rowId; });
    this.deselection.push(rowId);

    //save state
    this.writeSelections();
    if (this.cfg.instantSelect) {
        this.fireRowDeselectEvent(rowId);
        if (this.isSingleSelection()) {
            this.deselection = [];
            this.selection = [];
            this.writeSelections();
        }
    }
}
ice.ace.DataTable.prototype.fireSelectEvent = function() {
    var options = {
        source: this.id,
        process: this.id,
        formId: this.cfg.formId
    };


    if (this.cfg.onRowSelectUpdate) options.update = this.cfg.onRowSelectUpdate;
    if (this.cfg.onRowSelectStart) options.onstart = ice.ace.bind(this, function() {
        this.cfg.onRowSelectStart.apply(this, arguments);
        return true;
    });
    if (this.cfg.onRowSelectComplete) options.oncomplete = ice.ace.bind(this, function() {
        this.cfg.onRowSelectComplete.apply(this, arguments);
        return true;
    });
    var params = {},
        _self = this;
    params['ice.customUpdate'] = this.id;
    options.params = params;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id != _self.id) ice.ace.AjaxUtils.updateElement(id, content);
        });
        return false;
    };

    ice.ace.AjaxRequest(options);
}
ice.ace.DataTable.prototype.fireRowSelectEvent = function(rowId, deselectRowId) {
    var options = {
        source: this.id,
        process: this.id,
        formId: this.cfg.formId
    };

    if (this.cfg.onRowSelectUpdate) options.update = this.cfg.onRowSelectUpdate;
    if (this.cfg.onRowSelectStart) options.onstart = ice.ace.bind(this, function() {
        this.cfg.onRowSelectStart.apply(this, arguments);
        return true;
    });
    if (this.cfg.onRowSelectComplete) options.oncomplete = ice.ace.bind(this, function() {
        this.cfg.onRowSelectComplete.apply(this, arguments);
        return true;
    });

    var params = {},
        _self = this;
    params[this.id + '_instantSelectedRowIndex'] = rowId;
    if (deselectRowId) params[this.id + '_instantUnselectedRowIndex'] = deselectRowId;
    params['ice.customUpdate'] = this.id;
    options.params = params;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id != _self.id) ice.ace.AjaxUtils.updateElement(id, content);
        });
        return false;
    };

    ice.ace.AjaxRequest(options);
}
ice.ace.DataTable.prototype.fireRowDeselectEvent = function(rowId) {
    var options = {
        source: this.id,
        process: this.id,
        formId: this.cfg.formId
    };

    if (this.cfg.onRowUnselectUpdate) options.update = this.cfg.onRowUnselectUpdate;
    var params = {},
        _self = this;
    params[this.id + '_instantUnselectedRowIndex'] = rowId;
    params['ice.customUpdate'] = this.id;
    options.params = params;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id != _self.id) ice.ace.AjaxUtils.updateElement(id, content);
        });
        return false;
    };

    ice.ace.AjaxRequest(options);
}
ice.ace.DataTable.prototype.selectRowWithRadio = function(radio) {
    var row = $(radio).parents('tr'),
    rowId = row.attr('id').split('_row_')[1];

    this.selection = [];
    this.selection.push(rowId);

    //save state
    this.writeSelections();
}
ice.ace.DataTable.prototype.selectRowWithCheckbox = function(element) {
    var checkbox = $(element),
    row = checkbox.parents('tr'),
    rowId = row.attr('id').split('_row_')[1],
    checked = checkbox.attr('checked');

    if (checked) {
        //add to selection
        this.selection.push(rowId);

    } else {
        //remove from selection
        this.selection = $.grep(this.selection, function(r) {
            return r != rowId;
        });

    }

    //save state
    this.writeSelections();
}
ice.ace.DataTable.prototype.toggleCheckAll = function(element) {
    var checkbox = $(element),
            checked = checkbox.attr('checked');

    this.clearSelection();

    if (checked) {
        $(this.jqId + ' tbody.ui-datatable-data td.ui-selection-column input:checkbox').attr('checked', true);

        if (this.getPaginator() != null) {
            for (var i=0; i < this.getPaginator().getTotalRecords(); i++) {
                this.selection.push(i);
            }

        } else {
            var _self = this;
            $(this.jqId + ' tbody.ui-datatable-data tr').each(function() {
                _self.selection.push($(this).attr('id').split('_row_')[1]);
            });
        }

    }
    else $(this.jqId + ' tbody.ui-datatable-data td.ui-selection-column input:checkbox').attr('checked', false);

    //save state
    this.writeSelections();
}

ice.ace.DataTable.prototype.onCellClick = function(event, cellElement) {
    //Check if rowclick triggered this event not an element in row content
    if ($(event.target).is('div,td,span')) {
        var cell = $(cellElement);
        if (cell.hasClass('ui-selected')) this.unselectCell(cell);
        else this.selectCell(cell);
    }
}
ice.ace.DataTable.prototype.selectCell = function(cell) {
    var rowId = cell.parent().attr('id').split('_row_')[1],
            columnIndex = cell.index();

    //unselect previous selection
    if (this.cfg.selectionMode === 'singlecell') {
        $(this.jqId + ' tbody.ui-datatable-data td').removeClass('ui-selected ui-state-highlight ui-state-hover');
        this.selection = [];
    }

    cell.addClass('ui-state-highlight ui-selected');
    this.selection.push(rowId + '#' + columnIndex);
    this.writeSelections();
    if (this.cfg.instantSelect) this.fireSelectEvent();
}
ice.ace.DataTable.prototype.unselectCell = function(cell) {
    var rowId = cell.parent().attr('id').split('_row_')[1],
        columnIndex = cell.index(),
        cellId = rowId + '#' + columnIndex;

    cell.removeClass('ui-selected ui-state-highlight ui-state-hover');
    this.selection = $.grep(this.selection, function(c) { return c != cellId; });
    this.writeSelections();
    if (this.cfg.instantSelect) this.fireSelectEvent();
}

ice.ace.DataTable.prototype.toggleExpansion = function(expanderElement) {
    var expander = $(expanderElement),
    row = expander.parents('tr'),
    expanded = row.hasClass('ui-expanded-row');
    $this = (this);

    if (expanded) {
        var removeTargets = row.siblings('[id^="'+row.attr('id')+'."]');
        if (removeTargets.size() == 0) removeTargets = row.next('tr.ui-expanded-row-content');
        expander.removeClass('ui-icon-circle-triangle-s');
        expander.addClass('ui-icon-circle-triangle-e');
        row.removeClass('ui-expanded-row');
        removeTargets.fadeOut(function() { $(this).remove(); });
        if ($this.cfg.scrollable) $this.setupScrolling();
        if (!expander.hasClass('ui-row-panel-toggler')) this.sendRowContractionRequest(row);
        else this.sendPanelContractionRequest(row);
    } else {
        expander.removeClass('ui-icon-circle-triangle-e');
        expander.addClass('ui-icon-circle-triangle-s');
        row.addClass('ui-expanded-row');
        if (expander.hasClass('ui-row-panel-toggler')) this.loadExpandedPanelContent(row);
        else this.loadExpandedRows(row);
    }
}


ice.ace.DataTable.prototype.sendPanelContractionRequest = function(row) {
    var options = {
        source: this.id,
        process: this.id,
        update: this.id,
        formId: this.cfg.formId
    },
    rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + '_rowPanelExpansion'] = true;
    params[this.id + '_contractedRowId'] = rowId;
    params['ice.customUpdate'] = this.id;
    options.params = params;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id != _self.id) ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    ice.ace.AjaxRequest(options);
}


ice.ace.DataTable.prototype.sendRowContractionRequest = function(row) {
    var options = {
        source: this.id,
        process: this.id,
        update: this.id,
        formId: this.cfg.formId
    },
    rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + '_rowExpansion'] = true;
    params[this.id + '_contractedRowId'] = rowId;
    params['ice.customUpdate'] = this.id;
    options.params = params;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id != _self.id) ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedRows = function(row) {
    if (this.cfg.onExpandStart) this.cfg.onExpandStart.call(this, row);

    var options = {
        source: this.id,
        process: this.id,
        update: this.id,
        formId: this.cfg.formId
    },
    rowId = row.attr('id').split('_row_')[1],
    _self = this;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id) { row.after(content).nextUntil(':visible').fadeIn(); }

            else ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + '_rowExpansion'] = true;
    params[this.id + '_expandedRowId'] = rowId;
    params['ice.customUpdate'] = this.id;
    options.params = params;

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedPanelContent = function(row) {
    if (this.cfg.onExpandStart) this.cfg.onExpandStart.call(this, row);

    var options = {
        source: this.id,
        process: this.id,
        update: this.id,
        formId: this.cfg.formId
    },
    rowId = row.attr('id').split('_row_')[1],
    _self = this;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id){
                row.after(content);
                row.next().fadeIn();
            }
            else ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + '_rowPanelExpansion'] = true;
    params[this.id + '_expandedRowId'] = rowId;
    params['ice.customUpdate'] = this.id;
    options.params = params;

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.showEditors = function(element) {
    $(element).parents('tr:first').addClass('ui-state-focus').find('.ui-editable-column').each(function() {
        var column = $(this);

        column.find('span.ui-cell-editor-output').hide();
        column.find('span.ui-cell-editor-input').show();

        $(element).hide();
        $(element).siblings().show();
    });
}
ice.ace.DataTable.prototype.saveRowEdit = function(element) {
    this.doRowEditRequest(element, 'save');
}
ice.ace.DataTable.prototype.cancelRowEdit = function(element) {
    this.doRowEditRequest(element, 'cancel');
}
ice.ace.DataTable.prototype.doRowEditRequest = function(element, action) {
    var row = $(element).parents('tr:first'),
        options = {
            source: this.id,
            update: this.id,
            formId: this.cfg.formId
        },
        _self = this,
        rowEditorId = row.find('span.ui-row-editor').attr('id'),
        expanded = row.hasClass('ui-expanded-row');

    if (action === 'save') {
        //Only process cell editors of current row
        var editorsToProcess = new Array();
        editorsToProcess.push(rowEditorId);

        row.find('span.ui-cell-editor').each(function() { editorsToProcess.push($(this).attr('id')); });
        options.process = editorsToProcess.join(' ');

        //Additional components to update after row edit request
        if (this.cfg.onRowEditUpdate) { options.update += ' ' + this.cfg.onRowEditUpdate; }
    }

    options.onsuccess = function(responseXML) {
        var xmlDoc = responseXML.documentElement,
            extensions = xmlDoc.getElementsByTagName("extension");

        _self.args = {};
        for (i=0; i < extensions.length; i++) {
            var extension = extensions[i];
            if (extension.getAttributeNode('aceCallbackParam')) {
                var jsonObj = $.parseJSON(extension.firstChild.data);

                for (var paramName in jsonObj)
                    if (paramName) _self.args[paramName] = jsonObj[paramName];
            }
        }

        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id) {
                if (!_self.args.validationFailed) {
                    if (expanded) {
                        var removeTargets = row.siblings('[id^="'+row.attr('id')+'."]');
                        if (removeTargets.size() == 0) removeTargets = row.next('tr.ui-expanded-row-content');
                        row.removeClass('ui-expanded-row');
                        removeTargets.remove();
                    }
                    if (_self.cfg.scrollable) _self.setupScrolling();
                    row.replaceWith(content);
                }
            }
            else ice.ace.AjaxUtils.updateElement(id, content);
        });

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_rowEdit'] = true;
    params[this.id + '_editedRowId'] = row.attr('id').split('_row_')[1];
    params['ice.customUpdate'] = this.id;

    options.params = params;

    ice.ace.AjaxRequest(options);
}
ice.ace.DataTable.prototype.getRowEditors = function() {
    return $(this.jqId + ' tbody.ui-datatable-data tr td span.ui-row-editor');
}
ice.ace.DataTable.prototype.setupCellEditorEvents = function(rowEditors) {
    var _self = this;
    // unbind and rebind these events.
    var showEditors = function(event) { event.stopPropagation(); _self.showEditors(event.target); },
        saveRowEditors = function(event) { event.stopPropagation(); _self.saveRowEdit(event.target); },
        cancelRowEditors = function(event) { event.stopPropagation(); _self.cancelRowEdit(event.target); };
    rowEditors.find('a.ui-icon-pencil').die().live('click', showEditors).live('keyup', function(event) { if (event.which == 32 || event.which == 13) { showEditors(event); }} );
    rowEditors.find('a.ui-icon-check').die().live('click', saveRowEditors).live('keyup', function(event) { if (event.which == 32 || event.which == 13) { saveRowEditors(event); }} );
    rowEditors.find('a.ui-icon-close').die().live('click', cancelRowEditors).live('keyup', function(event) { if (event.which == 32 || event.which == 13) { cancelRowEditors(event); }} );
}


ice.ace.DataTable.prototype.writeSelections = function() {
    $(this.selectionHolder).val(this.selection.join(','));
    $(this.deselectionHolder).val(this.deselection.join(','));
}
ice.ace.DataTable.prototype.isSingleSelection = function() {
    return this.cfg.selectionMode == 'single';
}
ice.ace.DataTable.prototype.clearSelection = function() {
    this.selection = [];
    $(this.selectionHolder).val('');
}
ice.ace.DataTable.prototype.isSelectionEnabled = function() {
    return this.cfg.selectionMode != undefined || this.cfg.columnSelectionMode != undefined;
}
ice.ace.DataTable.prototype.isCellSelectionEnabled = function() {
    return this.cfg.selectionMode === 'singlecell' || this.cfg.selectionMode === 'multiplecell';
}

ice.ace.DataTable.prototype.setupResizableColumns = function() {
    //Add resizers
    $(this.jqId + ' thead :not(th:last)').children('.ui-header-column').append('<div class="ui-column-resizer"></div>');

    //Setup resizing
    this.columnWidthsCookie = this.id + '_columnWidths',
            resizers = $(this.jqId + ' thead th div.ui-column-resizer'),
            columns = $(this.jqId + ' thead th'),
            _self = this;

    resizers.draggable({
                axis:'x',
                drag: function(event, ui) {
                    var column = ui.helper.parents('th:first'),
                            newWidth = ui.position.left + ui.helper.outerWidth();

                    column.css('width', newWidth);
                },
                stop: function(event, ui) {
                    ui.helper.css('left', '');

                    var columnWidths = [];

                    columns.each(function(i, item) {
                        var columnHeader = $(item);
                        columnWidths.push(columnHeader.css('width'));
                    });
                    ice.ace.setCookie(_self.columnWidthsCookie, columnWidths.join(','));
                }
            });

    //restore widths on postback
    var widths = ice.ace.getCookie(this.columnWidthsCookie);
    if (widths) {
        widths = widths.split(',');
        for (var i in widths) {
            $(columns.get(i)).css('width', widths[i]);
        }
    }
}
