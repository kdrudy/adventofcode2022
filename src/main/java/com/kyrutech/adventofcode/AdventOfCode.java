package com.kyrutech.adventofcode;

import java.awt.*;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdventOfCode {

    public static int shortestPathSteps = Integer.MAX_VALUE;
    public static Stack<Point> shortestPath = new Stack<>();

    public static void main(String[] args) {
//        day10();
//        day11();
//        day12();
        InputStream is = AdventOfCode.class.getClassLoader().getResourceAsStream("adventofcode/pairPackets.txt");
        Scanner sc = new Scanner(is);
//        int count = 1;
//        int sumOfTrues = 0;
//        while (sc.hasNext()) {
//            String left = sc.nextLine();
//            String right = sc.nextLine();
//            boolean isBalanced = isBalanced(left, right) != -1;
//            System.out.println(left);
//            System.out.println(right);
//            System.out.println(count + " Is Balanced: " + isBalanced);
//            System.out.println();
//            if(isBalanced) {
//                sumOfTrues += count;
//            }
//
//            if (sc.hasNext()) {
//                sc.nextLine(); //Eat space between
//            }
//            count++;
//        }
//        System.out.println("Sum of Trues: " + sumOfTrues);

        List<String> packets = new ArrayList<>();
        packets.add("[[2]]");
        packets.add("[[6]]");
        while(sc.hasNext()) {
            String line = sc.nextLine();
            if(!line.isEmpty()) {
                packets.add(line);
            }
        }
        packets.sort((a,b) -> isBalanced(b, a));
        packets.forEach(System.out::println);
        int first = packets.indexOf("[[2]]")+1;
        int second = packets.indexOf("[[6]]")+1;
        System.out.println(first*second);
    }

    private static int isBalanced(String leftString, String rightString) {
        List<String> leftPieces = tokenize(leftString);
        List<String> rightPieces = tokenize(rightString);

//        if(leftPieces.size() < rightPieces.size()) { return true; }
//        if(rightPieces.size() < leftPieces.size()) { return false; }

        int b = 0;
        if(leftPieces.size() == 0 && rightPieces.size() > 0) { return 1;}
        for(int i = 0;i<leftPieces.size() && b == 0;i++) {
            String left = leftPieces.get(i);
            if(rightPieces.size() <= i) { return -1; }
            String right = rightPieces.get(i);
            if(left.startsWith("[") && right.startsWith("[")) {
                b = isBalanced(left, right);
            } else if(left.startsWith("[")) {
                b = isBalanced(left, "[" + right + "]");
            } else if(right.startsWith("[")) {
                b = isBalanced("[" + left +"]", right);
            } else {
                if(Integer.parseInt(left) > Integer.parseInt(right)) {
                    b = -1;
                }
                if(Integer.parseInt(left) < Integer.parseInt(right)) {
                    b = 1;
                }
            }
        }

        return b;
    }

    private static List<String> tokenize(String string) {
        List<String> tokens = new ArrayList<>();
        String[] pieces = string.split("");
        Stack<String> brackets = new Stack<>();
        StringBuilder currentToken = new StringBuilder();
        for (String piece : pieces) {
            if (piece.equals("[")) {
                if(brackets.size() > 0) {
                    currentToken.append(piece);
                }
                brackets.push(piece);
            } else if (piece.equals("]")) {
                if(brackets.size() > 1) {
                    currentToken.append(piece);
                }
                brackets.pop();
                if(brackets.size() == 0 && currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
            } else if (piece.equals(",") && brackets.size() == 1) {
                tokens.add(currentToken.toString());
                currentToken = new StringBuilder();
            } else {
                currentToken.append(piece);
            }
        }

        return tokens;
    }

    private static void day12() {
        InputStream is = AdventOfCode.class.getClassLoader().getResourceAsStream("adventofcode/heightmap.txt");
        Scanner sc = new Scanner(is);
        List<String> lines = new ArrayList<>();
        int height = 0;
        while (sc.hasNext()) {
            lines.add(sc.nextLine());
            height++;
        }
        char[][] grid = new char[height][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
//        Arrays.stream(grid).forEach(r -> System.out.println(Arrays.toString(r)));
        List<Node> potentialStarts = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S') {
                    potentialStarts.add(new Node(i, j));
                }
                if (grid[i][j] == 'a') {
                    potentialStarts.add(new Node(i, j));
                }
            }
        }

        int fewestStepsStart = Integer.MAX_VALUE;
        for (Node start : potentialStarts) {
            List<Node> queue = new ArrayList<>();
            Set<Node> visited = new HashSet<>();
            queue.add(start);
            visited.add(start);
            Node end = null;
            while (!queue.isEmpty()) {
                Node current = queue.remove(0);
                if (grid[current.getX()][current.getY()] == 'E') {
                    end = current;
                    break;
                }
                List<Node> nextNodes = getNextNodes(current, grid);
                for (Node n : nextNodes) {
                    if (!visited.contains(n)) {
                        visited.add(n);
                        n.setParent(current);
                        queue.add(n);
                    }
                }
//            System.out.println(queue.size());
            }

            if (end != null) {
                int steps = 0;
                Node current = end;
                while (current.getParent() != null) {
                    current = current.getParent();
                    steps++;
                }
//            System.out.println(steps);
                if (steps < fewestStepsStart) {
                    fewestStepsStart = steps;
                }
            }
        }
        System.out.println("Fewest: " + fewestStepsStart);
    }

    private static List<Node> getNextNodes(Node current, char[][] grid) {
        List<Node> nextNodes = new ArrayList<>();
        int value = grid[current.getX()][current.getY()] == 'S' ? 'a' : grid[current.getX()][current.getY()];
        if (current.getX() - 1 >= 0) {
            if (grid[current.getX() - 1][current.getY()] - value == 1 || grid[current.getX() - 1][current.getY()] <= value) {
                nextNodes.add(new Node(current.getX() - 1, current.getY()));
            } else if ((grid[current.getX()][current.getY()] == 'z' || grid[current.getX()][current.getY()] == 'y') && grid[current.getX() - 1][current.getY()] == 'E') {
                nextNodes.add(new Node(current.getX() - 1, current.getY()));
            }
        }
        if (current.getY() - 1 >= 0) {
            if (grid[current.getX()][current.getY() - 1] - value == 1 || grid[current.getX()][current.getY() - 1] <= value) {
                nextNodes.add(new Node(current.getX(), current.getY() - 1));
            } else if ((grid[current.getX()][current.getY()] == 'z' || grid[current.getX()][current.getY()] == 'y') && grid[current.getX()][current.getY() - 1] == 'E') {
                nextNodes.add(new Node(current.getX(), current.getY() - 1));
            }
        }
        if (current.getX() + 1 < grid.length) {
            if (grid[current.getX() + 1][current.getY()] - value == 1 || grid[current.getX() + 1][current.getY()] <= value) {
                nextNodes.add(new Node(current.getX() + 1, current.getY()));
            } else if ((grid[current.getX()][current.getY()] == 'z' || grid[current.getX()][current.getY()] == 'y') && grid[current.getX() + 1][current.getY()] == 'E') {
                nextNodes.add(new Node(current.getX() + 1, current.getY()));
            }
        }
        if (current.getY() + 1 < grid[current.getX()].length) {
            if (grid[current.getX()][current.getY() + 1] - value == 1 || grid[current.getX()][current.getY() + 1] <= value) {
                nextNodes.add(new Node(current.getX(), current.getY() + 1));
            } else if ((grid[current.getX()][current.getY()] == 'z' || grid[current.getX()][current.getY()] == 'y') && grid[current.getX()][current.getY() + 1] == 'E') {
                nextNodes.add(new Node(current.getX(), current.getY() + 1));
            }
        }
        return nextNodes;
    }

    private static void findPath(Stack<Point> path, char[][] grid) {
        ;
        Point p = path.peek();
        int value = grid[p.x][p.y] == 'S' ? 'a' : grid[p.x][p.y];
        if (grid[p.x][p.y] == 'E') {
            if (shortestPathSteps > path.size() - 1) {
                shortestPathSteps = path.size() - 1;
                shortestPath = (Stack<Point>) path.clone();
                System.out.println("Current Shortest: " + shortestPathSteps);
                System.out.println("Current Shortest: " + shortestPath);
            }
            return;
        }
        if (path.size() - 1 >= shortestPathSteps) {
            return;
        }

        List<Point> nextPoints = new ArrayList<>();
        if (p.x - 1 >= 0 && !path.contains(new Point(p.x - 1, p.y))) {
            if (Math.abs(grid[p.x - 1][p.y] - value) == 1 || grid[p.x - 1][p.y] == value) {
                nextPoints.add(new Point(p.x - 1, p.y));
            } else if ((grid[p.x][p.y] == 'z' || grid[p.x][p.y] == 'y') && grid[p.x - 1][p.y] == 'E') {
                nextPoints.add(new Point(p.x - 1, p.y));
            }
        }
        if (p.y - 1 >= 0 && !path.contains(new Point(p.x, p.y - 1))) {
            if (Math.abs(grid[p.x][p.y - 1] - value) == 1 || grid[p.x][p.y - 1] == value) {
                nextPoints.add(new Point(p.x, p.y - 1));
            } else if ((grid[p.x][p.y] == 'z' || grid[p.x][p.y] == 'y') && grid[p.x][p.y - 1] == 'E') {
                nextPoints.add(new Point(p.x, p.y - 1));
            }
        }
        if (p.x + 1 < grid.length && !path.contains(new Point(p.x + 1, p.y))) {
            if (Math.abs(grid[p.x + 1][p.y] - value) == 1 || grid[p.x + 1][p.y] == value) {
                nextPoints.add(new Point(p.x + 1, p.y));
            } else if ((grid[p.x][p.y] == 'z' || grid[p.x][p.y] == 'y') && grid[p.x + 1][p.y] == 'E') {
                nextPoints.add(new Point(p.x + 1, p.y));
            }
        }
        if (p.y + 1 < grid[p.x].length && !path.contains(new Point(p.x, p.y + 1))) {
            if (Math.abs(grid[p.x][p.y + 1] - value) == 1 || grid[p.x][p.y + 1] == value) {
                nextPoints.add(new Point(p.x, p.y + 1));
            } else if ((grid[p.x][p.y] == 'z' || grid[p.x][p.y] == 'y') && grid[p.x][p.y + 1] == 'E') {
                nextPoints.add(new Point(p.x, p.y + 1));
            }
        }

        while (!nextPoints.isEmpty()) {
            Point nextPoint = nextPoints.remove(0);
            path.push(nextPoint);
            findPath(path, grid);
            path.pop();
        }
    }

    private static void day11() {
        InputStream is = AdventOfCode.class.getClassLoader().getResourceAsStream("adventofcode/monkeystuff.txt");
        Scanner sc = new Scanner(is);
        List<Monkey> monkeys = new ArrayList<>();
        int lcm = 1;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.startsWith("Monkey")) {
                String[] parts = line.split(" ");
                monkeys.add(new Monkey(Integer.parseInt(parts[1].replace(":", ""))));
            } else {
                String[] parts = line.trim().split(":");
                switch (parts[0]) {
                    case "Starting items":
                        String[] items = parts[1].split(",");
                        Arrays.stream(items).forEach(i -> monkeys.get(monkeys.size() - 1).addItem(Long.parseLong(i.trim())));
                        break;
                    case "Operation":
                        monkeys.get(monkeys.size() - 1).setOperation(parts[1]);
                        break;
                    case "Test":
                        String[] testParts = parts[1].trim().split(" ");
                        int testValue = Integer.parseInt(testParts[2]);
                        lcm = lcm * testValue;
                        monkeys.get(monkeys.size() - 1).setTest(testValue);
                        break;
                    case "If true":
                        String[] ifTrueParts = parts[1].trim().split(" ");
                        int tossToTrue = Integer.parseInt(ifTrueParts[3]);
                        monkeys.get(monkeys.size() - 1).setIfTrue(tossToTrue);
                        break;
                    case "If false":
                        String[] ifFalseParts = parts[1].trim().split(" ");
                        int tossToFalse = Integer.parseInt(ifFalseParts[3]);
                        monkeys.get(monkeys.size() - 1).setIfFalse(tossToFalse);
                        break;
                }
            }
        }
        System.out.println("Start");
        monkeys.forEach(System.out::println);
        System.out.println();

        for (int i = 0; i < 10000; i++) {
            for (Monkey monkey : monkeys) {
                while (!monkey.getItems().isEmpty()) {
                    monkey.incrementInspections();
                    long item = monkey.getItems().remove(0);
                    long worryLevel = item;
                    String[] opParts = monkey.getOperation().trim().split(" ");
                    long left = "old".equals(opParts[2]) ? item : Integer.parseInt(opParts[2]);
                    long right = "old".equals(opParts[4]) ? item : Integer.parseInt(opParts[4]);
                    switch (opParts[3]) {
                        case "+":
                            worryLevel = left + right;
                            break;
                        case "-":
                            worryLevel = left - right;
                            break;
                        case "*":
                            worryLevel = left * right;
                            break;
                        case "/":
                            worryLevel = left / right;
                            break;
                    }
//                    worryLevel = worryLevel / 3;
                    worryLevel = worryLevel % lcm;

                    if (worryLevel % monkey.getTest() == 0) {
                        Monkey tossToMonkey = monkeys.stream().filter(m -> m.getId() == monkey.getIfTrue()).findFirst().orElse(null);
                        tossToMonkey.addItem(worryLevel);
                    } else {
                        Monkey tossToMonkey = monkeys.stream().filter(m -> m.getId() == monkey.getIfFalse()).findFirst().orElse(null);
                        tossToMonkey.addItem(worryLevel);
                    }
                }
            }
        }
        System.out.println("End");
        monkeys.forEach(System.out::println);
        System.out.println();

        List<Long> collect = monkeys.stream().map(Monkey::getInspections).sorted(Comparator.reverseOrder()).limit(2).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(collect.get(0) * collect.get(1));
    }

    private static void day10() {
        InputStream is = AdventOfCode.class.getClassLoader().getResourceAsStream("adventofcode/instructions.txt");
        Scanner sc = new Scanner(is);
        int cycle = 0;
        int x = 1;
        int sum = 0;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            int instructionCycles = 0;
            switch (parts[0]) {
                case "noop":
                    instructionCycles = 1;
                    break;
                case "addx":
                    instructionCycles = 2;
                    break;
            }
            for (int i = 0; i < instructionCycles; i++) {
                cycle++;
//                System.out.printf("Cycle: %d -- Register X: %d -- Command Processing: %s\n", cycle, x, line);
                if ((cycle - 20) % 40 == 0) {
                    sum += (cycle * x);
//                    System.out.println("Signal Strength: " + (cycle*x));
                }
                int pixelPosition = (cycle % 40);
                if (pixelPosition == x || pixelPosition == x + 1 || pixelPosition == x + 2) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
                if (cycle % 40 == 0) {
                    System.out.println();
                }
            }
            if ("addx".equals(parts[0])) {
                x += Integer.parseInt(parts[1]);
            }
        }
        System.out.println("Final Register X: " + x);
        System.out.println("Signal Strength Sum: " + sum);
    }
}
