package com.paniclab.persistory;

import com.paniclab.persistory.annotations.Entity;
import com.paniclab.persistory.annotations.JoinForeignKeyColumn;
import com.paniclab.persistory.annotations.Linked;
import com.paniclab.persistory.annotations.OnDelete;
import com.paniclab.persistory.configuration.Configuration;
import com.paniclab.persistory.configuration.ConfigurationFactory;
import com.paniclab.persistory.table.constraint.CheckConstraint;
import com.paniclab.persistory.table.constraint.Constraint;
import com.paniclab.persistory.table.constraint.ConstraintBuilder;
import com.paniclab.persistory.table.constraint.ConstraintFactory;


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

@Linked(reference = LoggerOld.class,
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
        LoggerOld loggerOld = new LoggerOld(Configuration.PRODUCTION);
        System.out.println();
        System.out.println("Абсолютный путь для логов:");
        System.out.println(loggerOld.getDefaultLogPath());

        loggerOld.log(Configuration.DEVELOPING, "Это лог сообщение");
       //logger.log("Это сообщение появляется всегда");
        loggerOld.logOnDevelop("Еще одно сообщение");

        LoggerOld fileLoggerOld = new LoggerOld(Configuration.DEBUG, LoggerOld.FILE);
        fileLoggerOld.log(Configuration.DEVELOPING, "Hello");

        loggerOld.logOnProduction("Однажды, в студеную зимнюю {1} я из лесу вышел, был сильный {2}!", "пору", "мороз");

       ConstraintBuilder builder = new ConstraintFactory().getConstraintBuilder().setType(Constraint.CHECK);
       Constraint constraint = new ConstraintFactory().getCheckConstraint(builder);
    }
}
