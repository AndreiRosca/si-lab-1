package md.utm.si.labs.crypto;

public interface CryptoAlgorithm {
    byte[] encrypt(String data, String key);
}
