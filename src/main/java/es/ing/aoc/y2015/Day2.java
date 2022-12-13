package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day2 extends Day {


    @Override
    protected String part1(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator());

        int totalNeededSize = 0;

        for (String pack : packages){

            String[] dimensions = pack.split("x");

            int a = Integer.parseInt(dimensions[0]);
            int b = Integer.parseInt(dimensions[1]);
            int c = Integer.parseInt(dimensions[2]);

            int totalSize = (2*a*b) + (2*b*c) + (2*a*c);
            int minSize = Math.min(Math.min(a*b, b*c), a*c);
            totalSize += minSize;

            totalNeededSize += totalSize;

        }

        return String.valueOf(totalNeededSize);
    }

    @Override
    protected String part2(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator());

        int totalNeededSize = 0;

        for (String pack : packages){

            String[] dimensions = pack.split("x");

            List<Integer> dimsList = Arrays.asList(
                    Integer.parseInt(dimensions[0]),
                    Integer.parseInt(dimensions[1]),
                    Integer.parseInt(dimensions[2]));
            Collections.sort(dimsList);

            int a = dimsList.get(0);
            int b = dimsList.get(1);
            int c = dimsList.get(2);

            int ribbon = a + a + b + b;
            int bow = a * b * c;
            totalNeededSize += ribbon + bow;
        }

        return String.valueOf(totalNeededSize);
    }

    public static void main(String[] args) {
        Day.run(Day2::new, "2015/D2_small.txt", "2015/D2_full.txt");
    }
}
