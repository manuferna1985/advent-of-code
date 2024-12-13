package es.ing.aoc.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class GaussianEliminationSystemTest {

  @Test
  void testGaussianSolver1eq() {
    Double equations2[][] = {{5.0, 20.0}};
    BigDecimal[] solutions = GaussianEliminationSystem.applyAlgorithm(MatrixUtils.transformMatrix(BigDecimal.class, equations2, BigDecimal::new));
    Assertions.assertEquals(4.0, solutions[0].doubleValue());
  }

  @Test
  void testGaussianSolver3eqs() {

    Double equations2[][] = {
        {1.0, 2.0, 3.0, 34.0},
        {-4.0, 15.0, 6.0, 105.0},
        {27.0, -8.0, -59.0, -372.0}};

    BigDecimal[] solutions = GaussianEliminationSystem.applyAlgorithm(MatrixUtils.transformMatrix(BigDecimal.class, equations2, BigDecimal::new));

    Assertions.assertEquals(3.0, solutions[0].doubleValue());
    Assertions.assertEquals(5.0, solutions[1].doubleValue());
    Assertions.assertEquals(7.0, solutions[2].doubleValue());
  }

  @Test
  void testGaussianSolver5eqs() {

    Double equations2[][] = {
        {4.0, -2.0, -1.0, 1.0, 2.0, 14.0},
        {1.0, 2.0, 2.0, -1.0, 4.0, 14.0},
        {2.0, -1.0, 4.0, -2.0, 2.0, -8.0},
        {1.0, 1.0, 1.0, 1.0, 1.0, 23.0},
        {6.0, 4.0, 1.0, -6.0, 6.0, -4.0}};

    BigDecimal[] solutions = GaussianEliminationSystem.applyAlgorithm(MatrixUtils.transformMatrix(BigDecimal.class, equations2, BigDecimal::new));

    Assertions.assertEquals(4.0, solutions[0].doubleValue());
    Assertions.assertEquals(6.0, solutions[1].doubleValue());
    Assertions.assertEquals(2.0, solutions[2].doubleValue());
    Assertions.assertEquals(10.0, solutions[3].doubleValue());
    Assertions.assertEquals(1.0, solutions[4].doubleValue());
  }
}