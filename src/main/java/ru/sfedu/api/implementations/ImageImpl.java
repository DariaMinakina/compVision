package ru.sfedu.api.implementations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import ru.sfedu.utlis.ConfigurationUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;

import static ru.sfedu.Constants.*;
import static ru.sfedu.Constants.OSType.*;

public class ImageImpl {

    private static Logger log = LogManager.getLogger(ImageImpl.class);
    String OS = System.getProperty(OS_NAME, GENERIC).toLowerCase(Locale.ENGLISH);

    public ImageImpl(){
        try {
            log.info(CHECKING_OS);
            // init the API with curent os..
            switch (getOperatingSystemType(OS)) {
                case LINUX -> loadProp(PATH_TO_NATIVE_LIB_LINUX);
                case WINDOWS -> loadProp(PATH_TO_NATIVE_LIB_WIN);
                case MACOS -> throw new Exception(MACOS_DOESNT_SUPPORT);
                case OTHER -> throw new Exception(OTHER_OS_DOESNT_SUPPORT);
            }
        } catch(Exception e){
             log.error(e);
                }
        }

    private void loadProp(String path) throws IOException {
        String pathLin = ConfigurationUtil.getConfigurationEntry(path);
        System.load(Paths.get(pathLin).toAbsolutePath().toString());
        log.debug(PROP_ARE_LOADED + OS_VERSION + OS + NEXT_LINE + OPENCV_VERSION + Core.getVersionString());
    }

}
