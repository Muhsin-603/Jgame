package src.com.buglife.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.geom.AffineTransform;

public class Player {
    // Player attributes
    private int x, y;
    private int width, height;
    private double speed = 4.0;
    private int health = 100;
    private int collisionRadius;
    private int healthDrainTimer = 0;

    private BufferedImage sprite_walk1, sprite_walk2; // Just our two images
    private int animationTick = 0;
    private int animationSpeed = 3; // Change sprite every 15 frames. Higher is slower.
    private int spriteNum = 1; // Which sprite to show: 1 or 2

    private double rotationAngle = 0;

    public void heal(int amount) {
        this.health += amount;
        if (this.health > 100) {
            this.health = 100;
        }
    }

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.translate(this.x, this.y);

        g2d.rotate(Math.toRadians(this.rotationAngle), this.width / 2.0, this.height / 2.0);

        BufferedImage imageToDraw = (spriteNum == 1) ? sprite_walk1 : sprite_walk2;
        if (imageToDraw != null) {
            g2d.drawImage(imageToDraw, 0, 0, this.width, this.height, null);
        }

        // 4. Throw the "div" away. The main screen is unaffected.
        g2d.dispose();
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
        loadSprites();
    }

    private void loadSprites() {
        try {
            // Using the path that worked for you before!
            sprite_walk1 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/bug.png"));
            sprite_walk2 = ImageIO.read(getClass().getResourceAsStream("/res/sprites/bug_mov_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the player's position based on movement flags.
     * This will be called in the main game loop.
     */
    // The new update method now takes the World as an argument
    public void update(src.com.buglife.world.World world) {
        // Calculate how far we WILL move this frame
        double nextX = x;
        double nextY = y;

        if (movingUp) {
            nextY -= speed;
        }
        if (movingDown) {
            nextY += speed;
        }
        if (movingLeft) {
            nextX -= speed;
        }
        if (movingRight) {
            nextX += speed;
        }

        // --- NEW COLLISION CHECK ---
        // Get the player's collision bounding box at the *next* position
        int nextLeft = (int) nextX;
        int nextRight = (int) nextX + width - 1;
        int nextTop = (int) nextY;
        int nextBottom = (int) nextY + height - 1;

        // Check all four corners for a collision in the next position
        boolean topLeftSolid = world.isTileSolid(nextLeft, nextTop);
        boolean topRightSolid = world.isTileSolid(nextRight, nextTop);
        boolean bottomLeftSolid = world.isTileSolid(nextLeft, nextBottom);
        boolean bottomRightSolid = world.isTileSolid(nextRight, nextBottom);

        // If NONE of the corners are about to hit a solid tile, the move is safe!
        if (!topLeftSolid && !topRightSolid && !bottomLeftSolid && !bottomRightSolid) {
            // Commit the move
            x = (int) nextX;
            y = (int) nextY;
        }
        healthDrainTimer++;

        if (healthDrainTimer > 120) {

            takeDamage(1);

            healthDrainTimer = 0;

        }

        // --- Animation logic (this part stays the same) ---
        boolean isMoving = movingUp || movingDown || movingLeft || movingRight;
        if (isMoving) {
            animationTick++;
            if (animationTick > animationSpeed) {
                animationTick = 0;
                spriteNum = (spriteNum == 1) ? 2 : 1;
            }
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