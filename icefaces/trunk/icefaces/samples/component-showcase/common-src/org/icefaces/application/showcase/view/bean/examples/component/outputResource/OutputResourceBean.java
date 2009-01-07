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
package org.icefaces.application.showcase.view.bean.examples.component.outputResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import com.icesoft.faces.context.Resource;

public class OutputResourceBean{
	
	private Resource imgResource;
	private Resource pdfResource;
	private Resource pdfResourceDynFileName;
	private String fileName = "Choose-a-new-file-name";
	public static final String RESOURCE_PATH = "org/icefaces/application/showcase/view/resources/";
		

	public OutputResourceBean(){
		try{
			imgResource = new MyResource("logo.jpg");
			pdfResource =  new MyResource("WP_Security_Whitepaper.pdf");
			pdfResourceDynFileName = new MyResource("WP_Security_Whitepaper.pdf");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public Resource getImgResource() {
		return imgResource;
	}

	
	public Resource getPdfResource(){
		return pdfResource;
	}
	   
	public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
        return output.toByteArray();
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Resource getPdfResourceDynFileName() {
		return pdfResourceDynFileName;
	}

	public void setPdfResourceDynFileName(Resource pdfResourceDynFileName) {
		this.pdfResourceDynFileName = pdfResourceDynFileName;
	}
}

class MyResource implements Resource, Serializable{
    private String resourceName;
    private InputStream inputStream;
    private final Date lastModified;
    public MyResource(String resourceName) {
        this.resourceName = resourceName;
        this.lastModified = new Date();        
    }
    public InputStream open() throws IOException {
        if (inputStream == null) {
            inputStream = new ByteArrayInputStream( OutputResourceBean.toByteArray( Thread.currentThread().getContextClassLoader().getResourceAsStream(OutputResourceBean.RESOURCE_PATH + resourceName)));
        }
        return inputStream;
    }
    public String calculateDigest() {
        return resourceName;
    }
    public Date lastModified() {
        return lastModified;
    }
    public void withOptions(Options arg0) throws IOException {
    }
}   


