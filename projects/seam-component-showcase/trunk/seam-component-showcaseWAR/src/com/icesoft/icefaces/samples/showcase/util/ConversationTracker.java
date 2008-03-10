package com.icesoft.icefaces.samples.showcase.util;


import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.ScopeType;
import org.jboss.seam.core.Manager;
import org.jboss.seam.log.Log;

@Scope(ScopeType.EVENT)
@Name("convTracker")
public class ConversationTracker {
    private String convId;
    private String longRunning;
    private String convParam;
    @Logger private Log log;
    
    public void setConvId(String cid){
    	this.convId=cid;
    }
    
	public String getConvId() {
		return Manager.instance().getCurrentConversationId();
	}

	public String getLongRunning() {
		return String.valueOf(Manager.instance().isLongRunningConversation());
	}

	public String getConvParam() {
		return Manager.instance().getConversationIdParameter();
	}
	   @Destroy
	   public void destroy() {
		 //  log.info("destroying ConversationTracker");
	   }
	
}
