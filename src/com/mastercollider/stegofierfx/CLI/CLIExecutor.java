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

import com.mastercollider.stegofierfx.Color.ColorChannel;
import com.mastercollider.stegofierfx.Decoder.Decoder;
import com.mastercollider.stegofierfx.Encoder.Encoder;
import com.mastercollider.stegofierfx.Encryption.AES256;
import com.mastercollider.stegofierfx.Encryption.EncryptionType;
import com.mastercollider.stegofierfx.Encryption.RSA.RSA;
import com.mastercollider.stegofierfx.Encryption.RSA.RSAKeyPairGenerator;
import com.mastercollider.stegofierfx.Encryption.TripleDES;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class CLIExecutor {

    public static void RunCLI(){

        System.out.println("Running Stegofier");
        System.out.println("Main Menu");
        System.out.println("1. Hide message in a Image.");
        System.out.println("2. Retrieve message from a Image.");
        System.out.println("3. Generate RSA Key Pairs.");
        System.out.println("Enter Your Choice: ");
        Scanner mainMenuScanner = new Scanner(System.in);
        switch (mainMenuScanner.nextInt())
        {
            case 1:
                try {
                    runCLIEncoder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                runCLIDecoder();
                break;
            case 3:
                try {
                    runRSAKeyGenerator();
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
        }
        mainMenuScanner.close();
    }

    private static void runCLIEncoder() throws Exception {
        Scanner cliEncoderScanner = new Scanner(System.in);
        EncodeCLIParameters encodeArgs = new EncodeCLIParameters();
        System.out.println("Hide in a Image");

        System.out.println("\nEnter the cover image Path (Must be PNG,BMP):");
        String cFilePath = cliEncoderScanner.nextLine();
        FileParameterValidator fileParameterValidator = new FileParameterValidator();
        fileParameterValidator.validate("Cover Image ", cFilePath);
        encodeArgs.coverImageFile = Path.of(cFilePath);

        System.out.println("\nEnter the output Image Path (Must be PNG,BMP):");
        String oFilePath = cliEncoderScanner.nextLine();
        encodeArgs.outputImageFile = Path.of(oFilePath);

        System.out.println("\nColor Channels  (1 - 7) - R: 1, G: 2, B :3, RG: 4, GB: 5, RB: 6, RGB: 7");
        System.out.println("Enter ColorChannel:");
        String cc = cliEncoderScanner.next();
        ColorChannelValidator colorChannelValidator = new ColorChannelValidator();
        colorChannelValidator.validate("ColorChannel",cc);
        encodeArgs.colorChannel = Integer.parseInt(cc);

        System.out.println("\nEncryption type (0 - 3):0 - NO Encryption, 1 - AES256, 2 - TripleDES, 3 - RSA ");
        System.out.println("Enter Encryption type:");
        String et = cliEncoderScanner.next();
        EncryptionTypeValidator encryptionTypeValidator = new EncryptionTypeValidator();
        encryptionTypeValidator.validate("EncryptionType",et);
        encodeArgs.encryptionType = Integer.parseInt(et);

        System.out.println("\nNumber of least significant bits: ranges 0 - 7 ");
        System.out.println("Enter Number of LSB to be used:");
        String nLsb = cliEncoderScanner.next();
        LSBValidator lsbValidator = new LSBValidator();
        lsbValidator.validate("LSB",nLsb);
        encodeArgs.noOfLSB = Integer.parseInt(nLsb);

        encode(encodeArgs);
    }
    private static void runCLIDecoder()
    {
        Scanner cliDecoderScanner = new Scanner(System.in);
        DecodeCLIParameters decodeArgs = new DecodeCLIParameters();
        System.out.println("Extract from Image");

        System.out.println("\nEnter the cover image Path to extract data (Must be PNG,BMP):");
        String cFilePath = cliDecoderScanner.nextLine();
        FileParameterValidator fileParameterValidator = new FileParameterValidator();
        fileParameterValidator.validate("Cover Image ", cFilePath);
        decodeArgs.coverImageFile = Path.of(cFilePath);

        String Message = "";
        try {
            Message = decode(decodeArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("Hidden Message: "+Message);
        }
    }

    private static void runRSAKeyGenerator() throws IOException, NoSuchAlgorithmException {
        Scanner cliRSAKeyGeneratorScanner = new Scanner(System.in);
        System.out.println("Generate RSA Key Pair");
        System.out.println("\nEnter the Directory to store RSA Keys Pair:");
        String Dir = cliRSAKeyGeneratorScanner.nextLine();
        if(Dir.equals("")||Dir.equals("/")||Dir.isEmpty())
            RSAKeyPairGenerator.GenerateKeyToFiles();
        else
            RSAKeyPairGenerator.GenerateKeyToFiles(Dir);

    }



    public static void encode(@NotNull EncodeCLIParameters encodeArgs) throws Exception {

        if (encodeArgs.message==null)
        {
            StringBuilder message = new StringBuilder();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the message to encode: ");
            encodeArgs.message = scanner.nextLine();
            if (encodeArgs.encryptionType==3)
                if (encodeArgs.message.length()>117)
                {
                    System.out.println("Parameter ET=3(Encryption type RSA) requires message -m --message to be less than 117 characters");
                    System.exit(0);
                }
        }
        System.out.println("Message to encode: \n"+encodeArgs.message);


        System.out.println("Cover file name: \n"+encodeArgs.coverImageFile);

        if (encodeArgs.outputImageFile == null || encodeArgs.outputImageFile.equals(""))
        {
            encodeArgs.outputImageFile =Path.of(encodeArgs.coverImageFile.getRoot()
                    +randomString(16)+"."+
                    encodeArgs.coverImageFile.toString().
                    substring(encodeArgs.coverImageFile.toString().lastIndexOf(".")+1));
        }
        System.out.println("Output file name: \n"+encodeArgs.outputImageFile);




        if (EncryptionType.getEncryptionType(encodeArgs.encryptionType)!=EncryptionType.NOT_ENCRYPTED)
        {
            if(EncryptionType.getEncryptionType(encodeArgs.encryptionType)==EncryptionType.RSA)
            {
                if (encodeArgs.publicKeyFile==null)
                {
                    String pbKey;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the path to Private Key : ");
                    pbKey = scanner.nextLine();
                    File file = new File(pbKey);
                    if (file.exists())
                        encodeArgs.publicKeyFile = file.toPath();
                    else
                    {
                        System.out.println("Error File "+file.toString()+" Does Not Exists!");
                        System.exit(0);
                    }
                }else
                {
                    encodeArgs.message = Base64.getEncoder().encodeToString(RSA.encrypt(encodeArgs.message,RSA.readKeys(new File(encodeArgs.publicKeyFile.toString()))));
                }
            }
            else if (EncryptionType.getEncryptionType(encodeArgs.encryptionType)==EncryptionType.AES256)
            {
                if (encodeArgs.password==null)
                {
                    String password;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the password for the image : ");
                    password = scanner.nextLine();
                    encodeArgs.password = password;
                }else
                {
                    encodeArgs.message = AES256.encrypt(encodeArgs.message,encodeArgs.password);
                }
            }else if (EncryptionType.getEncryptionType(encodeArgs.encryptionType)==EncryptionType.TRIPLEDES)
            {
                if (encodeArgs.password==null)
                {
                    String password;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the password for the image : ");
                    password = scanner.nextLine();
                    encodeArgs.password = password;
                }else
                {
                    TripleDES tripleDES = new TripleDES(encodeArgs.password);
                    encodeArgs.message = tripleDES.encrypt(encodeArgs.message);
                }
            }
        }






        try {
            Encoder encoder = new Encoder(encodeArgs.coverImageFile.toString(),
                    encodeArgs.outputImageFile.toString(),
                    encodeArgs.message,
                    EncryptionType.getEncryptionType(encodeArgs.encryptionType),
                    ColorChannel.getColorChannel(encodeArgs.colorChannel),
                    encodeArgs.noOfLSB);
            encoder.Encode();
            encoder.saveImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String decode(@NotNull DecodeCLIParameters decodeArgs) throws Exception {

        String message = "";

        Decoder decoder = new Decoder(decodeArgs.coverImageFile.toString());
        decoder.Decode();

        if (decoder.getEncryptionType()!=EncryptionType.NOT_ENCRYPTED)
        {
            if(decoder.getEncryptionType()==EncryptionType.RSA)
            {
                if (decodeArgs.privateKeyFile==null)
                {
                    String pvtKey;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the path to Private Key : ");
                    pvtKey = scanner.nextLine();
                    File file = new File(pvtKey);
                    if (file.exists())
                        decodeArgs.privateKeyFile = file.toPath();
                    else
                    {
                        System.out.println("Error File "+file.toString()+" Does Not Exists!");
                        System.exit(0);
                    }
                }

                    message = RSA.decrypt(decoder.getMessage(),RSA.readKeys(new File(decodeArgs.privateKeyFile.toString())));

            }
            else if (decoder.getEncryptionType()==EncryptionType.AES256)
            {
                if (decodeArgs.password==null)
                {
                    String password;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the path to Password of the image : ");
                    password = scanner.nextLine();
                    decodeArgs.password = password;
                }

                    message = AES256.decrypt(decoder.getMessage(),decodeArgs.password);

            }else if (decoder.getEncryptionType()==EncryptionType.TRIPLEDES)
            {
                if (decodeArgs.password==null)
                {
                    String password;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the path to Password of the image : ");
                    password = scanner.nextLine();
                    decodeArgs.password = password;
                }

                    TripleDES tripleDES = new TripleDES(decodeArgs.password);
                    message = tripleDES.decrypt(decoder.getMessage());

            }
        }
        else
            message = decoder.getMessage();

        if (decodeArgs.outputFile!=null)
        {
            File oFile = new File(decodeArgs.outputFile.toString());
            if (oFile.exists()) oFile.delete();
            oFile.createNewFile();
            FileWriter myWriter = new FileWriter(oFile);
            myWriter.write(message);
            myWriter.close();
            System.out.println("Successfully printed the result to "+decodeArgs.outputFile);
        }
        return message;
    }


    public static String randomString( int len ){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
