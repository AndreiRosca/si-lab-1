package md.utm.si.labs.crypto;

import java.util.BitSet;
import java.util.List;

public class DesCryptoAlgoritm implements CryptoAlgorithm {

    DesUtil desUtil = new DesUtil();

    @Override
    public byte[] encrypt(String data, String hexKey) {
        String binaryKey = desUtil.toBinary(hexKey);
        String permutedKey = desUtil.permuteKey(binaryKey, DesUtil.FIRST_KEY_PERMUTATION_TABLE);
        List<String> rotatedKeys = desUtil.produceRotatedKeys(permutedKey);
        return new byte[0];
    }
}
