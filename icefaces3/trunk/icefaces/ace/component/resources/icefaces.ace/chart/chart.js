if (!window.ice['ace']) {
    window.ice.ace = {};
}
ice.ace.Charts = {};
ice.ace.Chart = function (id, data, cfg) {
    var self = this;
    this.id = id;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.cfg  = cfg;
    this.behaviors = cfg.behaviors;
    this.chart_region = ice.ace.jq(this.jqId+'_chart');


    // Clear existing ace plot instance.
    if (ice.ace.Charts[id]) {
        if (ice.ace.Charts[id].plot)
            ice.ace.Charts[id].plot.destroy();
        else // clean up error message that is probably present if plot is not present
            this.chart_region.html('');
    }

    ice.ace.jq.jqplot.config.catchErrors = true;
    ice.ace.jq.jqplot.config.errorBorder = '1px solid #aaaaaa';
    this.plot = ice.ace.jq.jqplot(this.jqId.substring(1)+'_chart', data, cfg);
    ice.ace.Charts[id] = self;

    if (cfg.handlePointClick)
        ice.ace.jq(this.jqId).off("jqplotDataClick").on(
                "jqplotDataClick",
                function(e, seriesIndex, pointIndex, data) {
                    self.handlePointClick.call(self, e, seriesIndex, pointIndex, data);
                }
    );

    ice.ace.jq(this.jqId).off("jqplotDragStart").on(
            "jqplotDragStart",
            function(e, seriesIndex, pointIndex, data) {
                self.handleDragStart.call(self, e, seriesIndex, pointIndex, data);
            }
    );

    ice.ace.jq(this.jqId).off("jqplotDragStop").on(
            "jqplotDragStop",
            function(e, seriesIndex, pointIndex, data) {
                self.handleDragStop.call(self, e, seriesIndex, pointIndex, data);
            }
    );

    if (this.chart_region.is(':hidden')) {
        if (!this.cfg.disableHiddenInit) {
            var _self = this;
            setTimeout(function () { _self.plot.replot(); }, 100);
        }
    }
};


ice.ace.Chart.prototype.handleDragStart = function(e, seriesIndex, pointIndex, data) {
    var options = {
        source: this.id,
        execute: '@this',
        render: '@none'
    };

    var params = {};
    options.params = params;

    // Record old value

    // Call behaviours
    if (self.behaviors)
        if (self.behaviors.dragStart) {
            ice.ace.ab(ice.ace.extendAjaxArguments(self.behaviors.dragStart,options));
            return;
        }
};

ice.ace.Chart.prototype.handleDragStop = function(e, seriesIndex, pointIndex, data) {
    var options = {
        source: this.id,
        execute: '@this',
        render: '@none'
    };

    var params = {};
    options.params = params;

    if (self.behaviors)
        if (self.behaviors.dragStop) {
            ice.ace.ab(ice.ace.extendAjaxArguments(self.behaviors.dragStop,options));
            return;
        }

    // If value change has occurred communicate it to the server
    ice.ace.AjaxRequest(options);

};

ice.ace.Chart.prototype.handlePointClick = function(e, seriesIndex, pointIndex, data) {
    var options = {
            source: this.id,
            execute: '@this',
            render: '@this'
        };

    var params = {};
    params[this.id+'_selection'] = data;
    options.params = params;

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            ice.ace.AjaxUtils.updateElement(id, content);
        });
        return true;
    };

    if (this.behaviors)
        if (this.behaviors.click) {
            ice.ace.ab(ice.ace.extendAjaxArguments(this.behaviors.click, options));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.Chart.prototype.downloadAsImage = function() {
    this.chart_region.jqplotSaveImage();
}

ice.ace.Chart.prototype.exportToImage = function(img) {
    ice.ace.jq(img).attr('src',
        this.chart_region.jqplotToImageStr());
}
