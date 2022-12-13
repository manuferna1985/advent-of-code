package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 extends Day {

    private static final Integer DIR_LIMIT_SIZE = 100000;
    private static final Integer TOTAL_DISK = 70000000;
    private static final Integer UPDATE_NEEDED_SPACE = 30000000;

    abstract static class Item {
        public String name;
        public Directory parent;

        protected Item(Directory parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item item = (Item) o;

            return name.equals(item.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        abstract Integer getSize();
    }

    static final class Directory extends Item {
        public final Map<String, Item> childs = new HashMap<>();
        private Integer size = null;

        public Directory(Directory parent, String name) {
            super(parent, name);
        }

        public void add(Item item) {
            this.childs.put(item.name, item);
        }

        public boolean contains(Item item) {
            return this.childs.containsKey(item.name);
        }

        public Item get(Item item) {
            return this.childs.get(item.name);
        }

        public Integer getSize() {
            if (this.size == null) {
                this.size = this.childs.values().stream().mapToInt(Item::getSize).sum();
            }
            return this.size;
        }
    }

    static final class File extends Item {
        public final Integer size;

        public File(Directory parent, String name, Integer size) {
            super(parent, name);
            this.size = size;
        }

        public Integer getSize() {
            return this.size;
        }
    }

    private List<Directory> getAllDirs(Directory root) {
        List<Directory> dirs = new ArrayList<>();
        dirs.add(root);
        for (Item item : root.childs.values()) {
            if (item instanceof Directory) {
                dirs.addAll(getAllDirs((Directory) item));
            }
        }
        return dirs;
    }

    private Integer countDirectoriesWithAtMost(Directory root, Integer limitSize) {
        return getAllDirs(root).stream().mapToInt(Item::getSize).filter(size -> size <= limitSize).sum();
    }

    private Integer findSmallestDirectoryWithAtLeast(Directory root, Integer minSize) {
        return getAllDirs(root).stream().mapToInt(Item::getSize).filter(size -> size > minSize).min().orElse(-1);
    }

    private Directory processLines(String[] lines) {
        Directory root = new Directory(null, "/");
        Directory pwd = root;

        for (String line : lines) {
            if ("$ cd /".equals(line)) {
                pwd = root;
            } else if ("$ cd ..".equals(line)) {
                pwd = pwd.parent;
            } else if (line.startsWith("$ cd")) {
                Directory newDir = new Directory(pwd, line.split(" ")[2]);
                if (pwd.contains(newDir)) {
                    pwd = (Directory) pwd.get(newDir);
                } else {
                    pwd.add(newDir);
                    pwd = newDir;
                }
            } else if (line.startsWith("dir")) {
                // Directory (LS)
                Directory newDir = new Directory(pwd, line.split(" ")[1]);
                if (!pwd.contains(newDir)) {
                    pwd.add(newDir);
                }
            } else if (!line.startsWith("$ ls")) {
                // File (LS)
                String[] fileParts = line.split(" ");
                File newFile = new File(pwd, fileParts[1], Integer.parseInt(fileParts[0]));
                if (!pwd.contains(newFile)) {
                    pwd.add(newFile);
                }
            }
        }
        return root;
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        String[] lines = fileContents.split(System.lineSeparator()); // when input file is multiline
        Directory root = processLines(lines);
        return String.valueOf(countDirectoriesWithAtMost(root, DIR_LIMIT_SIZE));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        String[] lines = fileContents.split(System.lineSeparator()); // when input file is multiline
        Directory root = processLines(lines);

        Integer currentFreeSpace = TOTAL_DISK - root.getSize();
        Integer minSpaceToDelete = UPDATE_NEEDED_SPACE - currentFreeSpace;

        return String.valueOf(findSmallestDirectoryWithAtLeast(root, minSpaceToDelete));
    }

    public static void main(String[] args) {
        Day.run(Day7::new, "2022/D7_small.txt", "2022/D7_full.txt");
    }
}
