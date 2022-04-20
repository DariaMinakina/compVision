package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import ru.sfedu.api.interfaces.LabsInterface;

import javax.swing.*;
import java.awt.*;
import org.opencv.core.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;
import java.util.List;

import static org.opencv.core.CvType.CV_8UC3;
import static ru.sfedu.Constants.*;

public class LabsImpl implements LabsInterface {

    private static Logger log = LogManager.getLogger(ImageImpl.class);

    @Override
    public void task2(int numberOfChannel, String pathName, String imageName) {
        showImageByPath(pathName + imageName);
        Mat mat = imgToMatByPath(numberOfChannel, pathName, imageName);
        showImageByBufferedImage(matToBufferedImage(mat));
        saveMatToFile(imageName, mat);
    }

    @Override
    public void task4(String path, int dx, int dy, String srcFileName) {
        Size size = new Size(dx, dy);
        Mat image = Imgcodecs.imread(path + srcFileName);
        Mat newImage = new Mat();

        Mat mat = baseBlur(image, image, size);
        showImageByBufferedImage(matToBufferedImage(mat));
        saveMatToFile(srcFileName + BLUR, mat);

        Mat matGaussian = gaussianBlur(mat, mat, size, 90, 90, 2);
        showImageByBufferedImage(matToBufferedImage(matGaussian));
        saveMatToFile(srcFileName + GAUSSIAN, matGaussian);

        Mat median = medianBlur(matGaussian, matGaussian, dx);
        showImageByBufferedImage(matToBufferedImage(median));
        saveMatToFile(srcFileName + MEDIAN, median);

        Mat bilateral = bilateralFilter(median, newImage, 15, 80, 80, Core.BORDER_DEFAULT);
        showImageByBufferedImage(matToBufferedImage(bilateral));
        saveMatToFile(srcFileName + BILATERAL, bilateral);
    }

    public Mat imgToMat(int numberOfChannel, Mat image) {
        int totalBytes = (int) (image.total() * image.elemSize());
        byte[] buffer = new byte[totalBytes];
        image.get(0, 0, buffer);
        for (int i = 0; i < totalBytes; i++) {
            if (i % numberOfChannel == 0) {
                buffer[i] = 0;
            }
        }
        image.put(0, 0, buffer);
        return image;
    }

    public Mat imgToMatByPath(int numberOfChannel, String pathName, String imageName) {
        Mat image = Imgcodecs.imread(pathName + imageName);
        return imgToMat(numberOfChannel, image);
    }

    public void showImageByPath(String path) {
        ImageIcon icon = new ImageIcon(path);
        frame(icon);
    }

    public void showImageByBufferedImage(BufferedImage bufferedImage) {
        ImageIcon icon = new ImageIcon(bufferedImage);
        frame(icon);
    }

    private void frame(ImageIcon icon) {
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(frame.getToolkit().getScreenSize().width - 100, frame.getToolkit().getScreenSize().height - 150);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public BufferedImage matToBufferedImage(Mat matImage) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matImage.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matImage.channels() * matImage.cols() * matImage.rows();
        byte[] b = new byte[bufferSize];
        matImage.get(0, 0, b);
        BufferedImage bufferedImage = new BufferedImage(matImage.cols(), matImage.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return bufferedImage;
    }

    public void saveMatToFile(String imageName, Mat img) {
        try {
            final String modFilePath = buildImageName(IMAGE_PATH, imageName);
            Imgcodecs.imwrite(modFilePath, img);
        } catch (Exception e) {
            log.info(SUPPORTS_ONLY_JPG_AND_PNG);
            log.error(e);
        }
    }

    private String buildImageName(String base, String name) {
        return String.format(IMAGE_FORMAT, base, name);
    }

    public Mat convertSobel(String dirPath, String srcFileName, int dx, int dy) {
        Mat image = Imgcodecs.imread(dirPath + srcFileName, Imgcodecs.IMREAD_COLOR);
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat dstSobel = new Mat();
        Mat dstSobelX = new Mat();
        Imgproc.Sobel(grayImage, dstSobelX, CvType.CV_32F, dx, dy);
        saveMatToFile(srcFileName + SOBELY, dstSobelX);
        return dstSobel;
    }

    public Mat convertLaplace(String dirPath, String srcFileName) {
        Mat image = Imgcodecs.imread(dirPath + srcFileName, Imgcodecs.IMREAD_COLOR);
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat dstLaplace = new Mat();
        Imgproc.Laplacian(image, dstLaplace, CvType.CV_32F);
        Mat absLaplaceImg = new Mat();
        Core.convertScaleAbs(dstLaplace, absLaplaceImg);
        saveMatToFile(srcFileName + LAPLACE, absLaplaceImg);
        return absLaplaceImg;
    }

    public Mat mirror(Mat image, int flipCode, String srcFileName) {
        Mat dstV = new Mat();
        Core.flip(image, dstV, flipCode);
        saveMatToFile(srcFileName + MIRROR, dstV);
        return dstV;
    }

    public Mat repeatImage(Mat image, int nx, int ny, String srcFileName) {
        Mat dst = new Mat();
        Core.repeat(image, ny, nx, dst);
        saveMatToFile(srcFileName + REPEAT, dst);
        return dst;
    }

    public Mat unityImage(Mat imageOne, Mat imageTwo, String srcFileName, int axis) {
        Mat dst = new Mat();
        List<Mat> listOfImages = Arrays.asList(imageOne, imageTwo);
        switch (axis) {
            case 0:
                Core.vconcat(listOfImages, dst);
                break;
            case 1:
                Core.hconcat(listOfImages, dst);
                break;
        }

        saveMatToFile(srcFileName + UNITY, dst);
        return dst;
    }

    public Mat changeImageSize(Mat image, int dx, int dy, String srcFileName) {
        Mat dst = new Mat();
        Imgproc.resize(image, dst, new Size(dx, dy));
        saveMatToFile(srcFileName + SIZE, dst);
        return dst;
    }

    public Mat rotationChanges(Mat srcImage, Mat dst, int angle, boolean isCutOff, String srcFileName) {
        Point center = new Point(srcImage.width() / 2, srcImage.height() / 2);
        int bordMode = Core.BORDER_CONSTANT;
        if (isCutOff) {
            bordMode = Core.BORDER_REFLECT;
        }
        Mat rotationMat = Imgproc.getRotationMatrix2D(center, angle, 1);
        Imgproc.warpAffine(srcImage, dst, rotationMat, new Size(srcImage.width(), srcImage.height()),
                Imgproc.INTER_LINEAR, bordMode, new Scalar(0, 0, 0, 255));
        saveMatToFile(srcFileName + ROTATION, dst);
        return dst;
    }

    public Mat warpImage(Mat image, int dx, int dy, String srcFileName) {
        MatOfPoint2f src = new MatOfPoint2f(
                new Point(0, 0),
                new Point(image.cols(), 0),
                new Point(0, image.rows()),
                new Point(image.cols(), image.rows())
        );
        MatOfPoint2f target = new MatOfPoint2f(
                new Point(dx, dy),
                new Point(image.cols() - dx, 0),
                new Point(0, image.rows() - dy),
                new Point(image.cols() - dx, image.rows() - dy)
        );
        Mat matWarp = Imgproc.getPerspectiveTransform(src, target);
        Mat dst = new Mat();
        Imgproc.warpPerspective(image, dst, matWarp, image.size());
        saveMatToFile(srcFileName + WARP, dst);
        return dst;
    }

    public Mat baseBlur(Mat src, Mat dst, Size ksize) {
        Imgproc.blur(src, dst, ksize, new Point(-1, -1));
        return src;
    }

    public Mat gaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX, double sigmaY, int borderType) {
        Imgproc.GaussianBlur(src, dst, ksize, sigmaX, sigmaY, borderType);
        return src;
    }

    public Mat medianBlur(Mat src, Mat dst, int ksize) {
        Imgproc.medianBlur(src, dst, ksize);
        return src;
    }

    public Mat bilateralFilter(Mat src, Mat dst, int d, double sigmaColor, double sigmaSpace, int borderType) {
        Imgproc.bilateralFilter(src, dst, d, sigmaColor, sigmaSpace, borderType);
        return dst;
    }

    public void morphingRect(Mat image, String srcFileName) {
        double[] sizes = {3, 5, 7, 9, 13, 15};
        for (double size : sizes) {
            Mat morphRect = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size, size));

            Mat dst1_0 = image.clone();
            Imgproc.erode(image, dst1_0, morphRect);
            saveMatToFile(srcFileName + MRF_ERODE_RECT + size, dst1_0);

            Mat dst1 = image.clone();
            Imgproc.dilate(image, dst1, morphRect);
            saveMatToFile(srcFileName + MRF_DILATE_RECT + size, dst1);

            Mat dst1_1 = image.clone();
            Imgproc.morphologyEx(image, dst1_1, Imgproc.MORPH_GRADIENT, morphRect);
            saveMatToFile(srcFileName + MRF_GRADIENT_RECT + size, dst1_1);

            Mat dst1_2 = image.clone();
            Imgproc.morphologyEx(image, dst1_2, Imgproc.MORPH_BLACKHAT, morphRect);
            saveMatToFile(srcFileName + MRF_BLACKHAT_RECT + size, dst1_2);
        }
    }

    public void morphingEllipse(Mat image, String srcFileName) {
        double[] sizes = {3, 5, 7, 9, 13, 15};
        for (double size : sizes) {
            Mat morphEllipse = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(size, size));

            Mat dst1_0 = image.clone();
            Imgproc.erode(image, dst1_0, morphEllipse);
            saveMatToFile(srcFileName + MRF_ERODE_EL + size, dst1_0);

            Mat dst1 = image.clone();
            Imgproc.dilate(image, dst1, morphEllipse);
            saveMatToFile(srcFileName + MRF_DILATE_EL + size, dst1);

            Mat dst1_1 = image.clone();
            Imgproc.morphologyEx(image, dst1_1, Imgproc.MORPH_GRADIENT, morphEllipse);
            saveMatToFile(srcFileName + MRF_GRADIENT_EL + size, dst1_1);

            Mat dst1_2 = image.clone();
            Imgproc.morphologyEx(image, dst1_2, Imgproc.MORPH_BLACKHAT, morphEllipse);
            saveMatToFile(srcFileName + MRF_BLACKHAT_EL + size, dst1_2);
        }
    }

    public void testFillFlood(int initVal, Mat image, String srcFileName) {
        Point seedPoint = new Point(0,0);
        Scalar newVal = new Scalar(0,255,0);
        Scalar loDiff = new Scalar(initVal,initVal,initVal);
        Scalar upDiff = new Scalar(initVal,initVal,initVal);
        Mat mask = new Mat();
        Imgproc.floodFill(image, mask, seedPoint, newVal, new Rect(), loDiff, upDiff,
                Imgproc.FLOODFILL_FIXED_RANGE);
        saveMatToFile(srcFileName + FILL+initVal, image);
    }

    public void pyramid(int width, int height, String srcFileName) {
        Mat mask = new Mat();
        Mat noiseMat = new Mat(new Size(width, height), CV_8UC3, new Scalar(0, 0, 0));
        Core.randn(noiseMat, 20, 50);
        Core.add(noiseMat, noiseMat, noiseMat);
        Imgproc.pyrDown(noiseMat, mask);
        saveMatToFile(srcFileName + PYRDOWN_NATIVE, noiseMat);

        Imgproc.pyrUp(mask, mask);
        saveMatToFile(srcFileName + PYRUP, noiseMat);

        Core.subtract(noiseMat, mask, mask);
        saveMatToFile(srcFileName + CORE, noiseMat);
    }

    public void rectangle(Mat image){
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        saveMatToFile(GRAYIMAGE, grayImage);

        Mat denoisingImage = new Mat();
        Photo.fastNlMeansDenoising(grayImage, denoisingImage);
        saveMatToFile(NO_NOISE, denoisingImage);

        Mat histogramEqualizationImage = new Mat();
        Imgproc.equalizeHist(denoisingImage, histogramEqualizationImage);
        saveMatToFile(HISTOGRAM, histogramEqualizationImage);


        Mat morphologicalOpeningImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(histogramEqualizationImage, morphologicalOpeningImage,
                Imgproc.MORPH_RECT, kernel);
        saveMatToFile(MORPHOLOGICAL_OPENING, morphologicalOpeningImage);

        Mat subtractImage = new Mat();
        Core.subtract(histogramEqualizationImage, morphologicalOpeningImage, subtractImage);
        saveMatToFile(SUBTRACT, subtractImage);

        Mat thresholdImage = new Mat();
        double threshold = Imgproc.threshold(subtractImage, thresholdImage, 50, 255,
                Imgproc.THRESH_OTSU);
        saveMatToFile(THRESHOLD, thresholdImage);
        thresholdImage.convertTo(thresholdImage, CvType.CV_16SC1);

        Mat edgeImage = new Mat();
        thresholdImage.convertTo(thresholdImage, CvType.CV_8U);
        saveMatToFile(TR2, thresholdImage);

        Imgproc.Canny(thresholdImage, edgeImage, threshold, threshold * 3, 3, true);
        saveMatToFile(EDGE, edgeImage);

        Mat dilatedImage = new Mat();
        Imgproc.dilate(thresholdImage, dilatedImage, kernel);
        saveMatToFile(DILATION, dilatedImage);

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(dilatedImage, contours, new Mat(), Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_SIMPLE);
        contours.sort(Collections.reverseOrder(Comparator.comparing(Imgproc::contourArea)));
        for (MatOfPoint contour : contours.subList(0, 10)) {
            System.out.println(Imgproc.contourArea(contour));
            MatOfPoint2f point2f = new MatOfPoint2f();
            MatOfPoint2f approxContour2f = new MatOfPoint2f();
            MatOfPoint approxContour = new MatOfPoint();
            contour.convertTo(point2f, CvType.CV_32FC2);

            double arcLength = Imgproc.arcLength(point2f, true);
            Imgproc.approxPolyDP(point2f, approxContour2f, 0.03 * arcLength, true);
            approxContour2f.convertTo(approxContour, CvType.CV_32S);
            Rect rect = Imgproc.boundingRect(approxContour);
            double ratio = (double) rect.height / rect.width;
            if (Math.abs(0.3 - ratio) > 0.15) {
                continue;
            }
            Mat submat = image.submat(rect);
            Imgproc.resize(submat, submat, new Size(400, 400 * ratio));
            saveMatToFile(RESULT+ contour.hashCode(), submat);
        }

    }

}