package org.example.FileSystem;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

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
            if (value == null)
                map.remove(key);
            else
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

    public boolean isConfigurationSection(String key) {
        if (!hasSpliter(key)) {
            Object data = this.map.get(key);
            return data != null && data instanceof HashMap;
        } else
            return moveToKey(key).isConfigurationSection(getFinalKey(key));
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

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public boolean isInteger(String key) {
        if (!hasSpliter(key))
            return map.get(key) instanceof Integer;
        else
            return moveToKey(key).isInteger(getFinalKey(key));
    }

    public Integer getInteger(String key) {
        if (!hasSpliter(key))
            return (Integer) map.get(key);
        else
            return moveToKey(key).getInteger(getFinalKey(key));
    }

    public boolean isLong(String key) {
        if (!hasSpliter(key))
            return map.get(key) instanceof Long;
        else
            return moveToKey(key).isLong(getFinalKey(key));
    }

    public Long getLong(String key) {
        if (!hasSpliter(key))
            return (Long) map.get(key);
        else
            return moveToKey(key).getLong(getFinalKey(key));
    }

    public boolean isDouble(String key) {
        if (!hasSpliter(key))
            return map.get(key) instanceof Double;
        else
            return moveToKey(key).isDouble(getFinalKey(key));
    }

    public Double getDouble(String key) {
        if (!hasSpliter(key))
            return (Double) map.get(key);
        else
            return moveToKey(key).getDouble(getFinalKey(key));
    }

    public boolean isString(String key) {
        if (!hasSpliter(key))
            return map.get(key) instanceof String;
        else
            return moveToKey(key).isString(getFinalKey(key));
    }

    public String getString(String key) {
        if (!hasSpliter(key))
            return (String) map.get(key);
        else
            return moveToKey(key).getString(getFinalKey(key));
    }

    public boolean isBoolean(String key) {
        if (!hasSpliter(key))
            return map.get(key) instanceof Boolean;
        else
            return moveToKey(key).isString(getFinalKey(key));
    }

    public Boolean getBoolean(String key) {
        if (!hasSpliter(key))
            return (Boolean) map.get(key);
        else
            return moveToKey(key).getBoolean(getFinalKey(key));
    }
}
