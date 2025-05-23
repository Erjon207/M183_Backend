package ch.bbw.pr.tresorbackend.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * PasswordEncryptionService
 * @author Peter Rutschmann
 */
@Service
public class PasswordEncryptionService {
   public static String hashPassword(String plainTextPassword) {
      return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
   }

   public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
      return BCrypt.checkpw(plainTextPassword, hashedPassword);
   }
}
