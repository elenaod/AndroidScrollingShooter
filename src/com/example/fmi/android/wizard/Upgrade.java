package com.example.fmi.android.wizard;

/*
 * MAKE PRETTIER!!!
 */
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Upgrade extends Activity {
	private Player P;
	
	private TextView fireScore;
	private TextView iceScore;
	private TextView earthScore;
	private TextView magicScore;
	private TextView pointsAvail;
	
	private Button fireButton;
	private Button iceButton;
	private Button earthButton;
	private Button magicButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade);
		
		P = new Player();
		
		if (getIntent().hasExtra("fmi.android.wizard.player"))
			P = (Player) getIntent().getParcelableExtra("fmi.android.wizard.player");
		else
		{
			P = new Player();
			Toast debug = Toast.makeText(getApplicationContext(), 
					"no extra!", Toast.LENGTH_SHORT);
			debug.show();
		}
		
		TextView charName = (TextView) findViewById(R.id.charName);
		charName.setText(P.getName());
		fireScore = (TextView) findViewById(R.id.fireValue);
		fireScore.setText(P.getFireMagic());		
		
		iceScore = (TextView) findViewById(R.id.iceValue);
		iceScore.setText(P.getIceMagic());
		earthScore = (TextView) findViewById(R.id.earthValue);
		earthScore.setText(P.getEarthMagic());
		magicScore = (TextView) findViewById(R.id.magicValue);
		magicScore.setText(P.getSpellPower());
		pointsAvail = (TextView) findViewById(R.id.points);
		pointsAvail.setText("Points available: " + P.getPoints());
		
		fireButton = (Button) findViewById(R.id.buttonFire);
		iceButton = (Button) findViewById(R.id.buttonIce);
		earthButton = (Button) findViewById(R.id.buttonEarth);
		magicButton = (Button) findViewById(R.id.buttonMagic);
		
		fireButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (P.addPoints(-1))
					P.increaseFireMagic();
				
				fireScore.setText(P.getFireMagic());
				pointsAvail.setText("Points available: " + P.getPoints());
			}
		});
		iceButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (P.addPoints(-1))
					P.increaseIceMagic();
				
				iceScore.setText(P.getIceMagic());
				pointsAvail.setText("Points available: " + P.getPoints());
			}
		});
		earthButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (P.addPoints(-1))
					P.increaseEarthMagic();
				
				earthScore.setText(P.getEarthMagic());
				pointsAvail.setText("Points available: " + P.getPoints());
			}
		});
		magicButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (P.addPoints(-1)) 
					P.increaseSpellPower();
				
				magicScore.setText(P.getSpellPower());
				pointsAvail.setText("Points available: " + P.getPoints());
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

		Intent intent = new Intent(getApplicationContext(), MainMenu.class);
		
		if (getIntent().hasExtra("fmi.android.wizard.players"))
		{
			intent.putExtra("fmi.android.wizard.players", 
					getIntent().getParcelableArrayListExtra("fmi.android.wizard.players"));
		}
		
		intent.putExtra("fmi.android.wizard.currentPlayerFromGame", P);
		
		startActivity(intent);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
