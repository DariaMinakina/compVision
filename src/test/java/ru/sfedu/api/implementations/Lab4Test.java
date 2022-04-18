package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lab4Test {

    private static final Logger log = LogManager.getLogger(Lab4Test.class);
    LabsImpl labsImpl = new LabsImpl();
    String TEST_IMAGE_PATH = "D:/compVision/images/";
    String BAD="480.jpg";
    String FINE="720.jpg";
    String GOOD="1080.jpg";
    String IMAGE_AD="ad.jpg";


    @Test
    @Order(0)
    void lab4() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab4");
        labsImpl.task4(TEST_IMAGE_PATH, 5,5, BAD);
        labsImpl.task4(TEST_IMAGE_PATH, 5,5, FINE);
        labsImpl.task4(TEST_IMAGE_PATH, 5,5, GOOD);
    }

    @Test
    @Order(1)
    void lab4Rect() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab4Rect");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + IMAGE_AD, Imgcodecs.IMREAD_COLOR);
        labsImpl.morphingRect(image, IMAGE_AD);
    }

    @Test
    @Order(2)
    void lab4MorphingEllipse() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab4MorphingEllipse");
        Mat image = Imgcodecs.imread(TEST_IMAGE_PATH + IMAGE_AD, Imgcodecs.IMREAD_COLOR);
        labsImpl.morphingEllipse(image, IMAGE_AD);
    }


}
