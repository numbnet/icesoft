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

package com.icesoft.icefaces.samples.showcase.components.autocomplete;

import com.icesoft.faces.component.selectinputtext.SelectInputText;
import org.icefaces.application.showcase.view.bean.examples.component.selectInputText.City;
import org.icefaces.application.showcase.view.bean.BaseBean;

import org.jboss.seam.log.Log;

import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.ScopeType;


import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;
/**
 * Stores the values picked from the AutoCompleteDictionary (different scope to
 * avoid memory hole). The showcase stores one static copy of the actual
 * dictionary information.
 *
 * @see AutoCompleteDictionary
 */

@Scope(ScopeType.PAGE)
@Name("selectInputText")
public class SeamInputTextController extends BaseBean{

    @Logger Log log;

    // default city, no value.
    @In(required=false) 
    @Out(required=false)
    private City currentCity = new City();

    // city dictionary
    @In("#{cityDictionary}")
    private SeamCityDictionary cityDictionary;

    // list of possible city matches for a given city dictionary lookup
    private List cityMatchPossibilities = new ArrayList<City>();

    // number of city possibilities to show
    private int cityListLength = 15;

    // value associatd with first selectInput Component
    private String selectedCityValue1 = "";
    // value associatd with first selectInput Component
    private String selectedCityValue2 = "";

    // selected city information, assigned when user uses mouse or enter key
    // to select a city.
    // default city, no value.
 
    private City selectedCity = new City();

    @Create
    public void init(){

    }
    /**
     * <p>Called by the selectInputText component at set intervals.  By using
     * the change event we can can get the newly typed work and do a look up in
     * the city dictionary.  The list of possible cities calculatd from the city
     * dictionary is assigned back to the component for display.</p>
     * <p>If the component selected a value then we find the respective city
     * information for dispaly purposes.
     *
     * @param event jsf value change event.
     */
    public void selectInputValueChanged(ValueChangeEvent event) {

        if (event.getComponent() instanceof SelectInputText) {

            // get the number of displayable records from the component
            SelectInputText autoComplete =
                    (SelectInputText) event.getComponent();
            // get the new value typed by component user.
            String newWord = (String) event.getNewValue();

            cityMatchPossibilities =
                    cityDictionary.generateCityMatches(newWord, cityListLength);

            // if there is a selected item then find the city object of the
            // same name
            if (autoComplete.getSelectedItem() != null) {
                selectedCity = getFindCityMatch(
                        autoComplete.getSelectedItem().getLabel());
                // fire effect to draw attention
                valueChangeEffect.setFired(false);
            }
            // if there was no selection we still want to see if a proper
            // city was typed and update our selectedCity instance.
            else{
                City tmp = getFindCityMatch(autoComplete.getValue().toString());
                if (tmp != null){
                    selectedCity = tmp;
                     // fire effect to draw attention
                    valueChangeEffect.setFired(false);
                }
            }

        }
    }

    /**
     * Utility method for finding detailed city information fromn the list of
     * possibile city names.
     *
     * @param cityName city name to do city search on.
     * @return found city object if any, null otherwise.
     */
    private City getFindCityMatch(String cityName) {
        if (cityMatchPossibilities != null) {
            SelectItem city;
            for(int i = 0, max = cityMatchPossibilities.size(); i < max; i++){
                city = (SelectItem)cityMatchPossibilities.get(i);
                if (city.getLabel().compareToIgnoreCase(cityName) == 0) {
                    return (City) city.getValue();
                }
            }
        }
        return null;
    }

    public void setCityDictionary(SeamCityDictionary cityDictionary) {
        this.cityDictionary = cityDictionary;
    }

    public List getCityMatchPossibilities() {
        return cityMatchPossibilities;
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public String getSelectedCityValue1() {
        return selectedCityValue1;
    }

    public void setSelectedCityValue1(String selectedCityValue1) {
        this.selectedCityValue1 = selectedCityValue1;
    }

    public String getSelectedCityValue2() {
        return selectedCityValue2;
    }

    public void setSelectedCityValue2(String selectedCityValue2) {
        this.selectedCityValue2 = selectedCityValue2;
    }

    public int getCityListLength() {
        return cityListLength;
    }
   
    @Destroy
    public void destroy(){
    	
    }
}