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

package org.icefaces.application.showcase.view.bean.examples.component.outputMedia;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "media")
@ViewScoped
public class Media  implements Serializable {
    private static Map players = new HashMap();

    static {
        Properties props = new Properties();
        props.setProperty("source", "/media/ICEfaces_Flash.swf");
        props.setProperty("style", "width:300px;height:250px;");
        props.setProperty("autoStartParamName", "play");
        props.setProperty("autoStartParamValue", "true");
        props.setProperty("controlsParamName", "menu");
        props.setProperty("controlsParamValue", "true");
        players.put("flash", props);

        props = new Properties();
        props.setProperty("source", "/media/ICEfaces_Quicktime.mov");
        props.setProperty("style", "width:300px;height:270px;");
        props.setProperty("autoStartParamName", "autoplay");
        props.setProperty("autoStartParamValue", "true");
        props.setProperty("controlsParamName", "controller");
        props.setProperty("controlsParamValue", "true");
        players.put("quicktime", props);

        props = new Properties();
        props.setProperty("source", "/media/ICEfaces_Windows_Media.wmv");
        props.setProperty("style", "width:300px;height:250px;");
        props.setProperty("autoStartParamName", "autostart");
        props.setProperty("autoStartParamValue", "1"); // Firefox doesn't work with true/false
        props.setProperty("controlsParamName", "showcontrols");
        props.setProperty("controlsParamValue", "1");
        players.put("windows", props);
    }

    private String selectedPlayer = "flash";
    Properties playerProps = (Properties) players.get(selectedPlayer);

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
        if (playerProps != null) return playerProps.getProperty("source");
        // ICE-4749
        selectedPlayer = "flash";
        return "/media/ICEfaces_Flash.swf";
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
