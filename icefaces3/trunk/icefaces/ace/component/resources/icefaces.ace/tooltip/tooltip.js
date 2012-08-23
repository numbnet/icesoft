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
		if (this.cfg.forComponent) {
			this.target = ice.ace.escapeClientId(this.cfg.forComponent);
		} else if (this.cfg.forComponents) {
			var arr = this.cfg.forComponents;
			for (var i = 0; i < arr.length; i++) {
				arr[i] = ice.ace.escapeClientId(arr[i]);
			}
			this.target = arr.join(', ');
		}
	}

    this.jq = ice.ace.jq(this.target);
    if (this.jq.length <= 0) {
        return;
    }
	
	var styleClasses = 'ui-widget-content ice-ace-tooltip ui-corner-all';
	var showTip = false;
    if (this.cfg.speechBubble) {
		styleClasses += ' ice-ace-speechbubble'
		showTip = true;
    }
	this.cfg.style = {widget:true, tip:{corner:showTip, width:12, height:12}};
	
	var self = this;
	var events = {};
	events.render = function(event, api) {api.elements.tooltip.addClass(styleClasses)};
	events.show = function() { if (!ice.ace.Tooltips[self.cfg.id] && (self.cfg.displayListener || self.cfg.behaviors.display)) { ice.ace.Tooltips[self.cfg.id] = true; self.triggerDisplayListener(); }};
	events.hide = function() { delete ice.ace.Tooltips[self.cfg.id] };
	this.cfg.events = events;
	
	this.jq.qtip(this.cfg);
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

    var behavior = this.cfg && this.cfg.behaviors && this.cfg.behaviors.display;
    if (behavior) {
        ice.ace.ab(ice.ace.extendAjaxArguments(
                behavior,
                ice.ace.removeExecuteRenderOptions(options)
        ));
    } else ice.ace.AjaxRequest(options);
};
