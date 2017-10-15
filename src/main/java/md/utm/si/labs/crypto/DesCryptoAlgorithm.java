package md.utm.si.labs.crypto;

import javafx.util.Pair;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.BitSet;

import java.util.List;

public class DesCryptoAlgorithm implements CryptoAlgorithm {

    private DesUtil desUtil = createDesUtility();

    protected DesUtil createDesUtility() {
        return new DesUtil();
    }

    @Override
    public String encrypt(String hexMessage, String hexKey) {
        String binaryKey = desUtil.toBinary(hexKey);
        String binaryMessage = desUtil.toBinary(hexMessage);
        String permutedKey = desUtil.permuteUsingTable(binaryKey, DesUtil.FIRST_KEY_PERMUTATION_TABLE);
        List<String> rotatedKeys = desUtil.produceRotatedKeys(permutedKey);
        String permuttedMessage = desUtil.permuteUsingTable(binaryMessage, DesUtil.INITIAL_MESSAGE_PERMUTATION);
        Pair<String, String> messageParts = desUtil.splitInHalf(permuttedMessage);
        for (int i = 0; i < rotatedKeys.size(); ++i) {
            messageParts = performIteration(messageParts, rotatedKeys.get(i));
        }
        String roundsResult = reverseAndJoin(messageParts);
        String permutted = desUtil.permuteUsingTable(roundsResult, desUtil.INVERSE_INITIAL_PERMUTATION);
        return toHex(permutted);
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key) {
        List<byte[]> dataBlocks = desUtil.makeBlocks(data);
        String hexKey = desUtil.toHexString(key);
        StringBuilder result = new StringBuilder();
        for (byte[] block : dataBlocks) {
            String hexData = desUtil.toHexString(block);
            String encrypted = encrypt(hexData, hexKey);
            result.append(encrypted);
        }
        return getEncryptedData(result.toString());
    }

    private byte[] getEncryptedData(String data) {
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<String, String> performIteration(Pair<String, String> messageParts, String rotatedKey) {
        String left = messageParts.getValue();
        String right = messageParts.getKey();
        String functionResult = desUtil.applyFunctionF(left, rotatedKey);
        right = xor(right, functionResult);
        return new Pair<>(left, right);
    }

    @Override
    public String decrypt(String hexMessage, String hexKey) {
        String binaryKey = desUtil.toBinary(hexKey);
        String binaryMessage = desUtil.toBinary(hexMessage);
        String permutedKey = desUtil.permuteUsingTable(binaryKey, DesUtil.FIRST_KEY_PERMUTATION_TABLE);
        List<String> rotatedKeys = desUtil.produceRotatedKeys(permutedKey);
        String permuttedMessage = desUtil.permuteUsingTable(binaryMessage, DesUtil.INITIAL_MESSAGE_PERMUTATION);
        Pair<String, String> messageParts = desUtil.splitInHalf(permuttedMessage);
        for (int i = rotatedKeys.size() - 1; i >= 0; --i) {
            messageParts = performIteration(messageParts, rotatedKeys.get(i));
        }
        String roundsResult = reverseAndJoin(messageParts);
        String permutted = desUtil.permuteUsingTable(roundsResult, desUtil.INVERSE_INITIAL_PERMUTATION);
        return toHex(permutted);
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key) {
        List<byte[]> dataBlocks = desUtil.makeBlocks(data, 16);
        String hexKey = desUtil.toHexString(key);
        StringBuilder result = new StringBuilder();
        for (byte[] block : dataBlocks) {
            String hexData = new String(block);
            String decrypted = decrypt(hexData, hexKey);
            result.append(decrypted);
        }
        return getEncryptedData(desUtil.hexStringToRegularString(result.toString()));
    }

    private String toHex(String binaryString) {
        String[] fourBitBlocks = to4BitBlocks(binaryString);
        StringBuilder hexString = new StringBuilder();
        for (String block : fourBitBlocks)
            hexString.append(desUtil.binaryToHex(block));
        return hexString.toString();
    }

    private String[] to4BitBlocks(String binaryString) {
        final int BLOCK_SIZE = 4;
        int numberOfBlocks = binaryString.length() / 4;
        String[] blocks = new String[numberOfBlocks];
        for (int i = 0; i < numberOfBlocks; ++i) {
            blocks[i] = binaryString.substring(i * BLOCK_SIZE, i * BLOCK_SIZE + BLOCK_SIZE);
        }
        return blocks;
    }

    private String reverseAndJoin(Pair<String, String> pair) {
        return pair.getValue() + pair.getKey();
    }

    private String xor(String first, String second) {
        BitSet firstSet = desUtil.toBitSet(first);
        BitSet secondSet = desUtil.toBitSet(second);
        return desUtil.toString(desUtil.xorBitSets(firstSet, secondSet), 32);
    }
}
