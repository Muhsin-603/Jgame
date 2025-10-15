package src.com.buglife.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Spider {
    private int x, y;
    private int speed = 2;
    private int direction = 1; // 1 for right, -1 for left
    private int moveRange = 200; // How far it moves before turning around
    private int startX;

    public Spider(int startX, int startY) {
        this.startX = startX;
        this.x = startX;
        this.y = startY;
    }

    public void update() {
        x += speed * direction;
        if (x > startX + moveRange || x < startX) {
            direction *= -1; // Reverse direction
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 40, 40);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, 40, 40);
    }
}