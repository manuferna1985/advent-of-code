package es.ing.aoc.common;

import java.util.Collection;

public interface TransitionSystem <S> {
    Collection<S> initial();
    Collection<Pair<S, Long>> successors(S state);
    boolean isFinal(S state);
}
