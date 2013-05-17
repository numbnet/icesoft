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
package org.icemobile.samples.spring.mediacast;

import javax.validation.constraints.Size;

public class UploadForm {
	
	public static final String PAGE_UPLOAD = "upload";
	public static final String PAGE_GALLERY = "gallery";
	public static final String PAGE_VIEWER = "viewer";
	public static final String PAGE_ALL = "all";


	
	@Size(max = 164)
	private String description;
	
	private String page = PAGE_UPLOAD;
	
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = cleanParam(id);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String cleanParam(String param){
		if( param != null && param.indexOf(",") > 0 ){
			param = param.substring(0,param.indexOf(","));
		}
		return param;
	}
	
	private boolean notNullOrEmpty(String param){
		if( param != null && param.length() > 0 ){
			return true;
		}
		return false;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = cleanParam(page);
	}

	@Override
	public String toString() {
		return "UploadForm [description=" + description
				+ ", page=" + page + ", id=" + id  + "]";
	}


}
