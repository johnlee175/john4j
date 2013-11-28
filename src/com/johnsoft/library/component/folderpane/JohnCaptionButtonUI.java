package com.johnsoft.library.component.folderpane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;


public class JohnCaptionButtonUI extends ComponentUI implements
		MouseMotionListener, MouseListener, FocusListener
{

	public static final Color DEFAULT_FOREGROUND = new Color(33, 93, 198);
	public static final Color DEFAULT_HOVER_COLOR = new Color(66, 142, 255);
	public static final Font DEFAULT_FONT = new Font("Dialog", Font.BOLD, 12);
	public static final Color LIGHTER = new Color(255, 255, 255);
	public static final Color DARKER = new Color(198, 211, 247);
	public static final int TEXT_LEADING_GAP = 14;
	public static final int IMAGE_TAILING_GAP = 12;
	private static Stroke DASHED_STROKE = new BasicStroke(1,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 1 },
			0);
	private static Icon iconExpanded;
	private static Icon iconFoldered;
	private static Icon hoveredExpanded;
	private static Icon hoveredFoldered;
	static
	{
		iconExpanded = new ImageIcon("images/expanded.png");
		iconFoldered = new ImageIcon("images/foldered.png");
		hoveredExpanded = new ImageIcon("images/hovered_expanded.png");
		hoveredFoldered = new ImageIcon("images/hovered_foldered.png");
	}

	private boolean armed;
	private int textLeadingGap = TEXT_LEADING_GAP;
	private int imageTailingGap = IMAGE_TAILING_GAP;
	private Color lightColor = LIGHTER;
	private Color darkColor = DARKER;
	private boolean hovered;

	protected JohnCaptionButton button;

	public static ComponentUI createUI(JComponent c)
	{
		return new JohnCaptionButtonUI();
	}

	public JohnCaptionButtonUI()
	{
	}

	public void installUI(JComponent c)
	{
		button = (JohnCaptionButton) c;
		button.setForeground(DEFAULT_FOREGROUND);
		button.setFont(DEFAULT_FONT);
		button.setFocusable(true);

		button.addMouseListener(this);
		button.addMouseMotionListener(this);
		button.addFocusListener(this);
	}

	public void uninstallUI(JComponent c)
	{
		button.removeMouseListener(this);
		button.removeMouseMotionListener(this);
		button.removeFocusListener(this);
	}

	protected void paintBackground(Graphics g)
	{
		int w = button.getWidth();
		int h = button.getHeight();
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gp = new GradientPaint(1, 1, lightColor, w - 2, 1,
				darkColor);
		g2d.setPaint(gp);
		g2d.fillRect(1, 1, w - 2, h - 1);
		gp = new GradientPaint(2, 0, lightColor, w - 4, 0, darkColor);
		g2d.setPaint(gp);
		g2d.fillRect(2, 0, w - 4, 1);
		g2d.setColor(lightColor);
		g2d.drawLine(0, 2, 0, h - 1);
		g2d.setColor(darkColor);
		g2d.drawLine(w - 1, 2, w - 1, h - 1);
	}

	public void paint(Graphics g, JComponent c)
	{
		paintBackground(g);
		paintCaptionText(g);
		paintIcon(g);
		if (armed)
			paintArmed(g);
	}

	protected void paintArmed(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.setStroke(DASHED_STROKE);
		g2d.drawRoundRect(1, 1, button.getWidth() - 3, button.getHeight() - 3,
				2, 2);
	}

	protected void paintIcon(Graphics g)
	{
		Icon icon = null;
		if (hovered)
			icon = button.isExpanded() ? hoveredExpanded : hoveredFoldered;
		else
			icon = button.isExpanded() ? iconExpanded : iconFoldered;
		int x = button.getWidth() - imageTailingGap - icon.getIconWidth();
		int y = (button.getHeight() - icon.getIconHeight()) / 2;
		icon.paintIcon(button, g, x, y);
	}

	protected void paintCaptionText(Graphics g)
	{
		FontMetrics fm = g.getFontMetrics();
		if (button.getText() != null)
		{
			g.setColor(hovered ? button.getHoverColor() : button.getForeground());
			int y = (button.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
			g.drawString(button.getText(), textLeadingGap, y);
		}
	}

	public void mousePressed(MouseEvent e)
	{
		setArmed(true);
		button.setExpanded(!button.isExpanded());
		ItemEvent evt = new ItemEvent(button, button.isExpanded() ? 0 : 1,
				button.getText(), button.isExpanded() ? ItemEvent.SELECTED
						: ItemEvent.DESELECTED);
		button.fireItemStateChanged(evt);
		button.requestFocus();
	}

	public void mouseEntered(MouseEvent e)
	{
		setHovered(true);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public void mouseExited(MouseEvent e)
	{
		setHovered(false);
	}

	void setHovered(boolean b)
	{
		hovered = b;
		button.repaint();
	}

	public void focusLost(FocusEvent e)
	{
		setArmed(false);
	}

	void setArmed(boolean b)
	{
		armed = b;
		button.repaint();
	}
	
	public void setLightColor(Color lightColor)
	{
		this.lightColor = lightColor;
	}

	public void setDarkColor(Color darkColor)
	{
		this.darkColor = darkColor;
	}

	public void mouseDragged(MouseEvent e)
	{
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void focusGained(FocusEvent e)
	{
	}
}
