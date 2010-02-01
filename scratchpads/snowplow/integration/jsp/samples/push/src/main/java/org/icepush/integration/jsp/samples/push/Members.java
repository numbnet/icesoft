package org.icepush.integration.jsp.samples.push;

import java.util.Vector;
import java.util.List;

public class Members {
    private Vector in;
    private Vector out;
    private String nickname;

    public Members() {
	in = new Vector();
	out = new Vector();
	nickname = null;
    }
    
    public String getNickname() {
	return nickname;
    }
    public void setNickname(String nn) {
	nickname = nn;
	if (out.remove(nickname)) {
	    System.out.println("moving " + nickname + " to IN");
	    in.add(nickname);
	} else if (in.remove(nickname)) {
	    System.out.println("moving " + nickname + " to OUT");
	    out.add(nickname);
	} else {
	    System.out.println("adding " + nickname + " to IN");
	    in.add(nickname);
	}
    }

    public List getIn() {
	return (List)in;
    }
    public List getOut() {
	return (List)out;
    }
}
