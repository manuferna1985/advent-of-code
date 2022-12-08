package es.ing.aoc.y2021;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class D2_Submarine_Part2 {

    public static void main (String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File("/Users/gp78mg/workspaces/AzureMigration/LOCAL_AOC/src/main/resources/D2_full.txt")));

        Double horizontalPosition = 0.0;
        Double deepPosition = 0.0;
        Double aimValue = 0.0;

        String line;
        while ((line = br.readLine()) != null){
            System.out.println(line);

            String[] lineParts = line.split(" ");

            switch(lineParts[0]){
                case "forward":
                    horizontalPosition += Integer.parseInt(lineParts[1]);
                    deepPosition += (aimValue * Integer.parseInt(lineParts[1]));
                    break;
                case "up":
                    aimValue -= Integer.parseInt(lineParts[1]);
                    break;
                case "down":
                    aimValue += Integer.parseInt(lineParts[1]);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        System.out.println((long)(horizontalPosition * deepPosition));
    }
}
