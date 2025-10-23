package src.com.buglife.main;

import java.util.Random;

import src.com.buglife.assets.SoundManager;
import src.com.buglife.entities.Food;
// Make sure to import your new Player class!
import src.com.buglife.entities.Player;
import src.com.buglife.entities.Snail;
import src.com.buglife.entities.Spider;
import src.com.buglife.ui.MainMenu;
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
    private MainMenu mainMenu;
    private SoundManager soundManager;
    
    private Snail snail;

    public enum GameState {
        MAIN_MENU, PLAYING, GAME_OVER
    }

    private GameState currentState;

    public GamePanel(SoundManager sm) {
        world = new World();
        this.soundManager = sm;
        player = new Player(200, 200, 32, 32); // Create player first
        snail = new Snail(205, 205, player);

        mainMenu = new MainMenu();
        currentState = GameState.MAIN_MENU;
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
        // This is the fix that brings your game to life!
        currentState = GameState.MAIN_MENU;

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        this.player = new Player(594, 2484, 32, 32);

        this.food = new Food(600, 500, 20);
        spawnFood();

        addKeyListener(new KeyInputAdapter());
    }

    public void restartGame() {
        soundManager.loopSound("music");
        System.out.println("Resetting the nightmare...");
        // In restartGame()
if (snail != null) {
     // Re-position or re-create the snail. Simple reset:
     snail = new Snail(150, 150, player); // Recreate it near the player
}

        // 1. Reset the player.
        player.reset();

        // 2. Reset every spider in our existing army.
        for (Spider spider : spiders) {
            if (spider != null) {
                spider.reset();
            }
        }

        // 3. Respawn the food.
        spawnFood();

        // 4. Set the scene back to the beginning.
        currentState = GameState.PLAYING;
    }

    private void handleSpiderAlerts() {
        if (player.isCrying()) {
            for (Spider spider : spiders) {
                if (spider != null && spider.getCurrentState() == Spider.SpiderState.PATROLLING) {
                    spider.setReturnPoint(new Point(spider.getCenterX(), spider.getCenterY()));
                    spider.startChasing(player, soundManager);
                    // soundManager.playSound("spiderAlert");
                }

            }
        }
    }

    public void updateGame() {

        if (currentState == GameState.PLAYING) {
            // In updateGame() -> inside the if (currentState == GameState.PLAYING) block
            if (snail != null) {snail.update(world);}
            player.update(world, soundManager);
            handleSpiderAlerts();
            for (Spider currentSpider : spiders) {
                if (currentSpider != null) {

                    currentSpider.update(player, world, soundManager);

                    // Check collision with each spider
                    double dx = player.getCenterX() - currentSpider.getCenterX();
                    double dy = player.getCenterY() - currentSpider.getCenterY();
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double requiredDistance = player.getRadius() + currentSpider.getRadius();

                    if (distance < requiredDistance) {
                        if (player.getHunger() <= 0) {
                            System.out.println("GAME OVER: Player caught with zero hunger!");
                            soundManager.stopSound("music"); // Stop background music
                            soundManager.stopSound("chasing");
                            soundManager.playSound("gameOver"); // Play game over sound
                            currentState = GameState.GAME_OVER; // Set game state to GAME_OVER
                            return; // Exit updateGame immediately
                        }
                        if (currentSpider.isChasing()) { // We'll add this method next
                            if (player.isCrying()) { // check baby is crying if crying then game over
                                soundManager.stopSound("music");
                                soundManager.stopSound("chasing");
                                soundManager.playSound("gameover");
                                currentState = GameState.GAME_OVER;
                                return;
                            } else { // normally get webbed
                                player.getWebbed();
                                soundManager.playSound("webbed");
                            }
                        } else {
                            // If it just bumps into you while patrolling, it's just a little damage.
                            player.decreaseHunger(1);
                        }
                    }

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
                    player.eat(25); // Heal for a nice chunk of health
                    soundManager.playSound("eat");
                    spawnFood();
                }
            }
            if (player.getHunger() <= 0 && !player.isCrying()) {
                soundManager.stopSound("music"); // Stop the background music
                soundManager.playSound("chasing");
                soundManager.playSound("gameOver"); // Play the death sound
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
            
        // --- 1. Draw the World ---
        world.render(g, cameraX, cameraY, SCREEN_WIDTH, SCREEN_HEIGHT);

        // --- 2. Draw Entities (affected by camera) ---
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(-cameraX, -cameraY);
        if (player != null) { // Added null check for safety
             player.render(g2d, world, soundManager);
        }
        for (Spider spider : spiders) {
             if (spider != null) spider.draw(g2d);
        }
        if (food != null) {
             food.draw(g2d);
        }
        if (snail != null) {
            snail.draw(g2d);
        }
        g2d.dispose();

        // --- 3. DRAW HUD (fixed on screen, uses original 'g') ---

        // Hunger Bar
        g.setColor(Color.DARK_GRAY);
        g.fillRect(10, 10, 200, 20);
        g.setColor(Color.ORANGE);
        if (player != null) { // Added null check
            g.fillRect(10, 10, player.getHunger() * 2, 20);
        }
        g.setColor(Color.BLACK);
        g.drawRect(10, 10, 200, 20);

        // --- Coordinates Text (Moved Here!) ---
        /*g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.PLAIN, 16));
        if (player != null) { // Added null check
             String coordText = "Coords: [" + player.getX() + ", " + player.getY() + "]";
             g.drawString(coordText, 10, 50); // Position below hunger bar
        }*/
            if (player.isWebbed()) {
                // Upgrade our drawing tool
                Graphics2D hintG2d = (Graphics2D) g;

                // Set the font and color for our panic button sign
                hintG2d.setColor(Color.WHITE);
                hintG2d.setFont(new Font("Consolas", Font.BOLD, 40));

                // The message of hope (or despair)
                String struggleMsg = "PRESS [SPACE] TO STRUGGLE!";

                // Center it on the screen
                int msgWidth = hintG2d.getFontMetrics().stringWidth(struggleMsg);
                hintG2d.drawString(struggleMsg, (SCREEN_WIDTH - msgWidth) / 2, SCREEN_HEIGHT - 100);
            }
        } else if (currentState == GameState.GAME_OVER) {
            // --- If the game is over, draw the final scene! ---
            // 1. A dark, semi-transparent overlay to set the mood
            player.reset();
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

        } else if (currentState == GameState.MAIN_MENU) {
            // --- DRAW THE MAIN MENU ---
            mainMenu.draw(g);
        }
    }

    // An inner class for handling key inputs. This is a clean way to do it.
    // Inside GamePanel.java

    // Inside GamePanel.java

    private class KeyInputAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // The actor checks which scene they're in...
            if (currentState == GameState.MAIN_MENU) {
                // --- SCENE 1: THE MAIN MENU ---

                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    mainMenu.moveUp();
                    soundManager.playSound("menu");
                }
                if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    mainMenu.moveDown();
                    soundManager.playSound("menu");
                }
                if (key == KeyEvent.VK_ENTER) {
                    String selectedOption = mainMenu.options[mainMenu.currentSelection];
                    if (selectedOption.equals("New Game")) {
                        soundManager.stopSound("menuMusic");
                        restartGame();
                    }
                    if (selectedOption.equals("Resume")) {
                        // We need to check if a game is actually in progress!
                        // For now, let's just switch to playing.
                        soundManager.stopSound("menuMusic");
                        currentState = GameState.PLAYING;
                    }
                    if (selectedOption.equals("Quit")) {
                        System.exit(0);
                    }
                }
            } else if (currentState == GameState.PLAYING) {
                // --- SCENE 2: THE ACTION ---
                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                    player.movingUp = true;
                    // We need to make sure the player object isn't null!
                    // if (player != null) player.setRotationAngle(0);
                }
                if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                    player.movingDown = true;
                    // if (player != null) player.setRotationAngle(180);
                }
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                    player.movingLeft = true;
                }
                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                    player.movingRight = true;
                }
                if (key == KeyEvent.VK_SPACE) {
                    player.struggle();
                    soundManager.playSound("struggle");
                }
            } else if (currentState == GameState.GAME_OVER) {
                // --- SCENE 3: THE TRAGIC ENDING ---
                if (key == KeyEvent.VK_ENTER) {
                    soundManager.loopSound("gameOver");
                    soundManager.stopSound("menuMusic");
                    soundManager.stopSound("music");
                    currentState = GameState.MAIN_MENU;
                    restartGame();
                }
            }
        }

        // You should also update your keyReleased to only work when playing!
        @Override
        public void keyReleased(KeyEvent e) {
            if (currentState == GameState.PLAYING) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
                    player.movingUp = false;
                if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)
                    player.movingDown = false;
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
                    player.movingLeft = false;
                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT)
                    player.movingRight = false;
            }
        }
    }

}