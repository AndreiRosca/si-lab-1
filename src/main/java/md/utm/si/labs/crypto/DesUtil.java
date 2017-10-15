package md.utm.si.labs.crypto;

import javafx.util.Pair;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class DesUtil {
    private static final int HALF_KEY_LENGTH = 28;

    public static final int[] FIRST_KEY_PERMUTATION_TABLE = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4,
    };

    public static final int[] SECOND_KEY_PERMUTATION_TABLE = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    public static final int[] INITIAL_MESSAGE_PERMUTATION = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    public static final int[] E_FUNCTION_SELECTION_TABLE = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    public static final int[][] FIRST_S_BOX = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    public static final int[][] SECOND_S_BOX = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
    };

    public static final int[][] THIRD_S_BOX = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
    };

    public static final int[][] FOURTH_S_BOX = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
    };

    public static final int[][] FIFTH_S_BOX = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
    };

    public static final int[][] SIXTH_S_BOX = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
    };

    public static final int[][] SEVENTH_S_BOX = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},
    };

    public static final int[][] EIGHTH_S_BOX = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11},
    };

    public static final int[] P_PERMUATTION_TABLE = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    public static final int[] INVERSE_INITIAL_PERMUTATION = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private static final Map<String, String> binaryDigits = new HashMap<>();

    static {
        binaryDigits.put("0", "0000");
        binaryDigits.put("1", "0001");
        binaryDigits.put("2", "0010");
        binaryDigits.put("3", "0011");
        binaryDigits.put("4", "0100");
        binaryDigits.put("5", "0101");
        binaryDigits.put("6", "0110");
        binaryDigits.put("7", "0111");
        binaryDigits.put("8", "1000");
        binaryDigits.put("9", "1001");
        binaryDigits.put("A", "1010");
        binaryDigits.put("B", "1011");
        binaryDigits.put("C", "1100");
        binaryDigits.put("D", "1101");
        binaryDigits.put("E", "1110");
        binaryDigits.put("F", "1111");
    }

    private static final Map<Integer, Integer> keyRotations = new HashMap<>();

    static {
        keyRotations.put(1, 1);
        keyRotations.put(2, 1);
        keyRotations.put(3, 2);
        keyRotations.put(4, 2);
        keyRotations.put(5, 2);
        keyRotations.put(6, 2);
        keyRotations.put(7, 2);
        keyRotations.put(8, 2);
        keyRotations.put(9, 1);
        keyRotations.put(10, 2);
        keyRotations.put(11, 2);
        keyRotations.put(12, 2);
        keyRotations.put(13, 2);
        keyRotations.put(14, 2);
        keyRotations.put(15, 2);
        keyRotations.put(16, 1);
    }

    private static final Map<Integer, int[][]> sBoxes = new HashMap<>();

    static {
        sBoxes.put(1, FIRST_S_BOX);
        sBoxes.put(2, SECOND_S_BOX);
        sBoxes.put(3, THIRD_S_BOX);
        sBoxes.put(4, FOURTH_S_BOX);
        sBoxes.put(5, FIFTH_S_BOX);
        sBoxes.put(6, SIXTH_S_BOX);
        sBoxes.put(7, SEVENTH_S_BOX);
        sBoxes.put(8, EIGHTH_S_BOX);
    }

    public DesUtil() {
    }

    public String toBinary(String hexString) {
        StringBuilder binaryKey = new StringBuilder();
        for (String hexDigit : hexString.toUpperCase().split("")) {
            binaryKey.append(hexDigitToBinary(hexDigit));
        }
        return binaryKey.toString();
    }

    private String hexDigitToBinary(String hexDigit) {
        return binaryDigits.get(hexDigit);
    }

    public BitSet toBitSet(String binaryString) {
        BitSet bitSet = new BitSet();
        int bitIndex = 0;
        for (String binaryDigit : binaryString.split("")) {
            if (binaryDigitIsOne(binaryDigit))
                bitSet.set(bitIndex);
            ++bitIndex;
        }
        return bitSet;
    }

    private boolean binaryDigitIsOne(String binaryDigit) {
        return binaryDigit.equals("1");
    }

    public String permuteUsingTable(String binaryKey, int[] bitPermutationTable) {
        StringBuilder permutedKey = new StringBuilder();
        for (int bitIndex : bitPermutationTable) {
            permutedKey.append(binaryKey.charAt(bitIndex - 1));
        }
        return permutedKey.toString();
    }

    public void rotateLeft(BitSet bitSet, int numberOfBits) {
        BitSet inputSet = (BitSet) bitSet.clone();
        bitSet.clear();
        int highestBitIndex = numberOfBits - 1;
        bitSet.set(highestBitIndex, inputSet.get(0));
        inputSet.clear(0);
        for (int i = inputSet.nextSetBit(0); i >= 0; i = inputSet.nextSetBit(i + 1)) {
            bitSet.set(i - 1, inputSet.get(i));
        }
    }

    public void rotateLeft(BitSet bitSet, int numberOfBits, int rotateCount) {
        for (int i = 0; i < rotateCount; ++i)
            rotateLeft(bitSet, numberOfBits);
    }

    public String toString(BitSet bitSet, int length) {
        StringBuilder bitString = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            bitString.append(bitToString(bitSet.get(i)));
        }
        return bitString.toString();
    }

    public String toString(BitSet bitSet) {
        return toString(bitSet, HALF_KEY_LENGTH);
    }

    private String bitToString(boolean bit) {
        return bit ? "1" : "0";
    }

    public Pair<String, String> splitInHalf(String key) {
        int middleIndex = key.length() / 2;
        String firstHalf = key.substring(0, middleIndex);
        String secondHalf = key.substring(middleIndex);
        return new Pair<>(firstHalf, secondHalf);
    }

    public List<String> produceRotatedKeys(String initialKey) {
        List<String> rotatedKeys = new ArrayList<>();
        Pair<String, String> splittedKey = splitInHalf(initialKey);
        for (int i = 0; i < keyRotations.size(); ++i) {
            Pair<String, String> rotatedKeyParts = rotateSplittedKey(splittedKey, i);
            String joinedKey = joinKey(rotatedKeyParts);
            String permutedKey = permuteUsingTable(joinedKey, SECOND_KEY_PERMUTATION_TABLE);
            rotatedKeys.add(permutedKey);
            splittedKey = splitInHalf(joinedKey);
        }
        return rotatedKeys;
    }

    private String joinKey(Pair<String, String> keyParts) {
        return keyParts.getKey() + keyParts.getValue();
    }

    private Pair<String, String> rotateSplittedKey(Pair<String, String> splittedKey, int index) {
        BitSet firstKeyPart = toBitSet(splittedKey.getKey());
        BitSet secondKeyPart = toBitSet(splittedKey.getValue());
        rotateLeft(firstKeyPart, HALF_KEY_LENGTH, keyRotations.get(index + 1));
        rotateLeft(secondKeyPart, HALF_KEY_LENGTH, keyRotations.get(index + 1));
        return new Pair<>(toString(firstKeyPart), toString(secondKeyPart));
    }

    public BitSet xorBitSets(BitSet first, BitSet second) {
        BitSet result = (BitSet) first.clone();
        result.xor(second);
        return result;
    }

    public String[] toSixBitBlocks(String input) {
        final int BLOCK_SIZE = 6;
        int numberOfBlocks = input.length() / BLOCK_SIZE;
        String[] blocks = new String[numberOfBlocks];
        for (int i = 0; i < numberOfBlocks; ++i) {
            blocks[i] = input.substring(i * BLOCK_SIZE, i * BLOCK_SIZE + BLOCK_SIZE);
        }
        return blocks;
    }

    public String calculateSBoxes(String[] blocks) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < blocks.length; ++i) {
            String block = blocks[i];
            int row = getRowValue(block);
            int col = getColumnValue(block);
            int[][] sBox = getSBox(i + 1);
            int sBoxValue = sBox[row][col];
            result.append(binaryDigits.get(toHex(sBoxValue)));
        }
        return result.toString();
    }

    private String toHex(int value) {
        return Integer.toHexString(value).toUpperCase();
    }

    private int[][] getSBox(int i) {
       return sBoxes.get(i);
    }

    private int getColumnValue(String block) {
        String index = block.substring(1, block.length() - 1);
        return Integer.parseInt(index, 2);
    }

    private int getRowValue(String block) {
        String index = "" + block.charAt(0) + block.charAt(block.length() - 1);
        return Integer.parseInt(index, 2);
    }

    public String applyFunctionF(String messagePart, String key) {
        String expandedPart = permuteUsingTable(messagePart, DesUtil.E_FUNCTION_SELECTION_TABLE);
        BitSet keyBits = toBitSet(key);
        BitSet messageBits = toBitSet(expandedPart);
        BitSet xorResult = xorBitSets(keyBits, messageBits);
        String[] bitBlocks = toSixBitBlocks(toString(xorResult, 48));
        String sBoxesResult = calculateSBoxes(bitBlocks);
        return permuteUsingTable(sBoxesResult, P_PERMUATTION_TABLE);
    }

    public String binaryToHex(String binaryNumber) {
        for (Map.Entry<String, String> entry : binaryDigits.entrySet()) {
            if (entry.getValue().equals(binaryNumber))
                return entry.getKey();
        }
        return null;
    }

    public String hexStringToRegularString(String hexString) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < hexString.length(); i += 2) {
            if (hexString.charAt(i) == '0' && hexString.charAt(i + 1) == '0')
                break;
            String hexByte = "" + hexString.charAt(i) + hexString.charAt(i + 1);
            result.append(Character.toString((char) Integer.parseInt(hexByte, 16)));
        }
        return result.toString();
    }

    public String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hexDigit = Integer.toHexString(b & 0xff);
            if (binaryDigits.containsKey(hexDigit))
                hexDigit = "0" + hexDigit;
            hexString.append(hexDigit.toUpperCase());
        }
        return hexString.toString();
    }

    private byte[] makeBlock(byte[] bytes, int blockIndex, int blockSize) {
        byte[] block = new byte[blockSize];
        System.arraycopy(bytes, blockIndex * blockSize, block, 0, blockSize);
        return block;
    }

    public List<byte[]> makeBlocks(byte[] messageBytes) {
        return makeBlocks(messageBytes, 8);
    }

    public String asHexString(byte[] data) {
        return null;
    }

    public List<byte[]> makeBlocks(byte[] messageBytes, int blockSize) {
        List<byte[]> blocks = new ArrayList<>();
        int numberOfBlocks = messageBytes.length / blockSize;
        int remainingBytes = messageBytes.length % blockSize;
        for (int i = 0; i < numberOfBlocks; ++i) {
            blocks.add(makeBlock(messageBytes, i,blockSize));
        }
        if (remainingBytes != 0) {
            byte[] block = new byte[blockSize];
            System.arraycopy(messageBytes, numberOfBlocks * blockSize, block, 0, remainingBytes);
            blocks.add(block);
        }
        return blocks;
    }
}
