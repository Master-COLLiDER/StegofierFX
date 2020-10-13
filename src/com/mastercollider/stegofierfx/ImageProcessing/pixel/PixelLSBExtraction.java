/*
 * Master-COLLiDER CONFIDENTIAL
 * This file is made by Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 */

package com.mastercollider.stegofierfx.ImageProcessing.pixel;

import com.mastercollider.stegofierfx.Color.ColorChannel;
import org.jetbrains.annotations.NotNull;

public class PixelLSBExtraction {

    public static int doExtract(@NotNull Pixel input, @NotNull ColorChannel channel , int bitPosition)
    {
        if(channel.toString().length() != 1 )
            throw new RuntimeException("Invalid ColorChannel "+channel+"for method doExtract()");

        int b;
        b = input.getColorValue(channel);
        b = (b >> bitPosition)& 1;

        return b;
    }
}
