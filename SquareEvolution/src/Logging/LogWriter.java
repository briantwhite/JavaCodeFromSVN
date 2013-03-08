package Logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.LinkedBlockingQueue;

import SE.Configuration;


/**
 * log writing is slow, so move it to another thread
 * @author brian
 *
 */
public class LogWriter {

	private LinkedBlockingQueue<LogEntry> logEventQueue;
	private Configuration config;

	StringBuffer outputLogBuffer;
	StringBuffer fitnessLogBuffer;
	StringBuffer foldedProteinLogBuffer;
	StringBuffer badProteinLogBuffer;

	public LogWriter(Configuration config) {
		this.config = config;
		logEventQueue = new LinkedBlockingQueue<LogEntry>();
	}

	public void saveLogs() {
		startBuffers();
		processEntries();
		saveFiles();
	}

	public void addLogEntry(LogEntry e) {
		try {
			logEventQueue.put(e);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private void startBuffers() {
		outputLogBuffer = new StringBuffer();
		fitnessLogBuffer = new StringBuffer();	
		if (!config.getProteinFoldingFileName().equals("")) {
			foldedProteinLogBuffer = new StringBuffer();
		}
		badProteinLogBuffer = new StringBuffer();
	}

	private void processEntries() {
		while (!logEventQueue.isEmpty()) {
			LogEntry e = logEventQueue.poll();
			if (e instanceof OutputLogEntry) {
				outputLogBuffer.append(e.toString());
				outputLogBuffer.append("\n");
			}
			if (e instanceof FitnessLogEntry) {
				fitnessLogBuffer.append(e.toString());
				fitnessLogBuffer.append("\n");
			}
			if (e instanceof FoldedProteinLogEntry) {
				foldedProteinLogBuffer.append(e.toString());
				foldedProteinLogBuffer.append("\n");
			}
			if (e instanceof BadProteinLogEntry) {
				badProteinLogBuffer.append(e.toString());
				badProteinLogBuffer.append("\n");
			}
		}
	}

	private void saveFiles() {
		try {

			byte[] outputLogBytes = outputLogBuffer.toString().getBytes();
			FileChannel outputLogFC = 
				new RandomAccessFile(config.getLogFileName(), "rw").getChannel();
			ByteBuffer outputLogBuffer = 
				outputLogFC.map(FileChannel.MapMode.READ_WRITE, 0, outputLogBytes.length);
			outputLogBuffer.put(outputLogBytes);
			outputLogFC.close();

			byte[] fitnessLogBytes = fitnessLogBuffer.toString().getBytes();
			FileChannel fitnessLogFC = 
				new RandomAccessFile(config.getFitnessFileName(), "rw").getChannel();
			ByteBuffer fitnessLogBuffer = 
				fitnessLogFC.map(FileChannel.MapMode.READ_WRITE, 0, fitnessLogBytes.length);
			fitnessLogBuffer.put(fitnessLogBytes);
			fitnessLogFC.close();

			if (!config.getProteinFoldingFileName().equals("")) {
				byte[] foldedProteinLogBytes = foldedProteinLogBuffer.toString().getBytes();
				FileChannel foldedProteinLogFC = 
					new RandomAccessFile(config.getProteinFoldingFileName(), "rw").getChannel();
				ByteBuffer foldedProteinLogByteBuffer = 
					foldedProteinLogFC.map(FileChannel.MapMode.READ_WRITE, 0, foldedProteinLogBytes.length);
				foldedProteinLogByteBuffer.put(foldedProteinLogBytes);
				foldedProteinLogFC.close();
			}

			byte[] badProteinLogBytes = badProteinLogBuffer.toString().getBytes();
			FileChannel badProteinLogFC = 
				new RandomAccessFile(config.getBadProteinFileName(), "rw").getChannel();
			ByteBuffer badProteinLogBuffer = 
				badProteinLogFC.map(FileChannel.MapMode.READ_WRITE, 0, badProteinLogBytes.length);
			badProteinLogBuffer.put(badProteinLogBytes);
			badProteinLogFC.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
