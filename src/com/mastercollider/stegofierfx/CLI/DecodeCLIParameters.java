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
public class DecodeCLIParameters {


    @Parameter(names = {"-h", "--help"},
            help = true, //if not then will get
            description = "Displays help information")
    private boolean help;

    @Parameter(names = {"-c", "--cover"},
            required = true,
            validateWith = FileParameterValidator.class,
            converter = PathConverter.class,
            description = "Absolute path to the Cover Image which stores the information (Must be PNG or BMP).")
    public Path coverImageFile;

    @Parameter(names = {"-o", "--output"},
            converter = PathConverter.class,
            description = "File name for the output extracted data ")
    public Path outputFile = null;


    @Parameter(names = {"-p", "--password"},
            description = "Password for decryption")
    public String password = null;


    @Parameter(names = {"-pvtk", "--private-key"},
            validateWith = FileParameterValidator.class,
            converter = PathConverter.class,
            description = "File name for the private key (Required during decryption if -ET = 3 (RSA) was used as encryption algorithm)")
    public Path privateKeyFile = null;


    public boolean isHelp() {
        return help;
    }

    @Override
    public String toString() {
        return "DecodeCLIParameters{" +
                "\n help=" + help +
                ",\n coverImageFile=" + coverImageFile +
                ",\n outputFile=" + outputFile +
                ",\n password='" + password + '\'' +
                ",\n privateKeyFile=" + privateKeyFile +
                "\n }";
    }
}