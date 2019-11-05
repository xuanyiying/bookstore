package com.xuanyiying.bookstore.data.service.impl;

import com.xuanyiying.bookstore.data.service.ImgaeWaterTask;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
public class ImageWaterTaskManager {
    private static ExecutorService executor;
    private List<File> images;
    public  ImageWaterTaskManager(String imageParentPath) throws FileNotFoundException {
        int nThreads = calculateThreadsNum(imageParentPath);
        executor = Executors.newFixedThreadPool(nThreads);
    }
    private int calculateThreadsNum(String imageParentPath) throws FileNotFoundException {
        File parent = new File(imageParentPath);
        if(null == parent || !parent.exists()){
            throw new FileNotFoundException();
        }
        Collection<File>  files = FileUtils.listFiles(parent,FileFileFilter.FILE,null);
        images = (List<File>)files;
        log.info("The current directory has {} files need to add water mark",files.size());
        int num = images.size();
        if(10 < num && num < 100){
            num = num/5;
        } else if (100 < num && num < 1000){
            num = num/50;
        } else if (1000 < num && num < 10000){
            num = num/500;
        } else if (10000< num){
            num = 20;
        }
        log.info("Create the ImageWaterTask thread number is {}",num);
        return num;
    }
    public  void markWater(String watermark,boolean textWaterMark,int degree){
        for (File file: images) {
            ImgaeWaterTask task = new ImgaeWaterTask(watermark, file.getPath(), degree, textWaterMark);
            executor.submit(task);
        }
    }


}
