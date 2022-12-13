package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day8Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day8::new, "2022/D8_small.txt");
        assertEquals("21", results.a);
        assertEquals("8", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day8::new, "2022/D8_full.txt");
        assertEquals("1695", results.a);
        assertEquals("287040", results.b);
    }
}