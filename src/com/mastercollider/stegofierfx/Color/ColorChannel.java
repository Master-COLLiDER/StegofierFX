/*
 * Master-COLLiDER CONFIDENTIAL
 * This file is made by Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 */

package com.mastercollider.stegofierfx.Color;


import com.mastercollider.stegofierfx.Color.Exceptions.InvalidColorChannelException;

public enum ColorChannel {
    R, G, B, RG, GB, RB, RGB;



    public static ColorChannel getColorChannel(int num){

        if(num == 1){ return ColorChannel.R; }
        else if (num == 2){ return ColorChannel.G;}
        else if (num == 3){ return ColorChannel.B;}
        else if (num == 4){ return ColorChannel.RG;}
        else if (num == 5){ return ColorChannel.GB;}
        else if (num == 6){ return ColorChannel.RB;}
        else if (num == 7){ return ColorChannel.RGB;}
        else{ throw new InvalidColorChannelException( String.format("Invalid Color: %d", num));}

    }

    public static int getColorChannelToInt(ColorChannel channel)
    {
        if (channel==ColorChannel.R){return 1;}
        else if (channel==ColorChannel.G){return 2;}
        else if (channel==ColorChannel.B){return 3;}
        else if (channel==ColorChannel.RG){return 4;}
        else if (channel==ColorChannel.GB){return 5;}
        else if (channel==ColorChannel.RB){return 6;}
        else if (channel==ColorChannel.RGB){return 7;}
        { throw new InvalidColorChannelException("Invalid Color: "+channel.toString());}
    }
}
