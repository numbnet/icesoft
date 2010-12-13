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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

//fckeditor needs Ice namespace
window.Ice = {};

if (!window.ice) {
    window.ice = new Object;
}

if (!window.ice.compat) {
    (function(namespace) {
        namespace.compat = true;
        //include functional.js
        //include oo.js
        //include collection.js
        //include string.js
        //include window.js
        //include delay.js

        //include status.js
        namespace.DefaultIndicators = DefaultIndicators;
        namespace.ComponentIndicators = ComponentIndicators;
        window.setFocus = namespace.setFocus;
        //include submit.js
        window.iceSubmitPartial = iceSubmitPartial;
        window.iceSubmit = iceSubmit;
        window.formOf = formOf;

        window.onLoad = namespace.onLoad;
        window.onUnload = namespace.onUnload;

        var log = namespace.log.childLogger(namespace.log, "compat");
        window.logger = {
            debug:  curry(namespace.log.debug, log),
            info:   curry(namespace.log.info, log),
            warn:   curry(namespace.log.warn, log),
            error:  curry(namespace.log.error, log),
            child:  function() {
                return window.logger;
            }
        };
    })(window.ice);
}

