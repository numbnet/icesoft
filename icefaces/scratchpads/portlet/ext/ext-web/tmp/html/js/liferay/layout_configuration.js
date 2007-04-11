var LayoutConfiguration = {
	categories : new Array(),
	initialized : false,
	loadingImage : null,
	menu : null,
	menuDiv : null,
	menuIframe : null,
	portlets : new Array(),
	showTimer : 0,
	
	init : function () {
		var arrow1 = new Image();
		var arrow2 = new Image();
		arrow1.src = themeDisplay.getPathThemeImages() + "/arrows/01_down.png";
		arrow2.src = themeDisplay.getPathThemeImages() + "/arrows/01_right.png";
		
		var menu = document.getElementById("portal_add_content");
		LayoutConfiguration.menu = menu;
		
		if (menu != null) {
			var list = menu.childNodes;
			
			for (var i = 0; i < list.length; i++) {
				if (list[i].className != null && list[i].className.match("portal-add-content")) {
					LayoutConfiguration.menuDiv = list[i];
				}
				if (list[i].nodeName != null && list[i].nodeName.toLowerCase().match("iframe")) {
					LayoutConfiguration.menuIframe = list[i];
				}
			}

			var elems = menu.getElementsByTagName("div");

			for (var i = 0; i < elems.length; i++) {
				if (elems[i].className == "layout_configuration_portlet") {
					LayoutConfiguration.portlets.push(elems[i]);
				}
				else if (elems[i].className == "layout_configuration_category") {
					LayoutConfiguration.categories.push(elems[i]);
				}
			}

			LayoutConfiguration.initialized = true;
			
			// Double foucus for IE bug
			if (is_ie) {
				document.getElementById("layout_configuration_content").focus();
			}
		}
	},	

	toggle : function (plid, ppid, doAsUserId) {
		if (!LayoutConfiguration.menu) {
			var url = themeDisplay.getPathMain() + "/portal/render_portlet?p_l_id=" + plid + "&p_p_id=" + ppid + "&doAsUserId=" + doAsUserId + "&p_p_state=exclusive";
			var popup = Alerts.fireMessageBox({
					width: 250,
					noCenter: true,
					title: "Add Content",
					onClose: function() {
						LayoutConfiguration.menu = null;
					}
				});
			AjaxUtil.update(url, popup, {
							onComplete: function() {
											LayoutConfiguration.init();
											Liferay.Util.addInputType();
										}
									});
		}
	},

	startShowTimer : function (word) {
		if (this.showTimer) {
			clearTimeout(this.showTimer);
			this.showTimer = 0;
		}

		this.showTimer = setTimeout("LayoutConfiguration.showMatching(\"" + word + "\")", 250);
	},
	
	showMatching : function (word) {
		var portlets = this.portlets;
		var categories = this.categories;

		if (word == "*") {
			for (var i = 0; i < portlets.length; i++) {
				portlets[i].style.display = "block";
			}

			for (var i = 0; i < categories.length; i++) {
				categories[i].style.display = "block";
				this.toggleCategory(categories[i].getElementsByTagName("table")[0], "block");
			}
		}
		else if (word == "") {
			for (var i = 0; i < categories.length; i++) {
				categories[i].style.display = "block";
				this.toggleCategory(categories[i].getElementsByTagName("table")[0], "none");
			}
			for (var i = 0; i < portlets.length; i++) {
				portlets[i].style.display = "block";
			}
		}
		else {
			word = word.toLowerCase();

			for (var i = 0; i < categories.length; i++) {
				categories[i].style.display = "none";
			}

			for (var i = 0; i < portlets.length; i++) {
				if (portlets[i].id.toLowerCase().match(word)) {
					portlets[i].style.display = "block";

					this.showCategories(categories, portlets[i].id);
				}
				else {
					portlets[i].style.display = "none";
				}
			}
		}
	},

	showCategories : function (categories, name) {
		var colon = name.lastIndexOf(":");

		while (colon != -1) {
			name = name.substr(0, colon);

			for (var i = 0; i < categories.length; i++) {
				if (name.match(categories[i].id)) {
					categories[i].style.display = "block";
					this.toggleCategory(categories[i].getElementsByTagName("table")[0], "block");
				}
			}

			colon = name.lastIndexOf(":");
		}
	},

	toggleCategory : function (obj, display) {
		var parent = obj;
		
		while(parent.nodeName.toLowerCase() != "table") {
			parent = parent.parentNode;
		}
		
		var data = parent.rows[1].cells[0];
		var pane = _$J(".layout_configuration_category_pane:first", data).get(0);
		var image = obj.getElementsByTagName("img")[0];
		var imagePath = themeDisplay.getPathThemeImages();
		
		if (display) {
			pane.style.display = display;
			if (display.toLowerCase().match("block")) {
				image.src = imagePath + "/arrows/01_down.png";
			}
			else {
				image.src = imagePath + "/arrows/01_right.png";
			}
		}
		else {
			if (Liferay.Util.toggle(pane, true)) {
				image.src = imagePath + "/arrows/01_down.png";
			}
			else {
				image.src = imagePath + "/arrows/01_right.png";
			}
		}
	}

};