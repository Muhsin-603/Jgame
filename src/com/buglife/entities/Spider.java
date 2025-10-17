package src.com.buglife.entities;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Spider {
    // Core Attributes
    private int x, y;
    private int width = 32, height = 32; // Using the bug's original size
    private int speed = 2;
    private int direction = 1;
    private int moveRange = 200;
    private int startX;
    private double rotationAngle = 90; // Start facing right (90 degrees from North)

    // Animation Reel (for the 2-frame bug)
    private BufferedImage[] walkingFrames;
    private final int TOTAL_FRAMES = 2;
    private int currentFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 15;

    public Spider(int startX, int startY) {
        this.startX = startX;
        this.x = startX;
        this.y = startY;
        loadSprites();
    }
    
    private void loadSprites() {
        walkingFrames = new BufferedImage[TOTAL_FRAMES];
        try {
            // Load the original bug sprites
            walkingFrames[0] = ImageIO.read(getClass().getResourceAsStream("/res/sprites/bug.png"));
            walkingFrames[1] = ImageIO.read(getClass().getResourceAsStream("/res/sprites/bug_mov_1.png"));
        } catch (Exception e) {
            System.err.println("CRASH! Could not load the bug sprites for the enemy.");
            e.printStackTrace();
        }
    }

    public void update() {
        // Movement AI
        x += speed * direction;
        if (x > startX + moveRange || x < startX) {
            direction *= -1;

            // USE THE ROTATION LOGIC FOR AN UP-FACING SPRITE
            rotationAngle = (direction == 1) ? 90 : -90; // 90 for Right, -90 for Left
        }

        // Animation Logic
        animationTick++;
        if (animationTick > animationSpeed) {
            animationTick = 12;
            currentFrame = (currentFrame == 0) ? 1 : 0; // Flip between 0 and 1
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(this.x, this.y);
        g2d.rotate(Math.toRadians(this.rotationAngle), this.width / 2.0, this.height / 2.0);

        BufferedImage imageToDraw = walkingFrames[currentFrame];

        if (imageToDraw != null) {
            g2d.drawImage(imageToDraw, 0, 0, this.width, this.height, null);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(0, 0, this.width, this.height);
        }
        g2d.dispose();
    }
    
    // Collision Helpers
    public int getCenterX() { return x + width / 2; }
    public int getCenterY() { return y + height / 2; }
    public double getRadius() { return width / 2.0; }
}