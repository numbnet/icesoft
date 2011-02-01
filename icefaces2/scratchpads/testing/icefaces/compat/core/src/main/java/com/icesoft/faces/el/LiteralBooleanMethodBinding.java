/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.el;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * The EL that Facelets uses allows for a MethodBinding with
 *  "true" or "false" that will resolve to Boolean.TRUE or
 *  Boolean.FALSE.  The EL with JSF1.1-JSP doesn't seem so
 *  forgiving.  So we need this helper class to act as a
 *  MethodBinding, but just return a constant Boolean value.
 * 
 * @author Mark Collette
 * @since 1.6
 */
public class LiteralBooleanMethodBinding
    extends MethodBinding
    implements Serializable
{
    private String svalue;
    private Boolean value;
    
    public LiteralBooleanMethodBinding(String svalue) {
        this.svalue = svalue;
        this.value = resolve(svalue);
    }
    
    public Object invoke(FacesContext facesContext, Object[] objects)
        throws EvaluationException, MethodNotFoundException
    {
        return value;
    }
    
    public Class getType(FacesContext facesContext)
        throws MethodNotFoundException
    {
        return Boolean.class;
    }
    
    public String getExpressionString() {
        return svalue;
    }
    
    private static Boolean resolve(String value) {
        Boolean ret = Boolean.FALSE;
        if( value != null ) {
            try {
                ret = Boolean.valueOf(value);
            }
            catch(Exception e) {} // Leave it as Boolean.FALSE
        }
        return ret;
    }
}
