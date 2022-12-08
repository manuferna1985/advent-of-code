package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class D14_exercise_Part1 {

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
                Paths.get(D14_exercise_Part1.class.getResource("2021/D14_small.txt").toURI()), Charset.defaultCharset());


        StringBuilder template = new StringBuilder(allLines.get(0));

        List<Pair> rules = allLines.stream().filter(c -> c.contains("->")).map(c -> {
            return new Pair(c.substring(0, c.indexOf("-")).trim(), c.substring(c.indexOf(">") + 1).trim());
        }).collect(Collectors.toList());


        Map<String, Pair> rulesMap = new HashMap<>();
        rules.forEach(r -> rulesMap.put(r.left, r));

        for (int step = 0; step < 10; step++) {
            for (int i = 0; i < template.length() - 1; i++) {
                String current = template.substring(i, i + 2);
                if (rulesMap.containsKey(current)) {
                    String insertion = rulesMap.get(current).right;
                    template.insert(i + 1, insertion);
                    i++;
                }
            }
        }

        Map<String, Integer> times = new HashMap<>();

        Arrays.stream(template.toString().split("")).forEach(letter -> {
            if (times.containsKey(letter)){
                times.put(letter, times.get(letter) + 1);
            } else {
                times.put(letter, 1);
            }
        });


        final Integer[] min = {Integer.MAX_VALUE};
        final Integer[] max = {Integer.MIN_VALUE};
        times.values().forEach(t -> {
            if (t < min[0]){
                min[0] = t;
            }
            if (t > max[0]){
                max[0] = t;
            }
        });

        System.out.println(max[0] - min[0]);
    }
}
