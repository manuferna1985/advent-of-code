package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day6Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day6::new, "2022/D6_small.txt");
        assertEquals("11", results.a);
        assertEquals("26", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day6::new, "2022/D6_full.txt");
        assertEquals("1566", results.a);
        assertEquals("2265", results.b);
    }
}