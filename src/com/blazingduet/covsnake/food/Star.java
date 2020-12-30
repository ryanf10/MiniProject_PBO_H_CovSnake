package com.blazingduet.covsnake.food;

import java.awt.Graphics;
import java.awt.Image;

import com.blazingduet.covsnake.snake.Snake;

public class Star extends Food {
	private Image imgStar;
	private boolean available;
	
	
	public Star(int positionX, int positionY) {
		super(positionX, positionY);
		this.imgStar = super.loadImg("Star.png");
		this.available = true;
		
		//Thread untuk menampilkan bintang selama 5 detik
		Thread showStarThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				available = false;
			}
		};
		showStarThread.start();
	}
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public void giveBenefit(Snake snake) {
		snake.setActiveMultiplier(true);
		Thread multiplierThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				snake.setActiveMultiplier(false);
			}
		};
		multiplierThread.start();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.imgStar,super.getPositionX(), super.getPositionY(),null);
	}

}
