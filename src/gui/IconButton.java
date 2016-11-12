package gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import io.FileUtil;

public class IconButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8818385378361528932L;

	private BufferedImage image;
	private IconType iconType;

	public IconButton(IconType iconType) {
		super();

		this.iconType = iconType;

		try {
			// Loads the image
			image = FileUtil.loadImage(iconType.getTextureDir());
			// Sets the iamge as the button's icon
			setIcon(new ImageIcon(image));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setSize(new Dimension(image.getWidth(), image.getHeight()));
	}

	public IconType getIconType() {
		return iconType;
	}

	public BufferedImage getImage() {
		return image;
	}
}
