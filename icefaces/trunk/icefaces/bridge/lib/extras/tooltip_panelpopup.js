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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */
ToolTipPanelPopup = Class.create({
  initialize: function(srcComp, tooltipCompId, event, autoHide, delay, dynamic) {
    this.src = srcComp;
    this.delay = delay || 500;
    this.dynamic = (dynamic == "true"); ;
    this.tooltip = this.getToolTipElement(tooltipCompId);
    this.autoHide = "mouseout";
    //cancel bubbling
    event.cancelBubble = true;
    //attach events

    if (autoHide == "onClick") {
        this.autoHide = "click"
        this.hideEvent = this.hidePopupOnMouseClick.bindAsEventListener(this);
          $(action).innerHTML += "event registered as "+ this.autoHide ;

    } else if (autoHide == "onExit") {
        this.autoHide = "mouseout"
        this.hideEvent = this.hidePopupOnMouseOut.bindAsEventListener(this);
        $(action).innerHTML += "event registered as "+ this.autoHide ;
    } else {
        this.autoHide = false;
    }


    this.eventMouseMove = this.updateCordinate.bindAsEventListener(this);
    this.clearTimerEvent = this.clearTimer.bindAsEventListener(this);
    Event.observe(document, "mouseout" , this.clearTimerEvent);
    Event.observe(document, this.autoHide , this.hideEvent);
    Event.observe(document, "mousemove", this.eventMouseMove);

    this.timer = setTimeout(this.showPopup.bind(this), this.delay);
  },

  showPopup: function() {
    this.state = "show";
    if (this.dynamic) {
        $("action").innerHTML += "dynamic request <br/> set hidden field tooltip_id = tooltipCompId, tooltip_src_id <br/> set tooltip_action=show\hide, <br/> set tooltip_x, <br/> set tooltip_y <br/> partialSubmit"
    } else {
        $("action").innerHTML += "set PanelPopup's visible proprty to true"
        this.tooltip.style.visibility = "visible";
        this.tooltip.style.top = parseInt(this.y) + parseInt(8);
        this.tooltip.style.left = this.x;
    }


  },

  hidePopupOnMouseOut: function(event) {
    var eventSrc = Event.element(event);
        if(eventSrc == this.src) {
            if (this.dynamic) {
                $(action).innerHTML = "dynamic request<br/> call above Submitmethod with action=hide"
            } else {
                if (this.tooltip)
                    this.tooltip.style.visibility = "hidden";
            }
        }
    this.hidePopup(event);
  },

  hidePopupOnMouseClick: function(event) {
    var eventSrc = Event.element(event);
    if(this.srcOrchildOfSrcElement(eventSrc)) {
        return;
    } else {
        if (this.dynamic) {
            $(action).innerHTML = "dynamic request<br/> call above Submitmethod with action=hide"
        } else {
            if (this.tooltip)
                this.tooltip.style.visibility = "hidden";
        }
    }

    this.hidePopup(event);
  },


  hidePopup: function(event) {

  $(action).innerHTML += "<br/> Hide pop"+ this.autoHide;

        Event.stopObserving(document, this.autoHide, this.hideEvent);
        Event.stopObserving(document, "mousemove", this.eventMouseMove);
  },

  clearTimer:function() {
        $(action).innerHTML += "<br/> Clearing the event";
        Event.stopObserving(document, "mouseout", this.clearTimerEvent);
        clearTimeout(this.timer);

  },

  updateCordinate: function(event) {
    if (Event.element(event) != this.src) return;
    this.x = Event.pointerX(event);
    this.y = Event.pointerY(event);
  },

  srcOrchildOfSrcElement: function(ele) {
     if (this.tooltip  == ele) return true;
     while (ele.parentNode) {
        ele = ele.parentNode;
        if (this.tooltip  == ele){
            return true;
        }
     }
  },

  getToolTipElement: function (tooltipCompId) {
    if (tooltipCompId.charAt(0)==':') {

        var tooltip = $("ice_tooltip_element");
        if (!tooltip) {

            tooltip = document.createElement('div');
            tooltip.style.visibility = "hidden";
            tooltip.style.position = "absolute" ;
            tooltip.style.border = "1px solid" ;
            tooltip.style.backgroundColor = "white";
            document.body.appendChild(tooltip);
        }
            tooltip.innerHTML = tooltipCompId.substring(1);
        return tooltip;
    }
    if (this.dynamic) { // component is not rendered yet so just assign the id to the component
     $(action).innerHTML += "<br/> getTooltip called dynainc";
        return tooltipCompId;

    } else {
         $(action).innerHTML += "<br/> getTooltip called not dynainc " + tooltipCompId;
        return $(tooltipCompId);

    }
  }

});