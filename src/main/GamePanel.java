package main;

import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import java.awt.Dimension;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel{
	
	private MouseInputs mouseInputs;
	private Game game;

	public GamePanel(Game game) {
		//Set Control Inputs
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		this.addKeyListener(new KeyboardInputs(this));
		this.addMouseListener(mouseInputs);
		this.addMouseMotionListener(mouseInputs);	
	}
	
	// Sets dimension of the game window
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		this.setPreferredSize(size);
		System.out.print("size: "  + GAME_WIDTH + " : " + GAME_HEIGHT);
	}
	
	//Paint Game Character
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);		
		game.render(g);
	}
	
	public Game getGame() {
		return game;
	}	
}