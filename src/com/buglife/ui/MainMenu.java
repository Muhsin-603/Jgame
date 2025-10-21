package src.com.buglife.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

import src.com.buglife.main.Game;
import src.com.buglife.main.GamePanel; // To get screen dimensions


public class MainMenu {
    public String[] options = {"New Game", "Resume", "Quit"};
    public int currentSelection = 0;
    private BufferedImage backgroundImage;
    private BufferedImage titleimg;

    public MainMenu() {
        loadBackgroundImage("/res/sprites/ui/main_bg.png"); //image location
        loadTitle("/res/sprites/ui/logo.png");
    }
    
    private void loadTitle(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                titleimg = ImageIO.read(is);
                is.close();
            } else {
                System.err.println("Logo adichu poyi guys");
            }
        } catch (IOException e) {
            System.err.println("Logo adichu poyi guys");
            e.printStackTrace();
        }
    }
    private void loadBackgroundImage(String path) {
    try {
        InputStream is = getClass().getResourceAsStream(path);
        if (is != null) {
            backgroundImage = ImageIO.read(is);
            is.close();
        } else {
            System.err.println("Error: myr image kanunnilla : " + path);
        }
    } catch (IOException e) {
        System.err.println("image kittan task annu onnude poyi sheriyakku: " + path);
        e.printStackTrace();
    }
}

    private static final int TITLE_WIDTH = 552;  //title width and height
    private static final int TITLE_HEIGHT = 160;
    public void draw(Graphics g) {
    // 1. Draw the background image FIRST
    if (backgroundImage != null) {
        g.drawImage(backgroundImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
    } else {
        // Fallback: Dark overlay if image failed to load
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
    }

    if (titleimg != null) {
        int titleX = (GamePanel.SCREEN_WIDTH - TITLE_WIDTH) / 2; // screen width - title width 
        int titleY = 100;
        g.drawImage(titleimg, titleX, titleY, TITLE_WIDTH, TITLE_HEIGHT, null);
    } else {
        Font titleFont = Game.Tiny5 != null ? Game.Tiny5.deriveFont(Font.BOLD, 90)
                : new Font("Consolas", Font.BOLD, 90);
        g.setFont(titleFont);
        g.setColor(Color.GREEN);
        String title = "BUGLIFE";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (GamePanel.SCREEN_WIDTH - titleWidth) / 2, 200);
    }
    Font optionFont;

    if (Game.Tiny5 != null) {
        // Use the custom font if loaded successfully
        optionFont = Game.Tiny5.deriveFont(Font.PLAIN, 40);
    } else {
        // Use a default fallback font if custom font failed
        System.err.println("MainMenu: Custom font not loaded, using fallback."); // Optional warning
        optionFont = new Font("Consolas", Font.PLAIN, 40);
    }
   // 3. Draw Menu Options using the selected font
    g.setFont(optionFont);
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