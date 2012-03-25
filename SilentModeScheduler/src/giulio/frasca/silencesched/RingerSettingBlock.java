package giulio.frasca.silencesched;

import android.media.AudioManager;

public class RingerSettingBlock {
	private long startTime;
	private long endTime;
	private int ringVal;
	private int id;
	//to use this, set 1 as 'on' and 0 as 'off' on a 7 digit number
	//i.e. 1001101 is for sunday, wednesday, thursday, and saturday
	private int days;
	private boolean enabled;

	public RingerSettingBlock(long minTime, long maxTime, int ringerValue, int id, int days) {
		this.enabled=true;
		this.startTime=minTime;
		this.endTime=maxTime;
		this.ringVal=ringerValue;
		this.id=id;
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
	
	public RingerSettingBlock(int ringerValue, int id, int days){
		new RingerSettingBlock(0,7*24*60*60*1000,ringerValue,id,days);
	}
	
	public RingerSettingBlock(int id, int days){
		new RingerSettingBlock(0,7*24*60*60*1000,AudioManager.RINGER_MODE_VIBRATE, id, days);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getRingVal() {
		return ringVal;
	}

	public void setRingVal(int ringVal) {
		this.ringVal = ringVal;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the days
	 */
	public int getDays() {
		return days;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(int days) {
		this.days = days;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	


}
