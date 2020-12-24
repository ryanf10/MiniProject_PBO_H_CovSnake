package com.blazingduet.covsnake.gamestate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.blazingduet.covsnake.snake.Snake;

public class PlayState extends GameState {
	
	private static final String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/gameplay/";
	private static final int REFRESH_RATE = 30;
	
	private static Image header, map;
	
	private Snake snake;
	private int score;
	private int movementSpeedDelay, healthDecreaseSpeed;
	
	public PlayState(JFrame referred) {
		super(referred);
		header = loadImg("Header.png");
		map = loadImg("Map.png");
		this.score = 0;
		this.movementSpeedDelay = 5000;
		this.healthDecreaseSpeed = 1000;
		snake = new Snake();
		
		
		referred.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {
					snake.setDirection(true,false,false,false);
				}else if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
					snake.setDirection(false,false,true,false);
				}else if(e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
					snake.setDirection(false,true,false,false);
				}else if(e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
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

					try {
						Thread.sleep(healthDecreaseSpeed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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

	@Override
	public void stateChange(int state) {
		// TODO Auto-generated method stub
		
	}

}
