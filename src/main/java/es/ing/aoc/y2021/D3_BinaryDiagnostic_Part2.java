package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class D3_BinaryDiagnostic_Part2 {


    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part1.class.getResource("2021/D3_full.txt").toURI()), Charset.defaultCharset());

        System.out.println(allLines);


        List<String> oxygen = new ArrayList<>(allLines);
        final AtomicInteger index = new AtomicInteger(0);
        boolean end = false;
        while(!end){
            Long ones = oxygen.stream().map(s -> String.valueOf(s.charAt(index.get()))).filter("1"::equals).count();
            Long zeroes = oxygen.stream().map(s -> String.valueOf(s.charAt(index.get()))).filter("0"::equals).count();

            if (ones >= zeroes){
                oxygen = oxygen.stream().filter(s -> "1".equals(String.valueOf(s.charAt(index.get())))).collect(Collectors.toList());
            } else {
                oxygen = oxygen.stream().filter(s -> "0".equals(String.valueOf(s.charAt(index.get())))).collect(Collectors.toList());
            }

            if (oxygen.size() == 1){
                end = true;
            }
            index.incrementAndGet();
        }

        System.out.println(oxygen);

        List<String> co2 = new ArrayList<>(allLines);
        final AtomicInteger index2 = new AtomicInteger(0);
        end = false;
        while(!end){
            Long ones = co2.stream().map(s -> String.valueOf(s.charAt(index2.get()))).filter("1"::equals).count();
            Long zeroes = co2.stream().map(s -> String.valueOf(s.charAt(index2.get()))).filter("0"::equals).count();

            if (ones >= zeroes){
                co2 = co2.stream().filter(s -> "0".equals(String.valueOf(s.charAt(index2.get())))).collect(Collectors.toList());
            } else {
                co2 = co2.stream().filter(s -> "1".equals(String.valueOf(s.charAt(index2.get())))).collect(Collectors.toList());
            }

            if (co2.size() == 1){
                end = true;
            }
            index2.incrementAndGet();
        }
        System.out.println(co2);


        System.out.println("Life support rating: " + Long.parseLong(oxygen.get(0), 2) * Long.parseLong(co2.get(0), 2));
    }
}
