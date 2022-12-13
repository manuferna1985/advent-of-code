package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day1Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day1::new, "2022/D1_small.txt");
        assertEquals("24000", results.a);
        assertEquals("45000", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day1::new, "2022/D1_full.txt");
        assertEquals("72240", results.a);
        assertEquals("210957", results.b);
    }
}