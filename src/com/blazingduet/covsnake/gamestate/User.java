package com.blazingduet.covsnake.gamestate;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class User implements Comparable<User>, Serializable{
	private static final long serialVersionUID = 1L;
	private static int compareType = 0;
	private int score;
	private float surviveTime;
	private String nama;
	
	public User(int score, float surviveTime, String nama) {
		this.score = score;
		this.nama = nama;
		this.surviveTime = surviveTime;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}
	
	public float getSurviveTime() {
		return surviveTime;
	}

	public void setSurviveTime(float surviveTime) {
		this.surviveTime = surviveTime;
	}
	
	public int getSurviveTimeHour() {
		return (int)(this.surviveTime/(60.00 * 60.00 * 1000.00));
	}
	
	public int getSurviveTimeMinute() {
		return (int)(this.surviveTime/(60.00 * 1000.00));
	}
	
	public int getSurviveTimeSecond() {
		return (int)(this.surviveTime/(1000.00));
	}

	public static List<User> load(String filename) {
		List<User> userScore;
		try {
            FileInputStream fileIn = new FileInputStream("src/com/blazingduet/covsnake/highscore/" + filename);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            userScore = (List<User>) objectIn.readObject();
            objectIn.close(); 
            return userScore;
        }catch(EOFException e) {

        }catch (IOException e) {

        }catch (ClassNotFoundException e) {

		}
		return null;
	}
	
	public static void save(List<User> userScore, String filename) {
		try {			 
          FileOutputStream fileOut = new FileOutputStream("src/com/blazingduet/covsnake/highscore/" + filename);
          ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
          objectOut.writeObject(userScore);
          objectOut.close();
      } catch (IOException e) {

      }
	}

	public static void setCompareType(int state) {
		compareType = state;
	}
	
	@Override
	public int compareTo(User o) {
		if(User.compareType == 0) {
			if(this.score > o.score ) {
				return -1;
			}else {
				return 1;
			}			
		}else {
			if(this.surviveTime > o.surviveTime) {
				return -1;
			}else {
				return 1;
			}
		}
	}
}
