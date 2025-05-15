package ch.bbw.pr.tresorbackend.util;

import org.jasypt.util.text.AES256TextEncryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * EncryptUtil
 * Used to encrypt content.
 * Not implemented yet.
 * @author Peter Rutschmann
 */
public class EncryptUtil {

   String secretKey;

   public EncryptUtil(String secretKey) {
      this.secretKey = secretKey;
   }

   public String encrypt(String data) throws Exception {
      byte[] iv = new byte[16];
      new SecureRandom().nextBytes(iv);
      IvParameterSpec ivSpec = new IvParameterSpec(iv);

      byte[] salt = "1234567890abcdef".getBytes(); // You may replace with real random salt
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
      byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

      byte[] combined = new byte[iv.length + encrypted.length];
      System.arraycopy(iv, 0, combined, 0, iv.length);
      System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

      return Base64.getEncoder().encodeToString(combined);
   }

   public String decrypt(String encryptedData) throws Exception {
      byte[] combined = Base64.getDecoder().decode(encryptedData);
      byte[] iv = new byte[16];
      byte[] encrypted = new byte[combined.length - 16];

      System.arraycopy(combined, 0, iv, 0, 16);
      System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

      IvParameterSpec ivSpec = new IvParameterSpec(iv);

      byte[] salt = "1234567890abcdef".getBytes(); // Same salt as used in encrypt()
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
      byte[] decrypted = cipher.doFinal(encrypted);

      return new String(decrypted, StandardCharsets.UTF_8);
   }
}
