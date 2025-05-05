package ch.bbw.pr.tresorbackend.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordEncryptionService
 * @author Peter Rutschmann
 */
@Service
public class PasswordEncryptionService {
   //todo ergänzen!

   public boolean checkPassword(String rawPassword, String hashedPassword) {
      try {
         String hashedInput = hashPassword(rawPassword);
         return hashedInput.equals(hashedPassword); //Es gibt keine decryption nur einen vergleich im gehashtem zustand
      } catch (Exception e) {
         //logger hinzufügen
         return false;
      }
   }

   public String hashPassword(String password) throws Exception {
      try {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

         StringBuilder hexString = new StringBuilder();
         for (byte b : encodedHash) {
            hexString.append(String.format("%02x", b));
         }

         return hexString.toString();
      } catch (Exception e) {
         //logger hinzufügen
         throw new Exception("SHA-256 algorithm not found", e);
      }
   }
}
