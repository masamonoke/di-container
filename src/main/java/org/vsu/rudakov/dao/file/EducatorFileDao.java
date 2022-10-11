package org.vsu.rudakov.dao.file;

import org.vsu.rudakov.annotation.DataAccessObject;
import org.vsu.rudakov.dao.EducatorDao;
import org.vsu.rudakov.model.Educator;

@DataAccessObject
public class EducatorFileDao extends FileDao<Educator> implements EducatorDao {
    public EducatorFileDao(String url) {
        super(url);
    }

    @Override
    public Educator update(Educator entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Long id) {
        return super.delete(id);
    }

    @Override
    public boolean create(Educator entity) {
        return super.create(entity);
    }
}
