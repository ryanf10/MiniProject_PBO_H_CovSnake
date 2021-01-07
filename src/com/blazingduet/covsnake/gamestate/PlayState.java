package com.blazingduet.covsnake.gamestate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.blazingduet.covsnake.food.Apple;
import com.blazingduet.covsnake.food.Chicken;
import com.blazingduet.covsnake.food.Food;
import com.blazingduet.covsnake.food.Star;
import com.blazingduet.covsnake.obstacle.CoronaVirus;
import com.blazingduet.covsnake.obstacle.Obstacle;
import com.blazingduet.covsnake.obstacle.Stone;
import com.blazingduet.covsnake.snake.Snake;

public class PlayState extends GameState {
	public static final int HEADER_START_POSITION_X = 0, HEADER_START_POSITION_Y = 0;
	public static final int MAP_START_POSITION_X = 0, MAP_START_POSITION_Y = 80;
	public static final int GRASS_AREA_START_X = 80, GRASS_AREA_START_Y = 60, GRASS_AREA_WIDTH = 640, GRASS_AREA_HEIGHT = 400;
	
	private static final String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/gameplay/";
	private static final String FONT_LOCATION = "src/com/blazingduet/covsnake/resources/font/";
	private static final int REFRESH_RATE = 30;
	
	Cursor default_ = new Cursor(Cursor.DEFAULT_CURSOR);
	Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	Font kongtext;

	private static Image header, map, gameOverBanner,heart,multiplier,enterUsernameImg, okayButton, okayButtonHover;

	boolean isHover;
	
	private Snake snake;

	private int foodEatenBySnake, movementSpeedDelay, healthDecreaseDelay;
	private boolean isStarAlreadyOnMap, isGameOver;
	private List<Character> tempMoveInput;
	private List<Food> food;
	private List<Obstacle> obstacle;
	private float timeCounter;
	private JTextField username;
	
	public PlayState(JFrame referred) {
		super(referred);
		header = loadImg("Header.png");
		map = loadImg("Map.png");
		gameOverBanner = loadImg("GameOver.png");
		heart = loadImg("HP.png");
		enterUsernameImg = loadImg("EnterUsername.png");
		okayButton = loadImg("OKButton.png");
		okayButtonHover = loadImg("OKButtonHover.png");
		multiplier = loadImg("MultiplierActive.png");

		this.foodEatenBySnake = 0;
		this.movementSpeedDelay = 5000;
		this.healthDecreaseDelay = 1000;
		snake = new Snake();
		tempMoveInput = new ArrayList<>();
		food = Collections.synchronizedList(new ArrayList<>());
		obstacle = Collections.synchronizedList(new ArrayList<>());
		
		username = new JTextField();
		
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
		
		this.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseMoved(MouseEvent e) {
				
				setCursor(default_);
				isHover = false;
				repaint();
				if(e.getPoint().x >=560 && e.getPoint().x <=635 && e.getPoint().y >= 305 && e.getPoint().y <= 335){
					setCursor(hand);
					isHover = true;
					repaint();
				};
			}
		});
		
		Thread moveThread = new Thread() {
			@Override
			public void run() {
				while(snake.getHealthPoint()>0) {
					checkMove();
					snake.move();
					checkFood();
					checkObstacle();
					repaint();
					if(snake.getHealthPoint() <= 0) {
						break;
					}
					try {
						Thread.sleep(movementSpeedDelay/REFRESH_RATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					timeCounter += (float)movementSpeedDelay/(float)REFRESH_RATE; //tambah timeCounter
				}
				if(snake.getHealthPoint() <= 0) {
					if(!isGameOver) {
						addGameOverSection();
						isGameOver = true;
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
					if(!isGameOver) {
						addGameOverSection();
						isGameOver = true;
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
	
	private void addGameOverSection() {
		snake.setHealthPoint(0);
		username.setBounds(400,305,150,30);
		this.add(username);
		username.requestFocus();
		
		PlayState temp = this;
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getX() >= 560 && e.getX() <= 635 && e.getY() >= 305 && e.getY() <= 335) {
					if(username.getText().isEmpty()) {
						JOptionPane.showMessageDialog(temp, "The field is empty");
					}
					else if(username.getText().length() > 8) {
						JOptionPane.showMessageDialog(temp, "Maximum number of characters is 8");
					}
					else stateChange(0);
				}
			}
		});
		
		repaint();
	}
	
	private void changeDifficulty(){
		this.movementSpeedDelay -= 200;
		if(this.movementSpeedDelay < 2000) {
			this.movementSpeedDelay = 2000;
		}
		
		this.healthDecreaseDelay -= 40;
		if(this.healthDecreaseDelay < 800) {
			this.healthDecreaseDelay = 800;
		}
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
						//setiap ular makan 5 makanan
						
						//tambah kecepatan
						changeDifficulty();
						
						//tambah obstacle
						if(obstacle.size() < 50) {
							for(int i = 0; i < 5; i++) {
								obstacle.add(generateStone());
							}
						}
						
						//random kemunculan corona virus 40%
						Random rnd = new Random();
						int rndCoronaVirus = rnd.nextInt(101);
						if(rndCoronaVirus >= 60) {
							obstacle.add(generateCoronaVirus());
						}
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
					if(rndGenerateStar >= 70) {
						food.add(generateStar());
						isStarAlreadyOnMap = true;
					}
				}
				
				//generate chicken dengan chance 30%
				int rndGenerateChicken = rnd.nextInt(101);
				if(rndGenerateChicken >= 70) {
					food.add(generateChicken());
				}
	
			}
						
		}
	}
	
	private void checkObstacle() {
		synchronized(obstacle) {
			Iterator<Obstacle> it = obstacle.iterator();
			while(it.hasNext()) {
				Obstacle temp = it.next();
				if(temp.TouchedBySnake(snake)) {
					if(temp instanceof CoronaVirus) {
						it.remove();
					}
				}else if(temp instanceof CoronaVirus) {
					if(!((CoronaVirus)temp).isAvailable()) {
						it.remove();
					}
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
	
	private Stone generateStone() {
		Random rnd = new Random();
		int rndX,rndY;
		do {
			rndX = rnd.nextInt(GRASS_AREA_WIDTH/Obstacle.WIDTH_SIZE) * Obstacle.WIDTH_SIZE + MAP_START_POSITION_X + GRASS_AREA_START_X;
			rndY = rnd.nextInt(GRASS_AREA_HEIGHT/Obstacle.HEIGHT_SIZE) * Obstacle.HEIGHT_SIZE + MAP_START_POSITION_Y + GRASS_AREA_START_Y;
		}while(!isSpaceAvailable(rndX,rndY) || !(Math.abs(rndX-snake.getHeadX()) > 100 || Math.abs(rndY-snake.getHeadY()) > 100));
		
		return new Stone(rndX,rndY);
	}
	
	private CoronaVirus generateCoronaVirus() {
		Random rnd = new Random();
		int rndX,rndY;
		do {
			rndX = rnd.nextInt(GRASS_AREA_WIDTH/Obstacle.WIDTH_SIZE) * Obstacle.WIDTH_SIZE + MAP_START_POSITION_X + GRASS_AREA_START_X;
			rndY = rnd.nextInt(GRASS_AREA_HEIGHT/Obstacle.HEIGHT_SIZE) * Obstacle.HEIGHT_SIZE + MAP_START_POSITION_Y + GRASS_AREA_START_Y;
		}while(!isSpaceAvailable(rndX,rndY) || !(Math.abs(rndX-snake.getHeadX()) > 100 || Math.abs(rndY-snake.getHeadY()) > 100));
		
		return new CoronaVirus(rndX,rndY);
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
		
		synchronized (obstacle) {
			Iterator<Obstacle> it = obstacle.iterator();
			while(it.hasNext()) {
				Obstacle temp = it.next();				
				if(temp.getPositionX() == positionX && temp.getPositionY() == positionY) {	
					return false;
				}
			}
		}
		
		return true;
	}
	
	public int timeCounterHour() {
		return (int)(timeCounter/(60.00 * 60.00 * 1000.00));
	}
	
	public int timeCounterMinute() {
		return (int)(timeCounter/(60.00 * 1000.00)) - timeCounterHour() * 60;
	}
	
	public int timeCounterSecond() {
		return (int)(timeCounter/(1000.00)) - timeCounterMinute() * 60;
	}
	
	public void drawTimeCounter(Graphics g) {
		StringBuffer sb = new StringBuffer();
		if(this.timeCounterHour() >= 10) {
			sb.append(this.timeCounterHour()+":");
		}else {
			sb.append("0"+this.timeCounterHour()+":");
		}
		
		if(this.timeCounterMinute() >= 10) {
			sb.append(this.timeCounterMinute()+":");
		}else {
			sb.append("0"+this.timeCounterMinute()+":");
		}
		
		if(this.timeCounterSecond() >= 10) {
			sb.append(this.timeCounterSecond());
		}else {
			sb.append("0"+this.timeCounterSecond());
		}
		
		g.drawString(sb.toString(), 350, 45);
	}
	
	@Override
	public void render(Graphics g) {
		
		Color fontColor = new Color(247, 247, 240);
		
		g.drawImage(header, HEADER_START_POSITION_X, HEADER_START_POSITION_Y, null);
		
		try {
			kongtext = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_LOCATION + "kongtext.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("kongtext.ttf")));
		
		} catch (IOException | FontFormatException e) {
			// TODO: handle exception
		}
		
		g.setColor(fontColor);
		g.setFont(kongtext);
		g.drawString("Score: "+snake.getScore(), 70, 45);
		
		//untuk mencetak timeCounter ke layar
		this.drawTimeCounter(g);
		
		g.drawString("health: "+snake.getHealthPoint(), 600, 80);

		if(snake.isActiveMultiplier()) {
			g.drawImage(multiplier, 220, 14, null);
		}
		
		//warna HP bar

		g.setColor(Color.BLACK);
		g.drawRoundRect(600, 27, 120, 30, 25, 25);
		
		Color barBackgroundHP = new Color(242, 242, 237);
		g.setColor(barBackgroundHP);
		g.fillRoundRect(602, 28, 118, 29, 28, 28);
		
		Color barHP_green = new Color(65, 217, 65);
		Color barHP_yellow = new Color(235, 235, 52);
		Color barHP_red = new Color(232, 71, 53);
		
		if(snake.getHealthPoint() >= 54) {
			g.setColor(barHP_green);
		}
		else if(snake.getHealthPoint() >= 36 && snake.getHealthPoint() <= 53) {
			g.setColor(barHP_yellow);
		}
		else g.setColor(barHP_red);
		g.fillRoundRect(607, 28, snake.getHealthPointBar(), 29, 25, 25);
		
		g.drawImage(heart, 580, 20, null);
		g.drawImage(map, MAP_START_POSITION_X, MAP_START_POSITION_Y, null);
		
		synchronized (food) {
			Iterator<Food> it = food.iterator();
			while(it.hasNext()) {
				Food temp = it.next();				
				temp.render(g);
			}
		}
		
		synchronized (obstacle) {
			Iterator<Obstacle> it = obstacle.iterator();
			while(it.hasNext()) {
				Obstacle temp = it.next();				
				temp.render(g);
			}
		}
		
		snake.render(g);
		
		if(snake.getHealthPoint() <= 0) {
			g.drawImage(gameOverBanner, 0, 260, null);
			g.drawImage(enterUsernameImg, 100, 300, null);
			if(isHover) {
				g.drawImage(okayButtonHover, 560, 305, null);
			}
			else g.drawImage(okayButton, 560, 305, null);
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
		if(state == 0) {
			saveScore();
			referred.setContentPane(new MenuState(referred));
			referred.validate();
			referred.getContentPane().requestFocusInWindow();
		}
	}
	
	private void saveScore() {
		//untuk highscore berdasarkan jumlah skor
		List<User> userScorePodium = User.load("score.txt");
		User.setCompareType(0);
		if(userScorePodium == null) {
			userScorePodium = new ArrayList<>();
		}		
		userScorePodium.add(new User(snake.getScore(),this.timeCounter, username.getText()));		
		Collections.sort(userScorePodium);			
		User.save(userScorePodium, "score.txt");
		
		
		//untuk highscore berdasarkan survive time
		List<User> userSurviveTimePodium = User.load("time.txt");
		User.setCompareType(1);
		if(userSurviveTimePodium == null) {
			userSurviveTimePodium = new ArrayList<>();
		}		
		userSurviveTimePodium.add(new User(snake.getScore(), this.timeCounter, username.getText()));		
		Collections.sort(userSurviveTimePodium);
		User.save(userSurviveTimePodium, "time.txt");

	}
	
}



