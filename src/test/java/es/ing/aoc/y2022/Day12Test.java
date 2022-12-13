package es.ing.aoc.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;

import es.ing.aoc.common.DayV2;
import es.ing.aoc.common.Pair;
import org.junit.jupiter.api.Test;

class Day12Test {

    @Test
    void testSmallProblem() {
        Pair<String, String> results = DayV2.run(Day12::new, "2022/D12_small.txt");
        assertEquals("31", results.a);
        assertEquals("29", results.b);
    }

    @Test
    void testCustomProblem() {
        Pair<String, String> results = DayV2.run(Day12::new, "2022/D12_custom.txt");
        assertEquals("63", results.a);
        assertEquals("27", results.b);
    }

    @Test
    void testFullProblem() {
        Pair<String, String> results = DayV2.run(Day12::new, "2022/D12_full.txt");
        assertEquals("412", results.a);
        assertEquals("402", results.b);
    }

}