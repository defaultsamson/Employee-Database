package gui;
import hashTable.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.PrintWriter;
import javax.swing.*;

import javax.swing.JFrame;

public class Core {
	
	//Graphic interface
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
	private MyHashTable database;
	
	//String constants in English
	private static final String WINDOW_TITLE = "Employee Database System";
	private static final String FILE_MENU_TITLE = "File";
	private static final String OPEN_MENU_ITEM = "Open File...";
	private static final String SAVE_MENU_ITEM = "Save";
	//html text for employee number with two lines
	private static final String TABLE_TITLE_NUMBER = "<html>Employee<br>&nbsp;Number</p></html>";
	private static final String TABLE_FIRST_NAME = "First Name ";
	private static final String TABLE_LAST_NAME = "Last Name ";
	
	//Dimension constants
	private static final Point WINDOWOFFSET = new Point(100,100);
	private static final Dimension WINDOWSIZE = new Dimension(800,600); 
	private static final Dimension TABLEWIDTH = new Dimension(250,0); 
	private static final Dimension TABLEHEADINGSIZE = new Dimension(250,100); 
	
	//The main method runs to set up the core and displays the content
	public static void main (String[] args){
		Core core = new Core();
		int numberOfBuckets = (int) Math.round(Math.random()*6 + 2);
		core.database = new MyHashTable(numberOfBuckets);
		core.initialiseUserInterface();
	}
	
	//So this is where you use a private constructor
	private Core(){
		window = new JFrame(WINDOW_TITLE);
		menuBar = new JMenuBar();
		fileMenu = new JMenu(FILE_MENU_TITLE);
		openMenuItem = new JMenuItem(OPEN_MENU_ITEM);
		saveMenuItem = new JMenuItem(SAVE_MENU_ITEM);
		content = new JPanel();
		content.setLayout(new BorderLayout());
		table = new JPanel();
		table.setLayout(new GridLayout(0,3));
		databaseVisual= new JPanel();
		databaseVisual.setLayout(new BoxLayout(databaseVisual,BoxLayout.Y_AXIS));
		searchBar = new JTextField();
		searchBar.setMaximumSize(TABLEWIDTH);
		tableScrollPane = new JScrollPane(table);
		tableScrollPane.setMinimumSize(TABLEWIDTH);
		headings = new JPanel();
		headings.setLayout(new GridLayout(1,3));
		headings.setMaximumSize(TABLEHEADINGSIZE);
	}
	
	//Initialise the graphic stuff
	private void initialiseUserInterface(){
		window.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		JLabel empnumLabel = new JLabel(TABLE_TITLE_NUMBER);
		headings.add(empnumLabel);
		headings.add(new JLabel(TABLE_FIRST_NAME));
		headings.add(new JLabel(TABLE_LAST_NAME));
		for (int i = 0; i < 99; i++)
		{
		JLabel text = new JLabel("100000000000");
		text.setHorizontalAlignment((int) Component.CENTER_ALIGNMENT);
		table.add(text);
		}
		databaseVisual.add(searchBar);
		databaseVisual.add(headings);
		databaseVisual.add(tableScrollPane);
		content.add(databaseVisual, BorderLayout.WEST);
		window.setContentPane(content);
		window.setSize(WINDOWSIZE);
		window.setLocation(WINDOWOFFSET);
		window.setVisible(true);
	}
	
	//Saving the table
	private void save(MyHashTable table){
		PrintWriter writer;
	}
}
