package com.johnsoft.tools;

public class WaitNotifyTest
{
	public static void main(String[] args)
	{
		Env env = new Env();
		new IncreaseThread(env).start();
		new IncreaseThread(env).start();
		new IncreaseThread(env).start();
		new IncreaseThread(env).start();
		new DecreaseThread(env).start();
		new DecreaseThread(env).start();
	}
}

class IncreaseThread extends Thread
{
	Env env;
	
	public IncreaseThread(Env env)
	{
		this.env = env;
	}
	
	@Override
	public void run()
	{
		 for(int i = 0; i < 10; ++i)
		 {
			env.increase();
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		 }
	}
}

class DecreaseThread extends Thread
{
	Env env;
	
	public DecreaseThread(Env env)
	{
		this.env = env;
	}
	
	@Override
	public void run()
	{
		 for(int i = 0; i < 20; ++i)
		 {
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			env.decrease();
		 }
	}
}

class Env
{
	int num;
	
	synchronized void increase()
	{
		while(num > 0)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println(num);
		++num;
		notify();
	}
	
	synchronized void decrease()
	{
		while(num == 0)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println(num);
		--num;
		notify();
	}
}