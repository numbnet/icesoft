/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.samples.spring;

import java.util.Map;

public class ModelBean {
	
	private String name = " ";
	
	private Map<String, String> additionalInfo;

    public String getName() {
        return name;
	}

	public void setName(String name) {
	    this.name = name != null ?  name.trim() : " ";
	}


	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("properties name=");
        if (name != null) {
        	sb.append("'").append(name).append("', ");
        } else {
        	sb.append(name).append(", ");
        }
        sb.append("additionalInfo=").append(additionalInfo);
        return sb.toString();
    }
}
