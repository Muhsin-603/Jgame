package src.com.buglife.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import src.com.buglife.main.GamePanel; // To get screen dimensions

public class MainMenu {
    private BufferedImage backgroundImage; //declared bg
    public String[] options = {"New Game", "Resume", "Quit"};
    public int currentSelection = 0;
    
    public MainMenu() { //oru main menu interface mode akki
        try {
            backgroundImage = ImageIO.read(new File("res/img/main_bg.png")); //njan ivide img add akkittind
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }
    }

    public void draw(Graphics g) {
        if (backgroundImage != null) { //image added to graphics
            g.drawImage(backgroundImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        } 
        else { //athava paliyal ithu upagarapedum
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
        }
        // Title
        g.setColor(Color.GREEN);
        g.setFont(new Font("Consolas", Font.BOLD, 90));
        String title = "BUGLIFE";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (GamePanel.SCREEN_WIDTH - titleWidth) / 2, 200);

        // Menu Options
        g.setFont(new Font("Consolas", Font.PLAIN, 40));
        for (int i = 0; i < options.length; i++) {
            if (i == currentSelection) {
                g.setColor(Color.YELLOW); // Highlight the selected option
            } else {
                g.setColor(Color.WHITE);
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