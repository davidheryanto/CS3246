package lucene;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class WindowTest {
	private static final int FRAME_DEFAULT_WIDTH = 800;
	private static final int FRAME_DEFAULT_HEIGHT= 500;

	public static void init() {
		JFrame frame = new JFrame();
		// set frame to close when exit icon clicked
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// set frame default size
		frame.setSize(FRAME_DEFAULT_WIDTH, FRAME_DEFAULT_HEIGHT);

		// set frame to center of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (dim.width / 2) - (frame.getWidth() / 2);
		int posY = (dim.height / 2) - (frame.getHeight() / 2);
		frame.setLocation(posX, posY);

		// add search bar
		JTextField textFieldQuery = new JTextField("type query");
		textFieldQuery.addFocusListener(new TextFieldFocusListener());



		// add list components

		// contentPane houses all the components in frame
		Container contentPane = frame.getContentPane();

		// TODO: configure contentPane: font etc

		contentPane.add(textFieldQuery);
		contentPane.add(new JTextField(), BorderLayout.SOUTH);












		frame.setVisible(true);
	}
}


class TextFieldFocusListener implements FocusListener {

	@Override
	public void focusGained(FocusEvent e) {
		Component source = e.getComponent();

		// clear search bar on focus
		if (source instanceof JTextField) {
			((JTextField) source).selectAll();
		}

	}

	@Override
	public void focusLost(FocusEvent e) {
		
	}

}
