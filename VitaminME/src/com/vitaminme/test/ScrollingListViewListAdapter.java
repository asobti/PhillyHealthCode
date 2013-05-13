package com.vitaminme.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitaminme.main.R;

public class ScrollingListViewListAdapter extends ArrayAdapter<String>
{
	private Context context;
	private int[] drawables;
	private String[] labels;

	public ScrollingListViewListAdapter(Context context, int[] drawables,
			String[] labels)
	{
		super(context, R.layout.scrolling_listview_items, labels);
		this.context = context;
		this.drawables = drawables;
		this.labels = labels;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.scrolling_listview_items,
				parent, false);

		ImageView iv1 = (ImageView) rowView.findViewById(R.id.sidebarImage);
		TextView tv1 = (TextView) rowView.findViewById(R.id.sidebarLabel);

		iv1.setImageResource(drawables[pos]);
		tv1.setText(labels[pos]);

		return rowView;
	}

}
