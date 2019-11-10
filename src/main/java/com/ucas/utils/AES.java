package com.ucas.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AES {
    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    public static final byte[] iv = "abcdefghijklmnop".getBytes();

    public static byte[] encrypt(byte[] data, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    public static SecretKey GetKey(String seed) throws Exception{
        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes());
        keygen.init(128, secureRandom);
        SecretKey original_key=keygen.generateKey();

        byte [] raw=original_key.getEncoded();

        return new SecretKeySpec(raw, "AES");
    }
// demo
//    public static void main(String[] args) throws Exception {
//        byte[] text = "Hello, AES cipher!".getBytes(StandardCharsets.UTF_8);
//
//        byte[] cipherText = encrypt(text, GetKey("hello"), iv);
//        String base64String = Base64.getEncoder().encodeToString(cipherText);
//        System.out.println(base64String);
//
//        byte[] plainText = decrypt(Base64.getDecoder().decode(base64String), GetKey("hello"), iv);
//        System.out.println(new String(plainText, StandardCharsets.UTF_8));
//    }

}
