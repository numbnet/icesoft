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

    function globalEval(src) {
        if (window.execScript) {
            window.execScript(src);
        } else {
            (function() {
                window.eval.call(window, src);
            })();
        }
    }

    function extractScriptContent(html) {
        var start = new RegExp('\<script[^\<]*\>', 'g').exec(html);
        var end = new RegExp('\<\/script\>', 'g').exec(html);
        if (start && end && start.index > -1 && end.index > -1) {
            var tagWithContent = html.substring(start.index, end.index + end[0].length);
            return tagWithContent.substring(tagWithContent.indexOf('>') + 1, tagWithContent.lastIndexOf('<'));
        } else {
            return '';
        }
    }

    function extractSrcAttribute(html) {
        var result = html.match(/src="([\S]*?)"/im);
        return result ? result[1] : null;
    }

    function unescapeHtml(text) {
        if (text) {
            var temp = document.createElement("div");
            temp.innerHTML = text;
            var result = temp.firstChild.data;
            temp.removeChild(temp.firstChild);
            return result;
        } else {
            return text;
        }
    }

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

        evaluateScripts: function(scriptTags) {
            $enumerate(scriptTags).each(function(script) {
                this.evaluateScript(script);
            }.bind(this));
        },

        evaluateScript: function(script) {
            var uri = extractSrcAttribute(script);
            if (uri) {
                uri = unescapeHtml(uri);
                if (!this.referencedScripts.include(uri)) {
                    this.logger.debug('loading : ' + uri);
                    this.client.getSynchronously(uri, '', function(request) {
                        request.on(Ice.Connection.OK, function() {
                            this.referencedScripts.push(uri);
                            this.logger.debug('evaluating script at : ' + uri);
                            try {
                                globalEval(request.content());
                            } catch (e) {
                                this.logger.warn('Failed to evaluate script located at: ' + uri, e);
                            }
                        }.bind(this));
                    }.bind(this));
                }
            } else {
                var code = extractScriptContent(script);
                this.logger.debug('evaluating script : ' + code);
                try {
                    globalEval(code);
                } catch (e) {
                    this.logger.warn('Failed to evaluate script: \n' + code, e);
                }
            }
        }
    });
});