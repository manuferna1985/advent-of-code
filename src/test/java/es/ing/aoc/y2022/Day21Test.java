package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day21Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day21::new, "2022/D21_small.txt");
        assertEquals("152", results.a);
        assertEquals("302", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day21::new, "2022/D21_full.txt");
        assertEquals("78342931359552", results.a);
        assertEquals("3296135418820", results.b);
    }
}