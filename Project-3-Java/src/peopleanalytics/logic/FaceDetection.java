package peopleanalytics.logic;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.w3c.dom.css.Rect;

import peopleanalytics.Main;

import com.googlecode.javacv.cpp.opencv_objdetect.CascadeClassifier;



public class FaceDetection {
	public static String PATH_CASCADE_CLASSIFIER = "data/lbpcascade_frontalface.xml";
	
	public FaceDetection() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


	public BufferedImage run(File file) throws IOException {

		// Create a face detector from the cascade file in the resources
		// directory.
		CascadeClassifier faceDetector = new CascadeClassifier(new File(PATH_CASCADE_CLASSIFIER).getPath());
		Mat image = Highgui.imread(file.getPath());

		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		Main.LOGGER.info(String.format("Detected %s faces", faceDetections.toArray().length));

		// Draw a bounding box around each face.
		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}

		MatOfByte byteMat = new MatOfByte();
		Highgui.imencode(".jpg", image, byteMat);
		byte[] bytes = byteMat.toArray();
		
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));

		return img;
	}


}
