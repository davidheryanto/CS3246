#pragma once

#include "opencv2\core\core.hpp"
#include "opencv2\contrib\contrib.hpp"
#include "opencv2\highgui\highgui.hpp"

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

private:
	std::vector<cv::Mat> images;
	std::vector<int> labels;
	cv::Ptr<cv::FaceRecognizer> model;

	void ReadCsv(std::string csvPath);
	cv::Mat Normalize(cv::InputArray src);
};