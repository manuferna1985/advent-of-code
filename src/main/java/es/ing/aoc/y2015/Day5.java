package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day5 extends Day {

    private static final List<String> VOWELS = List.of("a", "e", "i", "o", "u");

    @Override
    protected String part1(String fileContents) throws Exception {

        AtomicInteger niceWords = new AtomicInteger();

        Arrays.stream(fileContents.split(System.lineSeparator())).forEach(word -> {
            if (rule1(word) && rule2(word) && rule3(word)) {
                niceWords.getAndIncrement();
            }
        });

        return String.valueOf(niceWords.get());
    }

    @Override
    protected String part2(String fileContents) throws Exception {

        AtomicInteger niceWords = new AtomicInteger();

        Arrays.stream(fileContents.split(System.lineSeparator())).forEach(word -> {
            if (rule4(word) && rule5(word)) {
                niceWords.getAndIncrement();
            }
        });

        return String.valueOf(niceWords.get());
    }

    private boolean rule1(String word) {
        return Arrays.stream(word.split("")).filter(VOWELS::contains).count() >= 3;
    }

    private boolean rule2(String word) {
        String[] letters = word.split("");
        for (int i = 0; i < letters.length - 1; i++) {
            if (letters[i].equalsIgnoreCase(letters[i + 1])) {
                return true;
            }
        }
        return false;
    }

    private boolean rule3(String word) {
        return !word.contains("ab") && !word.contains("cd") && !word.contains("pq") && !word.contains("xy");
    }

    private boolean rule4(String word) {
        final int pairLength = 2;
        AtomicInteger i = new AtomicInteger();

        for (; i.get() < word.length() - pairLength + 1; i.getAndIncrement()) {
            String pairToSearch = word.substring(i.get(), i.get() + pairLength);

            List<Integer> list = new ArrayList<>();
            Matcher matcher = Pattern.compile(pairToSearch).matcher(word);
            while (matcher.find()) {
                list.add(matcher.start());
            }

            if (list.size() >= 2 && list.stream().anyMatch(x -> Math.abs(i.get() - x) >= pairLength)) {
                return true;
            }
        }

        return false;
    }

    private boolean rule5(String word) {
        for (int i = 0; i < word.length() - 2; i++) {
            if (word.charAt(i) == word.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Day.run(Day5::new, "2015/D5_small.txt", "2015/D5_full.txt");
    }
}
