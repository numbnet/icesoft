/* 
* Original Code developed and contributed by Prime Technology. 
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

ice.ace.NotificationBar = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jq = ice.ace.escapeClientId(this.id);

    jQuery(this.jq).css(this.cfg.position, '0');
    jQuery(this.jq).css("left", '0');

//	jQuery(this.jq).appendTo(jQuery('body'));

    if (this.cfg.visible) {
        jQuery(this.jq).css({'display':'block'});
    } else {
        jQuery(this.jq).css({'display':'none'});
    }
};

ice.ace.NotificationBar.prototype.show = function() {
    if (this.cfg.effect === "slide")
        jQuery(this.jq).slideDown(this.cfg.effect);
    else if (this.cfg.effect === "fade")
        jQuery(this.jq).fadeIn(this.cfg.effect);
    else if (this.cfg.effect === "none")
        jQuery(this.jq).show();
    this.cfg.visible = true;
    var listener = this.cfg && this.cfg.behaviors && this.cfg.behaviors.display;
    if (jQuery.isFunction(listener)) {
        listener();
    }
};

ice.ace.NotificationBar.prototype.hide = function() {
    if (this.cfg.effect === "slide")
        jQuery(this.jq).slideUp(this.cfg.effect);
    else if (this.cfg.effect === "fade")
        jQuery(this.jq).fadeOut(this.cfg.effect);
    else if (this.cfg.effect === "none")
        jQuery(this.jq).hide();
    this.cfg.visible = false;
    var listener = this.cfg && this.cfg.behaviors && this.cfg.behaviors.close;
    if (jQuery.isFunction(listener)) {
        listener();
    }
};