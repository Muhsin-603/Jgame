package src.com.buglife.main;

import java.util.Random;

import src.com.buglife.entities.Food;
// Make sure to import your new Player class!
import src.com.buglife.entities.Player;
import src.com.buglife.entities.Spider;
import src.com.buglife.world.World;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;

// Your class declaration will look something like this
public class GamePanel extends JPanel {

    private List<Spider> spiders = new ArrayList<>();
    private Player player;
    private Food food;
    private World world;
    private int cameraX, cameraY;
    private Random rand = new Random();
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;

    public enum GameState {
        PLAYING, GAME_OVER
    }

    private GameState currentState;

    public GamePanel() {
        world = new World();
        // spider path creation
        List<Point> patrolPath1 = new ArrayList<>();
        patrolPath1.add(new Point(7, 7)); // Start in first open floor tile
        patrolPath1.add(new Point(18, 7));
        patrolPath1.add(new Point(7, 7));

        List<Point> patrolPath2 = new ArrayList<>();
        patrolPath2.add(new Point(14, 3)); // Start in first open floor tile
        patrolPath2.add(new Point(14, 6));
        patrolPath2.add(new Point(14, 3));

        List<Point> patrolPath3 = new ArrayList<>();
        patrolPath3.add(new Point(18, 5)); // Start in first open floor tile
        patrolPath3.add(new Point(18, 3));
        patrolPath3.add(new Point(26, 3));
        patrolPath3.add(new Point(26, 8));
        patrolPath3.add(new Point(26, 3));
        patrolPath3.add(new Point(18, 3));
        patrolPath3.add(new Point(18, 5));
        // new spider creation
        spiders.add(new Spider(patrolPath1));
        spiders.add(new Spider(patrolPath2));
        spiders.add(new Spider(patrolPath3));
        currentState = GameState.PLAYING;

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        this.player = new Player(100, 100, 32, 32);

        this.food = new Food(600, 500, 20);
        spawnFood();

        addKeyListener(new KeyInputAdapter());
    }

    // Add this new method anywhere in GamePanel.java
    // In GamePanel.java

    public void restartGame() {
        System.out.println("Restarting the nightmare...");

        // --- 1. Re-create the player with the CORRECT size ---
        // Make sure these numbers match the ones in your GamePanel constructor!
        this.player = new Player(200, 200, 32, 32);

        // --- 2. Vaporize the OLD spider army ---
        //spiders.clear();

        // --- 3. Rebuild the NEW spider army from the blueprints ---
        // (This is the logic that was missing!)
        /*List<Point> path1 = new ArrayList<>();
        path1.add(new Point(7, 7));
        path1.add(new Point(18, 7));
        spiders.add(new Spider(path1));

        List<Point> path2 = new ArrayList<>();
        path2.add(new Point(14, 3));
        path2.add(new Point(14, 6));
        spiders.add(new Spider(path2));

        List<Point> patrolPath3 = new ArrayList<>();
        patrolPath3.add(new Point(18, 5)); // Start in first open floor tile
        patrolPath3.add(new Point(18, 3));
        patrolPath3.add(new Point(26, 3));
        patrolPath3.add(new Point(26, 8));
        patrolPath3.add(new Point(26, 3));
        patrolPath3.add(new Point(18, 3));
        patrolPath3.add(new Point(18, 5));*/

        // --- 4. Respawn the food ---
        spawnFood();

        // --- 5. Set the scene back to the beginning ---
        currentState = GameState.PLAYING;
    }

    // Add this method anywhere inside your Spider.java class

    public void updateGame() {

        if (currentState == GameState.PLAYING) {
            player.update(world);
            for (Spider spider : spiders) {
                spider.update(world);

                // Check collision with each spider
                double dx = player.getCenterX() - spider.getCenterX();
                double dy = player.getCenterY() - spider.getCenterY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                double requiredDistance = player.getRadius() + spider.getRadius();

                if (distance < requiredDistance) {
                    player.takeDamage(1);
                }

            }
            // Update camera with bounds checking
            cameraX = Math.max(0, Math.min(player.getCenterX() - (SCREEN_WIDTH / 2),
                    world.getMapWidth() * World.TILE_SIZE - SCREEN_WIDTH));
            cameraY = Math.max(0, Math.min(player.getCenterY() - (SCREEN_HEIGHT / 2),
                    world.getMapHeight() * World.TILE_SIZE - SCREEN_HEIGHT));

            // 2. Create the spider and give it the track

            if (food != null) {
                double dxFood = player.getCenterX() - food.getCenterX();
                double dyFood = player.getCenterY() - food.getCenterY();
                double distanceFood = Math.sqrt(dxFood * dxFood + dyFood * dyFood);
                double requiredDistanceFood = player.getRadius() + food.getRadius();

                if (distanceFood < requiredDistanceFood) {
                    player.heal(25); // Heal for a nice chunk of health
                    spawnFood();
                }
            }
            if (player.getHealth() <= 0) {
                currentState = GameState.GAME_OVER; // End the scene!
            }
        }

    }

    public void spawnFood() {
        int maxTries = 100; // Give it 100 attempts to find a spot.
        int tries = 0;

        while (tries < maxTries) {
            int randomCol = rand.nextInt(world.getMapWidth());
            int randomRow = rand.nextInt(world.getMapHeight());

            // Check if the tile at that random spot is a floor tile (ID 0)
            if (world.getTileIdAt(randomCol, randomRow) == 0) {
                // Success! Create the food and exit.
                int foodX = randomCol * World.TILE_SIZE + (World.TILE_SIZE / 4);
                int foodY = randomRow * World.TILE_SIZE + (World.TILE_SIZE / 4);
                food = new Food(foodX, foodY, 20);
                System.out.println("Food spawned successfully at [" + randomCol + ", " + randomRow + "]");
                return; // We're done, exit the method.
            }

            tries++; // Increment our attempt counter.
        }

        // If the loop finishes without finding a spot, we print a warning.
        System.err.println(
                "WARNING: Could not find a valid spot to spawn food after " + maxTries + " tries. Is your map full?");
    }

    // The paintComponent method now tells the player to draw itself
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentState == GameState.PLAYING) {

            world.render(g, cameraX, cameraY, SCREEN_WIDTH, SCREEN_HEIGHT);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(-cameraX, -cameraY);

            player.render(g2d);
            for (Spider spider : spiders) {
                spider.draw(g2d);
            }
            if (food != null) {
                food.draw(g2d);
            }
            g2d.dispose();

            // --- DRAW HUD HERE ---
            // Background of the health bar (the empty part)
            g.setColor(Color.DARK_GRAY);
            g.fillRect(10, 10, 200, 20); // x, y, width, height

            // The current health (the red part)
            g.setColor(Color.RED);
            // The width of this rectangle depends on the player's health!
            g.fillRect(10, 10, player.getHealth() * 2, 20); // health * 2 because 100 * 2 = 200 width

            // A border to make it look clean
            g.setColor(Color.WHITE);
            g.drawRect(10, 10, 200, 20);
        } else if (currentState == GameState.GAME_OVER) {
            // --- If the game is over, draw the final scene! ---
            // 1. A dark, semi-transparent overlay to set the mood
            g.setColor(new Color(0, 0, 0, 150)); // Black, with transparency
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            // 2. The tragic final words
            g.setColor(Color.RED);
            g.setFont(new Font("Consolas", Font.BOLD, 80));
            String msg = "GAME OVER";
            int msgWidth = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, (SCREEN_WIDTH - msgWidth) / 2, SCREEN_HEIGHT / 2);

            // 3. A glimmer of hope...
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            String restartMsg = "Press Enter to Restart";
            int restartWidth = g.getFontMetrics().stringWidth(restartMsg);
            g.drawString(restartMsg, (SCREEN_WIDTH - restartWidth) / 2, SCREEN_HEIGHT / 2 + 50);

        }
    }

    // An inner class for handling key inputs. This is a clean way to do it.
    // Inside GamePanel.java

    private class KeyInputAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (currentState == GameState.PLAYING) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_W)
                    player.movingUp = true;
                if (key == KeyEvent.VK_S)
                    player.movingDown = true;
                if (key == KeyEvent.VK_A)
                    player.movingLeft = true;
                if (key == KeyEvent.VK_D)
                    player.movingRight = true;
            } else if (currentState == GameState.GAME_OVER) {
                // --- Game Over controls ---
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W)
                player.movingUp = false;
            if (key == KeyEvent.VK_S)
                player.movingDown = false;
            if (key == KeyEvent.VK_A)
                player.movingLeft = false;
            if (key == KeyEvent.VK_D)
                player.movingRight = false;
        }
    }
}