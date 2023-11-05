package levels;
import javax.swing.*;

import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

public class TimerApp {

    private static final int WIDTH = (int)(120 * Game.SCALE);
    private static final int HEIGHT = (int)(110 * Game.SCALE);
    private static final int COUNTDOWN_DURATION = 30; // Countdown-Dauer in Sekunden
    private int remainingSeconds;
    private Timer timer;
    private boolean paused = false; // Variable zum Verfolgen des Pausenstatus
    private boolean active = false;

    public TimerApp(Playing playing) {
        remainingSeconds = COUNTDOWN_DURATION;
        
        // Timer, der jede Sekunde aktualisiert wird
         timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingSeconds > 0) {
                	if(!paused && active)
                		remainingSeconds--;
                } else {
                	playing.setGameOver(true);
                	playing.getPlayer().setActive(false);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    
    public void drawTimer(Graphics g) {

    	  try {
              InputStream fontStream = TimerApp.class.getResourceAsStream("/VT323-Regular.ttf");
              Font pixelartFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
              Font pixelartFontSized = pixelartFont.deriveFont(Font.BOLD, 44f * Game.SCALE);
              g.setFont(pixelartFontSized);
          } catch (Exception e) {
              // Fallback, falls die Schriftart nicht geladen werden kann
        	  System.out.println("Failed");
              g.setFont(new Font("Arial", Font.PLAIN, 24));
          }

        g.setColor(Color.BLACK);
        //g.setFont(new Font("Arial", Font.PLAIN, 24));
        String timeText = remainingSeconds + "s";
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(timeText);
        int x = (WIDTH - textWidth) / 2;
        int y = HEIGHT / 2;
        g.drawString(timeText, x, y);
    }
    
    public void resetTimer() {
		active = false;
		paused = false;
    	remainingSeconds = COUNTDOWN_DURATION;
        timer.start();
    }

	public void setActive(boolean b) {
		active = b;
	}
	
	public void setPaused(boolean b) {
		paused = b;
	}
	
	public void update() {
		setActive(true);
	}

}


