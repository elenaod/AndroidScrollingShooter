package com.example.fmi.android.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putParcelableArrayList("fmi.android.wizard.players", players);
		outState.putParcelableArray("fmi.android.wizard.results", results);
		outState.putParcelable("fmi.android.wizard.currentPlayer", currentPlayer);
	}

	private Context context;
	
    private Button change_character;
	private Button play;
	private Button upgrade_character;
	private Button hall_of_fame;
	
	private ArrayList<Player> players;
	private ScoreEntry [] results;
	private final int numberOfResults = 10;
	private Player currentPlayer;
	
	private Bundle extras;
	
	public void addResult(ScoreEntry newScore)
	{
		int i;
		// that's supposed to find the position of the score
		for (i = 0; i < numberOfResults && newScore.score < results[i].score; i++)
		{
			if (newScore.score < results[i].score)
				break;
		}
		for (int j = numberOfResults - 1; j > i; j--)
		{
			results[j] = results[j-1];
		}
		results[i] = newScore;
	}
	
	public void updatePlayer(Player p)
	{
		int index = 0;
		Toast debug = Toast.makeText(context, "here!", Toast.LENGTH_SHORT);
		debug.show();
		for (int i = 0; i < players.size(); i++)
		{
			debug.setText("comparing " + p.getName() + " to " + players.get(i).getName());
			debug.show();
			if (players.get(i).getName().equals(p.getName()))
				index = i;
		}
		debug.setText("found at " + new String(new StringBuilder(index)));
		debug.show();
		players.set(index, p);
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        context = getApplicationContext();
       
        extras = new Bundle();
       
        // get memory
        players = new ArrayList<Player>();
        results = new ScoreEntry [numberOfResults];

        for (int i = 0; i < numberOfResults; i++)
        {
        	results[i] = new ScoreEntry();
        }
        // fill memory, if available
        
        if (savedInstanceState != null)
        {
        	if (savedInstanceState.containsKey("fmi.android.players"))
        	{
        		ArrayList<Parcelable> playersBuffer = 
        			savedInstanceState.getParcelableArrayList("fmi.android.wizard.players");
        	
        		for (int i = 0; i < playersBuffer.size(); i++)
        		{
        			Player newPlayer = (Player) playersBuffer.get(i);
        			players.add(newPlayer);
        		}
        	}
        	if (savedInstanceState.containsKey("fmi.android.results"))
        	{
        		Parcelable [] scoresBuffer = 
        				savedInstanceState.getParcelableArray("fmi.android.wizard.results");
        		
        		for (int i = 0 ; i < scoresBuffer.length; i++)
        		{
        			results[i] = (ScoreEntry) scoresBuffer[i];
        		}
        	}
        	if (savedInstanceState.containsKey("fmi.android.wizard.currentPlayer"))
        	{
        		currentPlayer = (Player) savedInstanceState.getParcelable("fmi.android.wizard.currentPlayer");
        	}
        }
        
        if (players.size() > 0)
        	currentPlayer = players.get(0);
        else
        	currentPlayer = null;
        
        change_character = (Button) findViewById(R.id.change_character);
    	play = (Button) findViewById(R.id.new_game);
    	upgrade_character = (Button) findViewById(R.id.upgrade_character);
    	hall_of_fame = (Button) findViewById(R.id.hall_of_fame);

    	change_character.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ChangeCharacter.class);
				intent.putExtra("fmi.android.wizard.players", players);
				startActivity(intent);
			}
		});
    	
    	play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentPlayer != null)
				{
					Intent intent = new Intent(context, Game.class);
					intent.putExtra("fmi.android.wizard.players", players);
					intent.putExtra("fmi.android.wizard.player", currentPlayer);
					startActivity(intent);
				}
				else
				{
					Toast choose = 
							Toast.makeText(getApplicationContext(), 
									"You need to create a character first!", 
									Toast.LENGTH_SHORT);
					choose.show();
				}
			}
		});
    	
    	upgrade_character.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentPlayer != null)
				{
					Intent intent = new Intent(context, Upgrade.class);
					intent.putExtra("fmi.android.wizard.players", players);
					intent.putExtra("fmi.android.wizard.player", currentPlayer);		
					startActivity(intent);
				}
				else
				{
					Toast choose = 
							Toast.makeText(getApplicationContext(), 
									"You need to create a character first!", 
									Toast.LENGTH_SHORT);
					choose.show();
				}
			}
		});
    	
    	hall_of_fame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HallOfFame.class);
				
				if (results != null)
					intent.putExtra("fmi.android.wizard.results", results);
				
				startActivity(intent);
			}
		});
    }
    
	@Override
	protected void onPause() {
		super.onPause();

		extras.putParcelable("fmi.android.wizard.player", currentPlayer);
		extras.putParcelableArrayList("fmi.android.wizard.players", players);
		extras.putParcelableArray("fmi.android.wizard.results", results);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Intent intent = getIntent();
		
		if (intent.hasExtra("fmi.android.wizard.currentPlayer"))
		{
			currentPlayer = (Player) intent.getParcelableExtra("fmi.android.wizard.currentPlayer");
		}
		
		if (intent.hasExtra("fmi.android.wizard.players"))
		{
			ArrayList<Parcelable> playersList = intent.getParcelableArrayListExtra
					("fmi.android.wizard.players");
			
			players = new ArrayList<Player> ();
			
			for (int i = 0; i < playersList.size(); i++)
			{
				Player newPlayer = (Player) playersList.get(i);
				players.add(newPlayer);
			}
		}
		
		if (intent.hasExtra("fmi.android.wizard.currentPlayerFromGame"))
		{	
			currentPlayer = (Player) intent.getParcelableExtra("fmi.android.wizard.currentPlayerFromGame");
			ScoreEntry se = new ScoreEntry(currentPlayer.getName(),
					currentPlayer.getWaveNumber(),
					currentPlayer.getScore());
			addResult(se);
			
			int index = 0;
			for (int i = 0; i < players.size(); i++)
			{
				if (currentPlayer.getName().equals(players.get(i).getName()))
				{
					index = i;
					break;
				}
			}
			players.set(index, currentPlayer);
		}
	}
}