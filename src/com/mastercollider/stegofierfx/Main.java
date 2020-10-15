package com.mastercollider.stegofierfx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.mastercollider.stegofierfx.CLI.*;
import com.mastercollider.stegofierfx.Encryption.RSA.RSAKeyPairGenerator;
import com.mastercollider.stegofierfx.GUI.SplashScreen;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main extends Application {

    final MainCLIParameters mainArgs = new MainCLIParameters();
    final DecodeCLIParameters decodeArgs = new DecodeCLIParameters();
    final EncodeCLIParameters encodeArgs = new EncodeCLIParameters();
    final RSAKeyGeneratorCLIParameters rsaKeyGeneratorArgs = new RSAKeyGeneratorCLIParameters();
    private String parsedCommand=null;

    public static void main(String[] args) {
        if (args.length>0)
        {
            Main CLIMode = new Main();
            CLIMode.handleInputArgs(args);
            CLIMode.run();
        }
        else {
            launch(args);
        }
    }

    void handleInputArgs(String args[]) {
        JCommander jCommander = new JCommander.Builder()
                .addObject(mainArgs)
                .addCommand("encode", encodeArgs)
                .addCommand("decode", decodeArgs)
                .addCommand("generate", rsaKeyGeneratorArgs)
                .build();

        jCommander.setProgramName("Stegofier");


        try {
            jCommander.parse(args);
        } catch (ParameterException exception) {
            System.out.println(exception.getMessage());
            showUsage(jCommander);
        }

        if (mainArgs.isHelp()) {
            showUsage(jCommander);
        }else if (mainArgs.isCli()){
           parsedCommand = "cli";
        }
        else {
            if (jCommander.getParsedCommand() == "encode")
            {
                if (encodeArgs.isHelp()) {
                    showUsage(jCommander);
                }
                else if (encodeArgs.encryptionType>0)
                {
                    if (encodeArgs.isRSA())
                    {
                        if (!encodeArgs.isPublicKeyGiven())
                        {
                            System.out.println("Parameter ET=3(Encryption type RSA) requires public key -pbk, --public-key = [Public Key file] ");
                            System.out.println("use -h, --help for help option");
                            System.exit(0);
                        }
                        if (encodeArgs.message!=null)
                            if (encodeArgs.message.length()>117)
                            {
                                System.out.println("Parameter ET=3(Encryption type RSA) requires message -m --message to be less than 117 characters");
                                System.exit(0);
                            }

                    }else if (!encodeArgs.passwordGiven())
                    {
                        System.out.println("Parameter ET=1,2(Encryption type AES , TripleDES respectively) requires password -p,--password = [password] ");
                        showUsage(jCommander);
                    }

                }

                    parsedCommand = "encode";
            }else if (jCommander.getParsedCommand() == "decode")
            {
                if (decodeArgs.isHelp()) {
                    showUsage(jCommander);
                }
                    parsedCommand = "decode";
            }else if (jCommander.getParsedCommand() == "generate")
            {
                if (rsaKeyGeneratorArgs.isHelp()) {
                    showUsage(jCommander);
                }
                    parsedCommand = "generate";
            }
        }
    }

    void showUsage(JCommander jCommander) {
        jCommander.usage();
        System.exit(0);
    }

    void run() {
        System.out.println("Running stegofier with ...");

        if (parsedCommand!=null)
        switch (parsedCommand) {
            case "generate":
                System.out.println(rsaKeyGeneratorArgs);
                try {
                    RSAKeyPairGenerator.GenerateKeyToFiles(rsaKeyGeneratorArgs.destinationDir);
                } catch (NoSuchAlgorithmException | IOException e) {
                    e.printStackTrace();
                }
                System.exit(1);
                break;
            case "encode":
                System.out.println(encodeArgs);
                try {
                    CLIExecutor.encode(encodeArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(1);
                break;
            case "decode":
                System.out.println(decodeArgs);
                String message = "";
                try {
                    message = CLIExecutor.decode(decodeArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Hidden Message: "+message);
                }
                System.exit(1);
                break;

            case "cli":
                CLIExecutor.RunCLI();
                break;

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(primaryStage);
//        EncoderFX encoderFX = new EncoderFX();
//        encoderFX.start(primaryStage);
    }
}
