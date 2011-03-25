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

Sound = {
    tracks: {},
    _enabled: true,
    template:
            new Template('<embed style="height:0" id="sound_#{track}_#{id}" src="#{url}" loop="false" autostart="true" hidden="true"/>'),
    enable: function() {
        Sound._enabled = true;
    },
    disable: function() {
        Sound._enabled = false;
    },
    play: function(url) {
        if (!Sound._enabled) return;
        var options = Object.extend({
            track: 'global', url: url, replace: false
        }, arguments[1] || {});

        if (options.replace && this.tracks[options.track]) {
            $R(0, this.tracks[options.track].id).each(function(id) {
                var sound = $('sound_' + options.track + '_' + id);
                sound.Stop && sound.Stop();
                sound.remove();
            })
            this.tracks[options.track] = null;
        }

        if (!this.tracks[options.track])
            this.tracks[options.track] = { id: 0 }
        else
            this.tracks[options.track].id++;

        options.id = this.tracks[options.track].id;
        $$('body')[0].insert(
                Prototype.Browser.IE ? new Element('bgsound', {
                    id: 'sound_' + options.track + '_' + options.id,
                    src: options.url, loop: 1, autostart: true
                }) : Sound.template.evaluate(options));
    }
};

if (Prototype.Browser.Gecko && navigator.userAgent.indexOf("Win") > 0) {
    if (navigator.plugins && $A(navigator.plugins).detect(function(p) {
        return p.name.indexOf('QuickTime') != -1
    }))
        Sound.template = new Template('<object id="sound_#{track}_#{id}" width="0" height="0" type="audio/mpeg" data="#{url}"/>')
    else
        Sound.play = function() {
        }
}
