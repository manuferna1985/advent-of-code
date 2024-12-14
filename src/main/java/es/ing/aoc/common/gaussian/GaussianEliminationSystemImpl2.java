package es.ing.aoc.common.gaussian;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GaussianEliminationSystemImpl2 implements GaussianEliminationSystem {

  private static final Integer EXEC_SCALE = 16;
  private static final Integer RESULT_SCALE = 3;

  public BigDecimal[] applyAlgorithm(BigDecimal[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      int pivotRow = i;
      for (int j = i + 1; j < matrix.length; j++) {
        if (matrix[j][i].abs().compareTo(matrix[pivotRow][i].abs()) > 0) {
          pivotRow = j;
        }
      }
      if (pivotRow!=i) {
        for (int j = i; j <= matrix.length; j++) {
          BigDecimal temp = matrix[i][j];
          matrix[i][j] = matrix[pivotRow][j];
          matrix[pivotRow][j] = temp;
        }
      }
      for (int j = i + 1; j < matrix.length; j++) {
        BigDecimal factor = matrix[j][i].divide(matrix[i][i], EXEC_SCALE, RoundingMode.HALF_UP);
        for (int k = i; k <= matrix.length; k++) {
          matrix[j][k] = matrix[j][k].subtract(factor.multiply(matrix[i][k]));
        }
      }
    }

    return backSubstitute(matrix);
  }

  private BigDecimal[] backSubstitute(BigDecimal[][] matrix) {
    BigDecimal[] x = new BigDecimal[matrix.length]; // An array to store solution
    for (int i = matrix.length - 1; i >= 0; i--) {
      BigDecimal sum = BigDecimal.ZERO;
      for (int j = i + 1; j < matrix.length; j++) {
        sum = sum.add(matrix[i][j].multiply(x[j]));
      }
      x[i] = (matrix[i][matrix.length].subtract(sum)).divide(matrix[i][i], EXEC_SCALE, RoundingMode.HALF_UP);
    }

    for (int i = 0; i < matrix.length; i++) {
      x[i] = x[i].setScale(RESULT_SCALE, RoundingMode.HALF_UP);
    }
    return x;
  }

  // function to print matrix content at any stage
  private void print(BigDecimal[][] matrix) {
    for (int i = 0; i < matrix.length; i++, System.out.println()) {
      for (int j = 0; j <= matrix.length; j++) {
        System.out.print(matrix[i][j]);
      }
      System.out.println();
    }
  }
}
