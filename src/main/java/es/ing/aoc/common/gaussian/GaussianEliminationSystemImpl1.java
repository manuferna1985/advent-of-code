package es.ing.aoc.common.gaussian;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GaussianEliminationSystemImpl1 implements GaussianEliminationSystem {

  private static final Integer EXEC_SCALE = 16;
  private static final Integer RESULT_SCALE = 3;

  // function to get matrix content
  public BigDecimal[] applyAlgorithm(BigDecimal[][] matrix) {

    /* reduction into r.e.f. */
    int singularFlag = forwardElimination(matrix);

    /* if matrix is singular */
    if (singularFlag!=-1) {
      System.out.println("Singular Matrix.");

      /* if the RHS of equation corresponding to zero row  is 0, * system has infinitely many solutions, else inconsistent*/
      if (matrix[singularFlag][matrix.length].compareTo(BigDecimal.ZERO)!=0) {
        System.out.print("Inconsistent System.");
      } else {
        System.out.print("May have infinitely many solutions.");
      }

      return new BigDecimal[0];
    }

    /* get solution to system and print it using backward substitution */
    return backSubstitution(matrix);
  }

  // function for elementary operation of swapping two rows
  private void swapRows(BigDecimal[][] matrix, int i, int j) {
    for (int k = 0; k <= matrix.length; k++) {
      BigDecimal temp = matrix[i][k];
      matrix[i][k] = matrix[j][k];
      matrix[j][k] = temp;
    }
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

  // function to reduce matrix to r.e.f.
  private int forwardElimination(BigDecimal[][] matrix) {
    for (int k = 0; k < matrix.length; k++) {

      // Initialize maximum value and index for pivot
      int iMax = k;
      BigDecimal vMax = new BigDecimal(matrix[iMax][k].toBigInteger());

      /* find greater amplitude for pivot if any */
      for (int i = k + 1; i < matrix.length; i++)
        if (matrix[i][k].abs().compareTo(vMax) > 0) {
          vMax = new BigDecimal(matrix[iMax][k].toBigInteger());
          iMax = i;
        }

      /* if a principal diagonal element  is zero,
       * it denotes that matrix is singular, and
       * will lead to a division-by-zero later. */
      if (matrix[k][iMax].compareTo(BigDecimal.ZERO)==0) {
        return k; // Matrix is singular
      }

      /* Swap the greatest value row with current row
       */
      if (iMax!=k) {
        swapRows(matrix, k, iMax);
      }

      for (int i = k + 1; i < matrix.length; i++) {

        /* factor f to set current row kth element to 0, and subsequently remaining kth column to 0 */
        BigDecimal f = matrix[i][k].divide(matrix[k][k], EXEC_SCALE, RoundingMode.HALF_UP);

        /* subtract fth multiple of corresponding kth row element*/
        for (int j = k + 1; j <= matrix.length; j++)
          matrix[i][j] = matrix[i][j].subtract(matrix[k][j].multiply(f));

        /* filling lower triangular matrix with zeros*/
        matrix[i][k] = BigDecimal.ZERO;
      }
    }
    return -1;
  }

  // function to calculate the values of the unknowns
  private BigDecimal[] backSubstitution(BigDecimal[][] matrix) {
    BigDecimal[] x = new BigDecimal[matrix.length]; // An array to store solution

    /* Start calculating from last equation up to the first */
    for (int i = matrix.length - 1; i >= 0; i--) {

      /* start with the RHS of the equation */
      x[i] = matrix[i][matrix.length];

      /* Initialize j to i+1 since matrix is upper triangular*/
      for (int j = i + 1; j < matrix.length; j++) {

        /* subtract all the lhs values
         * except the coefficient of the variable
         * whose value is being calculated */
        x[i] = x[i].subtract(matrix[i][j].multiply(x[j]));
      }

      /* divide the RHS by the coefficient of the unknown being calculated */
      x[i] = x[i].divide(matrix[i][i], EXEC_SCALE, RoundingMode.HALF_UP);
    }

    for (int i = 0; i < matrix.length; i++) {
      x[i] = x[i].setScale(RESULT_SCALE, RoundingMode.HALF_UP);
    }

    return x;
  }
}
