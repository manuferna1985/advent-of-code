package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class D17_exercise {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D17_exercise.class.getResource("2021/D17_full.txt").toURI()), Charset.defaultCharset());

        // Start
        String data = allLines.get(0);

        List<Integer> xPositions = Arrays.stream(data.substring(data.indexOf("x=") + 2, data.indexOf(","))
                .split("\\.\\."))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());

        List<Integer> yPositions = Arrays.stream(data.substring(data.indexOf("y=") + 2)
                .split("\\.\\."))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());

        System.out.println(xPositions);
        System.out.println(yPositions);

        int overallMaxHeight = 0;
        Set<Long> matchingSpeeds = new HashSet<>();

        for (int i = 0; i < xPositions.get(0) * 2; i++) {
            for (int j = yPositions.get(0) * 2; j < Math.abs(yPositions.get(0) * 2); j++) {

                Integer[] position = {0, 0};
                Integer[] speed = {i, j};

                boolean crossTarget = false;
                boolean end = false;
                int maxHeight = 0;

                while (!end) {
                    shoot(position, speed);

                    if (!crossTarget && isInsideTargetArea(xPositions, yPositions, position)) {
                        matchingSpeeds.add((i * 1000L + j));
                        crossTarget = true;
                    }

                    end = crossTarget || isFarAwayFromTargetArea(xPositions, yPositions, position);
                    maxHeight = Math.max(maxHeight, position[1]);
                }

                if (crossTarget) {
                    overallMaxHeight = Math.max(overallMaxHeight, maxHeight);
                }
            }
        }

        System.out.println("Total matching speeds: " + matchingSpeeds.size());
        System.out.println("Overall Max height: " + overallMaxHeight);
    }

    private static void shoot(Integer[] current, Integer[] speed) {

        // New position
        current[0] += speed[0];
        current[1] += speed[1];

        // New speed
        if (speed[0] > 0) {
            speed[0]--;
        } else if (speed[0] < 0) {
            speed[0]++;
        }
        speed[1]--;
    }

    private static boolean isInsideTargetArea(List<Integer> xTarget, List<Integer> yTarget, Integer[] current) {
        return xTarget.get(0) <= current[0] && current[0] <= xTarget.get(1) &&
                yTarget.get(0) <= current[1] && current[1] <= yTarget.get(1);
    }

    private static boolean isFarAwayFromTargetArea(List<Integer> xTarget, List<Integer> yTarget, Integer[] current) {
        return current[0] > xTarget.get(1) || current[1] < yTarget.get(0);
    }
}
