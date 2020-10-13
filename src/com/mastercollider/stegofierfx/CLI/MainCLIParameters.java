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

@Parameters(separators = "=") //space works too
public class MainCLIParameters {


    @Parameter(names = {"-h", "--help"},
            help = true, //if not then will get
            description = "Displays help information")
    private boolean help;

    @Parameter(names = {"-cli", "--command-line-interface"},
            description = "Open StegofierFx in Command line Interface")
    private boolean cli;

    public boolean isHelp(){return help;}

    public boolean isCli(){return cli;}
}
