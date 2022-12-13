package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.DayV2;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day13Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = DayV2.run(Day13::new, "2022/D13_small.txt");
        assertEquals("13", results.a);
        assertEquals("140", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = DayV2.run(Day13::new, "2022/D13_full.txt");
        assertEquals("5503", results.a);
        assertEquals("20952", results.b);
    }
}
