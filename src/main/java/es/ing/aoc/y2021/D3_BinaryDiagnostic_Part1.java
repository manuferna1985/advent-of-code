package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class D3_BinaryDiagnostic_Part1 {


    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part1.class.getResource("2021/D3_full.txt").toURI()), Charset.defaultCharset());


        System.out.println(allLines);

        int[][] matrix = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++){
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }

        Long epsilonRate = 0L, gammaRate = 0L;
        StringBuilder epsilonBinary = new StringBuilder(), gammaBinary = new StringBuilder();

        for (int j = 0; j < matrix[0].length; j++){
            StringBuilder column = new StringBuilder();
            for (int i = 0; i < matrix.length; i++) {
                column.append(matrix[i][j]);
            }

            long ones = Arrays.stream(column.toString().split("")).filter("1"::equals).count();
            long zeroes = Arrays.stream(column.toString().split("")).filter("0"::equals).count();

            if (ones > zeroes){
                gammaBinary.append("1");
                epsilonBinary.append("0");
            } else {
                gammaBinary.append("0");
                epsilonBinary.append("1");
            }
        }

        gammaRate = Long.parseLong(gammaBinary.toString(), 2);
        epsilonRate = Long.parseLong(epsilonBinary.toString(), 2);

        System.out.println(gammaRate * epsilonRate);
    }
}
