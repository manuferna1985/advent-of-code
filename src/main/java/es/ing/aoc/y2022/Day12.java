package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;

public class Day12 extends Day {

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
        Day.run(Day12::new, "2022/D12_small.txt", "2022/D12_full.txt");
    }
}
