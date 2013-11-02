Project 3 - Face profiling
----------------------------
Eclipse config:
http://docs.opencv.org/trunk/doc/tutorials/introduction/java_eclipse/java_eclipse.html

Read Mat by BufferedImage:
Mat image = Highgui.imread("/Users/Sumit/Desktop/image.jpg");

MatOfByte bytemat = new MatOfByte();

Highgui.imencode(".jpg", dst, bytemat);

byte[] bytes = bytemat.toArray();

InputStream in = new ByteArrayInputStream(bytes);

BufferedImage img = ImageIO.read(in);

