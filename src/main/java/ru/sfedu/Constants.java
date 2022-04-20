package ru.sfedu;

import java.util.Locale;

public class Constants {

    public enum OSType {

        MACOS,
        WINDOWS,
        LINUX,
        OTHER;

        public static OSType getOperatingSystemType(String OS) {
            if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                return OSType.MACOS;
            } else if (OS.contains("win")) {
                return OSType.WINDOWS;
            } else if (OS.contains("nux")) {
                return OSType.LINUX;
            } else {
                return OSType.OTHER;
            }
        }
    }

    public static final String CHECKING_OS="Checking OS.....";
    public static final String MACOS_DOESNT_SUPPORT="Mac OS does not support!!!!!!!!";
    public static final String OTHER_OS_DOESNT_SUPPORT="Current OS does not support!!!!!";
    public static final String OS_NAME="os.name";
    public static final String GENERIC="generic";

    public static final String PATH_TO_NATIVE_LIB_LINUX = "pathToNativeLib";
    public static final String PATH_TO_NATIVE_LIB_WIN = "pathToNativeLib";
    public static final String PROP_ARE_LOADED="Properties are loaded \n";
    public static final String OS_VERSION="OS Version: ";
    public static final String NEXT_LINE= "\n";
    public static final String OPENCV_VERSION="Open CV version - ";

    public static final String IMAGE_PATH = "./src/main/resources/image";
    public static final String SUPPORTS_ONLY_JPG_AND_PNG="only .JPG and .PNG files are supported";
    public static final String IMAGE_FORMAT="%s/%s.jpg";
    public static final String BLUR ="Blur";
    public static final String GAUSSIAN ="Gaussian";
    public static final String MEDIAN ="Median";
    public static final String BILATERAL ="Bilateral";
    public static final String SOBELX="SobelX_";
    public static final String SOBELY="SobelY_";
    public static final String LAPLACE = "Laplace";
    public static final String MIRROR = "Mirror";
    public static final String REPEAT="Repeat";
    public static final String UNITY ="Unity";
    public static final String SIZE = "Size";
    public static final String ROTATION ="Rotation";
    public static final String WARP ="Warp";
    public static final String MRF_ELIPSE_RECT="mrf_ellipse_rect";
    public static final String MRF_GRADIENT_RECT="mrf_gradient_rect";
    public static final String MRF_BLACKHAT_RECT="mrf_blackhat_rect";
    public static final String MRF_ELLIPSE_EL="mrf_ellipse_el_";
    public static final String MRF_GRADIENT_EL="mrf_gradient_el";
    public static final String MRF_BLACKHAT_EL="mrf_blackhat_el";
    public static final String MRF_ERODE_RECT="mrf_erode_rect";
    public static final String MRF_DILATE_RECT="mrf_dilate_rect";
    public static final String MRF_ERODE_EL="mrf_erode_el";
    public static final String MRF_DILATE_EL="mrf_dilate_el";
    public static final String PRF_NAME="mrf_";
    public static final String FILL ="FILL";
    public static final String PYRDOWN_NATIVE="pyrDown native";
    public static final String PYRUP="pyrUp";
    public static final String CORE="Core";
    public static final String GRAYIMAGE="grayImage.jpg";
    public static final String NO_NOISE= "noNoise";
    public static final String HISTOGRAM="histogramEq";
    public static final String MORPHOLOGICAL_OPENING="morphologicalOpening";
    public static final String SUBTRACT="subtract";
    public static final String THRESHOLD="threshold";
    public static final String TR2 ="tr2";
    public static final String EDGE="edge";
    public static final String DILATION ="dilation";
    public static final String RESULT="result";
    public static final String DETECTED_EDGES= "detectedEdges";
}
