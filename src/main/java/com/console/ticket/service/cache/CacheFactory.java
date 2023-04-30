package com.console.ticket.service.cache;

import com.console.ticket.exception.FileException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class CacheFactory {
    private static int capacity;
    private static CacheType type;

    public Cache getCache(int capacity, CacheType type) {
        if (Objects.requireNonNull(type) == CacheType.LRU) {
            return new LruCache(capacity);
        }

        return new LfuCache(capacity);
    }

    public Cache getCacheFromYml(String resourceName) {
        readYmlFile(resourceName);

        if (Objects.requireNonNull(type) == CacheType.LRU) {
            return new LruCache(capacity);
        }

        return new LfuCache(capacity);
    }

    private void readYmlFile(String resourceName) {
        Map<String, Object> data;

        try (InputStream inputStream = CacheFactory.class.getClassLoader().getResourceAsStream(resourceName)) {
            Yaml yaml = new Yaml();
            data = yaml.load(inputStream);
        } catch (IOException e) {
            throw new FileException("Exception read " + resourceName + " file: ", e);
        }

        capacity = (int) data.get("capacity");
        type = CacheType.valueOf((String) data.get("type"));
    }
}
