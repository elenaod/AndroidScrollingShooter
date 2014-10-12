package com.example.fmi.android.wizard;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BoardAdapter extends BaseAdapter{

	/*
	 * an array of integers for every tile in the board
	 * 0 - empty
	 * 1 - player
	 * 2 - enemy
	 * 3 - bullet
	 * 5 - exp_fire
	 * 6 - exp_ice
	 * 7 - exp_earth
	 */
	
	int [][] board;
	int rows;
	int cols;
	
	public BoardAdapter()
	{
		rows = 10; cols = 4;
		board = new int [][] {{0,1,0,0},{0,2,2,0},{0,2,2,2},
							   {2,2,2,0},{2,2,2,0},{2,2,0,0},
							   {2,2,0,0},{2,2,0,0},{2,0,0,0},{0,0,0,0}};
	}
	
	public BoardAdapter(int rows, int cols, int [][] board)
	{
		this.rows = rows;
		this.cols = cols;
		this.board = new int [rows][];
		
		for (int i = 0; i < rows; i++)
			this.board[i] = new int [cols];
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.board[i][j] = board[i][j];
	}
	
	public void setBoard(int [][] board)
	{
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.board[i][j] = board[i][j];
	}
	@Override
	public int getCount() {
		return rows*cols;
	}

	@Override
	public Object getItem(int index) {
		int iCoord = index/cols;
		int jCoord = index%cols;
		
		return board[iCoord][jCoord];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) convertView;
		
		if (textView == null) {
			final LayoutInflater inflator = LayoutInflater.from(parent
					.getContext());
			textView = (TextView) inflator.inflate(
					android.R.layout.simple_list_item_1, null);
		}
		
		Integer val = Integer.valueOf(getItem(position).toString());
		//textView.setText(val.toString());
		
		/*if (val.intValue() == 0) textView.setBackgroundColor(Color.BLACK);
		else if (val.intValue() == 1) textView.setBackgroundColor(Color.BLUE);
		else if (val.intValue() == 2) textView.setBackgroundColor(Color.RED);
		else if (val.intValue() == 3) textView.setBackgroundColor(Color.YELLOW);*/
		
		if (val.intValue() == 0) 
		{
			textView.setBackgroundColor(Color.BLACK);
		}
		else if (val.intValue() == 1) textView.setBackgroundResource(R.drawable.player);
		else if (val.intValue() == 2) textView.setBackgroundResource(R.drawable.enemy);
		else if (val.intValue() == 3) textView.setBackgroundResource(R.drawable.bullet);
		return textView;
	}

}
