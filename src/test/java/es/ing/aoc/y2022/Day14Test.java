package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day14Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day14::new, "2022/D14_small.txt");
        assertEquals("24", results.a);
        assertEquals("93", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day14::new, "2022/D14_full.txt");
        assertEquals("828", results.a);
        assertEquals("25500", results.b);
    }
}