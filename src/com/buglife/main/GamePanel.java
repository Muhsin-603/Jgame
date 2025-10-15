package src.com.buglife.main;

// Make sure to import your new Player class!
import src.com.buglife.entities.Player;
import src.com.buglife.entities.Spider;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;

// Your class declaration will look something like this
public class GamePanel extends JPanel {

    private Spider spider;

    // GamePanel no longer holds player x/y. It holds a Player object!
    private Player player;

    public GamePanel() {

        this.spider = new Spider(400, 300);
        // ... (your existing panel setup)
        setPreferredSize(new Dimension(1920, 1080));
        setFocusable(true);

        // Create an instance of the Player. Let's start it at (100, 100) with a size of 32x32.
        this.player = new Player(100, 100, 32);

        // Your key listener now talks to the Player object
        addKeyListener(new KeyInputAdapter());
    }

    // This method is called by your game loop to update game logic
    public void updateGame() {
        spider.update();
        player.update();

        

        if(player.getBounds().intersects(spider.getBounds())){

            System.out.println("Ouch spider....!");
            player.takeDamage(1);
        }
    }
    
    // The paintComponent method now tells the player to draw itself
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
    
        player.draw(g);
        spider.draw(g);

    // --- DRAW HUD HERE ---
    // Background of the health bar (the empty part)
        g.setColor(Color.DARK_GRAY);
        g.fillRect(10, 10, 200, 20); // x, y, width, height

    // The current health (the red part)
        g.setColor(Color.RED);
    // The width of this rectangle depends on the player's health!
        g.fillRect(10, 10, player.getHealth() * 2, 20); // health * 2 because 100 * 2 = 200 width

    // A border to make it look clean
        g.setColor(Color.WHITE);
        g.drawRect(10, 10, 200, 20);
    }

    // An inner class for handling key inputs. This is a clean way to do it.
    private class KeyInputAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W) player.movingUp = true;
            if (key == KeyEvent.VK_S) player.movingDown = true;
            if (key == KeyEvent.VK_A) player.movingLeft = true;
            if (key == KeyEvent.VK_D) player.movingRight = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_W) player.movingUp = false;
            if (key == KeyEvent.VK_S) player.movingDown = false;
            if (key == KeyEvent.VK_A) player.movingLeft = false;
            if (key == KeyEvent.VK_D) player.movingRight = false;
        }
    }
}