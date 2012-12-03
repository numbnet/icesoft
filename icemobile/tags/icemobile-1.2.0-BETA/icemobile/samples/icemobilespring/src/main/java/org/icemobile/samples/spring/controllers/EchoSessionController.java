
package org.icemobile.samples.spring.controllers;

import org.icemobile.samples.spring.QRScanBean;
import org.icemobile.zxing.qrcode.QRCodeWriter;
import org.icemobile.zxing.common.BitMatrix;
import org.icemobile.zxing.BarcodeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Session Controller for echoing simple input pages
 */
@Controller
@SessionAttributes("QRScanBean")
public class EchoSessionController extends BaseController {

private static Logger LOG = Logger.getLogger(EchoSessionController.class.getName());

     private String PLAINTEXT_DEFAULT = "Is there in truth, no beauty?";


    @ModelAttribute("QRScanBean")
    public QRScanBean initQRScanBean() {
        return new QRScanBean();
    }

    @RequestMapping(value = "/qrscan")
    public void doRequest(
        @ModelAttribute("QRScanBean") QRScanBean model) {
    }

    @RequestMapping(value = "/qrscan/theImage")
    public void doImageRequest( HttpServletResponse response,
                                @ModelAttribute("QRScanBean") QRScanBean model) {

        String plaintext = model.getPlaintextOne();
        if (plaintext == null || "".equals(plaintext)) {
            plaintext = PLAINTEXT_DEFAULT;
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            writeQRToStream(plaintext, out);

        } catch (IOException ioe) {
            LOG.severe("IOException writing QRCode to stream");
        }
    }

    private void writeQRToStream(String data, OutputStream out)  {
        try {
            int bigEnough = 80;
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(data,
                                             BarcodeFormat.QR_CODE, bigEnough,
                                             bigEnough, null);

            int width = 160, height = 160;

            BufferedImage bi = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D ig2 = bi.createGraphics();
            ig2.setColor(Color.WHITE);
            ig2.fillRect(0, 0, width, height);
            ig2.setColor(Color.BLACK);
            int d = width / bigEnough;
            int bitWidth = matrix.getWidth();
            int bitHeight = matrix.getHeight();
            for (int i = 0; i < bitHeight; i++)  {
                for (int j = 0; j < bitWidth; j++)  {
                    boolean isSet = matrix.get(j, i);
                    if (isSet)  {
                        ig2.fillRect(d * j, d * i, d, d);
                    }                 }
            }
            ImageIO.write(bi, "PNG", out);

        } catch (Exception e)  {
            LOG.log(Level.WARNING, "Error create QR Code image", e);
        }
    }
}

