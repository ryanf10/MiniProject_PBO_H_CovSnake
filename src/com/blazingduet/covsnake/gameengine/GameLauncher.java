package com.blazingduet.covsnake.gameengine;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.blazingduet.covsnake.gamestate.GameState;
import com.blazingduet.covsnake.gamestate.MenuState;

public class GameLauncher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new JFrame("CovSnake");
				frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
				GameState gs = new MenuState(frame);
				frame.setContentPane(gs);
				frame.setVisible(true);
				frame.pack();
				frame.setResizable(false);
			}
		}); 
	}

}
