package crowdanalytics.logic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import crowdanalytics.Main;



public class FaceDetection {

	public FaceDetection() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


	public BufferedImage detect(File file) throws IOException {

		// Create a face detector from the cascade file in the resources
		// directory.
		CascadeClassifier faceDetector = new CascadeClassifier(new File("data/lbpcascade_frontalface.xml").getPath());
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
