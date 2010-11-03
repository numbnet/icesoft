
/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

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
        
        this.handler = this.closePopupOnClickOutside.bindAsEventListener(this);
        Event.observe(document,'mousedown',this.handler);
    },
    closePopupOnClickOutside: function(event) {
        if (this.getPopup()) {
            if (this.isInPopup(event.element())) {
                return;
            }
            if (this.isWithin(this.getPopup(),event)) {
                return;
            }
            if (event.element() == this.getButton()) {
                this.dispose();
                return;
            }
                
            var id = event.element().id;
            if (id) setFocus(id);
            else setFocus('');
            
            this.submit(event);
            this.dispose();
        }
    },
    isInPopup: function(element) {
        if (element.id == this.popupId) return true;
        if (element == undefined || element == document) return false;
        return this.isInPopup(element.parentNode);
    },
    isWithin: function(element,event) {
        return Position.within(element, Event.pointerX(event), Event.pointerY(event));
    },
    dispose: function() {
        Ice.Calendar.listeners[this.calendarId] = null;
        Event.stopObserving(document,'mousedown',this.handler);
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