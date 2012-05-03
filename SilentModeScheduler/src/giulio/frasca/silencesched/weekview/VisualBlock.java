package giulio.frasca.silencesched.weekview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class VisualBlock extends View implements View.OnClickListener{

	private long startPixel;
	private long endPixel;
	private int ringLevel;
	private long height;
	private int id;
	
	   public VisualBlock(Context context){
		   super(context);
		   setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				logcatPrint("ID "+id+" selected");
				
			}
			   
		   });
		   setOnTouchListener(new OnTouchListener(){

			public boolean onTouch(View arg0, MotionEvent arg1) {
				arg0.onTouchEvent(arg1);
				logcatPrint("ID "+id+" selected");
				return false;
			}
			   
		   });
	   }
	   
	   public void setAttributes(int id, long startPixel, long endPixel, long height, int ringLevel){
		   this.startPixel=startPixel;
		   this.endPixel=endPixel;
		   this.ringLevel=ringLevel;
		   this.height=height;
		   this.id=id;
	   }
	   
	   @Override
	   public void onDraw(Canvas c){
		   c.drawRect(startPixel,0,endPixel,height-5,getPaintColorForLevel(ringLevel));
	   }
	   
	
	   public Paint getPaintColorForLevel(int ringLevel){
	    	Paint red= new Paint();
	    	red.setColor(Color.rgb(178,34,34));
	    	red.setStrokeWidth(10L);
	    	Paint yellow= new Paint();
	    	yellow.setColor(Color.rgb(255,185,0));
	    	yellow.setStrokeWidth(10L);
	    	Paint green = new Paint();
	    	green.setColor(Color.rgb(0, 100 ,0));
	    	green.setStrokeWidth(10L);
	    	if (ringLevel == AudioManager.RINGER_MODE_NORMAL){
	    		return green;
	    	}
	    	if (ringLevel == AudioManager.RINGER_MODE_VIBRATE){
	    		return yellow;
	    	}
	    	return red;
	    }
	   
	    public void logcatPrint(String message){
	    	Log.v("customdebug",message + " | sent from " +this.getClass().getSimpleName());
	    }

		public void onClick(View v) {
			logcatPrint("ID "+id+" selected");
			
		}
		
		public boolean contains(float x, float y){
			if (x>startPixel && x<endPixel && y>0 && y<height ){
				return true;
			}
			return false;
		}
		
		public int getId(){
			return id;
		}
}
