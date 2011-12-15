/* 
* Original Code developed and contributed by Prime Technology. 
* Subsequent Code Modifications Copyright 2011 ICEsoft Technologies Canada Corp. (c) 
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

ice.ace.Draggable = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
	
    jQuery(ice.ace.escapeClientId(this.cfg.target)).draggable(this.cfg);
}

ice.ace.Droppable = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
	
    this.setupDropHandler();
	
    jQuery(ice.ace.escapeClientId(this.cfg.target)).droppable(this.cfg);
}

ice.ace.Droppable.prototype.setupDropHandler = function() {
    this.cfg.formId = ice.ace.jq(ice.ace.escapeClientId(this.id)).parents('form:first').attr('id');

    var _self = this;
    
    this.cfg.drop = function(event, ui) {
        var dropListener = _self.cfg && _self.cfg.behaviors && _self.cfg.behaviors.drop;
        if(_self.cfg.onDrop) {
            _self.cfg.onDrop.call(this, event, ui);
        }

        var options = {
            source: _self.id,
            execute: _self.id,
            formId: _self.cfg.formId
        };

        if(_self.cfg.onDropUpdate) {
            options.render = _self.cfg.onDropUpdate;
        }
	
        var params = {};
        params[_self.id + "_dragId"] = ui.draggable.attr('id');
        params[_self.id + "_dropId"] = _self.cfg.target;

        options.params = params;

        if (jQuery.isFunction(dropListener)) {
            dropListener(options);
        } else ice.ace.AjaxRequest(options);
    };
}