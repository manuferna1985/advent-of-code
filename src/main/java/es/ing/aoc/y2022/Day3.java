package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.HashSet;
import java.util.Set;

public class Day3 extends Day {

    private static int getLetterPriority(char letter) {
        if (letter >= 97 && letter <= 122) {
            return letter - 96;
        } else {
            return letter - 38;
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        int total = 0;

        for (String sack : packages) {

            String first = sack.substring(0, sack.length() / 2);
            String second = sack.substring(sack.length() / 2);

            Set<String> commonLetters = new HashSet<>();
            for (char letter : first.toCharArray()) {
                if (second.contains(String.valueOf(letter))) {
                    commonLetters.add(String.valueOf(letter));
                }
            }

            for (String letter : commonLetters) {
                total += getLetterPriority(letter.charAt(0));
            }
        }
        return String.valueOf(total);
    }

    @Override
    protected String part2(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        int total = 0;

        int i = 0;

        do {
            String first = packages[i++];
            String second = packages[i++];
            String third = packages[i++];

            for (char letter : first.toCharArray()) {
                if (second.contains(String.valueOf(letter)) && third.contains(String.valueOf(letter))) {
                    total += getLetterPriority(letter);
                    break;
                }
            }
        } while (i < packages.length);

        return String.valueOf(total);
    }

    public static void main(String[] args) {
        Day.run(Day3::new, "2022/D3_small.txt", "2022/D3_full.txt");
    }
}
