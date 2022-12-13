package es.ing.aoc.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TreeNodeBuilder {

    private TreeNodeBuilder() {
        throw new RuntimeException("Constructor not meant to be called");
    }

    public static TreeNode createDivider(String line) {
        TreeNode div = createTree(line);
        div.isDivider = true;
        return div;
    }

    public static TreeNode createTree(String line) {
        TreeNode n = new TreeNode();
        if (line.charAt(0) == '[') {
            n.elements = splitBySameLevelCommas(line).stream()
                    .map(TreeNodeBuilder::createTree)
                    .peek(child -> child.parent = n)
                    .collect(Collectors.toList());
        } else {
            n.value = Integer.parseInt(line);
        }
        return n;
    }

    private static List<String> splitBySameLevelCommas(String line) {

        if ("[]".equals(line)) {
            return Collections.emptyList();
        }

        List<Integer> commas = new ArrayList<>();
        List<String> elements = new ArrayList<>();

        int depth = 0;
        int i = 1;
        do {
            if (line.charAt(i) == '[') {
                depth++;
            } else if (line.charAt(i) == ']') {
                depth--;
            } else if (line.charAt(i) == ',') {
                if (depth == 0) {
                    commas.add(i);
                }
            }
            i++;
        } while (i < line.length());

        if (commas.isEmpty()) {
            return List.of(line.substring(1, line.length() - 1));
        } else {
            elements.add(line.substring(1, commas.get(0)));
            for (int c = 0; c < commas.size() - 1; c++) {
                elements.add(line.substring(commas.get(c) + 1, commas.get(c + 1)));
            }
            elements.add(line.substring(commas.get(commas.size() - 1) + 1, line.length() - 1));
            return elements;
        }
    }
}
