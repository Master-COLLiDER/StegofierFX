
/*******************************************************************************
 * Master-COLLiDER CONFIDENTIAL
 * @author Probal D. Saikia.
 * Github.com/Master-COLLiDER
 * Copyright (c) 2020 - 2020.
 * NOTICE: This file is subject to the terms and conditions defined
 *  in file 'LICENSE.txt' which is part of this source code package.
 *
 ******************************************************************************/

package com.mastercollider.stegofierfx.Encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AES256 {

    private static String salt = randomString(16);

    public static String encrypt(String strToEncrypt, String secret) throws Exception {
        // System.out.println("Salt: "+salt);
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        String encryptedMessage = salt+ Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        //  System.out.println("Encrypted Message: "+encryptedMessage);
        return encryptedMessage;
    }

    // This function receives password
    //Decryption Function
    //Decrypts text using a password
    public static String decrypt(String fullStrToDecrypt, String secret) throws Exception {
        String strToDecrypt = fullStrToDecrypt.substring(16,fullStrToDecrypt.length());
        salt = fullStrToDecrypt.substring(0,16);
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        String decryptedMessage = new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        //   System.out.println("Decrypted Message: "+decryptedMessage);
        return decryptedMessage;
    }




    public static String randomString( int len ){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }


    public static int lengthAfterEncryption(int length)
    {
        if (length<13)
            return 40;

        return (24+((length-5)/8)*20)+(((length-5)/8)/3)*4+16;

    }
}
