package foldingServer;

import java.io.Serializable;

public class SsBond implements Serializable {
	private int first;
	private int second;
	
	public SsBond(int f, int s) {
		first = f;
		second = s;
	}
	
	public int getFirst() {
		return first;
	}
	
	public int getSecond() {
		return second;
	}

}
