package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D8_Segments_Part1 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part1.class.getResource("2021/D8_small.txt").toURI()), Charset.defaultCharset());

        int uniqueNumbers = 0;

        for (String line : allLines) {
            String[] parts = line.split("\\|");

            System.out.println(parts[1]);

            for (String number : parts[1].trim().split(" ")) {

                int len = number.length();

                if (len == 2 || len == 4 || len == 3 || len == 7) {
                    uniqueNumbers++;
                }
            }
        }


        System.out.println(uniqueNumbers);
        //System.out.println(allLines);
    }
}
