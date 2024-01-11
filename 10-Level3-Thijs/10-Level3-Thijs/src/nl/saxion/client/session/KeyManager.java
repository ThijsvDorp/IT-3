package nl.saxion.client.session;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyManager {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private final Map<String, SecretKey> sessionKeys;
    public KeyManager() {
        this.sessionKeys = new HashMap<>();
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            KeyPair pair = keyPairGen.generateKeyPair();

            this.publicKey = pair.getPublic();
            this.privateKey = pair.getPrivate();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void storeSessionKey(String username, SecretKey sessionKey) {
        sessionKeys.put(username, sessionKey);
    }

    public boolean removeSessionKey(String username){
        if (sessionKeys.containsKey(username)){
            sessionKeys.remove(username);
            return true;
        }
        return false;
    }

    public boolean hasUsername(String username){
        return sessionKeys.containsKey(username);
    }

    public SecretKey getSessionKey(String username) {
        return sessionKeys.get(username);
    }

    public PublicKey getPublicKey(){
        return publicKey;
    }




}
