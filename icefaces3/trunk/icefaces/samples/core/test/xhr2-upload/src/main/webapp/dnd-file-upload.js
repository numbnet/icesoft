/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

window.onload = function() {
    var progressDiv = document.getElementById('progress');
    jsf.ajax.addOnEvent(function(event) {
        progressDiv.appendChild(document.createElement('div')).appendChild(document.createTextNode(event.status));
    });


    var dropTarget = document.getElementById("drop-target");
    var form = document.getElementById("the-form");
    var fileInput = document.getElementById('the-form:file');
    fileInput.style.visibility = 'hidden';
    dropTarget.ondragover = function () {
        dropTarget.className = 'hover'; return false;
    };
    dropTarget.ondragend = function () {
        dropTarget.className = ''; return false;
    };
    dropTarget.ondrop = function(event) {
        dropTarget.className = '';
        event.preventDefault();
        event.stopPropagation();
        //remove file input element so that the form will not submit it
        form.removeChild(fileInput);

        var files = event.dataTransfer.files;
        for (var i=0, len = files.length; i < len; i++) {
            var f = files[i];
            jsf.ajax.request(form, event, {
                //send the content in place of the file input
                "the-form:file": f,
                "javax.faces.partial.render": '@all'
            });
            form.appendChild(fileInput);
        }
    }
}
