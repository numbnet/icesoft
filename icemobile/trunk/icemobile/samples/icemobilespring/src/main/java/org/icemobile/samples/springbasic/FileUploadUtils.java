package org.icemobile.samples.springbasic;

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

}
