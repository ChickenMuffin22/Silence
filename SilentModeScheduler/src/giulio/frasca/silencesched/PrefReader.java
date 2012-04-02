package giulio.frasca.silencesched;


import android.content.*;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.util.Log;

/**
 * Reads the preference file to create new settings blocks to load into device memory.
 * This also creates an iterator interface such that all saved schedule blocks can be easily
 * loaded into a list data structure.
 * 
 * @author Giulio Frasca
 *
 */
public class PrefReader{

	// the preference reader
	private SharedPreferences settings;
	//the iterator pointer
	private int iterPos;
	
	/**
	 * Standard constructor, initiates the preference file reader
	 * and resets the iterator pointer to the first block
	 */
	public PrefReader(SharedPreferences settings){
		this.settings  = settings;
		iterPos =0;
	}
	
	/**
	 * Resets the iterator pointer to the first block
	 */
	public void resetIteratorPosition(){
		//just move the iterator position to 0
		moveIteratorPosition(0);
	}
	
	/**
	 * Moves the iterator pointer to the specified block
	 * 
	 * @param destination - the block id to move the iterator pointer to
	 */
	public void moveIteratorPosition(int destination){
		this.iterPos=destination;
	}
	
	/**
	 * Gets the first RingerSettingBlock
	 * 
	 * @return the first block in formated (RingerSettingBlock) mode
	 */
	public RingerSettingBlock getFirst(){
		//if no blocks exist, return null
		if (getAlarmCount() <= 0){
			logcatPrint("No blocks exist");
			addBlock(0,(24*60*60*1000)-1,AudioManager.RINGER_MODE_NORMAL,111111);
			addBlock(0,(24*60*60*1000)-60000+1,AudioManager.RINGER_MODE_NORMAL,111111);
		}
		//otherwise, return the first block
		return getBlock(0);
	}
	
	/**
	 * Checks if there is another ringer setting block after the pointer that is available to be loaded
	 * 
	 * @return boolean for whether or not another block is available 
	 */
	public boolean hasNext(){
		//if the pointer is already at the alarm count, then there are no more left.
		if (iterPos>=getAlarmCount() && iterPos>=-1 ){
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the next ringer block
	 * 
	 * @return a RingerSettingBlock object that is loaded, or null if another doesn't exist
	 */
	public RingerSettingBlock getNext(){
		if (hasNext()){
			iterPos++;
			return getBlock(iterPos-1);
		}
		return null;
	}
	
	/**
	 * Checks if there is there a block that can be loaded BEFORE the pointer
	 * 
	 * @return boolean for whether or not another block is available
	 */
	public boolean hasPrevious(){
		
		if (iterPos>0 && iterPos<=(getAlarmCount()+1)){
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the previous ringer block
	 * 
	 * @return a RingerSettingBlock object that is loaded, or null if another doesn't exist
	 */
	public RingerSettingBlock getPrevious(){
		if (hasPrevious()){
			iterPos--;
			return getBlock(iterPos+1);
		}
		return null;
	}
	
	/**
	 * Adds a ringer block to the stored pref file
	 * 
	 * @param start - the start time for the alarm
	 * @param end - the end time for the alarm
	 * @param ringer - the ring level for the alarm
	 * @param days - the days for the alarm
	 * @return the ID number given to the block
	 */
	public int addBlock(long start, long end, int ringer, int days){
		int id = getAlarmCount();
		incrementAlarmCount();
		editId(id,id);
		editStart(id,start);
		editEnd(id,end);
		editRinger(id,ringer);
		editEnabled(id,true);
		editDays(id,days);
		return id;
	}
	
	/**
	 * Retrieves a block from the pref file
	 * 
	 * @param id - the id of the alarm requested
	 * @return a new RingerSeetingBlock object representative of that ID, or null if the ID does not exist.
	 */
	public RingerSettingBlock getBlock(int id){
		if (id>getAlarmCount() || id<0){
			return null;
		}
		logcatPrint("ID: " + id);
		logcatPrint("start: " + getStart(id));
		logcatPrint("end: " + getEnd(id));
		logcatPrint("ringer: "+ getRinger(id));
		logcatPrint("days: " + getDays(id));
		return new RingerSettingBlock(getStart(id),getEnd(id),getRinger(id),id,getDays(id));
	}
	
	/**
	 * Removes an alarm.  Note that this doesn't actually remove it, but just disables it.
	 * 
	 * @param id - the alarm to 'remove'
	 */
	public void removeBlock(int id){
		Editor edit = settings.edit();
		edit.putBoolean(id+".enabled", false);
	}
	
	/**
	 * Gets the number of alarms stored in the file EVEN DISABLED ONES.  This is used for ID handout
	 * @return the number of alarms stored in the pref file, or -1 if undefined
	 */
	public int getAlarmCount(){
		int count =settings.getInt("alarmCount", -1);
		if (count == -1){
			Editor e = settings.edit();
			e.putInt("alarmCount", -1);
			e.commit();
		}
		return count;
		
	}
	
	/**
	 * Increments the alarm counter.
	 * For use with adding blocks
	 */
	private void incrementAlarmCount(){
		int alarms = settings.getInt("alarmCount",-1);
		Editor edit = settings.edit();
		edit.putInt("alarmCount", alarms+1);
		edit.commit();
	}
	
	/**
	 * Decrements the alarm counter.
	 * Never really used, since 'removed' alarms are just disabled.
	 */
	@SuppressWarnings("unused")
	private void decrementAlarmCount(){
		int alarms = settings.getInt("alarmCount",-1);
		Editor edit = settings.edit();
		edit.putInt("alarmCount", alarms-1);
		edit.commit();		
	}
	
	/**
	 * Edits the start time for an alarm in the pref file
	 * 
	 * @param id - the alarm to edit
	 * @param time - the time to change to
	 */
	public void editStart(int id, long time){
		Editor edit = settings.edit();
		edit.putLong(id + ".start", time);
		edit.commit();
	}
	
	/**
	 * Edits the end time for an alarm in the pref file
	 * 
	 * @param id - the alarm to edit
	 * @param time - the time to change to
	 */
	public void editEnd(int id, long time){
		Editor edit = settings.edit();
		edit.putLong(id + ".end", time);
		edit.commit();
	}
	
	/**
	 * Edits the days specifier for an alarm in the pref file
	 * 
	 * @param id - the alarm to edit
	 * @param days - the day specifier in the pref file
	 */
	public void editDays(int id, int days){
		Editor edit = settings.edit();
		edit.putInt(id + ".days", days);
		edit.commit();
	}
	
	/**
	 * Edits whether the alarm is enabled or disabled in the pref file
	 * 
	 * @param id - the alarm to edit
	 * @param enabled - should the alarm be enabled?
	 */
	public void editEnabled(int id, boolean enabled){
		Editor edit = settings.edit();
		edit.putBoolean(id + ".enabled", enabled);
		edit.commit();
	}
	
	/**
	 * Edits the ID of an alarm in the pref file.  This really should never be used...
	 * 
	 * @param id - the id of the alarm to edit
	 * @param newId - the new id of the alarm
	 */
	public void editId(int id, int newId){
		//THIS SHOULD NEVER BE USED TO ACTUALLY 'EDIT' AN ID....ITS JUST HERE
		//TO FOLLOW CONVENTION AND ADD A NEW ID.
		Editor edit = settings.edit();
		edit.putInt(id + ".id", newId);
		edit.commit();
	}
	
	/**
	 * Edits the ringer level of an alarm in the pref file
	 * 
	 * @param id - the id of the alarm to edit
	 * @param ringer - the ringer level of the alarm
	 */
	public void editRinger(int id, int ringer){
		Editor edit = settings.edit();
		edit.putInt(id + ".ringer", ringer);
		edit.commit();
	}
	
	/**
	 * Gets the ID of an alarm in the pref file.  No idea why you'd every use this...
	 * 
	 * @param id - the id of the target alarm
	 * @return the id of the target alarm
	 */
	public int getId(int id){
		return settings.getInt(id+".id", -1);
	}
	
	/**
	 * Gets the Start time of an alarm in the pref file.
	 * 
	 * @param id - the id of the target alarm
	 * @return the start time of the target alarm
	 */
	public long getStart(int id){
		return settings.getLong(id+".start", -1);
	}
	
	/**
	 * Gets the End time of an alarm in the pref file
	 * 
	 * @param id - the id of the target alarm
	 * @return the end time of the target alarm
	 */
	public long getEnd(int id){
		return settings.getLong(id+".end", -1);
	}
	
	/**
	 * Gets the ringer level of an alarm in the pref file
	 * 
	 * @param id - the id of the target alarm
	 * @return the ringer level of the target alarm
	 */
	public int getRinger(int id){
		return settings.getInt(id+".ringer", 0);
	}
	
	/**
	 * Gets the enabled/disabled status of an alarm in the pref file
	 * 
	 * @param id - the id of the target alarm
	 * @return boolean for if the alarm is enabled.
	 */
	public boolean getEnabled(int id){
		return settings.getBoolean(id+".enabled", false);
	}
	
	/**
	 * Gets the days specifier of an alarm in the pref file
	 * 
	 * @param id - the id of the target alarm
	 */
	public int getDays(int id){
		return settings.getInt(id+".days", 0);
	}
	
    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
    }
}
