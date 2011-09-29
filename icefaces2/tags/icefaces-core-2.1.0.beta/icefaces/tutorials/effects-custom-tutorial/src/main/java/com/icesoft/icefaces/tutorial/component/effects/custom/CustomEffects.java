/*
 * CustomEffects.java
 *
 * Created on July 17, 2007, 1:32 PM
 *
 */

package com.icesoft.icefaces.tutorial.component.effects.custom;

import com.icesoft.faces.context.effects.BlindDown;
import com.icesoft.faces.context.effects.Effect;


public class CustomEffects {
    //effect used to expand the panelGroup
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
    /*
     *fires the blindDown effect
     *@return null
     */
    public String fireEffect(){
        panelEffect = new BlindDown();
        return null;
    }
    
}
