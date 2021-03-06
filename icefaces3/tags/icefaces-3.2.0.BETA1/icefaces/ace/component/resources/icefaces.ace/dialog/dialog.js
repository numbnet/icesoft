/* 
 * Original Code Copyright Prime Technology.
 * Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
 *
 * Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
 *
 * Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
 * Contributors: ICEsoft Technologies Canada Corp. (c)
 *
 * Code Modification 2: [ADD BRIEF DESCRIPTION HERE]
 * Contributors: ______________________
 * Contributors: ______________________
 *
 */

/**
 *  Dialog Widget
 */
if (!window.ice['ace']) {
    window.ice.ace = {};
}
ice.ace.Dialog = function(id, cfg) {
    var callee = arguments.callee, prevAceDialog = callee[id], jqo;
    if (prevAceDialog) {
        jqo = prevAceDialog.jq;
        if (jqo.dialog("isOpen")) {
            jqo.dialog("close", {type: "close", synthetic: true});
        }
    }
    cfg.width = cfg.width || "auto";
    cfg.height = cfg.height || "auto";
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jq = ice.ace.jq(this.jqId);
    var _self = this, closable = this.cfg.closable;

    if (closable == false) {
        this.cfg.closeOnEscape = false;
    }

    // disable unsupported effects
    if (this.cfg.hide == 'pulsate') {
        this.cfg.hide = null;
    } else {
        var browser = ice.ace.Dialog.browser();
        if (browser == 'ie7' || browser == 'ie8') {
            var hide = this.cfg.hide;
            if (hide) {
                if (hide == 'highlight' || hide == 'bounce')
                    this.cfg.hide = null;
            }
        }
    }

    //Remove scripts to prevent duplicate widget issues
    this.jq.find("script").remove();

    //Create the dialog
    this.cfg.autoOpen = false;
    this.jq.dialog(this.cfg);

    ice.onElementRemove(id, function() {
        _self.jq.dialog('close');
    });

    //Event handlers
    this.jq.bind('dialogclose', function(event, ui) {
        _self.onHide(event, ui);
    });
    this.jq.bind('dialogopen', function(event, ui) {
        _self.onShow(event, ui);
    });
	
	this.jq.parent().find('.ui-dialog-titlebar-close').bind('click', function(event, ui) {
		_self.ajaxHide();
	});

    //Hide close icon if dialog is not closable
    if (closable == false) {
        this.jq.parent().find('.ui-dialog-titlebar-close').hide();
    }

    //Hide header if showHeader is false
    if (this.cfg.showHeader == false) {
        this.jq.parent().children('.ui-dialog-titlebar').hide();
    }

    //Relocate dialog to body if appendToBody is true
//    if(this.cfg.appendToBody) {
//        this.jq.parent().appendTo(document.body);
//    }

    //Apply focus to first input initially
    if (this.cfg.isVisible) {
        this.jq.dialog("open");
        this.focusFirstInput();
    }
    callee[id] = this;
};

ice.ace.Dialog.prototype.show = function() {
    this.jq.dialog('open');

    this.focusFirstInput();
};

ice.ace.Dialog.prototype.hide = function() {
    this.jq.dialog('close');
	this.ajaxHide();
};

/**
 * Invokes user provided callback
 */
ice.ace.Dialog.prototype.onShow = function(event, ui) {
    if (this.cfg.onShow) {
        this.cfg.onShow.call(this, event, ui);
    }
};

/**
 * Fires an ajax request to invoke a closeListener passing a CloseEvent
 */
ice.ace.Dialog.prototype.onHide = function(event, ui) {
    if (typeof event.originalEvent != 'undefined') {
        if (event.originalEvent.synthetic) return;
    } else {
        if (event.synthetic) return;
    }

    if (this.cfg.onHide) {
        this.cfg.onHide.call(this, event, ui);
    }
};

ice.ace.Dialog.prototype.ajaxHide = function() {
    if (this.cfg.behaviors) {
        var closeBehavior = this.cfg.behaviors['close'];

        if (closeBehavior) {
            ice.ace.ab(closeBehavior);
        }
    }
}

ice.ace.Dialog.prototype.focusFirstInput = function() {
    this.jq.find(':not(:submit):not(:button):input:visible:enabled:first').focus();
};

ice.ace.Dialog.browser = function() {
    if (ice.ace.jq.browser.msie)
        if (ice.ace.jq.browser.version < 8) {
            if (navigator.userAgent.indexOf("Trident/5") < 0) // detects IE9, regardless of compatibility mode
                return 'ie7';
        } else {
            if (ice.ace.jq.browser.version < 9)
                return 'ie8';
        }
    return '';
};
