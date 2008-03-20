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
package org.icefaces.application.showcase.view.bean.examples.component.selectInputText;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.application.showcase.util.MessageBundleLoader;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>Application-scope bean used to store static lookup information for
 * AutoComplete (selectInputText) example. Statically referenced by
 * SelectInputTextBean as the cityDictionary is rather large.</p>
 * <p>This class loads the city data from an xml data set.  Once an instance
 * is created a call to #generateCityMatches will generate a list of potential
 * matches. </p>
 *
 * @since 1.7
 */
public class CityDictionary {

    private static Log log = LogFactory.getLog(CityDictionary.class);

    // initialized flag, only occures once ber deployment. 
    private static boolean initialized;

    // list of cities.
    private static ArrayList<SelectItem> cityDictionary;

    /**
     * Creates a new instnace of CityDictionary.  The city dictionary is unpacked
     * and initialized during construction.  This will result in a short delay
     * of 2-3 seconds on the server as a result of processing the large file.
     */
    public CityDictionary() {

        try {
            log.info(MessageBundleLoader.getMessage(
                    "bean.selectInputText.info.initializingDictionary"));

            // initialized the bean, load xml database.
            synchronized (this) {
                init();
            }

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(MessageBundleLoader.getMessage(
                        "bean.selectInputText.error.initializingDictionary"));
            }
        }
    }

    /**
     * Comparator utility for sorting city names.
     */
    private static final Comparator<SelectItem> LABEL_COMPARATOR = new Comparator<SelectItem>() {

        // compare method for city entries.
        public int compare(SelectItem o1, SelectItem o2) {
            // compare ignoring case, give the user a more automated feel when typing
            return o1.getLabel().compareToIgnoreCase(o2.getLabel());
        }
    };

    /**
     * Gets the cityDictionary of cities.
     *
     * @return cityDictionary list in sorted by city name, ascending.
     */
    public List<SelectItem> getDictionary() {
        return cityDictionary;
    }

    /**
     * Generates a short list of cities that match the given searchWord.  The
     * length of the list is specified by the maxMatches attribute.
     *
     * @param searchWord city name to search for
     * @param maxMatches max number of possibilities to return
     * @return list of SelectItem objects which contain potential city names.
     */
    public ArrayList<SelectItem> generateCityMatches(String searchWord, int maxMatches) {

        ArrayList<SelectItem> matchList = new ArrayList<SelectItem>(maxMatches);
        
        // ensure the autocomplete search word is present
        if ((searchWord == null) || (searchWord.trim().length() == 0)) {
            return matchList;
        }

        try {

            int insert = Collections.binarySearch(
                    cityDictionary,
                    new SelectItem(null, searchWord),
                    LABEL_COMPARATOR);

            // less then zero if we have a partial match
            if (insert < 0) {
                insert = Math.abs(insert) - 1;
            }

            for (int i = 0; i < maxMatches; i++) {
                // quit the match list creation if the index is larger than
                // max entries in the cityDictionary if we have added maxMatches.
                if ((insert + i) >= cityDictionary.size() ||
                        i >= maxMatches) {
                    break;
                }
                matchList.add(cityDictionary.get(insert + i));
            }
        } catch (Throwable e) {
            log.error(MessageBundleLoader.getMessage(
                    "bean.selectInputText.error.findingMatches"), e);
        }
        // assign new matchList
        return matchList;
    }

    /**
     * Reads the zipped xml city cityDictionary and loads it into memory.
     */
    private static void init() {

        if (!initialized) {

            initialized = true;

            // Raw list of xml cities.
            List<City> cityList = null;

            // load the city cityDictionary from the compressed xml file.

            // get the path of the compressed file
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().
                    getExternalContext().getSession(true);
            String basePath =
                    session.getServletContext().getRealPath(
                            "/WEB-INF/classes/org/icefaces/application/showcase/view/resources");
            basePath += "/city.xml.zip";

            // extract the file
            ZipEntry zipEntry;
            ZipFile zipFile;
            try {
                zipFile = new ZipFile(basePath);
                zipEntry = zipFile.getEntry("city.xml");
            }
            catch (Exception e) {
                log.error(MessageBundleLoader.getMessage(
                        "bean.selectInputText.error.retrievingRecords"), e);
                return;
            }

            // get the xml stream and decode it.
            if (zipFile.size() > 0 && zipEntry != null) {
                try {
                    BufferedInputStream dictionaryStream =
                            new BufferedInputStream(
                                    zipFile.getInputStream(zipEntry));
                    XMLDecoder xDecoder = new XMLDecoder(dictionaryStream);
                    // get the city list.
                    cityList = (List) xDecoder.readObject();
                    dictionaryStream.close();
                    zipFile.close();
                    xDecoder.close();
                } catch (ArrayIndexOutOfBoundsException e) {
                    log.error(MessageBundleLoader.getMessage(
                            "bean.selectInputText.error.record"), e);
                    return;
                } catch (IOException e) {
                    log.error(MessageBundleLoader.getMessage(
                            "bean.selectInputText.error.recordIO"), e);
                    return;
                }
            }

            // Finally load the object from the xml file.
            if (cityList != null) {
                cityDictionary = new ArrayList<SelectItem>(cityList.size());

                for (City city : cityList) {
                    if (city != null && city.getCity() != null) {
                        cityDictionary.add(new SelectItem(city, city.getCity()));
                    }
                }

                // finally sort the list
                Collections.sort(cityDictionary, LABEL_COMPARATOR);
            }
            // clean up 
            cityList.clear();
        }

    }
}
