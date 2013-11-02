package faceprofile.logic;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import faceprofile.Main;

public class FaceDetect implements Observer {

	@Override
	public void update(Observable observed, Object imgFile) {
		// TODO Auto-generated method stub
		if (imgFile instanceof File) {
			Main.LOGGER.info(((File) imgFile).getPath());
		}
	}

}
