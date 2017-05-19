package com.paniclab.persistory;

import com.paniclab.persistory.configuration.Configuration;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.paniclab.persistory.Utils.isNot;

/**
 * Простенький логгер для местного использования. Может писать логи в файл, System.out или System.err по выбору. По
 * умолчанию пишет System.out. Выбор этот задается однажды при создании экземпляра и более не изменяется. Для создания
 * экземпляра необходимо передать конструктору в качестве аргумента объект типа Configuration. Второй необязательный
 * аргумент это одна из трех констант - logger.TO_FILE, logger.SYS_OUT или logger.SYS_ERR. Enumeration не используется,
 * поскольку не рекомендуется их использование в Android.
 *
 *              logger fileLogger = new logger(pm.getConfig(), logger.TO_FILE);
 *              ...
 *              logger logger = new logger(pm.getConfig);                       // logger.SYS_OUT
 *
 * При выводе лог-сообщения следует указать режим запуска приложения, при котором данный лог будет отображаться -
 * Configuration.PRODUCTION, Configuration.DEVELOPING или Configuration.DEBUG. К примеру, если приложение запущено в
 * режиме PRODUCTION, то лог с параметром DEVELOPING отображаться не будет, однако этот же лог будет отображаться, если
 * приложение будет запущено в режиме DEVELOPING или DEBUG.
 *
 *              logger.log(DEVELOPING, "Hello");
 *
 * В качестве альтернативы можно использовать более удобные
 * методы logOnProduction(), logOnDevelop() или logOnDebug().
 * Логгер поддерживает форматированный вывод, который позволяет увеличить производительность:
 *
 *              logger.LogOnProduction("Птички все на {1}, а я, бедняжка, в {2}", branch, cage);
 *
 *
 */
public class Logger {

    public static final int FILE = 1;
    public static final int SYS_OUT = 2;
    public static final int SYS_ERR = 4;
    public static final int PRINT_WRITER = 8;
    public static final Path RELATIVE_LOG_PATH = Paths.get("logs", "log.log");

    private String appMode;
    private int dest;


    Logger() {
    }

    Logger(String appMode) {
        this.appMode = appMode;
        this.dest = SYS_OUT;
    }

    Logger(String appMode, int dest) {
        this.appMode = appMode;
        this.dest = dest;
    }

    public Logger(Configuration conf) {
        this(conf.get(Configuration.MODE));
        this.dest = SYS_OUT;
    }

    public Logger(Configuration conf, int dest) {
        this(conf.get(Configuration.MODE), dest);
    }


    public Path getDefaultLogPath() {
        return Paths.get(Utils.getApplicationURI(this))
                .resolve(Configuration.DEFAULT_CONFIG_PATH)
                .getParent()
                .resolveSibling(RELATIVE_LOG_PATH);
    }

    public void log(String level, String message) {
        switch (level) {
            case Configuration.PRODUCTION:
                logOnProduction(message);
                break;
            case Configuration.DEVELOPING:
                logOnDevelop(message);
                break;
            case Configuration.DEBUG:
                logOnDebug(message);
                break;
            default:
                throw new InternalError("Неизвестный режим кофигурации Persistory: " + level);
        }
    }

    public void logOnProduction(String message) {
        log(message);
    }

    private void log(String message) {
        switch (dest) {
            case Logger.FILE:
                logToFile(message);
                break;
            case Logger.SYS_OUT:
                log(System.out, message);
                break;
            case Logger.SYS_ERR:
                log(System.err, message);
                break;
            default:
                throw new InternalError("Неизвестный режим вывода логгера: " + dest);
        }
    }

    private void logToFile(String message) {
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

    private void log(PrintStream printStream, String message) {
        printStream.println(message);
    }

    public void log(String level, String message, Object...objects) {
        switch (level) {
            case Configuration.PRODUCTION:
                logOnProduction(message, objects);
                break;
            case Configuration.DEVELOPING:
                logOnDevelop(message, objects);
                break;
            case Configuration.DEBUG:
                logOnDebug(message, objects);
                break;
            default:
                throw new InternalError("Неизвестный режим вывода логгера: " + dest);
        }
    }


    public void logOnDevelop(String message) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        log(message);
    }

    public void logOnDebug(String message) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        if(appMode.equals(Configuration.DEVELOPING)) return;
        log(message);
    }

    public void logOnProduction(String message, Object...objects) {
        log(message,objects);
    }

    private void log(String message, Object...objects) {
        switch (dest) {
            case Logger.FILE:
                logToFile(parseMessage(message, objects));
                break;
            case Logger.SYS_OUT:
                log(System.out, parseMessage(message, objects));
                break;
            case Logger.SYS_ERR:
                log(System.err, parseMessage(message, objects));
                break;
            default:
                throw new InternalError("Неизвестный режим вывода логгера: " + dest);
        }
    }

    public void logOnDevelop(String message, Object...objects) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        log(message, objects);
    }

    public void logOnDebug(String message, Object...objects) {
        if(appMode.equals(Configuration.PRODUCTION)) return;
        if(appMode.equals(Configuration.DEVELOPING)) return;
        log(message, objects);
    }

    String parseMessage(String message, Object...objects) {
        String pattern = "{i}";
        for (int x = 1; x <= objects.length; x++)
        {
            String sub = pattern.replace("i",String.valueOf(x));
            message = message.replace(sub, String.valueOf(objects[x-1]));
        }
        return message;
    }

    static class Builder {

        private int dest;
        private String name = "";
        private PrintWriter printWriter;
        private PrintStream printStream;
        private boolean alreadySet;


        private Builder() {}

        public Logger.Builder setDestination(int file) {
            if(alreadySet) throw new InternalError("Пункт назначения логгера уже назначен");
            switch (file) {
                case Logger.FILE:
                    dest = Logger.FILE;
                    break;
                case Logger.SYS_OUT:
                    dest = Logger.SYS_OUT;
                    break;
                case Logger.SYS_ERR:
                    dest = Logger.SYS_ERR;
                    break;
                default:
                    throw new InternalError("Неизвестный тип логгера: " + file);
            }
            alreadySet = true;
            return this;
        }

        public Logger.Builder setDestination(PrintWriter pw) {
            if(alreadySet) throw new InternalError("Пункт назначения логгера уже назначен");
            alreadySet = true;
            printWriter = pw;
            return this;
        }

        public Logger.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Logger build() {
            return new Logger();
        }
    }
}
