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

public class PixelLSBInsertion {


    public static Pixel doInsert(@NotNull Pixel input, @NotNull ColorChannel channel, int value, int bitPosition) {
        if (channel.toString().length() != 1)
            throw new RuntimeException("Invalid Parameter ColorChannel: " + channel.toString() + "For doInsert2P() ");

        int b= input.getColorValue(channel);
        b = (b & ~(1 << bitPosition)) | (value << bitPosition);

        input.setColorValue(channel, b);
        return input;
    }

    public static Pixel doInsert2P(@NotNull Pixel input, @NotNull ColorChannel channel, int value1, int value2, int bitPosition)
    {

        if (channel.toString().length() != 2)
            throw new RuntimeException("Invalid Parameter ColorChannel: " + channel.toString() + "For doInsert2P() ");

        if (ColorChannel.RG == channel){
            input = doInsert(input,ColorChannel.R,value1,bitPosition);
            input = doInsert(input,ColorChannel.G,value2,bitPosition);

        }else if (ColorChannel.GB == channel){

            input = doInsert(input,ColorChannel.G,value1,bitPosition);
            input = doInsert(input,ColorChannel.B,value2,bitPosition);
        }else {
            input = doInsert(input,ColorChannel.R,value1,bitPosition);
            input = doInsert(input,ColorChannel.B,value2,bitPosition);
        }

        return input;
    }
    
    public static Pixel doInsert3P(@NotNull Pixel input, @NotNull ColorChannel channel, int value1, int value2, int value3, int bitPosition)
    {
        if (channel.toString().length() != 3)
            throw new RuntimeException("Invalid Parameter ColorChannel: " + channel.toString() + "For doInsert2P() ");

        input = doInsert(input,ColorChannel.R,value1,bitPosition);
        input = doInsert(input,ColorChannel.G,value2,bitPosition);
        input = doInsert(input,ColorChannel.B,value3,bitPosition);

        return input;
    }

}
