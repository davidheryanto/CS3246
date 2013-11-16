#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include "GenderDetection.h"

#include <iostream>
#include <stdio.h>


#define GENDER_TRAINING_PATH "Data\\Gender-training.csv"

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

/** Function Headers */
void RunGenderDetection();
void detectAndDisplay( Mat frame );

/** Global variables */
String face_cascade_name = "data\\haarcascades\\haarcascade_frontalface_alt.xml";
CascadeClassifier face_cascade;
string window_name = "Capture - Face detection";

int main(int argc, const char** argv)
{
	CvCapture* capture;
	Mat frame;
 
	//-- 1. Load the cascades
	if( !face_cascade.load( face_cascade_name ) ){ printf("--(!)Error loading\n"); return -1; };

	//-- 2. Read the video stream
	capture = cvCaptureFromCAM( CV_CAP_ANY );
	if( capture )
	{
		frame = cvQueryFrame( capture );
		while( true )
		{
			frame = cvQueryFrame( capture );
			//imshow( window_name, frame );
			//-- 3. Apply the classifier to the frame
			if( !frame.empty() )
				detectAndDisplay( frame );
			else
			{
				printf(" --(!) No captured frame -- Break!");
				break;
			}
 
			int c = waitKey(10);
			if( (char)c == 'c' )
				break;
		}
	}

	/*RunGenderDetection();
	cout << "Gender detection finished" << endl;


	getchar();*/
	cvReleaseCapture(&capture); //Release capture.
    destroyWindow(window_name); //Destroy Window
	return 0;
}

void RunGenderDetection()
{
	GenderDetection genderDetection;
	genderDetection.Train(GENDER_TRAINING_PATH);

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
		int predicted_label = genderDetection.GetGender(test_image);

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

void detectAndDisplay( Mat frame )
{
	std::vector<Rect> faces;
	Mat frame_gray;
 
	cvtColor( frame, frame_gray, CV_BGR2GRAY );
	equalizeHist( frame_gray, frame_gray );
 
	//-- Detect faces
	face_cascade.detectMultiScale( frame_gray, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );
	for( int i = 0; i < faces.size(); i++ )
	{
		Point center( faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5 );
		//ellipse( frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );
		rectangle(frame, faces[i], Scalar( 255, 0, 255 ), 2, 8, 0);
	}
	//-- Show what you got
	imshow( window_name, frame );
}