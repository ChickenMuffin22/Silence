package giulio.frasca.silencesched;

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
import android.content.SharedPreferences;
import giulio.frasca.silencesched.exceptions.*;

public class SilentModeSchedulerActivity extends Activity {
	
	//private LinkedList<RingerSettingBlock> ringerSchedule;
	private Schedule schedule;
	// filename
	private final String PREF_FILE = "ncsusilencepreffile2";
	Button settingsButton,confirmButton,cancelButton,addButton,deleteButton;
	Spinner daySpinner,startSpinner,endSpinner,ringSpinner;
	Spinner alarmSpinner;
	EditText startHour,endHour,startMinute,endMinute;
	
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
        RingerSettingBlock first =schedule.getBlock(0);
        startHour.setText(""+getHourOfTime(first.getStartTime()));
        endHour.setText(""+getHourOfTime(first.getEndTime()));
        startMinute.setText(minFormat(getMinuteOfTime(first.getStartTime())));
        endMinute.setText(minFormat(getMinuteOfTime(first.getEndTime())));
        ringSpinner.setSelection(first.getRingVal());
        
        if (isAM(first.getStartTime())){
        	startSpinner.setSelection(0);
        }
        else{
        	startSpinner.setSelection(1);
        }
        if (isAM(first.getEndTime())){
        	endSpinner.setSelection(0);
        }
        else{
        	endSpinner.setSelection(1);
        }
        //ringerSchedule = new LinkedList<RingerSettingBlock>();
    	//initRingerSched();
        //checkCurrentSetting();
        
    }
    
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

	public String minFormat(int input){
    	if (input > 9) { return ""+input; }
    	else { return "0" + input ; }
    }
    
    public int getHourOfTime(long unixtime){
    	unixtime-=5999;
    	int retHour=0;
    	long edittime=unixtime;
    	while (edittime>60*60*1000){
    		edittime=edittime-60*60*1000;
    		retHour++;
    	}
    	retHour = retHour % 12;
    	if (retHour==0){ retHour=12; }
    	return retHour;
    }
    
    public boolean isAM(long unixtime){
    	unixtime-=5999;
    	if (unixtime < 12*60*60*1000){
    		return true;
    	}
    	return false;
    		
    }
    
    public int getMinuteOfTime(long unixtime){
    	unixtime-=5999;
    	if (unixtime==0) { return 0; }
    	int retMin=0;
    	long editMin = unixtime % (60 * 60 * 1000) ;
    	int oneMin = 1000 * 60;
    	while (editMin > 0){
    		editMin -= oneMin;
    		retMin++;
    	}
    	return retMin;
    }

    public void initComponents(){
        settingsButton = (Button)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	//stuff
        });
        
        
        confirmButton = (Button)findViewById(R.id.editEventButton);
        confirmButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	//stuff
        });
        
        addButton = (Button)findViewById(R.id.addNewEventButton);
        addButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				try {
					schedule.addBlock(getStartFromForm(), getEndFromForm(), ringSpinner.getSelectedItemPosition(), 1111111);
				} catch (inputValidationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
        	//stuff
        });
        
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				RingerSettingBlock first =schedule.getBlock(0);
		        startHour.setText(""+getHourOfTime(first.getStartTime()));
		        endHour.setText(""+getHourOfTime(first.getEndTime()));
		        startMinute.setText(minFormat(getMinuteOfTime(first.getStartTime())));
		        endMinute.setText(minFormat(getMinuteOfTime(first.getEndTime())));
		        ringSpinner.setSelection(first.getRingVal());
				
			}
        	//stuff
        });
        
        deleteButton = (Button)findViewById(R.id.deleteEventButton);
        deleteButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	//stuff
        });
        
        startHour = (EditText)findViewById(R.id.startHour);
        startMinute = (EditText)findViewById(R.id.startMinute);
        endHour = (EditText)findViewById(R.id.endHour);
        endMinute = (EditText)findViewById(R.id.endMinute);
        
        startSpinner = (Spinner)findViewById(R.id.startSpinner);
        endSpinner = (Spinner)findViewById(R.id.endSpinner);
        ringSpinner = (Spinner)findViewById(R.id.ringSpinner);
        daySpinner = (Spinner)findViewById(R.id.daySpinner);
        alarmSpinner = (Spinner)findViewById(R.id.alarmSpinner);
        
    }
    
    public boolean inputValidates(){
    	String sHour = startHour.getText().toString();
    	String sMin = startMinute.getText().toString();
    	String eHour = endHour.getText().toString();
    	String eMin = endMinute.getText().toString();
    	
    	Pattern minPattern = Pattern.compile("^\\d{2}$");
    	Matcher ms = minPattern.matcher(sMin);
    	Matcher me = minPattern.matcher(eMin);
    	Pattern hourPattern = Pattern.compile("^\\d{1,2}$");
    	Matcher hs = hourPattern.matcher(sHour);
    	Matcher he = hourPattern.matcher(eHour);
    	
    
    	if ( !ms.matches() || !me.matches() || !hs.matches() | !he.matches() ){
    		return false;
    	}
    	int sHourInt = Integer.parseInt(sHour);
    	int eHourInt = Integer.parseInt(eHour);
    	int sMinInt = Integer.parseInt(sMin);
    	int eMinInt = Integer.parseInt(eMin);
    	
    	if ( sHourInt > 12 || sHourInt < 1){
    		return false;
    	}
    	if ( eHourInt > 12 || eHourInt < 1){
    		return false;
    	}
    	if ( sMinInt > 59 || sMinInt < 0){
    		return false;
    	}
    	if ( eMinInt > 59 || eMinInt < 0){
    		return false;
    	}
    	return true;
    	
    }
    
    
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
    
    public long formTimestamp(int hour, int minute, boolean AM){
    	int retTime = 0;
    	if (hour == 12){ hour=0; }
    	if (!AM) { hour+=12; }

    	retTime += hour * 60 * 60 * 1000;
    	retTime += minute * 60 *1000;
    	retTime += 59999;
    	return retTime;
    }
    
    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
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