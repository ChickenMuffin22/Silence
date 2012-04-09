package giulio.frasca.silencesched;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.*;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
//import android.view.*;
//import android.view.Gravity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.content.SharedPreferences;
import giulio.frasca.silencesched.exceptions.*;

public class SilentModeSchedulerActivity extends Activity {
	
	//private LinkedList<RingerSettingBlock> ringerSchedule;
	private Schedule schedule;
	// filename
	private final String PREF_FILE = "ncsusilencepreffile2";
	private int currentBlockId;
	
	//GUI components
	Button settingsButton,confirmButton,cancelButton,addButton,deleteButton;
	Spinner startSpinner,endSpinner,ringSpinner;
	Spinner alarmSpinner;
	EditText startHour,endHour,startMinute,endMinute,dateText;
	ToggleButton sunToggle,monToggle,tueToggle,wedToggle,thuToggle,friToggle,satToggle;
	
	//status vars
	boolean sunOn,monOn,tueOn,wedOn,thuOn,friOn,satOn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SharedPreferences settings = getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        clearPrefsForTesting(settings);
        schedule = new Schedule(settings);
        initComponents();
        int alarmCount = schedule.getAlarmCount();
        String [] alarms = new String[alarmCount];
        for (int i=0; i<alarmCount; i++){
        	alarms[i] = "Event " + i;
        }
        currentBlockId = 0;
        RingerSettingBlock first =schedule.getBlock(currentBlockId);
        updateInterface(first);
        
        //ringerSchedule = new LinkedList<RingerSettingBlock>();
    	//initRingerSched();
        //checkCurrentSetting();
        
    }
    
    /**
     * Formats the Name of the alarm for the spinner if the form SMTWTFS (start)-(end)
     * 
     * @param block - The block to format the time after
     * @return the string representation of this block
     */
    public String formatName(RingerSettingBlock block){
    	String retString = "";
    	boolean sunday = block.isEnabledSunday();
    	boolean monday = block.isEnabledMonday();
    	boolean tuesday = block.isEnabledTuesday();
    	boolean wednesday = block.isEnabledWednesday();
    	boolean thursday = block.isEnabledThursday();
    	boolean friday = block.isEnabledFriday();
    	boolean saturday = block.isEnabledSaturday();
    	String startTime = getHourOfTime(block.getStartTime())+":";
    	String endTime = getHourOfTime(block.getEndTime())+":";
    	if (getMinuteOfTime(block.getStartTime())>9){
    		startTime+=getMinuteOfTime(block.getStartTime());
    	}
    	else{
    		startTime+="0"+getMinuteOfTime(block.getStartTime());
    	}
    	if (getMinuteOfTime(block.getEndTime())>9){
    		endTime+=getMinuteOfTime(block.getEndTime());
    	}
    	else{
    		endTime+="0"+getMinuteOfTime(block.getEndTime());
    	}
    	if (isAM(block.getStartTime())){
    		startTime+="a";
    	}
    	else{
    		startTime+="p";
    	}
    	if (isAM(block.getEndTime())){
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
     * Updates the name spinner on the user interface
     */
    public void updateSpinner(){
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	LinkedList<RingerSettingBlock> list = schedule.getList();
    	Iterator<RingerSettingBlock> i = list.iterator();
    	boolean skippedFirst=false;
    	while (i.hasNext()){
    		RingerSettingBlock thisBlock = i.next();
    		if (skippedFirst && thisBlock.isEnabled() && thisBlock.getId()>=0){
    			adapter.add(formatName(thisBlock));
    		}
    		skippedFirst=true;
    	}
    	adapter.notifyDataSetChanged();
    	alarmSpinner.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    }
    
    /**
     * Updates the entire interface to display the settings for the current block
     * 
     * @param block - the block to update the GUI to
     */
    public void updateInterface(RingerSettingBlock block){
        startHour.setText(""+getHourOfTime(block.getStartTime()));
        endHour.setText(""+getHourOfTime(block.getEndTime()));
        startMinute.setText(minFormat(getMinuteOfTime(block.getStartTime())));
        endMinute.setText(minFormat(getMinuteOfTime(block.getEndTime())));
        ringSpinner.setSelection(block.getRingVal());
        //repeatDate
        int day = getDayFromTimestamp(block.getRepeatUntil());
        int month = getMonthFromTimestamp(block.getRepeatUntil());
        int year = getYearFromTimestamp(block.getRepeatUntil());
        dateText.setText(month+"/"+day+"/"+year);
        setDaysChecking(block);
        updateSpinner();
        
        if (isAM(block.getStartTime())){
        	startSpinner.setSelection(0);
        }
        else{
        	startSpinner.setSelection(1);
        }
        if (isAM(block.getEndTime())){
        	endSpinner.setSelection(0);
        }
        else{
        	endSpinner.setSelection(1);
        }
    }
    
    /**
     * Updates the toggle buttons on the user interface
     * 
     * @param block - the block to update the toggle buttons to match
     */
    private void setDaysChecking(RingerSettingBlock block) {
    	if (block.isEnabledSunday()){
			sunToggle.setChecked(true);
		}
		else{
			sunToggle.setChecked(false);
		}
		
		if (block.isEnabledMonday()){
			monToggle.setChecked(true);
		}
		else{
			monToggle.setChecked(false);
		}
		
		if (block.isEnabledTuesday()){
			tueToggle.setChecked(true);
		}
		else{
			tueToggle.setChecked(false);
		}
		if (block.isEnabledWednesday()){
			wedToggle.setChecked(true);
		}
		else{
			wedToggle.setChecked(false);
		}
		if (block.isEnabledThursday()){
			thuToggle.setChecked(true);
		}
		else{
			thuToggle.setChecked(false);
		}
		if (block.isEnabledFriday()){
			friToggle.setChecked(true);
		}
		else{
			friToggle.setChecked(false);
		}
		if (block.isEnabledSaturday()){
			satToggle.setChecked(true);
		}
		else{
			satToggle.setChecked(false);
		}
		
		
	}

    /**
     * Testing Method.  Clears the prefs file
     * 
     * @param settings - The SharedPreferences object that contains the user data
     */
	private void clearPrefsForTesting(SharedPreferences settings) {
    	Editor e = settings.edit().clear();
        for (int i=0; i<100; i++){
        	e.remove(i+".id");
        	e.remove(i+".start");
        	e.remove(i+".end");
        	e.remove(i+".ringer");
        	e.remove(i+".days");
        }
        e.remove("alarmCount");
        e.commit();
        logcatPrint("cleared");
		
	}

	/**
	 * Formats an int representin a minute to a string, such that there is a prepending '0' if the time is less than 10
	 * For use with GUI output.
	 * 
	 * @param input - the int for the time to check
	 * @return an outputable string representation of the minutes
	 */
	public String minFormat(int input){
    	if (input > 9) { return ""+input; }
    	else { return "0" + input ; }
    }
    
	/**
	 * Gets the hour of the given unix timestamp.
	 * 
	 * @param time - a long for milliseconds after the epoch, or ms after any midnight for that matter.
	 * @return The number of hours, in 12 hour format, after midnight.
	 */
    public int getHourOfTime(long time){
    	time-=5999;
    	int retHour=0;
    	long edittime=time;
    	while (edittime>60*60*1000){
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
    public boolean isAM(long time){
    	time-=5999;
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
    public int getMinuteOfTime(long time){
    	time-=5999;
    	if (time==0) { return 0; }
    	int retMin=0;
    	long editMin = time % (60 * 60 * 1000) ;
    	int oneMin = 1000 * 60;
    	while (editMin > 0){
    		editMin -= oneMin;
    		retMin++;
    	}
    	return retMin;
    }

    /**
     * Initializes all of the GUI components
     */
    public void initComponents(){
    	//the repeat until date text box
    	dateText = (EditText)findViewById(R.id.dateText);
    	//testing
    	dateText.setText("11/11/1111");
    	
    	//The toggle button for sunday
    	sunToggle = (ToggleButton)findViewById(R.id.sunToggle);
    	sunToggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){
    		
    		/**
    		 * Changes the value representing this date in the edit memory 
    		 */
			public void onCheckedChanged(CompoundButton sunButton, boolean isChecked) {
				sunOn=isChecked;
				
			}
    	});
    	
    	//The toggle button for monday
    	monToggle = (ToggleButton)findViewById(R.id.monToggle);
    	monToggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){
    		
    		/**
    		 * Changes the value representing this date in the edit memory 
    		 */
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				monOn=isChecked;
				
			}

    	});
    	
    	//The toggle button for tuesday
    	tueToggle = (ToggleButton)findViewById(R.id.tueToggle);
    	tueToggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){

    		/**
    		 * Changes the value representing this date in the edit memory 
    		 */
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				tueOn=isChecked;
				
			}
    	});
    	
    	//The toggle button for wednesday
    	wedToggle = (ToggleButton)findViewById(R.id.wedToggle);
    	wedToggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){

    		/**
    		 * Changes the value representing this date in the edit memory 
    		 */
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				wedOn=isChecked;
				
			}
    	});
    	
    	//The toggle button for thursday
    	thuToggle = (ToggleButton)findViewById(R.id.thuToggle);
    	thuToggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){

    		/**
    		 * Changes the value representing this date in the edit memory 
    		 */
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				thuOn=isChecked;
				
			}
    	});
    	
    	//The toggle button for friday
    	friToggle = (ToggleButton)findViewById(R.id.friToggle);
    	friToggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){

    		/**
    		 * Changes the value representing this date in the edit memory 
    		 */
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				friOn=isChecked;
				
			}
    	});
    	
    	//The toggle button for saturday
    	satToggle = (ToggleButton)findViewById(R.id.satToggle);
    	satToggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){

    		/**
    		 * Changes the value representing this date in the edit memory 
    		 */
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				satOn=isChecked;
				
			}
    	});
    	
    	//The settings button
        settingsButton = (Button)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Currently does not do anything
				toastMessage("Sorry this currently doesn't do anything");
				
			}
        	
        });
        
        //The confirm edit button
        confirmButton = (Button)findViewById(R.id.editEventButton);
        confirmButton.setOnClickListener(new OnClickListener(){

        	/**
        	 * If button is clicked, edit the current block permanently
        	 */
			public void onClick(View v) {
				if (inputValidates()){
					try{
						long startTime = getStartFromForm();
						long endTime= getEndFromForm();
						int ringer = getRinger();
						long repeatUntil = getRepeatFromForm();
						logcatPrint("repTil: "+ repeatUntil);
						schedule.editBlockDays(currentBlockId, schedule.formatDays(sunOn, monOn, tueOn, wedOn, thuOn,  friOn, satOn));
						schedule.editBlockStart(currentBlockId, startTime);
						schedule.editBlockEnd(currentBlockId, endTime);
						schedule.editBlockRinger(currentBlockId, ringer);
						schedule.editRepeatUntil(currentBlockId, repeatUntil);
						updateInterface(schedule.getBlock(currentBlockId));
						toastMessage("Current Block Editted");
					}
					catch (inputValidationError ive){
						toastMessage("Incorrect input formatting");
					}
				}
				
			}
        });

        //The add block button
        addButton = (Button)findViewById(R.id.addNewEventButton);
        addButton.setOnClickListener(new OnClickListener(){

        	/**
        	 * Adds a new block to the list if clicked
        	 */
			public void onClick(View v) {
				try {
					
					int id= schedule.addBlock(getStartFromForm(), getEndFromForm(), ringSpinner.getSelectedItemPosition(), 1111111, getRepeatFromForm());
					currentBlockId = id;
					updateInterface(schedule.getBlock(currentBlockId));
					toastMessage("New Block Added.  Now edit it and click 'confirm'");
				} catch (inputValidationError e) {
					toastMessage("Couldn't Add Block: Input Not Correctly Formatted");
				}
				
			}
        });
        
        //The cancel edit button
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener(){

        	/**
        	 * Discards any changes made to the UI and refreshes back to a stored state
        	 */
			public void onClick(View v) {
				
				RingerSettingBlock block =schedule.getBlock(currentBlockId);
				updateInterface(block);
				toastMessage("Current Changes Cancelled. Returning to Stored Block");
				
			}
        });

        //The delete alarm button
        deleteButton = (Button)findViewById(R.id.deleteEventButton);
        deleteButton.setOnClickListener(new OnClickListener(){
        	
        	/**
        	 * Deletes the current block, unless it is the default block, which cant be deleted
        	 */
			public void onClick(View v) {
				if (currentBlockId == 0){
					toastMessage("Cannot Delete: Default Ringer Setting Undeleteable.\n     Please edit instead");
					return;
				}
				//pick the next lowest block
				int decr=1;
				boolean deleted=false;
				while (schedule.hasBlock(currentBlockId-decr)){
					if (schedule.getBlock(currentBlockId-decr).isEnabled()){
						schedule.disableBlock(currentBlockId);
						currentBlockId=currentBlockId-decr;
						updateInterface(schedule.getBlock(currentBlockId));
						deleted=true;
						break;
					}
					decr++;
				}
				if (deleted){
					toastMessage("Current Block deleted. Now showing previous block");
				}
				else{
					toastMessage("Could not delete current block: Default setting is undeletable!");
				}
				
			}
        });

        //The hour of the start block text field
        startHour = (EditText)findViewById(R.id.startHour);
        //The minute of the start block text field
        startMinute = (EditText)findViewById(R.id.startMinute);
        //The hour of the end block text field
        endHour = (EditText)findViewById(R.id.endHour);
        //The minute of the end block text field
        endMinute = (EditText)findViewById(R.id.endMinute);
        
        //The am/pm spinner for the start of the current block
        startSpinner = (Spinner)findViewById(R.id.startSpinner);
        //The am/pm spinner for the end of the current block
        endSpinner = (Spinner)findViewById(R.id.endSpinner);
        //The ringer level for the current block
        ringSpinner = (Spinner)findViewById(R.id.ringSpinner);
        //The name spinner for the current block
        alarmSpinner = (Spinner)findViewById(R.id.alarmSpinner);
        
    }

    /**
     * Gets the start time from the UI
     * DO NOT USE...USE getStartFromForm() instead
     * as it has text validation
     * 
     * @return The start time shown in the user interface 
     */
    public long getStartTime(){
    	int hour = Integer.parseInt(startHour.getText().toString());
    	int min =  Integer.parseInt(startMinute.getText().toString());
    	boolean am = false;
    	if (startSpinner.getSelectedItemPosition() == 0) { am=true; } 
    	return formTimestamp(hour , min , am);
    }

    /**
     * Gets the end time from the UI
     * DO NOT USE...USE getEndFromForm() instead
     * as it has text validation
     * 
     * @return The end time shown in the user interface 
     */
    public long getEndTime(){
    	int hour = Integer.parseInt(endHour.getText().toString());
    	int min =  Integer.parseInt(endMinute.getText().toString());
    	boolean am = false;
    	if (endSpinner.getSelectedItemPosition() == 0) { am=true; } 
    	return formTimestamp(hour , min , am);
    	
    }
    
    /**
     * Gets the ringer setting from the UI
     * 
     * @return - The ringer level (0, 1, or 2) displayed on the UI
     */
    public int getRinger(){
    	return ringSpinner.getSelectedItemPosition();
    }
    
    /**
     * Ensure that the input validates correctly (only number or correctly formatted dates)
     * also displays toast message if there is an error
     * 
     * @return true if input is valid, false if not.  
     */
    public boolean inputValidates() {
    	String sHour = startHour.getText().toString();
    	String sMin = startMinute.getText().toString();
    	String eHour = endHour.getText().toString();
    	String eMin = endMinute.getText().toString();
    	String repeatUntil = dateText.getText().toString();
    	
    	Pattern minPattern = Pattern.compile("^\\d{2}$");
    	Matcher ms = minPattern.matcher(sMin);
    	Matcher me = minPattern.matcher(eMin);
    	Pattern hourPattern = Pattern.compile("^\\d{1,2}$");
    	Matcher hs = hourPattern.matcher(sHour);
    	Matcher he = hourPattern.matcher(eHour);
    	
    	Pattern datePattern = Pattern.compile("^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$");
    	Matcher d = datePattern.matcher(repeatUntil);

    	if (!d.matches()){
    		toastMessage("Date Not Formatted Correctly.\n            Use mm/dd/yyyy");
    		return false;
    	}
    	String[] dateArray = repeatUntil.split("/");
    	String month = dateArray[0];
    	String day = dateArray[1];
    	String year = dateArray[2];
    	if (Integer.parseInt(month)>12 || Integer.parseInt(month)<1){
    		toastMessage("Nonexistant Month Used:\n   Please use a number 1-12");
    		return false;
    	}
    	int dayInt = Integer.parseInt(day);
    	int monthInt = Integer.parseInt(month);
    	if (dayInt<0){
    		toastMessage("Nonexistant Day Used:\nPlease use a date that exists");
    		return false;
    	}
    	//30 day months
    	if ((monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11) && dayInt>30){
    		toastMessage("Nonexistant Day Used:\nPlease use a date that exists");
    		return false;
    	}
    	//february (counts leap years)
    	if (monthInt == 2 && ((Integer.parseInt(year)%4 == 0 && dayInt>29) || (Integer.parseInt(year)%4 != 0 && dayInt>28))){
    		toastMessage("Nonexistant Day Used:\nPlease use a date that exists");
    		return false;
    	}
    	//all other months
    	if (dayInt>31){
    		toastMessage("Nonexistant Day Used:\nPlease use a date that exists");
    		return false;
    	}
    	
    	
    	if (!ms.matches() || Integer.parseInt(sMin)<0 || Integer.parseInt(sMin)>59){
    		toastMessage("Start Minutes Not Formatted Correctly.\n              Must be a number 0-59");
    		return false;
    	}
    	if (!me.matches()|| Integer.parseInt(eMin)<0 || Integer.parseInt(eMin)>59){
    		toastMessage("End Minutes Not Formatted Correctly.\n              Must be a number 0-59");
    		return false;
    	}
    	if (!hs.matches() || Integer.parseInt(sHour)<1 || Integer.parseInt(sHour)>12){
    		toastMessage("Start Hour Not Formatted Correctly.\n        Must be a number 1-12");
    		return false;
    	}
    	if (!he.matches() || Integer.parseInt(eHour)<1 || Integer.parseInt(eHour)>12){
    		toastMessage("End Hour Not Formatted Correctly.\n        Must be a number 1-12");
    		return false;
    	}

    	return true;
    	
    }
    
    /**
     * Gets the start time from the UI, given that it is correctly formatted
     * 
     * @return the start time, in ms since midnight
     * @throws inputValidationError if the input is not correctly formatted
     */
    public long getStartFromForm() throws inputValidationError{
    	if (!inputValidates()){
    		throw new inputValidationError("Input is not in correct time format");
    	}
    	String sHour = startHour.getText().toString();
    	String sMin = startMinute.getText().toString();
  
    	
    	int sHourInt = Integer.parseInt(sHour);
    	int sMinInt = Integer.parseInt(sMin);
    	
    	
    	return formTimestamp(sHourInt, sMinInt, (startSpinner.getSelectedItemPosition()==0));
    }
    
    /**
     * Gets the end time from the UI, given that it is correctly formatted
     * 
     * @return the end time, in ms since midnight
     * @throws inputValidationError if the input is not correctly formatted
     */
    public long getEndFromForm() throws inputValidationError{
    	if (!inputValidates()){
    		throw new inputValidationError("Input is not in correct time format");
    	}
    	String eHour = endHour.getText().toString();
    	String eMin = endMinute.getText().toString();
    	
    	int eHourInt = Integer.parseInt(eHour);
    	int eMinInt = Integer.parseInt(eMin);
    
    	return formTimestamp(eHourInt, eMinInt, (endSpinner.getSelectedItemPosition()==0));
    }

    /**
     * Gets the repeat until timestamp from the UI
     * 
     * @return a long for ms after the epoch for when the given block no longer becomes valid
     * @throws inputValidationError if the input is not correclty formatted as mm/dd/yyyy
     */
    private long getRepeatFromForm() throws inputValidationError{
    	if (!inputValidates()){
    		throw new inputValidationError("Input is not in the correct date format");
    	}
		String date = dateText.getText().toString();
		String [] dateSplit = date.split("/");
		int month = Integer.parseInt(dateSplit[0])-1;
		int day   = Integer.parseInt(dateSplit[1]);
		int year  = Integer.parseInt(dateSplit[2]);
    	return componentTimeToTimestamp(year,month,day,0,0);
	}
    
    /**
     * Converts a human-readable date into a unix-y timestamp, in mmmm
     * @param hour the hours since 
     * @param minute
     * @param AM
     * @return
     */
    public long formTimestamp(int hour, int minute, boolean AM){
    	int retTime = 0;
    	if (hour == 12){ hour=0; }
    	if (!AM) { hour+=12; }

    	retTime += hour * 60 * 60 * 1000;
    	retTime += minute * 60 *1000;
    	retTime += 5999;
    	return retTime;
    }
    
    /**
     * Prints a temporary 'tooltip' style reminder on the GUI
     * 
     * @param msg - a short message to print
     */
    public void toastMessage(String msg){
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    
    /**
	 * Prints a logcat message with a customdebug tag
	 * 
	 * @param message - the message to include with the logcat packet
	 */
    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
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
    public long componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

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
    public int getYearFromTimestamp(long unixtime){
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
    public int getMonthFromTimestamp(long unixtime){
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
    public int getDayFromTimestamp(long unixtime){
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(unixtime);
    	return c.get(Calendar.DATE);
    }
    
//    private void initRingerSched() {
//		//SharedPreferences settings = getSharedPreferences(PREFS_FILE,0);
//		if ( settings.getBoolean("initialized",false) ){
//			//recreate ringer schedule
//			
//			long overflow = (7*24*60*60*1000)+1;
//			long start=settings.getLong("0_0_start",overflow);
//			long end=settings.getLong("0_0_end",overflow);
//			int ringer=settings.getInt("0_0_ringer",AudioManager.RINGER_MODE_VIBRATE);
//			for (int dayOfWeek=0; dayOfWeek<7; dayOfWeek++){
//				int eventNumber=0;
//				while (start <overflow){
//					start=settings.getLong(dayOfWeek+"_"+eventNumber+"_start", overflow);
//					end=settings.getLong(dayOfWeek+"_"+eventNumber+"_end", overflow);
//					ringer=settings.getInt(dayOfWeek+"_"+eventNumber+"_ringer", AudioManager.RINGER_MODE_VIBRATE);
//					ringerSchedule.add(new RingerSettingBlock(start,end,ringer));
//					eventNumber++;
//				}
//			}
//		}
//		else{
//			//create a default schedule event
//			//id like to have a setup popup at some point...
//			long day= 24*60*60*1000;
//			int ringer=((AudioManager)getSystemService(Context.AUDIO_SERVICE)).getRingerMode();
//			for (int i=0;i<7;i++){
//				
//				RingerSettingBlock defsched = new RingerSettingBlock(i*day,(i+1)*day,ringer);
//				ringerSchedule.add(defsched);
//				SharedPreferences.Editor e = settings.edit();
//				e.putLong(i+"_0_start", i*day);
//				e.putLong(i+"_0_end", (i+1)*day);
//				e.putInt(i+"_0_ringer", ringer);
//				e.putBoolean("initialized", true);
//				e.commit();
//			}
//			
//		}
//	}
//
//	private void checkCurrentSetting(){
//		long offset = 4*24*60*60*1000; //time between Jan 1 1970 and midnight on a sunday
//        long currentTime= System.currentTimeMillis()-offset; //this will set currentTime to time since midnight on sunday
//        int modulater= 7*24*60*60*1000;
//    	int targetMode = getTargetMode(currentTime % modulater);
//    	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//        int ringerMode = am.getRingerMode();
//        if (ringerMode != targetMode){
//        	am.setRingerMode(targetMode);
//        }
//	}
//    
//    private int getTargetMode(long currentTime){
//        Iterator<RingerSettingBlock> i = ringerSchedule.iterator();
//        while (i.hasNext()){
//        	RingerSettingBlock r = i.next();
//        	if (currentTime>=r.getStartTime() && currentTime<r.getEndTime()){
//        		return r.getRingVal();
//        	}
//        }
//        return ringerSchedule.getFirst().getRingVal();
//
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.settings_menu, menu);
//        return true;
//    }
}