import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class Solver {
    private final ArrayList<Board> trace = new ArrayList<>();
    private int n_moves = 0;
    private final HashSet<String> visited = new HashSet<>();

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        var last = solve(initial);
        if (last == null) {
            n_moves = -1;
            return;
        }
        while (last.prev != null) {
            trace.add(last);
            last = last.prev;
        }
        Collections.reverse(trace);
        n_moves = trace.size();
    }

    // test client (see below)
    public static void main(String[] args) {
//        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        int[][] tiles = {{1, 2, 3}, {0, 4, 8}, {7, 6, 5}};
        var board = new Board(tiles);

        var solver = new Solver(board);

        System.out.println(solver.solution());

        System.out.println(solver.isSolvable());
        System.out.println(solver.moves());
    }

    private Board solve(Board initial) {
        var priority = new MinPQ<Board>(Comparator.comparing(Board::cost));
        priority.insert(initial);

        while (!priority.isEmpty()) {
            var node = priority.delMin();

            if (node.isGoal()) {
                System.out.println(node.prev);
                return node;
            }

            visited.add(node.toString());
//            System.out.println(node);

            for (var next : node.neighbors()) {
                if (!visited.contains(next.toString())) {
                    priority.insert(next);
                }
            }
        }
        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return n_moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return n_moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return trace;
    }
}
