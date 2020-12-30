package com.blazingduet.covsnake.food;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import com.blazingduet.covsnake.gamestate.PlayState;
import com.blazingduet.covsnake.movable.Movable;
import com.blazingduet.covsnake.snake.Snake;

public class Chicken extends Food implements Movable {
	private static Image chickenUp, chickenDown, chickenLeft, chickenRight;
	
	private PlayState referred;
	private boolean available, isMovingUp, isMovingDown, isMovingLeft, isMovingRight;
	
	public Chicken(int positionX, int positionY, PlayState referred) {
		super(positionX, positionY);
		chickenUp = super.loadImg("ChickenUp.png");
		chickenDown = super.loadImg("ChickenDown.png");
		chickenLeft = super.loadImg("ChickenLeft.png");
		chickenRight = super.loadImg("ChickenRight.png");
		this.referred = referred;
		this.setDirection(false, true, false, false);
		this.available = true;
		
		Thread chickenMoveThread = new Thread() {
			@Override
			public void run() {
				for(int i = 0; i < 5; i++) {
					move();
					referred.repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				available = false;
			}
		};
		chickenMoveThread.start();
	}
	
	private void setDirection(boolean isMovingUp, boolean isMovingDown, boolean isMovingLeft, boolean isMovingRight) {
		this.isMovingUp = isMovingUp;
		this.isMovingDown = isMovingDown;
		this.isMovingLeft = isMovingLeft;
		this.isMovingRight = isMovingRight;
	}
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public void giveBenefit(Snake snake) {
		snake.setHealthPoint(Snake.MAX_HEALTH_POINT);
		
	}

	@Override
	public void render(Graphics g) {
		if(isMovingUp) {
			g.drawImage(chickenUp, super.getPositionX(), super.getPositionY(), null);
		}else if(isMovingDown) {
			g.drawImage(chickenDown, super.getPositionX(), super.getPositionY(), null);
		}else if(isMovingLeft) {
			g.drawImage(chickenLeft, super.getPositionX(), super.getPositionY(), null);
		}else if(isMovingRight) {
			g.drawImage(chickenRight, super.getPositionX(), super.getPositionY(), null);
		}
		
	}

	@Override
	public void move() {
		boolean canMoveUp = true,canMoveDown = true, canMoveLeft = true, canMoveRight = true;
		Random rnd = new Random();
		while(true){
			int rndMove = rnd.nextInt(4);
			if(rndMove == 0) {
				
				if(referred.isSpaceAvailable(super.getPositionX(), super.getPositionY()-20)) {
					this.setDirection(true, false, false, false);
					if(super.getPositionY()-20 >= PlayState.MAP_START_POSITION_Y + PlayState.GRASS_AREA_START_Y) {
						super.setPositionY(super.getPositionY()-20);
					}
					break;
				}else {
					canMoveUp = false;
				}
		
			}else if(rndMove == 1) {
				
				if(referred.isSpaceAvailable(super.getPositionX(), super.getPositionY()+20)) {
					this.setDirection(false, true, false, false);
					if(super.getPositionY()+20 < PlayState.MAP_START_POSITION_Y + PlayState.GRASS_AREA_START_Y + PlayState.GRASS_AREA_HEIGHT) {
						super.setPositionY(super.getPositionY()+20);
					}
					break;
				}else {
					canMoveDown = false;
				}
				
			}else if(rndMove == 2) {
				
				if(referred.isSpaceAvailable(super.getPositionX()-20, super.getPositionY())) {
					this.setDirection(false, false, true, false);
					if(super.getPositionX()-20 >= PlayState.MAP_START_POSITION_X + PlayState.GRASS_AREA_START_X) {
						super.setPositionX(super.getPositionX()-20);
					}
					break;
				}else {
					canMoveLeft = false;
				}
				
			}else if(rndMove == 3) {
				if(referred.isSpaceAvailable(super.getPositionX()+20, super.getPositionY())) {
					this.setDirection(false, false, false, true);
					if(super.getPositionX()+20 < PlayState.MAP_START_POSITION_X + PlayState.GRASS_AREA_START_X + PlayState.GRASS_AREA_WIDTH) {
						super.setPositionX(super.getPositionX()+20);
					}
					break;
				}else {
					canMoveRight = false;
				}
			}
			
			if(!canMoveUp && !canMoveDown && !canMoveLeft && !canMoveRight) {
				break;
			}
		}
		
		
	}
	
	

}
