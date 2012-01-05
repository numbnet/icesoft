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
* Code Modification 2: [ADD BRIEF DESCRIPTION HERE] 
* Contributors: ______________________ 
* Contributors: ______________________ 
* 
*/

/**
 *  Menubar Widget
 */
ice.ace.Menubar = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.jq = jQuery(this.jqId + " > ul");

    if(!this.cfg.autoSubmenuDisplay) {
        this.cfg.trigger = this.jqId + ' li';
        this.cfg.triggerEvent = 'click';
    }

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jq.wijmenu('deactivate');
    };

    this.jq.wijmenu(this.cfg);

    if(this.cfg.style)
        this.jq.parent().parent().attr('style', this.cfg.style);
    if(this.cfg.styleClass)
        this.jq.parent().parent().addClass(this.cfg.styleClass);
}

/**
 *  Menubar Widget
 */
ice.ace.Menu = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.jq = jQuery(this.jqId + '_menu');

    this.cfg.orientation = 'vertical';

    if(this.cfg.position == 'dynamic') {
        this.cfg.position = {
            my: this.cfg.my
            ,at: this.cfg.at
        }

        this.cfg.trigger = ice.ace.escapeClientId(this.cfg.trigger);
    }

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jq.wijmenu('deactivate');
    };

    this.jq.wijmenu(this.cfg);

    this.element = this.jq.parent().parent();       //overlay element
    this.element.css('z-index', this.cfg.zindex);

    if(this.cfg.style)
        this.element.attr('style', this.cfg.style);
    if(this.cfg.styleClass)
        this.element.addClass(this.cfg.styleClass);
}

/*
 *  MenuButton Widget
 */
ice.ace.MenuButton = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;
	this.jqId = ice.ace.escapeClientId(id);
    this.jqbutton = jQuery(this.jqId + '_button');
    this.jqMenu = jQuery(this.jqId + '_menu');

    //menu options
    this.cfg.trigger = this.jqId + '_button';
    this.cfg.orientation = 'vertical';
    this.cfg.position = {
        my: 'left top'
        ,at: 'left bottom'
    };

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jqMenu.wijmenu('deactivate');
    };

    //crete button and menu
    this.jqbutton.button({icons:{primary:'ui-icon-triangle-1-s'}});
    this.jqMenu.wijmenu(this.cfg);

    if(this.cfg.disabled) {
        this.jqbutton.button('disable');
    }

    this.jqMenu.parent().parent().css('z-index', this.cfg.zindex);      //overlay element
}

/*
 *  ContextMenu Widget
 */
ice.ace.ContextMenu = function(id, cfg) {
	this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.jq = jQuery(this.jqId + '_menu');

    //mouse tracking
    if(!ice.ace.ContextMenu.mouseTracking) {
        ice.ace.ContextMenu.mouseTracking = true;

        jQuery(document).mousemove(function(e){
            ice.ace.ContextMenu.pageX = e.pageX;
            ice.ace.ContextMenu.pageY = e.pageY;
            ice.ace.ContextMenu.event = e;
        });
    }

    //configuration
    this.cfg.orientation = 'vertical';
    this.cfg.triggerEvent = 'rtclick';
    this.cfg.trigger = typeof this.cfg.target == 'string' ? ice.ace.escapeClientId(this.cfg.target) : this.cfg.target;

    this.cfg.position = {
            my: 'left top'
            ,using: function(to) {
/*
                jQuery(this).css({
                    top: ice.ace.ContextMenu.pageY,
                    left: ice.ace.ContextMenu.pageX
                });
*/
                jQuery(this).position({
                    my: "left top",
                    of: ice.ace.ContextMenu.event,
                    collision: "flip"
                });
            }
        }

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jq.wijmenu('deactivate');
    };
	
	if (ice.ace.jq.browser.msie) { // ICE-7532 not supported in IE
		if (this.cfg.animation) { 
			delete this.cfg.animation;
		}
	}

    this.jq.wijmenu(this.cfg);

    this.element = this.jq.parent().parent();   //overlay element
    this.element.css('z-index', this.cfg.zindex);

    if(this.cfg.style)
        this.element.attr('style', this.cfg.style);
    if(this.cfg.styleClass)
        this.element.addClass(this.cfg.styleClass);
}