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

   public PasswordEncryptionService() {
      //todo anpassen!
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
