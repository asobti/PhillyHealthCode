package com.vitaminme.data;

public class TimeSpan
{

	int seconds;

	public TimeSpan(int seconds)
	{
		this.seconds = seconds;
	}

	public int getHours()
	{
		return (int) this.seconds / 3600;
	}

	public int getMinutes()
	{
		int hours = this.getHours();
		int left = this.seconds - (hours * 3600);
		return (int) left / 60;
	}

	public int getTotalMinutes()
	{
		return (int) this.seconds / 60;
	}

	public int getSeconds()
	{
		int minutes = this.getTotalMinutes();
		int left = this.seconds - (minutes * 60);
		return (int) left;
	}

	@Override
	public String toString()
	{
		String res = "";

		if (this.getHours() > 0)
			res = this.getHours() + "h";

		if (this.getMinutes() > 0)
			res = this.getMinutes() + "m";

		if (this.getSeconds() > 0)
			res = this.getSeconds() + "s";

		return res;
	}
}
