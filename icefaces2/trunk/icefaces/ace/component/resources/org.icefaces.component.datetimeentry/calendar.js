/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

//(function(){
/*
//YAHOO.namespace("icefaces.calendar");
var calendarns = this, i, ns;
//calendarns = (function(){return this;}).call();
//ns = "YAHOO.icefaces.calendar".split(".");
ns = "ice.component.calendar".split(".");
for (i = 0; i < ns.length; i++) {
    calendarns[ns[i]] = calendarns[ns[i]] || {};
    calendarns = calendarns[ns[i]];
}
*/
//var calendarns = ice.component.calendar; // namespace creation as in tabset.js
/*
YAHOO.util.Event.onDOMReady(function() {
    var calLogReader = new YAHOO.widget.LogReader(null, {newestOnTop:false});
    calLogReader.setTitle("Calendar Logger");
    calLogReader.hideSource("global");
    calLogReader.hideSource("LogReader");
});
var logger = new YAHOO.widget.LogWriter("Calendar 4");
*/
ice.component.calendar = { // public functions for calendar namespace
IceCalendar: function(container, config, params) { // ICE calendar constructor
    ice.component.calendar.IceCalendar.superclass.constructor.call(this, container, config);
    this.params = params;
},
isReady: false,
setupLib: function() {
		var thisYUI = ice.yui3.getNewInstance();
		thisYUI.use('yui2-yahoo', 'yui2-yahoo-dom-event', 'yui2-dom', 'yui2-event', 'yui2-element', 'yui2-json', 'yui2-container', 'yui2-selector', 'yui2-datasource', 'yui2-calendar', 'yui2-button', function(Yui) {
			var YAHOO = Yui.YUI2;
//var YAHOO = ice.component.calendar.getUtilYUI();
var YuiCalendar = YAHOO.widget.Calendar,
           lang = YAHOO.lang,
           JSON = lang.JSON,
            Dom = YAHOO.util.Dom,
          Event = YAHOO.util.Event;

var renderFooter = function(html) {
    html = ice.component.calendar.IceCalendar.superclass.renderFooter.call(this, html);
    var cfg = this.cfg;
    var hourField = cfg.getProperty("hourField");
    if (!hourField) return html;

    var selectedHour = parseInt(cfg.getProperty("selectedHour"), 10);
    var selectedMinute = parseInt(cfg.getProperty("selectedMinute"), 10);
    var amPmStr = cfg.getProperty("amPmStr");
    var amStr = cfg.getProperty("amStr");
    var pmStr = cfg.getProperty("pmStr");
    var renderAsPopup = cfg.getProperty("renderAsPopup");
    var selHrId = this.containerId + "_selHr";
    var selMinId = this.containerId + "_selMin";
    var selAmPmId = this.containerId + "_selAmPm";
    var disabled = this.params.disabled ? " disabled='disabled'" : "";
    var Event = YAHOO.util.Event;
    var Dom = YAHOO.util.Dom;
    var calendar = this;

    var doSelChange = function (evt, selId) {
        var selValue = Dom.get(selId).value;
        var result, cfg = this.cfg;
        if (selId == selHrId) {
            result = cfg.setProperty("selectedHour", selValue, true);
        } else if (selId == selMinId) {
            result = cfg.setProperty("selectedMinute", selValue, true);
        } else if (selId == selAmPmId) {
            result = cfg.setProperty("amPmStr", selValue, true);
        }
        ice.setFocus(selId);
        if (!renderAsPopup) {
            ice.component.calendar.timeSelectHandler(this, evt);
        }
    };
    var selAvailable = function (selId) {
        Event.addListener(selId, "change", doSelChange, selId, calendar);
    };

    var j = parseInt(hourField.charAt(hourField.length - 1), 10);
    var k = hourField.match(/^HOUR_OF_DAY[01]$/) ? j + 23 : j + 11;
    var i, temp;
    html[html.length] = "<tfoot><tr><td align='center' colspan='7'";
    if (this.params.disabled) {
        html[html.length] = " class='disabled'";
    }
    html[html.length] = ">";
    html[html.length] = "<select" + " id='" + selHrId + "' name='" + selHrId + "'" + disabled + ">";
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
    html[html.length] = "<select" + " id='" + selMinId + "' name='" + selMinId + "'" + disabled + ">";
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
        html[html.length] = "<select" + " id='" + selAmPmId + "' name='" + selAmPmId + "'" + disabled + ">";
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
var selectCell = function(cellIndex) {
    this.currentFocus = this.cells[cellIndex].id;
    ice.component.calendar.IceCalendar.superclass.selectCell.call(this, cellIndex);
};
var styleCellDefault = function(workingDate, cell) {
    if (this.params.disabled) {
        Dom.addClass(cell, "disabled");
    } else {
        Dom.addClass(cell, this.Style.CSS_CELL_SELECTABLE);
    }
};
var renderCellDefault = function(workingDate, cell) {
    if (this.params.disabled) {
        cell.innerHTML = workingDate.getDate();
    } else {
        cell.innerHTML = '<a href="#" class="' + this.Style.CSS_CELL_SELECTOR + '">' + this.buildDayLabel(workingDate) + "</a>";
    }
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
var overrides = {renderFooter:renderFooter, selectCell:selectCell, styleCellDefault:styleCellDefault,
                 renderCellDefault:renderCellDefault, get:getProperty, set:setProperty};
lang.extend(ice.component.calendar.IceCalendar, YuiCalendar, overrides);

});
},
getTime: function(calendar) {
    var hr = 0, min = 0;
    var cfg = calendar.cfg;
    var hourField = cfg.getProperty("hourField");
    if (hourField) {
        hr = parseInt(cfg.getProperty("selectedHour"), 10);
        min = parseInt(cfg.getProperty("selectedMinute"), 10);
        if (hourField == "HOUR_OF_DAY1" && hr == 24 || hourField == "HOUR1" && hr == 12) {
            hr = 0;
        }
        if (hourField.match(/^HOUR[01]$/) && cfg.getProperty("amPmStr") == cfg.getProperty("pmStr")) {
            hr += 12;
        }
    }
    return {hr:hr, min:min};
},
timeSelectHandler: function(calendar, evt) {
    var rootId = calendar.params.clientId, calValueEl = this[rootId].calValueEl;
    var date = calendar.getSelectedDates()[0];
    var time = this.getTime(calendar);
    var dateStr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + time.hr + ":" + time.min;
    calValueEl.set("value", dateStr, true);
    if (calendar.params.singleSubmit) {
        ice.se(evt, rootId);
    }
},
configCal: function (calendar, params) {
    var cfg = calendar.cfg;
    cfg.addProperty("selectedHour", {value:params.selectedHour});
    cfg.addProperty("selectedMinute", {value:params.selectedMinute});
    cfg.addProperty("hourField", {value:params.hourField});
    cfg.addProperty("amPmStr", {value:params.amPmStr});
    cfg.addProperty("amStr", {value:params.amStr});
    cfg.addProperty("pmStr", {value:params.pmStr});
    cfg.addProperty("renderAsPopup", {value:params.renderAsPopup});
    cfg.setProperty("MONTHS_LONG", params.longMonths);
    cfg.setProperty("WEEKDAYS_SHORT", params.shortWeekdays);
    if (params.ariaEnabled) {
        calendar.renderEvent.subscribe(this.aria, null, calendar);
    }
    this[params.clientId].yuiComponent = calendar;
},
aria: function() {
	//var YAHOO = ice.component.calendar.getUtilYUI();
	var thiz = this;
		var thisYUI = ice.yui3.getNewInstance();
		thisYUI.use('yui2-yahoo', 'yui2-yahoo-dom-event', 'yui2-dom', 'yui2-event', 'yui2-element', 'yui2-json', 'yui2-container', 'yui2-selector', 'yui2-datasource', 'yui2-calendar', 'yui2-button', function(Yui) {
			var YAHOO = Yui.YUI2;
    var Event = YAHOO.util.Event,
        Dom = YAHOO.util.Dom,
        KeyListener = YAHOO.util.KeyListener,
        Selector = YAHOO.util.Selector;
    var fnTrue = function() {
        return true;
    };
    Dom.setAttribute(thiz.id, "role", "grid");
    var tbody = Dom.getElementBy(fnTrue, "tbody", thiz.id);
    var weeks = Dom.getElementsBy(fnTrue, "tr", tbody);
    for (var i = 0; i < weeks.length; i++) {
        weeks[i].setAttribute("aria-label", "week " + (i + 1));
        //            Dom.setAttribute(weeks[i], "aria-label", "week " + (i + 1));
    }
/*
    var mthYr = Dom.getElementsByClassName("calnav", null, thiz.id)[0];
    mthYr.setAttribute("role", "heading");
    mthYr.setAttribute("aria-label", mthYr.text);
    mthYr.setAttribute("aria-live", "assertive");
    mthYr.setAttribute("aria-atomic", "true");
*/
    Dom.getElementsByClassName("calweekdaycell", null, thiz.id, function(el) {
        el.setAttribute("role", "columnheader");
    });
    Dom.getElementsByClassName("calcell", null, thiz.id, function(el) {
        el.setAttribute("role", "gridcell");
    });
    Dom.getElementsByClassName("selected", null, tbody, function(el) {
        el.setAttribute("aria-selected", "true");
    });
    var tabIndex = thiz.params.tabIndex;
    var selectedCell = Dom.getElementsByClassName(thiz.Style.CSS_CELL_SELECTED, "td", thiz.id)[0];
    Dom.batch(thiz.oDomContainer.getElementsByTagName("a"), function(el) {
        Dom.setAttribute(el, "tabindex", "-1");
    });
    if (selectedCell) {
        Dom.setAttribute(Dom.getFirstChild(selectedCell), "tabindex", tabIndex);
    } else {
        Dom.setAttribute(thiz.oDomContainer.getElementsByTagName("a")[0], "tabindex", tabIndex);
    }
    var keys = KeyListener.KEY;
    var kl1Handler = function(evType, fireArgs, subscribeObj) {
        var charCode = fireArgs[0], evt = fireArgs[1];
        var target = Event.getTarget(evt), newTarget;
        var i;
        switch (charCode) {
            case keys.SPACE:
                if (Selector.test(target, ".selectable .selector")) {
                    thiz.doSelectCell(evt, thiz);
                }
                break;
            case keys.LEFT:
                if (Dom.hasClass(target, thiz.Style.CSS_NAV_LEFT)) {
                    for (i = thiz.cells.length - 1; i >= 0; i--) {
                        if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_SELECTABLE)) {
                            newTarget = thiz.cells[i].getElementsByTagName("a")[0];
                            break;
                        }
                    }
                } else if (Dom.hasClass(target, thiz.Style.CSS_NAV)) {
                    newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV_LEFT, "a", thiz.id)[0];
                } else if (Dom.hasClass(target, thiz.Style.CSS_NAV_RIGHT)) {
//                    newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV, "a", thiz.id)[0];
                    newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV_LEFT, "a", thiz.id)[0];
                } else if (Dom.hasClass(target, thiz.Style.CSS_CELL_SELECTOR)) {
                    i = thiz.getIndexFromId(target.parentNode.id) - 1;
                    if (i < 0) {
                        newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV_RIGHT, "a", thiz.id)[0];
//                        thiz.doPreviousMonthNav(evt, thiz);
                    } else {
                        for (; i >= 0; i--) {
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = thiz.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_OOM)) {
                                newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV_RIGHT, "a", thiz.id)[0];
//                                thiz.doPreviousMonthNav(evt, thiz);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.RIGHT:
                if (Dom.hasClass(target, thiz.Style.CSS_NAV_LEFT)) {
//                    newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV, "a", thiz.id)[0];
//                } else if (Dom.hasClass(target, thiz.Style.CSS_NAV)) {
                    newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV_RIGHT, "a", thiz.id)[0];
                } else if (Dom.hasClass(target, thiz.Style.CSS_NAV_RIGHT)) {
                    for (i = 0; i < thiz.cells.length; i++) {
                        if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_SELECTABLE)) {
                            newTarget = thiz.cells[i].getElementsByTagName("a")[0];
                            break;
                        }
                    }
                } else if (Dom.hasClass(target, thiz.Style.CSS_CELL_SELECTOR)) {
                    i = thiz.getIndexFromId(target.parentNode.id) + 1;
                    if (i >= thiz.cells.length) {
                        newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV_LEFT, "a", thiz.id)[0];
//                        thiz.doNextMonthNav(evt, thiz);
                    } else {
                        for (; i < thiz.cells.length; i++) {
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = thiz.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_OOM)) {
                                newTarget = Dom.getElementsByClassName(thiz.Style.CSS_NAV_LEFT, "a", thiz.id)[0];
//                                thiz.doNextMonthNav(evt, thiz);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.UP:
                if (Dom.hasClass(target, thiz.Style.CSS_CELL_SELECTOR)) {
                    i = thiz.getIndexFromId(target.parentNode.id) - 7;
                    if (i < 0) {
//                        thiz.doPreviousMonthNav(evt, thiz);
                    } else {
                        for (; i >= 0; i -= 7) {
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = thiz.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_OOM)) {
//                                thiz.doPreviousMonthNav(evt, thiz);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.DOWN:
                if (Dom.hasClass(target, thiz.Style.CSS_CELL_SELECTOR)) {
                    i = thiz.getIndexFromId(target.parentNode.id) + 7;
                    if (i >= thiz.cells.length) {
//                        thiz.doNextMonthNav(evt, thiz);
                    } else {
                        for (; i < thiz.cells.length; i += 7) {
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_SELECTABLE)) {
                                newTarget = thiz.cells[i].getElementsByTagName("a")[0];
                                break;
                            }
                            if (Dom.hasClass(thiz.cells[i], thiz.Style.CSS_CELL_OOM)) {
//                                thiz.doNextMonthNav(evt, thiz);
                                break;
                            }
                        }
                    }
                }
                break;
            case keys.PAGE_UP:
                thiz.doPreviousMonthNav(evt, thiz);
                break;
            case keys.PAGE_DOWN:
                thiz.doNextMonthNav(evt, thiz);
                break;
            case keys.HOME:
                newTarget = Dom.getElementsByClassName(thiz.Style.CSS_CELL_SELECTOR, "a", thiz.id)[0];
                break;
            case keys.END:
                newTarget = Dom.getElementsByClassName(thiz.Style.CSS_CELL_SELECTOR, "a", thiz.id).pop();
                break;
        }
        if (newTarget) {
            Dom.setAttribute(target, "tabindex", "-1");
            Dom.setAttribute(newTarget, "tabindex", tabIndex);
            newTarget.focus();
        }
        Event.stopEvent(evt);
    };
    var kl1 = new KeyListener(thiz.oDomContainer,
    {keys:[keys.SPACE,keys.LEFT,keys.RIGHT,keys.UP,keys.DOWN,keys.HOME,keys.END]},
//    {keys:[keys.SPACE,keys.LEFT,keys.RIGHT,keys.UP,keys.DOWN,keys.PAGE_UP,keys.PAGE_DOWN,keys.HOME,keys.END]},
    {fn:kl1Handler, correctScope:thiz});
    kl1.enable();
	});
},
init: function(params) {

		var thisYUI = ice.yui3.getNewInstance();
		var thiz = this;
		thisYUI.use('yui2-yahoo', 'yui2-yahoo-dom-event', 'yui2-dom', 'yui2-event', 'yui2-element', 'yui2-json', 'yui2-container', 'yui2-selector', 'yui2-datasource', 'yui2-calendar', 'yui2-button', function(Yui) {
			var YAHOO = Yui.YUI2;
			
	if (!ice.component.calendar.isReady) {
		ice.component.calendar.setupLib();
		ice.component.calendar.isReady = true;
	}

    var Element = YAHOO.util.Element,
        Event = YAHOO.util.Event,
        Dom = YAHOO.util.Dom,
        KeyListener = YAHOO.util.KeyListener;

    var rootId = params.clientId, rootEl = new Element(rootId); // server-side rendering root
    var calRootId = rootId + "_calRoot", calRootEl;             // client-side rendering root
    var calContainerId = rootId + "_cal", calContainerEl;       // calendar container
    var calRootHtmlEl = Dom.getLastChildBy(Dom.get(rootId), function(el){return el.id == calRootId;});
    if (calRootHtmlEl) {
        calRootHtmlEl.innerHTML = "";
        calRootEl = new Element(calRootHtmlEl);
    } else {
        calRootEl = new Element(document.createElement("div"), {id:calRootId, className:"ice-calcontainer"});
        calRootEl.appendTo(rootEl);
        var clearDiv = new Element(document.createElement("div"));
        Dom.setAttribute(clearDiv, "style", "clear: left;");
        clearDiv.appendTo(rootEl);
    }
    // hidden field to store date value string
    var calValueId = rootId + "_value";
    var calValueEl = new Element(document.createElement("input"), {type:"hidden", id:calValueId, name:calValueId, value:params.hiddenValue});
    calValueEl.appendTo(calRootEl);
    thiz[rootId].calValueEl = calValueEl;
    var calendar; // IceCalendar object
    if (!params.renderAsPopup) { // inline calendar
        calContainerEl = new Element(document.createElement("div"), {id:calContainerId});
        calContainerEl.appendTo(calRootEl);
        function inlineDateSelectHandler(type, args, calendar) {
//            console.log("calendar.currentFocus =", calendar.currentFocus);
            ice.setFocus(calendar.currentFocus);
            Dom.getFirstChild(calendar.currentFocus).focus();
            var time = ice.component.calendar.getTime(calendar);
            var dateStr = args[0][0][0] + "-" + args[0][0][1] + "-" + args[0][0][2] + " " + time.hr + ":" + time.min;
            calValueEl.set("value", dateStr, true);
            var context = ice.component.getJSContext(params.clientId);
            var sJSFProps = context.getJSFProps();
            if (sJSFProps.singleSubmit) {
                ice.se(null, rootId);
            }
        }
        calendar = new ice.component.calendar.IceCalendar(calContainerId, { // Art: non-structural change, 'calContainerEl' was before 'calContainerId'
            pagedate:params.pageDate,
            selected:params.selectedDate,
            hide_blank_weeks:true,
			iframe:false // Art: non-structural change, this option wasn't present
//            navigator:true
        }, params);
        thiz.configCal(calendar, params);
        calendar.selectEvent.subscribe(inlineDateSelectHandler, calendar, true);
        calendar.render();
        if (Dom.isAncestor(rootId, params.currentFocus)) {
            Dom.getFirstChild(params.currentFocus).focus();
        }
        if (params.tabIndex && !params.ariaEnabled) {
            calContainerEl.set("tabIndex", params.tabIndex, true);
        }
        return;
    }
    var inputId = rootId + "_input";
    var inputEl = new Element(document.createElement("input"), {type:"text", value:params.dateStr, className:"text-input", id:inputId, name:inputId});
    var toggleBtnEl = new Element(document.createElement("input"), {type:"button", className:"toggle-popup open-popup"});
    if (params.disabled) {
        inputEl.setAttributes({disabled:"disabled", "aria-disabled":true}, true);
        toggleBtnEl.setAttributes({disabled:"disabled", "aria-disabled":true}, true);
    }
    if (params.tabIndex) {
        inputEl.set("tabIndex", params.tabIndex, true);
        toggleBtnEl.set("tabIndex", params.tabIndex, true);
    }
    var inputChange = function(evt) {
        calValueEl.setAttributes({value:this.get("value")}, true);
        var context = ice.component.getJSContext(params.clientId);
        var sJSFProps = context.getJSFProps();
        if (sJSFProps.singleSubmit) {
            ice.se(evt, rootId);
        }
    };
    if (params.renderInputField) {
        inputEl.appendTo(calRootEl);
        inputEl.addListener("change", inputChange);
    }
    toggleBtnEl.appendTo(calRootEl);

    var cancelClick = function() {
        this.hide();
        toggleBtnEl.replaceClass("close-popup", "open-popup");
    };
    var okClick = function(evt, dialog) {
        this.hide();
        toggleBtnEl.replaceClass("close-popup", "open-popup");
        var context = ice.component.getJSContext(params.clientId);
        var calendar = context.getComponent();
        if (calendar.getSelectedDates().length <= 0) return;
        var date = calendar.getSelectedDates()[0];
        var time = ice.component.calendar.getTime(calendar);
        var dateStr = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + time.hr + ":" + time.min;
        calValueEl.setAttributes({value:dateStr}, true);
        var sJSFProps = context.getJSFProps();
        var submit = sJSFProps.singleSubmit ? ice.se : (sJSFProps.renderInputField ? ice.ser : null);
        if (submit) submit(evt, rootId);
    };
    var popupDateSelectHandler = function(type, args, calendar) {
        ice.setFocus(calendar.currentFocus);
        Dom.getFirstChild(calendar.currentFocus).focus();
    }
    var dialog = new YAHOO.widget.Dialog(rootId + "_dialog", {
        visible:false,
        context:[params.renderInputField ? inputEl : toggleBtnEl, "tl", "bl"],
        buttons:[{text:"OK", handler:okClick}, {text:"Cancel", isDefault:true, handler:cancelClick}],
        draggable:false,
        close:false,
        underlay:"none",
        constraintoviewport:true
    });
    dialog.setBody("<div id='" + calContainerId + "'/>");
    dialog.render(calRootEl);
    if (params.tabIndex && !params.ariaEnabled) {
        Dom.setAttribute(dialog.id, "tabindex", params.tabIndex);
    }

    calendar = new ice.component.calendar.IceCalendar(calContainerId, {
        pagedate:params.pageDate,
        selected:params.selectedDate,
        iframe:false,
        hide_blank_weeks:true
//        navigator:true
    }, params);
    thiz.configCal(calendar, params);
    calendar.selectEvent.subscribe(popupDateSelectHandler, calendar, true);
    calendar.render();

//   thiz = this;
   var toggleClick = null;
	ice.yui3.use(function(Y) {

    var animation = ice.animation.getAnimation(params.clientId, "transition");

	if (animation) {
			animation.chain.set('node', '#'+ dialog.id+'_c');
			animation.chain.on('end', function() {
				if (animation.get('reverse')) {
					dialog.show();
					toggleBtnEl.replaceClass("open-popup", "close-popup");
				} else {
					dialog.hide();
					toggleBtnEl.replaceClass("close-popup", "open-popup");
				}
			});
	}
     toggleClick = function() {

	     thix = this;
        if (this.hasClass("open-popup")) {
				if (animation) {
					animation.chain.set('reverse', true);
					animation.chain.run();
				} else {
				dialog.show();
				this.replaceClass("open-popup", "close-popup");
			}
        } else {
			if (animation) {
				animation.chain.set('reverse', false);
				animation.chain.run();
			} else {
				dialog.hide();
				this.replaceClass("close-popup", "open-popup");
			}
        }
    };


	});

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
	});
},
initialize: function(clientId, jsProps, jsfProps, bindYUI) {
	//var YAHOO = ice.component.calendar.getUtilYUI();
	var thiz = this;
	 ice.yui3.use(function(Y){ 
	 Y.on('domready', function(){
		var thisYUI = ice.yui3.getNewInstance();
		thisYUI.use('yui2-yahoo', 'yui2-yahoo-dom-event', 'yui2-dom', 'yui2-event', 'yui2-element', 'yui2-json', 'yui2-container', 'yui2-selector', 'yui2-datasource', 'yui2-calendar', 'yui2-button', function(Yui) {
			var YAHOO = Yui.YUI2;
	var lang = YAHOO.lang;
    thiz[clientId] = thiz[clientId] || {};
    var params = lang.merge({clientId:clientId}, jsProps, jsfProps);
//    console.log("params =", lang.dump(params));
    thiz.init(params);
	});
	bindYUI(thiz[clientId].yuiComponent);
	});
	});
},
updateProperties: function(clientId, jsProps, jsfProps, events) {
	//var YAHOO = ice.component.calendar.getUtilYUI();
	var thiz = this;
		var thisYUI = ice.yui3.getNewInstance();
		thisYUI.use('yui2-yahoo', 'yui2-yahoo-dom-event', 'yui2-dom', 'yui2-event', 'yui2-element', 'yui2-json', 'yui2-container', 'yui2-selector', 'yui2-datasource', 'yui2-calendar', 'yui2-button', function(Yui) {
			var YAHOO = Yui.YUI2;
	var lang = YAHOO.lang;
	var Event = YAHOO.util.Event;
    Event.onContentReady(clientId, function(){
//    console.log("jsProps =", JSON.stringify(jsProps, null, 4));
//    console.log("jsfProps =", JSON.stringify(jsfProps, null, 4));
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
    ice.component.updateProperties(clientId, jsProps, jsfProps, events, ice.component.calendar);
    }, thiz, true);
	});	
},
getInstance: function(clientId, callback) {
    ice.component.getInstance(clientId, callback, this);
},
utilYUI: null,
getUtilYUI: function() {
    if (ice.component.calendar.utilYUI == null) {
		var thisYUI = ice.yui3.getNewInstance();
		thisYUI.use('yui2-yahoo', 'yui2-yahoo-dom-event', 'yui2-dom', 'yui2-event', 'yui2-element', 'yui2-json', 'yui2-container', 'yui2-selector', 'yui2-datasource', 'yui2-calendar', 'yui2-button', function(Yui) {
	        ice.component.calendar.utilYUI = Yui.YUI2;
		});
	}
	return ice.component.calendar.utilYUI;
}
};
//lang.augmentObject(calendarns, ns, true);
//})();
