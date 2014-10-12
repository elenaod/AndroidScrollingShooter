package com.example.fmi.android.wizard;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChangeCharacter extends Activity {
	private Player currentPlayer;
	private ArrayList<Player> players;
	private ListView charList;
	private CharListAdapter charListAdapter;
	private Button addChar;
	
	private AlertDialog.Builder alert;
	private EditText nameInput;
	private String name;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_character);
	
		context = getApplicationContext();
		
		players = new ArrayList<Player>();
		ArrayList<Parcelable> buffer = getIntent().
				getParcelableArrayListExtra("fmi.android.wizard.players");
		
		for (int i = 0; i < buffer.size(); i++)
		{
			players.add((Player) buffer.get(i));
		}
		
		charList = (ListView) findViewById(R.id.charList);
		charListAdapter = new CharListAdapter(players);
		charList.setAdapter(charListAdapter);
		
		charList.setOnItemClickListener(new ListView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int index,
					long value) {
				currentPlayer = players.get(index);
				
				Intent intent = new Intent(context, MainMenu.class);
				intent.putExtra("fmi.android.wizard.currentPlayer", currentPlayer);
				intent.putExtra("fmi.android.wizard.players", players);
				
				startActivity(intent);
			}
		});
		
		addChar = (Button) findViewById(R.id.addChar);
		addChar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// prompt for name
				alert = new AlertDialog.Builder(ChangeCharacter.this);
				nameInput = new EditText(context);
				nameInput.setEnabled(true);
				nameInput.setTextColor(Color.BLACK);
				name = new String();
				
				alert.setView(nameInput);
				
				alert.setTitle("Enter wizard's name:");
				
				alert.setPositiveButton("Create!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						name = nameInput.getText().toString();
						Player newPlayer = new Player(name);

						players.add(newPlayer);
						charList.setAdapter(new CharListAdapter(players));
						currentPlayer = players.get(players.size() - 1);
						
						Intent intent = new Intent(context, MainMenu.class);
						intent.putExtra("fmi.android.wizard.currentPlayer", currentPlayer);
						intent.putExtra("fmi.android.wizard.players", players);
						
						startActivity(intent);
					}
				});
				
				AlertDialog inputDialog = alert.create();
				inputDialog.show();
			}
		});
	}
}
