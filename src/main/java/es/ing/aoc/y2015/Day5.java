package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day5 extends Day {

    private static final List<String> VOWELS = List.of("a", "e", "i", "o", "u");

    @Override
    protected void part1(String fileContents) throws Exception {

        AtomicInteger niceWords = new AtomicInteger();

        String[] words = fileContents.split(System.lineSeparator());

        Arrays.stream(words).forEach(word -> {
            if (rule1(word) && rule2(word) && rule3(word)){
                niceWords.getAndIncrement();
            }
        });

        System.out.println("Part 1: " + niceWords.get());
    }

    private boolean rule1(String word){
        return Arrays.stream(word.split("")).filter(VOWELS::contains).count() >= 3;
    }

    private boolean rule2(String word){
        String[] letters = word.split("");
        for (int i=0; i < letters.length - 1; i++){
            if (letters[i].equalsIgnoreCase(letters[i+1])){
                return true;
            }
        }
        return false;
    }

    private boolean rule3(String word){
        return !word.contains("ab") && !word.contains("cd") && !word.contains("pq") && !word.contains("xy");
    }

    @Override
    protected void part2(String fileContents) throws Exception {

        AtomicInteger niceWords = new AtomicInteger();

        String[] words = fileContents.split(System.lineSeparator());

        Arrays.stream(words).forEach(word -> {
            if (rule4(word) && rule5(word)){
                niceWords.getAndIncrement();
            }
        });

        System.out.println("Part 2: " + niceWords.get());

    }

    private boolean rule4(String word){
        return false; // WIP....
    }

    private boolean rule5(String word){
        return false; // WIP....
    }

    public static void main(String[] args) {
        Day.run(Day5::new, "2015/D5_small.txt", "2015/D5_full.txt");
    }
}
