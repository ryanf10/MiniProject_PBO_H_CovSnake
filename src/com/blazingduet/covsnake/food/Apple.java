package com.blazingduet.covsnake.food;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.blazingduet.covsnake.snake.Snake;

public class Apple extends Food {
	
	private static final String DEFAULT_LOCATION="src/com/blazingduet/covsnake/resources/food/";
	
	public Apple(int positionX, int positionY) {
		super(positionX,positionY);
		super.setImgFood(this.loadImg("Apple.png"));
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
	public void giveBenefit(Snake s) {
		s.setHealthPoint(s.getHealthPoint() + 20);
		s.setScore(s.getScore() + 10);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(super.getImgFood(),super.getPositionX(), super.getPositionY(),null);
	}

}
