package g15.pas.utils;

import org.junit.jupiter.api.Test;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.*;

public class DiffieHellmanTest {

    @Test
    public void testGeneratePrivateKey() {
        try {
            BigInteger privateKey = DiffieHellman.generatePrivateKey();

            assertNotNull(privateKey, "A chave privada não deve ser nula");
        } catch (NoSuchAlgorithmException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testGeneratePublicKey() {
        try {
            BigInteger privateKey = DiffieHellman.generatePrivateKey();

            BigInteger publicKey = DiffieHellman.generatePublicKey(privateKey);

            assertNotNull(publicKey, "A chave pública não deve ser nula");
        } catch (NoSuchAlgorithmException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testComputeSecret() {
        try {
            BigInteger privateKeyAlice = DiffieHellman.generatePrivateKey();
            BigInteger privateKeyBob = DiffieHellman.generatePrivateKey();

            BigInteger publicKeyAlice = DiffieHellman.generatePublicKey(privateKeyAlice);
            BigInteger publicKeyBob = DiffieHellman.generatePublicKey(privateKeyBob);

            BigInteger secretAlice = DiffieHellman.computeSecret(publicKeyBob, privateKeyAlice);
            BigInteger secretBob = DiffieHellman.computeSecret(publicKeyAlice, privateKeyBob);

            assertEquals(secretAlice, secretBob, "Os segredos compartilhados devem ser iguais para Alice e Bob");
        } catch (NoSuchAlgorithmException e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

}
