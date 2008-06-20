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
 
Ice.KeyNavigator = Class.create({
  initialize: function(componentId) {
    this.component = $(componentId);
    this.component.onkeydown = this.keydown.bindAsEventListener(this);
  },

  keydown: function(event) {
    this.srcElement = Event.element(event);
    switch(event.keyCode) {
    
        case Event.KEY_RETURN:
            this.showMenu(event);
            break;
/*    
        case Event.KEY_UP:
            this.goNorth(event);
            Event.stop(event);            
            break;

        case Event.KEY_DOWN:
            this.goSouth(event);
            Event.stop(event);            
            break;

        case Event.KEY_LEFT:
            this.goWest(event);
            Event.stop(event);            
            break;
*/
        case Event.KEY_RIGHT:
            this.goEast(event);
            Event.stop(event);
            break;
    }
  },

  goNorth: function(event) {},

  goSouth: function(event) {},

  goWest: function(event) {},

  goEast: function(event) {}

});

Ice.MenuBarKeyNavigator = Class.create(Ice.KeyNavigator, {
  initialize: function($super, componentId, displayOnClick) {
    $super(componentId);
    this.displayOnClick = displayOnClick;
    document.onclick = this.hideAll.bindAsEventListener(this);

    if (Element.hasClassName(this.component, 'iceMnuBarVrt')) {
        this.vertical = true;
    } else {
        this.vertical = false;
    }
    this.setClickState(this.component, false); 
    this.configureRootItems();
  }
});

Ice.MenuBarKeyNavigator.addMethods({
  goEast: function(event) {
    var rootItem = this.srcElement.up('.'+this.getMenuBarItemClass());
    if (rootItem) {
//        Element.removeClassName(rootItem, 'iceMnuBarItemhover');
        var nextItem = rootItem.next('.'+this.getMenuBarItemClass());
        if (nextItem) {
//            Element.addClassName(nextItem, 'iceMnuBarItemhover');
            var anch = nextItem.down('a');
            anch.focus();
        }
        return;
    }

    var submenu = this.srcElement.down('.'+ this.getSubMenuIndClass());
    if (submenu) {
 //       Element.removeClassName(submenu, 'iceMnuBarItemhover');
        var pdiv = this.srcElement.up('.iceMnuItm');
 //     Element.addClassName(pdiv, 'iceMnuBarItemhover');        
        var submnuDiv = $(pdiv.id+'_sub');
        var firstAnch = submnuDiv.down('a');
        firstAnch.focus();

    }
  },

  goWest: function(event) {
    var rootItem = this.srcElement.up('.'+this.getMenuBarItemClass());
    if (rootItem) {
        Element.removeClassName(rootItem, 'iceMnuBarItemhover');
        var previousItem = rootItem.previous('.'+this.getMenuBarItemClass());
        if (previousItem) {
            Element.addClassName(previousItem, 'iceMnuBarItemhover');
            var anch = previousItem.down('a');
            anch.focus();
        }
        return;
    }

    var submenu = this.srcElement.previous('.iceMnuItm');
    Element.removeClassName(submenu, 'iceMnuItemhover');    
    if (submenu == null) {
        var pdiv = this.srcElement.up('.'+ this.getSubMenuClass());
        if (pdiv) {
            var owner = $(pdiv.id.substring(0, pdiv.id.length-4));
            if (owner) {
                var anch = owner.down('a');
                Element.addClassName(owner, 'iceMnuItemhover');
                anch.focus();
            }
        }
    }
  },

  goSouth: function(event) {
    var rootItem = this.srcElement.up('.'+this.getMenuBarItemClass());
    if (rootItem) {
        Element.removeClassName(rootItem, 'iceMnuBarItemhover');
        var submenu = $(rootItem.id+'_sub');
        if (submenu) {
            var anch = submenu.down('a');
            Element.addClassName(submenu, 'iceMnuBarItemhover');
            anch.focus();
        }
        return;
    }
    var menuItem = this.srcElement.up('.iceMnuItm');
    Element.removeClassName(menuItem, 'iceMnuBarItemhover');    
    var nextItem = menuItem.next('.iceMnuItm');
    if (nextItem) {
        var anch = nextItem.down('a');
        Element.addClassName(nextItem, 'iceMnuBarItemhover');
        anch.focus();
    }
  },

  goNorth: function(event) {
    var menuItem = this.srcElement.up('.iceMnuItm');
    Element.removeClassName(menuItem, 'iceMnuBarItemhover');    
    var previousItem = menuItem.previous('.iceMnuItm');
    if (previousItem ) {
        var anch = previousItem.down('a');
        Element.addClassName(previousItem, 'iceMnuItemhover');
        anch.focus();
    }
  },

  getMenuBarItemClass: function(event) {
    if (this.vertical) {
        return "iceMnuBarVrtItem";
    } else {
        return "iceMnuBarItem";
    }
  },

  getSubMenuClass: function(event) {
    if (this.vertical) {
        return "iceMnuBarVrtSubMenu";
    } else {
        return "iceMnuBarSubMenu";
    }
  },

  getSubMenuIndClass: function(event) {
    if (this.vertical) {
        return "iceMnuBarVrtSubMenuInd";
    } else {
        return "iceMnuBarSubMenuInd";
    }
  },
  
  getRootClass: function() {
    if (this.vertical) {
        return "iceMnuBarVrt";
    } else {
        return "iceMnuBar";
    }  
  },

  getMenuItemClass: function() {
     return "iceMnuItm";
  },  
  
  hover: function(event) {
    if (this.isClicked(Event.element(event))) {
	    element = Event.element(event).up('.'+ this.getMenuBarItemClass()); 
	    if (!element) return;
        var submenu = $(element.id + '_sub');
	    Ice.Menu.hideOrphanedMenusNotRelatedTo(element);
	    if (this.vertical) {
	        var rootElement = element.up('.'+ this.getRootClass())
	        Ice.Menu.show(rootElement,submenu,element);
	    } else {
	        Ice.Menu.show(element,submenu,null);
	    }
    }
  },
  
  mousedown: function(event) {
    element = Event.element(event);  
    if (this.isClicked(element)) {
        this.setClickState(element, false);
    } else {
        this.setClickState(element, true); 
        this.hover(event);
    }
  },
  
  focus: function(event) {
    this.hover(event);
  },
  
  configureRootItems: function () {
     mnuBarItem = this.component.down('.'+ this.getMenuBarItemClass());
     this.registerEvents(mnuBarItem);
  },
  
  registerEvents:function (mnuBarItem) {
    if (mnuBarItem) {
        mnuBarItem.onmouseover = this.hover.bindAsEventListener(this);
        //add focus support 
        var anch = mnuBarItem.down('a');
        anch.onfocus = this.focus.bindAsEventListener(this);
        if (this.displayOnClick) { 
            mnuBarItem.onmousedown = this.mousedown.bindAsEventListener(this);
            this.setClickState(mnuBarItem, false);           
        }
        var sibling = mnuBarItem.next('.'+ this.getMenuBarItemClass());
        if (sibling) {
	        this.registerEvents(sibling);
        }
    }  
  },
  
  hideAll:function(event) {
      element = Event.element(event); 
      var menu = element.up('.'+ this.getRootClass());
      var menuItem = element.up('.'+ this.getMenuItemClass());
      if (!(menu && this.displayOnClick)) {
        Ice.Menu.hideAll();
      }
      if(menuItem && this.displayOnClick) {
        this.hideAllDisplayOnClickMenus();
        Ice.Menu.hideAll();
      }else if (!menu && this.displayOnClick) {
        this.hideAllDisplayOnClickMenus();
        
      }
   },
   
   showMenu:function(event) {
     element = Event.element(event);    
     var baritem = element.up('.'+ this.getMenuBarItemClass());
     if (baritem && this.displayOnClick) {
        this.mousedown(event);
     }
   },
   
   isClicked:function(element) {
	    if (!this.displayOnClick) {
	          return true;
	    }
        var root = element.up('.'+ this.getRootClass());
        if (!root) return true;
        var clickState = $(root.id + 'clickState');
        if (!clickState) return true;  
        return (clickState.value == "true");  
   },
   
   setClickState:function(element, state) {
        if (!this.displayOnClick){
            this.click = state;
            return;
        }
        var root = element.up('.'+ this.getRootClass());
        if (!root) return true;
        var clickState = $(root.id + 'clickState');
        if (!clickState) return true;
        if (state) {
            clickState.value="true";
        } else {
            this.hideAllDisplayOnClickMenus();
            clickState.value="false";
        }        
   },
   
   hideAllDisplayOnClickMenus: function() {
        var allDisplayOnClickMns = $$("input.mnuClickState");
        for(i=0; i <allDisplayOnClickMns.length; i++) {
	       allDisplayOnClickMns[i].value="false";
        }
   }

});