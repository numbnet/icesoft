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
package action.com.icesoft.ejb;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.ScopeType;
import org.jboss.seam.core.Manager;

import javax.faces.context.FacesContext;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import java.util.Date;
import java.text.DateFormat;
import java.io.Serializable;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.IntervalRenderer;
import com.icesoft.faces.async.render.Renderable;
import com.icesoft.faces.component.accordion.PanelAccordion;

/**
 * @author ICEsoft Technologies, Inc.
 */

@Name("timer")
@Scope(ScopeType.SESSION)
public class TimerBeanImpl implements Renderable, TimerBean, Serializable {

	private DateFormat dateFormatter;

	@In
	private RenderManager renderManager;

	private boolean doneSetup;
	private boolean openStatus;
	private IntervalRenderer ir;

	private PersistentFacesState state = PersistentFacesState.getInstance();

	private String synchronous;

	public PersistentFacesState getState() {
		return state;
	}

	public void renderingException(RenderingException re) {
		System.out.println("Exception in rendering: " + re);
		if (ir != null) {
			ir.requestStop();
		}
	}

	public TimerBeanImpl() {
		dateFormatter = DateFormat.getDateTimeInstance();
	}

	public String getCurrentTime() {

		state = PersistentFacesState.getInstance();

		if (!doneSetup) {
			FacesContext fc = FacesContext.getCurrentInstance();
			synchronous = (String) fc.getExternalContext()
					.getInitParameterMap().get(
							"com.icesoft.faces.synchronousUpdate");
			boolean timed = Boolean.valueOf((String) fc.getExternalContext()
					.getInitParameterMap().get(
							"org.icesoft.examples.serverClock"));

			if (timed) {
				ir = renderManager.getIntervalRenderer("Temp renderer");
				ir.setInterval(5000);
				ir.add(this);
				ir.requestRender();
			}
		}

		doneSetup = true;
		return dateFormatter.format(new Date(System.currentTimeMillis()));
	}

	public String getRenderMode() {
		return synchronous;

	}

	@Begin(join = true)
	public String getCurrentConversation() {
		Manager m = Manager.instance();
		return m.getCurrentConversationId();
	}

	public String getLongRunning() {
		Manager m = Manager.instance();
		return Boolean.toString(m.isLongRunningConversation());
	}

	public void toggle(ActionEvent event) {
		PanelAccordion component1 = (PanelAccordion) event.getSource();
		openStatus = component1.getExpanded().booleanValue();
	}

	public void setOpenStatus(boolean input) {
		this.openStatus = input;
	}

	public boolean getOpenStatus() {
		return openStatus;
	}

	@Remove
	@Destroy
	public void remove() {
		if (ir != null) {
			ir.requestStop();
		}
	}
}
