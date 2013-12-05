package com.johnsoft.library.swing.component.autocomplete;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import com.johnsoft.library.util.gui.JohnSwingUtilities;


@SuppressWarnings({"rawtypes","unchecked"})
public class JohnAutoCompleteInput extends JTextField implements AWTEventListener,
		ListCellRenderer, AncestorListener, KeyListener, DocumentListener,
		MouseListener, MouseMotionListener, ListSelectionListener
{
	private static final long serialVersionUID = 1L;

	public enum JohnMatchType
	{
		STARTSWITH, ENDWIDTH, CONTAINS, INDEXOF
	};

	public enum JohnOutVisible
	{
		WRAPLINE, EXTEND, TOOLTIP, SCROLL
	};
	
	public static final int COMMIT_DOUBLECLICK=1<<0;
	public static final int COMMIT_ENTERKEY=1<<1;
	public static final int COMMIT_ROLLOVER=1<<2;
	public static final int COMMIT_UPDOWNKEY=1<<3;	

	protected JTextField jtf;

	protected boolean ignoreCase;
	protected boolean upperCaseIfIgnore;
	protected JohnMatchType matchType;
	protected boolean updateListAllowed;

	protected JScrollPane jsp;
	protected JList jList;
	protected DefaultListModel jListModel;
	protected int visibleRowCount;
	protected int wrapLineIndex;
	protected int rollOverIndex;
	protected String multiMatchSplitSeparator;

	protected JohnAutoCompleteModel model;
	protected boolean inputTextAndShowTextAllMatched;
	protected Set showItemSet;

	protected Popup popup;
	protected boolean autoCompleteShowing;
	protected JohnOutVisible outVisible;

	protected Popup toolTip;

	protected Border listRollOverBorder;
	protected Color listRollOverBackground;
	protected Color listRollOverForeground;
	
	protected int commitStyle;
	
	protected Object selectedValue;
	
	public static int getFullCommitStyle()
	{
		return 0x0000000F;
	}
	
	public JohnAutoCompleteInput()
	{
		this((JohnAutoCompleteModel)null);
	}
	
	public JohnAutoCompleteInput(JohnAutoCompleteModel model)
	{
		super();
		jtf=this;
		this.model=model;
		init();
	}

	public JohnAutoCompleteInput(int columns)
	{
		this(columns,null);
	}
	
	public JohnAutoCompleteInput(int columns,JohnAutoCompleteModel model)
	{
		super(columns);
		jtf = this;
		this.model=model;
		init();
	}

	public JohnAutoCompleteInput(String text)
	{
		 this(text,null);
	}
	
	public JohnAutoCompleteInput(String text,JohnAutoCompleteModel model)
	{
		super(text);
		jtf = this;
		this.model=model;
		init();
	}

	public JohnAutoCompleteInput(String text, int columns)
	{
		 this(text,columns,null);
	}
	
	public JohnAutoCompleteInput(String text, int columns,JohnAutoCompleteModel model)
	{
		super(text, columns);
		jtf = this;
		this.model=model;
		init();
	}
	
	public JohnAutoCompleteInput(Document doc)
	{
		super();
		setDocument(doc);
		jtf = this;
		init();
	}
	
	public JohnAutoCompleteInput(Document doc,JohnAutoCompleteModel model)
	{
		super();
		setDocument(doc);
		jtf = this;
		this.model=model;
		init();
	}
	
	public JohnAutoCompleteInput(Document doc, String text, int columns, JohnAutoCompleteModel model)
	{
		super(doc, text, columns);
		jtf = this;
		this.model=model;
		init();
	}
	
	public JohnAutoCompleteInput(JTextField jtf)
	{
		this(jtf,null);
	}
	
	public JohnAutoCompleteInput(JTextField jtf,JohnAutoCompleteModel model)
	{
		super();
		this.jtf = jtf;
		this.model=model;
		init();
	}
	
	protected void init()
	{
		initUserDefaults();
		initComponentDefaults();
		installListeners();
	}
	
	protected void initUserDefaults()
	{
		ignoreCase = true;
		upperCaseIfIgnore = true;
		inputTextAndShowTextAllMatched = false;
		wrapLineIndex = -1;
		visibleRowCount = 10;
		matchType = JohnMatchType.INDEXOF;
		outVisible = JohnOutVisible.TOOLTIP;
		listRollOverBackground = Color.LIGHT_GRAY;
		listRollOverForeground = Color.DARK_GRAY;
		listRollOverBorder = BorderFactory.createEmptyBorder();
		setTraditionalCommitStyle();
	}
	
	protected void initComponentDefaults()
	{
		updateListAllowed = true;
		rollOverIndex = -1;
		showItemSet = new HashSet();
		jListModel = new DefaultListModel();
		jList = new JList();
		jsp = new JScrollPane(jList);
		jList.setCellRenderer(this);
		jList.setVisibleRowCount(visibleRowCount);
	}

	protected void installListeners()
	{
		jtf.addAncestorListener(this);
		jtf.addKeyListener(this);
		jtf.getDocument().addDocumentListener(this);
		jList.addListSelectionListener(this);
		jList.addMouseListener(this);
		jList.addMouseMotionListener(this);
	}
	
	public void setTraditionalCommitStyle()
	{
		setCommitStyle(COMMIT_DOUBLECLICK | COMMIT_ENTERKEY);
	}
	
	public void setBaiduCommitStyle()
	{
		setCommitStyle(COMMIT_ROLLOVER | COMMIT_UPDOWNKEY);
	}
	
	protected void updateList()
	{
		if (!updateListAllowed)
			return;
		jListModel.removeAllElements();
		showItemSet.clear();
		String text = jtf.getText();
		text = ignoreCase ? toSameCase(text) : text;
		if (!text.trim().isEmpty())
		{
			filter(model.getInputKeys(), text, true);
			if (inputTextAndShowTextAllMatched)
			{
				filter(model.getShowKeys(), text, false);
			}
		}
		List tmp=new ArrayList(); 
		for (Object obj : showItemSet)
		{
			String item=model.getKeyString(obj);
			if(item!=null&&!item.trim().isEmpty())
			{
				tmp.add(item);
			}
		}
		sort(tmp);
		for(Object item : tmp)
		{
			jListModel.addElement(item);
		}
		jList.setModel(jListModel);
		if (jListModel.size() > 0)
		{
			hideAutoComplete();
			showAutoComplete();
		} else
		{
			hideAutoComplete();
		}
	}
	
	protected void sort(List list)
	{
		Collections.sort(list);
	}
	
	protected String[] split(String text)
	{
		return (multiMatchSplitSeparator==null||multiMatchSplitSeparator.isEmpty())?
				new String[]{text}:text.split(multiMatchSplitSeparator);
	}
	
	protected void customFilter(Set showItemSet, JohnAutoCompleteModel model, 
			Object item, String utext, boolean translateItem)
	{
	}

	protected void filter(Collection collection, String text,
			boolean translateItem)
	{
		for (Object item : collection)
		{
			if(item==null) continue;
			String itemStr = model.getKeyString(item);
			String itemUL = ignoreCase ? toSameCase(itemStr) : itemStr;
			String[] utexts=split(text);
			for(String utext:utexts)
			{
				if (matchType == JohnMatchType.STARTSWITH)
				{
					if (itemUL.startsWith(utext))
					{
						showItemSet.add(wrapLine(translateItem ? model.getShowValue(item)
								: itemStr));
					}
				} else if (matchType == JohnMatchType.ENDWIDTH)
				{
					if (itemUL.endsWith(utext))
					{
						showItemSet.add(wrapLine(translateItem ? model.getShowValue(item)
								: itemStr));
					}
				} else if (matchType == JohnMatchType.CONTAINS)
				{
					if (itemUL.contains(utext))
					{
						showItemSet.add(wrapLine(translateItem ? model.getShowValue(item)
								: itemStr));
					}
				} else if (matchType == JohnMatchType.INDEXOF)
				{
					int lastIdx=-1;
					boolean matched=true;
					for(int i=0;i<utext.length();i++)
					{
						 char ch=utext.charAt(i);
						 int idx=itemUL.indexOf(String.valueOf(ch),lastIdx+1);
						 if(idx<=lastIdx)
						 {
							 matched=false;
						 }else{
							 lastIdx=idx;
						 }
					}
					if(matched)
					{
						showItemSet.add(wrapLine(translateItem ? model.getShowValue(item)
								: itemStr));
					}
				} else customFilter(showItemSet, model, item, utext, translateItem);
			}
		}
	}

	protected String wrapLine(String item)
	{
		if (outVisible == JohnOutVisible.WRAPLINE)
		{
			if (wrapLineIndex < 0)
			{
				int columns = jtf.getColumns();
				wrapLineIndex = columns > 1 ? columns : 1;
			}
			if (item.length() > wrapLineIndex)
			{
				StringBuffer sb = new StringBuffer("<html>");
				int start = 0;
				while (start < item.length())
				{
					if (item.length() - (start + wrapLineIndex) > 0)
					{
						sb.append(item.substring(start, start + wrapLineIndex))
								.append("<br/>&nbsp;&nbsp;");
					} else
					{
						sb.append(item.substring(start));
					}
					start += wrapLineIndex;
				}
				return sb.append("</html>").toString();
			}
		}
		return item;
	}

	protected String toSameCase(String text)
	{
		if (upperCaseIfIgnore)
		{
			return text.toUpperCase();
		} 
		else
		{
			return text.toLowerCase();
		}
	}

	protected void hideAutoComplete()
	{
		if (outVisible == JohnOutVisible.TOOLTIP)
		{
			hideToolTip();
		}
		if (popup != null)
		{
			popup.hide();
			popup = null;
			autoCompleteShowing = false;
			Toolkit.getDefaultToolkit().removeAWTEventListener(this);
		}
	}

	protected void showAutoComplete()
	{
		if (popup == null)
		{
			fitView();
			Point p = jtf.getLocationOnScreen();
			popup = PopupFactory.getSharedInstance().getPopup(null, jsp, p.x,
					p.y + jtf.getHeight());
			popup.show();
			autoCompleteShowing = true;
			Toolkit.getDefaultToolkit().addAWTEventListener(this,
					AWTEvent.MOUSE_EVENT_MASK+AWTEvent.WINDOW_EVENT_MASK);
		}
	}
	
	protected void fitView()
	{
		Dimension preferDim = jList.getPreferredSize();
		int viewPortHeight = jList.getPreferredScrollableViewportSize().height;
		int scrollBarW = jsp.getVerticalScrollBar().getPreferredSize().width;
		Insets insets = jsp.getInsets();
		int h = Math.min(viewPortHeight, preferDim.height) + insets.bottom
				+ insets.top;
		switch (outVisible)
		{
		case EXTEND:
		{
			int preferW = preferDim.width + scrollBarW + insets.left
					+ insets.right;
			jsp.setPreferredSize(new Dimension(
					Math.max(preferW, jtf.getWidth()), h));
		}
			break;
		case TOOLTIP:
		{
			jsp.setPreferredSize(new Dimension(jtf.getWidth(), h));
			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
			break;
		case WRAPLINE:
		{
			jsp.setPreferredSize(new Dimension(jtf.getWidth(), h));
			jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
			break;
		case SCROLL:
		{
			jsp.setPreferredSize(new Dimension(jtf.getWidth(), h));
		}
			break;
		default:
			break;
		}
	}
	
	protected void commit(Object obj)
	{
		selectedValue=obj;
		if (obj != null)
		{
			jtf.setText(getCommitString(obj));
		}
	}
	
	protected String getCommitString(Object obj)
	{
		String oldText=jtf.getText();
		String value=model.getCommitValue(obj, oldText);
		if(multiMatchSplitSeparator==null||multiMatchSplitSeparator.isEmpty()||!replacePart(value))
		{
			return value;
		}
		String[] texts=oldText.split(multiMatchSplitSeparator);
		for(int i=0;i<texts.length;i++)
		{
			if(value.contains(texts[i]))
			{
				texts[i]=formatCommitUnit(value);
			}
		}
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<texts.length-1;i++)
		{
			sb.append(texts[i]).append(multiMatchSplitSeparator);
		}
		return sb.append(texts[texts.length-1]).toString();
	}
	
	protected String formatCommitUnit(String value)
	{
		return value;
	}
	
	protected boolean replacePart(String value)
	{
		return true;
	}

	protected void commitInProtection(Object obj)
	{
		updateListAllowed = false;
		commit(obj);
		updateListAllowed = true;
	}

	protected void finishCommit()
	{
		jList.clearSelection();
		hideAutoComplete();
	}

	protected void hideToolTip()
	{
		if (toolTip != null)
		{
			toolTip.hide();
		}
	}

	protected void showToolTip()
	{
		hideToolTip();
		JComponent tipLabel=createToolTip(jList.getModel().getElementAt(rollOverIndex)
				.toString());
		int scrollBarW = 0;
		if (jList.getPreferredScrollableViewportSize().height < jList
				.getPreferredSize().height)
		{
			scrollBarW = jsp.getVerticalScrollBar().getPreferredSize().width;
		}
		if (tipLabel.getPreferredSize().width > (jtf.getWidth() - scrollBarW))
		{
			Rectangle r = jList.getCellBounds(rollOverIndex, rollOverIndex);
			Point p = r.getLocation();
			SwingUtilities.convertPointToScreen(p, jList);
			tipLabel.setSize(tipLabel.getPreferredSize().width, r.height);
			toolTip = PopupFactory.getSharedInstance().getPopup(null, tipLabel,
					p.x, p.y);
			toolTip.show();
		}
	}
	
	protected JComponent createToolTip(String showText)
	{
	    JLabel tipLabel = new JLabel();
		tipLabel.setOpaque(true);
		tipLabel.setBackground(jList.getBackground());
		tipLabel.setForeground(jList.getForeground());
		tipLabel.setFont(jList.getFont());
		tipLabel.setText(showText);
		return tipLabel;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER
				|| e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (autoCompleteShowing)
			{
				e.setSource(jList);
				jList.dispatchEvent(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER
						&& (commitStyle&COMMIT_ENTERKEY)!=0)
				{
					commit(jList.getSelectedValue());
					finishCommit();
				}
			} else
			{
				if (e.getKeyCode() == KeyEvent.VK_DOWN && jListModel.size() > 0)
				{
					showAutoComplete();
				}
			}
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			hideAutoComplete();
		}
	}
	
	@Override
	public void eventDispatched(AWTEvent event)
	{
		Object obj=event.getSource();
		if(event.getID()==MouseEvent.MOUSE_CLICKED)
		{
			MouseEvent evt=(MouseEvent)event;
			if(JohnSwingUtilities.isLButtonDBClick(evt)||JohnSwingUtilities.isLButtonDown(evt)
					&&obj!=jtf&&obj!=jsp&&obj!=jList
					&&!obj.getClass().getName().startsWith("javax.swing.Popup$"))
			{
				Component[] children=jsp.getComponents();
				boolean isChild=false;
				for(Component c:children)
				{
					if(c instanceof JScrollBar)
					{
						JScrollBar bar=(JScrollBar)c;
						Component[] comps=bar.getComponents();
						for(Component comp:comps)
						{
							if(comp==obj) isChild=true;
						}
					}
					if(c==obj) isChild=true;
				}
				if(!isChild)
				{
					hideAutoComplete();
				}
			}
			return;
		}
		if(event.getID()==WindowEvent.WINDOW_DEACTIVATED&&
				(((WindowEvent)event).getWindow()==SwingUtilities.windowForComponent(jtf)))
		{
			hideAutoComplete();
		}
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
	{
		JLabel label = new JLabel();
		label.setOpaque(true);
		label.setText(model.getKeyString(value));
		label.setFont(UIManager.getFont("List.font"));
		if (isSelected)
		{
			label.setBackground(UIManager.getColor("List.selectionBackground"));
			label.setForeground(UIManager.getColor("List.selectionForeground"));
			label.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		} else
		{
			if (index == rollOverIndex)
			{
				label.setBackground(listRollOverBackground);
				label.setForeground(listRollOverForeground);
				label.setBorder(listRollOverBorder);
			} else
			{
				label.setBackground(UIManager.getColor("List.background"));
				label.setForeground(UIManager.getColor("List.foreground"));
				label.setBorder(UIManager.getBorder("List.noFocusBorder"));
			}
		}
		return label;
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if ((commitStyle&COMMIT_UPDOWNKEY)!=0)
		{
			commitInProtection(jList.getSelectedValue());
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		int oldIndex = rollOverIndex;
		rollOverIndex = jList.locationToIndex(e.getPoint());
		jsp.repaint();
		if (rollOverIndex >= 0)
		{
			if (outVisible == JohnOutVisible.TOOLTIP
					&& oldIndex != rollOverIndex)
			{
				showToolTip();
			}
			if ((commitStyle&COMMIT_ROLLOVER)!=0)
			{
				commitInProtection(jListModel.getElementAt(rollOverIndex));
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if ((commitStyle&COMMIT_DOUBLECLICK)!=0
				&& e.getClickCount() == 2
				&& e.getButton() == MouseEvent.BUTTON1)
		{
			commit(jList.getSelectedValue());
			finishCommit();
		}
		if (e.getButton() == MouseEvent.BUTTON3)
		{
			// 添加autoComplete提示的拖拽,改变大小,排序功能,使之类似eclipse的提示框
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		updateList();
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		updateList();
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		updateList();
	}

	@Override
	public void ancestorAdded(AncestorEvent event)
	{
		hideAutoComplete();
	}

	@Override
	public void ancestorRemoved(AncestorEvent event)
	{
		hideAutoComplete();
	}

	@Override
	public void ancestorMoved(AncestorEvent event)
	{
		hideAutoComplete();
	}
	
	public Object getSelectedValue()
	{
		return selectedValue;
	}

	public boolean isUpperCaseIfIgnore()
	{
		return upperCaseIfIgnore;
	}

	public void setUpperCaseIfIgnore(boolean upperCaseIfIgnore)
	{
		this.upperCaseIfIgnore = upperCaseIfIgnore;
	}
	
	public boolean isIgnoreCase()
	{
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase)
	{
		this.ignoreCase = ignoreCase;
	}

	public boolean isInputTextAndShowTextAllMatched()
	{
		return inputTextAndShowTextAllMatched;
	}

	public void setInputTextAndShowTextAllMatched(
			boolean inputTextAndShowTextAllMatched)
	{
		this.inputTextAndShowTextAllMatched = inputTextAndShowTextAllMatched;
	}

	public JohnMatchType getMatchType()
	{
		return matchType;
	}

	public void setMatchType(JohnMatchType matchType)
	{
		this.matchType = matchType;
	}

	public int getVisibleRowCount()
	{
		return visibleRowCount;
	}

	public void setVisibleRowCount(int visibleRowCount)
	{
		this.visibleRowCount = visibleRowCount;
	}

	public int getWrapLineIndex()
	{
		return wrapLineIndex;
	}

	public void setWrapLineIndex(int wrapLineIndex)
	{
		this.wrapLineIndex = wrapLineIndex;
	}

	public JohnOutVisible getOutVisible()
	{
		return outVisible;
	}

	public void setOutVisible(JohnOutVisible outVisible)
	{
		this.outVisible = outVisible;
	}

	public Color getListRollOverBackground()
	{
		return listRollOverBackground;
	}

	public void setListRollOverBackground(Color listRollOverBackground)
	{
		this.listRollOverBackground = listRollOverBackground;
	}

	public Color getListRollOverForeground()
	{
		return listRollOverForeground;
	}

	public void setListRollOverForeground(Color listRollOverForeground)
	{
		this.listRollOverForeground = listRollOverForeground;
	}
	
	public Border getListRollOverBorder()
	{
		return listRollOverBorder;
	}

	public void setListRollOverBorder(Border listRollOverBorder)
	{
		this.listRollOverBorder = listRollOverBorder;
	}
	
	public JohnAutoCompleteModel getModel()
	{
		return model;
	}

	public void setModel(JohnAutoCompleteModel model)
	{
		this.model = model;
	}

	public int getCommitStyle()
	{
		return commitStyle;
	}

	public void setCommitStyle(int commitStyle)
	{
		this.commitStyle = commitStyle;
	}
	
	public void setMultiMatchSplitSeparator(String multiMatchSplitSeparator)
	{
		this.multiMatchSplitSeparator = multiMatchSplitSeparator;
	}
	
	public String getMultiMatchSplitSeparator()
	{
		return multiMatchSplitSeparator;
	}
	
}
