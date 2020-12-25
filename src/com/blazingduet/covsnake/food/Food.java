package com.blazingduet.covsnake.food;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import com.blazingduet.covsnake.snake.Snake;

public abstract class Food {
	public static final int HEIGHT_SIZE = 20, WIDTH_SIZE = 20;
	
	private int positionX, positionY;
	private Image imgFood;
	
	public Food(int positionX, int positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public abstract void giveBenefit(Snake s);
	public abstract void render(Graphics g);
	
	public boolean eatenBySnake(Snake s) {
		if(s.getHeadX() == this.positionX && s.getHeadY() == this.positionY) {
			s.addBody();
			this.giveBenefit(s);
			return true;
		}
		return false;
	}

	public Image getImgFood() {
		return imgFood;
	}

	public void setImgFood(Image imgFood) {
		this.imgFood = imgFood;
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
