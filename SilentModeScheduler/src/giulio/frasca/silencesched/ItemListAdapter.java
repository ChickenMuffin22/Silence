package giulio.frasca.silencesched;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter primarily used to set the icon next to each item in the list to an
 * red X or a green check
 */

public class ItemListAdapter extends ArrayAdapter<String> {
	private final Context context;

	// will likely need to be changed away from a String
	private final String[] values;

	public ItemListAdapter(Context context, String[] values) {
		super(context, R.layout.liststyle, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.liststyle, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(values[position]);

		// Determines if icon next to item is a check or an X
		// temporarily only checking if the string starts with S
		String s = values[position];
		if (s.startsWith("S")) {
			imageView.setImageResource(R.drawable.redxmed);
		} else {
			imageView.setImageResource(R.drawable.greencheckmed);
		}

		return rowView;
	}
}