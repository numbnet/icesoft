/*
 * Original Code developed and contributed by Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
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


// Constructor
ice.ace.DataTable = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.sortOrder = [];
    this.tbody = this.jqId + '_data';
    this.delayedFilterCall = null;
    this.filterSource = null;
    this.behaviors = cfg.behaviors;
    var rowEditors = this.getRowEditors();

    if (this.cfg.paginator) this.setupPaginator();

    if (!this.cfg.disabled) {
        this.setupSortEvents();

        if (this.cfg.selectionMode) {
            this.selectionHolder = this.jqId + '_selection';
            this.deselectionHolder = this.jqId + '_deselection';
            this.selection = [];
            this.deselection = [];
            this.setupSelectionEvents();
        }

        if (this.cfg.configPanel)
            if (this.cfg.configPanel.startsWith(":"))
                this.cfg.configPanel = this.cfg.configPanel.substring(1);

        if (this.cfg.panelExpansion) this.setupPanelExpansionEvents();

        if (this.cfg.rowExpansion) this.setupRowExpansionEvents();

        if (this.cfg.scrollable) this.setupScrolling();

        if (rowEditors.length > 0) this.setupCellEditorEvents(rowEditors);

        if (this.cfg.resizableColumns) this.setupResizableColumns();

        // blur and keyup are handled by the xhtml on____ attributes, and written by the renderer
        if (this.cfg.filterEvent != "blur" && this.cfg.filterEvent != "keyup")
            this.setupFilterEvents();

        if (this.cfg.reorderableColumns) {
            this.reorderStart = 0;
            this.reorderEnd = 0;
            this.setupReorderableColumns();
        }
    } else
        this.setupDisabledStyling();
}




/* #########################################################################
 ########################## Event Binding & Setup ########################
 ######################################################################### */
ice.ace.DataTable.prototype.setupFilterEvents = function() {
    var _self = this;
    if (this.cfg.filterEvent == "enter") ice.ace.jq(this.jqId + ' th .ui-column-filter').unbind('keypress').bind('keypress', function(event) {
        event.stopPropagation();
        if (event.which == 13) {
            _self.filter(event);
            return false; // Don't run form level enter key handling
        }
    });
    else if (this.cfg.filterEvent == "change") ice.ace.jq(this.jqId + ' th .ui-column-filter').die('keyup').live('keyup', function(event) {
        var _event = event;
        if (event.which != 9) {
            if (_self.delayedFilterCall) clearTimeout(_self.delayedFilterCall);
            _self.delayedFilterCall = setTimeout(function() {
                _self.filter(_event);
            } , 400);
        }
    });
}

ice.ace.DataTable.prototype.setupPaginator = function() {
    var paginator = this.cfg.paginator;

    if (!this.cfg.disabled) paginator.subscribe('changeRequest', this.paginate, null, this);
    paginator.render();
}

ice.ace.DataTable.prototype.setupSortEvents = function() {
    var _self = this;
    ice.ace.jq(this.jqId + ' th > div.ui-sortable-column .ui-sortable-control')
            .die('click').live("click",function(event, altY, altMeta) {
                var $this = ice.ace.jq(this),
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

                // If we are looking a freshly rendered DT initalize our JS sort state
                // from the state of the rendered controls
                if (_self.sortOrder.length == 0) {
                    ice.ace.jq(_self.jqId + ' th > div.ui-sortable-column .ui-sortable-control').each(function() {
                        var $this = ice.ace.jq(this);
                        if (ice.ace.getOpacity($this.find('.ui-icon-triangle-1-n')[0]) == 1 ||
                                ice.ace.getOpacity($this.find('.ui-icon-triangle-1-s')[0]) == 1 )
                            _self.sortOrder.splice(
                                    parseInt($this.find('.ui-sortable-column-order').html())-1,
                                    0,
                                    $this.closest('.ui-header-column')
                            );
                    });
                }

                if (!metaKey || _self.cfg.singleSort) {
                    // Remake sort criteria
                    // Reset all other arrows
                    _self.sortOrder = [];
                    $this.closest('.ui-header-column').siblings().find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s').css('opacity', .2).removeClass('ui-toggled');
                    headerCell.parent().siblings().find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s').css('opacity', .2).removeClass('ui-toggled');
                    if (!_self.cfg.singleSort) {
                        // Handle stacked cell case
                        $this.closest('.ui-header-column').siblings().find('.ui-sortable-column-order').html('&#160;');
                        // Handle sibling cell case
                        headerCell.parent().siblings().find('.ui-sortable-column-order').html('&#160;');
                    }

                    // remove previous gradients
                    //headerCell.siblings().removeClass('ui-state-active').find('.ui-sortable-column-icon').removeClass('ui-icon-triangle-1-n ui-icon-triangle-1-s');
                }

                var cellFound = false, index = 0;
                ice.ace.jq(_self.sortOrder).each(function() {
                    if (headerCell.attr('id') === this.attr('id')) {
                        cellFound = true;
                        // If the cell already exists in our list, update the reference
                        _self.sortOrder.splice(index, 1, headerCell);
                    }
                    index++;
                });

                if (metaKey && cellFound) {
                    if ((ice.ace.getOpacity(topCarat) == 1 && !descending) ||
                            (ice.ace.getOpacity(bottomCarat) == 1 && descending)) {
                        // Remove from sort order
                        _self.sortOrder.splice(headerCell.find('.ui-sortable-column-order').html()-1,1);
                        ice.ace.jq(bottomCarat).css('opacity', .2).removeClass('ui-toggled');
                        ice.ace.jq(topCarat).css('opacity', .2).removeClass('ui-toggled');
                        if (!_self.cfg.singleSort) {
                            headerCell.find('.ui-sortable-column-order').html('&#160;');
                            var i = 0;
                            ice.ace.jq(_self.sortOrder).each(function(){
                                this.find('.ui-sortable-column-order').html(parseInt(i++)+1);
                            });
                        }
                    } else {
                        // Not a deselect, just a meta-toggle
                        if (descending) {
                            ice.ace.jq(bottomCarat).css('opacity', 1).addClass('ui-toggled');
                            ice.ace.jq(topCarat).css('opacity', .2).removeClass('ui-toggled');
                        } else {
                            ice.ace.jq(topCarat).css('opacity', 1).addClass('ui-toggled');
                            ice.ace.jq(bottomCarat).css('opacity', .2).removeClass('ui-toggled');
                        }
                    }
                } else {
                    // add header gradient
                    //headerCell.addClass('ui-state-active');

                    if (descending) {
                        ice.ace.jq(bottomCarat).css('opacity', 1).addClass('ui-toggled');
                        ice.ace.jq(topCarat).css('opacity', .2).removeClass('ui-toggled');
                    } else {
                        ice.ace.jq(topCarat).css('opacity', 1).addClass('ui-toggled');
                        ice.ace.jq(bottomCarat).css('opacity', .2).removeClass('ui-toggled');
                    }

                    // add to sort order
                    var cellFound = false;
                    ice.ace.jq(_self.sortOrder).each(function() { if (headerCell.attr('id') === this.attr('id')) { cellFound = true; } });
                    if (cellFound == false) {
                        var order = _self.sortOrder.push(headerCell);
                        // write control display value
                        if (!_self.cfg.singleSort) $this.find('.ui-sortable-column-order').html(order);
                    }
                }
                // submit sort info
                _self.sort(_self.sortOrder);
                return false;
            })
            .unbind('mousemove').bind('mousemove', function(event) {
                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-s")[0]),
                    controlOffset = $this.offset(),
                    controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22;

                if (event.pageY > (controlOffset.top + (controlHeight / 2)+3)) {
                    if (!bottomCarat.hasClass('ui-toggled'))
                        bottomCarat.fadeTo(0, .66);
                    if (!topCarat.hasClass('ui-toggled'))
                        topCarat.fadeTo(0, .33);
                } else {
                    if (!topCarat.hasClass('ui-toggled'))
                        topCarat.fadeTo(0, .66);
                    if (!bottomCarat.hasClass('ui-toggled'))
                        bottomCarat.fadeTo(0, .33);
                }
            })
            .unbind('mouseleave').bind('mouseleave', function(event) {
                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-s")[0]);
                if (!bottomCarat.hasClass('ui-toggled')) bottomCarat.fadeTo(100, .33);
                if (!topCarat.hasClass('ui-toggled')) topCarat.fadeTo(100, .33);
            });

    // Pre-fade and bind keypress & hover to kb-navigable sort icons
    ice.ace.jq(this.jqId + ' th > div.ui-sortable-column .ui-sortable-control')
            .find('.ui-icon-triangle-1-n')
            .die('keypress').live('keypress',function(event) {
                if (event.which == 32 || event.which == 13) {
                    var $currentTarget = ice.ace.jq(event.currentTarget);
                    $currentTarget.closest('.ui-sortable-control')
                            .trigger('click', [$currentTarget.offset().top, event.metaKey]);
                    return false;
                }})
            .not('.ui-toggled').fadeTo(0, 0.33);

    ice.ace.jq(this.jqId + ' th > div.ui-sortable-column .ui-sortable-control')
            .find('.ui-icon-triangle-1-s')
            .die('keypress').live('keypress',function(event) {
                if (event.which == 32 || event.which == 13) {
                    var $currentTarget = ice.ace.jq(event.currentTarget);
                    $currentTarget.closest('.ui-sortable-control')
                            .trigger('click', [$currentTarget.offset().top + 6, event.metaKey]);
                    return false;
                }})
            .not('.ui-toggled').fadeTo(0, 0.33);
}

ice.ace.DataTable.prototype.setupSelectionEvents = function() {
    var _self = this;
    var selectEvent = this.cfg.dblclickSelect ? 'dblclick' : 'click',
        selector = this.isCellSelectionEnabled()
                ? this.jqId + ' tbody.ui-datatable-data > tr > td'
                : this.jqId + ' tbody.ui-datatable-data:first > tr:not(.ui-unselectable)';

    ice.ace.jq(selector)
            .css('cursor', 'pointer')
            .die()
            .live('mouseenter', function() {
                var element = ice.ace.jq(this);
                if (!element.hasClass('ui-selected')) element.addClass('ui-state-hover');
                else element.addClass('ui-state-highlight');

                element.siblings('.ui-state-highlight, .ui-state-hover')
                       .removeClass('ui-state-highlight ui-state-hover');
                if (_self.isCellSelectionEnabled()) {
                    element.parent().siblings().children('.ui-state-highlight, .ui-state-hover')
                           .removeClass('ui-state-highlight ui-state-hover');
                }
            })
            .live(selectEvent, function(event) {
                if (this.nodeName == 'TR') _self.onRowClick(event, this);
                else _self.onCellClick(event, this);
            })
            .closest('table').bind('mouseleave',function() {
                var element = (_self.isCellSelectionEnabled() ? 'td' : 'tr');
                ice.ace.jq(this).find('tbody ' + element + ".ui-state-hover" + ", " + 'tbody ' + element + ".ui-state-highlight")
                    .removeClass('ui-state-highlight ui-state-hover');
            }).find('thead').bind('mouseenter', function() {
                var element = (_self.isCellSelectionEnabled() ? 'td' : 'tr');
                ice.ace.jq(this).siblings().find(element + ".ui-state-hover" + ", " + element + ".ui-state-highlight")
                    .removeClass('ui-state-highlight ui-state-hover');
            });
}

ice.ace.DataTable.prototype.setupReorderableColumns = function() {
    var _self = this;
    ice.ace.jq(this.jqId + ' thead').sortable({
                items:'th.ui-reorderable-col', helper:'clone',
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
    ice.ace.jq(this.jqId + ' tbody.ui-datatable-data tr td a.ui-row-toggler')
            .die()
            .live('keyup', function(event) { if (event.which == 32 || event.which == 13) { table.toggleExpansion(this); }})
            .live('click', function(event) { event.stopPropagation(); table.toggleExpansion(this); });
}

ice.ace.DataTable.prototype.setupPanelExpansionEvents = function() {
    var table = this;
    ice.ace.jq(this.jqId + ' > table > tbody.ui-datatable-data > tr:not(.ui-expanded-row-content) td a.ui-row-panel-toggler')
            .die()
            .live('keyup', function(event) { if (event.which == 32 || event.which == 13) { table.toggleExpansion(this); }})
            .live('click', function(event) { event.stopPropagation(); table.toggleExpansion(this); });
}

ice.ace.DataTable.prototype.setupScrolling = function() {
    var delayedCleanUpResizeToken,
        delayedCleanUpResize = function() {
            var headerTable = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-header table'),
                bodyTable = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body table');

            _self.resizeScrolling();
            if (headerTable.width() != (bodyTable.width() - 1)) {
                setTimeout(delayedCleanUpResize ,150);
            }
         };

    this.resizeScrolling();
    _self = this;

    ice.ace.jq(window).bind('resize', function() {
        _self.resizeScrolling();
        if (delayedCleanUpResizeToken) clearTimeout(delayedCleanUpResizeToken);
        delayedCleanUpResizeToken = setTimeout(delayedCleanUpResize ,150);
    });

    //live scroll
    if (this.cfg.liveScroll) {
        var bodyContainer = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body');
        this.scrollOffset = this.cfg.scrollStep;
        this.shouldLiveScroll = true;
        var _self = this;
        bodyContainer.scroll(function() {
            if (_self.shouldLiveScroll) {
                var $this = ice.ace.jq(this);
                var sTop = $this.scrollTop(), sHeight = this.scrollHeight, viewHeight = $this.height();
                if (sTop >= (sHeight - viewHeight)) _self.loadLiveRows();
            }
        });
    }
}

ice.ace.DataTable.prototype.setupResizableColumns = function() {
    //Add resizers
    ice.ace.jq(this.jqId + ' thead :not(th:last)').children('.ui-header-column').append('<div class="ui-column-resizer"></div>');

    //Setup resizing
    this.columnWidthsCookie = this.id + '_columnWidths',
            resizers = ice.ace.jq(this.jqId + ' thead th div.ui-column-resizer'),
            columns = ice.ace.jq(this.jqId + ' thead th'),
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
                        var columnHeader = ice.ace.jq(item);
                        columnWidths.push(columnHeader.css('width'));
                    });
                    ice.ace.setCookie(_self.columnWidthsCookie, columnWidths.join(','));
                }
            });

    //restore widths on postback
    var widths = ice.ace.getCookie(this.columnWidthsCookie);
    if (widths) {
        widths = widths.split(',');
        for (var i = 0; i < widths.length; i++) {
            ice.ace.jq(columns.get(i)).css('width', widths[i]);
        }
    }
}

ice.ace.DataTable.prototype.resizeScrolling = function() {
    var dupeHead = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body thead'), i;

    var dupeHeadSingleCols = dupeHead.find('th:not([colspan]) .ui-header-column:first-child').get().reverse();
    if (dupeHeadSingleCols.size() == 0)
        dupeHeadSingleCols = dupeHead.find('th[colspan="1"] .ui-header-column:first-child').get().reverse();

    var realHeadSingleCols = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-header th:not([colspan]) .ui-header-column:first-child').get().reverse();
    if (realHeadSingleCols.size() == 0)
        realHeadSingleCols = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-header th[colspan="1"] .ui-header-column:first-child').get().reverse();

    var bodySingleCols = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body tbody tr:first td div:first-child').get().reverse();

    
    // Reset fixed sizing if set by previous sizing.
    for (i = 0; i < bodySingleCols.length; i++)
        ice.ace.jq(bodySingleCols[i]).css('width', 'auto');

    // Show Duplicate Header
    dupeHead.css('display', 'table-header-group');

    // Get Duplicate Header Sizing
    var dupeHeadColumn, realHeadColumn, bodyColumn;
    for (i = 0; i < bodySingleCols.length; i++) {
        dupeHeadColumn = ice.ace.jq(dupeHeadSingleCols[i]);
        realHeadColumn = ice.ace.jq(realHeadSingleCols[i]);
        bodyColumn = ice.ace.jq(bodySingleCols[i]);

        // Set Duplicate Header Sizing to True Header Columns
        realHeadColumn.width(dupeHeadColumn.width());
        // Apply same width to stacked sibling columns
        realHeadColumn.siblings('.ui-header-column').width(dupeHeadColumn.width());
        // Equiv of max width
        realHeadColumn.parent().width(dupeHeadColumn.width());

        // Set Duplicate Header Sizing to Body Columns
        // Equiv of max width
        bodyColumn.parent().width(dupeHeadColumn.width());
        // Equiv of min width
        var dupeHeadColumnWidth = i == (bodySingleCols.length - 1) ?
            dupeHeadColumn.width() - 1 : dupeHeadColumn.width();
        bodyColumn.width(dupeHeadColumnWidth);
    }

    // Hide Duplicate Header
    dupeHead.css('display', 'none');
}

ice.ace.DataTable.prototype.setupDisabledStyling = function() {
    // Fade out controls
    ice.ace.jq(this.jqId + ' > table > tbody.ui-datatable-data > tr > td a.ui-row-toggler, ' +
               this.jqId + ' > table > thead th .ui-column-filter, ' +
               this.jqId + ' > table > thead > th > div.ui-sortable-column .ui-sortable-control, ' +
               this.jqId + ' > table > tbody.ui-datatable-data tr td span.ui-row-editor .ui-icon'
               ).css({opacity:0.4});

    // Add pagination disabled style
    ice.ace.jq(this.jqId + ' > .ui-paginator .ui-icon, ' +
               this.jqId + ' > .ui-paginator .ui-paginator-current-page, ' +
               this.jqId + ' > table > thead .ui-tableconf-button a').each(function () {
        ice.ace.jq(this).parent().addClass('ui-state-disabled');
    });

    // Disable filter text entry
    ice.ace.jq(this.jqId + ' > table > thead th .ui-column-filter').keypress(function () {
        return false;
    });

    // Row style
    ice.ace.jq(this.jqId + ' > table > tbody.ui-datatable-data:first > tr > td')
        .css({backgroundColor:'#EDEDED', opacity:0.8});
}




/* #########################################################################
 ############################### Requests ################################
 ######################################################################### */
ice.ace.DataTable.prototype.reorderColumns = function(oldIndex, newIndex) {
    var options = {
        source: this.id,
        execute: this.id,
        render: (this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        formId: this.cfg.formId
    };

    var params = {},
            _self = this;
    params[this.id + '_columnReorder'] = oldIndex + '-' + newIndex;

    options.params = params;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.reorder) {
            this.behaviors.reorder(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadLiveRows = function() {
    var options = {
        source: this.id,
        execute: this.id,
        render: this.id,
        formId: this.cfg.formId
    },
            _self = this;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if (id == _self.id){
                ice.ace.jq(_self.jqId + ' .ui-datatable-scrollable-body table tr:last').after(content);
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

ice.ace.DataTable.prototype.paginate = function(newState) {
    var options = {
        source: this.id,
        render: this.id,
        execute: this.id,
        formId: this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.resizeScrolling();

        return false;
    };

    var params = {};
    params[this.id + "_paging"] = true;
    params[this.id + "_rows"] = newState.rowsPerPage;
    params[this.id + "_page"] = newState.page;

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.page) {
            this.behaviors.page(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.sort = function(headerCells) {
    var options = {
        source: this.id,
        render: (this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        execute: this.id,
        formId: this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });

        if (_self.isSelectionEnabled()) _self.clearSelection();
        _self.setupSortEvents();
        return false;
    };

    var params = {}, sortDirs = [], sortKeys=[];
    params[this.id + "_sorting"] = true;
    ice.ace.jq.each(headerCells, function() {
        sortKeys.push(ice.ace.jq(this).attr('id'));
    });
    params[this.id + "_sortKeys"] = sortKeys;
    ice.ace.jq.each(headerCells, function() {
        // Have to "refind" the elements by id, as in IE browsers, the dom
        // elements referenced by headerCells return undefined for
        // .hasClass('ui-toggled')
        sortDirs.push(ice.ace.jq(ice.ace.escapeClientId(ice.ace.jq(this).attr('id')))
                .find('.ui-icon-triangle-1-n').hasClass('ui-toggled'));
    });
    params[this.id + "_sortDirs"] = sortDirs;

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.sort) {
            this.behaviors.sort(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.filter = function(evn) {
    var options = {
        source: this.id,
        render: (this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        execute: this.id,
        formId: this.cfg.formId
    };

    var _self = this;
    this.filterSource = (evn.target) ? evn.target : evn.srcElement;

    options.onsuccess = function(responseXML) {
        var xmlDoc = responseXML.documentElement,
                updates = xmlDoc.getElementsByTagName("extension");

        var paginator = _self.cfg.paginator;
        if (paginator) {
            var extensions = xmlDoc.getElementsByTagName("extension"),
                    totalRecords = _self.cfg.paginator.getTotalRecords();

            for (var i=0; i < extensions.length; i++) {
                var callbackParam = extensions[i].attributes.getNamedItem("aceCallbackParam");
                if ((callbackParam) && (callbackParam.nodeValue == 'totalRecords')) {
                    totalRecords = ice.ace.jq.parseJSON(extensions[i].firstChild.data).totalRecords;

                    //Reset paginator state
                    paginator.setPage(1, true);
                    paginator.setTotalRecords(totalRecords, true);
                }
            }
        }

        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });

        // Search by id rather than $(evn.target) to get updated copy now in DOM
        var newInput = ice.ace.jq(ice.ace.escapeClientId(ice.ace.jq(_self.filterSource).attr('id')));
        // Reset input value after focus to prevent selection of text
        newInput.setCaretToEnd();

        return false;
    };

    var params = {};
    params[this.id + "_filtering"] = true;
    params[this.id + "_filteredColumn"] =
            ice.ace.jq(this.filterSource).attr('id');
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.filter) {
            this.behaviors.filter(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.doSelectionEvent = function(type, deselection, element) {
    // Get Id(s) //
    var targetId, deselectedId;
    if (type == 'row') {
        targetId = element.attr('id').split('_row_')[1];
    }
    else if (type == 'cell') {
        var rowId = element.parent().attr('id').split('_row_')[1],
            columnIndex = element.index();
        targetId = rowId + '#' + columnIndex;
    }

    // Sync State //
    this.readSelections();

    // Adjust State //
    if (!deselection) {
        if (this.isSingleSelection()) {
            // If single selection unselect previous selection
            if (type == 'row')
                element.siblings('.ui-selected').removeClass('ui-selected ui-state-active ui-state-highlight');
            else if (type == 'cell')
                ice.ace.jq(this.jqId + ' tbody.ui-datatable-data td').removeClass('ui-selected ui-state-active ui-state-highlight');

            // Add current selection to deselection delta
            this.deselection = [];
            deselectedId = this.selection[0];
            this.deselection.push(deselectedId);

            // The new selection will be the only member of the delta
            this.selection = [];
        }

        // Add selected styling
        element.addClass('ui-state-active ui-state-highlight ui-selected');
        // Filter id from deselection delta
        this.deselection = ice.ace.jq.grep(this.deselection, function(r) { return r != targetId; });
        // Add filter id to selection delta
        this.selection.push(targetId);
    } else {
        // Remove selected styling
        element.removeClass('ui-selected ui-state-active ui-state-highlight');
        // Remove from selection
        this.selection = ice.ace.jq.grep(this.selection, function(r) { return r != targetId; });
        // Add to deselection
        this.deselection.push(targetId);
    }

    // Write State //
    this.writeSelections();

    // Submit State //
    if (this.cfg.instantSelect) {
        var options = {
            source: this.id,
            execute: this.id,
            formId: this.cfg.formId
        };

        var params = {},
            _self = this;

        if (type == 'row') {
            if (!deselection) {
                // Submit selected index and deselection if single selection enabled
                params[this.id + '_instantSelectedRowIndex'] = targetId;
                if (deselectedId) params[this.id + '_instantUnselectedRowIndex'] = deselectedId;
            } else {
                // Submit deselected index
                params[this.id + '_instantUnselectedRowIndex'] = targetId;
            }
        }

        options.params = params;

        if (this.behaviors)
            if (this.behaviors.select && !deselection) {
                this.behaviors.select(params);
                return;
            } else if (this.behaviors.deselect && deselection) {
                this.behaviors.deselect(params);
                return;
            }

        ice.ace.AjaxRequest(options);
    }
}

ice.ace.DataTable.prototype.onRowClick = function(event, rowElement) {
    //Check if rowclick triggered this event not an element in row content
    if (ice.ace.jq(event.target).is('td,span,div')) {
        var row = ice.ace.jq(rowElement);
        if (row.hasClass('ui-selected')) this.doSelectionEvent('row', true, row);
        else this.doSelectionEvent('row', false, row);
    }
}

ice.ace.DataTable.prototype.onCellClick = function(event, cellElement) {
    //Check if rowclick triggered this event not an element in row content
    if (ice.ace.jq(event.target).is('div,td,span')) {
        var cell = ice.ace.jq(cellElement);
        if (cell.hasClass('ui-selected')) this.doSelectionEvent('cell', true, cell);
        else this.doSelectionEvent('cell', false, cell);
    }
}





/* #########################################################################
 ########################### Expansion ###################################
 ######################################################################### */
ice.ace.DataTable.prototype.toggleExpansion = function(expanderElement) {
    var expander = ice.ace.jq(expanderElement),
            row = expander.closest('tr'),
            expanded = row.hasClass('ui-expanded-row');
    $this = (this);

    if (expanded) {
        var removeTargets = row.siblings('[id^="'+row.attr('id')+'."]');
        if (removeTargets.size() == 0) removeTargets = row.next('tr.ui-expanded-row-content');
        expander.removeClass('ui-icon-circle-triangle-s');
        expander.addClass('ui-icon-circle-triangle-e');
        row.removeClass('ui-expanded-row');
        removeTargets.fadeOut(function() { ice.ace.jq(this).remove(); });
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
        execute: this.id,
        render: this.id,
        formId: this.cfg.formId
    },
            rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.contract) {
            this.behaviors.contract(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.sendRowContractionRequest = function(row) {
    var options = {
        source: this.id,
        execute: this.id,
        render: this.id,
        formId: this.cfg.formId
    },
            rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;;
    options.params = params;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.contract) {
            this.behaviors.contract(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedRows = function(row) {
    var options = {
        source: this.id,
        execute: this.id,
        render: this.id,
        formId: this.cfg.formId
    },
            rowId = row.attr('id').split('_row_')[1],
            _self = this;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.expand) {
            this.behaviors.expand(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedPanelContent = function(row) {
    var options = {
        source: this.id,
        execute: this.id,
        render: this.id,
        formId: this.cfg.formId
    },
            rowId = row.attr('id').split('_row_')[1],
            _self = this;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.expand) {
            this.behaviors.expand(params);
            return;
        }

    ice.ace.AjaxRequest(options);
}




/* #########################################################################
 ########################### Row Editing #################################
 ######################################################################### */
ice.ace.DataTable.prototype.showEditors = function(element) {
    ice.ace.jq(element).closest('tr').addClass('ui-state-focus').find('.ui-editable-column').each(function() {
        var column = ice.ace.jq(this);

        column.find('span.ui-cell-editor-output').hide();
        column.find('span.ui-cell-editor-input').show();

        ice.ace.jq(element).hide();
        ice.ace.jq(element).siblings().show();
    });

    if (this.behaviors)
        if (this.behaviors.editStart) {
            this.behaviors.editStart();
            return;
        }
}

ice.ace.DataTable.prototype.saveRowEdit = function(element) {
    this.doRowEditRequest(element);
}

ice.ace.DataTable.prototype.cancelRowEdit = function(element) {
    var row = ice.ace.jq(element).parents('tr:first');

    row.removeClass('ui-state-focus').find('.ui-editable-column').each(function() {
        var column = ice.ace.jq(this);
        column.find('span.ui-cell-editor-output').show();
        column.find('span.ui-cell-editor-input').hide();
    });

    ice.ace.jq(element).hide();
    ice.ace.jq(element).siblings().hide();
    ice.ace.jq(element).siblings('.ui-icon-pencil').show();

    this.doRowEditCancelRequest(element);
}

ice.ace.DataTable.prototype.doRowEditCancelRequest = function(element) {
    var row = ice.ace.jq(element).parents('tr:first'),
            rowEditorId = row.find('span.ui-row-editor').attr('id'),
            options = {
                source: rowEditorId,
                execute: '@this',
                formId: this.cfg.formId
            },
            _self = this,
            expanded = row.hasClass('ui-expanded-row'),
            editorsToProcess = new Array();

    row.find('span.ui-cell-editor').each(function() { editorsToProcess.push(ice.ace.jq(this).attr('id')); });
    options.render = editorsToProcess.join(' ');

    //Additional components to update after row edit request
    if (this.cfg.onRowEditUpdate) { options.render += (" " + this.cfg.onRowEditUpdate); }

    options.onsuccess = function(responseXML) {
        var xmlDoc = responseXML.documentElement;

        _self.args = {};

        if (_self.cfg.scrollable)
            _self.setupScrolling();

        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editCancel) {
            this.behaviors.editCancel(params, null, options.render);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.doRowEditRequest = function(element) {
    var row = ice.ace.jq(element).parents('tr:first'),
            rowEditorId = row.find('span.ui-row-editor').attr('id'),
            options = {
                source: rowEditorId,
                formId: this.cfg.formId
            },
            _self = this,
            expanded = row.hasClass('ui-expanded-row'),
            editorsToProcess = new Array();

    row.find('span.ui-cell-editor').each(function() { editorsToProcess.push(ice.ace.jq(this).attr('id')); });
    options.execute = editorsToProcess.join(' ');
    options.render = options.execute;

    //Additional components to update after row edit request
    if (this.cfg.onRowEditUpdate) { options.render += (" " + this.cfg.onRowEditUpdate); }

    options.onsuccess = function(responseXML) {
        var xmlDoc = responseXML.documentElement,
                extensions = xmlDoc.getElementsByTagName("extension");

        _self.args = {};
        for (i=0; i < extensions.length; i++) {
            var extension = extensions[i];
            if (extension.getAttributeNode('aceCallbackParam')) {
                var jsonObj = ice.ace.jq.parseJSON(extension.firstChild.data);

                for (var paramName in jsonObj)
                    if (paramName) _self.args[paramName] = jsonObj[paramName];
            }
        }

        if (!_self.args.validationFailed) {
            if (_self.cfg.scrollable) _self.setupScrolling();

            row.removeClass('ui-state-focus').find('.ui-editable-column').each(function() {
                var column = ice.ace.jq(this);
                column.find('span.ui-cell-editor-output').show();
                column.find('span.ui-cell-editor-input').hide();
            });

            ice.ace.jq(element).hide();
            ice.ace.jq(element).siblings().hide();
            ice.ace.jq(element).siblings('.ui-icon-pencil').show();
        }

        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_rowEdit'] = true;
    params[this.id + '_editedRowId'] = row.attr('id').split('_row_')[1];

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editSubmit) {
            this.behaviors.editSubmit(params, options.execute, options.render);
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.getRowEditors = function() {
    return ice.ace.jq(this.jqId + ' tbody.ui-datatable-data tr td span.ui-row-editor');
}

ice.ace.DataTable.prototype.setupCellEditorEvents = function(rowEditors) {
    var _self = this;

    // unbind and rebind these events.
    var showEditors = function(event) { event.stopPropagation(); _self.showEditors(event.target); },
            saveRowEditors = function(event) { event.stopPropagation(); _self.saveRowEdit(event.target); },
            cancelRowEditors = function(event) { event.stopPropagation(); _self.cancelRowEdit(event.target); },
            inputCellKeypress = function(event) { if (event.which == 13) return false; };
    rowEditors.find('a.ui-icon-pencil').die().live('click', showEditors).live('keyup', function(event) { if (event.which == 32 || event.which == 13) { showEditors(event); }} );
    rowEditors.find('a.ui-icon-check').die().live('click', saveRowEditors).live('keyup', function(event) { if (event.which == 32 || event.which == 13) { saveRowEditors(event); }} );
    rowEditors.find('a.ui-icon-close').die().live('click', cancelRowEditors).live('keyup', function(event) { if (event.which == 32 || event.which == 13) { cancelRowEditors(event); }} );
    rowEditors.closest('td').siblings().find('span.ui-cell-editor-input input').bind('keypress', inputCellKeypress);
}




/* #########################################################################
 ########################## Selection Helpers ############################
 ######################################################################### */
ice.ace.DataTable.prototype.writeSelections = function() {
    // Writes selection state to hidden field for submission
    ice.ace.jq(this.selectionHolder).val(this.selection.join(','));
    ice.ace.jq(this.deselectionHolder).val(this.deselection.join(','));
};

ice.ace.DataTable.prototype.readSelections = function() {
    // Reading clears JS selected state following delta field submissions
    var selectionVal = ice.ace.jq(this.selectionHolder).val(),
        deselectionVal = ice.ace.jq(this.deselectionHolder).val();
    this.selection = (selectionVal == '') ? [] : selectionVal.split(',');
    this.deselection = (deselectionVal == '') ? [] : deselectionVal.split(',');
}

ice.ace.DataTable.prototype.isSingleSelection = function() {
    return this.cfg.selectionMode == 'single' || this.cfg.selectionMode === 'singlecell';
};

ice.ace.DataTable.prototype.isSelectionEnabled = function() {
    return this.cfg.selectionMode != undefined;
};

ice.ace.DataTable.prototype.isCellSelectionEnabled = function() {
    return this.cfg.selectionMode === 'singlecell' || this.cfg.selectionMode === 'multiplecell';
};

ice.ace.DataTable.prototype.clearSelection = function() {
    this.selection = [];
    ice.ace.jq(this.selectionHolder).val('');
};
