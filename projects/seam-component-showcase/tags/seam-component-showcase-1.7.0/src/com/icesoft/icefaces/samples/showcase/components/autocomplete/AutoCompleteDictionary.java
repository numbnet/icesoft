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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.ScopeType;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Application-scope bean used to store static lookup information for
 * AutoComplete (selectInputText) example. Statically referenced by
 * AutoCompleteBean as the dictionary is rather large.
 *
 * @see AutoCompleteBean
 */
@Name("dictionary")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class AutoCompleteDictionary {

    private static Log log = LogFactory.getLog(AutoCompleteDictionary.class);

    // list of cities
    private static List dictionary;
    private static final String DATA_RESOURCE_PATH = "/WEB-INF/resources/city.xml.zip";
    

    @Create
    public void loadDictionary(){
      try {
          loadCityData();
      }
      catch (Exception e) {
        if (log.isErrorEnabled()) {
            log.error("could load city data for auto-complete", e);
        }  
      }
    }
    
    /**
     * Comparator utility for sorting city names.
     */
    public static final Comparator LABEL_COMPARATOR = new Comparator() {
        String s1;
        String s2;

        // compare method for city entries.
        public int compare(Object o1, Object o2) {

            if (o1 instanceof SelectItem) {
                s1 = ((SelectItem) o1).getLabel();
            } else {
                s1 = o1.toString();
            }

            if (o2 instanceof SelectItem) {
                s2 = ((SelectItem) o2).getLabel();
            } else {
                s2 = o2.toString();
            }
            // compare ingnoring case, give the user a more automated feel when typing
            return s1.compareToIgnoreCase(s2);
        }
    };

    /**
     * Gets the dictionary of cities.
     *
     * @return dictionary list in sorted by city name, ascending.
     */
    public List getDictionary() {
        return dictionary;
    }

    private static void loadCityData() throws IOException {

        // Loading of the resource must be done the "JSF way" so that
        // it is agnostic about it's environment (portlet vs servlet).
        // First we get the resource as an InputStream
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        InputStream is = ec.getResourceAsStream(DATA_RESOURCE_PATH);

        //Wrap the InputStream as a ZipInputStream since it
        //is a zip file.
        ZipInputStream zipStream = new ZipInputStream(is);

        //Prime the stream by reading the first entry.  The way
        //we have it currently configured, there should only be
        //one.
        ZipEntry firstEntry = zipStream.getNextEntry();

        //Pass the ZipInputStream to the XMLDecoder so that it
        //ca read in the list of cities and associated data.
        XMLDecoder xDecoder = new XMLDecoder(zipStream);
        List cityList = (List) xDecoder.readObject();

        //Close the decoder and the stream.
        xDecoder.close();
        zipStream.close();

        if (cityList == null) {
            String message = "could not read city data from " + DATA_RESOURCE_PATH;
            if (log.isErrorEnabled()) {
                log.error(message);
            }
            throw new IOException(message);
        }

        dictionary = new ArrayList(cityList.size());
        City tmpCity;
        for (int i = 0, max = cityList.size(); i < max; i++) {
            tmpCity = (City) cityList.get(i);
            if (tmpCity != null && tmpCity.getCity() != null) {
                dictionary.add(new SelectItem(tmpCity, tmpCity.getCity()));
            }
        }
        cityList.clear();

        Collections.sort(dictionary, LABEL_COMPARATOR);

    }

}