/*
 * Master-COLLiDER CONFIDENTIAL
 * This file is made by Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 */

package com.mastercollider.stegofierfx.ImageProcessing;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public enum ImageFormat {
    NOT_DEFINED,PNG, BMP,JPEG;

    public static ImageFormat getImageFormat(int ImageFormat)
    {
        if (ImageFormat == 1){return PNG;}
        else if(ImageFormat == 2){return BMP;}
        else if(ImageFormat == 3){return JPEG;}
        else {return  NOT_DEFINED;}
    }

    public static ImageFormat getImageFormat(String ImageFormatInString)
    {
        if(ImageFormatInString.equals("PNG")||ImageFormatInString.equals("png")){ return PNG; }
        else if(ImageFormatInString.equals("BMP")||ImageFormatInString.equals("bmp")){ return BMP; }
        else if(ImageFormatInString.equals("JPEG")||ImageFormatInString.equals("jpeg")){ return JPEG; }
        else { return NOT_DEFINED; }
    }

    public static ImageFormat doDetect(@NotNull File imagefile) throws IOException {

        ImageInputStream iis = ImageIO.createImageInputStream(imagefile);
        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
        if (!imageReaders.hasNext()) {
            throw new RuntimeException("No readers found!");
        }
        ImageReader reader = imageReaders.next();
       return getImageFormat(reader.getFormatName());

    }
    public static ImageFormat doDetect(@NotNull String FileName) throws IOException {
       return doDetect(new File(FileName));
    }

}
