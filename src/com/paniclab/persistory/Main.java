package com.paniclab.persistory;

import com.paniclab.persistory.annotations.Entity;
import com.paniclab.persistory.annotations.JoinForeignKeyColumn;
import com.paniclab.persistory.annotations.Linked;
import com.paniclab.persistory.annotations.OnDelete;
import com.paniclab.persistory.configuration.Configuration;
import com.paniclab.persistory.configuration.ConfigurationFactory;


import java.nio.file.Path;
import java.nio.file.Paths;

import static com.paniclab.persistory.annotations.Linked.*;

/**
 * Класс создан в тестовых целях. Будет удален в окончательной версии
 */
@Entity
@Linked(reference = InternalError.class,
        assotiation = ONE_TO_MANY,
        type = UNIDIRECTIONAL)

@Linked(reference = Logger.class,
        assotiation = ONE_TO_ONE,
        foreignKey = @JoinForeignKeyColumn(columnName = "niaM",
                notNull = true,
                onDelete = OnDelete.CASCADE))
public class Main {

    public static void main(String[] args) {

        Configuration cfg = Configuration.builder()
                .add("ss", "ss")
                .set(Configuration.MODE, Configuration.PRODUCTION)
                .setApplicationMode(Configuration.PRODUCTION)
                .create();

        Configuration another = new ConfigurationFactory().set("asfdad", "dasdad").create();


        Path somePath = Paths.get("logs", "log.log");
        System.out.println("Путь для логов: " + somePath);

        System.out.println("Путь к файлу конфигурации: " + Configuration.DEFAULT_CONFIG_PATH);

        Path newPath = Configuration.DEFAULT_CONFIG_PATH.getParent().relativize(somePath);
        System.out.println("Относительный путь: " + newPath);

        newPath = Configuration.DEFAULT_CONFIG_PATH.getParent().resolveSibling(somePath);
        System.out.println("Получается следующий путь:");
        System.out.println(newPath);

        //Path absoluteLogPath = Paths.get(Utils.getApplicationURI(newPath));
        Logger logger = new Logger(Configuration.PRODUCTION);
        System.out.println();
        System.out.println("Абсолютный путь для логов:");
        System.out.println(logger.getLogPath());

        logger.log(Configuration.DEVELOPING, "Это лог сообщение");
       //logger.log("Это сообщение появляется всегда");
        logger.logOnDevelop("Еще одно сообщение");

        Logger fileLogger = new Logger(Configuration.DEBUG, Logger.FILE);
        fileLogger.log(Configuration.DEVELOPING, "Hello");

        logger.logOnProduction("Однажды, в студеную зимнюю {1} я из лесу вышел, был сильный {2}!", "пору", "мороз");
    }
}
