package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import java.util.Arrays;

public class Day1 extends Day {

    @Override
    protected void part1(String fileContents) throws Exception {
        long opened = Arrays.stream(fileContents.split("")).filter("("::equalsIgnoreCase).count();
        long closed = Arrays.stream(fileContents.split("")).filter(")"::equalsIgnoreCase).count();
        System.out.println("Part1: " + (opened - closed));
    }

    @Override
    protected void part2(String fileContents) throws Exception {

        long currentFloor = 0;

        int position = 1;
        for (String letter : fileContents.split("")){
            if ("(".equalsIgnoreCase(letter)){
                currentFloor++;
            } else {
                currentFloor--;
            }
            if (currentFloor == -1){
                System.out.println("Part2: " + position);
                break;
            }
            position++;
        }
    }

    public static void main(String[] args) {
        Day.run(Day1::new, "2015/D1_full.txt");
    }
}
