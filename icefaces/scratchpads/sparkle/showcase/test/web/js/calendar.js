YAHOO.namespace('YIP');

YAHOO.YIP.Calendar = function(container) {
    YAHOO.YIP.Calendar.superclass.constructor.call(this, container);
};

YAHOO.lang.extend(YAHOO.YIP.Calendar, YAHOO.widget.Calendar, {
    render: function() {
        YAHOO.YIP.Calendar.superclass.render.call(this);
        var table = YAHOO.util.Dom.get(this.id);
        var tfoot = table.appendChild(document.createElement("tfoot"));
        var selId = this.containerId.replace(/_rd$/, "_hr");
        var hrSel = "<select onchange='hrSelectHandler(form, this, event);' id='" + selId + "' name='" + selId + "'>";
        selId = this.containerId.replace(/_rd$/, "_min");
        var minSel = "<select onchange='minSelectHandler(form, this, event);' id='" + selId + "' name='" + selId + "'>";
        var i;
        for (i = 0; i < 24; i++) {
            hrSel += "<option value='" + i + "'";
            if (i === this.hour) hrSel += " selected='selected'";
            hrSel += ">" + i + "</option>";
        }
        hrSel += "</select>";
        for (i = 0; i < 60; i++) {
            minSel += "<option value='" + i + "'";
            if (i === this.minute) minSel += " selected='selected'";
            minSel += ">" + i + "</option>";
        }
        minSel += "</select>";
        var okButton = " <button id='ok' type='button'>OK</button>";
        tfoot.innerHTML = "<tr><td colspan='7'> </td></tr><tr><td colspan='7'>" + hrSel + " <b>:</b> " + minSel + okButton + "</td></tr>";
    },
    hour: 0,
    minute: 0
});

var Calendar = {};
Calendar.init = function(container, pagedate, selectedDate, selectedHour, selectedMinute) {
    YAHOO.namespace("example.calendar");
    YAHOO.example.calendar.init = function() {
        YAHOO.example.calendar.cal1 = new YAHOO.YIP.Calendar(container);
        YAHOO.example.calendar.cal1.cfg.setProperty("pagedate", pagedate);
        YAHOO.example.calendar.cal1.cfg.setProperty("selected", selectedDate);
//        YAHOO.example.calendar.cal1.cfg.setProperty("close", true);
        YAHOO.example.calendar.cal1.hour = selectedHour;
        YAHOO.example.calendar.cal1.minute = selectedMinute;
        YAHOO.example.calendar.cal1.selectEvent.subscribe(mySelectHandler, YAHOO.example.calendar.cal1, true);
        YAHOO.example.calendar.cal1.changePageEvent.subscribe(pageChangeHandler, YAHOO.example.calendar.cal1, true);
        YAHOO.example.calendar.cal1.hide();
        YAHOO.example.calendar.cal1.render();
        YAHOO.util.Event.addListener("show1up", "click", calButtonHandler, YAHOO.example.calendar.cal1, true);
//        YAHOO.util.Event.addListener("ok", "click", okButtonHandler, YAHOO.example.calendar.cal1, true);
    };
    YAHOO.util.Event.onDOMReady(YAHOO.example.calendar.init);
};

function mySelectHandler(type, args, obj) {
/*
    var selected = args[0];
    var form = formOf(YAHOO.util.Dom.get(this.id));
    var fieldName = this.containerId.replace(/_rd$/, "_cc");
    form[fieldName].value = selected[0][1] + "/" + selected[0][2] + "/" + selected[0][0];
    form[form.id + ":_idcl"].value = this.id;
    iceSubmitPartial(form, YAHOO.util.Dom.get(this.id), undefined);
*/
}

function hrSelectHandler(form, sel, event) {
    YAHOO.example.calendar.cal1.hour = parseInt(sel.value);
//    alert(sel.value);
/*
    var fieldName = YAHOO.example.calendar.cal1.containerId.replace(/_rd$/, "_cc");
    var selected = YAHOO.example.calendar.cal1.cfg.getProperty("selected");
    form[fieldName].value = selected[0][1] + "/" + selected[0][2] + "/" + selected[0][0];
    iceSubmitPartial(form, sel, event);
*/
}

function minSelectHandler(form, sel, event) {
    YAHOO.example.calendar.cal1.minute = parseInt(sel.value);
//    alert(sel.value);
/*
    var fieldName = YAHOO.example.calendar.cal1.containerId.replace(/_rd$/, "_cc");
    var selected = YAHOO.example.calendar.cal1.cfg.getProperty("selected");
    form[fieldName].value = selected[0][1] + "/" + selected[0][2] + "/" + selected[0][0];
    iceSubmitPartial(form, sel, event);
*/
}

function calButtonHandler(ev) {
    YAHOO.util.Event.purgeElement("show1up");
    var button = YAHOO.util.Event.getTarget(ev);
    if (button.innerHTML == "Show Calendar") {
        this.show();
        button.innerHTML = "Hide Calendar";
        YAHOO.util.Event.purgeElement("ok");
        YAHOO.util.Event.addListener("ok", "click", okButtonHandler, YAHOO.example.calendar.cal1, true);
    } else {
        this.hide();
        button.innerHTML = "Show Calendar";
    }
    YAHOO.util.Event.addListener("show1up", "click", calButtonHandler, YAHOO.example.calendar.cal1, true);
}

function okButtonHandler(ev) {
//    alert("OK");
    var button = YAHOO.util.Dom.get("show1up");
    this.hide();
    button.innerHTML = "Show Calendar";
//    var form = formOf(YAHOO.util.Dom.get(this.id));
    var fieldName = this.containerId.replace(/_rd$/, "_cc");
    var selected = this.cfg.getProperty("selected");
//    form[fieldName].value = selected[0][1] + "/" + selected[0][2] + "/" + selected[0][0];
//    form[form.id + ":_idcl"].value = this.id;
//    iceSubmitPartial(form, YAHOO.util.Dom.get(this.id), undefined);
    ice.submit(ev, YAHOO.util.Dom.get(this.id)); 
//    ice.singleSubmit(ev, YAHOO.util.Dom.get(this.id));
}

function pageChangeHandler() {
//    alert("New month");
    YAHOO.util.Event.purgeElement("ok");
    YAHOO.util.Event.addListener("ok", "click", okButtonHandler, YAHOO.example.calendar.cal1, true);
}
