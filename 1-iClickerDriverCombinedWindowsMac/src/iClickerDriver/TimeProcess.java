package iClickerDriver;

import java.util.*;

/**
 * TimeProcess: get timestamp information with various format.
 * @author Junhao
 *
 */
public class TimeProcess {
	/**
	 * Return the current system time in the format required.
	 * @param timeEnum time format.
	 * @return current system time in the format required.
	 */
	public static String getTime(TimeEnum timeEnum) {
		String timeStr = "";
		
		switch (timeEnum) {
		case YMDHMS: 
			timeStr = getTimeYMDHMS();
			break;
		case YMDHM:
			timeStr = getTimeYMDHM();
			break;
		case HMS:
			timeStr = getTimeHMS();
			break;
		case HMSM:
			timeStr = getTimeHMSM();
			break;
		default:
			System.err.println("Bad time format");
		}
		
		return timeStr;
	}

	private static String getTimeYMDHMS() {
		final Calendar calendar = new GregorianCalendar();
		
		int yearInt	  = calendar.get(Calendar.YEAR);
		int monthInt  = calendar.get(Calendar.MONTH);
		int dateInt   = calendar.get(Calendar.DATE);
		int hourInt   = calendar.get(Calendar.HOUR_OF_DAY);
		int minuteInt = calendar.get(Calendar.MINUTE);
		int secondInt = calendar.get(Calendar.SECOND);
		
		String yearStr   = yearInt + "";
		String monthStr  = monthInt  < 10 ? "0" +  monthInt : "" +  monthInt;
		String dateStr   = dateInt   < 10 ? "0" +   dateInt : "" +   dateInt;
		String hourStr   = hourInt   < 10 ? "0" +   hourInt : "" +   hourInt;
		String minuteStr = minuteInt < 10 ? "0" + minuteInt : "" + minuteInt;
		String secondStr = secondInt < 10 ? "0" + secondInt : "" + secondInt;
		
		return yearStr   + "-" +
			   monthStr  + "-" + 
			   dateStr   + "_" + 
			   hourStr   + "-" +
			   minuteStr + "-" + 
			   secondStr + "";
	}
	
	private static String getTimeYMDHM() {
		final Calendar calendar = new GregorianCalendar();
		
		int yearInt	  = calendar.get(Calendar.YEAR);
		int monthInt  = calendar.get(Calendar.MONTH) + 1;
		int dateInt   = calendar.get(Calendar.DATE);
		int hourInt   = calendar.get(Calendar.HOUR_OF_DAY);
		int minuteInt = calendar.get(Calendar.MINUTE);
		
		String yearStr   = (yearInt + "").substring(2, 4);
		String monthStr  = monthInt  < 10 ? "0" +  monthInt : "" +  monthInt;
		String dateStr   = dateInt   < 10 ? "0" +   dateInt : "" +   dateInt;
		String hourStr   = hourInt   < 10 ? "0" +   hourInt : "" +   hourInt;
		String minuteStr = minuteInt < 10 ? "0" + minuteInt : "" + minuteInt;
		
		return 		 "" +
			   yearStr  +
			   monthStr + 
			   dateStr  + 
			   hourStr  +
			   minuteStr;	
	}
	
	private static String getTimeHMS() {
		final Calendar calendar = new GregorianCalendar();
		
		int hourInt   	   = calendar.get(Calendar.HOUR_OF_DAY);
		int minuteInt 	   = calendar.get(Calendar.MINUTE);
		int secondInt	   = calendar.get(Calendar.SECOND);
		
		String hourStr   	  = hourInt   < 10 ? "0" +   hourInt : "" +   hourInt;
		String minuteStr 	  = minuteInt < 10 ? "0" + minuteInt : "" + minuteInt;
		String secondStr 	  = secondInt < 10 ? "0" + secondInt : "" + secondInt;
		
		return hourStr   + ":" +
			   minuteStr + ":" + 
			   secondStr;
	}
	
	private static String getTimeHMSM() {
		final Calendar calendar = new GregorianCalendar();
		
		int hourInt   	   = calendar.get(Calendar.HOUR_OF_DAY);
		int minuteInt 	   = calendar.get(Calendar.MINUTE);
		int secondInt	   = calendar.get(Calendar.SECOND);
		int millisecondInt = calendar.get(Calendar.MILLISECOND);
		
		String hourStr   	  = hourInt   < 10 ? "0" +   hourInt : "" +   hourInt;
		String minuteStr 	  = minuteInt < 10 ? "0" + minuteInt : "" + minuteInt;
		String secondStr 	  = secondInt < 10 ? "0" + secondInt : "" + secondInt;
		String millisecondStr;
		
		if(millisecondInt < 10) {
			millisecondStr = "00" + millisecondInt;
		}
		else if(millisecondInt < 100) {
			millisecondStr = "0"  + millisecondInt;
		}
		else {
			millisecondStr = ""   + millisecondInt;
		}
		
		return hourStr   + ":" +
			   minuteStr + ":" + 
			   secondStr + "." +
			   millisecondStr;
	}
}
