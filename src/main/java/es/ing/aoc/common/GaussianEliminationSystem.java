package es.ing.aoc.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GaussianEliminationSystem {

  private static final Integer EXEC_SCALE = 16;
  private static final Integer RESULT_SCALE = 3;

  private GaussianEliminationSystem() {
    throw new RuntimeException("Constructor not meant to be called");
  }

  // function to get matrix content
  public static BigDecimal[] applyAlgorithm(BigDecimal[][] mat) {

    /* reduction into r.e.f. */
    int singularFlag = forwardElimination(mat);

    /* if matrix is singular */
    if (singularFlag!=-1) {
      System.out.println("Singular Matrix.");

      /* if the RHS of equation corresponding to zero row  is 0, * system has infinitely many solutions, else inconsistent*/
      if (mat[singularFlag][mat.length].compareTo(BigDecimal.ZERO)!=0) {
        System.out.print("Inconsistent System.");
      } else {
        System.out.print("May have infinitely many solutions.");
      }

      return new BigDecimal[0];
    }

    /* get solution to system and print it using backward substitution */
    return backSubstitution(mat);
  }

  // function for elementary operation of swapping two rows
  private static void swapRows(BigDecimal[][] mat, int i, int j) {
    for (int k = 0; k <= mat.length; k++) {
      BigDecimal temp = mat[i][k];
      mat[i][k] = mat[j][k];
      mat[j][k] = temp;
    }
  }

  // function to print matrix content at any stage
  private static void print(BigDecimal[][] mat) {
    for (int i = 0; i < mat.length; i++, System.out.println()) {
      for (int j = 0; j <= mat.length; j++) {
        System.out.print(mat[i][j]);
      }
      System.out.println();
    }
  }

  // function to reduce matrix to r.e.f.
  private static int forwardElimination(BigDecimal[][] mat) {
    for (int k = 0; k < mat.length; k++) {

      // Initialize maximum value and index for pivot
      int iMax = k;
      BigDecimal vMax = new BigDecimal(mat[iMax][k].toBigInteger());

      /* find greater amplitude for pivot if any */
      for (int i = k + 1; i < mat.length; i++)
        if (mat[i][k].abs().compareTo(vMax) > 0) {
          vMax = new BigDecimal(mat[iMax][k].toBigInteger());
          iMax = i;
        }

      /* if a principal diagonal element  is zero,
       * it denotes that matrix is singular, and
       * will lead to a division-by-zero later. */
      if (mat[k][iMax].compareTo(BigDecimal.ZERO)==0) {
        return k; // Matrix is singular
      }

      /* Swap the greatest value row with current row
       */
      if (iMax!=k) {
        swapRows(mat, k, iMax);
      }

      for (int i = k + 1; i < mat.length; i++) {

        /* factor f to set current row kth element to 0, and subsequently remaining kth column to 0 */
        BigDecimal f = mat[i][k].divide(mat[k][k], EXEC_SCALE, RoundingMode.HALF_UP);

        /* subtract fth multiple of corresponding kth row element*/
        for (int j = k + 1; j <= mat.length; j++)
          mat[i][j] = mat[i][j].subtract(mat[k][j].multiply(f));

        /* filling lower triangular matrix with zeros*/
        mat[i][k] = BigDecimal.ZERO;
      }
    }
    return -1;
  }

  // function to calculate the values of the unknowns
  private static BigDecimal[] backSubstitution(BigDecimal[][] mat) {
    BigDecimal[] x = new BigDecimal[mat.length]; // An array to store solution

    /* Start calculating from last equation up to the first */
    for (int i = mat.length - 1; i >= 0; i--) {

      /* start with the RHS of the equation */
      x[i] = mat[i][mat.length];

      /* Initialize j to i+1 since matrix is upper triangular*/
      for (int j = i + 1; j < mat.length; j++) {

        /* subtract all the lhs values
         * except the coefficient of the variable
         * whose value is being calculated */
        x[i] = x[i].subtract(mat[i][j].multiply(x[j]));
      }

      /* divide the RHS by the coefficient of the unknown being calculated */
      x[i] = x[i].divide(mat[i][i], EXEC_SCALE, RoundingMode.HALF_UP);
    }

    for (int i = 0; i < mat.length; i++) {
      x[i] = x[i].setScale(RESULT_SCALE, RoundingMode.HALF_UP);
    }

    return x;
  }
}
