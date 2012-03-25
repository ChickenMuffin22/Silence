package giulio.frasca.silencesched;

import java.util.Iterator;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
//import android.view.*;
//import android.view.Gravity;
import android.view.Menu;
//import android.view.MenuInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.content.SharedPreferences;

public class SilentModeSchedulerActivity extends Activity {
	
	private LinkedList<RingerSettingBlock> ringerSchedule;
	public final String PREFS_FILE = "SilentModeSchedPrefs";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ringerSchedule = new LinkedList<RingerSettingBlock>();
    	initRingerSched();
        checkCurrentSetting();
        Button settingsButton = (Button)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	//stuff
        });
        
    }
    
    private void initRingerSched() {
		SharedPreferences settings = getSharedPreferences(PREFS_FILE,0);
		if ( settings.getBoolean("initialized",false) ){
			//recreate ringer schedule
			
			long overflow = (7*24*60*60*1000)+1;
			long start=settings.getLong("0_0_start",overflow);
			long end=settings.getLong("0_0_end",overflow);
			int ringer=settings.getInt("0_0_ringer",AudioManager.RINGER_MODE_VIBRATE);
			for (int dayOfWeek=0; dayOfWeek<7; dayOfWeek++){
				int eventNumber=0;
				while (start <overflow){
					start=settings.getLong(dayOfWeek+"_"+eventNumber+"_start", overflow);
					end=settings.getLong(dayOfWeek+"_"+eventNumber+"_end", overflow);
					ringer=settings.getInt(dayOfWeek+"_"+eventNumber+"_ringer", AudioManager.RINGER_MODE_VIBRATE);
					ringerSchedule.add(new RingerSettingBlock(start,end,ringer));
					eventNumber++;
				}
			}
		}
		else{
			//create a default schedule event
			//id like to have a setup popup at some point...
			long day= 24*60*60*1000;
			int ringer=((AudioManager)getSystemService(Context.AUDIO_SERVICE)).getRingerMode();
			for (int i=0;i<7;i++){
				
				RingerSettingBlock defsched = new RingerSettingBlock(i*day,(i+1)*day,ringer);
				ringerSchedule.add(defsched);
				SharedPreferences.Editor e = settings.edit();
				e.putLong(i+"_0_start", i*day);
				e.putLong(i+"_0_end", (i+1)*day);
				e.putInt(i+"_0_ringer", ringer);
				e.putBoolean("initialized", true);
				e.commit();
			}
			
		}
	}

	private void checkCurrentSetting(){
		long offset = 4*24*60*60*1000; //time between Jan 1 1970 and midnight on a sunday
        long currentTime= System.currentTimeMillis()-offset; //this will set currentTime to time since midnight on sunday
        int modulater= 7*24*60*60*1000;
    	int targetMode = getTargetMode(currentTime % modulater);
    	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        if (ringerMode != targetMode){
        	am.setRingerMode(targetMode);
        }
	}
    
    private int getTargetMode(long currentTime){
        Iterator<RingerSettingBlock> i = ringerSchedule.iterator();
        while (i.hasNext()){
        	RingerSettingBlock r = i.next();
        	if (currentTime>=r.getStartTime() && currentTime<r.getEndTime()){
        		return r.getRingVal();
        	}
        }
        return ringerSchedule.getFirst().getRingVal();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
}