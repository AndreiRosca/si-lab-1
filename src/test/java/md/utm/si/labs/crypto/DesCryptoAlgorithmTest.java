package md.utm.si.labs.crypto;

import org.junit.Test;

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
    public void test() {
        String encryptedData = des.encrypt(data, key);
        assertEquals("85E813540F0AB405", encryptedData);
    }
}
