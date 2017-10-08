package md.utm.si.labs.crypto;

import org.junit.Test;

public class DesCryptoAlgorithmTest {

    DesCryptoAlgoritm des = new DesCryptoAlgoritm();
    String key = "133457799BBCDFF1";
    String data = "";

    @Test
    public void test() {
        des.encrypt(data, key);
    }
}
