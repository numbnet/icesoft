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

Ice.PanelConfirmation = Class.create({
    initialize: function(trigger,e,confirmationPanelId,autoCentre,draggable,displayAtMouse,iframeUrl,handler) {
        this.srcComp = trigger;
        this.event = e;
        this.panel = $(confirmationPanelId);
        this.url = iframeUrl;
        this.srcHandler = handler;
        
        this.isAutoCentre = autoCentre;
        this.isDraggable = draggable;
        this.isAtMouse = displayAtMouse;
        
        Ice.PanelConfirmation.current = this;
        this.showPanel();
    },
    showPanel: function() {
        Ice.modal.start(this.panel.id,this.url);
        Ice.iFrameFix.start(this.panel.id,this.url);
        this.panel.style.display = '';
        this.handleDraggableObject();
        Ice.autoPosition.stop(this.panel.id);
        if (this.isAtMouse) {
            this.panel.style.left = parseInt(Event.pointerX(this.event)) + "px";
            this.panel.style.top = parseInt(Event.pointerY(this.event)) + "px"; 
        } else {
            Ice.autoCentre.start(this.panel.id);
        }
        if (!this.isAutoCentre) {
            Ice.autoCentre.stop(this.panel.id);
        }
        this.setDefaultFocus();
    },
    accept: function() {
        this.close();
        setFocus(this.srcComp.id);
        this.srcHandler.call(this.srcComp,this.event);
    },
    cancel: function() {
        this.close();
    },
    close: function() {
        Ice.PanelConfirmation.current = null;
        this.panel.style.visibility = 'hidden';
        this.panel.style.display = 'none';
        Ice.modal.stop(this.panel.id);
        Ice.autoCentre.stop(this.panel.id);
        Draggable.removeMe(this.panel.id);
        Ice.Focus.setFocus(this.srcComp.id);
    },
    handleDraggableObject: function() {
        if (this.isDraggable) {
            Ice.DnD.adjustPosition(this.panel.id);
            new Draggable(this.panel.id,{
                handle:this.panel.id+'-handle',
                dragGhost:false,
                dragCursor:false,
                ghosting:false,
                revert:false,
                mask:'1,2,3,4,5'
            });
        }
    },
    setDefaultFocus: function() {
        var cancel = $(this.panel.id + '-cancel');
        if (cancel) {
            cancel.focus();
        } else {
            $(this.panel.id + '-accept').focus();
        }
    }
});

Ice.PanelConfirmation.current = null;