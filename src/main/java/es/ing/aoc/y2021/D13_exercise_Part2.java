package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D13_exercise_Part2 {

    private static final Integer SIZE = 1500;

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D13_exercise_Part2.class.getResource("2021/D13_full.txt").toURI()), Charset.defaultCharset());

        int[][] matrix = new int[SIZE][SIZE];

        for (String line : allLines){
            if (line.trim().length() > 0){
                if (!line.startsWith("fold along")){

                    int i = Integer.parseInt(line.split(",")[1]);
                    int j = Integer.parseInt(line.split(",")[0]);

                    matrix[i][j] = 1;
                }
            }
        }

        int positionToFold;

        for (String line : allLines) {
            if (line.trim().length() > 0) {
                if (line.startsWith("fold along")) {

                    positionToFold = Integer.parseInt(line.substring(line.indexOf("=") + 1));

                    if (line.contains("y")){
                        // Horizontal

                        int i1 = positionToFold - 1;
                        int i2 = positionToFold + 1;

                        do {
                            for (int k = 0; k < matrix.length; k++){
                                matrix[i1][k] = matrix[i1][k] + matrix[i2][k];
                                matrix[i2][k] = 0;
                            }

                            i1--;
                            i2++;
                        } while (i1 >=  0   && i2 <= matrix.length - 1);

                    } else {
                        // Vertical

                        int j1 = positionToFold - 1;
                        int j2 = positionToFold + 1;

                        do {
                            for (int k = 0; k < matrix.length; k++){
                                matrix[k][j1] = matrix[k][j1] + matrix[k][j2];
                                matrix[k][j2] = 0;
                            }

                            j1--;
                            j2++;
                        } while (j1 >=  0  && j2 <= matrix.length - 1);
                    }
                }
            }
        }

        int dots = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] > 0){
                    dots++;
                    matrix[i][j] = 1;
                }
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }

        System.out.println(matrix);
        System.out.println(dots);

        /*

        1001  0  1111  0  0110  0  1110  0  1111  0  1001  0  1110  0  1110
        1001  0  1000  0  1001  0  1001  0  0001  0  1010  0  1001  0  1001
        1111  0  1110  0  1000  0  1001  0  0010  0  1100  0  1001  0  1001
        1001  0  1000  0  1000  0  1110  0  0100  0  1010  0  1110  0  1110
        1001  0  1000  0  1001  0  1010  0  1000  0  1010  0  1000  0  1010
        1001  0  1111  0  0110  0  1001  0  1111  0  1001  0  1000  0  1001

        H        E        C        R        Z        K        P        R

         */
    }
}
