package dev.prognitio.palaestra_tournaments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    static Logger logger = LoggerFactory.getLogger(Log.class);

    public static void log(Type type, String message, Class source) {
        switch (type.name) {
            case "DEBUG" -> logger.debug(message + " | Class: " + source.getName());
            case "INFO" -> logger.info(message + " | Class: " + source.getName());
            case "WARN" -> logger.warn(message + " | Class: " + source.getName());
            case "ERROR" -> logger.error(message + " | Class: " + source.getName());
        }
    }

    public enum Type {

        DEBUG("DEBUG"), INFO("INFO"), WARN("WARN"), ERROR("ERROR");

        private String name;
        Type(String name) {
            this.name = name;
        }
    }

}
