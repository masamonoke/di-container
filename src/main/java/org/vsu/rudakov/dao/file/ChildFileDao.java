package org.vsu.rudakov.dao.file;

import org.vsu.rudakov.annotation.DataAccessObject;
import org.vsu.rudakov.dao.ChildDao;
import org.vsu.rudakov.model.Child;

import java.util.List;

@DataAccessObject
public class ChildFileDao extends FileDao<Child> implements ChildDao {

    public ChildFileDao(String url) {
        super(url);
    }

    @Override
    public Child get(Long id) {
        return super.get(id);
    }

    @Override
    public List<Child> getAll() {
        return super.getAll();
    }

    @Override
    public Child update(Child entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Long id) {
        return super.delete(id);
    }

    @Override
    public boolean create(Child entity) {
        return super.create(entity);
    }
}
