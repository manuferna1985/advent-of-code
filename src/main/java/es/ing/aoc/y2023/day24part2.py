#!/usr/bin/env python3

from z3 import *

rx, ry, rz = Ints('rx ry rz')
rvx, rvy, rvz = Ints('rvx rvy rvz')
t0, t1, t2 = Ints('t0 t1 t2')
answer = Int('answer')

solve(
    rx + t0 * rvx == 364193859817003 + t0 * 85,
    ry + t0 * rvy == 337161998875178 + t0 * 85,
    rz + t0 * rvz == 148850519939119 + t0 * 473,

    rx + t1 * rvx == 222402516161891 + t1 * 123,
    ry + t1 * rvy == 289638719990878 + t1 * -40,
    rz + t1 * rvz == 261939904566871 + t1 * 25,

    rx + t2 * rvx == 219626703416113 + t2 * 115,
    ry + t2 * rvy == 76777384100180 + t2 * 317,
    rz + t2 * rvz == 165418060594769 + t2 * 193,

    answer == rx + ry + rz,
)