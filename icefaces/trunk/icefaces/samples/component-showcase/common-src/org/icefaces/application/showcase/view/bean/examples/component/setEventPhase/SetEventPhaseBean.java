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

package org.icefaces.application.showcase.view.bean.examples.component.setEventPhase;

import org.icefaces.application.showcase.util.MessageBundleLoader;
import org.icefaces.application.showcase.view.bean.BaseBean;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 * <p>The SetEventPhaseBean class is the backing bean for the setEventPhase
 * component demonstration. It is derived from SelectionTagsBean, and shows
 * how the bean code may be simplified with use of the setEventPhase component.</p>
 *
 * @since 1.8
 */
public class SetEventPhaseBean extends BaseBean {

    /**
     * Countries and Cities declorations
     */

    private SelectItem[] cityItems;

    private static final String COUNTRY_CANADA =
            MessageBundleLoader.getMessage("bean.selection.country1.value");
    private static final String COUNTRY_USA =
            MessageBundleLoader.getMessage("bean.selection.country2.value");
    private static final String COUNTRY_CHINA =
            MessageBundleLoader.getMessage("bean.selection.country3.value");
    private static final String COUNTRY_UK =
            MessageBundleLoader.getMessage("bean.selection.country4.value");
    private static final String COUNTRY_RUSSIA =
            MessageBundleLoader.getMessage("bean.selection.country5.value");

    private static final SelectItem[] COUNTRY_ITEMS = new SelectItem[]{
            new SelectItem(COUNTRY_CANADA),
            new SelectItem(COUNTRY_USA),
            new SelectItem(COUNTRY_CHINA),
            new SelectItem(COUNTRY_UK),
            new SelectItem(COUNTRY_RUSSIA)
    };

    private static final SelectItem[] CITIES_CANADA = new SelectItem[]{
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country1.city1.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country1.city2.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country1.city3.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country1.city4.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country1.city5.value"))
    };

    private static final SelectItem[] CITIES_USA = new SelectItem[]{
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country2.city1.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country2.city2.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country2.city3.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country2.city4.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country2.city5.value"))
    };

    private static final SelectItem[] CITIES_CHINA = new SelectItem[]{
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country3.city1.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country3.city2.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country3.city3.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country3.city4.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country3.city5.value"))
    };

    private static final SelectItem[] CITIES_UK = new SelectItem[]{
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country4.city1.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country4.city2.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country4.city3.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country4.city4.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country4.city5.value"))
    };

    private static final SelectItem[] CITIES_RUSSIA = new SelectItem[]{
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country5.city1.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country5.city2.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country5.city3.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country5.city4.value")),
            new SelectItem(MessageBundleLoader.getMessage(
                    "bean.selection.country5.city5.value"))
    };

    // selectOneListbox example value
    private String selectedCountry;
    // selectManyListbox example value
    private String[] selectedCities;

    /**
     * Value change listener for the country change event. Sets up the cities
     * listbox according to the country.
     *
     * @param event value change event
     */
    public void countryChanged(ValueChangeEvent event) {
        // get new city value and assign it. 
        String newCountry = (String) event.getNewValue();

        if (COUNTRY_CANADA.equals(newCountry)) {
            cityItems = CITIES_CANADA;
        } else if (COUNTRY_USA.equals(newCountry)) {
            cityItems = CITIES_USA;
        } else if (COUNTRY_CHINA.equals(newCountry)) {
            cityItems = CITIES_CHINA;
        } else if (COUNTRY_UK.equals(newCountry)) {
            cityItems = CITIES_UK;
        } else if (COUNTRY_RUSSIA.equals(newCountry)) {
            cityItems = CITIES_RUSSIA;
        } else {
            cityItems = null;
        }

        // Select the first city in the list, for that country
        selectedCities = new String[]{ cityItems[0].getLabel() };

        // reset effect
        valueChangeEffect.setFired(false);
    }

    /**
     * Value change listener called when an new item is selected in the list of
     * cities.  No actual work is done for this method but it does show
     * what a ValueChange method signature should look like.
     *
     * @param event JSF value change event
     */
    public void cityChanged(ValueChangeEvent event) {
        valueChangeEffect.setFired(false);
    }

    /**
     * Gets the option items for countries.
     *
     * @return array of country items
     */
    public SelectItem[] getCountryItems() {
        return COUNTRY_ITEMS;
    }

    /**
     * Gets the option items of cities.
     *
     * @return array of city items
     */
    public SelectItem[] getCityItems() {
        return cityItems;
    }

    /**
     * Gets the selected country.
     *
     * @return the selected country
     */
    public String getSelectedCountry() {
        return selectedCountry;
    }

    /**
     * Gets the selected cities.
     *
     * @return array of selected cities
     */
    public String[] getSelectedCities() {
        return selectedCities;
    }

    /**
     * Returns the selectedCities array a comma seperated list
     *
     * @return comma seperated list of selected cities.
     */
    public String getSelectedCitiesStrings() {
        return convertToString(selectedCities);
    }

    public void setCityItems(SelectItem[] cityItems) {
        this.cityItems = cityItems;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public void setSelectedCities(String[] selectedCities) {
        this.selectedCities = selectedCities;
    }

    /**
     * Converts string arrays for displays.
     *
     * @param stringArray string array to convert
     * @return a string concatenating the elements of the string array
     */
    private static String convertToString(String[] stringArray) {
        if (stringArray == null) {
            return "";
        }
        StringBuffer itemBuffer = new StringBuffer();
        for (int i = 0, max = stringArray.length; i < max; i++) {
            if (i > 0) {
                itemBuffer.append(" , ");
            }
            itemBuffer.append(stringArray[i]);
        }
        return itemBuffer.toString();
    }
}
