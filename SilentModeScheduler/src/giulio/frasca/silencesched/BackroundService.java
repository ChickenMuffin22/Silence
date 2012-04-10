package giulio.frasca.silencesched;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BackroundService extends Service {

	private final long UPDATE_TIME=15*1000;
	
	private long lastUpdated;
	
	private Timer timer;
	
	private TimerTask updateTask = new TimerTask() {
	    @Override
	    public void run() {
	      //TODO heres where the work needs to be done
	    	logcatPrint("doin work");
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
		 
		 timer = new Timer("TweetCollectorTimer");
		 timer.schedule(updateTask, 1000L, 6 * 1000L);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	    logcatPrint("Service destroying");
	 
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
