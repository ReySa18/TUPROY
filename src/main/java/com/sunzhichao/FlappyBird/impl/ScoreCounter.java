package com.sunzhichao.FlappyBird.impl;

import com.sunzhichao.FlappyBird.util.Constant;

import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Score counter class
 * Since ScoreCounter is globally unique and cannot have more than one,
 * the Singleton Pattern is used
 * Simply put, the Singleton Pattern is to ensure that a class has only one instance
 * and provide a global access method to access it.
 * @author sunzhichao
 */
public class ScoreCounter {

    private static class ScoreCounterHolder{
        private static final ScoreCounter scoreCounter = new ScoreCounter();
    }
    public static ScoreCounter getInstance(){
        return ScoreCounterHolder.scoreCounter;
    }

    private long score = 0;
    private long bestScore;
    private Clip scoreSound;

    /**
     * private Constructor
     */
    private ScoreCounter(){
        bestScore = -1;
        try {
            // Muat file audio score
            File soundFile = new File("src\\main\\resources\\music\\point.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            scoreSound = AudioSystem.getClip();
            scoreSound.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            loadBestScore();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Read the highest score from the file and put it in the bestScore variable
     */
    private void loadBestScore() throws IOException {
        File file = new File(Constant.SCORE_FILE_PATH);
        if(file.exists()){
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            bestScore = dis.readLong();
            dis.close();
        }
    }

    /**
     * Save the highest score in a file
     */
    public void saveScore(){
        bestScore = Math.max(bestScore,getCurrentScore());
        try {
            File file = new File(Constant.SCORE_FILE_PATH);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeLong(bestScore);
            dos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * score add one
     * @param bird bird instance
     */
    public void score(Bird bird){
        if(!bird.isDead()){
            score += 1;
            playScoreSound();
        }
    }

    public long getBestScore(){
        return bestScore;
    }

    public long getCurrentScore(){
        return score;
    }

    public void reset(){
        score = 0;
    }

    public void resetBestScore(){
        bestScore = 0; 
        saveScore();
    }

    private void playScoreSound() {
        if (scoreSound != null && !scoreSound.isRunning()) {
            scoreSound.setFramePosition(0); // Putar dari awal
            scoreSound.start(); // Mulai memainkan suara
        }
    }
}
