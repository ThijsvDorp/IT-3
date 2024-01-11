package nl.saxion.client.encryption;

import nl.saxion.client.session.KeyManager;
import nl.saxion.client.session.KeyUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.SecretKey;
import java.security.PublicKey;
public class KeyTest {
    @Test
    void testKeyGenerationAndStorage() {
        KeyManager keyManager = new KeyManager();

        // Test Key Generation
        SecretKey secretKey = KeyUtils.generateAESKey();
        assertNotNull(secretKey, "Generated SecretKey should not be null");

        // Test Storing and Retrieving Session Key
        keyManager.storeSessionKey("user1", secretKey);
        assertTrue(keyManager.hasUsername("user1"), "KeyManager should have entry for user1");
        assertEquals(secretKey, keyManager.getSessionKey("user1"), "Retrieved key should match stored key");

        // Test Removing Session Key
        assertTrue(keyManager.removeSessionKey("user1"), "Removing existing key should return true");
        assertFalse(keyManager.hasUsername("user1"), "KeyManager should no longer have entry for user1");
    }

    @Test
    void testKeyToStringConversions() throws Exception {
        KeyManager keyManager = new KeyManager();

        // Test PublicKey to String Conversion and Back
        String publicKeyStr = KeyUtils.keyToString(keyManager.getPublicKey());
        assertNotNull(publicKeyStr, "PublicKey string should not be null");
        PublicKey publicKey = KeyUtils.stringToPublicKey(publicKeyStr);
        assertNotNull(publicKey, "Converted PublicKey should not be null");
        assertEquals(publicKeyStr, KeyUtils.keyToString(publicKey), "Converted PublicKey string should match original");

        // Test SecretKey to String Conversion and Back
        SecretKey secretKey = KeyUtils.generateAESKey();
        String secretKeyStr = KeyUtils.keyToString(secretKey);
        assertNotNull(secretKeyStr, "SecretKey string should not be null");
        SecretKey convertedSecretKey = KeyUtils.stringToSecretKey(secretKeyStr);
        assertNotNull(convertedSecretKey, "Converted SecretKey should not be null");
        assertEquals(secretKeyStr, KeyUtils.keyToString(convertedSecretKey), "Converted SecretKey string should match original");
    }
}
