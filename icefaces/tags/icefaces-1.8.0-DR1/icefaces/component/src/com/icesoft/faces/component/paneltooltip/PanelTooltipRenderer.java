package com.icesoft.faces.component.paneltooltip;

import com.icesoft.faces.component.panelpopup.PanelPopupRenderer;
import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

public class PanelTooltipRenderer extends PanelPopupRenderer{
    // Basically, everything is excluded
    private static final String[] PASSTHRU_EXCLUDE =
        new String[] { HTML.STYLE_ATTR };
    private static final String[] PASSTHRU =
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_PANELTOOLTIP,
            PASSTHRU_EXCLUDE);

    protected void doPassThru(FacesContext facesContext, UIComponent uiComponent) {
        PassThruAttributeRenderer.renderHtmlAttributes(
            facesContext, uiComponent, PASSTHRU);
    }
}
