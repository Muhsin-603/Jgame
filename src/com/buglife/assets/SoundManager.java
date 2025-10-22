package src.com.buglife.assets;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
//import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private Map<String, Clip> soundClips; // Stores loaded sound effects

    public SoundManager() {
        soundClips = new HashMap<>();
        // Pre-load sounds you'll use often
        
        loadSound("eat", "/res/sounds/eat_sound.wav");
        loadSound("webbed", "/res/sounds/web_sound.wav");
        loadSound("gameOver", "/res/sounds/game_over.wav");
        loadSound("music", "/res/sounds/game_theme.wav");
        loadSound("struggle", "/res/sounds/struggle.wav");
        loadSound("menu", "/res/sounds/menu_selection.wav");
        // Load others as needed
    }

    public void loadSound(String name, String path) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(path);
            if (audioSrc == null) {
                System.err.println("Sound file not found: " + path);
                return;
            }
            // Use BufferedInputStream for better performance, especially with larger files
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundClips.put(name, clip);
            System.out.println("Loaded sound: " + name);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Error: Audio file format not supported: " + path + " - Use WAV format.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Error: Audio line unavailable.");
            e.printStackTrace();
        } catch (Exception e) { // Catch general IO exceptions too
            System.err.println("Error loading sound: " + path);
            e.printStackTrace();
        }
    }

    public void playSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null) {
            // Stop and reset the clip before playing again
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        } else {
            System.err.println("Sound not found: " + name);
        }
    }

    // Special method for looping background music
    public void loopSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Looping sound not found: " + name);
            // Try loading it now if not pre-loaded
            // loadSound(name, "/res/sounds/" + name + ".wav"); // Adjust path as needed
            // clip = soundClips.get(name);
            // if (clip != null) clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    // Method to stop a specific sound or all sounds
    public void stopSound(String name) {
         Clip clip = soundClips.get(name);
         if (clip != null && clip.isRunning()) {
             clip.stop();
         }
    }

    public void stopAllSounds() {
        for (Clip clip : soundClips.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
}