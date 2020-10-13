/*
 * Master-COLLiDER CONFIDENTIAL
 * @author Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 */

package com.mastercollider.stegofierfx.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringConversionUtility {

    public static String intToBinaryLength(int input,int length)
    {
        String s = "%"+ length +"s";
        return String.format(s, Integer.toBinaryString(input)).replace(' ', '0');
    }

    public static int binaryToInt(String input)
    {
        return Integer.parseInt(input,2);
    }

    public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
                            .replaceAll(" ", "0")                         // zero pads
            );
        }
        return result.toString();

    }

    public static String convertByteArraysToBinary(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte b : input) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                result.append((val & 128) == 0 ? 0 : 1);      // 128 = 1000 0000
                val <<= 1;
            }
        }
        return result.toString();
    }

    public static String convertBinaryToString(String input) {
        String subject = input.replaceAll(" ","");
        subject = subject.replaceAll("........(?!$)", "$0 ");
        String raw = Arrays.stream(subject.split(" "))
                .map(binary -> Integer.parseInt(binary, 2))
                .map(Character::toString)
                .collect(Collectors.joining()); // cut the space

        return raw;
    }
}
