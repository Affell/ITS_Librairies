//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.kio.security.dataencryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeysGenerator {
    private final int keySize;
    private final String asyncAlgorithm;
    private KeyPairGenerator keyPairGenerator;
    private KeyGenerator keyGenerator;
    private SecretKey secretKey;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public KeysGenerator(int keySize, String asyncAlgorithm) {
        this.secretKey = null;
        this.publicKey = null;
        this.privateKey = null;
        this.keySize = keySize;
        this.asyncAlgorithm = asyncAlgorithm;
        this.initializeGenerators();
    }

    public KeysGenerator() {
        this(2048);
    }

    public KeysGenerator(int keySize) {
        this(keySize, "RSA/None/OAEPWithSHA-256AndMGF1Padding");
    }

    private void initializeGenerators() {
        try {
            this.keyGenerator = KeyGenerator.getInstance("AES");
            this.keyGenerator.init(128);
            this.keyPairGenerator = KeyPairGenerator.getInstance(this.asyncAlgorithm.split("/")[0]);
            this.keyPairGenerator.initialize(this.keySize);
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
        }

    }

    public void generateKeys(boolean secretKey, boolean asynchronousKeys) {
        if (secretKey) {
            this.secretKey = this.keyGenerator.generateKey();
        }

        if (asynchronousKeys) {
            KeyPair keyPair = this.keyPairGenerator.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        }

    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getStringPublicKey() {
        return Base64.getEncoder().encodeToString(this.getPublicKey().getEncoded());
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String getAsyncAlgorithm() {
        return asyncAlgorithm;
    }

    public KeysGenerator clone() throws CloneNotSupportedException {
        return (KeysGenerator) super.clone();
    }
}
