
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
        
        this.popupId = this.calendarId + '_ct';
        this.buttonId = this.calendarId + '_cb'
        this.monthMenuId = this.calendarId + '_sm';
        this.yearMenuId = this.calendarId + '_sy';
        this.hourMenuId = this.calendarId + '_hr';
        this.minuteMenuId = this.calendarId + '_min';
        this.ampmMenuId = this.calendarId + '_amPm';
        
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
            if (event.element() == this.getButton()) {
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
        if (id == this.monthMenuId || id == this.yearMenuId || id == this.hourMenuId || 
            id == this.minuteMenuId || id == this.ampmMenuId) {
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
        return $(this.popupId);
    },
    getButton: function() {
        return $(this.buttonId);
    }
});