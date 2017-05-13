package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.paniclab.persistory.configuration.Configuration.DEFAULT_CONFIG_PATH;

/**
 * Created by Сергей on 13.05.2017.
 */
class Secretary {
    private ConfigurationImpl.Builder builder = new ConfigurationImpl.Builder();

    Secretary() {}

    final ConfigurationImpl.Builder getDefaults() {

        Path defaultConfigPath = getDefaultConfigPath();
        readDefaultConfig(defaultConfigPath);
        return builder;
    }

    private Path getDefaultConfigPath() {
        URI appCatalogURI = Utils.getApplicationURI(this);
        Path appCatalogPath = Paths.get(appCatalogURI);
        return appCatalogPath.resolve(DEFAULT_CONFIG_PATH);
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
        builder.set(key, value);
    }
}
