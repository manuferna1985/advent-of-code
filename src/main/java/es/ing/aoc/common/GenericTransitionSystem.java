package es.ing.aoc.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class GenericTransitionSystem <S> implements TransitionSystem<S> {

    public Map<S, Long> reachAll() {
        Map<S, Long> distance = new HashMap<>();
        PriorityQueue<S> unvisited = new PriorityQueue<>(Comparator.comparingLong(distance::get));

        for (var s : this.initial()) {
            distance.put(s, 0L);
            unvisited.add(s);
        }

        while (!unvisited.isEmpty()) {
            var v = unvisited.poll();
            var dv = distance.get(v);

            for (var s : successors(v)) {
                var nv = s.a;
                var dnv = dv + s.b;

                if (distance.containsKey(nv)) {
                    var cur = distance.get(nv);
                    if (cur > dnv) {
                        unvisited.remove(nv);
                        distance.put(nv, dnv);
                        unvisited.add(nv);
                    }
                } else {
                    distance.put(nv, dnv);
                    unvisited.add(nv);
                }
            }
        }
        return distance;
    }

    public Pair<Map<S, Long>, List<S>> reachFinal() {
        Map<S, Long> distance = new HashMap<>();
        Map<S, S> pred = new HashMap<>();
        PriorityQueue<S> unvisited = new PriorityQueue<>(Comparator.comparingLong(distance::get));

        for (var s : this.initial()) {
            distance.put(s, 0L);
            unvisited.add(s);
        }

        while (!unvisited.isEmpty()) {
            var v = unvisited.poll();
            var dv = distance.get(v);
            if (isFinal(v)) {
                List<S> trace = new ArrayList<>();
                {
                    var s = v;
                    while (s != null) {
                        trace.add(0, s);
                        s = pred.get(s);
                    }
                }
                return Pair.of(distance, trace);
            }

            for (var s : successors(v)) {
                var nv = s.a;
                var dnv = dv + s.b;

                if (distance.containsKey(nv)) {
                    var cur = distance.get(nv);
                    if (cur > dnv) {
                        unvisited.remove(nv);
                        distance.put(nv, dnv);
                        unvisited.add(nv);
                        pred.put(nv, v);
                    }
                } else {
                    distance.put(nv, dnv);
                    unvisited.add(nv);
                    pred.put(nv, v);
                }
            }
        }

        return Pair.of(distance, null);
    }
}
