package com.paniclab.persistory.logger;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.LoggingException;
import com.paniclab.persistory.Utils;
import com.paniclab.persistory.configuration.Configuration;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.paniclab.persistory.Utils.isNot;

/**
 * Created by Сергей on 19.05.2017.
 */
public class Logger {

    private static final int FILE = 1;
    private static final int PRINT_STREAM = 2;
    private static final int PRINT_WRITER = 4;
    private static final Path RELATIVE_LOG_PATH = Paths.get("logs", "log.log");

    private String appMode;
    private int dest;
    private String name = "";
    private PrintStream printStream;
    private PrintWriter printWriter;
    private Path path;

    private Logger(Logger.Builder builder) {
        this.appMode = Configuration.getCurrent().get("MODE");
        this.name = builder.name;
        this.dest = builder.dest;
        this.path = builder.path;
        this.printStream = builder.printStream;
        this.printWriter = builder.printWriter;
    }

    public static Logger.Builder customFileLogger(Object obj) {
        return new Logger.Builder().setDefaultName(obj)
                                   .setDefaultDest(Logger.FILE);
    }

    public static Logger newFileLogger(Object obj) {
        return new Logger.Builder().setDefaultName(obj)
                                   .setDefaultDest(Logger.FILE)
                                   .build();
    }

    public static Logger.Builder customLogger(Object obj) {
        return new Logger.Builder().setDefaultName(obj);
    }

    public static Logger newLogger(Object obj) {
        return new Logger.Builder().setDefaultName(obj)
                                   .setDefaultDest(Logger.PRINT_STREAM)
                                   .build();
    }


    private static Path getDefaultLogPath(Object obj) {
        return Paths.get(Utils.getApplicationURI(obj))
                .resolve(Configuration.DEFAULT_CONFIG_PATH)
                .getParent()
                .resolveSibling(RELATIVE_LOG_PATH);
    }

    String getName() {
        return name;
    }

    public void log(String level, String message, Object...objects) {
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


    public void log(String message, Object...objects) {
        message = parseMessage(message, objects);
        log(message);
    }


    public  void logDev(String message, Object...objects) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        log(message, objects);
    }


    public  void logDebug(String message, Object...objects) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        if(appMode.equals(Configuration.DEVELOPING)) return;
        log(message, objects);
    }


    public void log(String level, String message) {
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


    private void log(String message) {
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


    public  void logDev(String message) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        log(message);
    }


    public void logDebug(String message) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        if(appMode.equals(Configuration.DEVELOPING)) return;
        log(message);
    }


    private void toFile(String message) {
        if (Files.notExists(path)) {
            boolean success = path.toFile().mkdirs();
            if (isNot(success)) {
                throw new LoggingException("Не удалось создать каталог лог-файла");
            }
        }

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(getDefaultLogPath(this),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND))){
            pw.println();
            long timeMillis = System.currentTimeMillis();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());

            pw.println(name + " " + time.format(formatter) + ": ");
            pw.println(message);
        } catch (IOException e) {
            throw new LoggingException("Ошибка при записи лог-файла.", e);
        }
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


    public static class Builder {
        private int dest;
        private String name = "";
        private PrintStream printStream;
        private PrintWriter printWriter;
        private Path path;

        private Builder() {}

        public Logger.Builder setName(String name) {
            this.name = name;
            return this;
        }

        private Logger.Builder setDefaultName(Object obj) {
            name = "@" + obj.hashCode() + " " + obj.getClass().getSimpleName();
            return this;
        }

        public Logger.Builder setDestination(String logPath) {
            if(printStream != null) throw new InternalError("Этот экземпляру уже задан PrintStream для вывода " +
                    "сообщений");
            if(printWriter != null) throw new InternalError("Этот экземпляру уже задан PrintWriter для вывода " +
                    "сообщений");

            dest = Logger.FILE;

            URI uri;
            try {
                uri = new URI(logPath);
            } catch (URISyntaxException e) {
                throw new InternalError("Невозможно создать лог файл. Путь к файлу задан неверно."
                        + System.lineSeparator() + "Путь: " + logPath, e);
            }
            path = Paths.get(uri);

            return this;
        }

        private Logger.Builder setDefaultDest(int dest) {
            switch (dest) {
                case Logger.FILE:
                    this.dest = dest;
                    this.path = Logger.getDefaultLogPath(this);
                    break;
                case Logger.PRINT_STREAM:
                    this.dest = dest;
                    this.printStream = System.out;
                    break;
                case Logger.PRINT_WRITER:
                    throw new InternalError("Невозможно установить данный тип логгера по умолчанию: " + dest);
                default:
                    throw new InternalError("Неизвестный тип логгера: " + dest);
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

        public Logger build() {
            return new Logger(this);
        }
    }
}
