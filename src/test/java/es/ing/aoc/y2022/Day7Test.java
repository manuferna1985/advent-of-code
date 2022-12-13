package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day7Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day7::new, "2022/D7_small.txt");
        assertEquals("95437", results.a);
        assertEquals("24933642", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day7::new, "2022/D7_full.txt");
        assertEquals("1517599", results.a);
        assertEquals("2481982", results.b);
    }
}