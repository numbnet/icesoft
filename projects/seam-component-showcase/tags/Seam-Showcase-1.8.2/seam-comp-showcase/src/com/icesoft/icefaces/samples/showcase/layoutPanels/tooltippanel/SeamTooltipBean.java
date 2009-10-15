package com.icesoft.icefaces.samples.showcase.layoutPanels.tooltippanel;

import org.icefaces.application.showcase.view.bean.examples.layoutPanel.panelToolTip.PanelToolTipController;
import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


@Scope(ScopeType.PAGE)
@Name("tooltipController")
public class SeamTooltipBean extends PanelToolTipController{
    //source component for which the tooltip will be rendered/unrendered


 
}