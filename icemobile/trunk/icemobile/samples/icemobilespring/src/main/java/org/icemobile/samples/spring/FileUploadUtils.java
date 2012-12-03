package org.icemobile.samples.spring;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtils {

    public static String saveImage(HttpServletRequest request,
            MultipartFile file, MultipartFile inputFile) throws IOException {

        String uuid = Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
        String cameraFilename = "media/img-" + uuid + ".jpg";
        if ((null != file) && !file.isEmpty()) {
            file.transferTo(new File(request.getRealPath("/" + cameraFilename)));
        }
        if ((null != inputFile) && !inputFile.isEmpty()) {
            inputFile.transferTo(new File(request.getRealPath("/"
                    + cameraFilename)));
        }

        return cameraFilename;
    }
    
    public static String saveAudio(HttpServletRequest request,
            MultipartFile file, MultipartFile inputFile) throws IOException {

        String uuid = Long.toString(
                Math.abs(UUID.randomUUID().getMostSignificantBits()), 32);
        String audioFilename = "media/audio-" + uuid + ".m4a";
        if ((null != file) && !file.isEmpty()) {
            file.transferTo(new File(request.getRealPath("/" + audioFilename)));
        }
        if ((null != inputFile) && !inputFile.isEmpty()) {
            inputFile.transferTo(new File(request.getRealPath("/"
                    + audioFilename)));
        }

        return audioFilename;
    }


}
