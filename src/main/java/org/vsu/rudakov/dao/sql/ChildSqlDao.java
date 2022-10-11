package org.vsu.rudakov.dao.sql;

import org.vsu.rudakov.dao.ChildDao;
import org.vsu.rudakov.model.Child;

import java.util.List;

public class ChildSqlDao implements ChildDao {
    @Override
    public Child get(Long id) {
        return null;
    }

    @Override
    public List<Child> getAll() {
        return null;
    }

    @Override
    public Child update(Child entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean create(Child entity) {
        return false;
    }
}
