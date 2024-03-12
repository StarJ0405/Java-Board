package org.example.FileSystem;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class YamlConfiguration {
    private final Yaml yaml;
    private final LinkedHashMap<String, Object> map;
    private final LinkedHashMap<String, Object> parent;

    public YamlConfiguration() {
        this(new LinkedHashMap<>(), null);
    }

    private YamlConfiguration(LinkedHashMap<String, Object> parent) {
        this(new LinkedHashMap<>(), parent);
    }

    private YamlConfiguration(LinkedHashMap<String, Object> map, LinkedHashMap<String, Object> parent) {
        this.yaml = new Yaml();
        this.map = map;
        this.parent = Optional.ofNullable(parent).orElse(map);
    }

    public void load(File file) throws FileNotFoundException {
        Object data = yaml.load(new FileReader(file));
        if (data != null && data instanceof HashMap) map.putAll((Map<? extends String, ?>) data);
    }

    public void save(File file) throws IOException {
        yaml.dump(this.parent, new FileWriter(file));
    }

    public <T> T get(String key) {
        if (!hasSpliter(key))
            return (T) map.get(key);
        else
            return moveToKey(key).get(getFinalKey(key));
    }

    public boolean has(String key) {
        if (!hasSpliter(key))
            return map.containsKey(key);
        else {
            String[] sp = key.split("\\.");
            LinkedHashMap<String, Object> nowMap = this.map;
            for (int i = 0; i < sp.length - 1; i++) {
                String subKey = sp[i];
                if (nowMap.containsKey(subKey)) {
                    Object data = nowMap.get(subKey);
                    if (data != null && data instanceof LinkedHashMap)
                        nowMap = (LinkedHashMap<String, Object>) data;
                    else return false;
                } else return false;
            }
            return true;
        }
    }

    public <T> void set(String key, T value) {
        if (!hasSpliter(key))
            map.put(key, value);
        else
            moveToKey(key).set(getFinalKey(key), value);
    }

    public YamlConfiguration getConfigurationSection(String key) {
        if (!hasSpliter(key)) {
            Object data = this.map.get(key);
            if (data != null && data instanceof HashMap) {
                YamlConfiguration section = new YamlConfiguration((LinkedHashMap<String, Object>) data, parent);
                return section;
            } else {
                YamlConfiguration section = new YamlConfiguration(this.parent);
                this.map.put(key, section.map);
                return section;
            }
        } else
            return moveToKey(key).getConfigurationSection(getFinalKey(key));
    }

    private boolean hasSpliter(String key) {
        return key.contains(".");
    }

    private String getFinalKey(String key) {
        String[] sp = key.split("\\.");
        return sp[sp.length - 1];
    }

    private YamlConfiguration moveToKey(String key) {
        String[] sp = key.split("\\.");
        LinkedHashMap<String, Object> nowMap = this.map;
        for (int i = 0; i < sp.length - 1; i++) {
            String subKey = sp[i];
            if (nowMap.containsKey(subKey)) {
                nowMap = (LinkedHashMap<String, Object>) nowMap.get(subKey);
            } else {
                LinkedHashMap<String, Object> newMap = new LinkedHashMap<>();
                nowMap.put(subKey, newMap);
                nowMap = newMap;
            }
        }
        YamlConfiguration yaml = new YamlConfiguration(nowMap, this.parent);
        return yaml;
    }
}
