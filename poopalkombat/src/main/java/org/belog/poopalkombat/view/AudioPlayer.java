package org.belog.poopalkombat.view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;

public class AudioPlayer {
	private Clip clip;

	public void setSourceFile(String fileName) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream(fileName)));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if (clip == null) {
			return;
		}
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void stop() {
		if (clip.isRunning()) { clip.stop(); }
	}
	
	public void close() {
		stop();
		clip.close();
	}

	public boolean isPlaying() {
		if (clip == null) {
			return false;
		}
		return clip.isRunning();
	}
}