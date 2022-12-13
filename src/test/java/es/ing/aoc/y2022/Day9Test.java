package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day9Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day9::new, "2022/D9_small.txt");
        assertEquals("88", results.a);
        assertEquals("36", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day9::new, "2022/D9_full.txt");
        assertEquals("6243", results.a);
        assertEquals("2630", results.b);
    }
}