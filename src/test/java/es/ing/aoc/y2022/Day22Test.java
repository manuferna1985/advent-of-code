package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day22Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day22::new, "2022/D22_small.txt");
        assertEquals("6032", results.a);
        assertEquals("5031", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day22::new, "2022/D22_full.txt");
        assertEquals("131052", results.a);
        assertEquals("2", results.b);
    }
}