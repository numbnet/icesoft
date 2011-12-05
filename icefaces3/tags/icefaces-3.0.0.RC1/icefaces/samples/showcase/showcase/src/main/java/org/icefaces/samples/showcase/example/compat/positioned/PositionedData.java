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

package org.icefaces.samples.showcase.example.compat.positioned;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

@ManagedBean(name= PositionedData.BEAN_NAME)
@ViewScoped
public class PositionedData implements Serializable {
	public static final String BEAN_NAME = "positionedData";
	
	public static List<String> FOODS = generateFoods();
	
	public List<String> getAvailableFoods() { return FOODS; }
	
	private static List<String> generateFoods() {
        List<String> toReturn = new ArrayList<String>(6);
        
        toReturn.add("Cake");
        toReturn.add("Popcorn");
	    toReturn.add("Steak");
	    toReturn.add("Stirfry");
	    toReturn.add("Rice");
	    toReturn.add("Chocolate");
	    
	    return toReturn;
	}
	
	public static boolean empty() {
	    return ((FOODS == null) || (FOODS.size() == 0));
	}
	
	public static void defaultFoods() {
	    FOODS = generateFoods();
	}
	
	public static String getFood(int index) {
	    return FOODS.get(index);
	}
	
	public static void addFood(String food) {
	    FOODS.add(food);
	}
	
	public static String removeFood(int index) {
        return FOODS.remove(index);
	}
	
	public static String getTopFood() {
	    return getFood(0);
	}
	
	public static String getBottomFood() {
	    return getFood(FOODS.size()-1);
	}
	
	public static void sortAscending() {
	    Collections.sort(FOODS);
	}
	
	public static void sortDescending() {
	    Collections.sort(FOODS);
	    Collections.reverse(FOODS);
	}
	
	public static void sortRandom() {
	    Collections.shuffle(FOODS);
	}
}
