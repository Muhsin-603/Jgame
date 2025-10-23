package src.com.buglife.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class Food {
    private int x, y;
    private int size;

    public Food(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    private boolean isBeingEaten = false;
    private int eatAnimationTick = 0;
    private final int EAT_ANIMATION_DURATION = 5;

    public void draw(Graphics g) {
        if (!isBeingEaten) {
            // Normal food appearance
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, size, size);
            
            // Add a slight glow effect
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2d.setColor(new Color(255, 255, 200));
            g2d.fillOval(x - 2, y - 2, size + 4, size + 4);
            g2d.dispose();
        } else {
            // Eating animation
            float alpha = 1.0f - (float)eatAnimationTick / EAT_ANIMATION_DURATION;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(x, y, size, size);
            g2d.dispose();
            
            eatAnimationTick++;
        }
    }
    
    public void startEatingAnimation() {
        isBeingEaten = true;
        eatAnimationTick = 0;
    }
    
    public boolean isAnimationComplete() {
        return isBeingEaten && eatAnimationTick >= EAT_ANIMATION_DURATION;
    }

    // --- Collision Helpers ---
    public int getCenterX() {
        return x + size / 2;
    }

    public int getCenterY() {
        return y + size / 2;
    }

    public double getRadius() {
        return size / 2.0;
    }
}