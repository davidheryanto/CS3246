#pragma once

#include "opencv2\core\core.hpp"
#include "opencv2\contrib\contrib.hpp"
#include "opencv2\highgui\highgui.hpp"

#include <vector>

#define GENDER_MALE = 0
#define GENDER_FEMALE = 1


class GenderDetection
{
public:
	void train(std::string csvPath);

	// face is assumed to have size 150px X 150px
	// aligned properly.
	// Returns the gender of the face using FisherFaceRecognizer.
	int getGender(cv::Mat face);

private:
	std::vector<cv::Mat> images;

	std::vector<int> labels;

	cv::Ptr<cv::FaceRecognizer> model;

};
