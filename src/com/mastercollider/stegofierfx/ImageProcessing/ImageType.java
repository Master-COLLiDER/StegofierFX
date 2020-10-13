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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageType {

    public static BufferedImage doConvert(@NotNull BufferedImage inputImage, @NotNull int to)
    {
        BufferedImage result = new BufferedImage(inputImage.getWidth(),inputImage.getHeight(), to);
        result.getGraphics().drawImage(inputImage, 0, 0, null);
        return result;
    }

    public static BufferedImage doConvert(@NotNull File inputImageFile, @NotNull int to) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputImageFile);
        return doConvert(inputImage, to);
    }
    public static BufferedImage doConvert(@NotNull String inputImageFile, @NotNull int to) throws IOException {
        return doConvert(new File(inputImageFile), to);
    }
}
