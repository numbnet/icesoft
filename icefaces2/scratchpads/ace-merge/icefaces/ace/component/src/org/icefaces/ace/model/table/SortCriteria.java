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
package org.icefaces.ace.model.table;

public class SortCriteria {

	private String propertyName;
	private boolean ascending;
	
	public SortCriteria(String propertyName, boolean ascending) {
		this.propertyName = propertyName;
		this.ascending = ascending;
	}
	
	public String getPropertyName() {
		return this.propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	public boolean getAscending() {
		return this.ascending;
	}
	
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}