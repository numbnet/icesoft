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

var iceSubmitPartial;
var iceSubmit;
var formOf;

(function() {
    function resetHiddenFieldsFor(aForm) {
        try {
            each(aForm.elements, function(formElement) {
                var elName = formElement.name;
                if (formElement.type == 'hidden' && formElement.id == '' &&
                    formElement.name != 'javax.faces.ViewState' &&
                    formElement.name != 'ice.window' &&
                    //special case for Portlet bridge
                    formElement.name != 'javax.faces.encodedURL' &&
                    //special case for MyFaces 2.0
                    (elName.lastIndexOf("_SUBMIT") != (elName.length - 7)) &&
                    formElement.name != 'ice.view') {
                    formElement.value = '';
                }
            });
        } catch (e) {

        }
    }

    iceSubmitPartial = function(form, component, evt) {
        form = form || formOf(component);
        ice.submit(evt, component || form, function(parameter) {
            if (Ice.Menu != null && Ice.Menu.menuContext != null) {
                parameter('ice.menuContext', Ice.Menu.menuContext);
            }
            parameter('ice.submit.partial', true);
        });

        resetHiddenFieldsFor(form);
        return false;
    };

    iceSubmit = function(form, component, evt) {
        form = form || formOf(component);
        var code;
        if (evt.keyCode) code = evt.keyCode;
        else if (evt.which) code = evt.which;
        if (code > 3) {
            if (code != 13) {
                return false;
            }
        }
        ice.submit(evt, component || form, function(parameter) {
            if (Ice.Menu != null && Ice.Menu.menuContext != null) {
                parameter('ice.menuContext', Ice.Menu.menuContext);
            }
        });
        resetHiddenFieldsFor(form);

        return false;
    };

    formOf = function(element) {
        var parent = element.parentNode;
        while (parent) {
            if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
            parent = parent.parentNode;
        }

        throw 'Cannot find enclosing form.';
    };
})();
