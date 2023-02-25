package org.vsu.rudakov.responce.console;

import lombok.Setter;
import org.vsu.rudakov.TestMainConsoleAppRunner;
import org.vsu.rudakov.request.ConsoleRequest;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Response;

import java.util.List;

public abstract class ResponseTest {
    @Setter
    protected List<?> controllers;
    protected final Request request;

    public ResponseTest() {
        request = new ConsoleRequest();
    }

    List<?> getResponse(String query, Response resp) {
        return TestMainConsoleAppRunner.getResponse(query, request, resp, controllers);
    }
}
