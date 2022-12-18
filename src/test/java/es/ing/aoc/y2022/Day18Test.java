package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day18Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day18::new, "2022/D18_small.txt");
        assertEquals("64", results.a);
        assertEquals("58", results.b);
    }

    @Test
    void testCustomProblem() {
        Pair<String, String> results = Day.run(Day18::new, "2022/D18_custom.txt");
        assertEquals("76", results.a);
        assertEquals("66", results.b);
    }

    @Test
    void testCustom2Problem() {
        Pair<String, String> results = Day.run(Day18::new, "2022/D18_custom2.txt");
        assertEquals("24", results.a);
        assertEquals("24", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day18::new, "2022/D18_full.txt");
        assertEquals("3390", results.a);
        assertEquals("2058", results.b);
    }
}