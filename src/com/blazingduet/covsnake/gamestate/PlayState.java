package com.blazingduet.covsnake.gamestate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.blazingduet.covsnake.snake.Snake;

public class PlayState extends GameState {
	
	private static final String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/gameplay/";
	private static final int REFRESH_RATE = 30;
	
	private static Image header, map, gameOverBanner,continueBanner;
	
	private Snake snake;
	private int score;
	private int movementSpeedDelay, healthDecreaseDelay;
	private boolean isContinueBannerDrawn;
	
	public PlayState(JFrame referred) {
		super(referred);
		header = loadImg("Header.png");
		map = loadImg("Map.png");
		gameOverBanner = loadImg("GameOver.png");
		continueBanner = loadImg("Continue.png");
		this.score = 0;
		this.movementSpeedDelay = 5000;
		this.healthDecreaseDelay = 100;
		snake = new Snake();
		
		
		referred.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(!snake.isMovingDown() && (e.getKeyChar() == 'w' || e.getKeyChar() == 'W')) {
					snake.setDirection(true,false,false,false);
				}else if(!snake.isMovingRight() && (e.getKeyChar() == 'a' || e.getKeyChar() == 'A')) {
					snake.setDirection(false,false,true,false);
				}else if(!snake.isMovingUp() && (e.getKeyChar() == 's' || e.getKeyChar() == 'S')) {
					snake.setDirection(false,true,false,false);
				}else if(!snake.isMovingLeft() && (e.getKeyChar() == 'd' || e.getKeyChar() == 'D')) {
					snake.setDirection(false,false,false,true);
				}
			}
		});
		
		Thread moveThread = new Thread() {
			@Override
			public void run() {
				while(snake.getHealthPoint()>0) {
					snake.move();
					repaint();
					try {
						Thread.sleep(movementSpeedDelay/REFRESH_RATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		moveThread.start();
		
		Thread healthDecreaseThread = new Thread() {
			@Override
			public void run() {
				while(snake.getHealthPoint()>0) {
					snake.setHealthPoint(snake.getHealthPoint()-2);
					if(snake.getHealthPoint() <= 0) {
						break;
					}
					try {
						Thread.sleep(healthDecreaseDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if(snake.getHealthPoint() <= 0) {
					snake.setHealthPoint(0);
					addListenerGameOver();
					makeContinueBannerAnimation();
				}
			}
		};
		healthDecreaseThread.start();		
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
		g.drawImage(header, 0, 0, null);
		g.drawString("HP: "+snake.getHealthPoint(),600, 40);
		
		g.drawImage(map, 0, 80, null);
		
		snake.render(g);
		
		if(snake.getHealthPoint() <= 0) {
			g.drawImage(gameOverBanner, 0, 260, null);
			if(isContinueBannerDrawn) {
				g.drawImage(continueBanner, 0, 300, null);
			}
		}
		
		//map & header border
		Graphics2D g2 = (Graphics2D) g;
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(4));
		Color borderColor = new Color(102,51,0);
		g2.setColor(borderColor);
		g2.drawRect(2, 2, 796, 78);
		g2.drawRect(2, 82, 796, 516);
		g2.setStroke(oldStroke);
		
		
	}
	
	private void addListenerGameOver() {
		referred.removeKeyListener(referred.getKeyListeners()[0]);
		referred.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				stateChange(0);
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				stateChange(0);
			}
		});
	}
	
	private void makeContinueBannerAnimation() {
		Thread continueAnimationThread = new Thread() {
			@Override
			public void run() {
				while(true) {
					isContinueBannerDrawn = true;
					repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					isContinueBannerDrawn = false;
					repaint();
					try {
						Thread.sleep(1000);						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		continueAnimationThread.start();
	}

	@Override
	public void stateChange(int state) {
		switch(state) {
		case 0:
			referred.removeKeyListener(referred.getKeyListeners()[0]);
			referred.setContentPane(new MenuState(referred));
			referred.validate();
			break;
		}
	}
	
	

}
