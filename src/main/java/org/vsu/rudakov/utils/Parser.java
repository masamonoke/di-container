package org.vsu.rudakov.utils;

import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

@AllArgsConstructor
public class Parser {
    private final Class<?> entityClass;

    public Object parse(String json) {
        if (!(json.startsWith("{") && json.endsWith("}"))) {
            return null;
        }
        var s = json.substring(1, json.length() - 1);
        var fields = s.split(",");
        var map = new LinkedHashMap<String, String>();
        for (var f : fields) {
            var params = f.split(":");
            map.put(params[0].trim(), params[1].trim());
        }
        try {
            var instance = entityClass.getConstructor().newInstance();
            var keys = map.keySet().toArray();
            var mapper = new Mapper();
            int i = 0;
            for (var field : instance.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(instance, mapper.map(field.getType().getTypeName(), map.get(keys[i])));
                i++;
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
