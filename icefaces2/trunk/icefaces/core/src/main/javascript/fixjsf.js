/*
 * Version: MPL 1.1
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

//fix for ICE-6481, effective only when the document found in the DOM update is not valid XML
(function() {
    function globalEval(src) {
        if (window.execScript) {
            window.execScript(src);
        } else {
            (function() {
                window.eval.call(window, src);
            })();
        }
    }

    function extractTagContent(tag, html) {
        var start = new RegExp('\<' + tag + '[^\<]*\>', 'g').exec(html);
        var end = new RegExp('\<\/' + tag + '\>', 'g').exec(html);
        if (start && end && start.index && end.index) {
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
        var temp = document.createElement("div");
        temp.innerHTML = text;
        var result = temp.firstChild.data;
        temp.removeChild(temp.firstChild);
        return result;
    }

    var scriptElementMatcher = /<script[^>]*>([\S\s]*?)<\/script>/igm;
    var client = Client();
    //remember loaded script references
    document.scriptRefs = document.scriptRefs ? document.scriptRefs : [];
    onLoad(window, function() {
        var scriptElements = document.documentElement.getElementsByTagName('script');
        inject(scriptElements, document.scriptRefs, function(result, s) {
            var src = s.getAttribute('src');
            if (src) {
                append(result, src);
            }
            return result;
        });
    });

    function findViewRootUpdate(content) {
        return detect(content.getElementsByTagName('update'), function(update) {
            return update.getAttribute('id') == 'javax.faces.ViewRoot';
        });
    }

    onLoad(window, function() {
        //clear the flag, only DOM updates are supposed to update it
        document.documentElement.isHeadUpdateSuccessful = null;
    });

    namespace.onAfterUpdate(function(content) {
        var rootUpdate = findViewRootUpdate(content);

        //isHeadUpdateSuccessful property is set when a script element rendered in the head is properly evaluated
        if (rootUpdate && !document.documentElement.isHeadUpdateSuccessful) {
            var headContent = extractTagContent('head', rootUpdate.firstChild.data);
            var scriptTags = headContent.match(scriptElementMatcher);
            var scripts = collect(scriptTags, function(script) {
                var src = extractSrcAttribute(script);
                var code;
                if (src) {
                    if (contains(document.scriptRefs, unescapeHtml(src))) {
                        code = '';
                    } else {
                        getSynchronously(client, src, noop, noop, function(response) {
                            code = contentAsText(response);
                        });
                        append(document.scriptRefs, src);
                    }
                } else {
                    code = unescapeHtml(extractTagContent('script', script));
                }
                return code;
            });

            each(scripts, globalEval);
        } else {
            //clear the flag for the next update
            document.documentElement.isHeadUpdateSuccessful = null;
        }

        //fix for ICE-6916
        if (rootUpdate) {
            document.title = extractTagContent('title', rootUpdate.firstChild.data);
        }
    });

    if (!/MSIE/.test(navigator.userAgent)) {
        namespace.onBeforeUpdate(function(content) {
            var rootUpdate = findViewRootUpdate(content);
            //move configuration to the 'html' element to allow the rest of the callbacks to find it once the update is applied
            if (rootUpdate) {
                var configuration = document.body.configuration;
                if (configuration) {
                    document.documentElement.configuration = configuration;
                }
            }
        });
    }
})();