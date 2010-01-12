package org.icepush.samples.icechat.service.exception;

public class UnauthorizedException extends Exception {
	
	public UnauthorizedException(String msg){
		super(msg);
	}
	
	public UnauthorizedException(){
		super();
	}

}
