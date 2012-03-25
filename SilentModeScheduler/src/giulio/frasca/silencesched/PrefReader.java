package giulio.frasca.silencesched;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefReader extends Activity {

	private final String PREF_FILE = "ncsusilencepreffile";
	private SharedPreferences settings;
	private int iterPos;
	
	public PrefReader(){
		settings  = getSharedPreferences(PREF_FILE,0);
		iterPos =0;
	}
	
	public void resetIteratorPosition(){
		resetIteratorPosition(0);
	}
	
	public void resetIteratorPosition(int i){
		this.iterPos=i;
	}
	
	public RingerSettingBlock getFirst(){
		if (getAlarmCount() == -1){
			return null;
		}
		return getBlock(0);
	}
	
	public boolean hasNext(){
		if (iterPos>=getAlarmCount()){
			return false;
		}
		return true;
	}
	
	public RingerSettingBlock getNext(){
		iterPos++;
		return getBlock(iterPos-1);
	}
	
	public boolean hasPrevious(){
		if (iterPos>0){
			return true;
		}
		return false;
	}
	
	public RingerSettingBlock getPrevious(){
		iterPos--;
		return getBlock(iterPos+1);
	}
	
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
	
	public RingerSettingBlock getBlock(int id){
		if (id>getAlarmCount() || id<0){
			return null;
		}
		return new RingerSettingBlock(getStart(id),getEnd(id),getRinger(id),id,getDays(id));
	}
	
	public void removeBlock(int id){
		Editor edit = settings.edit();
		edit.putBoolean(id+".enabled", false);
	}
	
	/**
	 * Gets the number of alarms stored in the file EVEN DISABLED ONES.  This is used for ID handout
	 * @return the number of alarms stored in the pref file, or -1 if undefined
	 */
	public int getAlarmCount(){
		return settings.getInt("alarmCount", -1);
		
	}
	
	public void incrementAlarmCount(){
		int alarms = settings.getInt("alarmCount",-1);
		Editor edit = settings.edit();
		edit.putInt("alarmCount", alarms+1);
		edit.commit();
	}
	
	public void decrementAlarmCount(){
		int alarms = settings.getInt("alarmCount",-1);
		Editor edit = settings.edit();
		edit.putInt("alarmCount", alarms-1);
		edit.commit();		
	}
	
	public void editStart(int id, long time){
		Editor edit = settings.edit();
		edit.putLong(id + ".start", time);
		edit.commit();
	}
	
	public void editEnd(int id, long time){
		Editor edit = settings.edit();
		edit.putLong(id + ".end", time);
		edit.commit();
	}
	
	public void editDays(int id, int days){
		Editor edit = settings.edit();
		edit.putInt(id + ".start", days);
		edit.commit();
	}
	
	public void editEnabled(int id, boolean enabled){
		Editor edit = settings.edit();
		edit.putBoolean(id + ".start", enabled);
		edit.commit();
	}
	
	public void editId(int id, int newId){
		//THIS SHOULD NEVER BE USED TO ACTUALLY 'EDIT' AN ID....ITS JUST HERE
		//TO FOLLOW CONVENTION AND ADD A NEW ID.
		Editor edit = settings.edit();
		edit.putLong(id + ".start", newId);
		edit.commit();
	}
	
	public void editRinger(int id, int ringer){
		Editor edit = settings.edit();
		edit.putLong(id + ".start", ringer);
		edit.commit();
	}
	
	public int getId(int id){
		return settings.getInt(id+".id", -1);
	}
	
	public long getStart(int id){
		return settings.getLong(id+".start", -1);
	}
	
	public long getEnd(int id){
		return settings.getLong(id+".end", -1);
	}
	
	public int getRinger(int id){
		return settings.getInt(id+".ringer", 0);
	}
	
	public boolean getEnabled(int id){
		return settings.getBoolean(id+".enabled", false);
	}
	
	public int getDays(int id){
		return settings.getInt(id+".days", 0);
	}
}
