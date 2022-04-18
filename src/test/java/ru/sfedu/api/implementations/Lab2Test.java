package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lab2Test {

    private static final Logger log = LogManager.getLogger(Lab2Test.class);
    LabsImpl labsImpl = new LabsImpl();
    String TEST_IMAGE_PATH = "D:/compVision/images/";

    @Test
    @Order(0)
    void lab2Success() {
        ImageImpl imageImlp = new ImageImpl();
        log.info("lab2Success");
        String TEST_IMAGE_NAME = "testimage.jpg";
        labsImpl.task2(4, TEST_IMAGE_PATH, TEST_IMAGE_NAME);
    }

}
