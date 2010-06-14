(function(){

//YAHOO.namespace("icefaces.calendar");
var calendarns = this, i, ns;
//calendarns = (function(){return this;}).call();
//ns = "YAHOO.icefaces.calendar".split(".");
ns = "ice.component.calendar".split(".");
for (i = 0; i < ns.length; i++) {
    calendarns[ns[i]] = calendarns[ns[i]] || {};
    calendarns = calendarns[ns[i]];
}
YAHOO.util.Event.onDOMReady(function() {
    var calLogReader = new YAHOO.widget.LogReader(null, {newestOnTop:false});
    calLogReader.setTitle("Calendar Logger");
    calLogReader.hideSource("global");
    calLogReader.hideSource("LogReader");
});
var logger = new YAHOO.widget.LogWriter("Calendar 4");
//YAHOO.icefaces.calendar.logger = new YAHOO.widget.LogWriter("Calendar 4");

var YuiCalendar = YAHOO.widget.Calendar;
var IceCalendar = function(container, config, params) {
    IceCalendar.superclass.constructor.call(this, container, config);
    this.params = params;
};
var renderFooter = function(html) {
    html = IceCalendar.superclass.renderFooter.call(this, html);
    var hourField = this.cfg.getProperty("hourField");
    if (!hourField) return html;

    var selectedHour = parseInt(this.cfg.getProperty("selectedHour"), 10);
    var selectedMinute = parseInt(this.cfg.getProperty("selectedMinute"), 10);
    var amPmStr = this.cfg.getProperty("amPmStr");
    var amStr = this.cfg.getProperty("amStr");
    var pmStr = this.cfg.getProperty("pmStr");
    var renderAsPopup = this.cfg.getProperty("renderAsPopup");
    var selHrId = this.containerId + "_selHr";
    var selMinId = this.containerId + "_selMin";
    var selAmPmId = this.containerId + "_selAmPm";
    var Event = YAHOO.util.Event;
    var Dom = YAHOO.util.Dom;
    var calendar = this;

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
        if (!renderAsPopup) {
            calendarns.timeSelectHandler(this, evt);
        }
    };
    var selAvailable = function (selId) {
        Event.addListener(selId, "change", doSelChange, selId, calendar);
    };

    var j = parseInt(hourField.charAt(hourField.length - 1), 10);
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
        html[html.length] = temp + (i < 10 ? 0 : "") + i + "</option>";
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
        html[html.length] = temp + (i < 10 ? 0 : "") + i + "</option>";
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
var getProperty = function(key) {
    if (key == "pageDate") {
        key = "pagedate";
    } else if (key == "selectedDate") {
        key = "selected";
    }
    var value = this.cfg.getProperty(key);
    if (key == "pagedate") {
        value = YAHOO.util.Date.format(value, {format:"%m/%Y"}, "en-US");
    } else if (key == "selected") {
        value = YAHOO.util.Date.format(new Date(value[0][0], value[0][1] - 1, value[0][2]), {format:"%x"}, "en-US");
    }
    return value;
};
var setProperty = function(key, value) {
    if (key == "pageDate") {
        key = "pagedate";
    } else if (key == "selectedDate") {
        key = "selected";
    }
    this.cfg.setProperty(key, value, false);
};
var overrides = {renderFooter:renderFooter, get:getProperty, set:setProperty};
YAHOO.lang.extend(IceCalendar, YuiCalendar, overrides);

calendarns.getTime = function(calendar) {
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
    return {hr:hr, min:min};
};
calendarns.timeSelectHandler = function(calendar, evt) {
    var Dom = YAHOO.util.Dom;
    var rootDivId = calendar.params.divId, calValueEl = this[rootDivId].calValueEl;
    var date = calendar.getSelectedDates()[0];
    var time = this.getTime(calendar);
    var dateStr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + time.hr + ":" + time.min;
    calValueEl.set("value", dateStr, true);
    if (calendar.params.singleSubmit) {
        ice.se(evt, Dom.get(rootDivId), function(p) {
            p(calValueEl.get("name"), calValueEl.get("value"));
        });
    }
};
calendarns.configCal = function (calendar, params) {
    calendar.cfg.addProperty("selectedHour", {value:params.selectedHour});
    calendar.cfg.addProperty("selectedMinute", {value:params.selectedMinute});
    calendar.cfg.addProperty("hourField", {value:params.hourField});
    calendar.cfg.addProperty("amPmStr", {value:params.amPmStr});
    calendar.cfg.addProperty("amStr", {value:params.amStr});
    calendar.cfg.addProperty("pmStr", {value:params.pmStr});
    calendar.cfg.addProperty("renderAsPopup", {value:params.renderAsPopup});
    if (params.ariaEnabled) {
        calendar.renderEvent.subscribe(this.aria, null, calendar);
    }
    this[params.divId].yuiComponent = calendar;
};
calendarns.aria = function() {
    var Event = YAHOO.util.Event,
        Dom = YAHOO.util.Dom,
        KeyListener = YAHOO.util.KeyListener,
        Selector = YAHOO.util.Selector;
    var fnTrue = function() {
        return true;
    };
    Dom.setAttribute(this.id, "role", "grid");
    var tbody = Dom.getElementBy(fnTrue, "tbody", this.id);
    var weeks = Dom.getElementsBy(fnTrue, "tr", tbody);
    for (var i = 0; i < weeks.length; i++) {
        weeks[i].setAttribute("aria-label", "week " + (i + 1));
        //            Dom.setAttribute(weeks[i], "aria-label", "week " + (i + 1));
    }
    var mthYr = Dom.getElementsByClassName("calnav", null, this.id)[0];
    mthYr.setAttribute("role", "heading");
    mthYr.setAttribute("aria-label", mthYr.text);
    mthYr.setAttribute("aria-live", "assertive");
    mthYr.setAttribute("aria-atomic", "true");
    Dom.getElementsByClassName("calweekdaycell", null, this.id, function(el) {
        el.setAttribute("role", "columnheader");
    });
    Dom.getElementsByClassName("calcell", null, this.id, function(el) {
        el.setAttribute("role", "gridcell");
    });
    Dom.getElementsByClassName("selected", null, tbody, function(el) {
        el.setAttribute("aria-selected", "true");
    });
    Dom.batch(this.oDomContainer.getElementsByTagName("a"), function(el) {
        Dom.setAttribute(el, "tabindex", "-1");
    });
    Dom.setAttribute(this.oDomContainer.getElementsByTagName("a")[0], "tabindex", "0");

    var keys = KeyListener.KEY;
    var kl1Handler = function(evType, fireArgs, subscribeObj) {
        var charCode = fireArgs[0], evt = fireArgs[1];
        var target = Event.getTarget(evt), newTarget;
        var i;
        switch (charCode) {
            case keys.SPACE:
                if (Selector.test(target, ".selectable .selector")) {
                    this.doSelectCell(evt, this);
                }
                break;
            case keys.LEFT:
                if (Dom.hasClass(target, this.Style.CSS_NAV_LEFT)) {
                    for (i = this.cells.length - 1; i >= 0; i--) {
                        if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_SELECTABLE)) {
                            newTarget = this.cells[i].getElementsByTagName("a")[0];
                            break;
                        }
                    }
                } else if (Dom.hasClass(target, this.Style.CSS_NAV)) {
                    newTarget = Dom.getElementsByClassName(this.Style.CSS_NAV_LEFT, "a", this.id)[0];
                } else if (Dom.hasClass(target, this.Style.CSS_NAV_RIGHT)) {
                    newTarget = Dom.getElementsByClassName(this.Style.CSS_NAV, "a", this.id)[0];
                } else if (Dom.hasClass(target, this.Style.CSS_CELL_SELECTOR)) {
                    i = this.getIndexFromId(target.parentNode.id) - 1;
                    if (i < 0) {
                        this.doPreviousMonthNav(evt, this);
                    } else {
                        for (; i >= 0; i--) {
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = this.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_OOM)) {
                                this.doPreviousMonthNav(evt, this);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.RIGHT:
                if (Dom.hasClass(target, this.Style.CSS_NAV_LEFT)) {
                    newTarget = Dom.getElementsByClassName(this.Style.CSS_NAV, "a", this.id)[0];
                } else if (Dom.hasClass(target, this.Style.CSS_NAV)) {
                    newTarget = Dom.getElementsByClassName(this.Style.CSS_NAV_RIGHT, "a", this.id)[0];
                } else if (Dom.hasClass(target, this.Style.CSS_NAV_RIGHT)) {
                    for (i = 0; i < this.cells.length; i++) {
                        if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_SELECTABLE)) {
                            newTarget = this.cells[i].getElementsByTagName("a")[0];
                            break;
                        }
                    }
                } else if (Dom.hasClass(target, this.Style.CSS_CELL_SELECTOR)) {
                    i = this.getIndexFromId(target.parentNode.id) + 1;
                    if (i >= this.cells.length) {
                        this.doNextMonthNav(evt, this);
                    } else {
                        for (; i < this.cells.length; i++) {
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = this.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_OOM)) {
                                this.doNextMonthNav(evt, this);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.UP:
                if (Dom.hasClass(target, this.Style.CSS_CELL_SELECTOR)) {
                    i = this.getIndexFromId(target.parentNode.id) - 7;
                    if (i < 0) {
                        this.doPreviousMonthNav(evt, this);
                    } else {
                        for (; i >= 0; i -= 7) {
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = this.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_OOM)) {
                                this.doPreviousMonthNav(evt, this);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.DOWN:
                if (Dom.hasClass(target, this.Style.CSS_CELL_SELECTOR)) {
                    i = this.getIndexFromId(target.parentNode.id) + 7;
                    if (i >= this.cells.length) {
                        this.doNextMonthNav(evt, this);
                    } else {
                        for (; i < this.cells.length; i += 7) {
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = this.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(this.cells[i], this.Style.CSS_CELL_OOM)) {
                                this.doNextMonthNav(evt, this);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.PAGE_UP:
                this.doPreviousMonthNav(evt, this);
                break;
            case keys.PAGE_DOWN:
                this.doNextMonthNav(evt, this);
                break;
            case keys.HOME:
                newTarget = Dom.getElementsByClassName(this.Style.CSS_CELL_SELECTOR, "a", this.id)[0];
                break;
            case keys.END:
                newTarget = Dom.getElementsByClassName(this.Style.CSS_CELL_SELECTOR, "a", this.id).pop();
                break;
        }
        if (newTarget) {
            newTarget.focus();
            Dom.setAttribute(target, "tabindex", "-1");
            Dom.setAttribute(newTarget, "tabindex", "0");
        }
        Event.stopEvent(evt);
    };
    var kl1 = new KeyListener(this.oDomContainer,
    {keys:[keys.SPACE,keys.LEFT,keys.RIGHT,keys.UP,keys.DOWN,keys.PAGE_UP,keys.PAGE_DOWN,keys.HOME,keys.END]},
    {fn:kl1Handler, correctScope:this});
    kl1.enable();
};
calendarns.init = function(params) {
    var Element = YAHOO.util.Element,
        Event = YAHOO.util.Event,
        Dom = YAHOO.util.Dom,
        KeyListener = YAHOO.util.KeyListener;

    var rootDivId = params.divId;
    Dom.setAttribute(rootDivId, "className", "");
    Dom.get(rootDivId).innerHTML = "";
    var rootDiv = new Element(rootDivId);
    var calValueId = rootDivId + "_value";
    var calValueHtmlEl = Dom.get(calValueId);
    var calValueEl;
    rootDiv.addClass("ice-calcontainer");
    if (calValueHtmlEl) {
        calValueEl = new Element(calValueHtmlEl);
    } else {
        calValueEl = new Element(document.createElement("input"), {type:"hidden", id:calValueId, name:calValueId});
    }
    calValueEl.set("value", params.dateStr, true);
    this[rootDivId].calValueEl = calValueEl;
    Dom.insertBefore(calValueEl, rootDiv);
    if (!params.renderAsPopup) {
        function dateSelectHandler(type, args, calendar) {
            var time = calendarns.getTime(calendar);
            var dateStr = args[0][0][0] + "-" + args[0][0][1] + "-" + args[0][0][2] + " " + time.hr + ":" + time.min;
            calValueEl.set("value", dateStr, true);
            if (params.singleSubmit) {
                ice.se(null, Dom.get(rootDivId), function(p) {
                    p(calValueEl.get("name"), calValueEl.get("value"));
                });
            }
        }
        var calendar = new IceCalendar(rootDivId, {
            pagedate:params.pageDate,
            selected:params.selectedDate,
            hide_blank_weeks:true,
            navigator:true
        }, params);
        this.configCal(calendar, params);
        calendar.selectEvent.subscribe(dateSelectHandler, calendar, true);
        calendar.render();
        return;
    }
    var inputId = rootDivId + "_input";
    var inputEl = new Element(document.createElement("input"), {type:"text", value:params.dateStr, className:"text-input", id:inputId, name:inputId});
    var toggleBtnEl = new Element(document.createElement("div"), {className:"toggle-popup open-popup"});
    var inputChange = function(evt) {
        calValueEl.setAttributes({value:this.get("value")}, true);
        if (params.singleSubmit) {
            ice.se(evt, Dom.get(rootDivId), function(p) {
                p(calValueEl.get("name"), calValueEl.get("value"));
            });
        }
    };
    if (params.renderInputField) {
        inputEl.appendTo(rootDiv);
        inputEl.addListener("change", inputChange);
    }
    toggleBtnEl.appendTo(rootDiv);

    var cancelClick = function() {
        this.hide();
        toggleBtnEl.replaceClass("close-popup", "open-popup");
    };
    var okClick = function(evt, dialog) {
        this.hide();
        toggleBtnEl.replaceClass("close-popup", "open-popup");
        if (calendar.getSelectedDates().length > 0) {
            var date = calendar.getSelectedDates()[0];
            var time = calendarns.getTime(calendar);
            var dateStr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + time.hr + ":" + time.min;
            calValueEl.setAttributes({value:dateStr}, true);
            if (params.singleSubmit || params.renderInputField) {
                ice.se(evt, Dom.get(rootDivId), function(p) {
                    p(calValueEl.get("name"), calValueEl.get("value"));
                    if (!params.singleSubmit && params.renderInputField) {
                        p("formatSubmit", "formatSubmit");
                    }
                });
            }
        }
    };
    var dialog = new YAHOO.widget.Dialog(rootDivId + "_dialog", {
        visible:false,
        context:[params.renderInputField ? inputEl : toggleBtnEl, "tl", "bl"],
        buttons:[{text:"OK", handler:okClick}, {text:"Cancel", isDefault:true, handler:cancelClick}],
        draggable:false,
        close:false,
        underlay:"none",
        constraintoviewport:true
    });
    dialog.setBody("<div id='" + rootDivId + "_cal'/>");
    dialog.render(rootDiv);

    calendar = new IceCalendar(rootDivId + "_cal", {
        pagedate:params.pageDate,
        selected:params.selectedDate,
        iframe:false,
        hide_blank_weeks:true,
        navigator:true
    }, params);
    this.configCal(calendar, params);
    calendar.render();

    var toggleClick = function() {
        if (this.hasClass("open-popup")) {
            dialog.show();
            this.replaceClass("open-popup", "close-popup");
        } else {
            dialog.hide();
            this.replaceClass("close-popup", "open-popup");
        }
    };
    var inputEnter = function(evType, fireArgs, subscribeObj) {
//        console.log(this);
//        for (var i = 0; i < arguments.length; i++) {
//            console.log(arguments[i]);
//        }
        inputEl.removeListener("change", inputChange);
        calValueEl.setAttributes({value:this.value}, true);
/*
        ice.submit(fireArgs[1], this, function(p) {
            p(calValueEl.get("name"), calValueEl.get("value"));
        });
*/
        inputEl.addListener("blur", function() {
            this.addListener("change", inputChange);
        });
    };
    var inputKL = new KeyListener(inputEl.get("element"), {keys:KeyListener.KEY.ENTER},
                                  {fn:inputEnter, scope:inputEl.get("element"), correctScope:true});
    var domReady = function() {
        Event.addListener(document, "click", function(e) {
            var el = Event.getTarget(e);
            var dialogEl = dialog.element;
            var showBtn = toggleBtnEl.get("element");
            if (el != dialogEl && !Dom.isAncestor(dialogEl, el) && el != showBtn && !Dom.isAncestor(showBtn, el)) {
                dialog.hide();
                toggleBtnEl.replaceClass("close-popup", "open-popup");
            }
        });
        toggleBtnEl.addListener("click", toggleClick);
        inputKL.enable();
    };

    Event.onDOMReady(domReady);
};
calendarns.initialize = function(clientId, jsProps, jsfProps, bindYUI) {
    this[clientId] = this[clientId] || {};
    var lang = YAHOO.lang;
    var params = lang.merge({divId:clientId}, jsProps, jsfProps);
//    console.log(lang.dump(params));
    this.init(params);
    bindYUI(this[clientId].yuiComponent);
};
calendarns.updateProperties = function(clientId, jsProps, jsfProps, events) {
//    YAHOO.log("In updateProperties()", "info", "calendar.js");
//    YAHOO.log("renderAsPopup = " + jsfProps.renderAsPopup, "info", "calendar.js");
    logger.log("In updateProperties()");
    logger.log("renderAsPopup = " + jsfProps.renderAsPopup);
    var lang = YAHOO.lang;
    var context = ice.component.getJSContext(clientId);
    if (context && context.isAttached()) {
        var prevProps = lang.merge(context.getJSProps(), context.getJSFProps());
        var currProps = lang.merge(jsProps, jsfProps);
        for (var prop in currProps) {
//            console.log(prop);
            if (!lang.hasOwnProperty(currProps, prop)) continue;
            if (currProps[prop] == prevProps[prop]) continue;
            context.getComponent().destroy();
            document.getElementById(clientId)['JSContext'] = null;
            JSContext[clientId] = null;
            break;
        }
    }
    ice.component.updateProperties(clientId, jsProps, jsfProps, events, this);
};
calendarns.getInstance = function(clientId, callback) {
    ice.component.getInstance(clientId, callback, this);
};

})();
    