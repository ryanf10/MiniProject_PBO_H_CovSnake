package com.blazingduet.covsnake.gamestate;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class HighScoreState extends GameState {
	
	private final static String DEFAULT_LOCATION = "src/com/blazingduet/covsnake/resources/highscore/";
	private static final String FONT_LOCATION = "src/com/blazingduet/covsnake/resources/font/";
	private final static int SCORE_PODIUM_STATE = 1, SURVIVE_TIME_PODIUM_STATE = 2;
	
	private Image backgroundHighScore, scorePodiumBtn, surviveTimePodiumBtn,scorePodiumActiveBtn, surviveTimePodiumActiveBtn, backBtn, backHover;
	
	private int podiumState;
	private List<User> userScorePodium;
	private List<User> userSurviveTimePodium;
	
	boolean isHover;
	
	Font kongtext;
	
	Cursor default_ = new Cursor(Cursor.DEFAULT_CURSOR);
	Cursor hand = new Cursor(Cursor.HAND_CURSOR);
	
	public HighScoreState(JFrame referred) {
		super(referred);
		backgroundHighScore = loadImg("HighScoreBackground.png");
		scorePodiumBtn = loadImg("ScorePodiumButton.png");
		scorePodiumActiveBtn = loadImg("ScorePodiumActiveButton.png");
		surviveTimePodiumBtn = loadImg("SurviveTimePodiumButton.png");
		surviveTimePodiumActiveBtn = loadImg("SurviveTimePodiumActiveButton.png");
		backBtn = loadImg("Back.png");
		backHover = loadImg("BackHover.png");
		podiumState = SCORE_PODIUM_STATE;
		
		this.userScorePodium = User.load("score.txt");
		if(userScorePodium == null) {
			userScorePodium = new ArrayList<>();
		}

		this.userSurviveTimePodium = User.load("time.txt");
		if(userSurviveTimePodium == null) {
			userSurviveTimePodium = new ArrayList<>();
		}

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//untuk scorePodiumBtn
				if(e.getPoint().x >=250 && e.getPoint().x <400 && e.getPoint().y >= 70 && e.getPoint().y <= 150){
					podiumState = SCORE_PODIUM_STATE;
					repaint();
				};
				
				//untuk surviveTimePodiumBtn
				if(e.getPoint().x >=401 && e.getPoint().x <= 550 && e.getPoint().y >= 70 && e.getPoint().y <= 150){
					podiumState = SURVIVE_TIME_PODIUM_STATE;
					repaint();
				};
				
				//untuk backBtn
				if(e.getPoint().x >=250 && e.getPoint().x <= 550 && e.getPoint().y >= 450 && e.getPoint().y <= 530){
					stateChange(0);
				};
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
				setCursor(default_);
				isHover = false;
				
				//untuk scorePodiumBtn
				if(e.getPoint().x >=250 && e.getPoint().x <400 && e.getPoint().y >= 70 && e.getPoint().y <= 150){
					setCursor(hand);
				};
				
				//untuk surviveTimePodiumBtn
				if(e.getPoint().x >=401 && e.getPoint().x <= 550 && e.getPoint().y >= 70 && e.getPoint().y <= 150){
					setCursor(hand);
				};
				
				if(e.getPoint().x >=250 && e.getPoint().x <= 550 && e.getPoint().y >= 450 && e.getPoint().y <= 530){
					setCursor(hand);
					isHover = true;
					repaint();
				};
				repaint();
			}
		});
	
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
	public void render(Graphics g) {
		
		try {
			kongtext = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_LOCATION + "kongtext.ttf")).deriveFont(15f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("kongtext.ttf")));
		
		} catch (IOException | FontFormatException e) {
			// TODO: handle exception
		}
		
		g.drawImage(backgroundHighScore, 0, 0, null);
		g.setFont(kongtext);
		if(podiumState == SCORE_PODIUM_STATE) {
			g.drawImage(scorePodiumActiveBtn, 250, 70, null);
			g.drawImage(surviveTimePodiumBtn, 400, 70, null);
			
			
			//tampilkan 5 user dengan skor tertinggi
			g.setColor(Color.WHITE);
			int startX = 100, startY = 200;
			for(int i = 0; i < userScorePodium.size() && i < 5; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append(i+1 + ". " + userScorePodium.get(i).getNama() + " " + userScorePodium.get(i).getScore() + " points");
				g.drawString(sb.toString(), startX, startY + 50 * i);
			}
	
			
		}else if(podiumState == SURVIVE_TIME_PODIUM_STATE) {
			g.drawImage(scorePodiumBtn, 250, 70, null);
			g.drawImage(surviveTimePodiumActiveBtn, 400, 70, null);
		
			//tampilkan 5 user dengan survive time terlama
			g.setColor(Color.WHITE);
			int startX = 100, startY = 200;
			for(int i = 0; i < userSurviveTimePodium.size() && i < 5; i++) {
				StringBuffer sb = new StringBuffer();
				sb.append(i+1 + ". " + userSurviveTimePodium.get(i).getNama() +" ");
				
				if(userSurviveTimePodium.get(i).getSurviveTimeHour() >= 10) {
					sb.append(userSurviveTimePodium.get(i).getSurviveTimeHour() +":");
				}else {
					sb.append("0"+userSurviveTimePodium.get(i).getSurviveTimeHour()+":");
				}
				
				if(userSurviveTimePodium.get(i).getSurviveTimeMinute() >= 10) {
					sb.append(userSurviveTimePodium.get(i).getSurviveTimeMinute()+":");
				}else {
					sb.append("0"+userSurviveTimePodium.get(i).getSurviveTimeMinute()+":");
				}
				
				if(userSurviveTimePodium.get(i).getSurviveTimeSecond() >= 10) {
					sb.append(userSurviveTimePodium.get(i).getSurviveTimeSecond());
				}else {
					sb.append("0"+userSurviveTimePodium.get(i).getSurviveTimeSecond());
				}
				
				g.drawString(sb.toString(), startX, startY + 50 * i);
			}
		}
		
		if(isHover) {
			g.drawImage(backHover, 250, 450, null);
		}
		else g.drawImage(backBtn, 250, 450, null);
	}

	@Override
	public void stateChange(int state) {
		switch(state) {
		case 0:
			referred.setContentPane(new MenuState(referred));
			referred.validate();
			referred.getContentPane().requestFocusInWindow();
			break;
		}
		
	}
	
}
