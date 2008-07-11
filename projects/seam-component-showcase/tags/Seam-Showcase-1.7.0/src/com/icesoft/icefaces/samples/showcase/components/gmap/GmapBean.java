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
package com.icesoft.icefaces.samples.showcase.components.gmap;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.icesoft.faces.component.gmap.GMapLatLng;
import java.io.Serializable;

@Scope(ScopeType.PAGE)
@Name("gmap2")
public class GmapBean implements Serializable{
	private String geoCoderAddress="";
	private String address="";
	private boolean locateAddress = false;
	private List points = new ArrayList();
	
	
	@Create
	public void init(){
		points.add(new GMapLatLng("37.379434", "-121.92293"));
		points.add(new GMapLatLng("33.845449", "-84.368682"));
		points.add(new GMapLatLng("34.05333", "-118.24499"));
		points.add(new GMapLatLng("33.072694", "-97.06234"));
        points.add(new GMapLatLng("37.391278", "-121.952451"));
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void enterKeyPressed(ActionEvent event) {
		locateAddress = true;
	}
	
	
	public boolean isLocateAddress() {
		if (locateAddress) {
			locateAddress = false;
			return true;
		}
		return false;
	}
	
	public void findAddress(ValueChangeEvent event) {
		locateAddress = true;
	}

	public List getPoints() {
		return points;
	}

	public void setPoints(List points) {
		this.points = points;
	}

	public String getGeoCoderAddress() {
		return geoCoderAddress;
	}

	public void setGeoCoderAddress(String geoCoderAddress) {
		this.geoCoderAddress = geoCoderAddress;
	}
	
	@Destroy
	public void destroy(){

	}
}
