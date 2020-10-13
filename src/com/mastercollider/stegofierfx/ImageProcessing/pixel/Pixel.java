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

public abstract class Pixel {
    protected int x, y;
    protected int Value;

    public Pixel(int x, int y, int Value) {
        this.x = x;
        this.y = y;
        this.Value = Value;
    }

    public abstract int getRedValue();
    public  abstract int getGreenValue();
    public  abstract int getBlueValue();

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return Value;
    }
    public void setValue(int value) {
        Value = value;
    }

    public  abstract void setRedValue(int R);
    public abstract  void setGreenValue(int G);
    public  abstract void setBlueValue(int B);



    public abstract  int getColorValue(@NotNull ColorChannel channel);
    public abstract  void setColorValue(@NotNull ColorChannel channel, int value);


    public abstract boolean isTransparent();
}
