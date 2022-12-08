package es.ing.aoc.y2021;

import es.ing.aoc.common.Cube;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D22_exercise_Part2 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D22_full.txt").toURI()), Charset.defaultCharset());


        // Start coding here. ;-)
        long before = System.nanoTime();

        List<Cube> cubes = new ArrayList<>();

        int cubeCounter = 1;
        for (String line : allLines) {

            boolean operationType = line.substring(0, line.indexOf(" ")).equalsIgnoreCase("on");

            String[] coordinateParts = line.substring(line.indexOf(" ") + 1).split(",");
            int[] xLimits = createMinAndMaxCoordinatesFrom(coordinateParts[0]);
            int[] yLimits = createMinAndMaxCoordinatesFrom(coordinateParts[1]);
            int[] zLimits = createMinAndMaxCoordinatesFrom(coordinateParts[2]);

            var newCube = Cube.of(String.valueOf(cubeCounter++), operationType, xLimits, yLimits, zLimits);

            var contained = new ArrayList<Cube>();
            for (var cube : cubes) {
                if (cube.isWithin(newCube)) {
                    contained.add(cube);
                }
            }
            cubes.removeAll(contained);

            var overlaps = new ArrayList<Cube>();
            for (var cube : cubes) {
                var ol = Cube.intersect(cube, newCube, !cube.state);
                ol.ifPresent(overlaps::add);
            }
            cubes.addAll(overlaps);

            if (newCube.state) {
                cubes.add(newCube); // and add lights on
            }
        }

        var outer = Cube.of("BLACK", false, new int[]{-50, 50}, new int[]{-50, 50}, new int[]{-50, 50});
        long sum1 = 0;
        for (var s : cubes) {
            var ol = Cube.intersect(outer, s, s.state);
            if (ol.isPresent()) sum1 += ol.get().eval();
        }
        System.out.println("Part 1: " + sum1);

        long sum2 = 0;
        for (var s : cubes) {
            sum2 += s.eval();
        }

        System.out.println("Part 2: " + sum2);

        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double) (after - before)) / 1000000);
    }

    private static int[] createMinAndMaxCoordinatesFrom(String rawCoordinates) {
        int coordMin = Integer.parseInt(rawCoordinates.substring(rawCoordinates.indexOf("=") + 1, rawCoordinates.indexOf(".")));
        int coordMax = Integer.parseInt(rawCoordinates.substring(rawCoordinates.lastIndexOf(".") + 1));
        return new int[]{coordMin, coordMax};
    }
}
