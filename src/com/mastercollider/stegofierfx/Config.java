/*
 * Master-COLLiDER CONFIDENTIAL
 * This file is made by Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 */

package com.mastercollider.stegofierfx;

import com.mastercollider.stegofierfx.Color.ColorChannel;

public class Config {


    public Config() {
    }

    public static final String STEGO_IMAGE_DETECTOR_PATTERN = "COVER_IMAGE";

    public static final int STEGO_IMAGE_DETECTOR_PATTERN_STORE = STEGO_IMAGE_DETECTOR_PATTERN.length();
    public static final int COLOR_CHANNEL_STORE = 1;
    public static final int ENCRYPTION_TYPE_STORE = 1;
    public static final int NO_OF_LSB_STORE = 1;
    public static final int MESSAGE_LENGTH_STORE = 4;

    public static final ColorChannel HEADER_COLOR_CHANNEL = ColorChannel.B;
    public static final int HEADER_LSB = 0;



    public static final int HEADER_LENGTH_BYTE = STEGO_IMAGE_DETECTOR_PATTERN_STORE
                                                + COLOR_CHANNEL_STORE
                                                + ENCRYPTION_TYPE_STORE + NO_OF_LSB_STORE + MESSAGE_LENGTH_STORE;


}
