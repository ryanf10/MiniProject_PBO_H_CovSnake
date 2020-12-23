package com.blazingduet.engine;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new JFrame("CovSnake");
				frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

				frame.setVisible(true);
				frame.pack();
				
			}
		}); 
	}

}
