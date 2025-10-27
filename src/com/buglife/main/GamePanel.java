package src.com.buglife.main;

import java.util.Random;

import src.com.buglife.assets.SoundManager;
import src.com.buglife.entities.Food;
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

public class GamePanel extends JPanel {

    private List<Spider> spiders = new ArrayList<>();
    private Player player;
    private Food food;
    private World world;
    private int cameraX, cameraY;
    private Random rand = new Random();
    public static final int VIRTUAL_WIDTH = 1024;
    public static final int VIRTUAL_HEIGHT = 768;
    private MainMenu mainMenu;
    private SoundManager soundManager;
    private int pauseMenuSelection = 0;
    private String[] pauseOptions = { "Resume", "Quit to Menu" };

    private Snail snail;

    public enum GameState {
        MAIN_MENU, PLAYING, GAME_OVER, PAUSED
    }

    private GameState currentState;

    public class KeyInputAdapter extends KeyAdapter {
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
                if (key == KeyEvent.VK_ESCAPE) {
                    currentState = GameState.PAUSED;
                    // We can also stop the game music and play a pause sound here
                    soundManager.stopSound("music");
                    soundManager.stopSound("chasing");
                    // soundManager.playSound("pause_jingle"); // (If you add one later)
                }
            } else if (currentState == GameState.GAME_OVER) {
                // --- SCENE 3: THE TRAGIC ENDING ---
                if (key == KeyEvent.VK_ENTER) {
                    // soundManager.loopSound("gameOver");

                    soundManager.stopSound("music");
                    soundManager.stopSound("chasing");
                    soundManager.stopSound("gameOver");

                    soundManager.stopSound("menuMusic");
                    currentState = GameState.MAIN_MENU;
                    restartGame();
                }
            } else if (currentState == GameState.PAUSED) {
                if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    pauseMenuSelection--;
                    if (pauseMenuSelection < 0)
                        pauseMenuSelection = pauseOptions.length - 1;
                    soundManager.playSound("menu");
                }
                if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    pauseMenuSelection++;
                    if (pauseMenuSelection >= pauseOptions.length)
                        pauseMenuSelection = 0;
                    soundManager.playSound("menu");
                }
                if (key == KeyEvent.VK_ESCAPE) { // Quick-resume
                    currentState = GameState.PLAYING;
                    soundManager.loopSound("music"); // Resume game music
                }
                if (key == KeyEvent.VK_ENTER) {
                    if (pauseOptions[pauseMenuSelection].equals("Resume")) {
                        currentState = GameState.PLAYING;
                        soundManager.loopSound("music"); // Resume game music
                    } else if (pauseOptions[pauseMenuSelection].equals("Quit to Menu")) {
                        currentState = GameState.MAIN_MENU;
                        soundManager.loopSound("menuMusic"); // Start menu music
                    }
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

    public GamePanel(SoundManager sm) {
        world = new World();
        this.soundManager = sm;

        mainMenu = new MainMenu();
        currentState = GameState.MAIN_MENU;
        // spider path creation
        List<Point> patrolPath1 = new ArrayList<>();
        patrolPath1.add(new Point(23, 23)); // Start in first open floor tile
        patrolPath1.add(new Point(29, 23));
        patrolPath1.add(new Point(29, 25));
        patrolPath1.add(new Point(23, 25));
        patrolPath1.add(new Point(23, 23));

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

        setPreferredSize(new Dimension(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        setFocusable(true);
        this.player = new Player(594, 2484, 32, 32);
        snail = new Snail(534, 2464, player);

        spawnFood();

        addKeyListener(new KeyInputAdapter());
    }

    public void restartGame() {

        soundManager.stopAllSounds();
        System.out.println("Resetting the nightmare...");
        soundManager.loopSound("music");
        // In restartGame()
        if (snail != null) {
            // Re-position or re-create the snail. Simple reset:
            snail = new Snail(534, 2464, player); // Recreate it near the player
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

                }

            }
        }
    }

    public void updateGame() {

        if (currentState == GameState.PLAYING) {
            // In updateGame() -> inside the if (currentState == GameState.PLAYING) block
            if (snail != null) {
                snail.update(world);
            }
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
            cameraX = Math.max(0, Math.min(player.getCenterX() - (VIRTUAL_WIDTH / 2),
                    world.getMapWidth() * World.TILE_SIZE - VIRTUAL_WIDTH));
            cameraY = Math.max(0, Math.min(player.getCenterY() - (VIRTUAL_HEIGHT / 2),
                    world.getMapHeight() * World.TILE_SIZE - VIRTUAL_HEIGHT));

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

        // --- 1. THE SCALING CALCULATIONS ---
        // Get the actual, physical size of our fullscreen window
        int realScreenWidth = getWidth();
        int realScreenHeight = getHeight();

        // Create a new, temporary graphics object to draw on
        Graphics2D g2d = (Graphics2D) g.create();

        // Calculate how much to scale our virtual game to fit the real screen
        double scaleX = (double) realScreenWidth / VIRTUAL_WIDTH;
        double scaleY = (double) realScreenHeight / VIRTUAL_HEIGHT;
        // To maintain the aspect ratio (no stretching), we use the smaller of the two
        // scales
        double scale = Math.min(scaleX, scaleY);

        // Calculate the size of our game after scaling
        int scaledWidth = (int) (VIRTUAL_WIDTH * scale);
        int scaledHeight = (int) (VIRTUAL_HEIGHT * scale);

        // Calculate the top-left corner to center our game on the screen (for
        // letterboxing)
        int xOffset = (realScreenWidth - scaledWidth) / 2;
        int yOffset = (realScreenHeight - scaledHeight) / 2;

        // --- 2. APPLY THE TRANSFORMATION ---
        // Tell the graphics object to move to our centered position
        g2d.translate(xOffset, yOffset);
        // Tell it to scale everything it draws from now on
        g2d.scale(scale, scale);

        // --- 3. DRAW THE ENTIRE GAME (onto the scaled g2d) ---
        // Now, we draw our whole game as if it were on a 1024x768 screen.
        // The g2d object will handle all the scaling and centering automatically!

        if (currentState == GameState.PLAYING) {
            // --- 1. Draw the World (pass VIRTUAL size for culling) ---
            world.render(g2d, cameraX, cameraY, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

            // --- 2. Draw Entities (affected by camera) ---
            Graphics2D entityG2d = (Graphics2D) g2d.create();
            try {
                entityG2d.translate(-cameraX, -cameraY);
                if (player != null)
                    player.render(entityG2d, world);
                for (Spider spider : spiders)
                    if (spider != null)
                        spider.draw(entityG2d);
                if (food != null)
                    food.draw(entityG2d);
                if (snail != null)
                    snail.draw(entityG2d);
            } finally {
                entityG2d.dispose();
            }

            // --- 3. DRAW HUD (fixed on screen, uses g2d) ---
            // Hunger Bar
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(10, 10, 200, 20);
            g2d.setColor(Color.ORANGE);
            if (player != null)
                g2d.fillRect(10, 10, player.getHunger() * 2, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(10, 10, 200, 20);

            // Webbed Text
            if (player != null && player.isWebbed()) {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Consolas", Font.BOLD, 40));
                String struggleMsg = "PRESS [SPACE] TO STRUGGLE!";
                int msgWidth = g2d.getFontMetrics().stringWidth(struggleMsg);
                g2d.drawString(struggleMsg, (VIRTUAL_WIDTH - msgWidth) / 2, VIRTUAL_HEIGHT - 100);
            }

        } else if (currentState == GameState.GAME_OVER) {
            // --- Game Over (uses g2d) ---
            if (player != null)
                player.reset();
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Consolas", Font.BOLD, 80));
            String msg = "GAME OVER";
            int msgWidth = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (VIRTUAL_WIDTH - msgWidth) / 2, VIRTUAL_HEIGHT / 2);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Consolas", Font.PLAIN, 20));
            String restartMsg = "Press Enter to Restart";
            int restartWidth = g2d.getFontMetrics().stringWidth(restartMsg);
            g2d.drawString(restartMsg, (VIRTUAL_WIDTH - restartWidth) / 2, VIRTUAL_HEIGHT / 2 + 50);
        }

        else if (currentState == GameState.PAUSED) {
            // --- 1. First, draw the game world in the background ---
            // This makes it look like we're pausing *over* the game
            world.render(g, cameraX, cameraY, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

            try {
                g2d.translate(-cameraX, -cameraY);
                if (player != null)
                    player.render(g2d, world);
                for (Spider spider : spiders)
                    if (spider != null)
                        spider.draw(g2d);
                if (food != null)
                    food.draw(g2d);
                if (snail != null)
                    snail.draw(g2d);
            } finally {
                g2d.dispose();
            }
            // Also draw the HUD
            // ... (copy/paste your HUD drawing code here: hunger bar, etc.) ...

            // --- 2. Now, draw the pause menu overlay on top ---
            g.setColor(new Color(0, 0, 0, 150)); // Dark overlay
            g.fillRect(0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 80));
            String msg = "PAUSED";
            int msgWidth = g.getFontMetrics().stringWidth(msg);
            g.drawString(msg, (VIRTUAL_WIDTH - msgWidth) / 2, VIRTUAL_HEIGHT / 3);

            // Draw options
            g.setFont(new Font("Consolas", Font.PLAIN, 40));
            for (int i = 0; i < pauseOptions.length; i++) {
                if (i == pauseMenuSelection) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.WHITE);
                }
                int optionWidth = g.getFontMetrics().stringWidth(pauseOptions[i]);
                g.drawString(pauseOptions[i], (VIRTUAL_WIDTH - optionWidth) / 2, VIRTUAL_HEIGHT / 2 + i * 60);
            }
        }

        else if (currentState == GameState.MAIN_MENU) {
            
            mainMenu.draw(g2d);
        }

        
        g2d.dispose();

        
    }

}
