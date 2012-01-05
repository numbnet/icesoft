/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
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

        var compatLogger = namespace.log.childLogger(namespace.log, "compat");
        window.logger = {
            debug:  curry(namespace.log.debug, compatLogger),
            info:   curry(namespace.log.info, compatLogger),
            warn:   curry(namespace.log.warn, compatLogger),
            error:  curry(namespace.log.error, compatLogger),
            child:  function() {
                return window.logger;
            }
        };
    })(window.ice);
}

