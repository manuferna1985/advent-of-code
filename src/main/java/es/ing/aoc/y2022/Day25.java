package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;

public class Day25 extends Day {

    @Override
    protected String part1(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        return String.valueOf(1);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        return String.valueOf(2);
    }

    public static void main(String[] args) {
        Day.run(Day25::new, "2022/D25_small.txt", "2022/D25_full.txt");
    }
}