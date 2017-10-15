package md.utm.si.labs.crypto;

import org.junit.Ignore;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DesCryptoAlgorithmTest {

    DesUtil desUtil = spy(new DesUtil());
    DesCryptoAlgorithm des = new DesCryptoAlgorithm() {
        @Override
        protected DesUtil createDesUtility() {
            return desUtil;
        }
    };

    String key = "133457799BBCDFF1";
    String data = "0123456789ABCDEF";

    @Test
    public void canEncryptOneBlockMessages() {
        String encryptedData = des.encrypt(data, key);
        assertEquals("85E813540F0AB405", encryptedData);
    }

    @Test
    public void canEncryptAStringMessage() throws UnsupportedEncodingException {
        String encryptedData = des.encrypt("5661737961000000", "1234567800000000");
        assertEquals("B2BFE8B206BC4158", encryptedData);
    }

    @Test
    public void canDecryptOneBlockMessages() {
        String decryptedData = des.decrypt("85E813540F0AB405", key);
        assertEquals(data, decryptedData);
    }

    @Test
    public void canEncryptAOneBlockByteArray() throws UnsupportedEncodingException {
        byte[] key = { 0x13, 0x34, 0x57, 0x79, (byte) 0x9B, (byte) 0xBC, (byte) 0xDF, (byte) 0xF1};
        byte[] encrypted = des.encrypt("Vasya".getBytes("UTF-8"), key);
        assertEquals("1285E5F066146C66", new String(encrypted));

        String decrypted = des.decrypt("1285E5F066146C66", desUtil.toHexString(key));
        assertEquals("Vasya", desUtil.hexStringToRegularString(decrypted));

        byte[] decrypted2 = des.decrypt(encrypted, key);
        assertArrayEquals("Vasya".getBytes("UTF-8"), decrypted2);
    }

    @Test
    public void canEncryptAManyBlockStringMessage() throws UnsupportedEncodingException {
        String message = "This is a man's world! But it will be nothing";
        byte[] key = new byte[] {
                0x12, 0x34, 0x56, 0x78, 0x09, (byte) 0x87, 0x65, 0x43
        };
        byte[] encrypted = des.encrypt(message.getBytes("UTF-8"), key);
        String expected = "5BDCD5F32DB687C5BF25C5E5B7126B9AA302B9AA21F5808A53D686E8A9E3D32E95E9ED8E011CBD500AF92EF9769A2811";
        assertEquals(expected, new String(encrypted));

        byte[] decrypted = des.decrypt(encrypted, key);
        assertEquals(message, new String(decrypted));
    }
}
