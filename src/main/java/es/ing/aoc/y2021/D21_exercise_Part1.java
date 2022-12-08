package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class D21_exercise_Part1 {

    private static Integer LAST_ROLL = 1;
    private static Integer TIMES_ROLLED = 0;

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D21_full.txt").toURI()), Charset.defaultCharset());


        // Start coding here. ;-)
        Integer[] players = allLines.stream()
                .map(s -> Integer.parseInt(s.substring(s.indexOf(":") + 2).trim()))
                .collect(Collectors.toList()).toArray(new Integer[0]);

        Integer[] points = {0, 0};

        boolean matchEnd = false;
        do{
            Integer p0Roll = threeRolls();
            players[0] = modDirac10(players[0] + p0Roll);
            points[0] += players[0];

            if (points[0] < 1000) {
                Integer p1Roll = threeRolls();
                players[1] = modDirac10(players[1] + p1Roll);
                points[1] += players[1];
            }

            matchEnd = points[0] >= 1000 || points[1] >= 1000;

        } while (!matchEnd);

        Integer winner = Math.max(points[0], points[1]);
        Integer looser = Math.min(points[0], points[1]);

        System.out.println(looser * TIMES_ROLLED);
    }

    private static Integer threeRolls(){
        return rollDice() + rollDice() + rollDice();
    }

    private static Integer rollDice(){
        TIMES_ROLLED++;

        if (LAST_ROLL > 100){
            LAST_ROLL = 1;
        }

        System.out.println(LAST_ROLL);
        return LAST_ROLL++;
    }

    private static Integer modDirac10(Integer position){
        return (position > 10) ? modDirac10(position - 10) : position;
    }
}
