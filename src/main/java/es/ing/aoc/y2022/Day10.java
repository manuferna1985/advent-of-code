package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.Range;

public class Day10 extends Day {

    private static final Integer SCREEN_WIDTH = 40;
    private static final Integer CYCLE_STRENGTH_SIZE = 40;
    private static final Integer CYCLE_STRENGTH_OFFSET = 20;

    @Override
    protected String part1(String fileContents) throws Exception {
        return processCpuInstructions(fileContents, false);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return processCpuInstructions(fileContents, true);
    }

    private String processCpuInstructions(String fileContents, boolean screenEnabled) {
        final Queue<String> instructions = new LinkedList<>(
                Arrays.asList(fileContents.split(System.lineSeparator())));

        int cycle = 1;
        int crtDrawPosition = 0;
        int signalStrength = 0;
        int x = 1;
        int valueToAddAtEnd = 0;
        int cyclesBusy = 0;
        StringBuilder builder = new StringBuilder("\n");

        String line;
        while (!instructions.isEmpty()) {
            if (cyclesBusy == 0) {
                x += valueToAddAtEnd;
                line = instructions.remove();
                if ("noop".equals(line)) {
                    // noop
                    valueToAddAtEnd = 0;
                    cyclesBusy = 1;
                } else {
                    // addx
                    valueToAddAtEnd = Integer.parseInt(line.split(" ")[1]);
                    cyclesBusy = 2;
                }
            }

            if (xInCrtWindowToPrint(x, crtDrawPosition)) {
                builder.append("#");
            } else {
                builder.append(".");
            }

            if ((cycle - CYCLE_STRENGTH_OFFSET) % CYCLE_STRENGTH_SIZE == 0) {
                signalStrength += x * cycle;
            }

            cycle++;
            cyclesBusy--;
            crtDrawPosition++;

            if (crtDrawPosition == SCREEN_WIDTH) {
                crtDrawPosition = 0;
                builder.append("\n");
            }
        }

        if (screenEnabled) {
            return builder.toString();
        } else {
            return String.valueOf(signalStrength);
        }
    }

    private boolean xInCrtWindowToPrint(int x, int crtDrawPosition) {
        // 3 pixels
        return Range.between(crtDrawPosition - 1, crtDrawPosition + 1).contains(x);
    }

    public static void main(String[] args) {
        Day.run(Day10::new, "2022/D10_small.txt", "2022/D10_full.txt");
    }
}
