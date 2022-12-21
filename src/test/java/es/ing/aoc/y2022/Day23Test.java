package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day23Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day23::new, "2022/D23_small.txt");
        assertEquals("1", results.a);
        assertEquals("2", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day23::new, "2022/D23_full.txt");
        assertEquals("1", results.a);
        assertEquals("2", results.b);
    }
}