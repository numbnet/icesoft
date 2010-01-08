package org.icepush.samples.icechat.service.exception;

public class ConfigurationException extends RuntimeException {

	public ConfigurationException(String genericMessage){
		super(genericMessage);
	}
	
	public ConfigurationException(Class clazz, String field, String problem){
		this("field [" + field + "] in class [ " + clazz.getName() + "] should not be " + problem);
	}
	
}
