package eu.concept.util.other;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christos Paraskeva
 */
public class Util {

    /**
     * Encodes a String based on a specific algorithm
     *
     * @param content The String to encrypt
     * @param algorithm The algorithm to be used
     * @return The encoded String
     */
    public static String createAlgorithm(String content, String algorithm) {
        String encryptedContent = "";
        try {
            //Producing the SHA hash for the input
            MessageDigest m;
            m = MessageDigest.getInstance(algorithm);
            m.update(content.getBytes(), 0, content.length());
            encryptedContent = new BigInteger(1, m.digest()).toString(16);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Util.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedContent;
    }

    public static String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }

    public static void main(String[] args) {
        String sha1 = Util.createAlgorithm("ttest", "SHA");
        System.out.println("SHA1: " + sha1);
        //String salt = "377aef61316588f56555fe2de599f40a";
        String salt = Util.getRandomHexString(32);
        
        String sha1_salt = Util.createAlgorithm(salt + sha1, "SHA");
        System.out.println("Salt= " + salt);
        System.out.println("salt+sha1=" +salt + sha1);
        System.out.println("SHA1(salt+SHA1(palin_text)) =  " + sha1_salt);

    }

}
