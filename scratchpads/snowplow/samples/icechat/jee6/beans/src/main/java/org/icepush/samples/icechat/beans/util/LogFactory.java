package org.icepush.samples.icechat.beans.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

class LogFactory {
	@Produces Logger createLogger(InjectionPoint injectionPoint) {
	return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
}
