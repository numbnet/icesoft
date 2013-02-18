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

package org.icemobile.samples.spring.controllers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.samples.spring.QRScanBean;
import org.icemobile.spring.controller.ICEmobileBaseController;
import org.icemobile.zxing.BarcodeFormat;
import org.icemobile.zxing.common.BitMatrix;
import org.icemobile.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Session Controller for echoing simple input pages
 */
@Controller
@SessionAttributes("QRScanBean")
public class EchoSessionController extends ICEmobileBaseController {

private static Logger LOG = Logger.getLogger(EchoSessionController.class.getName());

    private String PLAINTEXT_DEFAULT = "Is there in truth, no beauty?";
    private final String QRCODE_TOKEN = "/qrcode:";


    @ModelAttribute("QRScanBean")
    public QRScanBean initQRScanBean() {
        return new QRScanBean();
    }

    @RequestMapping(value = "/qrscan")
    public void doRequest(
        @ModelAttribute("QRScanBean") QRScanBean model) {
    }

    @RequestMapping(value = "/qrcode:{theMessage}")
    public void doImageRequest( HttpServletResponse response, @PathVariable String theMessage,
                                HttpServletRequest request,
                                @ModelAttribute("QRScanBean") QRScanBean model) {

        // Ideally, theMessage would be what we want, but Spring is cutting off
        // portions of this string if the String contains multiple periods "."
        String path = request.getRequestURI();
        String plaintext = PLAINTEXT_DEFAULT;
        int spos = path.indexOf(QRCODE_TOKEN);
        if (spos > 0) {
            plaintext = path.substring(spos + QRCODE_TOKEN.length());
            plaintext = URLDecoder.decode(plaintext);
        }
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

