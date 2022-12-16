package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day16Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day16::new, "2022/D16_small.txt");
        assertEquals("1651", results.a);
        assertEquals("1707", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day16::new, "2022/D16_full.txt");
        assertEquals("1460", results.a);
        assertEquals("2117", results.b);
    }
}