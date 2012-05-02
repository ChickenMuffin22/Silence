package giulio.frasca.silencesched;

import giulio.frasca.lib.Formatter;
import giulio.frasca.silencesched.weekview.WeekViewActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ListActivity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;


public class ItemListActivity extends ListActivity {
	
	private Schedule schedule;
	private final String PREF_FILE = "ncsusilencepreffile2";
	private LinkedList<RingerSettingBlock> list;
	private LinkedList<RingerSettingBlock> undeletedList;
	private HashMap<Integer,Integer> nameDictionary;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nameDictionary=new HashMap<Integer,Integer>();
		SharedPreferences settings = getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        schedule = new Schedule(settings);

        undeletedList = schedule.getList();
        list =new LinkedList<RingerSettingBlock>();
        for (int i=0; i<undeletedList.size();i++){
        	if (!undeletedList.get(i).isDeleted()){
        		list.add(undeletedList.get(i));
        	}
        }
        
       	int size = list.size();
    	
       	// events stores the Strings of the items in the list
		String[] events = new String[size];
		int listPosition=0;
    	for (int j = 0; j < size; j++){
    		RingerSettingBlock thisBlock = list.get(j);
    		if (!thisBlock.isDeleted()){
    			String name = thisBlock.getName();	
    			events[j] = name;
    			nameDictionary.put(listPosition,j);
    			listPosition++;
    		}
    	}

		/**
		 * Use this line instead of the ItemListAdapter to not use the ItemListAdapter:
		   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.liststyle, R.id.label, events);
		 */

		// Using ItemListAdapter for custom icons
    	ItemListAdapter adapter = new ItemListAdapter(this, events, list);
		setListAdapter(adapter);
		
	}

	
	/**
	 *  
	 *  Code used for the spinner:
	 *  
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	LinkedList<RingerSettingBlock> list = schedule.getList();
    	Iterator<RingerSettingBlock> i = list.iterator();
    	nameDictionary.clear();
    	nameDictionaryReverse.clear();
    	boolean skippedFirst=true;
    	int mapID=0;
    	while (i.hasNext()){
    		RingerSettingBlock thisBlock = i.next();
    		if (skippedFirst && thisBlock.isEnabled() && thisBlock.getId()>=0){
    			nameDictionary.put(mapID, thisBlock.getId());
    			nameDictionaryReverse.put(thisBlock.getId(),mapID);
    			adapter.add(Formatter.formatName(thisBlock));
    			mapID++;
    		}
    		skippedFirst=true;
    	}
    	adapter.notifyDataSetChanged();
    	alarmSpinner.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
	 */
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		RingerSettingBlock selectedBlock = list.get(nameDictionary.get(position));
		//String description = "is sunday?" + list.get(nameDictionary.get(position)).isEnabledSunday();
		 Intent i = new Intent(this, EditEventActivity.class);
         Bundle params = new Bundle();
         params.putInt("selected", selectedBlock.getId());
         i.putExtras(params);
         this.startActivityForResult(i,22);
         toastMessage("returned");
         this.onCreate(new Bundle());
		
		
		//Toast.makeText(this, description +  item + " selected", Toast.LENGTH_LONG).show();
	}
	
	 public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.settings_menu, menu);
	        return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	            case R.id.menuBar:
	                Intent i = new Intent(this, WeekViewActivity.class);
	                Bundle params = new Bundle();
	                i.putExtras(params);
	                this.startActivity(i);
	                return true;
	            case R.id.exitActivityOption:
	            	finish();
	            	return true;
	            case R.id.addMenuOption:
	            	addBlock();
	            	//add events stuff
	            	return true;
	            case R.id.serviceToggleOption:
	            	//service toggle stuff
	            	toggleSerivce();
	            	return true;
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }


		private void addBlock() {
			int id = schedule.addBlock(1*60*60*1000, 2*60*60*1000, AudioManager.RINGER_MODE_NORMAL, 1111111, 253402300799000L , "Untitled Setting", false, true);
			Intent i = new Intent(this, EditEventActivity.class);
	         Bundle params = new Bundle();
	         params.putInt("selected", id);
	         i.putExtras(params);
	         this.startActivityForResult(i,22);
			
		}


		private void toggleSerivce() {
			boolean serviceRunning=false;
			ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
			List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(Integer.MAX_VALUE);
			if (!(serviceList.size()>0)){
				serviceRunning=false;
			}
			for ( int i=0;i<serviceList.size();i++){
				RunningServiceInfo serviceInfo = serviceList.get(i);
				ComponentName serviceName = serviceInfo.service;
				if (serviceName.getClassName().equals("giulio.frasca.silencesched.BackroundService")){
					serviceRunning = true;
				}
			}
			
			
			// TODO Auto-generated method stub
			if (!serviceRunning){
				serviceRunning=true;
				startService(new Intent(BackroundService.class.getName()));
				
			}
			else{
				serviceRunning=false;
				stopService(new Intent(BackroundService.class.getName()));
			}
		}
	    /**
	     * Prints a temporary 'tooltip' style reminder on the GUI
	     * 
	     * @param msg - a short message to print
	     */
	    public void toastMessage(String msg){
	    	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	    }
	    
	    public void onActivityResult (int requestCode, int resultCode, Intent data){
	    	this.onCreate(new Bundle());
	    }
}