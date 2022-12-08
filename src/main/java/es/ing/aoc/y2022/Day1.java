package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day1 extends Day {

    @Override
    protected void part1(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        int max = Integer.MIN_VALUE;

        int current = 0;
        for (String s : packages) {
            if (s.length() > 0) {
                current += Integer.parseInt(s);
            } else {
                max = Math.max(max, current);
                current = 0;
            }
        }

        max = Math.max(max, current);

        System.out.println("Part1: " + max);
    }

    @Override
    protected void part2(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        List<Integer> calories = new ArrayList<>();

        int current = 0;
        for (String s : packages) {
            if (s.length() > 0) {
                current += Integer.parseInt(s);
            } else {
                calories.add(current);
                current = 0;
            }
        }

        calories.add(current);

        Integer[] caloriesArray = calories.toArray(new Integer[0]);
        Arrays.sort(caloriesArray, Collections.reverseOrder());

        int total = caloriesArray[0] + caloriesArray[1] + caloriesArray[2];

        System.out.println("Part2: " + total);
    }

    public static void main(String[] args) {
        Day.run(Day1::new, "2022/D1_small.txt", "2022/D1_full.txt");
    }
}
