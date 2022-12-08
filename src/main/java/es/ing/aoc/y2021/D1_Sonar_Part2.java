package es.ing.aoc.y2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class D1_Sonar_Part2 {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(new File("/Users/gp78mg/workspaces/AzureMigration/LOCAL_AOC/src/main/resources/D1_full.txt")));

        String line;
        List<Long> allMines = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            allMines.add(Long.parseLong(line));
        }

        System.out.println(allMines);


        Long increases = 0L;

        for (int i = 0; i < allMines.size() - 3; i++){

            if (giveWindowMeasureFor(allMines, i+1) > giveWindowMeasureFor(allMines, i)){
                increases++;
            }
        }

        System.out.println(increases);


    }

    private static Long giveWindowMeasureFor(List<Long> allMines, int i){
        return allMines.get(i) + allMines.get(i+1) + allMines.get(i+2);
    }
}
