/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */
package org.icefaces.application.showcase.view.bean.examples.component.outputResource;

import com.icesoft.faces.context.Resource;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name = "resourceBean")
@ViewScoped
public class OutputResourceBean implements Serializable {

    private Resource imgResource;
    private Resource pdfResource;
    private Resource pdfResourceDynFileName;
    private String fileName = "Choose-a-new-file-name";
    public static final String RESOURCE_PATH = "/WEB-INF/classes/org/icefaces/application/showcase/view/resources/";


	public OutputResourceBean(){
		try{
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            imgResource = new MyResource(ec,"logo.jpg");
			pdfResource =  new MyResource(ec,"WP_Security_Whitepaper.pdf");
			pdfResourceDynFileName = new MyResource(ec,"WP_Security_Whitepaper.pdf");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

    public Resource getImgResource() {
        return imgResource;
    }


    public Resource getPdfResource() {
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

class MyResource implements Resource, Serializable {
    private String resourceName;
    private InputStream inputStream;
    private final Date lastModified;
    private ExternalContext extContext;

    public MyResource(ExternalContext ec, String resourceName) {
        this.extContext = ec;
        this.resourceName = resourceName;
        this.lastModified = new Date();
    }

    /**
     * This intermediate step of reading in the files from the JAR, into a
     * byte array, and then serving the Resource from the ByteArrayInputStream,
     * is not strictly necessary, but serves to illustrate that the Resource
     * content need not come from an actual file, but can come from any source,
     * and also be dynamically generated. In most cases, applications need not
     * provide their own concrete implementations of Resource, but can instead
     * simply make use of com.icesoft.faces.context.ByteArrayResource,
     * com.icesoft.faces.context.FileResource, com.icesoft.faces.context.JarResource.
     */
    public InputStream open() throws IOException {
        if (inputStream == null) {
            InputStream stream = extContext.getResourceAsStream(OutputResourceBean.RESOURCE_PATH + resourceName);
            byte[] byteArray = OutputResourceBean.toByteArray(stream);
            inputStream = new ByteArrayInputStream(byteArray);
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


