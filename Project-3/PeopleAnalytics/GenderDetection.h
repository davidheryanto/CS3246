#pragma once

#include "opencv2\core\core.hpp"
#include "opencv2\contrib\contrib.hpp"
#include "opencv2\highgui\highgui.hpp"

#include <vector>
#include <iostream>
#include <fstream>

#define GENDER_MALE 0
#define GENDER_FEMALE 1
#define CSV_SEPARATOR ','
#define NORMALIZE_MIN 0
#define NORMALIZE_MAX 255

class GenderDetection
{
public:
	// face is assumed to have size 150px X 150px
	// aligned properly.
	// Returns the gender of the face using FisherFaceRecognizer.
	int GetGender(cv::Mat face);

	void Train(std::string csvPath);


private:
	std::vector<cv::Mat> images;
	std::vector<int> labels;
	cv::Ptr<cv::FaceRecognizer> model;

	void ReadCsv(std::string csvPath);
	cv::Mat Normalize(cv::InputArray src);

};
