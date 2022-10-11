package org.vsu.rudakov.repo;

import java.util.List;

public interface Repository<E, K> {
    List<E> getAll();
    E getById(K id);
    E update(E entity);
    boolean delete(K id);
    boolean create(E entity);
}
