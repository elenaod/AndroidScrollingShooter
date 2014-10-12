package com.example.fmi.android.wizard;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity {

	// an attempt to replicate c/c++ structures
	// seemed pointless to have a class with set/get
	private class Bullet
	{
		public int row;
		public int col;
		
		public Bullet()
		{
			row = -1;
			col = -1;
		}
	}
	
	private Player player;
	private int player_column;
	
	private int [] firstEnemy;
	private int [] numberOfEnemies;
	private double [] maxEnemyHealth;
	private double [] firstEnemyHealth;
	private int [] moving;
	
	Bullet [] bullets;
	private int [][] gameBoard;
	
	private int rows;
	private int columns;
	private boolean turn;
	
	private GridView board;
	private BoardAdapter boardAdapter;
	private Button fireAttack;
	private int fireAttackCooldown;
	private Button iceAttack;
	private int iceAttackCooldown;
	private Button earthAttack;
	private int earthAttackCooldown;
	private Button left;
	private Button right;
	private Handler updateHandler;
	private Thread updateThread;
	private boolean running;
	
	private void init()
	{
		running = true;
		turn = true;
		rows = 9;
		columns = 4;
		
		if (getIntent().hasExtra("fmi.android.wizard.player"))
		{
			player = (Player) getIntent().
				getParcelableExtra("fmi.android.wizard.player");
		}
		else
		{
			player = new Player();
		}
		
		gameBoard = new int [rows][];
		
		for (int i = 0 ; i < rows; i++)
		{
			gameBoard[i] = new int [columns];
			for (int j = 0; j < columns; j++)
			{
				gameBoard[i][j] = 0;
			}
		}
		
		player_column = 2;
		firstEnemy = new int [columns];
		numberOfEnemies = new int [columns];
		maxEnemyHealth = new double [columns];
		firstEnemyHealth = new double [columns];
		moving = new int [columns];
		
		for (int i = 0; i < columns; i ++)
		{
			firstEnemy[i] = rows;
			numberOfEnemies[i] = 2*player.getWaveNumber();
			maxEnemyHealth[i] = 10;
			firstEnemyHealth[i] = 10;
			moving[i] = 0;
		}
		bullets = new Bullet [rows];
		for (int i = 0; i < rows; i++) bullets[i] = new Bullet();
		
	}
	private void actions()
	{	
		// advance bullets
		for (int i = 0; i < rows; i++)
		{
			if (bullets[i].row != -1)
			{
				bullets[i].row++;
				if (bullets[i].row >= rows)
				{
					bullets[i].col = -1;
					bullets[i].row = -1;
				}
				else if (firstEnemy[bullets[i].col] == bullets[i].row)
				{
					firstEnemyHealth[i] -= player.attack(0);
					bullets[i].row = -1;
					bullets[i].col = -1;
					if (firstEnemyHealth[i] <= 0)
					{
						firstEnemy[i]++;
						numberOfEnemies[i]--;
						firstEnemyHealth[i] = maxEnemyHealth[i];
						player.addPoints(i);
						player.increaseScore();
					}
				}
			}
		}
		if (turn)
		{
			// create bullet
			boolean flag = true;
			for (int i = 0; i < rows && flag; i++)
			{
				if (bullets[i].row == -1)
				{
					bullets[i].row = 1;
					bullets[i].col = player_column;
				
					if (firstEnemy[player_column] == 1)
					{
						firstEnemyHealth[i] -= player.attack(0);
						if (firstEnemyHealth[i] <= 0)
						{
							firstEnemy[i]++;
							numberOfEnemies[i]--;
							firstEnemyHealth[i] = maxEnemyHealth[i];
							player.addPoints(i);
							player.increaseScore();
						}
					}
					
					flag = false;
				}
			}
		}	
		turn = !turn;
		
		// advance enemies
		for (int i = 0; i < columns; i++)
		{
			if (numberOfEnemies[i] > 0 && moving[i] == 0)
			{
				firstEnemy[i]--;
				
				int k = hasBullet(firstEnemy[i],i);
				if (k != -1)
				{
					firstEnemyHealth[i] -= player.attack(0);
					if (firstEnemyHealth[i] <= 0)
					{
						bullets[k].row = -1;
						bullets[k].col = -1;
						firstEnemy[i]++;
						numberOfEnemies[i]--;
						firstEnemyHealth[i] = maxEnemyHealth[i];
						player.addPoints(i);
						player.increaseScore();
					}
				}
				
				if (firstEnemy[i] == 0 && i == player_column)
				{
					Toast gameOver = Toast.makeText(getApplicationContext(),
							"GAME OVER!", Toast.LENGTH_LONG);
					gameOver.show();
					Intent intent = new Intent(getApplicationContext(),
							MainMenu.class);
							startActivity(intent);					
				}
			}
		}
				
		if (fireAttackCooldown > 0)
			fireAttackCooldown--;
		if (earthAttackCooldown > 0)
			earthAttackCooldown--;
		for (int i = 0; i < columns; i++)
		{
			if (moving[i] > 0) moving[i]--;
		}
		if (iceAttackCooldown > 0)
			iceAttackCooldown--;
		
		if (fireAttackCooldown <= 0)
			fireAttack.setEnabled(true);
		if (iceAttackCooldown <= 0)
			iceAttack.setEnabled(true);
		if (earthAttackCooldown <= 0)
			earthAttack.setEnabled(true);
		
		boolean newWave = true;
		for (int i = 0; i < columns && newWave; i++)
		{
			if (!(firstEnemy[i] + numberOfEnemies[i] < 0 || numberOfEnemies[i] <= 0))
			newWave = false;
		}
			
		if (newWave)
		{
			player.setWaveNumber(player.getWaveNumber() + 1);
			for (int i = 0; i < columns; i++)
			{
				firstEnemy[i] = rows;
				numberOfEnemies[i] = 2*player.getWaveNumber();
				firstEnemyHealth[i] = 10;
				maxEnemyHealth[i] = 10;
			}
		}
	}
	private void createBoard()
	{
		for (int i = 0; i < rows; i ++)
			for (int j = 0; j < columns; j++)
			{
				if (firstEnemy[j] <= i && i < firstEnemy[j] + numberOfEnemies[j])
					gameBoard[i][j] = 2;
				else if (i == 0 && player_column == j)
					gameBoard[i][j] = 1;
				else if (hasBullet(i,j) != -1)
					gameBoard[i][j] = 3;
				else
					gameBoard[i][j] = 0;
			}
	}
	private int hasBullet(int i, int j)
	{
		for (int k = 0 ; k < rows; k++)
		{
			if (bullets[k].row == i && bullets[k].col == j)
				return k;
		}
		return -1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		init();
		createBoard();

		board = (GridView) findViewById(R.id.board);
		boardAdapter = new BoardAdapter(rows, columns, gameBoard);
		board.setAdapter(boardAdapter);
		board.setNumColumns(columns);
		
		fireAttack = (Button) findViewById (R.id.fireAttack);
		iceAttack = (Button) findViewById (R.id.iceAttack);
		earthAttack = (Button) findViewById (R.id.earthAttack);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);
		
		fireAttack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (fireAttackCooldown == 0)
				{
					for (int i = 0; i < columns; i++)
					{
						firstEnemyHealth[i] -= player.attack(1);
						if (firstEnemyHealth[i] <= 0)
						{
							firstEnemy[i] ++;
							numberOfEnemies[i] --;
							player.addPoints(1);
							player.increaseScore();
						}
					}
					fireAttackCooldown = 5;
					fireAttack.setEnabled(false);
					board.setAdapter(new BoardAdapter(rows, columns, gameBoard));
				}
			}
		});
		
		iceAttack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (iceAttackCooldown == 0)
				{
					maxEnemyHealth[player_column] -= player.attack(2);
					firstEnemyHealth[player_column] -= player.attack(2);
					
					if (maxEnemyHealth[player_column] <= 0)
					{

						player.addPoints(numberOfEnemies[player_column]);
						numberOfEnemies[player_column] = 0;
						firstEnemy[player_column] = -1;
					}
					if (firstEnemyHealth[player_column] <= 0)
					{
						player.addPoints(1);
						player.increaseScore();
						firstEnemyHealth[player_column] ++;
						numberOfEnemies[player_column] --;
						firstEnemyHealth[player_column] = maxEnemyHealth[player_column];
					}
					
					iceAttackCooldown = 5;
					iceAttack.setEnabled(false);
					board.setAdapter(new BoardAdapter(rows, columns, gameBoard));
				}
			}
		});
		
		earthAttack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (earthAttackCooldown == 0)
				{
					moving [player_column] = (int) (2*player.attack(3));
					earthAttackCooldown = 10;
					earthAttack.setEnabled(false);
				}
				board.setAdapter(new BoardAdapter(rows, columns, gameBoard));
			}
		});
		
		left.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				player_column--;
				board.setAdapter(new BoardAdapter(rows, columns, gameBoard));
			}
		});
		
		right.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				player_column++;
				board.setAdapter(new BoardAdapter(rows, columns, gameBoard));
			}
		});
		
		updateHandler = new Handler();
		updateThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true)
				{
					if (running)
					{
						try {
							Thread.sleep(2000);
					
							updateHandler.post(new Runnable() {
								
								@Override
								public void run() {
									actions();
									createBoard();
									// update UI next - only bother if it runs
									board.setAdapter(new BoardAdapter(rows, columns, gameBoard));
								}
							});
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		updateThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		running = false;
		Intent intent = new Intent(getApplicationContext(), MainMenu.class);
		intent.putExtra("fmi.android.wizard.currentPlayerFromGame", player);
		
		if (getIntent().hasExtra("fmi.android.wizard.players"))
		{
			intent.putExtra("fmi.android.wizard.players", 
					getIntent().getParcelableArrayListExtra("fmi.android.wizard.players"));
		}
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (getIntent().hasExtra("fmi.android.wizard.player"))
		{
			player = (Player) getIntent().
				getParcelableExtra("fmi.android.wizard.player");
		}
		else
		{
			player = new Player();
		}
		running = true;
	}
}