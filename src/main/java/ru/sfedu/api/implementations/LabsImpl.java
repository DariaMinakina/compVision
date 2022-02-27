package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import ru.sfedu.api.interfaces.LabsInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            final String modFilePath = buildImageName(IMAGE_PATH,imageName);
            Imgcodecs.imwrite(modFilePath, img);
        } catch (Exception e) {
            log.info(SUPPORTS_ONLY_JPG_AND_PNG);
            log.error(e);
        }
    }

    private String buildImageName(String base, String name) {
        return String.format(IMAGE_FORMAT, base, name);
    }

}
