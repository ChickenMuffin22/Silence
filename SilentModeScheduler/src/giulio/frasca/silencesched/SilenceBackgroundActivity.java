package giulio.frasca.silencesched;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SilenceBackgroundActivity extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toastMessage("started service");
        /* This is the important part */
        startService(new Intent(BackroundService.class.getName()));
    }
	
	 public void toastMessage(String msg){
	    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	 }
}
