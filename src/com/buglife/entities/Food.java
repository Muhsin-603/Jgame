package src.com.buglife.entities;

import java.awt.Color;
import java.awt.Graphics;

public class Food {
    private int x, y;
    private int size;

    public Food(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics g) {
        // A delicious, life-giving yellow square. The finest crumb.
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, size, size);
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