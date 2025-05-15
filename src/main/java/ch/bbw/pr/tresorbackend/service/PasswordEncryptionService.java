package ch.bbw.pr.tresorbackend.service;

import ch.bbw.pr.tresorbackend.util.EncryptUtil;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

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
      EncryptUtil encryptUtil = new EncryptUtil(password);

      return encryptUtil.encrypt(password);
   }
}
