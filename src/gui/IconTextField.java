package gui;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import io.FileUtil;

public class IconTextField extends HintTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1171403969452664605L;

	private int rightIconDrawPadding;
	private BufferedImage image;
	private IconType iconType;

	// No-argument constructor for net beans implementation
	public IconTextField() {
		// Do not call this constructor
		super();

		image = null;
	}

	public IconTextField(String hint, IconType iconType) {
		super(hint);

		this.iconType = iconType;

		try {
			// Loads the image
			image = FileUtil.loadImage(iconType.getTextureDir());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Ensures that the text never goes over the icon
		rightIconDrawPadding = getInsets().right;
		int rightMargin = image.getWidth() + (getMargin().right * 2) + 2;
		setMargin(new Insets(getMargin().top, getMargin().left, getMargin().bottom, rightMargin));

	}

	public IconType getIconType() {
		return iconType;
	}

	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null) {
			// Gets the location to draw the icon at
			// (Can change dynamically, so must be calculated
			int x = getWidth() - image.getWidth() - rightIconDrawPadding;
			int y = (getHeight() / 2) - (image.getHeight() / 2);

			// Draws a
			// g.drawRect(x-1, y-1, image.getWidth(), image.getHeight());

			// Draws the icon
			g.drawImage(image, x, y, null);
		}
	}
}
