package com.paniclab.persistory.configuration;

import com.paniclab.persistory.Logger;
import com.paniclab.persistory.Utils;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс предназначен для создания объекта типа Configuration. Используется следующий синтаксис:
 *
 * 	Configuration cfg = new ConfigurationFactory().getDefault()
 * 	                                    .set(Configuration.MODE, Configuration.DEBUG)
 * 	                                    .set(Configuration.DEFAULT_DBMS, PersistorManager.H2);
 * 	                                    .add(Configuration.DOMAIN_PACKAGE, "com.github.paniclab.coolApp.Models")
 * 	                                    .add(Configuration.CLASS_TO_PERSIST, "com.github.paniclab.coolApp.Models.jet")
 * 	                                    .create();
 *
 * 	Создание конфигурации опираетс на конфигурационный файл, содержащий в себе настройки по умолчанию. В процессе
 * 	создания экземпляра типа Configuration параметры могут быть добавлены (или переопределены) программно.
 */
public class ConfigurationFactory {

    private Configuration configuration = new Configuration();
    
    public ConfigurationFactory() {
    }


    public ConfigurationFactory getDefault() {

        Path defaultConfigPath = getDefaultConfigPath();
        readDefaultConfig(defaultConfigPath);
        return this;
    }

    public Path getDefaultConfigPath() {
        URI appCatalogURI = Utils.getApplicationURI(this);
        Path appCatalogPath = Paths.get(appCatalogURI);
        System.out.println("Каталог определен как: " + appCatalogPath);
        System.out.println("Путь файла конфигурации задан как: " + Configuration.DEFAULT_CONFIG);

        return appCatalogPath.resolve(Configuration.DEFAULT_CONFIG);
    }

    private void readDefaultConfig(Path cfgPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(cfgPath.toFile()))){
            String line = br.readLine();
            while (line != null) {
                //System.out.println(line);
                populateConfig(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Persistory: Ошибка при чтении файла конфигурации по умолчанию - файл не найден.");
            System.err.println("Запуск приложения невозможен. Обратитесь к разработчику.");
            System.err.println("Путь к файлу: " + cfgPath.toString());
            throw new InternalError(e);
        } catch (IOException e) {
            System.err.println("Persistory: Ошибка чтения файла конфигурации по умолчанию.");
            System.err.println("Запуск приложения невозможен. Обратитесь к разработчику.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void populateConfig(String line) {
        String key = line.split("=")[0].trim();
        String value = line.split("=")[1].trim();
        this.configuration.set(key, value);
    }


    public ConfigurationFactory set(String property, String value) {
        this.configuration.set(property,value);
        return this;
    }

    //TODO не реализовано
    public ConfigurationFactory add(String property, String value) {
        return this;
    }


    public Configuration create() {
        return configuration;
    }
}
