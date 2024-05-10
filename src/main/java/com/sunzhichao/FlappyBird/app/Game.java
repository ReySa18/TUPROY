package com.sunzhichao.FlappyBird.app;

import com.sunzhichao.FlappyBird.impl.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import static com.sunzhichao.FlappyBird.util.Constant.*;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The main logic of the game, used to control the game flow
 */
public class Game extends Frame {

    private static int gameState;
    private GameBackground background;
    private GameDardBackground darkbackground;
    private CloudControl cloudControl;
    private Bird bird;
    private PipeControl pipeControl;
    private WelcomeAnimation welcomeAnimation;
 


    /**
     * Constructor
     */
    public Game() {
        initFrame();
        setVisible(true);//The window is invisible by default, set to visible
        initGame();
    }
    

    /**
     * Initialize the game frame
     */
    private void initFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("Flappy Bird Kelompok 5");
        setLocationRelativeTo(null); // memposisikan windows program ke tengah
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // ScoreCounter.getInstance().saveScore(); // Save the best score before 
                ScoreCounter.getInstance().resetBestScore(); // Set the best score to 0 when closing the window
                System.exit(0);//End the program
            }
        });
        addKeyListener(new BirdKeyListener());
    }

    /**
     * Initialize the game object
     */
    private void initGame() {
        background = new GameBackground();
        darkbackground = new GameDardBackground();
        cloudControl = new CloudControl();
        pipeControl = new PipeControl();
        welcomeAnimation = new WelcomeAnimation();
        bird = new Bird();
        setGameState(GAME_READY);

        //Start a new thread to refresh the window
        new Thread(() -> {
            while (true) {
                repaint();//The system will call the update method
                try {
                    Thread.sleep(FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final BufferedImage bufImg = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

    public void update(Graphics g) {
        Graphics bufG = bufImg.getGraphics();//Get “picture brush”
        //Use the picture brush to draw the content that needs to be drawn to the picture
        if (ScoreCounter.getInstance().getCurrentScore() >= 3) {
            darkbackground.draw(bufG, bird);
        } else {
            background.draw(bufG, bird);
        }
        cloudControl.draw(bufG, bird);
        if (gameState == GAME_READY) {
            welcomeAnimation.draw(bufG);
        } else {
            pipeControl.draw(bufG, bird);
        }
        bird.draw(bufG);
        g.drawImage(bufImg, 0, 0, null);//Draw the picture on the screen at once
    }
    

    public static void setGameState(int gameState) {
        Game.gameState = gameState;
    }

    class BirdKeyListener implements KeyListener {

        /**
         * Press the button, call different methods
         * according to the current state of the game
         */

         private Clip spaceClip; //sound

        public BirdKeyListener() {
            try {
                File file = new File("src\\main\\resources\\music\\wing.wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                spaceClip = AudioSystem.getClip();
                spaceClip.open(audioInputStream);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyChar();
            switch (gameState) {
                case GAME_READY:
                    if(keycode ==KeyEvent.VK_SPACE){
                        bird.birdFlap();
                        bird.birdFall();
                        setGameState(GAME_START);

                        if (spaceClip != null) {
                            spaceClip.stop();
                            spaceClip.setFramePosition(0);
                            spaceClip.start();
                        }
                    }
                    break;
                case GAME_START:
                    if (keycode ==KeyEvent.VK_SPACE){
                        bird.birdFlap();
                        bird.birdFall();

                        if (spaceClip != null) {
                            spaceClip.stop();
                            spaceClip.setFramePosition(0);
                            spaceClip.start();
                        }
                    }
                    break;
                case STATE_OVER:
                    if(keycode == KeyEvent.VK_SPACE){
                        resetGame();
                    }
                    break;
            }
        }

        private void resetGame(){
            setGameState(GAME_READY);
            pipeControl.reset();
            bird.reset();
        }

        


        /**
         * When the space button is released,
         * the state needs to be changed manually
         */
        @Override
        public void keyReleased(KeyEvent e) {
            int keycode = e.getKeyChar();
            if(keycode == KeyEvent.VK_SPACE){
                bird.keyReleased();
            }
        }

        /**
         * When other buttons are pressed,no action happens
         */
        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}