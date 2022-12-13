package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;

public class Day4 extends Day {

    private static final String START_SEQ_5 = "00000";
    private static final String START_SEQ_6 = "000000";

    @Override
    protected String part1(String fileContents) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");

        boolean found = false;
        String result = "";

        long number = 0;
        while (!found) {

            md.update((fileContents + number).getBytes());
            byte[] digest = md.digest();

            result = Hex.encodeHexString(digest).toUpperCase();

            if (result.startsWith(START_SEQ_5)) {
                found = true;
            } else {
                number++;
            }
        }

        System.out.println(fileContents + " --> " + result + " ::: " + number);
        return String.valueOf(number);
    }

    @Override
    protected String part2(String fileContents) throws Exception {

        MessageDigest md = MessageDigest.getInstance("MD5");

        boolean found = false;
        String result = "";

        long number = 0;
        while (!found) {

            md.update((fileContents + number).getBytes());
            byte[] digest = md.digest();

            result = Hex.encodeHexString(digest).toUpperCase();

            if (result.startsWith(START_SEQ_6)) {
                found = true;
            } else {
                number++;
            }
        }

        System.out.println("Part 2: " + fileContents + " --> " + result + " ::: " + number);
        return String.valueOf(number);
    }

    public static void main(String[] args) {
        Day.run(Day4::new, "2015/D4_small.txt", "2015/D4_full.txt");
    }
}
