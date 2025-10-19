package src.com.buglife.entities;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
//import java.util.ArrayList; 
import java.util.List;
import src.com.buglife.world.World;
import java.awt.Point;

public class Spider {
    // Core Attributes
    private double x, y;
    private int width = 48, height = 48;
    private int speed = 2;

    private double rotationAngle = 90; // Start facing right (90 degrees from North)

    // Animation Reel (for the 2-frame bug)
    private BufferedImage[] walkingFrames;
    private final int TOTAL_FRAMES = 2;
    private int currentFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 5;
    private List<Point> patrolPath;
    private int currentTargetIndex = 0;
    // private boolean isMovingForward = true;

    public Spider(List<Point> tilePath) {
        this.patrolPath = tilePath;  // Store the original tile coordinates
        
        // Start at the first point
        if (!tilePath.isEmpty()) {
            Point start = tilePath.get(0);
            this.x = start.x * World.TILE_SIZE;  // Convert to pixel position
            this.y = start.y * World.TILE_SIZE;
            this.currentTargetIndex = 0;
        }

        loadSprites();
    }

    private void loadSprites() {
        walkingFrames = new BufferedImage[TOTAL_FRAMES];
        try {
            // Load the original bug sprites
            walkingFrames[0] = ImageIO.read(getClass().getResourceAsStream("/res/sprites/spider/Walk_0001.png"));
            walkingFrames[1] = ImageIO.read(getClass().getResourceAsStream("/res/sprites/spider/Walk_0002.png"));
        } catch (Exception e) {
            System.err.println("CRASH! Could not load the bug sprites for the enemy.");
            e.printStackTrace();
        }
    }

    // In Spider.java, replace the entire update method.
    // In Spider.java

    public void update(World world) {
        if (patrolPath == null || patrolPath.isEmpty()) return;

        // Get current target
        Point target = patrolPath.get(currentTargetIndex);
        int targetX = target.x * World.TILE_SIZE;
        int targetY = target.y * World.TILE_SIZE;

        // Calculate next position
        double newX = x;
        double newY = y;

        // Calculate horizontal movement
        if (Math.abs(x - targetX) > speed) {
            newX += (x < targetX) ? speed : -speed;
        }

        // Check horizontal movement for collision
        if (!checkWallCollision(world, newX, y)) {
            x = newX;
        }

        // Calculate vertical movement
        if (Math.abs(y - targetY) > speed) {
            newY += (y < targetY) ? speed : -speed;
        }

        // Check vertical movement for collision
        if (!checkWallCollision(world, x, newY)) {
            y = newY;
        }

        // Check if we reached target
        if (Math.abs(x - targetX) <= speed && Math.abs(y - targetY) <= speed) {
            x = targetX;
            y = targetY;
            currentTargetIndex = (currentTargetIndex + 1) % patrolPath.size();
        }

        // Update rotation based on movement direction
        if (x != targetX || y != targetY) {
            rotationAngle = Math.toDegrees(Math.atan2(targetY - y, targetX - x));
        }

        // Update animation
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            currentFrame = (currentFrame + 1) % TOTAL_FRAMES;
        }
    }

    public void draw(Graphics g) {
        // We will use the simple draw method for now to be safe
        BufferedImage imageToDraw = walkingFrames[currentFrame];
        if (imageToDraw != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                g2d.rotate(Math.toRadians(this.rotationAngle), this.getCenterX(), this.getCenterY());
                g2d.drawImage(imageToDraw, (int) this.x, (int) this.y, this.width, this.height, null);
            } finally {g2d.dispose();}
        } 
        else {
            // Failsafe so we can see it even if sprites are null
            g.setColor(Color.MAGENTA);
            g.fillRect((int) this.x, (int) this.y, this.width, this.height);
        }
    }

    public double getRadius() {
        // My radius is just half my width!
        return width / 2.0;
    }

    // Check if spider is colliding with a wall
    private boolean checkWallCollision(World world, double checkX, double checkY) {
        // Check all four corners
        int left = (int)checkX;
        int right = (int)(checkX + width - 1);
        int top = (int)checkY;
        int bottom = (int)(checkY + height - 1);

        boolean collision = world.isTileSolid(left, top) ||
                          world.isTileSolid(right, top) ||
                          world.isTileSolid(left, bottom) ||
                          world.isTileSolid(right, bottom);

        

        return collision;
    }

    // Collision Helpers
    // Helper methods now cast the double to an int just before returning
    public int getX() {return (int) this.x;}
    public int getY() {return (int) this.y;}
    public int getCenterX() {return (int) this.x + width / 2;}
    public int getCenterY() {return (int) this.y + height / 2;}
    }
