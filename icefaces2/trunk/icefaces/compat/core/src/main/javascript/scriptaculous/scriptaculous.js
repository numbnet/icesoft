/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

var Scriptaculous = {
    Version: '1.8.1',
    require: function(libraryName) {
        // inserting via DOM fails in Safari 2.0, so brute force approach
        document.write('<script type="text/javascript" src="' + libraryName + '"><\/script>');
    },
    REQUIRED_PROTOTYPE: '1.6.0',
    load: function() {
        function convertVersionString(versionString) {
            var r = versionString.split('.');
            return parseInt(r[0]) * 100000 + parseInt(r[1]) * 1000 + parseInt(r[2]);
        }

        if ((typeof Prototype == 'undefined') ||
            (typeof Element == 'undefined') ||
            (typeof Element.Methods == 'undefined') ||
            (convertVersionString(Prototype.Version) <
             convertVersionString(Scriptaculous.REQUIRED_PROTOTYPE)))
            throw("script.aculo.us requires the Prototype JavaScript framework >= " +
                  Scriptaculous.REQUIRED_PROTOTYPE);

        $A(document.getElementsByTagName("script")).findAll(function(s) {
            return (s.src && s.src.match(/scriptaculous\.js(\?.*)?$/))
        }).each(function(s) {
            var path = s.src.replace(/scriptaculous\.js(\?.*)?$/, '');
            var includes = s.src.match(/\?.*load=([a-z,]*)/);
            (includes ? includes[1] : 'builder,effects,dragdrop,controls,slider,sound').split(',').each(
                    function(include) {
                        Scriptaculous.require(path + include + '.js')
                    });
        });
    }
}

Scriptaculous.load();