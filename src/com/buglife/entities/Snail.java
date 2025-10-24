package src.com.buglife.entities;

//import src.com.buglife.main.GamePanel; // For screen dimensions if needed
import src.com.buglife.world.World;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
//import java.util.function.IntBinaryOperator;
//import java.util.function.BiFunction;

public class Snail {

    private double x, y;
    private int width = 35, height = 55; // Use exact dimensions
    private double speed = 0.5;
    private Player targetPlayer;
    private SnailState currentState = SnailState.IDLE;

    // --- Animation Fields ---
    private List<BufferedImage> idleFrames;
    private List<BufferedImage> walkUpFrames;
    private List<BufferedImage> walkDownFrames; // Placeholder
    private List<BufferedImage> walkLeftFrames;
    private List<BufferedImage> walkRightFrames;

    private int currentFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 20; // Snails are slow

    // --- Glow effect ---
    private boolean isGlowing = true;
    
    private int glowRadius = 50;
    private Color glowColor = new Color(255, 255, 150, 60); // Soft yellow glow, semi-transparent
    public enum SnailState {
        IDLE,
        MOVING_UP,
        MOVING_DOWN, // Need a down animation? Let's use idle for now.
        MOVING_LEFT,
        MOVING_RIGHT
    }

    public Snail(int startX, int startY, Player playerToFollow) {
        this.x = startX;
        this.y = startY;
        this.targetPlayer = playerToFollow;
        loadAnimations();
        
    
    }

    private void loadAnimations() {
    idleFrames = new ArrayList<>();
    walkUpFrames = new ArrayList<>();
    walkDownFrames = new ArrayList<>(); // Will reuse idle
    walkLeftFrames = new ArrayList<>();
    walkRightFrames = new ArrayList<>();

    try {
        // Use the correct path for the 143x224 sheet
        BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/sprites/snail/snail.png"));

        // --- Animation Frame Dimensions ---
        final int IDLE_WIDTH = 35, IDLE_HEIGHT = 55;
        final int RIGHT_WIDTH = 45, RIGHT_HEIGHT = 55;
        final int LEFT_WIDTH = 45, LEFT_HEIGHT = 55; // Corrected width based on spacing logic
        final int UP_WIDTH = 32, UP_HEIGHT = 55;

        // --- Horizontal Steps (Width + Padding) ---
        final int IDLE_H_STEP = IDLE_WIDTH + 17; // 35 + 17 = 52
        final int RIGHT_H_STEP = RIGHT_WIDTH + 7; // 45 + 7 = 52
        final int LEFT_H_STEP = LEFT_WIDTH + 7; // 45 + 7 = 52
        final int UP_H_STEP = UP_WIDTH + 18; // 32 + 18 = 50

        // --- Row Start Coordinates ---
        final int START_X = 0; // Top-left X is 0
        final int IDLE_START_Y = 0; // Top-left Y is 0
        final int RIGHT_START_Y = IDLE_START_Y + IDLE_HEIGHT + 2; // 0 + 55 + 2 = 57
        final int LEFT_START_Y = RIGHT_START_Y + RIGHT_HEIGHT + 2; // 57 + 55 + 2 = 114
        final int UP_START_Y = LEFT_START_Y + LEFT_HEIGHT + 2; // 114 + 55 + 2 = 171

        // --- Number of Frames ---
        final int NUM_FRAMES = 3; // Looks like 3 frames per row

        // --- THE DIGITAL SCISSORS ---

        // Idle Frames (Row 0)
        for (int i = 0; i < NUM_FRAMES; i++) {
            idleFrames.add(spriteSheet.getSubimage(START_X + i * IDLE_H_STEP, IDLE_START_Y, IDLE_WIDTH, IDLE_HEIGHT));
        }
        walkDownFrames.addAll(idleFrames); // Reuse idle for moving down

        // Walk Right Frames (Row 1)
        for (int i = 0; i < NUM_FRAMES; i++) {
            walkRightFrames.add(spriteSheet.getSubimage(START_X + i * RIGHT_H_STEP, RIGHT_START_Y, RIGHT_WIDTH, RIGHT_HEIGHT));
        }

        // Walk Left Frames (Row 2)
        for (int i = 0; i < NUM_FRAMES; i++) {
            walkLeftFrames.add(spriteSheet.getSubimage(START_X + i * LEFT_H_STEP, LEFT_START_Y, LEFT_WIDTH, LEFT_HEIGHT));
        }

        // Walk Up Frames (Row 3)
        for (int i = 0; i < NUM_FRAMES; i++) {
            walkUpFrames.add(spriteSheet.getSubimage(START_X + i * UP_H_STEP, UP_START_Y, UP_WIDTH, UP_HEIGHT));
        }

        System.out.println("Snail animations sliced precisely!");

    } catch (Exception e) {
        System.err.println("CRASH! Could not slice new snail sheet. Double-check measurements/calculations!");
        e.printStackTrace();
    }
}

    public void update(World world) {
    if (targetPlayer == null) return;

    double dx = targetPlayer.getCenterX() - getCenterX();
    double dy = targetPlayer.getCenterY() - getCenterY();
    double distance = Math.sqrt(dx * dx + dy * dy);
    int followDistance = 60;

    double moveX = 0, moveY = 0;
    SnailState previousState = currentState; // Remember previous state

    if (distance > followDistance) {
        // --- Determine Movement and State ---
        //isMoving = true;
        moveX = (dx / distance) * speed;
        moveY = (dy / distance) * speed;

        // Determine primary direction for animation state
        if (Math.abs(moveY) > Math.abs(moveX)) { // Moving mostly vertically
            currentState = (moveY < 0) ? SnailState.MOVING_UP : SnailState.MOVING_DOWN;
        } else { // Moving mostly horizontally
            currentState = (moveX < 0) ? SnailState.MOVING_LEFT : SnailState.MOVING_RIGHT;
        }

        // Apply movement (add wall collision here later if needed)
        x += moveX;
        y += moveY;

    } else {
        // Not moving, set state to Idle
        //isMoving = false;
        currentState = SnailState.IDLE;
    }

    // Reset animation if state changed
    if (previousState != currentState) {
        currentFrame = 0;
        animationTick = 0;
    }

    // --- Animate ---
    animationTick++;
    if (animationTick > animationSpeed) {
        animationTick = 0;
        List<BufferedImage> currentAnimation = getActiveAnimation();
        if (currentAnimation != null && !currentAnimation.isEmpty()) {
            currentFrame = (currentFrame + 1) % currentAnimation.size();
        }
    }
}

// Add this helper method
private List<BufferedImage> getActiveAnimation() {
    switch (currentState) {
        case MOVING_UP:     return walkUpFrames;
        case MOVING_DOWN:   return walkDownFrames; // Using idle frames for down
        case MOVING_LEFT:   return walkLeftFrames;
        case MOVING_RIGHT:  return walkRightFrames;
        case IDLE:
        default:            return idleFrames;
    }
}
    

    public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();
    try {
        // Draw glow effect first (code remains the same)
        // Inside Snail.java -> draw() method

// Draw the glow effect first (if glowing)
if (isGlowing) {
    Point center = new Point(getCenterX(), getCenterY());
    float[] dist = {0.0f, 1.0f};
    
    // --- USE the glowColor variable HERE ---
    Color[] colors = {this.glowColor, new Color(0, 0, 0, 0)}; // Use the field!
    
    RadialGradientPaint paint = new RadialGradientPaint(center, glowRadius, dist, colors);
    g2d.setPaint(paint);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.glowColor.getAlpha() / 255f)); // Use alpha from the field
    g2d.fillOval(getCenterX() - glowRadius, getCenterY() - glowRadius, glowRadius * 2, glowRadius * 2);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
}

        // Get the current animation frame
        List<BufferedImage> currentAnimation = getActiveAnimation();
        BufferedImage imageToDraw = null;
        if (currentAnimation != null && !currentAnimation.isEmpty()) {
            imageToDraw = currentAnimation.get(currentFrame % currentAnimation.size()); // Safe modulo
        }

        // Draw the snail sprite on top
        if (imageToDraw != null) {
            g2d.drawImage(imageToDraw, (int)x, (int)y, width, height, null);
        } else {
            // Failsafe drawing
            g2d.setColor(Color.CYAN);
            g2d.fillOval((int)x, (int)y, width, height);
        }
    } finally {
        g2d.dispose();
    }
}
@FunctionalInterface
interface TriFunction<T, U, V, W, R> {
    R apply(T t, U u, V v, W w);
}

    // Helper methods
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getCenterX() { return (int)x + width / 2; }
    public int getCenterY() { return (int)y + height / 2; }
    public boolean isCurrentlyGlowing() { return isGlowing; }
    public int getGlowRadius() { return isGlowing ? glowRadius : 0; }
}