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
  initialize: function(srcComp, tooltipCompId, event, autoHide, delay, dynamic, formId) {
   logger.info ("1- ToolTip initilized");
    this.src = srcComp;
    this.delay = delay || 500;
    this.dynamic = (dynamic == "true"); 
    this.tooltipCompId = tooltipCompId;
    this.srcCompId = srcComp.id;
    this.autoHide = "mouseout";
    this.x = Event.pointerX(event);
    this.y = Event.pointerY(event);
    this.formId = formId;
    logger.info('event found'+ event + 'Evene x' +Event.pointerX(event));

    //cancel bubbling
    event.cancelBubble = true;
    //attach events

    if (autoHide == "onClick") {
        this.autoHide = "mousedown"
        this.hideEvent = this.hidePopupOnMouseClick.bindAsEventListener(this);
      logger.info ("event registered as "+ this.autoHide ) ;

    } else if (autoHide == "onExit") {
        this.autoHide = "mouseout"
        this.hideEvent = this.hidePopupOnMouseOut.bindAsEventListener(this);
     logger.info ("event registered as "+ this.autoHide) ;
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
    logger.info("2- Show popup");
    if (this.isTooltipVisible()) return;
    this.addToVisibleList();

    if (this.dynamic) {
    //dynamic? set status=show, populatefields, and submit
      this.submit("show");
    } else {
        //static? just set the visibility= true 
       var tooltip = this.getTooltip();
       if (this.tooltipCompId.charAt(0)==':') {
            //make visible true, so the page won't sbmuit
             tooltip.innerHTML = this.tooltipCompId.substring(1);
        }
        tooltip.style.visibility = "visible";
        tooltip.style.top = (parseInt(this.y) + parseInt(8)) +"px";
        tooltip.style.left = this.x+"px";
        tooltip.style.position = "absolute" ;
        tooltip.style.display = "";
    }
    
  },

  hidePopupOnMouseOut: function(event) {
    if (!this.isTooltipVisible()) return;
    logger.info("3- Hide popup mouseout");
    this.hidePopup(event);
    this.state = "hide";
    this.populateFields();
    if (this.autoHide == "mouseout") {
        this.removedFromVisibleList();
    }
    this.dispose(event);
  },

  hidePopupOnMouseClick: function(event) {
    if (!this.isTooltipVisible() || !Event.isLeftClick(event)) return;
   
    logger.info("3- Hide popup mouseclick" + this.tooltipCompId);
    var eventSrc = Event.element(event);
    if(this.srcOrchildOfSrcElement(eventSrc)) {
       logger.info( "can not hide, its a src child");
        return;
    } else {
        this.hidePopup(event);
    }
    if (this.autoHide == "mousedown") {
        logger.info("TOTAL LENGTH : " + visibleTooltipList.length);
        this.removedFromVisibleList();
        logger.info("TOTAL LENGTH After Remove: " + visibleTooltipList.length);
    }
    this.dispose(event);
  },


 dispose: function(event) {
    logger.info( "Hide pop called NEW LIST INDEX "+ visibleTooltipList.length);
    Event.stopObserving(document, this.autoHide, this.hideEvent);
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
      logger.info("Submitting the Form");
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
     logger.info( "updating cordinates"+ this.autoHide);
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
    if (this.tooltipCompId.charAt(0)==':') {
        var tooltip = $("ice_tooltip_element");
        if (!tooltip) {
            tooltip = document.createElement('div');
            tooltip.id = "ice_tooltip_element";
            tooltip.style.visibility = "hidden";
            tooltip.style.position = "absolute" ;
            tooltip.style.border = "1px solid" ;
            tooltip.style.backgroundColor = "white";
            
            document.body.appendChild(tooltip);
        }
        return tooltip;
    }
      return $(this.tooltipCompId);
  },
  
  populateFields: function() {
  // the following field should be rendered by the panelPoupRenderer if rendered as tooltip

    var form = $(this.formId);
    if (form == null) return;
    var iceTooltipInfo = form.getElements().find( function(element) {
        if (element.id == "iceTooltipInfo") return element;
    });
    if (!iceTooltipInfo) { 
        logger.info("TOOLTIP Element not found creating one");
	    iceTooltipInfo = document.createElement('input');
	    iceTooltipInfo.id="iceTooltipInfo";
	    iceTooltipInfo.name="iceTooltipInfo";            
	    iceTooltipInfo.type="hidden";
        form.appendChild(iceTooltipInfo);
    }  else {
            logger.info("TOOLTIP Element found REUSING IT");
    }
    iceTooltipInfo.value = "tooltip_id=" + this.tooltipCompId + 
                     "; tooltip_src_id="+ this.src.id+ 
                     "; tooltip_state="+ this.state +
                     "; tooltip_x="+ this.x +
                     "; tooltip_y="+ this.y;
     logger.info("Fields are populated :: "+ iceTooltipInfo.value);  
    },
    
    addToVisibleList: function() {
        if (!this.isTooltipVisible()) {
            logger.info("ADD: NOT in the visible list ADDINGGGGGG");
            visibleTooltipList[parseInt(visibleTooltipList.length)] = this.tooltipCompId;
        } else {
            logger.info("ADD: Already found in the visible list NOT ADDINGGGGGG");
        }
    },
    
    removedFromVisibleList: function() {
     logger.info("REMOVE: TRYING TO REMOVING");
        if (this.isTooltipVisible()) {
             logger.info("REMOVE: FOUND IN REMOVE LIST");
	        var newList = new Array();
		    var index = -1;
		    for (i=0; i < visibleTooltipList.length; i++) {
		        logger.info ("REMOVELIST : "+ visibleTooltipList[i]);
		        if (visibleTooltipList[i] != this.tooltipCompId) {
		            index = parseInt(index)+ 1;
		            newList[index] = visibleTooltipList[i];
		        }else {
		         logger.info("REMOVE: FOUND in the visible list REMOVING");
		        }
		    }
		    visibleTooltipList = newList;
		} else {
		logger.info("REMOVE: NOT FOUND in the visible list CANT REMOVE");
		}
    },
    
    isTooltipVisible: function() {
        for (i=0; i < visibleTooltipList.length; i++) {
            if (visibleTooltipList[i]== this.tooltipCompId) {
                return true;
            }   
        }
        return false;
    }
    
});