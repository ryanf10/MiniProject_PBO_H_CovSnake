package com.blazingduet.covsnake.obstacle;

import java.awt.Graphics;
import java.awt.Image;

import com.blazingduet.covsnake.snake.Snake;

public class Stone extends Obstacle {
	private Image imgStone;
	public Stone(int positionX, int positionY) {
		super(positionX, positionY);	
		this.imgStone = super.loadImg("Stone.png");
	}

	@Override
	public void giveDisadvantage(Snake snake) {
		snake.setHealthPoint(0);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(imgStone, super.getPositionX(), super.getPositionY(), null);
	}

}
