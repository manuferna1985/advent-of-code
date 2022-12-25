package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class Day25 extends Day {

    private static final Integer SNAFU_BASE = 5;

    private enum SnafuDigit {
        MINUS2('=', -2),
        MINUS1('-', -1),
        ZERO('0', 0),
        ONE('1', 1),
        TWO('2', 2);

        private final char letter;
        private final Integer value;

        SnafuDigit(char letter, Integer value) {
            this.letter = letter;
            this.value = value;
        }

        public static SnafuDigit ofLetter(char letter) {
            return Arrays.stream(SnafuDigit.values())
                    .filter(d -> d.letter == letter)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Wrong digit!"));
        }

        public static SnafuDigit ofValue(int value) {
            return Arrays.stream(SnafuDigit.values())
                    .filter(d -> d.value == value)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Wrong value!"));
        }

        public SnafuDigit next() {
            return ofValue(((value + 3) % 5) - 2);
        }

        @Override
        public String toString() {
            return String.valueOf(letter);
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        String[] numbers = fileContents.split(System.lineSeparator()); // when input file is multiline
        return decimalToSnafu(Arrays.stream(numbers).mapToLong(Day25::snafuToDecimal).sum());
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        String[] numbers = fileContents.split(System.lineSeparator()); // when input file is multiline

        return String.valueOf(2);
    }

    private static String decimalToSnafu(long number) {

        long remainder = number;
        StringBuilder snafu = new StringBuilder();

        long currentFactor = 1L;
        List<Long> factorPows = new ArrayList<>();
        while (true) {
            if (currentFactor <= number) {
                factorPows.add(0, currentFactor);
            } else {
                break;
            }
            currentFactor *= SNAFU_BASE;
        }
        factorPows.add(0, currentFactor);

        int index = 0;
        while (true) {
            if (remainder == 0) {
                snafu.append(String.valueOf(SnafuDigit.ZERO.letter).repeat(Math.max(0, factorPows.size() - index)));
                break;
            }
            Long factorToCheck = factorPows.get(index);

            if (remainder > 0) {
                // Positive -> substract
                int currentDigit = 0;
                while (Math.abs(remainder) >= factorToCheck / 2.0) {
                    currentDigit++;
                    remainder -= factorToCheck;
                }

                if (currentDigit > 0 || index > 0) {
                    snafu.append(SnafuDigit.ofValue(currentDigit));
                }
            } else {
                // Negative -> adding
                int currentDigit = 0;
                while (Math.abs(remainder) >= factorToCheck / 2.0) {
                    currentDigit++;
                    remainder += factorToCheck;
                }

                if (currentDigit > 0  || index > 0) {
                    snafu.append(SnafuDigit.ofValue(currentDigit * -1));
                }
            }
            index++;
        }

        return snafu.toString();
    }

    private static long snafuToDecimal(String snafu) {
        String reverseSnafu = new StringBuilder(snafu).reverse().toString();
        long factor = 1L;
        long result = 0L;

        for (int i = 0; i < reverseSnafu.length(); i++) {
            result += SnafuDigit.ofLetter(reverseSnafu.charAt(i)).value * factor;
            factor *= SNAFU_BASE;
        }

        return result;
    }

    public static void main(String[] args) {
        Day.run(Day25::new, "2022/D25_small.txt", "2022/D25_full.txt");
/*
        for (int x = 0; x < 100; x++) {
            String snafu = decimalToSnafu(x);
            long decimal = snafuToDecimal(snafu);

            System.out.printf("%-5d %-8s %-5d\n", x, snafu, decimal);
        }

 */
    }
}
