package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;

public class Day9 extends Day {

    @Override
    protected void part1(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        System.out.println("Part1: " + 1);
    }

    @Override
    protected void part2(String fileContents) throws Exception {

        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        System.out.println("Part2: " + 2);
    }

    public static void main(String[] args) {
        Day.run(Day9::new, "2022/D9_small.txt", "2022/D9_full.txt");
    }
}
