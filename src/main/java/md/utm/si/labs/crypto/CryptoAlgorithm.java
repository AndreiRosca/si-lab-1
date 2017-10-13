package md.utm.si.labs.crypto;

public interface CryptoAlgorithm {
   String encrypt(String data, String key);
   byte[] encrypt(byte[] data, byte[] key);
   String decrypt(String data, String key);
   byte[] decrypt(byte[] data, byte[] key);
}
