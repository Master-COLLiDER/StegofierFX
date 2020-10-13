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

public class Pixel_ARGB extends Pixel_RGB{

    public Pixel_ARGB(int x, int y, int Value) {
        super(x, y, Value);
    }




    public int getAlphaValue(){ return (this.Value>>24) & 0xFF;}
//    public int getRedValue()
//    {
//        return (this.Value>>16) & 0xFF;
//    }
//    public int getGreenValue()
//    {
//        return (this.Value>>8) & 0xFF;
//    }
//    public int getBlueValue()
//    {
//        return this.Value & 0xFF;
//    }


    public void setAlphaValue(int A){
        this.Value = (A<<24) | (getRedValue()<<16) | (getGreenValue()<<8) | getBlueValue();
    }
    @Override
    public void setRedValue(int R){
        this.Value = (getAlphaValue()<<24) | (R<<16) | (getGreenValue()<<8) | getBlueValue();
    }
    @Override
    public void setGreenValue(int G){
        this.Value = (getAlphaValue()<<24) | (getRedValue()<<16) | (G<<8) | getBlueValue();
    }
    @Override
    public void setBlueValue(int B){
        this.Value = (getAlphaValue()<<24) | (getRedValue()<<16) | (getGreenValue()<<8) | B;
    }


//    public int getColorValue(@NotNull ColorChannel channel)
//    {
//        if (channel.toString().length()!=1)
//            throw new RuntimeException("Invalid "+channel+" for getCValue().");
//        if (ColorChannel.R == channel){ return getRedValue(); }
//        else if (ColorChannel.G == channel){ return getGreenValue(); }
//        else{ return getBlueValue(); }
//    }
//
//    public void setColorValue(@NotNull ColorChannel channel, @NotNull int value)
//    {
//        if (ColorChannel.R == channel){ setRedValue(value); }
//        else if (ColorChannel.G == channel){ setGreenValue(value); }
//        else if (ColorChannel.B == channel){ setBlueValue(value); }
//    }

    @Override
    public boolean isTransparent() {
        return this.getAlphaValue()<=0;
    }

}
