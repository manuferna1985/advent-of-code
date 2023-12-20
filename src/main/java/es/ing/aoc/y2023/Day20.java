package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MathUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static es.ing.aoc.y2023.Day20.Signal.HIGH;
import static es.ing.aoc.y2023.Day20.Signal.LOW;
import static es.ing.aoc.y2023.Day20.Status.OFF;
import static es.ing.aoc.y2023.Day20.Status.ON;
import static es.ing.aoc.y2023.Day20.Type.BROADCASTER;
import static es.ing.aoc.y2023.Day20.Type.BUTTON;
import static es.ing.aoc.y2023.Day20.Type.CONJUNCTION;
import static es.ing.aoc.y2023.Day20.Type.FLIP_FLOP;

public class Day20 extends Day {

  private static final String RX_MODULE = "rx";

  enum Signal {HIGH, LOW}

  enum Status {ON, OFF}

  enum Type {
    BUTTON("button"),
    FLIP_FLOP("%"),
    CONJUNCTION("&"),
    BROADCASTER("broadcaster");

    private final String value;

    Type(String value) {
      this.value = value;
    }

    public static Type of(String str) {
      return Arrays.stream(Type.values())
          .filter(m -> str.startsWith(m.value))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Wrong module!"));
    }
  }

  static class Module {
    String name;
    Type type;
    List<String> destModules;
    List<Pair<String, Signal>> pendingSignals;
    Status status;
    Map<String, Signal> memory;

    public Module(String name, Type type, List<String> destModules) {
      this.name = name;
      this.type = type;
      this.destModules = destModules;
      this.pendingSignals = new ArrayList<>();
      this.status = OFF;
      this.memory = new HashMap<>();
    }

    public Signal flip() {
      this.status = ON.equals(this.status) ? OFF:ON;
      return ON.equals(this.status) ? HIGH:LOW;
    }

    public String status() {
      if (FLIP_FLOP.equals(this.type)) {
        return ON.equals(this.status) ? "1":"0";
      } else if (CONJUNCTION.equals(this.type)) {
        return this.memory.values().stream().allMatch(HIGH::equals) ? "1":"0";
      } else {
        return ".";
      }
    }
  }

  record WatchModule(String moduleName, AtomicLong repPattern) {
  }

  record PulseCounter(AtomicLong high, AtomicLong low, AtomicLong it, List<WatchModule> watched) {

    PulseCounter() {
      this(new AtomicLong(), new AtomicLong(), new AtomicLong(), new ArrayList<>());
    }

    public void inc(Signal sig) {
      if (HIGH.equals(sig)) {
        high.incrementAndGet();
      } else {
        low.incrementAndGet();
      }
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    PulseCounter counter = getPulses(fileContents, 1000L);
    return String.valueOf(counter.high.get() * counter.low.get());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    if (fileContents.contains(RX_MODULE)) {
      PulseCounter counter = getPulses(fileContents, null);
      return String.valueOf(counter.it);
    } else {
      return "N/A";
    }
  }

  private PulseCounter getPulses(String fileContents, Long maxIterations) {
    String[] lines = fileContents.split(System.lineSeparator());

    Map<String, Module> modules = new HashMap<>();
    for (String line : lines) {
      String[] parts = line.split(" -> ");
      Type type = Type.of(parts[0]);
      String name = BROADCASTER.equals(type) ? parts[0]:parts[0].substring(1);
      List<String> desModules = Arrays.stream(parts[1].split(",")).map(String::trim).toList();
      modules.put(name, new Module(name, type, desModules));
    }

    initConjMemories(modules);

    PulseCounter counter = new PulseCounter();
    Module bc = modules.get(BROADCASTER.value);
    Module button = new Module(BUTTON.value, BUTTON, List.of(bc.name));
    modules.put(BUTTON.value, button);

    long i = 0L;
    boolean maxItReached = false;

    if (maxIterations==null) {
      counter.watched.addAll(searchModulesToWatch(modules));
    }

    while (!maxItReached) {
      sendSignal(i, button, modules, LOW, counter);

      boolean pulsesSent = true;
      while (pulsesSent) {
        pulsesSent = false;
        for (Module m : modules.values()) {
          if (!m.pendingSignals.isEmpty()) {

            Pair<String, Signal> lastSignal = m.pendingSignals.remove(0);

            switch (m.type) {
              case BROADCASTER:
                sendSignal(i, m, modules, LOW, counter);
                pulsesSent = true;
                break;
              case FLIP_FLOP:
                if (LOW.equals(lastSignal.getRight())) {
                  sendSignal(i, m, modules, m.flip(), counter);
                  pulsesSent = true;
                }
                break;
              case CONJUNCTION:
                // Conjunction
                m.memory.put(lastSignal.getLeft(), lastSignal.getRight());
                if (m.memory.values().stream().allMatch(HIGH::equals)) {
                  sendSignal(i, m, modules, LOW, counter);
                } else {
                  sendSignal(i, m, modules, HIGH, counter);
                }
                pulsesSent = true;
                break;
            }
          }
        }
      }
      i++;

      if (maxIterations!=null){
        if (i==maxIterations) {
          maxItReached = true;
        }
      } else {
        if (counter.watched.stream().allMatch(w -> w.repPattern.get()!=0L)) {
          long mcm = counter.watched.stream().mapToLong(w -> w.repPattern.get()).reduce(1L, MathUtils::mcm);
          counter.it.set(mcm);
          maxItReached = true;
        }
      }
    }
    return counter;
  }

  private List<WatchModule> searchModulesToWatch(Map<String, Module> modules) {
    List<String> watch = new ArrayList<>();
    watch.add(RX_MODULE);

    while (watch.size() <= 1) {
      String name = watch.remove(0);
      watch.addAll(modules.values().stream().filter(m -> m.destModules.contains(name)).map(m -> m.name).toList());
    }
    return watch.stream().map(name -> new WatchModule(name, new AtomicLong(0))).toList();
  }

  private void initConjMemories(Map<String, Module> modules) {
    for (Module m1 : modules.values()) {
      for (Module m2 : modules.values()) {
        if (!m2.equals(m1) && m2.destModules.contains(m1.name) && CONJUNCTION.equals(m1.type)) {
          m1.memory.put(m2.name, LOW);
        }
      }
    }
  }

  private void sendSignal(long i, Module m, Map<String, Module> modules, Signal signal, PulseCounter counter) {
    m.destModules.stream().map(modules::get).forEach(dest -> {
      if (dest!=null) {
        Optional<WatchModule> watch = counter.watched.stream().filter(w -> w.moduleName.equals(m.name)).findFirst();
        if (HIGH.equals(signal) && watch.isPresent()) {
          System.out.printf("[%-10d] %s -%s-> %s\n", i, m.name, signal.name(), dest.name);
          watch.get().repPattern.set((i + 1) - watch.get().repPattern.get());
        }

        dest.pendingSignals.add(Pair.of(m.name, signal));
      }
      counter.inc(signal);
    });
  }

  public static void main(String[] args) {
    Day.run(Day20::new, "2023/D20_small.txt", "2023/D20_full.txt");
  }
}
