package org.vsu.rudakov.dao.file;

import org.vsu.rudakov.dao.Dao;
import org.vsu.rudakov.utils.FileResource;
import org.vsu.rudakov.utils.Mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class FileDao<E> implements Dao<E> {
    protected String url;
    protected Class<?> entityClass;
    private Constructor<?> noArgsConstructor;

    public FileDao(String url) {
        var genericSuperClass = getClass().getGenericSuperclass();
        var parametrizedType = (ParameterizedType) genericSuperClass;
        var typeArgument = parametrizedType.getActualTypeArguments()[0];
        entityClass = (Class<?>) typeArgument;
        var constructors = entityClass.getDeclaredConstructors();
        for (var c : constructors) {
            if (c.getParameterCount() == 0) {
                noArgsConstructor = c;
                break;
            }
        }
        if (noArgsConstructor == null) {
            throw new RuntimeException();
        }
        this.url = url + "/" + entityClass.getSimpleName().toLowerCase() + ".txt";
    }

    public E getInstance() {
        try {
            var instance = noArgsConstructor.newInstance();
            return (E) instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Field[] getFields() {
        return entityClass.getDeclaredFields();
    }

    @Override
    public List<E> getAll() {
        var fileLines = FileResource.getLines(url);
        var entitiesMaps = Mapper.getEntitiesMap(fileLines);
        List<E> entities = new ArrayList<>();
        for (var map : entitiesMaps) {
            E instance;
            try {
                instance = getInstance(map);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            entities.add(instance);
        }
        return entities;
    }

    E getInstance(Map<String, Object> map) throws IllegalAccessException {
        var instance = getInstance();
        var fields = getFields();
        int i = 0;
        for (var k : map.keySet()) {
            fields[i].setAccessible(true);
            var type = fields[i].getType();
            var mapper = new Mapper();
            var value = mapper.map(type.getName(), (String) map.get(k));
            fields[i].set(instance, value);
            i++;
        }
        return instance;
    }

    @Override
    public E get(Long id) {
        var fileLines = FileResource.getLines(url);
        var entitiesMaps = Mapper.getEntitiesMap(fileLines);
        for (var map : entitiesMaps) {
            for (var key: map.keySet()) {
                if (key.equals("id")) {
                    if (Long.parseLong((String) map.get(key)) == id) {
                        try {
                            return getInstance(map);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public E update(E entity) {
        writeEntity(entity);
        return entity;
    }

    private void writeEntity(E entity) {
        var entitiesMaps= getEntitiesMap();
        boolean isFound = false;
        for (var entityMap : entitiesMaps) {
            for (var key : entityMap.keySet()) {
                try {
                    var field = entity.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    if ("id".equals(field.getName()) && entityMap.get("id").equals(field.get(entity).toString())) {
                        isFound = true;
                    }
                    if (isFound) {
                        entityMap.put(key, field.get(entity));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            isFound = false;
        }
        writeEntitiesMap(entitiesMaps);
    }

    private List<Map<String, Object>> getEntitiesMap() {
        var fileLines = FileResource.getLines(url);
        return Mapper.getEntitiesMap(fileLines);
    }

    @Override
    public boolean delete(Long id) {
        var entitiesMaps = getEntitiesMap();
        boolean isFound = false;
        for (var entityMap : entitiesMaps) {
            for (var key : entityMap.keySet()) {
                if (key.equals("id")) {
                    var mapper = new Mapper();
                    var foundData = mapper.map(Long.class.getTypeName(), (String) entityMap.get(key));
                    //todo: почему то после 100, если не использовать equals,
                    // то не выполняется равенство равных по сути значений
                    if (foundData.equals(id)) {
                        isFound = true;
                    }
                    break;
                }
            }
            if (isFound) {
                entitiesMaps.remove(entityMap);
                break;
            }
        }
        if (isFound) {
            writeEntitiesMap(entitiesMaps);
            return true;
        }
        return false;
    }

    private void writeEntitiesMap(List<Map<String, Object>> entitiesMaps) {
        var strings = new ArrayList<String>();
        for (var entityMap : entitiesMaps) {
            StringBuilder str = new StringBuilder("{\n");
            for (var key : entityMap.keySet()) {
                str.append("\t").append(key).append(": ").append(entityMap.get(key)).append("\n");
            }
            str.append("}");
            strings.add(str.toString());
            str.setLength(0);
        }
        FileResource.writeFile(url, strings);
    }

    @Override
    public boolean create(E entity) {
        var entitiesMap = getEntitiesMap();
        var mapEntity = new LinkedHashMap<String, Object>();
        for (var field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                mapEntity.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                return false;
            }
        }
        for (var map : entitiesMap) {
            if (map.get("id").equals(mapEntity.get("id").toString())) {
                for (var key : map.keySet()) {
                    map.put(key, mapEntity.get(key));
                }
                writeEntitiesMap(entitiesMap);
                return true;
            }
        }

        entitiesMap.add(mapEntity);
        writeEntitiesMap(entitiesMap);
        return true;
    }
}
