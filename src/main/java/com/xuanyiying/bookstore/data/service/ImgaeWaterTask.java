package com.xuanyiying.bookstore.data.service;

import lombok.extern.slf4j.Slf4j;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
@Slf4j
public class ImgaeWaterTask implements Runnable {
	private String watermark;
	private String sourceImg;
	private String output;
	private Integer  degree;
    private boolean textWaterMark;
	public ImgaeWaterTask(String watermark, String sourceImg,Integer degree,boolean textWaterMark) {
		this.sourceImg = sourceImg;
		this.watermark =watermark;
		this.degree = degree;
		this.textWaterMark = textWaterMark;
		this.output = getOutput();
	}
    public ImgaeWaterTask(String watermark, String sourceImg,boolean textWaterMark) {
     this(watermark, sourceImg,0, textWaterMark);
    }

	private String getOutput() {
		File image = new File(sourceImg);
		String filename = sourceImg.substring(sourceImg.lastIndexOf(File.separator)+1);
		output = image.getParent()+"/water/" + filename;
		if(!image.exists())
			try {
				image.createNewFile();
			} catch (IOException e) {
				log.error("Create temp file met exception: error info: \n {}",e);
		}
		return output;
	}

    private void markTextWater() throws IOException {
        Font font = new Font("04b_08", Font.PLAIN, 30);//字体
        Color color= new Color(255,255,255,128);
        File srcImgFile = new File(sourceImg);
        Image srcImg = ImageIO.read(srcImgFile);
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufImg.createGraphics();
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        g.setColor(color);
        g.setFont(font);
        //设置水印的坐标
        int x = srcImgWidth - (g.getFontMetrics(g.getFont()).charsWidth(watermark.toCharArray(), 0, watermark.length())+20);
        int y = srcImgHeight - 25;
        g.drawString(watermark, x, y);  //加水印
        g.dispose();
        // 输出图片
        FileOutputStream outImgStream = new FileOutputStream(output);
        ImageIO.write(bufImg, "jpg", outImgStream);
        log.info("Add water mark successfully.");
        outImgStream.flush();
        outImgStream.close();
    }


    private void markImageWater() {
        OutputStream os = null;
        try {
            Image srcImage = ImageIO.read(new File(sourceImg));
            BufferedImage bufferImg = new BufferedImage(
                    srcImage.getWidth(null), srcImage.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImage.getScaledInstance(srcImage.getWidth(null),
                    srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB), 0,
                    0, null);
            if (null != degree) {
                g.rotate(Math.toDegrees(degree),
                        (double) bufferImg.getWidth() / 2,
                        (double) bufferImg.getHeight() / 2);
            }            
            ImageIcon imageIcon = new ImageIcon(watermark);
            Image img = imageIcon.getImage();          
            float alpha = 0.5f;
            
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        
            g.drawImage(img, 150, 300, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            os = new FileOutputStream(output);
            ImageIO.write(bufferImg, "JPG", os);
           log.info("Add text water mark successfully  file path：" + output);
        } catch (IOException e) {
            log.error("Add text water mark for image met exception: error info: \n {}",e);
        } finally{
            if(null != os){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	@Override
	public void run(){
		if(!textWaterMark) {
			try {
				markImageWater();
			} catch (Exception e) {
				log.error("Add text water mark for image met exception: "
						+ "error info: \n {}",e);
			}
		} else try {
            markTextWater();
        } catch (IOException e) {
            log.error("Add image water mark for image met exception:"
            		+ " error info: \n {}", e);
        }
	}
}

