package giulio.frasca.lib;

import java.util.Calendar;

public class TimeFunctions {
	/**
	 * Gets the hour of the given unix timestamp.
	 * 
	 * @param time - a long for milliseconds after the epoch, or ms after any midnight for that matter.
	 * @return The number of hours, in 12 hour format, after midnight.
	 */
    public static int getHourOfTime(long time){
    	//time-=59999;
    	int retHour=0;
    	long edittime=time;
    	while (edittime>=60*60*1000){
    		edittime=edittime-60*60*1000;
    		retHour++;
    	}
    	retHour = retHour % 12;
    	if (retHour==0){ retHour=12; }
    	return retHour;
    }
    
    /**
     * 
     * @param time - the number of milliseconds after the epoch to check.
     * @return The number of minutes since midnight for the given time
     */
    public static boolean isAM(long time){
    	//time-=59999;
    	if (time < 12*60*60*1000){
    		return true;
    	}
    	return false;
    		
    }
    
    /**
     * Gets the minute of the given unix-y timestamp (unix x 1000)
     * 
     * @param time - milliseconds since the epoch, or beginning of any hour (either works)
     * @return the minutes since the last hour
     */
    public static int getMinuteOfTime(long time){
    	//time-=59999;
    	if (time==0) { return 0; }
    	int retMin=0;
    	long editMin = time % (60 * 60 * 1000) ;
    	int oneMin = 1000 * 60;
    	while ((retMin*oneMin) < (editMin - 59999)){
    		retMin++;
    	}
    	return retMin;
    }
    
    /**
     * Converts a date into a unix timestamp.  Thanks to user Jerry-Brady on <a href="http://http://stackoverflow.com/questions/4674174/convert-integer-dates-times-to-unix-timestamp-in-java">StackOverflow</a> for this function
     *
     * @param year - the year to convert from
     * @param month - the month to convert from
     * @param day - the day to convert from
     * @param hour - the hour to convert from
     * @param minute - the minute to convert from
     * @return unix timestamp for the date given.
     */
    public static long componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }
    
    /**
     * Gets the year of a unix-y timestamp (ms after epoch)
     * 
     * @param unixtime - the number of ms after the epoch
     * @return the year in yyyy format
     */
    public static int getYearFromTimestamp(long unixtime){
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(unixtime);
    	return c.get(Calendar.YEAR);
    }
    
    /**
     * Gets the month of a unix-y timestamp (ms after epoch)
     * 
     * @param unixtime - the number of ms after the epoch
     * @return the month in mm format
     */
    public static int getMonthFromTimestamp(long unixtime){
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(unixtime);
    	return (1 + c.get(Calendar.MONTH));
    }
    
    /**
     * Gets the day of a unix-y timestamp (ms after epoch)
     * 
     * @param unixtime - the number of ms after the epoch
     * @return the day in dd format
     */
    public static int getDayFromTimestamp(long unixtime){
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(unixtime);
    	return c.get(Calendar.DATE);
    }

	public static int getDayofWeekFromTimestamp(long today) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(today);
		return c.get(Calendar.DAY_OF_WEEK);
		
	}

	public static boolean isSunday(long today) {
		int day = getDayofWeekFromTimestamp(today);
		if (day == Calendar.SUNDAY){
			return true;
		}
		return false;
	}
	public static boolean isMonday(long today) {
		int day = getDayofWeekFromTimestamp(today);
		if (day == Calendar.MONDAY){
			return true;
		}
		return false;
	}
	public static boolean isTuesday(long today) {
		int day = getDayofWeekFromTimestamp(today);
		if (day == Calendar.TUESDAY){
			return true;
		}
		return false;
	}
	public static boolean isWednesday(long today) {
		int day = getDayofWeekFromTimestamp(today);
		if (day == Calendar.WEDNESDAY){
			return true;
		}
		return false;
	}
	public static boolean isThursday(long today) {
		int day = getDayofWeekFromTimestamp(today);
		if (day == Calendar.THURSDAY){
			return true;
		}
		return false;
	}
	public static boolean isFriday(long today) {
		int day = getDayofWeekFromTimestamp(today);
		if (day == Calendar.FRIDAY){
			return true;
		}
		return false;
	}
	public static boolean isSaturday(long today) {
		int day = getDayofWeekFromTimestamp(today);
		if (day == Calendar.SATURDAY){
			return true;
		}
		return false;
	}

}
