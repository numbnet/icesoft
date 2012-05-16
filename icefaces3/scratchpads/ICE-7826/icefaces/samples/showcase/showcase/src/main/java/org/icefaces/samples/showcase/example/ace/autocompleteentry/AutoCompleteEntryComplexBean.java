/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.autocompleteentry;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ComponentExample(
        parent = AutoCompleteEntryBean.BEAN_NAME,
        title = "example.ace.autocompleteentry.complex.title",
        description = "example.ace.autocompleteentry.complex.description",
        example = "/resources/examples/ace/autocompleteentry/autoCompleteEntryComplex.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="autoCompleteEntryComplex.xhtml",
                    resource = "/resources/examples/ace/autocompleteentry/autoCompleteEntryComplex.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AutoCompleteEntryComplexBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/autocompleteentry/AutoCompleteEntryComplexBean.java")
        }
)
@ManagedBean(name= AutoCompleteEntryComplexBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AutoCompleteEntryComplexBean extends ComponentExampleImpl<AutoCompleteEntryComplexBean> implements Serializable
{
    public static final String BEAN_NAME = "autoCompleteEntryComplexBean";
	
    public static final String CITIES_FILENAME = "World_Cities_Location_table.txt";
	public static final String RESOURCE_PATH = "/resources/autocompleteentry/";

    public AutoCompleteEntryComplexBean() 
    {
        super(AutoCompleteEntryComplexBean.class);
    }
    
	public List<City> cities;
	
	private String selectedText = null; // Text the user is typing in
	public String getSelectedText() { return selectedText; }
	public void setSelectedText(String selectedText) { this.selectedText = selectedText; }
	
	public void submitText(ActionEvent event) {

	}

    public List<City> getCities(){
		if (cities == null) {
			cities = readLocationsFile();
		}
        return cities;
    }
	
	private static List<City> readLocationsFile() {
	    InputStream fileIn = null;
	    BufferedReader in = null;
                    
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            fileIn= ec.getResourceAsStream(AutoCompleteEntryComplexBean.RESOURCE_PATH + CITIES_FILENAME);
            
            if (fileIn != null) {
                // Wrap in a buffered reader so we can parse it
                in = new BufferedReader(new InputStreamReader(fileIn));
                
                // Populate our list of cities from the file
                List<City> loadedCities = new ArrayList<City>();
                String read;
                while ((read = in.readLine()) != null) {
                    loadedCities.add(parseCity(read));
                }
                
                return loadedCities;
            }
        }catch (Exception failedRead) {
            failedRead.printStackTrace();
        }finally {
            // Close the stream if we can
            try{
                if (in != null) {
                    in.close();
                }
            }catch (Exception failedClose) {
                failedClose.printStackTrace();
            }
        }
        
        // Return an informative list if something went wrong in the process
		City errorCity = new City();
		errorCity.setName("Error Loading City List");
		errorCity.setCountry("ERROR");
        List<City> errorReturn = new ArrayList<City>(1);
        errorReturn.add(errorCity);
        return errorReturn;
	}
	
	private static City parseCity(String line) {
		City city = new City();
		
		StringTokenizer st = new StringTokenizer(line, ";");
		if (st.countTokens() == 6) {
			st.nextToken();
			city.setCountry(st.nextToken().replace("\"", ""));
			city.setName(st.nextToken().replace("\"", ""));
		}
		
		return city;
	}
	
	public static class City implements Serializable {

		private String name;
		private String country;
		
		public String getName() {
			return this.name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getCountry() {
			return this.country;
		}
		
		public void setCountry(String country) {
			this.country = country;
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof City)) return false;
			City other = (City) o;
			if (this.name != null && this.name.equals(other.name)
				&& this.country != null && this.country.equals(other.country))
				return true;
			return false;
		}
	}
}