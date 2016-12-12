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
 *Creates a panel with an image as its background
 *
 */
class ImagePanel extends JPanel {

	//Serial ID for extending JPanel
	private static final long serialVersionUID = 3966638652448600131L;

	//The displayed image
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
	
	/**
	 * Change to a new image
	 * @param The image to be displayed
	 */
	public void setImage(IconType icon){
		try {
			// Loads the image
			image = FileUtil.loadImage(icon.getTextureDir());
			repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Override the method in parent to paint the image
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
