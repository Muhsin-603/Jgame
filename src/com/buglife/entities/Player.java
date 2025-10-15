package src.com.buglife.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

//import java.awt.Graphics;
//import java.awt.Rectangle;
import javax.imageio.ImageIO;
import java.io.*;


public class Player {
    // Player attributes
    private int x, y;
    private int width, height;
    private double speed = 4.0;
    private int health = 100;

    private BufferedImage sprite_walk1, sprite_walk2; // Just our two images
    private int animationTick = 0;
    private int animationSpeed = 15; // Change sprite every 15 frames. Higher is slower.
    private int spriteNum = 1;       // Which sprite to show: 1 or 2

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        loadSprites();
    }

    

    // In Player.java




    // Movement state flags
    public boolean movingUp, movingDown, movingLeft, movingRight;

    /**
     * Constructor for our heroic bug.
     * @param startX The initial X position.
     * @param startY The initial Y position.
     * @param size The width and height of the player.
     */
    public Player(int startX, int startY, int size) {
        this.x = startX;
        this.y = startY;
        this.width = size;
        this.height = size;
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
     * @param g The graphics context to draw with.
     */
        public void draw(Graphics g) {
            BufferedImage imageToDraw = null;

        // Decide which image to draw based on our spriteNum
            if (spriteNum == 1) {
                imageToDraw = sprite_walk1;
            } else {
                imageToDraw = sprite_walk2;
            }
        
            if (imageToDraw != null) {
                g.drawImage(imageToDraw, x, y, width, height, null);
            } else {
            // Failsafe green square
                g.setColor(Color.GREEN);
                g.fillRect(x, y, width, height);
            }
        }
    
    /**
     * Returns the player's collision bounds. Super useful later!
     * @return A Rectangle object representing the player's position and size.
     */
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public void takeDamage(int amount) { this.health -= amount; if(health < 0) health = 0; System.out.println("Health: " + health); }
    public int getHealth() { return this.health; }
}