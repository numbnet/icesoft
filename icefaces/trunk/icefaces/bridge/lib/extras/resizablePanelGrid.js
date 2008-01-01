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
Resizable = Class.create({
  initialize: function(event) {
    this.print("Resizable initialized....");
    this.source = Event.element(event);
    this.pointerLocation = parseInt(Event.pointerX(event));
    this.eventMouseMove = this.resize.bindAsEventListener(this);
    this.eventMouseUp = this.detachEvent.bindAsEventListener(this);
    this.resizeAction;
    this.source.style.position = "absolute";
    this.source.style.left= Event.pointerX(event) + "px";
    Event.observe(document, "mousemove", this.eventMouseMove);
    Event.observe(document, "mouseup", this.eventMouseUp);
    this.origionalHeight = this.source.style.height;
    this.source.style.height = (Element.getHeight(this.getTableElement().parentNode) -15) + "px";
  },

  print: function(msg) {
    logger.info(msg);
  },

  getPreviousTd: function() {
    if (this.source.parentNode.previousSibling.tagName == "TD") {
        return this.source.parentNode.previousSibling;
    } else {
        return this.source.parentNode.previousSibling.previousSibling;
    }
  },

  getTableElement: function() {
    return this.source.parentNode.parentNode.parentNode.parentNode;
  },

  getNextTd: function() {
    if (this.source.parentNode.nextSibling.tagName == "TD") {
        return this.source.parentNode.nextSibling;
    } else {
        return this.source.parentNode.nextSibling.nextSibling;
    }
  },

  resize: function(event) {
    var diff = this.getDifference(event);
    var leftTdWidth = Element.getWidth(this.getPreviousTd());
    if (this.resizeAction == "dec") {
        if ((leftTdWidth - diff) < 40) {
            this.detachEvent(event);
        }
    }
    this.source.style.left = Event.pointerX(event) + "px";
  },


  detachEvent: function(event) {
    //restore height
    this.source.style.height =  this.origionalHeight;

    var leftTdWidth = Element.getWidth(this.getPreviousTd());
    var rightTdWidth = this.getNextTd();
    var tableWidth = Element.getWidth(this.getTableElement());
    var diff = this.getDifference(event);

    if (this.resizeAction == "inc") {
        this.getPreviousTd().style.width = leftTdWidth + diff + "px";
    //    this.getTableElement().style.width = tableWidth + diff + "px";;

        this.print("Diff "+ diff);
        this.print("Td width "+ leftTdWidth + this.getPreviousTd().id);
        this.print("Table width "+ tableWidth);


    } else {
        this.getPreviousTd().style.width = leftTdWidth - diff + "px";
  //      this.getTableElement().style.width = tableWidth - diff + "px";
    }
    Event.stopObserving(document, "mousemove", this.eventMouseMove);
    Event.stopObserving(document, "mouseup", this.eventMouseUp);

    this.source.style.position = "";

  },

  getDifference: function(event) {
    var x = parseInt(Event.pointerX(event));
    if (this.pointerLocation > x) {
        this.resizeAction = "dec";
        return this.pointerLocation - x;
    } else {
        this.resizeAction = "inc";
        return x -this.pointerLocation;
    }
  }




});