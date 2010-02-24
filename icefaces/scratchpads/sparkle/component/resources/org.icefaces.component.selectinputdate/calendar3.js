YAHOO.widget.Calendar.prototype.renderFooter = function(html) {
    var hourField = this.cfg.getProperty("hourField");
    if (!hourField) return html;

    var selectedHour = parseInt(this.cfg.getProperty("selectedHour"), 10);
    var selectedMinute = parseInt(this.cfg.getProperty("selectedMinute"), 10);
    var amPmStr = this.cfg.getProperty("amPmStr");
    var amStr = this.cfg.getProperty("amStr");
    var pmStr = this.cfg.getProperty("pmStr");
    var selHrId = this.containerId + "_selHr";
    var selMinId = this.containerId + "_selMin";
    var selAmPmId = this.containerId + "_selAmPm";
    var Event = YAHOO.util.Event;
    var Dom = YAHOO.util.Dom;
    var cal =this;

    var doSelChange = function (evt, selId) {
        var selValue =  Dom.get(selId).value;
        var result;
        if (selId == selHrId) {
            result = this.cfg.setProperty("selectedHour", selValue, true);
        } else if (selId == selMinId) {
            result = this.cfg.setProperty("selectedMinute", selValue, true);
        } else if (selId == selAmPmId) {
            result = this.cfg.setProperty("amPmStr", selValue, true);
        }
    };
    var selAvailable = function (selId) {
        Event.addListener(selId, "change", doSelChange, selId, cal);
    };

    var j = parseInt(hourField[hourField.length - 1], 10);
    var k = hourField.match(/^HOUR_OF_DAY[01]$/) ? j + 23 : j + 11;
    var i, temp;
    html[html.length] = "<tfoot><tr><td align='center' colspan='7'>";
    html[html.length] = "<select" + " id='" + selHrId + "'>";
    for (i = j; i <= k; i++) {
        temp = "<option value='" + i + "'";
        if (i == selectedHour) {
            temp += " selected='selected'";
        }
        temp += ">";
        html[html.length] = temp + i + "</option>";
    }
    html[html.length] = "</select>";
    Event.onAvailable(selHrId, selAvailable, selHrId);
    html[html.length] = ": ";
    html[html.length] = "<select" + " id='" + selMinId + "'>";
    for (i = 0; i <= 59; i++) {
        temp = "<option value='" + i + "'";
        if (i == selectedMinute) {
            temp += " selected='selected'";
        }
        temp += ">";
        html[html.length] = temp + i + "</option>";
    }
    html[html.length] = "</select>";
    Event.onAvailable(selMinId, selAvailable, selMinId);
    if (hourField.match(/^HOUR[01]$/)) {
        html[html.length] = "<select" + " id='" + selAmPmId + "'>";
        temp = "<option value='" + amStr + "'";
        if (amStr == amPmStr) {
            temp += " selected='selected'";
        }
        temp += ">";
        html[html.length] = temp + amStr + "</option>";
        temp = "<option value='" + pmStr + "'";
        if (pmStr == amPmStr) {
            temp += " selected='selected'";
        }
        temp += ">";
        html[html.length] = temp + pmStr + "</option>";
        html[html.length] = "</select>";
        Event.onAvailable(selAmPmId, selAvailable, selAmPmId);
    }
    html[html.length] = "</td></tr></tfoot>";
    return html;
};

YAHOO.namespace("icefaces.calendar.calendars");

YAHOO.icefaces.calendar.init = function(params) {
    var Element = YAHOO.util.Element,
        Event = YAHOO.util.Event,
        Dom = YAHOO.util.Dom,
        KeyListener = YAHOO.util.KeyListener,
        Calendar = YAHOO.widget.Calendar;

    var rootDivId = params.divId;
    var rootDiv = new Element(rootDivId);
    var inputEl = new Element(document.createElement("input"), {type:"text", value:params.dateStr, size:"30"});
    var buttonEl = new Element(document.createElement("button"), {type:"button"});
    var imageEl = new Element(document.createElement("img"), {src:"images/cal_button.gif"});
    inputEl.appendTo(rootDiv);
    buttonEl.appendTo(rootDiv);
    imageEl.appendTo(buttonEl);

    var cancelClick = function() {
        this.hide();
    };
    var okClick = function(evt, dialog) {
        this.hide();
        if (calendar.getSelectedDates().length > 0) {
            var selDate = calendar.getSelectedDates()[0];
            var hr = 0, min = 0;
            var hourField = calendar.cfg.getProperty("hourField");
            if (hourField) {
                hr = parseInt(calendar.cfg.getProperty("selectedHour"), 10);
                min = parseInt(calendar.cfg.getProperty("selectedMinute"), 10);
                if (hourField == "HOUR_OF_DAY1" && hr == 24 || hourField == "HOUR1" && hr == 12) {
                    hr = 0;
                }
                if (hourField.match(/^HOUR[01]$/) && calendar.cfg.getProperty("amPmStr") == calendar.cfg.getProperty("pmStr")) {
                    hr += 12;
                }
            }
            var dateStr = selDate.getFullYear() + "-" + (selDate.getMonth() + 1) + "-" + selDate.getDate() + " " + hr + ":" + min;
//            var dateStr = selDate.getFullYear() + "-" + (selDate.getMonth() + 1) + "-" + selDate.getDate() +
//                    " " + hrSelEl.get("value") + ":" + minSelEl.get("value");
//            inputEl.setAttributes({value:dateStr}, true);
            ice.submit(evt, Dom.get(calendar.id), function(p) {
                p(rootDivId, dateStr);
            });
        }
    };
    var dialog = new YAHOO.widget.Dialog(rootDivId + "_dialog", {
        visible:false,
        context:[inputEl, "tl", "bl"],
        buttons:[{text:"OK", handler:okClick}, {text:"Cancel", isDefault:true, handler:cancelClick}],
        draggable:true,
        close:true
    });
    dialog.setHeader("Date of Independence");
    dialog.setBody("<div id='" + rootDivId + "_cal'/>");
    var hrSelEl = new Element(document.createElement("select"));
    var optionEl;
    for (i = 0; i < 24; i++) {
        optionEl = new Element(document.createElement("option"), {value:i});
        if (i == params.selectedHour) optionEl.set("selected", "selected", true);
        optionEl.appendChild(document.createTextNode(i));
        optionEl.appendTo(hrSelEl);
    }
//    dialog.appendToBody(hrSelEl.get("element"));
//    dialog.appendToBody(document.createTextNode(":"));
    var minSelEl = new Element(document.createElement("select"));
    for (i = 0; i < 60; i++) {
        optionEl = new Element(document.createElement("option"), {value:i});
        if (i == params.selectedMinute) optionEl.set("selected", "selected", true);
        optionEl.appendChild(document.createTextNode(i));
        optionEl.appendTo(minSelEl);
    }
//    dialog.appendToBody(minSelEl.get("element"));
    var docFrag = document.createDocumentFragment();
    docFrag.appendChild(hrSelEl.get("element"));
    docFrag.appendChild(document.createTextNode(" : "));
    docFrag.appendChild(minSelEl.get("element"));
//    dialog.appendToBody(docFrag);
    dialog.render(rootDiv);

    var calendar = new Calendar(rootDivId + "_cal", {
        pagedate:params.pageDate,
        selected:params.selectedDate,
        iframe:false,
        HIDE_BLANK_WEEKS:true,
        navigator:true
    });
    calendar.cfg.addProperty("selectedHour", {value:params.selectedHour});
    calendar.cfg.addProperty("selectedMinute", {value:params.selectedMinute});
    calendar.cfg.addProperty("hourField", {value:params.hourField});
    calendar.cfg.addProperty("amPmStr", {value:params.amPmStr});
    calendar.cfg.addProperty("amStr", {value:params.amStr});
    calendar.cfg.addProperty("pmStr", {value:params.pmStr});
    if (params.minDate) {
        calendar.cfg.setProperty(Calendar.DEFAULT_CONFIG.MINDATE.key, params.minDate);
    }
    if (params.maxDate) {
        calendar.cfg.setProperty(Calendar.DEFAULT_CONFIG.MAXDATE.key, params.maxDate);
    }
    if (params.disabledDates) {
        calendar.addRenderer(params.disabledDates, calendar.renderBodyCellRestricted);
    }
    calendar.render();

    var buttonClick = function() {
        dialog.show();
    };
    var inputEnter = function(evType, fireArgs, subscribeObj) {
//        for (var i = 0; i < arguments.length; i++) {
//            alert(arguments[i]);
//        }
        var dateStr = this.value;
        ice.submit(fireArgs[1], this, function(p) {
            p(rootDivId, dateStr);
        });
    };
    var inputKL = new KeyListener(inputEl.get("element"), {keys:KeyListener.KEY.ENTER},
                                  {fn:inputEnter, scope:inputEl.get("element"), correctScope:true});
    var domReady = function() {
        Event.addListener(document, "click", function(e) {
            var el = Event.getTarget(e);
            var dialogEl = dialog.element;
            var showBtn = buttonEl.get("element");
            if (el != dialogEl && !Dom.isAncestor(dialogEl, el) && el != showBtn && !Dom.isAncestor(showBtn, el)) {
                dialog.hide();
            }
        });
        buttonEl.addListener("click", buttonClick);
        inputKL.enable();
    };

    Event.onDOMReady(domReady);
};
