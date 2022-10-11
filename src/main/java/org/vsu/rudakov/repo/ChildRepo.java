package org.vsu.rudakov.repo;

import org.vsu.rudakov.annotation.Inject;
import org.vsu.rudakov.dao.ChildDao;
import org.vsu.rudakov.model.Child;

import java.util.List;

public class ChildRepo implements Repository<Child, Long> {
    @Inject
    private ChildDao dao;

    @Override
    public List<Child> getAll() {
        return dao.getAll();
    }

    @Override
    public Child getById(Long id) {
        return dao.get(id);
    }

    @Override
    public Child update(Child entity) {
        return dao.update(entity);
    }

    @Override
    public boolean delete(Long id) {
        return dao.delete(id);
    }

    @Override
    public boolean create(Child entity) {
        return dao.create(entity);
    }
}
