package io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import gui.IconButton;

public class FileUtil {
	private static final String ICON_DIR = "/";

	public static BufferedImage loadImage(String path) throws IOException {
		return ImageIO.read(IconButton.class.getResourceAsStream(ICON_DIR + path));
	}

	public static void writeLines(File file, List<String> lines) throws IOException {
		if (!file.exists())
			file.createNewFile();

		PrintWriter pw = new PrintWriter(file);

		for (String s : lines) {
			pw.println(s);
		}

		pw.close();
	}

	/**
	 * Takes all the lines from a file as Strings.
	 * 
	 * @param file
	 *            the file to read
	 * @return the lines read.
	 */
	public static List<String> readAllLines(File file) throws FileNotFoundException {
		List<String> toReturn = new ArrayList<String>();
		Scanner fileScanner = new Scanner(file);

		while (fileScanner.hasNextLine()) {
			toReturn.add(fileScanner.nextLine());
		}

		fileScanner.close();

		return toReturn;
	}
}
