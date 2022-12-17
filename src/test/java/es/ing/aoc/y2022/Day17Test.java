package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day17Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day17::new, "2022/D17_small.txt");
        assertEquals("3068", results.a);
        assertEquals("1514285714288", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day17::new, "2022/D17_full.txt");
        assertEquals("3130", results.a);
        assertEquals("1556521739139", results.b);
    }
}