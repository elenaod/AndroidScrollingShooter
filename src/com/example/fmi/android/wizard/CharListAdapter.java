package com.example.fmi.android.wizard;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CharListAdapter extends BaseAdapter{

	private ArrayList<Player> chars;
	
	public CharListAdapter(ArrayList<Player> players)
	{
		chars = new ArrayList<Player>(players.size());
		
		for (int i = 0; i < players.size(); i++)
		{
			chars.add(players.get(i));
		}
	}
	@Override
	public int getCount() {
		return chars.size();
	}

	@Override
	public Player getItem(int index) {
		return chars.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		TextView textView = (TextView) convertView;
		
		if (textView == null) {
			final LayoutInflater inflator = LayoutInflater.from(parent
					.getContext());
			textView = (TextView) inflator.inflate(
					android.R.layout.simple_list_item_1, null);
		}
		
		textView.setText(getItem(index).toString());
		textView.setTextColor(Color.YELLOW);
		return textView;
	}

}
