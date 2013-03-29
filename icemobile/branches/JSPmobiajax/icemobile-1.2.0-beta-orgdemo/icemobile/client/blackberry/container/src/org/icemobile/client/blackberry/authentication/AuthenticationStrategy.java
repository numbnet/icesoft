package org.icemobile.client.blackberry.authentication;

public interface AuthenticationStrategy {
	
	public void doAuthorization();
	
	public int fetchResult();
	
	public String getAuthorizationValue( ); 
	
		
}
