#include "SmileDetection.h"

using namespace std;
using namespace cv;

SmileDetection::SmileDetection()
{

}

SmileDetection::~SmileDetection()
{

}

bool isSmiling(Mat face)
{

	return false;
}

void SmileDetection::ReadCsv(string csvPath)
{
	string line, path, class_label;
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
		getline(liness, class_label);

		if (!path.empty() && !class_label.empty())
		{
			images.push_back(imread(path, 0));
			labels.push_back(atoi(class_label.c_str()));
		}
	}
}


Mat SmileDetection::Normalize(InputArray src)
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