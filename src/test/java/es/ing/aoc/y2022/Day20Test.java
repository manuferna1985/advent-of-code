package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day20Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day20::new, "2022/D20_small.txt");
        assertEquals("3", results.a);
        assertEquals("1623178306", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day20::new, "2022/D20_full.txt");
        assertEquals("27726", results.a);
        assertEquals("4275451658004", results.b);
    }
}