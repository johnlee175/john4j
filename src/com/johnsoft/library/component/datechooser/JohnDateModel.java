package com.johnsoft.library.component.datechooser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.johnsoft.library.component.JohnSpinner;

/**
 * 枚举的日历控件可选值模型
 * @author 李哲浩
 */
public class JohnDateModel
{
	private Calendar now=Calendar.getInstance();
	
	private List<String> monthList=new ArrayList<String>();
	private List<String> yearList=new ArrayList<String>();
	private List<String> hourList=new ArrayList<String>();
	private List<String> miniteList=new ArrayList<String>();
	private List<String> secondList=new ArrayList<String>();

	private String selectedMonth;
	private String selectedYear;
	private String selectedHour;
	private String selectedMinite;
	private String selectedSecond;
	
	private int minMonth=1;
	private int minYear=1970;
	private int minHour=0;
	private int minMinite=0;
	private int minSecond=0;
	
	private int maxMonth=13;
	private int maxHour=24;
	private int maxMinite=60;
	private int maxSecond=60;
	private int maxYear=now.get(Calendar.YEAR)+51;
	
	private JohnSpinner monthSpinner;
	private JohnSpinner yearSpinner;
	private JohnSpinner hourSpinner;
	private JohnSpinner miniteSpinner;
	private JohnSpinner secondSpinner;
	
	
	public JohnDateModel()
	{
		initMonthList();
		initYearList();
		initHourList();
		initMiniteList();
		initSecondList();
		initSelectedMonth();
		initSelectedYear();
		initSelectedHour();
		initSelectedMinite();
		initSelectedSecond();
	}
	
	public void initMonthList()
	{
		for(int i=minMonth;i<maxMonth;i++)
		{
			if(i<10)
			{
				monthList.add("0"+i+"");
			}else{
				monthList.add(""+i+"");
			}
		}
	}
	
	public void initYearList()
	{
		for(int i=minYear;i<maxYear;i++)
		{
			yearList.add(""+i+"");
		}
	}
	
	public void initHourList()
	{
		for(int i=minHour;i<maxHour;i++)
		{
			if(i<10)
			{
				hourList.add("0"+i+"");
			}else{
				hourList.add(""+i+"");
			}
		}
	}
	
	public void initMiniteList()
	{
		for(int i=minMinite;i<maxMinite;i++)
		{
			if(i<10)
			{
				miniteList.add("0"+i+"");
			}else{
				miniteList.add(""+i+"");
			}
		}
	}
	
	public void initSecondList()
	{
		for(int i=minSecond;i<maxSecond;i++)
		{
			if(i<10)
			{
				secondList.add("0"+i+"");
			}else{
				secondList.add(""+i+"");
			}
		}	
	}

	public void initSelectedMonth()
	{
		int mm=now.get(Calendar.MONTH)+1;
		if(mm<10)
		{
			selectedMonth="0"+mm+"";
		}else{
			selectedMonth=""+mm+"";
		}
	}
	
	public void initSelectedYear()
	{
		int yyyy=now.get(Calendar.YEAR);
		selectedYear=""+yyyy+"";
	}
	
	public void initSelectedHour()
	{
		int hh=now.get(Calendar.HOUR_OF_DAY);
		if(hh<10)
		{
			selectedHour="0"+hh+"";
		}else{
			selectedHour=""+hh+"";
		}		
	}
	
	public void initSelectedMinite()
	{
		int mm=now.get(Calendar.MINUTE);
		if(mm<10)
		{
			selectedMinite="0"+mm+"";
		}else{
			selectedMinite=""+mm+"";
		}	
	}
	
	public void initSelectedSecond()
	{
		selectedSecond="00";
	}
		
	//getter and setter
	
	public String getSelectedMonth()
	{
		return selectedMonth;
	}

	public String getSelectedYear()
	{
		return selectedYear;
	}

	public String getSelectedHour()
	{
		return selectedHour;
	}

	public String getSelectedMinite()
	{
		return selectedMinite;
	}

	public String getSelectedSecond()
	{
		return selectedSecond;
	}

	public List<String> getMonthList()
	{
		return monthList;
	}

	public List<String> getYearList()
	{
		return yearList;
	}

	public List<String> getHourList()
	{
		return hourList;
	}

	public List<String> getMiniteList()
	{
		return miniteList;
	}

	public List<String> getSecondList()
	{
		return secondList;
	}

	public int getMaxMonth()
	{
		return maxMonth;
	}

	public int getMaxHour()
	{
		return maxHour;
	}

	public int getMaxYear()
	{
		return maxYear;
	}

	public Calendar getNow()
	{
		return now;
	}

	public void setNow(Calendar now)
	{
		this.now = now;
	}

	public int getMinMonth()
	{
		return minMonth;
	}

	public void setMinMonth(int minMonth)
	{
		this.minMonth = minMonth;
	}

	public int getMinYear()
	{
		return minYear;
	}

	public void setMinYear(int minYear)
	{
		this.minYear = minYear;
	}

	public int getMinHour()
	{
		return minHour;
	}

	public void setMinHour(int minHour)
	{
		this.minHour = minHour;
	}

	public int getMinMinite()
	{
		return minMinite;
	}

	public void setMinMinite(int minMinite)
	{
		this.minMinite = minMinite;
	}

	public int getMinSecond()
	{
		return minSecond;
	}

	public void setMinSecond(int minSecond)
	{
		this.minSecond = minSecond;
	}

	public void setMonthList(List<String> monthList)
	{
		this.monthList = monthList;
	}

	public void setYearList(List<String> yearList)
	{
		this.yearList = yearList;
	}

	public void setHourList(List<String> hourList)
	{
		this.hourList = hourList;
	}

	public void setMiniteList(List<String> miniteList)
	{
		this.miniteList = miniteList;
	}

	public void setSecondList(List<String> secondList)
	{
		this.secondList = secondList;
	}

	public void setSelectedMonth(String selectedMonth)
	{
		this.selectedMonth = selectedMonth;
	}

	public void setSelectedYear(String selectedYear)
	{
		this.selectedYear = selectedYear;
	}

	public void setSelectedHour(String selectedHour)
	{
		this.selectedHour = selectedHour;
	}

	public void setSelectedMinite(String selectedMinite)
	{
		this.selectedMinite = selectedMinite;
	}

	public void setSelectedSecond(String selectedSecond)
	{
		this.selectedSecond = selectedSecond;
	}

	public void setMaxMonth(int maxMonth)
	{
		this.maxMonth = maxMonth;
	}

	public void setMaxHour(int maxHour)
	{
		this.maxHour = maxHour;
	}

	public void setMaxYear(int maxYear)
	{
		this.maxYear = maxYear;
	}

	public int getMaxMinite()
	{
		return maxMinite;
	}

	public void setMaxMinite(int maxMinite)
	{
		this.maxMinite = maxMinite;
	}

	public int getMaxSecond()
	{
		return maxSecond;
	}

	public void setMaxSecond(int maxSecond)
	{
		this.maxSecond = maxSecond;
	}

	public JohnSpinner getMonthSpinner()
	{
		return monthSpinner;
	}

	public void setMonthSpinner(JohnSpinner monthSpinner)
	{
		this.monthSpinner = monthSpinner;
	}

	public JohnSpinner getYearSpinner()
	{
		return yearSpinner;
	}

	public void setYearSpinner(JohnSpinner yearSpinner)
	{
		this.yearSpinner = yearSpinner;
	}

	public JohnSpinner getHourSpinner()
	{
		return hourSpinner;
	}

	public void setHourSpinner(JohnSpinner hourSpinner)
	{
		this.hourSpinner = hourSpinner;
	}

	public JohnSpinner getMiniteSpinner()
	{
		return miniteSpinner;
	}

	public void setMiniteSpinner(JohnSpinner miniteSpinner)
	{
		this.miniteSpinner = miniteSpinner;
	}

	public JohnSpinner getSecondSpinner()
	{
		return secondSpinner;
	}

	public void setSecondSpinner(JohnSpinner secondSpinner)
	{
		this.secondSpinner = secondSpinner;
	}

	
	
}
