package org.vsu.rudakov;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.vsu.rudakov.app.App;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Response;
import org.vsu.rudakov.responce.console.ResponseTest;

import java.util.List;

public class TestMainConsoleAppRunner extends Runner {
    private final Class<?> testClass;

    public TestMainConsoleAppRunner(Class<?> testClass) {
        super();
        App.prepare();
        this.testClass = testClass;
    }

    @Override
    public Description getDescription() {
        return Description
                .createTestDescription(testClass, "My runner description");
    }

    @SneakyThrows
    @Override
    public void run(RunNotifier runNotifier) {
        var test = testClass.getConstructor().newInstance();
        if (testClass.getSuperclass() == ResponseTest.class) {
            var field = test.getClass().getSuperclass().getDeclaredField("controllers");
            field.setAccessible(true);
            field.set(test, App.getControllers());
        }
        for (var method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                runNotifier.fireTestStarted(Description.createTestDescription(testClass, method.getName()));
                method.invoke(test);
                runNotifier.fireTestFinished(Description.createTestDescription(testClass, method.getName()));
            }
        }
    }

    public static List<?> getResponse(String query, Request request, Response resp, List<?> controllers) {
        request.setInput(query);
        var res = resp.response(controllers, request);
        if (res == null) {
            return null;
        }
        return res instanceof List ? (List<?>) res : List.of(res);
    }
}
