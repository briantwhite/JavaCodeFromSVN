package gui;

import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundUtils implements Runnable {

	public static float SAMPLE_RATE = 8000f;
	
	private boolean running;
	private int hz;
	private double vol;
	private double maxVol;
	
	public SoundUtils(int hz, double maxVol) {
		this.hz = hz;
		this.maxVol = maxVol;
	}
	 
	public void run() {
		vol = 0.0f;
		try {
			toneThread();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void beep(int ms) {
		vol = maxVol;
		try {
			TimeUnit.MILLISECONDS.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vol = 0.0f;
	}
	
	private void toneThread() throws LineUnavailableException {
		byte[] buf = new byte[1];
		AudioFormat af = 
				new AudioFormat(
						SAMPLE_RATE, // sampleRate
						8,           // sampleSizeInBits
						1,           // channels
						true,        // signed
						false);      // bigEndian
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl.open(af);
		sdl.start();
		int i = 0;
		while (running) {
			double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
			buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
			sdl.write(buf,0,1);
			i++;
		}
		sdl.drain();
		sdl.stop();
		sdl.close();
	}

}