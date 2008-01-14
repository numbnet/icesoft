package action.com.icesoft.ejb;

import javax.ejb.Local;

@Local
public interface Authenticator
{
   boolean authenticate();
}
