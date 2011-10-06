/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.icefaces.ace.model;

import org.icefaces.ace.model.table.SortCriteria;

import java.util.Comparator;
import javax.faces.context.FacesContext;

public class SinglePropertyComparator implements Comparator {

	private String property;
	private boolean ascending;
	
	public SinglePropertyComparator(SortCriteria sortCriteria) {
		this.property = sortCriteria.getPropertyName();
		this.ascending = sortCriteria.getAscending();
	}
	
	public int compare(Object obj1, Object obj2) {
	
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			javax.el.ELResolver resolver = facesContext.getELContext().getELResolver();
			
			Object value1 = resolver.getValue(facesContext.getELContext(), obj1, this.property);
			Object value2 = resolver.getValue(facesContext.getELContext(), obj2, this.property);

			//Empty check
			if(value1 == null)
				return 1;
			else if(value2 == null)
				return -1;
				
			int result = ((Comparable) value1).compareTo(value2);
			
			return this.ascending ? result : -1 * result;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}