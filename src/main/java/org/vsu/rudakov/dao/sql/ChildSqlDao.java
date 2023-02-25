package org.vsu.rudakov.dao.sql;

import lombok.extern.slf4j.Slf4j;

import org.vsu.rudakov.app.App;
import org.vsu.rudakov.dao.ChildDao;
import org.vsu.rudakov.model.Child;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChildSqlDao implements ChildDao, SqlDao {
    @Override
    public Child get(Long id) {
        try {
            var statement = getStatement();
            var rs = statement.executeQuery
                    ("select id, firstName, lastName, groupNumber from kindergarten.public.child where id=" + id);
            if (!rs.isBeforeFirst()) {
                log.info("Not found child by id = {}", id);
                return null;
            }
            rs.next();
            var child = new Child();
            child.setId(rs.getLong("id"));
            child.setFirstName(rs.getString("firstName"));
            child.setLastName(rs.getString("lastName"));
            child.setGroupNumber(rs.getInt("groupNumber"));
            statement.close();
            return child;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Child> getAll() {
        try {
            var statement = getStatement();
            var rs = statement.executeQuery
                    ("select id, firstName, lastName, groupNumber from kindergarten.public.child");
            var children = new ArrayList<Child>();
            while (rs.next()) {
                var child = new Child();
                child.setId(rs.getLong("id"));
                child.setFirstName(rs.getString("firstName"));
                child.setLastName(rs.getString("lastName"));
                child.setGroupNumber(rs.getInt("groupNumber"));
                children.add(child);
            }
            statement.close();
            return children;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Statement getStatement() throws SQLException { 
        var url = App.getAppContext().getConfiguration().getConfig().get("db.url");
        var user = App.getAppContext().getConfiguration().getConfig().get("db.username");
        var password = App.getAppContext().getConfiguration().getConfig().get("db.password");
        var conn = DriverManager.getConnection(url, user, password);
        return conn.createStatement();
    }

    @Override
    public Child update(Child entity) {
        if (!isPresent(entity)) {
            var isCreated = create(entity);
            if (isCreated) {
                return entity;
            }
            return null;
        }

        try {
            var statement = getStatement();
            var query = "update kindergarten.public.child set firstname='"
                    + entity.getFirstName() + "', lastName='" + entity.getLastName() + "', groupNumber="
                    + entity.getGroupNumber() + "  where id = 1;";
            statement.executeUpdate(query);
            statement.close();
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean delete(Long id) {
        try {
            var statement = getStatement();
            var query = "delete from kindergarten.public.child where id=" + id;
            var res = statement.executeUpdate(query);
            statement.close();
            return res == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean create(Child entity) {
        if (isPresent(entity)) {
            update(entity);
            return true;
        }

        try {
            var statement = getStatement();
            var query = "insert into kindergarten.public.child (firstName, lastName, groupNumber) values ('"
                    + entity.getFirstName() + "', '" + entity.getLastName() +"', " + entity.getGroupNumber() + ");";
            var res = statement.executeUpdate(query);
            statement.close();
            return res == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
