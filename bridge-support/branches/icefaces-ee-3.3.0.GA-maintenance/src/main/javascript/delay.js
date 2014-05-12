/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2014 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 */

/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

var run = operator();
var runOnce = operator();
var stop = operator();

function Delay(f, milliseconds) {
    return object(function(method) {
        var id = null;
        var canceled = false;

        method(run, function(self, times) {
            //avoid starting a new process
            if (id || canceled) return;

            var call = times ? function() {
                try {
                    f();
                } finally {
                    if (--times < 1) stop(self);
                }
            } : f;

            id = setInterval(call, milliseconds);

            return self;
        });

        method(runOnce, function(self) {
            return run(self, 1);
        });

        method(stop, function(self) {
            //stop only an active process
            if (id) {
                clearInterval(id);
                id = null;
                //cancel execution completely if run* was not called
            } else {
                canceled = true;
            }
        });
    });
}