package es.ing.aoc.y2021;

import es.ing.aoc.common.Point;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D22_exercise_Part1 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D22_small.txt").toURI()), Charset.defaultCharset());


        // Start coding here. ;-)
        long before = System.nanoTime();

        Set<Point> enabledPoints = new HashSet<>();

        int lineCounter = 1;
        for (String line : allLines) {

            System.out.println(">>> Working with line: " + (lineCounter++));

            boolean operationType = line.substring(0, line.indexOf(" ")).equalsIgnoreCase("on");

            String[] coordinateParts = line.substring(line.indexOf(" ") + 1).split(",");
            int[] xLimits = createMinAndMaxCoordinatesFrom(coordinateParts[0]);
            int[] yLimits = createMinAndMaxCoordinatesFrom(coordinateParts[1]);
            int[] zLimits = createMinAndMaxCoordinatesFrom(coordinateParts[2]);

            if (allInRegionMinMax(xLimits, yLimits, zLimits)) {

                for (int x = xLimits[0]; x <= xLimits[1]; x++) {
                    for (int y = yLimits[0]; y <= yLimits[1]; y++) {
                        for (int z = zLimits[0]; z <= zLimits[1]; z++) {
                            Point p = new Point(x, y, z);
                            if (operationType) {
                                enabledPoints.add(p);
                            } else {
                                enabledPoints.remove(p);
                            }
                        }
                    }
                }
            }

            System.out.println(">> Partial Enabled points: " + enabledPoints.size());
            System.out.println();
        }

        System.out.println("Total Enabled points: " + enabledPoints.size());


        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double) (after - before)) / 1000000);
    }

    private static int[] createMinAndMaxCoordinatesFrom(String rawCoordinates) {
        int coordMin = Integer.parseInt(rawCoordinates.substring(rawCoordinates.indexOf("=") + 1, rawCoordinates.indexOf(".")));
        int coordMax = Integer.parseInt(rawCoordinates.substring(rawCoordinates.lastIndexOf(".") + 1));
        return new int[]{coordMin, coordMax};
    }

    private static boolean allInRegionMinMax(int[] x, int[] y, int[] z) {
        return allInRegion(x[0], y[0], z[0]) && allInRegion(x[1], y[1], z[1]);
    }

    private static boolean allInRegion(int x, int y, int z) {
        return inRegion(x) && inRegion(y) && inRegion(z);
    }

    private static boolean inRegion(int coordinate) {
        return coordinate >= -50 && coordinate <= 50;
    }
}
