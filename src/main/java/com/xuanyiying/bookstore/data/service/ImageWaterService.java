package com.xuanyiying.bookstore.data.service;

import java.io.IOException;

public interface ImageWaterService {
    /**
     *
     * @param sourceImage
     * @param watermark
     * @param textWaterMark
     * @throws IOException
     */
    void markImageWater(String sourceImage, String watermark,
                        boolean textWaterMark) throws IOException;

    /**
     *
     * @param sourceImage
     * @param watermark
     * @param textWaterMark
     * @param degree
     * @throws IOException
     */
    void markImageWater(String sourceImage, String watermark,
                        boolean textWaterMark,int degree)  throws IOException;
}
