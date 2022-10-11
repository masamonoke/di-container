package org.vsu.rudakov.repo;

import org.vsu.rudakov.annotation.Inject;
import org.vsu.rudakov.dao.EducatorDao;
import org.vsu.rudakov.model.Educator;

import java.util.List;

public class EducatorRepo implements Repository<Educator, Long> {
    @Inject
    private EducatorDao dao;

    @Override
    public List<Educator> getAll() {
        return dao.getAll();
    }

    @Override
    public Educator getById(Long id) {
        return dao.get(id);
    }

    @Override
    public Educator update(Educator entity) {
        return dao.update(entity);
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
