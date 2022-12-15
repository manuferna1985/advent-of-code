package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day15Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day15::new, "2022/D15_small.txt");
        assertEquals("26", results.a);
        assertEquals("56000011", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day15::new, "2022/D15_full.txt");
        assertEquals("4861076", results.a);
        assertEquals("10649103160102", results.b);
    }
}