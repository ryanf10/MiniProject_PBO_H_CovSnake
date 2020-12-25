package com.blazingduet.covsnake.snake;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.blazingduet.covsnake.movable.Movable;

public class Snake implements Movable {
	private static final String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/snake/";
	private static final int MAX_HEALTH_POINT = 100;
	private static Image headUp, headDown, headLeft, headRight, body;
	
	private boolean isMovingUp, isMovingDown, isMovingLeft, isMovingRight;
	private List<Integer> bodyX;
	private List<Integer> bodyY;
	private int length,HealthPoint,score;
	
	public Snake() {
		this.bodyX = new ArrayList<>();
		this.bodyY = new ArrayList<>();
	
		this.HealthPoint = 100;
				
		//init snake dengan length = 3
		this.length = 3;
		
		this.bodyX.add(240);
		this.bodyY.add(200);
		
		this.bodyX.add(220);
		this.bodyY.add(200);
		
		this.bodyX.add(200);
		this.bodyY.add(200);
		
		this.setDirection(false, false, false, true);
		
		headUp = this.loadImg("HeadUp.png");
		headDown = this.loadImg("HeadDown.png");
		headLeft = this.loadImg("HeadLeft.png");
		headRight = this.loadImg("HeadRight.png");
		body = this.loadImg("Body.png");
		
	}
	
	public void setDirection(boolean isMovingUp, boolean isMovingDown, boolean isMovingLeft, boolean isMovingRight) {
		this.isMovingUp = isMovingUp;
		this.isMovingDown = isMovingDown;
		this.isMovingLeft = isMovingLeft;
		this.isMovingRight = isMovingRight;
	}
	
	private Image loadImg(String filename) {
		try {
			return ImageIO.read(new File(DEFAULT_LOCATION+filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void render(Graphics g) {
		for(int i = 1; i < this.length; i++) {
			g.drawImage(body, bodyX.get(i), bodyY.get(i), null);
		}
		
		if(isMovingUp) {
			g.drawImage(headUp, bodyX.get(0),bodyY.get(0) ,null);
		}else if(isMovingDown) {
			g.drawImage(headDown, bodyX.get(0),bodyY.get(0) ,null);
		}else if(isMovingLeft) {
			g.drawImage(headLeft, bodyX.get(0),bodyY.get(0) ,null);
		}else if(isMovingRight) {
			g.drawImage(headRight, bodyX.get(0),bodyY.get(0) ,null);
		}
	}
	
	@Override
	public void move() {
		for(int i = length - 1; i > 0; i--) {
			bodyX.set(i, bodyX.get(i-1));
			bodyY.set(i, bodyY.get(i-1));
		}
		
		if(isMovingUp) {
			bodyY.set(0, bodyY.get(0) - 20);
			
			if(bodyY.get(0) < 80) {
				bodyY.set(0, 580);
			}
			
		}else if(isMovingDown) {
			bodyY.set(0, bodyY.get(0) + 20);
			
			if(bodyY.get(0) > 580) {
				bodyY.set(0, 80);
			}
			
		}else if(isMovingLeft) {
			bodyX.set(0, bodyX.get(0) - 20);
			
			if(bodyX.get(0) < 0) {
				bodyX.set(0, 780);
			}
			
		}else if(isMovingRight) {
			bodyX.set(0, bodyX.get(0) + 20);
			
			if(bodyX.get(0) > 780) {
				bodyX.set(0, 0);
			}
			
		}
		
		//jika snake menyentuh badannya sendiri
		for(int i = 1; i < length; i++) {
			if(bodyX.get(0).equals(bodyX.get(i)) && bodyY.get(0).equals(bodyY.get(i))) {
				this.HealthPoint = 0;
				break;
			}
		}
		
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHealthPoint() {
		return HealthPoint;
	}

	public void setHealthPoint(int HealthPoint) {
		if(HealthPoint > MAX_HEALTH_POINT) {
			this.HealthPoint = MAX_HEALTH_POINT;
		}else {
			this.HealthPoint = HealthPoint;
		}

	}

	public boolean isMovingUp() {
		return isMovingUp;
	}

	public boolean isMovingDown() {
		return isMovingDown;
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getHeadX() {
		return bodyX.get(0);
	}
	
	public int getHeadY() {
		return bodyY.get(0);
	}
	
	public void addBody() {
		this.bodyX.add(bodyX.get(this.length-1));
		this.bodyY.add(bodyY.get(this.length-1));
		this.length++;
	}
	

}
