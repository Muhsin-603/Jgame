package src.com.buglife.entities; // <-- 1. "src." REMOVED

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
    private int width = 35; // Use the Idle dimensions
    private int height = 55;
    
    // --- Simplified Animation ---
    private List<BufferedImage> idleFrames;
    private int currentFrame = 0;
    private int animationTick = 0;
    private int animationSpeed = 20; // Snails are slow
    private Player player;  // Reference to the player

    // --- State ---
    private boolean isVisible = true;

    // --- Glow effect ---
    private int glowRadius = 50;
    private Color glowColor = new Color(255, 255, 150, 60);

    // Add new fields for NPC behavior
    private static final int INTERACTION_RADIUS = 50;
    private String[] dialogues = {
        "Hello little one...",
        "Be careful of the spiders!",
        "The shadows might help you hide...",
        "Follow my light, it will guide you."
    };
    private int currentDialogue = 0;
    //private boolean isInteracting = false;
    private boolean showingDialog = false;

    // --- 2. THE NEW, SIMPLER CONSTRUCTOR ---
    public Snail(int startX, int startY, Player player) {
        this.x = startX;
        this.y = startY;
        this.player = player;
        this.isVisible = true; // Explicitly set visibility
        loadAnimations(); // Make sure animations are loaded
    }

    private void loadAnimations() {
        idleFrames = new ArrayList<>();
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/sprites/snail/snail.png"));
            if (spriteSheet == null) {
                System.err.println("Failed to load snail sprite sheet!");
                return;
            }

            // --- Slicing ONLY the Idle Animation (from 143x224 sheet) ---
            final int IDLE_WIDTH = 35, IDLE_HEIGHT = 55;
            final int IDLE_START_X = 0; // Top-left of sheet
            final int IDLE_START_Y = 0;
            final int IDLE_H_STEP = 35 + 17; // 52
            final int IDLE_FRAMES = 3; // The first row has 3 frames

            for (int i = 0; i < IDLE_FRAMES; i++) {
                int frameX = IDLE_START_X + i * IDLE_H_STEP;
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
        if (!isVisible) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Draw glow effect
            g2d.setColor(glowColor);
            g2d.fillOval((int)x - glowRadius, (int)y - glowRadius, 
                         glowRadius * 2, glowRadius * 2);

            // Draw snail sprite
            if (idleFrames != null && !idleFrames.isEmpty()) {
                BufferedImage currentSprite = idleFrames.get(currentFrame);
                g2d.drawImage(currentSprite, (int)x, (int)y, width, height, null);
            } else {
                // Debug rectangle if sprite fails to load
                g2d.setColor(Color.MAGENTA);
                g2d.fillRect((int)x, (int)y, width, height);
            }
        } finally {
            g2d.dispose();
        }
    }

    // --- 3. THE MISSING METHODS ---
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        System.out.println("Snail position set to: " + x + ", " + y);
    }
    public boolean isVisible() { return this.isVisible; }
    public void hide() { this.isVisible = false; }
    public void show() {
        this.isVisible = true;
        System.out.println("Snail is now visible at: " + x + ", " + y);
    }
    
    
    // Helper methods
    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
    public int getWidth() { return width; }   // <-- Missing method
    public int getHeight() { return height; } // <-- Missing method
    public int getCenterX() { return (int)x + width / 2; }
    public int getCenterY() { return (int)y + height / 2; }

    // Add new method for interaction check
    public boolean canInteract(Player player) {
        double dx = player.getCenterX() - getCenterX();
        double dy = player.getCenterY() - getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= INTERACTION_RADIUS;
    }

    // Add dialogue methods
    public void interact() {
        if (!showingDialog) {
            showingDialog = true;
            currentDialogue = (currentDialogue + 1) % dialogues.length;
        }
    }

    public void closeDialog() {
        showingDialog = false;
    }

    // Add new method for drawing dialogue box
    private void drawDialogBox(Graphics2D g2d) {
        int boxWidth = 300;
        int boxHeight = 80;
        int boxX = getCenterX() - boxWidth/2;
        int boxY = getY() - boxHeight - 20;

        // Draw dialogue box background
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        // Draw text
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        drawWrappedText(g2d, dialogues[currentDialogue], boxX + 10, boxY + 30, boxWidth - 20);

        // Draw "press E" prompt
        g2d.setFont(new Font("Arial", Font.ITALIC, 12));
        g2d.drawString("Press E to continue", boxX + boxWidth - 100, boxY + boxHeight - 10);
    }

    // Add new method for drawing interaction prompt
    private void drawInteractionPrompt(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String prompt = "Press E to talk";
        int promptWidth = g2d.getFontMetrics().stringWidth(prompt);
        g2d.drawString(prompt, getCenterX() - promptWidth/2, getY() - 10);
    }

    // Helper method for text wrapping
    private void drawWrappedText(Graphics2D g2d, String text, int x, int y, int maxWidth) {
        FontMetrics fm = g2d.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;

        for (String word : words) {
            if (fm.stringWidth(line + word) < maxWidth) {
                line.append(word).append(" ");
            } else {
                g2d.drawString(line.toString(), x, lineY);
                line = new StringBuilder(word + " ");
                lineY += fm.getHeight();
            }
        }
        g2d.drawString(line.toString(), x, lineY);
    }
}