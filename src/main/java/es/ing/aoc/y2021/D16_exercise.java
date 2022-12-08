package es.ing.aoc.y2021;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D16_exercise {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D16_exercise.class.getResource("2021/D16_full.txt").toURI()), Charset.defaultCharset());

        StringBuilder binaryStr;
        for (String line : allLines) {
            System.out.println(line);

            binaryStr = new StringBuilder();
            for (String c : line.split("")) {
                binaryStr.append(hexToBin(c));
            }

            Integer[] versionsSum = {0};
            Integer[] index = {0};

            System.out.println("BinaryRepr:   " + binaryStr.toString());
            Long result = loopPackets(binaryStr.toString(), index, versionsSum);

            System.out.println("Versions sum: " + versionsSum[0]);
            System.out.println("Result:       " + result);
        }
    }

    private static String hexToBin(String s) {
        return String.format("%1$4s", new BigInteger(s, 16).toString(2)).replace(' ', '0');
    }

    private static Long loopPackets(String chain, Integer[] index, Integer[] versionsSum) {

        int version = Integer.parseInt(chain.substring(index[0], index[0] + 3), 2);
        index[0] += 3;
        int typeId = Integer.parseInt(chain.substring(index[0], index[0] + 3), 2);
        index[0] += 3;

        versionsSum[0] += version;

        if (typeId == 4) {
            // Literal value packet

            boolean lastGroup = false;
            StringBuilder value = new StringBuilder();
            do {
                String group = chain.substring(index[0], index[0] + 5);
                value.append(chain.substring(index[0] + 1, index[0] + 5));
                index[0] += 5;

                if (group.charAt(0) == '0') {
                    lastGroup = true;
                }
            } while (!lastGroup);

            return Long.parseLong(value.toString(), 2);

        } else {
            // Operator packet
            String bit = chain.substring(index[0], index[0] + 1);
            index[0]++;

            List<Long> results = new ArrayList<>();

            if (bit.equals("1")) {
                // 11-bit number representing the number of sub-packets
                int subPacketsNumber = Integer.parseInt(chain.substring(index[0], index[0] + 11), 2);
                index[0] += 11;

                for (int i = 0; i < subPacketsNumber; i++) {
                    results.add(loopPackets(chain, index, versionsSum));
                }

            } else {
                // 15-bit number representing the number of bits in the sub-packets
                int totalPacketsLength = Integer.parseInt(chain.substring(index[0], index[0] + 15), 2);
                index[0] += 15;

                Integer currentPosition = index[0];

                do {
                    results.add(loopPackets(chain, index, versionsSum));
                } while (index[0] - currentPosition < totalPacketsLength);
            }

            long result;

            switch (typeId) {
                case 0:
                    // Sum
                    result = results.stream().mapToLong(value -> value).sum();
                    break;
                case 1:
                    // Product
                    result = results.stream().mapToLong(value -> value).reduce((left, right) -> left * right).getAsLong();
                    break;
                case 2:
                    // Minimum
                    result = results.stream().mapToLong(value -> value).min().getAsLong();
                    break;
                case 3:
                    // Maximum
                    result = results.stream().mapToLong(value -> value).max().getAsLong();
                    break;
                case 5:
                    // Greater than
                    result = (results.get(0) > results.get(1)) ? 1L : 0L;
                    break;
                case 6:
                    // Less than
                    result = (results.get(0) < results.get(1)) ? 1L : 0L;
                    break;
                case 7:
                    // Equal To
                    result = results.get(0).equals(results.get(1)) ? 1L : 0L;
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            return result;
        }
    }
}
