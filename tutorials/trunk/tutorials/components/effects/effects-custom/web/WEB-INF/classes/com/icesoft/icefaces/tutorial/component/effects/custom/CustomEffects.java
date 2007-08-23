/*
 * CustomEffects.java
 *
 * Created on July 17, 2007, 1:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.icefaces.tutorial.component.effects.custom;

import com.icesoft.faces.context.effects.BlindDown;
import com.icesoft.faces.context.effects.Effect;

/**
 *
 * @author dnorthcott
 */
public class CustomEffects {
    
    private Effect panelEffect;
    
    /** Creates a new instance of CustomEffects */
    public CustomEffects() {
    }

    public Effect getPanelEffect() {
        return panelEffect;
    }

    public void setPanelEffect(Effect panelEffect) {
        this.panelEffect = panelEffect;
    }
    public String fireEffect(){
        panelEffect = new BlindDown();
        return null;
    }
    
}
