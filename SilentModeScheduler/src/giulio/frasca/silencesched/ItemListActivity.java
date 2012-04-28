package giulio.frasca.silencesched;

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
		// Use your own layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.liststyle, R.id.label, values);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}

}