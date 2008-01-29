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
 var visibleTooltipList = new Array();
 
ToolTipPanelPopup = Class.create({
  initialize: function(srcComp, tooltipCompId, event, hideOn, delay, dynamic, formId) {
    this.src = srcComp;
    this.delay = delay || 500;
    this.dynamic = (dynamic == "true"); 
    this.tooltipCompId = tooltipCompId;
    this.srcCompId = srcComp.id;
    this.hideOn = hideOn;
    this.x = Event.pointerX(event);
    this.y = Event.pointerY(event);
    this.formId = formId;
    //cancel bubbling
    event.cancelBubble = true;
    //attach events

    if (this.hideOn == "mousedown") {
        this.hideEvent = this.hidePopupOnMouseClick.bindAsEventListener(this);
    } else if (this.hideOn == "mouseout") {
        this.hideEvent = this.hidePopupOnMouseOut.bindAsEventListener(this);
    } else {
        this.hideOn = "none";
    }

    this.eventMouseMove = this.updateCordinate.bindAsEventListener(this);
    this.clearTimerEvent = this.clearTimer.bindAsEventListener(this);
    Event.observe(document, "mouseout" , this.clearTimerEvent);
    Event.observe(document, this.hideOn , this.hideEvent);
    Event.observe(document, "mousemove", this.eventMouseMove);

    this.timer = setTimeout(this.showPopup.bind(this), parseInt(this.delay));
  },

  showPopup: function() {
    if (this.isTooltipVisible()) return;
    this.addToVisibleList();

    if (this.dynamic) {
    //dynamic? set status=show, populatefields, and submit
      this.submit("show");
      if (this.hideOn == "none") {
        //reset the info
        this.populateFields(true);
      }
    } else {
        //static? just set the visibility= true 
       var tooltip = this.getTooltip();
        tooltip.style.visibility = "visible";
        tooltip.style.top = (parseInt(this.y) + parseInt(8)) +"px";
        tooltip.style.left = this.x+"px";
        tooltip.style.position = "absolute" ;
        tooltip.style.display = "";
    }
    
  },

  hidePopupOnMouseOut: function(event) {
    if (!this.isTooltipVisible()) return;
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
    if(this.srcOrchildOfSrcElement(eventSrc)) {
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
    if(this.dynamic) {
    //dynamic? set status=hide, populatefiels and submit 
        this.submit("hide");
    } else {
        //static? set visibility = false;
        tooltip =  this.getTooltip();
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
        iceSubmitPartial(form,element,event);
      } catch (e) {logger.info("Form not found" + e);}
  },
  
  clearTimer:function() {
     //   $(action).innerHTML += "<br/> Clearing the event";
        Event.stopObserving(document, "mouseout", this.clearTimerEvent);
        clearTimeout(this.timer);

  },

  updateCordinate: function(event) {
    if (Event.element(event) != this.src) return;
    this.x = Event.pointerX(event);
    this.y = Event.pointerY(event);
  },

  srcOrchildOfSrcElement: function(ele) {
     var tooltip =  this.getTooltip();
     if (tooltip  == ele) return true;
     while (ele.parentNode) {
        ele = ele.parentNode;
        if (tooltip  == ele){
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
	    var iceTooltipInfo = form.getElements().find( function(element) {
	        if (element.id == "iceTooltipInfo") return element;
	    });
	    if (!iceTooltipInfo) { 
		    iceTooltipInfo = document.createElement('input');
		    iceTooltipInfo.id="iceTooltipInfo";
		    iceTooltipInfo.name="iceTooltipInfo";            
		    iceTooltipInfo.type="hidden";
	        form.appendChild(iceTooltipInfo);
	    }  else {
	 
	    }
	    if (reset) {
	       iceTooltipInfo.value = "";
	    } else {
	       iceTooltipInfo.value = "tooltip_id=" + this.tooltipCompId + 
	                     "; tooltip_src_id="+ this.src.id+ 
	                     "; tooltip_state="+ this.state +
	                     "; tooltip_x="+ this.x +
	                     "; tooltip_y="+ this.y;
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
		    for (i=0; i < visibleTooltipList.length; i++) {
		        if (visibleTooltipList[i].tooltipId != this.tooltipCompId) {
		            index = parseInt(index)+ 1;
		            newList[index] = visibleTooltipList[i];
		        }else {
		        }
		    }
		    visibleTooltipList = newList;
		} else {
		}
    },
    
    isTooltipVisible: function() {
        for (i=0; i < visibleTooltipList.length; i++) {
            if (visibleTooltipList[i].tooltipId== this.tooltipCompId && visibleTooltipList[i].srcCompId == this.srcCompId) {
                return true;
            }   
        }
        return false;
    }
});

ToolTipPanelPopupUtil = {
    removeFromVisibleList:function(comp_id) {
        var newList = new Array();
        var index = -1;
        for (i=0; i < visibleTooltipList.length; i++) {
            if (visibleTooltipList[i].tooltipId != comp_id) {
                index = parseInt(index)+ 1;
                newList[index] = visibleTooltipList[i];
            }else {
            }
        }
        visibleTooltipList = newList;  
    }
}
    
