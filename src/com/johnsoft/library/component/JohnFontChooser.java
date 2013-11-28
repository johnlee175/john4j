package com.johnsoft.library.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;

/**
 * 字体选择器Windows风格实现,但并未采用UI,Model的分离实现,仅应作为对话框使用.<br>
 * 示例:<br/>
 * public static void main(String[] args) throws Exception<br>
   {<br>
    	&nbsp;UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());<br>
		&nbsp;final JFrame jf=new JFrame();<br>
		&nbsp;JPanel jp=new JPanel();<br>
		&nbsp;final JButton jb=new JButton("click me static");<br>
		&nbsp;final JButton jb2=new JButton("click me instance");<br>
		&nbsp;jp.add(jb);<br>
		&nbsp;jp.add(jb2);<br>
		&nbsp;jp.add(new JohnFontChooser(new Font("微软雅黑",Font.PLAIN,12)));<br>
		&nbsp;jf.add(jp);<br>
		&nbsp;jf.setBounds(200,100,800,500);<br>
		&nbsp;jf.setVisible(true);<br>
		&nbsp;jb.addActionListener(new ActionListener()<br>
		&nbsp;{<br>
			&nbsp;&nbsp;public void actionPerformed(ActionEvent e)<br>
			&nbsp;&nbsp;{<br>
				&nbsp;&nbsp;&nbsp;Font f=JohnFontChooser.showFontDialog(jb.getFont(),jb);<br>
				&nbsp;&nbsp;&nbsp;jb.setFont(f);<br>
			&nbsp;&nbsp;}<br>
		&nbsp;});<br>
		&nbsp;jb2.addActionListener(new ActionListener()<br>
		&nbsp;{<br>
			&nbsp;&nbsp;public void actionPerformed(ActionEvent paramActionEvent)<br>
			&nbsp;&nbsp;{<br>
				&nbsp;&nbsp;&nbsp;final Font oldFont=jb2.getFont();<br>
				&nbsp;&nbsp;&nbsp;final JohnFontChooser chooser=new JohnFontChooser(oldFont);<br>
				&nbsp;&nbsp;&nbsp;chooser.swapApplyResetPosition();<br>
				&nbsp;&nbsp;&nbsp;chooser.getResetButton().addActionListener(new ActionListener()<br>
				&nbsp;&nbsp;&nbsp;{<br>
					&nbsp;&nbsp;&nbsp;&nbsp;public void actionPerformed(ActionEvent paramActionEvent)<br>
					&nbsp;&nbsp;&nbsp;&nbsp;{<br>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;chooser.resetTo(oldFont);<br>
					&nbsp;&nbsp;&nbsp;&nbsp;}<br>
				&nbsp;&nbsp;&nbsp;});<br>
				&nbsp;&nbsp;&nbsp;chooser.getApplyButton().addActionListener(new ActionListener()<br>
				&nbsp;&nbsp;&nbsp;{<br>
					&nbsp;&nbsp;&nbsp;&nbsp;public void actionPerformed(ActionEvent e)<br>
					&nbsp;&nbsp;&nbsp;&nbsp;{<br>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;jb2.setFont(chooser.getChoosedFont());<br>
					&nbsp;&nbsp;&nbsp;&nbsp;}<br>
				&nbsp;&nbsp;&nbsp;});<br>
				&nbsp;&nbsp;&nbsp;if(chooser.showFontDialog(jb2.getFont(), jf, false))<br>
				&nbsp;&nbsp;&nbsp;{<br>
					&nbsp;&nbsp;&nbsp;&nbsp;jb2.setFont(chooser.getChoosedFont());<br>
					&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("OK pressed,you had chosen "+chooser.getChoosedFont().getName());<br>
				&nbsp;&nbsp;&nbsp;}else{<br>
					&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("you had canceled!");<br>
				&nbsp;&nbsp;&nbsp;}<br>
			&nbsp;&nbsp;}<br>
		&nbsp;});<br>
	}<br>
	
 * @author 李哲浩
 */
public class JohnFontChooser extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	/*public static void main(String[] args) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		final JFrame jf=new JFrame();
		JPanel jp=new JPanel();
		final JButton jb=new JButton("click me static");
		final JButton jb2=new JButton("click me instance");
		jp.add(jb);
		jp.add(jb2);
		jp.add(new JohnFontChooser(new Font("微软雅黑",Font.PLAIN,12)));
		jf.add(jp);
		jf.setBounds(200,100,800,500);
		jf.setVisible(true);
		jb.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Font f=JohnFontChooser.showFontDialog(jb.getFont(),jb);
				jb.setFont(f);
			}
		});
		jb2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent paramActionEvent)
			{
				final Font oldFont=jb2.getFont();
				final JohnFontChooser chooser=new JohnFontChooser(oldFont);
				chooser.swapApplyResetPosition();
				chooser.getResetButton().addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent paramActionEvent)
					{
						chooser.resetTo(oldFont);
					}
				});
				chooser.getApplyButton().addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jb2.setFont(chooser.getChoosedFont());
					}
				});
				if(chooser.showFontDialog(jb2.getFont(), jf, false))
				{
					jb2.setFont(chooser.getChoosedFont());
					System.out.println("OK pressed,you had chosen "+chooser.getChoosedFont().getName());
				}else{
					System.out.println("you had canceled!");
				}
			}
		});
	}*/
	
	/**
	 * 静态弹出字体对话框,仅有确认,取消按钮,如果点击确认返回选择的新字体,否则返回提供的初始字体
	 * @param initFont 初始字体
	 * @param parent 对话框父组件
	 * @return 选择或原始的字体
	 */
	public static Font showFontDialog(Font initFont,Component parent)
	{
		JohnFontChooser chooser=new JohnFontChooser(initFont);
		JDialog dialog=chooser.createDialog(parent);
		chooser.installDefOKCancelActionTo(dialog,true);
		chooser.getApplyButton().setVisible(false);
		chooser.getResetButton().setVisible(false);
		dialog.setVisible(true);
		return chooser.getChoosedFont();
	}
	
	protected JTextField familyField;
	protected JList familyList;
	protected JTextField styleField;
	protected JList styleList;
	protected JTextField sizeField;
	protected JList sizeList;
	protected JCheckBox strikethroughCheck;
	protected JCheckBox underlineCheck;
	protected JLabel colorPickerTrigger;
	protected JTextField colorTipField;
	protected JTextField example;
	protected JComboBox charsetCombo;
	protected JButton okButton;
	protected JButton cancelButton;
	protected JButton applyButton;
	protected JButton resetButton;
	
	protected Map<TextAttribute,Object> textAttrsMap;
	protected Font m_font;
	protected Font old_font;
	protected boolean isSelectListNotKeyDown;
	protected String ok_cancel;
	
	/**示例文本,(注意示例文本非JLabel实现,而是采用JTextField实现),如果选择Charset组合框的第一项,则对应此数组的第一项,以此类推*/
	public static String[] exampleText=new String[]{"中国智造 慧及全球","AaBbYyZz0189"};
	protected static String[] styleType=new String[]{"常规","倾斜","加粗","展开","倾斜 加粗","倾斜 展开","加粗 展开","倾斜 加粗 展开"};
	protected static String[] zhSizeType=new String[]{"初号","小初","一号","小一","二号","小二","三号","小三",
												   "四号","小四","五号","小五","六号","小六","七号","八号"};
	
	public JohnFontChooser(Font font)
	{
		if(font==null)
		{
			throw new IllegalArgumentException("The param named font can't be null!");
		}
		if(font instanceof FontUIResource)
		{
			font=new Font(font.getName(),font.getStyle(),font.getSize()).deriveFont(font.getAttributes());
		}
		old_font=m_font=font;
		textAttrsMap=new HashMap<TextAttribute, Object>(font.getAttributes());
		
		setLayout(null);
		
		JLabel familyLabel = new JLabel("字体:");
		familyLabel.setBounds(10, 10, 55, 15);
		add(familyLabel);
		
		JLabel styleLabel = new JLabel("字形:");
		styleLabel.setBounds(260, 10, 55, 15);
		add(styleLabel);
		
		JLabel sizeLabel = new JLabel("字号:");
		sizeLabel.setBounds(430, 10, 55, 15);
		add(sizeLabel);
		
		familyField = new JTextField(getFamilyString());
		familyField.setBounds(10, 25, 240, 25);
		add(familyField);
		familyField.setColumns(10);
		
		styleField = new JTextField(getStyleString());
		styleField.setBounds(260, 25, 160, 25);
		add(styleField);
		styleField.setColumns(10);
		
		sizeField = new JTextField(getSizeString());
		sizeField.setBounds(430, 25, 50, 25);
		add(sizeField);
		sizeField.setColumns(10);
		 
		familyList= new JList();
		familyList.setModel(createFamilyListModel());
		familyList.setCellRenderer(createFamilyListRenderer());
		familyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		familyList.setSelectedValue(familyField.getText(), true);
		installFamilyListSelectionListener();
		
		styleList = new JList();
		styleList.setModel(createStyleListModel());
		styleList.setCellRenderer(createStyleListRenderer());
		styleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		styleList.setSelectedValue(styleField.getText(), true);
		installStyleListSelectionListener();
		
		sizeList = new JList();
		sizeList.setModel(createSizeListModel());
		sizeList.setCellRenderer(createSizeListRenderer());
		sizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sizeList.setSelectedValue(sizeField.getText(), true);
		installSizeListSelectionListener();
		
		installDocumentListener(familyField,familyList);
		installDocumentListener(styleField,styleList);
		installDocumentListener(sizeField,sizeList);
		
		keyUpDownFromFieldToList();
		scrollVisibleOnComponentResize();
		
		JScrollPane familyScrollPane = new JScrollPane();
		familyScrollPane.setBounds(10, 50, 240, 155);
		add(familyScrollPane);
		familyScrollPane.setViewportView(familyList);
		familyScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JScrollPane styleScrollPane = new JScrollPane();
		styleScrollPane.setBounds(260, 50, 160, 155);
		add(styleScrollPane);
		styleScrollPane.setViewportView(styleList);
		styleScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JScrollPane sizeScrollPane = new JScrollPane();
		sizeScrollPane.setBounds(430, 50, 50, 155);
		add(sizeScrollPane);
		sizeScrollPane.setViewportView(sizeList);
		sizeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JPanel effectPanel = new JPanel(null);
		effectPanel.setBounds(10, 215, 240, 140);
		add(effectPanel);
		effectPanel.setBorder(new TitledBorder(null, "效果", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel examplePanel=new JPanel(null);
		examplePanel.setBounds(260, 215, 220, 75);
		add(examplePanel);
		examplePanel.setBorder(new TitledBorder(null, "示例", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel colorLabel = new JLabel("颜色:");
		colorLabel.setBounds(15, 70, 60, 25);
		effectPanel.add(colorLabel);
		
		JLabel charsetLabel = new JLabel("字符集:");
		charsetLabel.setBounds(260, 300, 220, 15);
		add(charsetLabel);
		
		strikethroughCheck = new JCheckBox("删除线");
		strikethroughCheck.setBounds(15, 20, 110, 25);
		effectPanel.add(strikethroughCheck);
		if(TextAttribute.STRIKETHROUGH_ON.equals(textAttrsMap.get(TextAttribute.STRIKETHROUGH)))
		{
			strikethroughCheck.setSelected(true);
		}
		installStrikethroughItemListener();
		
		underlineCheck = new JCheckBox("下划线");
		underlineCheck.setBounds(15, 45, 110, 25);
		effectPanel.add(underlineCheck);
		if(TextAttribute.UNDERLINE_ON.equals(textAttrsMap.get(TextAttribute.UNDERLINE)))
		{
			underlineCheck.setSelected(true);
		}
		installUnderlineItemListener();
		
		Color textColor=getTextColor();
		
		colorPickerTrigger = new JLabel("");
		colorPickerTrigger.setBounds(15, 95, 100, 25);
		effectPanel.add(colorPickerTrigger);
		installColorPickListener();
		setPointCursor(colorPickerTrigger);
		colorPickerTrigger.setOpaque(true);
		colorPickerTrigger.setBackground(textColor);
		colorPickerTrigger.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		
		colorTipField = new JTextField(getColorTipText(textColor));
		colorTipField.setBounds(120, 95, 100, 25);
		effectPanel.add(colorTipField);
		colorTipField.setEditable(false);
		colorTipField.setColumns(10);
		
		example = new JTextField(exampleText[0]);
		example.setBounds(10, 20, 200, 45);
		examplePanel.add(example);
		example.setFont(font);
		example.setEnabled(false);
		example.setDisabledTextColor(textColor);
		example.setBorder(BorderFactory.createEmptyBorder());
		example.setOpaque(false);
		example.setMargin(new Insets(0, 0, 0, 0));
		
		charsetCombo = new JComboBox();
		charsetCombo.setBounds(260, 320, 220, 25);
		add(charsetCombo);
		charsetCombo.setModel(new DefaultComboBoxModel(new String[] {"中文 GBK", "西文 ISO-8859-1"}));
		installCharsetActionListener();
		
		okButton = new JButton("确定(O)");
		okButton.setMnemonic(KeyEvent.VK_O);
		okButton.setDisplayedMnemonicIndex(3);
		okButton.setBounds(495, 25, 80, 30);
		add(okButton);
		
		cancelButton = new JButton("取消(C)");
		cancelButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.setDisplayedMnemonicIndex(3);
		cancelButton.setBounds(495, 60, 80, 30);
		add(cancelButton);
		
		applyButton = new JButton("应用(A)");
		applyButton.setMnemonic(KeyEvent.VK_A);
		applyButton.setDisplayedMnemonicIndex(3);
		applyButton.setBounds(495, 95, 80, 30);
		add(applyButton);
		
		resetButton = new JButton("重置(R)");
		resetButton.setMnemonic(KeyEvent.VK_R);
		resetButton.setDisplayedMnemonicIndex(3);
		resetButton.setBounds(495, 130, 80, 30);
		add(resetButton);
		
		Dimension d=new Dimension(590,400);
		setSize(d);
		setPreferredSize(d);
		setMinimumSize(d);
		
		applyFontToExample();
	}
	
	/**
	 * 实例弹出字体对话框,如果在构造后,调用此方法前不做任何处理,将有确认,取消,重置,应用四个按钮.<br/>
	 * 确认和取消按钮具有在点击时销毁窗口的默认行为,如果给此按钮添加事件监听,勿需再次销毁窗口.<br/>
	 * 而重置按钮和应用按钮默认没有任何行为,需自己添加行为;<br/>
	 * 对于重置按钮可简单的调用resetTo方法,传入要重置到的那个字体即可;<br/>
	 * 如果不需要某个按钮可将其可见性设为false,由于此对话框采用绝对布局,并且应用按钮在重置按钮上方,可能需要在设置可见性前调用swapApplyResetPosition交换两个按钮的位置
	 * @param initFont 初始字体
	 * @param parent 对话框父组件
	 * @param resetOnCancel 当点击取消时是否重置字体为初始字体,如果为true,当点击取消时,仍可用getChoosedFont方法获取字体,只不过返回的是初始字体
	 * @return 返回true表示点击了确认,可用getChoosedFont方法获取选择的字体;否则返回false,此时调用getChoosedFont方法可能会产生意外的结果
	 */
	public boolean showFontDialog(Font initFont,Component parent,boolean resetOnCancel)
	{
		JDialog dialog=createDialog(parent);
		installDefOKCancelActionTo(dialog, resetOnCancel);
		dialog.setVisible(true);
		if("ok".equals(ok_cancel))
		{
			return true;
		}
		return false;
	}
	
	/**获取确认按钮*/
	public JButton getOKButton()
	{
		return okButton;
	}
	
	/**获取取消按钮*/
	public JButton getCancelButton()
	{
		return cancelButton;
	}
	
	/**获取应用按钮*/
	public JButton getApplyButton()
	{
		return applyButton;
	}
	
	/**获取重置按钮*/
	public JButton getResetButton()
	{
		return resetButton;
	}
	
	/**交换应用和重置按钮的位置*/
	public void swapApplyResetPosition()
	{
		if(applyButton.getY()==95)
		{
			applyButton.setLocation(495, 130);
			resetButton.setLocation(495, 95);
		}else{
			resetButton.setLocation(495, 130);
			applyButton.setLocation(495, 95);
		}
	}
	
	/**获取选择的字体,如果需要从此类继承,注意此方法将实际的应用各种属性到实际的字体成员,是核心方法之一*/
	public Font getChoosedFont()
	{
		return m_font=m_font.deriveFont(textAttrsMap);
	}
	
	/**重置到指定字体*/
	public void resetTo(Font font)
	{
		textAttrsMap=new HashMap<TextAttribute, Object>(font.getAttributes());
		familyList.setSelectedValue(getFamilyString(), true);
		styleList.setSelectedValue(getStyleString(), true);
		sizeList.setSelectedValue(getSizeString(), true);
		colorPickerTrigger.setBackground(getTextColor());
		if(TextAttribute.STRIKETHROUGH_ON.equals(textAttrsMap.get(TextAttribute.STRIKETHROUGH)))
		{
			strikethroughCheck.setSelected(true);
		}else{
			strikethroughCheck.setSelected(false);
		}
		if(TextAttribute.UNDERLINE_ON.equals(textAttrsMap.get(TextAttribute.UNDERLINE)))
		{
			underlineCheck.setSelected(true);
		}else{
			underlineCheck.setSelected(false);
		}
	}
	
	protected JDialog createDialog(Component parent)
	{
		Window wnd=null;
		if(parent!=null)
		{
			wnd=(parent instanceof Window)?(Window)parent:SwingUtilities.windowForComponent(parent);
		}
		JDialog dialog=new JDialog(wnd);
		dialog.add(this);
		dialog.getRootPane().setDefaultButton(okButton);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setTitle("字体");
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(wnd);
		return dialog;
	}
	
	protected String getFamilyString()
	{
		String family=(String)textAttrsMap.get(TextAttribute.FAMILY);
		return family==null?m_font.getFamily():family; 
	}
	
	protected String getStyleString()
	{
		return translateStyle((Float)textAttrsMap.get(TextAttribute.WEIGHT),(Float)textAttrsMap.get(TextAttribute.POSTURE),(Float)textAttrsMap.get(TextAttribute.WIDTH));
	}
	
	protected String getSizeString()
	{
		Number size=(Number)textAttrsMap.get(TextAttribute.SIZE);
		return size==null?"12":String.valueOf(size.intValue());
	}
	
	protected Color getTextColor()
	{
		Color color=(Color)textAttrsMap.get(TextAttribute.FOREGROUND);
		return color==null?color=Color.BLACK:color;
	}
	
	protected ListModel createFamilyListModel()
	{
		return new AbstractListModel()
		{
			private static final long serialVersionUID = 1L;
			private List<String> fontFamilys;
			
			private void initFontFamilys()
			{
				String[] szFonts=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
				fontFamilys=new ArrayList<String>();
				Font font=new Font("微软雅黑",Font.PLAIN,12);
				Map<TextAttribute,Object> attrs=new HashMap<TextAttribute, Object>(font.getAttributes());
				for(String szFont:szFonts)
				{
					attrs.put(TextAttribute.FAMILY, szFont);
					font=font.deriveFont(attrs);
					if(canDisplayGBK(font)||canDisplayISO(font))
					{
						fontFamilys.add(szFont);
					}
				}
			}
			public List<String> getFontFamilys()
			{
				 if(fontFamilys==null)
				 {
					 initFontFamilys();
				 }
				 return fontFamilys;
			}
			@Override
			public int getSize()
			{
				return getFontFamilys().size();
			}
			@Override
			public Object getElementAt(int index)
			{
				return getFontFamilys().get(index);
			}
		};
	}
	
	protected ListCellRenderer createFamilyListRenderer()
	{
		return new ListCellRenderer()
		{
			JLabel label=new JLabel();
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus)
			{
				label.setText(value.toString());
				label.setOpaque(true);
				label.setForeground(Color.BLACK);
				label.setBackground(Color.WHITE);
				Font font=new Font(value.toString(),Font.PLAIN,14);
				if(canDisplayISO(font)||canDisplayGBK(font))
				{
					label.setFont(font);
				}else{
					label.setFont(label.getFont().deriveFont(14));
				}
				if(isSelected)
				{
					label.setBackground(UIManager.getColor("List.selectionBackground"));
					label.setForeground(UIManager.getColor("List.selectionForeground"));
				}
				return label;
			}
		};
	}
	
	protected void installFamilyListSelectionListener()
	{
		familyList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				isSelectListNotKeyDown=true;
				if(!familyList.isSelectionEmpty())
				{
					String familys=familyList.getSelectedValue().toString();
					familyField.setText(familys);
					textAttrsMap.put(TextAttribute.FAMILY, familys);
					applyFontToExample();
				}
				isSelectListNotKeyDown=false;
			}
		});
	}
	
	protected ListModel createStyleListModel()
	{
		DefaultListModel dlm=new DefaultListModel();
		for(String item:styleType)
		{
			dlm.addElement(item);
		}
		return dlm;
	}
	
	protected ListCellRenderer createStyleListRenderer()
	{
		return new ListCellRenderer()
		{
			JLabel label=new JLabel();
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus)
			{
				label.setText(value.toString());
				label.setOpaque(true);
				label.setForeground(Color.BLACK);
				label.setBackground(Color.WHITE);
				Font font=label.getFont();
				Map<TextAttribute, Object> attrs=new HashMap<TextAttribute, Object>(font.getAttributes());
				attrs.put(TextAttribute.SIZE, 12);
				label.setFont(font.deriveFont(parseStyle(value.toString(), attrs)));
				if(isSelected)
				{
					label.setBackground(UIManager.getColor("List.selectionBackground"));
					label.setForeground(UIManager.getColor("List.selectionForeground"));
				}
				return label;
			}
		};
	}
	
	protected void installStyleListSelectionListener()
	{
		styleList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				isSelectListNotKeyDown=true;
				if(!styleList.isSelectionEmpty())
				{
					String styles=styleList.getSelectedValue().toString();
					styleField.setText(styles);
					parseStyle(styles, textAttrsMap);
					applyFontToExample();
				}
				isSelectListNotKeyDown=false;
			}
		});
	}
	
	protected ListModel createSizeListModel()
	{
		DefaultListModel listModel=new DefaultListModel();
		for(int i=8;i<=72;i++)
		{
			listModel.addElement(String.valueOf(i));
		}
		for(String size:zhSizeType)
		{
			listModel.addElement(size);
		}
		return listModel;
	}
	
	protected ListCellRenderer createSizeListRenderer()
	{
		return new DefaultListCellRenderer();
	}
	
	protected void installSizeListSelectionListener()
	{
		sizeList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				isSelectListNotKeyDown=true;
				if(!sizeList.isSelectionEmpty())
				{
					String size=sizeList.getSelectedValue().toString();
					sizeField.setText(size);
					float isize;
					if(Character.isDigit(size.charAt(0)))
					{
						isize=Integer.parseInt(size);
					}else{
						isize=parseSize(size);
					}
					isize=isize==0?8:isize;
					textAttrsMap.put(TextAttribute.SIZE, isize);
					applyFontToExample();
				}
				isSelectListNotKeyDown=false;
			}
		});
	}
	
	protected void keyUpDownFromFieldToList()
	{
		familyField.addKeyListener(new KeyAdapter()
		{
			boolean isFirst=true;
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if(isFirst)
					{
						isFirst=false;
						familyList.setSelectedIndex(familyList.getFirstVisibleIndex());
					}
					e.setSource(familyList);
					familyList.dispatchEvent(e);
				}else{
					isFirst=true;
				}
			}
		});
		styleField.addKeyListener(new KeyAdapter()
		{
			boolean isFirst=true;
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if(isFirst)
					{
						isFirst=false;
						styleList.setSelectedIndex(styleList.getFirstVisibleIndex());
					}
					e.setSource(styleList);
					styleList.dispatchEvent(e);
				}else{
					isFirst=true;
				}
			}
		});
		sizeField.addKeyListener(new KeyAdapter()
		{
			boolean isFirst=true;
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if(isFirst)
					{
						isFirst=false;
						sizeList.setSelectedIndex(sizeList.getFirstVisibleIndex());
					}
					e.setSource(sizeList);
					sizeList.dispatchEvent(e);
				}else{
					isFirst=true;
				}
			}
		});
	}
	
	protected void scrollVisibleOnComponentResize()
	{
		familyList.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				familyList.ensureIndexIsVisible(familyList.getSelectedIndex());
			}
		});
		styleList.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				styleList.ensureIndexIsVisible(styleList.getSelectedIndex());
			}
		});
		sizeList.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				sizeList.ensureIndexIsVisible(sizeList.getSelectedIndex());
			}
		});
	}
	
	protected void installStrikethroughItemListener()
	{
		strikethroughCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(strikethroughCheck.isSelected())
				{
					textAttrsMap.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
				}else{
					textAttrsMap.put(TextAttribute.STRIKETHROUGH, false);
				}
				applyFontToExample();
			}
		});
	}
	
	protected void installUnderlineItemListener()
	{
		underlineCheck.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if(underlineCheck.isSelected())
				{
					textAttrsMap.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				}else{
					textAttrsMap.put(TextAttribute.UNDERLINE, -1);
				}
				applyFontToExample();
			}
		});
	}
	
	protected void installColorPickListener()
	{
		colorPickerTrigger.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				 Color color=JColorChooser.showDialog(JohnFontChooser.this, "设置字体颜色", getTextColor());
				 if(color!=null)
				 {
					 colorPickerTrigger.setBackground(color);
				 }
			}
		});
		colorPickerTrigger.addPropertyChangeListener(new PropertyChangeListener()
		{
			@Override
			public void propertyChange(PropertyChangeEvent evt)
			{
				if("background".equals(evt.getPropertyName()))
				{
					if(colorTipField!=null)
					{
						Color color=(Color)evt.getNewValue();
						colorTipField.setText(getColorTipText(color));
						textAttrsMap.put(TextAttribute.FOREGROUND, color);
						applyFontToExample();
					}
				}
			}
		});
	}
	
	protected void setPointCursor(final Component comp)
	{
		MouseAdapter adapter=new MouseAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent paramMouseEvent)
			{
				 comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent paramMouseEvent)
			{
				 comp.setCursor(Cursor.getDefaultCursor());
			}
		};
		comp.addMouseMotionListener(adapter);
		comp.addMouseListener(adapter);
	}
	
	protected void installCharsetActionListener()
	{
		charsetCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(charsetCombo.getSelectedIndex())
				{
				case 0:
					example.setText(exampleText[0]);
					break;
				case 1:
					example.setText(exampleText[1]);
					break;
				default:
					break;
				}
				example.setCaretPosition(0);
			}
		});
	}
	
	protected void installDefOKCancelActionTo(final JDialog dialog,final boolean resetOnCancel)
	{
		okButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ok_cancel="ok";
				if(dialog!=null) 
					dialog.dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ok_cancel="cancel";
				if(dialog!=null)
					dialog.dispose();
				if(resetOnCancel)
					resetChoose();
			}
		});
		if(dialog!=null)
		{
			dialog.addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					ok_cancel="cancel";
					if(resetOnCancel)
						resetChoose();
				}
			});
		}
	}
	
	protected void installDocumentListener(final JTextField field,final JList jList)
	{
		field.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				filterSelectItemFromSearchString(field,jList,e);
			}
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				filterSelectItemFromSearchString(field,jList,e);
			}
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				filterSelectItemFromSearchString(field,jList,e);
			}
		});
	}
	
	protected void filterSelectItemFromSearchString(final JTextField field,final JList jList,final DocumentEvent e)
	{
		if(!isSelectListNotKeyDown)
		{
			String text=field.getText();
			ListModel model=jList.getModel();
			for(int i=0;i<model.getSize();i++)
			{
				String item=model.getElementAt(i).toString();
				if(item.startsWith(text))
				{
					jList.ensureIndexIsVisible(i);
					System.out.println(i);
					return;
				}
			}
		}
	}
	
	/**应用字体到示例中,如果需要继承此类,注意,所有的组件输入或选择都会反映到示例中,即是通过调用此方法,
	 * 事实上,获取的最后字体其实是示例字体,就由此产生,是核心方法之一*/
	protected void applyFontToExample()
	{
		 example.setFont(getChoosedFont());
		 if(canDisplayGBK(m_font)&&charsetCombo.getSelectedIndex()==1)
		 {
			 charsetCombo.setSelectedIndex(0);
		 }
		 if(!canDisplayGBK(m_font)&&charsetCombo.getSelectedIndex()==0)
		 {
			 charsetCombo.setSelectedIndex(1);
		 }
		 example.setCaretPosition(0);
	}
	
	protected String translateStyle(Float weight,Float posture,Float width)
	{
		boolean weight_regular=weight==null||weight.floatValue()<=1.0f;
		boolean width_regular=width==null||width.floatValue()<=1.0f;
		boolean posture_regular=posture==null||posture.floatValue()==0.0f;
		
		if(weight_regular&&width_regular&&posture_regular)
		{
			return styleType[0];
		}
		else
		if(weight_regular&&width_regular&&!posture_regular)
		{
			return styleType[1];
		}
		else
		if(!weight_regular&&width_regular&&posture_regular)
		{
			return styleType[2];
		}
		else
		if(weight_regular&&!width_regular&&posture_regular)
		{
			return styleType[3];
		}
		else
		if(!weight_regular&&width_regular&&!posture_regular)
		{
			return styleType[4];
		}
		else
		if(weight_regular&&!width_regular&&!posture_regular)
		{
			return styleType[5];
		}
		else
		if(!weight_regular&&!width_regular&&posture_regular)
		{
			return styleType[6];
		}
		else
		if(!weight_regular&&!width_regular&&!posture_regular)
		{
			return styleType[7];
		}
		return "";
	}
	
	protected Map<TextAttribute, Object> parseStyle(String styles,Map<TextAttribute, Object> attrs)
	{ 
		if(styleType[0].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_REGULAR);
		}
		else
		if(styleType[1].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_REGULAR);
		}
		else
		if(styleType[2].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_REGULAR);
		}
		else
		if(styleType[3].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_EXTENDED);
		}
		else
		if (styleType[4].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_REGULAR);
		}
		else
		if (styleType[5].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_EXTENDED);
		} 
		else 
		if (styleType[6].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_EXTENDED);
		} 
		else 
		if (styleType[7].equals(styles))
		{
			attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
			attrs.put(TextAttribute.WIDTH,TextAttribute.WIDTH_EXTENDED);
		}
		return attrs;
	}
	
	protected float parseSize(String size)
	{
		if (zhSizeType[0].equals(size))
		{
			return 42;
		} else if (zhSizeType[1].equals(size))
		{
			return 36;
		} else if (zhSizeType[2].equals(size))
		{
			return 26;
		} else if (zhSizeType[3].equals(size))
		{
			return 24;
		} else if (zhSizeType[4].equals(size))
		{
			return 22;
		} else if (zhSizeType[5].equals(size))
		{
			return 18;
		} else if (zhSizeType[6].equals(size))
		{
			return 16;
		} else if (zhSizeType[7].equals(size))
		{
			return 15;
		} else if (zhSizeType[8].equals(size))
		{
			return 14;
		} else if (zhSizeType[9].equals(size))
		{
			return 12;
		} else if (zhSizeType[10].equals(size))
		{
			return 10.5f;
		} else if (zhSizeType[11].equals(size))
		{
			return 9;
		} else if (zhSizeType[12].equals(size))
		{
			return 7.5f;
		} else if (zhSizeType[13].equals(size))
		{
			return 6.5f;
		} else if (zhSizeType[14].equals(size))
		{
			return 5.5f;
		}else if (zhSizeType[15].equals(size))
		{
			return 5;
		}
		return 0;
	}
	
	protected String getColorTipText(Color color)
	{
		return "#"+Integer.toHexString(color.getRGB()).substring(2);
	}
	
	protected boolean canDisplayGBK(Font font)
	{
		return font.canDisplay('中');
	}
	
	protected boolean canDisplayISO(Font font)
	{
		return font.canDisplay('A')||font.canDisplay('a')||font.canDisplay('0');
	}
	
	protected void resetChoose()
	{
		m_font=old_font;
		textAttrsMap=new HashMap<TextAttribute, Object>(old_font.getAttributes());
	}
	
}
