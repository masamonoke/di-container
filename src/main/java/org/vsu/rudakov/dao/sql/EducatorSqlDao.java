package org.vsu.rudakov.dao.sql;

import org.vsu.rudakov.dao.EducatorDao;
import org.vsu.rudakov.model.Educator;

import java.util.List;

public class EducatorSqlDao implements EducatorDao {
    @Override
    public Educator get(Long id) {
        return null;
    }

    @Override
    public List<Educator> getAll() {
        return null;
    }

    @Override
    public Educator update(Educator entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean create(Educator entity) {
        return false;
    }
}
