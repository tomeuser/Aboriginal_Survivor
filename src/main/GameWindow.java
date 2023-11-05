package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

//GUI
public class GameWindow {
	private JFrame jframe;
	
	//Constructor 
	public GameWindow(GamePanel gamePanel) {
		
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close program if press EXIT
		jframe.add(gamePanel);
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null); // Window opens in the middle of the screen
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() { // resets KeyboardInput if user clicks on another software
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();		
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
			
	
		
	}
	
}
