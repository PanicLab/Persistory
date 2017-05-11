package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.Utils;

import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс предназначен для создания объекта типа Configuration. Используется следующий синтаксис:
 *
 * 	Configuration cfg = new ConfigurationBuilder().getDefault()
 * 	                                    .set(Configuration.MODE, Configuration.DEBUG)
 * 	                                    .set(Configuration.DEFAULT_DBMS, PersistorManager.H2);
 * 	                                    .add(Configuration.DOMAIN_PACKAGE, "com.github.paniclab.coolApp.Models")
 * 	                                    .add(Configuration.CLASS_TO_PERSIST, "com.github.paniclab.coolApp.Models.jet")
 * 	                                    .create();
 *
 * 	Создание конфигурации опираетс на конфигурационный файл, содержащий в себе настройки по умолчанию. В процессе
 * 	создания экземпляра типа Configuration параметры могут быть добавлены (или переопределены) программно.
 */
public class ConfigurationBuilder {

    private ConfigurationImpl.ConfigHelper configuration = new ConfigurationImpl.ConfigHelper();
    
    ConfigurationBuilder() {}


    public final ConfigurationBuilder getDefault() {

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
        String[] s = line.split("=");
        String key = s[0].trim();
        String value = s.length > 1 ? line.split("=")[1].trim() : "";
        this.configuration.set(key, value);
    }

    /**
     * Метод задает параметр конфигурации и его значение.
     * @param property: один из параметров конфигурации.
     * @param value: значение параметра конфигурации
     */
    public ConfigurationBuilder set(String property, String value) {
        this.configuration.set(property,value);
        return this;
    }

    //TODO не реализовано
    public ConfigurationBuilder add(String property, String value) {
        return this;
    }

    public Configuration create() {
        return immutable(configuration);
    }

    private Configuration immutable(ConfigurationImpl.ConfigHelper helper) {
        return new ConfigurationImpl(helper);
    }

}
