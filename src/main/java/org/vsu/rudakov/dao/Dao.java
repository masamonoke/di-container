package org.vsu.rudakov.dao;

import java.util.List;

public interface Dao<T> {
    T get(Long id);
    List<T> getAll();
    T update(T entity);
    boolean delete(Long id);
    boolean create(T entity);

    default boolean isPresent(T entity) {
        var entities = getAll();
        for (var item : entities) {
            if (item.equals(entity)) {
                return true;
            }
        }
        return false;
    }
}
