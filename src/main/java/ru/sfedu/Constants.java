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
}
