
/**
 * POINT CLASS
 * Stores [x, y] coordinates from grid.
 */

package logic;


public class Point {
    private int x;
    private int y;


    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int pointX() {
        return this.x;
    }

    public int pointY() {
        return this.y;
    }

    public String toString() {
        return this.x + ", " + this.y;
    }
}