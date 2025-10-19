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

// Your class declaration will look something like this
public class GamePanel extends JPanel {

    private Spider spider;
    private Player player;
    private Food food;
    private World world;
    private int cameraX, cameraY;
    private Random rand = new Random();
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;

    public GamePanel() {

        world = new World();
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        this.player = new Player(100, 100, 32, 32);

        this.food = new Food(600, 500, 20);
        spawnFood();

        addKeyListener(new KeyInputAdapter());
        // Inside your GamePanel constructor

        // --- A NEW, LEGAL GPS ROUTE ---
        List<Point> patrolPath = new ArrayList<>();
        patrolPath.add(new Point(10, 8)); // Start in the safe central corridor
        patrolPath.add(new Point(17, 8)); // Move right into the pillar room
        patrolPath.add(new Point(17, 10)); // Move down
        patrolPath.add(new Point(22, 10)); // Move to the far right
        patrolPath.add(new Point(22, 2)); // Go all the way up
        patrolPath.add(new Point(14, 2)); // Patrol left along the top
        patrolPath.add(new Point(14, 5)); // Dip down into the middle

        this.spider = new Spider(patrolPath);
    }

    // Add this method anywhere inside your Spider.java class

    public void updateGame() {

        player.update(world);
        spider.update(world);
        world = new World();

        cameraX = player.getCenterX() - (SCREEN_WIDTH / 2);
        cameraY = player.getCenterY() - (SCREEN_HEIGHT / 2);

        double dx = player.getCenterX() - spider.getCenterX();
        double dy = player.getCenterY() - spider.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double requiredDistance = player.getRadius() + spider.getRadius();

        List<Point> spiderTrack = world.findSpiderPath();

        // 2. Create the spider and give it the track
        if (spiderTrack != null && !spiderTrack.isEmpty()) {
            this.spider = new Spider(spiderTrack);
        }

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

        if (distance < requiredDistance) {
            player.takeDamage(1);
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
        System.err.println("WARNING: Could not find a valid spot to spawn food after " + maxTries + " tries. Is your map full?");
    }

    // The paintComponent method now tells the player to draw itself
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        world.render(g, cameraX, cameraY, SCREEN_WIDTH, SCREEN_HEIGHT);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(-cameraX, -cameraY);

        player.render(g2d);
        spider.draw(g2d);
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

    }

    // An inner class for handling key inputs. This is a clean way to do it.
    // Inside GamePanel.java

    private class KeyInputAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W)
                player.movingUp = true;
            if (key == KeyEvent.VK_S)
                player.movingDown = true;
            if (key == KeyEvent.VK_A)
                player.movingLeft = true;
            if (key == KeyEvent.VK_D)
                player.movingRight = true;
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