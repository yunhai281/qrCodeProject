package com.qrcode;

import java.awt.image.BufferedImage;

import jp.sourceforge.qrcode.data.QRCodeImage;

public class ImageUtil implements QRCodeImage {

	BufferedImage bufferedImage;

	public ImageUtil(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
	public int getHeight() {
        return bufferedImage.getHeight();
    }

    public int getPixel(int arg0, int arg1) {
        return bufferedImage.getRGB(arg0, arg1);
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

}
