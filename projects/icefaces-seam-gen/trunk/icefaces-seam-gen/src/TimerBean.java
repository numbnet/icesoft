package @actionPackage@;


import javax.ejb.Local;

/**
 * @author ICEsoft Technologies, Inc.
 */
@Local
public interface TimerBean {

    public String getCurrentTime();

    public String getRenderMode();

    public void remove();

    public String getCurrentConversation();

    public String getLongRunning();

}
