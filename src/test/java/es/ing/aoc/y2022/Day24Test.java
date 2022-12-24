package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day24Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day24::new, "2022/D24_small.txt");
        assertEquals("18", results.a);
        assertEquals("54", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day24::new, "2022/D24_full.txt");
        assertEquals("228", results.a);
        assertEquals("723", results.b);
    }
}