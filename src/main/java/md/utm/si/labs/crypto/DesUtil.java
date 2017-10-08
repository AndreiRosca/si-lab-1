package md.utm.si.labs.crypto;

import javafx.util.Pair;

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

    public String permuteKey(String binaryKey, int[] bitPermutationTable) {
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

    public Pair<String,String> splitInHalf(String key) {
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
            String permutedKey = permuteKey(joinedKey, SECOND_KEY_PERMUTATION_TABLE);
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
}
