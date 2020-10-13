/*******************************************************************************
 * Master-COLLiDER CONFIDENTIAL
 * @author Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 ******************************************************************************/

package com.mastercollider.stegofierfx.CLI;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.nio.file.Path;

@Parameters(separators = "=") //space works too
public class EncodeCLIParameters {


    @Parameter(names = {"-h", "--help"},
            help = true, //if not then will get
            description = "Displays help information")
    private boolean help;

    @Parameter(names = {"-c", "--cover"},
            required = true,
            validateWith = FileParameterValidator.class,
            converter = PathConverter.class,
            description = "Absolute path to the Cover Image which will store data (Must be PNG or BMP).")
    public Path coverImageFile;

    @Parameter(names = {"-o", "--output"},
            converter = PathConverter.class,
//            validateWith = FileParameterValidator.class,
            description = "File name for the output Image.")
    public Path outputImageFile = null;

    @Parameter(names = {"-m", "--message"},
            description = "Message to be stored")
    public String message;

    @Parameter(names = {"-cc", "--color-channel",""},
            validateWith = ColorChannelValidator.class,
            description = "Color Channels  (1 - 7) - R: 1, G: 2, B :3, RG: 4, GB: 5, RB: 6, RGB: 7")
    public int colorChannel = 1; // If 1, then Red channel is selected


    @Parameter(names = {"-ET", "--encryption-type"},
            validateWith = EncryptionTypeValidator.class,
            description = "Encryption type (1 - 3): 1 - AES256, 2 - TripleDES, 3 - RSA ")
    public int encryptionType = 0; // If 0, then message will not be decrypted

    @Parameter(names = {"-lsb", "--no-of-lsb"},
            validateWith = LSBValidator.class,
            description = "Number of least significant bits: ranges 0 - 7 ")
    public int noOfLSB = 0; // If 0, then message will not be decrypted

    @Parameter(names = {"-p", "--password"},
            description = "Password for encryption")
    public String password = null;

    @Parameter(names = {"-pbk", "--public-key"},
            validateWith = FileParameterValidator.class,
            converter = PathConverter.class,
            description = "File name for the public key (Required during encryption if -ET=3 (RSA) is selected)")
    public Path publicKeyFile = null;


    public boolean isHelp() {
        return help;
    }

    public boolean isEncrypted(){return encryptionType!=0;}

    public boolean isRSA(){return encryptionType == 3;}

    public boolean isPublicKeyGiven(){return publicKeyFile!=null;}

    public  boolean passwordGiven(){return password!=null && !password.equals("");}


    @Override
    public String toString() {
        return "EncodeCLIParameters{" +
                "  help=" + help +
                ", coverImageFile=" + coverImageFile +
                ", outputImageFile=" + outputImageFile +
                ", message='" + message + '\'' +
                ", colorChannel=" + colorChannel +
                ", encryptionType=" + encryptionType +
                ", noOfLSB=" + noOfLSB +
                ", password='" + password + '\'' +
                ", publicKeyFile=" + publicKeyFile +
                '}';
    }
}
