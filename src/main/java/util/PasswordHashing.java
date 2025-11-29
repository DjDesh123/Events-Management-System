package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHashing {

    private String salt;

    // hashing constructor
    public PasswordHashing() {
        SecureRandom random = new SecureRandom();

        byte[] saltByte = new byte[16];

        random.nextBytes(saltByte);

        this.salt = Base64.getEncoder().encodeToString(saltByte);
    }

    // converts the bytes into a string
    public String getSalt(){
        return salt;
    }

    // hashes password
    public String hashingPassword(String password, String salt){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] saltBytes = Base64.getDecoder().decode(salt.trim());

            md.update(saltBytes);

            byte[] passwordBytes = password.getBytes();

            byte[] hashedBytes = md.digest(passwordBytes);


            return Base64.getEncoder().encodeToString(hashedBytes);
        }catch(NoSuchAlgorithmException e ){
            throw new RuntimeException("Error hashing handling",e);
        }
    }

}
