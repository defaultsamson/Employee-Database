package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.CellRendererPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import data.EmployeeInfo;
import data.FullTimeEmployee;
import data.Gender;
import data.Location;
import data.PartTimeEmployee;

public class EmployeeList extends JList<EmployeeInfo> {

	private static final int LINE_X_OFFSET = -3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5854108411878572054L;

	public EmployeeList() {
		this(getDefaultListModel());
	}

	private static DefaultListModel<EmployeeInfo> getDefaultListModel() {
		DefaultListModel<EmployeeInfo> listModel = new DefaultListModel<EmployeeInfo>();
		listModel.addElement(new PartTimeEmployee(64, "Samasioduyajsdghakjsdghaskjdgoajshd", "Close", Gender.MALE, Location.MISSISSAUGA, 0.2, 12, 2, 2));
		listModel.addElement(new FullTimeEmployee(64, "Mike", "Oxlittle", Gender.FEMALE, Location.CHICAGO, 0.13, 12000));
		
		return listModel;
	}

	public EmployeeList(ListModel<EmployeeInfo> list) {
		super(list);

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Can only select one employee at a time
		setLayoutOrientation(JList.VERTICAL); // list scrolls vertically
		setCellRenderer(new EmployeeListRenderer()); // Renders the desired information
		
		setPreferredSize(new Dimension(10, 10));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Color oldColor = g.getColor();
		g.setColor(new Color(0.5F, 0.5F, 0.5F));

		int numberDrawWidth = (int) (getWidth() * (1D / 3D)) + LINE_X_OFFSET;
		g.drawLine(numberDrawWidth, 0, numberDrawWidth, getHeight());

		int nameDrawWidth = (int) (getWidth() * (1D / 3D));
		g.drawLine(numberDrawWidth + nameDrawWidth, 0, numberDrawWidth + nameDrawWidth, getHeight());

		g.setColor(oldColor);

		CellRendererPane pane = (CellRendererPane) this.getComponent(0);

		for (Component c : pane.getComponents()) {
			System.out.println(c);

			if (c instanceof JLabel) {
				((JLabel) c).setText("Dong");
			}
		}

	}
}

class EmployeeListRenderer extends JPanel implements ListCellRenderer<EmployeeInfo> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1350369091537651743L;

	private DefaultListCellRenderer defaultRenderer;

	private JLabel c1;
	private JLabel c2;
	private JLabel c3;

	private Color background = null;
	private Color foreground = null;
	private Color selectedBackground = null;
	private Color selectedForeground = null;

	public EmployeeListRenderer() {
		super();

		defaultRenderer = new DefaultListCellRenderer();

		GridLayout experimentLayout = new GridLayout(1, 3);
		setLayout(experimentLayout);

		c1 = new JLabel("");
		c2 = new JLabel("");
		c3 = new JLabel("");
		add(c1);
		add(c2);
		add(c3);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends EmployeeInfo> list, EmployeeInfo value, int index, boolean isSelected, boolean cellHasFocus) {
		DefaultListCellRenderer renderer = defaultRenderer;
		JLabel defaultLabel = (JLabel) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		c1.setText(value.getFirstName());
		c2.setText(value.getLastName());
		c3.setText("" + value.getEmployeeNumber());

		// Sets the colours based on whether the entry is selected or not
		if (isSelected) {
			// Sets the background and foreground colours from the defaults
			if (background == null) {
				Color bg = defaultLabel.getBackground();
				background = new Color(bg.getRed(), bg.getGreen(), bg.getBlue());
			}
			if (foreground == null) {
				Color fg = defaultLabel.getForeground();
				foreground = new Color(fg.getRed(), fg.getGreen(), fg.getBlue());
			}

			// Uses the selection colours
			list.setSelectionBackground(background);
			list.setSelectionForeground(foreground);

			c1.setForeground(list.getSelectionForeground());
			c2.setForeground(list.getSelectionForeground());
			c3.setForeground(list.getSelectionForeground());

			setBackground(list.getSelectionBackground());
		} else {
			// Sets the selected background and foreground colours from the defaults
			if (selectedBackground == null) {
				Color bg = defaultLabel.getBackground();
				selectedBackground = new Color(bg.getRed(), bg.getGreen(), bg.getBlue());
			}
			if (selectedForeground == null) {
				Color fg = defaultLabel.getForeground();
				selectedForeground = new Color(fg.getRed(), fg.getGreen(), fg.getBlue());
			}

			// Uses the default colours
			list.setBackground(selectedBackground);
			list.setForeground(selectedForeground);

			c1.setForeground(list.getForeground());
			c2.setForeground(list.getForeground());
			c3.setForeground(list.getForeground());

			setBackground(list.getBackground());
		}

		// Sets other attributes
		setFont(list.getFont());
		setEnabled(list.isEnabled());
		if (isSelected && cellHasFocus)
			setBorder(defaultLabel.getBorder());
		else
			setBorder(null);

		return this;
	}
}
