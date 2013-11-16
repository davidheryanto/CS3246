#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include "GenderDetection.h"

#include <iostream>
#include <stdio.h>

#define GENDER_TRAINING_PATH "C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Gender-training.csv"

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

void runGenderDetection();

int main(int argc, const char** argv)
{
	runGenderDetection();
	cout << "Gender detection finished" << endl;
	getchar();

	return 0;
}

void runGenderDetection()
{
	GenderDetection genderDetection;
	genderDetection.Train(GENDER_TRAINING_PATH);

	String testImages[] =
	{
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\ayumi-hamasaki.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\ayumi-hamasaki-2.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\ben-afflect.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\ben-stiller.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\downey-jr.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\jack-johnson.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\mika-nakashima.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\richard-gere.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\taylor-swift.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\utada-hikaru.jpg",
		"C:\\Users\\David\\Git\\School\\CS3246\\Project-3\\PeopleAnalytics\\Data\\Test-images\\yozoh.jpg"
	};

	int len = sizeof(testImages) / sizeof(*testImages);
	for (int i = 0; i < len; i++)
	{
		int index_last_backslash = testImages[i].find_last_of('\\');
		string person_name = testImages[i].substr(index_last_backslash + 1);
		Mat test_image = imread(testImages[i], 0);
		int predictedLabel = genderDetection.GetGender(test_image);

		cout << "Predicted label for " << person_name << ": ";
		if (predictedLabel > 0)
		{
			cout << "female";
		}
		else
		{
			cout << "male";
		}
		cout << endl;
	}
}