package com.xuanyiying.bookstore.data.service.impl;

import com.xuanyiying.bookstore.data.service.ImageWaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Slf4j
@Service
public class ImageWaterServiceImpl implements ImageWaterService {
    @Override
    public void markImageWater(String sourceImage, String watermark, boolean textWaterMark)  throws IOException {
        log.info("Start add water mark for image......");
    	ImageWaterTaskManager manager = new ImageWaterTaskManager(sourceImage);
        manager.markWater(watermark,textWaterMark,0);
    }
    @Override
    public void markImageWater(String sourceImage, String watermark, boolean textWaterMark,int degree)  throws IOException {
        log.info("Start add water mark for image......");
    	ImageWaterTaskManager manager = new ImageWaterTaskManager(sourceImage);
        manager.markWater(watermark,textWaterMark,degree);
    }
}
