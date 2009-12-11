YAHOO.namespace("icefaces.calendar.calendars");

YAHOO.icefaces.calendar.init = function(params) {
    Element = YAHOO.util.Element;
    Event = YAHOO.util.Event;

    var rootDivId = params.divId;
    var rootDiv = new Element(rootDivId);
    var inputEl = new Element(document.createElement("input"), {type:"text", value:""});
    var buttonEl = new Element(document.createElement("button"), {type:"button"});
    var imageEl = new Element(document.createElement("img"), {src:"images/cal_button.gif"});
    inputEl.appendTo(rootDiv);
    buttonEl.appendTo(rootDiv);
    imageEl.appendTo(buttonEl);

    var cancelClick = function() {
        dialog.hide();
    };
    var okClick = function() {
        if (calendar.getSelectedDates().length > 0) {
            var selDate = calendar.getSelectedDates()[0];
            var dateStr = selDate.getFullYear() + "-" + (selDate.getMonth() + 1) + "-" + selDate.getDate();
            inputEl.setAttributes({value:dateStr}, true);
        }
        dialog.hide();
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
        iframe:false,
        HIDE_BLANK_WEEKS:true,
        navigator:true
    });
    calendar.render();

    var buttonClick = function() {
        dialog.show();
    };
    var domReady = function() {
        buttonEl.addListener("click", buttonClick);
    };

    Event.onDOMReady(domReady);
};