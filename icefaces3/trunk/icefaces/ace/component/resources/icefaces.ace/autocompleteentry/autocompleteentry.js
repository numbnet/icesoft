if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
ice.ace.Autocompleters = {};

ice.ace.Autocompleter = function(id, updateId, rowClass, selectedRowClass, delay, minChars, height, direction, focus, behaviors, cfg) {
	this.id = id;
	ice.ace.Autocompleters[this.id] = this;
	this.delay = delay;
	this.minChars = minChars;
	this.height = height == 0 ? 'auto' : height;
	this.direction = direction;
    this.cfg = cfg;

	var options = {minChars:0};
	this.root = ice.ace.jq(ice.ace.escapeClientId(this.id));
	var element = this.root.find('input[name="'+this.id+'_input"]').get(0);
	element.id = this.id + "_input";
	var ue = ice.ace.jq(ice.ace.escapeClientId(updateId)).get(0);
	this.baseInitialize(element, ue, options, rowClass, selectedRowClass);

	var self = this;
	this.options.onComplete = function() { self.onComplete() };
	this.options.defaultParams = this.options.parameters || null;
	
	if (behaviors) {
		if (behaviors.behaviors) {
			if (behaviors.behaviors.submit)
				this.ajaxSubmit = behaviors.behaviors.submit;
			if (behaviors.behaviors.blur)
				this.ajaxBlur = behaviors.behaviors.blur;
		}
	}
	
	this.tabKeyPressed = false;
	if (focus) this.element.focus();
};

ice.ace.Autocompleter.keys = {
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

ice.ace.Autocompleter.Browser = (function() {
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

ice.ace.Autocompleter.collectTextNodes = function(element) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 ? ice.ace.Autocompleter.collectTextNodes(node) : '');
	}
	return str;
};

ice.ace.Autocompleter.collectTextNodesIgnoreClass = function(element, className) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 && !ice.ace.jq(node).hasClass(className) ? ice.ace.Autocompleter.collectTextNodesIgnoreClass(node, className) : '' );
	}
	return str;
};

ice.ace.Autocompleter.cleanWhitespace = function(element) {
	var node = element.firstChild;
	while (node) {
		var nextNode = node.nextSibling;
		if (node.nodeType == 3 && !/\S/.test(node.nodeValue))
			element.removeChild(node);
		node = nextNode;
	}
	return element;
};

ice.ace.Autocompleter.prototype = {

    baseInitialize: function(element, update, options, rowC, selectedRowC) {
        var self = this;
        this.element = element;
        this.update = update;
        this.hasFocus = false;
        this.changed = false;
        this.active = false;
        this.index = -1;
        this.entryCount = 0;
        this.rowClass = rowC;
        this.selectedRowClass = selectedRowC;

        if (this.setOptions)
            this.setOptions(options);
        else
            this.options = options || {};

        this.options.paramName = this.options.paramName || this.element.name;
        this.options.tokens = this.options.tokens || [];
        this.options.frequency = this.options.frequency || 0.4;
        this.options.minChars = this.options.minChars || 1;
        this.options.onShow = this.options.onShow ||
            function(element, update) {
                // Based on code from MSDN
                var ieEngine = null;
                if (window.navigator.appName == "Microsoft Internet Explorer") {
                    if (document.documentMode) {
                        ieEngine = document.documentMode;
                    } else if (document.compatMode && document.compatMode == "CSS1Compat") {
                        ieEngine = 7;
                    } else {
                        ieEngine = 5;
                    }
                }
                try {
                    if (update["style"] && (!update.style.position || update.style.position == 'absolute')) {
                        update.style.position = 'absolute';
						var jqElement = ice.ace.jq(element);
						var jqUpdate = ice.ace.jq(update);
						var pos = jqElement.offset();
						var autoUp = false;
						if (self.direction == 'auto') {
							var updateHeight = jqUpdate.height();
							updateHeight = updateHeight > self.height ? self.height : updateHeight;
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
						if (self.direction == 'up' || autoUp) {
							var updateHeight = jqUpdate.height();
							updateHeight = updateHeight > self.height ? self.height : updateHeight;
							jqUpdate.css({ position: "absolute", top: pos.top - updateHeight, left: pos.left, marginTop: 0, marginLeft: 0, width: jqElement.width(), maxHeight: self.height, overflow: "auto" });
							if (ieEngine >= 7) {
								var savedPos = element.style.position;
								element.style.position = "relative";
								update.style.left = element.offsetLeft + "px";
								if (ieEngine == 7) {
									update.style.top = (element.offsetTop - updateHeight) + "px";
								} else {
									var scrollTop = pos.top - document.documentElement.scrollTop;
									update.style.top = (element.offsetTop - updateHeight) + "px";
								}
								element.style.position = savedPos;
							}						
						} else {
							jqUpdate.css({ position: "absolute", top: pos.top + element.offsetHeight, left: pos.left, marginTop: 0, marginLeft: 0, width: jqElement.width(), maxHeight: self.height, overflow: "auto" });
							if (ieEngine >= 7) {
								var savedPos = element.style.position;
								element.style.position = "relative";
								update.style.left = element.offsetLeft + "px";
								if (ieEngine == 7) {
									update.style.top = (element.offsetTop + element.offsetHeight) + "px";
								} else {
									var scrollTop = pos.top - document.documentElement.scrollTop;
									update.style.top = (element.offsetTop + element.offsetHeight) + "px";
								}
								element.style.position = savedPos;
							}
						}
                    }
                    ice.ace.jq(update).fadeIn(150)
                } catch(e) {
                    //logger.info(e);
                }
            };
        this.options.onHide = this.options.onHide ||
            function(element, update) {
			ice.ace.jq(update).fadeOut(150)
            };

        if (typeof(this.options.tokens) == 'string')
            this.options.tokens = new Array(this.options.tokens);

        this.observer = null;
        this.element.setAttribute('autocomplete', 'off');
        ice.ace.jq(this.update).hide();
        ice.ace.jq(this.element).data("labelIsInField", this.cfg.labelIsInField);
		ice.ace.jq(this.element).on("blur", function(e) { self.onBlur.call(self, e); });
		ice.ace.jq(this.element).on("focus", function(e) { self.onFocus.call(self, e); });
        var keyEvent = "keypress";
        if (ice.ace.Autocompleter.Browser.IE || ice.ace.Autocompleter.Browser.WebKit) {
            keyEvent = "keydown";
        }
		ice.ace.jq(this.element).on(keyEvent, function(e) { self.onKeyPress.call(self, e); } );
        // ICE-3830
        if (ice.ace.Autocompleter.Browser.IE || ice.ace.Autocompleter.Browser.WebKit)
		ice.ace.jq(this.element).on("paste", function(e) { self.onPaste.call(self, e); });
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
                case ice.ace.Autocompleter.keys.KEY_TAB:
					setFocus('');
					if (this.ajaxBlur) {
						this.tabKeyPressed = true;
						ice.ace.ab(this.ajaxBlur);
					}
                case ice.ace.Autocompleter.keys.KEY_RETURN:
					if (this.element.value.length < this.minChars) {
						event.stopPropagation();
						event.preventDefault();
						return false;
					}
                    this.getUpdatedChoices(true, event, -1);
                    return;
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.getUpdatedChoices(false, event, -1);
                    return;
            }
        }

        if (this.active) {
            switch (event.keyCode) {
                case ice.ace.Autocompleter.keys.KEY_TAB:
					setFocus('');
					if (this.ajaxBlur) {
						this.tabKeyPressed = true;
						ice.ace.ab(this.ajaxBlur);
					}
                case ice.ace.Autocompleter.keys.KEY_RETURN:
					if (this.element.value.length < this.minChars) {
						event.stopPropagation();
						event.preventDefault();
						return false;
					}

                    this.hidden = true; // Hack to fix before beta. Was popup up the list after a selection was made
                    var idx = this.selectEntry();
                    this.getUpdatedChoices(true, event, idx);
                    this.hide();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_ESC:
                    this.hide();
                    this.active = false;
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_LEFT:
                case ice.ace.Autocompleter.keys.KEY_RIGHT:
                    return;
                case ice.ace.Autocompleter.keys.KEY_UP:
                    this.markPrevious();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.markNext();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
            }
        }
        else {
            if (event.keyCode == ice.ace.Autocompleter.keys.KEY_TAB || event.keyCode == ice.ace.Autocompleter.keys.KEY_RETURN) return;
        }

        this.changed = true;
        this.hasFocus = true;
        this.index = -1;
        //This is to avoid an element being select because the mouse just happens to be over the element when the list pops up
        this.skip_mouse_hover = true;
        if (this.active) this.render();
        if (this.observer) clearTimeout(this.observer);
		var self = this;
        this.observer = setTimeout(function() { self.onObserverEvent() }, this.options.frequency * 1000);
    },

    onKeyDown: function(event) {
        if (!this.active) {
            switch (event.keyCode) {
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.getUpdatedChoices(false, event, -1);
                    return;
                case ice.ace.Autocompleter.keys.KEY_BACKSPACE:
                case ice.ace.Autocompleter.keys.KEY_DELETE:
                    if (this.observer) clearTimeout(this.observer);
				var self = this;
                    this.observer = setTimeout( function() { self.onObserverEvent() }, this.options.frequency * 1000);
                    return;
            }
        }
        else if (this.active) {
            switch (event.keyCode) {
                case ice.ace.Autocompleter.keys.KEY_UP:
                    this.markPrevious();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.markNext();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_ESC:
                    if (ice.ace.Autocompleter.Browser.WebKit) {
                        this.hide();
                        this.active = false;
					event.stopPropagation();
					event.preventDefault();
                        return;
                    }
                case ice.ace.Autocompleter.keys.KEY_BACKSPACE:
                case ice.ace.Autocompleter.keys.KEY_DELETE:
                    if (this.observer) clearTimeout(this.observer);
					var self = this;
                    this.observer = setTimeout(function() { self.onObserverEvent() }, this.options.frequency * 1000);
                    return;
            }
        }
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
        this.hidden = true;
        // Hack to fix before beta. Was popup up the list after a selection was made
		var element = ice.ace.jq(event.currentTarget).closest('div').get(0);
        this.index = element.autocompleteIndex;
        var idx = element.autocompleteIndex;
        this.selectEntry();
        this.getUpdatedChoices(true, event, idx);
        this.hide();
    },

    onBlur: function(event) {
        var input = ice.ace.jq(this.element);
        if (ice.ace.jq.trim(input.val()) == "" && this.cfg.inFieldLabel) {
            input.val(this.cfg.inFieldLabel);
            input.addClass(this.cfg.inFieldLabelStyleClass);
            input.data("labelIsInField", true);
        }
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
		if (this.ajaxBlur) {
			if (this.tabKeyPressed) {
				this.tabKeyPressed = false;
			} else {
				ice.ace.ab(this.ajaxBlur);
			}
		}
		setFocus('');
    },

    onFocus: function(event) {
        var input = ice.ace.jq(this.element);
        if (input.data("labelIsInField")) {
            input.val("");
            input.removeClass(this.cfg.inFieldLabelStyleClass);
            input.data("labelIsInField", false);
        }
      if (this.element.createTextRange) {
       //IE  
	  this.element.focus();
       var fieldRange = this.element.createTextRange();  
       fieldRange.moveStart('character', this.element.value.length);  
       fieldRange.collapse(false);  
       fieldRange.select();
       }  
      else {
       this.element.focus();
       var length = this.element.value.length;  
       this.element.setSelectionRange(length, length);  
      } 
    },

    // ICE-3830
    onPaste: function(event) {
        this.changed = true;
        this.hasFocus = true;
        this.index = -1;
        this.skip_mouse_hover = true;
        if (this.active) this.render();
        if (this.observer) clearTimeout(this.observer);
		var self = this;
        this.observer = setTimeout(function() { self.onObserverEvent(); }, this.options.frequency * 1000);
        return;
    },

    render: function() {
        if (this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++)
                if (this.index == i) {
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.selectedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);
                }
                else {
                    ar = this.selectedRowClass.split(" ");
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
            if (nodes.length > 0) value = ice.ace.Autocompleter.collectTextNodes(nodes[0], this.options.select);
        } else {
            value = ice.ace.Autocompleter.collectTextNodesIgnoreClass(selectedElement, 'informal');
	}

        var lastTokenPos = this.findLastToken();
        if (lastTokenPos != -1) {
            var newValue = this.element.value.substr(0, lastTokenPos + 1);
            var whitespace = this.element.value.substr(lastTokenPos + 1).match(/^\s+/);
            if (whitespace)
                newValue += whitespace[0];
            this.element.value = newValue + value;
        } else {
            this.element.value = value;
        }
        this.element.focus();

        if (this.options.afterUpdateElement)
            this.options.afterUpdateElement(this.element, selectedElement);
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
            ice.ace.Autocompleter.cleanWhitespace(this.update);
            ice.ace.Autocompleter.cleanWhitespace(this.update.firstChild);

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
        if (ice.ace.Autocompleter.Browser.IE || ice.ace.Autocompleter.Browser.WebKit)
			ice.ace.jq(this.element).off('keydown');
    },

    onObserverEvent: function() {
        this.changed = false;
        if (this.getToken().length >= this.options.minChars) {
            this.startIndicator();
            this.getUpdatedChoices(false, undefined, -1);
        } else {
            this.active = false;
            this.hide();
            this.getUpdatedChoices(false, undefined, -1);
        }
    },

    getToken: function() {
        var tokenPos = this.findLastToken();
        if (tokenPos != -1)
            var ret = this.element.value.substr(tokenPos + 1).replace(/^\s+/, '').replace(/\s+$/, '');
        else
            var ret = this.element.value;

        return /\n/.test(ret) ? '' : ret;
    },

    findLastToken: function() {
        var lastTokenPos = -1;

        for (var i = 0; i < this.options.tokens.length; i++) {
            var thisTokenPos = this.element.value.lastIndexOf(this.options.tokens[i]);
            if (thisTokenPos > lastTokenPos)
                lastTokenPos = thisTokenPos;
        }
        return lastTokenPos;
    },

    getUpdatedChoices: function(isEnterKey, event, idx) {
		if (this.element.value.length < this.minChars) return; // this.hide()
		var self = this;
        if (!event) {
            event = new Object();
        }
        entry = encodeURIComponent(this.options.paramName) + '=' +
            encodeURIComponent(this.getToken());

        this.options.parameters = this.options.callback ?
            this.options.callback(this.element, entry) : entry;

        if (this.options.defaultParams)
            this.options.parameters += '&' + this.options.defaultParams;

        var form = formOf(this.element);
        if (idx > -1) {
            var indexName = this.id + "_idx";
            form[indexName].value = idx;
        }

        var abCall = function() { ice.ace.ab(self.ajaxSubmit); };
		var icesCall = function() { ice.s(event, self.element); };
		//     form.focus_hidden_field.value=this.element.id;
        if (isEnterKey) {
            //iceSubmit(form, this.element, event);
		if (this.ajaxSubmit) {	
			ice.ace.ab(this.ajaxSubmit);
		} else {
			ice.s(event, this.element);
		}
        }
        else {
            //iceSubmitPartial(form, this.element, event);
		if (this.ajaxSubmit) {	
			setTimeout(abCall, self.delay);
		} else {
			setTimeout(icesCall, self.delay);
		}
        }

        var indexName = this.id + "_idx";
        form[indexName].value = "";
    },

    onComplete: function(request) {
        this.updateChoices(request.responseText);
    },

    updateNOW: function(text) {


        if (this.hidden) {
            this.hidden = false;
        }
        this.hasFocus = true;
        ice.ace.Autocompleter.cleanWhitespace(this.update);
        this.updateChoices(text);
        this.show();
        this.render();
		this.element.focus();
    }
}
