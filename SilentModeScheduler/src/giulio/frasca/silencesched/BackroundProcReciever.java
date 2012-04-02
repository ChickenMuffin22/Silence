package giulio.frasca.silencesched;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BackroundProcReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent = new Intent("giulio.frasca.silencesched.BackroundService");
            context.startService(serviceIntent);
        }
    }
    
    public void logcatPrint(String message){
    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
    }
}
