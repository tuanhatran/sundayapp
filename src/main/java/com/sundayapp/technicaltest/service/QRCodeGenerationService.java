package com.sundayapp.technicaltest.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class QRCodeGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeGenerationService.class);

    public BufferedImage generateEAN13BarcodeImage(String barcodeText) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 300, 300);
        } catch (WriterException e) {
            LOGGER.error("Error while trying to generate QRCode with text : " + barcodeText);
        }
        assert bitMatrix != null;
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
