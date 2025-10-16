package src.com.buglife.world;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {
    public static final int TILE_SIZE = 64; // The size of each tile in pixels

    private Tile[] tileTypes; // An array to hold our different tile types (floor, wall, etc.)
    private int[][] mapData; // The 2D array that is our level design

    public int getMapWidth(){
        return mapData[0].length;
    }
    public int getMapHeight(){
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
        loadMap();
    }

    private void loadTileTypes() {
        tileTypes = new Tile[2]; // We have 2 types of tiles right now
        try {
            // Tile 0: The Floor
            BufferedImage floorImage = ImageIO.read(getClass().getResourceAsStream("/res/sprites/tiles/floor.png"));
            tileTypes[0] = new Tile(floorImage, false); // false = not solid

            // Tile 1: The Wall
            BufferedImage wallImage = ImageIO.read(getClass().getResourceAsStream("/res/sprites/tiles/wall.png"));
            tileTypes[1] = new Tile(wallImage, true); // true = solid

        } catch (IOException e) {
            System.err.println("Could not load tile images!");
            e.printStackTrace();
        }
    }

    private void loadMap() {
        // This is our hard-coded level! 1=wall, 0=floor.
        // Feel free to go crazy and design a bigger room.
        this.mapData = new int[][] {
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
        };
    }

    public void render(Graphics g, int cameraX, int cameraY) {
        for (int row = 0; row < mapData.length; row++) {
            for (int col = 0; col < mapData[row].length; col++) {
                int tileID = mapData[row][col];
                Tile tileToDraw = tileTypes[tileID];

                // Calculate where to draw the tile on the screen
                int tileX = col * TILE_SIZE - cameraX;
                int tileY = row * TILE_SIZE - cameraY;

                g.drawImage(tileToDraw.image, tileX, tileY, TILE_SIZE, TILE_SIZE, null);
            }
        }
    }
}