package ch.bbw.pr.tresorbackend.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * EncryptUtil
 * Used to encrypt content.
 * Not implemented yet.
 * @author Peter Rutschmann
 */
public class EncryptUtil {

   private final String secretKey;
   private static final int IV_LENGTH = 12;
   private static final int SALT_LENGTH = 16;
   private static final int GCM_TAG_LENGTH = 128;

   public EncryptUtil(String secretKey) {
      this.secretKey = secretKey;
   }

   public String encrypt(String data) throws Exception {
      byte[] iv = new byte[IV_LENGTH];
      new SecureRandom().nextBytes(iv);

      byte[] salt = new byte[SALT_LENGTH];
      new SecureRandom().nextBytes(salt);

      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmSpec);

      byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

      byte[] combined = new byte[SALT_LENGTH + IV_LENGTH + encrypted.length];
      System.arraycopy(salt, 0, combined, 0, SALT_LENGTH);
      System.arraycopy(iv, 0, combined, SALT_LENGTH, IV_LENGTH);
      System.arraycopy(encrypted, 0, combined, SALT_LENGTH + IV_LENGTH, encrypted.length);

      return Base64.getEncoder().encodeToString(combined);
   }

   public String decrypt(String encryptedData) throws Exception {
      byte[] combined = Base64.getDecoder().decode(encryptedData);

      byte[] salt = new byte[SALT_LENGTH];
      byte[] iv = new byte[IV_LENGTH];
      byte[] encrypted = new byte[combined.length - SALT_LENGTH - IV_LENGTH];

      System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
      System.arraycopy(combined, SALT_LENGTH, iv, 0, IV_LENGTH);
      System.arraycopy(combined, SALT_LENGTH + IV_LENGTH, encrypted, 0, encrypted.length);

      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt, 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmSpec);

      byte[] decrypted = cipher.doFinal(encrypted);
      return new String(decrypted, StandardCharsets.UTF_8);
   }
}