/*
 * InterceptErrorListener.java
 *
 * Created on June 27, 2007, 10:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jboss.seam.example.booking;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.icesoft.faces.component.ext.HtmlInputText;

/**
 *
 * @author jguglielmin
 */
@SuppressWarnings("all")
public class InterceptErrorListener implements PhaseListener{
    
	public void afterPhase(PhaseEvent e) {}
	
	public void beforePhase(PhaseEvent e) {
		FacesContext c=e.getFacesContext();
		ValueBinding binding=c.getApplication().createValueBinding("#{register}");
		
		RegisterAction aBean=(RegisterAction)binding.getValue(c);
		System.out.println("Phase ID: " + e.getPhaseId() + ", Register: " + aBean);
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
