package giulio.frasca.silencesched;

import android.media.AudioManager;
import android.util.Log;

/**
 * Data Bean for storing info about a ringer block
 * 
 * @author Giulio Frasca
 *
 */
public class RingerSettingBlock {
	//the time since midnight to start this alarm
	private long startTime;
	//the time since midnight to end this block
	private long endTime;
	//the ring value of this block
	private int ringVal;
	//it id number of this block
	private int id;
	//to use this, set 1 as 'on' and 0 as 'off' on a 7 digit number
	//i.e. 1001101 is for sunday, wednesday, thursday, and saturday
	private int days;
	//is this block enabled?
	private boolean enabled;
	//reapeat until timestamp
	private long repeatUntil;
	
	private final long MAX_TIMESTAMP = 253402300799000L;

	public long getRepeatUntil() {
		return repeatUntil;
	}

	public void setRepeatUntil(long repeatUntil) {
		this.repeatUntil = repeatUntil;
	}

	/**
	 * Constructor Method.  Will disable block if day specifier is wrong, but otherwise sets everything as planned.
	 * @param minTime - the start time of the block
	 * @param maxTime - the end time of the block
	 * @param ringerValue - the ring value of the block.  use AudioManger.RINGER_MODE_(SILENT|VIBRATE|NORMAL)
	 * @param id - the id number of the block
	 * @param days - the date specifier of the block
	 * @param repeatUntil - the timestamp that this block is effective for
	 */
	public RingerSettingBlock(long minTime, long maxTime, int ringerValue, int id, int days, long repeatUntil) {
		this.enabled=true;
		this.startTime=timeSinceMidnight(minTime);
		this.endTime=timeSinceMidnight(maxTime);
		this.ringVal=ringerValue;
		this.id=id;
		this.repeatUntil=repeatUntil;
		if (days<0 || days>9999999){
			//incorrectly formatted date specifier. make it a nonexistant alarm;
			this.days=0;
			startTime=0;
			endTime=0;
			this.enabled = false;
		}
		else{
			//otherwise, its fine
			this.days=days;
		}
	}
	
	/**
	 * Constructor Method.  Will disable block if day specifier is wrong, but otherwise sets everything as planned.
	 * @param minTime - the start time of the block
	 * @param maxTime - the end time of the block
	 * @param ringerValue - the ring value of the block.  use AudioManger.RINGER_MODE_(SILENT|VIBRATE|NORMAL)
	 * @param id - the id number of the block
	 * @param days - the date specifier of the block
	 */
	public RingerSettingBlock(long minTime, long maxTime, int ringerValue, int id, int days) {
		this.enabled=true;
		this.startTime=timeSinceMidnight(minTime);
		this.endTime=timeSinceMidnight(maxTime);
		this.ringVal=ringerValue;
		this.id=id;
		//maximum date handled by program
		this.repeatUntil=MAX_TIMESTAMP;
		if (days<0 || days>9999999){
			//incorrectly formatted date specifier. make it a nonexistant alarm;
			this.days=0;
			startTime=0;
			endTime=0;
			this.enabled = false;
		}
		else{
			//otherwise, its fine
			this.days=days;
		}
	}
	
	/**
	 * Contstuctor Method.  Defaults to a full day
	 * 
	 * @param ringerValue - the ring value of the block.  use AudioManger.RINGER_MODE_(SILENT|VIBRATE|NORMAL)
	 * @param id - the id number of the block
	 * @param days - the date specifier of the block
	 */
	public RingerSettingBlock(int ringerValue, int id, int days){
		new RingerSettingBlock(0,24*60*60*1000,ringerValue,id,days);
	}
	
	/**
	 * Constructor Method. Defaults to a full day on vibrate
	 * @param id - the id number of the block
	 * @param days - the date specifier of the block
	 */
	public RingerSettingBlock(int id, int days){
		new RingerSettingBlock(0,24*60*60*1000,AudioManager.RINGER_MODE_VIBRATE, id, days);
	}
	
	/**
	 * Constructor Method. Defaults to the entire week on specified ringVal.
	 * @param ringerValue - the ring value of the block.  use AudioManger.RINGER_MODE_(SILENT|VIBRATE|NORMAL)
	 * @param id - the id number of the block
	 * @param unusedString - unusedString that exists just to differentiate between another constructor method
	 */
	public RingerSettingBlock(int ringerValue, int id, String unusedString){
		new RingerSettingBlock(0,24*60*60*1000,ringerValue, id, 1111111);
	}
	
	/**
	 * Constructor Method.  Defaults to the entire week on vibrate
	 * @param id - the id number of the block
	 */
	public RingerSettingBlock(int id){
		new RingerSettingBlock(0,24*60*60*1000,AudioManager.RINGER_MODE_VIBRATE, id, 1111111);
	}

	/**
	 * Gets the start time of this block
	 * 
	 * @return The start time of this block
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time of this block
	 * 
	 * @param startTime - the new start time for this block
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time of this block
	 * 
	 * @return the end time for this block
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time of this block
	 * @param endTime - the new endTime for this block
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Gets the ring value for this block
	 * @return The ring value for this block
	 */
	public int getRingVal() {
		return ringVal;
	}

	/**
	 * Sets the ring value for this block
	 * 
	 * @param ringVal - The new ring value for this block
	 */
	public void setRingVal(int ringVal) {
		this.ringVal = ringVal;
	}

	
	/**
	 * Gets the id of this block
	 * 
	 * @return the id for this block
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id of this block
	 * 
	 * @param id - the new id to set for this block (be careful with this...)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the days specifier integer for this block
	 * 
	 * @return the days specifier integer for this block
	 */
	public int getDays() {
		return days;
	}

	/**
	 * Sets the days specifier integer for this block
	 * 
	 * @param days - the new days specifer integer for this block
	 */
	public void setDays(int days) {
		this.days = days;
	}

	/**
	 *  Check if this block is enabled
	 * 
	 * @return true if this block is enabled, false if not
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled boolean for this block
	 * 
	 * @param enabled - the new enabled/disabled boolean to set for this block
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Checks if this block is enabled on sundays
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean isEnabledSunday(){
		int testDays = days;
		int subDays = 999999;
		int modDays = 10000000;
		int retDays = (testDays % modDays) - subDays;
		if ( retDays < 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this block is enabled on mondays
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean isEnabledMonday(){
		int testDays = days;
		int subDays = 99999;
		int modDays = 1000000;
		int retDays = (testDays % modDays) - subDays;
		if ( retDays < 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this block is enabled on tuesdays
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean isEnabledTuesday(){
		int testDays = days;
		int subDays = 9999;
		int modDays = 100000;
		int retDays = (testDays % modDays) - subDays;
		if ( retDays < 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this block is enabled on wednesdays
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean isEnabledWednesday(){
		int testDays = days;
		int subDays = 999;
		int modDays = 10000;
		int retDays = (testDays % modDays) - subDays;
		if ( retDays < 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this block is enabled on thursdays
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean isEnabledThursday(){
		int testDays = days;
		int subDays = 99;
		int modDays = 1000;
		int retDays = (testDays % modDays) - subDays;
		if ( retDays < 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this block is enabled on fridays
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean isEnabledFriday(){
		int testDays = days;
		int subDays = 9;
		int modDays = 100;
		int retDays = (testDays % modDays) - subDays;
		if ( retDays < 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if this block is enabled on saturdays
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean isEnabledSaturday(){
		int testDays = days;
		int subDays = 0;
		int modDays = 10;
		int retDays = (testDays % modDays) - subDays;
		if ( retDays < 0 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Converts a unix timestamp into a more useful 'time since midnight' timestamp
	 * 
	 * @param time - Unix timestamp
	 * @return the number of ms between the input time and midnight of that day
	 */
	public long timeSinceMidnight(long time){
		long day = 24*60*60*1000;
		return (time % day);
	}
	
    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
    }

}
