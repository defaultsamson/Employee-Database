/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import io.FileUtil;

/**
 *
 *
 */
class ImagePanel extends JPanel {

	private BufferedImage image;

	public ImagePanel(IconType icon) {
		super();

		try {
			// Loads the image
			image = FileUtil.loadImage(icon.getTextureDir());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
