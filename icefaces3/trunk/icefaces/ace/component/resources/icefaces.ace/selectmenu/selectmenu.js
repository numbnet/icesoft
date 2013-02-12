/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
if (!ice.ace.SelectMenus) ice.ace.SelectMenus = {};

ice.ace.SelectMenu = function(id, updateId, rowClass, highlightedRowClass, selectedRowClass, height, rows, behaviors) {
	this.id = id;
	var isInitialized = false;
	if (ice.ace.SelectMenus[this.id] && ice.ace.SelectMenus[this.id].initialized) isInitialized = true;
	this.showingList = false;
	if (isInitialized) this.showingList = ice.ace.SelectMenus[this.id].showingList;
	ice.ace.SelectMenus[this.id] = this;
	this.height = height == 0 ? 'auto' : height;
	this.direction = 'down';
    //this.cfg = cfg;
	var options = {};
	this.root = ice.ace.jq(ice.ace.escapeClientId(this.id));
	var $element = this.root.find('span');
	this.element = $element.get(0);
	this.element.id = this.id + "_input";
	this.displayedValue = $element.find('span').get(0);
	ice.ace.jq(this.displayedValue).css('width', $element.width() - 19);
	var $input = this.root.find('input[name="'+this.id+'_input"]');
	this.input = $input.get(0);
	this.input.id = this.id + "_input";
	this.update = ice.ace.jq(ice.ace.escapeClientId(updateId)).get(0);
	//$element.data("labelIsInField", this.cfg.labelIsInField);
	
	if (isInitialized) {
		this.initialize(this.element, this.update, options, rowClass, highlightedRowClass, selectedRowClass, behaviors);
	} else {
		var self = this;
		$element.on('focus', function() {
			$element.off('focus');
			//if ($element.data("labelIsInField")) {
			//	$element.val("");
			//	$element.removeClass(self.cfg.inFieldLabelStyleClass);
			//	$element.data("labelIsInField", false);
			//	self.cfg.labelIsInField = false;
			//}
			self.initialize(self.element, self.update, options, rowClass, highlightedRowClass, selectedRowClass, behaviors); 
		});
	}
};

ice.ace.SelectMenu.keys = {
KEY_BACKSPACE: 8,
KEY_TAB:       9,
KEY_RETURN:   13,
KEY_ESC:      27,
KEY_LEFT:     37,
KEY_UP:       38,
KEY_RIGHT:    39,
KEY_DOWN:     40,
KEY_DELETE:   46,
KEY_HOME:     36,
KEY_END:      35,
KEY_PAGEUP:   33,
KEY_PAGEDOWN: 34,
KEY_INSERT:   45
};

ice.ace.SelectMenu.Browser = (function() {
        var ua = navigator.userAgent;
        var isOpera = Object.prototype.toString.call(window.opera) == '[object Opera]';
        return {
            IE:             !!window.attachEvent && !isOpera,
            Opera:          isOpera,
            WebKit:         ua.indexOf('AppleWebKit/') > -1,
            Gecko:          ua.indexOf('Gecko') > -1 && ua.indexOf('KHTML') === -1,
            MobileSafari:   /Apple.*Mobile/.test(ua)
        }
    })();

ice.ace.SelectMenu.collectTextNodes = function(element) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 ? ice.ace.SelectMenu.collectTextNodes(node) : '');
	}
	return str;
};

ice.ace.SelectMenu.collectTextNodesIgnoreClass = function(element, className) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 && !ice.ace.jq(node).hasClass(className) ? ice.ace.SelectMenu.collectTextNodesIgnoreClass(node, className) : '' );
	}
	return str;
};

ice.ace.SelectMenu.cleanWhitespace = function(element) {
	var node = element.firstChild;
	while (node) {
		var nextNode = node.nextSibling;
		if (node.nodeType == 3 && !/\S/.test(node.nodeValue))
			element.removeChild(node);
		node = nextNode;
	}
	return element;
};

ice.ace.SelectMenu.prototype = {

    initialize: function(element, update, options, rowC, highlightedRowClass, selectedRowC, behaviors) {
        var self = this;
        this.hasFocus = false;
        this.changed = false;
        this.active = false;
        this.index = -1;
		this.selectedIndex = -1;
        this.entryCount = 0;
        this.rowClass = rowC;
		this.highlightedRowClass = highlightedRowClass;
        this.selectedRowClass = selectedRowC;

        if (this.setOptions)
            this.setOptions(options);
        else
            this.options = options || {};

        this.options.onShow = this.options.onShow ||
            function(element, update) {
                try {
					self.calculateListPosition();
                    ice.ace.jq(update).fadeIn(150)
                } catch(e) {
                    //logger.info(e);
                }
            };
        this.options.onHide = this.options.onHide ||
            function(element, update) {
			ice.ace.jq(update).fadeOut(150)
            };

        this.observer = null;
        ice.ace.jq(this.update).hide();
		//ice.ace.jq(this.element).data("labelIsInField", this.cfg.labelIsInField);
		ice.ace.jq(this.element).on("blur", function(e) { self.onBlur.call(self, e); });
		ice.ace.jq(this.element).on("focus", function(e) { self.onFocus.call(self, e); });
		ice.ace.jq(this.element).on("click", function(e) { self.onElementClick.call(self, e); });
        var keyEvent = "keypress";
        if (ice.ace.SelectMenu.Browser.IE || ice.ace.SelectMenu.Browser.WebKit) {
            keyEvent = "keydown";
        }
		ice.ace.jq(this.element).on(keyEvent, function(e) { self.onKeyPress.call(self, e); } );
		
		// ajax behaviors
		if (behaviors) {
			if (behaviors.behaviors) {
				if (behaviors.behaviors.submit) {
					this.ajaxSubmit = behaviors.behaviors.submit;
					this.ajaxSubmit.source = this.ajaxSubmit.source + "_input";
				}
				if (behaviors.behaviors.blur) {
					this.ajaxBlur = behaviors.behaviors.blur;
				}
				if (behaviors.behaviors.textChange) {
					this.ajaxTextChange = behaviors.behaviors.textChange;
				}
				if (behaviors.behaviors.change) {
					this.ajaxValueChange = behaviors.behaviors.change;
				}
			}
		}
		
		//this.updateNOW(this.content);
		
		this.initialized = true;
    },
	
	calculateListPosition: function() {
		var element = this.element;
		var update = this.update;
		if (update["style"] && (!update.style.position || update.style.position == 'absolute')) {
			update.style.position = 'absolute';
			var jqElement = ice.ace.jq(element);
			var jqUpdate = ice.ace.jq(update);
			var pos = jqElement.offset();
			var autoUp = false;
			if (this.direction == 'auto') {
				var updateHeight = jqUpdate.height();
				updateHeight = updateHeight > this.height ? this.height : updateHeight;
				var winHeight = ice.ace.jq(window).height();
				var docHeight = ice.ace.jq(document).height();
				var scrollTop = ice.ace.jq(document).scrollTop()
				var lengthAbove = pos.top - scrollTop;
				var lengthBelow = scrollTop + winHeight - pos.top - element.offsetHeight;
				if (lengthBelow < updateHeight) {
					if (lengthAbove > lengthBelow)
						autoUp = true;
				}
			}
			if (this.direction == 'up' || autoUp) {
				var updateHeight = jqUpdate.height();
				updateHeight = updateHeight > this.height ? this.height : updateHeight;
				jqUpdate.css({ position: "absolute", marginTop: 0, marginLeft: 0, maxHeight: this.height, overflow: "auto" });
				var savedPos = element.style.position;
				element.style.position = "relative";
				update.style.left = element.offsetLeft + "px";
				update.style.top = (element.offsetTop - updateHeight) + "px";
				element.style.position = savedPos;
			} else {
				jqUpdate.css({ position: "absolute", marginTop: 0, marginLeft: 0, maxHeight: this.height, overflow: "auto" });
				var savedPos = element.style.position;
				element.style.position = "relative";
				update.style.left = element.offsetLeft + "px";
				update.style.top = (element.offsetTop + element.offsetHeight) + "px";
				element.style.position = savedPos;
			}
		}
	},

    show: function() {
        try {
            if (ice.ace.jq(this.update).css('display') == 'none')this.options.onShow(this.element, this.update);
            if (!this.iefix &&
                (navigator.appVersion.indexOf('MSIE') > 0) &&
                (navigator.userAgent.indexOf('Opera') < 0) &&
                (ice.ace.jq(this.update).css('position') == 'absolute')) {
                ice.ace.jq('<iframe id="' + this.update.id + '_iefix" title="IE6_Fix" ' +
                        'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' +
                        'src="javascript:\'<html></html>\'" frameborder="0" scrolling="no"></iframe>').insertAfter(this.update);
                this.iefix = ice.ace.jq('#' + this.update.id + '_iefix').get(0);
            }
		  var self = this;
            if (this.iefix) setTimeout(function() { self.fixIEOverlapping.call(self) }, 50);
            this.element.focus();
        } catch (e) {
            //logger.info(e);
        }
    },

    fixIEOverlapping: function() {
        try {
		var pos = ice.ace.jq(this.update).offset();
            ice.ace.jq(this.iefix).css(pos);
            this.iefix.style.zIndex = 1;
            this.update.style.zIndex = 2;
            ice.ace.jq(this.iefix).show();
        } catch(e) {
            //logger.info(e);
        }
    },

    hide: function() {
        this.stopIndicator();
        if (ice.ace.jq(this.update).css('display') != 'none') this.options.onHide(this.element, this.update);
        if (this.iefix) ice.ace.jq(this.iefix).hide();
		this.showingList = false;
    },

    startIndicator: function() {
        if (this.options.indicator) ice.ace.jq(this.options.indicator).show();
    },

    stopIndicator: function() {
        if (this.options.indicator) ice.ace.jq(this.options.indicator).hide();
    },

    onKeyPress: function(event) {
        if (!this.active) {
            switch (event.keyCode) {
                case ice.ace.SelectMenu.keys.KEY_TAB:
					setFocus('');
					return;
                case ice.ace.SelectMenu.keys.KEY_RETURN:
                    this.getUpdatedChoices(true, event, -1);
					event.stopPropagation();
					event.preventDefault();
                    return;
				case ice.ace.SelectMenu.keys.KEY_UP:
					this.index = this.selectedIndex;
                    this.markPrevious();
					this.selectEntry();// submit if ajax
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_DOWN:
					this.index = this.selectedIndex;
                    this.markNext();
					this.selectEntry();// submit if ajax
					event.stopPropagation();
					event.preventDefault();
                    return;
                    //this.active = true;
					//this.updateNOW(this.content);
					//break;
            }
        }

        if (this.active) {
            switch (event.keyCode) {
                case ice.ace.SelectMenu.keys.KEY_TAB:
					setFocus('');
					return;
                case ice.ace.SelectMenu.keys.KEY_RETURN:
                    var idx = this.selectEntry();
                    this.getUpdatedChoices(true, event, idx);
                    this.hide();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_ESC:
                    this.hide();
                    this.active = false;
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_UP:
                    this.markPrevious();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_DOWN:
                    this.markNext();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
            }
        }
/*		
		if (!this.isCharacterCode(event.keyCode)) return;

        this.changed = true;
        this.hasFocus = true;
        this.index = -1;
        this.skip_mouse_hover = true;
        if (this.active) this.render();
        if (this.observer) clearTimeout(this.observer);
		var self = this;
        this.observer = setTimeout(function() { self.onObserverEvent() }, this.delay);
*/
    },

    activate: function() {
        this.changed = false;
        this.hasFocus = true;
    },

    onHover: function(event) {
		var element = ice.ace.jq(event.currentTarget).closest('div').get(0);
        if (this.index != element.autocompleteIndex) {
            if (!this.skip_mouse_hover) this.index = element.autocompleteIndex;
            this.render();
        }
		event.stopPropagation();
		event.preventDefault();
    },

    onMove: function(event) {
        if (this.skip_mouse_hover) {
            this.skip_mouse_hover = false;
            this.onHover(event);
        }
    },

    onClick: function(event) {
		var element = ice.ace.jq(event.currentTarget).closest('div').get(0);
        this.index = element.autocompleteIndex;
        var idx = element.autocompleteIndex;
        this.selectEntry();
        this.getUpdatedChoices(true, event, idx);
        this.hide();
    },

    onBlur: function(event) {
        var input = ice.ace.jq(this.element);
        //if (ice.ace.jq.trim(input.val()) == "" && this.cfg.inFieldLabel) {
        //    input.val(this.cfg.inFieldLabel);
        //    input.addClass(this.cfg.inFieldLabelStyleClass);
        //    input.data("labelIsInField", true);
        //}
        if (navigator.userAgent.indexOf("MSIE") >= 0) { // ICE-2225
            var strictMode = document.compatMode && document.compatMode == "CSS1Compat";
            var docBody = strictMode ? document.documentElement : document.body;
            // Right or bottom border, if any, will be treated as scrollbar.
            // No way to determine their width or scrollbar width accurately.
            if (event.clientX > docBody.clientLeft + docBody.clientWidth ||
                event.clientY > docBody.clientTop + docBody.clientHeight) { 
                this.element.focus();
                return;
            }
        }
        // needed to make click events working
		var self = this;
        setTimeout(function () { self.hide(); }, 250);
        this.hasFocus = false;
        this.active = false;
		setFocus('');
		if (this.ajaxBlur) {
			if (this.blurObserver) clearTimeout(this.blurObserver);
			this.ajaxBlur.params = this.ajaxBlur.params || {};
			this.ajaxBlur.params[this.id + '_hardSubmit'] = true;
			var self = this;
			this.blurObserver = setTimeout(function() { ice.ace.ab(self.ajaxBlur); }, 200);
		}
    },

    onFocus: function(event) {
        //var input = ice.ace.jq(this.element);
        //if (input.data("labelIsInField")) {
        //    input.val("");
        //    input.removeClass(this.cfg.inFieldLabelStyleClass);
        //    input.data("labelIsInField", false);
        //}
		if (this.justSelectedItem) {
			this.justSelectedItem = false;
			return;
		}
		this.active = true;
		this.updateNOW(this.content);
    },
	
	onElementClick: function(event) {
			this.active = true;
			this.updateNOW(this.content);
	},

    render: function() {
        if (this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++)
				if (this.selectedIndex == i) {
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.highlightedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.selectedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);				
				} else if (this.index == i) {
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.highlightedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);
                } else {
                    ar = this.highlightedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);
                }
            if (this.hasFocus) {
                this.show();
                this.active = true;
            }
        } else {
            this.active = false;
            this.hide();
        }
    },

    markPrevious: function() {
        if (this.index > 0) this.index--
        else this.index = this.entryCount - 1;
    },

    markNext: function() {
        if (this.index == -1) {
            this.index++;
            return;
        }
        if (this.index < this.entryCount - 1) this.index++
        else this.index = 0;
    },

    getEntry: function(index) {
        try {
            return this.update.firstChild.childNodes[index];
        } catch(ee) {
            return null;
        }
    },

    getCurrentEntry: function() {
        return this.getEntry(this.index);
    },

    selectEntry: function() {
        var idx = -1;
        this.active = false;
        if (this.index >= 0) {
            idx = this.index;
			this.selectedIndex = this.index;
            this.updateElement(this.getCurrentEntry());
            this.index = -1;
        }
        return idx;
    },

    updateElement: function(selectedElement) {
        if (this.options.updateElement) {
            this.options.updateElement(selectedElement);
            return;
        }
        var value = '';
        if (this.options.select) {
            var nodes = document.getElementsByClassName(this.options.select, selectedElement) || [];
            if (nodes.length > 0) value = ice.ace.SelectMenu.collectTextNodes(nodes[0], this.options.select);
        } else {
            value = ice.ace.SelectMenu.collectTextNodesIgnoreClass(selectedElement, 'informal');
	}

		this.input.value = value;
		this.displayedValue.innerHTML = this.input.value;
		this.justSelectedItem = true;
        this.element.focus();

        if (this.options.afterUpdateElement)
            this.options.afterUpdateElement(this.element, selectedElement);
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
			this.calculateListPosition();
            ice.ace.SelectMenu.cleanWhitespace(this.update);
            ice.ace.SelectMenu.cleanWhitespace(this.update.firstChild);

            if (this.update.firstChild && this.update.firstChild.childNodes) {
                this.entryCount =
                    this.update.firstChild.childNodes.length;
                for (var i = 0; i < this.entryCount; i++) {
                    var entry = this.getEntry(i);
                    entry.autocompleteIndex = i;
                    this.addObservers(entry);
                }
            } else {
                this.entryCount = 0;
            }
            this.stopIndicator();
            this.index = -1;
            this.render();
        } else {

        }
    },

    addObservers: function(element) {
		var self = this;
		ice.ace.jq(element).on("mouseover", function(e) { self.onHover.call(self, e); });
		ice.ace.jq(element).on("click", function(e) { self.onClick.call(self, e); });
		ice.ace.jq(element).on("mousemove", function(e) { self.onMove.call(self, e); });
    },

    dispose:function() {
        for (var i = 0; i < this.entryCount; i++) {
            var entry = this.getEntry(i);
            entry.autocompleteIndex = i;
			ice.ace.jq(entry).off('mouseover');
			ice.ace.jq(entry).off('click');
			ice.ace.jq(entry).off('mousemove');
        }
		ice.ace.jq(this.element).off('mouseover');
		ice.ace.jq(this.element).off('click');
		ice.ace.jq(this.element).off('mousemove');
		ice.ace.jq(this.element).off('blur');
		ice.ace.jq(this.element).off('keypress');
        if (ice.ace.SelectMenu.Browser.IE || ice.ace.SelectMenu.Browser.WebKit)
			ice.ace.jq(this.element).off('keydown');
    },

    onObserverEvent: function() {
        this.changed = false;
        this.startIndicator();
        this.getUpdatedChoices(false, undefined, -1);
    },

    getUpdatedChoices: function(isHardSubmit, event, idx) {
        if (!event) {
            event = new Object();
        }

		if (this.observer) clearTimeout(this.observer);
		if (this.blurObserver) clearTimeout(this.blurObserver);
		if (isHardSubmit) {
			if (this.ajaxValueChange || this.ajaxSubmit) {
				var ajaxCfg = {};
				var options = {params: {}};
				options.params[this.id + '_hardSubmit'] = true;
				options.params['ice.event.keycode'] = event.keyCode;
				if (this.ajaxValueChange) {
					ice.ace.jq.extend(ajaxCfg, this.ajaxValueChange, options);
				} else {
					ice.ace.jq.extend(ajaxCfg, this.ajaxSubmit, options);
				}
				ice.ace.ab(ajaxCfg);
			} else {
				ice.s(event, this.input);
			}
		} else {
			if (this.ajaxTextChange || this.ajaxSubmit) {
				var ajaxCfg = {};
				var options = {params: {}};
				options.params['ice.event.keycode'] = event.keyCode;
				if (this.ajaxTextChange) {
					ice.ace.jq.extend(ajaxCfg, this.ajaxTextChange, options);
				} else {
					ice.ace.jq.extend(ajaxCfg, this.ajaxSubmit, options);
				}
				ice.ace.ab(ajaxCfg);
			} else {
				ice.s(event, this.input);
			}
		}
    },
	
	isCharacterCode: function(keyCode) {
		if (keyCode == 8 || keyCode == 46) return true; // backspace, del
		if (keyCode >= 16 && keyCode <= 20) return false; // shift, ctrl, alt, pause, caps lock
		if (keyCode >= 33 && keyCode <= 40) return false; // pg up, pg down, end, home, arrow keys
		if (keyCode == 44 || keyCode == 45) return false; // print screen, insert
		if (keyCode == 144 || keyCode == 145) return false; // num lock, scroll lock
		if (keyCode >= 91 && keyCode <= 93) return false; // windows keys, context menu
		if (keyCode >= 112 && keyCode <= 123) return false; // f keys
		if (keyCode == 9 || keyCode == 10 || keyCode == 13 || keyCode ==  27) return false; // tab, lf, cr, esc
		return true;
	},
	
	setContent: function(content) {
		this.content = content;
	},

    updateNOW: function(text) {

		if (!text) return;
        this.hasFocus = true;
        ice.ace.SelectMenu.cleanWhitespace(this.update);
		if (ice.ace.jq.support.leadingWhitespace) { // browsers other than IE7/8
			this.updateChoices(text);
			this.show();
			this.render();
			this.element.focus();
		}
		else { // give time to IE7/8 to have nodes ready when the full form has been updated
			var self = this;
			setTimeout(function() { 
				self.updateChoices(text);
				self.show();
				self.render();
				if (focus) ice.ace.jq(ice.ace.escapeClientId(self.element.id)).focus(); 
			}, 50);
		}
    }
}
