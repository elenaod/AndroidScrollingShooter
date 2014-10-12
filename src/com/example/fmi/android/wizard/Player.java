package com.example.fmi.android.wizard;

import java.util.Scanner;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable{
	private String name;
	
	private int spellPower;
	private int fireMagic;
	private int iceMagic;
	private int earthMagic;
	
	private int waveNumber;
	
	private int points;
	private int score;
	
	public Player()
	{
		name = new String ();
		spellPower = 10;
		fireMagic = 5; iceMagic = 5; earthMagic = 5;
		waveNumber = 1; points = 0; score = 0;
	}
	
	public Player(String name)
	{
		this.name = new String(name);
		spellPower = 10;
		fireMagic = 5; iceMagic = 5; earthMagic = 5;
		waveNumber = 1; points = 0; score = 0;
	}
	
	public Player(Parcel in)
	{
		name = in.readString();
		fireMagic = in.readInt(); earthMagic = in.readInt(); iceMagic = in.readInt();
		spellPower = in.readInt(); waveNumber = in.readInt(); points = in.readInt();
		score = in.readInt();
	}
	
	public Player readPlayer(String string)
	{
		Scanner input = new Scanner(string);
		Player readPlayer = new Player();
		
		input.useDelimiter(" ");
		readPlayer.name = input.next();
		readPlayer.fireMagic = input.nextInt();
		readPlayer.iceMagic = input.nextInt();
		readPlayer.earthMagic = input.nextInt();
		readPlayer.spellPower = input.nextInt();
		readPlayer.waveNumber = input.nextInt();
		readPlayer.points = input.nextInt();
		readPlayer.score = input.nextInt();
		
		input.close();
		return readPlayer;
	}
	
	public String writePlayer()
	{
		StringBuilder str = new StringBuilder();
		
		str.append(name); str.append(" ");
		str.append(fireMagic); str.append(" ");
		str.append(iceMagic); str.append(" ");
		str.append(earthMagic); str.append(" ");
		str.append(spellPower); str.append(" ");
		str.append(waveNumber); str.append(" ");
		str.append(points); str.append(" ");
		str.append(score); str.append("\n");
		
		return new String(str);
	}

	public void increaseScore()
	{
		score += waveNumber*points;
	}
	public void setWaveNumber (int newWaveNumber)
	{
		waveNumber = newWaveNumber;
	}
	public void increaseSpellPower()
	{
		spellPower += 1;
	}
	public void increaseFireMagic()
	{
		fireMagic +=1;
	}
	public void increaseIceMagic()
	{
		iceMagic +=1;
	}
	public void increaseEarthMagic()
	{
		earthMagic +=1;
	}
	
	public String getName()
	{
		return new String(name);
	}
	public boolean addPoints(int p)
	{
		points += p;
		if (points < 0)
		{
			points -= p;
			return false;
		}
		return true;
	}
	
	public double attack(int method) 
	{
		switch (method)
		{
			case 0:
			{
				return spellPower*2.5;
			}
			case 1:
			{
				return fireMagic*1.5;
			}
			case 2:
			{
				return iceMagic*1.5;
			}
			case 3:
			{
				return 2*earthMagic;
			}
		}
		
		return 0;
	}
	
	public int getWaveNumber()
	{
		return waveNumber;
	}
	public String getFireMagic() 
	{
		StringBuilder str = new StringBuilder();
		str.append(fireMagic);
		
		return new String(str);
	}
	public String getEarthMagic() 
	{
		StringBuilder str = new StringBuilder();
		str.append(earthMagic);
		
		return new String(str);
	}
	public String getIceMagic() 
	{

		StringBuilder str = new StringBuilder();
		str.append(iceMagic);
		
		return new String(str);
	}
	public String getSpellPower() 
	{

		StringBuilder str = new StringBuilder();
		str.append(spellPower);
		
		return new String(str);
	}
	public String getPoints() 
	{
		StringBuilder str = new StringBuilder();
		str.append(points);
		
		return new String(str);
	}
	
	public int getScore()
	{
		return score;
	}
	public String toString()
	{
		return new String(name);
	}

	public static final Creator<Player> CREATOR = 
			new Parcelable.Creator<Player>() {

		@Override
		public Player createFromParcel(Parcel source) {
			return new Player(source);
		}

		@Override
		public Player[] newArray(int size) {
			return new Player[size];
		}
		
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(fireMagic); dest.writeInt(earthMagic); dest.writeInt(iceMagic);
		dest.writeInt(spellPower); dest.writeInt(waveNumber); dest.writeInt(points);
		dest.writeInt(score);
	}
}
