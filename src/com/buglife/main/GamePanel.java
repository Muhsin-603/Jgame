package src.com.buglife.main;

import src.com.buglife.assets.SoundManager;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Color;
 

public class GamePanel extends JPanel {
    public static final int VIRTUAL_WIDTH = 1366;
    public static final int VIRTUAL_HEIGHT = 768;

    private GameStateManager stateManager;
    private SoundManager soundManager;

    public GamePanel(SoundManager sm) {
        this.soundManager = sm;
        this.stateManager = new GameStateManager(soundManager, this);

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        setFocusable(true);
        addKeyListener(new KeyInputAdapter());
    }

    public void updateGame() {
        stateManager.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // --- SCALING CALCULATIONS ---
        int realScreenWidth = getWidth();
        int realScreenHeight = getHeight();

        double scaleX = (double) realScreenWidth / VIRTUAL_WIDTH;
        double scaleY = (double) realScreenHeight / VIRTUAL_HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        int scaledWidth = (int) (VIRTUAL_WIDTH * scale);
        int scaledHeight = (int) (VIRTUAL_HEIGHT * scale);

        int xOffset = (realScreenWidth - scaledWidth) / 2;
        int yOffset = (realScreenHeight - scaledHeight) / 2;

        // --- APPLY TRANSFORMATION ---
        g2d.translate(xOffset, yOffset);
        g2d.scale(scale, scale);

        // --- DELEGATE TO STATE MANAGER ---
        stateManager.draw(g2d);

        g2d.dispose();
    }

    private class KeyInputAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            stateManager.keyPressed(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            stateManager.keyReleased(e.getKeyCode());
        }
    }
}