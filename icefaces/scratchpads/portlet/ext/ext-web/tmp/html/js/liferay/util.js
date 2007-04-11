Liferay.Util = {
	submitCountdown: 0,

	addEventHandler: function(obj, type, func) {
		if (type.indexOf("on") != 0) {
			type = "on" + type;
		}

	   var temp = obj[type];

		if (typeof obj[type] != "function") {
	       obj[type] = func;
	   }
		else {
	       obj[type] = function() {
	       	if (temp) {
		           temp();
	       	}

				func();
	       }
	   }
	},

	addInputType: function(el) {
		var item;

		if (jQuery.browser.msie && jQuery.browser.version.number() < 7) {
			if (el) {
				if (typeof el == 'object') {
					item = jQuery(el);
				}
				else {
					item = jQuery('#' + el);
				}
			}
			else {
				item = document.body;
			}

			_$J("input", item).each(function() {
				var current = _$J(this);
				var type = this.type || "text";

				current.addClass(type);
			});
		}
	},

	check: function(form, name, checked) {
		for (var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];

			if ((e.name == name) && (e.type == "checkbox")) {
				e.checked = checked;
			}
		}
	},

	checkAll: function(form, name, allBox) {
		if (Liferay.Util.isArray(name)) {
			for (var i = 0; i < form.elements.length; i++) {
				var e = form.elements[i];

				if (e.type == "checkbox") {
					for (var j = 0; j < name.length; j++) {
						if (e.name == name[j]) {
							e.checked = allBox.checked;
						}
					}
				}
			}
		}
		else {
			for (var i = 0; i < form.elements.length; i++) {
				var e = form.elements[i];

				if ((e.name == name) && (e.type == "checkbox")) {
					e.checked = allBox.checked;
				}
			}
		}
	},

	checkAllBox: function(form, name, allBox) {
		var totalBoxes = 0;
		var totalOn = 0;

		if (Liferay.Util.isArray(name)) {
			for (var i = 0; i < form.elements.length; i++) {
				var e = form.elements[i];

				if ((e.name != allBox.name) && (e.type == "checkbox")) {
					for (var j = 0; j < name.length; j++) {
						if (e.name == name[j]) {
							totalBoxes++;

							if (e.checked) {
								totalOn++;
							}
						}
					}
				}
			}
		}
		else {
			for (var i = 0; i < form.elements.length; i++) {
				var e = form.elements[i];

				if ((e.name != allBox.name) && (e.name == name) && (e.type == "checkbox")) {
					totalBoxes++;

					if (e.checked) {
						totalOn++;
					}
				}
			}
		}

		if (totalBoxes == totalOn) {
			allBox.checked = true;
		}
		else {
			allBox.checked = false;
		}
	},

	checkMaxLength: function(box, maxLength) {
		if ((box.value.length) >= maxLength) {
			box.value = box.value.substring(0, maxLength - 1);
		}
	},

	checkTab: function(box) {
		if ((document.all) && (event.keyCode == 9)) {
			box.selection = document.selection.createRange();
			setTimeout("Liferay.Util.processTab(\"" + box.id + "\")", 0);
		}
	},

	createInputElement: function(name) {
		if (is_ie) {
			var entry = document.createElement("<input name='" + name + "'></input>");
		}
		else {
			var entry = document.createElement("input");
			entry.name = name;
		}

		return entry;
	},

	disableEsc: function() {
		if ((document.all) && (event.keyCode == 27)) {
			event.returnValue = false;
		}
	},

	getSelectedIndex: function(col) {
		for (var i = 0; i < col.length; i++) {
			if (col[i].checked == true) {
				return i;
			}
		}

		return -1;
	},

	getSelectedRadioValue: function(col) {
		var i = Liferay.Util.getSelectedIndex(col);

		if (i == -1) {
			var radioValue = col.value;

			if (radioValue == null) {
				radioValue = "";
			}

			return radioValue;
		}
		else {
			return col[i].value;
		}
	},

	isArray: function(object) {
		if (!window.Array) {
			return false;
		}
		else {
			return object.constructor == window.Array;
		}
	},

	listChecked: function(form) {
		var s = "";

		for (var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];

			if ((e.type == "checkbox") && (e.checked == true) && (e.value > "")) {
				s += e.value + ",";
			}
		}

		return s;
	},

	listCheckedExcept: function(form, except) {
		var s = "";

		for (var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];

			if ((e.type == "checkbox") && (e.checked == true) && (e.value > "") && (e.name.indexOf(except) != 0)) {
				s += e.value + ",";
			}
		}

		return s;
	},

	listSelect: function(box, delimeter) {
		var s = "";

		if (delimeter == null) {
			delimeter = ",";
		}

		if (box == null) {
			return "";
		}

		for (var i = 0; i < box.length; i++) {
			if (box.options[i].value > "") {
				s += box.options[i].value + delimeter;
			}
		}

		if (s == ".none,") {
			return "";
		}
		else {
			return s;
		}
	},

	listUncheckedExcept: function(form, except) {
		var s = "";

		for (var i = 0; i < form.elements.length; i++) {
			var e = form.elements[i];

			if ((e.type == "checkbox") && (e.checked == false) && (e.value > "") && (e.name.indexOf(except) != 0)) {
				s += e.value + ",";
			}
		}

		return s;
	},

	moveItem: function(fromBox, toBox, sort) {
		var newText = null;
		var newValue = null;
		var newOption = null;

		if (fromBox.selectedIndex >= 0) {
			for (var i = 0; i < fromBox.length; i++) {
				if (fromBox.options[i].selected) {
					newText = fromBox.options[i].text;
					newValue = fromBox.options[i].value;

					newOption = new Option(newText, newValue);

					toBox[toBox.length] = newOption;
				}
			}

			for (var i = 0; i < toBox.length; i++) {
				for (var j = 0; j < fromBox.length; j++) {
					if (fromBox[j].value == toBox[i].value) {
						fromBox[j] = null;

						break;
					}
				}
			}
		}

		if (newText != null) {
			if (sort == true) {
				Liferay.Util.sortBox(toBox);
			}
		}
	},

	processTab: function(id) {
		document.all[id].selection.text = String.fromCharCode(9);
		document.all[id].focus();
	},

	removeItem: function(box, value) {
		if (value == null) {
			for (var i = box.length - 1; i >= 0; i--) {
				if (box.options[i].selected) {
					box[i] = null;
				}
			}
		}
		else {
			for (var i = box.length - 1; i >= 0; i--) {
				if (box.options[i].value == value) {
					box[i] = null;
				}
			}
		}
	},

	reorder: function(box, down) {
		var si = box.selectedIndex;

		if (si == -1) {
			box.selectedIndex = 0;
		}
		else {
			sText = box.options[si].text;
			sValue = box.options[si].value;

			if ((box.options[si].value > "") && (si > 0) && (down == 0)) {
				box.options[si].text = box.options[si - 1].text;
				box.options[si].value = box.options[si - 1].value;
				box.options[si - 1].text = sText;
				box.options[si - 1].value = sValue;
				box.selectedIndex--;
			}
			else if ((si < box.length - 1) && (box.options[si + 1].value > "") && (down == 1)) {
				box.options[si].text = box.options[si + 1].text;
				box.options[si].value = box.options[si + 1].value;
				box.options[si + 1].text = sText;
				box.options[si + 1].value = sValue;
				box.selectedIndex++;
			}
			else if (si == 0) {
				for (var i = 0; i < (box.length - 1); i++) {
					box.options[i].text = box.options[i + 1].text;
					box.options[i].value = box.options[i + 1].value;
				}

				box.options[box.length - 1].text = sText;
				box.options[box.length - 1].value = sValue;

				box.selectedIndex = box.length - 1;
			}
			else if (si == (box.length - 1)) {
				for (var j = (box.length - 1); j > 0; j--) {
					box.options[j].text = box.options[j - 1].text;
					box.options[j].value = box.options[j - 1].value;
				}

				box.options[0].text = sText;
				box.options[0].value = sValue;

				box.selectedIndex = 0;
			}
		}
	},

	resubmitCountdown: function(formName) {
		if (Liferay.Util.submitCountdown > 0) {
			Liferay.Util.submitCountdown--;

			setTimeout("Liferay.Util.resubmitCountdown('" + formName + "')", 1000);
		}
		else {
			Liferay.Util.submitCountdown = 0;

			if (!is_ns_4) {
				document.body.style.cursor = "auto";
			}

			var form = document.forms[formName];

			for (var i = 0; i < form.length; i++){
				var e = form.elements[i];

				if (e.type && (e.type.toLowerCase() == "button" || e.type.toLowerCase() == "reset" || e.type.toLowerCase() == "submit")) {
					e.disabled = false;
				}
			}
		}
	},

	selectAndCopy: function(el) {
		el.focus();
		el.select();

		if (document.all) {
			var textRange = el.createTextRange();

			textRange.execCommand("copy");
		}
	},

	setBox: function(oldBox, newBox) {
		for (var i = oldBox.length - 1; i > -1; i--) {
			oldBox.options[i] = null;
		}

		for (var i = 0; i < newBox.length; i++) {
			oldBox.options[i] = new Option(newBox[i].value, i);
		}

		oldBox.options[0].selected = true;
	},

	setSelectedValue: function(col, value) {
		for (var i = 0; i < col.length; i++) {
			if ((col[i].value != "") && (col[i].value == value)) {
				col.selectedIndex = i;

				break;
			}
		}
	},

	setSelectVisibility: function(mode, obj) {
		if (is_ie) {
			if (obj) {
				obj = jQuery.getOne(obj);
			}
			else {
				obj = document.getElementsByTagName("body")[0];
			}

			selectList = obj.getElementsByTagName("select");
			for (var i = 0; i < selectList.length; i++) {
				selectList[i].style.visibility = mode;
			}
		}
	},

	slideMaximize: function(id, height, speed) {
		var obj = document.getElementById(id);
		var reference = obj.getElementsByTagName("DIV")[0];

		height += speed;

		if (height < (reference.offsetHeight)) {
			obj.style.height = height + "px";

			setTimeout("Liferay.Util.slideMaximize(\"" + id + "\"," + height + "," + speed + ")", 10);
		}
		else {
			obj.style.overflow = "";
			obj.style.height = "";
		}
	},

	slideMinimize: function(id, height, speed) {
		var obj = document.getElementById(id);

		height -= speed;

		if (height > 0) {
			obj.style.height = height + "px";
			setTimeout("Liferay.Util.slideMinimize(\"" + id + "\"," + height + "," + speed + ")", 10);
		}
		else {
			obj.style.height = "1px";
		}
	},

	sortBox: function(box) {
		var newBox = new Array();

		for (var i = 0; i < box.length; i++) {
			newBox[i] = new Array(box[i].value, box[i].text);
		}

		newBox.sort(Liferay.Util.sortByAscending);

		for (var i = box.length - 1; i > -1; i--) {
			box.options[i] = null;
		}

		for (var i = 0; i < newBox.length; i++) {
			box.options[box.length] = new Option(newBox[i][1], newBox[i][0]);
		}
	},

	sortByAscending: function(a, b) {
		if (a[1].toLowerCase() > b[1].toLowerCase()) {
			return 1;
		}
		else if (a[1].toLowerCase() < b[1].toLowerCase()) {
			return -1;
		}
		else {
			return 0;
		}
	},

	toggleByIdSpan: function(obj, id) {
		var hidden = Liferay.Util.toggle(id, true);
		var spanText = obj.getElementsByTagName("span");

		if (hidden) {
			spanText[0].style.display = "none";
			spanText[1].style.display = "";
		}
		else {
			spanText[0].style.display = "";
			spanText[1].style.display = "none";
		}
	},

	toggle: function(obj, returnState, displayType) {
		var hidden = false;
		var display = "";
		obj = _$J.getOne(obj);

		if (displayType != null) {
			display = displayType;
		}

		if (obj != null) {
			if (!obj.style.display || !obj.style.display.toLowerCase().match("none")) {
				obj.style.display = "none";
			}
			else {
				obj.style.display = display;
				hidden = true;
			}
		}

		if (returnState) {
			return hidden;
		}
	}
};

function cloneObject (obj, recurse) {
	for (i in obj) {
		if (typeof obj[i] == 'object' && recurse) {
			this[i] = new cloneObject(obj[i], true);
		}
		else
			this[i] = obj[i];
	}
}

Element = new Object();

Element.disable = function(element) {
	element = _$J.getOne(element);

	var items = element.getElementsByTagName("*");

	for (var i = 0; i < items.length; i++) {
		var item = items[i];
		var nodeName = item.nodeName.toLowerCase();

		item.onclick = function() {};
		item.onmouseover = function() {};
		item.onmouseout = function() {};

		if (is_ie) {
			item.onmouseenter = function() {};
			item.onmouseleave = function() {};
		}

		if (nodeName == "a") {
			item.href = "javascript: void(0)";
		}
		else if (nodeName == "input" || nodeName == "select" || nodeName == "script") {
			item.disabled = "true";
		}
		else if (nodeName == "form") {
			item.action = "";
			item.onsubmit = function() { return false; };
		}

		item.style.cursor = "default";
	}
};

Element.changeOpacity = function(object, opacity) {
	opacity = (opacity >= 100) ? 99.999 : opacity;
	opacity = (opacity < 0) ? 0 : opacity;

	object.style.opacity = (opacity / 100);
	object.style.MozOpacity = (opacity / 100);
	object.style.KhtmlOpacity = (opacity / 100);
	object.style.filter = "alpha(opacity=" + opacity + ")";
};

Element.remove = function(id) {
	var obj = jQuery.getOne(id);

	obj.parentNode.removeChild(obj);
};

function LinkedList() {
	this.head = null;
	this.tail = null;
}

LinkedList.prototype.add = function(obj) {
	obj.listInfo = new Object();
	var tail = this.tail;
	var head = this.head;

	if (this.head == null) {
		this.head = obj;
		this.tail = obj;
	}
	else {
		this.tail.listInfo.next = obj;
		obj.listInfo.prev = this.tail;
		this.tail = obj;
	}
};

LinkedList.prototype.remove = function(obj) {
	if (this.head) {
		var next = obj.listInfo.next;
		var prev = obj.listInfo.prev;

		if (next) {
			next.listInfo.prev = prev;
		}
		if (prev) {
			prev.listInfo.next = next;
		}
		if (this.head = obj) {
			this.head = next;
		}
		if (this.tail = obj) {
			this.tail = prev;
		}
	}
};

LinkedList.prototype.each = function(func) {
	var cur = this.head;
	var count = 0;

	while (cur){
		count++;
		var next = cur.listInfo.next;

		if (func) {
			func(cur);
		}

		cur = next;
	}

	return count;
};

LinkedList.prototype.size = function() {
	return this.each();
};

// String functions

function startsWith(str, x) {
	if (str.indexOf(x) == 0) {
		return true;
	}
	else {
		return false;
	}
}

function endsWith(str, x) {
	if (str.lastIndexOf(x) == str.length - x.length) {
		return true;
	}
	else {
		return false;
	}
}

// Netscape 4 functions

if (is_ns_4) {
	encodeURIComponent = new function(uri) {
		return escape(uri);
	};

	decodeURIComponent = new function(uri) {
		return unescape(uri);
	};
}

function submitForm(form, action, singleSubmit) {
	if (Liferay.Util.submitCountdown == 0) {
		Liferay.Util.submitCountdown = 10;

		setTimeout("Liferay.Util.resubmitCountdown('" + form.name + "')", 1000);

		if (singleSubmit == null || singleSubmit) {
			Liferay.Util.submitCountdown++;

			for (var i = 0; i < form.length; i++){
				var e = form.elements[i];

				if (e.type && (e.type.toLowerCase() == "button" || e.type.toLowerCase() == "reset" || e.type.toLowerCase() == "submit")) {
					e.disabled = true;
				}
			}
		}

		if (action != null) {
			form.action = action;
		}

		if (!is_ns_4) {
			document.body.style.cursor = "wait";
		}

		form.submit();
	}
	else {
		if (Liferay.Util.submitFormAlert != null) {
			submitFormAlert(Liferay.Util.submitCountdown);
		}
	}
}

var Viewport = {
	frame: function() {
		var x,y;
		if (self.innerHeight) // all except Explorer
		{
			x = self.innerWidth;
			y = self.innerHeight;
		}
		else if (document.documentElement && document.documentElement.clientHeight)
			// Explorer 6 Strict Mode
		{
			x = document.documentElement.clientWidth;
			y = document.documentElement.clientHeight;
		}
		else if (document.body) // other Explorers
		{
			x = document.body.clientWidth;
			y = document.body.clientHeight;
		}

		return (new Coordinate(x,y));
	},

	scroll: function() {
		var x,y;
		if (self.pageYOffset) {
			// all except Explorer
			x = self.pageXOffset;
			y = self.pageYOffset;
		}
		else if (document.documentElement && document.documentElement.scrollTop) {
			// Explorer 6 Strict
			x = document.documentElement.scrollLeft;
			y = document.documentElement.scrollTop;
		}
		else if (document.body) {
			// all other Explorers
			x = document.body.scrollLeft;
			y = document.body.scrollTop;
		}

		return (new Coordinate(x,y));
	},

	page: function() {
		var x,y;
		var test1 = document.body.scrollHeight;
		var test2 = document.body.offsetHeight;
		if (test1 > test2) // all but Explorer Mac
		{
			x = document.body.scrollWidth;
			y = document.body.scrollHeight;
		}
		else // Explorer Mac;
		    //would also work in Explorer 6 Strict, Mozilla and Safari
		{
			x = document.body.offsetWidth;
			y = document.body.offsetHeight;
		}

		return (new Coordinate(x,y));
	}
};

String.prototype.trim = jQuery.trim;

var ZINDEX = {
	ALERT: 100,
	CHAT_BOX: 11,
	DRAG_ITEM: 10,
	DRAG_ARROW: 9
};

//0-100: Theme Developer
//100-200: Portlet Developer
//200-300: Liferay

//var Liferay.zIndex = {};