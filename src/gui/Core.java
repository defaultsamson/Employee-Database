package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import data.OpenHashTable;
import io.Database;

public class Core {

	// String constants in English
	private static final String WINDOW_TITLE = "Employee Database System";
	private static final String FILE_MENU_TITLE = "File";
	private static final String OPEN_MENU_ITEM = "Open File...";
	private static final String SAVE_MENU_ITEM = "Save";

	// html text for employee number with two lines
	private static final String TABLE_TITLE_NUMBER = "<html>Employee<br>&nbsp;Number</p></html>";
	private static final String TABLE_FIRST_NAME = "First Name ";
	private static final String TABLE_LAST_NAME = "Last Name ";

	// Dimension constants
	private static final Point WINDOW_OFFSET = new Point(100, 100);
	private static final Dimension WINDOW_SIZE = new Dimension(800, 600);
	private static final Dimension TABLE_WIDTH = new Dimension(250, 0);
	private static final Dimension TABLE_HEADING_SIZE = new Dimension(250, 100);

	// The main method runs to set up the core and displays the content
	public static void main(String[] args) {
		// Sets the look and feel of the program to that of the OS
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		int numberOfBuckets = (int) Math.round(Math.random() * 6 + 2);
		OpenHashTable table = new OpenHashTable(numberOfBuckets);

		new Core();
	}

	// Graphic interface
	private JFrame window;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JPanel content;

	private JPanel databaseVisual;
	private JPanel table;
	private JPanel headings;
	private JScrollPane tableScrollPane;

	private JTextField searchBar;

	private OpenHashTable hashTable;

	// So this is where you use a private constructor
	private Core() {
		window = new JFrame(WINDOW_TITLE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar = new JMenuBar();
		fileMenu = new JMenu(FILE_MENU_TITLE);

		openMenuItem = new JMenuItem(OPEN_MENU_ITEM);
		saveMenuItem = new JMenuItem(SAVE_MENU_ITEM);

		content = new JPanel();
		content.setLayout(new BorderLayout());

		table = new JPanel();
		table.setLayout(new GridLayout(0, 3));

		databaseVisual = new JPanel();
		databaseVisual.setLayout(new BoxLayout(databaseVisual, BoxLayout.Y_AXIS));

		searchBar = new IconTextField("Search", IconType.SEARCH);
		searchBar.setMaximumSize(TABLE_WIDTH);

		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setMinimumSize(TABLE_WIDTH);

		headings = new JPanel();
		headings.setLayout(new GridLayout(1, 3));
		headings.setMaximumSize(TABLE_HEADING_SIZE);

		// Initializes the graphical stuff
		window.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);

		JLabel empnumLabel = new JLabel(TABLE_TITLE_NUMBER);
		headings.add(empnumLabel);
		headings.add(new JLabel(TABLE_FIRST_NAME));
		headings.add(new JLabel(TABLE_LAST_NAME));

		// TODO: remove this after the grid is sorted out
		// Filler fluff
		for (int i = 0; i < 99; i++) {
			JLabel text = new JLabel("1000000");
			text.setHorizontalAlignment((int) Component.CENTER_ALIGNMENT);
			table.add(text);
		}

		databaseVisual.add(searchBar);
		databaseVisual.add(headings);
		databaseVisual.add(tableScrollPane);
		content.add(databaseVisual, BorderLayout.WEST);

		window.setContentPane(content);
		window.setSize(WINDOW_SIZE);
		window.setLocation(WINDOW_OFFSET);
		window.setVisible(true);
	}
}
