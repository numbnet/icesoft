/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.applications.faces.webmail.util.encryption;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

/**
 * The PasswordHash class is a utility method for keeping passwords out of plain
 * site.   This class uses a static pass phrase but could easily be altered to
 * use a dynamic pass phrase for different applicatoins.
 *
 * @author based on example from Java developers Almanac 1.4 - Encrypting with
 *  DES Using a Pass Phrase, http://javaalmanac.com/egs/javax.crypto/PassKey.html
 */
public class PasswordHash {

    private static Log log = LogFactory.getLog(PasswordHash.class);

    private static Cipher enryptCipher;
    private static Cipher decriptCipher;

    // 8-byte Salt
    private static final byte[] SALT = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };

    // Iteration count
    private static final int iterationCount = 19;

    // passphrase initialization
    private static final String PASSPHRASE = "ICEsoft Rocks!";
    private static PasswordHash encrypter = new PasswordHash(PASSPHRASE);

    

    /**
     * Encrypts the given string using symmetric encryption algorithm.
     *
     * @see #decrypt(String)
     *
     * @param plainText String to encrypt.
     * @return encrypted representation of attribute value.
     */
    public static String encrypt(String plainText){
        if (encrypter == null){
            encrypter = new PasswordHash(PASSPHRASE);    
        }
        return encrypter.encrypter(plainText);
    }

    /**
     * Encrypts the given string using symmetric encryption algorithm.
     *
     * @see #decrypt(String)
     *
     * @param encryptedText String to encrypt.
     * @return decrypted representation of attribute value.
     */
    public static String decrypt(String encryptedText){
        if (encrypter == null){
            encrypter = new PasswordHash(PASSPHRASE);    
        }
        return encrypter.decrypter(encryptedText);
    }

    /**
     *  Defautl constructor for creating a DES encrypt/decryption mechanism.
     * @param passPhrase
     */
    private PasswordHash(String passPhrase) {
        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance(
                    "PBEWithMD5AndDES").generateSecret(keySpec);

            enryptCipher = Cipher.getInstance(key.getAlgorithm());
            decriptCipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, iterationCount);

            // Create the ciphers
            enryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            decriptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (java.security.InvalidAlgorithmParameterException e) {
            log.error("InvalidAlgorithmParameterException", e);
            encrypter = null;
        } catch (java.security.spec.InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
            encrypter = null;
        } catch (javax.crypto.NoSuchPaddingException e) {
            log.error("NoSuchPaddingException", e);
            encrypter = null;
        } catch (java.security.NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
            encrypter = null;
        } catch (java.security.InvalidKeyException e) {
            log.error("InvalidKeyException", e);
            encrypter = null;
        }
    }

    /**
     * Utility method for encrypting plain text using the DES algorithm
     * and a specified passPhrase.
     * @param plaintext String to encrypt
     * @return encrypted String
     */
    private String encrypter(String plaintext) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = plaintext.getBytes("UTF8");

            // Encrypt
            byte[] enc = enryptCipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);

        } catch (javax.crypto.BadPaddingException e) {
             log.error("BadPaddingException", e);
        } catch (IllegalBlockSizeException e) {
             log.error("IllegalBlockSizeException", e);
        } catch (UnsupportedEncodingException e) {
             log.error("UnsupportedEncodingException", e);
        } catch (java.io.IOException e) {
            log.error("IOException", e);
        }
        return null;
    }

    /**
     * Utility method for decrypting text that was encrypted with the DES algorithm.
     * @param plaintext String to encrypt
     * @return encrypted String
     */
    private String decrypter(String plaintext) {
        try {
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(plaintext);

            // Decrypt
            byte[] utf8 = decriptCipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");

        } catch (javax.crypto.BadPaddingException e) {
            log.error("BadPaddingException", e);
        } catch (IllegalBlockSizeException e) {
            log.error("IllegalBlockSizeException", e);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException", e);
        } catch (java.io.IOException e) {
            log.error("IOException", e);
        }
        return null;
    }


}