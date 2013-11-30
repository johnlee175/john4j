package com.johnsoft.library.swing.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

// @author  santhosh kumar - santhosh@in.fiorano.com 
public abstract class JohnDropDownButton extends JButton implements ChangeListener,
		PopupMenuListener, ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private final JButton mainButton = this;

	private final JButton arrowButton = new JButton(new SmallDownArrow());

	private boolean popupVisible = false;

	public JohnDropDownButton() {
		mainButton.getModel().addChangeListener(this);
		arrowButton.getModel().addChangeListener(this);
		arrowButton.addActionListener(this);
		arrowButton.setMargin(new Insets(3, 0, 3, 0));
		mainButton.addPropertyChangeListener("enabled", this); // NOI18N
	}

	/*------------------------------[ PropertyChangeListener ]---------------------------------------------------*/

	public void propertyChange(PropertyChangeEvent evt) {
		arrowButton.setEnabled(mainButton.isEnabled());
	}

	private static class SmallDownArrow implements Icon {

		Color arrowColor = Color.black;

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(arrowColor);
			g.drawLine(x, y, x + 4, y);
			g.drawLine(x + 1, y + 1, x + 3, y + 1);
			g.drawLine(x + 2, y + 2, x + 2, y + 2);
		}

		public int getIconWidth() {
			return 6;
		}

		public int getIconHeight() {
			return 4;
		}

	}

	/*------------------------------[ ChangeListener ]---------------------------------------------------*/

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == mainButton.getModel()) {
			if (popupVisible && !mainButton.getModel().isRollover()) {
				mainButton.getModel().setRollover(true);
				return;
			}
			arrowButton.getModel().setRollover(
					mainButton.getModel().isRollover());
			arrowButton.setSelected(mainButton.getModel().isArmed()
					&& mainButton.getModel().isPressed());
		} else {
			if (popupVisible && !arrowButton.getModel().isSelected()) {
				arrowButton.getModel().setSelected(true);
				return;
			}
			mainButton.getModel().setRollover(
					arrowButton.getModel().isRollover());
		}
	}

	/*------------------------------[ ActionListener ]---------------------------------------------------*/

	public void actionPerformed(ActionEvent ae) {
		JPopupMenu popup = getPopupMenu();
		popup.addPopupMenuListener(this);
		popup.show(mainButton, 0, mainButton.getHeight());
	}

	/*------------------------------[ PopupMenuListener ]---------------------------------------------------*/

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		popupVisible = true;
		mainButton.getModel().setRollover(true);
		arrowButton.getModel().setSelected(true);
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		popupVisible = false;

		mainButton.getModel().setRollover(false);
		arrowButton.getModel().setSelected(false);
		((JPopupMenu) e.getSource()).removePopupMenuListener(this); // act as
		// good
		// programmer
		// :)
	}

	public void popupMenuCanceled(PopupMenuEvent e) {
		popupVisible = false;
	}

	/*------------------------------[ Other Methods ]---------------------------------------------------*/

	protected abstract JPopupMenu getPopupMenu();

	public JButton addToToolBar(JToolBar toolbar) {
		toolbar.add(mainButton);
		toolbar.add(arrowButton);
		return mainButton;
	}

//	public static void main(String[] args) throws ClassNotFoundException,
//			InstantiationException, IllegalAccessException,
//			UnsupportedLookAndFeelException {
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		JFrame frame = new JFrame();
//		JPanel contentPanel = (JPanel) frame.getContentPane();
//		contentPanel.setLayout(new BorderLayout());
//		JToolBar toolbar = new JToolBar();
//		DropDownButton drowDownButton = new DropDownButton() {
//			private static final long serialVersionUID = 1L;
//			@Override
//			protected JPopupMenu getPopupMenu() {
//				JPopupMenu popupMenu = new JPopupMenu();
//				popupMenu.add(new JMenuItem("hello"));
//				return popupMenu;
//			}
//
//		};
//		drowDownButton.setText("hello");
//		drowDownButton.setFocusable(false);
//		drowDownButton.addToToolBar(toolbar);
//		contentPanel.add(toolbar, BorderLayout.NORTH);
//		contentPanel.add(new JPanel(), BorderLayout.CENTER);
//		frame.pack();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//	}
}