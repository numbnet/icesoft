package org.icemobile.samples.spring;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtils {

    /**
     * Save an uploaded image file to the media directory. The file will be saved in jpg format.
     * 
     * @param request The HttpServletRequest
     * @param file The multi-part upload file
     * @param inputFile The file uploaded via the browser input type file (for non-enhanced clients)
     * @return The name of the saved file
     * @throws IOException If the file could not be successfully saved
     */
    public static String saveImage(HttpServletRequest request,
            MultipartFile file, MultipartFile inputFile) throws IOException {

        return saveMedia(request,file,inputFile,"img","jpg");
    }
    
    /**
     * Save an uploaded audio file to the media directory. The file will be saved in m4a format.
     * 
     * @param request The HttpServletRequest
     * @param file The multi-part upload file
     * @param inputFile The file uploaded via the browser input type file (for non-enhanced clients)
     * @return The name of the saved file
     * @throws IOException If the file could not be successfully saved
     */
    public static String saveAudio(HttpServletRequest request,
            MultipartFile file, MultipartFile inputFile) throws IOException {

       return saveMedia(request,file,inputFile,"audio","m4a");
    }

    /**
     * Save an uploaded video file to the media directory. The file will be saved in mp4 format.
     * 
     * @param request The HttpServletRequest
     * @param file The multi-part upload file
     * @param inputFile The file uploaded via the browser input type file (for non-enhanced clients)
     * @return The name of the saved file
     * @throws IOException If the file could not be successfully saved
     */
    public static String saveVideo(HttpServletRequest request,
            MultipartFile file, MultipartFile inputFile) throws IOException {

        return saveMedia(request,file,inputFile,"video","mp4");
    }
    
    private static String saveMedia(HttpServletRequest request,
            MultipartFile file, MultipartFile inputFile, String prefix, String suffix) throws IOException {

        String uuid = Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
        String filename = "media/"+prefix+"-"+uuid+"."+suffix;
        File newFile = new File(request.getServletContext().getRealPath("/" + filename));
        
        if ((null != file) && !file.isEmpty()) {
            file.transferTo(newFile);
        }
        if ((null != inputFile) && !inputFile.isEmpty()) {
            inputFile.transferTo(newFile);
        }

        return filename;
    }
}
