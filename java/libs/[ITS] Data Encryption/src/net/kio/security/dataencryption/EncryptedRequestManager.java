//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.kio.security.dataencryption;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptedRequestManager {
    public EncryptedRequestManager() {
    }

    public static String encrypt(@NotNull String data, @NotNull SecretKey secretKey) {
        try {
            return encrypt0(data, secretKey);
        } catch (InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static String encrypt0(String data, SecretKey secretKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Cipher aesCipher = Cipher.getInstance(secretKey.getAlgorithm());
        aesCipher.init(1, secretKey);
        byte[] byteCipherText = aesCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.encode(byteCipherText);
    }

    public static SecretKey decryptSecretKey(String data, PrivateKey privateKey) {
        try {
            return decryptSecretKey0(Base64.decode(data), privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static SecretKey decryptSecretKey0(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(2, privateKey);
        byte[] decryptedKey = cipher.doFinal(data);
        return new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
    }

    public static String encryptSecretKey(SecretKey secretKey, PublicKey publicKey) {
        try {
            return Base64.encode(encryptSecretKey0(secretKey, publicKey));
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static byte[] encryptSecretKey0(SecretKey secretKey, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm() + "/None/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(1, publicKey);
        return cipher.doFinal(secretKey.getEncoded());
    }

    public static String decrypt(String data, SecretKey secretKey) {
        try {
            return decrypt0(Base64.decode(data), secretKey);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NullPointerException | IllegalArgumentException var3) {
            return null;
        }
    }

    private static String decrypt0(byte[] data, SecretKey secretKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, NullPointerException, IllegalArgumentException {
        Cipher aesCipher = Cipher.getInstance(secretKey.getAlgorithm() + "/None/OAEPWithSHA-256AndMGF1Padding");
        aesCipher.init(2, secretKey);
        byte[] bytePlainText = aesCipher.doFinal(data);
        return new String(bytePlainText, StandardCharsets.UTF_8);
    }
}

