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

[ Ice.Script = new Object, Ice.Ajax.Client ].as(function(This, Client) {

    //todo: should this code be part of Element.replaceHtml method?    
    This.Loader = Object.subclass({
        initialize: function(logger) {
            this.logger = logger.child('script-loader');
            this.referencedScripts = [];
            //list of urls
            this.client = new Client(this.logger);
            //the scripts present on the page are already evaluated
            $enumerate(document.documentElement.getElementsByTagName('script')).each(function(script) {
                if (script.src) this.referencedScripts.push(script.src);
            }.bind(this));
        },

        searchAndEvaluateScripts: function(element) {
            $enumerate(element.getElementsByTagName('script')).each(function(s) {
                this.evaluateScript(s);
            }.bind(this));
        },

        evaluateScript: function(script) {
            var uri = script.src;
            if (uri) {
                if (!this.referencedScripts.include(script.src)) {
                    this.logger.debug('loading : ' + uri);
                    this.client.getSynchronously(uri, '', function(request) {
                        request.on(Ice.Connection.OK, function() {
                            this.referencedScripts.push(uri);
                            this.logger.debug('evaluating script at : ' + uri);
                            try {
                                eval(request.content());
                            } catch (e) {
                                this.logger.warn('Failed to evaluate script located at: ' + uri, e);
                            }
                        }.bind(this));
                    }.bind(this));
                }
            } else {
                var code = script.innerHTML;
                this.logger.debug('evaluating script : ' + code);
                try {
                    eval(code);
                } catch (e) {
                    this.logger.warn('Failed to evaluate script: \n' + code, e);
                }
            }
        }
    });
});