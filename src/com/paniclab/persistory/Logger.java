package com.paniclab.persistory;

import com.paniclab.persistory.configuration.Configuration;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.paniclab.persistory.Utils.isNot;
import static com.paniclab.persistory.configuration.Configuration.*;

/**
 * Простенький логгер для местного использования. Может писать логи в файл, System.out или System.err по выбору. По
 * умолчанию пишет System.out. Выбор этот задается однажды при создании экземпляра и более не изменяется. Для создания
 * экземпляра необходимо передать конструктору в качестве аргумента объект типа Configuration. Второй необязательный
 * аргумент это одна из трех констант - Logger.TO_FILE, Logger.SYS_OUT или Logger.SYS_ERR. Enumeration не используется,
 * поскольку не рекомендуется их использование в Android.
 *
 *              Logger fileLogger = new Logger(pm.getConfig(), Logger.TO_FILE);
 *              ...
 *              Logger logger = new Logger(pm.getConfig);                       // Logger.SYS_OUT
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

    public static final int TO_FILE = 1;
    public static final int SYS_OUT = 2;
    public static final int SYS_ERR = 3;
    public static final Path RELATIVE_LOG_PATH = Paths.get("logs", "log.log");

    private String execMode;
    private int dest;


    Logger() {
    }

    Logger(String execMode) {
        this.execMode = execMode;
        this.dest = SYS_OUT;
    }

    Logger(String execMode, int dest) {
        this.execMode = execMode;
        this.dest = dest;
    }

    public Logger(Configuration conf) {
        this(conf.get(MODE));
        this.dest = SYS_OUT;
    }

    public Logger(Configuration conf, int dest) {
        this(conf.get(MODE), dest);
    }


    public Path getLogPath() {
        return Paths.get(Utils.getApplicationURI(this))
                .resolve(DEFAULT_CONFIG)
                .getParent()
                .resolveSibling(RELATIVE_LOG_PATH);
    }

    public void log(String level, String message) {
        switch (level) {
            case PRODUCTION:
                logOnProduction(message);
                break;
            case DEVELOPING:
                logOnDevelop(message);
                break;
            case DEBUG:
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
            case Logger.TO_FILE:
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
        Path dir = Paths.get(getLogPath().getParent().toUri());
        if (Files.notExists(dir)) {
            boolean success = dir.toFile().mkdirs();
            if (isNot(success)) {
                throw new LoggingException("Не удалось создать каталог лог-файла");
            }
        }

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(getLogPath(),
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
            case PRODUCTION:
                logOnProduction(message, objects);
                break;
            case DEVELOPING:
                logOnDevelop(message, objects);
                break;
            case DEBUG:
                logOnDebug(message, objects);
                break;
            default:
                throw new InternalError("Неизвестный режим вывода логгера: " + dest);
        }
    }


    public void logOnDevelop(String message) {
        if(execMode.equals(PRODUCTION)) return;
        log(message);
    }

    public void logOnDebug(String message) {
        if(execMode.equals(PRODUCTION)) return;
        if(execMode.equals(DEVELOPING)) return;
        log(message);
    }

    public void logOnProduction(String message, Object...objects) {
        log(message,objects);
    }

    private void log(String message, Object...objects) {
        switch (dest) {
            case Logger.TO_FILE:
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
        if(execMode.equals(PRODUCTION)) return;
        log(message, objects);
    }

    public void logOnDebug(String message, Object...objects) {
        if(execMode.equals(PRODUCTION)) return;
        if(execMode.equals(DEVELOPING)) return;
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
}
