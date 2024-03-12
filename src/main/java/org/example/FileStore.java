package org.example;

import org.example.FileSystem.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileStore {
    public static <T> void saveData(String key, T value) {
        try {
            File file = new File("./store.yml");
            if (!file.exists())
                file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            yaml.set(key, value);
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T loadData(String key) {
        try {
            File file = new File("./store.yml");
            if (!file.exists())
                file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            return (T) yaml.get(key);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
