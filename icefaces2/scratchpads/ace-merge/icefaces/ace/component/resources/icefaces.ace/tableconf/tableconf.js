if (!window.ice['ace']) {
    window.ice.ace = {};
}
ice.ace.TableConf = function (id, cfg) {
    this.id = ice.ace.escapeClientId(id.replace("_tableconf", ""));
    this.cfg = cfg;
    this.sortOrder = [];
    $(this.id+'_tableconf').draggable();

    if (cfg.reorderable) {
        // Return a helper with preserved width of cells
        var fixHelper = function(e, ui) {
            ui.children().each(function() {
                $(this).width($(this).width());
            });
            return ui;
        };


        $(this.id+'_tableconf > .ui-tableconf-body > table > tbody')
                .sortable({
                    axis:'y',
                    containment:'parent',
                    helper: fixHelper,
                    handle: '.ui-sortable-handle'
                });
    }

    if (cfg.sortable) {
        var _self = this;
         $(this.id+'_tableconf > .ui-tableconf-body')
                .find('.ui-sortable-control')
                .click(function(event, altY, altMeta) {
                    event.stopPropagation();
                    var $this = $(this),
                        topCarat = $this.find(".ui-icon-triangle-1-n")[0],
                        bottomCarat = $this.find(".ui-icon-triangle-1-s")[0],
                        controlCell =  $this.parent().parent(),
                        controlRow  =  $(controlCell).parent(),
                        controlOffset = $this.offset(),
                        controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22,
                        descending = false,
                        metaKey = (altMeta == undefined) ? event.metaKey : altMeta;

                    // altY and altMeta allow these event parameters to be optionally passed in
                    // from an event triggering this event artificially
                    var eventY = (altY == undefined) ? event.pageY : altY;
                    if (eventY > (controlOffset.top + (controlHeight / 2)+2))
                        descending = true;

                     if (!metaKey || _self.cfg.singleSort) {
                         // Remake sort criteria
                         // Reset all other arrows
                         _self.sortOrder = [];
                         controlRow.siblings()
                                 .find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s')
                                 .animate({opacity : .2}, 200)
                                 .removeClass('ui-toggled');

                         if (!_self.cfg.singleSort) controlRow.siblings()
                                 .find('.ui-sortable-column-order')
                                 .html('&#160;');
                         // remove previous gradients
                         //headerCell.siblings().removeClass('ui-state-active').find('.ui-sortable-column-icon').removeClass('ui-icon-triangle-1-n ui-icon-triangle-1-s');
                     }

                    var rowFound = false;
                    $(_self.sortOrder).each(function() {
                        if (controlRow.attr('class') === this.attr('class')) { rowFound = true; } });

                    // if meta clicking a currently sorted row
                    if (metaKey && rowFound) {
                        // if deselecting
                        if ((getOpacity(topCarat) == 1 && !descending) ||
                            (getOpacity(bottomCarat) == 1 && descending)) {
                             // Remove from sort order
                             _self.sortOrder.splice(controlCell.find('.ui-sortable-column-order').html()-1,1);
                             $(bottomCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                             $(topCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                             if (!_self.cfg.singleSort) {
                                 controlCell.find('.ui-sortable-column-order').html('&#160;');
                                 var i = 0;
                                 $(_self.sortOrder).each(function(){
                                    this.find('.ui-sortable-column-order').html(parseInt(i++)+1);
                                 });
                             }
                        } else {
                            // Not a deselect, just meta toggle
                            if (descending) {
                                $(bottomCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                                $(topCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                            } else {
                                $(topCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                                $(bottomCarat).animate({opacity : .2},  200).removeClass('ui-toggled');
                            }
                        }
                    // if not a deselect
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
                         var rowFound = false;
                         $(_self.sortOrder).each(
                                 function() { if (controlRow.attr('class') === this.attr('class')) { rowFound = true; } });
                         if (!rowFound) {
                             var order = _self.sortOrder.push(controlRow);
                             // write control display value
                             if (!_self.cfg.singleSort) controlCell.find('.ui-sortable-column-order').html(order);
                         }
                    }
                })
                .find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s')
                .animate({opacity : .2},  200);

        // Pre-fade and bind keypress to kb-navigable sort icons
        $(this.id+'_tableconf > .ui-tableconf-body .ui-sortable-control')
                .find('.ui-icon-triangle-1-n')
                .fadeTo(0, 0.2)
                .keypress(function(event) {
                    if (event.which == 32 || event.which == 13) {
                        var $currentTarget = $(event.currentTarget);
                        $currentTarget.closest('.ui-sortable-control')
                                .trigger('click', [$currentTarget.offset().top, event.metaKey]);
                }});
        $(this.id+'_tableconf > .ui-tableconf-body .ui-sortable-control')
                .find('.ui-icon-triangle-1-s')
                .fadeTo(0, 0.2)
                .keypress(function(event) {
                    if (event.which == 32 || event.which == 13) {
                        var $currentTarget = $(event.currentTarget);
                        $currentTarget.closest('.ui-sortable-control')
                                .trigger('click', [$currentTarget.offset().top + 6, event.metaKey]);
                }});
    }

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

ice.ace.TableConf.prototype.getSortAscending= function() {
    var sortAsc = {}, maxOrder = 0;
    $(this.id+'_tableconf tr[class^="ui-tableconf-row"] .ui-sortable-column-icon').each(function(i, val) {
        var $val = $(val),
            $order = $($($val.closest('.ui-sortable-control')).find('.ui-sortable-column-order')[0]),
            topCarat = $val.find('.ui-icon-triangle-1-n')[0],
            bottomCarat = $val.find('.ui-icon-triangle-1-s')[0];
        if (getOpacity(topCarat) == 1 || getOpacity(bottomCarat) == 1) {
            sortAsc[parseInt($order.html())] = (getOpacity(topCarat) == 1);
            maxOrder++;
        }
    });

    var ascList = [], i = 0;
    while (i < maxOrder) {
        i++;
        ascList.push(sortAsc[i]);
    }

    return ascList;
}

ice.ace.TableConf.prototype.getSortOrder = function() {
    var sortOrders = {},
        _self = this,
        maxOrder = 0;

    // collect order and id for all sorted controls
    $(this.id+'_tableconf tr[class^="ui-tableconf-row"] .ui-sortable-column-icon').each(function(i, val) {
        var $val = $(val),
            topCarat = $val.find('.ui-icon-triangle-1-n')[0],
            bottomCarat = $val.find('.ui-icon-triangle-1-s')[0],
            $row = $($val.closest('tr')),
            $order = $($row.find('.ui-sortable-column-order')[0]);

        if (getOpacity(topCarat) == 1 || getOpacity(bottomCarat) == 1) {
            sortOrders[parseInt($order.html())] =
                    ($(_self.id + " thead tr .ui-header-column")
                    [parseInt($row.attr('class').replace("ui-tableconf-row-",""))].getAttribute('id'));
            maxOrder++;
        }
    });

    // reorder ids according to order
    var sortList = [], i = 0;
    while (i < maxOrder) {
        i++;
        sortList.push(sortOrders[i]);
    }

    return sortList;
}

ice.ace.TableConf.prototype.getColOrder = function() {
    var columnOrders = [];
    $(this.id+'_tableconf tr[class^="ui-tableconf-row"]').each(function(i, val) {
        columnOrders.push(val.getAttribute('class').replace("ui-tableconf-row-",""));
    });
    return columnOrders;
}

ice.ace.TableConf.prototype.submitTableConfig = function (target) {
    var id = this.id.replace("#","").replace(/\\/g,""),
        body = $(target).parents('.ui-tableconf').find('.ui-tableconf-body:first'),
        options = {
            source: id,
            process: id,
            update: id
        };

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        return false;
    };

    var params = {}, panelConf = "";
    if (this.cfg.reorderable) panelConf += " colOrd";
    if (this.cfg.sortable) panelConf += " colSor";
    if (this.cfg.naming) panelConf += " colName";
    if (this.cfg.visibility) panelConf += " colVis";
    if (this.cfg.position == "first-col")
        panelConf += " butPos-first-col";
    else if (this.cfg.position == "last-col")
        panelConf += " butPos-last-col";

    params[id+"_tableconf"] = panelConf;
    params[id+'_colorder'] = this.getColOrder();
    params[id+'_sortKeys'] = this.getSortOrder();
    params[id+'_sortDirs'] = this.getSortAscending();
    params['ice.customUpdate'] = id;

    options.params = params;
    ice.ace.AjaxRequest(options);
}
