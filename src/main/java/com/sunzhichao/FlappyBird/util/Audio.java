package com.sunzhichao.FlappyBird.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Audio {

    private Clip clip;

    private void playSound(String sound) {
        // Path to sound file
        String soundURL = "/resources/music/" + sound + ".wav";
        System.out.println("Trying to load sound from: " + soundURL);
        // Try to load and play sound
        try {
            URL url = getClass().getResource(soundURL);
            if (url != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.out.printf("Could not load music %s.wav!\n", sound);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Could not load sound  %s.wav!\n", sound);
        }
    }

    /**
     * Public method for bird jump sound
     */
    public void jump() {
        playSound("flap");
    }

    /**
     * Public method for point sound
     */
    public void point() {
        playSound("score");
    }

    /**
     * Public method for collision/death sound
     */
    public void hit() {
        playSound("crash");
    }
}
