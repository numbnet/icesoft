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
 *  Accordion Widget
 */
ice.ace.AccordionPanel = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jq = ice.ace.jq(this.jqId + '_acco');
    this.stateHolder = ice.ace.jq(this.jqId + '_active');
    var _self = this;

    //Create accordion
    this.jq.accordion(this.cfg);

    if(this.cfg.dynamic && this.cfg.cache) {
        this.markAsLoaded(this.jq.children('div').get(this.cfg.active));
    }
    
    this.jq.bind('accordionchangestart', function(event, ui) {
        _self.onTabChange(event, ui);
    });
    
    setTimeout(function() { _self.jq.accordion('resize'); }, 100); // for calculating correct heights when inside tabSet
}

/**
 * TabChange handler
 */
ice.ace.AccordionPanel.prototype.onTabChange = function(event, ui) {
    var panel = ui.newContent.get(0),
        shouldLoad = this.cfg.dynamic && !this.isLoaded(panel);

    //Write state
    this.stateHolder.val(ui.options.active);

    if(shouldLoad) {
        this.loadDynamicTab(panel);
    }
    else {
        if (this.cfg.ajaxTabChange) {
            this.fireAjaxTabChangeEvent(panel);
        }
    }
}

/**
 * Loads tab contents with ajax
 */
ice.ace.AccordionPanel.prototype.loadDynamicTab = function(panel) {
    var _self = this,
    options = {
        source: this.id,
        execute: this.id,
        render: this.id
    };

    options.onsuccess = function(responseXML) {
        ice.ace.selectCustomUpdates(responseXML, function(id, content) {
            if(id == _self.id){
			if (panel) {
				ice.ace.jq(panel).children('div').html(content);

				if(_self.cfg.cache) {
					_self.markAsLoaded(panel);
				}
			}

            }
            else {
                ice.ace.AjaxUtils.updateElement(id, content);
            }

        });

        return false;
    };

    var params = {};
    params[this.id + '_contentLoad'] = true;
    if (panel) params[this.id + '_newTab'] = panel.id;
    params[this.id + '_active'] = this.stateHolder.val();
    params['ice.customUpdate'] = this.id;

    if(this.cfg.ajaxTabChange) {
        params[this.id + '_tabChange'] = true;
    }

    options.params = params;

    ice.ace.AjaxRequest(options);
}

/**
 * Fires an ajax tabChangeEvent if a tabChangeListener is defined on server side
 */
ice.ace.AccordionPanel.prototype.fireAjaxTabChangeEvent = function(panel) {
    var formId = formOf(this.jq.get(0)).id;
    var options = {
        source: this.id,
        execute: formId
    },
    listener = this.cfg && this.cfg.behaviors && this.cfg.behaviors.panechange;

    var params = {};
    params[this.id + '_tabChange'] = true;
    if (panel) params[this.id + '_newTab'] = panel.id;
    params[this.id + '_active'] = this.stateHolder.val();

    options.params = params;

    if (ice.ace.jq.isFunction(listener)) {
        listener(options.params);
    } else ice.ace.AjaxRequest(options);
}

ice.ace.AccordionPanel.prototype.markAsLoaded = function(panel) {
    ice.ace.jq(panel).data('loaded', true);
}

ice.ace.AccordionPanel.prototype.isLoaded = function(panel) {
    return ice.ace.jq(panel).data('loaded') == true;
}

ice.ace.AccordionPanel.prototype.select = function(index) {
    this.jq.accordion('activate', index);
}

ice.ace.AccordionPanel.prototype.collapseAll = function() {
    this.jq.accordion('activate', false);
}