import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.EmployeeList;
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

		// DefaultListModel<EmployeeInfo> listModel = new DefaultListModel<EmployeeInfo>();
		// listModel.addElement(new PartTimeEmployee(64, "Samasioduyajsdghakjsdghaskjdgoajshd", "Close", Gender.MALE, Location.MISSISSAUGA, 0.2, 12, 2, 2));
		// listModel.addElement(new FullTimeEmployee(64, "Mike", "Oxlittle", Gender.FEMALE, Location.CHICAGO, 0.13, 12000));

		EmployeeList list = new EmployeeList();
		list.setSize(new Dimension(400, 150));
		list.setLocation(300, 150);
		con.add(list);

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {

				System.out.println(arg0.getSource());
			}
		});

		list.getSelectedValue();

		try {
			Image im = new Image(ImageIO.read(Main.class.getResource("ic_search_black_18dp_1x.png")));
			con.add(im);
			im.setSize(200, 200);
			im.setLocation(10, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Image im = new Image(ImageIO.read(Main.class.getResource("ic_search_black_18dp_1x.png")));
			con.add(im);
			im.setSize(200, 200);
			im.setLocation(10, 10);
		} catch (IOException e) {
			e.printStackTrace();
		}

		frame.setVisible(true);
	}
}

class Image extends JPanel {

	private BufferedImage image;

	public Image(BufferedImage image) {
		super();

		this.image = image;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
