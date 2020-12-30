package com.blazingduet.covsnake.gamestate;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MenuState extends GameState {

	private final static String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/menu/";
	
	private Image backgroundMenu, newGameBtn, highScoreBtn, hover_newGameBtn, hover_highScoreBtn;

	boolean isHoverA,isHoverB;
	
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
				
				//untuk newGameBtn
				if(e.getPoint().x >=250 && e.getPoint().x <=550 && e.getPoint().y >= 325 && e.getPoint().y <= 405){
					isHoverA = true;
					repaint();
				}else{
					isHoverA = false;
					repaint();
				};
				
				//untuk highScoreBtn
				if(e.getPoint().x >=250 && e.getPoint().x <=550 && e.getPoint().y >= 425 && e.getPoint().y <= 505){
					isHoverB = true;
					repaint();
				}else{
					isHoverB = false;
					repaint();
				};
			}
		});
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
			referred.setContentPane(new PlayState(referred));
			referred.validate();
			referred.getContentPane().requestFocusInWindow();
			break;
		}
	}

}
