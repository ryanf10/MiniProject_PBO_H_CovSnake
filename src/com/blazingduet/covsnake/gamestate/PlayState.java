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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;

import com.blazingduet.covsnake.food.Apple;
import com.blazingduet.covsnake.food.Chicken;
import com.blazingduet.covsnake.food.Food;
import com.blazingduet.covsnake.food.Star;
import com.blazingduet.covsnake.snake.Snake;

public class PlayState extends GameState {
	public static final int HEADER_START_POSITION_X = 0, HEADER_START_POSITION_Y = 0;
	public static final int MAP_START_POSITION_X = 0, MAP_START_POSITION_Y = 80;
	public static final int GRASS_AREA_START_X = 80, GRASS_AREA_START_Y = 60, GRASS_AREA_WIDTH = 640, GRASS_AREA_HEIGHT = 400;
	
	private static final String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/gameplay/";
	private static final int REFRESH_RATE = 30;
	
	private static Image header, map, gameOverBanner,continueBanner;
	
	private Snake snake;
	private int score, foodEatenBySnake;
	private int movementSpeedDelay, healthDecreaseDelay;
	private boolean isContinueBannerDrawn, isStarAlreadyOnMap;
	private List<Character> tempMoveInput;
	private List<Food> food;
	
	public PlayState(JFrame referred) {
		super(referred);
		header = loadImg("Header.png");
		map = loadImg("Map.png");
		gameOverBanner = loadImg("GameOver.png");
		continueBanner = loadImg("Continue.png");
		this.score = 0;
		this.foodEatenBySnake = 0;
		this.movementSpeedDelay = 5000;
		this.healthDecreaseDelay = 1000;
		snake = new Snake();
		tempMoveInput = new ArrayList<>();
		food = Collections.synchronizedList(new ArrayList<>());
		food.add(generateApple());
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == KeyEvent.VK_UP) {
					tempMoveInput.add('w');
				}else if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A' || e.getKeyCode() == KeyEvent.VK_LEFT) {
					tempMoveInput.add('a');
				}else if(e.getKeyChar() == 's' || e.getKeyChar() == 'S' || e.getKeyCode() == KeyEvent.VK_DOWN) {
					tempMoveInput.add('s');
				}else if(e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
					tempMoveInput.add('d');
				}
			}
		});
		this.setFocusable(true);
		
		Thread moveThread = new Thread() {
			@Override
			public void run() {
				while(snake.getHealthPoint()>0) {
					checkMove();
					snake.move();
					checkFood();
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
	
	private void checkMove() {
		if(tempMoveInput.size() > 0) {
			char key = tempMoveInput.get(0);
			tempMoveInput.remove(0);
			
			if(!snake.isMovingDown() && key == 'w') {
				snake.setDirection(true,false,false,false);
			}else if(!snake.isMovingRight() && key == 'a') {
				snake.setDirection(false,false,true,false);
			}else if(!snake.isMovingUp() && key == 's') {
				snake.setDirection(false,true,false,false);
			}else if(!snake.isMovingLeft() && key == 'd') {
				snake.setDirection(false,false,false,true);
			}
			
		}
	}

	
	
	private void addListenerGameOver() {
		this.removeKeyListener(this.getKeyListeners()[0]);
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				stateChange(0);
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
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
	
	private void checkFood() {
		//hapus food yang sudah termakan
		boolean doGenerateFood = false;
		
		synchronized (food) {
			Iterator<Food> it = food.iterator();
			while(it.hasNext()) {
				Food temp = it.next();
				if(temp.eatenBySnake(snake)) {
					this.foodEatenBySnake++;
					
					if(this.foodEatenBySnake % 5 == 0) {
						//generate 5 obstacle
					}
					
					if(temp instanceof Apple) {
						doGenerateFood = true;
					}else if(temp instanceof Star) {
						isStarAlreadyOnMap = false;
					}
					
					it.remove();
				}else if(temp instanceof Star) {
					if(!((Star)temp).isAvailable()) {
						it.remove();
						isStarAlreadyOnMap = false;
					}
				}else if(temp instanceof Chicken) {
					if(!((Chicken)temp).isAvailable()) {
						it.remove();
					}
				}
			}
			if(doGenerateFood) {
				//setelah apple dimakan, generate sebuah apple baru
				food.add(generateApple());
				
				Random rnd = new Random();
				
				//generate star dengan chance 30%
				if(!snake.isActiveMultiplier() && !isStarAlreadyOnMap) {
					int rndGenerateStar = rnd.nextInt(101);
					if(rndGenerateStar >= 0) {
						food.add(generateStar());
						isStarAlreadyOnMap = true;
					}
				}
				
				//generate chicken dengan chance 30%
				int rndGenerateChicken = rnd.nextInt(101);
				if(rndGenerateChicken >= 0) {
					food.add(generateChicken());
				}
	
			}
						
		}
	}
	
	private Apple generateApple() {
		Random rnd = new Random();
		int rndX,rndY;
		do {
			rndX = rnd.nextInt(GRASS_AREA_WIDTH/Food.WIDTH_SIZE) * Food.WIDTH_SIZE + MAP_START_POSITION_X + GRASS_AREA_START_X;
			rndY = rnd.nextInt(GRASS_AREA_HEIGHT/Food.HEIGHT_SIZE) * Food.HEIGHT_SIZE + MAP_START_POSITION_Y + GRASS_AREA_START_Y;
		}while(!isSpaceAvailable(rndX,rndY));
		
		return new Apple(rndX,rndY);
	}
	
	private Star generateStar() {
		Random rnd = new Random();
		int rndX,rndY;
		do {
			rndX = rnd.nextInt(GRASS_AREA_WIDTH/Food.WIDTH_SIZE) * Food.WIDTH_SIZE + MAP_START_POSITION_X + GRASS_AREA_START_X;
			rndY = rnd.nextInt(GRASS_AREA_HEIGHT/Food.HEIGHT_SIZE) * Food.HEIGHT_SIZE + MAP_START_POSITION_Y + GRASS_AREA_START_Y;
		}while(!isSpaceAvailable(rndX,rndY));
		
		return new Star(rndX,rndY);
	}
	
	private Chicken generateChicken() {
		Random rnd = new Random();
		int rndX,rndY;
		do {
			rndX = rnd.nextInt(GRASS_AREA_WIDTH/Food.WIDTH_SIZE) * Food.WIDTH_SIZE + MAP_START_POSITION_X + GRASS_AREA_START_X;
			rndY = rnd.nextInt(GRASS_AREA_HEIGHT/Food.HEIGHT_SIZE) * Food.HEIGHT_SIZE + MAP_START_POSITION_Y + GRASS_AREA_START_Y;
		}while(!isSpaceAvailable(rndX,rndY));
		
		return new Chicken(rndX,rndY,this);
	}
	
	public boolean isSpaceAvailable(int positionX, int positionY) {
		for(int i = 0; i < snake.getLength(); i++) {
			if(snake.getBodyX().get(i) == positionX && snake.getBodyY().get(i) == positionY) {
				return false;
			}
		}
		
		synchronized (food) {
			Iterator<Food> it = food.iterator();
			while(it.hasNext()) {
				Food temp = it.next();				
				if(temp.getPositionX() == positionX && temp.getPositionY() == positionY) {	
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(header, HEADER_START_POSITION_X, HEADER_START_POSITION_Y, null);
		g.drawString("Score: "+snake.getScore(),100, 40);
		g.drawString("HP: "+snake.getHealthPoint(),600, 40);
		
		g.drawImage(map, MAP_START_POSITION_X, MAP_START_POSITION_Y, null);
		
		synchronized (food) {
			Iterator<Food> it = food.iterator();
			while(it.hasNext()) {
				Food temp = it.next();				
				temp.render(g);
			}
		}
		
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

	@Override
	public void stateChange(int state) {
		switch(state) {
		case 0:
			referred.setContentPane(new MenuState(referred));
			referred.validate();
			referred.getContentPane().requestFocusInWindow();
			break;
		}
	}
	
	

}
