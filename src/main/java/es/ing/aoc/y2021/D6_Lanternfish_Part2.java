package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D6_Lanternfish_Part2 {

    public static void main(String[] args) throws Exception {

        Instant start = Instant.now();

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part2.class.getResource("2021/D6_full.txt").toURI()), Charset.defaultCharset());

        List<Byte> fishes = Arrays.stream(allLines.get(0).split(","))
                .map(Byte::parseByte)
                .collect(Collectors.toList());

        System.out.println(fishes.size());

        Long[] daysToGrow = {0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L};

        fishes.forEach(f -> daysToGrow[f]++);

        for (int r = 0; r < 256; r++) {
            long newFishes = daysToGrow[0];
            for (int i = 0; i < 8; i++) {
                daysToGrow[i] = daysToGrow[i+1];
            }
            daysToGrow[8] = newFishes;
            daysToGrow[6] += newFishes;
        }

        System.out.println(Arrays.stream(daysToGrow).mapToLong(Long::longValue).sum());

        Instant end = Instant.now();
        System.out.printf("Time: %dms%n", Duration.between(start, end).toMillis());

    }
}
