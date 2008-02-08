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
Ice.Resizable = Class.create({
  initialize: function(event, horizontal) {

    //resize handler
    this.source = Event.element(event);
    this.horizontal = horizontal;
    //initial pointer location
    if (this.horizontal) {
        this.pointerLocation = parseInt(Event.pointerY(event));
    } else {
        this.pointerLocation = parseInt(Event.pointerX(event));
    }

    this.eventMouseMove = this.resize.bindAsEventListener(this);
    this.eventMouseUp = this.detachEvent.bindAsEventListener(this);
    Event.observe(document, "mousemove", this.eventMouseMove);
    Event.observe(document, "mouseup", this.eventMouseUp);
    this.origionalHeight = this.source.style.height;
    this.disableTextSelection();
    this.getGhost().style.position = "absolute";
    this.getGhost().style.backgroundColor = "green";
    this.getGhost().style.border= "1px dashed";

    this.deadPoint = 20;
  },

  print: function(msg) {
    logger.info(msg);
  },

  getPreviousElement: function() {},

  getContainerElement: function() {},

  getNextElement: function() {},

  getGhost:function() {
    return this.source;
  },

  finalize: function (event) {
    this.source.style.position = "";
    this.source.style.left= Event.pointerX(event) + "px";
    this.source.style.backgroundColor = "#EFEFEF";
    this.source.style.border = "none";
  },

  resize: function(event) {
    this.getGhost().style.visibility="";
    if (this.deadEnd(event)) return;
    this.getGhost().style.backgroundColor = "green";
    if(this.horizontal) {
        this.getGhost().style.cursor="n-resize";
        this.getGhost().style.top = Event.pointerY(event) + "px";
    } else {
        this.getGhost().style.cursor="e-resize";
        this.getGhost().style.left = Event.pointerX(event) + "px";
    }
  },


  detachEvent: function(event) {
    //restore height
    this.source.style.height =  this.origionalHeight;
    if (this.getDifference(event) > 0 && !this.deadEnd(event)) {
        this.adjustPosition(event);
    }

    Event.stopObserving(document, "mousemove", this.eventMouseMove);
    Event.stopObserving(document, "mouseup", this.eventMouseUp);
    this.enableTextSelection();
    this.finalize(event);
  },

  adjustPosition:function(event) {
    var leftElementWidth = Element.getWidth(this.getPreviousElement());
    var rightElementWidth = Element.getWidth(this.getNextElement());
    var tableWidth = Element.getWidth(this.getContainerElement());
    var diff = this.getDifference(event);

    if (this.resizeAction == "inc") {
        this.getPreviousElement().style.width = (leftElementWidth + diff) + "px";
        this.getNextElement().style.width = (rightElementWidth - diff) + "px"

    //    this.getContainerElement().style.width = tableWidth + diff + "px";;

        this.print("Diff "+ diff);
        this.print("Td width "+ leftElementWidth + this.getPreviousElement().id);
        this.print("Table width "+ tableWidth);


    } else {
        this.getPreviousElement().style.width = (leftElementWidth - diff) + "px";
        this.getNextElement().style.width = (rightElementWidth + diff) + "px"

  //      this.getContainerElement().style.width = tableWidth - diff + "px";
    }
  },

  getDifference: function(event) {
    var x;
     if (this.horizontal) {
        x = parseInt(Event.pointerY(event));
     } else {
        x = parseInt(Event.pointerX(event));
     }
    if (this.pointerLocation > x) {
        this.resizeAction = "dec";
        return this.pointerLocation - x;
    } else {
        this.resizeAction = "inc";
        return x -this.pointerLocation;
    }
  },

  deadEnd: function(event) {
    var diff = this.getDifference(event);
    if (this.resizeAction == "dec") {
        var leftElementWidth;
        if(this.horizontal) {
            leftElementWidth = Element.getHeight(this.getPreviousElement());
        } else {
            leftElementWidth = Element.getWidth(this.getPreviousElement());
        }

        if ((leftElementWidth - diff) < this.deadPoint) {
           this.getGhost().style.backgroundColor = "red";
           return true;
        }
    } else {
        var rightElementWidth;
        if(this.horizontal) {
            rightElementWidth = Element.getHeight(this.getNextElement());
        } else {
            rightElementWidth = Element.getWidth(this.getNextElement());
        }

        if ((rightElementWidth - diff) < this.deadPoint) {
           this.getGhost().style.backgroundColor = "red";
           return true;
        }
    }
    return false;
  },

  disableTextSelection:function() {
    this.getContainerElement().onselectstart = function () { return false; }
    this.source.style.unselectable = "on";
    this.source.style.MozUserSelect = "none";
    this.source.style.KhtmlUserSelect ="none";
  },

    enableTextSelection:function() {
    this.getContainerElement().onselectstart = function () { return true; }
    this.source.style.unselectable = "";
    this.source.style.MozUserSelect = "";
    this.source.style.KhtmlUserSelect = "";
  }
});

Ice.ResizableGrid = Class.create(Ice.Resizable, {
  initialize: function($super, event) {
    $super(event);
    this.source.style.height = (Element.getHeight(this.getContainerElement())) + "px";
    this.getGhost().style.left= Event.pointerX(event) + "px";
  }
});

Ice.ResizableGrid.addMethods({
  getDifference: function($super, event) {
    return $super(event);
  },

  getContainerElement: function() {
      return this.source.parentNode.parentNode.parentNode.parentNode;
  },

  getPreviousElement: function() {
    if (this.source.parentNode.previousSibling.tagName == "TH") {
        return this.source.parentNode.previousSibling;
    } else {
        return this.source.parentNode.previousSibling.previousSibling;
    }
  },

  getNextElement: function() {
    if (this.source.parentNode.nextSibling.tagName == "TH") {
        return this.source.parentNode.nextSibling;
    } else {
        return this.source.parentNode.nextSibling.nextSibling;
    }
  }
});

Ice.PanelDivider = Class.create(Ice.Resizable, {
  initialize: function($super, event, horizontal) {
    $super(event, horizontal);
    this.deadPoint = 20;
    if (this.horizontal) {
        var spliterHeight = Element.getHeight(this.source);
        var mouseTop = Event.pointerY(event);
        this.getGhost().style.top = (mouseTop - (spliterHeight )) + "px";
        //this.getGhost().style.height = (Element.getHeight(this.getContainerElement())) + "px";
    } else {
        var spliterWidth = Element.getWidth(this.source);
        var mouseLeft = Event.pointerX(event);
        this.getGhost().style.left = (mouseLeft - (spliterWidth )) + "px";
        this.getGhost().style.height = (Element.getHeight(this.getContainerElement())) + "px";
    }
  }
});

Ice.PanelDivider.addMethods({
  getDifference: function($super, event) {
    return $super(event);
  },

  getContainerElement: function() {
      return this.source.parentNode.parentNode;
  },


  getPreviousElement: function() {
    if (this.source.previousSibling.tagName == "DIV") {
        return this.source.previousSibling;
    } else {
        return this.source.previousSibling.previousSibling;
    }
  },

  getNextElement: function() {
    if (this.source.nextSibling.tagName == "DIV") {
        return this.source.nextSibling;
    } else {
        return this.source.nextSibling.nextSibling;
    }
  },

  getGhost: function() {
    if(!this.ghost) {
        this.ghost = this.source.cloneNode(true);
        this.source.parentNode.appendChild(this.ghost);
        this.ghost.style.width = Element.getWidth(this.source) + "px";
        this.getGhost().style.visibility="hidden";
    }

    return this.ghost;
  },

  finalize: function (event) {
    Element.remove(this.ghost);
  },

  adjustPosition:function(event) {
   if (this.horizontal) {
        var leftElementHeight = (Element.getHeight(this.getPreviousElement()));
        var rightElementHeight = (Element.getHeight(this.getNextElement()));

        logger.info("leftHeight "+ leftElementHeight);
        logger.info("rightElementHeight "+ rightElementHeight);

        var tableHeight = Element.getHeight(this.getContainerElement());
        var totalHeight = (parseInt(leftElementHeight) + parseInt(rightElementHeight));
        var diff = this.getDifference(event);
        logger.info("tableHeight "+ tableHeight);
        logger.info("totalHeight "+ totalHeight);
        logger.info("diff "+ diff);
        var inPercent;
        if (this.resizeAction == "inc") {
            this.getPreviousElement().style.height = (leftElementHeight + diff)  + "px";
            this.getNextElement().style.height = (rightElementHeight - diff) + "px"
            inPercent = (leftElementHeight + diff) /tableHeight  ;
        } else {
            this.getPreviousElement().style.height = (leftElementHeight - diff) + "px";
            this.getNextElement().style.height = (rightElementHeight + diff) + "px"
            inPercent = (leftElementHeight - diff) / tableHeight ;
        }
   } else {
        var leftElementWidth = (Element.getWidth(this.getPreviousElement()));
        var rightElementWidth = (Element.getWidth(this.getNextElement()));

        var tableWidth = Element.getWidth(this.getContainerElement());
        var totalWidth = (parseInt(leftElementWidth) + parseInt(rightElementWidth));
        var diff = this.getDifference(event);
        if (this.resizeAction == "inc") {
            this.getPreviousElement().style.width = (leftElementWidth + diff)  + "px";
            this.getNextElement().style.width = (rightElementWidth - diff) + "px"
            inPercent = (leftElementWidth + diff) /totalWidth  ;
        } else {
            this.getPreviousElement().style.width = (leftElementWidth - diff) + "px";
            this.getNextElement().style.width = (rightElementWidth + diff) + "px"
            inPercent = (leftElementWidth - diff) / totalWidth ;
        }
     }
        this.submitInfo(event, inPercent);
  },
  
  submitInfo:function(event, inPercent) {
        var form = Ice.util.findForm(this.source);
        var clientId = this.getContainerElement().id;
        var firstPaneStyleElement  = $(clientId + "FirstPane");
        var secondPaneStyleElement  = $(clientId + "SecondPane");
        var inPercentElement  = $(clientId + "InPercent");        
        firstPaneStyleElement.value = this.getPreviousElement().style.cssText;
        secondPaneStyleElement.value = this.getNextElement().style.cssText; 
        inPercentElement.value = Math.round(inPercent * 100);
        iceSubmitPartial(form,this.source,event);
  }

});