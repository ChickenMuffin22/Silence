package giulio.frasca.lib;

import giulio.frasca.silencesched.RingerSettingBlock;
import giulio.frasca.lib.TimeFunctions;

public class Formatter {

	/**
     * Formats the Name of the alarm for the spinner if the form SMTWTFS (start)-(end)
     * 
     * @param block - The block to format the time after
     * @return the string representation of this block
     */
    public static String formatName(RingerSettingBlock block){
    	String retString = block.getId()+": ";
    	boolean sunday = block.isEnabledSunday();
    	boolean monday = block.isEnabledMonday();
    	boolean tuesday = block.isEnabledTuesday();
    	boolean wednesday = block.isEnabledWednesday();
    	boolean thursday = block.isEnabledThursday();
    	boolean friday = block.isEnabledFriday();
    	boolean saturday = block.isEnabledSaturday();
    	String startTime = TimeFunctions.getHourOfTime(block.getStartTime())+":";
    	String endTime = TimeFunctions.getHourOfTime(block.getEndTime())+":";
    	if (TimeFunctions.getMinuteOfTime(block.getStartTime())>9){
    		startTime+=TimeFunctions.getMinuteOfTime(block.getStartTime());
    	}
    	else{
    		startTime+="0"+TimeFunctions.getMinuteOfTime(block.getStartTime());
    	}
    	if (TimeFunctions.getMinuteOfTime(block.getEndTime())>9){
    		endTime+=TimeFunctions.getMinuteOfTime(block.getEndTime());
    	}
    	else{
    		endTime+="0"+TimeFunctions.getMinuteOfTime(block.getEndTime());
    	}
    	if (TimeFunctions.isAM(block.getStartTime())){
    		startTime+="a";
    	}
    	else{
    		startTime+="p";
    	}
    	if (TimeFunctions.isAM(block.getEndTime())){
    		endTime+="a";
    	}
    	else{
    		endTime+="p";
    	}
    	if (sunday){
    		retString+="S";
    	}
    	else{
    		retString+="-";
    	}
    	if (monday){
    		retString+="M";
    	}
    	else{
    		retString+="-";
    	}
    	if (tuesday){
    		retString+="T";
    	}
    	else{
    		retString+="-";
    	}
    	if (wednesday){
    		retString+="W";
    	}
    	else{
    		retString+="-";
    	}
    	if (thursday){
    		retString+="T";
    	}
    	else{
    		retString+="-";
    	}
    	if (friday){
    		retString+="F";
    	}
    	else{
    		retString+="-";
    	}
    	if (saturday){
    		retString+="S";
    	}
    	else{
    		retString+="-";
    	}
    	retString+=" "+startTime+"-"+endTime;
    	if (block.getId() == 0){
    		retString+=" (Default)";
    	}
    	return retString;
    }
    
	/**
	 * Formats an int representin a minute to a string, such that there is a prepending '0' if the time is less than 10
	 * For use with GUI output.
	 * 
	 * @param input - the int for the time to check
	 * @return an outputable string representation of the minutes
	 */
	public static String minFormat(int input){
    	if (input > 9) { return ""+input; }
    	else { return "0" + input ; }
    }
	
    /**
     * Converts a human-readable date into a unix-y timestamp, in mmmm
     * @param hour the hours since 
     * @param minute
     * @param AM
     * @return
     */
    public static long formTimestamp(long hour, long minute, boolean AM){
    	int retTime = 0;
    	if (hour == 12){ hour=0; }
    	if (!AM) { hour+=12; }

    	retTime += hour * 60 * 60 * 1000;
    	retTime += minute * 60 *1000;
    	retTime += 5999;
    	return retTime;
    }
}
