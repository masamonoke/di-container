package org.vsu.rudakov.dao;

import java.util.List;

public interface Dao<T> {
    T get(Long id);
    List<T> getAll();
    T update(T entity);
    boolean delete(Long id);
    boolean create(T entity);
}
