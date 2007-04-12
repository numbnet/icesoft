package org.icesoft.testProject;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.In;
import org.jboss.seam.ScopeType;
import org.jboss.seam.core.Manager;

import javax.faces.context.FacesContext;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import java.util.Date;
import java.text.DateFormat;

import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.async.render.IntervalRenderer;
import com.icesoft.faces.async.render.Renderable;

/**
 * @author ICEsoft Technologies, Inc.
 */

@Name("timer")
@Scope(ScopeType.SESSION)
public class TimerBeanImpl implements Renderable, TimerBean {

    private DateFormat dateFormatter;

    @In
    private RenderManager renderManager;

    private boolean doneSetup;

    private IntervalRenderer ir;

    private PersistentFacesState state = PersistentFacesState.getInstance();

    private String synchronous;


    public PersistentFacesState getState() {
        return state;
    }

    public void renderingException( RenderingException re) {
        System.out.println("Exception in rendering: " + re);
        if (ir != null) {
            ir.requestStop();
        }
    }


    public TimerBeanImpl() {
        dateFormatter =  DateFormat.getDateTimeInstance();
    }


    
    public String getCurrentTime() {

        state = PersistentFacesState.getInstance();

        if (!doneSetup) {
            FacesContext fc = FacesContext.getCurrentInstance();
            synchronous = (String) fc.getExternalContext().getInitParameterMap().
                    get( "com.icesoft.faces.synchronousUpdate" );
            boolean timed = Boolean.valueOf( (String) fc.getExternalContext().getInitParameterMap().
                    get("org.icesoft.examples.serverClock"));

            if (timed) {
                ir = renderManager.getIntervalRenderer("Temp renderer");
                ir.setInterval(5000);
                ir.add(this);
                ir.requestRender();
            }
        }
        
        doneSetup = true;
        return dateFormatter.format( new Date( System.currentTimeMillis() ) );
    }



    public String getRenderMode() {
        return  synchronous;
        
    }

    public String getCurrentConversation() {
        Manager m = Manager.instance();
        return m.getCurrentConversationId();
    }

    public String getLongRunning() {
        Manager m = Manager.instance();
        return Boolean.toString( m.isLongRunningConversation() );
    } 

    @Remove
    @Destroy
    public void remove() {
        if (ir != null) {
            ir.requestStop();
        } 
    }
}
