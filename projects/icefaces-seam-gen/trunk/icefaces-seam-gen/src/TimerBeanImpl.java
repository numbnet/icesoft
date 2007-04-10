package @actionPackage@;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.In;
import org.jboss.seam.ScopeType;
import org.jboss.seam.core.Manager;

import javax.faces.context.FacesContext;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import java.util.Timer;
import java.util.TimerTask;
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
    
    private RenderManager renderManager;

    private boolean doneSetup;

    private IntervalRenderer ir;

    private PersistentFacesState state = PersistentFacesState.getInstance();

    public PersistentFacesState getState() {
        return state;
    }

    public void renderingException( RenderingException re) {
        System.out.println("Exception in rendering: " + re);
    } 


    public TimerBeanImpl() {
        
        System.out.println("xxxxxxxxxx       TimerBean Constructed");

//          tt = new TimerTask() {
//            public void run() {
//
//                PersistentFacesState state = PersistentFacesState.getInstance();
//
//                try {
//                    state.execute();
//                    state.render();
//                } catch (Exception e) {
//                    System.out.println("Exception during lifecycle: " + e);
//                    e.printStackTrace();
//                    t.cancel();
//                }
//            }
//        };
//        t.scheduleAtFixedRate( tt, 2000, 5000 );
        dateFormatter =  DateFormat.getDateTimeInstance();
    }


    
    public String getCurrentTime() {

       //        if (!doneSetup) {
//            renderManager.
       //     ir = renderManager.getIntervalRenderer("Temp renderer");
       //     ir.setInterval(5000);
       //     ir.add(this);

       //     ir.requestRender();
       //     doneSetup = true; 
       // }


        return dateFormatter.format( new Date( System.currentTimeMillis() ) );
    }

    public String getRenderMode() {

        FacesContext fc = FacesContext.getCurrentInstance();
        boolean isSynchronous = Boolean.valueOf((String)
                fc.getExternalContext().getInitParameterMap().get( "com.icesoft.faces.synchronousUpdate" ));
        
        return  isSynchronous? "Synchronous mode": "Asynchronous mode";
        
        
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
        ir.requestStop();
    } 
}
