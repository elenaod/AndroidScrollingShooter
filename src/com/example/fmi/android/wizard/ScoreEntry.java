package com.example.fmi.android.wizard;

import android.os.Parcel;
import android.os.Parcelable;

public class ScoreEntry implements Parcelable{
	public String name;
	public int waveNumber;
	public int score;
	
	public ScoreEntry()
	{
		name = new String(" ");
		waveNumber = -1;
		score = 0;
	}
	public ScoreEntry(String name, int wave, int score)
	{
		this.name = new String(name);
		this.waveNumber = wave;
		this.score = score;
	}
	public ScoreEntry(ScoreEntry se)
	{
		name = new String(se.name);
		waveNumber = se.waveNumber;
		score = se.score;
	}
	
	public ScoreEntry (Parcel source)
	{
		name = source.readString();
		waveNumber = source.readInt();
		score = source.readInt();
	}
	
	public String toString()
	{
		String string = String.format("%-20s - %3d - %6d", name, waveNumber, score);
		return string;
	}
	
	public void read(String string)
	{
		String [] tokens = string.split("/");
		name = tokens[0];
		waveNumber = Integer.parseInt(tokens[1]);
		score = Integer.parseInt(tokens[2]);
	}
	
	public String write()
	{
		String string = name + "/" + waveNumber + "/" + score + "\n";
		return string;
	}
	
	public static final Creator<ScoreEntry> CREATOR = 
			new Parcelable.Creator<ScoreEntry>() {

		@Override
		public ScoreEntry createFromParcel(Parcel source) {
			return new ScoreEntry(source);
		}

		@Override
		public ScoreEntry[] newArray(int size) {
			return new ScoreEntry[size];
		}
		
	};
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(waveNumber);
		dest.writeInt(score);
	}
}
