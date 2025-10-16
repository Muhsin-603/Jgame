package src.com.buglife.main;

import java.util.Random;

import src.com.buglife.entities.Food;
// Make sure to import your new Player class!
import src.com.buglife.entities.Player;
import src.com.buglife.entities.Spider;
import src.com.buglife.world.World;

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

    public GamePanel() {

        this.spider = new Spider(400, 300);
        world = new World();
        setPreferredSize(new Dimension(1920, 1080));
        setFocusable(true);
        this.player = new Player(100, 100, 32, 32);

        this.food = new Food(600, 500, 20);
        spawnFood();

        addKeyListener(new KeyInputAdapter());
    }

    public void updateGame() {

        player.update(world);
        spider.update();

        cameraX = player.getCenterX() - (1920 / 2);
        cameraY = player.getCenterY() - (1080 / 2);

        double dx = player.getCenterX() - spider.getCenterX();
        double dy = player.getCenterY() - spider.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double requiredDistance = player.getRadius() + spider.getRadius();

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
        while (true) {
            int randomCol = rand.nextInt(world.getMapWidth());
            int randomRow = rand.nextInt(world.getMapHeight());

            if (world.getTileIdAt(randomCol, randomRow) == 0) { // Check for floor tile
                int foodX = randomCol * World.TILE_SIZE + (World.TILE_SIZE / 4);
                int foodY = randomRow * World.TILE_SIZE + (World.TILE_SIZE / 4);
                food = new Food(foodX, foodY, 20);
                break; // Found a spot, exit the loop
            }
        }
    }

    // The paintComponent method now tells the player to draw itself
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        world.render(g, cameraX, cameraY);

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
    private class KeyInputAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W) {
                player.movingUp = true;
                player.setRotationAngle(0); // Face Up (default)
            }
            if (key == KeyEvent.VK_S) {
                player.movingDown = true;
                player.setRotationAngle(180); // Face Down
            }
            if (key == KeyEvent.VK_A) {
                player.movingLeft = true;
                player.setRotationAngle(-90); // Face Left-ish
            }
            if (key == KeyEvent.VK_D) {
                player.movingRight = true;
                player.setRotationAngle(90); // Face Right-ish
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