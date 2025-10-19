package src.com.buglife.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;

public class Player {
    // Player attributes
    private int x, y;
    private int width, height;
    private double speed = 4.0;
    private int health = 100;
    private int collisionRadius;
    private int healthDrainTimer = 0;
    private boolean isFacingLeft = false;

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
    private List<BufferedImage> walkRightFrames;

    public enum PlayerState {
        IDLE_DOWN, WALKING_DOWN, WALKING_UP, WALKING_HORIZONTAL
    }

    public void heal(int amount) {
        this.health = Math.min(100, this.health + amount);
        if (this.health > 100) {
            this.health = 100;
        }
    }
    // Add this method anywhere inside your Player class

public void reset() {
    // Reset to the default starting position and state
    this.x = 200;
    this.y = 200;
    this.health = 100; // Assuming health is a field in Player
    this.currentState = PlayerState.IDLE_DOWN;
    
    // Make sure it's not moving
    this.movingUp = false;
    this.movingDown = false;
    this.movingLeft = false;
    this.movingRight = false;
}

    public void render(Graphics g) {
        // --- PART 1: DRAW THE PLAYER (This part is the same) ---
        List<BufferedImage> currentAnimation = getActiveAnimation();
        if (currentAnimation == null || currentAnimation.isEmpty() || currentFrame >= currentAnimation.size()) {
            // If something is wrong, draw a magenta square so we KNOW
            g.setColor(Color.MAGENTA);
            g.fillRect(this.x, this.y, this.width, this.height);
            return;
        }

        BufferedImage imageToDraw = currentAnimation.get(currentFrame);

        int drawX = this.x;
        int drawWidth = this.width;

        if (!isFacingLeft && currentState == PlayerState.WALKING_HORIZONTAL) {
            drawX = this.x + this.width;
            drawWidth = -this.width;
        }

        g.drawImage(imageToDraw, drawX, this.y, drawWidth, this.height, null);

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

    private void loadAnimations() {
        idleDownFrames = new ArrayList<>();
        walkDownFrames = new ArrayList<>();
        walkUpFrames = new ArrayList<>();
        walkRightFrames = new ArrayList<>();

        try {
            BufferedImage spriteSheet = ImageIO
                    .read(getClass().getResourceAsStream("/res/sprites/player/loose sprites.png"));
            final int SPRITE_WIDTH = 16;
            final int SPRITE_HEIGHT = 16;

            // --- THE CORRECTED SLICING ---
            // Walk Up (Top Row, showing the back)
            walkUpFrames.add(spriteSheet.getSubimage(0 * SPRITE_WIDTH, 0, SPRITE_WIDTH, SPRITE_HEIGHT));
            walkUpFrames.add(spriteSheet.getSubimage(1 * SPRITE_WIDTH, 0, SPRITE_WIDTH, SPRITE_HEIGHT));

            // Walk Down (Second Row, facing us)
            walkDownFrames
                    .add(spriteSheet.getSubimage(0 * SPRITE_WIDTH, 1 * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT));
            walkDownFrames
                    .add(spriteSheet.getSubimage(1 * SPRITE_WIDTH, 1 * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT));

            // Walk Right (Third Row, facing right)
            walkRightFrames
                    .add(spriteSheet.getSubimage(0 * SPRITE_WIDTH, 2 * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT));
            walkRightFrames
                    .add(spriteSheet.getSubimage(1 * SPRITE_WIDTH, 2 * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT));

            // Idle (first frame of walking down)
            idleDownFrames.add(walkDownFrames.get(0));

        } catch (Exception e) {
            System.err.println("CRASH! Could not slice the player sprite sheet correctly.");
            e.printStackTrace();
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

    /**
     * Updates the player's position based on movement flags. This will be called in
     * the main game loop.
     */
    // The new update method now takes the World as an argument
    public void update(src.com.buglife.world.World world) {
        // Calculate how far we WILL move this frame

        PlayerState previousState = currentState;
        if (movingUp) {
            currentState = PlayerState.WALKING_UP;
        } else if (movingDown) {
            currentState = PlayerState.WALKING_DOWN;
        } else if (movingLeft || movingRight) {
            currentState = PlayerState.WALKING_HORIZONTAL;
            isFacingLeft = movingLeft;
        } else {
            currentState = PlayerState.IDLE_DOWN;
        }

        // If we changed state, reset the animation to the first frame
        if (previousState != currentState) {
            currentFrame = 0;
            animationTick = 0;
        }

        double nextX = x, nextY = y;
        if (movingUp)
            nextY -= speed;
        if (movingDown)
            nextY += speed;
        if (movingLeft)
            nextX -= speed;
        if (movingRight)
            nextX += speed;

        // We check for wall collision only if the player is actually trying to move.
        if (nextX != x || nextY != y) {
            int nextLeft = (int) nextX;
            int nextRight = (int) nextX + width - 1;
            int nextTop = (int) nextY;
            int nextBottom = (int) nextY + height - 1;

            boolean topLeftSolid = world.isTileSolid(nextLeft, nextTop);
            boolean topRightSolid = world.isTileSolid(nextRight, nextTop);
            boolean bottomLeftSolid = world.isTileSolid(nextLeft, nextBottom);
            boolean bottomRightSolid = world.isTileSolid(nextRight, nextBottom);

            // Only commit the move if the path is clear.
            if (!topLeftSolid && !topRightSolid && !bottomLeftSolid && !bottomRightSolid) {
                x = (int) nextX;
                y = (int) nextY;
            }
        }
        healthDrainTimer++;

        if (healthDrainTimer > 120) {

            takeDamage(1);

            healthDrainTimer = 0;

        }

        // --- Animation logic (this part stays the same) ---
        boolean isMoving = movingUp || movingDown || movingLeft || movingRight;

        if (isMoving) { // <-- THE FIX! Only animate if we are moving.
            animationTick++;
            if (animationTick > animationSpeed) {
                animationTick = 0;
                currentFrame++;

                List<BufferedImage> currentAnimation = getActiveAnimation();
                if (currentFrame >= currentAnimation.size()) {
                    currentFrame = 0; // Loop the animation
                }
            }
        } else {
            // If we're not moving, reset to the first frame of the animation
            currentFrame = 0;
        }
    }

    private List<BufferedImage> getActiveAnimation() {
        switch (currentState) {
        case WALKING_UP:
            return walkUpFrames;
        case WALKING_DOWN:
            return walkDownFrames;
        case WALKING_HORIZONTAL:
            return walkRightFrames;
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

    /**
     * Returns the player's collision bounds. Super useful later!
     * 
     * @return A Rectangle object representing the player's position and size.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void takeDamage(int amount) {
        // System.out.println("!!! DAMAGE METHOD CALLED !!!");
        this.health -= amount;
        if (health < 0)
            health = 0;

    }

    public int getHealth() {
        return this.health;
    }
}