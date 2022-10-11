package org.vsu.rudakov.responce.file;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vsu.rudakov.MainConsoleAppTestRunner;
import org.vsu.rudakov.model.Child;
import org.vsu.rudakov.model.Educator;
import org.vsu.rudakov.responce.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MainConsoleAppTestRunner.class)
public class GetResponseTest extends ResponseTest {
    private final Response get;

    public GetResponseTest() {
        super();
        get = new GetResponse();
    }

    @Test
    public void getAllChildEntities() {
        var res = getResponse("child -get", get);
        Assert.assertEquals(res.size(), 499);
        for (var entity : res) {
            Assert.assertEquals(entity.getClass(), Child.class);
        }
    }

    @Test
    public void getAllEducatorEntities() {
        var res = getResponse("educator -get", get);
        Assert.assertEquals(res.size(), 499);
        for (var entity : res) {
            Assert.assertEquals(entity.getClass(), Educator.class);
        }
    }

    @Test
    public void getChildEntity() {
        var res = getResponse("child/50 -get", get);
        Assert.assertEquals(res.size(), 1);
        for (var entity : res) {
            assertTrue(entity instanceof Child);
            var child = (Child) entity;
            assertEquals(50, (long) child.getId());
        }
    }

    @Test
    public void getEducatorEntity() {
        var res = getResponse("educator/401 -get", get);
        Assert.assertEquals(res.size(), 1);
        for (var entity : res) {
            assertTrue(entity instanceof Educator);
            var educator = (Educator) entity;
            assertEquals(401, (long) educator.getId());
        }
    }
}