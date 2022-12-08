package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.Arrays;
import java.util.HashSet;

public class Day6 extends Day {

    private boolean isMessageMarker(String marker) {
        return new HashSet<>(Arrays.asList(marker.split(""))).size() == marker.length();
    }

    private int getPosition(String message, int markerLength) {
        int i = 0;
        while (true) {
            if (isMessageMarker(message.substring(i, i + markerLength))) {
                return i + markerLength;
            }
            i++;
        }
    }

    @Override
    protected void part1(String fileContents) throws Exception {
        String message = fileContents.split(System.lineSeparator())[0]; // when input file is multiline
        System.out.println("Part1: " + getPosition(message, 4));
    }

    @Override
    protected void part2(String fileContents) throws Exception {
        String message = fileContents.split(System.lineSeparator())[0]; // when input file is multiline
        System.out.println("Part2: " + getPosition(message, 14));

    }

    public static void main(String[] args) {
        Day.run(Day6::new, "2022/D6_small.txt", "2022/D6_full.txt");
    }
}
