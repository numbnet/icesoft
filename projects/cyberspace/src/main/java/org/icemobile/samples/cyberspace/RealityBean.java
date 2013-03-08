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

package org.icemobile.samples.cyberspace;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.icefaces.application.ResourceRegistry;

@ManagedBean(name = RealityBean.BEAN_NAME)
@ApplicationScoped
public class RealityBean implements Serializable {

    private static final Logger logger =
            Logger.getLogger(RealityBean.class.toString());

    public static final String BEAN_NAME = "realityBean";

    static String TEMP_DIR = "javax.servlet.context.tmpdir";
    public static final String FILE_KEY = "file";
    public static final String RELATIVE_PATH_KEY = "relativePath";
    public static final String CONTENT_TYPE_KEY = "contentType";

    private Map cameraImage = new HashMap();
    private File cameraFile;
    // uploaded video will be stored as a resource.
    private Resource outputResource;
    private String resourcePath;
    private String imagePath;
    private String selection;
    private String label;
    private double latitude = 0.0;
    private double longitude = 0.0;
    static int THUMBSIZE = 128;
    HashMap<String,HashMap> markerIndex = new HashMap();;
    List<HashMap> markerList;
    List<HashMap> selectedMarkers;
    private String selectedModel1 = "ICEmobile";
    private String selectedModel2 = "ICEfaces";
    private String selectedModel3 = "Puzzle1";
    private String selectedModel4 = "Puzzle2";

    // upload error message
    private String uploadMessage;

    public RealityBean() {
        markerList = new ArrayList();

        HashMap marker = new HashMap();
        marker.put("label", "Puzzle1");
        marker.put("model", getContextURL() + 
                "/resources/3d/puz1.obj" );
        markerList.add(marker);

        marker = new HashMap();
        marker.put("label", "Puzzle2");
        marker.put("model", getContextURL() +
                "/resources/3d/puz2.obj" );
        markerList.add(marker);

        marker = new HashMap();
        marker.put("label", "ICEmobile");
        marker.put("model", getContextURL() +
                "/resources/3d/icemobile.obj" );
        markerList.add(marker);

        marker = new HashMap();
        marker.put("label", "ICEfaces");
        marker.put("model", getContextURL() +
                "/resources/3d/icefaces.obj" );
        markerList.add(marker);

        for (HashMap theMarker : markerList)  {
            markerIndex.put((String) theMarker.get("label"), theMarker);
        }

        selectedMarkers = new ArrayList();
        selectedMarkers.add(markerIndex.get(selectedModel1));
        selectedMarkers.add(markerIndex.get(selectedModel2));
        selectedMarkers.add(markerIndex.get(selectedModel3));
        selectedMarkers.add(markerIndex.get(selectedModel4));
System.out.println("Constructed markerList " + markerList);
    }


    @PreDestroy
    public void disposeResources(){
        if( cameraFile != null ){
            boolean success = cameraFile.delete();
            if (!success && logger.isLoggable(Level.FINE)){
                logger.fine("Could not dispose of media file" + cameraFile.getAbsolutePath());
            }
        }
    }

    public String upload()  {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            Part objPart = request.getPart("objfile");
            File partFile = writePart(facesContext, objPart);
            File newFile = getTempFile(facesContext, ".obj");
            Format3dTransformer.simplifyMesh(partFile, newFile);
            String dirPath = facesContext.getExternalContext()
                            .getRealPath("/resources/3d") + "/";
            String newFileName = newFile.getName();
            String newPath = dirPath + newFileName;
            newFile.renameTo(new File(newPath));

System.out.println("stored model " + label + " " + newFile);
        HashMap marker = new HashMap();
        marker.put("label", label);
        marker.put("model", getContextURL() +
                "/resources/3d/" + newFileName );
        markerList.add(marker);
        markerIndex.put(label, marker);
        label = "";
//            for (Part part : request.getParts())  {
//            
//                System.out.println("Request " + part.getName() + " " + part.getContentType() + " " + part.getSize());
//                if ( "application/octet-stream".equals(part.getContentType()) )  {
//                    File partFile = writePart(facesContext, part);
//                    File newFile = getTempFile(facesContext);
//                    Format3dTransformer.simplifyMesh(partFile, newFile);
//System.out.println("Simplified mesh " + newFile.getAbsolutePath());
//                }
//            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        return "";
    }

    File writePart(FacesContext facesContext, Part part) 
            throws IOException {
        File tempFile = getTempFile(facesContext);
        FileOutputStream tempStream = new FileOutputStream(tempFile);

        InputStream partStream = part.getInputStream();
        copyStream(partStream, tempStream);

        return tempFile;
    }

    File getTempFile(FacesContext facesContext, String extension)
            throws IOException {
        File tempDir = (File) ( facesContext.getExternalContext()
                .getApplicationMap().get(TEMP_DIR) );
        File tempFile = File.createTempFile("ice", extension, tempDir);
        return tempFile;
    }

    File getTempFile(FacesContext facesContext) throws IOException {
        return getTempFile(facesContext, ".tmp");
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1000];
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                out.write(buf, 0, l);
            }
        }
    }

    public void setCameraImage(Map cameraImage) {

        this.cameraImage = cameraImage;

    }

    public Map getCameraImage() {
        return cameraImage;
    }

    public String getImage() {
        return imagePath;
    }

    public String getUploadMessage() {
        return uploadMessage;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setSelection(String selection)  {
        this.selection = selection;
    }

    public String getSelection()  {
        return selection;
    }

    public String getSelectedModel1() {
        return selectedModel1;
    }

    public void setSelectedModel1(String selectedModel) {
        this.selectedModel1 = selectedModel;
        HashMap marker = markerIndex.get(selectedModel);
        selectedMarkers.set(0, marker);
    }

    public String getSelectedModel2() {
        return selectedModel2;
    }

    public void setSelectedModel2(String selectedModel) {
        this.selectedModel2 = selectedModel;
        HashMap marker = markerIndex.get(selectedModel);
        selectedMarkers.set(1, marker);
    }

    public String getSelectedModel3() {
        return selectedModel3;
    }

    public void setSelectedModel3(String selectedModel) {
        this.selectedModel3 = selectedModel;
        HashMap marker = markerIndex.get(selectedModel);
        selectedMarkers.set(2, marker);
    }

    public String getSelectedModel4() {
        return selectedModel4;
    }

    public void setSelectedModel4(String selectedModel) {
        this.selectedModel4 = selectedModel;
        HashMap marker = markerIndex.get(selectedModel);
        selectedMarkers.set(3, marker);
    }

    public List getMarkers()  {
        return markerList;
    }

    public List getSelectedMarkers()  {
        return selectedMarkers;
    }

    public String getBaseURL()  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String serverName = externalContext.getRequestHeaderMap()
                .get("x-forwarded-host");
        if (null == serverName) {
            serverName = externalContext.getRequestServerName() + ":" + 
            externalContext.getRequestServerPort();
        }
        String url = externalContext.getRequestScheme() + "://" + serverName;
        return url;
    }

    public String getContextURL()  {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        return getBaseURL() + "/" + externalContext.getRequestContextPath();
    }

    private void scaleImage(File photoFile) throws IOException  {

        if (null == photoFile) {
            return;
        }

        BufferedImage image = ImageIO.read(photoFile);
        // scale the original file into a small thumbNail and the other
        // into a 1 megapixelish sized image.
        int width = image.getWidth();
        int height = image.getHeight();

        // create the thumbnail
        AffineTransform tx = new AffineTransform();
        //default image type creates nonstandard all black jpg file
        BufferedImage thumbNailImage = 
                new BufferedImage(THUMBSIZE, THUMBSIZE, 
                        BufferedImage.TYPE_3BYTE_BGR);
        double imageScale = calculateImageScale(THUMBSIZE, width, height);
        tx.scale(imageScale, imageScale);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        op.filter(image, thumbNailImage);

        // clean up the original image.
        image.flush();

        writeImage(thumbNailImage, photoFile);

    }

    private double calculateImageScale(double intendedSize, int width, int height) {
        double scaleHeight = height / intendedSize;
        // change the algorithm, so height is always the same
        return 1 / scaleHeight;
    }

    private void writeImage(BufferedImage image, File imageFile)
            throws IOException {
        FileOutputStream fs = new FileOutputStream(imageFile);
        ImageIO.write(image, "jpg", fs);
        fs.close();
    }
    
}

