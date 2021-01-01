package com.blazingduet.covsnake.obstacle;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.blazingduet.covsnake.snake.Snake;

public abstract class Obstacle {
	public static final int HEIGHT_SIZE = 20, WIDTH_SIZE = 20;
	private static final String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/obstacle/";
	
	private int positionX, positionY;
	
	public Obstacle(int positionX, int positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public abstract void giveDisadvantage(Snake snake);
	public abstract void render(Graphics g);
	
	public boolean TouchedBySnake(Snake snake) {
		if(snake.getHeadX() == this.positionX && snake.getHeadY() == this.positionY) {
			this.giveDisadvantage(snake);
			return true;
		}
		return false;
	}
	
	protected Image loadImg(String filename) {
		try {
			return ImageIO.read(new File(DEFAULT_LOCATION+filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
			
}
