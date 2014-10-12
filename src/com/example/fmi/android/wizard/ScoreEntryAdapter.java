package com.example.fmi.android.wizard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreEntryAdapter extends BaseAdapter{

	private ScoreEntry [] results;
	
	public ScoreEntryAdapter()
	{
		results = new ScoreEntry [10];
		
		for (int i = 0; i < 10; i++)
		{
			results [i] = new ScoreEntry();
		}
	}
	
	public ScoreEntryAdapter(ScoreEntry [] arr)
	{
		results = new ScoreEntry [arr.length];
		
		for (int i = 0; i < arr.length; i++)
		{
			results[i] = new ScoreEntry(arr[i]);
		}
	}
	@Override
	public int getCount() {
		return results.length;
	}

	@Override
	public ScoreEntry getItem(int index) {
		return results [index];
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
		
		/*LinearLayout resultView = (LinearLayout) convertView;
		
		if (resultView == null)
		{
			final LayoutInflater inflator = LayoutInflater.from(parent.getContext());
			resultView = (LinearLayout) 
					inflator.inflate(R.layout.score_entry_layout, null);
		}
		
		TextView name = (TextView) resultView.findViewById (R.id.name);
		name.setText(getItem(index).name);
		name.setTextColor(Color.YELLOW);
		TextView score = (TextView) resultView.findViewById(R.id.score);
		score.setText(new StringBuilder (getItem(index).score));
		score.setTextColor(Color.YELLOW);
		TextView wave = (TextView) resultView.findViewById(R.id.wave);
		wave.setText(new StringBuilder (getItem(index).waveNumber));
		wave.setTextColor(Color.YELLOW);
		
		return resultView;*/
	}

}
