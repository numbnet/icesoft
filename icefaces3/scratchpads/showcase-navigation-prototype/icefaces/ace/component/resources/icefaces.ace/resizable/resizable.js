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

ice.ace.Resizable = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.target = ice.ace.escapeClientId(this.cfg.target);

    if(this.cfg.ajaxResize) {
        this.cfg.formId = ice.ace.jq(this.target).parents('form:first').attr('id');
    }

    var _self = this;

    this.cfg.stop = function(event, ui) {
        if(_self.cfg.onStop) {
            _self.cfg.onStop.call(_self, event, ui);
        }

        if(_self.cfg.ajaxResize) {
            _self.fireAjaxResizeEvent(event, ui);
        }
    }

    this.cfg.start = function(event, ui) {
        if(_self.cfg.onStart) {
            _self.cfg.onStart.call(_self, event, ui);
        }
    }
    
    this.cfg.resize = function(event, ui) {
        if(_self.cfg.onResize) {
            _self.cfg.onResize.call(_self, event, ui);
        }
    }

    jQuery(this.target).resizable(this.cfg);
    
}

ice.ace.Resizable.prototype.fireAjaxResizeEvent = function(event, ui) {
    var options = {
        source: this.id,
        execute: this.id,
        formId: this.cfg.formId
    };

    if(this.cfg.onResizeUpdate) {
        options.render = this.cfg.onResizeUpdate;
    }

    var params = {};
    params[this.id + '_ajaxResize'] = true;
    params[this.id + '_width'] = ui.helper.width();
    params[this.id + '_height'] = ui.helper.height();

    options.params = params;
    
    ice.ace.AjaxRequest(options);
}