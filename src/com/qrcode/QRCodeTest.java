package com.qrcode;

import java.io.File;

/**
 * 二维码生成测试类
 * @author Cloud
 * @data   2016-11-21
 * QRCodeTest
 */
 
public class QRCodeTest {
 
    public static void main(String[] args) throws Exception {
        /**
         *    QRcode 二维码生成测试
         *    QRCodeUtil.QRCodeCreate("http://blog.csdn.net/u014266877", "E://qrcode.jpg", 15, "E://icon.png");
         */
    	QRCodeUtil.QRCodeCreate("中文-开源中国", "E://qrcode.png", 15, "E://icon.jpg");
        /**
         *     QRcode 二维码解析测试
         *    String qrcodeAnalyze = QRCodeUtil.QRCodeAnalyze("E://qrcode.jpg");
         */
    	String qrcodeAnalyze = QRCodeUtil.QRCodeAnalyze("E://qrcode.png");
    	System.out.println("read content:"+qrcodeAnalyze);
        /**
         * ZXingCode 二维码生成测试
         * QRCodeUtil.zxingCodeCreate("http://blog.csdn.net/u014266877", 300, 300, "E://zxingcode.jpg", "jpg");
         */
    	QRCodeUtil.zxingCodeCreate("http://blog.csdn.net/u014266877", 300, 300, "E://zxingcode.jpg", "jpg");
        /**
         *    ZxingCode 二维码解析
         *    String zxingAnalyze =  QRCodeUtil.zxingCodeAnalyze("E://zxingcode.jpg").toString();
         */
    	String zxingAnalyze =  QRCodeUtil.zxingCodeAnalyze("E://zxingcode.jpg").toString();
    	System.out.println("read content:"+zxingAnalyze);
        System.out.println("success");
        
        /**
         *    ZxingCode 条形码生成
         */
        File file = new File("E://test.png");
        // 生成一维码  
        QRCodeUtil.getBarcodeWriteFile("6940577300018", 215, 30, file,"png");  
  
        System.out.println("-----成生成功----");  
        /**
         *    ZxingCode 条形码解析
         */
        String jxr = QRCodeUtil.read(file);
        System.out.println("-----解析成功----");  
        System.out.println(jxr);
        
        /**
         * ZxingCode 生成带图片、文字的二维码
         */
        //logo
  		File logoFile = new File("E://icon.jpg");
  		//背景图片
  		File bgFile = new File("E://background.png");
        //生成图片
  		File codeFile = new File("E://output001.png");
  		//二维码内容
  		//String url = "https://w.url.cn/s/AYmfAV3";
  		String url = "fsi words";
  		//二维码下面的文字
  		String words = "PSD000001";
        QRCodeUtil.drawLogoQRCode(logoFile,bgFile,codeFile, url, "test word", 215, 215);
        /**
         * ZxingCode 带图片、文字的二维码解析
         */
        String readZxing = QRCodeUtil.read(new File("E://output001.png"));
        System.out.println("readZxing:"+readZxing);
    }
}
