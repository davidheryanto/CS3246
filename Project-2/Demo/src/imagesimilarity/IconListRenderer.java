package imagesimilarity;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class IconListRenderer extends DefaultListCellRenderer {
	public Component getListCellRendererComponent(
			JList<?> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(
				list, value, index, isSelected, cellHasFocus);
		Icon icon = getIcon(value);
		label.setIcon(icon);
		return label;
	}

	private Icon getIcon(Object filePath) {
		Image img = null;
		try {
			img = ImageIO.read(new File((String) filePath));
			img = img.getScaledInstance(-1, 100, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ImageIcon(img);
	}

}