package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class D8_Segments_Part2 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part1.class.getResource("2021/D8_full.txt").toURI()), Charset.defaultCharset());

        Long result = 0L;

        for (String line : allLines) {

            Map<Integer, String> patterns = new HashMap<>();

            String[] parts = line.split("\\|");

            System.out.println(parts[0]);

            for (String number : parts[0].trim().split(" ")) {
                int len = number.length();

                switch (len) {
                    case 2:
                        patterns.put(1, sort(number));
                        break;
                    case 3:
                        patterns.put(7, sort(number));
                        break;
                    case 4:
                        patterns.put(4, sort(number));
                        break;
                    case 7:
                        patterns.put(8, sort(number));
                        break;
                }
            }

            // Segmentos de 6 (0, 6, 9)
            List<String> sixSegments = Arrays.stream(parts[0].trim().split(" "))
                    .filter(s -> s.length() == 6)
                    .map(D8_Segments_Part2::sort)
                    .collect(Collectors.toList());

            // Segmentos de 5 (2, 3, 5)
            List<String> fiveSegments = Arrays.stream(parts[0].trim().split(" "))
                    .filter(s -> s.length() == 5)
                    .map(D8_Segments_Part2::sort)
                    .collect(Collectors.toList());

            // 9 is the only 6-segment number to contain the segments of 7 and 4
            patterns.put(9, sixSegments.stream().filter(seg -> isInside(seg, patterns.get(7)) && isInside(seg, patterns.get(4))).findFirst().get());
            sixSegments.remove(patterns.get(9));

            // 0 is the only remaining 6-segment number to contain the segments of 7
            patterns.put(0, sixSegments.stream().filter(seg -> isInside(seg, patterns.get(7))).findFirst().get());
            sixSegments.remove(patterns.get(0));

            // 6 is the only 6-segment number remaining
            patterns.put(6, sixSegments.stream().findFirst().get());
            sixSegments.remove(patterns.get(6));

            // 3 is the only 5-segment number to contain the segments of 7
            patterns.put(3, fiveSegments.stream().filter(seg -> isInside(seg, patterns.get(7))).findFirst().get());
            fiveSegments.remove(patterns.get(3));

            // To obtain five, I got the diffs of 3, 0, and 6
            // The remaining segments are inside 5's segments (but not 2's)
            String fiveCheck = diff(diff(patterns.get(3), patterns.get(0)), patterns.get(6));
            patterns.put(5, fiveSegments.stream().filter(seg -> isInside(seg, fiveCheck)).findFirst().get());
            fiveSegments.remove(patterns.get(5));

            // 2 is the only 5-segment number remaining
            patterns.put(2, fiveSegments.stream().findFirst().get());

            // ReverseMap
            Map<String, Integer> reverseMap = new HashMap<>();

            for (Map.Entry<Integer, String> entry : patterns.entrySet()) {
                reverseMap.put(entry.getValue(), entry.getKey());
            }

            // Recognize and make sum
            System.out.println(parts[1]);

            StringBuilder lineResult = new StringBuilder();
            for (String number : parts[1].trim().split(" ")) {
                String sortedNum = sort(number);
                lineResult.append(reverseMap.get(sortedNum));
            }

            result += Long.parseLong(lineResult.toString());
        }

        System.out.println("Result: " + result);

    }

    private static String sort(String text) {
        char[] chars = text.toCharArray();

        Arrays.sort(chars);
        return new String(chars);
    }

    private static Boolean isInside(String original, String check) {
        return Arrays.stream(check.split("")).allMatch(c -> {
            return original.contains(String.valueOf(c));
        });
    }

    private static String diff(String one, String two) {

        List<String> oneSplitted = List.of(one.split(""));
        List<String> twoSplitted = List.of(two.split(""));
        List<String> notCommon = new ArrayList<>();

        for (String letter : oneSplitted) {
            if (twoSplitted.contains(letter)) {
                notCommon.add(letter);
            }
        }

        for (String letter : twoSplitted) {
            if (oneSplitted.contains(letter)) {
                notCommon.add(letter);
            }
        }

        Collections.sort(notCommon);

        return String.join("", notCommon);
    }
}
