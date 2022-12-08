package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class D21_exercise_Part2 {

    public static class Pair{
        Player one;
        Player two;

        public static Pair of(Player one, Player two) {
            Pair p = new Pair();
            p.one = one;
            p.two = two;
            return p;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return one.equals(pair.one) && two.equals(pair.two);
        }

        @Override
        public int hashCode() {
            return Objects.hash(one, two);
        }

        public String toString(){
            return String.format("{%s | %s}", one, two);
        }
    }

    public static class Player{
        Integer position;
        Integer points;

        public static Player of(Integer position, Integer points){
            Player p = new Player();
            p.position = position;
            p.points = points;
            return p;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Player player = (Player) o;
            return position.equals(player.position) && points.equals(player.points);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, points);
        }

        public String toString(){
            return String.format("[%d]=%d", position, points);
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D21_full.txt").toURI()), Charset.defaultCharset());

        long before = System.nanoTime();

        // Start coding here. ;-)
        Integer[] players = allLines.stream()
                .map(s -> Integer.parseInt(s.substring(s.indexOf(":") + 2).trim()))
                .toArray(Integer[]::new);

        Pair initialUniverse = Pair.of(Player.of(players[0], 0), Player.of(players[1], 0));

        Map<Pair, Long> nonFinishedUniverses = new HashMap<>();
        nonFinishedUniverses.put(initialUniverse, 1L);

        Map<Pair, Long> finishedUniverses = new HashMap<>();

        Long p1Wins = 0L;
        Long p2Wins = 0L;

        do {
            Map.Entry<Pair, Long> firstUniverse = nonFinishedUniverses.entrySet().stream().findFirst().get();
            nonFinishedUniverses.remove(firstUniverse.getKey());

            for (Player p1Option : calculatePlayerNextOptions(firstUniverse.getKey().one)){
                if (p1Option.points >= 21){
                    p1Wins += firstUniverse.getValue();
                } else {
                    for (Player p2Option : calculatePlayerNextOptions(firstUniverse.getKey().two)){
                        if (p2Option.points >= 21) {
                            p2Wins += firstUniverse.getValue();
                        } else {
                            addNonFinishedUniverseToMap(
                                    nonFinishedUniverses,
                                    Pair.of(p1Option, p2Option),
                                    firstUniverse.getValue());
                        }
                    }
                }
            }
            //System.out.println("Universes: " + nonFinishedUniverses.size());
        } while (!nonFinishedUniverses.isEmpty());

        System.out.println(finishedUniverses.size());
        System.out.println("P1 wins in: " + p1Wins);
        System.out.println("P2 wins in: " + p2Wins);

        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double)(after-before))/1000000);
    }

    private static void addNonFinishedUniverseToMap(Map<Pair, Long> nonFinishedUniverses, Pair newUniverse, Long times){

        if (nonFinishedUniverses.containsKey(newUniverse)){
            nonFinishedUniverses.put(newUniverse, nonFinishedUniverses.get(newUniverse) + times);
        } else {
            nonFinishedUniverses.put(newUniverse, times);
        }
    }

    private static List<Player> calculatePlayerNextOptions(Player player){

        List<Player> options = new ArrayList<>();
        List<Integer> diceRolls = threeRolls();

        Integer newPosition;
        for (Integer currentDice : diceRolls){
            newPosition = modDirac10(player.position + currentDice);
            options.add(Player.of(newPosition, player.points + newPosition));
        }

        return options;
    }

    private static List<Integer> threeRolls() {
        return List.of(3, 4, 5, 4, 5, 6, 5, 6, 7, 4, 5, 6, 5, 6, 7, 6, 7, 8, 5, 6, 7, 6, 7, 8, 7, 8, 9);
    }

    private static Integer modDirac10(Integer position) {
        return (position > 10) ? modDirac10(position - 10) : position;
    }
}
