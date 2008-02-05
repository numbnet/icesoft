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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */

package com.icesoft.icefaces.samples.showcase.components.media;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Media {
    private static Map players = new HashMap();

    {
        Properties props = new Properties();
        props.setProperty("source", "/media/ICEfaces_Skyscraper_120x600.swf");
        props.setProperty("style", "width:120px;height:600px;");
        props.setProperty("autoStartParamName", "play");
        props.setProperty("autoStartParamValue", "true");
        props.setProperty("controlsParamName", "menu");
        props.setProperty("controlsParamValue", "true");
        players.put("flash", props);

        props = new Properties();
        props.setProperty("source", "http://icesoft.com/demo/icefaces-c3-demo.mov");
        props.setProperty("style", "width:640px;height:496px;");
        props.setProperty("autoStartParamName", "autoplay");
        props.setProperty("autoStartParamValue", "true");
        props.setProperty("controlsParamName", "controller");
        props.setProperty("controlsParamValue", "true");
        players.put("quicktime", props);

        props = new Properties();
        props.setProperty("source", "http://icesoft.com/demo/icefaces-c3-demo.wmv");
        props.setProperty("style", "width:640px;height:525px;");
        props.setProperty("autoStartParamName", "autostart");
        props.setProperty("autoStartParamValue", "1"); // Firefox doesn't work with true/false
        props.setProperty("controlsParamName", "showcontrols");
        props.setProperty("controlsParamValue", "1");
        players.put("windows", props);
    }

    private String selectedPlayer = "flash";
    Properties playerProps = (Properties) players.get(selectedPlayer);
    private String autoStartParamName;

    public Media() {
    }

    public String getSelectedPlayer() {
        return selectedPlayer;
    }

    public void setSelectedPlayer(String selectedPlayer) {
        this.selectedPlayer = selectedPlayer;
        playerProps = (Properties) players.get(selectedPlayer);
    }

    public String getSource() {
        return playerProps.getProperty("source");
    }

    public String getStyle() {
        return playerProps.getProperty("style");
    }

    public String getAutoStartParamName() {
        return playerProps.getProperty("autoStartParamName");
    }

    public String getAutoStartParamValue() {
        return playerProps.getProperty("autoStartParamValue");
    }

    public String getControlsParamName() {
        return playerProps.getProperty("controlsParamName");
    }

    public String getControlsParamValue() {
        return playerProps.getProperty("controlsParamValue");
    }
}