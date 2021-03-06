#pragma once

#include "opencv2\core\core.hpp"
#include "opencv2\contrib\contrib.hpp"
#include "opencv2\highgui\highgui.hpp"

#include <iostream>
#include <fstream>

#define CSV_SEPARATOR ','
#define NORMALIZE_MIN 0
#define NORMALIZE_MAX 255
#define SMILE_FALSE 0
#define SMILE_TRUE 1

class SmileDetection
{
public:
	SmileDetection();

	~SmileDetection();

	// face is assumed to have size 150px X 150px
	// aligned properly.
	// Returns true if the face is smiling
	// by utilizing FisherFaceRecognizer method.
	bool isSmiling(cv::Mat face);

	void train(std::string csvPath);

private:
	std::vector<cv::Mat> images;
	std::vector<int> labels;
	cv::Ptr<cv::FaceRecognizer> model;

	void readCsv(std::string csvPath);
	cv::Mat normalize(cv::InputArray src);
};