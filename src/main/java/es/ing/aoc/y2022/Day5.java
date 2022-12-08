package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Day5 extends Day {

    private static final String SPACE = " ";

    public static String safeSubstr(String line, int start, int end) {
        if (end < line.length() && start <= end) {
            return line.substring(start, end);
        } else {
            return SPACE;
        }
    }

    @Override
    protected void part1(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline
        Map<Integer, Stack<String>> containers = moveCratesAlgorithm(packages, false);
        System.out.println("Part1: " + getTopElementsFrom(containers));
    }

    @Override
    protected void part2(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline
        Map<Integer, Stack<String>> containers = moveCratesAlgorithm(packages, true);
        System.out.println("Part2: " + getTopElementsFrom(containers));
    }

    private static Map<Integer, Stack<String>> moveCratesAlgorithm(String[] packages, boolean multiCrateMovement) {

        Map<Integer, Stack<String>> containers = new HashMap<>();
        Stack<String> lines = new Stack<>();
        boolean endStructure = false;
        for (String line : packages) {

            if (!endStructure) {
                // Structure of the crates
                if (line.length() == 0) {
                    endStructure = true;

                    String[] numbers = lines.pop().split(SPACE);
                    int max = Integer.parseInt(numbers[numbers.length - 1]);

                    for (int i = 1; i <= max; i++) {
                        containers.put(i, new Stack<>());
                    }

                    do {
                        String row = lines.pop();
                        for (int i = 0; i < max; i++) {
                            String letter = safeSubstr(row,(4 * i) + 1, (4 * i) + 2);
                            if (!letter.equals(SPACE)) {
                                containers.get(i + 1).add(letter);
                            }
                        }
                    } while (!lines.empty());
                } else {
                    lines.add(line);
                }
            } else {
                // Instructions
                String[] parts = line.split(SPACE);
                int numberOfCrates = Integer.parseInt(parts[1]);
                Integer originStack = Integer.parseInt(parts[3]);
                Integer destinationStack = Integer.parseInt(parts[5]);

                Stack<String> aux = new Stack<>();
                for (int i=0; i<numberOfCrates; i++){
                    if (!containers.get(originStack).isEmpty()){
                        if (!multiCrateMovement) {
                            containers.get(destinationStack).add(containers.get(originStack).pop());
                        } else {
                            aux.add(containers.get(originStack).pop());
                        }
                    }
                }

                while(!aux.isEmpty()){
                    containers.get(destinationStack).add(aux.pop());
                }
            }
        }

        return containers;
    }

    private static String getTopElementsFrom(Map<Integer, Stack<String>> containers){
        StringBuilder topCrates = new StringBuilder();

        for (int i = 1; i<= containers.size(); i++){
            topCrates.append(containers.get(i).peek());
        }
        return topCrates.toString();
    }

    public static void main(String[] args) {
        Day.run(Day5::new, "2022/D5_small.txt", "2022/D5_full.txt");
    }
}
