/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
 
package com.icesoft.faces.facelets;

import com.sun.facelets.tag.TagDecorator;
import com.sun.facelets.tag.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * When people are converting from JSP to Facelets, they can
 *  forget that they can't use JSP tags, and get confused as
 *  to why things aren't working. This will give detect JSP
 *  tags and log a debug message.
 * 
 * @since 1.6
 */
public class JspTagDetector implements TagDecorator {
    private static Log log = LogFactory.getLog(TagDecorator.class);
    
    public JspTagDetector() {
        super();
    }
    
    /**
     * @see TagDecorator#decorate(com.sun.facelets.tag.Tag)
     */
    public Tag decorate(Tag tag) {
        if( log.isDebugEnabled() ) {
            if( tag.getNamespace() != null &&
                tag.getNamespace().startsWith("http://java.sun.com/JSP/") )
            {
                log.debug(
                    "A JSP tag has been detected in your Facelets page. "+
                    "Facelets is an alternative to JSP, so it is not valid " +
                    "to use JSP tags or directives here. The JSP tag:\n" + tag);
            }
        }
        return null;
    }
}
