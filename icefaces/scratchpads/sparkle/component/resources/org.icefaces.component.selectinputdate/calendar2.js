YAHOO.namespace("icefaces.calendar.calendars");

YAHOO.icefaces.calendar.init = function(params) {
    Element = YAHOO.util.Element;
    Event = YAHOO.util.Event;
    Dom = YAHOO.util.Dom;

    var rootDivId = params.divId;
    var rootDiv = new Element(rootDivId);
    var inputEl = new Element(document.createElement("input"), {type:"text", value:params.dateStr});
    var buttonEl = new Element(document.createElement("button"), {type:"button"});
    var imageEl = new Element(document.createElement("img"), {src:"images/cal_button.gif"});
    inputEl.appendTo(rootDiv);
    buttonEl.appendTo(rootDiv);
    imageEl.appendTo(buttonEl);

    var cancelClick = function() {
        this.hide();
    };
    var okClick = function(evt, dialog) {
//        for(var i=0; i<arguments.length; i++) {
//            alert(arguments[i]);
//        }
        this.hide();
        if (calendar.getSelectedDates().length > 0) {
            var selDate = calendar.getSelectedDates()[0];
            var dateStr = selDate.getFullYear() + "-" + (selDate.getMonth() + 1) + "-" + selDate.getDate() +
                    " " + params.selectedHour + ":" + params.selectedMinute;
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
    dialog.setHeader("Date of Birth");
    dialog.setBody("<div id='" + rootDivId + "_cal'/>");
    dialog.render(rootDiv);

    var calendar = new YAHOO.widget.Calendar(rootDivId + "_cal", {
        pagedate:params.pageDate,
        selected:params.selectedDate,
        iframe:false,
        HIDE_BLANK_WEEKS:true,
        navigator:true
    });
    calendar.render();

    var buttonClick = function() {
        dialog.show();
    };
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
    };

    Event.onDOMReady(domReady);
};