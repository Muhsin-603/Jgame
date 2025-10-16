package src.com.buglife.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.geom.AffineTransform;

public class Player {
    // Player attributes
    // x and y now represent the CENTER of the player
    private double x, y;
    private int width, height;
    private double speed = 4.0;
    private int health = 100;
    private int collisionRadius;

    private BufferedImage sprite_walk1, sprite_walk2; // Just our two images
    // Offsets to compensate for any transparent padding inside the sprite images.
    // These will be calculated from the image content so the visible bug is centered.
    private double spriteOffsetX1 = 0, spriteOffsetY1 = 0;
    private double spriteOffsetX2 = 0, spriteOffsetY2 = 0;
    private int animationTick = 0;
    private int animationSpeed = 3; // Change sprite every 15 frames. Higher is slower.
    private int spriteNum = 1; // Which sprite to show: 1 or 2

    private double rotationAngle = 0;

    // Movement state flags
    public boolean movingUp, movingDown, movingLeft, movingRight;

    // Add this method to your Player.java class

    public void drawDebugInfo(Graphics g) {
        // Create a disposable copy to be extra safe
        Graphics2D g2d = (Graphics2D) g.create();

        // Set a nice, visible color like yellow or white
        g2d.setColor(Color.YELLOW);

        // Set a font
        g2d.setFont(new Font("Consolas", Font.BOLD, 14));

        // Get the player's center coordinates
        int centerX = getCenterX();
        int centerY = getCenterY();

        // Draw the data near the player's center
        g2d.drawString("Player Center: [" + centerX + ", " + centerY + "]", centerX + 30, centerY);
        g2d.drawString("Hitbox Radius: " + this.collisionRadius, centerX + 30, centerY + 15);

        // Throw the copy away
        g2d.dispose();
    }

    // The new, super-safe drawHitbox method in Player.java
    public void drawHitbox(Graphics g) {
        // 1. Create a disposable copy, JUST for the hitbox.
        Graphics2D g2d = (Graphics2D) g.create();

        // Set the color to a semi-transparent red
        g2d.setColor(new Color(255, 0, 0, 100));

        // Draw the oval centered on the player's x, y coordinates
        g2d.fillOval(
                (int) Math.round(x - collisionRadius),
                (int) Math.round(y - collisionRadius),
                collisionRadius * 2,
                collisionRadius * 2);

        // 2. Throw the copy away immediately.
        g2d.dispose();
    }

    // In Player.java

    public int getCenterX() {
        // x is now the center, so just return it.
        return (int) Math.round(this.x);
    }

    public int getCenterY() {
        // y is now the center, so just return it.
        return (int) Math.round(this.y);
    }

    public double getRadius() {
        return this.collisionRadius;
    }

    // In Player.java

    // Movement state flags are now back to movingUp, movingDown, etc.

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
            // Compute content offsets so the visible content is centered on the logical center
            if (sprite_walk1 != null) {
                double[] o1 = computeSpriteContentOffset(sprite_walk1);
                spriteOffsetX1 = o1[0];
                spriteOffsetY1 = o1[1];
            }
            if (sprite_walk2 != null) {
                double[] o2 = computeSpriteContentOffset(sprite_walk2);
                spriteOffsetX2 = o2[0];
                spriteOffsetY2 = o2[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Scans the sprite image for non-transparent pixels and returns an (x,y)
     * offset in destination-pixels to shift the drawn image so its visible
     * content is centered. The offsets are in the same units as the final
     * drawing size (i.e. already scaled to the configured width/height).
     */
    private double[] computeSpriteContentOffset(BufferedImage img) {
        int iw = img.getWidth();
        int ih = img.getHeight();
        int minX = iw, minY = ih, maxX = 0, maxY = 0;
        boolean found = false;

        for (int yy = 0; yy < ih; yy++) {
            for (int xx = 0; xx < iw; xx++) {
                int a = (img.getRGB(xx, yy) >>> 24) & 0xff;
                if (a > 0) {
                    found = true;
                    if (xx < minX) minX = xx;
                    if (yy < minY) minY = yy;
                    if (xx > maxX) maxX = xx;
                    if (yy > maxY) maxY = yy;
                }
            }
        }

        if (!found) return new double[] { 0.0, 0.0 };

        double contentCenterX = (minX + maxX) / 2.0;
        double contentCenterY = (minY + maxY) / 2.0;

        // When we draw the image scaled to 'width'/'height', scale the pixel offset accordingly.
        double offsetX = (iw / 2.0 - contentCenterX) * ((double) width / (double) iw);
        double offsetY = (ih / 2.0 - contentCenterY) * ((double) height / (double) ih);

        return new double[] { offsetX, offsetY };
    }

    /**
     * Updates the player's position based on movement flags.
     * This will be called in the main game loop.
     */
    public void update() {
        if (movingUp) {
            y -= speed;
        }
        if (movingDown) {
            y += speed;
        }
        if (movingLeft) {
            x -= speed;
        }
        if (movingRight) {
            x += speed;
        }
        boolean isMoving = movingUp || movingDown || movingLeft || movingRight;

        if (isMoving) {
            animationTick++;
            if (animationTick > animationSpeed) {
                animationTick = 0;
                // Flip between sprite 1 and 2
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }
            }
        }
    }

    /**
     * Draws the player on the screen.
     * 
     * @param g The graphics context to draw with.
     */
    public void draw(Graphics g) {
        // Cast our crayon to a fancy art tool
        Graphics2D g2d = (Graphics2D) g.create(); // Create a copy to not mess up other drawings

        BufferedImage imageToDraw = (spriteNum == 1) ? sprite_walk1 : sprite_walk2;

        if (imageToDraw != null) {
            // The AffineTransform will handle the rotation and positioning.
            AffineTransform tx = new AffineTransform();

            // 1. Calculate the top-left corner for drawing, since x/y is the center
            double topLeftX = x - width / 2.0;
            double topLeftY = y - height / 2.0;

            // 2. Translate the transform to the player's top-left position.
            tx.translate(Math.round(topLeftX), Math.round(topLeftY));

            // Apply sprite-specific content offset so the visible pixels are centered.
            double offX = (spriteNum == 1) ? spriteOffsetX1 : spriteOffsetX2;
            double offY = (spriteNum == 1) ? spriteOffsetY1 : spriteOffsetY2;
            tx.translate(offX, offY);

            // 3. Set the rotation of the transform around the image's center.
            tx.rotate(Math.toRadians(rotationAngle), width / 2.0, height / 2.0);

            // Apply the transform to the graphics context
            g2d.setTransform(tx);

            // Draw the image at the origin (0,0) of the new, transformed coordinate space.
            g2d.drawImage(imageToDraw, 0, 0, width, height, null);

        } else {
            // Fallback green square in case something breaks
            double topLeftX = x - width / 2.0;
            double topLeftY = y - height / 2.0;
            g2d.setColor(Color.GREEN);
            g2d.fillRect((int) Math.round(topLeftX), (int) Math.round(topLeftY), width, height);
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
        double topLeftX = x - width / 2.0;
        double topLeftY = y - height / 2.0;
        return new Rectangle((int) Math.round(topLeftX), (int) Math.round(topLeftY), width, height);
    }

    public void takeDamage(int amount) {
        System.out.println("!!! DAMAGE METHOD CALLED !!!");
        this.health -= amount;
        if (health < 0)
            health = 0;
        System.out.println("Health: " + health);
    }

    public int getHealth() {
        return this.health;
    }
}