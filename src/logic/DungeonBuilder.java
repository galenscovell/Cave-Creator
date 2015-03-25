
/**
 * DUNGEONBUILDER CLASS
 * Constructs a new world grid and tileset with dungeon features. 
 * (rooms connected with single-tile corridors)
 */

package logic;

import automata.Tile;

import java.util.ArrayList;
import java.util.Random;


public class DungeonBuilder implements Builder {
    private int rows;
    private int columns;
    private int[][] grid;
    private ArrayList<Tile> tiles;

    public DungeonBuilder(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        
        this.grid = new int[columns][rows];
        this.tiles = new ArrayList<Tile>();
    }

    public void build() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                tiles.add(new Tile(x, y));
                grid[x][y] = 0;
            }
        }
        int midColumn = (columns - 1) / 2;
        int midRow = (rows - 1) / 2;
        createRoom(midColumn, midRow);
    }

    public void createRoom(int centerX, int centerY) {
        Random random = new Random();
        int roomSize = random.nextInt(3) + 1;
        ArrayList<Point> perimeterPoints = new ArrayList<Point>();
        int sumX, sumY;

        grid[centerX][centerY] = 1;
        for (int x = -roomSize; x <= roomSize; x++) {
            for (int y = -roomSize; y <= roomSize; y++) {
                sumX = centerX + x;
                sumY = centerY + y;

                if (isOutOfBounds(sumX, sumY)) {
                    continue;
                }
                if (x == -roomSize || x == roomSize || y == -roomSize || y == roomSize) {
                    if ((x == -roomSize && y == -roomSize) || (x == -roomSize && y == roomSize) || (x == roomSize && y == -roomSize) || (x == roomSize && y == roomSize)) {
                        grid[sumX][sumY] = 1;
                        continue;
                    } 
                    perimeterPoints.add(new Point(sumX, sumY));
                }
                grid[sumX][sumY] = 1;
            }
        }
        
        int chosenPoint = random.nextInt(perimeterPoints.size() - 1);
        Point corridorPoint = perimeterPoints.get(chosenPoint);
        createCorridor(corridorPoint.pointX(), corridorPoint.pointY());
    }

    public void createCorridor(int startX, int startY) {
        int sumX, sumY;
        String direction = "";

        for (int x = -1; x <= 1; x += 2) {
            sumX = startX + x;
            sumY = startY;

            if (isOutOfBounds(sumX, sumY)) {
                continue;
            }
            if (grid[sumX][sumY] == 0) {
                if (x == -1) {
                    direction = "left";
                } else if (x == 1) {
                    direction = "right";
                }
            }
        }

        for (int y = -1; y <= 1; y += 2) {
            sumX = startX;
            sumY = startY + y;

            if (isOutOfBounds(sumX, sumY)) {
                continue;
            }
            if (grid[sumX][sumY] == 0) {
                if (y == -1) {
                    direction = "up";
                } else if (y == 1) {
                    direction = "down";
                }
            }
        }
        extendCorridor(direction, startX, startY);
    }

    public void extendCorridor(String direction, int startX, int startY) {
        Random random = new Random();
        int corridorSize = random.nextInt(6) + 3;
        int currentX = startX;
        int currentY = startY;
        
        for (int i = 0; i < corridorSize; i++) {
            if (direction.equals("up")) {
                currentY -= 1;
            } else if (direction.equals("right")) {
                currentX += 1;
            } else if (direction.equals("down")) {
                currentY += 1;
            } else if (direction.equals("left")) {
                currentX -= 1;
            }

            if (isOutOfBounds(currentX, currentY)) {
                if (direction.equals("up")) {
                    currentY += 1;
                } else if (direction.equals("right")) {
                    currentX -= 1;
                } else if (direction.equals("down")) {
                    currentY -= 1;
                } else if (direction.equals("left")) {
                    currentX += 1;
                }
                grid[currentX][currentY] = 3;
                return;
            }
            grid[currentX][currentY] = 1;
        }
        grid[currentX][currentY] = 3;
    }

    public boolean isOutOfBounds(int x, int y) {
        if (x < 0 || y < 0){
            return true;
        } else if (x >= columns || y >= rows){
            return true;
        } else {
            return false;
        }
    }

    public void smooth(Tile tile) {
        if (tile.isCorridor(grid)) {
            createRoom(tile.getX(), tile.getY());
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}