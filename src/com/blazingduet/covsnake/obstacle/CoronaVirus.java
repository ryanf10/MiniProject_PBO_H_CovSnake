package com.blazingduet.covsnake.obstacle;

import java.awt.Graphics;
import java.awt.Image;

import com.blazingduet.covsnake.snake.Snake;

public class CoronaVirus extends Obstacle {
	private Image coronaVirusImg;
	private boolean available;
	
	public CoronaVirus(int positionX, int positionY) {
		super(positionX, positionY);
		this.coronaVirusImg = super.loadImg("CoronaVirus.png");
		this.available = true;
		
		//Thread untuk menampilkan CoronaVirus selama 15 detik di map
		Thread showCoronaVirusThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				available = false;
			}
		};
		showCoronaVirusThread.start();
	}
	
	

	public boolean isAvailable() {
		return available;
	}



	public void setAvailable(boolean available) {
		this.available = available;
	}



	@Override
	public void giveDisadvantage(Snake snake) {
		snake.setInfected(true);
		snake.setInfectedCount(snake.getInfectedCount() + 1);
		
		//Thread untuk memberikan pengurangan HP 5poin/detik selama 3 detik
		Thread infectedThread = new Thread() {
			@Override
			public void run() {
				for(int i = 0; i < 3; i++) {
					snake.setHealthPoint(snake.getHealthPoint() - 5);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				snake.setInfectedCount(snake.getInfectedCount() - 1);
				if(snake.getInfectedCount() == 0) {
					snake.setInfected(false);
				}
			}
		};
		infectedThread.start();
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.coronaVirusImg, super.getPositionX(), super.getPositionY(), null);
	}

}
