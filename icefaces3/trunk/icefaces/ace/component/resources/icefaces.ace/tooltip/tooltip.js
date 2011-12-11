ice.ace.Tooltips = {};
/*
 *  Tooltip Widget
 */
ice.ace.Tooltip = function(cfg) {
	this.cfg = cfg;
	this.target = "";
	
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
	this.jq.qtip(this.cfg);
	
	var self = this;
	this.jq.qtip("api").beforeShow = function() { if (!ice.ace.Tooltips[self.target] && self.cfg.displayListener) { ice.ace.Tooltips[self.target] = true; self.triggerDisplayListener(); }
                                                  if (!ice.ace.Tooltips[self.target] && self.cfg.behaviors && self.cfg.behaviors.display) { ice.ace.Tooltips[self.target] = true; self.triggerDisplayListener2(); }
    };
	this.jq.qtip("api").onHide = function() { ice.ace.Tooltips[self.target] = false; };
}

ice.ace.Tooltip.prototype.triggerDisplayListener = function() {

	formId = this.jq.parents('form:first').attr('id');

	var options = {
		source: this.cfg.id,
		execute: this.cfg.id,
		formId: formId,
		async: true
	};

	var params = {};
	params[this.cfg.id + '_displayListener'] = true;

	options.params = params;

	ice.ace.AjaxRequest(options);
}
ice.ace.Tooltip.prototype.triggerDisplayListener2 = function() {
    var listener = this.cfg && this.cfg.behaviors && this.cfg.behaviors.display;
    if (jQuery.isFunction(listener)) {
//            listener.call(this, event);
        listener();
    }
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