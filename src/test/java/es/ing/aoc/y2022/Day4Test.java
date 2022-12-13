package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day4Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day4::new, "2022/D4_small.txt");
        assertEquals("2", results.a);
        assertEquals("4", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day4::new, "2022/D4_full.txt");
        assertEquals("605", results.a);
        assertEquals("914", results.b);
    }
}