#pragma once

#include "opencv2\core\core.hpp"

#define GENDER_MALE = 0
#define GENDER_FEMALE = 1

// face is assumed to have size 150px X 150px
// aligned properly.
// Returns the gender of the face using FisherFaceRecognizer.
int getGender(cv::Mat face);