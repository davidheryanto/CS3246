package faceprofile;

import java.util.logging.Level;
import java.util.logging.Logger;

import faceprofile.logic.FaceDetect;
import faceprofile.ui.Window;

public class Main {
	public static Logger LOGGER;
	public static FaceDetect logic;
	public static Window window;
	
	public static void main(String[] args) {
		initLogger(Level.INFO);  // set to Level.OFF to turn off logging
		initLogic();
		initUI();
	}
	
	private static void initLogic() {
		logic = new FaceDetect();
	}
	
	private static void initUI() {
		window = new Window();
		window.addObserver(logic);
	}
	
	private static void initLogger(Level level) {
		LOGGER = Logger.getLogger(Main.class.getName());
		LOGGER.setLevel(level);
	}
}
