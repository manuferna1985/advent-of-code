package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day11Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day11::new, "2022/D11_small.txt");
        assertEquals("10605", results.a);
        assertEquals("2713310158", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day11::new, "2022/D11_full.txt");
        assertEquals("121450", results.a);
        assertEquals("28244037010", results.b);
    }
}