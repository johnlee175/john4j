/**
 *SimulatedActivity.java
 *2007-5-16
 *@author zhanghui
 */
package com.johnsoft.library.raw;

import java.util.Random;

import javax.swing.JProgressBar;

/**
 * imitate the progress bar
 * 
 * @author john
 * 
 */
public class SimulatedActivity implements Runnable
{
	private JProgressBar processBar = null;

	Random random = new Random();

	private int ranNum = 500;

	public SimulatedActivity(JProgressBar processBar, int ranNum)
	{
		this.processBar = processBar;
		this.ranNum = ranNum;
	}

	public int getTarget()
	{
		return processBar.getMaximum();
	}

	public int getCurrent()
	{
		return processBar.getValue();
	}

	private int getSleepTime()
	{
		return random.nextInt(ranNum);
	}

	public void run()
	{
		try
		{
			while (getCurrent() < getTarget())
			{
				Thread.sleep(getSleepTime());

				processBar.setValue(getCurrent() + getTarget() / 200);

			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
