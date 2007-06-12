/*
 * LoggedInInterceptor.java
 *
 * Created on June 5, 2007, 4:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.eb;

import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.faces.event.PhaseId;

import org.jboss.seam.annotations.Interceptor;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.interceptors.BijectionInterceptor;
import org.jboss.seam.interceptors.BusinessProcessInterceptor;
import org.jboss.seam.interceptors.ConversationInterceptor;
import org.jboss.seam.interceptors.RemoveInterceptor;
import org.jboss.seam.interceptors.ValidationInterceptor;

@Interceptor(around={BijectionInterceptor.class, ValidationInterceptor.class, 
                     ConversationInterceptor.class, BusinessProcessInterceptor.class},
             within=RemoveInterceptor.class)
public class LoggedInInterceptor
{

   @AroundInvoke
   public Object checkLoggedIn(InvocationContext invocation) throws Exception
   {
      boolean isLoggedIn = Contexts.getSessionContext().get("loggedIn")!=null;
      if ( Lifecycle.getPhaseId()==PhaseId.INVOKE_APPLICATION )
      {
         if (isLoggedIn) 
         {
            return invocation.proceed();
         }
         else 
         {
            return "login";
         }
      }
      else
      {
         if ( isLoggedIn )
         {
            return invocation.proceed();
         }
         else
         {
            Method method = invocation.getMethod();
            if ( method.getReturnType().equals(void.class) )
            {
               return null;
            }
            else
            {
               return method.invoke( invocation.getTarget(), invocation.getParameters() );
            }
         }
      }
   }

}
