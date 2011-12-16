ice.ace.Tooltips = {};
/*
 *  Tooltip Widget
 */
ice.ace.Tooltip = function(cfg) {
	this.cfg = cfg;
	this.target = "";

    if (this.cfg.behaviors === undefined)
        this.cfg.behaviors = {};

	if(this.cfg.global) {
		this.target = "*[title]";
	}else {
		this.target = ice.ace.escapeClientId(this.cfg.forComponent);
	}

    this.cfg.style = this.ThemeRoller;
	/*
	var offset = jQuery(target).offset();
	var adjustY = offset.top * -1;
	this.cfg.position.adjust = { y: adjustY, x: 0 };*/
	
	this.jq = jQuery(this.target);
    if (this.jq.length <= 0) {
        return;
    }
	this.jq.qtip(this.cfg);
	
	var self = this;
	this.jq.qtip("api").beforeShow = function() { if (!ice.ace.Tooltips[self.target] && (self.cfg.displayListener || self.cfg.behaviors.display)) { ice.ace.Tooltips[self.target] = true; self.triggerDisplayListener(); }};
	this.jq.qtip("api").onHide = function() { ice.ace.Tooltips[self.target] = false; };
}

ice.ace.Tooltip.prototype.triggerDisplayListener = function() {
	var formId = this.jq.parents('form:first').attr('id'),
	    options = {
		source: this.cfg.id,
		execute: this.cfg.id,
		formId: formId,
		async: true
	};

	var params = {};
	params[this.cfg.id + '_displayListener'] = true;

	options.params = params;

    var listener = this.cfg && this.cfg.behaviors && this.cfg.behaviors.display;
    if (jQuery.isFunction(listener)) {
        listener(options);
    } else ice.ace.AjaxRequest(options);
}

/*
 * ThemeRoller integration for qtip
 */
jQuery.fn.qtip.styles['defaults'].background=undefined;
jQuery.fn.qtip.styles['defaults'].color=undefined;
jQuery.fn.qtip.styles['defaults'].tip.background=undefined;
jQuery.fn.qtip.styles['defaults'].title.background=undefined;
jQuery.fn.qtip.styles['defaults'].title.fontWeight = undefined;
jQuery.fn.qtip.styles['defaults'].width.max = Number.MAX_VALUE;

ice.ace.Tooltip.prototype.ThemeRoller = {
    border: {
        width: 0,
        radius: 0
    },
    classes: {
        tooltip: 'ui-tooltip ui-widget',
        title: 'ui-widget-header',
        content: 'ui-tooltip-content ui-widget-content ui-corner-all'
    }
};