package com.paniclab.persistory.logger;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by Сергей on 19.05.2017.
 */
class LoggerFactory {

    LoggerFactory() {}

    Logger.Builder getFileLogger() {
        return null;
    }

    Logger.Builder getLogger(PrintStream ps) {
        return null;
    }

    Logger.Builder getLogger(PrintWriter pw) {
        return null;
    }
}
