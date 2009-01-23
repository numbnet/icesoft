
Ice.Calendar = {};
Ice.Calendar.listeners = {};

Ice.Calendar.addCloseListener = function(calendar,form,commandLink,hiddenField) {
    if (Ice.Calendar.listeners[calendar]) {
        return;
    } else {
        Ice.Calendar.listeners[calendar] = new Ice.Calendar.CloseListener(calendar,form,commandLink,hiddenField);
    }
};

Ice.Calendar.CloseListener = Class.create({
    initialize: function(calendar,form,commandLink,hiddenField) {
        this.calendarId = calendar;
        this.formId = form;
        this.commandLinkId = commandLink;
        this.hiddenFieldId = hiddenField;
        
        this.handler = this.closePopupOnClickOutside.bindAsEventListener(this);
        Event.observe(document, "click" , this.handler);
    },
    closePopupOnClickOutside: function(event) {
        if (this.getPopup()) {
            if (this.isMenuOption(event)) {
                return;
            }
            if (this.isWithin(this.getPopup(),event)) {
                return;
            }
            if (this.isWithin(this.getButton(),event)) {
                this.dispose();
                return;
            }
                
            this.submit(event);
            this.dispose();
        }
    },
    isWithin: function(element,event) {
        return Position.within(element, Event.pointerX(event), Event.pointerY(event));
    },
    isMenuOption: function(event) {
        var element = event.element();
        if (element.tagName.toLowerCase() == 'option') {
            return this.matchesId(element.parentNode.id);
        } else if (element.tagName.toLowerCase() == 'select') {
            return this.matchesId(element.id);
        } else {
            return false;
        }
    },
    matchesId: function(id) {
        var monthMenuId = this.calendarId + '_sm';
        var yearMenuId = this.calendarId + '_sy';
        var hourMenuId = this.calendarId + '_hr';
        var minuteMenuId = this.calendarId + '_min';
        var ampmMenuId = this.calendarId + '_amPm';
        if (id == monthMenuId || id == yearMenuId || id == hourMenuId || 
            id == minuteMenuId || id == ampmMenuId) {
            return true;
        } else {
            return false;
        }    
    },
    dispose: function() {
        Ice.Calendar.listeners[this.calendarId] = null;
        Event.stopObserving(document, "click" , this.handler);
    },
    submit: function(event) {
        document.forms[this.formId][this.commandLinkId].value=this.getButton().id;
        document.forms[this.formId][this.hiddenFieldId].value='toggle';
        iceSubmitPartial(document.forms[this.formId],this.getButton(),event);
    },
    getPopup: function() {
        return $(this.calendarId + '_ct');
    },
    getButton: function() {
        return $(this.calendarId + '_cb');
    }
});