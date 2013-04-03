package cn.edu.gzucm.web.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

/**
 * -----------------------------------------------------------------------------
 * The following example implements a class for encrypting and decrypting
 * strings using several Cipher algorithms. The class is created with a key and
 * can be used repeatedly to encrypt and decrypt strings using that key.
 * Some of the more popular algorithms are:
 *      Blowfish
 *      DES
 *      DESede
 *      PBEWithMD5AndDES
 *      PBEWithMD5AndTripleDES
 *      TripleDES
 *
 * @version 1.0
 * @author  Jeffrey M. Hunter  (jhunter@idevelopment.info)
 * @author  http://www.idevelopment.info
 * -----------------------------------------------------------------------------
 */

public class StringEncrypter {

    Cipher ecipher;
    Cipher dcipher;

    /**
     * Constructor used to create this object.  Responsible for setting
     * and initializing this object's encrypter and decrypter Chipher instances
     * given a Secret Key and algorithm.
     * @param key        Secret Key used to initialize both the encrypter and
     *                   decrypter instances.
     * @param algorithm  Which algorithm to use for creating the encrypter and
     *                   decrypter instances.
     */
    StringEncrypter(SecretKey key, String algorithm) {

        try {
            ecipher = Cipher.getInstance(algorithm);
            dcipher = Cipher.getInstance(algorithm);
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }
    }

    /**
     * Constructor used to create this object.  Responsible for setting
     * and initializing this object's encrypter and decrypter Chipher instances
     * given a Pass Phrase and algorithm.
     * @param passPhrase Pass Phrase used to initialize both the encrypter and
     *                   decrypter instances.
     */
    StringEncrypter(String passPhrase) {

        // 8-bytes Salt
        byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03 };

        // Iteration count
        int iterationCount = 19;

        try {

            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameters to the cipthers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            System.out.println("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }
    }

    /**
     * Takes a single String as an argument and returns an Encrypted version
     * of that String.
     * @param str String to be encrypted
     * @return <code>String</code> Encrypted version of the provided String
     */
    public String encrypt(String str) {

        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return URLEncoder.encode(Base64.encodeBase64String(enc), "UTF-8");

        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Takes a encrypted String as an argument, decrypts and returns the
     * decrypted String.
     * @param str Encrypted String to be decrypted
     * @return <code>String</code> Decrypted version of the provided String
     */
    public String decrypt(String str) {

        try {

            // Decode base64 to get bytes
            str = URLDecoder.decode(str, "UTF-8");
            byte[] dec = Base64.decodeBase64(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");

        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * The following method is used for testing the String Encrypter class.
     * This method is responsible for encrypting and decrypting a sample
     * String using several symmetric temporary Secret Keys.
     */
    public static void testUsingSecretKey() {

        try {

            System.out.println();
            System.out.println("+----------------------------------------+");
            System.out.println("|  -- Test Using Secret Key Method --    |");
            System.out.println("+----------------------------------------+");
            System.out.println();

            String secretString = "Attack at dawn!";

            // Generate a temporary key for this example. In practice, you would
            // save this key somewhere. Keep in mind that you can also use a
            // Pass Phrase.
            SecretKey desKey = KeyGenerator.getInstance("DES").generateKey();
            SecretKey blowfishKey = KeyGenerator.getInstance("Blowfish").generateKey();
            SecretKey desedeKey = KeyGenerator.getInstance("DESede").generateKey();

            // Create encrypter/decrypter class
            StringEncrypter desEncrypter = new StringEncrypter(desKey, desKey.getAlgorithm());
            StringEncrypter blowfishEncrypter = new StringEncrypter(blowfishKey, blowfishKey.getAlgorithm());
            StringEncrypter desedeEncrypter = new StringEncrypter(desedeKey, desedeKey.getAlgorithm());

            // Encrypt the string
            String desEncrypted = desEncrypter.encrypt(secretString);
            String blowfishEncrypted = blowfishEncrypter.encrypt(secretString);
            String desedeEncrypted = desedeEncrypter.encrypt(secretString);

            // Decrypt the string
            String desDecrypted = desEncrypter.decrypt(desEncrypted);
            String blowfishDecrypted = blowfishEncrypter.decrypt(blowfishEncrypted);
            String desedeDecrypted = desedeEncrypter.decrypt(desedeEncrypted);

            // Print out values
            System.out.println(desKey.getAlgorithm() + " Encryption algorithm");
            System.out.println("    Original String  : " + secretString);
            System.out.println("    Encrypted String : " + desEncrypted);
            System.out.println("    Decrypted String : " + desDecrypted);
            System.out.println();

            System.out.println(blowfishKey.getAlgorithm() + " Encryption algorithm");
            System.out.println("    Original String  : " + secretString);
            System.out.println("    Encrypted String : " + blowfishEncrypted);
            System.out.println("    Decrypted String : " + blowfishDecrypted);
            System.out.println();

            System.out.println(desedeKey.getAlgorithm() + " Encryption algorithm");
            System.out.println("    Original String  : " + secretString);
            System.out.println("    Encrypted String : " + desedeEncrypted);
            System.out.println("    Decrypted String : " + desedeDecrypted);
            System.out.println();

        } catch (NoSuchAlgorithmException e) {
        }
    }

    /**
     * The following method is used for testing the String Encrypter class.
     * This method is responsible for encrypting and decrypting a sample
     * String using using a Pass Phrase.
     */
    public static void testUsingPassPhrase() {

        System.out.println();
        System.out.println("+----------------------------------------+");
        System.out.println("|  -- Test Using Pass Phrase Method --   |");
        System.out.println("+----------------------------------------+");
        System.out.println();

        String secretString = "Attack at dawn!";
        String passPhrase = "My Pass Phrase";

        // Create encrypter/decrypter class
        StringEncrypter desEncrypter = new StringEncrypter(passPhrase);

        // Encrypt the string
        String desEncrypted = desEncrypter.encrypt(secretString);

        // Decrypt the string
        String desDecrypted = desEncrypter.decrypt(desEncrypted);

        // Print out values
        System.out.println("PBEWithMD5AndDES Encryption algorithm");
        System.out.println("    Original String  : " + secretString);
        System.out.println("    Encrypted String : " + desEncrypted);
        System.out.println("    Decrypted String : " + desDecrypted);
        System.out.println();

    }

    public static String encode(String a_String, String hash) {

        StringEncrypter stringEncrypter = new StringEncrypter(hash);
        return stringEncrypter.encrypt(a_String);
    }

    public static String encode(String a_String) {

        StringEncrypter stringEncrypter = new StringEncrypter("XKXKXK0099pp");
        return stringEncrypter.encrypt(a_String);
    }

    public static String decode(String a_String, String hash) {

        StringEncrypter stringEncrypter = new StringEncrypter(hash);
        return stringEncrypter.decrypt(a_String);
    }

    public static String decode(String a_String) {

        StringEncrypter stringEncrypter = new StringEncrypter("XKXKXK0099pp");
        return stringEncrypter.decrypt(a_String);
    }

    /**
     * Sole entry point to the class and application used for testing the
     * String Encrypter class.
     * @param args Array of String arguments.
     */
    public static void main(String[] args) {

        //testUsingSecretKey();
        //testUsingPassPhrase();

        StringEncrypter stringEncrypter = new StringEncrypter("XKXKXK");
        System.out.println(stringEncrypter.encrypt("192.168.0.102"));
        System.out.println(stringEncrypter.decrypt(stringEncrypter.encrypt("192.168.0.102")));

    }

}