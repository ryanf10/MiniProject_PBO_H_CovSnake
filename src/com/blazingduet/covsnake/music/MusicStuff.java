package com.blazingduet.covsnake.music;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicStuff {

	private Clip clip;
	
	public void playMusic(String location) {
		File file = new File(location);
	     
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float volumeRange = volume.getMaximum() - volume.getMinimum();
			volume.setValue(volumeRange * (float)0.7 + volume.getMinimum());
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
	     } catch (UnsupportedAudioFileException e) {
	    	 e.printStackTrace();
	     } catch (IOException e) {
	    	 e.printStackTrace();
	     } catch (LineUnavailableException e) {
	    	 e.printStackTrace();
	     }
	}
	 
	public void stopAll() {
	    clip.stop();
	}
}
