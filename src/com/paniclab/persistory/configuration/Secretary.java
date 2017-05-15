package com.paniclab.persistory.configuration;

import com.paniclab.persistory.InternalError;
import com.paniclab.persistory.Utils;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.paniclab.persistory.configuration.Configuration.DEFAULT_CONFIG_PATH;

/**
 * Created by Сергей on 13.05.2017.
 */
class Secretary {
    private Map<String, String> configMap;

    Secretary() {}

    final ConfigurationImpl.Builder getDefaults() {

        Path defaultConfigPath = getDefaultConfigPath();
        readConfig(defaultConfigPath);
        return new ConfigurationImpl.Builder(configMap);
    }

    private Path getDefaultConfigPath() {
        URI appCatalogURI = Utils.getApplicationURI(this);
        Path appCatalogPath = Paths.get(appCatalogURI);
        return appCatalogPath.resolve(DEFAULT_CONFIG_PATH);
    }

    private void readConfig(Path cfgPath) {
        configMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(cfgPath.toFile()),"UTF-8"))) {
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
        configMap.put(key, value);
    }

    final void saveCurrent() {
        configMap.put("IS_DEFAULT", "FALSE");
        String stringData = getConfigAsText();
        byte[] data = Utils.stringToBytes(stringData, "UTF-8");
        ByteBuffer buffer = ByteBuffer.wrap(data);
        Path path = getDefaultConfigPath().resolveSibling("current");
        try (WritableByteChannel channel = Channels.newChannel(new FileOutputStream(path.toFile(), false))) {
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (FileNotFoundException e) {
            throw new InternalError("Ошибка при сохранении текущей конфигурации. Файл конфигурации не может быть открыт"
                    + " или создан", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getConfigAsText() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry: configMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String separator = System.lineSeparator();
            sb.append(key).append("=").append(value).append(separator);
        }
        return sb.toString();
    }

    final ConfigurationImpl.Builder loadCurrent() {
        Path path = getDefaultConfigPath().resolveSibling("current");
        readConfig(path);
        return new ConfigurationImpl.Builder(configMap);
    }
}
