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
var visibleTooltipList = new Array();

ToolTipPanelPopup = Class.create({
    initialize: function(srcComp, tooltipCompId, event, hideOn, delay, dynamic, formId, ctxValue, iFrameUrl, displayOn, moveWithMouse) {
        //tooltip is not rendered
        if (!$(tooltipCompId)) return;
        this.src = srcComp;
        this.delay = delay || 500;
        this.dynamic = (dynamic == "true");
        this.tooltipCompId = tooltipCompId;
        this.srcCompId = srcComp.id;
        this.hideOn = hideOn;
        this.x = Event.pointerX(event);
        this.y = Event.pointerY(event);
        this.formId = formId;
        this.ctxValue = ctxValue
        this.iFrameUrl = iFrameUrl;
        this.moveWithMouse = moveWithMouse;
        this.displayOn = displayOn;
        this.event = event;
        //cancel bubbling
        event.cancelBubble = true;
        //attach events

        if (this.hideOn == "mousedown") {
            this.hideEvent = this.hidePopupOnMouseClick.bindAsEventListener(this);
        } else if (this.hideOn == "mouseout") {
            this.hideEvent = this.hidePopupOnMouseOut.bindAsEventListener(this);
        } else {
            this.hideOn = "none";
            //associate no-op callback to avoid stack overflow in IE
            this.hideEvent = Function.NOOP;
        }

        this.eventMouseMove = this.updateCordinate.bindAsEventListener(this);
        this.clearTimerEvent = this.clearTimer.bindAsEventListener(this);
        Event.observe(document, "mouseout", this.clearTimerEvent);
        Event.observe(document, this.hideOn, this.hideEvent);
        Event.observe(document, "mousemove", this.eventMouseMove);
        if (displayOn == "hover") {
            this.timer = setTimeout(this.showPopup.bind(this), parseInt(this.delay));
        } else {
            this.showPopup.bind(this)();
            //ICE-5406 : outputLink does not work inside a panelTooltip with displayOn="click"
            //Event.extend(event).stop();
        }
    },

    showPopup: function() {
        if (this.isTooltipVisible()) return;
        var tooltip = this.getTooltip();
        if (!tooltip) return;//tooltip removed by a DOM update
        if (this.dynamic) {
            //its a dynamic tooltip, so remove all its childres
            if (tooltip) {
                tooltip.style.visibility = "hidden";
                var table = tooltip.childNodes[0];
                if (table) {
                    tooltip.removeChild(table);
                }
            }
            //dynamic? set status=show, populatefields, and submit
            this.submit("show");
            if (this.hideOn == "none") {
                //reset the info
                this.populateFields(true);
            }
        } else {
            //static? just set the visibility= true
            tooltip.style.visibility = "visible";
            tooltip.style.position = "absolute";
            tooltip.style.display = "";
            var srcComp = $(this.srcCompId);
            var cumulativeOffset = srcComp.cumulativeOffset();
            var positionedOffset = srcComp.positionedOffset();
            tooltip.style.top = this.y - tooltip.offsetHeight - 4 - cumulativeOffset.top + positionedOffset.top + "px";
            tooltip.style.left = this.x + 4 - cumulativeOffset.left + positionedOffset.left + "px";
            ToolTipPanelPopupUtil.adjustPosition(tooltip, this.x, this.y);
            Ice.iFrameFix.start(this.tooltipCompId, this.iFrameUrl);
        }
        this.addToVisibleList();
        //prohibits to open browser's context menu, as 'altclick' uses onmenucontext handler
        if (this.event && this.displayOn == 'altclick') {
            Event.extend(this.event).stop();
        }
    },

    hidePopupOnMouseOut: function(event) {
        if (!this.isTooltipVisible()) return;
        var x = Event.pointerX(event);
        var y = Event.pointerY(event);
        var tooltipCompElement = $(this.tooltipCompId);
        if (!tooltipCompElement) return;//return if element is no more present
        if (Position.within(tooltipCompElement, x, y)) return; //ICE-3521
        if (Position.within(this.src, x, y)) return; //ICE-6285
        this.hidePopup(event);
        this.state = "hide";
        this.populateFields();
        if (this.hideOn == "mouseout") {
            this.removedFromVisibleList();
        }
        this.dispose(event);
    },

    hidePopupOnMouseClick: function(event) {
        if (!this.isTooltipVisible() || !Event.isLeftClick(event)) return;
        var eventSrc = Event.element(event);
        if (this.srcOrchildOfSrcElement(eventSrc)) {
            return;
        } else {
            this.hidePopup(event);
        }
        if (this.hideOn == "mousedown") {
            this.removedFromVisibleList();
        }
        this.dispose(event);
    },


    dispose: function(event) {
        Event.stopObserving(document, this.hideOn, this.hideEvent);
        Event.stopObserving(document, "mousemove", this.eventMouseMove);

    },

    hidePopup:function(event) {
        if (this.dynamic) {
            //dynamic? set status=hide, populatefiels and submit
            this.submit("hide");
        } else {
            //static? set visibility = false;
            tooltip = this.getTooltip();
            if (!tooltip) return;
            tooltip.style.visibility = "hidden";
            tooltip.style.display = "none";
        }
    },


    submit:function(state, event) {
        if (!event) event = new Object();
        this.state = state;
        this.populateFields();
        var element = $(this.srcCompId);
        try {
            var form = Ice.util.findForm(element);
            iceSubmitPartial(form, element, event);
        } catch (e) {
            logger.info("Form not found" + e);
        }
    },

    clearTimer:function() {
        //   $(action).innerHTML += "<br/> Clearing the event";
        Event.stopObserving(document, "mouseout", this.clearTimerEvent);
        clearTimeout(this.timer);

    },

    updateCordinate: function(event) {
        if (Event.element(event) != this.src && !Element.descendantOf(event.element(), this.src)) return;
        this.x = Event.pointerX(event);
        this.y = Event.pointerY(event);
        if (!this.isTooltipVisible() || !this.moveWithMouse) return;
        var tooltip = this.getTooltip();
        tooltip.style.top = this.y - tooltip.offsetHeight - 4 + "px";
        tooltip.style.left = this.x + 4 + "px";
        ToolTipPanelPopupUtil.adjustPosition(tooltip, this.x, this.y);
        Ice.iFrameFix.start(this.tooltipCompId, this.iFrameUrl);
    },

    srcOrchildOfSrcElement: function(ele) {
        var tooltip = this.getTooltip();
        if (tooltip == ele) return true;
        while (ele.parentNode) {
            ele = ele.parentNode;
            if (tooltip == ele) {
                return true;
            }
        }
    },

    getTooltip: function () {
        return $(this.tooltipCompId);
    },

    populateFields: function(reset) {
        // the following field should be rendered by the panelPoupRenderer if rendered as tooltip


        var form = $(this.formId);
        if (form == null) return;
        var iceTooltipInfo = form.getElements().find(function(element) {
            if (element.id == "iceTooltipInfo") return element;
        });
        if (!iceTooltipInfo) {
            iceTooltipInfo = document.createElement('input');
            iceTooltipInfo.id = "iceTooltipInfo";
            iceTooltipInfo.name = "iceTooltipInfo";
            iceTooltipInfo.type = "hidden";
            form.appendChild(iceTooltipInfo);
        } else {

        }
        if (reset) {
            iceTooltipInfo.value = "";
        } else {
            iceTooltipInfo.value = "tooltip_id=" + this.tooltipCompId +
                "; tooltip_src_id=" + this.src.id +
                "; tooltip_state=" + this.state +
                "; tooltip_x=" + this.x +
                "; tooltip_y=" + this.y +
                "; cntxValue=" + this.ctxValue;
        }
    },

    addToVisibleList: function() {
        if (!this.isTooltipVisible()) {
            this.removedFromVisibleList('all');
            visibleTooltipList[parseInt(visibleTooltipList.length)] = {tooltipId: this.tooltipCompId, srcCompId: this.srcCompId};
        } else {
        }
    },

    removedFromVisibleList: function(all) {
        if (this.isTooltipVisible() || all) {
            var newList = new Array();
            var index = -1;
            for (i = 0; i < visibleTooltipList.length; i++) {
                if (visibleTooltipList[i].tooltipId != this.tooltipCompId) {
                    index = parseInt(index) + 1;
                    newList[index] = visibleTooltipList[i];
                } else {
                }
            }
            visibleTooltipList = newList;
        } else {
        }
    },

    isTooltipVisible: function(onlyTooltip) {
        for (i = 0; i < visibleTooltipList.length; i++) {
            if (onlyTooltip) {
                if (visibleTooltipList[i].tooltipId == this.tooltipCompId) {
                    return true;
                }
            } else {
                if (visibleTooltipList[i].tooltipId == this.tooltipCompId && visibleTooltipList[i].srcCompId == this.srcCompId) {
                    return true;
                }
            }

        }
        return false;
    }
});

ToolTipPanelPopupUtil = {
    removeFromVisibleList:function(comp_id) {
        var newList = new Array();
        var index = -1;
        for (i = 0; i < visibleTooltipList.length; i++) {
            if (visibleTooltipList[i].tooltipId != comp_id) {
                index = parseInt(index) + 1;
                newList[index] = visibleTooltipList[i];
            } else {
            }
        }
        visibleTooltipList = newList;
    },
    adjustPosition: function(id, x, y) {
        var element = $(id);
        var viewportDimensions = document.viewport.getDimensions();
        var viewportScrollOffsets = document.viewport.getScrollOffsets();
        var elementDimensions = element.getDimensions();
        var elementOffsets = element.cumulativeOffset();
        var tooltipOffset = 4;

        //verify if the right edge of the tooltip is outside of the viewport
        if (x + elementDimensions.width + tooltipOffset > viewportScrollOffsets.left + viewportDimensions.width) {
            //align tooltip's right edge with the right edge of the view port
            element.style.left = (viewportScrollOffsets.left + viewportDimensions.width - elementDimensions.width) + 'px';
        } else {
            //just offset the tooltip with 4px
            element.style.left = (x + tooltipOffset) + 'px';
        }

        //verify if the top edge of the tooltip is outside of the viewport
        if (y - elementDimensions.height - tooltipOffset < viewportScrollOffsets.top) {
            //align tooltip's top edge with the top edge of the view port
            element.style.top = (viewportScrollOffsets.top) + 'px';
        } else {
            //just offset the tooltip with 4px plus the height of the tooltip
            element.style.top = (y - (elementDimensions.height + tooltipOffset)) + 'px';
        }
    },
    showPopup: function(id, x, y) {
        ToolTipPanelPopupUtil.adjustPosition(id, x, y);
    }
};
    
