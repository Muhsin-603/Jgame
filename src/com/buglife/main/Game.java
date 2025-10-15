package src.com.buglife.main;

import javax.swing.*;

public class Game implements Runnable {

    private JFrame window;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 60; // Our target frames per second

    public Game() {
        // 1. Create the panel that will hold all our game objects
        gamePanel = new GamePanel();

        // 2. Create the main window (the JFrame)
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Buglife");

        // 3. Add our game panel to the window
        window.add(gamePanel);

        // 4. Size the window to fit the game panel's preferred size
        window.pack();

        // 5. Center the window on the screen and make it visible
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        // Let the panel listen for key presses
        gamePanel.requestFocus(); 
    }

    /**
     * Creates and starts the game thread. This is the "beating heart" of the game.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // This will automatically call the run() method
    }

    /**
     * This is the Game Loop. It will run continuously.
     */
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS; // Time for one frame in nanoseconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            // When delta reaches 1, it means enough time has passed to draw the next frame
            if (delta >= 1) {
                // 1. UPDATE: Update all game logic (character positions, etc.)
                gamePanel.updateGame();

                // 2. DRAW: Redraw the screen with the new information
                gamePanel.repaint();

                delta--;
            }
        }
    }

    /**
     * The main entry point for our application.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.startGameThread();
    }
}