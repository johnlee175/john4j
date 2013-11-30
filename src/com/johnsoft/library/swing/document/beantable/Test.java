package com.johnsoft.library.swing.document.beantable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Test 
{
	public static void main(String[] args)
	{
		Model model1=new Model("hello",70.2,18,167,110,"weixiao",true,null);
		Model model2=new Model("world",79.5,22,170,80,"weixiao",false,false);
		Model model3=new Model("welcome",84.17,164,7,99,"weixiao",true,true);
		Model model4=new Model("okoko",60.9,20,173,104,"kuqi",false,null);
		List<Model> list=new ArrayList<Model>();
		list.add(model1);
		list.add(model2);
		list.add(model3);
		list.add(model4);
		BeanListTableModel model=new BeanListTableModel(Model.class,list);
		model.setCellEditable(true);
		JFrame frame=new JFrame();
		JTable table=new JTable();
		table.setModel(model);
		frame.add(new JScrollPane(table));
		frame.setBounds(200, 100, 600,400);
		frame.setVisible(true);
	}
	
	public static class Model
	{
		@TableColumn(value=0,name="name")
		private String name;
		@TableColumn(value=2,name="score")
		private double score;
		@TableColumn(value=1,name="age")
		private int age;
		private int height;
		private int weight;
		private String grade;
		@TableColumn(value=3,name="byBank")
		private boolean byBank;
		@TableColumn(value=4,name="chinese")
		private Boolean chinese;
		public Model()
		{
		}
		public Model(String name,double score,int age,int height,int weight,String grade,boolean byBank,Boolean chinese)
		{
			this.name=name;
			this.score=score;
			this.age=age;
			this.height=height;
			this.weight=weight;
			this.grade=grade;
			this.byBank=byBank;
			this.chinese=chinese;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public double getScore()
		{
			return score;
		}
		public void setScore(double score)
		{
			this.score = score;
		}
		public int getAge()
		{
			return age;
		}
		public void setAge(int age)
		{
			this.age = age;
		}
		public int getHeight()
		{
			return height;
		}
		public void setHeight(int height)
		{
			this.height = height;
		}
		public int getWeight()
		{
			return weight;
		}
		public void setWeight(int weight)
		{
			this.weight = weight;
		}
		public String getGrade()
		{
			return grade;
		}
		public void setGrade(String grade)
		{
			this.grade = grade;
		}
		public boolean isByBank()
		{
			return byBank;
		}
		public void setByBank(boolean byBank)
		{
			this.byBank = byBank;
		}
		public Boolean getChinese()
		{
			return chinese;
		}
		public void setChinese(Boolean chinese)
		{
			this.chinese = chinese;
		}
	}
}
