/*
 * Original Code Copyright Prime Technology.
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
if (!window.ice.ace['DataTables']) {
    window.ice.ace.DataTables = {};
}


(function ($) {
    var rootrx = /^(?:html)$/i;

    var converter = {
        vertical:{ x:false, y:true },
        horizontal:{ x:true, y:false },
        both:{ x:true, y:true },
        x:{ x:true, y:false },
        y:{ x:false, y:true }
    };

    var scrollValue = {
        auto:true,
        scroll:true,
        visible:false,
        hidden:false
    };

    $.extend($.expr[":"], {
        scrollable:function (element, index, meta, stack) {
            var direction = converter[typeof (meta[3]) === "string" && meta[3].toLowerCase()] || converter.both;
            var styles = (document.defaultView && document.defaultView.getComputedStyle ? document.defaultView.getComputedStyle(element, null) : element.currentStyle);
            var overflow = {
                x:scrollValue[styles.overflowX.toLowerCase()] || false,
                y:scrollValue[styles.overflowY.toLowerCase()] || false,
                isRoot:rootrx.test(element.nodeName)
            };

            // check if completely unscrollable (exclude HTML element because it's special)
            if (!overflow.x && !overflow.y && !overflow.isRoot) {
                return false;
            }

            var size = {
                height:{
                    scroll:element.scrollHeight,
                    client:element.clientHeight
                },
                width:{
                    scroll:element.scrollWidth,
                    client:element.clientWidth
                },
                // check overflow.x/y because iPad (and possibly other tablets) don't dislay scrollbars
                scrollableX:function () {
                    return (overflow.x || overflow.isRoot) && this.width.scroll > this.width.client;
                },
                scrollableY:function () {
                    return (overflow.y || overflow.isRoot) && this.height.scroll > (this.height.client + 1);
                }
            };
            return direction.y && size.scrollableY() || direction.x && size.scrollableX();
        }
    });

    var OSDetect = {
        init:function () {
            this.OS = this.searchString(this.dataOS) || "an unknown OS";
        },

        searchString:function (data) {
            for (var i = 0; i < data.length; i++) {
                var dataString = data[i].string;
                var dataProp = data[i].prop;
                if (dataString) {
                    if (dataString.indexOf(data[i].subString) != -1)
                        return data[i].identity;
                }
                else if (dataProp)
                    return data[i].identity;
            }
        },

        dataOS:[
            {   string:navigator.platform,
                subString:"Win",
                identity:"win"
            },
            {   string:navigator.platform,
                subString:"Mac",
                identity:"mac"
            },
            {   string:navigator.userAgent,
                subString:"iPhone",
                identity:"ios"
            },
            {   string:navigator.platform,
                subString:"Linux",
                identity:"linux"
            }
        ]
    };

    OSDetect.init();
    $.browser.chrome = /chrome/.test(navigator.userAgent.toLowerCase()) && !/chromeframe/.test(navigator.userAgent.toLowerCase());
    // Chrome has 'safari' in its user string so we need to exclude it explicitly
    $.browser.safari = /safari/.test(navigator.userAgent.toLowerCase()) && !$.browser.chrome;
    $.browser['os'] = OSDetect.OS;
})(ice.ace.jq);


// Constructor
ice.ace.DataTable = function (id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.sortOrder = [];
    this.parentResizeDelaySet = false;
    this.delayedFilterCall = null;
    this.filterSource = null;
    this.behaviors = cfg.behaviors;
    this.parentSize = 0;
    this.lastClickedIndex = -1;
    this.scrollLeft = 0;
    this.scrollTop = 0;

    this.sortColumnSelector = this.jqId + ' > div > table > thead > tr > th > div.ui-sortable-column';
    this.sortControlSelector = this.jqId + ' > div > table > thead > tr > th > div.ui-sortable-column > span > span.ui-sortable-control';
    this.sortUpSelector = this.sortControlSelector + ' a.ui-icon-triangle-1-n';
    this.sortDownSelector = this.sortControlSelector + ' a.ui-icon-triangle-1-s';
    this.rowSelector = this.jqId + ' > div > table > tbody.ui-datatable-data > tr:not(.ui-unselectable)';
    this.cellSelector = this.jqId + ' > div > table > tbody.ui-datatable-data > tr:not(.ui-unselectable) > td';
    this.scrollBodySelector = this.jqId + ' > div.ui-datatable-scrollable-body';
    this.filterSelector = this.jqId + ' > div > table > thead > tr > th > div > input.ui-column-filter';
    this.panelExpansionSelector = this.jqId + ' > div > table > tbody.ui-datatable-data > tr:not(.ui-expanded-row-content) > td *:not(tbody) a.ui-row-panel-toggler';
    this.rowExpansionSelector = this.jqId + ' > div > table > tbody.ui-datatable-data > tr > td *:not(tbody) a.ui-row-toggler';
    // 'link' will be replaced with the style class of the element in question
    this.cellEditorSelector = this.jqId + ' > div > table > tbody.ui-datatable-data > tr > td > div.ui-row-editor link, ' +
            this.jqId + ' > div > table > tbody.ui-datatable-data > tr > td > div > div.ui-row-editor link';

    var oldInstance = ice.ace.DataTables[this.id];
    var rowEditors = this.getRowEditors();

    // Persist State
    if (oldInstance) {
        this.scrollLeft = oldInstance.scrollLeft;
        this.scrollTop = oldInstance.scrollTop;
    }

    if (this.cfg.paginator)
        this.setupPaginator();

    if (!this.cfg.disabled) {
        if (this.cfg.sorting)
            this.setupSortEvents();

        if (this.isSelectionEnabled()) {
            this.selectionHolder = this.jqId + '_selection';
            this.deselectionHolder = this.jqId + '_deselection';
            this.selection = [];
            this.deselection = [];
            this.setupSelectionEvents();
        } else // Clean up global events left over from possible pre-existing selection mode
            this.tearDownSelectionEvents();

        if (this.cfg.configPanel)
            if (this.cfg.configPanel.startsWith(":"))
                this.cfg.configPanel = this.cfg.configPanel.substring(1);

        if (this.cfg.panelExpansion)
            this.setupPanelExpansionEvents();

        if (this.cfg.rowExpansion)
            this.setupRowExpansionEvents();

        if (this.cfg.scrollable)
            this.setupScrolling();

        if (rowEditors.length > 0)
            this.setupCellEditorEvents(rowEditors);

        if (this.cfg.resizableColumns)
            this.setupResizableColumns();

        // blur and keyup are handled by the xhtml on____ attributes, and written by the renderer
        if (this.cfg.filterEvent && this.cfg.filterEvent != "blur" && this.cfg.filterEvent != "keyup")
            this.setupFilterEvents();

        if (this.cfg.reorderableColumns) {
            this.reorderStart = 0;
            this.reorderEnd = 0;
            this.setupReorderableColumns();
        }
    } else
        this.setupDisabledStyling();

    // Explicitly dereference helper variables & old DT instance.
    oldInstance = null;
    rowEditors = null;

    ice.ace.DataTables[this.id] = this;

    // Setup unload callback if not already done
    if (!window[this.cfg.widgetVar]) {
        var self = this;
        ice.onElementUpdate(this.id, function() { self.unload(); });
    }
}


/* #########################################################################
 ########################## Event Binding & Setup ########################
 ######################################################################### */
ice.ace.DataTable.prototype.unload = function() {
    var jqSelf = ice.ace.jq(this.jqId);

    // Cleanup sort events
    ice.ace.jq(this.sortColumnSelector).unbind("click").unbind("mousemove").unbind("mouseleave");

    var sortControls = ice.ace.jq(this.sortControlSelector);
    sortControls.unbind("click mousemove mouseleave");
    jqSelf.off('keypress', this.sortUpSelector);
    jqSelf.off('keypress', this.sortDownSelector);

    // Clear selection events
    jqSelf.off('mouseenter click dblclick', this.cellSelector)
            .off('mouseenter click dblclick', this.rowSelector);

    ice.ace.jq(this.cellSelector).first()
            .closest('table').unbind('mouseleave')
            .find('> thead').unbind('mouseenter');

    // Clear scrolling
    ice.ace.jq(window).unbind('resize', this.scrollableResizeCallback);
    ice.ace.jq(this.scrollBodySelector).unbind('scroll')

    // Clear filter events
    ice.ace.jq(this.filterSelector).unbind('keypress').off('keyup');

    // Clear panel expansion events
    jqSelf.off('keyup click', this.panelExpansionSelector);

    // Clear row expansion events
    jqSelf.off('keyup click', this.rowExpansionSelector);

    // Clear cell editor events
    var icoSel = this.cellEditorSelector.replace(/link/g, 'a.ui-icon-pencil');
    jqSelf.off('click keyup', icoSel);

    icoSel = this.cellEditorSelector.replace(/link/g, 'a.ui-icon-check');
    jqSelf.off('click keyup', icoSel);

    icoSel = this.cellEditorSelector.replace(/link/g, 'a.ui-icon-close');
    jqSelf.off('click keyup', icoSel);

    this.getRowEditors().closest('tr')
            .find(' > div.ui-cell-editor > span > input')
            .unbind('keypress');

    // Clear YUI paginator
    if (this.cfg.paginator)
        this.cfg.paginator.destroy();

    var clientState = {scrollTop : this.scrollTop, scrollLeft : this.scrollLeft};
    ice.ace.DataTables[this.id] = clientState;
    window[this.cfg.widgetVar] = clientState;
}

ice.ace.DataTable.prototype.setupFilterEvents = function () {
    var _self = this;
    if (this.cfg.filterEvent == "enter") ice.ace.jq(this.filterSelector).unbind('keypress').bind('keypress', function (event) {
        event.stopPropagation();
        if (event.which == 13) {
            _self.filter(event);
            return false; // Don't run form level enter key handling
        }
    });
    else if (this.cfg.filterEvent == "change") ice.ace.jq(this.filterSelector).off('keyup').on('keyup', function (event) {
        var _event = event;
        if (event.which != 9) {
            if (_self.delayedFilterCall) clearTimeout(_self.delayedFilterCall);
            _self.delayedFilterCall = setTimeout(function () {
                _self.filter(_event);
            }, 400);
        }
    });
}

ice.ace.DataTable.prototype.setupPaginator = function () {
    if (!this.cfg.disabled) this.cfg.paginator.subscribe('changeRequest', this.paginate, null, this);
    this.cfg.paginator.render();
}

ice.ace.DataTable.prototype.setupSortRequest = function (_self, $this, event, headerClick, altY, altMeta) {
    var topCarat = $this.find(".ui-icon-triangle-1-n")[0],
        bottomCarat = $this.find(".ui-icon-triangle-1-s")[0],
        headerCell = (headerClick) ? $this : $this.parent().parent(),
        controlOffset = $this.offset(),
        controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22,
        descending = false,
        metaKey = (altMeta == undefined) ? (event.metaKey || event.ctrlKey ) : altMeta,
        ieOffset = ice.ace.jq.browser.msie ? 7 : 0,
        // altY and altMeta allow these event parameters to be optionally passed in
        // from an event triggering this event artificially
        eventY = (altY == undefined) ? event.pageY : altY;

    if (eventY > (controlOffset.top + (controlHeight / 2) - ieOffset))
        descending = true;

    if (headerClick) {
        if (ice.ace.jq(topCarat).hasClass('ui-toggled')) {
            descending = true;
        } else {
            descending = false;
        }
    }

    // If we are looking a freshly rendered DT initalize our JS sort state
    // from the state of the rendered controls
    if (_self.sortOrder.length == 0) {
        ice.ace.jq(this.sortControlSelector).each(function () {
            var $this = ice.ace.jq(this);
            if (ice.ace.getOpacity($this.find(' > span.ui-sortable-column-icon > a.ui-icon-triangle-1-n')[0]) == 1 ||
                ice.ace.getOpacity($this.find(' > span.ui-sortable-column-icon > a.ui-icon-triangle-1-s')[0]) == 1)
                _self.sortOrder.splice(
                    parseInt($this.find(' > span.ui-sortable-column-order').html()) - 1,
                    0,
                    $this.closest('.ui-header-column')
                );
        });
    }

    if (!metaKey || _self.cfg.singleSort) {
        // Remake sort criteria
        // Reset all other arrows
        _self.sortOrder = [];
        $this.closest('div.ui-header-column').siblings().find('> span > span > span.ui-sortable-column-icon > a').css('opacity', .2).removeClass('ui-toggled');
        headerCell.parent().siblings().find('> th > div.ui-header-column > span > span > span.ui-sortable-column-icon > a').css('opacity', .2).removeClass('ui-toggled');
    }

    var cellFound = false, index = 0;
    ice.ace.jq(_self.sortOrder).each(function () {
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
            _self.sortOrder.splice(headerCell.find('.ui-sortable-column-order').html() - 1, 1);
            //ice.ace.jq(bottomCarat).css('opacity', .2).removeClass('ui-toggled');
            //ice.ace.jq(topCarat).css('opacity', .2).removeClass('ui-toggled');
            if (!_self.cfg.singleSort) {
                headerCell.find('.ui-sortable-column-order').html('&#160;');
                var i = 0;
                ice.ace.jq(_self.sortOrder).each(function () {
                    this.find('.ui-sortable-column-order').html(parseInt(i++) + 1);
                });
            }
        } else {
            // Not a deselect, just a meta-toggle
            if (descending) {
                //ice.ace.jq(bottomCarat).css('opacity', 1).addClass('ui-toggled');
                //ice.ace.jq(topCarat).css('opacity', .2).removeClass('ui-toggled');
            } else {
                //ice.ace.jq(topCarat).css('opacity', 1).addClass('ui-toggled');
                //ice.ace.jq(bottomCarat).css('opacity', .2).removeClass('ui-toggled');
            }
        }
    } else {
        if (descending) {
            //ice.ace.jq(bottomCarat).css('opacity', 1).addClass('ui-toggled');
            //ice.ace.jq(topCarat).css('opacity', .2).removeClass('ui-toggled');
        } else {
            //ice.ace.jq(topCarat).css('opacity', 1).addClass('ui-toggled');
            //ice.ace.jq(bottomCarat).css('opacity', .2).removeClass('ui-toggled');
        }

        // add to sort order
        cellFound = false;
        ice.ace.jq(_self.sortOrder).each(function () {
            if (headerCell.attr('id') === this.attr('id')) {
                cellFound = true;
            }
        });
        if (cellFound == false) _self.sortOrder.push(headerCell);
    }
    // submit sort info
    _self.sort(_self.sortOrder);

    return false;
}

ice.ace.DataTable.prototype.setupSortEvents = function () {
    var _self = this;

    // Bind clickable header events
    if (_self.cfg.clickableHeaderSorting) {
        ice.ace.jq(this.sortColumnSelector)
            .unbind('click').bind("click", function (event) {
                var target = ice.ace.jq(event.target);

                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-s")[0]);
                selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

                // If the target of the event is not a layout element or
                // the target is a child of a sortable-control do not process event.
                if ((!(event.target.nodeName == 'SPAN') && !(event.target.nodeName == 'DIV') && !(event.target.nodeName == 'A')) ||
                    ((target.closest('.ui-sortable-control').length > 0) && !selectionMade))
                    return;

                _self.setupSortRequest(_self, ice.ace.jq(this), event, true);
            })
            .unbind('mousemove').bind('mousemove', function (event) {
                var target = ice.ace.jq(event.target);

                // If the target of the event is not a layout element do not process event.
                if ((!(event.target.nodeName == 'SPAN') && !(event.target.nodeName == 'DIV')
                    && !(event.target.nodeName == 'A'))) {
                    target.mouseleave();
                    return;
                }

                // if the target is a child of a sortable-control do not process the event
                if (target.closest('span.ui-sortable-control').length > 0) return;

                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);
                selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

                if (_self.cfg.clickableHeaderSorting && !selectionMade) {
                    topCarat.fadeTo(0, .66);
                } else if (!_self.cfg.clickableHeaderSorting) {
                    if (!topCarat.hasClass('ui-toggled')) topCarat.fadeTo(0, .66);
                    else bottomCarat.fadeTo(0, .66);
                }

                if ($this.closest('th').find('> hr').size() == 0)
                    $this.closest('th').addClass('ui-state-hover');
                else
                    $this.closest('div.ui-sortable-column').addClass('ui-state-hover');
            })
            .unbind('mouseleave').bind('mouseleave', function (event) {
                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);

                if (!bottomCarat.hasClass('ui-toggled'))
                    if (topCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                        bottomCarat.fadeTo(0, 0);
                    else bottomCarat.fadeTo(0, .33);

                if (!topCarat.hasClass('ui-toggled'))
                    if (bottomCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                        topCarat.fadeTo(0, 0);
                    else topCarat.fadeTo(0, .33);

                if ($this.closest('th').find('> hr').size() == 0)
                    $this.closest('th').removeClass('ui-state-hover');
                else
                    $this.closest('div.ui-sortable-column').removeClass('ui-state-hover');
            });
    }

    // Bind clickable control events
    ice.ace.jq(this.sortControlSelector)
        .unbind('click').bind("click", function (event, altY, altMeta) {
            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]),
                selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

            if ((_self.cfg.clickableHeaderSorting && !selectionMade) || (!_self.cfg.clickableHeaderSorting)) {
                _self.setupSortRequest(_self, ice.ace.jq(this), event, false, altY, altMeta);
                event.stopPropagation();
            }
        })
        .unbind('mousemove').bind('mousemove', function (event) {
            var target = ice.ace.jq(event.target);

            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]),
                controlOffset = $this.offset(),
                controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22,
                ieOffset = ice.ace.jq.browser.msie ? 4 : 0;

            if (!(_self.cfg.clickableHeaderSorting) || (!bottomCarat.hasClass('ui-toggled') && !topCarat.hasClass('ui-toggled'))) {
                if (event.pageY > (controlOffset.top + (controlHeight / 2) - ieOffset)) {
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
            }

            if (_self.cfg.clickableHeaderSorting)
                if ($this.closest('th').find('> hr').size() == 0)
                    $this.closest('th').addClass('ui-state-hover');
                else
                    $this.closest('div.ui-sortable-column').addClass('ui-state-hover');
        })
        .unbind('mouseleave').bind('mouseleave',function (event) {
            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);

            if (!bottomCarat.hasClass('ui-toggled'))
                if (topCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                    bottomCarat.fadeTo(0, 0);
                else bottomCarat.fadeTo(0, .33);

            if (!topCarat.hasClass('ui-toggled'))
                if (bottomCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                    topCarat.fadeTo(0, 0);
                else topCarat.fadeTo(0, .33);

            if ($this.closest('th').find('> hr').size() == 0)
                $this.closest('th').removeClass('ui-state-hover');
            else
                $this.closest('div.ui-sortable-column').removeClass('ui-state-hover');
        }).each(function () {
            // Prefade sort controls
            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);
            selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

            if (_self.cfg.clickableHeaderSorting && selectionMade) {
                if (!topCarat.hasClass('ui-toggled')) topCarat.fadeTo(0, 0);
                else bottomCarat.fadeTo(0, 0);
            } else {
                if (!topCarat.hasClass('ui-toggled')) topCarat.fadeTo(0, .33);
                if (!bottomCarat.hasClass('ui-toggled')) bottomCarat.fadeTo(0, .33);
            }
        });

    // Bind keypress kb-navigable sort icons
    ice.ace.jq(this.jqId)
        .off('keypress', this.sortUpSelector)
        .on('keypress', this.sortUpSelector, function (event) {
            if (event.which == 32 || event.which == 13) {
                var $currentTarget = ice.ace.jq(event.currentTarget);
                $currentTarget.closest('.ui-sortable-control')
                    .trigger('click', [$currentTarget.offset().top, event.metaKey]);
                return false;
            }
        });

    ice.ace.jq(this.jqId)
        .off('keypress', this.sortDownSelector)
        .on('keypress', this.sortDownSelector, function (event) {
            if (event.which == 32 || event.which == 13) {
                var $currentTarget = ice.ace.jq(event.currentTarget);
                $currentTarget.closest('.ui-sortable-control')
                    .trigger('click', [$currentTarget.offset().top + 6, event.metaKey]);
                return false;
            }
        });
}

ice.ace.DataTable.prototype.tearDownSelectionEvents = function () {
    var selectEvent = this.cfg.dblclickSelect ? 'dblclick' : 'click';
    var selector = this.isCellSelectionEnabled()
        ? this.cellSelector
        : this.rowSelector;

    ice.ace.jq(selector).die('dblclick').die('click').die('mouseenter');
}

ice.ace.DataTable.prototype.setupSelectionEvents = function () {
    var _self = this;
    var selectEvent = this.cfg.dblclickSelect ? 'dblclick' : 'click',
        selector = this.isCellSelectionEnabled()
            ? this.cellSelector
            : this.rowSelector;

    ice.ace.jq(selector)
        .css('cursor', 'pointer')
        .closest('table').bind('mouseleave',function () {
            if (!(_self.cfg.noiehover
                && ((ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7) ||
                (ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8)))) {
                var element = (_self.isCellSelectionEnabled() ? 'td' : 'tr');
                ice.ace.jq(this).find('tbody ' + element + ".ui-state-hover")
                    .removeClass('ui-state-hover');
            }
        }).find('thead').bind('mouseenter', function () {
            if (!(_self.cfg.noiehover
                && ((ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7) ||
                (ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8)))) {
                var element = (_self.isCellSelectionEnabled() ? 'td' : 'tr');
                ice.ace.jq(this).siblings().find(element + ".ui-state-hover")
                    .removeClass('ui-state-hover');
            }
        });
    ice.ace.jq(this.jqId)
        .off('mouseenter', selector)
        .on('mouseenter', selector, function () {
            if (!(_self.cfg.noiehover
                && ((ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7) ||
                (ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8)))) {
                var element = ice.ace.jq(this);
                if (!element.hasClass('dt-cond-row') &&
                    (!_self.isCellSelectionEnabled() || !element.parent().hasClass('dt-cond-row')))
                    element.addClass('ui-state-hover');

                element.siblings('.ui-state-hover')
                    .removeClass('ui-state-hover');
                if (_self.isCellSelectionEnabled()) {
                    element.parent().siblings().children('.ui-state-hover')
                        .removeClass('ui-state-hover');
                }
            }
        })
        .on(selectEvent, selector, function (event) {
            if (this.nodeName == 'TR') _self.onRowClick(event, this);
            else _self.onCellClick(event, this);
        });
}

ice.ace.DataTable.prototype.setupReorderableColumns = function () {
    var _self = this;
    ice.ace.jq(this.jqId + ' > div > table > thead').sortable({
        items:'th.ui-reorderable-col', helper:'clone',
        axis:'x', appendTo:this.jqId + ' thead',
        cursor:'move', placeholder:'ui-state-hover',
        cancel:'.ui-header-right, :input, button, .ui-tableconf-button, .ui-header-text'})
        .bind("sortstart", function (event, ui) {
            _self.reorderStart = ui.item.index();
        })
        .bind("sortstop", function (event, ui) {
            _self.reorderEnd = ui.item.index();
            if (_self.reorderStart != _self.reorderEnd)
                _self.reorderColumns(_self.reorderStart, _self.reorderEnd);
        });
}

ice.ace.DataTable.prototype.setupRowExpansionEvents = function () {
    var table = this;
    var selector = this.rowExpansionSelector;
    ice.ace.jq(this.jqId)
        .off('keyup click', selector)
        .on('keyup', selector, function (event) {
            if (event.which == 32 || event.which == 13) {
                table.toggleExpansion(this);
            }
        })
        .on('click', selector, function (event) {
            event.stopPropagation();
            table.toggleExpansion(this);
        });
}

ice.ace.DataTable.prototype.setupPanelExpansionEvents = function () {
    var table = this;
    var selector = this.panelExpansionSelector;
    ice.ace.jq(this.jqId)
        .off('keyup click', selector)
        .on('keyup', selector, function (event) {
            if (event.which == 32 || event.which == 13) {
                table.toggleExpansion(this);
            }
        })
        .on('click', selector, function (event) {
            event.stopPropagation();
            table.toggleExpansion(this);
        });
}

ice.ace.DataTable.prototype.setupScrolling = function () {
    var _self = this,
        delayedCleanUpResizeToken,
        delayedCleanUpResize = function () {
            _self.resizeScrolling();
        };

    this.resizeScrolling();

    // Persist scrolling position if one has been loaded from previous instance
    ice.ace.jq(this.scrollBodySelector)
        .scrollTop(this.scrollTop)
        .scrollLeft(this.scrollLeft);

    ice.ace.jq(this.scrollBodySelector).bind('scroll', function () {
        var $this = ice.ace.jq(this),
            $header = ice.ace.jq(_self.jqId + ' > div.ui-datatable-scrollable-header'),
            $footer = ice.ace.jq(_self.jqId + ' > div.ui-datatable-scrollable-footer'),
            scrollLeftVal = $this.scrollLeft(),
            scrollTopVal = $this.scrollTop();

        $header.scrollLeft(scrollLeftVal);
        $footer.scrollLeft(scrollLeftVal);
        _self.scrollLeft = scrollLeftVal;
        _self.scrollTop = scrollTopVal;
    });

    this.scrollableResizeCallback = function () {
        if (_self.parentSize != ice.ace.jq(_self.jqId).parent().width()) {
            _self.parentSize = ice.ace.jq(_self.jqId).parent().width();
            clearTimeout(delayedCleanUpResizeToken);
            delayedCleanUpResizeToken = setTimeout(delayedCleanUpResize, 100);
        }
    }

    ice.ace.jq(window).bind('resize', this.scrollableResizeCallback);

    //live scroll
    if (this.cfg.liveScroll) {
        var bodyContainer = ice.ace.jq(this.jqId + ' > div > table > tbody[display!=none]');
        this.scrollOffset = this.cfg.scrollStep;
        this.shouldLiveScroll = true;

        bodyContainer.scroll(function () {
            if (_self.shouldLiveScroll) {
                var $this = ice.ace.jq(this);
                var sTop = $this.scrollTop(), sHeight = this.scrollHeight, viewHeight = $this.height();
                if (sTop >= (sHeight - viewHeight)) _self.loadLiveRows();
            }
        });
    }
}

ice.ace.DataTable.prototype.setupResizableColumns = function () {
    //Add resizers
    ice.ace.jq(this.jqId + ' thead :not(th:last)').children('.ui-header-column').append('<div class="ui-column-resizer"></div>');

    //Setup resizing
    this.columnWidthsCookie = this.id + '_columnWidths',
        resizers = ice.ace.jq(this.jqId + ' > div > table > thead > tr > th > div > div.ui-column-resizer'),
        columns = ice.ace.jq(this.jqId + ' > div > table > thead > tr > th'),
        _self = this;

    resizers.draggable({
        axis:'x',
        drag:function (event, ui) {
            var column = ui.helper.closest('th'),
                newWidth = ui.position.left + ui.helper.outerWidth();

            column.css('width', newWidth);
        },
        stop:function (event, ui) {
            ui.helper.css('left', '');

            var columnWidths = [];

            columns.each(function (i, item) {
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

ice.ace.DataTable.prototype.resizeScrolling = function () {
    var scrollableTable = ice.ace.jq(this.jqId),
        resizableTableParents = scrollableTable.parents('.ui-datatable-scrollable');

    // If our parents are resizeable tables, allow them to resize before I resize myself
    if (resizableTableParents.size() > 0) {
        if (!this.parentResizeDelaySet) {
            this.parentResizeDelaySet = true;
            var _self = this;
            setTimeout(function () {
                _self.resizeScrolling()
            }, resizableTableParents.size() * 5);
            return;
        }
    }

    // Reattempt resize in 100ms if I or a parent of mine is currently hidden.
    // Sizing will not be accurate if the table is not being displayed, like at tabset load.
    // Hidden is true if any ancestors are hidden.
    if (scrollableTable.is(':hidden')) {
        if (!this.cfg.disableHiddenSizing) {
            var _self = this;
            setTimeout(function () {
                _self.resizeScrolling()
            }, 100);
        }
    } else {
        var headerTable = scrollableTable.find(' > div.ui-datatable-scrollable-header > table'),
            footerTable = scrollableTable.find(' > div.ui-datatable-scrollable-footer > table'),
            bodyTable = scrollableTable.find(' > div.ui-datatable-scrollable-body > table'),
            dupeHead = bodyTable.find(' > thead'),
            dupeFoot = bodyTable.find(' > tfoot');

        var dupeHeadCols = dupeHead.find('th > div.ui-header-column').get().reverse();
        var dupeFootCols = dupeFoot.find('td > div.ui-footer-column').get().reverse();

        var realHeadCols = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-header:first > table > thead > tr > th > .ui-header-column').get().reverse();
        var realFootCols = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-footer:first > table > tfoot > tr > td > .ui-footer-column').get().reverse();
        var bodySingleCols = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body:first > table > tbody > tr:visible:not(.dt-cond-row):first > td > div').get().reverse();
        var bodyFirstConditional = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body:first > table > tbody > tr:visible:first').is('.dt-cond-row');

        // Reset overflow if it was disabled as a hack from previous sizing
        var bodyTableParent = bodyTable.parent().css('overflow', '');
        headerTable.parent().css('overflow', '');
        footerTable.parent().css('overflow', '');
        headerTable.css('width', '');
        footerTable.css('width', '');

        // Reset fixed sizing if set by previous sizing.
        for (var i = 0; i < bodySingleCols.length; i++)
            ice.ace.jq(bodySingleCols[i]).css('width', 'auto');

        // Reset padding if added to offset scrollbar issues
        bodyTableParent.css('padding-right', '');

        var unsizedVScrollShown = bodyTableParent.is(':scrollable(vertical)'),
            unsizedBodyVScrollShown = ice.ace.jq('html').is(':scrollable(vertical)');

        // Show Duplicate Header / Footer
        dupeHead.css('display', 'table-header-group');
        dupeFoot.css('display', 'table-footer-group');

        // Get Duplicate Header/Footer Sizing
        var dupeHeadColumn, dupeFootColumn, dupeHeadColumnWidths = [], bodySingleColWidths = [], dupeFootColumnWidths = [], realHeadColumn, realFootColumn, bodyColumn,
            safari = ice.ace.jq.browser.safari,
            chrome = ice.ace.jq.browser.chrome,
            mac = ice.ace.jq.browser.os == 'mac',
            ie7 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7,
            ie8 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8,
            ie9 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 9,
            ie8as7 = false,
            firefox = ice.ace.jq.browser.mozilla;

        if (this.cfg.scrollIE8Like7 && ie8) {
            ie7 = true;
            ie8 = false;
            ie8as7 = true;
        }

        // If duplicate header/footer row causes body table to barely
        // exceed min-table size (causing scrollbar)
        var dupeCausesScrollChange = false,
            dupeCausesBodyScrollChange = false,
            vScrollShown = bodyTable.parent().is(':scrollable(vertical)'),
            bodyVScrollShown = ice.ace.jq('html').is(':scrollable(vertical)');

        if (!unsizedVScrollShown && vScrollShown)
            dupeCausesScrollChange = true;

        if (!unsizedBodyVScrollShown && bodyVScrollShown)
            dupeCausesBodyScrollChange = true;

        // Change table rendering algorithm to get more accurate sizing
        if (!ie7) bodyTable.css('table-layout', 'auto');

        // IE7 scrollbar fix
        if (bodyTable.size() > 0 && ie7 && bodyTable.parent().is(':scrollable(vertical)')) {
            bodyTable.parent().css('overflow-x', 'hidden');
            bodyTable.parent().css('padding-right', '17px');
            headerTable.parent().css('padding-right', '17px');
            footerTable.parent().css('padding-right', '17px');
        }

        // Return overflow to visible so sizing doesn't have scrollbar errors
        if (dupeCausesScrollChange) {
            bodyTable.parent().css('overflow', 'visible');
        }
        if (dupeCausesBodyScrollChange) {
            ice.ace.jq('html').css('overflow', 'hidden');
        }

        // Get Duplicate Sizing
        if (!ie7) {
            for (var i = 0; i < dupeHeadCols.length; i++) {
                dupeHeadColumn = ice.ace.jq(dupeHeadCols[i]);
                dupeHeadColumnWidths[i] = dupeHeadColumn.width();
            }

            for (var i = 0; i < bodySingleCols.length; i++) {
                bodyColumn = ice.ace.jq(bodySingleCols[i]);
                bodySingleColWidths[i] = bodyColumn.width();
            }

            for (var i = 0; i < dupeFootCols.length; i++) {
                dupeFootColumn = ice.ace.jq(dupeFootCols[i]);
                dupeFootColumnWidths[i] = dupeFootColumn.width();
            }
        }


        // Return overflow value
        if (dupeCausesScrollChange) {
            bodyTable.parent().css('overflow', '');
        }
        if (dupeCausesBodyScrollChange) {
            ice.ace.jq('html').css('overflow', '');
        }

        if (ie7) {
            headerTable.css('table-layout', 'fixed');
            bodyTable.css('table-layout', 'fixed');
            footerTable.css('table-layout', 'fixed');
        }

        // Set Duplicate Sizing
        if (!ie7) for (var i = 0; i < bodySingleCols.length; i++) {
            bodyColumn = ice.ace.jq(bodySingleCols[i]);

            // Work around webkit bug described here: https://bugs.webkit.org/show_bug.cgi?id=13339
            var bodyColumnWidth = (safari && ice.ace.jq.browser.version < 6)
                ? bodySingleColWidths[i] + parseInt(bodyColumn.parent().css('padding-right')) + parseInt(bodyColumn.parent().css('padding-left')) + 1
                : bodySingleColWidths[i];

            // Set Duplicate Header Sizing to Body Columns
            // Equiv of max width
            bodyColumn.parent().width(bodyColumnWidth);
            // Equiv of min width
            bodyColumnWidth = i == 0 ? bodySingleColWidths[i] - 1 : bodySingleColWidths[i];
            bodyColumn.width(bodyColumnWidth);
        }

        if (!ie7) for (var i = 0; i < realHeadCols.length; i++) {
            realHeadColumn = ice.ace.jq(realHeadCols[i]);

            var realHeadColumnWidth = dupeHeadColumnWidths[i];

            // Set Duplicate Header Sizing to True Header Columns
            realHeadColumn.width(realHeadColumnWidth);
            // Apply same width to stacked sibling columns
            realHeadColumn.siblings('.ui-header-column').width(realHeadColumnWidth);
            // Equiv of max width
            realHeadColumn.parent().width(realHeadColumnWidth);
        }

        if (!ie7) for (var i = 0; i < realFootCols.length; i++) {
            realFootColumn = ice.ace.jq(realFootCols[i]);

            // Work around webkit bug described here: https://bugs.webkit.org/show_bug.cgi?id=13339
            var realFootColumnWidth = (safari && ice.ace.jq.browser.version < 6)
                ? dupeFootColumnWidths[i] + parseInt(realFootColumn.parent().css('padding-right')) + parseInt(realFootColumn.parent().css('padding-left'))
                : dupeFootColumnWidths[i];

            // Set Duplicate Header Sizing to True Header Columns
            realFootColumn.parent().width(realFootColumnWidth);
            realFootColumn.width(realFootColumnWidth);
            // Apply same width to stacked sibling columns
            realFootColumn.siblings('.ui-footer-column').width(realFootColumnWidth);
        }

        // Browser / Platform specific scrollbar fixes
        // Fix body scrollbar overlapping content
        // Instance check to prevent IE7 dynamic scrolling change errors
        // Recheck scrollable, it may have changed again post resize
        if (vScrollShown && bodyTable.parent().is(':scrollable(vertical)')) {
            if (((firefox) || ((safari || chrome) && !mac) || (ie9 || ie8)) && !dupeCausesScrollChange) {
                var offset = firefox ? 14 : 17;
                headerTable.parent().css('margin-right', offset + 'px');
                footerTable.parent().css('margin-right', offset + 'px');
            }
            else if (dupeCausesScrollChange) {
                /* Correct scrollbars added when unnecessary. */
                if (safari || chrome || firefox || ie9) {
                    bodyTable.parent().css('overflow', 'visible');
                    headerTable.parent().css('overflow', 'visible');
                    footerTable.parent().css('overflow', 'visible');
                    headerTable.css('width', '100%');
                    headerTable.css('table-layout', '');
                    footerTable.css('width', '100%');
                    footerTable.css('table-layout', '');
                }

                /* Clean up IE 8/9 sizing bug in dupe scroll change case */
                headerTable.parent().css('margin-right', '');
                footerTable.parent().css('margin-right', '');
                headerTable.find('tr th:last').css('padding-right', '');
                footerTable.find('tr td:last').css('padding-right', '');
            }
        } else {
            headerTable.parent().css('margin-right', '');
            footerTable.parent().css('margin-right', '');
            headerTable.find('tr th:last').css('padding-right', '');
            footerTable.find('tr td:last').css('padding-right', '');
        }

        // If the body of the table starts with a conditonal row, duplicate the first non
        // conditional row and insert it before the conditionals with styling to hide it,
        // while still keeping it in flow to set the table sizing.
        if (bodyFirstConditional) {
            var firstNonCond = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body:first > table > tbody > tr:not(.dt-cond-row):first');

            firstNonCond.clone().attr('id', this.id + '_condAlgnr_'+Math.floor((Math.random()*100)+1))
                    .css('visibility', 'hidden').prependTo(bodyTable.find('> tbody'));

            bodyTable.css('margin-top',0 - firstNonCond.height());
        }

        // Hide Duplicate Segments
        dupeHead.css('display', 'none');
        dupeFoot.css('display', 'none');
    }
}

ice.ace.DataTable.prototype.setupDisabledStyling = function () {
    // Fade out controls
    ice.ace.jq(this.jqId + ' > table > tbody.ui-datatable-data > tr > td a.ui-row-toggler, ' +
        this.jqId + ' > table > thead > tr > th > div > input.ui-column-filter, ' +
        this.jqId + ' > table > thead > tr > th > div.ui-sortable-column span.ui-sortable-control:first, ' +
        this.jqId + ' > table > tbody.ui-datatable-data > tr > td > div.ui-row-editor span.ui-icon'
    ).css({opacity:0.4});

    // Add pagination disabled style
    ice.ace.jq(this.jqId + ' > .ui-paginator .ui-icon, ' +
        this.jqId + ' > .ui-paginator .ui-paginator-current-page, ' +
        this.jqId + ' > table > thead .ui-tableconf-button a').each(function () {
            ice.ace.jq(this).parent().addClass('ui-state-disabled');
        });

    // Disable filter text entry
    ice.ace.jq(this.jqId + ' > table > thead > tr > th > div > input.ui-column-filter').keypress(function () {
        return false;
    });

    // Row style
    ice.ace.jq(this.jqId + ' > table > tbody.ui-datatable-data:first > tr > td')
        .css({backgroundColor:'#EDEDED', opacity:0.8});
}


/* #########################################################################
 ############################### Requests ################################
 ######################################################################### */
ice.ace.DataTable.prototype.reorderColumns = function (oldIndex, newIndex) {
    var options = {
        source:this.id,
        execute:this.id,
        render:(this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        formId:this.cfg.formId
    };

    var params = {},
        _self = this;
    params[this.id + '_columnReorder'] = oldIndex + '-' + newIndex;

    options.params = params;
    options.onsuccess = function (responseXML) {
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.reorder) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.reorder,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadLiveRows = function () {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        _self = this;

    options.onsuccess = function (responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function (id, content) {
            if (id == _self.id) {
                ice.ace.jq(_self.jqId + ' div.ui-datatable-scrollable-body:first > table > tbody > tr:last').after(content);
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

ice.ace.DataTable.prototype.paginate = function (newState) {
    var options = {
        source:this.id,
        render:this.id,
        execute:this.id,
        formId:this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function (responseXML) {
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
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.page,
                ice.ace.removeExecuteRenderOptions(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.sort = function (headerCells) {
    var options = {
        source:this.id,
        render:(this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        execute:this.id,
        formId:this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.resizeScrolling();
        _self.setupSortEvents();
        return false;
    };

    var params = {}, sortDirs = [], sortKeys = [];
    params[this.id + "_sorting"] = true;
    ice.ace.jq.each(headerCells, function () {
        sortKeys.push(ice.ace.jq(this).attr('id'));
    });
    params[this.id + "_sortKeys"] = sortKeys;
    ice.ace.jq.each(headerCells, function () {
        // Have to "refind" the elements by id, as in IE browsers, the dom
        // elements referenced by headerCells return undefined for
        // .hasClass('ui-toggled')
        sortDirs.push(ice.ace.jq(ice.ace.escapeClientId(ice.ace.jq(this).attr('id')))
            .find('a.ui-icon-triangle-1-n').hasClass('ui-toggled'));
    });
    params[this.id + "_sortDirs"] = sortDirs;

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.sort) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.sort,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.filter = function (evn) {
    var options = {
        source:this.id,
        render:(this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        execute:this.id,
        formId:this.cfg.formId
    };

    var _self = this;
    this.filterSource = (evn.target) ? evn.target : evn.srcElement;

    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement,
            updates = xmlDoc.getElementsByTagName("extension");

        var paginator = _self.cfg.paginator;
        if (paginator) {
            var extensions = xmlDoc.getElementsByTagName("extension"),
                totalRecords = _self.cfg.paginator.getTotalRecords();

            for (var i = 0; i < extensions.length; i++) {
                var callbackParam = extensions[i].attributes.getNamedItem("aceCallbackParam");
                if ((callbackParam) && (callbackParam.nodeValue == 'totalRecords')) {
                    totalRecords = ice.ace.jq.parseJSON(extensions[i].firstChild.data).totalRecords;

                    //Reset paginator state
                    paginator.setPage(1, true);
                    paginator.setTotalRecords(totalRecords, true);
                }
            }
        }

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
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.filter,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
    ice.setFocus('');
}

ice.ace.DataTable.prototype.doSelectionEvent = function (type, deselection, element) {
    // Get Id(s) //
    var targetId, deselectedId, firstRowSelected;
    if (type == 'row') {
        targetId = element.attr('id').split('_row_')[1];
    }
    else if (type == 'cell') {
        var rowId = element.parent().attr('id').split('_row_')[1],
            columnIndex = element.index();
        targetId = rowId + '#' + columnIndex;
    }

    var firstRowSelected = ice.ace.jq(element).closest('tr').parent().children(':first').hasClass('ui-selected');

    // Sync State //
    this.readSelections();

    // Adjust State //
    if (!deselection) {
        if (this.isSingleSelection()) {
            // If single selection unselect previous selection
            if (type == 'row')
                element.siblings('.ui-selected').removeClass('ui-selected ui-state-active');
            else if (type == 'cell')
                ice.ace.jq(this.jqId + ' tbody.ui-datatable-data:first > tr > td').removeClass('ui-selected ui-state-active');

            // Add current selection to deselection delta
            this.deselection = [];
            deselectedId = this.selection[0];
            this.deselection.push(deselectedId);

            // The new selection will be the only member of the delta
            this.selection = [];
        }

        // Add selected styling
        element.addClass('ui-state-active ui-selected');
        // Filter id from deselection delta
        this.deselection = ice.ace.jq.grep(this.deselection, function (r) {
            return r != targetId;
        });
        // Add filter id to selection delta
        this.selection.push(targetId);
    } else {
        // Remove selected styling
        element.removeClass('ui-selected ui-state-active');
        // Remove from selection
        this.selection = ice.ace.jq.grep(this.selection, function (r) {
            return r != targetId;
        });
        // Add to deselection
        this.deselection.push(targetId);
    }

    // Write State //
    this.writeSelections();

    // Submit State //
    if (this.cfg.instantSelect) {
        var options = {
            source:this.id,
            execute:this.id,
            formId:this.cfg.formId
        };

        var params = {},
            _self = this;

        if (type == 'row') {
            if (!deselection) {
                // Submit selected index and deselection if single selection enabled
                params[this.id + '_instantSelectedRowIndexes'] = targetId;
                if (deselectedId) params[this.id + '_instantUnselectedRowIndexes'] = deselectedId;
            } else {
                // Submit deselected index
                params[this.id + '_instantUnselectedRowIndexes'] = targetId;
            }
        }

        // If first row is in this selection, deselection, or will be implicitly deselected by singleSelection
        // resize the scrollable table.
        if (_self.cfg.scrollable && (ice.ace.jq.inArray("0", this.selection) > -1 || ice.ace.jq.inArray("0", this.deselection) > -1 || (firstRowSelected && this.isSingleSelection()))) {
            options.onsuccess = function (responseXML) {
                _self.resizeScrolling();
                return false;
            };
        }


        options.params = params;

        if (this.behaviors)
            if (this.behaviors.select && !deselection) {
                ice.ace.ab(ice.ace.extendAjaxArguments(
                    this.behaviors.select,
                    ice.ace.removeExecuteRenderOptions(options)
                ));
                return;
            } else if (this.behaviors.deselect && deselection) {
                ice.ace.ab(ice.ace.extendAjaxArguments(
                    this.behaviors.deselect,
                    ice.ace.removeExecuteRenderOptions(options)
                ));
                return;
            }

        ice.ace.AjaxRequest(options);
    }
}

ice.ace.DataTable.prototype.onRowClick = function (event, rowElement) {
    //Check if rowclick triggered this event not an element in row content
    if (ice.ace.jq(event.target).is('td,span,div')) {
        var row = ice.ace.jq(rowElement);

        if (!this.isSingleSelection() && event.shiftKey && this.lastClickedIndex > -1)
            this.doMultiRowSelectionEvent(this.lastClickedIndex, row);
        else if (row.hasClass('ui-selected'))
            this.doSelectionEvent('row', true, row);
        else
            this.doSelectionEvent('row', false, row);

        this.lastClickedIndex = row.index();
    }
}

ice.ace.DataTable.prototype.onCellClick = function (event, cellElement) {
    //Check if rowclick triggered this event not an element in row content
    if (ice.ace.jq(event.target).is('div,td,span')) {
        var cell = ice.ace.jq(cellElement);
        if (cell.hasClass('ui-selected')) this.doSelectionEvent('cell', true, cell);
        else this.doSelectionEvent('cell', false, cell);
    }
}

ice.ace.DataTable.prototype.doMultiRowSelectionEvent = function (lastIndex, current) {
    var self = this,
        tbody = current.closest('tbody'),
        last = ice.ace.jq(tbody.children().get(lastIndex)),
        lower = current.index() < lastIndex,
        elemRange = lower ? last.prevUntil(current.prev()) : last.nextUntil(current.next()),
        deselectedId, firstRowSelected;

    // Sync State //
    self.readSelections();

    elemRange.each(function (i, elem) {
        var element = ice.ace.jq(elem),
            targetId = element.attr('id').split('_row_')[1];

        // Adjust State //
        element.addClass('ui-state-active ui-selected');
        self.deselection = ice.ace.jq.grep(self.deselection, function (r) {
            return r != targetId;
        });
        self.selection.push(targetId);
    });

    // Write State //
    self.writeSelections();

    // Submit State //
    if (self.cfg.instantSelect) {
        var options = {
            source:self.id,
            execute:self.id,
            formId:self.cfg.formId
        };

        var params = {};
        params[self.id + '_instantSelectedRowIndexes'] = this.selection;

        var firstRowSelected = tbody.children(':first').hasClass('ui-selected');

        // If first row is in this selection, deselection, or will be implicitly deselected by singleSelection
        // resize the scrollable table.
        if (self.cfg.scrollable && (ice.ace.jq.inArray("0", self.selection) > -1 || ice.ace.jq.inArray("0", self.deselection) > -1 || (firstRowSelected && self.isSingleSelection()))) {
            options.onsuccess = function (responseXML) {
                self.resizeScrolling();
                return false;
            };
        }

        options.params = params;

        if (this.behaviors)
            if (this.behaviors.select) {
                ice.ace.ab(ice.ace.extendAjaxArguments(
                    this.behaviors.select,
                    ice.ace.removeExecuteRenderOptions(options)
                ));
                return;
            }

        ice.ace.AjaxRequest(options);
    }
}


/* #########################################################################
 ########################### Expansion ###################################
 ######################################################################### */
ice.ace.DataTable.prototype.toggleExpansion = function (expanderElement) {
    var expander = ice.ace.jq(expanderElement),
        row = expander.closest('tr'),
        expanded = row.hasClass('ui-expanded-row');
    $this = (this);

    if (expanded) {
        var removeTargets = row.siblings('[id^="' + row.attr('id') + '."]');
        if (removeTargets.size() == 0) removeTargets = row.next('tr.ui-expanded-row-content');
        expander.removeClass('ui-icon-circle-triangle-s');
        expander.addClass('ui-icon-circle-triangle-e');
        row.removeClass('ui-expanded-row');
        removeTargets.fadeOut(function () {
            ice.ace.jq(this).remove();
        });
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

ice.ace.DataTable.prototype.sendPanelContractionRequest = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.contract) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.contract,
                ice.ace.removeExecuteRenderOptions(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.sendRowContractionRequest = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    ;
    options.params = params;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.contract) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.contract,
                ice.ace.removeExecuteRenderOptions(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedRows = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1],
        _self = this;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.expand) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.expand,
                ice.ace.removeExecuteRenderOptions(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedPanelContent = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1],
        _self = this;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.expand) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.expand,
                ice.ace.removeExecuteRenderOptions(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}


/* #########################################################################
 ########################### Row Editing #################################
 ######################################################################### */
ice.ace.DataTable.prototype.showEditors = function (element) {
    this.doRowEditShowRequest(element);
}

ice.ace.DataTable.prototype.saveRowEdit = function (element) {
    this.doRowEditSaveRequest(element);
}

ice.ace.DataTable.prototype.cancelRowEdit = function (element) {
    this.doRowEditCancelRequest(element);
}

ice.ace.DataTable.prototype.doRowEditShowRequest = function (element) {
    var row = ice.ace.jq(element).closest('tr'),
        rowEditorId = row.find('> td > div.ui-row-editor, > td > div > div.ui-row-editor').attr('id'),
        options = {
            source:rowEditorId,
            execute:'@this',
            formId:this.cfg.formId
        },
        _self = this,
        cellsToRender = new Array();

    row.find('> td > div.ui-cell-editor, > td > div > div.ui-cell-editor').each(function () {
        cellsToRender.push(ice.ace.jq(this).attr('id'));
    });
    options.render = cellsToRender.join(' ');
    options.render = options.render + " @this";

    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement;

        _self.args = {};

        if (_self.cfg.scrollable)
            _self.resizeScrolling();

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_editShow'] = row.attr('id').split('_row_')[1];
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editStart) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.editStart,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.doRowEditCancelRequest = function (element) {
    var row = ice.ace.jq(element).closest('tr'),
        rowEditorId = row.find('> td > div.ui-row-editor, > td > div > div.ui-row-editor').attr('id'),
        options = {
            source:rowEditorId,
            execute:'@this',
            formId:this.cfg.formId
        },
        _self = this,
        editorsToProcess = new Array();

    row.find('> td > div.ui-cell-editor, > td > div > div.ui-cell-editor').each(function () {
        editorsToProcess.push(ice.ace.jq(this).attr('id'));
    });
    options.render = editorsToProcess.join(' ');
    options.render = options.render + " @this";

    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement;

        _self.args = {};

        if (_self.cfg.scrollable)
            _self.resizeScrolling();

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_editCancel'] = row.attr('id').split('_row_')[1];
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editCancel) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.editCancel,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.doRowEditSaveRequest = function (element) {
    var row = ice.ace.jq(element).closest('tr'),
        rowEditorId = row.find('> td > div.ui-row-editor, > td > div > div.ui-row-editor').attr('id'),
        options = {
            source:rowEditorId,
            formId:this.cfg.formId
        },
        _self = this,
        editorsToProcess = new Array();

    row.find('> td > div.ui-cell-editor, > td > div > div.ui-cell-editor').each(function () {
        editorsToProcess.push(ice.ace.jq(this).attr('id'));
    });
    options.execute = editorsToProcess.join(' ');
    options.execute = options.execute + " @this";
    options.render = options.execute;

    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement,
            extensions = xmlDoc.getElementsByTagName("extension");

        _self.args = {};
        for (i = 0; i < extensions.length; i++) {
            var extension = extensions[i];
            if (extension.getAttributeNode('aceCallbackParam')) {
                var jsonObj = ice.ace.jq.parseJSON(extension.firstChild.data);

                for (var paramName in jsonObj)
                    if (paramName) _self.args[paramName] = jsonObj[paramName];
            }
        }

        if (!_self.args.validationFailed)
            if (_self.cfg.scrollable) _self.resizeScrolling();

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_editSubmit'] = row.attr('id').split('_row_')[1];

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editSubmit) {
            ice.ace.ab(ice.ace.extendAjaxArguments(
                this.behaviors.editSubmit,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.getRowEditors = function () {
    return ice.ace.jq(this.cellEditorSelector.replace(/link/g, ''));
}

ice.ace.DataTable.prototype.setupCellEditorEvents = function (rowEditors) {
    var _self = this;

    // unbind and rebind these events.
    var showEditors = function (event) {
            event.stopPropagation();
            _self.showEditors(event.target);
        },
        saveRowEditors = function (event) {
            event.stopPropagation();
            _self.saveRowEdit(event.target);
        },
        cancelRowEditors = function (event) {
            event.stopPropagation();
            _self.cancelRowEdit(event.target);
        },
        inputCellKeypress = function (event) {
            if (event.which == 13) return false;
        };
    var selector = this.cellEditorSelector;

    var icoSel = selector.replace(/link/g, 'a.ui-icon-pencil');
    ice.ace.jq(this.jqId).off('click keyup', icoSel)
            .on('click', icoSel, showEditors)
            .on('keyup', icoSel, function (event) {
        if (event.which == 32 || event.which == 13) {
            showEditors(event);
        }
    });

    icoSel = selector.replace(/link/g, 'a.ui-icon-check');
    ice.ace.jq(this.jqId).off('click keyup', icoSel)
            .on('click', icoSel, saveRowEditors)
            .on('keyup', icoSel, function (event) {
        if (event.which == 32 || event.which == 13) {
            saveRowEditors(event);
        }
    });

    icoSel = selector.replace(/link/g, 'a.ui-icon-close');
    ice.ace.jq(this.jqId).off('click keyup', icoSel)
            .on('click', icoSel, cancelRowEditors)
            .on('keyup', icoSel, function (event) {
        if (event.which == 32 || event.which == 13) {
            cancelRowEditors(event);
        }
    });

    rowEditors.closest('tr').find(' > div.ui-cell-editor > span > input')
            .bind('keypress', inputCellKeypress);
}


/* #########################################################################
 ########################## Selection Helpers ############################
 ######################################################################### */
ice.ace.DataTable.prototype.writeSelections = function () {
    // Writes selection state to hidden field for submission
    ice.ace.jq(this.selectionHolder).val(this.selection.join(','));
    ice.ace.jq(this.deselectionHolder).val(this.deselection.join(','));
};

ice.ace.DataTable.prototype.readSelections = function () {
    // Reading clears JS selected state following delta field submissions
    var selectionVal = ice.ace.jq(this.selectionHolder).val(),
        deselectionVal = ice.ace.jq(this.deselectionHolder).val();
    this.selection = (selectionVal == '') ? [] : selectionVal.split(',');
    this.deselection = (deselectionVal == '') ? [] : deselectionVal.split(',');
}

ice.ace.DataTable.prototype.isSingleSelection = function () {
    return this.cfg.selectionMode == 'single' || this.cfg.selectionMode === 'singlecell';
};

ice.ace.DataTable.prototype.isSelectionEnabled = function () {
    return this.cfg.selectionMode == 'single' || this.cfg.selectionMode == 'multiple' || this.isCellSelectionEnabled();
};

ice.ace.DataTable.prototype.isCellSelectionEnabled = function () {
    return this.cfg.selectionMode === 'singlecell' || this.cfg.selectionMode === 'multiplecell';
};

ice.ace.DataTable.prototype.clearSelection = function () {
    this.selection = [];
    ice.ace.jq(this.selectionHolder).val('');
};
