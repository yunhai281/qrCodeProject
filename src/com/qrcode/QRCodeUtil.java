package com.qrcode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
 
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.swetake.util.Qrcode;
 
/**
 * 二维码生成工具类，使用QRCode和zxing，js可以使用jQuery-qrcode生成二维码
 * @author Cloud
 * @data   2016-12-15
 * QRCode
 */
 
public class QRCodeUtil {
	
    //二维码颜色
    private static final int BLACK = 0xFF000000;
    //二维码颜色
    private static final int WHITE = 0xFFFFFFFF;
    
    private static final int WORDHEIGHT = 235; // 加文字二维码高
 
    /**
     * <span style="font-size:18px;font-weight:blod;">ZXing 方式生成二维码</span>
     * @param text    <a href="javascript:void();">二维码内容</a>
     * @param width    二维码宽
     * @param height    二维码高
     * @param outPutPath    二维码生成保存路径
     * @param imageType        二维码生成格式
     */
    public static void zxingCodeCreate(String text, int width, int height, String outPutPath, String imageType){
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            //1、生成二维码
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);
            
            //2、获取二维码宽高
            int codeWidth = encode.getWidth();
            int codeHeight = encode.getHeight();
            
            //3、将二维码放入缓冲流
            BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < codeWidth; i++) {
                for (int j = 0; j < codeHeight; j++) {
                    //4、循环将二维码内容定入图片
                    image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
                }
            }
            
            //image = insertWords(image, "测试显示文字", 215, 215);
            
            image.flush();
            
            
            File outPutImage = new File(outPutPath);
            //如果图片不存在创建图片
            if(!outPutImage.exists())
                outPutImage.createNewFile();
            //5、将二维码写入图片
            ImageIO.write(image, imageType, outPutImage);
        } catch (WriterException e) {
            e.printStackTrace();
            System.out.println("二维码生成失败");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("生成二维码图片失败");
        }
    }
    
    /**
     * <span style="font-size:18px;font-weight:blod;">二维码解析</span>
     * @param analyzePath    二维码路径
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object zxingCodeAnalyze(String analyzePath) throws Exception{
        MultiFormatReader formatReader = new MultiFormatReader();
        Object result = null;
        try {
            File file = new File(analyzePath);
            if (!file.exists())
            {
                return "二维码不存在";
            }
            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new LuminanceSourceUtil(image);
            Binarizer binarizer = new HybridBinarizer(source);  
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            result = formatReader.decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }  
        return result;
    }
    
    /**
     * <span style="font-size:18px;font-weight:blod;">QRCode 方式生成二维码</span>
     * @param content    二维码内容
     * @param imgPath    二维码生成路径
     * @param version    二维码版本
     * @param isFlag    是否生成Logo图片    为NULL不生成
     */
    public static void QRCodeCreate(String content, String imgPath, int version, String logoPath){
         try {  
            Qrcode qrcodeHandler = new Qrcode();  
            //设置二维码排错率，可选L(7%) M(15%) Q(25%) H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小    
            qrcodeHandler.setQrcodeErrorCorrect('M');  
            //N代表数字,A代表字符a-Z,B代表其他字符  
            qrcodeHandler.setQrcodeEncodeMode('B');  
            //版本1为21*21矩阵，版本每增1，二维码的两个边长都增4；所以版本7为45*45的矩阵；最高版本为是40，是177*177的矩阵  
            qrcodeHandler.setQrcodeVersion(version);
            //根据版本计算尺寸
            int imgSize = 67 + 12 * (version - 1) ;  
            byte[] contentBytes = content.getBytes("utf-8");  
            BufferedImage bufImg = new BufferedImage(imgSize , imgSize ,BufferedImage.TYPE_INT_RGB);  
            Graphics2D gs = bufImg.createGraphics();  
            gs.setBackground(Color.WHITE);  
            gs.clearRect(0, 0, imgSize , imgSize);
            // 设定图像颜色 > BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量 不设置可能导致解析出错  
            int pixoff = 2;
            // 输出内容 > 二维码  
            if (contentBytes.length > 0 && contentBytes.length < 130) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }  
                    }  
                }  
            } else {  
                System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,130 ]. ");  
            }
           /* 判断是否需要添加logo图片 */
            if(logoPath != null){
                File icon = new File(logoPath);
                if(icon.exists()){
                    int width_4 = imgSize / 4;
                    int width_8 = width_4 / 2;
                    int height_4 = imgSize / 4;
                    int height_8 = height_4 / 2;
                    Image img = ImageIO.read(icon);
                    gs.drawImage(img, width_4 + width_8, height_4 + height_8,width_4,height_4, null);
                    gs.dispose();
                    bufImg.flush();
                }else{
                    System.out.println("Error: login图片不存在！");
                }
 
            }
            gs.dispose();
            bufImg.flush();
            //创建二维码文件
            File imgFile = new File(imgPath);
            if(!imgFile.exists())
                imgFile.createNewFile();
            //根据生成图片获取图片
            String imgType = imgPath.substring(imgPath.lastIndexOf(".") + 1, imgPath.length());
            // 生成二维码QRCode图片  
            ImageIO.write(bufImg, imgType, imgFile);  
         } catch (Exception e) {  
             e.printStackTrace();  
         }
    }
    
    /**
     * <span style="font-size:18px;font-weight:blod;">QRCode二维码解析</span>
     * @param codePath    二维码路径
     * @return    解析结果
     */
    public static String QRCodeAnalyze(String codePath) {
        File imageFile = new File(codePath);
        BufferedImage bufImg = null;  
        String decodedData = null;  
        try {
            if(!imageFile.exists())
                return "二维码不存在";
            bufImg = ImageIO.read(imageFile);
          
            QRCodeDecoder decoder = new QRCodeDecoder();  
            decodedData = new String(decoder.decode(new ImageUtil(bufImg)), "utf-8");  
        } catch (IOException e) {  
            System.out.println("Error: " + e.getMessage());  
            e.printStackTrace();  
        } catch (DecodingFailedException dfe) {  
            System.out.println("Error: " + dfe.getMessage());  
            dfe.printStackTrace();  
        }
        return decodedData;
    }
    
    /** 
     * 生成一维码，写到文件中 
     * @param content 
     * @param width
     * @param height
     * @param file
     * @param imageType
     * @throws IOException
     */
    public static void getBarcodeWriteFile(String content, Integer width,  
                                           Integer height, File file, String imageType) throws IOException {  
        BufferedImage image = getBarcode(content, width, height);  
        ImageIO.write(image, imageType, file);  
    }
    
    /**
     * 生成一维码（128） 
     * @param content
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage getBarcode(String content, Integer width,  
                                           Integer height) {  
  
        if (width == null || width < 200) {  
            width = 200;  
        }  
  
        if (height == null || height < 50) {  
            height = 50;  
        }  
  
        try {  
            // 文字编码  
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
  
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
                    BarcodeFormat.CODE_128, width, height, hints);  
  
            return toBufferedImage(bitMatrix);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
 
    /** 
     * 转换成图片 
     * 
     * @param matrix 
     * @return 
     * @author wuhongbo 
     */  
    private static BufferedImage toBufferedImage(BitMatrix matrix) {  
        int width = matrix.getWidth();  
        int height = matrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_ARGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);  
            }  
        }
        //image=insertWords(image,"1234",215,30);
        return image;  
    }  
    
    /** 
     * 解码(二维、一维均可) 
     */  
    public static String read(File file) {  
  
        BufferedImage image;  
        try {  
            if (file == null || file.exists() == false) {  
                throw new Exception(" File not found:" + file.getPath());  
            }  
  
            image = ImageIO.read(file);  
  
            LuminanceSource source = new BufferedImageLuminanceSource(image);  
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
  
            Result result;  
  
            // 解码设置编码方式为：utf-8，  
            Hashtable hints = new Hashtable();  
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");  
  
            result = new MultiFormatReader().decode(bitmap, hints);  
  
            return result.getText();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }
    
    public static void drawLogoQRCode(File logoFile,File bgFile,File codeFile, String qrUrl, String words,int width,int height) {
        try {
        	Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        	      private static final long serialVersionUID = 1L;
        	      {
        	         // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        	         put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        	         // 设置编码方式
        	         put(EncodeHintType.CHARACTER_SET, "utf-8");
        	         put(EncodeHintType.MARGIN, 0);
        	      }
        	   };
        	MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
 
            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
 
            //把logo画到二维码上面
            if (Objects.nonNull(logoFile) && logoFile.exists()) {
                // 构建绘图对象
                Graphics2D g = image.createGraphics();
                setGraphics2D(g);
                // 读取Logo图片
                BufferedImage logo = ImageIO.read(logoFile);
                // 开始绘制logo图片 等比缩放：（width * 2 / 10 height * 2 / 10）不需缩放直接使用图片宽高
                //width * 2 / 5 height * 2 / 5  logo绘制在中心点位置
                g.drawImage(logo, width * 2 / 5, height * 2 / 5, logo.getWidth(), logo.getHeight(), null);
                g.dispose();
                logo.flush();
            }
 
            // 新的图片，把带logo的二维码下面加上文字
            image=insertWords(image,words,215,215);
 
            image.flush();
            ImageIO.write(image, "png", codeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
    
    /**
     * 把带logo的二维码下面加上文字
     * @param image
     * @param words
     * @return
     */
    private static BufferedImage insertWords(BufferedImage image,String words,int width,int height){
       // 新的图片，把带logo的二维码下面加上文字
       if (org.apache.commons.lang.StringUtils.isNotEmpty(words)) {
  
          //创建一个带透明色的BufferedImage对象
          BufferedImage outImage = new BufferedImage(width, WORDHEIGHT, BufferedImage.TYPE_INT_ARGB);
          Graphics2D outg = outImage.createGraphics();
          setGraphics2D(outg);
  
          // 画二维码到新的面板
          outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
          // 画文字到新的面板
          Color color=new Color(183,183,183);
          outg.setColor(color);
          // 字体、字型、字号
          outg.setFont(new Font("微软雅黑", Font.PLAIN, 18));
          //文字长度
          int strWidth = outg.getFontMetrics().stringWidth(words);
          //总长度减去文字长度的一半  （居中显示）
          int wordStartX=(width - strWidth) / 2;
          //height + (outImage.getHeight() - height) / 2 + 12
          int wordStartY=height+10;
          // 画文字
          outg.drawString(words, wordStartX, wordStartY);
          outg.dispose();
          outImage.flush();
          return outImage;
  
             /*if (strWidth > 399) {
                 // 长度过长就截取前面部分 然后换行
                 String note1 = note.substring(0, note.length() / 2);
                 String note2 = note.substring(note.length() / 2, note.length());
                 int strWidth1 = outg.getFontMetrics().stringWidth(note1);
                 int strWidth2 = outg.getFontMetrics().stringWidth(note2);
                 outg.drawString(note1, 200 - strWidth1 / 2, height + (outImage.getHeight() - height) / 2 + 12);
                 BufferedImage outImage2 = new BufferedImage(1000, 1120, BufferedImage.TYPE_4BYTE_ABGR);
                 Graphics2D outg2 = outImage2.createGraphics();
                 outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                 outg2.setColor(Color.BLACK);
            // 字体、字型、字号
                 outg2.setFont(new Font("宋体", Font.BOLD, 30));
            // 画文字
                 outg2.drawString(note2, 200 - strWidth2 / 2,outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
                 outg2.dispose();
                 outImage2.flush();
                 outImage = outImage2;
             } */
       }
       return null;
    }
    
    /**
     * 设置 Graphics2D 属性  （抗锯齿）
     * @param graphics2D
     */
    private static void setGraphics2D(Graphics2D graphics2D){
       graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
       Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
       graphics2D.setStroke(s);
    }
}

