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
    this.jq = ice.ace.jq(this.jqId + " > ul");

    if(!this.cfg.autoSubmenuDisplay) {
        this.cfg.trigger = this.jqId + ' li';
        this.cfg.triggerEvent = 'click';
    }

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jq.wijmenu('deactivate');
    };
	
	// determine X and Y directions
	var direction = this.cfg.direction;
	var left = direction.search(/left/i);
	var right = direction.search(/right/i);
	if (left >= 0 && right >= 0) {
		if (left < right) this.cfg.directionX = 'left';
	} else if (left >= 0) this.cfg.directionX = 'left';
	else if (right >= 0) this.cfg.directionX = 'right';
	else this.cfg.directionX = 'auto';

	var up = direction.search(/up/i);
	var down = direction.search(/down/i);
	if (up >= 0 && down >= 0) {
		if (up < down) this.cfg.directionY = 'up';
	} else if (up >= 0) this.cfg.directionY = 'up';
	else if (down >= 0) this.cfg.directionY = 'down';
	else this.cfg.directionY = 'auto';
	
    this.cfg.position = {
            my: 'left top',
            using: function(to) {

			// default values
			var _myFirst = 'left top';
			var _atFirst = 'left bottom';
			var _collisionFirst = 'flip';
			var _my = 'left top';
			var _at = 'right top';
			var _collision = 'flip';
			
			
			if (_self.cfg.directionX == 'auto' && _self.cfg.directionY == 'auto') { // use default values
				// do nothing
			} else { // construct new value strings
				// process horizontal direction
				if (_self.cfg.directionX == 'left') {
					_my = 'right ';
					_at = 'left ';
					_collision = 'none ';
				} else if (_self.cfg.directionX == 'right') {
					_my = 'left ';
					_at = 'right ';
					_collision = 'none ';
				} else {
					_my = 'left ';
					_at = 'right ';
					_collision = 'flip ';				
				}
				// process vertical direction
				if (_self.cfg.directionY == 'up') {
					_myFirst = 'left bottom';
					_atFirst = 'left top';
					_collisionFirst = 'none';
					_my += 'bottom';
					_at += 'bottom';
					_collision += 'none';
				} else if (_self.cfg.directionY == 'down') {
					_myFirst = 'left top';
					_atFirst = 'left bottom';
					_collisionFirst = 'none';
					_my += 'top';
					_at += 'top';
					_collision += 'none';
				} else {
					_myFirst = 'left top';
					_atFirst = 'left bottom';
					_collisionFirst = 'flip';
					_my += 'top';
					_at += 'top';
					_collision += 'flip';
				}
			}
			
			var _this = ice.ace.jq(this);
			if (!_this.parent().get(0)) return;
			if (_this.parent().get(0).id == _self.id) { // root menu
				// do nothing
			} else { // submenus
				var isFirstSubmenu = function(item) { // utility function
					var ulParents = item.parentsUntil(ice.ace.jq(_self.jqId), 'ul');
					return ulParents.size() == 1;
				};
				
				_this.css('list-style-type', 'none');
				var _item = _this.parents('li:first');
				if (isFirstSubmenu(_item)) { // first submenu level
					_this.position({
						my: _myFirst,
						at: _atFirst,
						of: _item.get(0),
						collision: _collisionFirst
					});				
				} else { // deeper submenu levels
					_this.position({
						my: _my,
						at: _at,
						of: _item.get(0),
						collision: _collision
					});
				}
			}
            }
        }


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
    this.jq = ice.ace.jq(this.jqId + '_menu');

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
    this.jqbutton = ice.ace.jq(this.jqId + '_button');
    this.jqMenu = ice.ace.jq(this.jqId + '_menu');

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
    this.jq = ice.ace.jq(this.jqId + '_menu');

    //mouse tracking
    if(!ice.ace.ContextMenu.mouseTracking) {
        ice.ace.ContextMenu.mouseTracking = true;

        ice.ace.jq(document).mousemove(function(e){
            ice.ace.ContextMenu.pageX = e.pageX;
            ice.ace.ContextMenu.pageY = e.pageY;
            ice.ace.ContextMenu.event = e;
        });
    }

    //configuration
    this.cfg.orientation = 'vertical';
    this.cfg.triggerEvent = 'rtclick';
    this.cfg.trigger = typeof this.cfg.target == 'string' ? ice.ace.escapeClientId(this.cfg.target) : this.cfg.target;

    var _self = this;
    this.cfg.position = {
            my: 'left top',
            using: function(to) {

			var _my = 'left top';
			var _at = 'right top';
			var _collision = 'flip';
			if (_self.cfg.direction == 'up') {
				_my = 'left bottom';
				_at = 'right bottom';
				_collision = 'flip none';
			} else if (_self.cfg.direction == 'down') {
				_collision = 'flip none';
			}
			
			var _this = ice.ace.jq(this);
			if (!_this.parent().get(0)) return;
			if (_this.parent().get(0).id == _self.id) { // root menu
				_this.position({
					my: _my,
					of: ice.ace.ContextMenu.event,
					collision: _collision
				});
			} else { // submenus
				_this.css('list-style-type', 'none');
				var _item = _this.parents('li:first').get(0);
				_this.position({
					my: _my,
					at: _at,
					of: _item,
					collision: _collision
				});
			}
            }
        }

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