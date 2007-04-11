var Alerts = {
	
	background: null,
	message: null,
	messageArray: new Array(),
	fadeTimer: 0,
	OPACITY: 51,
	STEPS: 3,

	bgFadeIn: function(max, steps, opacity) {
		var background = Alerts.background;
		var delta = max/steps;
		
		if (opacity == null) {
			opacity = delta;
		}
		
		if (background && opacity <= max) {
			Element.changeOpacity(background, opacity);
			opacity += delta;
			setTimeout("Alerts.bgFadeIn(" + max + "," + steps + "," + opacity + ")", 0);
		}
	},

	bgFadeOut: function(max, steps, opacity) {
		var background = Alerts.background;
		if (background) {
			var delta = max/steps;
			
			if (opacity == null) {
				opacity = max - delta;
			}
			
			if (opacity >= 0) {
				Element.changeOpacity(background, opacity);
				opacity -= delta;
				setTimeout("Alerts.bgFadeOut(" + max + "," + steps + "," + opacity + ")", 0);
			}
			else {
				background.parentNode.removeChild(background);
				Liferay.Util.setSelectVisibility("visible", Alerts.message);
				Alerts.background = null;
			}
		}
	},

	createWrapper: function(message, title) {
		var outer = document.createElement("div");
		var inner = document.createElement("div");
		var heading = document.createElement("table");
		var close = document.createElement("a");
		
		outer.className = "popup";
		outer.align = "center";
		inner.className = "popup-inner";
		
		close.innerHTML = "Close";
		close.href = "javascript:Alerts.killAlert()";
		
		heading.className = "popup-header";
		heading.border = 0;
		heading.width = "100%";
		heading.cellSpacing = 0;
		heading.cellPadding = 0;
		heading.insertRow(0);
		
		var row = heading.rows[0];
		row.insertCell(0);
		row.insertCell(1);
		
		var cell0 = row.cells[0];
		var cell1 = row.cells[1];
		cell0.className = "popup-title";
		cell0.width = "99%";
		
		if (title) {
			cell0.innerHTML = title;
		}
		
		cell1.className = "popup-close";
		cell1.width = "1%";
		cell1.innerHTML = "<a href=\"javascript:void(0)\" onclick=\"Alerts.killAlert(this)\"><img border=\"0\" src=\"" + themeDisplay.getPathThemeImages() + "/portlet/close.png\"/></a>";
		
		inner.appendChild(heading);
		inner.appendChild(message);
		outer.appendChild(inner);
		
		message.wrapper = outer;
		
		Drag.makeDraggable(outer, cell0);
		
		return outer;
	},

	killAlert : function(oLink) {
		if (oLink) {
			var wrapper = oLink;
			
			while (wrapper.parentNode) {
				if (wrapper.className && wrapper.className == "popup") {
					break;
				}
				wrapper = wrapper.parentNode;
			}
			
			var body = document.getElementsByTagName("body")[0];
			var options = wrapper.options;
			var background = null;
			var showSelects = false;
			
			Alerts.remove(wrapper);
			body.removeChild(wrapper);
			
			if (Alerts.messageArray.length > 0) {
				Alerts.message = Alerts.messageArray[Alerts.messageArray.length - 1];
				Alerts.message.style.zIndex = ZINDEX.ALERT + 1;
				Liferay.Util.setSelectVisibility("visible", Alerts.message);
				background = wrapper.background;
			}
			else {
				Alerts.message = null;
				background = Alerts.background;
			}
			
			if (background) {
				Alerts.bgFadeOut(Alerts.OPACITY, Alerts.STEPS);
			}
			
			if (options && options.onClose) options.onClose();
		}
	},

	fireMessageBox : function (options) {
		/*
		 * OPTIONS:
		 * modal (boolean) - show shaded background
		 * message (string) - default HTML to display
		 * height (int) - starting height of message box
		 * width (int) - starting width of message box
		 * onClose (function) - executes after closing
		 */
		var body = document.body;
		
		if (!options) options = new Object();
		
		var modal = options.modal;
		var myMessage = options.message;
		var msgHeight = options.height;
		var msgWidth = options.width;
		var noCenter = options.noCenter;
		var title = options.title;
		

		var message = document.createElement("div");
		message.align = "left";
		
		var wrapper = Alerts.createWrapper(message, title);
		wrapper.style.position = "absolute";
		wrapper.style.top = 0;
		wrapper.style.left = 0;
		wrapper.style.zIndex = ZINDEX.ALERT + 1;
		wrapper.options = options;
		
		if (myMessage) {
			message.innerHTML = myMessage;
		}
		else {
			message.innerHTML = "<div class=\"loading-animation\"></div>";
		}
		
		if (msgHeight) {
			if (is_ie) {
				message.style.height = msgHeight + "px";
			}
			else {
				message.style.minHeight = msgHeight + "px";
			}
		}
		
		if (msgWidth) {
			wrapper.style.width = msgWidth + "px";
		}
		
		if (!Alerts.background && modal) {
			var background = document.createElement("div");
			background.id = "alert-message";
			background.style.position = "absolute";
			background.style.top = "0";
			background.style.left = "0";
			background.style.zIndex = ZINDEX.ALERT;
			
			Alerts.background = background;
			wrapper.background = background;
			
			background.style.backgroundColor = "#000000";
			Element.changeOpacity(background, 0);
			body.appendChild(background);
			Alerts.bgFadeIn(Alerts.OPACITY, Alerts.STEPS);
		}
		Liferay.Util.setSelectVisibility("hidden");
		
		if (Alerts.messageArray.length > 0) {
			var lastMsg = Alerts.messageArray[Alerts.messageArray.length - 1];
			lastMsg.style.zIndex = ZINDEX.ALERT - 1;
			Liferay.Util.setSelectVisibility("hidden", lastMsg);
		}

		Liferay.Util.setSelectVisibility("visible", message);
		
		Alerts.message = message;
		Alerts.messageArray.push(wrapper);
		
		Alerts.resize();
		_$J(window).resize(Alerts.resize);
		
		if (noCenter) {
			Alerts.center();
		}
		else {
			Alerts.center(msgHeight, msgWidth);
		}

		_$J(window).resize(Alerts.center);
		body.appendChild(wrapper);
		Liferay.Util.addInputType(wrapper);
		window.focus();
		return message;
	},
	
	popupIframe : function(url, options) {
		var msgHeight = options.height;
		var msgWidth = options.width;
		var message = Alerts.fireMessageBox(options);
		var iframe = document.createElement("iframe");
		
		message.height = "";
		iframe.src = url;
		iframe.frameBorder = 0;
		if (msgWidth) iframe.style.width = "100%";
		
		message.appendChild(iframe);
		if (!options.noCenter) {
			Alerts.center(msgHeight, msgWidth);
		}
		
		return message;
	},
	
	center : function(height, width) {
        
        if (Alerts.message) {
	        var message = Alerts.message.wrapper;
            var body = document.getElementsByTagName("body")[0];
            var mode = message.centerMode;
            
            if (!mode) {
	            if (height && width) {
	            	mode = message.centerMode = "xy";
	            }
	            else if (height && !width) {
	            	mode = message.centerMode = "y";
	            }
	            else if (!height && width) {
	            	mode = message.centerMode = "x";
	            }
	            else {
	            	mode = message.centerMode = "none";
	            }
            }
            
            width = width || message.offsetWidth;
            height = height || message.offsetHeight;

            var centerLeft;
            var centerTop;

            if (!is_safari) {
                var centerLeft = (body.clientWidth - width) / 2;
                var centerTop = body.scrollTop + ((body.clientHeight - height) / 2);
            }
            else {
                var centerLeft = (body.offsetWidth - width) / 2;
                var centerTop = (body.offsetHeight - height) / 2;
            }

			if (mode == "xy" || mode == "y") {
	            message.style.top = centerTop + "px";
            }
            else {
	            message.style.top = (body.scrollTop + 20) + "px";
            }
            
            if (mode == "xy" || mode == "x") {
	            message.style.left = centerLeft + "px";
            }
            else {
	            message.style.left = "20px";
            }
        }
	},
	
    resize: function() {
    	if (Alerts.background) {
	        var background = Alerts.background;
	        var body = document.getElementsByTagName("body")[0];
	
	        if (!is_safari) {
	        	var scrollHeight = body.scrollHeight;
	        	var clientHeight = body.clientHeight;
	        	
	            background.style.height = (scrollHeight > clientHeight ? scrollHeight : clientHeight) + "px";
	            //background.style.width = (body.offsetWidth > body.clientWidth ? body.offsetWidth : body.clientWidth) + "px";
	            background.style.width = "100%";
	        }
	        else {
	            background.style.height = body.offsetHeight + "px";
	            background.style.width = body.offsetWidth + "px";
	        }
    	}
    },
    
    resizeIframe: function(options) {
    	if (Alerts.message && options) {
    		var iframe = Alerts.message.getElementsByTagName("iframe")[0];
			var loading = _$J.getOne(".loading-animation", Alerts.message);
			
			if (loading) {
				loading.parentNode.removeChild(loading);
			}
    		
    		if (iframe) {
	    		if (options.height) {
	    			iframe.height = options.height;
	    		}
    		
	    		if (options.width) {
	    			iframe.width = options.width;
	    		}
    		}
    	}
    	
    	Alerts.resize();
    },

    remove: function(obj) {
    	var msgArray = Alerts.messageArray;
    	
    	for (var i = 0; i < msgArray.length; i++) {
    		if (msgArray[i] == obj) {
    			msgArray.splice(i, 1);
    			break;
    		}
    	}
    }
};