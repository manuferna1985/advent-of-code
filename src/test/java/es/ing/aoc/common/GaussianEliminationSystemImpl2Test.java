package es.ing.aoc.common;

import es.ing.aoc.common.gaussian.GaussianEliminationSystem;
import es.ing.aoc.common.gaussian.GaussianEliminationSystemImpl2;

class GaussianEliminationSystemImpl2Test extends GaussianEliminationSystemTest{

  @Override
  GaussianEliminationSystem getGaussianAlgorithm() {
    return new GaussianEliminationSystemImpl2();
  }
}