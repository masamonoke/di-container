package org.vsu.rudakov.responce.console;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.vsu.rudakov.TestMainConsoleAppRunner;
import org.vsu.rudakov.responce.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(TestMainConsoleAppRunner.class)
public class UpdateResponseTest extends ResponseTest {
    private final Response update;
    private final Response get;

    public UpdateResponseTest() {
        super();
        update = new UpdateResponse();
        get = new GetResponse();
    }

    @Test
    public void updateChildEntity() {
        var updateRes =
                getResponse("child -update [child={id: 1, firstName: петр, lastName: пупкин, groupNumber: 2}]",
                        update);
        var getRes = getResponse("child/1 -get", get);
        assertEquals(updateRes, getRes);
    }

    @Test
    public void updateEducatorEntity() {
        var updateRes =
                getResponse("educator -update [educator={id: 1, firstName: петр, lastName: пупкин, groupNumber: 2, rating: 2.2}]",
                        update);
        var getRes = getResponse("educator/1 -get", get);
        assertEquals(updateRes, getRes);
    }
}
