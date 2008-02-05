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
    this.keydownEvent = this.keydown.bindAsEventListener(this);
    Event.observe(this.component, "keydown", this.keydownEvent);

  },

  keydown: function(event) {
    this.srcElement = Event.element(event);
    switch(event.keyCode) {
        case Event.KEY_UP:
            this.goNorth(event);
            break;

        case Event.KEY_DOWN:
            this.goSouth(event);
            break;

        case Event.KEY_LEFT:
            this.goWest(event);
            break;

        case Event.KEY_RIGHT:
            this.goEast(event);
            break;
    }
    Event.stop(event);
  },

  goNorth: function(event) {},

  goSouth: function(event) {},

  goWest: function(event) {},

  goEast: function(event) {}

});

Ice.MenuBarKeyNavigator = Class.create(Ice.KeyNavigator, {
  initialize: function($super, event) {
    $super(event);
  }
});

Ice.MenuBarKeyNavigator.addMethods({
  goEast: function(event) {
    var rootItem = this.srcElement.up('.iceMnuBarItem');
    if (rootItem) {
        Element.removeClassName(rootItem, 'iceMnuBarItemhover');
        var nextItem = rootItem.next('.iceMnuBarItem');
        if (nextItem) {
            Element.addClassName(nextItem, 'iceMnuBarItemhover');
            var anch = nextItem.down('a');
            anch.focus();
        }
        return;
    }

    var submenu = this.srcElement.down('.iceMnuBarSubMenuInd');
    if (submenu) {
        var pdiv = this.srcElement.up('.iceMnuItm');
        var submnuDiv = $(pdiv.id+'_sub');
        var firstAnch = submnuDiv.down('a');
        Element.show(submnuDiv);
        firstAnch.focus();

    }
  },

  goWest: function(event) {
    var rootItem = this.srcElement.up('.iceMnuBarItem');
    if (rootItem) {
        Element.removeClassName(rootItem, 'iceMnuBarItemhover');
        var previousItem = rootItem.previous('.iceMnuBarItem');
        if (previousItem) {
            Element.addClassName(previousItem, 'iceMnuBarItemhover');
            var anch = previousItem.down('a');
            anch.focus();
        }
        return;
    }

    var submenu = this.srcElement.previous('.iceMnuItm');
    if (submenu == null) {
        var pdiv = this.srcElement.up('.iceMnuBarSubMenu');
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
    var rootItem = this.srcElement.up('.iceMnuBarItem');
    if (rootItem) {
        Element.removeClassName(rootItem, 'iceMnuBarItemhover');
        var submenu = $(rootItem.id+'_sub');
        if (submenu) {
            var anch = submenu.down('a');
            Element.addClassName(submenu, 'iceMnuItemhover');
            anch.focus();
        }
        return;
    }
    var menuItem = this.srcElement.up('.iceMnuItm');
    var nextItem = menuItem.next('.iceMnuItm');
    if (nextItem) {
        var anch = nextItem.down('a');
        Element.addClassName(nextItem, 'iceMnuItemhover');
        anch.focus();
    }
  },

  goNorth: function(event) {
    var menuItem = this.srcElement.up('.iceMnuItm');
    var previousItem = menuItem.previous('.iceMnuItm');
    if (previousItem ) {
        var anch = previousItem.down('a');
        Element.addClassName(previousItem, 'iceMnuItemhover');
        anch.focus();
    }
  }
});