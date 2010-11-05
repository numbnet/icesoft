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

Ice.DnD = Class.create();
var IceLoaded = false;
Ice.DnD.logger = Class.create();
Ice.DnD.logger = {
    debug:function() {
    },
    warn:function() {
    },
    error:function() {
    }
};

Ice.DnD = {
    log: function(s) {
        Ice.DnD.logger.debug(s);
    },
    DRAG_START:1,
    DRAG_CANCEL:2,
    DROPPED:3,
    HOVER_START:4,
    HOVER_END:5,

    init:function() {
        Ice.DnD.logger = window.logger.child('dragDrop');
        Ice.StateMon.logger = window.logger.child('stateMon');
        Ice.StateMon.destroyAll();
        IceLoaded = true;
    },

    elementReplaced:function(ele) {
        var currentEle = $(ele.id);
        return currentEle != null && currentEle != ele;
    },

    check:function () {
        Ice.StateMon.checkAll();
    },

    alreadyDrag:function(ele) {
        ele = $(ele)
        var found = false;
        $A(Draggables.drags).each(function(drag) {
            if (drag.element && drag.element.id == ele.id) {
                found = true;
            }
        });
        return found;
    },

    sortableDraggable:function(ele) {
        ele = $(ele)
        var found = false;

        $A(Draggables.drags).each(function(drag) {
            if (drag.element && drag.element.id == ele.id) {
                if (drag.options.sort) {
                    found = true;
                }
            }
        });
        return found;
    },

    alreadyDrop:function(ele) {
        ele = $(ele)
        var found = false;
        $(Droppables.drops).each(function(drop) {
            if (drop && drop.element.id == ele.id) {

                found = true;
            }
        });
        return found;
    },

    alreadySort:function(ele) {
        var opts = Sortable.options(ele);
        if (opts)return true;
        return false;
    }
};

Ice.PanelCollapsible = {
    fire:function(eleId, hdrEleId, styleCls, usrdfndCls) {
        var ele = document.getElementById(eleId);
        var hdrEle = document.getElementById(hdrEleId);
        try {
            if (Element.visible(ele)) {
                hdrEle.className = Ice.PanelCollapsible.getStyleClass(styleCls, usrdfndCls, 'ColpsdHdr');
                Ice.PanelCollapsible.collapse(eleId);
            } else {
                hdrEle.className = Ice.PanelCollapsible.getStyleClass(styleCls, usrdfndCls, 'Hdr');
                Ice.PanelCollapsible.expand(eleId);
            }
        } catch(eee) {

            console.log("Error in panel collapsible [" + eee + "]");
        }

    },

    expand:function(eleId) {
        var ele = document.getElementById(eleId);
        if (!Element.visible(ele)) {
            Effect.SlideDown(eleId, {uploadCSS:true,submit:true,duration:0.5});
        }
    },

    collapse:function(eleId) {
        var ele = document.getElementById(eleId);
        if (Element.visible(ele)) {
            Effect.SlideUp(eleId, {uploadCSS:true,submit:true,duration:0.5});
        }
    },

    getStyleClass:function(styleCls, usrdfndCls, suffix) {
        var activeClass = styleCls + suffix;
        if (usrdfndCls != null) {
            activeClass += ' ' + usrdfndCls + suffix;
        }
        return activeClass;
    }
}

var singleRowSelectionExecuter;
var localEvent;
Ice.tableRowClicked = function(event, useEvent, rowid, formId, hdnFld, toggleClassNames, row) {
    var ctrlKyFld = document.getElementsByName(hdnFld+'ctrKy');
    var sftKyFld = document.getElementsByName(hdnFld+'sftKy');  
    if (ctrlKyFld.length > 0) {
        ctrlKyFld = ctrlKyFld[0];
    } else {
        ctrlKyFld = null;
    }
    
    if (sftKyFld.length > 0) {
        sftKyFld = sftKyFld[0];
    } else {
        sftKyFld = null;
    }    
    if (ctrlKyFld && event){
        ctrlKyFld.value = event.ctrlKey || event.metaKey;
    }
    if (sftKyFld && event){
        sftKyFld.value = event.shiftKey;
    }      
    try {
        if( useEvent ) {
            var targ;
            if (!event)
                var event = window.event;
            if (!event)
                var event = localEvent;                
            if (event.target)
                targ = event.target;
            else if (event.srcElement)
                targ = event.srcElement;
            // Some versions of Safari return the text node,
            //  while other browsers return the parent tag
            if (targ.nodeType == 3)
                targ = targ.parentNode;
            while( true ) {
                var tname = targ.tagName.toLowerCase();
                if( tname == 'tr' ) {
                    break;
                }
                if( tname == 'input' ||
                    tname == 'select' ||
                    tname == 'option' ||
                    (tname == 'a' && 'iceHdnLnk' != targ.className) ||
                    tname == 'textarea')
                {
                    return;
                }
                // Search up to see if we're deep within an anchor
                if(targ.parentNode)
                    targ = targ.parentNode;
                else {
                    break;
                }
            }
        }
        //mouse clicked on the row, here we would like to set the focus id to the 
        //hidden focus link on this tr.
        
        //first look if its already installed
        var focusElement = row["iceHdnLnk"];
        //not installed yet
        if (!focusElement) {
            //look in the dom
            var anchors = row.getElementsByTagName("a");
            if (anchors.length > 0 && 
                anchors[0].className == 'iceHdnLnk') {
               //found 
               focusElement = anchors[0];
               //install on the row, so next time dom lookup will not be required.
               row["iceHdnLnk"] = focusElement;
            }
        }
        //if focusElement has found, its mean keyboard navigation is enabled set the focus id.
        if (focusElement) {
            setFocus(focusElement.id);
        }
        
        // If preStyleOnSelection=false, then toggleClassNames=='', so we
        // should leave the row styling alone
        if(toggleClassNames) {
            row.className = toggleClassNames;
            row.onmouseover = Prototype.emptyFunction;
            row.onmouseout = Prototype.emptyFunction;
        }
        var fld = document.forms[formId][hdnFld];
        fld.value = rowid;
        var nothingEvent = new Object();
        iceSubmitPartial(null, fld, nothingEvent);
        setFocus('');
    } catch(e) {
        logger.info("Error in rowSelector[" + e + "]");
    }
}

Ice.clickEvents = {};

Ice.registerClick = function(elem,hdnClkRow,hdnClkCount,rowid,formId,delay,toggleOnClick,event,useEvent,hdnFld,toggleClassNames) {
    if (!Ice.clickEvents[elem.id]) {
        Ice.clickEvents[elem.id] = new Ice.clickEvent(elem,hdnClkRow,hdnClkCount,rowid,formId,delay,toggleOnClick,event,useEvent,hdnFld,toggleClassNames);
    } 
}

Ice.registerDblClick = function(elem) {
    if (document.selection) document.selection.empty();
    if (Ice.clickEvents[elem.id]) {
        Ice.clickEvents[elem.id].submit(2);
    }
}

Ice.clickEvent = Class.create({
    initialize: function(elem,hdnClkRow,hdnClkCount,rowid,formId,delay,toggleOnClick,event,useEvent,hdnFld,toggleClassNames) {
        this.elem = elem;
        this.hdnClkRow = hdnClkRow;
        this.hdnClkCount = hdnClkCount;
        this.rowid = rowid;
        this.formId = formId;
        
        if (delay < 0) this.delay = 0;
        else if (delay > 1000) this.delay = 1000;
        else this.delay = delay;
        
        this.toggleOnClick = toggleOnClick;
        if (this.toggleOnClick) {
            this.event = Object.clone(event);
            this.useEvent = useEvent;
            this.hdnFld = hdnFld;
            this.toggleClassNames = toggleClassNames;
        }
        
        this.timer = setTimeout(this.submit.bind(this,1),this.delay);
    },
    submit: function(numClicks) {
        clearTimeout(this.timer);
        Ice.clickEvents[this.elem.id] = null;
        var rowField = document.forms[this.formId][this.hdnClkRow];
        rowField.value = this.rowid;
        var countField = document.forms[this.formId][this.hdnClkCount];
        countField.value = numClicks;
        if (this.toggleOnClick) {
            Ice.tableRowClicked(this.event,this.useEvent,this.rowid,this.formId,this.hdnFld,this.toggleClassNames, this.elem);
        } else {
            var nothingEvent = new Object();
            iceSubmitPartial(null, rowField, nothingEvent);
        }
    }
});

Ice.preventTextSelection = function(event) {
    if (Ice.isEventSourceInputElement(event)) {
        return true;
    } else {
        if (event.shiftKey || event.ctrlKey) {
            Ice.disableTxtSelection(document.body);
            return false;
        }
        return true;
    }
}

Ice.disableTxtSelection = function (element) {
    element.onselectstart = function () { return false; }
    element.onmousedown = function (evt) { return false; };
}

Ice.enableTxtSelection = function (element) {
    element.onselectstart = function () { return true; }
    element.onmousedown = function (evt) { return true; };    
}

Ice.txtAreaMaxLen = function(field, maxlength) {
    if (maxlength >= 0 && field.value.length > maxlength) {
        field.value = field.value.substring(0, maxlength);
    }
}

Ice.isEventSourceInputElement = function(event) {
    var evt = event || window.event;
    var evt = Event.extend(evt);
    var elem = evt.element();
    var tag = elem.tagName.toLowerCase();
    if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea') {
        return true;
    } else {
        return false;
    }
}

Ice.tabNavigator = function(event) {
     var ele = Event.element(event);
     var kc= event.keyCode;
     switch (kc) {
        case 37:
        case 38:
           var preCell = ele.up('.icePnlTb').previousSibling;
           if (preCell) {
               var lnk = preCell.down('.icePnlTbLblLnk');
               if(lnk) {
                  lnk.focus();
               }
           }
           if (ele.up('.icePnlTb')) {
               if (event.preventDefault){
                   event.preventDefault();
               } else if (event.returnValue){
                   event.returnValue = false;
               }
           }           
        break; 
        case 39:
        case 40:
           var nextCell = ele.up('.icePnlTb').nextSibling;
           if (nextCell && Element.hasClassName(nextCell, 'icePnlTb')) {
               var lnk = nextCell.down('.icePnlTbLblLnk');
               if(lnk) {
                  lnk.focus();
               }
           }
           if (ele.up('.icePnlTb')) {
               if (event.preventDefault){
                   event.preventDefault();
               } else if (event.returnValue){
                   event.returnValue = false;
               }
           }
        break;            
     
    }    
}

Ice.pnlTabOnFocus = function(ele, facet, kbs) {
    setFocus(ele.id);
    if(kbs) { 
        Event.observe(ele, 'keydown', Ice.tabNavigator); 
    }   
    if (!facet) return;
    Ice.simulateFocus(ele.parentNode, ele);
}

Ice.pnlTabOnBlur = function(ele, facet, kbs) {
    if(kbs) { 
        Event.stopObserving(ele, 'keydown', Ice.tabNavigator);
    }
    if (!facet)return;    
    setFocus('');
    Ice.simulateBlur(ele.parentNode, ele);
}

Ice.pnlClpFocus = function(anc) {
    var parent = anc.parentNode;
    Ice.simulateFocus(parent, anc);
    parent.style.padding='0px'; 
}

Ice.pnlClpBlur = function(anc) {
    var parent = anc.parentNode;
    Ice.simulateBlur(parent, anc);
    parent.style.padding='1px';   
}

Ice.simulateFocus = function(ele, anc) {
    if(!document.all) {
        anc.style.visibility='hidden';
    } 
    anc.style.borderStyle='none';
    anc.style.outlineStyle='none'; 
    anc.style.borderWidth='0px';
    anc.style.outlineWidth='0px'; 
    anc.style.margin='0px'; 
    if (ele == null) return; 
    ele['_borderStyle'] = ele.style.borderStyle;     
    ele.style.borderStyle='dotted';
    ele['_borderWidth'] = ele.style.borderWidth;   
    ele.style.borderWidth='1px 1px 1px 1px';
    ele['_borderColor'] = ele.style.borderColor;   
    ele.style.borderColor = 'black';    
}

Ice.simulateBlur = function(ele, anc) {
    if(!document.all) {    
        anc.style.visibility='visible';
    } 
    if (ele == null) return; 
    ele.style.borderStyle = ele['_borderStyle'];
    ele.style.borderWidth = ele['_borderWidth'];  
    ele.style.borderColor = ele['_borderColor'];   
}


Ice.DataExporterOpenWindow = function(clientId, path, label, popupBlockerLbl) {
    var wdo = window.open(path);

    if (!wdo || typeof(wdo)== "undefined") {
        var ele = $(clientId+'container').firstChild;
        var lbl = popupBlockerLbl == "null"? label: popupBlockerLbl ;
        ele.onclick= function() {
           window.open(path);
        };
      
        if (ele.tagName == "INPUT") {
           ele.value=lbl;
        } else {
            if (ele.firstChild.tagName == "IMG") {
               ele.firstChild.title = lbl;
            } else {
               ele.innerHTML = lbl;
            }
        }
    }
    new Effect.Highlight(clientId+'container', { startcolor: '#fda505',endcolor: '#ffffff' });
}

Ice.tblRowFocus = function(anc, singleSelection) {
    var parent = anc.parentNode.parentNode;
    Ice.simulateFocus(null, anc);
    setFocus(anc.id);
    parent.onmouseover.apply(parent, arguments);
    if (anc["keydownRegistered"] == null) {
        Element.observe(anc, "keydown", function(event) {
            event = Event.extend(event);
            var keyCode = event.keyCode;
            switch(keyCode) {
                case 0://Firefox
                case 32://IE && Safari
                    parent.onclick.apply(parent, arguments);
                    Event.stop(event);
                    return false;
                case 38://up
                    if (Element.previous(parent)) {
                        var tr = Element.previous(parent);
                        tr.firstChild.firstChild.focus();
                        if (singleSelection) {
                            localEvent = Object.clone(event); 
                            window.clearTimeout (singleRowSelectionExecuter);
                            singleRowSelectionExecuter = window.setTimeout(function() {
                                try {
                                    arguments[0] = localEvent;
                                    tr.onclick.apply(tr, arguments);
                                 } catch(ee) {}
                            },400);
                        }                        
                    }

                    Event.stop(event);                    
                    return false;
                case 40://down
                    if (Element.next(parent)) {
                        var tr = Element.next(parent);
                        tr.firstChild.firstChild.focus();
                        if (singleSelection) {
                            localEvent = Object.clone(event); 
                            window.clearTimeout (singleRowSelectionExecuter);
                            singleRowSelectionExecuter = window.setTimeout(function() {
                                try {
                                    arguments[0] = localEvent;                                
                                    tr.onclick.apply(tr, arguments);
                                 } catch(ee) {}
                            },400);
                        }
                    }
                    Event.stop(event);                    
                    return false;
                case 33: //page up 
                case 34: //page down
                case 35: //end
                case 36: //home 
                   var table = Element.up(parent, ".iceDatTbl");
                   var paginator = null;
                   if (table["paginator"] == null) {
                       if (Prototype.Browser.IE) {
                            var paginators = $(document.body).select(".iceDatPgr");
                            for(i=0; i < paginators.length; i++) {
                                if (paginators[i].name == table.id){
                                    paginator = paginators[i];
                                    table["paginator"] = paginator;
                                    break;
                                }
                            }
                        } else {
                            var paginators = document.getElementsByName(table.id);
                            if (paginators.length > 0) {
                                paginator = paginators[0];
                            }
                        }
                   }
                   if (paginator) {
                        Ice.DatPagKybrd(paginator.id, event);
                   }
                   return false;                    
            }//select
        });
        anc["keydownRegistered"] = true;
    }
    
};

Ice.tblRowBlur = function(anc) {
    var parent = anc.parentNode.parentNode;
    Ice.simulateBlur(null, anc);
    parent.onmouseout.apply(parent, arguments);  
    setFocus('');
}

Ice.tblMsOvr = function(tr) {
  //  var focusHoveredTr = tr.up("table")["focusHoveredTr"];
   // if (focusHoveredTr && tr.id != focusHoveredTr.id) {
    //    focusHoveredTr.onmouseout.apply(focusHoveredTr, arguments);
   // }
    
} 


Ice.DatPagKybrd = function(pId, event){
     event = Event.extend(event);
     var keyCode = event.keyCode;
     var button = null;
     switch(keyCode) {  
        case 33: //page up
            logger.info('PageUp');
            button = "previous";
        break;
        case 34: //page down
             button = "next";
                    logger.info('pageDown');
        break;
        case 35: //end
             button = "last";
                   logger.info('end');
        break;
        case 36: //home 
                     button = "first";
                    logger.info('Home');
        break;
     }
     
     if (button == null) return;
     var form = formOf(document.getElementById(pId));
     var query = new Ice.Parameter.Query();
     query.add(pId, button);
     if (event.target.id.contains(pId)) {
        query.add(pId+'kbd', 'true');    
     } 
     iceSubmitPartial(form, null, event, query);
     Event.stop(event); 
     
}

Prototype.Browser.Safari4 = navigator.userAgent.indexOf('4.0.4 Safari') > -1;
Prototype.Browser.Chrome = navigator.userAgent.indexOf('Chrome') > -1;

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}

Ice.registerEventListener = function(ele, mask, handler) {
    if (mask == null || mask == '') return;
    ele = $(ele);
    if (ele["listenerInstalled"]) return;
    ele["listenerInstalled"] = true;
    events = mask.split(",");
    if (handler) {
        try {
          ele["jshandler"] = eval(handler.trim());
        } catch (e) {logger.info(e);}
    }
          
    for (i=0; i<events.length; i++) {
        Event.observe(ele, events[i].trim(), function(event) {
        event = Event.extend(event);
        var proceed = true;
        if (ele["jshandler"]) {
            try {
               proceed = ele["jshandler"](event);
            } catch (e) { logger.info(e) }
        }
        if (!proceed) return;
        try {
           var form = formOf(ele);
           var query = new Ice.Parameter.Query();
           query.add(ele.id, 'submitted'); 
           iceSubmitPartial(form, null, event, query); 
        } catch (e) {logger.info(e);}                       
        });
    }
}
