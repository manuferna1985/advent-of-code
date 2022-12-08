package es.ing.aoc.y2021;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class D14_exercise_Part2 {

    public static class Pair {

        String left;
        String right;

        public Pair(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D14_exercise_Part2.class.getResource("2021/D14_full.txt").toURI()), Charset.defaultCharset());

        // Initial structures
        StringBuilder template = new StringBuilder(allLines.get(0));

        List<Pair> rules = allLines.stream().filter(c -> c.contains("->")).map(c ->
                new Pair(
                        c.substring(0, c.indexOf("-")).trim(),
                        c.substring(c.indexOf(">") + 1).trim())
        ).collect(Collectors.toList());

        Map<String, Pair> rulesMap = new HashMap<>();
        rules.forEach(r -> rulesMap.put(r.left, r));

        Map<String, BigInteger> pairTimes = new HashMap<>();
        Map<String, BigInteger> times = new HashMap<>();

        Arrays.stream(template.toString().split("")).forEach(letter -> addOcurrence(times, letter));

        // Add initial step combinations
        for (int i = 0; i < template.length() - 1; i++) {
            String current = template.substring(i, i + 2);

            if (pairTimes.containsKey(current)) {
                pairTimes.put(current, pairTimes.get(current).add(BigInteger.ONE));
            } else {
                pairTimes.put(current, BigInteger.ONE);
            }
        }

        // Main loop
        for (int step = 0; step < 40; step++) {
            Map<String, BigInteger> nextPairTimes = new HashMap<>();

            // Loop current iteration combination to calculate next step ones.
            pairTimes.forEach((key, value) -> {

                if (rulesMap.containsKey(key)) {
                    // AB is found (AB -> C), then combinations AC & CB are added, and letter C is added N times.
                    Pair rule = rulesMap.get(key);
                    String a = key.charAt(0) + rule.right;
                    String b = rule.right + key.charAt(1);

                    // Add AC combination
                    addOcurrenceWith(nextPairTimes, a, value);

                    // Add CB combination
                    addOcurrenceWith(nextPairTimes, b, value);

                    // Add C letter N times
                    addOcurrenceWith(times, rule.right, value);
                } else {
                    nextPairTimes.put(key, value);
                    addOcurrenceWith(nextPairTimes, key, value);
                }
            });

            pairTimes = nextPairTimes;
        }

        Optional<BigInteger> max = times.values().stream().max(Comparator.naturalOrder());
        Optional<BigInteger> min = times.values().stream().min(Comparator.naturalOrder());

        if (max.isPresent() && min.isPresent()) {
            System.out.println(max.get().subtract(min.get()));
        }
    }

    private static void addOcurrence(Map<String, BigInteger> times, String letter) {
        addOcurrenceWith(times, letter, BigInteger.ONE);
    }

    private static void addOcurrenceWith(Map<String, BigInteger> times, String letter, BigInteger initial) {
        if (times.containsKey(letter)) {
            times.put(letter, times.get(letter).add(initial));
        } else {
            times.put(letter, initial);
        }
    }
}
