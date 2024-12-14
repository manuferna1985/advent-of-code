package es.ing.aoc.common;

import es.ing.aoc.common.gaussian.GaussianEliminationSystem;
import es.ing.aoc.common.gaussian.GaussianEliminationSystemImpl1;

class GaussianEliminationSystemImpl1Test extends GaussianEliminationSystemTest{

  @Override
  GaussianEliminationSystem getGaussianAlgorithm() {
    return new GaussianEliminationSystemImpl1();
  }
}