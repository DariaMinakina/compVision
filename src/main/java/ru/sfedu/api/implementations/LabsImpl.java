package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ru.sfedu.api.interfaces.LabsInterface;

import javax.swing.*;
import java.awt.*;
import org.opencv.core.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;
import java.util.List;

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

}