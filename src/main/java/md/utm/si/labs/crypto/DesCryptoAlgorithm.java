package md.utm.si.labs.crypto;

import javafx.util.Pair;
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
            String left = messageParts.getValue();
            String right = messageParts.getKey();
            String functionResult = desUtil.applyFunctionF(left, rotatedKeys.get(i));
            right = xor(right, functionResult);
            messageParts = new Pair<>(left, right);
        }
        String roundsResult = reverseAndJoin(messageParts);
        String permutted = desUtil.permuteUsingTable(roundsResult, desUtil.INVERSE_INITIAL_PERMUTATION);
        return toHex(permutted);
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
