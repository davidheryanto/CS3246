#pragma once

#include "opencv2\core\core.hpp"

// face is assumed to have size 150px X 150px
// aligned properly.
// Returns true if the face is smiling
// by utilizing FisherFaceRecognizer method.
bool isSmiling(cv::Mat face);