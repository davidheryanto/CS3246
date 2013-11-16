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

void RunGenderDetection();

int main(int argc, const char** argv)
{
	RunGenderDetection();
	cout << "Gender detection finished" << endl;


	getchar();

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