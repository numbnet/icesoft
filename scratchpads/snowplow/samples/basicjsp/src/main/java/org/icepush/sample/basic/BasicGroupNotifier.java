package org.icepush.sample.basic;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Iterator;
import org.icepush.PushContext;

public class BasicGroupNotifier extends Thread {
    private HashSet groups;
    private PushContext pushContext;

    public BasicGroupNotifier() {
	groups = new HashSet();
    }
    
    public PushContext getPushContext() {
	return pushContext;
    }
    public void setPushContext(PushContext pc) {
	pushContext = pc;
    }

    public void addGroup(String group) {
	groups.add(group);
    }
    public void removeGroup(String group) {
	groups.remove(group);
    }

    public void push() {
	for (Iterator i=groups.iterator(); i.hasNext();) {
	    String group = (String)i.next();
	    //	    System.out.println("Pushing to group " + group);
	    pushContext.push(group);
	}
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
	response.setContentType("text/html");
	response.setContentLength(0);
    }
}
