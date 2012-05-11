if (!window.ice['ace']) {
    window.ice.ace = {};
}
ice.ace.Charts = {};
ice.ace.Chart = function (id, data, cfg) {
    self = this;
    this.id = id;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.cfg  = cfg;
    this.behaviors = cfg.behaviors;

    // Clear existing ace plot instance.
    if (ice.ace.Charts[id]) {
        if (ice.ace.Charts[id].plot)
            ice.ace.Charts[id].plot.destroy();
        else // clean up error message that is probably present if plot is not present
            ice.ace.jq(this.jqId+'_chart').html('');
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
}

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
