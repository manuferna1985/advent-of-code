package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.TreeNode;
import es.ing.aoc.common.TreeNodeBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 extends Day {

    @Override
    protected String part1(String fileContents) throws Exception {
        String[] lines = fileContents.split(System.lineSeparator()); // when input file is multiline

        int groupCounter = 1;
        int sum = 0;
        for (int i = 0; i < lines.length; i += 3) {
            TreeNode p1 = TreeNodeBuilder.createTree(lines[i]);
            TreeNode p2 = TreeNodeBuilder.createTree(lines[i + 1]);
            if (p1.compareTo(p2) < 0) {
                sum += groupCounter;
            }
            groupCounter++;
        }

        return String.valueOf(sum);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        String[] lines = fileContents.split(System.lineSeparator()); // when input file is multiline

        List<TreeNode> packets = new ArrayList<>();
        for (int i = 0; i < lines.length; i += 3) {
            packets.add(TreeNodeBuilder.createTree(lines[i]));
            packets.add(TreeNodeBuilder.createTree(lines[i + 1]));
        }

        TreeNode div1 = TreeNodeBuilder.createDivider("[[2]]");
        packets.add(div1);
        TreeNode div2 = TreeNodeBuilder.createDivider("[[6]]");
        packets.add(div2);

        Collections.sort(packets);

        return String.valueOf((packets.indexOf(div1) + 1) * (packets.indexOf(div2) + 1));
    }

    public static void main(String[] args) {
        Day.run(Day13::new, "2022/D13_small.txt", "2022/D13_full.txt");
    }
}
