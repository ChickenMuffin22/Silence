package giulio.frasca.silencesched;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SilenceBackgroundActivity extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        /* This is the important part */
        startService(new Intent(BackroundService.class.getName()));
    }
}
