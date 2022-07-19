package com.haram.singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.haram.utils.JacksonUtil.transformJsonToMap;

public class Environment extends Properties{
    private final static String DEFAULT_PROP_JSON_FILE = "application.json";
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map propertyMap = new HashMap();

    private Environment() {
        File file = new File(getClass().getClassLoader().getResource(DEFAULT_PROP_JSON_FILE).getFile());
        try {
            this.propertyMap = objectMapper.readValue(file,Map.class);
            this.putAll(transformJsonToMap(objectMapper.readTree(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SingletonClass {
        private static final Environment instance = new Environment();
    }

    public static Environment getInstance(){
        return SingletonClass.instance;
    }

    @Override
    public Object get(Object key) {
        return propertyMap.get(key);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        this.put(key,value);
        return propertyMap.put(key, value);
    }
}
