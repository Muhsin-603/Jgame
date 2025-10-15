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

    private BufferedImage sprite;
    public int getHealth(){
        return this.health;
    }

    // In Player.java


    public void takeDamage(int amount) {
        this.health -= amount;
        System.out.println("Player health: " + health);
        if (health <= 0) {
            System.out.println("GAME OVER - You have been eaten.");
        // In a real game, you'd trigger a game over screen here.
        }
    }

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
        loadSprite();
    }

    private void loadSprite(){
        try{
            InputStream is = getClass().getResourceAsStream("/sprites/bug.png");
            if(is != null)
            {
                sprite = ImageIO.read(is);
            }else{
                System.err.println("sprite not found....!");
            }
        }catch(IOException e){
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
    }

    /**
     * Draws the player on the screen.
     * @param g The graphics context to draw with.
     */
    public void draw(Graphics g) {
        if(sprite != null){
            g.drawImage(sprite, x, y, width, height, null);
        }else{
        g.setColor(Color.GREEN); // Still our beloved green rectangle
        g.fillRect(x, y, width, height);
        }
    }
    
    /**
     * Returns the player's collision bounds. Super useful later!
     * @return A Rectangle object representing the player's position and size.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}