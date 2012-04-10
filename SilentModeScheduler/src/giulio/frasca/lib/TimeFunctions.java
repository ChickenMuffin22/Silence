package giulio.frasca.lib;

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

}
