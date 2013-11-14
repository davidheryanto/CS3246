package crowdanalytics;

import java.util.logging.Level;
import java.util.logging.Logger;

import crowdanalytics.ui.Window;

public class Main {
	public static Logger LOGGER;
	
	public static void main(String[] args) {
		initLogger(Level.INFO);  // set to Level.OFF to turn off logging
		initUI();
	}
	
	private static void initLogger(Level level) {
		LOGGER = Logger.getLogger(Main.class.getName());
		LOGGER.setLevel(level);
	}
	
	private static void initUI() {
		Window.initialize();
	}
}
