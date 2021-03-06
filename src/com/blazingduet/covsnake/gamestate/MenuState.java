package com.blazingduet.covsnake.gamestate;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.blazingduet.covsnake.gamelauncher.GameLauncher;
import com.blazingduet.covsnake.music.MusicStuff;

public class MenuState extends GameState {

	private final static String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/menu/";
	private final static String MUSIC_LOCATION = "src/com/blazingduet/covsnake/music/";
			
	private Image backgroundMenu, newGameBtn, highScoreBtn, hover_newGameBtn, hover_highScoreBtn;

	boolean isHoverA,isHoverB;
	
	Cursor default_ = new Cursor(Cursor.DEFAULT_CURSOR);
	Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	public MenuState(JFrame referred) {
		super(referred);
		backgroundMenu = this.loadImg("BackgroundMenu.png");
		newGameBtn = this.loadImg("NewGame.png");
		highScoreBtn = this.loadImg("HighScore.png");
		hover_newGameBtn = this.loadImg("NewGameHover.png");
		hover_highScoreBtn = this.loadImg("HighScoreHover.png");
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//untuk newGameBtn
				if(e.getPoint().x >=250 && e.getPoint().x <=550 && e.getPoint().y >= 325 && e.getPoint().y <= 405){
					stateChange(1);
				};
				
				//untuk highScoreBtn
				if(e.getPoint().x >=250 && e.getPoint().x <=550 && e.getPoint().y >= 425 && e.getPoint().y <= 505){
					stateChange(2);
				};
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
				setCursor(default_);
				//untuk newGameBtn
				if(e.getPoint().x >=250 && e.getPoint().x <=550 && e.getPoint().y >= 325 && e.getPoint().y <= 405){
					setCursor(hand);
					isHoverA = true;
					repaint();
				}else{
					isHoverA = false;
					repaint();
				};
				
				//untuk highScoreBtn
				if(e.getPoint().x >=250 && e.getPoint().x <=550 && e.getPoint().y >= 425 && e.getPoint().y <= 505){
					setCursor(hand);
					isHoverB = true;
					repaint();
				}else{
					isHoverB = false;
					repaint();
				};
			}
		});
	}	

	public void play() {
		GameLauncher.music.playMusic(MUSIC_LOCATION + "MenuStateMusic.wav");
	}
	
	private Image loadImg(String filename) {
		try {
			return ImageIO.read(new File(DEFAULT_LOCATION+filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(backgroundMenu, 0, 0, null);
		if(isHoverA) {
			g.drawImage(hover_newGameBtn, 250, 325, null);
			g.drawImage(highScoreBtn, 250, 425, null);
		}
		else if(isHoverB) {
			g.drawImage(hover_highScoreBtn, 250, 425, null);
			g.drawImage(newGameBtn, 250, 325, null);
		}
		else {
			g.drawImage(newGameBtn, 250, 325, null);
			g.drawImage(highScoreBtn, 250, 425, null);
		}
	}

	@Override
	public void stateChange(int state) {
		switch(state) {
		case 1:
			GameLauncher.music.stopAll();
			referred.setContentPane(new PlayState(referred));
			referred.validate();
			referred.getContentPane().requestFocusInWindow();
			break;
		case 2:
			referred.setContentPane(new HighScoreState(referred));
			referred.validate();
			referred.getContentPane().requestFocusInWindow();
		}
	}

}
