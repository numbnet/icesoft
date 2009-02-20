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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */

package com.icesoft.icefaces.samples.showcase.components.table;

import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import org.jboss.seam.annotations.Unwrap;
import com.icesoft.faces.component.datapaginator.DataPaginator;
import com.icesoft.faces.application.D2DViewHandler;



import java.io.Serializable;

/**
 * <p>The TableBean class is the backing bean for the table Component showcase
 * demonstration. It is used to store the visibility state of the four data
 * columns used in the demonstration.</p>
 *
 * @since 0.3.0
 */

/**
 * Until the DataTablePaginator is reworked for value binding, we will
 * use component-binding to reset to first row.  This example allows us 
 * to show component-binding as well as some of the other Seam annotaions
 * like @Unwrap which can be quite handy.
 * 
 */

@Scope(ScopeType.EVENT)
@Name("dataPaginator")
public class SeamDataPaginator implements Serializable{
   @Logger Log log;
	
   	DataPaginator seamDataPaginator;  	
   
	@Create
	public void init(){
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		D2DViewHandler dvh = (D2DViewHandler) FacesContext.getCurrentInstance().getApplication().getViewHandler();
		UIComponent uicomp = (DataPaginator)(dvh.findComponent(":iceform:dataScroll_1",viewRoot));
		
		if (uicomp==null){
				seamDataPaginator = new DataPaginator();
		}
		else {
			seamDataPaginator = (DataPaginator)uicomp;
		}
		
	}
	
	public void setSeamDataPaginator(DataPaginator datapaginator){
		this.seamDataPaginator = datapaginator;
	}

	@Unwrap
	public DataPaginator getSeamDataPaginator(){
		return this.seamDataPaginator;
	}
	

}