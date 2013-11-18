#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

#include "GenderDetection.h"
#include "SmileDetection.h"
#include "Timer.h"

#include <iostream>
#include <fstream>
#include <vector>
#include <math.h>
#include <time.h>

#define DEVICE_ID 0 // -1 means default webcam in PC

#define IMG_WIDTH 150
#define IMG_HEIGHT 150

#define PATH_GENDER_TRAINING "Data\\gender-training.csv"
#define PATH_SMILE_TRAINING "Data\\smile-training.csv"
#define PATH_HAAR_CASCADE_FRONT_FACE "Data\\haarcascades\\haarcascade_frontalface_alt.xml"
#define PATH_HAAR_CASCADE_SMILE "Data\\haarcascades\\haarcascade_smile.xml"
#define PATH_OUTPUT_FILE "Data\\output.csv"

#define KEY_DELAY 20
#define KEY_ESCAPE 27

#define THRESHOLD_UNIQUE_FACE 20 // Min % diff in size and position for new faces to be unique.

using namespace std;
using namespace cv;

GenderDetection gender_detection;
CascadeClassifier smile_detection;
CascadeClassifier face_detection;

Timer timer;

void testGenderDetection();

void initGenderDetection();
void initSmileDetection();
void initFaceDetection();
void startCapturing();

int getNewFacesCount(vector<Rect> current_faces, vector<Rect> prev_faces);
bool is_file_exists(string file_path);
void initOutputFile(ofstream& output_file);
const std::string getCurrentDateTime();


int main(int argc, const char** argv)
{
	initGenderDetection();
	initSmileDetection();
	initFaceDetection();
	startCapturing();

	return 0;
}


void startCapturing()
{
	ofstream output_file;
	Mat frame;
	vector<Rect> faces;
	vector<Rect> prev_faces;
	VideoCapture video_capture(DEVICE_ID);

	initOutputFile(output_file);
	if (!video_capture.isOpened())
	{
		cerr << "Default webcam cannot be opened. Try updating DEVICE_ID." << endl;
		exit(1);
	}
	video_capture.set(CV_CAP_PROP_FRAME_HEIGHT, 350);
	video_capture.set(CV_CAP_PROP_FRAME_WIDTH, 350);

	while (true)
	{
		timer.start();

		int faces_count = 0;
		int new_faces_count = 0;
		int male_count = 0;
		int female_count = 0;
		double smile_intensity = 0;
		double duration = 0.0;
		Mat original;
		Mat gray;

		video_capture >> frame;
		original = frame.clone();
		flip(original, original, 1); // Horizontal flip
		// Convert current frame to grayscale
		cvtColor(original, gray, CV_BGR2GRAY);

		prev_faces = faces; // Keep the old faces
		face_detection.detectMultiScale(
			gray,
			faces,
			1.1, // scale factor	
			3, // min_neighbours	 
			0 | CASCADE_SCALE_IMAGE,
			Size(60, 60),
			Size(160, 160)); // min_size
		faces_count = (int)faces.size();

		// Check how many of these new faces are unique
		new_faces_count = getNewFacesCount(faces, prev_faces);
		// cout << new_faces_count;

		cout << "----------------------" << endl;
		// We have positions of all faces at this point.
		for (size_t i = 0; i < faces.size(); i++)
		{
			Mat face = gray(faces[i]);
			Mat face_resized;
			Mat mouth_area;

			string gender;
			string smile;

			vector<Rect> smile_objects;
			Rect rect_mouth;

			int half_height;


			// Adjust the square for the face detect pos
			//Rect rect_face_crop;
			//Mat face_crop;
			//rect_face_crop = faces[i];
			//rect_face_crop.y = rect_face_crop.y + 10;
			//// Create region of interest (mouth)
			//face_crop = gray(rect_face_crop);


			// For the new faces find the gender
			// Resize image for detection using Fisherface method
			resize(face, face_resized, Size(IMG_WIDTH, IMG_HEIGHT), 1.0, 1.0, INTER_CUBIC);
			switch (gender_detection.getGender(face_resized))
			{
			case GENDER_MALE:
				gender = "Male";
				male_count += 1;
				break;
			case GENDER_FEMALE:
				gender = "Female";
				female_count += 1;
				break;
			default:
				gender = "";
			}

			// Finish processing gender.

			half_height = cvRound((float)faces[i].height / 2);
			rect_mouth = faces[i];
			rect_mouth.y = rect_mouth.y + half_height;
			rect_mouth.height = half_height;
			// Create region of interest (mouth)
			mouth_area = gray(rect_mouth);
			// Find the smile intensity of each face
			// NOTE : Intensity only valid after first smile is detected
			smile_detection.detectMultiScale(
				mouth_area,
				smile_objects,
				1.1,
				0,
				0 | CASCADE_SCALE_IMAGE,
				Size(20, 20));

			// The number of detected neighbors depends on image size (and also illumination, etc.). The
			// following steps use a floating minimum and maximum of neighbors. Intensity thus estimated will be
			//accurate only after a first smile has been displayed by the user.
			// const int smile_neighbors = (int)smile_objects.size();


			/*static int max_neighbors = -1;
			static int min_neighbors = -1;
			if (min_neighbors == -1) min_neighbors = smile_neighbors;
			max_neighbors = max(max_neighbors, smile_neighbors);
			float intensity_zero_one = ((float)smile_neighbors - min_neighbors) / (max_neighbors - min_neighbors + 1);*/

			int max_neighbors = 130;
			int smile_neighbors = (int)smile_objects.size();
			float intensity = (float)smile_neighbors / max_neighbors * 10;
			int intensity_normalized = intensity >= 10.0 ? 9 : (int)intensity;

			smile_intensity += intensity_normalized;


			cout << smile_neighbors << "\tintensity_sum_per_frame: " << smile_intensity << endl;



			// And finally write all we've found out to the original image!
			// First of all draw a green rectangle around the detected face:
			rectangle(original, faces[i], CV_RGB(0, 255, 0), 1);
			// Create the text we will annotate the box with:
			string box_text = format("%s %s: %d", gender.c_str(), smile.c_str(), intensity_normalized);
			// Calculate the position for annotated text 
			// (make sure we don'tput illegal values in there):
			int pos_x = max(faces[i].tl().x - 10, 0);
			int pos_y = max(faces[i].tl().y - 10, 0);

			// And now put it into the image:
			putText(original, box_text, Point(pos_x, pos_y), CV_FONT_HERSHEY_PLAIN, 1.0, CV_RGB(0, 255, 0), 2);
		}
		cout << "----------------------" << endl;

		// Finish processing faces.

		int interest = (int) (smile_intensity / faces_count) * 10; // Normalize the smile_intensity
		if (faces_count > 0 && interest >= 0)
		{
			// Only write to output if there is at least one face
			output_file << getCurrentDateTime()
				<< "," << (int) timer.getElapsedTimeInMilliSec()
				<< "," << faces_count
				<< "," << new_faces_count
				<< "," << male_count
				<< "," << female_count
				<< "," << interest
				<< endl;
		}

		imshow("People Analytics", original);
		char key = (char)waitKey(KEY_DELAY);
		if (key == (KEY_ESCAPE))
		{
			break;
		}

		timer.stop();
	}

	output_file.close();
}

// Get current date/time, format is YYYY-MM-DD.HH:mm:ss
const std::string getCurrentDateTime() {
	time_t     now = time(0);
	struct tm  tstruct;
	char       buf[80];
	tstruct = *localtime(&now);
	strftime(buf, sizeof(buf), "%Y-%m-%d.%X", &tstruct);

	return buf;
}

void initFaceDetection()
{
	if (!face_detection.load(PATH_HAAR_CASCADE_FRONT_FACE))
	{
		cerr << "ERROR: Could not load face cascade. Check cascade file path." << endl;
		exit(1);
	}
}

void initSmileDetection()
{
	if (!smile_detection.load(PATH_HAAR_CASCADE_SMILE))
	{
		cerr << "ERROR: Could not load smile cascade. Check cascade file path." << endl;
		exit(1);
	}
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
		"Data\\test-images\\ayumi-hamasaki.jpg",
		"Data\\test-images\\ayumi-hamasaki-2.jpg",
		"Data\\test-images\\ben-afflect.jpg",
		"Data\\test-images\\ben-stiller.jpg",
		"Data\\test-images\\downey-jr.jpg",
		"Data\\test-images\\jack-johnson.jpg",
		"Data\\test-images\\mika-nakashima.jpg",
		"Data\\test-images\\richard-gere.jpg",
		"Data\\test-images\\taylor-swift.jpg",
		"Data\\test-images\\utada-hikaru.jpg",
		"Data\\test-images\\yozoh.jpg"
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

int getNewFacesCount(vector<Rect> current_faces, vector<Rect> prev_faces)
{
	int current_faces_count = (int)current_faces.size();

	if (prev_faces.size() <= 0)
	{
		// In previous frame no faces detected
		// So we assume all the faces in current frame are new.
		return current_faces_count;
	}

	// If a current_face are similar to any of the prev_faces in terms of size and position
	// Then that current_face is not unique.
	for (vector<Rect>::size_type i = 0; i < current_faces.size(); i++)
	{
		double threshold_y = THRESHOLD_UNIQUE_FACE / 100.0 * current_faces[i].height;
		double threshold_x = THRESHOLD_UNIQUE_FACE / 100.0 * current_faces[i].width;

		for (vector<Rect>::size_type j = 0; j < prev_faces.size(); j++)
		{
			if (abs(current_faces[i].height - prev_faces[j].height) < threshold_y &&
				abs(current_faces[i].y - prev_faces[j].y) < threshold_y &&
				abs(current_faces[i].width - prev_faces[j].width) < threshold_x &&
				abs(current_faces[i].x - prev_faces[j].x) < threshold_x)
			{
				// current_face is the same as prev_face
				current_faces_count -= 1;
				break;
			}
		}
	}

	return current_faces_count;
}

bool is_file_exists(string file_path)
{
	std::ifstream infile(file_path);
	return infile.good();
}

void initOutputFile(ofstream& output_file)
{
	string filePath = (string)PATH_OUTPUT_FILE;
	if (is_file_exists(PATH_OUTPUT_FILE))
	{
		output_file.open(PATH_OUTPUT_FILE, ios::app); // Append mode.
	}
	else
	{
		output_file.open(PATH_OUTPUT_FILE);
		// Write the column headings
		output_file << "current_time"
			<< "," << "view_duration (ms)"
			<< "," << "faces_count"
			<< "," << "new_faces_count"
			<< "," << "male_count"
			<< "," << "female_count"
			<< "," << "interest (max 1.0)"
			<< endl;
	}
}
