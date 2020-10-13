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

public class Pixel_RGB extends Pixel{

    public Pixel_RGB(int x, int y, int Value) {
        super(x, y, Value);
    }


    @Override
    public int getRedValue()
    {
        return (this.Value>>16) & 0xFF;
    }
    @Override
    public int getGreenValue()
    {
        return (this.Value>>8) & 0xFF;
    }
    @Override
    public int getBlueValue()
    {
        return this.Value & 0xFF;
    }

    @Override
    public void setRedValue(int R){
        this.Value = (R<<16) | (getGreenValue()<<8) | getBlueValue();
    }
    @Override
    public void setGreenValue(int G){
        this.Value = (getRedValue()<<16) | (G<<8) | getBlueValue();
    }
    @Override
    public void setBlueValue(int B){
        this.Value = (getRedValue()<<16) | (getGreenValue()<<8) | B;
    }


    @Override
    public int getColorValue(@NotNull ColorChannel channel) {
        if (channel.toString().length()!=1)
            throw new RuntimeException("Invalid "+channel+" for getChannelValue().");

        if (ColorChannel.R == channel){ return getRedValue(); }
        else if (ColorChannel.G == channel){ return getGreenValue(); }
        else{ return getBlueValue(); }
    }

    @Override
    public void setColorValue(@NotNull ColorChannel channel, int value) {
        if (channel.toString().length()!=1)
            throw new RuntimeException("Invalid "+channel+" for setChannelValue().");

        if (ColorChannel.R == channel){ setRedValue(value); }
        else if (ColorChannel.G == channel){ setGreenValue(value); }
        else if (ColorChannel.B == channel){ setBlueValue(value); }
    }

    @Override
    public boolean isTransparent() { return false;}
}
