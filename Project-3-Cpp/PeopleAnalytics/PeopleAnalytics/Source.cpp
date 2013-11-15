#include "highgui.h"


int main(int argc, char** argv) {
	IplImage* img = cvLoadImage("../data/belle.jpg");
	cvNamedWindow("Example1", CV_WINDOW_AUTOSIZE);
	cvShowImage("Example1", img);
	cvWaitKey(0);
	cvReleaseImage(&img);
	cvDestroyWindow("Example1");
}