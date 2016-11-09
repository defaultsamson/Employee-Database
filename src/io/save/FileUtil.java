package io.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileUtil {
	public static void writeLines(File file, List<String> lines) {
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		PrintWriter pw;

		try {
			pw = new PrintWriter(file);

			for (String s : lines) {
				pw.println(s);
			}

			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
