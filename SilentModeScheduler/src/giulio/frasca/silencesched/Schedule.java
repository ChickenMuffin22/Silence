package giulio.frasca.silencesched;

import java.util.LinkedList;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import giulio.frasca.silencesched.exceptions.*;

/**
 * A aggregation object used to manage RingerSettingBlock objects
 * 
 * @author Giulio Frasca
 *
 */
public class Schedule {

	//the list of all the ringer blocks.  please maintain and update this whenever dealing with the pref file as well
	private LinkedList<RingerSettingBlock> blocks;
	//An abstraction object to handle all pref file handling
	private PrefReader reader;
	
	private final long MAX_TIMESTAMP=253402300799000L;
	
	/**
	 * Standard constructer.  Instantiates the list structure and loads blocks into it.
	 */
	public Schedule(SharedPreferences settings){
		reader = new PrefReader(settings);
		resetList();
		try{
			loadBlocks();
		}
		catch (NoAlarmsError e){
			addBlock(0,(24*59*60*1000),AudioManager.RINGER_MODE_NORMAL,1111111, MAX_TIMESTAMP);
			try{
				loadBlocks();
			}
			catch (NoAlarmsError f){
				logcatPrint("UNCAUGHT EXCEPTION");
			}
		}
		
	}

	
	
	/**
	 * Resets the list structure, probably to reload the stored blocks back into it.
	 */
	public void resetList(){
		blocks = new LinkedList<RingerSettingBlock>();
	}
	
	/**
	 * Load all of the saved blocks in the pref file into memory
	 */
	public void loadBlocks() throws NoAlarmsError {
		//load the first block into memory
		RingerSettingBlock first = reader.getFirst();
		try{ 
			if (first.getId() == -1){
				
				reader.addBlock(0,24*59*60*1000, AudioManager.RINGER_MODE_NORMAL, 1111111, MAX_TIMESTAMP);
				first = reader.getFirst();
			}
			blocks.add(first);
			while (reader.hasNext()){
				blocks.add(reader.getNext());
			}
		}
		catch (NullPointerException e){
			Log.e("error","No alarms found in pref file");
			throw new NoAlarmsError("No alarms found in pref file");
		}
	}
	
	/**
	 * Adds a block to the list and pref file
	 * 
	 * @param startTime - the time in ms since midnight in which the block will start
	 * @param endTime - the time in ms since midnight in which the block will end
	 * @param ringer - the ring level (ring, vibrate, silent) that the block entails
	 * @param days - the integer days specifier for this block
	 */
	public int addBlock(long startTime, long endTime, int ringer, int days, long repeatUntil){
		startTime = timeSinceSunday(startTime);
		endTime = timeSinceSunday(endTime);
		int id = reader.addBlock(startTime, endTime, ringer, days,repeatUntil);
		
		RingerSettingBlock newBlock = new RingerSettingBlock(startTime, endTime, id, ringer, days, repeatUntil);
		blocks.add(newBlock);
		return id;
	}
	
	public boolean hasBlock(int id){
		for (int i=0;i<blocks.size();i++){
			RingerSettingBlock test = blocks.get(i);
			if (test.getId() == id){
				return true;
			}
		}
		return false;
	}
	
	public RingerSettingBlock getBlock(int id){
		return blocks.get(id);
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
	
	/**
	 * Converts a unix timestamp into a more useful 'time since sunday' timestamp
	 * 
	 * @param time - Unix timestamp
	 * @return the number of ms between the input time and Sunday at midnight
	 */
	public long timeSinceSunday(long time){
		long offset = 4*24*60*60*1000;
		long week = 7*24*60*60*1000;
		long retTime = ( time - offset ) % week ;
		return retTime;
	}
	
	/**
	 * Disables a block (doesn't actually remove it) in the pref file and in memory
	 * 
	 * @param id - the target blocks id
	 */
	public void removeBlock(int id){
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setEnabled(false);
		reader.removeBlock(id);
	}
	
	/**
	 * Edits the start time of a block in the pref file and in memory
	 * @param id - the target block's id
	 * @param startTime - the new start time in ms from ht
	 */
	public void editBlockStart(int id, long startTime){
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setStartTime(startTime);
		reader.editStart(id, startTime);
	}
	
	/**
	 * Edits the end time of a block in memory and in the pref file
	 * @param id - the target blocks id
	 * @param endTime - the new end time in ms from midnight
	 */
	public void editBlockEnd(int id, long endTime) {
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setEndTime(endTime);
		reader.editEnd(id, endTime);
	}
	
	/**
	 * Edits the day specifier of a block in memory and in the pref file
	 * @param id - the id of the target block
	 * @param days - the new day specifer integer
	 */
	public void editBlockDays(int id, int days){
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setDays(days);
		reader.editDays(id, days);
	}
	
	/**
	 * Edits the Ringer level of a block in memory and in the pref file
	 * 
	 * @param id - The id of the target block
	 * @param ringer - The new ringer level
	 */
	public void editBlockRinger(int id, int ringer){
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setRingVal(ringer);
		reader.editRinger(id, ringer);
	}
	
	/**
	 * Enables a block in memory and in the pref file
	 * 
	 * @param id - the id of the target block
	 */
	public void enableBlock(int id){
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setEnabled(true);
		reader.editEnabled(id, true);
	}
	
	/**
	 * Disables a block in memory and in the pref file
	 * 
	 * @param id - the id of the target block
	 */
	public void disableBlock(int id){
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setEnabled(false);
		reader.editEnabled(id, false);
	}

	/**
	 * Gets the number of alarms stored in the pref file
	 * 
	 * @return The number of alarms stored in the pref file
	 */
	public int getAlarmCount(){
		return reader.getAlarmCount();
	}
	
	/**
	 * Utility funtion that will format a week into a day specifier that the RingerSettingBlock bean holds
	 * 
	 * @param Sunday - Is this block active for Sunday?
	 * @param Monday - Is this block active for Monday?
	 * @param Tuesday - Is this block active for Tuesday?
	 * @param Wednesday - Is this block active for Wednesday?
	 * @param Thursday - Is this block active for Thursday?
	 * @param Friday - Is this block active for Friday?
	 * @param Saturday - Is this block active for Saturday?
	 * @return the day specifier used for adding an block
	 */
	public int formatDays(boolean Sunday, boolean Monday, boolean Tuesday, boolean Wednesday, boolean Thursday, boolean Friday, boolean Saturday){
		int retDays = 0;
		if (Sunday) 	{ retDays+=1000000; }
		if (Monday)		{ retDays+=100000; }
		if (Tuesday)	{ retDays+=10000; }
		if (Wednesday)	{ retDays+=1000; }
		if (Thursday)	{ retDays+=100; }
		if (Friday)		{ retDays+=10; }
		if (Saturday)	{ retDays+=1; }
		return retDays;
	}
	
	/**
	 * Checks if a block is active for Sunday
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledSunday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		return checkBlock.isEnabledSunday();
	}
	
	/**
	 * Checks if a block is active for Monday
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledMonday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		return checkBlock.isEnabledMonday();
	}
	
	/**
	 * Checks if a block is active for Tuesday
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledTuesday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		return checkBlock.isEnabledTuesday();
	}
	
	/**
	 * Checks if a block is active for Wednesday
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledWednesday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		return checkBlock.isEnabledWednesday();
	}
	
	/**
	 * Checks if a block is active for Thursday
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledThursday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		return checkBlock.isEnabledThursday();
	}
	
	/**
	 * Checks if a block is active for Friday
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledFriday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		return checkBlock.isEnabledFriday();
	}
	
	/**
	 * Checks if a block is active for Saturday
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledSaturday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		return checkBlock.isEnabledSaturday();
	}
	
	/**
	 * Checks if a block is active for today
	 * 
	 * @param id - the id of the target block
	 * @return true if the block is active, false if not
	 */
	public boolean isEnabledToday(int id){
		RingerSettingBlock checkBlock = blocks.get(id);
		long today = timeSinceSunday(System.currentTimeMillis());
		long day = 24*60*60*1000;
		if (today < ( 1 * day ) ) { return checkBlock.isEnabledSunday(); }
		if (today < ( 2 * day ) ) { return checkBlock.isEnabledSunday(); }
		if (today < ( 3 * day ) ) { return checkBlock.isEnabledSunday(); }
		if (today < ( 4 * day ) ) { return checkBlock.isEnabledSunday(); }
		if (today < ( 5 * day ) ) { return checkBlock.isEnabledSunday(); }
		if (today < ( 6 * day ) ) { return checkBlock.isEnabledSunday(); }
		if (today < ( 7 * day ) ) { return checkBlock.isEnabledSunday(); }
		return false;
	}

    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
    }



	public void editRepeatUntil(int id, long repeatUntil) {
		RingerSettingBlock editBlock = blocks.get(id);
		editBlock.setRepeatUntil(repeatUntil);
		reader.editEnd(id, repeatUntil);
		
	}
}
