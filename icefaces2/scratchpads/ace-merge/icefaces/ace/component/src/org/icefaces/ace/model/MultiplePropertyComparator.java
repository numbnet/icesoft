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

public class MultiplePropertyComparator implements Comparator {

	private SinglePropertyComparator[] singlePropertyComparators;
	
	public MultiplePropertyComparator(SortCriteria[] sortCriteria) {
		singlePropertyComparators = new SinglePropertyComparator[sortCriteria.length];
		for (int i = 0; i < sortCriteria.length; i++) {
			singlePropertyComparators[i] = new SinglePropertyComparator(sortCriteria[i]);
		}
	}
	
	public int compare(Object obj1, Object obj2) {
	
		try {
			for (int i = 0; i < singlePropertyComparators.length; i++) {
				int result = singlePropertyComparators[i].compare(obj1, obj2);
				if (result != 0) return result;
			}
			return 0;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}