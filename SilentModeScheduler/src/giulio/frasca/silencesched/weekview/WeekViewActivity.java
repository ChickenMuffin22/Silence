package giulio.frasca.silencesched.weekview;

import java.util.LinkedList;

import giulio.frasca.lib.TimeFunctions;
import giulio.frasca.silencesched.R;
import giulio.frasca.silencesched.RingerSettingBlock;
import giulio.frasca.silencesched.Schedule;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.Toast;

public class WeekViewActivity extends Activity{

	private TableRow sunRow,monRow,tueRow,wedRow,thuRow,friRow,satRow;
	private RadioButton sunRadio,monRadio,tueRadio,wedRadio,thuRadio,friRadio,satRadio;
	private DayBar sunBar,monBar,tueBar,wedBar,thuBar,friBar,satBar;
	private Schedule schedule;
	private final String PREF_FILE = "ncsusilencepreffile2";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekview);
        SharedPreferences settings = getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        schedule = new Schedule(settings);
        initComponents();
        loadDaySched(schedule);
    }
    
    public void loadDaySched(Schedule schedule){
    	LinkedList<RingerSettingBlock> allBlocks = schedule.getList();
    	sunBar.loadApplicableBlocks(allBlocks);
    	monBar.loadApplicableBlocks(allBlocks);
    	tueBar.loadApplicableBlocks(allBlocks);
    	wedBar.loadApplicableBlocks(allBlocks);
    	thuBar.loadApplicableBlocks(allBlocks);
    	friBar.loadApplicableBlocks(allBlocks);
    	satBar.loadApplicableBlocks(allBlocks);
    }
    
    public void initComponents(){
    	sunRow = (TableRow)findViewById(R.id.sunRow);
    	monRow = (TableRow)findViewById(R.id.monRow);
    	tueRow = (TableRow)findViewById(R.id.tueRow);
    	wedRow = (TableRow)findViewById(R.id.wedRow);
    	thuRow = (TableRow)findViewById(R.id.thuRow);
    	friRow = (TableRow)findViewById(R.id.friRow);
    	satRow = (TableRow)findViewById(R.id.satRow);
    	
    	sunRadio = (RadioButton)findViewById(R.id.sunRadio);
    	monRadio = (RadioButton)findViewById(R.id.monRadio);
    	tueRadio = (RadioButton)findViewById(R.id.tueRadio);
    	wedRadio = (RadioButton)findViewById(R.id.wedRadio);
    	thuRadio = (RadioButton)findViewById(R.id.thuRadio);
    	friRadio = (RadioButton)findViewById(R.id.friRadio);
    	satRadio = (RadioButton)findViewById(R.id.satRadio);
    	
    	sunBar = (DayBar)findViewById(R.id.sunBar);
    	monBar = (DayBar)findViewById(R.id.monBar);
    	tueBar = (DayBar)findViewById(R.id.tueBar);
    	wedBar = (DayBar)findViewById(R.id.wedBar);
    	thuBar = (DayBar)findViewById(R.id.thuBar);
    	friBar = (DayBar)findViewById(R.id.friBar);
    	satBar = (DayBar)findViewById(R.id.satBar);
    	
    	sunBar.setDayName("Sunday");
    	monBar.setDayName("Monday");
    	tueBar.setDayName("Tuesday");
    	wedBar.setDayName("Wednesday");
    	thuBar.setDayName("Thursday");
    	friBar.setDayName("Friday");
    	satBar.setDayName("Saturday");
    	
    	updateRadios();
    }
    
    
    public void updateRadios(){
    	long today = System.currentTimeMillis();
   		sunRadio.setChecked(TimeFunctions.isSunday(today));
   		monRadio.setChecked(TimeFunctions.isMonday(today));
   		tueRadio.setChecked(TimeFunctions.isTuesday(today));
   		wedRadio.setChecked(TimeFunctions.isWednesday(today));
   		thuRadio.setChecked(TimeFunctions.isThursday(today));
   		friRadio.setChecked(TimeFunctions.isFriday(today));
   		satRadio.setChecked(TimeFunctions.isSaturday(today));
   		
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
    
}
