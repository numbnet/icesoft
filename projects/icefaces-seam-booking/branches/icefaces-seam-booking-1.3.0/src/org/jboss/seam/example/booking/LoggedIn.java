//$Id: LoggedIn.java,v 1.1 2006/11/20 05:19:01 gavin Exp $
package org.jboss.seam.example.booking;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.Interceptors;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Interceptors(LoggedInInterceptor.class)
public @interface LoggedIn {}
