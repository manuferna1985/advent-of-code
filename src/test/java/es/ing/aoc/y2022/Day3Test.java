package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day3Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = Day.run(Day3::new, "2022/D3_small.txt");
        assertEquals("157", results.a);
        assertEquals("70", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = Day.run(Day3::new, "2022/D3_full.txt");
        assertEquals("7967", results.a);
        assertEquals("2716", results.b);
    }
}