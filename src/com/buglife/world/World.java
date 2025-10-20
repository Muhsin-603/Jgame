package src.com.buglife.world;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class World {
    public static final int TILE_SIZE = 64; // The size of each tile in pixels

    private Tile[] tileTypes; // An array to hold our different tile types (floor, wall, etc.)
    private int[][] mapData; // The 2D array that is our level design

    public int getMapWidth() {
        return mapData[0].length;
    }

    public int getMapHeight() {
        return mapData.length;
    }

    // Add this method to your World.java class
    public int getTileIdAt(int mapCol, int mapRow) {
        // Check if the coordinate is out of bounds
        if (mapRow < 0 || mapRow >= mapData.length || mapCol < 0 || mapCol >= mapData[0].length) {
            return -1; // Return an invalid ID for out-of-bounds
        }
        // Return the tile ID from our map data
        return mapData[mapRow][mapCol];
    }

    // Add this method to your World.java class
    public boolean isTileSolid(int worldX, int worldY) {
        // Convert world pixel coordinates to map grid coordinates
        int mapCol = worldX / TILE_SIZE;
        int mapRow = worldY / TILE_SIZE;

        // First, check if the coordinate is even on the map
        if (mapRow < 0 || mapRow >= mapData.length || mapCol < 0 || mapCol >= mapData[0].length) {
            return true; // Treat anything outside the map as a solid wall
        }

        // Get the ID of the tile at that grid position
        int tileID = mapData[mapRow][mapCol];

        // Return whether that tile type is solid or not
        return tileTypes[tileID].solid;
    }

    public World() {
        loadTileTypes();
        loadMapFromFile("/res/maps/level1.txt");
    }

    private void loadMapFromFile(String filePath) {
        List<List<Integer>> mapRows = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                List<Integer> row = new ArrayList<>();
                String[] numbers = line.trim().split("\\s+"); // Split by one or more spaces
                for (String num : numbers) {
                    row.add(Integer.parseInt(num));
                }
                mapRows.add(row);
            }
            reader.close();

        } catch (Exception e) {
            System.err.println("CRITICAL FAILURE: Could not load map file: " + filePath);
            e.printStackTrace();
            return;
        }

        // Convert our flexible list into a rigid 2D array for performance
        int height = mapRows.size();
        int width = mapRows.get(0).size();
        this.mapData = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.mapData[row][col] = mapRows.get(row).get(col);
            }
        }
    } 

    private void loadTileTypes() {
        tileTypes = new Tile[10]; // We have 2 types of tiles right now
        try {
            // Tile 0: The Floor
            BufferedImage floorImage = ImageIO.read(getClass().getResourceAsStream("/res/sprites/tiles/floor.png"));
            tileTypes[0] = new Tile(floorImage, false); // false = not solid

            // Tile 1: The Wall
            BufferedImage wallImage = ImageIO.read(getClass().getResourceAsStream("/res/sprites/tiles/wall.png"));
            tileTypes[1] = new Tile(wallImage, true);
            BufferedImage wallImage1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/tiles/wall_5.png"));
            tileTypes[9] = new Tile(wallImage1, true);

            BufferedImage stickyImage = ImageIO.read(getClass().getResourceAsStream("/res/sprites/tiles/sticky_floor.png"));
            tileTypes[3] = new Tile(stickyImage, false);
            tileTypes[2] = new Tile(floorImage, false);

        } catch (IOException e) {
            System.err.println("Could not load tile images!");
            e.printStackTrace();
        }
    }
    // Add this method to World.java

    public List<Point> findSpiderPath() {
        List<Point> path = new ArrayList<>();
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                if (mapData[row][col] == 2) {
                    // We add the TILE grid coordinates, not pixels
                    path.add(new Point(col, row));
                }
            }
        }
        // This simple version just adds them in reading order.
        // More complex versions could sort them to make a clean path.
        return path;
    }

    // In World.java

    /*private void loadMap() {
    // A larger, more open level with a dangerous pantry section.
    // 1 = Outer Wall, 9 = Inner Obstacle, 2 = Invisible Spider Path
    this.mapData = new int[][]{
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 9, 9, 0, 0, 9, 9, 0, 0, 9, 9, 0, 0, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 1},
        {1, 0, 9, 9, 0, 0, 9, 9, 0, 0, 9, 9, 0, 0, 2, 0, 0, 9, 0, 9, 9, 9, 9, 9, 9, 9, 0, 9, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 9, 0, 9, 0, 0, 0, 0, 0, 9, 0, 9, 0, 1},
        {1, 1, 1, 1, 1, 1, 9, 9, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 9, 0, 9, 9, 9, 0, 9, 0, 9, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 1},
        {1, 0, 9, 9, 9, 9, 9, 0, 9, 9, 9, 9, 0, 0, 2, 0, 0, 0, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 1},
        {1, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 1},
        {1, 0, 9, 9, 9, 0, 9, 0, 9, 9, 9, 9, 9, 9, 2, 9, 9, 9, 9, 9, 0, 9, 9, 9, 0, 9, 9, 9, 0, 1},
        {1, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
}*/

    // In World.java

    public void render(Graphics g, int cameraX, int cameraY, int screenWidth, int screenHeight) {
        // 1. Calculate the range of tiles that are visible on screen.
        int startCol = cameraX / TILE_SIZE;
        int endCol = (cameraX + screenWidth) / TILE_SIZE + 1; // +1 to prevent gaps at the edge
        int startRow = cameraY / TILE_SIZE;
        int endRow = (cameraY + screenHeight) / TILE_SIZE + 1;

        // 2. Make sure we don't try to draw tiles that don't exist off the map edge.
        startCol = Math.max(0, startCol);
        endCol = Math.min(getMapWidth(), endCol);
        startRow = Math.max(0, startRow);
        endRow = Math.min(getMapHeight(), endRow);

        // 3. Now, loop ONLY through the visible tiles!
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                int tileID = mapData[row][col];
                Tile tileToDraw = tileTypes[tileID];

                if (tileToDraw != null && tileToDraw.image != null) {
                    // Calculate where to draw the tile on the screen
                    int tileX = col * TILE_SIZE - cameraX;
                    int tileY = row * TILE_SIZE - cameraY;
                    g.drawImage(tileToDraw.image, tileX, tileY, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }
}