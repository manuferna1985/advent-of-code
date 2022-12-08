package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D6_Lanternfish_Part1 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part2.class.getResource("2021/D6_small.txt").toURI()), Charset.defaultCharset());

        List<Integer> fishes = Arrays.stream(allLines.get(0).split(","))
                .mapToInt(Integer::parseInt)
                .boxed().collect(Collectors.toList());

        System.out.println(fishes.size());

        for (int r = 0; r < 80; r++) {
            List<Integer> nextGenFishes = new ArrayList<>();
            for (int i = 0; i < fishes.size(); i++) {
                int currentFish = fishes.get(i);

                if (currentFish == 0) {
                    nextGenFishes.add(6);
                    nextGenFishes.add(8);
                } else {
                    nextGenFishes.add(currentFish - 1);
                }
            }
            System.out.println(nextGenFishes.size());
            fishes = nextGenFishes;
        }
    }
}
