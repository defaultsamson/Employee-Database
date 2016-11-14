import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import gui.IconButton;
import gui.IconTextField;
import gui.IconType;

public class Main {
	public static void main(String[] args) {
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

		JFrame frame = new JFrame("Hnng");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 400));
		frame.setLocationRelativeTo(null);

		Container con = new Container();
		con.setLayout(null);
		frame.setContentPane(con);

		IconButton but = new IconButton(IconType.ADD);
		but.setLocation(20, 20 + (61 * 0));
		con.add(but);

		IconButton but1 = new IconButton(IconType.REMOVE);
		but1.setLocation(20, 20 + (61 * 1));
		con.add(but1);

		IconButton but2 = new IconButton(IconType.EDIT);
		but2.setLocation(20, 20 + (61 * 2));
		con.add(but2);

		IconButton but3 = new IconButton(IconType.DONE);
		but3.setLocation(20, 20 + (61 * 3));
		con.add(but3);

		IconTextField field = new IconTextField("Search", IconType.SEARCH);
		field.setLocation(100, 50);
		field.setSize(200, 32);
		con.add(field);

		frame.setVisible(true);
	}
}
