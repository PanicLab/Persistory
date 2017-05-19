package com.paniclab.persistory.logger;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.LoggingException;
import com.paniclab.persistory.Utils;
import com.paniclab.persistory.configuration.Configuration;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.paniclab.persistory.Utils.isNot;

/**
 * Created by Сергей on 19.05.2017.
 */
public abstract class Logger {

    public static final int FILE = 1;
    public static final int PRINT_STREAM = 2;
    public static final int PRINT_WRITER = 4;
    public static final Path RELATIVE_LOG_PATH = Paths.get("logs", "log.log");

    private String appMode;
    private int dest;
    private String name = "";
    private PrintStream printStream;
    private PrintWriter printWriter;

    private Logger(Logger.Builder builder) {
        this.appMode = Configuration.getCurrent().get("MODE");
        this.name = builder.name;
        this.dest = builder.dest;
        this.printStream = builder.printStream;
        this.printWriter = builder.printWriter;
    }

    public static Logger.Builder newFileLogger() {
        return new LoggerFactory().getFileLogger();
    }

    public static Logger.Builder newLogger(PrintStream ps) {
        return new LoggerFactory().getLogger(ps);
    }

    public static Logger.Builder newLogger(PrintWriter pw) {
        return null;
    }


    protected void log(String message) {
        switch (dest) {
            case FILE:
                toFile(message);
                break;
            case PRINT_STREAM:
                log(printStream, message);
                break;
            case PRINT_WRITER:
                log(printWriter, message);
            default:
                throw new InternalError("Неизвестный тип логгера: " + dest);
        }
    }


    protected void log(String message, Object...objects) {
        message = parseMessage(message, objects);
        log(message);
    }


    protected void log(String level, String message) {
        switch (level) {
            case Configuration.PRODUCTION:
                log(message);
                break;
            case Configuration.DEVELOPING:
                logDev(message);
                break;
            case Configuration.DEBUG:
                logDebug(message);
                break;
            default:
                throw new InternalError("Неизвестный режим запуска приложения: " + level);
        }
    }
    protected void log(String level, String message, Object...objects) {
        switch (level) {
            case Configuration.PRODUCTION:
                log(message, objects);
                break;
            case Configuration.DEVELOPING:
                logDev(message, objects);
                break;
            case Configuration.DEBUG:
                logDebug(message, objects);
                break;
            default:
                throw new InternalError("Неизвестный режим запуска приложения: " + level);
        }
    }

    public void toFile(String message) {
        Path dir = Paths.get(getDefaultLogPath().getParent().toUri());
        if (Files.notExists(dir)) {
            boolean success = dir.toFile().mkdirs();
            if (isNot(success)) {
                throw new LoggingException("Не удалось создать каталог лог-файла");
            }
        }

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(getDefaultLogPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND))){
            pw.println();
            long timeMillis = System.currentTimeMillis();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());

            pw.println("Message " + time.format(formatter) + ": ");
            pw.println(message);
        } catch (IOException e) {
            throw new LoggingException("Ошибка при записи лог-файла.", e);
        }
    }

    public Path getDefaultLogPath() {
        return Paths.get(Utils.getApplicationURI(this))
                .resolve(Configuration.DEFAULT_CONFIG_PATH)
                .getParent()
                .resolveSibling(RELATIVE_LOG_PATH);
    }


    public  void logDev(String message) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        log(message);
    }


    public  void logDev(String message, Object...objects) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        log(message, objects);
    }


    public void logDebug(String message) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        if(appMode.equals(Configuration.DEVELOPING)) return;
        log(message);
    }


    public  void logDebug(String message, Object...objects) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        if(appMode.equals(Configuration.DEVELOPING)) return;
        log(message, objects);
    }


    private void log(PrintStream ps, String message) {
        ps.println(message);
    }


    private void log(PrintWriter pw, String message) {
        pw.println(message);
    }


    private String parseMessage(String message, Object...objects) {
        String pattern = "{i}";
        for (int x = 1; x <= objects.length; x++)
        {
            String sub = pattern.replace("i",String.valueOf(x));
            message = message.replace(sub, String.valueOf(objects[x-1]));
        }
        return message;
    }


    protected abstract static class Builder {
        private int dest;
        private String name = "";
        private PrintStream printStream;
        private PrintWriter printWriter;

        protected Builder() {}

        public Logger.Builder setName(String name) {
            this.name = name;
            return this;
        }


        public Logger.Builder setDestination(int d) {
            if(this.dest != 0) throw new InternalError("Невозможно перезадать уже заданный параметр");
            switch (d) {
                case Logger.FILE:
                    dest = Logger.FILE;
                    break;
                case Logger.PRINT_STREAM:
                    dest = Logger.PRINT_STREAM;
                    break;
                case Logger.PRINT_WRITER:
                    dest = Logger.PRINT_WRITER;
                default:
                    throw new InternalError("Неизвестный тип логгера: " + d);
            }
            return this;
        }

        public Logger.Builder setDestination(PrintStream ps) {
            if(dest == Logger.FILE) throw new InternalError("Этот экземпляр уже определен как файловый логгер");
            if(printWriter != null) throw new InternalError("Этот экземпляру уже задан PrintWriter для вывода " +
                    "сообщений");
            this.printStream = ps;
            this.dest = Logger.PRINT_STREAM;
            return this;
        }

        public Logger.Builder setDestination(PrintWriter pw) {
            if(dest == Logger.FILE) throw new InternalError("Этот экземпляр уже определен как файловый логгер");
            if(printStream != null) throw new InternalError("Этот экземпляру уже задан PrintStream для вывода " +
                    "сообщений");
            this.printWriter = pw;
            this.dest = Logger.PRINT_WRITER;
            return this;
        }

        public abstract Logger build();
    }
}
