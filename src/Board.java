import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int n;
    private final int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
    public Board prev = null;
    private final int[][] tiles;
    private int x0;
    private int y0;
    private int steps = 0;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
        n = tiles.length;

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (tiles[j][i] == 0) {
                    x0 = i;
                    y0 = j;
                    break;
                }
            }
        }
    }

    private Board(int[][] tiles, int x0, int y0, Board prev) {
        this.tiles = tiles;
        n = tiles.length;
        this.x0 = x0;
        this.y0 = y0;
        this.prev = prev;
        this.steps = prev.steps + 1;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
//        int[][] tiles = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        int[][] tiles = {{6, 2, 3}, {4, 5, 1}, {7, 8, 0}};
        var board = new Board(tiles);
        System.out.println(board);
        System.out.println(board.manhattan());

//        System.out.println(board.twin());

//        System.out.println(board.swap(board.x0, board.y0, 0, 0));
//        System.out.println(board.neighbors());
    }

    // string representation of this board
    public String toString() {
        var rep = new StringBuilder();
        rep.append(n).append("\n");
        for (var r : tiles) {
            for (var c : r) {
                rep.append(String.format("%2d", c)).append(" ");
            }
            rep.append("\n");
        }
        return rep.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int dist = 0;
        int i = 1;
        for (var c : tiles) {
            for (var r : c) {
                dist += (r != i && r != 0) ? 1 : 0;
                i++;
            }
        }

        return dist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (tiles[j][i] != i + j * n + 1 && tiles[j][i] != 0) {
                    var dist = Math.abs((tiles[j][i] - 1) / n - j) + Math.abs((tiles[j][i] - 1) % n - i);
                    sum += Math.abs(dist);
                }
            }
        }

        return sum;
    }

    public int cost() {
        return steps + manhattan();
    }

    // is this board the goal board?
    public boolean isGoal() {
        return Arrays.deepEquals(tiles, goal);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y instanceof Board) {
            var other = (Board) y;
            return Arrays.deepEquals(tiles, other.tiles);
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        var neighbors = new ArrayList<Board>();

        if (x0 + 1 < n) {
            neighbors.add(swap(x0, y0, x0 + 1, y0));
        }
        if (x0 - 1 >= 0) {
            neighbors.add(swap(x0, y0, x0 - 1, y0));
        }
        if (y0 + 1 < n) {
            neighbors.add(swap(x0, y0, x0, y0 + 1));
        }
        if (y0 - 1 >= 0) {
            neighbors.add(swap(x0, y0, x0, y0 - 1));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x1 = (int) (Math.random() * n);
        int y1 = (int) (Math.random() * n);
        int x2 = (int) (Math.random() * n);
        int y2 = (int) (Math.random() * n);

        var new_tiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        new_tiles[y1][x1] = tiles[y2][x2];
        new_tiles[y2][x2] = tiles[y1][x1];

        return new Board(new_tiles);
    }

    private Board swap(int x0, int y0, int x1, int y1) {
        var new_tiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);

        new_tiles[y0][x0] = tiles[y1][x1];
        new_tiles[y1][x1] = tiles[y0][x0];

        return new Board(new_tiles, x1, y1, this);
    }
}
