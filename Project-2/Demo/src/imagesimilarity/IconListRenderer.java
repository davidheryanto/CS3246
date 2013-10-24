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
		
		String title = getTitle(value);
		label.setText(title);
		return label;
	}

	private Icon getIcon(Object img) {
		if (img instanceof Image) {
			return new ImageIcon((Image) img);
		}
		
		return null;
	}
	
	private String getTitle(Object filePath) {
		String title = "";
		if (filePath instanceof String) {
			String path = (String) filePath;
			int index = path.lastIndexOf('\\');
			if (index > 0) {
				title = path.substring(index + 1);
			}
		}
		
		return title;
	}

}