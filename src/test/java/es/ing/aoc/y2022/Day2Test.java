package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day2Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day2::new, "2022/D2_small.txt");
        assertEquals("15", results.a);
        assertEquals("12", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day2::new, "2022/D2_full.txt");
        assertEquals("9177", results.a);
        assertEquals("12111", results.b);
    }
}