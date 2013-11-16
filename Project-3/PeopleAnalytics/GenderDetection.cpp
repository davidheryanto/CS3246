#include "GenderDetection.h"

using namespace std;
using namespace cv;


int GenderDetection::GetGender(Mat face)
{
	int predictedLabel = model->predict(face);
	return predictedLabel;
}

void GenderDetection::Train(string csvPath)
{
	try
	{
		ReadCsv(csvPath);
	}
	catch (Exception& e)
	{
		cerr << "Error opening file \"" << csvPath << "\". Reason: " << e.msg << endl;
		exit(1);
	}

	if (images.size() <= 1)
	{
		string error_message = "Needs at least 2 images to work. Please add more images to the data set.";
		CV_Error(CV_StsError, error_message);
	}

	// Get the height from the first image. We'll need this
	// later in code to reshape the images to their original
	// size:
	int height = images[0].rows;
	
	// Create a Fisherface model and train it with the data set.
	model = createFisherFaceRecognizer();
	model->train(images, labels);
}


void GenderDetection::ReadCsv(string csvPath)
{
	string line, path, classlabel;
	ifstream file(csvPath.c_str(), ifstream::in);
	if (!file)
	{
		string error_message = "Valid path for csv file";
		CV_Error(CV_StsBadArg, error_message);
	}

	while (getline(file, line))
	{
		stringstream liness(line);
		getline(liness, path, CSV_SEPARATOR);
		getline(liness, classlabel);

		if (!path.empty() && !classlabel.empty())
		{
			images.push_back(imread(path, 0));
			labels.push_back(atoi(classlabel.c_str()));
		}
	}
}

Mat GenderDetection::Normalize(InputArray src)
{
	Mat _src = src.getMat();
	// Create and return normalized image:
	Mat dst;
	switch (_src.channels()) {
	case 1:
		cv::normalize(_src, dst, NORMALIZE_MIN, NORMALIZE_MAX, NORM_MINMAX, CV_8UC1);
		break;
	case 3:
		cv::normalize(_src, dst, NORMALIZE_MIN, NORMALIZE_MAX, NORM_MINMAX, CV_8UC3);
		break;
	default:
		_src.copyTo(dst);
		break;
	}
	return dst;
}