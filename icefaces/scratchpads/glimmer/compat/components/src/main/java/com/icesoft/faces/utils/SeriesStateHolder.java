/*
 * Version: MPL 1.1
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
 */

package com.icesoft.faces.utils;

import javax.faces.context.FacesContext;

/**
 * StateHolder is used to hold the complete state of a UIComponent, and so
 *  is used sparingly. UIData, which is used for iterative container
 *  components, only saves the EditableValueHolder fields. So, there's a
 *  huge gap inbetween, of being able to store fields when in an iterative
 *  container. This interface is used by our UISeries to manage non-UIData
 *  state that any component might require, without having to save everything.
 * 
 * @author Mark Collette
 */
public interface SeriesStateHolder {
    public Object saveSeriesState(FacesContext facesContext);
    
    public void restoreSeriesState(FacesContext facesContext, Object state);
}
