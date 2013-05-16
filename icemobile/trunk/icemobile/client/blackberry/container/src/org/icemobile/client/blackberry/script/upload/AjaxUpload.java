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
package org.icemobile.client.blackberry.script.upload;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.icemobile.client.blackberry.ContainerController;
import org.icemobile.client.blackberry.Logger;
import org.icemobile.client.blackberry.http.HttpRequestHandler;
import org.icemobile.client.blackberry.http.MultipartPostData;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.HttpUtils;
import org.icemobile.client.blackberry.utils.ResultHolder;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import net.rim.device.api.script.ScriptableFunction;
import net.rim.device.api.ui.UiApplication;

/**
 * A fileUpload javascript accessable extension that is AJAX aware.
 */
public class AjaxUpload extends ScriptableFunction {

    private ContainerController mController;
    private HttpRequestHandler mHandler;
    private static int uploadIndex;
    private String START_OF_CDATA = "<![CDATA[";

    public AjaxUpload(ContainerController controller) {

        mController = controller;
        mHandler = new HttpRequestHandler(controller);
    }

    public Object invoke(Object thiz, Object[] args) {

        if (args.length != 2) {
            Logger.ERROR("ajax.upload - wrong number of arguments:" + args.length);
            return Boolean.FALSE;
        }

        final String actionMethod = (String) args[0];
        String serializedForm = (String) args[1];
//        Logger.DEBUG("ajax.upload - actionMethod: " + actionMethod);

        //boolean gotValue = true;
        //String[] result;
        //		MultipartEntity content = new MultipartEntity();
        final MultipartPostData content = new MultipartPostData();
        final long startTime = System.currentTimeMillis();

        try {
//            Logger.DEBUG("ajax.upload - Serialized form:\n" + serializedForm);
            NameValuePair[] params = UploadUtilities.getNameValuePairs(serializedForm, "=", "&");
            //Log.e("ICEutil", "NVP=" + params.length);
            String value;
            for (int i = 0; i < params.length; i++) {
                value = HttpUtils.URLdecode(params[i].getValue());

                if (value.indexOf(".jpg") > -1) {
                    Logger.DEBUG("ajax.upload - image part encountered: " + value);
                    byte[] imageBytes;
                    InputStream theImage;
                    FileConnection fconn = (FileConnection) Connector.open(value);

                    imageBytes = new byte[(int) fconn.fileSize()];
                    theImage = fconn.openInputStream();
                    theImage.read(imageBytes);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(imageBytes.length);
                    byteArrayOutputStream.write(imageBytes);

                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();

                    String name = HttpUtils.URLdecode(params[i].getName());
                    String imageVar = byteArrayOutputStream.toString();
                    content.appendImagePart(name,
                                            value,
                                            imageVar);
                    fconn.close();
                } else if (value.indexOf(".amr") > -1) {
                    Logger.DEBUG("ajax.upload - audio part encountered: " + value);
                    byte[] clipBytes;
                    InputStream theClip;

                    // Audio encoder insists on having "file:// already in the storage location, while image
                    // control can't have it.
                    FileConnection fconn = (FileConnection) Connector.open(value);
                    clipBytes = new byte[(int) fconn.fileSize()];
                    theClip = fconn.openInputStream();
                    theClip.read(clipBytes);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(clipBytes.length);
                    byteArrayOutputStream.write(clipBytes);

                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();

                    String name = HttpUtils.URLdecode(params[i].getName());
                    String audioVar = byteArrayOutputStream.toString();
                    content.appendAudioPart(name,
                                            value,
                                            audioVar);
                    fconn.close();
                } else if (value.indexOf(".3gp") > -1) {
                    Logger.DEBUG("ajax.upload - video part encountered: " + value);
                    byte[] videoBytes;
                    InputStream theVideo;
                    FileConnection fconn = (FileConnection) Connector.open(value);

                    videoBytes = new byte[(int) fconn.fileSize()];
                    theVideo = fconn.openInputStream();
                    theVideo.read(videoBytes);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(videoBytes.length);
                    byteArrayOutputStream.write(videoBytes);

                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();

                    String name = HttpUtils.URLdecode(params[i].getName());
                    String videoVar = byteArrayOutputStream.toString();
                    content.appendVideoPart(name,
                                            value,
                                            videoVar);
                    fconn.close();
                } else {
                    if (params[i].getName() == null || params[i].getName().equals("")) {
                        continue;
                    }
                }
                content.append(HttpUtils.URLdecode(params[i].getName()), HttpUtils.URLdecode(params[i].getValue()));
            }


            final DisposableResultHolder drh = new DisposableResultHolder();
            UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {

                    String response = mHandler.processAJAXRequest(actionMethod, null, content);

                    Logger.TIME(startTime, "ajax.upload FormUpload");
                    Logger.DEBUG("ajax.upload - Raw response: " + response);

                    if (response.indexOf("<error>") > -1) {
                        processError(response);
                    } else {
                        drh.setResult(response);
                        if (response != null) {
                            mController.processResult("AjaxUpload_" + uploadIndex++, drh);
                        }
                    }
                }
            });


        } catch (Exception e) {
            Logger.ERROR("ajax.upload - Upload exception: " + e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void processError(String response) {

        int spos = response.indexOf(START_OF_CDATA);
        String error = null;
        if (spos > -1) {
            spos += START_OF_CDATA.length();
            int epos = response.indexOf("]]>", spos + 1);
            if (epos > -1) {
                error = response.substring(spos, epos);
                Logger.DIALOG("Error in upload: " + error);
            }
        } else {
            Logger.DIALOG("Error in upload... check container log for message contents");
        }
    }

    class DisposableResultHolder implements ResultHolder {

        private String result;

        public void setResult(String result) {
            if (result != null) {
                this.result = new String(result);
            } else {
                Logger.ERROR("Null result in ajaxUpload");
            }
        }

        public String getResult() {
            return this.result;
        }
    }

}
