package md.utm.si.labs.crypto;

import javafx.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DesUtilTest {

    DesUtil util = new DesUtil();

    @Test
    public void canConvertUppercaseHexStringToBinary() {
        String key = "133457799BBCDFF1";
        String binaryKey = "0001001100110100010101110111100110011011101111001101111111110001";
        assertEquals(binaryKey, util.toBinary(key));
    }

    @Test
    public void canConvertLowercaseHexStringToBinary() {
        String key = "133457799bbcdff1";
        String binaryKey = "0001001100110100010101110111100110011011101111001101111111110001";
        assertEquals(binaryKey, util.toBinary(key));
    }

    @Test
    public void canConvertBinaryStringToBisSet() {
        String binaryKey = "0001001000000000010101100000000000000000000000000000000000000001";
        assertEquals("{3, 6, 17, 19, 21, 22, 63}", util.toBitSet(binaryKey).toString());
    }

    @Test
    public void canPermuteKeyByPermutationTable() {
        String binaryKey = "0001001100110100010101110111100110011011101111001101111111110001";
        String permutedKey = util.permuteKey(binaryKey, DesUtil.FIRST_KEY_PERMUTATION_TABLE);
        assertEquals("11110000110011001010101011110101010101100110011110001111", permutedKey);
    }

    @Test
    public void canRotateLeftBitSet() {
        BitSet bitSet = util.toBitSet("010000000001");
        util.rotateLeft(bitSet, 28);
        BitSet targetBitSet = util.toBitSet("100000000010");
        assertEquals(targetBitSet, bitSet);
    }

    @Test
    public void canRotateLeftComplexBitSet() {
        BitSet bitSet = util.toBitSet("0101010101100110011110001111");
        util.rotateLeft(bitSet, 28);
        BitSet targetBitSet = util.toBitSet("1010101011001100111100011110");
        assertEquals(targetBitSet, bitSet);
    }

    @Test
    public void canRotateLeftBitSetMultipleTimes() {
        BitSet bitSet = util.toBitSet("1100001100110010101010111111");
        util.rotateLeft(bitSet, 28, 2);
        BitSet targetBitSet = util.toBitSet("0000110011001010101011111111");
        assertEquals(targetBitSet, bitSet);
    }

    @Test
    public void canConvertFromBitSetToPlainString() {
        String targetString = "1010101011001100111100011110";
        BitSet bitSet = util.toBitSet(targetString);
        String bitString = util.toString(bitSet, 28);
        assertEquals(targetString, bitString);
    }

    @Test
    public void canSplitKeyInHalf() {
        String key = "11110000110011001010101011110101010101100110011110001111";
        Pair<String, String> keyHalves = util.splitInHalf(key);
        assertEquals("1111000011001100101010101111", keyHalves.getKey());
        assertEquals("0101010101100110011110001111", keyHalves.getValue());
    }

    @Test
    public void canGenerate16SubkeysOfTheFirstStepOfTheAlgorithm() {
        String key = "11110000110011001010101011110101010101100110011110001111";
        List<String> rotatedKeys = util.produceRotatedKeys(key);
        List<String> targetRotatedKeys = makeTargetRotatedKeys();
        assertEquals(targetRotatedKeys, rotatedKeys);
    }

    private List<String> makeTargetRotatedKeys() {
        return Arrays.asList(new String[] {
                "000110110000001011101111111111000111000001110010",
                "011110011010111011011001110110111100100111100101",
                "010101011111110010001010010000101100111110011001",
                "011100101010110111010110110110110011010100011101",
                "011111001110110000000111111010110101001110101000",
                "011000111010010100111110010100000111101100101111",
                "111011001000010010110111111101100001100010111100",
                "111101111000101000111010110000010011101111111011",
                "111000001101101111101011111011011110011110000001",
                "101100011111001101000111101110100100011001001111",
                "001000010101111111010011110111101101001110000110",
                "011101010111000111110101100101000110011111101001",
                "100101111100010111010001111110101011101001000001",
                "010111110100001110110111111100101110011100111010",
                "101111111001000110001101001111010011111100001010",
                "110010110011110110001011000011100001011111110101"
        });
    }
}
