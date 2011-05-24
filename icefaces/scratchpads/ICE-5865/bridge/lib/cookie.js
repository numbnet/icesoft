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

[ Ice ].as(function(This) {
    This.Cookie = This.Parameter.Association.subclass({
        initialize: function(name, value, path) {
            this.name = name;
            this.value = value || '';
            this.path = path || '/';
            this.save();
        },

        saveValue: function(value) {
            this.value = value;
            this.save();
        },

        loadValue: function() {
            this.load();
            return this.value;
        },

        save: function() {
            document.cookie = this.name + '=' + this.value + '; path=' + this.path;
            return this;
        },

        load: function() {
            var foundTuple = This.Cookie.parse().detect(function(tuple) {
                return this.name == tuple[0];
            }.bind(this));
            this.value = foundTuple ? foundTuple[1] : null;
            return this;
        },

        remove: function() {
            var date = new Date();
            date.setTime(date.getTime() - 24 * 60 * 60 * 1000);
            document.cookie = this.name + '=; expires=' + date.toGMTString() + '; path=' + this.path;
        }
    });

    This.Cookie.all = function() {
        return This.Cookie.parse().collect(function(tuple) {
            var name = tuple[0];
            var value = tuple[1];
            return new This.Cookie(name, value);
        });
    };

    This.Cookie.lookup = function(name, value) {
        var foundTuple = This.Cookie.parse().detect(function(tuple) {
            return name == tuple[0];
        });
        if (foundTuple) {
            return new This.Cookie(name, foundTuple[1]);
        } else {
            if (value) {
                return new This.Cookie(name, value);
            } else {
                throw 'Cannot find cookie named: ' + name;
            }
        }
    };

    This.Cookie.exists = function(name) {
        return document.cookie.contains(name + '=');
    };

    //private
    This.Cookie.parse = function() {
        return document.cookie.split('; ').collect(function(tupleDetails) {
            return tupleDetails.contains('=') ? tupleDetails.split('=') : [tupleDetails, ''];
        });
    };
});
