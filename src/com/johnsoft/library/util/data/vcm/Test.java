package com.johnsoft.library.util.data.vcm;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Test
{
	public static void main(String[] args)
	{
		JFrame jf=new JFrame();
		JPanel jp=new JPanel();
		jp.prepareImage(new ImageIcon(Test.class.getResource("/resource/Garden.jpg")).getImage(), 500,400,null);
		JLabel jl=new JLabel("hello world");
		final JLabel bel=new JLabel();
		final CVCMString str=new CVCMString("welcome");
		str.setText(bel);
		final CVCMColor vclColor=new CVCMColor(Color.YELLOW);
		Font f=new Font("微软雅黑",Font.PLAIN,18);
		final CVCMFont vclFont=new CVCMFont(f);
		vclColor.setForeground(jl);
		vclFont.setFont(jl);
		bel.setOpaque(true);
		vclColor.setBackground(bel);
		jp.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				Color c=JColorChooser.showDialog(null, "", Color.WHITE);
				CVCMColor color2=new CVCMColor(Color.BLUE);
				color2.setBackground(bel);
				if(c!=null)
				{
					vclColor.set(c);
				}
				vclFont.set(new Font("微软雅黑",Font.BOLD,33));
				str.set("what can i do for you?");
			}
		});
		jp.add(jl);
		jp.add(bel);
		jf.add(jp);
		jf.setVisible(true);
	}
}
