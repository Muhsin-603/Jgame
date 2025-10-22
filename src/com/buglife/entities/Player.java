package src.com.buglife.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.util.function.IntBinaryOperator;

import src.com.buglife.assets.SoundManager;
import src.com.buglife.world.World;

import java.awt.geom.AffineTransform;

public class Player {
    // Player attributes
    private int x, y;
    private int width, height;
    private double currentSpeed; // How fast we are moving RIGHT NOW
    private final double NORMAL_SPEED = 2.0; // The default speed
    private final double SLOW_SPEED = 0.5; // The speed when stuck!
    private int hunger = 100;
    private final int MAX_HUNGER = 100;
    private int collisionRadius;
    private int hungerDrainTimer = 0;
    private boolean isCrying = false;
    private int cryDeathTimer = 0; // Timer for death by hunger after crying starts
    private final int CRY_DEATH_DURATION = 20 * 60;
    private boolean isLowHungerWarningPlayed = false;
    private final int LOW_HUNGER_THRESHOLD = 0;

    // This tracks hunger and crying mechanics

    private BufferedImage sprite_walk1, sprite_walk2; // Just our two images
    private int animationTick = 0;
    private int animationSpeed = 3; // Change sprite every 15 frames. Higher is slower.
    private int spriteNum = 1; // Which sprite to show: 1 or 2
    private int currentFrame = 0;
    private double rotationAngle = 0;
    private PlayerState currentState = PlayerState.IDLE_DOWN;
    private List<BufferedImage> idleDownFrames;
    private List<BufferedImage> walkDownFrames;
    private List<BufferedImage> walkUpFrames;
    private List<BufferedImage> walkLeftFrames; // New list
    private List<BufferedImage> walkRightFrames;
    private int webbedTimer = 0;
    private int webStrength = 0;
    private int webCounter = 4;

    public boolean isWebbed() {

        return this.currentState == PlayerState.WEBBED;
    }

    public enum PlayerState {
        IDLE_DOWN, // Standing, facing down
        WALKING_DOWN, WALKING_UP, WALKING_LEFT, // New state
        WALKING_RIGHT, WEBBED // New state
        // Maybe add IDLE_UP, IDLE_LEFT, IDLE_RIGHT later? For now, idle is just down.
    }

    public void eat(int amount) {

        this.hunger += amount;
        if (this.hunger > 100) {
            this.hunger = MAX_HUNGER;
        }
    }
    // Add this method anywhere inside your Player class

    // In Player.java

    public void reset() {
        // Reset position
        this.x = 594; // Or whatever your default start position is
        this.y = 2484;

        // Reset hunger and crying state
        this.hunger = 100;
        this.isCrying = false;

        // Reset state to default idle
        this.currentState = PlayerState.IDLE_DOWN;
        this.currentFrame = 0; // Reset animation frame too
        this.animationTick = 0;

        // --- THE FIX ---
        // Reset web status completely
        this.webbedTimer = 0;
        this.webStrength = 0;
        this.webCounter = 0;

        // Make sure movement flags are off
        this.movingUp = false;
        this.movingDown = false;
        this.movingLeft = false;
        this.movingRight = false;
        // this.isFacingLeft = false; // Reset facing direction
    }

    // Add this method to Player.java
    public void getWebbed() {
        if (currentState != PlayerState.WEBBED) {
            // System.out.println("PLAYER: I'M TRAPPED!");
            currentState = PlayerState.WEBBED;
            webCounter++;

            webbedTimer = 300; // You have 5 seconds to live...
            webStrength = webCounter; // ...and 4 taps to escape. Good luck.
            this.currentFrame = 0;
        }
    }

    public void render(Graphics g, World world, SoundManager soundManager) {
        List<BufferedImage> currentAnimation = getActiveAnimation();
        if (currentAnimation == null || currentAnimation.isEmpty() || currentFrame >= currentAnimation.size()) {
            g.setColor(Color.MAGENTA); // Failsafe
            g.fillRect(this.x, this.y, this.width, this.height);
            return;
        }

        BufferedImage imageToDraw = currentAnimation.get(currentFrame);

        Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Stealth transparency check (using world object)
            int playerTileCol = getCenterX() / World.TILE_SIZE;
            int playerTileRow = getCenterY() / World.TILE_SIZE;
            if (world.getTileIdAt(playerTileCol, playerTileRow) == 5) { // Shadow tile ID
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }

            // Draw the image normally. No flipping needed!
            g2d.drawImage(imageToDraw, this.x, this.y, this.width, this.height, null);

        } finally {
            g2d.dispose();
        }
    }

    // Add this method to your Player.java class

    // The new, super-safe drawHitbox method in Player.java
    public void drawHitbox(Graphics g) {
        // 1. Create a disposable copy, JUST for the hitbox.
        Graphics2D g2d = (Graphics2D) g.create();

        // Get the center of the bug
        int centerX = getCenterX();
        int centerY = getCenterY();

        // Set the color to a semi-transparent red
        g2d.setColor(new Color(255, 0, 0, 100));

        // Draw the oval on our clean, disposable copy
        g2d.fillOval(centerX - collisionRadius, centerY - collisionRadius, collisionRadius * 2, collisionRadius * 2);

        // 2. Throw the copy away immediately.
        g2d.dispose();
    }

    // In Player.java

    public int getCenterX() {
        return this.x + (this.width / 2);
    }

    public int getCenterY() {
        return this.y + (this.height / 2);
    }

    public double getRadius() {
        return this.collisionRadius;
    }

    // In Player.java

    // Movement state flags
    public boolean movingUp, movingDown, movingLeft, movingRight;

    /**
     * Constructor for our heroic bug.
     * 
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @param size   The width and height of the player.
     */
    public Player(int startX, int startY, int drawSize, int collisionSize) {
        this.x = startX;
        this.y = startY;
        this.width = drawSize;
        this.height = drawSize;
        this.collisionRadius = collisionSize / 2;
        loadAnimations();
    }
    /// res/sprites/player/pla.png"

    private void loadAnimations() {
        idleDownFrames = new ArrayList<>();
        walkDownFrames = new ArrayList<>();
        walkUpFrames = new ArrayList<>();
        walkLeftFrames = new ArrayList<>();
        walkRightFrames = new ArrayList<>();

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/sprites/player/pla.png"));

            // --- Sprite Dimensions ---
            final int SPRITE_WIDTH = 13;
            final int SPRITE_HEIGHT = 28;

            // --- !!! YOUR EXACT MEASUREMENTS !!! ---
            final int PADDING_LEFT = 25; // X offset of the very first sprite
            final int PADDING_TOP = 15; // Y offset of the very first sprite
            final int HORIZONTAL_SPACING = 64; // Calculated pixels between sprite starts horizontally
            final int VERTICAL_SPACING = 64; // Calculated pixels between sprite starts vertically

            // Helper function to calculate exact coordinates using your measurements
            // Calculates X based on padding, column index, and spacing
            // Calculates Y based on padding, row index, and spacing
            IntBinaryOperator getX = (col, row) -> PADDING_LEFT + col * HORIZONTAL_SPACING;
            IntBinaryOperator getY = (col, row) -> PADDING_TOP + row * VERTICAL_SPACING;

            // --- THE DIGITAL SCISSORS - Perfectly Calibrated ---

            // Idle Down (Stand - Row index 0, Col index 0)
            idleDownFrames.add(
                    spriteSheet.getSubimage(getX.applyAsInt(0, 0), getY.applyAsInt(0, 0), SPRITE_WIDTH, SPRITE_HEIGHT));

            // Walk Down (Row index 4, Cols 0-3)
            for (int i = 0; i < 4; i++) {
                walkDownFrames.add(spriteSheet.getSubimage(getX.applyAsInt(i, 4), getY.applyAsInt(i, 4), SPRITE_WIDTH,
                        SPRITE_HEIGHT));
            }

            // Walk Up (Row index 5, Cols 0-3)
            for (int i = 0; i < 4; i++) {
                walkUpFrames.add(spriteSheet.getSubimage(getX.applyAsInt(i, 5), getY.applyAsInt(i, 5), SPRITE_WIDTH,
                        SPRITE_HEIGHT));
            }

            // Walk Right (Row index 6, Cols 0-3)
            for (int i = 0; i < 4; i++) {
                walkRightFrames.add(spriteSheet.getSubimage(getX.applyAsInt(i, 6), getY.applyAsInt(i, 6), SPRITE_WIDTH,
                        SPRITE_HEIGHT));
            }

            // Walk Left (Row index 7, Cols 0-3)
            for (int i = 0; i < 4; i++) {
                walkLeftFrames.add(spriteSheet.getSubimage(getX.applyAsInt(i, 7), getY.applyAsInt(i, 7), SPRITE_WIDTH,
                        SPRITE_HEIGHT));
            }

            System.out.println("Loaded " + walkDownFrames.size() + " walk down frames."); // Keep confirmation prints

        } catch (Exception e) {
            System.err.println("CRASH! Could not slice player sheet. Double-check measurements!");
            e.printStackTrace();
        }
    }

    // Add this method to Player.java
    public void struggle() {
        if (currentState == PlayerState.WEBBED) {
            webStrength--; // Chip away at the web's strength
            System.out.println("Struggling! Taps left: " + webStrength);

            if (webStrength <= 0) {
                // The lock is broken!
                // System.out.println("PLAYER: I'M FREE!");
                currentState = PlayerState.IDLE_DOWN; // FREEDOM!
            }
        }
    }

    /*
     * private void loadSprites() { try { // Using the path that worked for you
     * before! sprite_walk1 =
     * ImageIO.read(getClass().getResourceAsStream("/res/sprites/bug.png"));
     * sprite_walk2 =
     * ImageIO.read(getClass().getResourceAsStream("/res/sprites/bug_mov_1.png")); }
     * catch (IOException e) { e.printStackTrace(); } }
     */
    private void checkLowHunger(SoundManager soundManager) {
        int hungerPercentage = (hunger*100)/MAX_HUNGER;

        if(hungerPercentage <= LOW_HUNGER_THRESHOLD && !isLowHungerWarningPlayed) {
            soundManager.playSound("lowhunger");
            isLowHungerWarningPlayed = true;
        } else if(hungerPercentage > LOW_HUNGER_THRESHOLD) {
            isLowHungerWarningPlayed = false;
        }
    }

    /**
     * Updates the player's position based on movement flags. This will be called in
     * the main game loop.
     */
    // The new update method now takes the World as an argument
    public void update(World world, SoundManager soundManager) {
        // --- First, check for paralysis ---
        // Inside Player.java's update() method...
        int currentTileCol = getCenterX() / World.TILE_SIZE;
        int currentTileRow = getCenterY() / World.TILE_SIZE;
        int tileID = world.getTileIdAt(currentTileCol, currentTileRow);
        if (tileID == 3) { // If it's our sticky tile ID
            this.currentSpeed = SLOW_SPEED;
        } else {
            this.currentSpeed = NORMAL_SPEED;
        }

        if (currentState == PlayerState.WEBBED) {
            // --- Webbed Logic (Check for instant death first) ---
            if (isCrying) { // If caught while already crying
                System.out.println("PLAYER: Caught while crying! Instant death.");
                this.hunger = 0; // Set hunger to 0 (or health if you reintroduced it)
                // No need to return here, let GamePanel handle the Game Over state next frame
            } else {
                // Normal webbed countdown
                webbedTimer--;
                if (webbedTimer <= 0) {
                    System.out.println("PLAYER: I'M FREE!");
                    currentState = PlayerState.IDLE_DOWN;
                }
                return; // Still can't move while webbed (unless instantly dead)
            }
        }

        if (isCrying) {
            // If crying, the death timer ticks down
            cryDeathTimer--;
            System.out.println("Crying! Time until death: " + cryDeathTimer / 60); // Debug print seconds left

            if (cryDeathTimer <= 0) {
                System.out.println("PLAYER: Died from hunger/crying.");
                this.hunger = 0; // Ensure hunger is 0 for Game Over check
            }
            // Player can potentially still move while crying (unless you want them frozen?)
            // If you want them frozen while crying, add a 'return;' here.
        } else {
            // --- Normal Hunger Drain (Only if NOT crying) ---
            hungerDrainTimer++;
            if (hungerDrainTimer > 180) { // Drain 1 hunger every 3 seconds
                this.hunger--;
                hungerDrainTimer = 0;
                // System.out.println("Hunger: " + this.hunger); // Optional debug

                if (this.hunger <= 0) {
                    this.hunger = 0;
                    if (!isCrying) { // Start crying ONLY if not already crying
                        System.out.println("PLAYER: WAAAAAAH! Hunger is zero!");
                        this.isCrying = true;
                        this.cryDeathTimer = CRY_DEATH_DURATION; // Start the 20-second death timer
                        // Maybe play a continuous cry sound loop here? soundManager.loopSound("cry");
                    }
                }
            }
        }
        checkLowHunger(soundManager);

        if (currentState != PlayerState.WEBBED) {
            // --- If we are NOT webbed, proceed with normal life ---

            // 1. State Management: Decide which animation to play.
            PlayerState previousState = currentState;

            if (movingUp) {
                currentState = PlayerState.WALKING_UP;
            } else if (movingDown) {
                currentState = PlayerState.WALKING_DOWN;
            } else if (movingLeft) { // Separate check for Left
                currentState = PlayerState.WALKING_LEFT;
            } else if (movingRight) { // Separate check for Right
                currentState = PlayerState.WALKING_RIGHT;
            } else {
                // If not moving, stay idle (facing down for now)
                currentState = PlayerState.IDLE_DOWN;
            }

            if (previousState != currentState) {
                currentFrame = 0;
                animationTick = 0;
            }

            // 2. Movement & Wall Collision
            // (Your existing wall collision logic is perfect here)
            double nextX = x, nextY = y;
            if (movingUp)
                nextY -= currentSpeed;
            if (movingDown)
                nextY += currentSpeed;
            if (movingLeft)
                nextX -= currentSpeed;
            if (movingRight)
                nextX += currentSpeed;

            if (nextX != x || nextY != y) {
                int nextLeft = (int) nextX;
                int nextRight = (int) nextX + width - 1;
                int nextTop = (int) nextY;
                int nextBottom = (int) nextY + height - 1;

                if (!world.isTileSolid(nextLeft, nextTop) && !world.isTileSolid(nextRight, nextTop)
                        && !world.isTileSolid(nextLeft, nextBottom) && !world.isTileSolid(nextRight, nextBottom)) {
                    x = (int) nextX;
                    y = (int) nextY;
                }
            }

            // 3. Hunger Drain (runs only when not webbed)
            hungerDrainTimer++;
            if (hungerDrainTimer > 120) {
                this.hunger--;
                hungerDrainTimer = 0;
                // Check if hunger is depleted
                if (this.hunger <= 0) {
                    this.hunger = 0;
                    System.out.println("PLAYER: WAAAAAAH!");
                    this.isCrying = true;
                }
            }

            // 4. Animation
            boolean isMoving = movingUp || movingDown || movingLeft || movingRight;
            if (isMoving) {
                animationTick++;
                if (animationTick > animationSpeed) {
                    animationTick = 0;
                    currentFrame = (currentFrame + 1) % getActiveAnimation().size();
                }
            } else {
                currentFrame = 0;
            }
        }
    }

    private List<BufferedImage> getActiveAnimation() {
        switch (currentState) {
        case WALKING_UP:
            return walkUpFrames;
        case WALKING_DOWN:
            return walkDownFrames;
        case WALKING_LEFT:
            return walkLeftFrames; // Use the new list
        case WALKING_RIGHT:
            return walkRightFrames; // Use the new list
        case IDLE_DOWN:
        default:
            return idleDownFrames;
        }
    }

    /**
     * Draws the player on the screen.
     * 
     * @param g The graphics context to draw with.
     */
    public void draw(Graphics g) {
        // --- This is the new, upgraded draw method ---

        // Cast our crayon to a fancy art tool
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy to not mess up other drawings

        BufferedImage imageToDraw = null;

        if (spriteNum == 1) {
            imageToDraw = sprite_walk1;
        } else {
            imageToDraw = sprite_walk2;
        }

        if (imageToDraw != null) {

            int centerX = x + width / 2;
            int centerY = y + height / 2;

            // This object holds our rotation information
            AffineTransform tx = new AffineTransform();
            // Tell it to rotate around the center of the bug
            tx.rotate(Math.toRadians(rotationAngle), centerX, centerY);

            g2d.setTransform(tx); // Apply the rotation to our drawing tool

            // Draw the image!
            g2d.drawImage(imageToDraw, x, y, width, height, null);
        } else {
            // Fallback green square in case something breaks
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }

        g2d.dispose(); // Clean up our copy
    }

    public void setRotationAngle(double angle) {
        this.rotationAngle = angle;
    }

    public int getX() {
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    /**
     * Returns the player's collision bounds. Super useful later!
     * 
     * @return A Rectangle object representing the player's position and size.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void decreaseHunger(int amount) {
        this.hunger -= amount;
        if (hunger < 0)
            hunger = 0;
    }

    public void takeDamage() {
        // For future implementation of damage mechanics
        this.isCrying = true;
    }

    public int getHunger() {
        return this.hunger;
    }

    public boolean isCrying() {
        return this.isCrying;
    }
}