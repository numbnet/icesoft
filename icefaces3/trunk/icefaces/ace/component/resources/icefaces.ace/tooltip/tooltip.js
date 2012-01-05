/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

ice.ace.Tooltips = {};
/*
 *  Tooltip Widget
 */
ice.ace.Tooltip = function(cfg) {
    var callee = arguments.callee, id = cfg.id, prevTooltip = callee[id];
    if (prevTooltip) {
        prevTooltip.jq.qtip("destroy");
    }
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
    callee[id] = this;
};

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
        listener(options.params);
    } else ice.ace.AjaxRequest(options);
};

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