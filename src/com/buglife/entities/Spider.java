package src.com.buglife.entities;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
    private boolean isMovingForward = true;

    public Spider(List<Point> tilePath) {
        // Convert the tile-based path to a pixel-based path
        this.patrolPath = new ArrayList<>();
        for (Point tilePoint : tilePath) {
            int pixelX = tilePoint.x * World.TILE_SIZE + (World.TILE_SIZE / 2);
            int pixelY = tilePoint.y * World.TILE_SIZE + (World.TILE_SIZE / 2);
            this.patrolPath.add(new Point(pixelX, pixelY));
        }

        // Spawn at the first point in the path
        if (!this.patrolPath.isEmpty()) {
            this.x = this.patrolPath.get(0).x - (width / 2);
            this.y = this.patrolPath.get(0).y - (height / 2);
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

    // Add this method anywhere inside your Spider class

public void reset() {
    System.out.println("Spider is resetting to its starting position.");
    
    // Teleport back to the first point in the patrol path
    if (patrolPath != null && !patrolPath.isEmpty()) {
        this.x = patrolPath.get(0).x - (width / 2.0);
        this.y = patrolPath.get(0).y - (height / 2.0);
    }
    
    // Reset the AI's brain to its initial state
    this.currentTargetIndex = 1; // Immediately target the second point to start moving
    this.isMovingForward = true;
    
    // Reset the animation to the first frame
    this.currentFrame = 0;
    this.animationTick = 0;
}

    // In Spider.java, replace the entire update method.
    // In Spider.java

    public void update(World world) {
    if (patrolPath == null || patrolPath.isEmpty() || patrolPath.size() < 2) {
        return; // Can't patrol without at least two points.
    }

    // --- The "Retrace Your Steps" AI ---
    
    // 1. Get the current target.
    Point target = patrolPath.get(currentTargetIndex);
    double dx = target.x - getCenterX();
    double dy = target.y - getCenterY();
    double distance = Math.sqrt(dx * dx + dy * dy);

    // 2. Check if we've arrived at our destination.
    if (distance < 5) { // The "close enough" rule
        // We've arrived! Decide where to go next based on our direction.
        if (isMovingForward) {
            currentTargetIndex++; // Move to the next point in the list.
            if (currentTargetIndex >= patrolPath.size()) {
                // We've hit the end! Time to turn back.
                isMovingForward = false;
                currentTargetIndex = patrolPath.size() - 2; // Target the second-to-last point.
            }
        } else { // We are moving backward.
            currentTargetIndex--; // Move to the previous point.
            if (currentTargetIndex < 0) {
                // We've hit the beginning! Time to turn back again.
                isMovingForward = true;
                currentTargetIndex = 1; // Target the second point.
            }
        }
        return; // Get a fresh start on the next frame.
    }
    
    // 3. If we haven't arrived, calculate movement.
    double moveX = (dx / distance) * speed;
    double moveY = (dy / distance) * speed;
    
    // 4. THE CONSCIENCE: Check for walls before moving.
    double nextX = x + moveX;
    double nextY = y + moveY;
    
    int nextLeft = (int) nextX;
    int nextRight = (int) nextX + width - 1;
    int nextTop = (int) nextY;
    int nextBottom = (int) nextY + height - 1;

    if (!world.isTileSolid(nextLeft, nextTop) && !world.isTileSolid(nextRight, nextTop) &&
        !world.isTileSolid(nextLeft, nextBottom) && !world.isTileSolid(nextRight, nextBottom)) {
        // Path is clear! Commit the move and update rotation.
        x = nextX;
        y = nextY;
        this.rotationAngle = Math.toDegrees(Math.atan2(moveY, moveX)) + 90;
    } else {
        // Hit a wall! Skip to the next waypoint to try and get unstuck.
        currentTargetIndex = (currentTargetIndex + 1) % patrolPath.size();
    }
    
    // 5. Animate.
    animationTick++;
    if (animationTick > animationSpeed) {
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

    // Collision Helpers
    // Helper methods now cast the double to an int just before returning
    public int getX() {return (int) this.x;}
    public int getY() {return (int) this.y;}
    public int getCenterX() {return (int) this.x + width / 2;}
    public int getCenterY() {return (int) this.y + height / 2;}
    }
