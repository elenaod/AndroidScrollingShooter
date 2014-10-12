package com.example.fmi.android.wizard;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class HallOfFame extends Activity {

	private ScoreEntry [] results;
	private final int numberOfEntries = 10;
	private ListView highScores;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hall_of_fame);

		results = new ScoreEntry[numberOfEntries];
		
		for(int i = 0; i < numberOfEntries; i++)
		{
			results[i] = new ScoreEntry();
		}
		
		/*
		 * NEEDS TO BE PRETTIER!
		 * should not be too hard to make it a view instead of a textView
		 * and to add the views, consisting of something fancier
		 * but enough for now
		 */
		
		if (getIntent().hasExtra("fmi.android.wizard.results"))
		{
			Parcelable [] resultsBuffer =
				getIntent().getParcelableArrayExtra("fmi.android.wizard.results");
		
			for (int i = 0; i < numberOfEntries; i++)
			{
				results[i] = (ScoreEntry) resultsBuffer[i];
			}
		}
		highScores = (ListView) findViewById(R.id.results);
		highScores.setAdapter(new ScoreEntryAdapter(results));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hall_of_fame, menu);
		return true;
	}
}
