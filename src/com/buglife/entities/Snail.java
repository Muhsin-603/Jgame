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
    private int width = 31, height = 54; // Use exact dimensions
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
        BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/sprites/snail/snail.png"));

        // --- Animation Start Coordinates & Dimensions (From User) ---
        final int IDLE_START_X = 342;
        final int IDLE_START_Y = 246;
        final int IDLE_PADDING_X = 18;
        final int IDLE_WIDTH = 30;
        final int IDLE_HEIGHT = 53;

        final int RIGHT_START_X = 286;
        final int RIGHT_START_Y = 280;
        final int RIGHT_PADDING_X = 5;
        final int RIGHT_WIDTH = 43;
        final int RIGHT_HEIGHT = 54;

        final int LEFT_START_X = 290;
        final int LEFT_START_Y = 338;
        final int LEFT_PADDING_X = 5;
        final int LEFT_WIDTH = 44;
        final int LEFT_HEIGHT = 54;

        final int UP_START_X = 295;
        final int UP_START_Y = 395;
        final int UP_PADDING_X = 18;
        final int UP_WIDTH = 31;
        final int UP_HEIGHT = 55;

        // --- Number of Frames (Assuming 1 for idle, 3 for movement) ---
        final int IDLE_FRAMES = 1; // Adjust if idle has more frames
        final int MOVE_FRAMES = 3; // Adjust if move animations have different lengths

        // --- Helper to calculate X coord for subsequent frames ---
        // Usage: getX.apply(START_X, frameIndex, SPRITE_WIDTH, PADDING_X)
        TriFunction<Integer, Integer, Integer, Integer, Integer> getX =
            (startX, index, width, paddingX) -> startX + index * (width + paddingX);

        // --- THE DIGITAL SCISSORS (Using Absolute Coordinates) ---

        // Idle Frames
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idleFrames.add(spriteSheet.getSubimage(getX.apply(IDLE_START_X, i, IDLE_WIDTH, IDLE_PADDING_X), IDLE_START_Y, IDLE_WIDTH, IDLE_HEIGHT));
        }
        walkDownFrames.addAll(idleFrames); // Reuse idle for moving down

        // Walk Right Frames
        for (int i = 0; i < MOVE_FRAMES; i++) {
            walkRightFrames.add(spriteSheet.getSubimage(getX.apply(RIGHT_START_X, i, RIGHT_WIDTH, RIGHT_PADDING_X), RIGHT_START_Y, RIGHT_WIDTH, RIGHT_HEIGHT));
        }

        // Walk Left Frames
        for (int i = 0; i < MOVE_FRAMES; i++) {
            walkLeftFrames.add(spriteSheet.getSubimage(getX.apply(LEFT_START_X, i, LEFT_WIDTH, LEFT_PADDING_X), LEFT_START_Y, LEFT_WIDTH, LEFT_HEIGHT));
        }

        // Walk Up Frames
        for (int i = 0; i < MOVE_FRAMES; i++) {
            walkUpFrames.add(spriteSheet.getSubimage(getX.apply(UP_START_X, i, UP_WIDTH, UP_PADDING_X), UP_START_Y, UP_WIDTH, UP_HEIGHT));
        }

        System.out.println("Snail animations sliced based on exact coordinates!");

    } catch (Exception e) {
        System.err.println("CRASH! Could not slice snail sheet. Double-check coordinates/padding!");
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