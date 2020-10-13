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

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileParameterValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        Path pathToConfigDir = Paths.get(value);
        if (!exists(pathToConfigDir)) {
            String message = String.format("[%s] does not exist: ",  value);
            throw new ParameterException(message);
        }
        if (!Files.isRegularFile(pathToConfigDir, LinkOption.NOFOLLOW_LINKS)) {
            String message = String.format("[%s] is not a file: ", value);
            throw new ParameterException(message);
        }
    }

    private boolean exists(Path path) {
        return (Files.exists(path, LinkOption.NOFOLLOW_LINKS));
    }

}
