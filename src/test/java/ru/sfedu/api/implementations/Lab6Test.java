package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lab6Test {

    private static final Logger log = LogManager.getLogger(Lab6Test.class);
    LabsImpl labsImpl = new LabsImpl();
    String TEST_IMAGE_PATH = "D:/compVision/images/";
    String SOFA_ONE_NAME="59383703cd75aeaf22999eb63044c2fa.jpg";

    @Test
    @Order(0)
    void lab6GrayToBlackAndWhite() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab6GrayToBlackAndWhite");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + SOFA_ONE_NAME, Imgcodecs.IMREAD_COLOR);
        labsImpl.grayToBlackAndWhite(image);
    }

}
