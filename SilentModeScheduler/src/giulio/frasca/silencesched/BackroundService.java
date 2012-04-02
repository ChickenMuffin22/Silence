package giulio.frasca.silencesched;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackroundService extends Service {

	
	public BackroundService(){
		System.out.print("hello world");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
    }
}
