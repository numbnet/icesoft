/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.examples.input.qrcode;


import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * This example Bean shows how to use both the QR code reader and generator
 * tags.<p/>
 * <b>mobi:scan</b> When mobile container is present a button will be rendered
 * and when pressed will activate the devices camera.  If a valid QR can be
 * scanned then the decode text is stored in the variable qrScanner .
 * <p/>
 * <b>mobi:qrcode</b>The qrcode component is used to generate a QR code given
 * an input string.  The string is converted into a valid QR graphic image.
 */
@Destination(
        title = "example.input.input.destination.title.short",
        titleExt = "example.input.input.destination.title.long",
        titleBack = "example.input.input.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/input/qrcode.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/qrcode-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/qrcode-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "qrcode-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/qrcode-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "QrCodeBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/input/QrCodeBean.java")
        }
)

@ManagedBean(name = QrCodeBean.BEAN_NAME)
@SessionScoped
public class QrCodeBean extends ExampleImpl<QrCodeBean> implements
        Serializable {

    public static final String BEAN_NAME = "qrCodeBean";

    // echo string for qr image scanner
    private String qrScanner;
    private boolean qrScannerUrl;
    // input string for qr image generation.
    private String qrString = "ICEmobile";

    public QrCodeBean() {
        super(QrCodeBean.class);
    }

    /**
     * Checks to see if the decoded qrScanner value is a url.
     *
     * @param event jsf action event
     */
    public void checkCodeForUrl(ActionEvent event) {
        if (qrScanner != null){
            qrScannerUrl = qrScanner.startsWith("http://");
        }else{
            qrScannerUrl = false;
            qrScanner = "Error decoding.";
        }
    }

    public boolean isQrScannerUrl() {
        return qrScannerUrl;
    }

    public void setQrScannerUrl(boolean qrScannerUrl) {
        this.qrScannerUrl = qrScannerUrl;
    }

    public String getQrScanner() {
        return qrScanner;
    }

    public void setQrScanner(String qrScanner) {
        this.qrScanner = qrScanner;
    }

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }
}
