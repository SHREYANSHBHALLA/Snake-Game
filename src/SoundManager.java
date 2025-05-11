import javax.sound.sampled.*;
import java.io.*;

public class SoundManager {
    private static final String SOUNDS_FOLDER = "/sounds/";
    
    public static void playSound(String filename) {
        try {
            InputStream audioSrc = SoundManager.class.getResourceAsStream(SOUNDS_FOLDER + filename);
            if (audioSrc == null) {
                System.err.println("Sound file not found: " + filename);
                return;
            }
            
            // Create temp file because AudioSystem requires a File
            File tempFile = File.createTempFile("sound", ".wav");
            tempFile.deleteOnExit();
            
            try (OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = audioSrc.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(tempFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}