package es.ing.aoc.y2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class D1_Sonar_Part1 {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(new File("/Users/gp78mg/workspaces/AzureMigration/LOCAL_AOC/src/main/resources/D1_full.txt")));

        String line;
        Long previousMeasure = null;
        Long currentMeasure = null;
        Long increments = 0L;

        while ((line = br.readLine()) != null) {
            System.out.println(line);

            currentMeasure = Long.parseLong(line);

            if (previousMeasure != null && currentMeasure > previousMeasure){
                increments++;
            }

            previousMeasure = currentMeasure;
        }

        System.out.println(increments);
    }
}
