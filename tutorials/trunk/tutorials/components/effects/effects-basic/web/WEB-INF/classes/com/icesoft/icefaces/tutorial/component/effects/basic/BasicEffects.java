package com.icesoft.icefaces.tutorial.component.effects.basic;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
/**
 * <p>
 * A basic backing bean for effect component.  </p>
 */
public class BasicEffects{
        //Effect used to highlight the text
	 private Highlight effectOutputText = new Highlight("#ffff99");
         //displayed input text
         private String text;

 /* Returns the text effect
     *@return Effect EffectOutputText
     */
    public Effect getEffectOutputText() {
        
        return effectOutputText;
    }
    
    /*
     * Sets the output text effect
     *@param Effect effectOutputText
     */
    public void setEffectOutputText(Effect effectOutputText) {
        this.effectOutputText = (Highlight) effectOutputText;
    }

    public String getText() {
        effectOutputText = new Highlight("#FFCC0B");
           
            effectOutputText.setFired(false);
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}