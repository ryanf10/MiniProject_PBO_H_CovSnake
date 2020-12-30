package com.blazingduet.covsnake.food;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.blazingduet.covsnake.snake.Snake;

public class Apple extends Food {
	private Image imgApple;

	public Apple(int positionX, int positionY) {
		super(positionX,positionY);
		imgApple = super.loadImg("Apple.png");
	}
	
	@Override
	public void giveBenefit(Snake snake) {
		snake.setHealthPoint(snake.getHealthPoint() + 20);
		
		if(snake.isActiveMultiplier()) {			
			snake.setScore(snake.getScore() + 10 * 2);
		}else {
			snake.setScore(snake.getScore() + 10);
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.imgApple,super.getPositionX(), super.getPositionY(),null);
	}

}
