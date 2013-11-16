#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include "GenderDetection.h"

#include <iostream>
#include <stdio.h>
#include <vector>

#define DEVICE_ID -1 // -1 means default webcam in PC

#define IMG_WIDTH 150
#define IMG_HEIGHT 150

#define PATH_GENDER_TRAINING "Data\\Gender-training.csv"
#define PATH_HAAR_CASCADE_FRONT_FACE "Data\\haarcascades\\haarcascade_frontalface_default.xml"

#define KEY_DELAY 20
#define KEY_ESCAPE 27

using namespace std;
using namespace cv;


/*

@William, 
Can you write your faceCount function in the Main class instead?
I think it's more appropriate here... So after you run your faceDetection algo
you'll get the coordinates of each face, so you can call the helper function
to get Mat that represents each face. Then we can just pass each Mat (i.e. each face)
to GenderDetection and SmileDetection.

Main should open up webcam capture, then capture a frame every N miliseconds.
For each frame, it runs a face detection algorithm, getting all the faces in the frame
along with their coordinates. (Main will get the faceCount then)

Then Main calls helper function to align, rotate and resize each face to 150x150, before
passing each face to GenderDetection and SmileDetection to get the data for each face.

*/

GenderDetection gender_detection;
CascadeClassifier face_detection;

void testGenderDetection();

void initGenderDetection();
void initFaceDetection();
void initWebcam();

int main(int argc, const char** argv)
{
	initGenderDetection();
	initFaceDetection();

	initWebcam();

	return 0;
}

void initWebcam()
{
	Mat frame;
	VideoCapture videoCapture(DEVICE_ID);

	if (!videoCapture.isOpened())
	{
		cerr << "Default webcam cannot be opened. Try updating DEVICE_ID." << endl;
	}

	while (true)
	{
		videoCapture >> frame;
		Mat original = frame.clone();

		// Convert current frame to grayscale
		Mat gray;
		cvtColor(original, gray, CV_BGR2GRAY);

		vector<Rect_<int>> faces;
		face_detection.detectMultiScale(gray, faces);

		// We have positions of all faces at this point.
		for (size_t i = 0; i < faces.size(); i++)
		{
			Rect face_i = faces[i];
			Mat face = gray(face_i);
			Mat face_resized;

			resize(face, face_resized, Size(IMG_WIDTH, IMG_HEIGHT), 1.0, 1.0, INTER_CUBIC);
			int gender_prediction = gender_detection.getGender(face_resized);

			// And finally write all we've found out to the original image!
			// First of all draw a green rectangle around the detected face:
			rectangle(original, face_i, CV_RGB(0, 255, 0), 1);
			// Create the text we will annotate the box with:
			string box_text = format("Prediction = %d", gender_prediction);
			// Calculate the position for annotated text (make sure we don't
			// put illegal values in there):
			int pos_x = std::max(face_i.tl().x - 10, 0);
			int pos_y = std::max(face_i.tl().y - 10, 0);
			// And now put it into the image:
			putText(original, box_text, Point(pos_x, pos_y), CV_FONT_HERSHEY_PLAIN, 1.0, CV_RGB(0, 255, 0), 2);
		}

		imshow("People Analytics", original);
		char key = (char)waitKey(KEY_DELAY);
		if (key == (KEY_ESCAPE))
		{
			break;
		}
	}
}

void initFaceDetection()
{
	face_detection.load(PATH_HAAR_CASCADE_FRONT_FACE);
}

void initGenderDetection()
{
	gender_detection.train(PATH_GENDER_TRAINING);
}

void testGenderDetection()
{
	GenderDetection gender_detection;
	gender_detection.train(PATH_GENDER_TRAINING);

	String test_images[] =
	{
		"Data\\Test-images\\ayumi-hamasaki.jpg",
		"Data\\Test-images\\ayumi-hamasaki-2.jpg",
		"Data\\Test-images\\ben-afflect.jpg",
		"Data\\Test-images\\ben-stiller.jpg",
		"Data\\Test-images\\downey-jr.jpg",
		"Data\\Test-images\\jack-johnson.jpg",
		"Data\\Test-images\\mika-nakashima.jpg",
		"Data\\Test-images\\richard-gere.jpg",
		"Data\\Test-images\\taylor-swift.jpg",
		"Data\\Test-images\\utada-hikaru.jpg",
		"Data\\Test-images\\yozoh.jpg"
	};

	int len = sizeof(test_images) / sizeof(*test_images);
	for (int i = 0; i < len; i++)
	{
		int index_last_backslash = test_images[i].find_last_of('\\');
		string person_name = test_images[i].substr(index_last_backslash + 1);
		Mat test_image = imread(test_images[i], CV_LOAD_IMAGE_GRAYSCALE);
		int predicted_label = gender_detection.getGender(test_image);

		cout << "Predicted label for " << person_name << ": ";
		switch (predicted_label)
		{
		case GENDER_MALE:
			cout << "Male";
			break;
		case GENDER_FEMALE:
			cout << "Female";
			break;
		}
		cout << endl;
	}
}