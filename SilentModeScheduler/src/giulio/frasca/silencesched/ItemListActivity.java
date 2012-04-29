package giulio.frasca.silencesched;

import giulio.frasca.lib.Formatter;

import java.util.Iterator;
import java.util.LinkedList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;



public class ItemListActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		String[] values = new String[] { "Apple", "Banana", "Carrot", "Dog", "Eagle", "Fish", "Gorilla", "Hockey", "Indigo", "Java", "Kahn", "Anyway", "Star", "Trek", "Is", "Pretty", "Cool", "But", "I Suppose", "That's A Bit", "Of a Stereotype", "Coming from a Comp Sci student."};

		/**
		 * Use this line instead of the ItemListAdapter to not use the ItemListAdapter:
		   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.liststyle, R.id.label, values);
		 */
		
		// Using ItemListAdapter for custom icons
		ItemListAdapter adapter = new ItemListAdapter(this, values);
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
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}

}