package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day25Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day25::new, "2022/D25_small.txt");
        assertEquals("2=-1=0", results.a);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day25::new, "2022/D25_full.txt");
        assertEquals("2==221=-002=0-02-000", results.a);
    }
}