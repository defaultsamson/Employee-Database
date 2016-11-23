package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JTextField;

public class HintTextField extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2985318102891981101L;

	private static final int TEXT_Y_OFFSET = 19;
        
        private static final String DEFAULT_HINT = "Insert hint here";

	private String hint;
        
        public HintTextField(){
            super();
            
            this.hint = DEFAULT_HINT;
        }

	public HintTextField(String hint) {
		super();

		this.hint = hint;
	}

	public String getHint() {
		return hint;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draws the input hint when no text is there
		if (getText().length() == 0) {
			Color oldColour = g.getColor();
			float grey = 0.7F;
			g.setColor(new Color(grey, grey, grey));
			g.drawString(hint, getInsets().left, TEXT_Y_OFFSET);
			g.setColor(oldColour);
		}
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}
