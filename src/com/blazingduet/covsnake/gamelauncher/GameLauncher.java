package com.blazingduet.covsnake.gamelauncher;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.blazingduet.covsnake.gamestate.GameState;
import com.blazingduet.covsnake.gamestate.MenuState;
import com.blazingduet.covsnake.music.MusicStuff;

public class GameLauncher {

	public static MusicStuff music = new MusicStuff();
	public final static String MUSIC_LOCATION = "src/com/blazingduet/covsnake/music/";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				JFrame frame = new JFrame("CovSnake");
				frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
				GameState gs = new MenuState(frame);
				((MenuState)gs).play();
				frame.setContentPane(gs);
				frame.setVisible(true);
				frame.pack();
				frame.setResizable(false);
			}
		}); 
	}

}
