package src.com.buglife.entities; // <-- Fixed the package!

import src.com.buglife.world.World;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

public class Snail {

    private double x, y;
    private int width = 35, height = 55; // Use the Idle dimensions
    
    // --- Simplified Animation ---
    private List<BufferedImage> idleFrames;
    private int currentFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 20; // Snails are slow
    //private int START_X;

    // --- State ---
    private boolean isVisible = true;

    // --- Glow effect ---
    private int glowRadius = 50;
    private Color glowColor = new Color(255, 255, 150, 60);

    public Snail(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        loadAnimations();
    }

    private void loadAnimations() {
        idleFrames = new ArrayList<>();
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/sprites/snail/snail.png"));

            // --- Slicing ONLY the Idle Animation ---
            // Based on your measurements for the 143x224 sheet:
            final int IDLE_WIDTH = 35, IDLE_HEIGHT = 55;
            final int IDLE_START_X = 0; // Top-left of sheet
            final int IDLE_START_Y = 0;
            final int IDLE_PADDING_X = 17;
            final int IDLE_H_STEP = IDLE_WIDTH + IDLE_PADDING_X; // 52
            final int IDLE_FRAMES = 3; // The first row has 3 frames

            for (int i = 0; i < IDLE_FRAMES; i++) {
                int frameX = IDLE_START_X + (i * IDLE_H_STEP);
                // In your Snail.java file, inside loadAnimations():
                idleFrames.add(spriteSheet.getSubimage(frameX, IDLE_START_Y, IDLE_WIDTH, IDLE_HEIGHT));
            }
            System.out.println("Snail Idle Animation Loaded!");

        } catch (Exception e) {
            System.err.println("CRASH! Could not slice snail idle frames. Check measurements!");
            e.printStackTrace();
        }
    }

    // Update now just animates the idle loop
    public void update(World world) {
        animationTick++;
        if (animationTick > animationSpeed) {
            animationTick = 0;
            if (idleFrames != null && !idleFrames.isEmpty()) {
                currentFrame = (currentFrame + 1) % idleFrames.size();
            }
        }
    }

    public void draw(Graphics g) {
        if (!isVisible) return; // If hidden, do nothing.

        Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Draw glow effect
            Point center = new Point(getCenterX(), getCenterY());
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {this.glowColor, new Color(0, 0, 0, 0)};
            RadialGradientPaint paint = new RadialGradientPaint(center, glowRadius, dist, colors);
            g2d.setPaint(paint);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.glowColor.getAlpha() / 255f));
            g2d.fillOval(getCenterX() - glowRadius, getCenterY() - glowRadius, glowRadius * 2, glowRadius * 2);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // Draw the current idle frame
            if (idleFrames != null && !idleFrames.isEmpty()) {
                BufferedImage imageToDraw = idleFrames.get(currentFrame);
                g2d.drawImage(imageToDraw, (int)x, (int)y, width, height, null);
            } else {
                g2d.setColor(Color.CYAN); // Failsafe
                g2d.fillOval((int)x, (int)y, width, height);
            }
        } finally {
            g2d.dispose();
        }
    }

    // --- New Methods for Teleporting ---
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void hide() { this.isVisible = false; }
    public void show() { this.isVisible = true; }
    public boolean isVisible() { return this.isVisible; }
    
    // Helper methods
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getCenterX() { return (int)x + width / 2; }
    public int getCenterY() { return (int)y + height / 2; }
}