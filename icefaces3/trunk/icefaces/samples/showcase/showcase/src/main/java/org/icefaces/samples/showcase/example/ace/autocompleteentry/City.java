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

public class City implements java.io.Serializable {

	private String name;
	private String country;
	private int latitude;
	private int longitude;
	private int altitude;
	
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
	
	public int getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	
	public int getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	
	public int getAltitude() {
		return this.altitude;
	}
	
	public void setAltitude(int altitude) {
		this.altitude = altitude;
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