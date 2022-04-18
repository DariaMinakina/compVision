package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lab3Test {

    private static final Logger log = LogManager.getLogger(Lab3Test.class);
    LabsImpl labsImpl = new LabsImpl();
    String TEST_IMAGE_PATH = "D:/compVision/images/";
    String SOFA_ONE_NAME="59383703cd75aeaf22999eb63044c2fa.jpg";
    String SOFA_TWO_NAME="alyaska_maron11.jpg";

    @Test
    @Order(0)
    void lab3Sobel() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3Sobel");
        labsImpl.convertSobel(TEST_IMAGE_PATH,SOFA_ONE_NAME, 1, 1);
    }

    @Test
    @Order(1)
    void lab3Laplace() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3Laplace");
        labsImpl.convertLaplace(TEST_IMAGE_PATH,SOFA_ONE_NAME);
    }

    @Test
    @Order(2)
    void lab3Mirror() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3Mirror");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_ONE_NAME, Imgcodecs.IMREAD_COLOR);
        labsImpl.mirror(image,0, SOFA_ONE_NAME);
    }

    @Test
    @Order(3)
    void lab3Unity() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3Unity");
        String SOFA_ONE_NAME_NEW="NEW59383703cd75aeaf22999eb63044c2fa.jpg";
        String SOFA_TWO_NAME_NEW="NEWalyaska_maron11.jpg";
        Mat imageOne = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_ONE_NAME, Imgcodecs.IMREAD_COLOR);
        Mat imageTwo = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_TWO_NAME, Imgcodecs.IMREAD_COLOR);
        imageOne=labsImpl.changeImageSize(imageOne, 600, 600, SOFA_ONE_NAME_NEW);
        imageTwo=labsImpl.changeImageSize(imageTwo, 600, 600, SOFA_TWO_NAME_NEW);
        labsImpl.unityImage(imageOne, imageTwo, SOFA_ONE_NAME, 0);
    }

    @Test
    @Order(4)
    void lab3Repeat() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3Repeat");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_ONE_NAME, Imgcodecs.IMREAD_COLOR);
        labsImpl.repeatImage(image,2,3, SOFA_ONE_NAME);
    }

    @Test
    @Order(5)
    void lab3ChangeImageSize() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3ChangeImageSize");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_ONE_NAME, Imgcodecs.IMREAD_COLOR);
        labsImpl.changeImageSize(image, 50, 50, SOFA_ONE_NAME);
    }

    @Test
    @Order(6)
    void lab3RotationChanges() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3RotationChanges");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_ONE_NAME, Imgcodecs.IMREAD_COLOR);
        Mat dst=new Mat();
        labsImpl.rotationChanges(image, dst, 45, false, SOFA_ONE_NAME);
    }

    @Test
    @Order(7)
    void lab3WarpImage() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab3WarpImage");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_ONE_NAME, Imgcodecs.IMREAD_COLOR);
        labsImpl.warpImage(image,  100,  100,  SOFA_ONE_NAME);
    }

}
