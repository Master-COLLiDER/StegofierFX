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


import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class EncryptionTypeValidator implements IParameterValidator {
    public void validate(String name, String value)
            throws ParameterException {
        int n = Integer.parseInt(value);
        if (n < 0 || n > 3) {
            throw new ParameterException("Parameter " + name + " should be between (0 - 3) (found " + value +")");
        }
    }
}