package org.vsu.rudakov.responce.file;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.vsu.rudakov.MainConsoleAppTestRunner;
import org.vsu.rudakov.model.Child;
import org.vsu.rudakov.responce.Response;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MainConsoleAppTestRunner.class)
public class PostResponseTest extends ResponseTest {
    private final Response get;
    private final Response post;

    public PostResponseTest() {
        super();
        get = new GetResponse();
        post = new PostResponse();
    }

    @Test
    public void createChild() {
        var postRes =
                getResponse("child -post [child={id: 500, firstName: петр, lastName: пупкин, groupNumber: 2}]",
                post);
        assertTrue((boolean) postRes.get(0));
        var entity = (Child) getResponse("child/500 -get", get).get(0);
        assertEquals(entity.getId(), 500);
        assertEquals(entity.getFirstName(), "петр");
        assertEquals(entity.getLastName(), "пупкин");
        assertEquals(entity.getGroupNumber(), 2);
    }
}
