package giulio.frasca.silencesched;

import giulio.frasca.lib.TimeFunctions;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BackroundService extends Service {

	private final long UPDATE_TIME=15*1000L;
	private static final int HELLO_ID = 1;
	private final String PREF_FILE = "ncsusilencepreffile2";

	
	
	private Timer timer;
	
	private TimerTask updateTask = new TimerTask() {
	    @Override
	    public void run() {
	    	AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	    	am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	    	SharedPreferences settings = getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
	    	Schedule s = new Schedule(settings);
	    	
	    	long currentTime = System.currentTimeMillis();
	    	LinkedList<RingerSettingBlock> list = s.getList();
	    	Iterator<RingerSettingBlock> i = list.iterator();
	    	while (i.hasNext()){
	    		RingerSettingBlock block = i.next();
	    		if (block.isEnabled() && isEnabledToday(block) && isWithinTimeBlock(block)){
	    			int newRingerLevel = block.getRingVal();
	    			if (newRingerLevel != am.getRingerMode()){
	    				am.setRingerMode(newRingerLevel);
	    				logcatPrint("CHANGING LEVEL");
	    			}
	    			else{
	    				logcatPrint("Ringer Level already the same!");
	    			}
	    		}
	    	}
	    	
	    	
	    	
	      //TODO heres where the work needs to be done
	    	logcatPrint(""+list.size());
	    }

		private boolean isWithinTimeBlock(RingerSettingBlock block) {
			long todayTimestamp = System.currentTimeMillis();
			long now = TimeFunctions.timeSinceMidnight(todayTimestamp);
			long start = block.getStartTime();
			long end = block.getEndTime();
			if ((start<now) && (now<end)){
				return true;
			}
			return false;
		}

		private boolean isEnabledToday(RingerSettingBlock block) {
			long now = System.currentTimeMillis();
			int dayOfWeek = TimeFunctions.getDayofWeekFromTimestamp(now);
			if (dayOfWeek == Calendar.SUNDAY){
				return block.isEnabledSunday();
			}
			if (dayOfWeek == Calendar.MONDAY){
				return block.isEnabledMonday();
			}
			if (dayOfWeek == Calendar.TUESDAY){
				return block.isEnabledTuesday();
			}
			if (dayOfWeek == Calendar.WEDNESDAY){
				return block.isEnabledWednesday();
			}
			if (dayOfWeek == Calendar.THURSDAY){
				return block.isEnabledThursday();
			}
			if (dayOfWeek == Calendar.FRIDAY){
				return block.isEnabledFriday();
			}
			if (dayOfWeek == Calendar.SATURDAY){
				return block.isEnabledSaturday();
			}
			return false;
		}
	  };
	
//	public BackroundService(){
//		this.lastUpdated=System.currentTimeMillis();
//		toastMessage("hello world");
//		toastMessage("last Updated:" + lastUpdated);
//	}
	
	@Override
	public void onCreate(){
		 super.onCreate();
		 logcatPrint("Service creating");
		 String ns = Context.NOTIFICATION_SERVICE;
	    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
	    	
	    	int icon = R.drawable.ic_launcher;
	    	CharSequence tickerText = "Service Started";
	    	long when = System.currentTimeMillis();
	    	
	    	Notification notification = new Notification(icon, tickerText, when);
	    	
	    	Context context = getApplicationContext();
	    	CharSequence contentTitle = "Silence";
	    	CharSequence contentText = "Service Running";
	    	Intent notificationIntent = new Intent(this, BackroundService.class);
	    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

	    	notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	    	notification.flags |= Notification.FLAG_ONGOING_EVENT;
	    	notification.flags |= Notification.FLAG_NO_CLEAR;
	    	
	    	mNotificationManager.notify(HELLO_ID, notification);
	    	
		 
		 timer = new Timer("TweetCollectorTimer");
		 timer.schedule(updateTask, 0L , UPDATE_TIME);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	    logcatPrint("Service destroying");
	    
	    String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
	    mNotificationManager.cancel(HELLO_ID);
	    
	    timer.cancel();
	    timer = null;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
    }
    /**
     * Prints a temporary 'tooltip' style reminder on the GUI
     * 
     * @param msg - a short message to print
     */
    public void toastMessage(String msg){
    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
