package org.vsu.rudakov.utils;

import java.util.*;

    public class Mapper {
    public Object map(String type, String str) {
        if (Objects.equals(type, Long.class.getName())) {
            return Long.parseLong(str);
        }
        if (Objects.equals(type, Integer.class.getName())) {
            return Integer.parseInt(str);
        }
        if (Objects.equals(type, Float.class.getName())) {
            return Float.parseFloat(str);
        }
        return str;
    }

    public static List<Map<String, Object>> getEntitiesMap(List<String> fileLines) {
        var entitiesMaps = new ArrayList<Map<String, Object>>();
        var entityMap = new LinkedHashMap<String, Object>();
        boolean isEntity = false;
        for (var line : fileLines) {
            if (line.contains("{")) {
                isEntity = true;
                continue;
            }
            if (isEntity) {
                if (line.contains("}")) {
                    isEntity = false;
                    entitiesMaps.add(entityMap);
                    entityMap = new LinkedHashMap<>();
                    continue;
                }
                var parts = line.split(":");
                entityMap.put(parts[0].replaceAll("\\s+",""),
                        parts[1].replaceAll("\\s+",""));
            }
        }
        return entitiesMaps;
    }
}
