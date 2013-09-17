package lucene;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;

import javax.swing.DefaultListModel;

import org.apache.lucene.util.Version;

public class Main implements AWTEventListener {
	public static final Version VERSION = Version.LUCENE_36;
	public static final String DIR_PATH_DATA = "./data";
	public static final String DIR_PATH_INDEX = "./index";

	public static DefaultListModel<String> model = new DefaultListModel<String>();
	
	public static Controller controller = new Controller();

	public static void main(String[] args) {
		Window window = new Window();
		window.initialize();
		window.setModel(model);
		
		
		
	}


	


	@Override
	public void eventDispatched(AWTEvent event) {
		// TODO Auto-generated method stub
		
		
		
		
		
	}
}
