import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.lang.StringBuffer;


public class client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket s = null;

		String str = "MKCILNNRVIVIIYKEIDTDI,*,*,0;"
                + "MILAVLCGAT,*,*,0;"
                + "MSEEGAQMTGHVMANYPPDLESQD,*,*,2;";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 90; i++) {
			sb.append(str);
		}
		String sendMe = sb.toString();

		Date start = new Date();

		try {
			s = new Socket("localhost", 12000);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader in = null;
		PrintStream out = null;

		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintStream(s.getOutputStream(), true /* autoFlush */);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < 1; i++) {
				System.out.println("Run " + i);
				out.print(sendMe);
				System.out.println(in.readLine());
			}

			out.print("?");

		} catch (IOException e) {
			e.printStackTrace();
		}

	

		try {
			in.close();
			out.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Date end = new Date();
		System.out.println("It took " + (end.getTime() - start.getTime()) + " ms.");
	}

}
