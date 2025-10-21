package src.com.buglife.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import src.com.buglife.main.GamePanel; // To get screen dimensions


public class MainMenu {
    public String[] options = {"New Game", "Resume", "Quit"};
    public int currentSelection = 0;
    private BufferedImage backgroundImage;

    public MainMenu() {
    // ... any existing setup ...
        loadBackgroundImage("/res/sprites/ui/main_bg.png"); // Adjust path if needed
    }
    private void loadBackgroundImage(String path) {
    try {
        InputStream is = getClass().getResourceAsStream(path);
        if (is != null) {
            backgroundImage = ImageIO.read(is);
            is.close(); // Good practice to close the stream
        } else {
            System.err.println("Error: Main menu background image not found: " + path);
        }
    } catch (IOException e) {
        System.err.println("Error loading main menu background image: " + path);
        e.printStackTrace();
    }
}

    public void draw(Graphics g) {
    // 1. Draw the background image FIRST
    if (backgroundImage != null) {
        // Draw the image to fill the whole screen
        g.drawImage(backgroundImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
    } else {
        // Fallback: Dark overlay if image failed to load
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
    }

    // 2. Draw Title and Menu Options on TOP
    // You might need to adjust colors/positions to look good on the background!
    g.setColor(Color.GREEN); // Example: Green might show well
    g.setFont(new Font("Consolas", Font.BOLD, 90));
    String title = "BUGLIFE";
    int titleWidth = g.getFontMetrics().stringWidth(title);
    g.drawString(title, (GamePanel.SCREEN_WIDTH - titleWidth) / 2, 200);

    g.setFont(new Font("Consolas", Font.PLAIN, 40));
    for (int i = 0; i < options.length; i++) {
        if (i == currentSelection) {
            g.setColor(Color.YELLOW); // Highlight selected
        } else {
            g.setColor(Color.WHITE); // Normal option color
        }
        int optionWidth = g.getFontMetrics().stringWidth(options[i]);
        g.drawString(options[i], (GamePanel.SCREEN_WIDTH - optionWidth) / 2, 350 + i * 60);
    }
}

    public void moveUp() {
        currentSelection--;
        if (currentSelection < 0) {
            currentSelection = options.length - 1;
        }
    }

    public void moveDown() {
        currentSelection++;
        if (currentSelection >= options.length) {
            currentSelection = 0;
        }
    }
}